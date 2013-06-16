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
 * @struts.form name="countryForm"
 */
public class CountryForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = -6853424419133663206L;

	
	/**
	 * ctrId field
	 */
	private String ctrId;
	
	/**
	 * ctrISO3 field
	 */
	private String ctrISO3;
	
	/**
	 * ctrDescSpanish field
	 */
	private String ctrDescSpanish;
	
	/**
	 * ctrDescEnglish field
	 */
	private String ctrDescEnglish;
	
	/**
	 * ctrStatus field
	 */
	private String ctrStatus;
	
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends CountryForm> clase = this.getClass();
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
		if(this.ctrISO3 == null || this.ctrISO3.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("country.ctrISO3.required"));
		} else {
			if(ctrISO3.length() > 3) {
				ctrISO3 = ctrISO3.substring(0, 3);
			}
		}

		if(this.ctrDescSpanish == null || this.ctrDescSpanish.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("country.ctrDescSpanish.required"));
		} else {
			if(ctrDescSpanish.length() > 100) {
				ctrDescSpanish = ctrDescSpanish.substring(0, 100);
			}
		}
		
		if(this.ctrDescEnglish == null || this.ctrDescEnglish.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("country.ctrDescEnglish.required"));
		} else {
			if(ctrDescEnglish.length() > 100) {
				ctrDescEnglish = ctrDescEnglish.substring(0, 100);
			}
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.ctrId == null || this.ctrId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("country.ctrId.required"));
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
	 * @return the ctrISO3
	 */
	public String getCtrISO3() {
		return ctrISO3;
	}

	/**
	 * @param ctrISO3 the ctrISO3 to set
	 */
	public void setCtrISO3(String ctrISO3) {
		this.ctrISO3 = ctrISO3.toUpperCase();
	}

	/**
	 * @return the ctrDescSpanish
	 */
	public String getCtrDescSpanish() {
		return ctrDescSpanish;
	}

	/**
	 * @param ctrDescSpanish the ctrDescSpanish to set
	 */
	public void setCtrDescSpanish(String ctrDescSpanish) {
		this.ctrDescSpanish = ctrDescSpanish;
	}

	/**
	 * @return the ctrDescEnglish
	 */
	public String getCtrDescEnglish() {
		return ctrDescEnglish;
	}

	/**
	 * @param ctrDescEnglish the ctrDescEnglish to set
	 */
	public void setCtrDescEnglish(String ctrDescEnglish) {
		this.ctrDescEnglish = ctrDescEnglish;
	}

	/**
	 * @return the ctrStatus
	 */
	public String getCtrStatus() {
		return ctrStatus;
	}

	/**
	 * @param ctrStatus the ctrStatus to set
	 */
	public void setCtrStatus(String ctrStatus) {
		this.ctrStatus = ctrStatus.toUpperCase();
	}
}
