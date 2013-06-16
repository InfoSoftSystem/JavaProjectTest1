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
 * @struts.form name="rolesForm"
 */
public class RolesForm extends BaseForm {
	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = -7211814423306301339L;

	/**
	 * sizeId field
	 */
	private String rolId;
	
	/**
	 * ctrDescSpanish field
	 */
	private String rolDesc;
		
	/**
	 * sizeStatus field
	 */
	private String rolStatus;
	
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends RolesForm> clase = this.getClass();
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
		if(this.rolDesc == null || this.rolDesc.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("roles.rolDesc.required"));
		} else {
			if(rolDesc.length() > 100) {
				rolDesc = rolDesc.substring(0, 100);
			}
		}
		
		if(this.rolStatus == null || "".equals(this.rolStatus)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("roles.rolStatus.required"));
		} else {
			if(rolStatus.length() > 1) {
				rolStatus = rolStatus.substring(0, 1);
			}
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.rolId == null || this.rolId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("roles.rolId.required"));
		}
	}

	/**
	 * @return the rolId
	 */
	public String getRolId() {
		return rolId;
	}

	/**
	 * @param rolId the rolId to set
	 */
	public void setRolId(String rolId) {
		this.rolId = rolId;
	}

	/**
	 * @return the rolDesc
	 */
	public String getRolDesc() {
		return rolDesc;
	}

	/**
	 * @param rolDesc the rolDesc to set
	 */
	public void setRolDesc(String rolDesc) {
		this.rolDesc = rolDesc.trim();
	}

	/**
	 * @return the rolStatus
	 */
	public String getRolStatus() {
		return rolStatus;
	}

	/**
	 * @param rolStatus the rolStatus to set
	 */
	public void setRolStatus(String rolStatus) {
		this.rolStatus = rolStatus.toUpperCase();
	}
}
