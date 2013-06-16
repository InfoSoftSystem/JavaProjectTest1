package org.promefrut.simefrut.struts.administration.actions;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.promefrut.simefrut.struts.administration.beans.OptionGroupBean;
import org.promefrut.simefrut.struts.administration.forms.OptionGroupForm;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;

/** 
 * @author HWM
 * 
 * XDoclet definition:
 * @struts.action path="/optionGroupAction" name="optionGroupForm" input="/jsp/optionGroup.jsp" parameter="accion" scope="request" validate="true"
 * @struts.action-forward name="success" path="/jsp/optionGroup.jsp"
 */
public class OptionGroupAction extends DispatchBaseAction {
	public String title = new String();
	
	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#Init(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		super.LoadBundle(request);
		title = super.bundle.getString("optionGroup.label.text");
		title = "" + title;
		
	}


	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#update(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#insert(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#delete(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#find(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward find(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		
		OptionGroupForm optionGroupForm = (OptionGroupForm)form;
		
		if(!isFLAG_UPDATE()) {
			optionGroupForm.reset(mapping, request);
		} else {
			setFLAG_UPDATE(false);
		}
		return mapping.findForward(KEY_SUCCESS);
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public String load(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		String resultJSON = new String();

		//OptionGroupForm countryForm = (OptionGroupForm)form;
		OptionGroupBean optionGroupBean = new OptionGroupBean(managerDB, bundle);
		
		resultJSON= optionGroupBean.find();
		
		return resultJSON;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxInsert(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxInsert(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		
		HttpSession session = request.getSession(true);
		OptionGroupForm optionGroupForm = (OptionGroupForm)form;
		OptionGroupBean optionGroupBean = new OptionGroupBean(managerDB, bundle);
		
		UserForm user;
		
		boolean success = true;

		optionGroupForm.validateInsert(null, request, errors);
		
		if(optionGroupBean.validarGroup(optionGroupForm) > 0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("optionGroup.grpId.exists"));
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			optionGroupBean.insert(optionGroupForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.insert", title, optionGroupForm.getGrpDesc()));
		} else {
			success = false;
		}
		
		saveErrors(request, errors);
		
		return success;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxUpdate(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxUpdate(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {

		HttpSession session = request.getSession(true);
		OptionGroupForm optionGroupForm = (OptionGroupForm)form;
		OptionGroupBean optionGroupBean = new OptionGroupBean(managerDB, bundle);
		boolean success = true;
		
		UserForm user;
		optionGroupForm.validateUpdate(null, request, errors);

		if(optionGroupBean.validarGroup(optionGroupForm) > 0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("optionGroup.grpId.exists"));
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			optionGroupBean.update(optionGroupForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.update", title, optionGroupForm.getGrpDesc()));
		} else {
			success = false;
		}
		
		saveErrors(request, errors);
		
		return success;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxDelete(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxDelete(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {

		OptionGroupForm optionGroupForm = (OptionGroupForm)form;
		OptionGroupBean optionGroupBean = new OptionGroupBean(managerDB, bundle);
		boolean success = true;
		
		optionGroupForm.validateDelete(null, request, errors);

		if(errors.isEmpty()) {
			optionGroupBean.delete(optionGroupForm);
			
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.delete", title, optionGroupForm.getGrpDesc()));
		} else {
			success = false;
		}
		
		saveErrors(request, errors);
		
		return success;
	}

}
