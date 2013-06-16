package org.promefrut.simefrut.struts.catalogs.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.catalogs.beans.CountryFAOSTATBean;
import org.promefrut.simefrut.struts.catalogs.forms.CountryFAOSTATForm;
import org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;



/**
 * @author HWM
 *
 */
public class CountryFAOSTATAction extends DispatchBaseAction {
	public String title = new String();
	

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#Init(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		super.LoadBundle(request);
		title = super.bundle.getString("countryFAOSTAT.label.text");
		title = "" + title;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#update(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
		
		CountryFAOSTATForm countryFAOSTATForm = (CountryFAOSTATForm)form;
		
		if(!isFLAG_UPDATE()) {
			countryFAOSTATForm.reset(mapping, request);
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

		CountryFAOSTATBean countryFAOSTATBean = new CountryFAOSTATBean(managerDB, bundle);
		String ctrId = (String)request.getParameter("ctrId");
		
		resultJSON= countryFAOSTATBean.find(ctrId);
		
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
		CountryFAOSTATForm countryFAOSTATForm = (CountryFAOSTATForm)form;
		CountryFAOSTATBean countryFAOSTATBean = new CountryFAOSTATBean(managerDB, bundle);
		UserForm user;

		boolean success = true;
		countryFAOSTATForm.validateInsert(null, request, errors);
		String anotherCountry = countryFAOSTATBean.existsData(countryFAOSTATForm);
		
		if(errors.isEmpty()) {
			if(!StringUtils.isBlank(anotherCountry)){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("countryFAOSTAT.faoctrId.exists", anotherCountry));
			}
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			countryFAOSTATBean.insert(countryFAOSTATForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.insert", title, countryFAOSTATForm.getFaoctrDesc()));
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
		CountryFAOSTATForm countryFAOSTATForm = (CountryFAOSTATForm)form;
		CountryFAOSTATBean countryFAOSTATBean = new CountryFAOSTATBean(managerDB, bundle);

		UserForm user;
		
		boolean success = true;
		countryFAOSTATForm.validateUpdate(null, request, errors);
		
		String anotherCountry = countryFAOSTATBean.existsData(countryFAOSTATForm);
		
		if(errors.isEmpty()) {
			if(!StringUtils.isBlank(anotherCountry)){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("countryFAOSTAT.faoctrId.exists", anotherCountry));
			}
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			countryFAOSTATBean.update(countryFAOSTATForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.update", title, countryFAOSTATForm.getFaoctrDesc()));
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

		HttpSession session = request.getSession(true);
		CountryFAOSTATForm countryFAOSTATForm = (CountryFAOSTATForm)form;
		CountryFAOSTATBean countryFAOSTATBean = new CountryFAOSTATBean(managerDB, bundle);

		UserForm user;
		
		boolean success = true;
		countryFAOSTATForm.validateDelete(null, request, errors);
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			countryFAOSTATBean.delete(countryFAOSTATForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.delete", title, countryFAOSTATForm.getFaoctrDesc()));
		} else {
			success = false;
		}
		saveErrors(request, errors);
		
		return success;
	}
}
