package org.promefrut.simefrut.struts.commons.forms;



import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.promefrut.simefrut.utils.LookUpResourceSchema;

/**
 * @author Henry Willy Melara
 *
 */
public class LoginForm extends ActionForm {

	/**
	 *
	 */
	private static final long serialVersionUID = 980889106217055693L;
	private String username;
	private String password;
	private String lenguaje;
    private String accion;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		String accion = request.getParameter("accion");
		accion = (accion == null) ?
				 "" :
				 accion;

		try {
			ResourceBundle mensajes = ResourceBundle.getBundle(LookUpResourceSchema.APPLICATION_RESOURCE, 
															   (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY));
			if(!accion.equals(mensajes.getString("opc.logout")) && !accion.equals(mensajes.getString("opc.change"))) {
				if(this.username == null || this.username.equals("")) {
					errors.add("login.username.requerido", new ActionError("login.message.username"));
				}
				if(this.password == null || this.password.equals("")) {
					errors.add("login.password.requerido", new ActionError("login.message.password"));
				}

			}
		} catch(Exception e) {
			request.getSession().setAttribute(Globals.LOCALE_KEY, new Locale("en", "SV")); /*En este caso guardamos la Locale de ingles*/
		    request.getSession().setAttribute("idioma", "E");
		}

		return errors;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public void setLenguaje(String lenguaje) {
		this.lenguaje = lenguaje;
	}

	public String getLenguaje() {
		return lenguaje;
	}
    
    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getAccion() {
        return accion;
    }

}
