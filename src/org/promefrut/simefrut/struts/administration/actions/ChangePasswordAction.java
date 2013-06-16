package org.promefrut.simefrut.struts.administration.actions;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.promefrut.simefrut.struts.administration.beans.UserBean;
import org.promefrut.simefrut.struts.administration.forms.ChangePasswordForm;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.commons.actions.BaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.utils.CustomExceptions;

/** 
 * MyEclipse Struts
 * Creation date: 04-02-2008
 * 
 * XDoclet definition:
 * @struts.action path="/changePasswordAction" name="changePasswordForm" input="/jsp/administracion/changePassword.jsp" parameter="accion" scope="request"
 * @struts.action-forward name="success" path="/jsp/administracion/changePassword.jsp"
 */
public class ChangePasswordAction extends BaseAction {

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, 
								ActionErrors errors, ActionMessages messages) throws Exception, ServletException {
		return null;
	}

	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, 
								ActionErrors errors, ActionMessages messages) throws Exception, ServletException {
		return null;
	}

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, 
								ActionErrors errors, ActionMessages messages) throws Exception, ServletException {
		HttpSession session = request.getSession(true);
		SessionManager managerOracle = new SessionManager((DataSource)session.getAttribute("datasource"));
		ChangePasswordForm changePasswordForm = (ChangePasswordForm)form;
		UserBean userBean = new UserBean(managerOracle);
		UserForm userForm = (UserForm)session.getAttribute("user");
		String target = KEY_SUCCESS;
		UserForm user;
		try {
			changePasswordForm.validate(mapping, request, errors);
			if(errors.isEmpty()) {
				user = (UserForm)session.getAttribute("user");
				userForm.setPassword(changePasswordForm.getPasswordNew());
				userForm.setFlgGeneratePsw("true");
				userBean.updatePassword(userForm, user.getUsername());
				managerOracle.commit();
				errors.add("changePassword.mansaje.exito", new ActionError("changePassword.mansaje.exito"));
				session.removeAttribute("user");
				session.removeAttribute("datasource");
			}
		} catch(Exception e) {
			CustomExceptions ce = new CustomExceptions(request, e);
			if(ce.getMensaje() != null) {
				errors.add("changePassword.mansaje.exito", new ActionError("mensaje", ce.getMensaje()));
			} else {
				System.out.println(ce.getMensaje());
				errors.add("changePassword.mansaje.exito", new ActionError("mensaje", ce.getMensaje()));
			}
			e.printStackTrace();
			managerOracle.rollback();
		} finally {
			changePasswordForm.reset(mapping, request);
			managerOracle.close();
		}
		return mapping.findForward(target);
	}

	public ActionForward find(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
							  HttpServletResponse response) {
		form.reset(mapping, request);
		return mapping.findForward(KEY_SUCCESS);
	}
}
