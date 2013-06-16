package org.promefrut.simefrut.struts.administration.actions;


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
import org.promefrut.simefrut.struts.administration.forms.ChangePwdForm;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;

/** 
 * 
 * XDoclet definition:
 * @struts.action path="/changePasswordAction" name="changePasswordForm" input="/jsp/administracion/changePassword.jsp" parameter="accion" scope="request"
 * @struts.action-forward name="success" path="/jsp/administracion/changePassword.jsp"
 */
public class ChangePwdAction extends DispatchBaseAction {
	
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		LoadBundle(request);
	}

	@Override
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerOracle) throws Exception, Error {
		
		HttpSession session = request.getSession(true);
		SessionManager managerDB = new SessionManager((DataSource)session.getAttribute("datasource"));
		ChangePwdForm changePasswordForm = (ChangePwdForm)form;
		UserBean userBean = new UserBean(managerDB);
		
		UserForm userForm = (UserForm)session.getAttribute("user");
		String target = KEY_SUCCESS;

		try {
			changePasswordForm.validate(mapping, request, errors);
			
			if(errors.isEmpty()) {
				userForm.setPassword(changePasswordForm.getPasswordNew());
				userForm.setFlgGeneratePsw("true");
				userBean.updatePassword(userForm, userForm.getUsername());
				
				managerDB.commit();
				
				errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("changePassword.message.success"));
				changePasswordForm.reset(mapping, request);
				
				session.removeAttribute("user");
				session.removeAttribute("datasource");
				
			} else {
				target = "fail";
			}
		} catch(Exception e) {
			e.printStackTrace();
			managerDB.rollback();

		} finally {
			managerDB.close();
		}
		return mapping.findForward(target);
	}

	@Override
	public ActionForward insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerOracle) throws Exception, Error {
		
		return null;
	}

	@Override
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerOracle) throws Exception, Error {
		
		return null;
	}

	@Override
	public ActionForward find(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerOracle) throws Exception, Error {
		
		form.reset(mapping, request);
		return mapping.findForward(KEY_FAIL);
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
