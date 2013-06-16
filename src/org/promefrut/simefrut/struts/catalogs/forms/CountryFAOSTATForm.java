package org.promefrut.simefrut.struts.catalogs.forms;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;

/**
 * @author HWM
 * 
 * XDoclet definition:
 * @struts.form name="countryFAOSTATForm"
 */
public class CountryFAOSTATForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ctrId field
	 */
	private String ctrId;
	
	/**
	 * faoctrSk field
	 */
	private String faoctrSk;
	
	/**
	 * faoctrId field
	 */
	private String faoctrId;
		
	/**
	 * faoctrDesc field
	 */
	private String faoctrDesc;
	
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends CountryFAOSTATForm> clase = this.getClass();
	    Field field[] = clase.getDeclaredFields();
	    try {
	        for(int i = 0; i < field.length; i++) {
	            try {
	            	if(!"serialVersionUID".equals(field[i].getName())){
	            		field[i].set(this, "");
	            	};
	            } catch(Exception e) {
	                field[i].set(this, new Double(0));
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
		if(this.ctrId == null || this.ctrId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("country.ctrId.required"));
		}
		
		if(this.faoctrId == null || this.faoctrId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("countryFAOSTAT.faoctrId.required"));
		}
		
		if(this.faoctrDesc == null || this.faoctrDesc.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("countryFAOSTAT.faoctrDesc.required"));
		} else {
			if(faoctrDesc.length() > 100) {
				faoctrDesc = faoctrDesc.substring(0, 100);
			}
		}
		
		
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.faoctrSk == null || this.faoctrSk.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("countryFAOSTAT.faoctrSk.required"));
		}
	}

	/**
	 * @return the ctrId
	 */
	public String getCtrId() {
		return ctrId;
	}

	/**
	 * @param ctrId the ctrId to set
	 */
	public void setCtrId(String ctrId) {
		this.ctrId = ctrId;
	}

	/**
	 * @return the faoctrSk
	 */
	public String getFaoctrSk() {
		return faoctrSk;
	}

	/**
	 * @param faoctrSk the faoctrSk to set
	 */
	public void setFaoctrSk(String faoctrSk) {
		this.faoctrSk = faoctrSk;
	}

	/**
	 * @return the faoctrId
	 */
	public String getFaoctrId() {
		return faoctrId;
	}

	/**
	 * @param faoctrId the faoctrId to set
	 */
	public void setFaoctrId(String faoctrId) {
		this.faoctrId = faoctrId;
	}

	/**
	 * @return the faoctrDesc
	 */
	public String getFaoctrDesc() {
		return faoctrDesc;
	}

	/**
	 * @param faoctrDesc the faoctrDesc to set
	 */
	public void setFaoctrDesc(String faoctrDesc) {
		this.faoctrDesc = faoctrDesc;
	}
}
