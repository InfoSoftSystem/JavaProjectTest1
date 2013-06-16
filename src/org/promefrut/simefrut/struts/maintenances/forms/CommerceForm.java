package org.promefrut.simefrut.struts.maintenances.forms;

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
 * @struts.form name="commerceForm"
 */
public class CommerceForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	private Double commId;
	private Integer yearSk;
	private BigDecimal ctrSk;
	private String ctrDesc;
	private BigDecimal oriCtrSk;
	private String oriCtrDesc;
	private String commType;
	private Integer prodSk;
	private String prodDesc;
	
	private BigDecimal volKilo;
	private BigDecimal volDollar;
	
	private BigDecimal commCIFOB;
	
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends CommerceForm> clase = this.getClass();
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
		if(this.ctrSk == null || this.ctrSk.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("comm.ctrSk.required"));
		}
		
		if(this.yearSk == null || this.yearSk.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("comm.yearSk.required"));
			
		}else if(this.yearSk < 1990){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("comm.yearSk.outOfRange"));
		}
		
		if(this.prodSk == null || this.prodSk.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("comm.prodSk.required"));
		}

		if(this.volKilo == null || this.volKilo.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("comm.volKilo.required"));
		}
		
		if(this.volDollar == null || this.volDollar.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("comm.volDollar.required"));
		}
		
		if(!(this.volKilo == null || this.volKilo.intValue()<=0) && !(this.volDollar == null || this.volDollar.intValue()<=0)) {
			this.commCIFOB = new BigDecimal(this.volDollar.doubleValue()/this.volKilo.doubleValue()); 
		}
		
		if(this.oriCtrSk == null || this.oriCtrSk.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("comm.oriCtrSk.required"));
		}
		
		if(this.commType == null || "".equals(this.commType)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("comm.commType.required"));
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.commId.doubleValue() <=0){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("comm.commId.required"));
		}
	}

	/**
	 * @return the commId
	 */
	public Double getCommId() {
		return commId;
	}

	/**
	 * @param commId the commId to set
	 */
	public void setCommId(Double commId) {
		this.commId = commId;
	}

	/**
	 * @return the yearSk
	 */
	public Integer getYearSk() {
		return yearSk;
	}

	/**
	 * @param yearSk the yearSk to set
	 */
	public void setYearSk(Integer yearSk) {
		this.yearSk = yearSk;
	}

	/**
	 * @return the ctrSk
	 */
	public BigDecimal getCtrSk() {
		return ctrSk;
	}

	/**
	 * @param ctrSk the ctrSk to set
	 */
	public void setCtrSk(BigDecimal ctrSk) {
		this.ctrSk = ctrSk;
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

	/**
	 * @return the prodSk
	 */
	public Integer getProdSk() {
		return prodSk;
	}

	/**
	 * @param prodSk the prodSk to set
	 */
	public void setProdSk(Integer prodSk) {
		this.prodSk = prodSk;
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
	 * @return the oriCtrSk
	 */
	public BigDecimal getOriCtrSk() {
		return oriCtrSk;
	}

	/**
	 * @param oriCtrSk the oriCtrSk to set
	 */
	public void setOriCtrSk(BigDecimal oriCtrSk) {
		this.oriCtrSk = oriCtrSk;
	}

	/**
	 * @return the oriCtrDesc
	 */
	public String getOriCtrDesc() {
		return oriCtrDesc;
	}

	/**
	 * @param oriCtrDesc the oriCtrDesc to set
	 */
	public void setOriCtrDesc(String oriCtrDesc) {
		this.oriCtrDesc = oriCtrDesc;
	}

	/**
	 * @return the commType
	 */
	public String getCommType() {
		return commType;
	}

	/**
	 * @param commType the commType to set
	 */
	public void setCommType(String commType) {
		this.commType = commType;
	}

	/**
	 * @return the volKilo
	 */
	public BigDecimal getVolKilo() {
		return volKilo;
	}

	/**
	 * @param volKilo the volKilo to set
	 */
	public void setVolKilo(BigDecimal volKilo) {
		this.volKilo = volKilo;
	}

	/**
	 * @return the volDollar
	 */
	public BigDecimal getVolDollar() {
		return volDollar;
	}

	/**
	 * @param volDollar the volDollar to set
	 */
	public void setVolDollar(BigDecimal volDollar) {
		this.volDollar = volDollar;
	}

	/**
	 * @return the commCIFOB
	 */
	public BigDecimal getCommCIFOB() {
		return commCIFOB;
	}

	/**
	 * @param commCIFOB the commCIFOB to set
	 */
	public void setCommCIFOB(BigDecimal commCIFOB) {
		this.commCIFOB = commCIFOB;
	}
}
