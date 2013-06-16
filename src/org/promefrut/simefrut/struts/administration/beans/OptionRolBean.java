package org.promefrut.simefrut.struts.administration.beans;



import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.administration.forms.OptionRolForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;

public class OptionRolBean extends BaseDAO {

	protected final String TABLA = super.ESQUEMA_TABLA + "ROLES_OPT_SEC";
	protected static final String PREFIJO = "ropt";
	
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
			+ "from "+ super.ESQUEMA_TABLA +"OPTIONS_SEC opt, \n"
			+ "		"+ super.ESQUEMA_TABLA +"GROUP_OPT_SEC grp \n"
			+ " where grp.grp_id = opt.grp_id ";
	

	/**
	 * @param session
	 */
	public OptionRolBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}
	
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find(String rolId) throws SQLException, Exception, Error {
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		
		String sql = "select " +
				"		ropt.rol_id rolId, \n"
				+ "		optId, \n"
				+ "		grpId, \n"
				+ "		grpDesc, \n"
				+ "		grpMsgProperty, \n"
				+ "		optDesc,\n"
				+ "		optUrl,\n"
				+ "		optVisible, \n"
				+ "		optIconCSS, \n"
				+ "		optMsgProperty, \n"
				+ "		optOrder, \n"+  
				"		case when ropt.opt_id is null then \n" +
				"			false \n" +
				"		else \n" +
				"			true \n" +
				"		end selected \n" +
				" from ("+QUERY_SELECT+") base " +
				"		left outer join "+ TABLA +" ropt \n"+
				"			on ropt.opt_id = base.optId \n"+
				"     		and ropt.rol_id = "+ rolId +"\n" ;
		
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		
		for(int i = 0; i<resultList.size(); i++){
			Map reg = (Map)resultList.get(i);
			
			String grpMsgProperty = (String)reg.get("grpMsgProperty");
			
			if(!StringUtils.isEmpty(grpMsgProperty)){
				try{
					reg.put("grpDescMsg", bundle.getString(grpMsgProperty));
				}catch(Exception e){
					reg.put("grpDescMsg", (String)reg.get("grpDesc"));
				}
				
			}else{
				reg.put("grpDescMsg", (String)reg.get("grpDesc"));
			}
			
			String optMsgProperty = (String)reg.get("optMsgProperty");
			
			if(!StringUtils.isEmpty(optMsgProperty)){
				try{
					reg.put("optDescMsg", bundle.getString(optMsgProperty));
				}catch(Exception e){
					reg.put("optDescMsg", (String)reg.get("optDesc"));
				}
			}else{
				reg.put("optDescMsg", (String)reg.get("optDesc"));
			}
		}
		
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		return data;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int update(OptionRolForm form, String username) throws SQLException, Exception, Error {
		
		QueryRunner query = new QueryRunner();
		
		//List the ones to insert
		String sql = "select opt_id " +
			" from "+ super.ESQUEMA_TABLA +"OPTIONS_SEC opt" +
			" where opt_id in ("+form.getOptId()+")" +
			"   and opt_id not in (select opt_id from "+TABLA+" where rol_id = "+form.getRolId()+")";
		
		List<Map> insertionList = (List<Map>)query.query(sessionManager.getConnection(), sql, new MapListHandler());
		
		//List the ones to eliminate
		sql = "select ropt_id roptId" +
			" from "+TABLA+" " +
			" where rol_id = "+form.getRolId() +
			"   and opt_id not in("+form.getOptId()+")";
		
		List<OptionRolForm> deleteList = (List<OptionRolForm>)query.query(sessionManager.getConnection(), sql, new BeanListHandler(OptionRolForm.class));
		
		//Delete the ones not associated
		for(int j=0; j<deleteList.size();j++){
			OptionRolForm tmpForm = ((OptionRolForm)deleteList.get(j));
			this.delete(tmpForm);
		}
		
		//Insert the ones with new association
		for(int j=0; j<insertionList.size();j++){
			Integer optId = (Integer)((Map)insertionList.get(j)).get("opt_id");
						
			sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
				+ "         from " + TABLA ;
			
			ScalarHandler handler = new ScalarHandler();
			Integer roptId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
			
			sql = " insert into " + TABLA + "\n" 
				+ " (\n" 
				+ "ROPT_ID,	\n" 
				+ "ROL_ID,	\n"
				+ "OPT_ID,	\n"
				+ "audit_user_ins,	\n"
				+ "audit_date_ins	\n"
				+ ") values " 
				+ " (?,?,?,?,now()) ";
	
			Object[] param = new Object[4];
			int i = 0;
			param[i++] = roptId;
			param[i++] = new Integer(form.getRolId());
			param[i++] = optId;
			param[i++] = username;
			
			query.update(sessionManager.getConnection(), sql, param);
		}
		
		return form.getOptId().split(",").length;
	}

	
	public int delete(OptionRolForm form) throws SQLException, Exception, Error {
		String sql = "DELETE FROM " + TABLA +  
					" WHERE "+PREFIJO+"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		param[0] = new Integer(form.getRoptId());

		return query.update(sessionManager.getConnection(), sql, param);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getRolesCollection() throws Exception, Error {
		String sql = "select \n"
			+ "rol_id rolId, \n"
			+ "rol_name rolDesc\n"
			+ "from "+ ESQUEMA +".roles_sec\n"
			+ "where rol_status = 'A'\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}

}//fin de la clase