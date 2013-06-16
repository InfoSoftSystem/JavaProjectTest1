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
import org.json.JSONArray;
import org.promefrut.simefrut.struts.catalogs.forms.ProductTypeForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class ProductTypeBean extends BaseDAO {
	protected final String TABLA = super.ESQUEMA_TABLA + "PRODUCT_TYPE_CTG";
	protected static final String PREFIJO = "ptype";
	protected final String DW_PREFIJO = "ptype";
	protected final String DW_TABLA = super.ESQUEMA_TABLA + "PRODUCT_TYPE_DIM";
	
	public ProductTypeBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find(String prodId) throws SQLException, Exception, Error {
		String sql = "select "+ PREFIJO +"_ID ptypeId,\n"
				+ " 	"+ PREFIJO +"_code ptypeCode, \n"
				+ " 	"+ PREFIJO +"_desc ptypeDesc, \n"
				+ "		"+ PREFIJO +"_status ptypeStatus,\n"
				+ "		prod.prod_id ptypeprodId,\n"
				+ "		prod.prod_code ptypeprodCode,\n"
				+ "		prod.prod_desc ptypeprodDesc,\n"
				+ "		CASE WHEN "+ PREFIJO +"_status = 'A' THEN '"
				+					 bundle.getString("registro.active") 
				+			"' ELSE '"
				+					bundle.getString("registro.inactive")
				+		"' END ptypeStatusText,\n"
				+ "		CASE WHEN ptype.audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
				+ "		coalesce(ptype.audit_user_upd, ptype.audit_user_ins) audit_user, \n" 
				+ "		coalesce(ptype.audit_date_upd, ptype.audit_date_ins) audit_date\n"
				+ " from " + TABLA+" ptype, "+super.ESQUEMA_TABLA+"PRODUCTS_CTG prod"
				+ " where prod.prod_id = ptype.prod_id \n"
				+ "  and prod.prod_id = "+prodId;

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		//String resultJSON = "Ext.data.JsonP.callback1("+data+")";
		return data;
	}

	public int insert(ProductTypeForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
					+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer ptypeId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into "+ TABLA + "\n"
					+ "("+ PREFIJO +"_ID,\n"
					+ PREFIJO +"_CODE,\n"
					+ PREFIJO +"_DESC,\n"
					+ PREFIJO + "_STATUS, \n"
					+ "    prod_id,\n"
					+ "    audit_user_ins, \n"
					+ "    audit_date_ins \n"
					+ ")\n"
					+ "values (?,?,?,?,?,?,now()"
					+ ")";

		Object[] param = new Object[6];
		int i = 0;
		param[i++] = ptypeId;
		param[i++] = form.getPtypeCode().trim();
		param[i++] = form.getPtypeDesc().trim();
	    param[i++] = form.getPtypeStatus();
	    param[i++] = new Integer(form.getProdId());
	    param[i++] = username;
	    
	    form.setPtypeId(String.valueOf(ptypeId));

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	public int update(ProductTypeForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ " set "+ PREFIJO +"_CODE = ?, \n"
					+ "    "+ PREFIJO +"_DESC= ?, \n"
					+ "    "+ PREFIJO +"_STATUS= ?, \n"
					+ "    audit_user_upd = ?, \n"
					+ "    audit_date_upd = current_date+current_time \n"
					+ " where "+ PREFIJO +"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[5];
		int i = 0;
		param[i++] = form.getPtypeCode().trim();
		param[i++] = form.getPtypeDesc().trim();
	    param[i++] = form.getPtypeStatus().trim();
	    param[i++] = username;
	    param[i++] = new Integer(form.getPtypeId());

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	@Deprecated
	public int delete(ProductTypeForm form) throws SQLException, Exception, Error {
		/*String sql = "DELETE FROM " + TABLA
					+ " WHERE "+ PREFIJO +"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		param[0] = form.getCtrId();
		*/
		return 0;//query.update(sessionManager.getConnection(), sql, param);
	}
	
	/**
	 * Function for Data Warehouse updating. This function changes the expiry_date and inserts new registers
	 * @param {org.promefrut.simefrut.struts.catalogs.forms.ProductTypeForm}form 
	 * @param {String} username
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	private void updateDW(ProductTypeForm form, String username) throws SQLException, Exception, Error {
		updateProductTypeDim(form,username);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateProductTypeDim(ProductTypeForm form, String username) throws SQLException, Exception, Error {
		
		String sql = " update " + DW_TABLA +" \n"
				+ " set EXPIRY_DATE = current_date-1, \n"
				+ "    AUDIT_USER_UPD = ?, \n"
				+ "    AUDIT_DATE_UPD = current_date + current_time \n"
				+ " where prod_id = ? \n"
				+ " AND ptype_id is null \n"
				+ " AND current_date between effective_date and expiry_date \n";
		
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[2];
		
		int i = 0;
		param[i++] = username;
		param[i++] = new Integer(form.getProdId());
		
		query.update(sessionManager.getConnection(), sql, param);
		
		sql = "select PTYPE_SK \n"
					+ " from " + DW_TABLA +" \n"
					+ " where "+DW_PREFIJO+"_ID = ?"
					+ " AND current_date between effective_date and expiry_date ";
		
		MapListHandler handler = new MapListHandler();
		param = new Object[1];
		i = 0;
		
		param[i++] = new Integer(form.getPtypeId());
	    
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param,handler);
		
		if(resultList.size()>0){
			for(Iterator it = resultList.iterator(); it.hasNext(); ){
				Map reg = (Map)it.next();
				
				BigDecimal fieldSk = (BigDecimal)reg.get("PTYPE_SK");
				
				sql = " update " + DW_TABLA +" \n"
						+ " set EXPIRY_DATE = current_date-1, \n"
						+ "    AUDIT_USER_UPD = ?, \n"
						+ "    AUDIT_DATE_UPD = current_date + current_time \n"
						+ " where PTYPE_SK = ? ";
				
				param = new Object[2];
				i = 0;
				param[i++] = username;
				param[i++] = fieldSk;
				
				query.update(sessionManager.getConnection(), sql, param);
			}
		}
		
		if("A".equals(form.getPtypeStatus())){
			sql = " insert into "+DW_TABLA+"(\n"
				+ "PTYPE_SK, \n"
				+ DW_PREFIJO+"_ID,\n" 
				+ DW_PREFIJO+"_CODE,\n"
				+ DW_PREFIJO+"_DESC,\n"
				+ " PROD_ID,\n"
				+ " PROD_CODE,\n"
				+ " PROD_DESC,\n"
				+ " EFFECTIVE_DATE,\n"
				+ " EXPIRY_DATE, \n"
				+ " AUDIT_USER_INS, \n"
				+ " AUDIT_DATE_INS \n"
				+ ")\n"
				+ "values ((select coalesce(max(PTYPE_SK) + 1,1)\n"
				+ "         from "+DW_TABLA+"),\n"
				+ " ?,?,?,?,?,?,CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
				+ ")";
	
			param = new Object[7];
			i = 0;
			param[i++] = new Integer(form.getPtypeId());
			param[i++] = form.getPtypeCode().trim();
			param[i++] = form.getPtypeDesc().trim();
			param[i++] = new Integer(form.getProdId());
			param[i++] = form.getProdCode().trim();
			param[i++] = form.getProdDesc().trim();
		    param[i++] = username;
	
			query.update(sessionManager.getConnection(), sql, param);
		}
	}
}
