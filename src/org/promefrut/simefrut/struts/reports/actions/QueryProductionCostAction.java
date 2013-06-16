package org.promefrut.simefrut.struts.reports.actions;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;
import org.promefrut.simefrut.struts.reports.beans.QueryProductionCostBean;
import org.promefrut.simefrut.utils.CustomExceptions;



/**
 * @author HWM
 *
 */
public class QueryProductionCostAction extends DispatchBaseAction {
	public String title = new String();
	

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#Init(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		super.LoadBundle(request);
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
		
		QueryProductionCostBean queryProductionCostBean = new QueryProductionCostBean(managerDB, bundle);
		
		request.setAttribute("yearsCollection", queryProductionCostBean.getYearsCollection());
		
		return mapping.findForward(KEY_SUCCESS);
	}

	public ActionForward loadCountries(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			  HttpServletResponse response) throws Exception, Error {
		SessionManager managerDB = null;
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

		try {
			this.Init(request);
		    managerDB = new SessionManager(dtsource);
		    
		    QueryProductionCostBean queryProductionCostBean = new QueryProductionCostBean(managerDB, bundle);
		    
		    String initialDate = (String)request.getParameter("initialDate");
		    String finalDate = (String)request.getParameter("finalDate");
		    
		    if(StringUtils.isEmpty(initialDate)){
		    	resultJSON += getAjaxTemplate(false, bundle.getString("queryProductionCost.initialDate.required"));
		    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryProductionCost.initialDate.required"));
		    }
		    
		    if(StringUtils.isEmpty(finalDate)){
		    	resultJSON += getAjaxTemplate(false, bundle.getString("queryProductionCost.finalDate.required"));
		    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryProductionCost.finalDate.required"));
		    }
		    
		    if(StringUtils.isEmpty(resultJSON)){
		    	resultJSON= queryProductionCostBean.getCountriesCollection(initialDate, finalDate);
		    }else{
		    	saveErrors(request, errors);
			    resultJSON = getErrors(request, false);
		    }
			
		} catch(Exception e) {
			try{
				CustomExceptions ce = new CustomExceptions(request, e);
				if(ce.getMensaje() != null) {
					resultJSON = getAjaxTemplate(false, ce.getMensaje());
				} else {
					resultJSON = getAjaxTemplate(false, e.getMessage());
				    e.printStackTrace();
				}
			}catch(Exception ee){
				resultJSON = getAjaxTemplate(false, ee.getMessage());
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
	
	public ActionForward loadProducts(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			  HttpServletResponse response) throws Exception, Error {
		SessionManager managerDB = null;
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

		try {
			this.Init(request);
		    managerDB = new SessionManager(dtsource);
		    
		    QueryProductionCostBean queryProductionCostBean = new QueryProductionCostBean(managerDB, bundle);
		    
		    String initialDate = (String)request.getParameter("initialDate");
		    String finalDate = (String)request.getParameter("finalDate");
		    String countries= (String)request.getParameter("countries");
		    
		    if(StringUtils.isEmpty(initialDate)){
		    	resultJSON += getAjaxTemplate(false, bundle.getString("queryProductionCost.initialDate.required"));
		    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryProductionCost.initialDate.required"));
		    }
		    
		    if(StringUtils.isEmpty(finalDate)){
		    	resultJSON += getAjaxTemplate(false, bundle.getString("queryProductionCost.finalDate.required"));
		    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryProductionCost.finalDate.required"));
		    }
		    
		    if(StringUtils.isEmpty(countries)){
		    	resultJSON += getAjaxTemplate(false, bundle.getString("queryProductionCost.countries.selected.required"));
		    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryProductionCost.countries.selected.required"));
		    }
		    
		    if(StringUtils.isEmpty(resultJSON)){
		    	resultJSON= queryProductionCostBean.getProductsCollection(initialDate, finalDate, countries);
		    }else{
		    	saveErrors(request, errors);
			    resultJSON = getErrors(request, true);
		    }
			
		} catch(Exception e) {
			try{
				CustomExceptions ce = new CustomExceptions(request, e);
				if(ce.getMensaje() != null) {
					resultJSON = getAjaxTemplate(false, ce.getMensaje());
				} else {
					resultJSON = getAjaxTemplate(false, e.getMessage());
				    e.printStackTrace();
				}
			}catch(Exception ee){
				resultJSON = getAjaxTemplate(false, ee.getMessage());
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
	
	public String getAjaxTemplate(boolean success, String msg){
		String results = new String();
		if(msg==null){
			msg ="Null Exception";
		}
		results = "{\"totalCount\":\"1\",\"registers\":[{\"msg\":\""+msg.replaceAll("\\\"", "\\\\\"").replaceAll("\n", "\\\\n").replaceAll("\r","")+"\"}]}";
		
		return results;
	}
}