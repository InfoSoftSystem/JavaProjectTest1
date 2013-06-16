package org.promefrut.simefrut.struts.catalogs.forms;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;

/**
 * @author HWM
 * 
 * XDoclet definition:
 * @struts.form name="traditionalUnitForm"
 */
/**
 * @author HWM
 *
 */
public class TraditionalUnitForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * tunitId field
	 */
	private Integer tunitId;
	
	/**
	 * tunitCode field
	 */
	private String tunitCode;
	
	/**
	 * tunitDesc field
	 */
	private String tunitDesc;
		
	/**
	 * tunitStatus field
	 */
	private String tunitStatus;
	
	/**
	 * ctrId field
	 */
	private Integer ctrId;
	
	/**
	 * prodId field
	 */
	private Integer prodId;
	
	/**
	 * varId field
	 */
	private Integer varId;
	
	/**
	 * sizeId field
	 */
	private Integer sizeId;
	
	/**
	 * tunitKilo field
	 */
	private Double tunitKilo;
	
	/**
	 * ptypeId field
	 */
	private Integer ptypeId;
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends TraditionalUnitForm> clase = this.getClass();
	    Field field[] = clase.getDeclaredFields();
	    try {
	        for(int i = 0; i < field.length; i++) {
	            try {
	            	if(!"serialVersionUID".equals(field[i].getName())){
	            		field[i].set(this, "");
	            	};
	            } catch(Exception e) {
	                try{
	                	field[i].set(this, new Double(0));
	                } catch(Exception ee) {
		                try{
		                	field[i].set(this, new Integer(0));
		                } catch(Exception eee) {
			                field[i].set(this, new BigDecimal(0));
			            }
		            }
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
		if(this.tunitDesc == null || this.tunitDesc.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("tunit.tunitDesc.required"));
		} else {
			if(tunitDesc.length() > 100) {
				tunitDesc = tunitDesc.substring(0, 100);
			}
		}
		
		/*
		if(this.tunitCode == null || this.tunitCode.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("tunit.tunitCode.required"));
		} else {
			if(tunitCode.length() > 10) {
				tunitCode = tunitCode.substring(0, 10);
			}
		}
		*/
		
		if(this.tunitStatus == null || "".equals(this.tunitStatus)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("tunit.tunitStatus.required"));
		} else {
			if(tunitStatus.length() > 1) {
				tunitStatus = tunitStatus.substring(0, 1);
			}
		}
		
		if(this.ctrId == null || this.ctrId.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("tunit.ctrId.required"));
		}
		
		if(this.prodId == null || this.prodId.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("tunit.prodId.required"));
		}
				
		if(this.tunitKilo == null || this.tunitKilo.doubleValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("tunit.tunitKilo.required"));
		}
		
		if(this.sizeId == null || this.sizeId.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("tunit.sizeId.required"));
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.tunitId == null || this.tunitId.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("tunit.tunitId.required"));
		}
	}
	/**
	 * @return the tunitStatus
	 */
	public String getTunitStatus() {
		return tunitStatus;
	}

	/**
	 * @param tunitStatus the tunitStatus to set
	 */
	public void setTunitStatus(String tunitStatus) {
		this.tunitStatus = tunitStatus.toUpperCase();
	}

	/**
	 * @return the tunitId
	 */
	public Integer getTunitId() {
		return tunitId;
	}

	/**
	 * @param tunitId the tunitId to set
	 */
	public void setTunitId(Integer tunitId) {
		this.tunitId = tunitId;
	}

	/**
	 * @return the tunitCode
	 */
	public String getTunitCode() {
		return tunitCode;
	}

	/**
	 * @param tunitCode the tunitCode to set
	 */
	public void setTunitCode(String tunitCode) {
		this.tunitCode = tunitCode;
	}

	/**
	 * @return the tunitDesc
	 */
	public String getTunitDesc() {
		return tunitDesc;
	}

	/**
	 * @param tunitDesc the tunitDesc to set
	 */
	public void setTunitDesc(String tunitDesc) {
		this.tunitDesc = tunitDesc;
	}

	/**
	 * @return the ctrId
	 */
	public Integer getCtrId() {
		return ctrId;
	}

	/**
	 * @param ctrId the ctrId to set
	 */
	public void setCtrId(Integer ctrId) {
		this.ctrId = ctrId;
	}

	/**
	 * @return the prodId
	 */
	public Integer getProdId() {
		return prodId;
	}

	/**
	 * @param prodId the prodId to set
	 */
	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	/**
	 * @return the varId
	 */
	public Integer getVarId() {
		return varId;
	}

	/**
	 * @param varId the varId to set
	 */
	public void setVarId(Integer varId) {
		this.varId = varId;
	}

	/**
	 * @return the sizeId
	 */
	public Integer getSizeId() {
		return sizeId;
	}

	/**
	 * @param sizeId the sizeId to set
	 */
	public void setSizeId(Integer sizeId) {
		this.sizeId = sizeId;
	}

	/**
	 * @return the tunitKilo
	 */
	public Double getTunitKilo() {
		return tunitKilo;
	}

	/**
	 * @param tunitKilo the tunitKilo to set
	 */
	public void setTunitKilo(Double tunitKilo) {
		this.tunitKilo = tunitKilo;
	}

	/**
	 * @return the ptypeId
	 */
	public Integer getPtypeId() {
		return ptypeId;
	}

	/**
	 * @param ptypeId the ptypeId to set
	 */
	public void setPtypeId(Integer ptypeId) {
		this.ptypeId = ptypeId;
	}
}
