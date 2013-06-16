package org.promefrut.simefrut.struts.administration.forms;



import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.promefrut.simefrut.utils.LookUpResourceSchema;

/**`
 * XDoclet definition:
 * @struts.form name="changePasswordForm"
 */
public class ChangePwdForm extends ActionForm {
	/*
	 * Generated fields
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -905633899129071754L;

	/** passwordNew property */
	private String passwordNew;

	/** confirmPsw property */
	private String confirmPsw;

	/** passwordOld property */
	private String passwordOld;

	/*
	 * Generated Methods
	 */

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public void validate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors) {
		String accion = request.getParameter("accion");
		accion = (accion == null) ?
				 "" :
				 accion;

		ResourceBundle mensajes = ResourceBundle.getBundle(LookUpResourceSchema.APPLICATION_RESOURCE, 
														   (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY));
		if(!accion.equals(mensajes.getString("opc.cancel")) && !accion.equals(mensajes.getString("opc.find"))) {
			if(this.passwordOld == null || this.passwordOld.equals("")) {
				errors.add("changePassword.passwordOld.requerido", new ActionError("changePassword.passwordOld.requerido"));
			} else {
				UserForm userForm = (UserForm)request.getSession().getAttribute("user");
				if(!userForm.getPassword().equals(userForm.getPasswordScriptado(this.passwordOld))) {
					//errors.add("changePassword.passwordOld.invalido", new ActionError("changePassword.passwordOld.invalido"));
					errors.add("globalErrors", new ActionError("changePassword.passwordOld.invalido"));
				}
			}
			if(this.passwordNew == null || this.passwordNew.equals("")) {
				errors.add("changePassword.passwordNew.requerido", new ActionError("changePassword.passwordNew.requerido"));
			} else {
				if(!this.passwordNew.equals(this.confirmPsw)) {
					errors.add("changePassword.passwordNew.confirm", new ActionError("changePassword.passwordNew.confirm"));
				}
			}
		}
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.confirmPsw = null;
		this.passwordNew = null;
		this.passwordOld = null;
	}

	/** 
	 * Returns the passwordNew.
	 * @return String
	 */
	public String getPasswordNew() {
		return passwordNew;
	}

	/** 
	 * Set the passwordNew.
	 * @param passwordNew The passwordNew to set
	 */
	public void setPasswordNew(String passwordNew) {
		this.passwordNew = passwordNew;
	}

	/** 
	 * Returns the confirmPsw.
	 * @return String
	 */
	public String getConfirmPsw() {
		return confirmPsw;
	}

	/** 
	 * Set the confirmPsw.
	 * @param confirmPsw The confirmPsw to set
	 */
	public void setConfirmPsw(String confirmPsw) {
		this.confirmPsw = confirmPsw;
	}

	/** 
	 * Returns the passwordOld.
	 * @return String
	 */
	public String getPasswordOld() {
		return passwordOld;
	}

	/** 
	 * Set the passwordOld.
	 * @param passwordOld The passwordOld to set
	 */
	public void setPasswordOld(String passwordOld) {
		this.passwordOld = passwordOld;
	}
}
