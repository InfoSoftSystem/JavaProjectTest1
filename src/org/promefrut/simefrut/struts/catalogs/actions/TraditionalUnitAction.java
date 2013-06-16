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
import org.promefrut.simefrut.struts.catalogs.beans.TraditionalUnitBean;
import org.promefrut.simefrut.struts.catalogs.forms.TraditionalUnitForm;
import org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;



/**
 * @author HWM
 *
 */
public class TraditionalUnitAction extends DispatchBaseAction {
	public String title = new String();
	

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#Init(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		super.LoadBundle(request);
		title = super.bundle.getString("tunit.label.text");
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
		
		HttpSession session = request.getSession(true);
		TraditionalUnitForm traditionalUnitForm = (TraditionalUnitForm)form;
		TraditionalUnitBean traditionalUnitBean = new TraditionalUnitBean(managerDB, bundle);

		UserForm user;
		String target = KEY_SUCCESS;
		traditionalUnitForm.validateUpdate(mapping, request, errors);

		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			traditionalUnitBean.update(traditionalUnitForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.update", title, traditionalUnitForm.getTunitDesc()));
			traditionalUnitForm.reset(mapping, request);
		} else {
			target = KEY_FAIL;
			setFLAG_UPDATE(true);
		}
		find(mapping, form, request, response);
		return mapping.findForward(target);
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#insert(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		
		HttpSession session = request.getSession(true);
		TraditionalUnitForm traditionalUnitForm = (TraditionalUnitForm)form;
		TraditionalUnitBean traditionalUnitBean = new TraditionalUnitBean(managerDB, bundle);
		UserForm user;

		traditionalUnitForm.validateInsert(mapping, request, errors);
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			Map<String, Object> tmpMap = traditionalUnitBean.countRegisters(traditionalUnitForm);
			
			if( tmpMap.size()==0){
				traditionalUnitBean.insert(traditionalUnitForm, user.getUsername());
				managerDB.commit();
				errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.insert", title, traditionalUnitForm.getTunitDesc()));
				traditionalUnitForm.reset(mapping, request);
			}else{
				String tmpMsg = new String();
				
				tmpMsg+= bundle.getString("tunit.tunitDesc")+": \""+tmpMap.get("tunitDesc")+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.prodId")+": \""+tmpMap.get("prodDesc")+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.ptypeId")+": \""+tmpMap.get("ptypeDesc")+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.varId")+": \""+ (tmpMap.get("varDesc")==null?"":tmpMap.get("varDesc"))+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.sizeId")+": \""+tmpMap.get("sizeDesc")+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.tunitKilo")+": \""+tmpMap.get("tunitKilo")+"\"<br/>";
				
				errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.dataAlreadyExists", tmpMap.get("tunitCode"), tmpMsg));
			}
		} else {
			setFLAG_UPDATE(true);
		}
		
		find(mapping, form, request, response);
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
		
		TraditionalUnitForm traditionalUnitForm = (TraditionalUnitForm)form;

		TraditionalUnitBean tradUnitBean = new TraditionalUnitBean(managerDB, bundle);
		
		request.setAttribute("varietiesCollection", tradUnitBean.getVarietiesCollection());
		request.setAttribute("productsCollection", tradUnitBean.getProductsCollection());
		request.setAttribute("countriesCollection", tradUnitBean.getCountriesCollection(super.user));
		request.setAttribute("sizesCollection", tradUnitBean.getSizesCollection());
		request.setAttribute("productTypesCollection", tradUnitBean.getProductTypesCollection());
		
		if(!isFLAG_UPDATE()) {
			traditionalUnitForm.reset(mapping, request);
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

		//TraditionalUnitForm traditionalUnitForm = (TraditionalUnitForm)form;
		TraditionalUnitBean traditionalUnitBean = new TraditionalUnitBean(managerDB, bundle);
		
		Integer tmpValor = new Integer(0);
		
		//If the user can see all countries
		if(user.getNoCountry()){
			tmpValor = new Integer(-1);
		}else{
			tmpValor = new Integer(user.getCtrId());
		}
		
		resultJSON= traditionalUnitBean.find(tmpValor);
		
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
		TraditionalUnitForm traditionalUnitForm = (TraditionalUnitForm)form;
		TraditionalUnitBean traditionalUnitBean = new TraditionalUnitBean(managerDB, bundle);
		
		UserForm user;
		boolean success = true;
		
		traditionalUnitForm.validateInsert(null, request, errors);
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			Map<String, Object> tmpMap = traditionalUnitBean.countRegisters(traditionalUnitForm);
			
			if( tmpMap.size()==0){
				traditionalUnitBean.insert(traditionalUnitForm, user.getUsername());
				managerDB.commit();
				errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.insert", title, traditionalUnitForm.getTunitDesc()));
				traditionalUnitForm.reset(null, request);
			}else{
				success = false;
				String tmpMsg = new String();
				
				tmpMsg+= bundle.getString("tunit.tunitDesc")+": \""+tmpMap.get("tunitDesc")+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.prodId")+": \""+tmpMap.get("prodDesc")+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.ptypeId")+": \""+tmpMap.get("ptypeDesc")+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.varId")+": \""+ (tmpMap.get("varDesc")==null?"":tmpMap.get("varDesc"))+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.sizeId")+": \""+tmpMap.get("sizeDesc")+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.tunitKilo")+": \""+tmpMap.get("tunitKilo")+"\"<br/>";
				
				errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.dataAlreadyExists", tmpMap.get("tunitCode"), tmpMsg));
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
		TraditionalUnitForm traditionalUnitForm = (TraditionalUnitForm)form;
		TraditionalUnitBean traditionalUnitBean = new TraditionalUnitBean(managerDB, bundle);

		UserForm user;
		traditionalUnitForm.validateUpdate(null, request, errors);

		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			Map<String, Object> tmpMap = traditionalUnitBean.countRegisters(traditionalUnitForm);
			
			if( tmpMap.size()==0){
				traditionalUnitBean.update(traditionalUnitForm, user.getUsername());
				managerDB.commit();
				errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.update", title, traditionalUnitForm.getTunitDesc()));
				traditionalUnitForm.reset(null, request);
			}else{
				success = false;
				String tmpMsg = new String();
				
				tmpMsg+= bundle.getString("tunit.tunitDesc")+": \""+tmpMap.get("tunitDesc")+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.ptypeId")+": \""+tmpMap.get("ptypeDesc")+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.prodId")+": \""+tmpMap.get("prodDesc")+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.varId")+": \""+ (tmpMap.get("varDesc")==null?"":tmpMap.get("varDesc"))+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.sizeId")+": \""+tmpMap.get("sizeDesc")+"\"<br/>";
				tmpMsg+= bundle.getString("tunit.tunitKilo")+": \""+tmpMap.get("tunitKilo")+"\"<br/>";
				
				errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.dataAlreadyExists", tmpMap.get("tunitCode"), tmpMsg));
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
		return false;
	}
}