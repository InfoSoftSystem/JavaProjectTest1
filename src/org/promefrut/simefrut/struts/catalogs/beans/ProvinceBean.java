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
import org.promefrut.simefrut.struts.catalogs.forms.ProvinceForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class ProvinceBean extends BaseDAO {
	protected final String TABLA = super.ESQUEMA_TABLA + "PROVINCE_CTG";
	protected static final String PREFIJO = "prov";
	protected final String DW_PREFIJO = "prov";
	protected final String DW_TABLA = super.ESQUEMA_TABLA + "ORIGIN_DIM";
	
	public ProvinceBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find(String ctrId, String regId) throws SQLException, Exception, Error {
		String sql = "select "+ PREFIJO +"_ID provId,\n"
				+ " 	"+ PREFIJO +"_desc provDesc, \n"
				+ "		"+ PREFIJO +"_status provStatus,\n"
				+ "		country.ctr_id provctrId,\n"
				+ "		country.ctr_ISO3 provctrISO3,\n"
				+ "		country.ctr_desc provctrDesc,\n"
				+ "		country.ctr_desc_eng provctrDescEng,\n"
				+ "		reg.reg_id provregId,\n"
				+ "		reg.reg_desc provregDesc,\n"
				+ "		CASE WHEN "+ PREFIJO +"_status = 'A' THEN '"
				+					 bundle.getString("registro.active") 
				+			"' ELSE '"
				+					bundle.getString("registro.inactive")
				+		"' END provStatusText,\n"
				+ "		CASE WHEN prov.audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
				+ "		coalesce(prov.audit_user_upd, prov.audit_user_ins) audit_user, \n" 
				+ "		coalesce(prov.audit_date_upd, prov.audit_date_ins) audit_date\n"
				+ " from " + TABLA+" prov, "
				+ super.ESQUEMA_TABLA+"COUNTRIES_CTG country,\n"
				+ super.ESQUEMA_TABLA+"REGION_CTG reg\n"
				+ " where country.ctr_id = reg.ctr_id \n"
				+ "  and reg.reg_id = prov.reg_id \n"
				+ "  and country.ctr_id = "+ctrId
				+ "  and reg.reg_id = "+regId;

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		return data;
	}

	public int insert(ProvinceForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
					+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer provId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into "+ TABLA + "\n"
					+ "("+ PREFIJO +"_ID,\n"
					+ PREFIJO +"_DESC,\n"
					+ PREFIJO + "_STATUS, \n"
					+ "    reg_id,\n"
					+ "    audit_user_ins, \n"
					+ "    audit_date_ins \n"
					+ ")\n"
					+ "values (?,?,?,?,?,now()"
					+ ")";

		Object[] param = new Object[5];
		int i = 0;
		param[i++] = provId;
		param[i++] = form.getProvDesc().trim();
	    param[i++] = form.getProvStatus();
	    param[i++] = new Integer(form.getRegId());
	    param[i++] = username;
	    
	    form.setProvId(String.valueOf(provId));

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	public int update(ProvinceForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ " set "+ PREFIJO +"_DESC= ?, \n"
					+ "    "+ PREFIJO +"_STATUS= ?, \n"
					+ "    audit_user_upd = ?, \n"
					+ "    audit_date_upd = current_date+current_time \n"
					+ " where "+ PREFIJO +"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[4];
		int i = 0;
		param[i++] = form.getProvDesc().trim();
	    param[i++] = form.getProvStatus().trim();
	    param[i++] = username;
	    param[i++] = new Integer(form.getProvId());

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	@Deprecated
	public int delete(ProvinceForm form) throws SQLException, Exception, Error {
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
	 * @param {org.promefrut.simefrut.struts.catalogs.forms.ProvinceForm}form 
	 * @param {String} username
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	private void updateDW(ProvinceForm form, String username) throws SQLException, Exception, Error {
		updateOriginDim(form,username);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateOriginDim(ProvinceForm form, String username) throws SQLException, Exception, Error {
		QueryRunner query = new QueryRunner();
		Object[] param = null;
		int i = 0;
		
		String sql = "select ORI_SK, ori_country, ori_country_eng, reg_desc \n"
			+ " from " + DW_TABLA +" \n"
			+ " where "+DW_PREFIJO+"_ID = ?"
			+ " AND current_date between effective_date and expiry_date ";
		
		MapListHandler handler = new MapListHandler();
		param = new Object[1];
		i = 0;
		param[i++] = new Integer(form.getProvId());
	    
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param,handler);
		String regDesc = null, ctrDesc=null, ctrDescEng=null;
		
		if(resultList.size()>0){
			for(Iterator it = resultList.iterator(); it.hasNext(); ){
				Map reg = (Map)it.next();
				
				BigDecimal fieldSk = (BigDecimal)reg.get("ORI_SK");
				regDesc = (String)reg.get("reg_desc");
				ctrDesc = (String)reg.get("ori_country");
				ctrDescEng = (String)reg.get("ori_country_eng");
				
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
			}
		}else{
			sql = "select ori_country, ori_country_eng, reg_desc \n"
				+ " from " + DW_TABLA +" \n"
				+ " where CTR_ID = ? "
				+ " AND REG_ID = ? "
				+ " and "+DW_PREFIJO+"_ID is null "
				+ " AND current_date between effective_date and expiry_date";
			
			param = new Object[2];
			i = 0;
			param[i++] = new Integer(form.getCtrId());
			param[i++] = new Integer(form.getRegId());
		    
			Map result = (Map)query.query(sessionManager.getConnection(), sql, param,new MapHandler());
			regDesc = (String)result.get("reg_desc");
			ctrDesc = (String)result.get("ori_country");
			ctrDescEng = (String)result.get("ori_country_eng");
		}
		
		if("A".equals(form.getProvStatus())){
			sql = " insert into "+DW_TABLA+"(\n"
				+ "ORI_SK, \n"
				+ " CTR_ID,\n"
				+ " ORI_COUNTRY,\n"
				+ " ORI_COUNTRY_ENG,\n"
				+ " REG_ID,\n"
				+ " REG_DESC,\n"
				+ DW_PREFIJO+"_ID,\n" 
				+ DW_PREFIJO+"_DESC,\n"
				+ " EFFECTIVE_DATE,\n"
				+ " EXPIRY_DATE, \n"
				+ " AUDIT_USER_INS, \n"
				+ " AUDIT_DATE_INS \n"
				+ ")\n"
				+ "values ((select coalesce(max(ORI_SK) + 1,1)\n"
				+ "         from "+DW_TABLA+"),\n"
				+ " ?,?,?,?,?,?,?,CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
				+ ")";
	
			param = new Object[8];
			i = 0;
			param[i++] = new Integer(form.getCtrId());
			param[i++] = ctrDesc;
			param[i++] = ctrDescEng;
			param[i++] = new Integer(form.getRegId());
			param[i++] = regDesc;
			param[i++] = new Integer(form.getProvId());
			param[i++] = form.getProvDesc().trim();
		    param[i++] = username;
	
			query.update(sessionManager.getConnection(), sql, param);
		}
	}
}
