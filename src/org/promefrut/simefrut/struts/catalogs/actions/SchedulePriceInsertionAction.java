package org.promefrut.simefrut.struts.catalogs.actions;

import java.util.Map;

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
import org.promefrut.simefrut.struts.catalogs.beans.SchedulePriceInsertionBean;
import org.promefrut.simefrut.struts.catalogs.forms.SchedulePriceInsertionForm;
import org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;



/**
 * @author HWM
 *
 */
public class SchedulePriceInsertionAction extends DispatchBaseAction {
	public String title = new String();
	

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#Init(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		super.LoadBundle(request);
		title = super.bundle.getString("schedulePriceInsertion.label.text");
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
		
		return mapping.findForward(KEY_SUCCESS);
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#insert(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		
		return mapping.findForward(KEY_SUCCESS);
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
		
		SchedulePriceInsertionForm schedulePriceInsertionForm = (SchedulePriceInsertionForm)form;

		SchedulePriceInsertionBean tradUnitBean = new SchedulePriceInsertionBean(managerDB, bundle);
		
		request.setAttribute("countriesCollection", tradUnitBean.getCountriesCollection(super.user));
		
		if(!isFLAG_UPDATE()) {
			schedulePriceInsertionForm.reset(mapping, request);
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

		//SchedulePriceInsertionForm schedulePriceInsertionForm = (SchedulePriceInsertionForm)form;
		SchedulePriceInsertionBean schedulePriceInsertionBean = new SchedulePriceInsertionBean(managerDB, bundle);
		
		Integer tmpValor = new Integer(0);
		
		//If the user can see all countries
		if(user.getNoCountry()){
			tmpValor = new Integer(-1);
		}else{
			tmpValor = new Integer(user.getCtrId());
		}
		
		resultJSON= schedulePriceInsertionBean.find(tmpValor);
		
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
		SchedulePriceInsertionForm schedulePriceInsertionForm = (SchedulePriceInsertionForm)form;
		SchedulePriceInsertionBean schedulePriceInsertionBean = new SchedulePriceInsertionBean(managerDB, bundle);
		
		UserForm user;
		boolean success = true;
		
		schedulePriceInsertionForm.validateInsert(null, request, errors);
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			Map<String, Object> tmpMap = schedulePriceInsertionBean.countRegisters(schedulePriceInsertionForm);
			
			if( tmpMap.size()==0){
				schedulePriceInsertionBean.insert(schedulePriceInsertionForm, user.getUsername());
				managerDB.commit();
				errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.insert", title, schedulePriceInsertionForm.getCtrDesc()));
				schedulePriceInsertionForm.reset(null, request);
			}else{
				success = false;
				String tmpMsg = new String();
				
				tmpMsg+= bundle.getString("schedulePriceInsertion.ctrId")+": \""+schedulePriceInsertionForm.getCtrDesc()+"\"<br/>";
				
				errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("schedulePriceInsertion.dataAlreadyExists", tmpMsg));
			}
		} else {
			success = false;
		}
		
		return success;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxUpdate(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxUpdate(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		
		boolean success = true;
		
		HttpSession session = request.getSession(true);
		SchedulePriceInsertionForm schedulePriceInsertionForm = (SchedulePriceInsertionForm)form;
		SchedulePriceInsertionBean schedulePriceInsertionBean = new SchedulePriceInsertionBean(managerDB, bundle);

		UserForm user;
		schedulePriceInsertionForm.validateUpdate(null, request, errors);

		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			Map<String, Object> tmpMap = schedulePriceInsertionBean.countRegisters(schedulePriceInsertionForm);
			
			if( tmpMap.size()==0){
				schedulePriceInsertionBean.update(schedulePriceInsertionForm, user.getUsername());
				schedulePriceInsertionBean.insert(schedulePriceInsertionForm, user.getUsername());
				managerDB.commit();
				errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.update", title, schedulePriceInsertionForm.getCtrDesc()));
				schedulePriceInsertionForm.reset(null, request);
			}else{
				success = false;
				String tmpMsg = new String();
				
				tmpMsg+= bundle.getString("schedulePriceInsertion.ctrId")+": \""+schedulePriceInsertionForm.getCtrDesc()+"\"<br/>";
				
				errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("schedulePriceInsertion.dataAlreadyExists", tmpMsg));
			}
		} else {
			success=false;
		}
		return success;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxDelete(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxDelete(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		boolean success = true;
		
		HttpSession session = request.getSession(true);
		SchedulePriceInsertionForm schedulePriceInsertionForm = (SchedulePriceInsertionForm)form;
		SchedulePriceInsertionBean schedulePriceInsertionBean = new SchedulePriceInsertionBean(managerDB, bundle);

		schedulePriceInsertionForm.validateDelete(null, request, errors);

		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			
			schedulePriceInsertionBean.update(schedulePriceInsertionForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.delete", title, schedulePriceInsertionForm.getCtrDesc()));
			schedulePriceInsertionForm.reset(null, request);
		} else {
			success=false;
		}
		return success;
	}
}