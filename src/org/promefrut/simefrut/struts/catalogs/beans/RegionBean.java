package org.promefrut.simefrut.struts.catalogs.beans;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.catalogs.forms.RegionForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class RegionBean extends BaseDAO {
	protected final String TABLA = super.ESQUEMA_TABLA + "REGION_CTG";
	protected static final String PREFIJO = "reg";
	protected final String DW_PREFIJO = "reg";
	protected final String DW_TABLA = super.ESQUEMA_TABLA + "ORIGIN_DIM";
	
	public RegionBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find(String ctrId) throws SQLException, Exception, Error {
		String sql = "select "+ PREFIJO +"_ID regId,\n"
				+ " 	"+ PREFIJO +"_desc regDesc, \n"
				+ "		"+ PREFIJO +"_status regStatus,\n"
				+ "		country.ctr_id regCtrId,\n"
				+ "		country.ctr_ISO3 regctrISO3,\n"
				+ "		country.ctr_desc regctrDesc,\n"
				+ "		country.ctr_desc_eng regctrDescEng,\n"
				+ "		CASE WHEN "+ PREFIJO +"_status = 'A' THEN '"
				+					 bundle.getString("registro.active") 
				+			"' ELSE '"
				+					bundle.getString("registro.inactive")
				+		"' END regStatusText,\n"
				+ "		CASE WHEN reg.audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
				+ "		coalesce(reg.audit_user_upd, reg.audit_user_ins) audit_user, \n" 
				+ "		coalesce(reg.audit_date_upd, reg.audit_date_ins) audit_date\n"
				+ " from " + TABLA+" reg, "+super.ESQUEMA_TABLA+"COUNTRIES_CTG country"
				+ " where country.ctr_id = reg.ctr_id \n"
				+ "  and country.ctr_id = "+ctrId;

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		return data;
	}

	public int insert(RegionForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
					+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer regId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into "+ TABLA + "\n"
					+ "("+ PREFIJO +"_ID,\n"
					+ PREFIJO +"_DESC,\n"
					+ PREFIJO + "_STATUS, \n"
					+ "    ctr_id,\n"
					+ "    audit_user_ins, \n"
					+ "    audit_date_ins \n"
					+ ")\n"
					+ "values (?,?,?,?,?,now()"
					+ ")";

		Object[] param = new Object[5];
		int i = 0;
		param[i++] = regId;
		param[i++] = form.getRegDesc().trim();
	    param[i++] = form.getRegStatus();
	    param[i++] = new Integer(form.getCtrId());
	    param[i++] = username;
	    
	    form.setRegId(String.valueOf(regId));

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	public int update(RegionForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ " set "+ PREFIJO +"_DESC= ?, \n"
					+ "    "+ PREFIJO +"_STATUS= ?, \n"
					+ "    audit_user_upd = ?, \n"
					+ "    audit_date_upd = current_date+current_time \n"
					+ " where "+ PREFIJO +"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[4];
		int i = 0;
		param[i++] = form.getRegDesc().trim();
	    param[i++] = form.getRegStatus().trim();
	    param[i++] = username;
	    param[i++] = new Integer(form.getRegId());

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	@Deprecated
	public int delete(RegionForm form) throws SQLException, Exception, Error {
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
	 * @param {org.promefrut.simefrut.struts.catalogs.forms.RegionForm}form 
	 * @param {String} username
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	private void updateDW(RegionForm form, String username) throws SQLException, Exception, Error {
		updateOriginDim(form,username);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateOriginDim(RegionForm form, String username) throws SQLException, Exception, Error {
		QueryRunner query = new QueryRunner();
		Object[] param = null;
		int i = 0;
		
		String sql = "select ORI_SK \n"
			+ " from " + DW_TABLA +" \n"
			+ " where "+DW_PREFIJO+"_ID = ?"
			+ " AND current_date between effective_date and expiry_date ";
		
		MapListHandler handler = new MapListHandler();
		param = new Object[1];
		i = 0;
		param[i++] = new Integer(form.getRegId());
	    
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param,handler);
		
		if(resultList.size()>0){
			for(Iterator it = resultList.iterator(); it.hasNext(); ){
				Map reg = (Map)it.next();
				
				BigDecimal fieldSk = (BigDecimal)reg.get("ORI_SK");
				
				sql = " update " + DW_TABLA +" \n"
						+ " set EXPIRY_DATE = current_date-1, \n"
						+ "    AUDIT_USER_UPD = ?, \n"
						+ "    AUDIT_DATE_UPD = current_date + current_time \n"
						+ " where ORI_SK = ? ";
				
				param = new Object[2];
				i = 0;
				param[i++] = username;
				param[i++] = fieldSk;
				
				query.update(sessionManager.getConnection(), sql, param);
				
				if("A".equals(form.getRegStatus())){
					sql = " insert into "+DW_TABLA+"(\n"
						+ "ORI_SK, \n"
						+ "CTR_ID,\n" 
						+ "ORI_COUNTRY,\n"
						+ "ORI_COUNTRY_ENG,\n"
						+ DW_PREFIJO+"_ID,\n" 
						+ DW_PREFIJO+"_DESC,\n"
						+ "PROV_ID,\n" 
						+ "PROV_DESC,\n"
						+ " EFFECTIVE_DATE, \n"
						+ " EXPIRY_DATE, \n"
						+ " AUDIT_USER_INS, \n"
						+ " AUDIT_DATE_INS \n"
						+ ")\n"
						+ " SELECT row_number() over(order by ORI_SK) + (select coalesce(max(ORI_SK),1)\n"
						+ "         from "+DW_TABLA+") ORI_SK,\n"
						+ "CTR_ID,\n" 
						+ "ORI_COUNTRY,\n"
						+ "ORI_COUNTRY_ENG,\n"
						+ "REG_ID,\n"
						+ "?,\n" 
						+ "PROV_ID,\n" 
						+ "PROV_DESC,\n"
						+ "CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() "
						+ "FROM "+DW_TABLA+"\n"
						+ " where ORI_SK = ? \n";
			
					param = new Object[3];
					i = 0;
					param[i++] = form.getRegDesc().trim();
				    param[i++] = username;
				    param[i++] = fieldSk;
			
					query.update(sessionManager.getConnection(), sql, param);
				}
			}
		}else{
		
			if("A".equals(form.getRegStatus())){
				sql = "select ori_country, ori_country_eng \n"
					+ " from " + DW_TABLA +" \n"
					+ " where CTR_ID = ? "
					+ " and "+DW_PREFIJO+"_ID is null "
					+ " AND current_date between effective_date and expiry_date";
				
				param = new Object[1];
				i = 0;
				param[i++] = new Integer(form.getCtrId());
			    
				Map result = (Map)query.query(sessionManager.getConnection(), sql, param,new MapHandler());
				
				sql = " insert into "+DW_TABLA+"(\n"
					+ "ORI_SK, \n"
					+ DW_PREFIJO+"_ID,\n" 
					+ DW_PREFIJO+"_DESC,\n"
					+ " CTR_ID,\n"
					+ " ORI_COUNTRY,\n"
					+ " ORI_COUNTRY_ENG,\n"
					+ " EFFECTIVE_DATE,\n"
					+ " EXPIRY_DATE, \n"
					+ " AUDIT_USER_INS, \n"
					+ " AUDIT_DATE_INS \n"
					+ ")\n"
					+ "values ((select coalesce(max(ORI_SK) + 1,1)\n"
					+ "         from "+DW_TABLA+"),\n"
					+ " ?,?,?,?,?,CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
					+ ")";
		
				param = new Object[6];
				i = 0;
				param[i++] = new Integer(form.getRegId());
				param[i++] = form.getRegDesc().trim();
				param[i++] = new Integer(form.getCtrId());
				param[i++] = (String)result.get("ori_country");
				param[i++] = (String)result.get("ori_country_eng");
			    param[i++] = username;
		
				query.update(sessionManager.getConnection(), sql, param);
			}
		}
	}
}
