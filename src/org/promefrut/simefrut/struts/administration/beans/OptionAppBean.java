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
import org.promefrut.simefrut.struts.administration.forms.OptionAppForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;

/**
 * @author HWM
 *
 */
public class OptionAppBean extends BaseDAO {

	protected final String TABLA = super.ESQUEMA_TABLA + "OPTIONS_SEC";
	protected static final String PREFIJO = "opt";
	
	public final String QUERY_SELECT = "select \n"
			+ "		opt_id optId, \n"
			+ "		opt.grp_id grpId, \n"
			+ "		grp.grp_name grpDesc, \n"
			+ "		grp.grp_msgproperty grpMsgProperty, \n"
			+ "		opt_name optDesc,\n"
			+ "		opt_url optUrl,\n"
			+ "		opt_visible optVisible, \n"
			+ "		opt_iconcss optIconCSS, \n"
			+ "		opt_msgproperty optMsgProperty, \n"
			+ "		opt_order optOrder, \n"
			+ "		CASE WHEN opt.audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
			+ "		coalesce(opt.audit_user_upd, opt.audit_user_ins) audit_user, \n" 
			+ "		coalesce(opt.audit_date_upd, opt.audit_date_ins) audit_date\n"
			+ "from "+ TABLA +" opt, \n"
			+ "		"+ super.ESQUEMA_TABLA +"GROUP_OPT_SEC grp \n"
			+ " where grp.grp_id = opt.grp_id ";
	

	/**
	 * @param session
	 */
	public OptionAppBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}
	
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find() throws SQLException, Exception, Error {
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), QUERY_SELECT, handler);
		
		for(int i = 0; i<resultList.size(); i++){
			Map reg = (Map)resultList.get(i);
			
			String grpMsgProperty = (String)reg.get("grpMsgProperty");
			
			if(!StringUtils.isEmpty(grpMsgProperty)){
				reg.put("grpDescMsg", bundle.getString(grpMsgProperty));
			}else{
				reg.put("grpDescMsg", (String)reg.get("grpDesc"));
			}
		}
		
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		return data;
	}

	/**
	 * Check if already exists
	 */
	public int validateOption(OptionAppForm form) throws SQLException, Exception, Error {
		String sql = "select count(*) cantidad\n" 
					+ "from " + TABLA + "\n" 
					+ "where opt_name = ?  ";
		
		if(!StringUtils.isEmpty(form.getOptId())){
			sql+="  and opt_id <> "+form.getOptId();
		}

		QueryRunner query = new QueryRunner();
		ScalarHandler handler = new ScalarHandler();
		Object[] param = new Object[1];
		param[0] = form.getOptDesc();

		Long result = (Long)query.query(sessionManager.getConnection(), sql, param, handler);

		return result.intValue();
	}

	public int insert(OptionAppForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
				+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer optId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into " + TABLA + "\n" 
					+ " (\n" 
					+ "OPT_ID,\n"
					+ "GRP_ID,\n" 
					+ "OPT_NAME,\n"
					+ "OPT_URL,\n"
					+ "OPT_VISIBLE,\n"
					+ "OPT_ICONCSS, \n"
					+ "OPT_MSGPROPERTY, \n" 
					+ "OPT_ORDER, \n" 
					+ "audit_user_ins,\n"
					+ "audit_date_ins\n"
					+ ") values " 
					+ " (?,?,?,?,?,?,?,?,?,now()) ";

		Object[] param = new Object[9];
		int i = 0;
		param[i++] = optId;
		param[i++] = new Integer(form.getGrpId());
		param[i++] = form.getOptDesc();
		param[i++] = form.getOptUrl();
		param[i++] = form.getOptVisible();
		param[i++] = form.getOptIconCSS();
		param[i++] = form.getOptMsgProperty();
		param[i++] = new Integer(form.getOptOrder());
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
	public int update(OptionAppForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ "set \n" 
					+ "		GRP_ID=?,\n"
					+ "		OPT_NAME=?,\n"
					+ "		OPT_URL=?,\n"
					+ "		OPT_VISIBLE=?,\n"
					+ "		OPT_iconcss=?, \n"
					+ "		OPT_msgproperty=?, \n"
					+ "		OPT_order=?, \n"
					+ "		audit_user_upd=?,\n"
					+ "		audit_date_upd=now()\n"
					+ " where "+ PREFIJO +"_ID = ? ";
	
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[9];
		int i = 0;
		
		param[i++] = new Integer(form.getGrpId());
		param[i++] = form.getOptDesc();
		param[i++] = form.getOptUrl();
		param[i++] = form.getOptVisible();
		param[i++] = form.getOptIconCSS();
		param[i++] = form.getOptMsgProperty();
		param[i++] = new Integer(form.getOptOrder());
		param[i++] = username;
		param[i++] = new Integer(form.getOptId());
	
		return query.update(sessionManager.getConnection(), sql, param);
	}
	
	public int delete(OptionAppForm form) throws SQLException, Exception, Error {
		String sql = "DELETE FROM " + TABLA +  
					" WHERE "+PREFIJO+"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		param[0] = new Integer(form.getOptId());

		return query.update(sessionManager.getConnection(), sql, param);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getOptionGroupsCollection() throws Exception, Error {
		String sql = "select \n"
			+ "grp_id grpId, \n"
			+ "grp_name grpDesc,\n"
			+ "grp_MsgProperty grpMsgProperty\n"
			+ "from "+ ESQUEMA +".GROUP_OPT_sec\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		
		for(int i = 0; i<resultList.size(); i++){
			Map reg = (Map)resultList.get(i);
			
			String grpMsgProperty = (String)reg.get("grpMsgProperty");
			
			if(!StringUtils.isEmpty(grpMsgProperty)){
				reg.put("grpDescMsg", bundle.getString(grpMsgProperty));
			}else{
				reg.put("grpDescMsg", bundle.getString("grpDesc"));
			}
		}
		
		JSONArray json = new JSONArray(resultList);
		return json.toString()==null?"":json.toString();
	}
}
