package org.promefrut.simefrut.struts.maintenances.beans;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.maintenances.forms.PriceForm;


/**
 * @author HWM
 *
 */
public class PriceBean extends BaseDAO {
	public final String TABLA = super.ESQUEMA_TABLA + "PRICE_FACT";
	protected static final String PREFIJO = "price";
	//private final String HIST_PREFIJO = PREFIJO;
	public final String HIST_TABLA = super.ESQUEMA_TABLA + "PRICE_FACT_HIST";
	private final String QUERY_SELECT = "SELECT  \n"
			+ "  price.price_id priceId,  \n"
			+ "  price.date_sk dateSk,  \n"
			+ "  price.prdvar_sk prdVarSk,  \n"
			+ "  price.qua_sk quaSk,  \n"
			+ "  price.size_sk sizeSk,  \n"
			+ "  price.tunit_sk tunitSk,  \n"
			+ "  price.ori_sk oriSk,  \n"
			+ "  price.ctr_sk ctrSk,  \n"
			+ "  price.clev_sk clevSk,  \n"
			+ "  price.ptype_sk ptypeSk,  \n"
			+ "  price.price_copy_flag priceCopyFlag,  \n"
			+ "  case when price.price_copy_flag = 1 then '"+super.bundle.getString("price.grid.original")+"' " +
			"		else '"+super.bundle.getString("price.grid.new")+"' end priceCopyFlagText,  \n"
			+ "  price.price_inf_uni priceInfUni,  \n"
			+ "  price.price_sup_uni priceSupUni,  \n"
			+ "  price.price_inf_kilo priceInfKilo,  \n"
			+ "  price.price_sup_kilo priceSupKilo,  \n"
			+ "  price.audit_user_ins auditUserIns,  \n"
			+ "  price.audit_date_ins auditDateIns,  \n"
			+ "  price.audit_user_upd auditUserUpd,  \n"
			+ "  price.audit_date_upd auditDateUpd,  \n"
			+ "  country.ctr_iso3 ctrIso3,  \n"
			+ "  origin.ctr_id oriCtrId,  \n"
			+ "  origin.ori_country ctrdescspa,  \n"
			+ "  origin.ori_country_eng ctrdesceng,  \n"
			+ "  origin.reg_id oriRegId,  \n"
			+ "  origin.reg_desc oriRegDesc,  \n"
			+ "  origin.prov_id oriProvId,  \n"
			+ "  origin.prov_desc oriProvDesc,  \n"
			+ "  tradunit.tunit_desc tunitMeasure,  \n"
			+ "  tradunit.tunit_kilo tunitKilo,  \n"
			+ "  to_char(fecha.date_field,'dd/mm/yyyy') dateField,  \n"
			+ "  product.prod_id prodId,  \n"
			+ "  ptype.ptype_id ptypeId,  \n"
			+ "  product.var_id varId,  \n"
			+ "  size.size_code,  \n"
			+ "  size.size_desc sizeDesc,  \n"
			+ "  quality.qua_code,  \n"
			+ "  quality.qua_desc quaDesc,  \n"
			+ "  product.prod_code,  \n"
			+ "  product.prod_desc||coalesce(' '||ptype_desc, '') prodDesc,  \n"
			+ "  product.var_code,  \n"
			+ "  product.var_desc varDesc,  \n"
			+ "  country.ctr_desc,  \n"
			+ "  commerce.clev_code,  \n"
			+ "  commerce.clev_desc clevDesc,  \n"
			+ "  tradunit.tunit_code \n"
			+ "FROM  \n"
			+ "  "+ ESQUEMA +".price_fact price, \n"
			+ "  "+ ESQUEMA +".quality_dim quality,  \n"
			+ "  "+ ESQUEMA +".size_dim size,  \n"
			+ "  "+ ESQUEMA +".date_dim fecha,  \n"
			+ "  "+ ESQUEMA +".product_variety_dim product,  \n"
			+ "  "+ ESQUEMA +".trad_unit_dim tradunit,  \n"
			+ "  "+ ESQUEMA +".commerce_level_dim commerce,  \n"
			+ "  "+ ESQUEMA +".origin_dim origin,  \n"
			+ "  "+ ESQUEMA +".country_dim country, \n"
			+ "  "+ ESQUEMA +".product_type_dim ptype \n"  
			+ "WHERE  \n"
			+ "  quality.qua_sk = price.qua_sk AND \n"
			+ "  size.size_sk = price.size_sk AND \n"
			+ "  fecha.date_sk = price.date_sk AND \n"
			+ "  product.prdvar_sk = price.prdvar_sk AND \n"
			+ "  tradunit.tunit_sk = price.tunit_sk AND \n"
			+ "  commerce.clev_sk = price.clev_sk AND \n"
			+ "  origin.ori_sk = price.ori_sk AND \n"
			+ "  country.ctr_sk = price.ctr_sk AND \n"
			+ "  ptype.ptype_sk = price.ptype_sk ";

	public PriceBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find(BigDecimal ctrSk, String dateField) throws SQLException, Exception, Error {
		String sql = new String();
		
		sql= QUERY_SELECT 
				+ "  AND price.date_sk = ? \n";
		
		//If the number is less than 0 it means that the current user can see data from other countries
		if(ctrSk.intValue()>0){
			sql += "		AND country.ctr_sk = ? \n";
		}
		
		sql += "ORDER BY \n"
				+ "  fecha.date_field DESC \n";
		
		String[] fechasExp = dateField.split("/");
		Integer dateSk = new Integer(fechasExp[2]+fechasExp[1]+fechasExp[0]);
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		
		Object[] param = null; 
		if(ctrSk.intValue()>0){
			param = new Object[2];
		}else{
			param = new Object[1];
		}
		int i = 0;
		param[i++] = dateSk;
		
		if(ctrSk.intValue()>0){
			param[i++] = ctrSk;
		}

		List<Map> resultList = null;
		
		resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param, handler);
		
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		//String resultJSON = "Ext.data.JsonP.callback1("+data+")";
		return data;
	}

	public int insert(PriceForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
					+ " from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		BigDecimal priceId = (BigDecimal)query.query(sessionManager.getConnection(), sql, handler);
		/*String ptypeSk = new String();
		
		sql="select ptype_sk::varchar "
			+" from "+ESQUEMA+".product_type_dim "
			+" where current_date between effective_date and expiry_date "
			+" and prod_id = "+form.getProdId()
			+" and ptype_id " + (StringUtils.isEmpty(form.getPtypeId())?"is null":"="+form.getPtypeId());
		
		ptypeSk = (String)query.query(sessionManager.getConnection(), sql, handler);*/
		
		sql = " insert into "+ TABLA + "(\n"
				+ "  price_id,  \n"
				+ "  date_sk,  	\n"
				+ "  prdvar_sk,  \n"
				+ "  ptype_sk,  \n"
				+ "  qua_sk,  	\n"
				+ "  size_sk,  	\n"
				+ "  tunit_sk,  \n"
				+ "  ori_sk,  	\n"
				+ "  ctr_sk,  	\n"
				+ "  clev_sk,  	\n"
				+ "  price_copy_flag,  	\n"
				+ "  price_inf_uni,   \n"
				+ "  price_sup_uni,   \n"
				+ "  price_inf_kilo,  \n"
				+ "  price_sup_kilo,  \n"
				+ "  audit_user_ins,  \n"
				+ "  audit_date_ins   \n"
				+ ")\n"
				+ "values (" 
				+ "?,?,?,?,?,?,?,?,?,?,0,?,?,?,?,?,now()"
				+ ")";
		
		String[] fechasExp = form.getDateField().split("/");
		
		this.fillDateDim(form.getDateField());
		form.setPriceId(priceId.doubleValue());
		form.setDateSk(new Integer(fechasExp[2]+fechasExp[1]+fechasExp[0]));
		
		Object[] param = new Object[15];
		int i = 0;
		param[i++] = new BigDecimal(form.getPriceId());
		param[i++] = form.getDateSk(); 
		param[i++] = new BigDecimal(form.getPrdVarSk()); //getPrdVar_SK();
		param[i++] = new BigDecimal(form.getPtypeSk());
		param[i++] = new BigDecimal(form.getQuaSk());
	    param[i++] = new BigDecimal(form.getSizeSk());
	    param[i++] = new BigDecimal(form.getTunitSk());
	    param[i++] = form.getOriSk();
	    param[i++] = new BigDecimal(form.getCtrSk());
	    param[i++] = new BigDecimal(form.getClevSk());
	    param[i++] = new Double(form.getPriceInfUni());
	    param[i++] = new Double(form.getPriceSupUni());
	    param[i++] = new Double(form.getPriceInfKilo());
	    param[i++] = new Double(form.getPriceSupKilo());
	    param[i++] = username;
	    
		i = query.update(sessionManager.getConnection(), sql, param);
		
		insertHist(form, username, "insert");
		return i;
	}

	public int update(PriceForm form, String username) throws SQLException, Exception, Error {
		String sql = new String();
		QueryRunner query = new QueryRunner();
		
		sql =" update " + TABLA + " \n"
			+ " set "
			+ "  date_sk=?,  	\n"
			+ "  prdvar_sk=?,  \n"
			+ "	 ptype_sk = ?, \n"
			+ "  qua_sk=?,  	\n"
			+ "  size_sk=?,  	\n"
			+ "  tunit_sk=?,  \n"
			+ "  ori_sk=?,  	\n"
			+ "  ctr_sk=?,  	\n"
			+ "  clev_sk=?,  	\n"
			+ "	 price_copy_flag = case when price_copy_flag = 1 then 2 else price_copy_flag end,"
			+ "  price_inf_uni=?,   \n"
			+ "  price_sup_uni=?,   \n"
			+ "  price_inf_kilo=?,  \n"
			+ "  price_sup_kilo=?,  \n"
			+ "  audit_user_upd = ?, \n"
			+ "  audit_date_upd = now() \n"
			+ " where PRICE_ID = ? ";


		String[] fechasExp = form.getDateField().split("/");
		SimpleDateFormat format = new SimpleDateFormat("d/M/y");
		
		this.fillDateDim(form.getDateField());
		form.setDateSk(new Integer(fechasExp[2]+fechasExp[1]+fechasExp[0]));
		
		updateDateSk(form.getDateSk(), format.parse(form.getDateField()));
		
		Object[] param = new Object[15];
		int i = 0;
		param[i++] = form.getDateSk();
		param[i++] = new BigDecimal(form.getPrdVarSk()); //getPrdVar_SK();
		param[i++] = new BigDecimal(form.getPtypeSk());
		param[i++] = new BigDecimal(form.getQuaSk());
	    param[i++] = new BigDecimal(form.getSizeSk());
	    param[i++] = new BigDecimal(form.getTunitSk());
	    param[i++] = form.getOriSk();
	    param[i++] = new BigDecimal(form.getCtrSk());
	    param[i++] = new BigDecimal(form.getClevSk());
	    param[i++] = new Double(form.getPriceInfUni());
	    param[i++] = new Double(form.getPriceSupUni());
	    param[i++] = new Double(form.getPriceInfKilo());
	    param[i++] = new Double(form.getPriceSupKilo());
	    param[i++] = username;
	    param[i++] = new BigDecimal(form.getPriceId());

		i = query.update(sessionManager.getConnection(), sql, param);
		
		insertHist(form,username,"update");
		return i;
	}

	public int delete(PriceForm form, String username) throws Exception, Error {
		String sql = new String();
		
		sql = QUERY_SELECT
			+ "  AND price.price_id = ? \n";
		
		QueryRunner query = new QueryRunner();
		BeanHandler handler = new BeanHandler(PriceForm.class);
		Object[] param = new Object[1];
		param[0] = form.getPriceId();
		
		form = (PriceForm)query.query(sessionManager.getConnection(), sql, param, handler);
		
		sql = "DELETE FROM " + TABLA
				+ " WHERE "+ PREFIJO +"_ID = ? ";
		
		String[] fechasExp = form.getDateField().split("/");
		
		form.setOriSk(getOriSK(form.getOriCtrId(), form.getOriRegId(), form.getOriProvId()));
		form.setDateSk(new Integer(fechasExp[2]+fechasExp[1]+fechasExp[0]));
		
		insertHist(form, username, "delete");
		return query.update(sessionManager.getConnection(), sql, param);
	}
	
	/**
	 * Get the surrogate key for the origin dimension. IF not found, then throws an Exception
	 * @param oriCtrId
	 * @param oriRegId
	 * @param oriProvId
	 * @return
	 * @throws Exception
	 * @throws Error
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BigDecimal getOriSK(String oriCtrId, String oriRegId, String oriProvId) throws Exception, Error{
		BigDecimal result = null;
		oriCtrId= oriCtrId==null?"":oriCtrId;
		oriRegId= oriRegId==null?"":oriRegId;
		oriProvId= oriProvId==null?"":oriProvId;
		
		//Buscar la combinacion de Pais, Region, Provincia
		String tablaOri = ESQUEMA+".ORIGIN_DIM";
		
		String sql = "select ori_sk \n"
				+ " from " + tablaOri +" \n"
				+ " where CTR_ID = " + oriCtrId
				+ "   AND REG_ID " + ("".equals(oriRegId)?"is null": "="+oriRegId+"")
				+ "   AND PROV_ID " + ("".equals(oriProvId)?"is null": "="+oriProvId+"")
				+ "   AND current_date BETWEEN effective_date AND expiry_date ";
	
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		
		if(resultList.size() == 0){
			throw new Exception(bundle.getString("price.oriSk.notFound"));
		}else if(resultList.size() > 1){
			throw new Exception(bundle.getString("price.oriSk.moreThanOne"));
		}else{
			for(Iterator it = resultList.iterator(); it.hasNext(); ){
				Map reg = (Map)it.next();
				
				result = (BigDecimal)reg.get("ori_sk");
			}
		}
		return result;
	}
	
	/**
	 * Function for Data Warehouse historical. This function inserts new registers as historical data, represanting the changes
	 * that has been made on the principal table.
	 * @param {org.promefrut.simefrut.struts.maintenances.forms.PriceForm}form 
	 * @param {String} username
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	private void insertHist(PriceForm form, String username, String accion) throws SQLException, Exception, Error {
		String sql = new String();
		QueryRunner query = new QueryRunner();
	
		sql = " insert into "+HIST_TABLA+"(\n"
			+ "  pricehist_sk,  \n"
			+ "  price_id,  \n"
			+ "  date_sk,  	\n"
			+ "  prdvar_sk,  \n"
			+ "  ptype_sk,  \n"
			+ "  qua_sk,  	\n"
			+ "  size_sk,  	\n"
			+ "  tunit_sk,  \n"
			+ "  ori_sk,  	\n"
			+ "  ctr_sk,  	\n"
			+ "  clev_sk,  	\n" 
			+ "  price_copy_flag,  	\n"
			+ "  price_inf_uni,   \n"
			+ "  price_sup_uni,   \n"
			+ "  price_inf_kilo,  \n"
			+ "  price_sup_kilo,  \n"
			+ "  audit_user_ins,  \n"
			+ "  audit_date_ins,   \n"
			+ "  action   \n"
			+ ")\n"
			+ "values ((select coalesce(max(PRICEHIST_SK) + 1,1)\n"
			+ "         from " + HIST_TABLA +"),"
			+ "?,?,?,?,?,?,?,?,?,?,(select price_copy_flag from "+TABLA+" where price_id = ?),?,?,?,?,?,now(),?"
			+ ")";

		Object[] param = new Object[17];
		int i = 0;
		
		param[i++] = new BigDecimal(form.getPriceId());
		param[i++] = form.getDateSk(); 
		param[i++] = new BigDecimal(form.getPrdVarSk()); //getPrdVar_SK();
		param[i++] = new BigDecimal(form.getPtypeSk());
		param[i++] = new BigDecimal(form.getQuaSk());
	    param[i++] = new BigDecimal(form.getSizeSk());
	    param[i++] = new BigDecimal(form.getTunitSk());
	    param[i++] = form.getOriSk();
	    param[i++] = new BigDecimal(form.getCtrSk());
	    param[i++] = new BigDecimal(form.getClevSk());
	    param[i++] = new BigDecimal(form.getPriceId());
	    param[i++] = new Double(form.getPriceInfUni());
	    param[i++] = new Double(form.getPriceSupUni());
	    param[i++] = new Double(form.getPriceInfKilo());
	    param[i++] = new Double(form.getPriceSupKilo());
	    param[i++] = username;
	    param[i++] = accion;

		query.update(sessionManager.getConnection(), sql, param);
	}
	
	/**
	 * Generate date register for Date Dimension, if it doesn't exists.
	 * @param dateSK
	 * @param dateField
	 * @throws Exception
	 * @throws Error
	 */
	private void updateDateSk(Integer dateSK, Date dateField) throws Exception, Error{
		String sql = "select count(date_sk) \n"
				+ " from " + ESQUEMA +".DATE_DIM \n"
				+ " where DATE_SK = ?";
		
		ScalarHandler handler = new ScalarHandler();
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		int i = 0;
		
		param[i++] = dateSK;
	    
		Long resultList = (Long)query.query(sessionManager.getConnection(), sql, param,handler);
		
		if(resultList.intValue() == 0){
			sql ="select FillDateDim(?)";
			i = 0;
			param[i++] = dateField;
			
			query.update(sessionManager.getConnection(), sql, param);
		}
	}
	
	/**
	 * @return JSON String containing the varieties
	 * @throws Exception
	 * @throws Error
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getVarietiesCollection() throws Exception, Error {
		String sql = "select prdvar_sk prdVarSk, \n"
				+ "	prod_id prodId, \n"
				//+ "	prod_desc||coalesce(' '||ptype_desc, '')  prodDesc, \n"
				+ "	var_id varId, \n"
				+ "	var_desc varDesc \n"
				+ "from "+ESQUEMA+".product_variety_dim \n"
				+ "where current_date between effective_date and expiry_date \n"
				+ " and var_id is not null\n"
				+ " union " 
				+ " select 0 prdVarSk, 0 prodId, /*'' prodDesc,*/ 0 varId, '' varDesc ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		//String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		//String resultJSON = "Ext.data.JsonP.callback1("+data+")";
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductsCollection() throws Exception, Error {
		String sql = "select distinct\n"
			+ "prod_id prodId,\n"
			+ "ptype_id ptypeId,\n"
			+ "prod_desc||coalesce(' '||ptype_desc, '')  prodDesc\n"
			+ "from "+ ESQUEMA +".product_type_dim \n"
			+ "where current_date between effective_date and expiry_date\n";
			
		
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
			+ "from "+ ESQUEMA +".product_type_dim\n"
			+ "where current_date between effective_date and expiry_date\n"
			+ "  and ptype_id is not null";
			
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getCountriesCollection(UserForm user) throws Exception, Error {
		String sql = "select \n"
			+ "ctr_sk ctrSk,\n"
			+ "ctr_id ctrId, \n"
			+ "ctr_desc ctrDescSpa,\n"
			+ "ctr_desc_eng ctrDescEng\n"
			+ "from "+ ESQUEMA +".country_dim\n"
			+ "where current_date between effective_date and expiry_date\n";
		
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
			+ "size_sk sizeSk,\n"
			+ "size_id sizeId,\n"
			+ "size_desc sizeDesc\n"
			+ "from "+ ESQUEMA +".size_dim\n"
			+ "where current_date between effective_date and expiry_date\n";

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getQualitiesCollection() throws Exception, Error {
		String sql = "select \n"
			+ "qua_sk quaSk,\n"
			+ "qua_desc quaDesc\n"
			+ "from "+ ESQUEMA +".quality_dim\n"
			+ "where current_date between effective_date and expiry_date\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getTraditionalUnitsCollection() throws Exception, Error {
		String sql = "select \n"
			+ "tunit_sk tunitSk,\n"
			+ "ctr_id ctrId,\n"
			+ "prod_id prodId,\n"
			+ "var_id prdVarId,\n"
			+ "ptype_id ptypeId,\n"
			+ "size_id sizeId,\n"
			+ "tunit_desc ||' ('||tunit_kilo::varchar||' Kg.)' tunitDesc,\n"
			+ "tunit_kilo tunitKilo \n"
			+ "from "+ ESQUEMA +".TRAD_UNIT_dim\n"
			+ "where current_date between effective_date and expiry_date\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getCommLevelsCollection() throws Exception, Error {
		String sql = "select \n"
			+ "clev_sk clevSk,\n"
			+ "clev_desc clevDesc\n"
			+ "from "+ ESQUEMA +".commerce_level_dim\n"
			+ "where current_date between effective_date and expiry_date\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getOriContriesCollection() throws Exception, Error {
		String sql = "select distinct \n"
			+ "ctr_id oriCtrId,\n"
			+ "ori_country ctrdescspa,\n"
			+ "ori_country_eng ctrdesceng\n"
			+ "from "+ ESQUEMA +".ORIGIN_DIM\n"
			+ "where current_date between effective_date and expiry_date\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getOriRegionsCollection() throws Exception, Error {
		String sql = "select distinct \n"
			+ "reg_id oriRegId,\n"
			+ "ctr_id oriCtrId,\n"
			+ "reg_desc oriRegDesc\n"
			+ "from "+ ESQUEMA +".ORIGIN_DIM\n"
			+ "where current_date between effective_date and expiry_date\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getOriProvincesCollection() throws Exception, Error {
		String sql = "select distinct\n"
			//+ "ori_sk oriSk,\n"
			+ "prov_id oriProvId,\n"
			+ "reg_id oriRegId,\n"
			+ "prov_desc oriProvDesc\n"
			+ "from "+ ESQUEMA +".ORIGIN_DIM\n"
			+ "where current_date between effective_date and expiry_date\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}

	
	private int fillDateDim(String dateField)throws Exception, Error{
		String sql = "select fillDateDim(to_date('"+dateField+"','dd/mm/yyyy'))" ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer reg = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		return reg.intValue();
	}
	
	public int copyData(PriceForm form, String username) throws SQLException, Exception, Error {
		String sql = new String();
		QueryRunner query = new QueryRunner();
		Object[] param = null;
		int i = 0;
		
		ScalarHandler handler = new ScalarHandler();
		String[] fechasExp = form.getDateTo().split("/");
		
		this.fillDateDim(form.getDateTo());
		form.setDateSk(new Integer(fechasExp[2]+fechasExp[1]+fechasExp[0]));
		form.setDateTo(fechasExp[2]+fechasExp[1]+fechasExp[0]);
		
		sql = "select max(date_sk)::varchar \n" +
			" from "+TABLA+"\n"+
			" where ctr_sk = ?\n" +
			"    AND date_sk < ?";
		param = new Object[2];
		i=0;
		param[i++] = new BigDecimal(form.getCtrSk());
		param[i++] = new Integer(form.getDateSk());
		
		sql = (String)query.query(sessionManager.getConnection(), sql,param, handler);
		
		if(sql==null || "".equals(sql)){
			return -2;
		}else{
			form.setDateFrom(sql);
		}
		
		
		sql = "select count(1) \n" +
				"from " + TABLA +"\n" +
				" where date_sk = ? \n" +
				"   and ctr_sk = ?";
		param = new Object[2];
		i=0;
		param[i++] = new Integer(form.getDateTo());
		param[i++] = new BigDecimal(form.getCtrSk());
		
		if(((Long)query.query(sessionManager.getConnection(), sql,param, handler)).intValue() >0){
			return -1;
		}
		
		i=0;
		param[i++] = new Integer(form.getDateFrom());
		param[i++] = new BigDecimal(form.getCtrSk());
		
		if(((Long)query.query(sessionManager.getConnection(), sql, param, handler)).intValue() ==0){
			return -2;
		}
		
		sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
			+ " 	from " + TABLA ;
		
		BigDecimal priceId = (BigDecimal)query.query(sessionManager.getConnection(), sql, handler);
		form.setPriceId(priceId.doubleValue());
		
		sql = " insert into "+ TABLA + "(\n"
				+ "  price_id,  \n"
				+ "  date_sk,  	\n"
				+ "  prdvar_sk,  \n"
				+ "  ptype_sk,  \n"
				+ "  qua_sk,  	\n"
				+ "  size_sk,  	\n"
				+ "  tunit_sk,  \n"
				+ "  ori_sk,  	\n"
				+ "  ctr_sk,  	\n"
				+ "  clev_sk,  	\n"
				+ "  price_copy_flag,  	\n"
				+ "  price_inf_uni,   \n"
				+ "  price_sup_uni,   \n"
				+ "  price_inf_kilo,  \n"
				+ "  price_sup_kilo,  \n"
				+ "  audit_user_ins,  \n"
				+ "  audit_date_ins   \n"
				+ ")\n"
				+ "select row_number() over(order by price_id)-1+ ?,\n "
				+ " ?, \n"
				+ "  prdvar_sk,  \n"
				+ "  ptype_sk,  \n"
				+ "  qua_sk,  	\n"
				+ "  size_sk,  	\n"
				+ "  tunit_sk,  \n"
				+ "  ori_sk,  	\n"
				+ "  ctr_sk,  	\n"
				+ "  clev_sk,  	\n"
				+ "  1,  	\n"
				+ "  price_inf_uni,   \n"
				+ "  price_sup_uni,   \n"
				+ "  price_inf_kilo,  \n"
				+ "  price_sup_kilo,  \n"
				+ "  ?,  \n"
				+ "  now() \n"
				+ "  from "+ TABLA +"\n"
				+ " where date_sk = ?"
				+ "   and ctr_sk = ? "
				;
		
		param = new Object[5];
		i = 0;
		param[i++] = new BigDecimal(form.getPriceId());
		param[i++] = new Integer(form.getDateTo()); 
		param[i++] = username;
		param[i++] = new Integer(form.getDateFrom());
		param[i++] = new BigDecimal(form.getCtrSk());
	    
		i = query.update(sessionManager.getConnection(), sql, param);
		
		
		//HISTORICAL DATA
		
		sql = "select coalesce(max(PRICEHIST_SK) + 1,1)\n"
			+ " 	from " + HIST_TABLA ;
		
		BigDecimal priceHistSk = (BigDecimal)query.query(sessionManager.getConnection(), sql, handler);
		//form.setPriceId(pricehist_Id.doubleValue());
		
		sql = " insert into "+ HIST_TABLA+ "(\n"
				+ "  pricehist_sk,  \n"
				+ "  price_id,  \n"
				+ "  date_sk,  	\n"
				+ "  prdvar_sk,  \n"
				+ "  ptype_sk,  \n"
				+ "  qua_sk,  	\n"
				+ "  size_sk,  	\n"
				+ "  tunit_sk,  \n"
				+ "  ori_sk,  	\n"
				+ "  ctr_sk,  	\n"
				+ "  clev_sk,  	\n"
				+ "  price_copy_flag,  	\n"
				+ "  price_inf_uni,   \n"
				+ "  price_sup_uni,   \n"
				+ "  price_inf_kilo,  \n"
				+ "  price_sup_kilo,  \n"
				+ "  audit_user_ins,  \n"
				+ "  audit_date_ins,   \n"
				+ "  action   \n"
				+ ")\n"
				+ "select row_number() over(order by price_id)-1+ ?,\n"
				+ "  row_number() over(order by price_id)-1+ ?, \n"
				+ "  ?, \n"
				+ "  prdvar_sk,  \n"
				+ "  ptype_sk,  \n"
				+ "  qua_sk,  	\n"
				+ "  size_sk,  	\n"
				+ "  tunit_sk,  \n"
				+ "  ori_sk,  	\n"
				+ "  ctr_sk,  	\n"
				+ "  clev_sk,  	\n"
				+ "  1,  	\n"
				+ "  price_inf_uni,   \n"
				+ "  price_sup_uni,   \n"
				+ "  price_inf_kilo,  \n"
				+ "  price_sup_kilo,  \n"
				+ "  ?,  \n"
				+ "  now(), \n"
				+ "  'insert'   \n"
				+ "  from "+ TABLA +"\n"
				+ " where date_sk = ?"
				+ "   and ctr_sk = ? ";
		
		param = new Object[6];
		i = 0;
		param[i++] = priceHistSk;
		param[i++] = new BigDecimal(form.getPriceId());
		param[i++] = new Integer(form.getDateTo()); 
		param[i++] = username;
		param[i++] = new Integer(form.getDateFrom());
		param[i++] = new BigDecimal(form.getCtrSk());
		
		query.update(sessionManager.getConnection(), sql, param);

		return i;
	}
	
	public String getPrdVarSK(PriceForm form)throws Exception, Error{
		String sql = "select prdvar_sk::varchar prdVarSk \n"
				+ "from "+ESQUEMA+".product_variety_dim \n"
				+ "where current_date between effective_date and expiry_date \n"
				+ " and prod_id = ? \n";
		
		if("".equals(form.getPrdVarSk())){
			sql+= " and var_id is null\n";
		}else{
			sql+= " and prdvar_sk = "+form.getPrdVarSk()+"\n";
		}
		
		ScalarHandler handler = new ScalarHandler();
		QueryRunner query = new QueryRunner();
		
		Object[] param = new Object[1];
		int i = 0; 
		
		param[i++] = new BigDecimal(form.getProdId());
		
		String result = (String)query.query(sessionManager.getConnection(), sql,param, handler);
		
		return result;
	}
	
	public String getPtypeSK(PriceForm form)throws Exception, Error{
		String sql="select ptype_sk::varchar "
				+" from "+ESQUEMA+".product_type_dim "
				+" where current_date between effective_date and expiry_date "
				+" and prod_id = "+form.getProdId()
				+" and ptype_id " + (StringUtils.isEmpty(form.getPtypeId())?"is null":"="+form.getPtypeId());
		
		ScalarHandler handler = new ScalarHandler();
		QueryRunner query = new QueryRunner();
		
		String result = (String)query.query(sessionManager.getConnection(), sql, handler);
		
		return result;
	}
	
	public String getTradUnit(PriceForm form)throws Exception, Error{
		String sql = new String();
		ScalarHandler handler = new ScalarHandler();
		QueryRunner query = new QueryRunner();
		
		sql="select var_id::varchar "
			+" from "+ESQUEMA+".product_variety_dim "
			+" where current_date between effective_date and expiry_date "
			+" and prdvar_sk = "+form.getPrdVarSk();
	
		String varId = (String)query.query(sessionManager.getConnection(), sql, handler);
		
		String ptypeId = form.getPtypeId();
		
		sql = "select tunit_sk::varchar prdVarSk \n"
			+ "from "+ESQUEMA+".trad_unit_dim \n"
			+ "where current_date between effective_date and expiry_date \n"
			+ " and prod_id = "+form.getProdId()+" \n";
		
		if(varId ==null || "".equals(varId)){
			sql+= " and var_id is null\n";
		}else{
			sql+= " and var_id = "+varId+"\n";
		}
		
		if(ptypeId == null || "".equals(ptypeId) || "0".equals(ptypeId)){
			sql+= " and ptype_id is null\n";
		}else{
			sql+= " and ptype_id  = "+ptypeId+"\n";
		}
		
		
		sql+= " and ctr_id in (select ctr_id \n"
			+ " 				from "+ESQUEMA+".COUNTRY_DIM\n"
			+ "					where ctr_sk = "+form.getCtrSk()+"\n"
			+ "				)\n"
			+ " and size_id in (select size_id \n"
			+ "					from "+ESQUEMA+".SIZE_DIM \n"
			+ "					where current_date between effective_date and expiry_date \n"
			+ "					and size_sk = "+form.getSizeSk()+"\n"
			+ "				) \n" 
			+ " and tunit_sk in ("+form.getTunitSk()+")";

		String result = (String)query.query(sessionManager.getConnection(), sql, handler);
		return result;
	}
	
	/**
	 * 
	 * @param form
	 * @return
	 * @throws Exception
	 * @throws Error
	 */
	public PriceForm getLastRecordFrom(PriceForm form)throws Exception, Error{ 
		String sql = new String();
		QueryRunner query = new QueryRunner();
		
		sql= QUERY_SELECT 
			+ "   AND country.ctr_sk =? \n"
			+ "   AND commerce.clev_sk = ? \n"
			+ "   AND product.prdvar_sk = ?\n"
			+ "   AND ptype.ptype_sk = ? \n"
			+ "   AND size.size_sk= ? \n"
			+ "   AND quality.qua_sk=? \n"
			+ "   AND origin.ori_sk = ? \n"
			+ "   AND fecha.date_sk <= ? \n";
			//+ "  tradunit.tunit_sk = ? \n"  //Recommended posteriori;
		
		if(form.getPriceId().doubleValue()>0){
			sql+= " AND price_id <> " + String.valueOf(form.getPriceId());
		}
		
		sql+= "   order by fecha.date_sk desc, price_id desc \n"
			+ "   limit 1 \n";
		
		//country, comercializacion, producto, variedad, calidad, tamaño y origen
		
		
		Object[] param = new Object[8];
		int i = 0;
		
		String[] fechasExp = form.getDateField().split("/");
		
		form.setDateSk(new Integer(fechasExp[2]+fechasExp[1]+fechasExp[0]));
		
		BeanHandler handler = new BeanHandler(PriceForm.class);
		
		param[i++] = new BigDecimal(form.getCtrSk());
		param[i++] = new BigDecimal(form.getClevSk());
		param[i++] = new BigDecimal(form.getPrdVarSk());
		param[i++] = new BigDecimal(form.getPtypeSk());
		param[i++] = new BigDecimal(form.getSizeSk());
		param[i++] = new BigDecimal(form.getQuaSk());
		param[i++] = form.getOriSk();
		param[i++] = form.getDateSk(); 
		//param[i++] = new BigDecimal(form.getTunitSk());

		PriceForm result = null;
		
		result = (PriceForm)query.query(sessionManager.getConnection(), sql, param, handler);
		return result;
	}
	
	public boolean isDuplicated(PriceForm form)throws Exception, Error{ 
		String sql = new String();
		
		sql= QUERY_SELECT 
			+ "   AND country.ctr_sk =? \n"
			+ "   AND commerce.clev_sk = ? \n"
			+ "   AND product.prdvar_sk = ?\n"
			+ "   AND ptype.ptype_sk = ?\n"
			+ "   AND size.size_sk= ? \n"
			+ "   AND quality.qua_sk=? \n"
			+ "   AND origin.ori_sk = ? \n"
			+ "   AND fecha.date_sk = ? \n"
			+ "   AND tradunit.tunit_sk = ? \n";
		if(form.getPriceId()>0){
			sql+= " AND price_id <> "+String.valueOf(form.getPriceId().intValue())+"\n";
		}
		sql+= "   order by fecha.date_sk desc, price_id desc \n"
			+ "   limit 1 \n";
		
		//country, comercializacion, producto, variedad, calidad, tamaño y origen
		
		
		Object[] param = new Object[9];
		int i = 0;
		
		String[] fechasExp = form.getDateField().split("/");
		
		form.setDateSk(new Integer(fechasExp[2]+fechasExp[1]+fechasExp[0]));
		
		BeanHandler handler = new BeanHandler(PriceForm.class);
		QueryRunner query = new QueryRunner();
		
		param[i++] = new BigDecimal(form.getCtrSk());
		param[i++] = new BigDecimal(form.getClevSk());
		param[i++] = new BigDecimal(form.getPrdVarSk());
		param[i++] = new BigDecimal(form.getPtypeSk());
		param[i++] = new BigDecimal(form.getSizeSk());
		param[i++] = new BigDecimal(form.getQuaSk());
		param[i++] = form.getOriSk();
		param[i++] = form.getDateSk(); 
		param[i++] = new BigDecimal(form.getTunitSk());

		PriceForm tmpForm = null;
		
		tmpForm = (PriceForm)query.query(sessionManager.getConnection(), sql, param, handler);
		
		boolean result = false;
		
		if(tmpForm==null){
			result = false;
		}else{
			result = true;
		}
		
		return result;
	}
}
