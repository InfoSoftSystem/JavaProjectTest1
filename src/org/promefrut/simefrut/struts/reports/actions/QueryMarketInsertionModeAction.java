package org.promefrut.simefrut.struts.reports.actions;

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
import org.promefrut.simefrut.struts.reports.beans.QueryMarketInsertionModeBean;



/**
 * @author HWM
 *
 */
public class QueryMarketInsertionModeAction extends DispatchBaseAction {
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
		
		QueryMarketInsertionModeBean queryMarketInsertionModeBean = new QueryMarketInsertionModeBean(managerDB, bundle);
		
		request.setAttribute("yearsCollection", queryMarketInsertionModeBean.getYearsCollection());
		
		return mapping.findForward(KEY_SUCCESS);
	}
	
	public String loadOffererCountries(HttpServletRequest request,SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

	    QueryMarketInsertionModeBean queryMarketInsertionModeBean = new QueryMarketInsertionModeBean(managerDB, bundle);
	    
	    String initialDate = (String)request.getParameter("initialDate");
	    String finalDate = (String)request.getParameter("finalDate");
	    
	    if(StringUtils.isEmpty(initialDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryMarketInsertionMode.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryMarketInsertionMode.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(finalDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryMarketInsertionMode.finalDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryMarketInsertionMode.finalDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= queryMarketInsertionModeBean.getOffererCollection(initialDate, finalDate);
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, false);
	    }
		return resultJSON;
	}
	
	public String loadDestinationCountries(HttpServletRequest request,SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

	    QueryMarketInsertionModeBean queryMarketInsertionModeBean = new QueryMarketInsertionModeBean(managerDB, bundle);
	    
	    String initialDate = (String)request.getParameter("initialDate");
	    String finalDate = (String)request.getParameter("finalDate");
	    String offererCountry= (String)request.getParameter("offererCountry");
	    
	    
	    if(StringUtils.isEmpty(initialDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryMarketInsertionMode.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryMarketInsertionMode.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(finalDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryMarketInsertionMode.finalDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryMarketInsertionMode.finalDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(offererCountry)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryMarketInsertionMode.offerer.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryMarketInsertionMode.offerer.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= queryMarketInsertionModeBean.getDestinationCollection(initialDate, finalDate, offererCountry);
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, false);
	    }
		return resultJSON;
	}
	
	public String loadProducts(HttpServletRequest request,SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

	    QueryMarketInsertionModeBean queryMarketInsertionModeBean = new QueryMarketInsertionModeBean(managerDB, bundle);
	    
	    String initialDate = (String)request.getParameter("initialDate");
	    String finalDate = (String)request.getParameter("finalDate");
	    String offererCountry= (String)request.getParameter("offererCountry");
	    String destinationCountry = (String)request.getParameter("destinationCountry");
	    
	    if(StringUtils.isEmpty(initialDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryMarketInsertionMode.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryMarketInsertionMode.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(finalDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryMarketInsertionMode.finalDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryMarketInsertionMode.finalDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(offererCountry)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryMarketInsertionMode.offerer.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryMarketInsertionMode.offerer.required"));
	    }
	    
	    if(StringUtils.isEmpty(destinationCountry)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryMarketInsertionMode.destination.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryMarketInsertionMode.destination.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= queryMarketInsertionModeBean.getProductsCollection(initialDate, finalDate, offererCountry, destinationCountry);
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, true);
	    }
			
		return resultJSON;
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
		
		if("loadOffererCountries".equals(store)){
			resultJSON = loadOffererCountries(request, managerDB);
			
		}else if("loadDestinationCountries".equals(store)){
			resultJSON = loadDestinationCountries(request, managerDB);
			
		}else if("loadProducts".equals(store)){
			resultJSON = loadProducts(request, managerDB);
			
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