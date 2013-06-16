package org.promefrut.simefrut.struts.maintenances.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;
import org.promefrut.simefrut.struts.maintenances.beans.CommerceBean;
import org.promefrut.simefrut.struts.maintenances.forms.CommerceForm;



/**
 * @author HWM
 *
 */
public class CommerceAction extends DispatchBaseAction {
	public String title = new String();
	

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#Init(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		super.LoadBundle(request);
		title = super.bundle.getString("comm.label.text");
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
		
		CommerceForm commerceForm = (CommerceForm)form;
		CommerceBean priceBean = new CommerceBean(managerDB, bundle);
		
		request.setAttribute("productsCollection", priceBean.getProductsCollection());
		request.setAttribute("countriesCollection", priceBean.getCountriesCollection(super.user));
		request.setAttribute("oriCountriesCollection", priceBean.getOriContriesCollection());
		
		if(!isFLAG_UPDATE()) {
			commerceForm.reset(mapping, request);
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

		CommerceForm commerceForm = (CommerceForm)form;
		CommerceBean priceBean = new CommerceBean(managerDB, bundle);
		
		resultJSON= priceBean.find(commerceForm.getCtrSk(), commerceForm.getYearSk());
		
		return resultJSON;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxInsert(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxInsert(ActionForm form, HttpServletRequest request,ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		
		HttpSession session = request.getSession(true);
		CommerceForm commerceForm = (CommerceForm)form;
		CommerceBean priceBean = new CommerceBean(managerDB, bundle);
		UserForm user;
		
		boolean success = true;

		commerceForm.validateInsert(null, request, errors);
		
		if(errors.isEmpty()) {
			this.validateDimensionSk(commerceForm, priceBean, errors);
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			priceBean.insert(commerceForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.insert", title, commerceForm.getYearSk()));
		}else{
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
		CommerceForm commerceForm = (CommerceForm)form;
		CommerceBean priceBean = new CommerceBean(managerDB, bundle);
		UserForm user;
		
		boolean success = true;

		commerceForm.validateUpdate(null, request, errors);

		if(errors.isEmpty()) {
			this.validateDimensionSk(commerceForm, priceBean, errors);
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			priceBean.update(commerceForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.update", title, commerceForm.getYearSk()));
		}else{
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
		CommerceForm commerceForm = (CommerceForm)form;
		CommerceBean priceBean = new CommerceBean(managerDB, bundle);
		UserForm user;
		
		boolean success = true;

		commerceForm.validateDelete(null, request, errors);
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			priceBean.delete(commerceForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.delete", title, commerceForm.getYearSk()));
		}else{
			success = false;
		}
		
		saveErrors(request, errors);
		return success;
	}
	
	
	private void validateDimensionSk(CommerceForm commerceForm, CommerceBean priceBean, ActionErrors errors)throws Exception, Error{
		if(errors.isEmpty()){
			if(priceBean.isDuplicated(commerceForm)){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("comm.duplicate.register"));
			}
		}
	}
}
