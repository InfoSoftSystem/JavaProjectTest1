package org.promefrut.simefrut.struts.administration.forms;


import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;

public class OptionRolForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer roptId;
	private String rolId;
	private String optId;
	private String rolDesc;
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends OptionRolForm> clase = this.getClass();
	    Field field[] = clase.getDeclaredFields();
	    try {
	        for(int i = 0; i < field.length; i++) {
	            try {
	            	if(!"serialVersionUID".equals(field[i].getName())){
	            		field[i].set(this, "");
	            	};
	            } catch(Exception e) {
	                field[i].set(this, new Integer(0));
	            }
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	        //throw e;
	    } catch(Error e) {
	        e.printStackTrace();
	        throw e;
	    }
	}

	public void validateInsert(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.rolId == null || this.rolId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("user.rolId.required"));
		}
		
		if(this.optId == null || "".equals(this.optId)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("optionRol.options.selected.required"));
		} 
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		return;
	}

	/**
	 * @return the rolId
	 */
	public String getRolId() {
		return rolId;
	}

	/**
	 * @param rolId the rolId to set
	 */
	public void setRolId(String rolId) {
		this.rolId = rolId;
	}

	/**
	 * @return the optId
	 */
	public String getOptId() {
		return optId;
	}

	/**
	 * @param optId the optId to set
	 */
	public void setOptId(String optId) {
		this.optId = optId;
	}

	/**
	 * @return the roptId
	 */
	public Integer getRoptId() {
		return roptId;
	}

	/**
	 * @param roptId the roptId to set
	 */
	public void setRoptId(Integer roptId) {
		this.roptId = roptId;
	}

	/**
	 * @return the rolDesc
	 */
	public String getRolDesc() {
		return rolDesc;
	}

	/**
	 * @param rolDesc the rolDesc to set
	 */
	public void setRolDesc(String rolDesc) {
		this.rolDesc = rolDesc;
	}

} //fin de la clase
