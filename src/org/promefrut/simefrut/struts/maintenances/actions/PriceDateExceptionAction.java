package org.promefrut.simefrut.struts.maintenances.actions;

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
import org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;
import org.promefrut.simefrut.struts.maintenances.beans.PriceDateExceptionBean;
import org.promefrut.simefrut.struts.maintenances.forms.PriceDateExceptionForm;



/**
 * @author HWM
 *
 */
public class PriceDateExceptionAction extends DispatchBaseAction {
	public String title = new String();
	

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#Init(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		super.LoadBundle(request);
		title = super.bundle.getString("priceDateException.label.text");
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
	
	public String loadGrid(HttpServletRequest request, SessionManager managerDB) throws Exception, Error {
		String resultJSON = new String();

		String ctrId = (String)request.getParameter("ctrId");
	    //String dateField = (String)request.getParameter("dateField");
	    
		PriceDateExceptionBean priceDateExceptionBean = new PriceDateExceptionBean(managerDB, bundle);
		
		resultJSON= priceDateExceptionBean.find(ctrId);
		
		return resultJSON;
	}
	
	public String loadProducts(HttpServletRequest request, SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

		PriceDateExceptionBean priceDateExceptionBean = new PriceDateExceptionBean(managerDB, bundle);
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= priceDateExceptionBean.getProductsCollection();
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, true);
	    }
	    return resultJSON;
		
	}
	
	public String loadVarieties(HttpServletRequest request, SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

		PriceDateExceptionBean priceDateExceptionBean = new PriceDateExceptionBean(managerDB, bundle);
	    
	    String prodId= (String)request.getParameter("prodId");
	    
	    if(StringUtils.isEmpty(prodId)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("priceDateException.prodId.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("priceDateException.prodId.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= priceDateExceptionBean.getVarietiesCollection(prodId);
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, true);
	    }
	    return resultJSON;
		
	}
	
	public String loadProductTypes(HttpServletRequest request, SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

		PriceDateExceptionBean priceDateExceptionBean = new PriceDateExceptionBean(managerDB, bundle);
	    
		String prodId= (String)request.getParameter("prodId");
	    
	    if(StringUtils.isEmpty(prodId)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("priceDateException.prodId.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("priceDateException.prodId.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= priceDateExceptionBean.getProductTypesCollection(prodId);
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, true);
	    }
	    return resultJSON;
		
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#find(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward find(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		
		PriceDateExceptionForm priceDateExceptionForm = (PriceDateExceptionForm)form;
		PriceDateExceptionBean priceDateExceptionBean = new PriceDateExceptionBean(managerDB, bundle);
		
		request.setAttribute("productsCollection", priceDateExceptionBean.getProductsCollection());
		request.setAttribute("countriesCollection", priceDateExceptionBean.getCountriesCollection(super.user));
		
		if(!isFLAG_UPDATE()) {
			priceDateExceptionForm.reset(mapping, request);
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
		String store = (String)request.getParameter("store");
		
		if("loadGrid".equals(store)){
			resultJSON = loadGrid(request, managerDB);
			
		}else if("loadProducts".equals(store)){
			resultJSON = loadProducts(request, managerDB);
			
		}else if("loadProductTypes".equals(store)){
			resultJSON = loadProductTypes(request, managerDB);
			
		}else if("loadVarieties".equals(store)){
			resultJSON = loadVarieties(request, managerDB);
			
		}
		
		return resultJSON;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxInsert(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxInsert(ActionForm form, HttpServletRequest request,ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		
		HttpSession session = request.getSession(true);
		PriceDateExceptionForm priceDateExceptionForm = (PriceDateExceptionForm)form;
		PriceDateExceptionBean priceDateExceptionBean = new PriceDateExceptionBean(managerDB, bundle);
		UserForm user;
		
		boolean success = true;

		priceDateExceptionForm.validateInsert(null, request, errors);
		
		if(errors.isEmpty()) {
			this.validateDimensionSk(priceDateExceptionForm, priceDateExceptionBean, errors);
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			priceDateExceptionBean.insert(priceDateExceptionForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.insert", title, priceDateExceptionForm.getDateField()));
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
		PriceDateExceptionForm priceDateExceptionForm = (PriceDateExceptionForm)form;
		PriceDateExceptionBean priceDateExceptionBean = new PriceDateExceptionBean(managerDB, bundle);
		UserForm user;
		
		boolean success = true;

		priceDateExceptionForm.validateUpdate(null, request, errors);

		if(errors.isEmpty()) {
			this.validateDimensionSk(priceDateExceptionForm, priceDateExceptionBean, errors);
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			priceDateExceptionBean.update(priceDateExceptionForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.update", title, priceDateExceptionForm.getDateField()));
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
		PriceDateExceptionForm priceDateExceptionForm = (PriceDateExceptionForm)form;
		PriceDateExceptionBean priceDateExceptionBean = new PriceDateExceptionBean(managerDB, bundle);
		UserForm user;
		
		boolean success = true;

		priceDateExceptionForm.validateDelete(null, request, errors);
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			priceDateExceptionBean.delete(priceDateExceptionForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.delete", title, priceDateExceptionForm.getDateField()));
		}else{
			success = false;
		}
		
		saveErrors(request, errors);
		return success;
	}
	
	
	private void validateDimensionSk(PriceDateExceptionForm priceDateExceptionForm, PriceDateExceptionBean priceDateExceptionBean, ActionErrors errors)throws Exception, Error{
		if(errors.isEmpty()){
			if(priceDateExceptionBean.isDuplicated(priceDateExceptionForm)){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("error.generic.unique"));
			}
						
			//If the variety is not selected, then we extract the according surrogate key
			if(priceDateExceptionForm.getVarId()==null || priceDateExceptionForm.getVarId().intValue()==0){
				/*
				//priceDateExceptionForm.setVarId("");
				 
				String varId = priceDateExceptionBean.isVarIdAssociatedToProdId(priceDateExceptionForm);
				
				if(varId!=null && !"".equals(varId)){
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.varId.required"));
				}
				
				*/
			}else{//If a variety and product is selected then, we should validate the association
				String VarId = priceDateExceptionBean.isVarIdAssociatedToProdId(priceDateExceptionForm);
				
				if(VarId==null||"".equals(VarId)){
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.prdVarSk.required"));
				}
			}
			
			//If the variety is not selected, then we extract the according surrogate key
			if(priceDateExceptionForm.getPtypeId()==null || priceDateExceptionForm.getPtypeId().intValue()==0){
				/*
				 
				String ptypeId = priceDateExceptionBean.isPtypeIdAssociatedToProdId(priceDateExceptionForm);
				
				if(ptypeId!=null && !"".equals(ptypeId)){
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.ptypeId.required"));
				}
				
				*/
			}else{//If a variety and product is selected then, we should validate the association
				String ptypeId = priceDateExceptionBean.isPtypeIdAssociatedToProdId(priceDateExceptionForm);
				
				if(ptypeId==null||"".equals(ptypeId)){
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.ptypeSk.required"));
				}
			}
		}
	}
}
