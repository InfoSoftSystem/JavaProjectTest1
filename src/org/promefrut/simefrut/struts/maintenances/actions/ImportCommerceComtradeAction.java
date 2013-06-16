package org.promefrut.simefrut.struts.maintenances.actions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;
import org.promefrut.simefrut.struts.maintenances.beans.ImportCommerceComtradeBean;
import org.promefrut.simefrut.struts.maintenances.forms.ImportCommerceComtradeForm;



/**
 * @author HWM
 *
 */
public class ImportCommerceComtradeAction extends DispatchBaseAction {
	public String title = new String();
	

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#Init(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void Init(HttpServletRequest request) throws Exception, Error {
		super.LoadBundle(request);
		title = super.bundle.getString("importCommerceComtrade.label.text");
		title = "" + title;
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#update(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {

		ImportCommerceComtradeForm importCommerceComtradeForm = (ImportCommerceComtradeForm) form;
		
		ImportCommerceComtradeBean bean = new ImportCommerceComtradeBean(managerDB, bundle);
		
		importCommerceComtradeForm.validateInsert(mapping, request, errors);
		
		if(errors.isEmpty()) {
			try {
				//ONLY XLSX
				XSSFWorkbook workBook = new XSSFWorkbook(importCommerceComtradeForm.getInputStream()); 
				XSSFSheet sheet = workBook.getSheetAt(0); 
				
				int totalRows = sheet.getPhysicalNumberOfRows(); 
				
				System.out.println("total no of rows >>>>"+totalRows);
				System.out.println();
				Iterator<Row> rows = sheet.rowIterator();
				
				//int x = 1;
				rows.next(); //Skip headers
				while (rows.hasNext()){
					XSSFRow row = ((XSSFRow) rows.next()); 
					
					String Year = getCellValue(row.getCell(0));
					String ReporterCountry = getCellValue(row.getCell(1));
					String Type = getCellValue(row.getCell(2));
					String DollarAmount = getCellValue(row.getCell(9));
					
					if(bean.validateCountry(ReporterCountry)){
						
						if("1".equals(Type.trim())){//IMPORT
							try{
								bean.insertImportDollar(Year, ReporterCountry, DollarAmount, user.getUsername());
							}catch(SQLException ee){
								if(ee.getMessage().contains("duplicate key value violates unique constraint")){
									if(errors.isEmpty()){
										errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("importCommerceComtrade.data.header"));
									}
									/*String mensaje = 
										bundle.getString("importCommerceWorld.reporter")+ "<i>"+ReporterCountry+"</i>, "+
										bundle.getString("importCommerceWorld.year")+"<i>"+Year+"</i>, "+
										bundle.getString("importCommerceWorld.type")+"<i>"+ Type+"</i>, "+
										bundle.getString("importCommerceWorld.value")+"<i>"+ DollarAmount+"</i><br/>";
									*/
									//errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", mensaje));
									errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("importCommerceComtrade.data.duplicated", ReporterCountry, Year, "Import", DollarAmount));
									bean.rollbackToSavePoint();
								}else{
									throw ee;
								}
							}
						}else if("2".equals(Type.trim())){//EXPORT
							try{
								bean.insertExportDollar(Year, ReporterCountry, DollarAmount, user.getUsername());
							}catch(SQLException ee){
								if(ee.getMessage().contains("duplicate key value violates unique constraint")){
									if(errors.isEmpty()){
										errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("importCommerceComtrade.data.header"));
									}
									/*String mensaje = 
										bundle.getString("importCommerceWorld.reporter")+ "<i>"+ReporterCountry+"</i>, "+
										bundle.getString("importCommerceWorld.year")+"<i>"+Year+"</i>, "+
										bundle.getString("importCommerceWorld.type")+"<i>"+ Type+"</i>, "+
										bundle.getString("importCommerceWorld.value")+"<i>"+ DollarAmount+"</i><br/>";
									*/
									//errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", mensaje));
									errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("importCommerceComtrade.data.duplicated", ReporterCountry, Year, "Export", DollarAmount));
									bean.rollbackToSavePoint();
								}else{
									throw ee;
								}
							}
						}
					}else{
						errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("importCommerceComtrade.country.nofound", ReporterCountry, Year, Type, DollarAmount));
					}
				}
				if(errors.isEmpty()){
					errors.add(BaseForm.GLOBAL_MESSAGES, new ActionError("mensaje.exito.update", title, ""));
					String lastYearLoad = String.valueOf(bean.getLastYearLoad());
					
					request.setAttribute("lastYearLoad", lastYearLoad);
				}else{
					managerDB.rollback();
				}
			
			} catch (Exception e) { 
				e.printStackTrace();
				managerDB.rollback();
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", e.getMessage()));
			
			} catch (Error e) { 
				e.printStackTrace();
				managerDB.rollback();
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", e.getMessage()));
			}finally{
				managerDB.commit();
			}
		}
		saveErrors(request, errors);
		return mapping.findForward(KEY_SUCCESS);
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#insert(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		
		ImportCommerceComtradeForm importCommerceComtradeForm = (ImportCommerceComtradeForm) form;
		
		ImportCommerceComtradeBean bean = new ImportCommerceComtradeBean(managerDB, bundle);
		
		importCommerceComtradeForm.validateInsert(mapping, request, errors);
		
		if(errors.isEmpty()) {
			if(StringUtils.isBlank(importCommerceComtradeForm.getYearList())){
				try {
					//ONLY XLSX
					XSSFWorkbook workBook = new XSSFWorkbook(importCommerceComtradeForm.getInputStream()); 
					XSSFSheet sheet = workBook.getSheetAt(0); 
					
					Iterator<Row> rows = sheet.rowIterator();
					
					rows.next(); //Skip headers
					
					Map<String,String> arrayYears = new HashMap<String,String>();
					
					while (rows.hasNext()){
						XSSFRow row = ((XSSFRow) rows.next());
						
						String Year = getCellValue(row.getCell(0));
						
						arrayYears.put(Year, "true");
					}
					
					Set<String> keySet = arrayYears.keySet();
					
					String yearList = new String();
					for(Iterator<String> it = keySet.iterator(); it.hasNext();){
						String tmpYear = it.next();
						
						if(bean.existsData(tmpYear)){
							if(yearList.length()>0){
								yearList+=",";
							}
							yearList+= tmpYear;
						}
					}
					
					if(yearList.length()>0){
						request.setAttribute("yearList", yearList);
						request.setAttribute("oldFilename", importCommerceComtradeForm.getRepFile().getFileName());
						String lastYearLoad = String.valueOf(bean.getLastYearLoad());
						
						request.setAttribute("lastYearLoad", lastYearLoad);
					}else{
						this.update(mapping, importCommerceComtradeForm, request, response, errors, messages, managerDB);
					}
				} catch (Exception e) { 
					e.printStackTrace();
					managerDB.rollback();
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", e.getMessage()));
				
				} catch (Error e) { 
					e.printStackTrace();
					managerDB.rollback();
					errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("mensaje", e.getMessage()));
				}
			}else{
				this.delete(mapping, importCommerceComtradeForm, request, response);
				this.update(mapping, importCommerceComtradeForm, request, response, errors, messages, managerDB);
			}
		}

		saveErrors(request, errors);
		return mapping.findForward(KEY_SUCCESS);
	}

	/* (non-Javadoc)
	 * @see org.promefrut.simefrut.struts.commons.actions.DispatchBaseAction#delete(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionErrors, org.apache.struts.action.ActionMessages, org.promefrut.simefrut.struts.commons.beans.SessionManager)
	 */
	@Override
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			ActionErrors errors, ActionMessages messages,
			SessionManager managerDB) throws Exception, Error {
		
		ImportCommerceComtradeForm importCommerceComtradeForm = (ImportCommerceComtradeForm) form;
		
		ImportCommerceComtradeBean bean = new ImportCommerceComtradeBean(managerDB, bundle);
		
		bean.deleteData(importCommerceComtradeForm.getYearList());
		
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
		String lastYearLoad = ""; 
		
		ImportCommerceComtradeBean bean = new ImportCommerceComtradeBean(managerDB, bundle);
		lastYearLoad = String.valueOf(bean.getLastYearLoad());
		
		request.setAttribute("lastYearLoad", lastYearLoad);
		
		return mapping.findForward(KEY_SUCCESS);
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
	
	public String getAjaxTemplate(boolean success, String msg){
		String results = new String();
		if(msg==null){
			msg ="Null Exception";
		}
		results = "{\"totalCount\":\"1\",\"registers\":[{\"msg\":\""+msg.replaceAll("\\\"", "\\\\\"").replaceAll("\n", "\\\\n").replaceAll("\r","")+"\"}]}";
		
		return results;
	}
	
	/**
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellValue(XSSFCell cell){
		if(cell.getCellType() == XSSFCell.CELL_TYPE_STRING){
			return cell.getStringCellValue();
			
		}else if(cell.getCellType()== XSSFCell.CELL_TYPE_FORMULA){
			return cell.getRawValue();
			
		}else if(cell.getCellType()==XSSFCell.CELL_TYPE_NUMERIC){
			return cell.getRawValue();
		}else{
			return "";
		}
	}
}