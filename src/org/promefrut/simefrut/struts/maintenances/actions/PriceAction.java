package org.promefrut.simefrut.struts.maintenances.actions;

import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.promefrut.simefrut.struts.maintenances.beans.PriceBean;
import org.promefrut.simefrut.struts.maintenances.forms.PriceForm;
import org.promefrut.simefrut.utils.CustomExceptions;



/**
 * @author HWM
 *
 */
public class PriceAction extends DispatchBaseAction {
	public String title = new String();
	

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#Init(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		super.LoadBundle(request);
		title = super.bundle.getString("price.label.text");
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
		
		PriceForm priceForm = (PriceForm)form;
		PriceBean priceBean = new PriceBean(managerDB, bundle);
		
		request.setAttribute("varietiesCollection", priceBean.getVarietiesCollection());
		request.setAttribute("productsCollection", priceBean.getProductsCollection());
		request.setAttribute("countriesCollection", priceBean.getCountriesCollection(super.user));
		request.setAttribute("sizesCollection", priceBean.getSizesCollection());
		request.setAttribute("qualitiesCollection", priceBean.getQualitiesCollection());
		request.setAttribute("traditionalUnitsCollection", priceBean.getTraditionalUnitsCollection());
		request.setAttribute("commLevelsCollection", priceBean.getCommLevelsCollection());
		request.setAttribute("oriCountriesCollection", priceBean.getOriContriesCollection());
		request.setAttribute("oriRegionsCollection", priceBean.getOriRegionsCollection());
		request.setAttribute("oriProvincesCollection", priceBean.getOriProvincesCollection());
		request.setAttribute("productTypesCollection", priceBean.getProductTypesCollection());
		
		if(!isFLAG_UPDATE()) {
			priceForm.reset(mapping, request);
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

		PriceForm priceForm = (PriceForm)form;
		PriceBean priceBean = new PriceBean(managerDB, bundle);
		
		resultJSON= priceBean.find(new BigDecimal(priceForm.getCtrSk()), priceForm.getDateField());
		
		return resultJSON;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxInsert(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxInsert(ActionForm form, HttpServletRequest request,ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		
		HttpSession session = request.getSession(true);
		PriceForm priceForm = (PriceForm)form;
		PriceBean priceBean = new PriceBean(managerDB, bundle);
		UserForm user;
		
		boolean success = true;

		priceForm.validateInsert(null, request, errors);
		
		if(errors.isEmpty()) {
			this.validateDimensionSk(managerDB, priceForm, priceBean, errors);
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			priceBean.insert(priceForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.insert", title, priceForm.getDateField()));
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
		PriceForm priceForm = (PriceForm)form;
		PriceBean priceBean = new PriceBean(managerDB, bundle);
		UserForm user;
		
		boolean success = true;

		priceForm.validateUpdate(null, request, errors);

		if(errors.isEmpty()) {
			this.validateDimensionSk(managerDB, priceForm, priceBean, errors);
		}
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			priceBean.update(priceForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.update", title, priceForm.getDateField()));
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
		PriceForm priceForm = (PriceForm)form;
		PriceBean priceBean = new PriceBean(managerDB, bundle);
		UserForm user;
		
		boolean success = true;

		priceForm.validateDelete(null, request, errors);
		
		if(errors.isEmpty()) {
			user = (UserForm)session.getAttribute("user");
			priceBean.delete(priceForm, user.getUsername());
			managerDB.commit();
			errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.delete", title, priceForm.getDateField()));
		}else{
			success = false;
		}
		
		saveErrors(request, errors);
		return success;
	}
	
	public ActionForward copyData(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			  HttpServletResponse response) throws Exception, Error {
		
		ActionErrors errors = new ActionErrors();
		SessionManager managerDB = null;

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();
		boolean success = true;

		try {
			this.Init(request);
		    managerDB = new SessionManager(dtsource);
		    //success = copyData(form, request, errors, messages, managerDB);
		    
		    HttpSession session = request.getSession(true);
			PriceForm priceForm = (PriceForm)form;
			PriceBean priceBean = new PriceBean(managerDB, bundle);
			UserForm user;

			//priceForm.validateCopyData(null, request, errors);
			priceForm.validateCopyDataYesterday(null, request, errors);
			
			if(errors.isEmpty()) {
				user = (UserForm)session.getAttribute("user");
				//int result = priceBean.copyData(priceForm, user.getUsername());
				
				priceForm.setDateTo(priceForm.getDateField());
				
				/*
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date dateTo = format.parse(priceForm.getDateField());
		
				//Obtiene la fecha del sistema
				Calendar c1 = Calendar.getInstance();
				c1.setTime(dateTo);
				
				c1.add(Calendar.DATE,-1); 
				priceForm.setDateFrom(format.format(c1.getTime()));*/				
				
				int result = priceBean.copyData(priceForm, user.getUsername());
				
				if( result == -1){
					success = false;
					managerDB.rollback();
					errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("price.dateTo.dataExists", priceForm.getDateTo()));
				}else if(result == -2){
					success = false;
					managerDB.rollback();
					errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("price.dateFrom.noExists", priceForm.getDateFrom()));
				}else{
					success = true;
					managerDB.commit();
					errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.update", title, priceForm.getDateTo()));
				};
			}else{
				success = false;
			}
			
			saveErrors(request, errors);
		    
		    
		    resultJSON = getErrors(request, success);
		} catch(Exception e) {
			try{
				CustomExceptions ce = new CustomExceptions(request, e);
				if(ce.getMensaje() != null) {
					resultJSON = getAjaxTemplate(false,ce.getMensaje());
				} else {
					resultJSON = getAjaxTemplate(false, e.getMessage());
				    e.printStackTrace();
				}
			}catch(Exception ee){
				resultJSON = getAjaxTemplate(false,ee.getMessage());
				System.out.println("*********** CUSTOM EXCEPTION ERROR **********");
				ee.printStackTrace();
			    System.out.println("^^^^^^^^^^^ CUSTOM EXCEPTION ERROR ^^^^^^^^^^");
				throw e;
			}finally{
				managerDB.rollback();
			}
		} catch(Error e) {
			managerDB.rollback();
			resultJSON = getAjaxTemplate(false, e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			managerDB.close();
			PrintWriter write = null;
			
			try{
            response.setHeader("Cache-Control", "none");
            response.setHeader("Pragma", "no-cache");
            response.setCharacterEncoding("ISO-8859-1");
            response.setContentType("text/html;charset=ISO-8859-1");
            write = response.getWriter();
            write.write(resultJSON);
            
			}catch(Exception e){
				e.printStackTrace();
			}catch(Error e){
				e.printStackTrace();
			}finally{
				if(write!=null){
					write.flush();
					write.close();
				}
			}
		}		
		return null;
	}
	
	private void validateDimensionSk(SessionManager managerDB, PriceForm priceForm, PriceBean priceBean, ActionErrors errors)throws Exception, Error{
		priceForm.setOriSk(priceBean.getOriSK(priceForm.getOriCtrId(), priceForm.getOriRegId(), priceForm.getOriProvId()));
		
		//If the variety is not selected, then we extract the according surrogate key
		if(priceForm.getPrdVarSk()==null || "".equals(priceForm.getPrdVarSk())){
			priceForm.setPrdVarSk("");
			String prdVarSk = priceBean.getPrdVarSK(priceForm);
			
			if(prdVarSk==null||"".equals(prdVarSk)){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.prdVarSk.required"));
			}else{
				priceForm.setPrdVarSk(prdVarSk);
			}
		}else{//If a variety and product is selected then, we should validate the association
			String prdVarSk = priceBean.getPrdVarSK(priceForm);
			
			if("".equals(prdVarSk)){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.prdVarSk.required"));
			}else{
				priceForm.setPrdVarSk(prdVarSk);
			}
		}
		
		priceForm.setPtypeSk(priceBean.getPtypeSK(priceForm));
		
		//Validate tradional unit association with Product, Variety, Size
		String tradUnitSK = priceBean.getTradUnit(priceForm);
		
		if(tradUnitSK==null||"".equals(tradUnitSK)){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.tradUnitSk.notFound"));
			
		}else if(!tradUnitSK.equals(priceForm.getTunitSk())){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.tradUnitSk.notFound"));
		}
		
		if(errors.isEmpty()){
			
			if(priceBean.isDuplicated(priceForm)){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.duplicate.register"));
			}else{
				//En relación al rango inferior del dato anterior, no debe variar en más de un 50%. 
				//El dato anterior es el registro próximo anterior a la fecha del registro, 
				//puede haber sido la del día anterior o mes anterior. 
				//Se utilizara para obtener el precio anterior: producto, variedad, calidad, tamaño y origen
				ParametersBean bean = new ParametersBean(managerDB, null);
				
				double tolerance = new Double(bean.getParameterValue(ParametersForm.priceToleranceID)).doubleValue();//1.05;
				tolerance/=100;
				
				PriceForm tmpForm = new PriceForm();
				tmpForm = priceBean.getLastRecordFrom(priceForm);
				
				if(tmpForm != null){
					double tmpInfUni = new Double(tmpForm.getPriceInfUni()).doubleValue();
					double tmpSupUni = new Double(tmpForm.getPriceSupUni()).doubleValue();
					double tmpInfKilo = new Double(tmpForm.getPriceInfKilo()).doubleValue();
					double tmpSupKilo = new Double(tmpForm.getPriceSupKilo()).doubleValue();
					
					double priceInfUni = new Double(priceForm.getPriceInfUni()).doubleValue();
					double priceSupUni = new Double(priceForm.getPriceSupUni()).doubleValue();
					double priceInfKilo = new Double(priceForm.getPriceInfKilo()).doubleValue();
					double priceSupKilo= new Double(priceForm.getPriceSupKilo()).doubleValue();
					
					if(Math.abs(priceInfUni-tmpInfUni)/tmpInfUni > tolerance){
						errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.priceInfUni.outOfTolerance", (tolerance*100)+"%", String.valueOf(new Double(Math.round(tmpInfUni*100))/100)));
					}
					
					if(Math.abs(priceSupUni-tmpSupUni)/tmpSupUni > tolerance){
						errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.priceSupUni.outOfTolerance", (tolerance*100)+"%", String.valueOf(new Double(Math.round(tmpSupUni*100))/100)));
					}
					
					if(Math.abs(priceInfKilo-tmpInfKilo)/tmpInfKilo > tolerance){
						errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.priceInfKilo.outOfTolerance", (tolerance*100)+"%", String.valueOf(new Double(Math.round(tmpInfKilo*100))/100)));
					}
					
					if(Math.abs(priceSupKilo-tmpSupKilo)/tmpSupKilo > tolerance){
						errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.priceSupKilo.outOfTolerance", (tolerance*100)+"%", String.valueOf(new Double(Math.round(tmpSupKilo*100))/100)));
					}
				}
			}
		}
	}
}
