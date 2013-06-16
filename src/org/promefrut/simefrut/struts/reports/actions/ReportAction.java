package org.promefrut.simefrut.struts.reports.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;

/** Esta clase utiliza la libreria <b>"JasperReport"</b> para generar los reportes en Jasper.<br>
 * Los parametros minimos necesarios que utiliza son:<br>
 * <br>
 * - report : nombre del reporte junto con su extension<br>
 * - FormatPrint : Formato en el que se generara el reporte<br>
 * - application: Contiene el nombre de la aplicacion a la que pertenece el reporte (Solo en Jasper)<br>
 */
public class ReportAction extends DispatchBaseAction{
	private final String PARAM_DS = "userid";
	private final String PARAM_EXPORT_FORMAT = "FormatPrint";
	private final String PARAM_REPORT = "report";
	private final String PARAM_APPLICATION = "application";
	private final String PARAM_CHARACTER_WIDTH = "character_width";
	private final String PARAM_CHARACTER_HEIGHT = "character_height";
	private final String PARAM_URL_SIZE_INFO = "urlSizeInfo";
	private final String PARAM_CATEGORY_INFO = "urlCategoryInfo";
	private final String PARAM_VCR_METODOLOGY = "urlVCRMetodology";
	private final String ENCODING = "ISO-8859-1";
	private final String REPORT_DIRECTORY = "reports";

	public String format = "";
	public String report = "";
	public String application = "";
	public String characterWidth = "";
	public String characterHeight = "";

	public ActionForward generateReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
								HttpServletResponse response) throws Exception, Error {
		SessionManager managerReporte = null;
		InputStream jrxml = null;

		try {
			super.LoadBundle(request);
			managerReporte = new SessionManager(dtsource);

			format = (String)request.getParameter(PARAM_EXPORT_FORMAT);
			report = (String)request.getParameter(PARAM_REPORT);
			application = (String)request.getParameter(PARAM_APPLICATION);
			application="SIMEFRUT";
			
			if(format==null || "".equals(format)){
				throw new Exception("El parametro "+ PARAM_EXPORT_FORMAT +" esta vacio. Se necesita especificar un formato de salida");
			}

			if(report==null || "".equals(report)){
				throw new Exception("El parametro \""+ PARAM_REPORT +"\" esta vacio. Necesita especificar el nombre del reporte");
			}else{
				if(!report.toLowerCase().endsWith(".jrxml")){
					report = report + ".jrxml";
				}
			}


			/**
			 * TextExporter uses a very smart algorithm to convert reports in graphics to reports in text. It first creates a virtual text grid based on your report's size and the character dimensiona given to the exporter. Then it places each individual item to the text grid based on the item's location in pixels.

				There are three very important parameters effecting how the exporter converts the output

				1- Width and Height of the reports in pixels
				2- Widths and Heights of the individual report elements (labels etc.)
				3- Character Width and Height parameters of the exporter


				To fit the report to the text grid, you need to make a simple calcuation:

				First find the maximum number of characters that can be fit to a row when you would print the report in text (Let's assume that it is 80 characters long and call this number MAX_CHAR_PER_ROW).
				Then find the maximum number of characters that can be fit to a column when you would print the report in text (Let's assume that it is 44 characters height and call this number MAX_CHAR_PER_COL).


				Then Divide your reports dimensions with these numbers to find the character height and width. LEt's assume the dimensions of the report are REPORT_WIDTH and REPORT_HEIGHT and the corresponding values are 524 and 524. Now the characters dimensions are calculated as:


				CHAR_WIDTH = REPORT_WIDTH / MAX_CHAR_PER_ROW
				CHAR_HEIGHT = REPORT_HEIGHT / MAX_CHAR_PER_COL

				CHAR_WIDTH = 524 / 80 = 6.55
				CHAR_HEIGHT = 524 / 44 = 11.9


				Of course, the resulting values will not be integers most of the time. Then if you want a perfect match, CHAR_WIDTH and CHAR_HEIGHT must be floating numbers. But the original exporter only accepts integers parameters. At this point, use my modified exporter here. I already converted these parameters to float.

				Here is how you may send character dimensions to the exporter.


				exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(6.55));//6.55 //6
				exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(11.9)); //11//10



				Another notice! Make sure that CHAR_HEIGHT is not lower that the actual heights of the report items. Otherwise, the items will not added to the grid since the exporter truncates their heights to 0 because of the arithmetic operation inside the exporter. Another solution would be using rounding instead of truncation by modifying the exporter a little more. Anyway, I did not need that feature since the character height is ok for my case.


				/**
				* Transforms y coordinates from pixel space to character space.
				* /
				protected int calculateYCoord(int y){
					int result = Math.round((float)pageHeight * y / jasperPrint.getPageHeight());
					return result;
				}

				/**
				* Transforms x coordinates from pixel space to character space.
				* /
				protected int calculateXCoord(int x){
					int result = Math.round((float)pageWidth * x / jasperPrint.getPageWidth());
					return result;
				}
			*/

			//Validacion cuando el formato es Texto. Se encesitan de los parametros PARAM_CHARACTER_WIDTH y PARAM_CHARACTER_HEIGHT
			if("text".equalsIgnoreCase(format)){
				characterWidth = (String)request.getParameter(PARAM_CHARACTER_WIDTH);
				characterHeight = (String)request.getParameter(PARAM_CHARACTER_HEIGHT);
				characterHeight = "14";
				characterWidth = "5";

				if(characterWidth == null || "".equalsIgnoreCase(characterWidth)){
					throw new Exception("El parametro " + PARAM_CHARACTER_WIDTH + " esta vacio. Debe ser especificado para el formato texto\n");
				}else{
					try{
						Float.parseFloat(characterWidth);
					}catch(NumberFormatException e){
						throw new Exception("El parametro " + PARAM_CHARACTER_WIDTH + " debe ser float \""+characterWidth+"\"\n");
					}
				}

				if(characterHeight == null || "".equalsIgnoreCase(characterHeight)){
					throw new Exception("El parametro " + PARAM_CHARACTER_HEIGHT + " esta vacio. Debe ser especificado para el formato texto\n");
				}else{
					try{
						Float.parseFloat(characterHeight);
					}catch(NumberFormatException e){
						throw new Exception("El parametro " + PARAM_CHARACTER_HEIGHT + " debe ser float \""+characterHeight+"\"\n");
					}
				}
			}

			/* *********************************************************************************************************************************************
									Let's find the report .jrxml
			//*********************************  *********************************  *********************************  *********************************  */

			String fileName = servlet.getServletContext().getRealPath(REPORT_DIRECTORY + "/" + report);
			jrxml = new BufferedInputStream(new FileInputStream(new File(fileName)));


			//Load .jrxml files
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxml);
			
			//Set temporary directory for compile files
			System.setProperty("jasper.reports.compile.temp", request.getSession().getServletContext().getRealPath("temp"));

			/* *********************************************************************************************************************************************
									Let's get the parameters from request
			//*********************************  *********************************  *********************************  *********************************  */
			HashMap<String,Object> reportParameters = new HashMap<String,Object>();
			@SuppressWarnings("unchecked")
			Enumeration<String> paramNames = request.getParameterNames();

			while (paramNames.hasMoreElements()){   // Extrae todos los paramentros del formulario
				String paramName = (String)paramNames.nextElement();

				if(!(paramName.equalsIgnoreCase(PARAM_EXPORT_FORMAT)||
						paramName.equalsIgnoreCase(PARAM_REPORT) ||
						paramName.equalsIgnoreCase(PARAM_DS) ||
						paramName.equalsIgnoreCase(PARAM_APPLICATION) ||
						paramName.equalsIgnoreCase("accion")
					)){
					String paramValue = request.getParameter(paramName);
					paramValue = paramValue==null?"":paramValue;
					reportParameters.put(paramName,paramValue);

					/*if(paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("false")){
						reportParameters.put(paramName, new Boolean(paramValue.equalsIgnoreCase("true")?true:false));
					}else{
						reportParameters.put(paramName,paramValue);
					}*/
				}
			}
			
			//Set the URL for the Size Information document
			reportParameters.put(PARAM_URL_SIZE_INFO, request.getSession().getAttribute("GeneralPath")+"getAction.do?accion="+PARAM_URL_SIZE_INFO);
			reportParameters.put(PARAM_CATEGORY_INFO, request.getSession().getAttribute("GeneralPath")+"getAction.do?accion="+PARAM_CATEGORY_INFO);
			reportParameters.put(PARAM_VCR_METODOLOGY, request.getSession().getAttribute("GeneralPath")+"getAction.do?accion="+PARAM_VCR_METODOLOGY);
			/* *********************************************************************************************************************************************
									SE REALIZA LA EXPORTACION Y EJECUCION DEL REPORTE
			//*********************************  *********************************  *********************************  *********************************  */
			// Fill the report using a data source
			JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParameters, managerReporte.getConnection());

			//CONTENT TYPE PARA TRANSFERIR ARCHIVOS BINARIOS application/octet-stream
			if("pdf".equalsIgnoreCase(format)){
				// Create a PDF exporter
				JRExporter exporter = new JRPdfExporter();

				// Configure the exporter (set output file name and print object)
				//response.setCharacterEncoding(ENCODING);

				//exporter.s//etParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, ENCODING);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

				response.setContentType("application/pdf;charset="+ENCODING);
				response.setHeader("Content-Disposition","attachment; filename=\"reporte.pdf\"");
				// Export the PDF file
				exporter.exportReport();

			}else if("xlsx".equalsIgnoreCase(format)){
				// Create a XLS exporter
				JRExporter exporter = new JRXlsxExporter();

				//response.setCharacterEncoding(ENCODING);
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, ENCODING);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

				response.setContentType("application/vnd.ms-excel;charset="+ENCODING);
				response.setHeader("Content-Disposition","attachment; filename=\"reporte.xlsx\"");
				// Export the file
				exporter.exportReport();

			}else if("xls".equalsIgnoreCase(format)){
				// Create a XLS exporter
				JRExporter exporter = new JRXlsExporter();

				//response.setCharacterEncoding(ENCODING);
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, ENCODING);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

				response.setContentType("application/vnd.ms-excel;charset="+ENCODING);
				response.setHeader("Content-Disposition","attachment; filename=\"reporte.xls\"");
				// Export the file
				exporter.exportReport();

			}else if("html".equalsIgnoreCase(format)){
				// Create exporter
				JRExporter exporter = new JRHtmlExporter();

				//response.setCharacterEncoding(ENCODING);
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, ENCODING);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

				response.setContentType("text/html;charset="+ENCODING);
				// Export the file
				exporter.exportReport();

			}else if("docx".equalsIgnoreCase(format)){
				// Create exporter
				JRExporter exporter = new JRDocxExporter();

				//response.setCharacterEncoding(ENCODING);
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, ENCODING);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

				response.setContentType("application/msword;charset="+ENCODING);
				response.setHeader("Content-Disposition","attachment; filename=\"reporte.docx\"");
				// Export the file
				exporter.exportReport();

			}else if("text".equalsIgnoreCase(format)){
				// Create exporter
				JRExporter exporter = new JRTextExporter();

				//response.setCharacterEncoding(ENCODING);
				exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(characterHeight)); //14
				exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(characterWidth)); //"5"
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, ENCODING);

				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

				response.setContentType("text/plain;charset="+ENCODING);
				response.setHeader("Content-Disposition","attachment; filename=\"reporte.txt\"");
				// Export the file
				exporter.exportReport();

			}else if("cvs".equalsIgnoreCase(format)){
				// Create exporter
				JRExporter exporter = new JRCsvExporter();

				//response.setCharacterEncoding(ENCODING);
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, ENCODING);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

				response.setContentType("text/plain;charset="+ENCODING);
				response.setHeader("Content-Disposition","attachment; filename=\"reporte.cvs\"");
				// Export the file
				exporter.exportReport();

			}else{
				throw new Exception("El formato de exportacion \""+ format +"\" no se encuentra definido.");
			}
			/*
			//Se actualiza la fecha de ejecucion
			sql = " UPDATE PROCESOS.PRC_REPORTES \n" +
				"  SET REP_FECHA_EXE = SYSDATE " +
				" where REP_FILENAME = '"+ report +"'\n" +
				"   and REP_APP = '"+ application +"'";

			QueryRunner query = new QueryRunner();
			query.update(managerOracle.getConnection(),sql);
			managerOracle.commit();*/
        } catch (JRException e) {
            response.setHeader("error",e.getMessage());
			e.printStackTrace();
			throw e;
            //System.exit(1);
        } catch (Exception e) {
            response.setHeader("error",e.getMessage());
			e.printStackTrace();
            throw e;
			//System.exit(1);
        } catch (Error e) {
			e.printStackTrace();
			throw e;
		}finally{
			try{
				managerReporte.close();
				try{
					if(jrxml!=null)
						jrxml.close();
				}catch(Exception ex){;}
			}catch(Exception e){
				e.printStackTrace();
				throw e;
			}catch(Error e){
				e.printStackTrace();
				throw e;
			}finally{
				Runtime.getRuntime().gc();
			}
		}

		return null;
	}


	public void Init(HttpServletRequest request)throws Exception, Error {
		super.LoadBundle(request);
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#update(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#insert(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#delete(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#find(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward find(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public String load(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxInsert(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxInsert(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxUpdate(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxUpdate(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#ajaxDelete(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public boolean ajaxDelete(ActionForm form, HttpServletRequest request,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		return false;
	}
}
