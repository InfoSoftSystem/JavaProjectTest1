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
import org.promefrut.simefrut.struts.reports.beans.QueryCIFOBBean;



/**
 * @author HWM
 *
 */
public class QueryCIFOBAction extends DispatchBaseAction {
	public String title = new String();
	

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#Init(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		super.LoadBundle(request);
		//title = super.bundle.getString("queryCIFOB.label.text");
		//title = "" + title;
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
		
		QueryCIFOBBean queryCIFOBBean = new QueryCIFOBBean(managerDB, bundle);
		
		request.setAttribute("yearsCollection", queryCIFOBBean.getYearsCollection());
		
		return mapping.findForward(KEY_SUCCESS);
	}
	
	public String loadOriginCountries(HttpServletRequest request,SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();


	    QueryCIFOBBean queryCIFOBBean = new QueryCIFOBBean(managerDB, bundle);
	    
	    String initialDate = (String)request.getParameter("initialDate");
	    String finalDate = (String)request.getParameter("finalDate");
	    
	    if(StringUtils.isEmpty(initialDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryCIFOB.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryCIFOB.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(finalDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryCIFOB.finalDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryCIFOB.finalDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= queryCIFOBBean.getOriginCountriesCollection(initialDate, finalDate);
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, false);
	    }
		return resultJSON;
	}
	
	public String loadProducts(HttpServletRequest request, SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

	    QueryCIFOBBean queryCIFOBBean = new QueryCIFOBBean(managerDB, bundle);
	    
	    String initialDate = (String)request.getParameter("initialDate");
	    String finalDate = (String)request.getParameter("finalDate");
	    String originCountries= (String)request.getParameter("originCountries");
	    String destinationCountry= (String)request.getParameter("destinationCountry");
	    
	    if(StringUtils.isEmpty(initialDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryCIFOB.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryCIFOB.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(finalDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryCIFOB.finalDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryCIFOB.finalDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(originCountries)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryCIFOB.countries.selected.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryCIFOB.countries.selected.required"));
	    }
	    
	    if(StringUtils.isEmpty(destinationCountry)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryCIFOB.country.destination.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryCIFOB.country.destination.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= queryCIFOBBean.getProductsCollection(initialDate, finalDate, originCountries, destinationCountry);
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, true);
	    }
	    
		return resultJSON;
	}
	
	public String loadDestinationCountries(HttpServletRequest request, SessionManager managerDB) throws Exception, Error {
		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();
		ActionErrors errors = new ActionErrors();

	    QueryCIFOBBean queryCIFOBBean = new QueryCIFOBBean(managerDB, bundle);
	    
	    String initialDate = (String)request.getParameter("initialDate");
	    String finalDate = (String)request.getParameter("finalDate");
	    String originCountries = (String)request.getParameter("originCountries");
	    
	    if(StringUtils.isEmpty(initialDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryCIFOB.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryCIFOB.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(finalDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryCIFOB.finalDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryCIFOB.finalDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(originCountries)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryCIFOB.countries.selected.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryCIFOB.countries.selected.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= queryCIFOBBean.getDestinationCountriesCollection(initialDate, finalDate, originCountries);
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, false);
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
		
		if("loadOriginCountries".equals(store)){
			resultJSON = loadOriginCountries(request, managerDB);
			
		}else if("loadProducts".equals(store)){
			resultJSON = loadProducts(request, managerDB);
			
		}else if("loadDestinationCountries".equals(store)){
			resultJSON = loadDestinationCountries(request, managerDB);
			
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