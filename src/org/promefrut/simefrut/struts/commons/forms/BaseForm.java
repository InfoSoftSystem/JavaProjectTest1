package org.promefrut.simefrut.struts.commons.forms;


import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Henry Willy Melara
 *
 */
public abstract class BaseForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	/**
	 * GLOBAL_ERRORS field used to set error messages on Request and present them on paginaBase.jsp
	 */
	public static final String GLOBAL_ERRORS = "globalErrors";
	
	/**
	 * GLOBAL_WARNNINGS field used to set warnning messages on Request and present them on paginaBase.jsp
	 */
	public static final String GLOBAL_WARNNINGS = "globalWarnings";
	
	/**
	 * GLOBAL_MESSAGES field used to set messages on Request and present them on paginaBase.jsp
	 */
	public static final String GLOBAL_MESSAGES = "globalMessages";
	
	private String accion;
	public SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
	private String export;
	
	public BaseForm(){
		formatFecha.setLenient(false);
	};
	public abstract void validateInsert(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error;
	public abstract void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error;
	public abstract void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error;
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		accion="";
	}
	
	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getAccion() {
		return accion;
	}

	public void setExport(String export) {
		this.export = export;
	}

	public String getExport() {
		return (export==null||"false".equals(export)||"".equals(export))?"false":"true";
	}
}
