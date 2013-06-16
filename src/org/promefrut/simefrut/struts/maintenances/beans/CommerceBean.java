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
import org.json.JSONArray;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;
import org.promefrut.simefrut.struts.maintenances.forms.CommerceForm;


/**
 * @author HWM
 *
 */
public class CommerceBean extends BaseDAO {
	public final String TABLA = super.ESQUEMA_TABLA + "COMMERCE_FACT";
	protected static final String PREFIJO = "comm";
	//private final String HIST_PREFIJO = PREFIJO;
	public final String HIST_TABLA = super.ESQUEMA_TABLA + "COMMERCE_FACT_HIST";
	private final String QUERY_SELECT = "SELECT \n"
			+ "  commerce.comm_id commId, \n"
			+ "  commerce.prod_sk prodSk, \n"
			+ "  commerce.year_sk yearSk, \n"
			+ "  commerce.ctr_sk ctrSk, \n"
			+ "  commerce.vol_kilo volKilo, \n"
			+ "  commerce.vol_dollar volDollar, \n"
			+ "  commerce.comm_cifob commCIFOB, \n"
			+ "  commerce.orictr_sk oriCtrSk, \n"
			+ "  commerce.comm_type commType, \n"
			+ "  product.prod_desc prodDesc, \n"
			+ "  product.prod_code prodCode, \n"
			+ "  country.ctr_desc ctrDescSpa, \n"
			+ "  country.ctr_desc_eng ctrDescEng,\n"
			+ "		CASE WHEN commerce.comm_type = 'I' THEN '"
			+					 bundle.getString("comm.commType.import") 
			+			"' ELSE '"
			+					bundle.getString("comm.commType.export")
			+		"' END commTypeText\n"
			+ "FROM \n"
			+ "  public.commerce_fact commerce, \n"
			+ "  public.product_dim product, \n"
			+ "  public.country_dim country\n"
			+ "WHERE \n"
			+ "  product.prod_sk = commerce.prod_sk AND\n"
			+ "  country.ctr_sk = commerce.orictr_sk \n";

	public CommerceBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find(BigDecimal ctrSk, Integer yearSk) throws SQLException, Exception, Error {
		String sql = new String();
		
		sql= QUERY_SELECT 
				+ "  AND commerce.year_sk = ? \n";
		
		//If the number is less than 0 it means that the current user can see data from other countries
		if(ctrSk.intValue()>0){
			sql += "	AND commerce.ctr_sk = ? \n";
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

	public int insert(CommerceForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
					+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		BigDecimal commId = (BigDecimal)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into "+ TABLA + "(\n"
				+ " comm_id,	\n"
				+ " year_sk,	\n"
				+ " prod_sk,	\n"
				+ " ctr_sk,	\n"
				+ " vol_kilo,	\n"
				+ " vol_dollar,	\n"
				+ " comm_cifob,	\n"
				+ " orictr_sk,	\n"
				+ " comm_type,	\n"
				+ "  audit_user_ins,  \n"
				+ "  audit_date_ins   \n"
				+ ")\n"
				+ "values (" 
				+ "?,?,?,?,?,?,?,?,?,?,now()"
				+ ")";
		
		this.fillYearDim(form.getYearSk());
		
		form.setCommId(commId.doubleValue());
		
		Object[] param = new Object[10];
		int i = 0;
		param[i++] = new BigDecimal(form.getCommId());
		param[i++] = form.getYearSk(); 
		param[i++] = form.getProdSk();
		param[i++] = form.getCtrSk();
	    param[i++] = form.getVolKilo();
	    param[i++] = form.getVolDollar();
	    param[i++] = form.getCommCIFOB();
	    param[i++] = form.getOriCtrSk();
	    param[i++] = form.getCommType();
	    param[i++] = username;
	    
		i = query.update(sessionManager.getConnection(), sql, param);
		
		insertHist(form, username, "insert");
		return i;
	}

	public int update(CommerceForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ " set "
					+ " year_sk =?,	\n"
					+ " prod_sk =?,	\n"
					+ " ctr_sk =?,	\n"
					+ " vol_kilo =?,	\n"
					+ " vol_dollar =?,	\n"
					+ " comm_cifob =?,	\n"
					+ " orictr_sk =?,	\n"
					+ " comm_type=?,	\n"
					+ "  audit_user_upd = ?, \n"
					+ "  audit_date_upd = now() \n"
					+ " where comm_ID = ? ";

		QueryRunner query = new QueryRunner();
		
		this.fillYearDim(form.getYearSk());
		
		Object[] param = new Object[10];
		int i = 0;
		param[i++] = form.getYearSk(); 
		param[i++] = form.getProdSk();
		param[i++] = form.getCtrSk();
	    param[i++] = form.getVolKilo();
	    param[i++] = form.getVolDollar();
	    param[i++] = form.getCommCIFOB();
	    param[i++] = form.getOriCtrSk();
	    param[i++] = form.getCommType();
	    param[i++] = username;
	    param[i++] = new BigDecimal(form.getCommId());

		i = query.update(sessionManager.getConnection(), sql, param);
		
		insertHist(form,username,"update");
		return i;
	}

	public int delete(CommerceForm form, String username) throws Exception, Error {
		String sql = new String();
		
		sql = QUERY_SELECT
			+ "  AND commerce.comm_id = ? \n";
		
		QueryRunner query = new QueryRunner();
		BeanHandler handler = new BeanHandler(CommerceForm.class);
		Object[] param = new Object[1];
		param[0] = form.getCommId();
		
		form = (CommerceForm)query.query(sessionManager.getConnection(), sql, param, handler);
		
		sql = "DELETE FROM " + TABLA
				+ " WHERE "+ PREFIJO +"_ID = ? ";
		
		insertHist(form, username, "delete");
		return query.update(sessionManager.getConnection(), sql, param);
	}
	
	/**
	 * Function for Data Warehouse historical. This function inserts new registers as historical data, represanting the changes
	 * that has been made on the principal table.
	 * @param {org.promefrut.simefrut.struts.maintenances.forms.CommerceForm}form 
	 * @param {String} username
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	private void insertHist(CommerceForm form, String username, String accion) throws SQLException, Exception, Error {
		String sql = new String();
		QueryRunner query = new QueryRunner();
	
		sql = " insert into "+HIST_TABLA+"(\n"
			+ "  COMMhist_sk,  \n"
			+ " comm_id,	\n"
			+ " year_sk,	\n"
			+ " prod_sk,	\n"
			+ " ctr_sk,	\n"
			+ " vol_kilo,	\n"
			+ " vol_dollar,	\n"
			+ " comm_cifob,	\n"
			+ " orictr_sk,	\n"
			+ " comm_type,	\n"
			+ "  audit_user_ins,  \n"
			+ "  audit_date_ins,   \n"
			+ "  action   \n"
			+ ")\n"
			+ "values ((select coalesce(max(COMMHIST_SK) + 1,1)\n"
			+ "         from " + HIST_TABLA +"),"
			+ "?,?,?,?,?,?,?,?,?,?,now(),? "
			+ ")";

		Object[] param = new Object[11];
		int i = 0;
		
		param[i++] = new BigDecimal(form.getCommId());
		param[i++] = form.getYearSk(); 
		param[i++] = form.getProdSk();
		param[i++] = form.getCtrSk();
	    param[i++] = form.getVolKilo();
	    param[i++] = form.getVolDollar();
	    param[i++] = form.getCommCIFOB();
	    param[i++] = form.getOriCtrSk();
	    param[i++] = form.getCommType();
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
	public boolean isDuplicated(CommerceForm form)throws Exception, Error{ 
		String sql = new String();
		
		sql= QUERY_SELECT 
			+ "   AND commerce.ctr_sk =? \n"
			+ "   AND commerce.prod_sk = ?\n"
			+ "   AND commerce.year_sk = ? \n"
			+ "   AND commerce.orictr_sk = ? \n"
			+ "   AND commerce.comm_type = ? \n";
		if(form.getCommId()>0){
			sql+= " AND commerce.comm_id <> "+String.valueOf(form.getCommId().intValue())+"\n";
		}
		sql+= "   order by commerce.year_sk desc, comm_id desc \n";
		
		BeanHandler handler = new BeanHandler(CommerceForm.class);
		QueryRunner query = new QueryRunner();
		
		Object[] param = new Object[5];
		int i = 0;
		
		param[i++] = form.getCtrSk();
		param[i++] = form.getProdSk();
		param[i++] = form.getYearSk();
		param[i++] = form.getOriCtrSk();
		param[i++] = form.getCommType();
		
		CommerceForm tmpForm = null;
		
		tmpForm = (CommerceForm)query.query(sessionManager.getConnection(), sql, param, handler);
		
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
}
