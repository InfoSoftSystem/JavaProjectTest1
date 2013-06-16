package org.promefrut.simefrut.struts.administration.beans;



import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.administration.forms.RolesForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;

/**
 * @author HWM
 *
 */
public class RolesBean extends BaseDAO {
	protected final String TABLA = super.ESQUEMA_TABLA + "ROLES_SEC";
	protected static final String PREFIJO = "rol";
	
	public final String QUERY_SELECT = "select \n"
			+ "		rol_id rolId, \n"
			+ "		rol_name rolDesc,\n"
			+ "		rol_status rolStatus, \n"
			+ "		CASE WHEN "+ PREFIJO +"_status = 'A' THEN '"
			+					 bundle.getString("registro.active") 
			+			"' ELSE '"
			+					bundle.getString("registro.inactive")
			+		"' END rolStatusText,\n"
			+ "		CASE WHEN audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
			+ "		coalesce(audit_user_upd, audit_user_ins) audit_user, \n" 
			+ "		coalesce(audit_date_upd, audit_date_ins) audit_date\n"
			+ "from "+ TABLA +" roles \n";
	

	/**
	 * @param session
	 */
	public RolesBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}
	
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find() throws SQLException, Exception, Error {
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), QUERY_SELECT, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		//String resultJSON = "Ext.data.JsonP.callback1("+data+")";
		return data;
	}

	/**
	 * Check if already exists
	 */
	public int validarRol(String rolDesc) throws SQLException, Exception, Error {
		String sql = "select count(*) cantidad\n" 
					+ "from " + TABLA + "\n" 
					+ "where rol_name = ?  ";

		QueryRunner query = new QueryRunner();
		ScalarHandler handler = new ScalarHandler();
		Object[] param = new Object[1];
		param[0] = rolDesc;

		Long result = (Long)query.query(sessionManager.getConnection(), sql, param, handler);

		return result.intValue();
	}

	public int insert(RolesForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
				+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer rolId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into " + TABLA + "\n" 
					+ " (\n" 
					+ "ROL_ID,\n" 
					+ "ROL_NAME,\n"
					+ "ROL_STATUS, \n" 
					+ "audit_user_ins,\n"
					+ "audit_date_ins\n"
					+ ") values " 
					+ " (?,?,?,?,now()) ";

		Object[] param = new Object[4];
		int i = 0;
		param[i++] = rolId;
		param[i++] = form.getRolDesc();
		param[i++] = form.getRolStatus();
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
	public int update(RolesForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ "set \n" 
					+ "		ROL_NAME=?,\n"
					+ "		ROL_STATUS=?, \n" 
					+ "		audit_user_upd=?,\n"
					+ "		audit_date_upd=now()\n"
					+ " where "+ PREFIJO +"_ID = ? ";
	
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[4];
		int i = 0;
		
		param[i++] = form.getRolDesc();
		param[i++] = form.getRolStatus();
		param[i++] = username;
		param[i++] = new Integer(form.getRolId());
	
		return query.update(sessionManager.getConnection(), sql, param);
	}
	
	public int delete(RolesForm form) throws SQLException, Exception, Error {
		String sql = "DELETE FROM " + TABLA +  
					" WHERE "+PREFIJO+"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		param[0] = new Integer(form.getRolId());

		return query.update(sessionManager.getConnection(), sql, param);
	}
	
}
