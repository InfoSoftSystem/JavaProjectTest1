package org.promefrut.simefrut.struts.administration.beans;



import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;

/**
 * @author HWM
 *
 */
public class ParametersBean extends BaseDAO {
	protected final String TABLA = super.ESQUEMA_TABLA + "PARAMETERS_SEC";
	protected static final String PREFIJO = "par";
	
	public final String QUERY_SELECT = "select \n"
			+ "		a.par_id parId, \n"
			+ "		par_name parName, \n"
			+ "		dpm_Value dpmValue,\n"
			+ "		CASE WHEN a.audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
			+ "		coalesce(a.audit_user_upd, a.audit_user_ins) audit_user, \n" 
			+ "		coalesce(a.audit_date_upd, a.audit_date_ins) audit_date\n"
			+ "from "+ TABLA +" a, "+ESQUEMA+".PARAMETERS_DETAIL_SEC b \n"
			+ "where a.par_id = b.par_id";

	/**
	 * @param session
	 */
	public ParametersBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}
	
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find() throws SQLException, Exception, Error {
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), QUERY_SELECT, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		return data;
	}

	/**
	 * Check if already exists
	 */
	public boolean exists(String parName) throws SQLException, Exception, Error {
		String sql = "select count(*) cantidad\n" 
					+ "from " + TABLA + "\n" 
					+ "where par_name = ?  ";

		QueryRunner query = new QueryRunner();
		ScalarHandler handler = new ScalarHandler();
		Object[] param = new Object[1];
		param[0] = parName;

		Long result = (Long)query.query(sessionManager.getConnection(), sql, param, handler);

		if(result.intValue()>0){
			return true;
		}else{
			return false;
		}
	}

	public int insert(String parName, String dpmValue, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
				+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer parId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into " + TABLA + "\n" 
					+ " (\n" 
					+ "PAR_ID,\n" 
					+ "PAR_NAME,\n"
					+ "PAR_DESC, \n" 
					+ "audit_user_ins,\n"
					+ "audit_date_ins\n"
					+ ") values " 
					+ " (?,?,?,?,now()) ";

		Object[] param = new Object[4];
		int i = 0;
		param[i++] = parId;
		param[i++] = parName;
		param[i++] = "--";
		param[i++] = username;
		
		query.update(sessionManager.getConnection(), sql, param);
		
		
		
		sql = "select coalesce(max(DPM_ID) + 1,1)\n"
				+ "         from " + ESQUEMA + ".PARAMETERS_DETAIL_SEC";
		
		Integer dpmId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into " + ESQUEMA + ".PARAMETERS_DETAIL_SEC \n" 
				+ " (\n" 
				+ "DPM_ID,\n"
				+ "PAR_ID,\n" 
				+ "DPM_VALUE,\n"
				+ "audit_user_ins,\n"
				+ "audit_date_ins\n"
				+ ") values " 
				+ " (?,?,?,?,now()) ";

		param = new Object[4];
		i = 0;
		param[i++] = dpmId;
		param[i++] = parId;
		param[i++] = dpmValue;
		param[i++] = username;
		
		return query.update(sessionManager.getConnection(), sql, param);
	}

	/**
	 * 
	 * @param form
	 * @param username
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	public int update(String parName, String dpmValue, String username) throws Exception, Error {
		String sql = " update " + ESQUEMA + ".PARAMETERS_DETAIL_SEC B \n"
					+ "set \n" 
					+ "		DPM_VALUE=?,\n"
					+ "		audit_user_upd=?,\n"
					+ "		audit_date_upd=now()\n"
					+ " where B.PAR_ID IN (SELECT A.PAR_ID" 
					+ "						from "+TABLA+" a" 
					+ "						where a.par_name = ?) ";
	
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[3];
		int i = 0;
		
		param[i++] = dpmValue;
		param[i++] = username;
		param[i++] = parName;
	
		return query.update(sessionManager.getConnection(), sql, param);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getParameterValue(String parName) throws Exception, Error {
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		
		String sql = QUERY_SELECT+
				"  and par_name = '"+parName+"'";
		
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql , handler);
		String dpmValue = new String();
		
		if(resultList.size()>0){
			dpmValue = (String)((Map)resultList.get(0)).get("dpmValue");
		}
		
		return dpmValue;
	}
}