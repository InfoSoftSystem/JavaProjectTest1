package org.promefrut.simefrut.struts.catalogs.beans;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.catalogs.forms.ProductFAOSTATForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class ProductFAOSTATBean extends BaseDAO {
	protected final String TABLA = super.ESQUEMA_TABLA + "PRODUCT_FAOSTAT_DIM";
	protected static final String PREFIJO = "prod";
	
	public ProductFAOSTATBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find() throws SQLException, Exception, Error {
		String sql = "select faoprod_sk faoprodSk, \n"
				+ "		a.PROD_SK prodSk,\n"
				+ "		b.PROD_desc prodDesc,\n"
				+ "		a.GRP_ID grpId,\n"
				+ "		c.grp_desc grpDesc,\n"
				+ "		a.PROD_CODE faoprodCode,\n"
				+ "		a.PROD_desc faoprodDescSpa,\n"
				+ "		a.PROD_desc_eng faoprodDescEng,\n"
				+ "		CASE WHEN a.audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
				+ "		coalesce(a.audit_user_upd, a.audit_user_ins) audit_user, \n" 
				+ "		coalesce(a.audit_date_upd, a.audit_date_ins) audit_date\n"
				+ " from " + TABLA+ " a left outer join " + ESQUEMA+".PRODUCT_DIM B \n" +
						"ON a.prod_sk = b.prod_sk \n"
				+ " 	inner join "+ESQUEMA+".GROUP_PRODUCT_FAO_DIM c \n" +
						"ON a.grp_id = c.grp_id"
				+ " where current_date between a.effective_date and a.expiry_date";

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		return data;
	}

	/**
	 * Function for Data Warehouse updating. This function changes the expiry_date and inserts new registers
	 * @param {org.promefrut.simefrut.struts.catalogs.forms.ProductFAOSTATForm}form 
	 * @param {String} username
	 * @param {String} accion
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	public void updateDW(ProductFAOSTATForm form, String username, String accion) throws SQLException, Exception, Error {
		String sql = null;
		QueryRunner query = new QueryRunner();
		Object[] param = new Object[5];
		int i = 0;
		
		if(!StringUtils.isBlank(form.getFaoprodSk())){
			BigDecimal fieldSk = new BigDecimal(form.getFaoprodSk());
			
			sql = " update " + TABLA +" \n"
					+ " set EXPIRY_DATE = current_date-1, \n"
					+ "    AUDIT_USER_UPD = ?, \n"
					+ "    AUDIT_DATE_UPD = now()\n"
					+ " where FAOPROD_SK = ? ";
			
			param = new Object[2];
			i = 0;
			param[i++] = username;
			param[i++] = fieldSk;
			
			query.update(sessionManager.getConnection(), sql, param);
		}
		
		if("A".equals(accion)){
			
			sql = "select coalesce(max(FAOPROD_SK) + 1,1)\n"
				+ "         from "+TABLA;
			
			BigDecimal newFaoProdSk = (BigDecimal) query.query(sessionManager.getConnection(), sql, new ScalarHandler());
			
			
			sql = " insert into "+TABLA+"(\n"
				+ "	FAOPROD_SK ,\n"
				+ "	PROD_SK ,\n"
				+ "	GRP_ID ,\n"
				+ "	GRP_DESC ,\n"
				+ "	PROD_CODE ,\n"
				+ "	PROD_desc ,\n"
				+ "	PROD_desc_eng ,\n"
				+ " EFFECTIVE_DATE,\n"
				+ " EXPIRY_DATE, \n"
				+ " AUDIT_USER_INS, \n"
				+ " AUDIT_DATE_INS \n"
				+ ")\n"
				+ "values (?,"; 
			
			if(!StringUtils.isBlank(form.getProdSk()) && !"0".equals(form.getProdSk())){
				sql+= String.valueOf(form.getProdSk())+",";
			}else{
				sql+= "null,";
			}
			
			sql+= "?,(select grp_desc \n" +
				"	from "+ESQUEMA+".GROUP_PRODUCT_FAO_DIM \n" 
				+ " where grp_id = ?),\n"
				+ "?,?,?,\n"
				+ " CURRENT_DATE,to_date('31-12-9999','dd/mm/yyyy'),?, now() " //current_date+current_Time
				+ ")";
	
			param = new Object[7];
			i = 0;
			param[i++] = newFaoProdSk;
			param[i++] = new BigDecimal(form.getGrpId());
			param[i++] = new BigDecimal(form.getGrpId());
			param[i++] = form.getFaoprodCode();
			param[i++] = form.getFaoprodDescSpa();
			param[i++] = form.getFaoprodDescEng();
		    param[i++] = username;
	
			query.update(sessionManager.getConnection(), sql, param);
		}
	}
	
	public String existsData(ProductFAOSTATForm form)throws Exception, Error{
		QueryRunner query = new QueryRunner();
		
		String sql = "select PROD_CODE||' - '||PROD_desc " +
				" from " + TABLA +" \n" +
				" where trim(lower(PROD_CODE)) = '"+ form.getFaoprodCode().trim().toLowerCase() +"'"+
				"   AND current_date between effective_date and expiry_date";
		
		if(!StringUtils.isBlank(form.getFaoprodSk())){
			sql+="	AND FAOPROD_SK <> " + form.getFaoprodSk();
		}
		
		String reg = (String)query.query(sessionManager.getConnection(), sql, new ScalarHandler());
		
		return reg;
	}
	
	public String existsProductSIMEFRUTData(ProductFAOSTATForm form)throws Exception, Error{
		QueryRunner query = new QueryRunner();
		
		String sql = "select PROD_CODE||' - '||PROD_desc " +
				" from " + TABLA +" \n" +
				" where PROD_SK = "+ form.getProdSk() +
				"   AND current_date between effective_date and expiry_date";
		
		if(!StringUtils.isBlank(form.getFaoprodSk())){
			sql+="	AND FAOPROD_SK <> " + form.getFaoprodSk();
		}
		
		String reg = (String)query.query(sessionManager.getConnection(), sql, new ScalarHandler());
		
		return reg;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductsCollection() throws Exception, Error {
		String sql = "select distinct\n"
			+ "prod_sk prodSk,\n"
			+ "prod_desc prodDesc\n"
			+ "from "+ ESQUEMA +".product_dim\n"
			+ "where current_date between effective_date and expiry_date\n"
			+ " union " 
			+ " select null, null";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getGroupProductsCollection() throws Exception, Error {
		String sql = "select distinct\n"
			+ "grp_id grpId,\n"
			+ "grp_desc grpDesc\n"
			+ "from "+ ESQUEMA +".GROUP_PRODUCT_FAO_DIM\n"
			+ "where current_date between effective_date and expiry_date\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
}
