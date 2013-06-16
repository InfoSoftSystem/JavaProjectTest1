package org.promefrut.simefrut.struts.administration.forms;



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
 * @struts.form name="optionAppForm"
 */
public class OptionAppForm extends BaseForm {
	
	private static final long serialVersionUID = 7911293868566233546L;
	
	/**
	 * optId field
	 */
	private String optId;
	
	/**
	 * grpId field
	 */
	private String grpId;
	
	/**
	 * optDescfield
	 */
	private String optDesc;
	
	private String optUrl;
	
	private String optVisible;
	
	private String optIconCSS;
	
	private String optMsgProperty;
	
	private String optOrder;
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends OptionAppForm> clase = this.getClass();
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
		
		if(this.optDesc == null || this.optDesc.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("optionApp.optDesc.required"));
		} else {
			if(optDesc.length() > 200) {
				optDesc = optDesc.substring(0, 200);
			}
		}
		
		if(this.optUrl == null || this.optUrl.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("optionApp.optUrl.required"));
		}
		
		if(this.optOrder == null || this.optOrder.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("optionApp.optOrder.required"));
		}
		
		if(this.grpId == null || this.grpId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("optionApp.grpId.required"));
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.optId == null || this.optId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("optionApp.optId.required"));
		}
	}

	/**
	 * @return the optId
	 */
	public String getOptId() {
		return optId;
	}

	/**
	 * @param optId the optId to set
	 */
	public void setOptId(String optId) {
		this.optId = optId;
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
	 * @return the optDesc
	 */
	public String getOptDesc() {
		return optDesc;
	}

	/**
	 * @param optDesc the optDesc to set
	 */
	public void setOptDesc(String optDesc) {
		this.optDesc = optDesc;
	}

	/**
	 * @return the optUrl
	 */
	public String getOptUrl() {
		return optUrl;
	}

	/**
	 * @param optUrl the optUrl to set
	 */
	public void setOptUrl(String optUrl) {
		this.optUrl = optUrl;
	}

	/**
	 * @return the optVisible
	 */
	public String getOptVisible() {
		return optVisible;
	}

	/**
	 * @param optVisible the optVisible to set
	 */
	public void setOptVisible(String optVisible) {
		this.optVisible = optVisible;
	}

	/**
	 * @return the optIconCSS
	 */
	public String getOptIconCSS() {
		return "".equals(optIconCSS)?null:optIconCSS;
	}

	/**
	 * @param optIconCSS the optIconCSS to set
	 */
	public void setOptIconCSS(String optIconCSS) {
		this.optIconCSS = optIconCSS;
	}

	/**
	 * @return the optMsgProperty
	 */
	public String getOptMsgProperty() {
		return "".equals(optMsgProperty)?null:optMsgProperty;
	}

	/**
	 * @param optMsgProperty the optMsgProperty to set
	 */
	public void setOptMsgProperty(String optMsgProperty) {
		this.optMsgProperty = optMsgProperty;
	}

	/**
	 * @return the optOrder
	 */
	public String getOptOrder() {
		return optOrder;
	}

	/**
	 * @param optOrder the optOrder to set
	 */
	public void setOptOrder(String optOrder) {
		this.optOrder = optOrder;
	}
}
