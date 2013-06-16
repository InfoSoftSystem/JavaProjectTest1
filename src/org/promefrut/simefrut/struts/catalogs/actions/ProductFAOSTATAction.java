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
import org.promefrut.simefrut.struts.catalogs.beans.ProductFAOSTATBean;
import org.promefrut.simefrut.struts.catalogs.forms.ProductFAOSTATForm;
import org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;



/**
 * @author HWM
 *
 */
public class ProductFAOSTATAction extends DispatchBaseAction {
	public String title = new String();
	

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#Init(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		super.LoadBundle(request);
		title = super.bundle.getString("productFAOSTAT.label.text");
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
		
		ProductFAOSTATForm productFAOSTATForm = (ProductFAOSTATForm)form;
		
		if(!isFLAG_UPDATE()) {
			productFAOSTATForm.reset(mapping, request);
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
			
		}else if("loadGroupProducts".equals(store)){
			resultJSON = loadGroupProducts(request, managerDB);
			
		}
		
		return resultJSON;
	}
	
	public String loadGrid(HttpServletRequest request, SessionManager managerDB) throws Exception, Error {
		String resultJSON = new String();

		ProductFAOSTATBean productFAOSTATBean = new ProductFAOSTATBean(managerDB, bundle);
		
		resultJSON= productFAOSTATBean.find();
		
		return resultJSON;
	}
	
	public String loadProducts(HttpServletRequest request, SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

		ProductFAOSTATBean productFAOSTATBean = new ProductFAOSTATBean(managerDB, bundle);
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= productFAOSTATBean.getProductsCollection();
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, true);
	    }
	    return resultJSON;
		
	}
	
	public String loadGroupProducts(HttpServletRequest request, SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

		ProductFAOSTATBean productFAOSTATBean = new ProductFAOSTATBean(managerDB, bundle);
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= productFAOSTATBean.getGroupProductsCollection();
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, true);
	    }
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
		ProductFAOSTATForm productFAOSTATForm = (ProductFAOSTATForm)form;
		ProductFAOSTATBean productFAOSTATBean = new ProductFAOSTATBean(managerDB, bundle);
		UserForm user;

		boolean success = true;
		productFAOSTATForm.validateInsert(null, request, errors);
		String anotherProduct = productFAOSTATBean.existsData(productFAOSTATForm);
		
		if(errors.isEmpty()) {
			if(!StringUtils.isBlank(anotherProduct)){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productFAOSTAT.faoprodCode.exists", anotherProduct));
			}
			
			if(!StringUtils.isBlank(productFAOSTATForm.getProdSk())){
				String anotherSIMEFRUTProduct = productFAOSTATBean.existsProductSIMEFRUTData(productFAOSTATForm);
				
				if(!StringUtils.isBlank(anotherSIMEFRUTProduct)){
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productFAOSTAT.prodSk.exists", anotherSIMEFRUTProduct));
				}
			}
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			productFAOSTATBean.updateDW(productFAOSTATForm, user.getUsername(),"A");
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.insert", title, productFAOSTATForm.getFaoprodDescSpa()));
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
		ProductFAOSTATForm productFAOSTATForm = (ProductFAOSTATForm)form;
		ProductFAOSTATBean productFAOSTATBean = new ProductFAOSTATBean(managerDB, bundle);

		UserForm user;
		
		boolean success = true;
		productFAOSTATForm.validateUpdate(null, request, errors);
		
		String anotherCountry = productFAOSTATBean.existsData(productFAOSTATForm);

		if(errors.isEmpty()) {
			if(!StringUtils.isBlank(anotherCountry)){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productFAOSTAT.grpId.exists", anotherCountry));
			}
			
			if(!StringUtils.isBlank(productFAOSTATForm.getProdSk())){
				String anotherSIMEFRUTProduct = productFAOSTATBean.existsProductSIMEFRUTData(productFAOSTATForm);
				
				if(!StringUtils.isBlank(anotherSIMEFRUTProduct)){
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productFAOSTAT.prodSk.exists", anotherSIMEFRUTProduct));
				}
			}
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			productFAOSTATBean.updateDW(productFAOSTATForm, user.getUsername(), "A");
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.update", title, productFAOSTATForm.getFaoprodDescSpa()));
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
		ProductFAOSTATForm productFAOSTATForm = (ProductFAOSTATForm)form;
		ProductFAOSTATBean productFAOSTATBean = new ProductFAOSTATBean(managerDB, bundle);

		UserForm user;
		
		boolean success = true;
		productFAOSTATForm.validateDelete(null, request, errors);
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			productFAOSTATBean.updateDW(productFAOSTATForm, user.getUsername(),"I");
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.delete", title, productFAOSTATForm.getFaoprodDescSpa()));
		} else {
			success = false;
		}
		saveErrors(request, errors);
		
		return success;
	}
}
