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
 * @struts.form name="productForm"
 */
public class ProductForm extends BaseForm {

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
	 * prodStatus field
	 */
	private String prodStatus;
	
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends ProductForm> clase = this.getClass();
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
		if(this.prodDesc == null || this.prodDesc.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("product.prodDesc.required"));
		} else {
			if(prodDesc.length() > 100) {
				prodDesc = prodDesc.substring(0, 100);
			}
		}
		
		if(this.prodCode == null || this.prodCode.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("product.prodCode.required"));
		} else {
			if(prodCode.length() > 10) {
				prodCode = prodCode.substring(0, 10);
			}
		}
		
		if(this.prodStatus == null || "".equals(this.prodStatus)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("product.prodStatus.required"));
		} else {
			if(prodStatus.length() > 1) {
				prodStatus = prodStatus.substring(0, 1);
			}
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.prodId == null || this.prodId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("product.prodId.required"));
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

	/**
	 * @return the prodStatus
	 */
	public String getProdStatus() {
		return prodStatus;
	}

	/**
	 * @param prodStatus the prodStatus to set
	 */
	public void setProdStatus(String prodStatus) {
		this.prodStatus = prodStatus.toUpperCase();
	}

}
