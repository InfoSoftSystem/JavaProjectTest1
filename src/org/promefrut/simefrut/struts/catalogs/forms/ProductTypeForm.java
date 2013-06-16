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
 * @struts.form name="productTypeForm"
 */
public class ProductTypeForm extends BaseForm {

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
	 * ptypeId field
	 */
	private String ptypeId;
	
	/**
	 * ptypeCode field
	 */
	private String ptypeCode;
	
	/**
	 * ptypeDesc field
	 */
	private String ptypeDesc;
		
	/**
	 * ptypeStatus field
	 */
	private String ptypeStatus;
	
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends ProductTypeForm> clase = this.getClass();
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
		if(this.ptypeDesc == null || this.ptypeDesc.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productType.ptypeDesc.required"));
		} else {
			if(ptypeDesc.length() > 100) {
				ptypeDesc = ptypeDesc.substring(0, 100);
			}
		}
		
		if(this.ptypeCode == null || this.ptypeCode.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productType.ptypeCode.required"));
		} else {
			if(ptypeCode.length() > 10) {
				ptypeCode = ptypeCode.substring(0, 10);
			}
		}
		
		if(this.ptypeStatus == null || "".equals(this.ptypeStatus)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productType.ptypeStatus.required"));
		} else {
			if(ptypeStatus.length() > 1) {
				ptypeStatus = ptypeStatus.substring(0, 1);
			}
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.ptypeId == null || this.ptypeId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("productType.ptypeId.required"));
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
	 * @return the ptypeId
	 */
	public String getPtypeId() {
		return ptypeId;
	}

	/**
	 * @param ptypeId the ptypeId to set
	 */
	public void setPtypeId(String ptypeId) {
		this.ptypeId = ptypeId;
	}

	/**
	 * @return the ptypeCode
	 */
	public String getPtypeCode() {
		return ptypeCode;
	}

	/**
	 * @param ptypeCode the ptypeCode to set
	 */
	public void setPtypeCode(String ptypeCode) {
		this.ptypeCode = ptypeCode;
	}

	/**
	 * @return the ptypeDesc
	 */
	public String getPtypeDesc() {
		return ptypeDesc;
	}

	/**
	 * @param ptypeDesc the ptypeDesc to set
	 */
	public void setPtypeDesc(String ptypeDesc) {
		this.ptypeDesc = ptypeDesc;
	}

	/**
	 * @return the ptypeStatus
	 */
	public String getPtypeStatus() {
		return ptypeStatus;
	}

	/**
	 * @param ptypeStatus the ptypeStatus to set
	 */
	public void setPtypeStatus(String ptypeStatus) {
		this.ptypeStatus = ptypeStatus.toUpperCase();
	}

}
