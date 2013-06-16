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
 * @struts.form name="provinceForm"
 */
public class ProvinceForm extends BaseForm {

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
	 * provId field
	 */
	private String provId;
	
	/**
	 * provDesc field
	 */
	private String provDesc;
	
	/**
	 * provStatus field
	 */
	private String provStatus;
	
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends ProvinceForm> clase = this.getClass();
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
		
		if(this.regId == null || this.regId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("region.regId.required"));
		}
		
		if(this.provDesc == null || this.provDesc.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("province.provDesc.required"));
		} else {
			if(provDesc.length() > 100) {
				provDesc = provDesc.substring(0, 100);
			}
		}
		
		if(this.provStatus == null || "".equals(this.provStatus)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("province.provStatus.required"));
		} else {
			if(provStatus.length() > 1) {
				provStatus = provStatus.substring(0, 1);
			}
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.provId == null || this.provId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("province.provId.required"));
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
	 * @return the provId
	 */
	public String getProvId() {
		return provId;
	}

	/**
	 * @param provId the provId to set
	 */
	public void setProvId(String provId) {
		this.provId = provId;
	}

	/**
	 * @return the provDesc
	 */
	public String getProvDesc() {
		return provDesc;
	}

	/**
	 * @param provDesc the provDesc to set
	 */
	public void setProvDesc(String provDesc) {
		this.provDesc = provDesc;
	}

	/**
	 * @return the provStatus
	 */
	public String getProvStatus() {
		return provStatus;
	}

	/**
	 * @param provStatus the provStatus to set
	 */
	public void setProvStatus(String provStatus) {
		this.provStatus = provStatus.toUpperCase();
	}

}
