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
import org.promefrut.simefrut.struts.reports.beans.QueryPricesBean;



/**
 * @author HWM
 *
 */
public class QueryPricesAction extends DispatchBaseAction {
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
		
		QueryPricesBean queryPricesBean = new QueryPricesBean(managerDB, bundle);
		
		//request.setAttribute("countriesCollection", queryPricesBean.getCountriesCollection());
		//request.setAttribute("productsCollection", queryPricesBean.getProductsCollection());
		//request.setAttribute("varietiesCollection", queryPricesBean.getVarietiesCollection());
		//request.setAttribute("qualitiesCollection", queryPricesBean.getQualitiesCollection());
		//request.setAttribute("sizesCollection", queryPricesBean.getSizesCollection());
		request.setAttribute("yearsCollection", queryPricesBean.getYearsCollection());
		
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
		
		if("loadCountries".equals(store)){
			resultJSON = loadCountries(request, managerDB);
			
		}else if("loadProducts".equals(store)){
			resultJSON = loadProducts(request, managerDB);
			
		}else if("loadProductTypes".equals(store)){
			resultJSON = loadProductTypes(request, managerDB);
			
		}else if("loadVarieties".equals(store)){
			resultJSON = loadVarieties(request, managerDB);
			
		}else if("loadQualities".equals(store)){
			resultJSON = loadQualities(request, managerDB);
			
		}else if("loadSizes".equals(store)){
			resultJSON = loadSizes(request, managerDB);
		}
		
		return resultJSON;
	}
	
	public String loadCountries(HttpServletRequest request, SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

	    QueryPricesBean queryPricesBean = new QueryPricesBean(managerDB, bundle);
	    
	    String initialDate = (String)request.getParameter("initialDate");
	    String finalDate = (String)request.getParameter("finalDate");
	    String productsFilter = (String)request.getParameter("productsFilter");
	    
	    if(StringUtils.isEmpty(initialDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(finalDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(productsFilter)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.product.selected.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.product.selected.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= queryPricesBean.getCountriesCollection(initialDate, finalDate, productsFilter);
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

		QueryPricesBean queryPricesBean = new QueryPricesBean(managerDB, bundle);
	    
	    String initialDate = (String)request.getParameter("initialDate");
	    String finalDate = (String)request.getParameter("finalDate");
	    //String countries= (String)request.getParameter("countries");
	    
	    if(StringUtils.isEmpty(initialDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(finalDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.initialDate.required"));
	    }
	    
	    /*if(StringUtils.isEmpty(countries)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.countries.selected.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.countries.selected.required"));
	    }*/
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= queryPricesBean.getProductsCollection(initialDate, finalDate);
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

		QueryPricesBean queryPricesBean = new QueryPricesBean(managerDB, bundle);
	    
		String initialDate = (String)request.getParameter("initialDate");
	    String finalDate = (String)request.getParameter("finalDate");
	    //String countries= (String)request.getParameter("countries");
	    String prodId= (String)request.getParameter("prodId");
	    String ptypeId= (String)request.getParameter("ptypeId");
	    
	    ptypeId = "null".equals(ptypeId)?null:ptypeId;
	    
	    if(StringUtils.isEmpty(initialDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(finalDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.initialDate.required"));
	    }
	    
	    /*if(StringUtils.isEmpty(countries)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.countries.selected.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.countries.selected.required"));
	    }*/
	    
	    if(StringUtils.isEmpty(prodId)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.product.selected.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.product.selected.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= queryPricesBean.getVarietiesCollection(initialDate, finalDate, prodId, ptypeId);
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

		QueryPricesBean queryPricesBean = new QueryPricesBean(managerDB, bundle);
	    
		String initialDate = (String)request.getParameter("initialDate");
	    String finalDate = (String)request.getParameter("finalDate");
	    //String countries= (String)request.getParameter("countries");
	    String prodId= (String)request.getParameter("prodId");
	    
	    if(StringUtils.isEmpty(initialDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(finalDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.initialDate.required"));
	    }
	    
	    /*if(StringUtils.isEmpty(countries)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.countries.selected.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.countries.selected.required"));
	    }*/
	    
	    if(StringUtils.isEmpty(prodId)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.product.selected.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.product.selected.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= queryPricesBean.getProductTypesCollection(initialDate, finalDate, prodId);
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, true);
	    }
	    return resultJSON;
		
	}
	
	public String loadQualities(HttpServletRequest request, SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

		QueryPricesBean queryPricesBean = new QueryPricesBean(managerDB, bundle);
	    
		String initialDate = (String)request.getParameter("initialDate");
	    String finalDate = (String)request.getParameter("finalDate");
	    //String countries= (String)request.getParameter("countries");
	    String prodId= (String)request.getParameter("prodId");
	    String varId= (String)request.getParameter("varId");
	    String ptypeId= (String)request.getParameter("ptypeId");
	    
	    varId = "null".equals(varId)?null:varId;
	    
	    ptypeId = "null".equals(ptypeId)?null:ptypeId;
	    
	    if(StringUtils.isEmpty(initialDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(finalDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.initialDate.required"));
	    }
	    
	    /*if(StringUtils.isEmpty(countries)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.countries.selected.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.countries.selected.required"));
	    }*/
	    
	    if(StringUtils.isEmpty(prodId)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.product.selected.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.product.selected.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= queryPricesBean.getQualitiesCollection(initialDate, finalDate, prodId, ptypeId, varId);
	    }else{
	    	saveErrors(request, errors);
		    resultJSON = getErrors(request, true);
	    }
	    return resultJSON;
		
	}
	
	public String loadSizes(HttpServletRequest request, SessionManager managerDB) throws Exception, Error {
		ActionErrors errors = new ActionErrors();

		//Se coloco KEY_SUCCESS debido a que normalmente la funcion Find se utiliza solo en jsp que muestran datos (jspList) y normalmente estan
		//colocadas en los success de los actions
		String resultJSON = new String();

		QueryPricesBean queryPricesBean = new QueryPricesBean(managerDB, bundle);
	    
		String initialDate = (String)request.getParameter("initialDate");
	    String finalDate = (String)request.getParameter("finalDate");
	    //String countries= (String)request.getParameter("countries");
	    String prodId= (String)request.getParameter("prodId");
	    String varId= (String)request.getParameter("varId");
	    String quaId= (String)request.getParameter("quaId");
	    String ptypeId= (String)request.getParameter("ptypeId");
	    
	    varId = "null".equals(varId)?null:varId;
	    ptypeId = "null".equals(ptypeId)?null:ptypeId;
	    
	    if(StringUtils.isEmpty(initialDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.initialDate.required"));
	    }
	    
	    if(StringUtils.isEmpty(finalDate)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.initialDate.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.initialDate.required"));
	    }
	    
	    /*if(StringUtils.isEmpty(countries)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.countries.selected.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.countries.selected.required"));
	    }*/
	    
	    if(StringUtils.isEmpty(prodId)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("queryPrices.product.selected.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("queryPrices.product.selected.required"));
	    }
	    
	    if(StringUtils.isEmpty(quaId)){
	    	resultJSON += getAjaxTemplate(false, bundle.getString("price.quaSk.required"));
	    	errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.quaSk.required"));
	    }
	    
	    if(StringUtils.isEmpty(resultJSON)){
	    	resultJSON= queryPricesBean.getSizesCollection(initialDate, finalDate, prodId, ptypeId, varId, quaId);
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