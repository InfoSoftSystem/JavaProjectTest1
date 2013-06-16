package org.promefrut.simefrut.utils;


import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.promefrut.simefrut.struts.administration.forms.UserForm;

//import org.apache.struts.action.RequestProcessor;

/**
 * @author Henry Willy Melara
 *
 */
public class Security extends TilesRequestProcessor {

	public boolean processRoles(HttpServletRequest request, HttpServletResponse response, 
								ActionMapping mapping) throws java.io.IOException, javax.servlet.ServletException {
		super.processPreprocess(request, response);
		String urlAsignado = processPath(request, response);
		String target = "";
		if(!urlAsignado.equals("/login") && !urlAsignado.equals("/getAction")&& !urlAsignado.equals("/reportAction")&& !urlAsignado.equals("/lostPwdAction")/**/) {
			try {
				String path = urlAsignado.replaceFirst("/", "") + ".do";
				UserForm user = (UserForm)request.getSession().getAttribute("user");
				if(user == null) {
					target = "logout";
					throw new Exception("SESION_EXPIRADA");
				}
				String validaOpcion = (String)user.getOpciones().get(path);
			    
				if(validaOpcion == null || validaOpcion.equals("null")) {
					target = "failOption";
					throw new Exception("SIN_PERMISO");
				}
			
			} catch(Exception e) {
				processForwardConfig(request, response, mapping.findForward(target));
				return false;
			} catch(Error e) {

				e.printStackTrace();
			}
		}
		return true;
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
	public static

	String getHash(String in) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA");
		byte[] out = in.getBytes();
		digest.update(out);
		byte[] dig = digest.digest();
		return byteArrayToHexString(dig);
	}

	/**
	 * Convert a byte[] array to readable string format. This makes the "hex"
	 readable!
	 * @return result String buffer in String format
	 * @param in byte[] buffer to convert to string format
	 */
	private static

	String byteArrayToHexString(byte[] in) {
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

}
