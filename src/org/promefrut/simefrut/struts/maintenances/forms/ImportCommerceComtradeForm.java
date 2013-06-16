package org.promefrut.simefrut.struts.maintenances.forms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.promefrut.simefrut.struts.commons.forms.BaseForm;

/**
 * @author HWM
 * 
 * XDoclet definition:
 * @struts.form name="importCommerceComtradeForm"
 */
public class ImportCommerceComtradeForm extends BaseForm {

	/**
	 * serialVersionUID field
	 */
	private static final long serialVersionUID = 1L;

	private FormFile repFile;
	private String yearList;
	private String oldFilename;
	
	public void destroy() {
        if(repFile!=null){
			repFile.destroy();
		}
    }
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
	    super.reset(mapping, request);
	    Class<? extends ImportCommerceComtradeForm> clase = this.getClass();
	    Field field[] = clase.getDeclaredFields();
	    try {
	        for(int i = 0; i < field.length; i++) {
	            try {
	            	if(!"serialVersionUID".equals(field[i].getName())){
	            		field[i].set(this, "");
	            	};
	            } catch(Exception e) {
	                try{
	                	field[i].set(this, new Double(0));
	                } catch(Exception ee) {
		                try{
		                	field[i].set(this, new Integer(0));
		                } catch(Exception eee) {
			                try{
			                	field[i].set(this, new BigDecimal(0));
			                }catch(Exception eeee){
			                	this.destroy();
			                }
			                
			            }
		            }
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
		
		if(this.repFile.getFileSize() == 0 ) {
			errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("importCommerceComtrade.repFile.required"));
		}else{
			if(!this.getFileName().contains("xlsx")){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("importCommerceComtrade.repFile.extension"));
			}
		}
		
		if(!StringUtils.isBlank(oldFilename)){
			if(!oldFilename.equals(this.getFileName())){
				errors.add(BaseForm.GLOBAL_ERRORS, new ActionError("importCommerceComtrade.repFile.notSame"));
			}
		}
		
	}

	public void validateUpdate(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		this.validateInsert(mapping, request, errors);
		this.validateDelete(mapping, request, errors);
	}

	public void validateDelete(ActionMapping mapping, HttpServletRequest request, ActionErrors errors)throws Exception, Error {
		
	}

	/**
	 * @return the repFile
	 */
	public FormFile getRepFile() {
		return repFile;
	}

	/**
	 * @param repFile the repFile to set
	 */
	public void setRepFile(FormFile repFile) {
		this.repFile = repFile;
	}
	
	public byte[] getFileData() throws FileNotFoundException, IOException {
        return repFile.getFileData();
    }

    public String getFileName() {
        return repFile.getFileName();
    }

    public int getFileSize() {
        return repFile.getFileSize();
    }

    public InputStream getInputStream() throws FileNotFoundException, 
                                               IOException {
        return repFile.getInputStream();
    }

    public void setFileName(String arg0) {
        repFile.setFileName(arg0);
    }

    public void setFileSize(int arg0) {
        repFile.setFileSize(arg0);
    }

	/**
	 * @return the yearList
	 */
	public String getYearList() {
		return yearList;
	}

	/**
	 * @param yearList the yearList to set
	 */
	public void setYearList(String yearList) {
		this.yearList = yearList;
	}

	/**
	 * @return the oldFilename
	 */
	public String getOldFilename() {
		return oldFilename;
	}

	/**
	 * @param oldFilename the oldFilename to set
	 */
	public void setOldFilename(String oldFilename) {
		this.oldFilename = oldFilename;
	}
	
}
