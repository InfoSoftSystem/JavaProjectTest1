package org.promefrut.simefrut.struts.catalogs.beans;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.catalogs.forms.TraditionalUnitForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class TraditionalUnitBean extends BaseDAO {
	public final String TABLA = super.ESQUEMA_TABLA + "TRAD_UNIT_CTG";
	protected static final String PREFIJO = "tunit";
	protected final String DW_PREFIJO = "tunit";
	protected final String DW_TABLA = super.ESQUEMA_TABLA + "TRAD_UNIT_DIM";

	public TraditionalUnitBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find(Integer ctrId) throws SQLException, Exception, Error {
		String sql = "select "+ PREFIJO +"_ID tunitId,\n"
				+ " 	"+ PREFIJO +"_code tunitCode, \n"
				+ " 	"+ PREFIJO +"_desc tunitDesc, \n"
				+ "		"+ PREFIJO +"_status tunitStatus,\n"
				+ " 	ctr.CTR_ID ctrId,\n"
				+ " 	ctr.CTR_Desc ctrDescSpa,\n"
				+ " 	ctr.CTR_Desc_eng ctrDescEng,\n"
				+ " 	prod.PROD_ID prodId,\n"
				+ " 	prod.PROD_desc prodDesc,\n"
				+ "		ptype.ptype_id ptypeId, \n"
				+ "		ptype.ptype_desc ptypeDesc, \n"
				+ " 	var.VAR_ID varId,\n"
				+ " 	var.VAR_Desc varDesc,\n"
				+ " 	size.SIZE_ID sizeId,\n"
				+ " 	size.SIZE_Desc sizeDesc,\n"
				+ " 	"+ PREFIJO +"_kilo tunitKilo,\n"
				+ "		CASE WHEN "+ PREFIJO +"_status = 'A' THEN '"
				+					 bundle.getString("registro.active") 
				+			"' ELSE '"
				+					bundle.getString("registro.inactive")
				+		"' END tunitStatusText,\n"
				+ "		CASE WHEN ctr.audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
				+ "		coalesce(ctr.audit_user_upd, ctr.audit_user_ins) audit_user, \n" 
				+ "		coalesce(ctr.audit_date_upd, ctr.audit_date_ins) audit_date\n"
				+ " from " + TABLA+" tunit left outer join "+ ESQUEMA+".VARIETIES_CTG var \n"
				+ " 	on tunit.var_id = var.var_id left outer join "+ ESQUEMA+".product_type_ctg ptype \n"
				+ " 		on ptype.ptype_id = tunit.ptype_id, "
				+ ESQUEMA+".COUNTRIES_CTG ctr, \n"
				+ ESQUEMA+".SIZE_CTG size, \n"
				+ ESQUEMA+".PRODUCTS_CTG prod \n"
				+ " where tunit.ctr_id = ctr.ctr_id \n"
				+ "   and tunit.size_id = size.size_id \n"
				+ "   and tunit.prod_id = prod.prod_id \n";
		
		//If the number is less than 0 it means that the current user can see data from other countries
		if(ctrId.intValue()>0){
			sql += " \n AND ctr.CTR_ID = ? \n";
		}
		sql += "ORDER BY \n"
			+ " "+ PREFIJO +"_ID DESC \n";

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		
		Object[] param = new Object[1];
		int i = 0;
		param[i++] = ctrId;
		
		List<Map> resultList = null;

		if(ctrId.intValue()>0){
			resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param, handler);
		}else{
			resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		}
		
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		//String resultJSON = "Ext.data.JsonP.callback1("+data+")";
		return data;
	}

	public int insert(TraditionalUnitForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
					+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer tunitId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into "+ TABLA + "\n"
					+ "("+ PREFIJO +"_ID,\n"
					+ PREFIJO +"_CODE,\n"
					+ PREFIJO +"_DESC,\n"
					+ PREFIJO + "_STATUS, \n"
					+ "CTR_ID,\n"
					+ "PROD_ID,\n";
		if(form.getVarId()>0){
				sql+= "VAR_ID,\n";
		}
		
		if(form.getPtypeId()>0){
				sql+= "PTYPE_ID,\n";
		}
		sql +=		"SIZE_ID,\n"
					+ ""+ PREFIJO +"_kilo,\n"
					+ "    audit_user_ins, \n"
					+ "    audit_date_ins \n"
					+ ")\n"
					+ "values (?,?,?,?,?,?,";
		if(form.getVarId()>0){
			sql+= "?,";
		}
		if(form.getPtypeId()>0){
			sql+= String.valueOf(form.getPtypeId())+",";
		}
		sql +="?,?,?,now()"
					+ ")";

		Object[] param = new Object[form.getVarId()>0?10:9];
		int i = 0;
		param[i++] = tunitId;
		param[i++] = tunitId;//form.getTunitCode().trim();
		param[i++] = form.getTunitDesc().trim();
	    param[i++] = form.getTunitStatus();
	    param[i++] = form.getCtrId();
		param[i++] = form.getProdId();
		
		if(form.getVarId()>0){
			param[i++] = form.getVarId();
		}
		
	    param[i++] = form.getSizeId();
	    param[i++] = form.getTunitKilo();
	    param[i++] = username;
	    
	    form.setTunitId(tunitId);

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	public int update(TraditionalUnitForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ " set "
					//""+ PREFIJO +"_CODE = ?, \n"
					+ "    "+ PREFIJO +"_DESC= ?, \n"
					+ "    "+ PREFIJO +"_STATUS= ?, \n"
					+ "		CTR_ID=?,\n"
					+ "		PROD_ID=?,\n"
					+ "		VAR_ID= case when ? = 0 then null else ? end,\n";
		
		if(form.getPtypeId()>0){
			sql+= "    PTYPE_ID = "+String.valueOf(form.getPtypeId())+",\n";
		}else{
			sql+= " PTYPE_ID = null, \n";
		}
				sql+= "		SIZE_ID=?,\n"
					+ "		"+ PREFIJO +"_kilo=?,\n"
					+ "    audit_user_upd = ?, \n"
					+ "    audit_date_upd = now() \n"
					+ " where "+ PREFIJO +"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[10];
		int i = 0;
		//param[i++] = form.getTunitCode().trim();
		param[i++] = form.getTunitDesc().trim();
	    param[i++] = form.getTunitStatus();
	    param[i++] = form.getCtrId();
		param[i++] = form.getProdId();
	    param[i++] = form.getVarId();
	    param[i++] = form.getVarId();
	    param[i++] = form.getSizeId();
	    param[i++] = form.getTunitKilo();
	    param[i++] = username;
	    param[i++] = form.getTunitId();

		i = query.update(sessionManager.getConnection(), sql, param);
		
		updateDW(form,username);
		return i;
	}

	@Deprecated
	public int delete(TraditionalUnitForm form) throws SQLException, Exception, Error {
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
	 * @param {org.promefrut.simefrut.struts.catalogs.forms.TraditionalUnitForm}form 
	 * @param {String} username
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateDW(TraditionalUnitForm form, String username) throws SQLException, Exception, Error {
		String sql = "select "+DW_PREFIJO+"_sk \n"
					+ " from " + DW_TABLA +" \n"
					+ " where "+DW_PREFIJO+"_ID = ?"
					+ " AND current_date between effective_date and expiry_date ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		int i = 0;
		param[i++] = form.getTunitId();
	    
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
		
		if("A".equals(form.getTunitStatus())){
			i =0;
			sql = " insert into "+DW_TABLA+"(\n"
				+ DW_PREFIJO+"_SK, \n"
				+ DW_PREFIJO+"_ID,\n" 
				+ DW_PREFIJO+"_CODE,\n"
				+ DW_PREFIJO+"_DESC,\n"
				+ "CTR_ID,\n"
				+ "PROD_ID,\n";
			
			if(form.getVarId()>0){		
				sql += "VAR_ID,\n";
			}
			if(form.getPtypeId()>0){
				sql += "PTYPE_ID,\n";
			}
			
			sql += "SIZE_ID,\n"
				+ ""+ PREFIJO +"_kilo,\n"
				+ " EFFECTIVE_DATE,\n"
				+ " EXPIRY_DATE, \n"
				+ " AUDIT_USER_INS, \n"
				+ " AUDIT_DATE_INS \n"
				+ ")\n"
				+ "values ((select coalesce(max("+DW_PREFIJO+"_SK) + 1,1)\n"
				+ "         from "+DW_TABLA+"),\n"
				+ " ?,?,?,?,?,";
			
			if(form.getVarId()>0){
				sql +="?,";
				i=1;
			}
			
			if(form.getPtypeId()>0){
				sql+= String.valueOf(form.getPtypeId())+",";
			}
			
			sql +="?,?,CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
				+ ")";
	
			param = new Object[8+i];
			i = 0;
			param[i++] = form.getTunitId();
			param[i++] = form.getTunitCode().trim();
			param[i++] = form.getTunitDesc().trim();
			param[i++] = form.getCtrId();
			param[i++] = form.getProdId();
			
			if(form.getVarId()>0){
				param[i++] = form.getVarId();
			}
		    
			param[i++] = form.getSizeId();
		    param[i++] = form.getTunitKilo();
		    param[i++] = username;
	
			query.update(sessionManager.getConnection(), sql, param);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductsCollection() throws Exception, Error {
		String sql = "select distinct\n"
			+ "prod_id prodId,\n"
			+ "prod_desc prodDesc\n"
			+ "from "+ ESQUEMA +".PRODUCTS_CTG\n"
			+ "where prod_status = 'A'\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}

	/**
	 * @return JSON String containing the varieties
	 * @throws Exception
	 * @throws Error
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getVarietiesCollection() throws Exception, Error {
		String sql = "select prod_id prodId, \n"
				+ "	var_id varId, \n"
				+ "	var_desc varDesc \n"
				+ "from "+ESQUEMA+".VARIETIES_CTG \n"
				+ "where var_status = 'A' \n"
				+ " union " 
				+ " select 0 prodId, 0 varId, '' varDesc ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		//String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		//String resultJSON = "Ext.data.JsonP.callback1("+data+")";
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getCountriesCollection(UserForm user) throws Exception, Error {
		String sql = "select \n"
			+ "ctr_id ctrId, \n"
			+ "ctr_desc ctrDescSpa,\n"
			+ "ctr_desc_eng ctrDescEng\n"
			+ "from "+ ESQUEMA +".COUNTRIES_CTG\n"
			+ "where ctr_status = 'A'\n";
		
		if(!user.getNoCountry()){
			sql+= " and ctr_id = " + user.getCtrId();
		}
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getSizesCollection() throws Exception, Error {
		String sql = "select \n"
			+ "size_id sizeId,\n"
			+ "size_desc sizeDesc\n"
			+ "from "+ ESQUEMA +".SIZE_CTG\n"
			+ "where SIZE_STATUS = 'A'\n";

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductTypesCollection() throws Exception, Error {
		String sql = "select distinct\n"
			+ "prod_id prodId,\n"
			+ "ptype_id ptypeId,\n"
			+ "ptype_desc ptypeDesc\n"
			+ "from "+ ESQUEMA +".product_type_ctg\n"
			+ "where ptype_STATUS = 'A'\n";
			
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object>countRegisters(TraditionalUnitForm form) throws SQLException, Exception, Error {
		Object[] param = null;
		int sizeArray = 5;
		String sql = "select "+ PREFIJO +"_ID tunitId,\n"
				+ " 	"+ PREFIJO +"_code tunitCode, \n"
				+ " 	"+ PREFIJO +"_desc tunitDesc, \n"
				+ "		"+ PREFIJO +"_status tunitStatus,\n"
				+ " 	ctr.CTR_ID ctrId,\n"
				+ " 	ctr.CTR_Desc ctrDescSpa,\n"
				+ " 	ctr.CTR_Desc_eng ctrDescEng,\n"
				+ " 	prod.PROD_ID prodId,\n"
				+ " 	prod.PROD_desc prodDesc,\n"
				+ "		ptype.ptype_id ptypeId, \n"
				+ "		ptype.ptype_desc ptypeDesc, \n"
				+ " 	var.VAR_ID varId,\n"
				+ " 	var.VAR_Desc varDesc,\n"
				+ " 	size.SIZE_ID sizeId,\n"
				+ " 	size.SIZE_Desc sizeDesc,\n"
				+ " 	"+ PREFIJO +"_kilo tunitKilo,\n"
				+ "		CASE WHEN "+ PREFIJO +"_status = 'A' THEN '"
				+					 bundle.getString("registro.active") 
				+			"' ELSE '"
				+					bundle.getString("registro.inactive")
				+		"' END tunitStatusText\n"
				+ " from " + TABLA+" tunit left outer join "+ ESQUEMA+".VARIETIES_CTG var \n"
				+ " 	on tunit.var_id = var.var_id \n" 
				+ "		left outer join public.product_type_ctg ptype \n"
				+ " 		on ptype.ptype_id = tunit.ptype_id, "
				+ ESQUEMA+".COUNTRIES_CTG ctr, \n"
				+ ESQUEMA+".SIZE_CTG size, \n"
				+ ESQUEMA+".PRODUCTS_CTG prod \n"
				+ " where tunit.ctr_id = ctr.ctr_id \n"
				+ "   and tunit.size_id = size.size_id \n"
				+ "   and tunit.prod_id = prod.prod_id \n"
				+ "   and tunit.ctr_id = ? \n"
				+ "   and tunit.size_id = ? \n"
				+ "   and tunit.prod_id = ? \n"
				+ "   and lower(tunit.tunit_desc) = ? \n"
				+ "   and tunit.tunit_kilo = ? \n";
		if(form.getVarId()>0){
			sql+= "   and tunit.var_id = ? \n";
			++sizeArray;
		}
		if(form.getTunitId()>0){
			sql+= "   and tunit.tunit_id <> ? \n";
			++sizeArray;
		}
		param = new Object[sizeArray];
		
		MapHandler handler = new MapHandler();
		QueryRunner query = new QueryRunner();
		
		int i = 0;
		param[i++] = form.getCtrId();
		param[i++] = form.getSizeId();
		param[i++] = form.getProdId();
		param[i++] = form.getTunitDesc().toLowerCase().trim();
		param[i++] = form.getTunitKilo();
		
		if(form.getVarId()>0){
			param[i++] = form.getVarId();
		}
		
		if(form.getTunitId()>0){
			param[i++] = form.getTunitId();
		}
		
		Map<String,Object> resultList = null;

		resultList = (Map<String,Object>)query.query(sessionManager.getConnection(), sql, param, handler);
		
		if(resultList==null){
			resultList = new HashMap<String,Object>();
		}
		
		return resultList;
	}
}
