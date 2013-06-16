package org.promefrut.simefrut.struts.catalogs.beans;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.catalogs.forms.ProductGroupFAOSTATForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class ProductGroupFAOSTATBean extends BaseDAO {
	protected final String TABLA = super.ESQUEMA_TABLA + "GROUP_PRODUCT_FAO_DIM";
	protected static final String PREFIJO = "grp";
	
	public ProductGroupFAOSTATBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find() throws SQLException, Exception, Error {
		String sql = "select "+ PREFIJO +"_ID grpId,\n"
				+ " 	"+ PREFIJO +"_desc grpDesc, \n"
				+ "		CASE WHEN audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
				+ "		coalesce(audit_user_upd, audit_user_ins) audit_user, \n" 
				+ "		coalesce(audit_date_upd, audit_date_ins) audit_date\n"
				+ " from " + TABLA
				+ " where current_date between effective_date and expiry_date";

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		//String resultJSON = "Ext.data.JsonP.callback1("+data+")";
		return data;
	}

	/**
	 * Function for Data Warehouse updating. This function changes the expiry_date and inserts new registers
	 * @param {org.promefrut.simefrut.struts.catalogs.forms.ProductGroupFAOSTATForm}form 
	 * @param {String} username
	 * @param {String} accion
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateDW(ProductGroupFAOSTATForm form, String username, String accion) throws SQLException, Exception, Error {
		String sql = null;
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[5];
		int i = 0;
		
		String oldRegistryId = new String();
		String newRegistryId = new String();
		
		if(!StringUtils.isBlank(form.getGrpId())){
			oldRegistryId = form.getGrpId();
			BigDecimal fieldSk = new BigDecimal(form.getGrpId());
			
			sql = " update " + TABLA +" \n"
					+ " set EXPIRY_DATE = current_date-1, \n"
					+ "    AUDIT_USER_UPD = ?, \n"
					+ "    AUDIT_DATE_UPD = now()\n"
					+ " where "+PREFIJO+"_ID = ? ";
			
			param = new Object[2];
			i = 0;
			param[i++] = username;
			param[i++] = fieldSk;
			
			query.update(sessionManager.getConnection(), sql, param);
		}
		
		if("A".equals(accion)){
			
			
			sql = "select coalesce(max("+PREFIJO+"_ID) + 1,1)\n"
				+ "         from "+TABLA;
			
			BigDecimal newGrpId = (BigDecimal) query.query(sessionManager.getConnection(), sql, new ScalarHandler());
			
			newRegistryId = String.valueOf(newGrpId);
			
			sql = " insert into "+TABLA+"(\n"
				+ PREFIJO+"_ID,\n" 
				+ PREFIJO+"_DESC,\n"
				+ " EFFECTIVE_DATE,\n"
				+ " EXPIRY_DATE, \n"
				+ " AUDIT_USER_INS, \n"
				+ " AUDIT_DATE_INS \n"
				+ ")\n"
				+ "values (?,\n"
				+ " ?,CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
				+ ")";
	
			param = new Object[3];
			i = 0;
			param[i++] = newGrpId;
			param[i++] = form.getGrpDesc().trim();
		    param[i++] = username;
	
			query.update(sessionManager.getConnection(), sql, param);
		}
		
		if(!StringUtils.isBlank(newRegistryId) && !StringUtils.isBlank(oldRegistryId)){

			sql = " insert into "+ESQUEMA+".product_faostat_dim (\n"
				+ " faoprod_sk, \n"
				+ " PROD_SK,\n" 
				+ " GRP_ID,\n"
				+ " GRP_DESC,\n"
				+ " PROD_CODE,\n"
				+ " PROD_DESC,\n"
				+ " PROD_DESC_ENG,\n"
				+ " EFFECTIVE_DATE,\n"
				+ " EXPIRY_DATE, \n"
				+ " AUDIT_USER_INS, \n"
				+ " AUDIT_DATE_INS \n"
				+ ")\n"
				+ "select row_number() over(order by faoprod_sk) + (select coalesce(max(faoprod_sk) + 1,1)\n"
				+ "         from "+ESQUEMA+".product_faostat_dim),\n"
				+ " PROD_SK, "
				+ " ?,?,"
				+ " PROD_CODE,\n"
				+ " PROD_DESC,\n"
				+ " PROD_DESC_ENG,\n"
				+ " CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() "
				+ "FROM "+ESQUEMA+".product_faostat_dim \n"
				+ " where grp_id = ? \n"
				+ " AND current_date between effective_date and expiry_date ";
	
			param = new Object[4];
			i = 0;
			param[i++] = new BigDecimal(newRegistryId);
			param[i++] = form.getGrpDesc().trim();
		    param[i++] = username;
		    param[i++] = new BigDecimal(oldRegistryId);
	
			query.update(sessionManager.getConnection(), sql, param);
			
			
			sql = "select faoprod_sk \n"
				+ " from "+ESQUEMA+".product_faostat_dim \n"
				+ " where grp_ID = ?"
				+ " AND current_date between effective_date and expiry_date ";
			
			MapListHandler handler = new MapListHandler();
			param = new Object[1];
			i = 0;
			param[i++] = new BigDecimal(oldRegistryId);
		    
			List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param,handler);
			
			if(resultList.size()>0){
				for(Iterator it = resultList.iterator(); it.hasNext(); ){
					Map reg = (Map)it.next();
					
					BigDecimal fieldSk = (BigDecimal)reg.get("faoprod_sk");
					
					sql = " update "+ESQUEMA+".product_faostat_dim \n"
							+ " set EXPIRY_DATE = current_date-1, \n"
							+ "    AUDIT_USER_UPD = ?, \n"
							+ "    AUDIT_DATE_UPD = now() \n"
							+ " where faoprod_sk = ? ";
					
					param = new Object[2];
					i = 0;
					param[i++] = username;
					param[i++] = fieldSk;
					
					query.update(sessionManager.getConnection(), sql, param);
				}
			}
		}
	}
	
	public String existsData(ProductGroupFAOSTATForm form)throws Exception, Error{
		QueryRunner query = new QueryRunner();
		
		String sql = "select "+PREFIJO+"_DESC " +
				" from " + TABLA +" \n" +
				" where trim(lower("+ PREFIJO +"_desc)) = '"+ form.getGrpDesc().trim().toLowerCase() +"'"+
				"   AND current_date between effective_date and expiry_date";
		
		if(!StringUtils.isBlank(form.getGrpId())){
			sql+="	AND "+ PREFIJO +"_id <> " + form.getGrpId();
		}
		
		String reg = (String)query.query(sessionManager.getConnection(), sql, new ScalarHandler());
		
		return reg;
	}
}
