package org.promefrut.simefrut.utils;


import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;

public class CustomExceptions {
	//private String codeError;
	private String msgError;
	private String resourceMsg;
	private Exception e = null;
	private Error er = null;
	private HttpServletRequest request = null;
	
	public CustomExceptions(Error er) throws Exception {
		this.er = er;
		int token = -1;
		
		if(!StringUtils.isBlank(er.getMessage())){
			token = er.getMessage().indexOf(":");
		}

		if(token >= 0) {
			//codeError = er.getMessage().substring(0, token);
			msgError = er.getMessage().substring(token + 1, er.getMessage().length()).trim();
		} else {
		    //e.printStackTrace();
			//throw e;
		}
	}
	
	public CustomExceptions(Exception e) throws Exception {
		this.e = e;
		int token = -1;
		
		if(!StringUtils.isBlank(e.getMessage())){
			token = e.getMessage().indexOf(":");
		}

		if(token >= 0) {
			//codeError = e.getMessage().substring(0, token);
			msgError = e.getMessage().substring(token + 1, e.getMessage().length()).trim();
		} else {
		    //e.printStackTrace();
			//throw e;
		}
	}

	public CustomExceptions(HttpServletRequest request, Exception e) throws Exception {
		this.e = e;
		this.request = request;

		int token = 0;
		
		if(e.getMessage()!= null){
			if(e.getMessage().length() > 0) {
				token = e.getMessage().indexOf(":");
			}
		}
		if(token > 0) {
			//codeError = e.getMessage().substring(0, token);
			msgError = e.getMessage().substring(token + 1, e.getMessage().length()).trim();
			resourceMsg = getResourceError(msgError);
			//resourceMsg = resourceMsg==null?e.getMessage():resourceMsg;
		} else {
		    //e.printStackTrace();
			//throw e;
		}
	}

	/**
	 * Devuelve el mensaje asociado al Resource de la aplicacion
	 * @return Mensaje del resource asociado al codigo de error
	 */
	public String getMensaje() {
		return resourceMsg;
	}

	/**
	 * Funcion que retorna el error completo
	 * @return error completo devuelto por la BD
	 */
	public String getCompleteError() {
		return this.e.getMessage();
	}

	/**
	 * Retorna mensaje de la aplicacion asociado a un codigo de error de la BD
	 * @param request
	 * @param ora
	 * @return
	 * @throws Exception
	 */
	public String getResourceError(HttpServletRequest request, String ora) throws Exception {
		this.request = request;
		return getResourceError(ora);
	}

	/**
	 * Retorna mensaje de la aplicacion asociado a un codigo de error de la BD
	 * @param MsgError
	 * @return The message according to the error, but personalize using internationalization.
	 * @throws Exception
	 */
	public String getResourceError(String MsgError) throws Exception {
		String res = null;

		if(request == null) {
			throw new Exception("The request is null");
		}
		ResourceBundle ms = ResourceBundle.getBundle(LookUpResourceSchema.APPLICATION_RESOURCE, 
													 (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY));

		if(ms == null) {
			throw new Exception("There's no MessageResource in session");
		}
		
		if(MsgError == null){
			throw new Exception("Error message is null");
		}
		
		//** ERRORES POSTGRESQL **
		if(MsgError.contains("duplicate key value violates unique constraint")) {
			res = ms.getString("error.generic.unique");
			
		} else if("ORA-02292".equalsIgnoreCase(MsgError)) {
			res = ms.getString("error.generic.integrity.child");
		}
		return res;
	}

	public String toString(){ 
		return this.e.toString();
	}
	
	public String getStackTrace(){
		String resultado = new String();
		StackTraceElement stack[] = null;
		
		if(this.e == null){
			stack = this.er.getStackTrace();
		}else{
			stack = this.e.getStackTrace();
		}
	    
		for(int i = 0; i< stack.length; i++){
		    if(stack[i]!=null){
		    	resultado += (stack[i]).toString() + " <br/>\n";
		    }else{
		    	resultado += "SIMEFRUT {null} <br/>\n";
		    }
		}
		return this.e.toString() +" <br/>\n "+ resultado;
	}
}
