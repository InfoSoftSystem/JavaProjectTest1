package org.promefrut.simefrut.struts.administration.beans;



import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;

/**
 * @author Henry Willy Melara
 *
 */
public class UserBean extends BaseDAO {
	protected final String TABLA = super.ESQUEMA_TABLA + "USERS_SEC";
	protected static final String PREFIJO = "user";
	public final String QUERY_SELECT = " SELECT "+ PREFIJO +"_ID userId,\n"
			+ " 	u.ctr_id ctrId, \n"
			+ " 	ctr_desc ctrDescSpa, \n"
			+ " 	ctr_desc_eng ctrDescEng,\n"
			+ " 	u.rol_id rolId, \n"
			+ " 	rol_name rolDesc, \n"
			+ " 	\"username\" \"username\", \n"
			+ " 	user_name namePerson, \n"
			+ " 	no_country noCountry, \n"
			+ " 	"+ PREFIJO +"_name \"name\", \n"
			+ " 	lower("+ PREFIJO +"_email) email, \n"
			+ " 	"+ PREFIJO +"_phone phone, \n"
			+ " 	notify_price notifyPrice, \n"
			+ " 	notify_commerce notifyCommerce, \n"
			+ " 	notify_production notifyProduction, \n"
			+ " 	false flgPasswordUpd \n"
			+ "FROM " + TABLA + " u, " 
			+ super.ESQUEMA_TABLA + "ROLES_SEC r, \n" 
			+ super.ESQUEMA_TABLA + "COUNTRIES_CTG c \n"
			+ "WHERE u.rol_id = r.rol_id \n"
			+ "  and u.ctr_id = c.ctr_id ";
	
	/**
	 * @param session
	 */
	public UserBean(SessionManager session) {
		super(session);
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
	 * se valida que el codigo de usuario no se repita
	 */
	public int validarcodigousuario(UserForm form) throws SQLException, Exception, Error {
		String sql = "select count(*) cantidad\n" 
					+ "from " + TABLA + "\n" 
					+ "where user_name = ?  ";
		
		if(!StringUtils.isEmpty(form.getUserId())){
			sql+="  and user_id <> "+form.getUserId();
		}
		
		QueryRunner query = new QueryRunner();
		ScalarHandler handler = new ScalarHandler();
		Object[] param = new Object[1];
		param[0] = form.getUsername();

		Long result = (Long)query.query(sessionManager.getConnection(), sql, param, handler);

		return result.intValue();
	}
	
	/**
	 * se valida que el email de usuario no se repita
	 */
	public int validarEmail(UserForm form) throws SQLException, Exception, Error {
		if(!StringUtils.isEmpty(form.getEmail())){
			return 0;
		}
		
		String sql = "select count(*) cantidad\n" 
					+ "from " + TABLA + "\n" 
					+ "where lower(user_email) = ?";
		
		if(!StringUtils.isEmpty(form.getUserId())){
			sql+= "  and user_id <> "+form.getUserId();
		}
		
		QueryRunner query = new QueryRunner();
		ScalarHandler handler = new ScalarHandler();
		Object[] param = new Object[1];
		param[0] = form.getEmail().toLowerCase();

		Long result = (Long)query.query(sessionManager.getConnection(), sql, param, handler);

		return result.intValue();
	}

	public int insert(UserForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
				+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer userId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into " + TABLA + "\n" 
					+ " (USER_ID,\n" 
					+ "ROL_ID,\n" 
					+ "CTR_ID,\n"
					+ "USERNAME,\n"
					+ "password,\n"
					+ "no_country,\n"
					+ "user_firsttime,\n"
					+ "user_name ,\n"
					+ "user_email,\n"
					+ "user_phone,\n"
					+ "notify_price ,\n" 
					+ "notify_commerce ,\n" 
					+ "notify_production, \n" 
					+ "audit_user_ins,\n"
					+ "audit_date_ins\n"
					+ ") values " 
					+ " (?, ?, ?,?,?,?, 'Y', ?, ?,?,?,?,?,?,now()) ";

		Object[] param = new Object[13];
		int i = 0;
		param[i++] = userId;
		param[i++] = new Integer(form.getRolId());
		param[i++] = new Integer(form.getCtrId());
		param[i++] = form.getUsername();
		param[i++] = form.getPasswordScriptado();
		param[i++] = form.getNoCountry();
		param[i++] = form.getNamePerson();
		param[i++] = form.getEmail().toLowerCase();
		param[i++] = form.getPhone();
		param[i++] = form.getNotifyPrice();
		param[i++] = form.getNotifyCommerce();
		param[i++] = form.getNotifyProduction();
		param[i++] = username;
		
		return query.update(sessionManager.getConnection(), sql, param);
	}

	public int update(UserForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ "set \n" 
					+ "		ROL_ID = ?,\n" 
					+ "		CTR_ID = ?,\n"
					+ "		USERNAME = ?,\n"
					+ "		PASSWORD = COALESCE(?,PASSWORD),\n"
					+ "		no_country = ?,\n"
					+ "		user_firsttime = case when COALESCE(?,'-')='-' then user_firsttime else 'Y' end ,\n"
					+ "		user_name = ?,\n"
					+ "		user_email = ?,\n"
					+ "		user_phone = ?,\n"
					+ "		notify_price = ?,\n" 
					+ "		notify_commerce = ?,\n" 
					+ "		notify_production = ?, \n" 
					+ "		audit_user_upd = ?, \n"
					+ "		audit_date_upd = now() \n"
					+ " where "+ PREFIJO +"_ID = ? ";
	
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[14];
		int i = 0;
		
		param[i++] = new Integer(form.getRolId());
		param[i++] = new Integer(form.getCtrId());
		param[i++] = form.getUsername();
		param[i++] = (form.getFlgPasswordUpd() ? form.getPasswordScriptado() : null);
		param[i++] = form.getNoCountry();
		param[i++] = (form.getFlgPasswordUpd() ? form.getPasswordScriptado() : null);
		param[i++] = form.getNamePerson();
		param[i++] = form.getEmail().toLowerCase();
		param[i++] = form.getPhone();
		param[i++] = form.getNotifyPrice();
		param[i++] = form.getNotifyCommerce();
		param[i++] = form.getNotifyProduction();
		param[i++] = username;
		param[i++] = new Integer(form.getUserId());
	
		return query.update(sessionManager.getConnection(), sql, param);
	}
	
	/**
	 * This function is used by ChangePwdAction o ChangePasswordAction. Just to update it's own password
	 * 
	 * @param form
	 * @param username
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	public int updatePassword(UserForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ "set \n" 
					+ "		PASSWORD = COALESCE(?,PASSWORD),\n"
					+ "		user_firsttime = 'N',\n"
					+ "		audit_user_upd = ?, \n"
					+ "		audit_date_upd = now() \n"
					+ " where "+ PREFIJO +"_ID = ? ";
	
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[3];
		int i = 0;
		
		param[i++] = form.getPasswordScriptado();
		param[i++] = username;
		param[i++] = new Integer(form.getUserId());
	
		return query.update(sessionManager.getConnection(), sql, param);
	}

	public int delete(UserForm form) throws SQLException, Exception, Error {
		String sql = "DELETE FROM " + TABLA +  
					" WHERE "+PREFIJO+"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		param[0] = new Integer(form.getUserId());

		return query.update(sessionManager.getConnection(), sql, param);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getCountriesCollection(UserForm user) throws Exception, Error {
		String sql = "select \n"
			+ "ctr_id ctrId, \n"
			+ "ctr_desc ctrDescSpa,\n"
			+ "ctr_desc_eng ctrDescEng\n"
			+ "from "+ ESQUEMA +".countries_ctg\n"
			+ "where ctr_status = 'A'\n";
		
		if(!user.getNoCountry()){
			sql+= " and ctr_id = " + user.getCtrId();
		}
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
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
	
	
	public UserForm findByEmail(String email) throws SQLException, Exception, Error {
		
		BeanHandler handler = new BeanHandler(UserForm.class);
		QueryRunner query = new QueryRunner();
		
		String sql = QUERY_SELECT + 
				"  AND lower(u."+ PREFIJO +"_email) = ? ";
		
		Object[] param = new Object[1];
		int i = 0;
		
		param[i++] = email.toLowerCase();
		
		UserForm result = (UserForm)query.query(sessionManager.getConnection(), sql, param, handler);

		return result;
	}

}
