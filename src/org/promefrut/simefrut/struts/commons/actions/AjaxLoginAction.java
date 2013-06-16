package org.promefrut.simefrut.struts.commons.actions;




import java.io.PrintWriter;

import java.util.Iterator;
import java.util.List;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.commons.beans.LoginBean;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.LoginForm;
import org.promefrut.simefrut.utils.LookUpResourceSchema;

public class AjaxLoginAction extends DispatchBaseAction {
    public AjaxLoginAction() {
    }

    public void Init(HttpServletRequest request) throws Exception, Error {
        super.LoadBundle(request);
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, 
                                HttpServletRequest request, 
                                HttpServletResponse response, 
                                ActionErrors errors, ActionMessages messages, 
                                SessionManager managerOracle) {
        return null;
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, 
                                HttpServletRequest request, 
                                HttpServletResponse response, 
                                ActionErrors errors, ActionMessages messages, 
                                SessionManager managerOracle) {
        return null;
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, 
                                HttpServletRequest request, 
                                HttpServletResponse response, 
                                ActionErrors errors, ActionMessages messages, 
                                SessionManager managerOracle) {
        return null;
    }

    public ActionForward find(ActionMapping mapping, ActionForm form, 
                              HttpServletRequest request, 
                              HttpServletResponse response, 
                              ActionErrors errors, ActionMessages messages, 
                              SessionManager managerOracle) {
        return null;
    }
    
    @SuppressWarnings("rawtypes")
	public ActionForward ajaxLogin(ActionMapping mapping, ActionForm form, 
                              HttpServletRequest request, 
                              HttpServletResponse response) throws Exception, Error{
        this.Init(request);
        SessionManager managerOracle = new SessionManager(dtsource);
        LoginBean loginBean = new LoginBean(managerOracle);
        LoginForm loginForm = (LoginForm)form;
        List data = null;
        Iterator it;
        ResourceBundle mensajes = ResourceBundle.getBundle(LookUpResourceSchema.APPLICATION_RESOURCE,(Locale) request.getSession().getAttribute(Globals.LOCALE_KEY));
        UserForm user = null;
        String xmlResponse = "";
        try{
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            PrintWriter writer = response.getWriter();
            data = loginBean.findUser(loginForm);
            
            if(data != null && !data.isEmpty()) {
                it = data.iterator();
                user = (UserForm)it.next();
                
                if(user.getPassword().equals(user.getPasswordScriptado(loginForm.getPassword()))) {
                    xmlResponse = "<xmlResponse>";
                    xmlResponse += "<code>OK</code>";
                    xmlResponse += "<message>"+mensajes.getString("login.message.logeo.valido")+"</message>";
                    xmlResponse += "</xmlResponse>";
                    writer.write(xmlResponse); 
                    
                }else{
                    xmlResponse = "<xmlResponse>";
                    xmlResponse += "<code>NO</code>";
                    xmlResponse += "<message>"+mensajes.getString("login.message.password.novalido")+"</message>";
                    xmlResponse += "</xmlResponse>";
                    writer.write(xmlResponse); 
                }
                
            }else{
                xmlResponse = "<xmlResponse>";
                xmlResponse += "<code>NO</code>";
                xmlResponse += "<message>"+mensajes.getString("login.message.username.novalido")+"</message>";
                xmlResponse += "</xmlResponse>";
                writer.write(xmlResponse); 
            }
            
            writer.flush();
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
            managerOracle.rollback();
        }finally{
            managerOracle.close();
        }
        
        return null;
    }

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public String load(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxInsert(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxInsert(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxUpdate(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxUpdate(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxDelete(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxDelete(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return false;
	}
    
}
