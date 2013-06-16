package org.promefrut.simefrut.struts.maintenances.beans;

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
import org.promefrut.simefrut.struts.maintenances.forms.PriceDateExceptionForm;


/**
 * @author HWM
 *
 */
public class PriceDateExceptionBean extends BaseDAO {
	protected final String TABLA = super.ESQUEMA_TABLA + "PRICE_DATE_EXCEPTION";
	protected static final String PREFIJO = "pde";
	
	private final String QUERY_SELECT = "SELECT \n"
			+ "  pde.pde_id pdeId, \n"
			+ "  pde.date_field dateField, \n"
			+ "  pde.ctr_id ctrId, \n"
			+ "  pde.prod_id prodId, \n"
			+ "  pde.ptype_id ptypeId, \n"
			+ "  pde.var_id varId, \n"
			+ "  pde.pde_Comment pdeComment, \n"
			+ "  product.prod_desc prodDesc, \n"
			+ "  product.prod_code prodCode, \n"
			+ "  var.var_desc varDesc, \n"
			+ "  ptype.ptype_desc ptypeDesc, \n"
			+ "  country.ctr_desc ctrDescSpa, \n"
			+ "  country.ctr_desc_eng ctrDescEng,\n"
			+ "  CASE WHEN pde.audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
			+ "	 coalesce(pde.audit_user_upd, pde.audit_user_ins) audit_user, \n" 
			+ "	 coalesce(pde.audit_date_upd, pde.audit_date_ins) audit_date \n"
			+ "FROM \n"
			+ "  "+super.ESQUEMA_TABLA+"Price_Date_Exception pde " 
			+ "		left outer join "+super.ESQUEMA_TABLA+"products_Ctg product \n"
			+ " 		on product.prod_id = pde.prod_id \n"
			+ "		left outer join "+super.ESQUEMA_TABLA+"varieties_ctg var \n"
			+ " 		on var.var_id = pde.var_id \n"
			+ "  	left outer join "+super.ESQUEMA_TABLA+"product_type_ctg ptype \n"
			+ "			on ptype.ptype_id = pde.ptype_id, \n"
			+ "  "+super.ESQUEMA_TABLA+"countries_ctg country\n"
			+ "WHERE \n"
			+ "  pde.ctr_id = country.ctr_id \n";

	public PriceDateExceptionBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find(String ctrId) throws SQLException, Exception, Error {
		String sql = new String();
		
		sql= QUERY_SELECT; 
				//+ "  AND pde.date_field = to_date(?,'dd/mm/yyyy') \n";
		
		//If the number is less than 0 it means that the current user can see data from other countries
		if((new Integer(ctrId)).intValue()>0){
			sql += "	AND pde.ctr_id = "+ctrId+" \n";
		}
		
		sql += "ORDER BY \n"
				+ "  date_field DESC \n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		
		List<Map> resultList = null;
		
		resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		//String resultJSON = "Ext.data.JsonP.callback1("+data+")";
		return data;
	}

	public int insert(PriceDateExceptionForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
					+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		Integer pdeId = (Integer)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into "+ TABLA + "(\n"
				+ " pde_id,	\n"
				+ " ctr_id,	\n"
				+ " date_field, \n";
		
		if(form.getProdId()>0){
			sql+= "PROD_ID,\n";
		}

		if(form.getVarId()>0){
			sql+= "VAR_ID,\n";
		}
		
		if(form.getPtypeId()>0){
			sql+= "PTYPE_ID,\n";
		}

		sql+= " pde_comment, \n"
				+ " audit_user_ins, \n"
				+ " audit_date_ins \n"
				+ ")\n"
				+ "values (" 
				+ "?,?, to_date(?,'dd/mm/yyyy'),";
		
		if(form.getProdId()>0){
			sql+= String.valueOf(form.getProdId())+",";
		}

		if(form.getVarId()>0){
			sql+= String.valueOf(form.getVarId())+",";
		}
		if(form.getPtypeId()>0){
			sql+= String.valueOf(form.getPtypeId())+",";
		}		
		
		sql+="?,?,now()"
			+ ")";
		
		form.setPdeId(pdeId.intValue());
		
		Object[] param = new Object[5];
		int i = 0;
		param[i++] = form.getPdeId();
		param[i++] = form.getCtrId();
		param[i++] = form.getDateField(); 
		param[i++] = form.getPdeComment();
	    param[i++] = username;
	    
		i = query.update(sessionManager.getConnection(), sql, param);
		
		return i;
	}

	public int update(PriceDateExceptionForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ " set "
					+ " ctr_id =?,	\n"
					+ " date_field = to_date(?,'dd/mm/yyyy'), \n";
			
			if(form.getProdId()>0){
				sql+= "PROD_ID= "+String.valueOf(form.getProdId())+",\n";
			}else{
				sql+= "PROD_ID= null,\n";
			}

			if(form.getVarId()>0){
				sql+= "VAR_ID = "+String.valueOf(form.getVarId())+",\n";
			}else{
				sql+= "VAR_ID = null,\n";
			}
			
			if(form.getPtypeId()>0){
				sql+= "PTYPE_ID = "+String.valueOf(form.getPtypeId())+",\n";
			}else{
				sql+= "PTYPE_ID = null,\n";
			}

			sql+= " pde_comment = ?, \n"
					+ "  audit_user_upd = ?, \n"
					+ "  audit_date_upd = now() \n"
					+ " where "+PREFIJO+"_ID = ? ";

		QueryRunner query = new QueryRunner();
		
		Object[] param = new Object[5];
		int i = 0;
		
		param[i++] = form.getCtrId();
		param[i++] = form.getDateField(); 
		param[i++] = form.getPdeComment();
	    param[i++] = username;
	    param[i++] = form.getPdeId();

		i = query.update(sessionManager.getConnection(), sql, param);
		
		return i;
	}

	public int delete(PriceDateExceptionForm form, String username) throws Exception, Error {
		String sql = new String();
		
		sql = QUERY_SELECT
			+ "  AND pde."+PREFIJO+"_id = ? \n";
		
		if(form.getVarId().intValue() !=0 ){
			sql+= " and var_id = "+String.valueOf(form.getVarId())+"\n";
		}
		
		QueryRunner query = new QueryRunner();
		BeanHandler handler = new BeanHandler(PriceDateExceptionForm.class);
		Object[] param = new Object[1];
		param[0] = form.getPdeId();
		
		form = (PriceDateExceptionForm)query.query(sessionManager.getConnection(), sql, param, handler);
		
		sql = "DELETE FROM " + TABLA
				+ " WHERE "+ PREFIJO +"_ID = ? ";
		
		return query.update(sessionManager.getConnection(), sql, param);
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
	
	//Validation Funtions
	public boolean isDuplicated(PriceDateExceptionForm form)throws Exception, Error{ 
		String sql = new String();
		
		sql= QUERY_SELECT 
			+ "   AND pde.ctr_id =? \n"
			+ "   AND pde.date_field = to_date(?,'dd/mm/yyyy') \n";

		if(form.getProdId()>0){
			sql+= "AND pde.PROD_ID= "+String.valueOf(form.getProdId())+"\n";
		}

		if(form.getVarId()>0){
			sql+= "AND pde.VAR_ID = "+String.valueOf(form.getVarId())+"\n";
		}
		
		if(form.getPtypeId()>0){
			sql+= "AND pde.PTYPE_ID = "+String.valueOf(form.getPtypeId())+"\n";
		}

		if(form.getPdeId()>0){
			sql+= " AND pde."+PREFIJO+"_id <> "+String.valueOf(form.getPdeId().intValue())+"\n";
		}
		sql+= "   order by pde.date_field desc, pde_id desc \n";
		
		BeanHandler handler = new BeanHandler(PriceDateExceptionForm.class);
		QueryRunner query = new QueryRunner();
		
		Object[] param = new Object[2];
		int i = 0;
		
		param[i++] = form.getCtrId();
		param[i++] = form.getDateField();
		
		PriceDateExceptionForm tmpForm = null;
		
		tmpForm = (PriceDateExceptionForm)query.query(sessionManager.getConnection(), sql, param, handler);
		
		boolean result = false;
		
		if(tmpForm==null){
			result = false;
		}else{
			result = true;
		}
		
		return result;
	}
	
	public String isVarIdAssociatedToProdId(PriceDateExceptionForm form)throws Exception, Error{
		String sql = "select var_id::varchar varId \n"
				+ "from "+ESQUEMA+".varieties_ctg \n"
				+ "where var_status='A' \n"
				+ " and prod_id = ? \n";
		
		ScalarHandler handler = new ScalarHandler();
		QueryRunner query = new QueryRunner();
		
		Object[] param = new Object[1];
		int i = 0; 
		
		param[i++] = form.getProdId();
		
		String result = (String)query.query(sessionManager.getConnection(), sql,param, handler);
		
		return result;
	}
	
	public String isPtypeIdAssociatedToProdId(PriceDateExceptionForm form)throws Exception, Error{
		String sql = "select ptype_id::varchar varId \n"
				+ "from "+ESQUEMA+".product_type_ctg \n"
				+ "where ptype_status='A' \n"
				+ " and prod_id = ? \n";
		
		ScalarHandler handler = new ScalarHandler();
		QueryRunner query = new QueryRunner();
		
		Object[] param = new Object[1];
		int i = 0; 
		
		param[i++] = form.getProdId();
		
		String result = (String)query.query(sessionManager.getConnection(), sql,param, handler);
		
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductsCollection() throws Exception, Error {
		String sql = "select distinct\n"
			+ "prod_id prodId,\n"
			+ "prod_desc prodDesc\n"
			+ "from "+ ESQUEMA +".products_ctg\n"
			+ "where prod_status = 'A'\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getVarietiesCollection(String prodId) throws Exception, Error {
		String sql = "select distinct\n"
			+ "var_id varId,\n"
			+ "var_desc varDesc\n"
			+ "from "+ ESQUEMA +".varieties_ctg\n"
			+ "where var_status = 'A'\n"
			+ "  and prod_id = "+prodId;
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductTypesCollection(String prodId) throws Exception, Error {
		String sql = "select distinct\n"
			+ "ptype_id ptypeId,\n"
			+ "ptype_desc ptypeDesc\n"
			+ "from "+ ESQUEMA +".product_type_ctg\n"
			+ "where ptype_status = 'A'\n"
			+ "  and prod_id = "+prodId;
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
}
