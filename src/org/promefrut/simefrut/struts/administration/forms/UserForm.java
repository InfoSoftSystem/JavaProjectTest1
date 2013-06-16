package org.promefrut.simefrut.struts.administration.forms;


import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;

/**
 * @author HWM
 * 
 * XDoclet definition:
 * @struts.form name="userForm"
 */
public class UserForm extends BaseForm {

	private static final long serialVersionUID = -2096143175354699214L;
	
	private String userId;
	
	/**
	 * Role Id for username 
	 */
	private String rolId; 
	
	/**
	 * ctrId field, ID of the country assigned to the user.
	 */
	private String ctrId;
	
	/**
	 * Username for application credential
	 */
	private String username;
	
	/**
	 * Password for application credential 
	 */
	private String password;
	
	
	private Boolean noCountry;
	
	private String userFirstTime;
	
	/**
	 * Real name of the user  
	 */
	private String namePerson; 
	
	private String email;
	private String phone;
	private Boolean notifyPrice;
	private Boolean notifyCommerce;
	private Boolean notifyProduction;

	
	
	/**
	 * Option list available for current username
	 */
	private HashMap<?, ?> opciones;
	
	private String confirmPsw;
	private String flgGeneratePsw;
	private Boolean flgPasswordUpd;
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    
	    Class<? extends UserForm> clase = this.getClass();
	    Field field[] = clase.getDeclaredFields();
	    try {
	        for(int i = 0; i < field.length; i++) {
	            try {
	            	if("noCountry".equals(field[i].getName()) || 
	            			!"notifyPrice".equals(field[i].getName()) || 
	            			!"notifyCommerce".equals(field[i].getName()) || 
	            			!"notifyProduction".equals(field[i].getName())
	            			){
	            		continue;
	            	}
	            	if(!"serialVersionUID".equals(field[i].getName()) && !"opciones".equals(field[i].getName())){
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
		if(this.ctrId == null || "".equals(this.ctrId)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("user.ctrId.required"));
		}
		
		if(this.username == null || "".equals(this.username.trim())) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("user.username.required"));
		}
		
		if(this.getFlgPasswordUpd().booleanValue()){
			if(this.password == null || this.password.equals("")) {
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("user.password.required"));
			} else {
				if(!this.password.equals(this.confirmPsw)) {
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("user.password.confirm"));
				}
			}
		}

		if(this.rolId == null || this.rolId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("user.rolId.required"));
		}

		if(this.namePerson == null || this.namePerson.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("user.namePerson.required"));
		}
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.userId == null || this.userId.equals("")) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("user.userId.required"));
		}
	}
	
	public void validateLostPwd(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		if(this.email == null || "".equals(this.email)) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("user.email.notFound"));
		}
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
		this.username = username.trim();
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

	/**
	 * @return the name
	 */
	public String getNamePerson() {
		return namePerson;
	}

	/**
	 * @param name the name to set
	 */
	public void setNamePerson(String name) {
		this.namePerson = name;
	}

	/**
	 * @return the opciones
	 */
	public HashMap<?, ?> getOpciones() {
		return opciones;
	}

	/**
	 * @param opciones the opciones to set
	 */
	public void setOpciones(HashMap<?, ?> opciones) {
		this.opciones = opciones;
	}

	
	
	
	
	public String getPasswordScriptado() {
		String cadena = "";
		try {
			cadena = getHash(this.password);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return cadena;
	}

	public String getPasswordScriptado(String password) {
		String cadena = "";
		try {
			cadena = getHash(password);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return cadena;
	}

	/**
	 * Return a string containing the SHA hash of another String
	 * @param in String String to be hashed.
	 * @return the hash.
	 */
	public static String getHash(String in) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA");
		byte[] out = in.getBytes();
		digest.update(out);
		byte[] dig = digest.digest();
		return byteArrayToHexString(dig);
	}

	/**
	 * Convert a byte[] array to readable string format. This makes the "hex" readable!
	 * @return result String buffer in String format
	 * @param in byte[] buffer to convert to string format
	 */
	private static	String byteArrayToHexString(byte[] in) {
		byte ch = 0x00;
		int i = 0;
		if(in == null || in.length <= 0)
			return null;
		String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

		StringBuffer out = new StringBuffer(in.length * 2);

		while(i < in.length) {
			ch = (byte)(in[i] & 0xF0); // Strip off high nibble
			ch = (byte)(ch >>> 4);
			// shift the bits down
			ch = (byte)(ch & 0x0F);
			// must do this is high order bit is on!
			out.append(pseudo[(int)ch]); // convert the nibble to a String Character
			ch = (byte)(in[i] & 0x0F); // Strip off low nibble
			out.append(pseudo[(int)ch]); // convert the nibble to a String Character
			i++;
		}
		String rslt = new String(out);
		return rslt;
	}

	/**
	 * @return the flgGeneratePsw
	 */
	public String getFlgGeneratePsw() {
		return flgGeneratePsw;
	}

	/**
	 * @param flgGeneratePsw the flgGeneratePsw to set
	 */
	public void setFlgGeneratePsw(String flgGeneratePsw) {
		this.flgGeneratePsw = flgGeneratePsw;
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
	 * @return the noCountry
	 */
	public boolean getNoCountry() {
		noCountry = noCountry==null?new Boolean(false):noCountry;
		return noCountry.booleanValue();
	}

	/**
	 * @param noCountry the noCountry to set
	 */
	public void setNoCountry(Boolean noCountry) {
		this.noCountry = noCountry==null?new Boolean(false):noCountry;
	}
	
	public void setNoCountry(boolean noCountry) {
		this.noCountry = new Boolean(noCountry);
	}
	

	/**
	 * @return the confirmpsw
	 */
	public String getConfirmPsw() {
		return confirmPsw;
	}

	/**
	 * @param confirmPsw the confirmpsw to set
	 */
	public void setConfirmPsw(String confirmPsw) {
		this.confirmPsw = confirmPsw;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the roleId
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
	 * @return the userFirstTime
	 */
	public String getUserFirstTime() {
		return userFirstTime;
	}

	/**
	 * @param userFirstTime the userFirstTime to set
	 */
	public void setUserFirstTime(String userFirstTime) {
		this.userFirstTime = userFirstTime;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return "".equals(phone)?null:phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the notifyPrice
	 */
	public Boolean getNotifyPrice() {
		return notifyPrice==null?new Boolean(false):notifyPrice;
	}

	/**
	 * @param notifyPrice the notifyPrice to set
	 */
	public void setNotifyPrice(Boolean notifyPrice) {
		this.notifyPrice = notifyPrice==null?new Boolean(false):notifyPrice;
	}

	/**
	 * @return the notifyCommerce
	 */
	public Boolean getNotifyCommerce() {
		return notifyCommerce==null?new Boolean(false):notifyCommerce;
	}

	/**
	 * @param notifyCommerce the notifyCommerce to set
	 */
	public void setNotifyCommerce(Boolean notifyCommerce) {
		this.notifyCommerce = notifyCommerce==null?new Boolean(false):notifyCommerce;
	}

	/**
	 * @return the notifyProduction
	 */
	public Boolean getNotifyProduction() {
		return notifyProduction==null?new Boolean(false):notifyProduction;
	}

	/**
	 * @param notifyProduction the notifyProduction to set
	 */
	public void setNotifyProduction(Boolean notifyProduction) {
		this.notifyProduction = notifyProduction==null?new Boolean(false):notifyProduction;
	}

	/**
	 * @return the flgPasswordUpd
	 */
	public Boolean getFlgPasswordUpd() {
		return flgPasswordUpd==null?new Boolean(false):flgPasswordUpd;
	}

	/**
	 * @param flgPasswordUpd the flgPasswordUpd to set
	 */
	public void setFlgPasswordUpd(Boolean flgPasswordUpd) {
		this.flgPasswordUpd = flgPasswordUpd==null?new Boolean(false):flgPasswordUpd;
	}
}