package org.promefrut.simefrut.struts.commons.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class GetAction extends DispatchAction{
	
	/**
	 * URL used in Prices Report
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @throws Error
	 */
	public ActionForward urlSizeInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
								HttpServletResponse response) throws Exception, Error {
		String nombreArchivo ="Tamanios y pesos de frutas.docx";
		getFile(nombreArchivo, response);
		
		return null;
	}
	
	/**
	 * URL used in Category Report
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @throws Error
	 */
	public ActionForward urlCategoryInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception, Error {
		String nombreArchivo ="Categorias Agricolas.xlsx";
		getFile(nombreArchivo, response);
		
		return null;
	}
	
	/**
	 * URL used in some reports' footer
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @throws Error
	 */
	public ActionForward urlVCRMetodology(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception, Error {
		String nombreArchivo ="Ventajas Comparativa Revelada Metodology.pdf";
		getFile(nombreArchivo, response);
		
		return null;
	}
	
	public ActionForward pptSIMEFRUTCapacitacion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception, Error {
		String nombreArchivo ="SIMEFRUT capacitacion.pptx";
		getFile(nombreArchivo, response);
		
		return null;
	}
	
	public ActionForward exampleFAOSTATWorld(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception, Error {
		String nombreArchivo ="Example FAOSTAT TradeMatrix World.xlsx";
		getFile(nombreArchivo, response);
		
		return null;
	}
	
	public ActionForward exampleComtrade(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception, Error {
		String nombreArchivo ="Example COMTRADE Datos.xlsx";
		getFile(nombreArchivo, response);
		
		return null;
	}
	
	public ActionForward exampleFAOSTATDetailed(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception, Error {
		String nombreArchivo ="Example FAOSTAT TradeMatrix Detailed.xlsx";
		getFile(nombreArchivo, response);
		
		return null;
	}
	
	public ActionForward userManual(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception, Error {
		String nombreArchivo ="Manual de Usuario - SIMEFRUT.docx";
		getFile(nombreArchivo, response);
		
		return null;
	}
	
	public ActionForward technicalManual(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception, Error {
		String nombreArchivo ="Manual Técnico - SIMEFRUT.docx";
		getFile(nombreArchivo, response);
		
		return null;
	}
	
	private void getFile(String nombreArchivo, HttpServletResponse response) throws Exception, Error {
		BufferedInputStream buf = null;
		OutputStream sream = null;
		
        int car = 0;
		
		try{
			String fileName = servlet.getServletContext().getRealPath("temp/"+nombreArchivo);
			buf = new BufferedInputStream(new FileInputStream(new File(fileName)));
			response.setHeader("Cache-Control", "none");
			response.setHeader("Pragma", "cache");
			//response.setContentType(contentType);
			response.setHeader("Content-Disposition","attachment; filename=\""+nombreArchivo+"\"");
			
			//response.setContentType("application/pdf");
			
			sream = response.getOutputStream();
			while ((car = buf.read()) != -1) {
				sream.write(car);
			}
			sream.flush();
		}finally{
			if(sream!=null)
			sream.close();
			
			if(buf!=null)
			buf.close();
		}/**/
		return;
	}

}
