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
 * @struts.form name="varietyForm"
 */
public class VarietyForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * prodId field
	 */
	private String prodId;
	
	/**
	 * prodCode field
	 */
	private String prodCode;
	
	/**
	 * prodDesc field
	 */
	private String prodDesc;
	
	/**
	 * varId field
	 */
	private String varId;
	
	/**
	 * varCode field
	 */
	private String varCode;
	
	/**
	 * varDesc field
	 */
	private String varDesc;
		
	/**
	 * varStatus field
	 */
	private String varStatus;
	
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends VarietyForm> clase = this.getClass();
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
		if(this.varDesc == null || this.varDesc.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("variety.varDesc.required"));
		} else {
			if(varDesc.length() > 100) {
				varDesc = varDesc.substring(0, 100);
			}
		}
		
		if(this.varCode == null || this.varCode.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("variety.varCode.required"));
		} else {
			if(varCode.length() > 10) {
				varCode = varCode.substring(0, 10);
			}
		}
		
		if(this.varStatus == null || "".equals(this.varStatus)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("variety.varStatus.required"));
		} else {
			if(varStatus.length() > 1) {
				varStatus = varStatus.substring(0, 1);
			}
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.varId == null || this.varId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("variety.varId.required"));
		}
	}

	/**
	 * @return the prodId
	 */
	public String getProdId() {
		return prodId;
	}

	/**
	 * @param prodId the prodId to set
	 */
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	/**
	 * @return the varId
	 */
	public String getVarId() {
		return varId;
	}

	/**
	 * @param varId the varId to set
	 */
	public void setVarId(String varId) {
		this.varId = varId;
	}

	/**
	 * @return the varCode
	 */
	public String getVarCode() {
		return varCode;
	}

	/**
	 * @param varCode the varCode to set
	 */
	public void setVarCode(String varCode) {
		this.varCode = varCode;
	}

	/**
	 * @return the varDesc
	 */
	public String getVarDesc() {
		return varDesc;
	}

	/**
	 * @param varDesc the varDesc to set
	 */
	public void setVarDesc(String varDesc) {
		this.varDesc = varDesc;
	}

	/**
	 * @return the varStatus
	 */
	public String getVarStatus() {
		return varStatus;
	}

	/**
	 * @param varStatus the varStatus to set
	 */
	public void setVarStatus(String varStatus) {
		this.varStatus = varStatus.toUpperCase();
	}

	/**
	 * @return the prodCode
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * @param prodCode the prodCode to set
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * @return the prodDesc
	 */
	public String getProdDesc() {
		return prodDesc;
	}

	/**
	 * @param prodDesc the prodDesc to set
	 */
	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}

}
