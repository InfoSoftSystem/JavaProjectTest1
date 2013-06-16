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
 * @struts.form name="qualityForm"
 */
public class QualityForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * qualityId field
	 */
	private String qualityId;
	
	/**
	 * qualityCode field
	 */
	private String qualityCode;
	
	/**
	 * ctrDescSpanish field
	 */
	private String qualityDesc;
		
	/**
	 * qualityStatus field
	 */
	private String qualityStatus;
	
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends QualityForm> clase = this.getClass();
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
		if(this.qualityCode == null || this.qualityCode.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("quality.qualityCode.required"));
		} else {
			if(qualityCode.length() > 10) {
				qualityCode = qualityCode.substring(0, 10);
			}
		}
		if(this.qualityDesc == null || this.qualityDesc.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("quality.qualityDesc.required"));
		} else {
			if(qualityDesc.length() > 100) {
				qualityDesc = qualityDesc.substring(0, 100);
			}
		}
		
		if(this.qualityStatus == null || "".equals(this.qualityStatus)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("quality.qualityStatus.required"));
		} else {
			if(qualityStatus.length() > 1) {
				qualityStatus = qualityStatus.substring(0, 1);
			}
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.qualityId == null || this.qualityId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("quality.qualityId.required"));
		}
	}

	/**
	 * @return the qualityId
	 */
	public String getQualityId() {
		return qualityId;
	}

	/**
	 * @param qualityId the qualityId to set
	 */
	public void setQualityId(String qualityId) {
		this.qualityId = qualityId;
	}

	/**
	 * @return the qualityDesc
	 */
	public String getQualityDesc() {
		return qualityDesc;
	}

	/**
	 * @param qualityDesc the qualityDesc to set
	 */
	public void setQualityDesc(String qualityDesc) {
		this.qualityDesc = qualityDesc;
	}

	/**
	 * @return the qualityStatus
	 */
	public String getQualityStatus() {
		return qualityStatus;
	}

	/**
	 * @param qualityStatus the qualityStatus to set
	 */
	public void setQualityStatus(String qualityStatus) {
		this.qualityStatus = qualityStatus.toUpperCase();
	}

	/**
	 * @return the qualityCode
	 */
	public String getQualityCode() {
		return qualityCode;
	}

	/**
	 * @param qualityCode the qualityCode to set
	 */
	public void setQualityCode(String qualityCode) {
		this.qualityCode = qualityCode;
	}

}
