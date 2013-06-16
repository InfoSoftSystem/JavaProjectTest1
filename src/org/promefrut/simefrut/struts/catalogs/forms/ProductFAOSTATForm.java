package org.promefrut.simefrut.struts.catalogs.forms;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;

/**
 * @author HWM
 * 
 * XDoclet definition:
 * @struts.form name="productFAOSTATForm"
 */
public class ProductFAOSTATForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * grpId field
	 */
	private String grpId;
	
	private String faoprodSk;
	private String prodSk;
	private String faoprodCode;
	private String faoprodDescSpa;
	private String faoprodDescEng;
	
		
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends ProductFAOSTATForm> clase = this.getClass();
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
		if(this.faoprodDescSpa == null || this.faoprodDescSpa.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productFAOSTAT.faoprodDescSpa.required"));
		} else {
			if(faoprodDescSpa.length() > 100) {
				faoprodDescSpa = faoprodDescSpa.substring(0, 100);
			}
		}
		
		if(this.faoprodDescEng == null || this.faoprodDescEng.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productFAOSTAT.faoprodDescEng.required"));
		} else {
			if(faoprodDescEng.length() > 100) {
				faoprodDescEng = faoprodDescEng.substring(0, 100);
			}
		}
		
		if(this.grpId == null || this.grpId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productFAOSTAT.grpId.required"));
		}
				
		if(StringUtils.isBlank(faoprodCode)){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productFAOSTAT.faoprodCode.required"));
		}
		
		if(StringUtils.isBlank(faoprodDescSpa)){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productFAOSTAT.faoprodDescSpa.required"));
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.faoprodSk == null || this.faoprodSk.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productFAOSTAT.faoprodSk.required"));
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
	 * @return the faoprodSk
	 */
	public String getFaoprodSk() {
		return faoprodSk;
	}

	/**
	 * @param faoprodSk the faoprodSk to set
	 */
	public void setFaoprodSk(String faoprodSk) {
		this.faoprodSk = faoprodSk;
	}

	/**
	 * @return the prodSk
	 */
	public String getProdSk() {
		return prodSk;
	}

	/**
	 * @param prodSk the prodSk to set
	 */
	public void setProdSk(String prodSk) {
		this.prodSk = prodSk;
	}

	/**
	 * @return the faoprodCode
	 */
	public String getFaoprodCode() {
		return faoprodCode;
	}

	/**
	 * @param faoprodCode the faoprodCode to set
	 */
	public void setFaoprodCode(String faoprodCode) {
		this.faoprodCode = faoprodCode;
	}

	/**
	 * @return the faoprodDescSpa
	 */
	public String getFaoprodDescSpa() {
		return faoprodDescSpa;
	}

	/**
	 * @param faoprodDescSpa the faoprodDescSpa to set
	 */
	public void setFaoprodDescSpa(String faoprodDescSpa) {
		this.faoprodDescSpa = faoprodDescSpa;
	}

	/**
	 * @return the faoprodDescEng
	 */
	public String getFaoprodDescEng() {
		return faoprodDescEng;
	}

	/**
	 * @param faoprodDescEng the faoprodDescEng to set
	 */
	public void setFaoprodDescEng(String faoprodDescEng) {
		this.faoprodDescEng = faoprodDescEng;
	}
}
