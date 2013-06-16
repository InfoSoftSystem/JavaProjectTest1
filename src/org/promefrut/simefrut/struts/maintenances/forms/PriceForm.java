package org.promefrut.simefrut.struts.maintenances.forms;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;

/**
 * @author HWM
 * 
 * XDoclet definition:
 * @struts.form name="pricesForm"
 */
public class PriceForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	private Double priceId;
	private Integer dateSk;
	private String dateField;
	private String ctrSk;
	private BigDecimal oriSk;
	private String oriCtrId;
	private String priceCopyFlag;
	private String oriCtrDesc;
	private String oriRegId;
	private String oriRegDesc;
	private String oriProvId;
	private String oriProvDesc;
	private String clevSk;
	private String tunitSk;
	private String sizeSk;
	private String quaSk;
	private String prodId;
	private String varId;
	private String ptypeId;
	private String ptypeSk;
	private String prdVarSk;
	
	private String priceInfUni;
	private String priceSupUni;
	private String priceInfKilo;
	private String priceSupKilo;
	
	private String dateFrom;
	private String dateTo;
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends PriceForm> clase = this.getClass();
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
		if(this.dateField == null || this.dateField.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.dateField.required"));
		} else {
			SimpleDateFormat format = new SimpleDateFormat("d/M/y");
			
			try{
				format.parse(this.dateField);
			}catch(ParseException e){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.dateField.required"));
			}
		}
		
		if(this.ctrSk == null || this.ctrSk.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.ctrSk.required"));
		}
		
		if(this.clevSk == null || "".equals(this.clevSk)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.clevSk.required"));
		}
		
		if(this.tunitSk == null || "".equals(this.tunitSk)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.tunitSk.required"));
		}
		
		if(this.sizeSk == null || "".equals(this.sizeSk)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.sizeSk.required"));
		}
		
		if(this.quaSk == null || "".equals(this.quaSk)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.quaSk.required"));
		}
		
		if(this.prodId == null || "".equals(this.prodId)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.prodId.required"));
		}
		
		/*if(this.prdVarSk == null || "".equals(this.prdVarSk)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.prdVarSk.required"));
		}//*/
		
		if(this.priceInfUni == null || "".equals(this.priceInfUni)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.priceInfUni.required"));
		}
		
		if(this.priceSupUni == null || "".equals(this.priceSupUni)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.priceSupUni.required"));
		}
		
		if(this.priceInfKilo == null || "".equals(this.priceInfKilo)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.priceInfKilo.required"));
		}
		
		if(this.priceSupKilo == null || "".equals(this.priceSupKilo)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.priceSupKilo.required"));
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		//if(this.priceId == null || this.priceId.equals("")) {
		if(this.priceId.doubleValue() <=0){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.priceId.required"));
		}
	}
	
	public void validateCopyData(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.dateFrom == null || this.dateFrom.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.dateFrom.required"));
		} else {
			SimpleDateFormat format = new SimpleDateFormat("d/M/y");
			
			try{
				format.parse(this.dateFrom);
			}catch(ParseException e){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.dateFrom.required"));
			}
		}
		
		if(this.dateTo == null || this.dateTo.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.dateTo.required"));
		} else {
			SimpleDateFormat format = new SimpleDateFormat("d/M/y");
			
			try{
				format.parse(this.dateTo);
			}catch(ParseException e){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.dateTo.required"));
			}
		}
		
		if(this.ctrSk == null || this.ctrSk.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.ctrSk.required"));
		}
	}
	
	public void validateCopyDataYesterday(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.dateField == null || this.dateField.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.dateFrom.required"));
		} else {
			SimpleDateFormat format = new SimpleDateFormat("d/M/y");
			
			try{
				format.parse(this.dateField);
			}catch(ParseException e){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.dateFrom.required"));
			}
		}
		
		if(this.ctrSk == null || this.ctrSk.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("price.ctrSk.required"));
		}
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
	 * @return the ctrSk
	 */
	public String getCtrSk() {
		return ctrSk;
	}

	/**
	 * @param ctrSk the ctrSk to set
	 */
	public void setCtrSk(String ctrSk) {
		this.ctrSk = ctrSk;
	}

	/**
	 * @return the oriCtrId
	 */
	public String getOriCtrId() {
		return oriCtrId;
	}

	/**
	 * @param oriCtrId the oriCtrId to set
	 */
	public void setOriCtrId(String oriCtrId) {
		this.oriCtrId = oriCtrId;
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
	 * @return the oriRegId
	 */
	public String getOriRegId() {
		return oriRegId;
	}

	/**
	 * @param oriRegId the oriRegId to set
	 */
	public void setOriRegId(String oriRegId) {
		this.oriRegId = oriRegId;
	}

	/**
	 * @return the oriRegDesc
	 */
	public String getOriRegDesc() {
		return oriRegDesc;
	}

	/**
	 * @param oriRegDesc the oriRegDesc to set
	 */
	public void setOriRegDesc(String oriRegDesc) {
		this.oriRegDesc = oriRegDesc;
	}

	/**
	 * @return the oriProvId
	 */
	public String getOriProvId() {
		return oriProvId;
	}

	/**
	 * @param oriProvId the oriProvId to set
	 */
	public void setOriProvId(String oriProvId) {
		this.oriProvId = oriProvId;
	}

	/**
	 * @return the oriProvDesc
	 */
	public String getOriProvDesc() {
		return oriProvDesc;
	}

	/**
	 * @param oriProvDesc the oriProvDesc to set
	 */
	public void setOriProvDesc(String oriProvDesc) {
		this.oriProvDesc = oriProvDesc;
	}

	/**
	 * @return the clevSk
	 */
	public String getClevSk() {
		return clevSk;
	}

	/**
	 * @param clevSk the clevSk to set
	 */
	public void setClevSk(String clevSk) {
		this.clevSk = clevSk;
	}

	/**
	 * @return the tunitSk
	 */
	public String getTunitSk() {
		return tunitSk;
	}

	/**
	 * @param tunitSk the tunitSk to set
	 */
	public void setTunitSk(String tunitSk) {
		this.tunitSk = tunitSk;
	}

	/**
	 * @return the sizeSk
	 */
	public String getSizeSk() {
		return sizeSk;
	}

	/**
	 * @param sizeSk the sizeSk to set
	 */
	public void setSizeSk(String sizeSk) {
		this.sizeSk = sizeSk;
	}

	/**
	 * @return the quaSk
	 */
	public String getQuaSk() {
		return quaSk;
	}

	/**
	 * @param quaSk the quaSk to set
	 */
	public void setQuaSk(String quaSk) {
		this.quaSk = quaSk;
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
	 * @return the priceInfUni
	 */
	public String getPriceInfUni() {
		return priceInfUni;
	}

	/**
	 * @param priceInfUni the priceInfUni to set
	 */
	public void setPriceInfUni(String priceInfUni) {
		this.priceInfUni = priceInfUni;
	}

	/**
	 * @return the priceSupUni
	 */
	public String getPriceSupUni() {
		return priceSupUni;
	}

	/**
	 * @param priceSupUni the priceSupUni to set
	 */
	public void setPriceSupUni(String priceSupUni) {
		this.priceSupUni = priceSupUni;
	}

	/**
	 * @return the priceInfKilo
	 */
	public String getPriceInfKilo() {
		return priceInfKilo;
	}

	/**
	 * @param priceInfKilo the priceInfKilo to set
	 */
	public void setPriceInfKilo(String priceInfKilo) {
		this.priceInfKilo = priceInfKilo;
	}

	/**
	 * @return the priceSupKilo
	 */
	public String getPriceSupKilo() {
		return priceSupKilo;
	}

	/**
	 * @param priceSupKilo the priceSupKilo to set
	 */
	public void setPriceSupKilo(String priceSupKilo) {
		this.priceSupKilo = priceSupKilo;
	}

	/**
	 * @return the priceId
	 */
	public Double getPriceId() {
		return priceId;
	}

	/**
	 * @param priceId the priceId to set
	 */
	public void setPriceId(Double priceId) {
		this.priceId = priceId;
	}
	
	/**
	 * @return the prdVarSk
	 */
	public String getPrdVarSk() {
		return prdVarSk;
	}

	/**
	 * @param prdVarSk the prdVarSk to set
	 */
	public void setPrdVarSk(String prdVarSk) {
		this.prdVarSk = prdVarSk;
	}

	/**
	 * @return the oriSk
	 */
	public BigDecimal getOriSk() {
		return oriSk;
	}

	/**
	 * @param oriSk the oriSk to set
	 */
	public void setOriSk(BigDecimal oriSk) {
		this.oriSk = oriSk;
	}

	/**
	 * @return the dateSk
	 */
	public Integer getDateSk() {
		return dateSk;
	}

	/**
	 * @param dateSk the dateSk to set
	 */
	public void setDateSk(Integer dateSk) {
		this.dateSk = dateSk;
	}

	/**
	 * @return the dateFrom
	 */
	public String getDateFrom() {
		return dateFrom;
	}

	/**
	 * @param dateFrom the dateFrom to set
	 */
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * @return the dateTo
	 */
	public String getDateTo() {
		return dateTo;
	}

	/**
	 * @param dateTo the dateTo to set
	 */
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	/**
	 * @return the priceCopyFlag
	 */
	public String getPriceCopyFlag() {
		return priceCopyFlag;
	}

	/**
	 * @param priceCopyFlag the priceCopyFlag to set
	 */
	public void setPriceCopyFlag(String priceCopyFlag) {
		this.priceCopyFlag = priceCopyFlag;
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
	 * @return the ptypeSk
	 */
	public String getPtypeSk() {
		return ptypeSk;
	}

	/**
	 * @param ptypeSk the ptypeSk to set
	 */
	public void setPtypeSk(String ptypeSk) {
		this.ptypeSk = ptypeSk;
	}
}
