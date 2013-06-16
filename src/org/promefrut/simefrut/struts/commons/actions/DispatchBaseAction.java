package org.promefrut.simefrut.struts.commons.actions;



import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;
import org.promefrut.simefrut.utils.CustomExceptions;
import org.promefrut.simefrut.utils.LookUpResourceSchema;
import org.promefrut.simefrut.utils.PostgreSQLConnection;

/**
 * @author Henry Willy Melara
 * Clase utilizada para base de los nuevos Actions, utilizando solamente el Dispatch y manejando las conexiones a la Base de datos externamente
 */
public abstract class DispatchBaseAction extends DispatchAction {
	public final String KEY_RESET = "reset";
	public final String KEY_CANCELAR = "cancel";
	public final String KEY_BACK = "back";
	public final String KEY_SUCCESS = "success";
	public final String KEY_FAIL = "fail";
	public ResourceBundle bundle;
	public UserForm user;
	public DataSource dtsource = null;
	
	private boolean FLAG_UPDATE = false;

	public abstract void Init(HttpServletRequest request) throws Exception, Error;

	/**
	 * @return the fLAG_UPDATE
	 */
	public boolean isFLAG_UPDATE() {
		return FLAG_UPDATE;
	}

	/**
	 * @param flag_update the fLAG_UPDATE to set
	 */
	public void setFLAG_UPDATE(boolean flag_update) {
		FLAG_UPDATE = flag_update;
	}
	
	public void setDataSource(HttpServletRequest request) throws Exception, Error{
	    //dtsource = (DataSource)request.getSession().getAttribute("datasource");
	    
	    String url = PostgreSQLConnection.getConexionUrl(), 
	    		database = PostgreSQLConnection.getDatabase();
	    
		String usuario = PostgreSQLConnection.getConexionUsuario(), 
				password = PostgreSQLConnection.getConexionPassword();
		
		if(dtsource ==null){
			try{
				Context ic = new InitialContext();
				Object obj = ic.lookup(LookUpResourceSchema.DATA_SOURCE); // Datasource Prod
				dtsource = (DataSource)obj; //getDataSource
				if(dtsource ==null){
					throw new Exception();
				}
			}catch(Exception e){
				try{
					System.out.println("No se encontro datasource en \"DispatchBaseAction.java\", Se creara nuevo datasource");
					//Si no logra crear la conexion con el datasource en el contexto. Intenta con la credencial asignada en properties
					Jdbc3PoolingDataSource dsSource = new Jdbc3PoolingDataSource();
					dsSource.setDataSourceName(LookUpResourceSchema.DATA_SOURCE_NAME);
					dsSource.setServerName(url);
					dsSource.setDatabaseName(database);
					dsSource.setPortNumber(5432);
					dsSource.setUser(usuario);
					dsSource.setPassword(password);
					dsSource.setMaxConnections(LookUpResourceSchema.DATA_SOURCE_MAX_CONNECTION);
					new InitialContext().rebind(LookUpResourceSchema.DATA_SOURCE, dsSource);
					
					Context ic = new InitialContext();
					Object obj = ic.lookup(LookUpResourceSchema.DATA_SOURCE); // Datasource Prod
					dtsource = (DataSource)obj; //getDataSource
					if(dtsource ==null){
						throw new Exception();
					}
				}catch(Exception ee){
					ee.printStackTrace();
					System.out.println("No se encontro el DS --> " + LookUpResourceSchema.DATA_SOURCE + ". Tampoco se logro establecer conexion a :" + url + "  "+ usuario+"/"+password);
					throw new Exception("No se encontro el DS --> " + LookUpResourceSchema.DATA_SOURCE + ". Tampoco se logro establecer conexion a :" + url + "  "+ usuario+"/"+password);
				}
			}
		}
	}

	public void LoadBundle(HttpServletRequest request) throws Exception, Error {
	    setDataSource(request);
	    try{
	    	
	    	this.bundle = ResourceBundle.getBundle(LookUpResourceSchema.APPLICATION_RESOURCE, 
											   (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY));
	    
	    	user = (UserForm)request.getSession().getAttribute("user");
	    }catch(Exception e){
	    	;
	    }
	}

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response) throws Exception, Error {
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		//HttpSession session = request.getSession(true);
		SessionManager managerDB = null;

		ActionForward forward = mapping.findForward(this.KEY_FAIL);

		try {
			this.Init(request);
			managerDB = new SessionManager(dtsource);
			forward = update(mapping, form, request, response, errors, messages, managerDB);
			managerDB.commit();
		} catch(Exception e) {
			try{
		        CustomExceptions ce = new CustomExceptions(request, e);
				if(ce.getMensaje() != null) {
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", ce.getMensaje()));
				} else {
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", e.getMessage()));
				    e.printStackTrace();
				}
		        find(mapping, form, request, response);
		    }catch(Exception ee){
		        System.out.println("*********** CUSTOM EXCEPTION ERROR **********");
		        ee.printStackTrace();
		        System.out.println("^^^^^^^^^^^ CUSTOM EXCEPTION ERROR ^^^^^^^^^^");
		        throw e;
		    }finally{
		        managerDB.rollback();
		    }
		} catch(Error e) {
			managerDB.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			managerDB.close();
		}

		if(!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		if(!messages.isEmpty()) {
			saveMessages(request, messages);
		}
		return forward;
	}
	
	public abstract ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
										 HttpServletResponse response, ActionErrors errors, ActionMessages messages, 
										 SessionManager managerDB) throws Exception, Error;
	
	

	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response) throws Exception, Error {
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		//HttpSession session = request.getSession(true);
		SessionManager managerDB = null;

		ActionForward forward = mapping.findForward(this.KEY_FAIL);

		try {
			this.Init(request);
		    managerDB = new SessionManager(dtsource);
			forward = insert(mapping, form, request, response, errors, messages, managerDB);
			managerDB.commit();
		} catch(Exception e) {
			try{
				CustomExceptions ce = new CustomExceptions(request, e);
				if(ce.getMensaje() != null) {
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", ce.getMensaje()));
				} else {
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", e.getMessage()));
				    e.printStackTrace();
				}
		        find(mapping, form, request, response);
		    }catch(Exception ee){
		        System.out.println("*********** CUSTOM EXCEPTION ERROR **********");
		        ee.printStackTrace();
		        System.out.println("^^^^^^^^^^^ CUSTOM EXCEPTION ERROR ^^^^^^^^^^");
		        throw e;
		    }finally{
		        managerDB.rollback();
		    }
		} catch(Error e) {
			managerDB.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			managerDB.close();
		}
		if(!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		if(!messages.isEmpty()) {
			saveMessages(request, messages);
		}
		return forward;
	}

	public abstract ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
										 HttpServletResponse response, ActionErrors errors, ActionMessages messages, 
										 SessionManager managerDB) throws Exception, Error;

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response) throws Exception, Error {
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		//HttpSession session = request.getSession(true);
		SessionManager managerDB = null;

		ActionForward forward = mapping.findForward(this.KEY_FAIL);

		try {
			this.Init(request);
		    managerDB = new SessionManager(dtsource);
			forward = delete(mapping, form, request, response, errors, messages, managerDB);
			managerDB.commit();
		} catch(Exception e) {
			try{
				CustomExceptions ce = new CustomExceptions(request, e);
				if(ce.getMensaje() != null) {
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", ce.getMensaje()));
				} else {
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", e.getMessage()));
				    e.printStackTrace();
				}
		        find(mapping, form, request, response);
		    }catch(Exception ee){
		        System.out.println("*********** CUSTOM EXCEPTION ERROR **********");
		        ee.printStackTrace();
		        System.out.println("^^^^^^^^^^^ CUSTOM EXCEPTION ERROR ^^^^^^^^^^");
		        throw e;
		    }finally{
		        managerDB.rollback();
		    }
		} catch(Error e) {
			managerDB.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			managerDB.close();
		}

		if(!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		if(!messages.isEmpty()) {
			saveMessages(request, messages);
		}
		return forward;
	}

	public abstract ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
										 HttpServletResponse response, ActionErrors errors, ActionMessages messages, 
										 SessionManager managerDB) throws Exception, Error;

	public ActionForward find(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
							  HttpServletResponse response) throws Exception, Error {
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		//HttpSession session = request.getSession(true);
		SessionManager managerDB = null;

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		ActionForward forward = mapping.findForward(this.KEY_SUCCESS);

		try {
			this.Init(request);
		    managerDB = new SessionManager(dtsource);
			forward = find(mapping, form, request, response, errors, messages, managerDB);
		} catch(Exception e) {
			try{
				CustomExceptions ce = new CustomExceptions(request, e);
				if(ce.getMensaje() != null) {
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", ce.getMensaje()));
				} else {
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", e.getMessage()));
				    e.printStackTrace();
				}
			}catch(Exception ee){
				System.out.println("*********** CUSTOM EXCEPTION ERROR **********");
				ee.printStackTrace();
			    System.out.println("^^^^^^^^^^^ CUSTOM EXCEPTION ERROR ^^^^^^^^^^");
				throw e;
			}finally{
				managerDB.rollback();
			}
		} catch(Error e) {
			managerDB.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			managerDB.close();
		}

		if(!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		if(!messages.isEmpty()) {
			saveMessages(request, messages);
		}
		return forward;
	}

	public abstract ActionForward find(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
									   HttpServletResponse response, ActionErrors errors, ActionMessages messages, 
									   SessionManager managerDB) throws Exception, Error;

	@SuppressWarnings("rawtypes")
	public String getErrors(HttpServletRequest request, boolean success) throws Exception, Error {

        // Were any error messages specified?
        ActionMessages errors = (ActionMessages) request.getAttribute(Globals.ERROR_KEY);

        // Render the error messages appropriately
        StringBuffer results = new StringBuffer();
        String message = null;
        
		Iterator reports = errors.get();
		
		while (reports.hasNext()) {
            ActionMessage report = (ActionMessage) reports.next();
            message = getError(report.getKey(),report.getValues());
                    
            if (message != null) {
                results.append(message);
            }
            
        }
        return getAjaxTemplate(success,results.toString());
    }
	
	private String getError(String key, Object[] values)throws Exception, Error{
		String msg = bundle.getString(key);
		
		if(msg==null){
			throw new Exception("No value for key \""+key+"\"");
		}
		
		if(values!=null){
			for(int i = 0; i<values.length; i++){
				Class<? extends Object> clase = values[i].getClass();
				String objClass = clase.getSimpleName();
				String value = new String();
				
				if("String".equalsIgnoreCase(objClass)){
					value = (String) values[i];
					
				}else if("Integer".equalsIgnoreCase(objClass)){
					value = String.valueOf((Integer) values[i]);
					
				}else if("Long".equalsIgnoreCase(objClass)){
					value = String.valueOf((Long) values[i]);
					
				}else if("Double".equalsIgnoreCase(objClass)){
					value = String.valueOf((Double) values[i]);
					
				}else if("BigDecimal".equalsIgnoreCase(objClass)){
					value = String.valueOf((BigDecimal) values[i]);
				}else{
					System.out.println("El objeto es de tipo: "+value);
					value = (String) values[i];
				}
				
				msg = msg.replaceAll("\\{"+i+"\\}", value.replaceAll("\\$", "\\\\\\$"))+"\n";
			}
		}
		return msg;
	}
	
	public String getAjaxTemplate(boolean success, String msg){
		String results = new String();
		if(msg==null){
			msg ="Null Exception";
		}
		results = "{\"success\":"+(success?"true":"false")+",\"msg\":\"	"+msg.replaceAll("\\\"", "\\\\\"").replaceAll("\n", "\\\\n").replaceAll("\r","")+"\"}";
		
		return results;
	}
	
	/**
	 * This function is used to extract the information using ajax. It's a generic one.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @throws Error
	 */
	public ActionForward load(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			  HttpServletResponse response) throws Exception, Error {
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		SessionManager managerDB = null;

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

		try {
			this.Init(request);
		    managerDB = new SessionManager(dtsource);
		    resultJSON = load(form, request, errors, messages, managerDB);
		} catch(Exception e) {
			try{
				CustomExceptions ce = new CustomExceptions(request, e);
				if(ce.getMensaje() != null) {
					resultJSON = getAjaxTemplate(false, ce.getMensaje());
				} else {
					resultJSON = getAjaxTemplate(false, e.getMessage());
				    e.printStackTrace();
				}
			}catch(Exception ee){
				resultJSON = getAjaxTemplate(false, ee.getMessage());
				System.out.println("*********** CUSTOM EXCEPTION ERROR **********");
				ee.printStackTrace();
			    System.out.println("^^^^^^^^^^^ CUSTOM EXCEPTION ERROR ^^^^^^^^^^");
				throw e;
			}finally{
				managerDB.rollback();
			}
		} catch(Error e) {
			managerDB.rollback();
			resultJSON = getAjaxTemplate(false, e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			managerDB.close();
			PrintWriter write = null;
			
			try{
              response.setHeader("Cache-Control", "none");
              response.setHeader("Pragma", "no-cache");
              response.setCharacterEncoding("ISO-8859-1");
              response.setContentType("text/html;charset=ISO-8859-1");
              write = response.getWriter();
              write.write(resultJSON);
              
			}catch(Exception e){
				e.printStackTrace();
			}catch(Error e){
				e.printStackTrace();
			}finally{
				if(write!=null){
					write.flush();
					write.close();
				}
			}
		}		
		return null;
	}

	public abstract String load(ActionForm form, HttpServletRequest request,
				ActionErrors errors, ActionMessages messages, SessionManager managerDB) throws Exception, Error;
	
	public ActionForward ajaxInsert(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			  HttpServletResponse response) throws Exception, Error {
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		SessionManager managerDB = null;

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();
		boolean success = true;

		try {
			this.Init(request);
		    managerDB = new SessionManager(dtsource);
		    success = ajaxInsert(form, request, errors, messages, managerDB);
		    saveErrors(request, errors);
		    resultJSON = getErrors(request, success);
		} catch(Exception e) {
			try{
				CustomExceptions ce = new CustomExceptions(request, e);
				if(ce.getMensaje() != null) {
					resultJSON = getAjaxTemplate(false,ce.getMensaje());
				} else {
					resultJSON = getAjaxTemplate(false, e.getMessage());
				    e.printStackTrace();
				}
			}catch(Exception ee){
				resultJSON = getAjaxTemplate(false,ee.getMessage());
				System.out.println("*********** CUSTOM EXCEPTION ERROR **********");
				ee.printStackTrace();
			    System.out.println("^^^^^^^^^^^ CUSTOM EXCEPTION ERROR ^^^^^^^^^^");
				throw e;
			}finally{
				managerDB.rollback();
			}
		} catch(Error e) {
			managerDB.rollback();
			resultJSON = getAjaxTemplate(false, e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			managerDB.close();
			PrintWriter write = null;
			
			try{
              response.setHeader("Cache-Control", "none");
              response.setHeader("Pragma", "no-cache");
              response.setCharacterEncoding("ISO-8859-1");
              response.setContentType("text/html;charset=ISO-8859-1");
              write = response.getWriter();
              write.write(resultJSON);
              
			}catch(Exception e){
				e.printStackTrace();
			}catch(Error e){
				e.printStackTrace();
			}finally{
				if(write!=null){
					write.flush();
					write.close();
				}
			}
		}		
		return null;
	}
	
	public abstract boolean ajaxInsert(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages, SessionManager managerDB) throws Exception, Error;
	
	public ActionForward ajaxUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			  HttpServletResponse response) throws Exception, Error {
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		SessionManager managerDB = null;

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();
		boolean success = true;

		try {
			this.Init(request);
		    managerDB = new SessionManager(dtsource);
		    success = ajaxUpdate(form, request, errors, messages, managerDB);
		    saveErrors(request, errors);
		    resultJSON = getErrors(request, success);
		} catch(Exception e) {
			try{
				CustomExceptions ce = new CustomExceptions(request, e);
				if(ce.getMensaje() != null) {
					resultJSON = getAjaxTemplate(false,ce.getMensaje());
				} else {
					resultJSON = getAjaxTemplate(false, e.getMessage());
				    e.printStackTrace();
				}
			}catch(Exception ee){
				resultJSON = getAjaxTemplate(false,ee.getMessage());
				System.out.println("*********** CUSTOM EXCEPTION ERROR **********");
				ee.printStackTrace();
			    System.out.println("^^^^^^^^^^^ CUSTOM EXCEPTION ERROR ^^^^^^^^^^");
				throw e;
			}finally{
				managerDB.rollback();
			}
		} catch(Error e) {
			managerDB.rollback();
			resultJSON = getAjaxTemplate(false, e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			managerDB.close();
			PrintWriter write = null;
			
			try{
            response.setHeader("Cache-Control", "none");
            response.setHeader("Pragma", "no-cache");
            response.setCharacterEncoding("ISO-8859-1");
            response.setContentType("text/html;charset=ISO-8859-1");
            write = response.getWriter();
            write.write(resultJSON);
            
			}catch(Exception e){
				e.printStackTrace();
			}catch(Error e){
				e.printStackTrace();
			}finally{
				if(write!=null){
					write.flush();
					write.close();
				}
			}
		}		
		return null;
	}
	
	public abstract boolean ajaxUpdate(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages, SessionManager managerDB) throws Exception, Error;
	
	public ActionForward ajaxDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			  HttpServletResponse response) throws Exception, Error {
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		SessionManager managerDB = null;

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();
		boolean success = true;

		try {
			this.Init(request);
		    managerDB = new SessionManager(dtsource);
		    success = ajaxDelete(form, request, errors, messages, managerDB);
		    saveErrors(request, errors);
		    resultJSON = getErrors(request, success);
		} catch(Exception e) {
			try{
				CustomExceptions ce = new CustomExceptions(request, e);
				if(ce.getMensaje() != null) {
					resultJSON = getAjaxTemplate(false,ce.getMensaje());
				} else {
					resultJSON = getAjaxTemplate(false, e.getMessage());
				    e.printStackTrace();
				}
			}catch(Exception ee){
				resultJSON = getAjaxTemplate(false,ee.getMessage());
				System.out.println("*********** CUSTOM EXCEPTION ERROR **********");
				ee.printStackTrace();
			    System.out.println("^^^^^^^^^^^ CUSTOM EXCEPTION ERROR ^^^^^^^^^^");
				throw e;
			}finally{
				managerDB.rollback();
			}
		} catch(Error e) {
			managerDB.rollback();
			resultJSON = getAjaxTemplate(false, e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			managerDB.close();
			PrintWriter write = null;
			
			try{
          response.setHeader("Cache-Control", "none");
          response.setHeader("Pragma", "no-cache");
          response.setCharacterEncoding("ISO-8859-1");
          response.setContentType("text/html;charset=ISO-8859-1");
          write = response.getWriter();
          write.write(resultJSON);
          
			}catch(Exception e){
				e.printStackTrace();
			}catch(Error e){
				e.printStackTrace();
			}finally{
				if(write!=null){
					write.flush();
					write.close();
				}
			}
		}		
		return null;
	}
	
	public abstract boolean ajaxDelete(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages, SessionManager managerDB) throws Exception, Error;
	
	public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response) throws Exception, Error {
		return mapping.findForward(KEY_CANCELAR);
	}

	public ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
							   HttpServletResponse response) throws Exception, Error {
		form.reset(mapping, request);
		return mapping.findForward(KEY_RESET);
	}

	public ActionForward back(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
							  HttpServletResponse response) throws Exception, Error {
		//form.reset(mapping,request);
		return mapping.findForward(KEY_BACK);
	}
	
	

}
