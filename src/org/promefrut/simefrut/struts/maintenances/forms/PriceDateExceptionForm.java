package org.promefrut.simefrut.struts.maintenances.forms;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
 * @struts.form name="priceDateExceptionForm"
 */
public class PriceDateExceptionForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	private Integer pdeId;
	private String dateField;
	private Integer ctrId;
	private Integer prodId;
	private Integer ptypeId;
	private Integer varId;
	private String pdeComment;
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends PriceDateExceptionForm> clase = this.getClass();
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
		
		if(this.ctrId == null || this.ctrId.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("priceDateException.ctrId.required"));
		}
		
		if(this.dateField == null || this.dateField.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("priceDateException.dateField.required"));
		} else {
			SimpleDateFormat format = new SimpleDateFormat("d/M/y");
			
			try{
				format.parse(this.dateField);
			}catch(ParseException e){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("priceDateException.dateField.required"));
			}
		}
		
		/*
		if(this.prodId == null || this.prodId.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("priceDateException.prodId.required"));
		}

		if(this.ptypeId == null || this.ptypeId.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("priceDateException.ptypeId.required"));
		}
		
		if(this.varId == null || this.varId.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("priceDateException.varId.required"));
		}*/
		
		if(this.pdeComment == null || StringUtils.isEmpty(this.pdeComment)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("priceDateException.pdeComment.required"));
		}
		
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.pdeId.doubleValue() <=0){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("priceDateException.pdeId.required"));
		}
	}

	/**
	 * @return the pdeId
	 */
	public Integer getPdeId() {
		return pdeId;
	}

	/**
	 * @param pdeId the pdeId to set
	 */
	public void setPdeId(Integer pdeId) {
		this.pdeId = pdeId;
	}

	/**
	 * @return the dateField
	 */
	public String getDateField() {
		return dateField;
	}

	/**
	 * @param dateField the dateField to set
	 */
	public void setDateField(String dateField) {
		this.dateField = dateField;
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
	 * @return the pdeComment
	 */
	public String getPdeComment() {
		return pdeComment;
	}

	/**
	 * @param pdeComment the pdeComment to set
	 */
	public void setPdeComment(String pdeComment) {
		this.pdeComment = pdeComment;
	}

}
