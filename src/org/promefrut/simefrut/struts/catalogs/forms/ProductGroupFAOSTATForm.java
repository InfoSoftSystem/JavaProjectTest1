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
 * @struts.form name="productGroupFAOSTATForm"
 */
public class ProductGroupFAOSTATForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * grpId field
	 */
	private String grpId;
	
	/**
	 * ctrDescSpanish field
	 */
	private String grpDesc;
		
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends ProductGroupFAOSTATForm> clase = this.getClass();
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
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productGroupFAOSTAT.grpDesc.required"));
		} else {
			if(grpDesc.length() > 200) {
				grpDesc = grpDesc.substring(0, 200);
			}
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.grpId == null || this.grpId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productGroupFAOSTAT.grpId.required"));
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
		this.grpDesc = grpDesc;
	}
}
