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
import org.promefrut.simefrut.struts.catalogs.forms.QualityForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class QualityBean extends BaseDAO {
	public final String TABLA = super.ESQUEMA_TABLA + "QUALITIES_CTG";
	protected static final String PREFIJO = "qua";
	protected final String DW_PREFIJO = "qua";
	protected final String DW_TABLA = super.ESQUEMA_TABLA + "QUALITY_DIM";

	public QualityBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find() throws SQLException, Exception, Error {
		String sql = "select "+ PREFIJO +"_ID qualityId,\n"
				+ " 	"+ PREFIJO +"_Code qualityCode, \n"	
				+ " 	"+ PREFIJO +"_desc qualityDesc, \n"
				+ "		"+ PREFIJO +"_status qualityStatus,\n"
				+ "		CASE WHEN "+ PREFIJO +"_status = 'A' THEN '"
				+					 bundle.getString("registro.active") 
				+			"' ELSE '"
				+					bundle.getString("registro.inactive")
				+		"' END qualityStatusText,\n"
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

	public int insert(QualityForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
					+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer qualityId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into "+ TABLA + "\n"
					+ "("+ PREFIJO +"_ID,\n"
					+ PREFIJO + "_CODE, \n"
					+ PREFIJO +"_DESC,\n"
					+ PREFIJO + "_STATUS, \n"
					+ "    audit_user_ins, \n"
					+ "    audit_date_ins \n"
					+ ")\n"
					+ "values (?,\n"
					+ " ?,?,?,?,now()"
					+ ")";

		Object[] param = new Object[5];
		int i = 0;
		param[i++] = qualityId;
		param[i++] = form.getQualityCode().trim();
		param[i++] = form.getQualityDesc().trim();
	    param[i++] = form.getQualityStatus();
	    param[i++] = username;
	    
	    form.setQualityId(String.valueOf(qualityId));

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	public int update(QualityForm form, String username) throws SQLException, Exception, Error {
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
		param[i++] = form.getQualityCode().trim();
		param[i++] = form.getQualityDesc().trim();
	    param[i++] = form.getQualityStatus().trim();
	    param[i++] = username;
	    param[i++] = new Integer(form.getQualityId());

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	@Deprecated
	public int delete(QualityForm form) throws SQLException, Exception, Error {
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
	 * @param {org.promefrut.simefrut.struts.catalogs.forms.QualityForm}form 
	 * @param {String} username
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateDW(QualityForm form, String username) throws SQLException, Exception, Error {
		String sql = "select "+DW_PREFIJO+"_sk \n"
					+ " from " + DW_TABLA +" \n"
					+ " where "+DW_PREFIJO+"_ID = ?"
					+ " AND current_date between effective_date and expiry_date ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		int i = 0;
		param[i++] = new Integer(form.getQualityId());
	    
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param,handler);
		
		if(resultList.size()>0){
			for(Iterator it = resultList.iterator(); it.hasNext(); ){
				Map reg = (Map)it.next();
				
				BigDecimal fieldSk = (BigDecimal)reg.get(DW_PREFIJO+"_sk");
				
				sql = " update " + DW_TABLA +" \n"
						+ " set EXPIRY_DATE = current_date-1, \n"
						+ "    AUDIT_USER_UPD = ?, \n"
						+ "    AUDIT_DATE_UPD = current_date + current_time \n"
						+ " where "+DW_PREFIJO+"_SK = ? ";
				
				param = new Object[2];
				i = 0;
				param[i++] = username;
				param[i++] = fieldSk;
				
				query.update(sessionManager.getConnection(), sql, param);
			}
		}
		
		if("A".equals(form.getQualityStatus())){
			sql = " insert into "+DW_TABLA+"(\n"
				+ DW_PREFIJO+"_SK, \n"
				+ DW_PREFIJO+"_ID,\n" 
				+ DW_PREFIJO+"_CODE,\n" 
				+ DW_PREFIJO+"_DESC,\n"
				+ " EFFECTIVE_DATE,\n"
				+ " EXPIRY_DATE, \n"
				+ " AUDIT_USER_INS, \n"
				+ " AUDIT_DATE_INS \n"
				+ ")\n"
				+ "values ((select coalesce(max("+DW_PREFIJO+"_SK) + 1,1)\n"
				+ "         from "+DW_TABLA+"),\n"
				+ " ?,?,?,CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
				+ ")";
	
			param = new Object[4];
			i = 0;
			param[i++] = new Integer(form.getQualityId());
			param[i++] = form.getQualityCode().trim();
			param[i++] = form.getQualityDesc().trim();
		    param[i++] = username;
	
			query.update(sessionManager.getConnection(), sql, param);
		}
	}
}
