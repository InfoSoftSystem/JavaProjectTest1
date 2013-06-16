package org.promefrut.simefrut.struts.maintenances.forms;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;
import org.promefrut.simefrut.utils.LookUpResourceSchema;

/**
 * @author HWM
 * 
 * XDoclet definition:
 * @struts.form name="productionForm"
 */
public class ProductionForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	private Double productionId;
	private Integer yearSk;
	private BigDecimal ctrSk;
	private String ctrDesc;
	private String harvestedArea;
	private Integer prodSk;
	private String prodDesc;
	
	private String volProd;
	private String costProd;
	private String maintenanceCost;
	private String establishmentCost;
	private String productionSystem;
	
	private Boolean jan;
	private Boolean feb;
	private Boolean mar;
	private Boolean apr;
	private Boolean may;
	private Boolean jun;
	private Boolean jul;
	private Boolean aug;
	private Boolean sep;
	private Boolean oct;
	private Boolean nov;
	private Boolean dec;
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends ProductionForm> clase = this.getClass();
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
			                try{
			                	field[i].set(this, new BigDecimal(0));
			                }catch(Exception r){
			                	field[i].set(this, new Boolean(false));
			                }
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
		ResourceBundle mensajes = ResourceBundle.getBundle(LookUpResourceSchema.APPLICATION_RESOURCE,(Locale) request.getSession().getAttribute(Globals.LOCALE_KEY));
		
		if(this.ctrSk == null || this.ctrSk.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.ctrSk.required"));
		}
		
		if(this.yearSk == null || this.yearSk.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.yearSk.required"));
			
		}else if(this.yearSk < 1990){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.yearSk.outOfRange"));
		}
		
		if(this.prodSk == null || this.prodSk.intValue()<=0) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.prodSk.required"));
		}

		try{
			if(!StringUtils.isEmpty(this.volProd) && (new BigDecimal(this.volProd)).doubleValue()<=0) {
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.volProd.required"));
			}
		}catch(NumberFormatException e){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("error.generic.numberFormat", mensajes.getString("production.volProd")));
		}
		
		try{
			if(!StringUtils.isEmpty(this.costProd) && (new BigDecimal(this.costProd)).doubleValue()<=0) {
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.costProd.required"));
			}
		}catch(NumberFormatException e){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("error.generic.numberFormat", mensajes.getString("production.costProd")));
		}
		
		try{
			if(!StringUtils.isEmpty(this.harvestedArea) && (new BigDecimal(this.harvestedArea)).doubleValue()<=0) {
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.harvestedArea.required"));
			}
		}catch(NumberFormatException e){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("error.generic.numberFormat", mensajes.getString("production.harvestedArea")));
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.productionId.doubleValue() <=0){
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("production.productionId.required"));
		}
	}

	/**
	 * @return the productionId
	 */
	public Double getProductionId() {
		return productionId;
	}

	/**
	 * @param productionId the productionId to set
	 */
	public void setProductionId(Double productionId) {
		this.productionId = productionId;
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
	 * @return the jan
	 */
	public Boolean getJan() {
		return jan;
	}

	/**
	 * @param jan the jan to set
	 */
	public void setJan(Boolean jan) {
		this.jan = jan;
	}

	/**
	 * @return the feb
	 */
	public Boolean getFeb() {
		return feb;
	}

	/**
	 * @param feb the feb to set
	 */
	public void setFeb(Boolean feb) {
		this.feb = feb;
	}

	/**
	 * @return the mar
	 */
	public Boolean getMar() {
		return mar;
	}

	/**
	 * @param mar the mar to set
	 */
	public void setMar(Boolean mar) {
		this.mar = mar;
	}

	/**
	 * @return the apr
	 */
	public Boolean getApr() {
		return apr;
	}

	/**
	 * @param apr the apr to set
	 */
	public void setApr(Boolean apr) {
		this.apr = apr;
	}

	/**
	 * @return the may
	 */
	public Boolean getMay() {
		return may;
	}

	/**
	 * @param may the may to set
	 */
	public void setMay(Boolean may) {
		this.may = may;
	}

	/**
	 * @return the jun
	 */
	public Boolean getJun() {
		return jun;
	}

	/**
	 * @param jun the jun to set
	 */
	public void setJun(Boolean jun) {
		this.jun = jun;
	}

	/**
	 * @return the jul
	 */
	public Boolean getJul() {
		return jul;
	}

	/**
	 * @param jul the jul to set
	 */
	public void setJul(Boolean jul) {
		this.jul = jul;
	}

	/**
	 * @return the aug
	 */
	public Boolean getAug() {
		return aug;
	}

	/**
	 * @param aug the aug to set
	 */
	public void setAug(Boolean aug) {
		this.aug = aug;
	}

	/**
	 * @return the sep
	 */
	public Boolean getSep() {
		return sep;
	}

	/**
	 * @param sep the sep to set
	 */
	public void setSep(Boolean sep) {
		this.sep = sep;
	}

	/**
	 * @return the oct
	 */
	public Boolean getOct() {
		return oct;
	}

	/**
	 * @param oct the oct to set
	 */
	public void setOct(Boolean oct) {
		this.oct = oct;
	}

	/**
	 * @return the nov
	 */
	public Boolean getNov() {
		return nov;
	}

	/**
	 * @param nov the nov to set
	 */
	public void setNov(Boolean nov) {
		this.nov = nov;
	}

	/**
	 * @return the dec
	 */
	public Boolean getDec() {
		return dec;
	}

	/**
	 * @param dec the dec to set
	 */
	public void setDec(Boolean dec) {
		this.dec = dec;
	}

	/**
	 * @return the harvestedArea
	 */
	public String getHarvestedArea() {
		return harvestedArea;
	}

	/**
	 * @param harvestedArea the harvestedArea to set
	 */
	public void setHarvestedArea(String harvestedArea) {
		this.harvestedArea = harvestedArea;
	}

	/**
	 * @return the volProd
	 */
	public String getVolProd() {
		return volProd;
	}

	/**
	 * @param volProd the volProd to set
	 */
	public void setVolProd(String volProd) {
		this.volProd = volProd;
	}

	/**
	 * @return the costProd
	 */
	public String getCostProd() {
		return costProd;
	}

	/**
	 * @param costProd the costProd to set
	 */
	public void setCostProd(String costProd) {
		this.costProd = costProd;
	}

	/**
	 * @return the maintenanceCost
	 */
	public String getMaintenanceCost() {
		return maintenanceCost;
	}

	/**
	 * @param maintenanceCost the maintenanceCost to set
	 */
	public void setMaintenanceCost(String maintenanceCost) {
		this.maintenanceCost = maintenanceCost;
	}

	/**
	 * @return the establishmentCost
	 */
	public String getEstablishmentCost() {
		return establishmentCost;
	}

	/**
	 * @param establishmentCost the establishmentCost to set
	 */
	public void setEstablishmentCost(String establishmentCost) {
		this.establishmentCost = establishmentCost;
	}

	/**
	 * @return the productionSystem
	 */
	public String getProductionSystem() {
		return productionSystem;
	}

	/**
	 * @param productionSystem the productionSystem to set
	 */
	public void setProductionSystem(String productionSystem) {
		this.productionSystem = productionSystem;
	}
}
