package org.promefrut.simefrut.struts.maintenances.beans;

import java.math.BigDecimal;
import java.sql.SQLException;
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
import org.promefrut.simefrut.struts.maintenances.forms.ProductionForm;


/**
 * @author HWM
 *
 */
public class ProductionBean extends BaseDAO {
	public final String TABLA = super.ESQUEMA_TABLA + "PRODUCTION_FACT";
	protected static final String PREFIJO = "production";
	//private final String HIST_PREFIJO = PREFIJO;
	public final String HIST_TABLA = super.ESQUEMA_TABLA + "PRODUCTION_FACT_HIST";
	private final String QUERY_SELECT = "SELECT \n"
			+ "  production.production_id productionId, \n"
			+ "  production.prod_sk prodSk, \n"
			+ "  production.year_sk yearSk, \n"
			+ "  production.ctr_sk ctrSk, \n"
			+ "  production.production_vol volProd, \n"
			+ "  production.production_cost costProd, \n"
			+ "  production.maintenance_cost maintenanceCost, \n"
			+ "  production.establishment_cost establishmentCost, \n"
			+ "  production.production_system productionSystem, \n"
			+ "		CASE WHEN production.production_system = 1 THEN '"
			+					 bundle.getString("production.productionSystem.low") 
			+ 			"' WHEN production.production_system = 2 THEN '"
			+					 bundle.getString("production.productionSystem.intermediate")
			+ 			"' WHEN production.production_system = 3 THEN '"
			+					 bundle.getString("production.productionSystem.high")
			+			"' END productionSystemText, \n"
			+ "  production.harvested_area harvestedArea, \n"
			+ "  product.prod_desc prodDesc, \n"
			+ "  product.prod_code prodCode, \n"
			+ "  country.ctr_desc ctrDescSpa, \n"
			+ "  country.ctr_desc_eng ctrDescEng,\n"
			+ "  production.january jan, \n"
			+ "  production.february feb, \n"
			+ "  production.march mar, \n"
			+ "  production.april apr, \n"
			+ "  production.may may, \n"
			+ "  production.june jun, \n"
			+ "  production.july jul, \n"
			+ "  production.august aug, \n"
			+ "  production.september sep, \n"
			+ "  production.october oct, \n"
			+ "  production.november nov, \n"
			+ "  production.december \"dec\" \n"
			+ "FROM \n"
			+ "  public.production_fact production, \n"
			+ "  public.product_dim product, \n"
			+ "  public.country_dim country\n"
			+ "WHERE \n"
			+ "  product.prod_sk = production.prod_sk AND\n"
			+ "  country.ctr_sk = production.ctr_sk \n";

	public ProductionBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find(BigDecimal ctrSk, Integer yearSk) throws SQLException, Exception, Error {
		String sql = new String();
		
		sql= QUERY_SELECT 
				+ "  AND production.year_sk = ? \n";
		
		//If the number is less than 0 it means that the current user can see data from other countries
		if(ctrSk.intValue()>0){
			sql += "	AND production.ctr_sk = ? \n";
		}
		
		sql += "ORDER BY \n"
				+ "  year_sk DESC \n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		
		Object[] param = null; 
		
		if(ctrSk.intValue()>0){
			param = new Object[2];
		}else{
			param = new Object[1];
		}
		
		
		int i = 0;
		param[i++] = yearSk;
		
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

	public int insert(ProductionForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
					+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		BigDecimal productionId = (BigDecimal)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into "+ TABLA + "(\n"
				+ " production_id,	\n"
				+ " year_sk,	\n"
				+ " prod_sk,	\n"
				+ " ctr_sk,	\n"
				+ " production_vol,	\n"
				+ " production_cost, \n"
				+ " maintenance_cost, \n"
				+ " establishment_cost, \n"
				+ " harvested_area,	\n"
				+ " production_system, \n"
				+ "  january , \n"
				+ "  february, \n"
				+ "  march , \n"
				+ "  april , \n"
				+ "  may , \n"
				+ "  june, \n"
				+ "  july, \n"
				+ "  august , \n"
				+ "  september , \n"
				+ "  october , \n"
				+ "  november, \n"
				+ "  december, \n"
				+ "  audit_user_ins,  \n"
				+ "  audit_date_ins   \n"
				+ ")\n"
				+ "values (" 
				+ "?,?,?,?,";
		
		int aditional = 0;
		
		if(StringUtils.isEmpty(form.getVolProd())){
			sql+="null,";
		}else{
			aditional++;
			sql+="?,";
		}
		
		if(StringUtils.isEmpty(form.getCostProd())){
			sql+="null,";
		}else{
			aditional++;
			sql+="?,";
		}
		
		if(StringUtils.isEmpty(form.getMaintenanceCost())){
			sql+="null,";
		}else{
			aditional++;
			sql+="?,";
		}
		
		if(StringUtils.isEmpty(form.getEstablishmentCost())){
			sql+="null,";
		}else{
			aditional++;
			sql+="?,";
		}
		
		if(StringUtils.isEmpty(form.getHarvestedArea())){
			sql+="null,";
		}else{
			aditional++;
			sql+="?,";
		}
		
		if(StringUtils.isEmpty(form.getProductionSystem())){
			sql+="null,";
		}else{
			aditional++;
			sql+="?,";
		}
		
			sql+="?,?,?,?,?,?,?,?,?,?,?,?,?,now()"
				+ ")";
		
		this.fillYearDim(form.getYearSk());
		
		form.setProductionId(productionId.doubleValue());
		
		Object[] param = new Object[17+aditional];
		int i = 0;
		param[i++] = new BigDecimal(form.getProductionId());
		param[i++] = form.getYearSk(); 
		param[i++] = form.getProdSk();
		param[i++] = form.getCtrSk();
		
		if(!StringUtils.isEmpty(form.getVolProd())){
			param[i++] = new BigDecimal(form.getVolProd());
		}
	    
		if(!StringUtils.isEmpty(form.getCostProd())){
			param[i++] = new BigDecimal(form.getCostProd());
		}
		
		if(!StringUtils.isEmpty(form.getMaintenanceCost())){
			param[i++] = new BigDecimal(form.getMaintenanceCost());
		}
		
		if(!StringUtils.isEmpty(form.getEstablishmentCost())){
			param[i++] = new BigDecimal(form.getEstablishmentCost());
		}
		
		if(!StringUtils.isEmpty(form.getHarvestedArea())){
			param[i++] = new BigDecimal(form.getHarvestedArea());
		}
		
		if(!StringUtils.isEmpty(form.getProductionSystem())){
			param[i++] = new Integer(form.getProductionSystem());
		}
		
	    param[i++] = form.getJan();
	    param[i++] = form.getFeb();
	    param[i++] = form.getMar();
	    param[i++] = form.getApr();
	    param[i++] = form.getMay();
	    param[i++] = form.getJun();
	    param[i++] = form.getJul();
	    param[i++] = form.getAug();
	    param[i++] = form.getSep();
	    param[i++] = form.getOct();
	    param[i++] = form.getNov();
	    param[i++] = form.getDec();
	    param[i++] = username;
	    
		i = query.update(sessionManager.getConnection(), sql, param);
		
		insertHist(form, username, "insert");
		return i;
	}

	public int update(ProductionForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ " set "
					+ " year_sk = ?,	\n"
					+ " prod_sk = ?,	\n"
					+ " ctr_sk = ?,	\n"
					+ " production_vol = ";
		int aditional = 0;
		
		if(StringUtils.isEmpty(form.getVolProd())){
			sql+="null,\n";
		}else{
			aditional++;
			sql+="?,\n";
		}
		
		sql+= " production_cost = ";
		if(StringUtils.isEmpty(form.getCostProd())){
			sql+="null,\n";
		}else{
			aditional++;
			sql+="?,\n";
		}
		
		sql+= " maintenance_cost = ";
		if(StringUtils.isEmpty(form.getMaintenanceCost())){
			sql+="null,\n";
		}else{
			aditional++;
			sql+="?,\n";
		}
		
		sql+= " establishment_cost = ";
		if(StringUtils.isEmpty(form.getEstablishmentCost())){
			sql+="null,\n";
		}else{
			aditional++;
			sql+="?,\n";
		}

		sql+= " harvested_area = ";
		if(StringUtils.isEmpty(form.getHarvestedArea())){
			sql+="null,\n";
		}else{
			aditional++;
			sql+="?,\n";
		}
		
		sql+= " production_system = ";
		if(StringUtils.isEmpty(form.getProductionSystem())){
			sql+="null,\n";
		}else{
			aditional++;
			sql+="?,\n";
		}
		
		sql+= 		"  january  = ?, \n"
					+ "  february = ?, \n"
					+ "  march  = ?, \n"
					+ "  april  = ?, \n"
					+ "  may  = ?, \n"
					+ "  june = ?, \n"
					+ "  july = ?, \n"
					+ "  august  = ?, \n"
					+ "  september  = ?, \n"
					+ "  october  = ?, \n"
					+ "  november = ?, \n"
					+ "  december = ?,\n"
					+ "  audit_user_upd = ?, \n"
					+ "  audit_date_upd = now() \n"
					+ " where production_ID = ? ";

		QueryRunner query = new QueryRunner();
		
		this.fillYearDim(form.getYearSk());
		
		Object[] param = new Object[17+aditional];
		int i = 0;
		param[i++] = form.getYearSk(); 
		param[i++] = form.getProdSk();
		param[i++] = form.getCtrSk();
		
		if(!StringUtils.isEmpty(form.getVolProd())){
			param[i++] = new BigDecimal(form.getVolProd());
		}
	    
		if(!StringUtils.isEmpty(form.getCostProd())){
			param[i++] = new BigDecimal(form.getCostProd());
		}
		
		if(!StringUtils.isEmpty(form.getMaintenanceCost())){
			param[i++] = new BigDecimal(form.getMaintenanceCost());
		}
		
		if(!StringUtils.isEmpty(form.getEstablishmentCost())){
			param[i++] = new BigDecimal(form.getEstablishmentCost());
		}
		
		if(!StringUtils.isEmpty(form.getHarvestedArea())){
			param[i++] = new BigDecimal(form.getHarvestedArea());
		}
		
		if(!StringUtils.isEmpty(form.getProductionSystem())){
			param[i++] = new Integer(form.getProductionSystem());
		}

		param[i++] = form.getJan();
	    param[i++] = form.getFeb();
	    param[i++] = form.getMar();
	    param[i++] = form.getApr();
	    param[i++] = form.getMay();
	    param[i++] = form.getJun();
	    param[i++] = form.getJul();
	    param[i++] = form.getAug();
	    param[i++] = form.getSep();
	    param[i++] = form.getOct();
	    param[i++] = form.getNov();
	    param[i++] = form.getDec();
	    param[i++] = username;
	    param[i++] = new BigDecimal(form.getProductionId());

		i = query.update(sessionManager.getConnection(), sql, param);
		
		insertHist(form,username,"update");
		return i;
	}

	public int delete(ProductionForm form, String username) throws Exception, Error {
		String sql = new String();
		
		sql = QUERY_SELECT
			+ "  AND production.production_id = ? \n";
		
		QueryRunner query = new QueryRunner();
		BeanHandler handler = new BeanHandler(ProductionForm.class);
		Object[] param = new Object[1];
		param[0] = form.getProductionId();
		
		form = (ProductionForm)query.query(sessionManager.getConnection(), sql, param, handler);
		
		sql = "DELETE FROM " + TABLA
				+ " WHERE "+ PREFIJO +"_ID = ? ";
		
		insertHist(form, username, "delete");
		return query.update(sessionManager.getConnection(), sql, param);
	}
	
	/**
	 * Function for Data Warehouse historical. This function inserts new registers as historical data, represanting the changes
	 * that has been made on the principal table.
	 * @param {org.promefrut.simefrut.struts.maintenances.forms.ProductionForm}form 
	 * @param {String} username
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	private void insertHist(ProductionForm form, String username, String accion) throws SQLException, Exception, Error {
		String sql = new String();
		QueryRunner query = new QueryRunner();
	
		sql = " insert into "+HIST_TABLA+"(\n"
			+ "  prodhist_sk,  \n"
			+ " production_id,	\n"
			+ " year_sk,	\n"
			+ " prod_sk,	\n"
			+ " ctr_sk,	\n"
			+ " production_vol,	\n"
			+ " production_cost,\n"
			+ " maintenance_cost, \n"
			+ " establishment_cost, \n"
			+ " harvested_area,	\n"
			+ " production_system, \n"
			+ "  january , \n"
			+ "  february, \n"
			+ "  march , \n"
			+ "  april , \n"
			+ "  may , \n"
			+ "  june, \n"
			+ "  july, \n"
			+ "  august , \n"
			+ "  september , \n"
			+ "  october , \n"
			+ "  november, \n"
			+ "  december, \n"
			+ "  audit_user_ins,  \n"
			+ "  audit_date_ins,   \n"
			+ "  action \n"
			+ ")\n"
			+ "values ((select coalesce(max(prodHIST_SK) + 1,1)\n"
			+ "         from " + HIST_TABLA +"),"
			+ "?,?,?,?,";
		int aditional = 0;
		
		if(StringUtils.isEmpty(form.getVolProd())){
			sql+="null,";
		}else{
			aditional++;
			sql+="?,";
		}
		
		if(StringUtils.isEmpty(form.getCostProd())){
			sql+="null,";
		}else{
			aditional++;
			sql+="?,";
		}
		
		if(StringUtils.isEmpty(form.getMaintenanceCost())){
			sql+="null,";
		}else{
			aditional++;
			sql+="?,";
		}
		
		if(StringUtils.isEmpty(form.getEstablishmentCost())){
			sql+="null,";
		}else{
			aditional++;
			sql+="?,";
		}
		
		if(StringUtils.isEmpty(form.getHarvestedArea())){
			sql+="null,";
		}else{
			aditional++;
			sql+="?,";
		}
		
		if(StringUtils.isEmpty(form.getProductionSystem())){
			sql+="null,";
		}else{
			aditional++;
			sql+="?,";
		}
		
		sql+="?,?,?,?,?,?,?,?,?,?,?,?,?,now(),? "
			+ ")";

		Object[] param = new Object[18+aditional];
		int i = 0;
		
		param[i++] = new BigDecimal(form.getProductionId());
		param[i++] = form.getYearSk(); 
		param[i++] = form.getProdSk();
		param[i++] = form.getCtrSk();
		
		if(!StringUtils.isEmpty(form.getVolProd())){
			param[i++] = new BigDecimal(form.getVolProd());
		}
	    
		if(!StringUtils.isEmpty(form.getCostProd())){
			param[i++] = new BigDecimal(form.getCostProd());
		}
		
		if(!StringUtils.isEmpty(form.getMaintenanceCost())){
			param[i++] = new BigDecimal(form.getMaintenanceCost());
		}
		
		if(!StringUtils.isEmpty(form.getEstablishmentCost())){
			param[i++] = new BigDecimal(form.getEstablishmentCost());
		}
		
		if(!StringUtils.isEmpty(form.getHarvestedArea())){
			param[i++] = new BigDecimal(form.getHarvestedArea());
		}
		
		if(!StringUtils.isEmpty(form.getProductionSystem())){
			param[i++] = new Integer(form.getProductionSystem());
		}
		
	    param[i++] = form.getJan();
	    param[i++] = form.getFeb();
	    param[i++] = form.getMar();
	    param[i++] = form.getApr();
	    param[i++] = form.getMay();
	    param[i++] = form.getJun();
	    param[i++] = form.getJul();
	    param[i++] = form.getAug();
	    param[i++] = form.getSep();
	    param[i++] = form.getOct();
	    param[i++] = form.getNov();
	    param[i++] = form.getDec();
	    param[i++] = username;
	    param[i++] = accion;

		query.update(sessionManager.getConnection(), sql, param);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductsCollection() throws Exception, Error {
		String sql = "select distinct\n"
			+ "prod_sk prodSk,\n"
			+ "prod_id prodId,\n"
			+ "prod_desc prodDesc\n"
			+ "from "+ ESQUEMA +".product_dim\n"
			+ "where current_date between effective_date and expiry_date\n";
		
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
	
	
	private int fillYearDim(Integer yearField)throws Exception, Error{
		String sql = "select count(1) " +
				"from "+ESQUEMA+".YEAR_DIM " +
				"where year_sk = "+ String.valueOf(yearField.intValue());
		
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		
		Long tmp = (Long)query.query(sessionManager.getConnection(), sql, handler);
		int reg = 0;
		if(tmp==0){
			sql = "insert into "+ESQUEMA+".YEAR_DIM (\n" +
					"year_sk,\n" +
					"audit_date_ins\n" +
					")values(\n" +
					String.valueOf(yearField.intValue()) +","+
					"now()\n" +
					")\n";
			reg = query.update(sessionManager.getConnection(), sql);
		}
		return reg;
	}
	
	
	//Validation Funtions
	public boolean isDuplicated(ProductionForm form)throws Exception, Error{ 
		String sql = new String();
		
		sql= QUERY_SELECT 
			+ "   AND production.ctr_sk =? \n"
			+ "   AND production.prod_sk = ?\n"
			+ "   AND production.year_sk = ? \n";
		if(form.getProductionId()>0){
			sql+= " AND production.production_id <> "+String.valueOf(form.getProductionId().intValue())+"\n";
		}
		sql+= "   order by production.year_sk desc, production_id desc \n";
		
		BeanHandler handler = new BeanHandler(ProductionForm.class);
		QueryRunner query = new QueryRunner();
		
		Object[] param = new Object[3];
		int i = 0;
		
		param[i++] = form.getCtrSk();
		param[i++] = form.getProdSk();
		param[i++] = form.getYearSk();
		
		ProductionForm tmpForm = null;
		
		tmpForm = (ProductionForm)query.query(sessionManager.getConnection(), sql, param, handler);
		
		boolean result = false;
		
		if(tmpForm==null){
			result = false;
		}else{
			result = true;
		}
		
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getOriContriesCollection() throws Exception, Error {
		String sql = "select \n"
			+ "ctr_sk ctrSk,\n"
			+ "ctr_id ctrId, \n"
			+ "ctr_desc ctrDescSpa,\n"
			+ "ctr_desc_eng ctrDescEng\n"
			+ "from "+ ESQUEMA +".country_dim\n"
			+ "where current_date between effective_date and expiry_date\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	public ProductionForm getLastRecordFrom(ProductionForm form)throws Exception, Error{ 
		String sql = new String();
		
		sql= QUERY_SELECT 
			+ "   AND production.ctr_sk =? \n"
			+ "   AND production.year_sk < ? \n"
			+ "   AND production.prod_sk = ?\n";
		
		if(form.getProductionId().doubleValue()>0){
			sql+= " AND production_id <> " + String.valueOf(form.getProductionId());
		}
		
		sql+= "   order by production.year_sk desc, production_id desc \n"
			+ "   limit 1 \n";
		
		Object[] param = new Object[3];
		int i = 0;
		
		BeanHandler handler = new BeanHandler(ProductionForm.class);
		QueryRunner query = new QueryRunner();
		
		param[i++] = form.getCtrSk();
		param[i++] = form.getYearSk();
		param[i++] = form.getProdSk();
		
		ProductionForm result = null;
		
		result = (ProductionForm)query.query(sessionManager.getConnection(), sql, param, handler);
		return result;
	}
}
