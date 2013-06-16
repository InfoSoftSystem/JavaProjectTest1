package org.promefrut.simefrut.struts.administration.forms;



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
 * @struts.form name="parametersForm"
 */
public class ParametersForm extends BaseForm {
	
	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = -1L;
	
	public static final String priceToleranceID ="PRICE_TOLERANCE";
	private String priceTolerance;
	
	public static final String productionToleranceID ="PRODUCTION_TOLERANCE";
	private String productionTolerance;
	
	public static final String emailAccountID ="EMAIL_ACCOUNT";
	private String emailAccount;
	
	public static final String emailPasswordID ="EMAIL_PASSWORD";
	private String emailPassword;
	
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    /*
	    Class<? extends ParametersForm> clase = this.getClass();
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
	    }*/
	}

	public void validateInsert(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(StringUtils.isBlank(this.priceTolerance)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("parameters.priceTolerance.required"));
		}
		
		if(StringUtils.isBlank(this.productionTolerance)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("parameters.productionTolerance.required"));
		}
		
		if(StringUtils.isBlank(this.emailAccount)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("parameters.emailAccount.required"));
		} else {
			if(this.emailAccount.length() > 200) {
				this.emailAccount = this.emailAccount.substring(0, 200);
			}
		}
		
		if(StringUtils.isBlank(this.emailPassword)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("parameters.emailPassword.required"));
		} else {
			if(this.emailPassword.length() > 200) {
				this.emailPassword = this.emailPassword.substring(0, 200);
			}
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
	}

	/**
	 * @return the priceTolerance
	 */
	public String getPriceTolerance() {
		return priceTolerance;
	}

	/**
	 * @param priceTolerance the priceTolerance to set
	 */
	public void setPriceTolerance(String priceTolerance) {
		this.priceTolerance = priceTolerance;
	}

	/**
	 * @return the productionTolerance
	 */
	public String getProductionTolerance() {
		return productionTolerance;
	}

	/**
	 * @param productionTolerance the productionTolerance to set
	 */
	public void setProductionTolerance(String productionTolerance) {
		this.productionTolerance = productionTolerance;
	}

	/**
	 * @return the emailAccount
	 */
	public String getEmailAccount() {
		return emailAccount;
	}

	/**
	 * @param emailAccount the emailAccount to set
	 */
	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}

	/**
	 * @return the emailPassword
	 */
	public String getEmailPassword() {
		return emailPassword;
	}

	/**
	 * @param emailPassword the emailPassword to set
	 */
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

}
