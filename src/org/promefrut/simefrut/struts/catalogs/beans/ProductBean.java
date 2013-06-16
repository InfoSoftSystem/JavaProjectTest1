package org.promefrut.simefrut.struts.catalogs.beans;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.catalogs.forms.ProductForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class ProductBean extends BaseDAO {
	public final String TABLA = super.ESQUEMA_TABLA + "PRODUCTS_CTG";
	public static final String PREFIJO = "prod";
	protected final String DW_PREFIJO = "prod";
	protected final String DW_TABLA = super.ESQUEMA_TABLA + "PRODUCT_DIM";
	protected final String DW_TABLA_VARIETY = super.ESQUEMA_TABLA + "PRODUCT_VARIETY_DIM";
	protected final String DW_TABLA_TYPE = super.ESQUEMA_TABLA + "PRODUCT_TYPE_DIM";

	public ProductBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find() throws SQLException, Exception, Error {
		String sql = "select "+ PREFIJO +"_ID prodId,\n"
				+ " 	"+ PREFIJO +"_code prodCode, \n"
				+ " 	"+ PREFIJO +"_desc prodDesc, \n"
				+ "		"+ PREFIJO +"_status prodStatus,\n"
				+ "		CASE WHEN "+ PREFIJO +"_status = 'A' THEN '"
				+					 bundle.getString("registro.active") 
				+			"' ELSE '"
				+					bundle.getString("registro.inactive")
				+		"' END prodStatusText,\n"
				+ "		CASE WHEN audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
				+ "		coalesce(audit_user_upd, audit_user_ins) audit_user, \n" 
				+ "		coalesce(audit_date_upd, audit_date_ins) audit_date\n"
				+ " from " + TABLA;

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		//String resultJSON = "Ext.data.JsonP.callback1("+data+")";
		return data;
	}

	public int insert(ProductForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
					+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer prodId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into "+ TABLA + "\n"
					+ "("+ PREFIJO +"_ID,\n"
					+ PREFIJO +"_CODE,\n"
					+ PREFIJO +"_DESC,\n"
					+ PREFIJO + "_STATUS, \n"
					+ "    audit_user_ins, \n"
					+ "    audit_date_ins \n"
					+ ")\n"
					+ "values (?,?,?,?,?,now()"
					+ ")";

		Object[] param = new Object[5];
		int i = 0;
		param[i++] = prodId;
		param[i++] = form.getProdCode().trim();
		param[i++] = form.getProdDesc().trim();
	    param[i++] = form.getProdStatus();
	    param[i++] = username;
	    
	    form.setProdId(String.valueOf(prodId));

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	public int update(ProductForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ " set "+ PREFIJO +"_CODE = ?, \n"
					+ "    "+ PREFIJO +"_DESC= ?, \n"
					+ "    "+ PREFIJO +"_STATUS= ?, \n"
					+ "    audit_user_upd = ?, \n"
					+ "    audit_date_upd = current_date+current_time \n"
					+ " where "+ PREFIJO +"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[5];
		int i = 0;
		param[i++] = form.getProdCode().trim();
		param[i++] = form.getProdDesc().trim();
	    param[i++] = form.getProdStatus().trim();
	    param[i++] = username;
	    param[i++] = new Integer(form.getProdId());

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	@Deprecated
	public int delete(ProductForm form) throws SQLException, Exception, Error {
		/*String sql = "DELETE FROM " + TABLA
					+ " WHERE "+ PREFIJO +"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		param[0] = form.getCtrId();
		*/
		return 0;//query.update(sessionManager.getConnection(), sql, param);
	}
	
	/**
	 * Function for Data Warehouse updating. This function changes the expiry_date and inserts new registers
	 * @param {org.promefrut.simefrut.struts.catalogs.forms.ProductForm}form 
	 * @param {String} username
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	private void updateDW(ProductForm form, String username) throws SQLException, Exception, Error {
		updateProductDim(form,username);
		updateProductVarietyDim(form,username);
		updateProductTypeDim(form,username);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateProductDim(ProductForm form, String username) throws SQLException, Exception, Error {
		String sql = "select "+DW_PREFIJO+"_sk \n"
					+ " from " + DW_TABLA +" \n"
					+ " where "+DW_PREFIJO+"_ID = ?"
					+ " AND current_date between effective_date and expiry_date ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		int i = 0;
		param[i++] = new Integer(form.getProdId());
	    
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param,handler);
		
		if(resultList.size()>0){
			for(Iterator it = resultList.iterator(); it.hasNext(); ){
				Map reg = (Map)it.next();
				
				BigDecimal fieldSk = (BigDecimal)reg.get(DW_PREFIJO+"_sk");
				
				sql = " update " + DW_TABLA +" \n"
						+ " set EXPIRY_DATE = current_date-1, \n"
						+ "    AUDIT_USER_UPD = ?, \n"
						+ "    AUDIT_DATE_UPD = current_date + current_time \n"
						+ " where "+DW_PREFIJO+"_SK = ? ";
				
				param = new Object[2];
				i = 0;
				param[i++] = username;
				param[i++] = fieldSk;
				
				query.update(sessionManager.getConnection(), sql, param);
			}
		}
		
		if("A".equals(form.getProdStatus())){
			sql = " insert into "+DW_TABLA+"(\n"
				+ DW_PREFIJO+"_SK, \n"
				+ DW_PREFIJO+"_ID,\n" 
				+ DW_PREFIJO+"_CODE,\n"
				+ DW_PREFIJO+"_DESC,\n"
				+ " EFFECTIVE_DATE,\n"
				+ " EXPIRY_DATE, \n"
				+ " AUDIT_USER_INS, \n"
				+ " AUDIT_DATE_INS \n"
				+ ")\n"
				+ "values ((select coalesce(max("+DW_PREFIJO+"_SK) + 1,1)\n"
				+ "         from "+DW_TABLA+"),\n"
				+ " ?,?,?,CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
				+ ")";
	
			param = new Object[4];
			i = 0;
			param[i++] = new Integer(form.getProdId());
			param[i++] = form.getProdCode().trim();
			param[i++] = form.getProdDesc().trim();
		    param[i++] = username;
	
			query.update(sessionManager.getConnection(), sql, param);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateProductVarietyDim(ProductForm form, String username) throws SQLException, Exception, Error {
		String sql = "select PRDVAR_SK \n"
					+ " from " + DW_TABLA_VARIETY +" \n"
					+ " where "+DW_PREFIJO+"_ID = ?"
					+ " AND current_date between effective_date and expiry_date ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		int i = 0;
		param[i++] = new Integer(form.getProdId());
	    
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param,handler);
		
		if(resultList.size()>0){
			for(Iterator it = resultList.iterator(); it.hasNext(); ){
				Map reg = (Map)it.next();
				
				BigDecimal fieldSk = (BigDecimal)reg.get("prdvar_sk");

				
				sql = " update " + DW_TABLA_VARIETY +" \n"
						+ " set EXPIRY_DATE = current_date-1, \n"
						+ "    AUDIT_USER_UPD = ?, \n"
						+ "    AUDIT_DATE_UPD = current_date + current_time \n"
						+ " where PRDVAR_SK = ?";
				
				param = new Object[2];
				i = 0;
				param[i++] = username;
				param[i++] = fieldSk;
				
				query.update(sessionManager.getConnection(), sql, param);
				
				if("A".equals(form.getProdStatus())){
					sql = " insert into "+DW_TABLA_VARIETY+"(\n"
						+ "PRDVAR_SK, \n"
						+ DW_PREFIJO+"_ID,\n" 
						+ DW_PREFIJO+"_CODE,\n"
						+ DW_PREFIJO+"_DESC,\n"
						+ "VAR_ID,\n" 
						+ "VAR_CODE,\n"
						+ "VAR_DESC,\n"
						+ " EFFECTIVE_DATE,\n"
						+ " EXPIRY_DATE, \n"
						+ " AUDIT_USER_INS, \n"
						+ " AUDIT_DATE_INS \n"
						+ ")\n"
						+ " SELECT row_number() over(order by prdvar_sk) + (select coalesce(max(PRDVAR_SK),1)\n"
						+ "         from "+DW_TABLA_VARIETY+") PRDVAR_SK,\n"
						+ DW_PREFIJO+"_ID,\n" 
						+ "? ,\n"
						+ "?,\n"
						+ "VAR_ID,\n" 
						+ "VAR_CODE,\n"
						+ "VAR_DESC,\n"
						+ "CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
						+ "FROM "+DW_TABLA_VARIETY+"\n"
						+ " where PRDVAR_SK = ? \n";
			
					param = new Object[4];
					i = 0;
					param[i++] = form.getProdCode().trim();
					param[i++] = form.getProdDesc().trim();
				    param[i++] = username;
				    param[i++] = fieldSk;
			
					query.update(sessionManager.getConnection(), sql, param);
				}
			}
		}else{
			if("A".equals(form.getProdStatus())){
				sql = " insert into "+DW_TABLA_VARIETY+"(\n"
					+ "PRDVAR_SK, \n"
					+ DW_PREFIJO+"_ID,\n" 
					+ DW_PREFIJO+"_CODE,\n"
					+ DW_PREFIJO+"_DESC,\n"
					+ " EFFECTIVE_DATE,\n"
					+ " EXPIRY_DATE, \n"
					+ " AUDIT_USER_INS, \n"
					+ " AUDIT_DATE_INS \n"
					+ ")\n"
					+ "values ((select coalesce(max(PRDVAR_SK) + 1,1)\n"
					+ "         from "+DW_TABLA_VARIETY+"),\n"
					+ " ?,?,?,CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
					+ ")";
		
				param = new Object[4];
				i = 0;
				param[i++] = new Integer(form.getProdId());
				param[i++] = form.getProdCode().trim();
				param[i++] = form.getProdDesc().trim();
			    param[i++] = username;
		
				query.update(sessionManager.getConnection(), sql, param);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateProductTypeDim(ProductForm form, String username) throws SQLException, Exception, Error {
		String sql = "select ptype_sk \n"
					+ " from " + DW_TABLA_TYPE +" \n"
					+ " where "+DW_PREFIJO+"_ID = ?"
					+ " AND current_date between effective_date and expiry_date ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		int i = 0;
		param[i++] = new Integer(form.getProdId());
	    
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param,handler);
		
		if(resultList.size()>0){
			for(Iterator it = resultList.iterator(); it.hasNext(); ){
				Map reg = (Map)it.next();
				
				BigDecimal fieldSk = (BigDecimal)reg.get("ptype_sk");

				sql = " update " + DW_TABLA_TYPE +" \n"
						+ " set EXPIRY_DATE = current_date-1, \n"
						+ "    AUDIT_USER_UPD = ?, \n"
						+ "    AUDIT_DATE_UPD = current_date + current_time \n"
						+ " where PTYPE_SK = ?";
				
				param = new Object[2];
				i = 0;
				param[i++] = username;
				param[i++] = fieldSk;
				
				query.update(sessionManager.getConnection(), sql, param);
				
				if("A".equals(form.getProdStatus())){
					sql = " insert into "+DW_TABLA_TYPE+"(\n"
						+ "ptype_SK, \n"
						+ DW_PREFIJO+"_ID,\n" 
						+ DW_PREFIJO+"_CODE,\n"
						+ DW_PREFIJO+"_DESC,\n"
						+ "PTYPE_ID,\n" 
						+ "PTYPE_CODE,\n"
						+ "PTYPE_DESC,\n"
						+ " EFFECTIVE_DATE,\n"
						+ " EXPIRY_DATE, \n"
						+ " AUDIT_USER_INS, \n"
						+ " AUDIT_DATE_INS \n"
						+ ")\n"
						+ " SELECT row_number() over(order by ptype_SK) + (select coalesce(max(ptype_SK),1)\n"
						+ "         from "+DW_TABLA_TYPE+") ptype_SK,\n"
						+ DW_PREFIJO+"_ID,\n" 
						+ "? ,\n"
						+ "?,\n"
						+ "PTYPE_ID,\n" 
						+ "PTYPE_CODE,\n"
						+ "PTYPE_DESC,\n"
						+ "CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
						+ "FROM "+DW_TABLA_TYPE+"\n"
						+ " where ptype_SK = ? \n";
			
					param = new Object[4];
					i = 0;
					param[i++] = form.getProdCode().trim();
					param[i++] = form.getProdDesc().trim();
				    param[i++] = username;
				    param[i++] = fieldSk;
			
					query.update(sessionManager.getConnection(), sql, param);
				}
			}
		}else{
			if("A".equals(form.getProdStatus())){
				sql = " insert into "+DW_TABLA_TYPE+"(\n"
					+ "PTYPE_SK, \n"
					+ DW_PREFIJO+"_ID,\n" 
					+ DW_PREFIJO+"_CODE,\n"
					+ DW_PREFIJO+"_DESC,\n"
					+ " EFFECTIVE_DATE,\n"
					+ " EXPIRY_DATE, \n"
					+ " AUDIT_USER_INS, \n"
					+ " AUDIT_DATE_INS \n"
					+ ")\n"
					+ "values ((select coalesce(max(PTYPE_SK) + 1,1)\n"
					+ "         from "+DW_TABLA_TYPE+"),\n"
					+ " ?,?,?,CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
					+ ")";
		
				param = new Object[4];
				i = 0;
				param[i++] = new Integer(form.getProdId());
				param[i++] = form.getProdCode().trim();
				param[i++] = form.getProdDesc().trim();
			    param[i++] = username;
		
				query.update(sessionManager.getConnection(), sql, param);
			}
		}
	}
}
