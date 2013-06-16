package org.promefrut.simefrut.struts.administration.forms;



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
 * @struts.form name="optionGroupForm"
 */
public class OptionGroupForm extends BaseForm {
	
	private static final long serialVersionUID = 228540461949542822L;
	/**
	 * grpId field
	 */
	private String grpId;
	
	/**
	 * grpDescfield
	 */
	private String grpDesc;
	
	private String grpIconCSS;
	
	private String grpOrder;
	
	private String grpMsgProperty;
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends OptionGroupForm> clase = this.getClass();
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
		if(this.grpDesc == null || this.grpDesc.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("optionGroup.grpDesc.required"));
		} else {
			if(grpDesc.length() > 100) {
				grpDesc = grpDesc.substring(0, 100);
			}
		}
		
		if(this.grpOrder == null || this.grpOrder.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("optionGroup.grpOrder.required"));
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.grpId == null || this.grpId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("optionGroup.grpId.required"));
		}
	}

	/**
	 * @return the grpId
	 */
	public String getGrpId() {
		return grpId;
	}

	/**
	 * @param grpId the grpId to set
	 */
	public void setGrpId(String grpId) {
		this.grpId = grpId;
	}

	/**
	 * @return the grpDesc
	 */
	public String getGrpDesc() {
		return grpDesc;
	}

	/**
	 * @param grpDesc the grpDesc to set
	 */
	public void setGrpDesc(String grpDesc) {
		this.grpDesc = grpDesc.trim();
	}

	/**
	 * @return the grpIconCSS
	 */
	public String getGrpIconCSS() {
		return grpIconCSS;
	}

	/**
	 * @param grpIconCSS the grpIconCSS to set
	 */
	public void setGrpIconCSS(String grpIconCSS) {
		this.grpIconCSS = grpIconCSS.trim();
	}

	/**
	 * @return the grpOrder
	 */
	public String getGrpOrder() {
		return grpOrder;
	}

	/**
	 * @param grpOrder the grpOrder to set
	 */
	public void setGrpOrder(String grpOrder) {
		this.grpOrder = grpOrder;
	}

	/**
	 * @return the grpMsgProperty
	 */
	public String getGrpMsgProperty() {
		return grpMsgProperty;
	}

	/**
	 * @param grpMsgProperty the grpMsgProperty to set
	 */
	public void setGrpMsgProperty(String grpMsgProperty) {
		this.grpMsgProperty = grpMsgProperty;
	}
}
