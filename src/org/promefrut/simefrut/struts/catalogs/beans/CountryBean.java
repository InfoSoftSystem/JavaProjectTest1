package org.promefrut.simefrut.struts.catalogs.beans;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.catalogs.forms.CountryForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class CountryBean extends BaseDAO {
	public final String TABLA = super.ESQUEMA_TABLA + "COUNTRIES_CTG";
	protected static final String PREFIJO = "ctr";
	protected final String DW_PREFIJO = "ctr";
	protected final String DW_TABLA = super.ESQUEMA_TABLA + "ORIGIN_DIM";

	public CountryBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find() throws SQLException, Exception, Error {
		String sql = "select "+ PREFIJO +"_ID ctrId,\n"
					+ " 	"+ PREFIJO +"_ISO3 ctrISO3, \n"
					+ " 	"+ PREFIJO +"_desc ctrDescSpanish, \n"
					+ " 	"+ PREFIJO +"_desc_eng ctrDescEnglish,\n"
					+ "		"+ PREFIJO +"_status ctrStatus,\n"
					+ "		CASE WHEN "+ PREFIJO +"_status = 'A' THEN '"
					+					 bundle.getString("registro.active") 
					+			"' ELSE '"
					+					bundle.getString("registro.inactive")
					+		"' END ctrStatusText,\n"
					+ "		CASE WHEN audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
					+ "		coalesce(audit_user_upd, audit_user_ins) audit_user, \n" 
					+ "		coalesce(audit_date_upd, audit_date_ins) audit_date\n"
					+ " from " + TABLA;

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		//String resultJSON = "Ext.data.JsonP.callback1("+data+")";
		return data;
	}

	public int insert(CountryForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
					+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer ctrId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into "+ TABLA + "\n"
					+ "("+ PREFIJO +"_ID,\n"
					+ ""+ PREFIJO +"_ISO3,\n"
					+ ""+ PREFIJO +"_DESC,\n"
					+ ""+ PREFIJO +"_DESC_ENG,\n"
					+ PREFIJO + "_STATUS, \n"
					+ "    audit_user_ins, \n"
					+ "    audit_date_ins \n"
					+ ")\n"
					+ "values (?,\n"
					+ " ?,?,?,?,?, now()"
					+ ")";

		Object[] param = new Object[6];
		int i = 0;
		param[i++] = ctrId;
		param[i++] = form.getCtrISO3().trim();
	    param[i++] = form.getCtrDescSpanish().trim();
	    param[i++] = form.getCtrDescEnglish();
	    param[i++] = form.getCtrStatus();
	    param[i++] = username;
	    form.setCtrId(String.valueOf(ctrId));

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	public int update(CountryForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ " set "+ PREFIJO +"_ISO3 = ?, \n"
					+ "    "+ PREFIJO +"_DESC = ?, \n"
					+ "    "+ PREFIJO +"_DESC_ENG= ?, \n"
					+ "    "+ PREFIJO +"_STATUS= ?, \n"
					+ "    audit_user_upd = ?, \n"
					+ "    audit_date_upd = current_date+current_time \n"
					+ " where "+ PREFIJO +"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[6];
		int i = 0;
		param[i++] = form.getCtrISO3().trim();
	    param[i++] = form.getCtrDescSpanish().trim();
	    param[i++] = form.getCtrDescEnglish().trim();
	    param[i++] = form.getCtrStatus().trim();
	    param[i++] = username;
	    param[i++] = new Integer(form.getCtrId());

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	@Deprecated
	public int delete(CountryForm form) throws SQLException, Exception, Error {
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
	 * @param {org.promefrut.simefrut.struts.catalogs.forms.CountryForm}form 
	 * @param {String} username
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	@SuppressWarnings({ "rawtypes" })
	private void updateDW(CountryForm form, String username) throws SQLException, Exception, Error {
		Map ctrSkHashMap = updateCoutryDimDW(form, username);
		updateOriginDimDW(form, username);
		updateCountryCOMTRADEEquiv(form, username, ctrSkHashMap);
		updateCountryFAOSTATEquiv(form, username, ctrSkHashMap);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map updateCoutryDimDW(CountryForm form, String username) throws SQLException, Exception, Error {
		Map result = new HashMap();
		
		String sql = "select ctr_sk \n"
					+ " from " + ESQUEMA +".COUNTRY_DIM \n"
					+ " where CTR_ID = ?"
					+ " AND current_date between effective_date and expiry_date ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		int i = 0;
		param[i++] = new Integer(form.getCtrId());
	    
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param,handler);
		
		if(resultList.size()>0){
			for(Iterator it = resultList.iterator(); it.hasNext(); ){
				Map reg = (Map)it.next();
				
				BigDecimal ctrSk = (BigDecimal)reg.get("ctr_sk");
				
				sql = " update " + ESQUEMA +".COUNTRY_DIM \n"
						+ " set EXPIRY_DATE = current_date-1, \n"
						+ "    AUDIT_USER_UPD = ?, \n"
						+ "    AUDIT_DATE_UPD = current_date + current_time \n"
						+ " where CTR_SK = ? ";
				
				param = new Object[2];
				i = 0;
				param[i++] = username;
				param[i++] = ctrSk;
				result.put("oldCtrSk", ctrSk);
				
				query.update(sessionManager.getConnection(), sql, param);
			}
		}
		
		if("A".equals(form.getCtrStatus())){
			sql = "select coalesce(max(CTR_SK) + 1,1)\n"
				+ "         from COUNTRY_DIM";
			BigDecimal newCtrSk = (BigDecimal) query.query(sessionManager.getConnection(), sql, new ScalarHandler());
			result.put("newCtrSk", newCtrSk);
			
			sql = " insert into COUNTRY_DIM(\n"
				+ " CTR_SK, \n"
				+ " CTR_ID,\n"
				+ " CTR_ISO3, \n" 
				+ " CTR_DESC,\n"
				+ " CTR_DESC_ENG,\n"
				+ " EFFECTIVE_DATE, \n"
				+ " EXPIRY_DATE, \n"
				+ " AUDIT_USER_INS, \n"
				+ " AUDIT_DATE_INS \n"
				+ ")\n"
				+ "values (?,\n"
				+ " ?,?,?,?,CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
				+ ")";
	
			param = new Object[6];
			i = 0;
			param[i++] = newCtrSk;
			param[i++] = new Integer(form.getCtrId());
			param[i++] = form.getCtrISO3().trim();
		    param[i++] = form.getCtrDescSpanish().trim();
		    param[i++] = form.getCtrDescEnglish();
		    param[i++] = username;
	
			query.update(sessionManager.getConnection(), sql, param);
		}
		
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateOriginDimDW(CountryForm form, String username) throws SQLException, Exception, Error {
		
		
		String sql = "select ori_sk \n"
					+ " from " + ESQUEMA +".ORIGIN_DIM \n"
					+ " where CTR_ID = ?"
					+ " AND current_date between effective_date and expiry_date ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		
		int i = 0;
		param[i++] = new Integer(form.getCtrId());
	    
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param,handler);
		
		if(resultList.size()>0){
			for(Iterator it = resultList.iterator(); it.hasNext(); ){
				Map reg = (Map)it.next();
				
				BigDecimal oriSk = (BigDecimal)reg.get("ori_sk");
				
				sql = " update " + ESQUEMA +".ORIGIN_DIM \n"
						+ " set EXPIRY_DATE = current_date-1, \n"
						+ "    AUDIT_USER_UPD = ?, \n"
						+ "    AUDIT_DATE_UPD = current_date + current_time \n"
						+ " where ori_SK = ? ";
				
				param = new Object[2];
				i = 0;
				param[i++] = username;
				param[i++] = oriSk;
				
				query.update(sessionManager.getConnection(), sql, param);
				
				if("A".equals(form.getCtrStatus())){
					sql = " insert into " + DW_TABLA +"(\n"
						+ "ORI_SK, \n"
						+ "CTR_ID,\n" 
						+ "ORI_COUNTRY,\n"
						+ "ORI_COUNTRY_ENG,\n"
						+ "REG_ID,\n" 
						+ "REG_DESC,\n"
						+ "PROV_ID,\n" 
						+ "PROV_DESC,\n"
						+ " EFFECTIVE_DATE,\n"
						+ " EXPIRY_DATE, \n"
						+ " AUDIT_USER_INS, \n"
						+ " AUDIT_DATE_INS \n"
						+ ")\n"
						+ " SELECT row_number() over(order by ORI_SK) + (select coalesce(max(ORI_SK),1)\n"
						+ "         from "+DW_TABLA+") ORI_SK,\n"
						+ "CTR_ID,\n" 
						+ "?,\n"
						+ "?,\n"
						+ "REG_ID,\n"
						+ "REG_DESC,\n"
						+ "PROV_ID,\n" 
						+ "PROV_DESC,\n"
						+ "CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() "
						+ "FROM "+DW_TABLA+"\n"
						+ " where ORI_SK = ? \n";
			
					param = new Object[4];
					i = 0;
					param[i++] = form.getCtrDescSpanish().trim();
					param[i++] = form.getCtrDescEnglish().trim();
				    param[i++] = username;
				    param[i++] = oriSk;
			
					query.update(sessionManager.getConnection(), sql, param);
				}
			}
			
		}else{// If there's no previous data, a new record must be created
			sql = " insert into ORIGIN_DIM(\n"
					+ " ORI_SK, \n"
					+ " CTR_ID,\n" 
					+ " ORI_COUNTRY,\n"
					+ " ORI_COUNTRY_ENG,\n"
					+ " EFFECTIVE_DATE, \n"
					+ " EXPIRY_DATE, \n"
					+ " AUDIT_USER_INS, \n"
					+ " AUDIT_DATE_INS \n"
					+ ")\n"
					+ "values ((select coalesce(max(ORI_SK) + 1,1)\n"
					+ "         from ORIGIN_DIM),\n"
					+ " ?,?,?,CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() "
					+ ")";
		
				param = new Object[4];
				i = 0;
				
				param[i++] = new Integer(form.getCtrId());
			    param[i++] = form.getCtrDescSpanish().trim();
			    param[i++] = form.getCtrDescEnglish().trim();
			    param[i++] = username;
	
			query.update(sessionManager.getConnection(), sql, param);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateCountryCOMTRADEEquiv(CountryForm form, String username, Map ctrSkHM) throws SQLException, Exception, Error {
		String sql = new String();
		Object[] param = null;
		int i = 0;
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		
		if("A".equals(form.getCtrStatus())){
			sql = " insert into "+ESQUEMA+".country_comtrade_equiv(\n"
				+ " CTRADE_SK, \n"
				+ " CTR_SK, \n"
				+ " CTRADE_ID, \n" 
				+ " CTRADE_DESC,\n"
				+ " EFFECTIVE_DATE, \n"
				+ " EXPIRY_DATE, \n"
				+ " AUDIT_USER_INS, \n"
				+ " AUDIT_DATE_INS \n"
				+ ")\n"
				+ " SELECT row_number() over(order by CTRADE_SK) + (select coalesce(max(CTRADE_SK),1)\n"
				+ "         from "+ESQUEMA+".country_comtrade_equiv ) CTRADE_SK,\n"
				+ "?,\n" 
				+ "CTRADE_ID,\n"
				+ "CTRADE_DESC,\n"
				+ "CURRENT_DATE,"
				+ "to_date('31-12-9999','dd/mm/yyyy'),?, now() "
				+ "FROM "+ESQUEMA+".country_comtrade_equiv \n"
				+ " where CTR_SK = ? \n"
				+ " AND current_date between effective_date and expiry_date ";
	
			param = new Object[3];
			i = 0;
			param[i++] = (BigDecimal) ctrSkHM.get("newCtrSk");
			param[i++] = username;
			param[i++] = (BigDecimal) ctrSkHM.get("oldCtrSk");
	
			query.update(sessionManager.getConnection(), sql, param);
		}
		
		sql = "select ctrade_sk \n"
			+ " from " + ESQUEMA +".country_comtrade_equiv \n"
			+ " where ctr_sk = ?"
			+ " AND current_date between effective_date and expiry_date ";
		
		param = new Object[1];
		i = 0;
		param[i++] = (BigDecimal) ctrSkHM.get("oldCtrSk");
	    
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param,handler);
		
		if(resultList.size()>0){
			for(Iterator it = resultList.iterator(); it.hasNext(); ){
				Map reg = (Map)it.next();
				
				BigDecimal ctradeSk = (BigDecimal)reg.get("ctrade_sk");
				
				sql = " update " + ESQUEMA +".country_comtrade_equiv \n"
						+ " set EXPIRY_DATE = current_date-1, \n"
						+ "    AUDIT_USER_UPD = ?, \n"
						+ "    AUDIT_DATE_UPD = now() \n"
						+ " where ctrade_sk = ? ";
				
				param = new Object[2];
				i = 0;
				param[i++] = username;
				param[i++] = ctradeSk;
				
				query.update(sessionManager.getConnection(), sql, param);
			}
		}
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateCountryFAOSTATEquiv(CountryForm form, String username, Map ctrSkHM) throws SQLException, Exception, Error {
		String sql = new String();
		Object[] param = null;
		int i = 0;
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		
		
		if("A".equals(form.getCtrStatus())){
			sql = " insert into "+ESQUEMA+".country_fao_equiv(\n"
				+ " faoctr_sk, \n"
				+ " CTR_SK, \n"
				+ " faoctr_ID, \n" 
				+ " faoctr_DESC,\n"
				+ " EFFECTIVE_DATE, \n"
				+ " EXPIRY_DATE, \n"
				+ " AUDIT_USER_INS, \n"
				+ " AUDIT_DATE_INS \n"
				+ ")\n"
				+ " SELECT row_number() over(order by faoctr_sk) + (select coalesce(max(faoctr_sk),1)\n"
				+ "         from "+ESQUEMA+".country_fao_equiv ) faoctr_sk,\n"
				+ "?,\n" 
				+ "faoctr_ID,\n"
				+ "faoctr_DESC,\n"
				+ "CURRENT_DATE,"
				+ "to_date('31-12-9999','dd/mm/yyyy'),?, now() "
				+ "FROM "+ESQUEMA+".country_fao_equiv \n"
				+ " where CTR_SK = ? \n"
				+ " AND current_date between effective_date and expiry_date ";
	
			param = new Object[3];
			i = 0;
			param[i++] = (BigDecimal) ctrSkHM.get("newCtrSk");
			param[i++] = username;
			param[i++] = (BigDecimal) ctrSkHM.get("oldCtrSk");
	
			query.update(sessionManager.getConnection(), sql, param);
		}
		
		sql = "select faoctr_sk \n"
			+ " from " + ESQUEMA +".country_fao_equiv \n"
			+ " where ctr_sk = ?"
			+ " AND current_date between effective_date and expiry_date ";
		
		param = new Object[1];
		i = 0;
		param[i++] = (BigDecimal) ctrSkHM.get("oldCtrSk");
	    
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param,handler);
		
		if(resultList.size()>0){
			for(Iterator it = resultList.iterator(); it.hasNext(); ){
				Map reg = (Map)it.next();
				
				BigDecimal faoctrSk = (BigDecimal)reg.get("faoctr_sk");
				
				sql = " update " + ESQUEMA +".country_fao_equiv \n"
						+ " set EXPIRY_DATE = current_date-1, \n"
						+ "    AUDIT_USER_UPD = ?, \n"
						+ "    AUDIT_DATE_UPD = now() \n"
						+ " where faoctr_sk = ? ";
				
				param = new Object[2];
				i = 0;
				param[i++] = username;
				param[i++] = faoctrSk;
				
				query.update(sessionManager.getConnection(), sql, param);
			}
		}
		
	}
}
