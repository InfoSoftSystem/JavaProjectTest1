package org.promefrut.simefrut.struts.catalogs.forms;

import java.lang.reflect.Field;
import java.math.BigDecimal;

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
 * @struts.form name="schedulePriceForm"
 */
public class SchedulePriceInsertionForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * spiId field
	 */
	private Integer spiId;
	
	/**
	 * spiValue field
	 */
	private String spiValue;
	
	/**
	 * ctrId field
	 */
	private Integer ctrId;
	
	/**
	 * ctrDesc field
	 */
	private String ctrDesc;
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends SchedulePriceInsertionForm> clase = this.getClass();
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
		if(StringUtils.isBlank(this.spiValue)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("schedulePriceInsertion.spiValue.required"));
		} else {
			if(spiValue.length() > 500) {
				spiValue = spiValue.substring(0, 500);
			}
		}
		
		if(this.ctrId == null || this.ctrId.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("schedulePriceInsertion.ctrId.required"));
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.spiId == null || this.spiId.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("schedulePriceInsertion.spiId.required"));
		}
	}
	
	
	/**
	 * @return the spiId
	 */
	public Integer getSpiId() {
		return spiId;
	}

	/**
	 * @param spiId the spiId to set
	 */
	public void setSpiId(Integer spiId) {
		this.spiId = spiId;
	}

	/**
	 * @return the spiValue
	 */
	public String getSpiValue() {
		return spiValue;
	}

	/**
	 * @param spiValue the spiValue to set
	 */
	public void setSpiValue(String spiValue) {
		this.spiValue = spiValue;
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
	 * @return the ctrDesc
	 */
	public String getCtrDesc() {
		return ctrDesc;
	}

	/**
	 * @param ctrDesc the ctrDesc to set
	 */
	public void setCtrDesc(String ctrDesc) {
		this.ctrDesc = ctrDesc;
	}
}
