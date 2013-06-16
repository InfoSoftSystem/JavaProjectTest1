package org.promefrut.simefrut.struts.commons.actions;



import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.commons.beans.LoginBean;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.LoginForm;
import org.promefrut.simefrut.utils.LookUpResourceSchema;
import org.promefrut.simefrut.utils.PostgreSQLConnection;

/**
 * @author Henry Willy Melara
 *
 */
public class LoginAction extends LookupDispatchAction {

	protected Map<String, String> getKeyMethodMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("opc.login", "login");
		map.put("opc.logout", "logout");
		map.put("opc.change", "change");
		return map;
	}

	public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response) throws Exception, ServletException {

		HttpSession session = request.getSession(true);
		LoginForm loginForm = (LoginForm)form;
		
		if(loginForm.getLenguaje() != null) {
			
			if(loginForm.getLenguaje().equals("1")) {
				session.setAttribute(Globals.LOCALE_KEY, new Locale("en", "US")); /*En este caso guardamos la Locale de ingles*/
				session.setAttribute("idioma", "I");
			
			} else if(loginForm.getLenguaje().equals("2")) {
				session.setAttribute(Globals.LOCALE_KEY,new Locale("en", "SV")); /*En este caso guardamos la Locale de spanish*/
				session.setAttribute("idioma", "E");
				
			} else {
				session.setAttribute(Globals.LOCALE_KEY,new Locale("en", "SV")); /*En este caso guardamos la Locale de spanish*/
				session.setAttribute("idioma", "E");
			}
		} else {
			session.setAttribute(Globals.LOCALE_KEY,new Locale("en", "SV")); /*En este caso guardamos la Locale de spanish*/
			session.setAttribute("idioma", "E");
		}

		return mapping.findForward("fail");
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward login(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
							   HttpServletResponse response) throws Exception, ServletException {
		
		/**
		* se obtiene el datasource de la sesion solamente en el login para luego guardarla
		* en session para se utilizada en toda la aplicacion*/


		DataSource ds = null; //getDataSource
		
		String url = PostgreSQLConnection.getConexionUrl(), 
	    		database = PostgreSQLConnection.getDatabase();
	    
		String usuario = PostgreSQLConnection.getConexionUsuario(), 
				password = PostgreSQLConnection.getConexionPassword();
		
		HttpSession session = request.getSession(true);
		LoginForm loginForm = (LoginForm)form;
		LoginBean loginBean = null;
		ActionErrors errors = new ActionErrors();
		
		try{
			Context ic = new InitialContext();
			Object obj = ic.lookup(LookUpResourceSchema.DATA_SOURCE); // Datasource Prod
			ds = (DataSource)obj; //getDataSource
		}catch(Exception e){
			try{
				System.out.println("No se encontro datasource en \"LoginAction.java\", Se creara nuevo datasource");
				//Si no logra crear la conexion con el datasource en el contexto. Intenta con la credencial asignada en properties
				Jdbc3PoolingDataSource dsSource = new Jdbc3PoolingDataSource();
				dsSource.setDataSourceName(LookUpResourceSchema.DATA_SOURCE_NAME);
				dsSource.setServerName(url);
				dsSource.setDatabaseName(database);
				dsSource.setUser(usuario);
				dsSource.setPassword(password);
				dsSource.setMaxConnections(LookUpResourceSchema.DATA_SOURCE_MAX_CONNECTION);
				new InitialContext().rebind(LookUpResourceSchema.DATA_SOURCE, dsSource);
				Context ic = new InitialContext();
				Object obj = ic.lookup(LookUpResourceSchema.DATA_SOURCE); // Datasource Prod
				ds = (DataSource)obj; //getDataSource
			}catch(Exception ee){
				ee.printStackTrace();
				errors.add("globalErrors", new ActionError("mensaje","No se encontro el DS --> " + LookUpResourceSchema.DATA_SOURCE + ". Tampoco se logro establecer conexion auxiliar a la base de datos"));
				//System.out.println("No se encontro el DS --> " + LookUpResourceSchema.DATA_SOURCE + ". Tampoco se logro establecer conexion a :" + url + "  "+ usuario+"/"+password);
				//throw new Exception("No se encontro el DS --> " + LookUpResourceSchema.DATA_SOURCE + ". Tampoco se logro establecer conexion a :" + url + "  "+ usuario+"/"+password);
			} catch(Error ee) {
				ee.printStackTrace();
				errors.add("globalErrors", new ActionError("mensaje",ee.getMessage()));
			}
		} catch(Error e) {
			e.printStackTrace();
			errors.add("globalErrors", new ActionError("mensaje",e.getMessage()));
		}

		SessionManager managerDB = new SessionManager(ds);
		loginBean = new LoginBean(managerDB);

		ResourceBundle mensajes = null;
		try {
			mensajes = ResourceBundle.getBundle(LookUpResourceSchema.APPLICATION_RESOURCE, 
												(Locale)session.getAttribute(Globals.LOCALE_KEY));
		} catch(Exception e) {
			session.setAttribute(Globals.LOCALE_KEY, new Locale("en", "SV")); /*En este caso guardamos la Locale de spanish*/
			session.setAttribute("idioma", "E");
			

			mensajes = ResourceBundle.getBundle(LookUpResourceSchema.APPLICATION_RESOURCE, 
												(Locale)session.getAttribute(Globals.LOCALE_KEY));
		}
		String optGrpName = "";
		String optName = "";
		
		UserForm user;
		List<UserForm> data;
		Iterator<UserForm> it;
		
		HashMap<String,String> opciones = new HashMap<String,String>();
		
		ArrayList<String> listMenu = new ArrayList<String>();
		
		String target = "fail";
		String visible;
		String GeneralPath = "";
		
		try {
			this.change(mapping, form, request, response);
			data = (List<UserForm>)loginBean.findUser(loginForm);
			
			if(data != null && !data.isEmpty()) {
				it = data.iterator();
				user = it.next();
				
				if(user.getPassword().equals(user.getPasswordScriptado(loginForm.getPassword()))) {

					// *** se buscan todas las opciones asociadas al usuario para agregarlas al menu de la aplicacion**
					List<Map> menu = new ArrayList();
					
					// ***  se obtinene los grupos de opciones ***
					List<String> groupOptionList = loginBean.findGroupOption(user.getRolId());
					
					for(Iterator itr = groupOptionList.iterator(); itr.hasNext(); ) {
						Map<String,Object> grupoOption = (Map<String,Object>)itr.next();
						try {
							optGrpName = mensajes.getString("menus." + ((String)grupoOption.get("nameGroup")).replaceAll(" ", "").trim()
															.toLowerCase());
						} catch(Exception e1) {
							optGrpName = (String)grupoOption.get("nameGroup");
						}
						
						// *** se obtienen las oopciones de los grupos ***
						List<String> optionList = (List<String>)loginBean.findOption(user.getRolId(), 
																	 (grupoOption.get("codeGroup")).toString());
						grupoOption.put("opciones", optionList);
						menu.add(grupoOption);
						
						for(Iterator itr2 = optionList.iterator(); itr2.hasNext(); ) {
							Map<String,String> option = (Map<String,String>)itr2.next();
							
							visible = String.valueOf(option.get("visible"));
							
							if(visible != null && visible.equals("Y")) {
								try {
									optName = mensajes.getString("submenus." + ((String)option.get("name")).replaceAll(" ", "").trim()
																 .toLowerCase());
								} catch(Exception e2) {
									optName = (String)option.get("name");
								}
							}
							url = (String)option.get("link");
							url = url.substring(0, url.indexOf(".do") + 3);
							opciones.put(url, url);
							optName = ""+optName;
						}

						//listMenu.add((String) grupoOption.get("nameGroup"));
						listMenu.add(optGrpName);

					}

					request.getSession().setAttribute("menuApp", menu);
					user.setOpciones(opciones);
					
					/*if("1".equals(user.getRoleCode())){
						user.setNoCountry(true);
					}else{
						user.setNoCountry(false);
					}/**/

					GeneralPath = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + LookUpResourceSchema.APP_NAME + "/";

					/** * se guarda en sesion los datos del usuario ,
						el datasource que se ocupara y los datos del menu */
					session.setAttribute("user", user);
					session.setAttribute("datasource", ds);
					session.setAttribute("menulist", listMenu);
					session.setAttribute("GeneralPath", GeneralPath);
                    
                    //ini - Agregados para control de Timeout de session
                    /*if(false){
						session.setAttribute("session_timeout", loginBean.findSessionTimeout());
	                    session.setAttribute("session_timeout_count", loginBean.findSessionTimeoutCount());
	                    //fin - Agregados para control de Timeout de session
                    }*/


					target = "success";

					String changePwd;
					if(target.equals("success")) {
						try {
							changePwd = loginBean.cambiarPwd(user);
							if(changePwd.equals("Y")) {
								target = "cambiarPwd";
							}
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				} else {
					errors.add("globalWarnings", new ActionError("login.message.password.novalido"));
				}
			} else {
				errors.add("globalWarnings", new ActionError("login.message.username.novalido"));
			}
		} catch(Exception e) {
			e.printStackTrace();
			errors.add("globalErrors", new ActionError("mensaje",e.getMessage()));
			
		} catch(Error e) {
			e.printStackTrace();
			errors.add("globalErrors", new ActionError("mensaje",e.getMessage()));
		} finally {
			managerDB.close();
			if(!errors.isEmpty()) {
				saveErrors(request, errors);
			}
		}

		return mapping.findForward(target);
	}
	
	
	public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response) throws Exception, ServletException {

		HttpSession session = request.getSession(true);
		Locale local = (Locale)session.getAttribute(Globals.LOCALE_KEY);
		String target = "fail";
		Enumeration<?> vars = session.getAttributeNames();
		while(vars.hasMoreElements()) {
			String var = ((String)vars.nextElement());
			session.removeAttribute(var);
		}
		/*session.removeAttribute("user");
			session.removeAttribute("datasource");			
			session.removeAttribute("menuAplicacion");
			session.removeAttribute("menulist");*/

		session.setAttribute(Globals.LOCALE_KEY, local); /*En este caso guardamos la Locale de ingles*/
		//session.invalidate();

		return mapping.findForward(target);
	}
	/*
	public void setAppName(String AppName) {
		this.AppName = AppName;
	}

	public String getAppName() {
		return AppName;
	}/**/

}
