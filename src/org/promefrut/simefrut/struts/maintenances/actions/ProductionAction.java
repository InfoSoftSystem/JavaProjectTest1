package org.promefrut.simefrut.struts.maintenances.actions;

import java.math.BigDecimal;

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
import org.promefrut.simefrut.struts.administration.beans.ParametersBean;
import org.promefrut.simefrut.struts.administration.forms.ParametersForm;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;
import org.promefrut.simefrut.struts.maintenances.beans.ProductionBean;
import org.promefrut.simefrut.struts.maintenances.forms.ProductionForm;



/**
 * @author HWM
 *
 */
public class ProductionAction extends DispatchBaseAction {
	public String title = new String();
	

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#Init(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		super.LoadBundle(request);
		title = super.bundle.getString("production.label.text");
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
		
		ProductionForm productionForm = (ProductionForm)form;
		ProductionBean productionBean = new ProductionBean(managerDB, bundle);
		
		request.setAttribute("productsCollection", productionBean.getProductsCollection());
		request.setAttribute("countriesCollection", productionBean.getCountriesCollection(super.user));
		request.setAttribute("oriCountriesCollection", productionBean.getOriContriesCollection());
		
		if(!isFLAG_UPDATE()) {
			productionForm.reset(mapping, request);
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

		ProductionForm productionForm = (ProductionForm)form;
		ProductionBean productionBean = new ProductionBean(managerDB, bundle);
		
		resultJSON= productionBean.find(productionForm.getCtrSk(), productionForm.getYearSk());
		
		return resultJSON;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxInsert(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxInsert(ActionForm form, HttpServletRequest request,ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		
		HttpSession session = request.getSession(true);
		ProductionForm productionForm = (ProductionForm)form;
		ProductionBean productionBean = new ProductionBean(managerDB, bundle);
		UserForm user;
		
		boolean success = true;

		productionForm.validateInsert(null, request, errors);
		
		if(errors.isEmpty()) {
			this.validateDimensionSk(managerDB, productionForm, productionBean, errors);
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			productionBean.insert(productionForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.insert", title, productionForm.getYearSk()));
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
		ProductionForm productionForm = (ProductionForm)form;
		ProductionBean productionBean = new ProductionBean(managerDB, bundle);
		UserForm user;
		
		boolean success = true;

		productionForm.validateUpdate(null, request, errors);

		if(errors.isEmpty()) {
			this.validateDimensionSk(managerDB, productionForm, productionBean, errors);
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			productionBean.update(productionForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.update", title, productionForm.getYearSk()));
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
		ProductionForm productionForm = (ProductionForm)form;
		ProductionBean productionBean = new ProductionBean(managerDB, bundle);
		UserForm user;
		
		boolean success = true;

		productionForm.validateDelete(null, request, errors);
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			productionBean.delete(productionForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.delete", title, productionForm.getYearSk()));
		}else{
			success = false;
		}
		
		saveErrors(request, errors);
		return success;
	}
	
	
	private void validateDimensionSk(SessionManager managerDB, ProductionForm productionForm, ProductionBean productionBean, ActionErrors errors)throws Exception, Error{
		if(errors.isEmpty()){
			if(productionBean.isDuplicated(productionForm)){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.duplicate.register"));
			}else{
				//En relación al rango inferior del dato anterior, no debe variar en más de un 30%. 
				//El dato anterior es el registro próximo anterior a la fecha del registro, 
				//tiene que ser la del año anterior. 
				//Se utilizara para obtener el precio anterior: Año, Pais, producto
				
				ParametersBean bean = new ParametersBean(managerDB, null);
				
				double tolerance = new Double(bean.getParameterValue(ParametersForm.productionToleranceID)).doubleValue();//0.3;
				tolerance /=100;
			
				ProductionForm tmpForm = new ProductionForm();
				tmpForm = productionBean.getLastRecordFrom(productionForm);
				
				if(tmpForm != null){
					/*
					double tmpVolProd = tmpForm.getVolProd().doubleValue();
					double productionVolProd = productionForm.getVolProd().doubleValue();
					
					if(Math.abs(productionVolProd-tmpVolProd)/tmpVolProd > tolerance){
						errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.productionVolProd.outOfTolerance", (tolerance*100)+"%", String.valueOf(Math.round(tmpVolProd*100)/100)));
					}/**/
					
					if(!StringUtils.isEmpty(tmpForm.getCostProd()) && !StringUtils.isEmpty(productionForm.getCostProd())){
					
						double tmpCostProd= (new BigDecimal(tmpForm.getCostProd())).doubleValue();
						double productionCostProd = (new BigDecimal(productionForm.getCostProd())).doubleValue();
						
						if(Math.abs(productionCostProd-tmpCostProd)/tmpCostProd > tolerance){
							errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.productionCostProd.outOfTolerance", (tolerance*100)+"%", String.valueOf(new Double(Math.round(tmpCostProd*100))/100)));
						}
					}
					
					if(!StringUtils.isEmpty(tmpForm.getMaintenanceCost()) && !StringUtils.isEmpty(productionForm.getMaintenanceCost())){
						
						double tmpMaintenanceCost= (new BigDecimal(tmpForm.getMaintenanceCost())).doubleValue();
						double maintenanceCost = (new BigDecimal(productionForm.getMaintenanceCost())).doubleValue();
						
						if(Math.abs(maintenanceCost-tmpMaintenanceCost)/tmpMaintenanceCost > tolerance){
							errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.maintenanceCost.outOfTolerance", (tolerance*100)+"%", String.valueOf(new Double(Math.round(tmpMaintenanceCost*100))/100)));
						}
					}
					
					if(!StringUtils.isEmpty(tmpForm.getEstablishmentCost()) && !StringUtils.isEmpty(productionForm.getEstablishmentCost())){
						
						double tmpEstablishmentCost= (new BigDecimal(tmpForm.getEstablishmentCost())).doubleValue();
						double establishmentCost = (new BigDecimal(productionForm.getEstablishmentCost())).doubleValue();
						
						if(Math.abs(establishmentCost-tmpEstablishmentCost)/tmpEstablishmentCost> tolerance){
							errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.establishmentCost.outOfTolerance", (tolerance*100)+"%", String.valueOf(new Double(Math.round(tmpEstablishmentCost*100))/100)));
						}
					}
				}
			}
		}
	}
}
