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
 * @struts.form name="regionForm"
 */
public class RegionForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ctrId field
	 */
	private String ctrId;
	
	/**
	 * regId field
	 */
	private String regId;
	
	/**
	 * regDesc field
	 */
	private String regDesc;
		
	/**
	 * regStatus field
	 */
	private String regStatus;
	
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends RegionForm> clase = this.getClass();
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
		
		if(this.regDesc == null || this.regDesc.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("region.regDesc.required"));
		} else {
			if(regDesc.length() > 100) {
				regDesc = regDesc.substring(0, 100);
			}
		}
		
		if(this.regStatus == null || "".equals(this.regStatus)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("region.regStatus.required"));
		} else {
			if(regStatus.length() > 1) {
				regStatus = regStatus.substring(0, 1);
			}
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.regId == null || this.regId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("region.regId.required"));
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
	 * @return the regId
	 */
	public String getRegId() {
		return regId;
	}

	/**
	 * @param regId the regId to set
	 */
	public void setRegId(String regId) {
		this.regId = regId;
	}

	/**
	 * @return the regDesc
	 */
	public String getRegDesc() {
		return regDesc;
	}

	/**
	 * @param regDesc the regDesc to set
	 */
	public void setRegDesc(String regDesc) {
		this.regDesc = regDesc;
	}

	/**
	 * @return the regStatus
	 */
	public String getRegStatus() {
		return regStatus;
	}

	/**
	 * @param regStatus the regStatus to set
	 */
	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus.toUpperCase();
	}
}
