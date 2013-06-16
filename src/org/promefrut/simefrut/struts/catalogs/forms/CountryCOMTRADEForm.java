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
 * @struts.form name="countryCOMTRADEForm"
 */
public class CountryCOMTRADEForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ctrId field
	 */
	private String ctrId;
	
	/**
	 * ctradeSk field
	 */
	private String ctradeSk;
	
	/**
	 * ctradeId field
	 */
	private String ctradeId;
		
	/**
	 * ctradeDesc field
	 */
	private String ctradeDesc;
	
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends CountryCOMTRADEForm> clase = this.getClass();
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
		if(this.ctrId == null || this.ctrId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("country.ctrId.required"));
		}
		
		if(this.ctradeId == null || this.ctradeId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("countryCOMTRADE.ctradeId.required"));
		}
		
		if(this.ctradeDesc == null || this.ctradeDesc.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("countryCOMTRADE.ctradeDesc.required"));
		} else {
			if(ctradeDesc.length() > 100) {
				ctradeDesc = ctradeDesc.substring(0, 100);
			}
		}
		
		
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.ctradeSk == null || this.ctradeSk.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("countryCOMTRADE.ctradeSk.required"));
		}
	}

	/**
	 * @return the ctrId
	 */
	public String getCtrId() {
		return ctrId;
	}

	/**
	 * @param ctrId the ctrId to set
	 */
	public void setCtrId(String ctrId) {
		this.ctrId = ctrId;
	}

	/**
	 * @return the ctradeSk
	 */
	public String getCtradeSk() {
		return ctradeSk;
	}

	/**
	 * @param ctradeSk the ctradeSk to set
	 */
	public void setCtradeSk(String ctradeSk) {
		this.ctradeSk = ctradeSk;
	}

	/**
	 * @return the ctradeId
	 */
	public String getCtradeId() {
		return ctradeId;
	}

	/**
	 * @param ctradeId the ctradeId to set
	 */
	public void setCtradeId(String ctradeId) {
		this.ctradeId = ctradeId;
	}

	/**
	 * @return the ctradeDesc
	 */
	public String getCtradeDesc() {
		return ctradeDesc;
	}

	/**
	 * @param ctradeDesc the ctradeDesc to set
	 */
	public void setCtradeDesc(String ctradeDesc) {
		this.ctradeDesc = ctradeDesc;
	}
}
