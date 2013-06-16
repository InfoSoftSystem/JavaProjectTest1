package org.promefrut.simefrut.struts.commons.beans;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.promefrut.simefrut.struts.administration.beans.UserBean;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.commons.forms.LoginForm;

/**
 * @author Henry Willy Melara
 *
 */
public class LoginBean extends BaseDAO {

	public LoginBean(SessionManager session) {
		super(session);
	}

	@SuppressWarnings("unchecked")
	public List<UserForm> findUser(LoginForm form) throws SQLException, Exception, Error {
		String sql = new String();

		sql = "select base.*, tabla.\"password\"\n" +
			" from ("+(new UserBean(null)).QUERY_SELECT+") base, "+ 
				super.ESQUEMA+".USERS_SEC tabla" + 
			" where tabla.user_id = base.userId \n" +
			"   and tabla.username = '"+form.getUsername()+"' \n" ;
		
		Object[] param = new Object[1];
		param[0] = form.getUsername();
		BeanListHandler handler = new BeanListHandler(UserForm.class);
		QueryRunner query = new QueryRunner();

		return (List<UserForm>)query.query(sessionManager.getConnection(), sql, handler);
	}

	@SuppressWarnings("unchecked")
	public List<String> findGroupOption(String role) throws SQLException, Exception, Error {
		String sql = new String();

		sql = " select distinct og.GRP_NAME nameGroup, og.GRP_ID codeGroup, "
			+ "og.grp_iconCss iconCSSGroup, og.grp_msgProperty msgPropertyGroup, grp_order \n" 
			+ "from " + super.ESQUEMA_TABLA + "GROUP_OPT_SEC og, " 
			+ super.ESQUEMA_TABLA + "OPTIONS_SEC o, " 
			+ super.ESQUEMA_TABLA + "ROLES_OPT_SEC p\n" 
			+ "where p.ROL_ID = ?\n" 
			+ "and p.OPT_ID = o.OPT_ID\n" 
			+ "and og.GRP_ID = o.GRP_ID\n" 
			+ "and o.OPT_VISIBLE = 'Y'\n" 
			+ "order by grp_order ";
		Object[] param = new Object[1];
		param[0] = new Integer(role);

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		return (List<String>)query.query(sessionManager.getConnection(), sql, param, handler);
	}

	@SuppressWarnings("unchecked")
	public List<String> findOption(String role, String group) throws SQLException, Exception, Error {
		String sql = new String();

		sql = " select OPT_URL link, OPT_NAME \"name\", " +
				"OPT_TOOLTIP tooltip, OPT_VISIBLE visible,\n" +
				"opt_iconCss iconCSS, opt_msgProperty msgProperty , opt_order \n" 
			+ "from " + super.ESQUEMA_TABLA + "GROUP_OPT_SEC og, " 
			+ super.ESQUEMA_TABLA + "OPTIONS_SEC o, " 
			+ super.ESQUEMA_TABLA + "ROLES_OPT_SEC p\n" 
			+ "where p.ROL_ID = ?\n" 
			+ "and p.OPT_ID = o.OPT_ID\n" 
			+ "and o.GRP_ID = ?\n" 
			+ "and og.GRP_ID = o.GRP_ID \n" 
			+ "order by opt_order";

		Object[] param = new Object[2];
		param[0] = new Integer(role);
		param[1] = new Integer(group);

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		return (List<String>)query.query(sessionManager.getConnection(), sql, param, handler);
	}

	public String cambiarPwd(UserForm usuario) throws Exception {
		String ret = "";
		String sql = "select upper(USER_FIRSTTIME) contrasena "
				+ " from " + super.ESQUEMA_TABLA + "USERS_SEC \n" 
				+ " where USERNAME = '" + usuario.getUsername() + "'";

		PreparedStatement ps = sessionManager.getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		if(rs.next() && rs != null) {
			ret = rs.getString("contrasena").toString();
		}
		if(rs != null) {
			rs.close();
		}
		if(ps != null) {
			ps.close();
		}
		return ret;
	}
    
    public String findSessionTimeout() throws Exception{
        String sql = "select DET.VALOR, PARAM.DESCRIPCION " +
        		"from PROCESOS.PRC_PARAMETROS param, PROCESOS.PRC_DETPARAM det \n" + 
        		"    where param.CODPARAM = det.CODPARAM\n" + 
        		"    and param.CODPARAM = 'SIMR_SESSION_TIMEOUT'";
        
        Statement stmnt = null;
        ResultSet rs = null;
        String result = "";
        try{
            stmnt = sessionManager.getConnection().createStatement();
            rs = stmnt.executeQuery(sql);
            rs.next();
            result = rs.getString(1);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            stmnt.close();
            rs.close();
        }
        return result;
    }
        
    public String findSessionTimeoutCount() throws Exception{
        String sql = "select DET.VALOR, PARAM.DESCRIPCION from PROCESOS.PRC_PARAMETROS param, PROCESOS.PRC_DETPARAM det \n" + 
        "    where param.CODPARAM = det.CODPARAM\n" + 
        "    and param.CODPARAM = 'SIMR_SESSION_TIMEOUT_CNT'";
        
        Statement stmnt = null;
        ResultSet rs = null;
        String result = "";
        try{
            stmnt = sessionManager.getConnection().createStatement();
            rs = stmnt.executeQuery(sql);
            rs.next();
            result = rs.getString(1);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            stmnt.close();
            rs.close();
        }
        return result;
    }

}
