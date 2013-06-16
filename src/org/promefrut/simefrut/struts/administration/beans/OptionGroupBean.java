package org.promefrut.simefrut.struts.administration.beans;



import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.administration.forms.OptionGroupForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;

/**
 * @author HWM
 *
 */
public class OptionGroupBean extends BaseDAO {

	protected final String TABLA = super.ESQUEMA_TABLA + "GROUP_OPT_SEC";
	protected static final String PREFIJO = "grp";
	
	public final String QUERY_SELECT = "select \n"
			+ "		grp_id grpId, \n"
			+ "		grp_name grpDesc,\n"
			+ "		grp_iconcss grpIconCSS, \n"
			+ "		grp_order grpOrder, \n"
			+ "		grp_msgproperty grpMsgProperty, \n"
			+ "		CASE WHEN audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
			+ "		coalesce(audit_user_upd, audit_user_ins) audit_user, \n" 
			+ "		coalesce(audit_date_upd, audit_date_ins) audit_date\n"
			+ "from "+ TABLA +" grp \n";
	

	/**
	 * @param session
	 */
	public OptionGroupBean(SessionManager session, ResourceBundle bundle) {
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
	public int validarGroup(OptionGroupForm form) throws SQLException, Exception, Error {
		String sql = "select count(*) cantidad\n" 
					+ "from " + TABLA + "\n" 
					+ "where grp_name = ?  ";
		
		if(!StringUtils.isEmpty(form.getGrpId())){
			sql+="  and grp_id <> "+form.getGrpId();
		}

		QueryRunner query = new QueryRunner();
		ScalarHandler handler = new ScalarHandler();
		Object[] param = new Object[1];
		param[0] = form.getGrpDesc();

		Long result = (Long)query.query(sessionManager.getConnection(), sql, param, handler);

		return result.intValue();
	}

	public int insert(OptionGroupForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
				+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer grpId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into " + TABLA + "\n" 
					+ " (\n" 
					+ "GRP_ID,\n" 
					+ "GRP_NAME,\n"
					+ "grp_iconcss, \n"
					+ "grp_order, \n" 
					+ "grp_msgproperty, \n" 
					+ "audit_user_ins,\n"
					+ "audit_date_ins\n"
					+ ") values " 
					+ " (?,?,?,?,?,?,now()) ";

		Object[] param = new Object[6];
		int i = 0;
		param[i++] = grpId;
		param[i++] = form.getGrpDesc();
		param[i++] = form.getGrpIconCSS();
		param[i++] = new Integer(form.getGrpOrder());
		param[i++] = form.getGrpMsgProperty();
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
	public int update(OptionGroupForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ "set \n" 
					+ "		GRP_NAME=?,\n"
					+ "		grp_iconcss=?, \n"
					+ "		grp_order=?, \n"
					+ "		grp_msgproperty=?, \n"
					+ "		audit_user_upd=?,\n"
					+ "		audit_date_upd=now()\n"
					+ " where "+ PREFIJO +"_ID = ? ";
	
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[6];
		int i = 0;
		
		param[i++] = form.getGrpDesc();
		param[i++] = form.getGrpIconCSS();
		param[i++] = new Integer(form.getGrpOrder());
		param[i++] = form.getGrpMsgProperty();
		param[i++] = username;
		param[i++] = new Integer(form.getGrpId());
	
		return query.update(sessionManager.getConnection(), sql, param);
	}
	
	public int delete(OptionGroupForm form) throws SQLException, Exception, Error {
		String sql = "DELETE FROM " + TABLA +  
					" WHERE "+PREFIJO+"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		param[0] = new Integer(form.getGrpId());

		return query.update(sessionManager.getConnection(), sql, param);
	}

}
