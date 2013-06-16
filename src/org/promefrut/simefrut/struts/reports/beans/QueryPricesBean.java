package org.promefrut.simefrut.struts.reports.beans;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class QueryPricesBean extends BaseDAO {
	
	public QueryPricesBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductsCollection(String initialDate, String finalDate) throws Exception, Error {
		String sql = "select distinct \n"
				+ "c.prod_id prodId,\n"
				+ "trim(c.prod_desc)prodDesc\n"
				+ "from "+ ESQUEMA +".price_fact b, "
						+ ESQUEMA +".product_variety_dim c  \n"
				+ "where b.prdvar_sk = c.prdvar_sk\n"
				+ "  and b.date_sk between "+initialDate+" and "+ finalDate;
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductTypesCollection(String initialDate, String finalDate, String prodId) throws Exception, Error {
		String sql = "select distinct \n"
				+ "ptype_id ptypeId,\n" 
				+ "prod_id prodId,\n"
				+ "ptype_desc ptypeDesc\n"
				+ "from "+ ESQUEMA +".price_fact b, \n"
						+ ESQUEMA +".product_type_dim c \n"
				+ "where b.ptype_sk = c.ptype_sk \n "
				+ "  and b.date_sk between "+initialDate+" and "+ finalDate
				+ "  and c.prod_id in ("+ prodId +")"
				+ "  AND C.ptype_id IS NOT NULL";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getVarietiesCollection(String initialDate, String finalDate, String prodId, String ptypeId) throws Exception, Error {
		String sql = "select distinct \n"
				+ "var_id varId,\n" 
				+ "trim(var_desc) varDesc\n"
				+ "from "+ ESQUEMA +".price_fact b, " 
						+ ESQUEMA +".product_variety_dim c,  \n"
						+ ESQUEMA +".product_type_dim d \n"
				+ "where b.prdvar_sk = c.prdvar_sk\n"
				+ "  and b.ptype_sk = d.ptype_sk\n"
				+ "  and b.date_sk between "+initialDate+" and "+ finalDate
				+ "  and c.prod_id in ("+ prodId +")"
				+ "  and c.var_id is not null";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getQualitiesCollection(String initialDate, String finalDate, String prodId, String ptypeId, String varId) throws Exception, Error {
		String sql = "select distinct \n"
				+ "qua_id quaId,\n"
				+ "trim(qua_desc) quaDesc\n"
				+ "from "+ ESQUEMA +".price_fact b, \n" 
						+ ESQUEMA +".product_variety_dim c, \n"
						+ ESQUEMA +".quality_dim d, \n"
						+ ESQUEMA +".product_type_dim e \n "
				+ "where b.prdvar_sk = c.prdvar_sk\n"
				+ "  and b.qua_sk = d.qua_sk\n"
				+ "  and b.ptype_sk = e.ptype_sk \n"
				+ "  and b.date_sk between "+initialDate+" and "+ finalDate
				+ "  and c.prod_id in ("+ prodId +")"
				+ "  and c.var_id " + (StringUtils.isEmpty(varId)?"is null":"="+varId)
				+ "  and e.ptype_id " + (StringUtils.isEmpty(ptypeId)?"is null":"="+ptypeId);
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getSizesCollection(String initialDate, String finalDate, String prodId, String ptypeId, String varId, String quaId) throws Exception, Error {
		String sql = "select distinct \n"
				+ "size_id sizeId,\n"
				+ "trim(size_desc) sizeDesc\n"
				+ "from "+ ESQUEMA +".price_fact b, " 
						+ ESQUEMA +".product_variety_dim c, \n"
						+ ESQUEMA +".quality_dim d, \n"
						+ ESQUEMA +".product_type_dim e, \n "
						+ ESQUEMA +".size_dim f \n"
				+ "where b.prdvar_sk = c.prdvar_sk\n"
				+ "  and b.qua_sk = d.qua_sk\n"
				+ "  and b.ptype_sk = e.ptype_sk \n"
				+ "  and b.size_sk = f.size_sk\n"
				+ "  and b.date_sk between "+initialDate+" and "+ finalDate
				+ "  and c.prod_id in ("+ prodId +")"
				+ "  and c.var_id " + (StringUtils.isEmpty(varId)?"is null":"="+varId)
				+ "  and e.ptype_id " + (StringUtils.isEmpty(ptypeId)?"is null":"="+ptypeId)
				+ "  and d.qua_id in ("+quaId+")";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getYearsCollection() throws Exception, Error {
		String sql = "select distinct\n"
			+ " to_char(to_date(date_sk::varchar,'yyyymmdd'),'yyyy') yearSk\n"
			+ "from "+ ESQUEMA +".price_fact\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getCountriesCollection(String initialDate, String finalDate, String productsFilter) throws Exception, Error {
		String sql = "select distinct \n"
				+ "a.ctr_id ctrId, \n"
				+ "a.ctr_desc ctrDescSpa,\n"
				+ "a.ctr_desc_eng ctrDescEng\n"
				+ "from "+ ESQUEMA +".country_dim a, "
						+ ESQUEMA +".price_fact b, "
						+ ESQUEMA +".quality_dim d, \n"
						+ ESQUEMA +".size_dim f \n"
				+ "where a.ctr_sk = b.ctr_sk\n"
				+ "  and b.qua_sk = d.qua_sk\n"
				+ "  and b.size_sk = f.size_sk\n"
				+ "  and b.date_sk between "+initialDate+" and "+ finalDate
				+ "  and (prdvar_sk, ptype_sk, d.qua_Id, f.size_Id) IN ("+productsFilter+")";
			
			MapListHandler handler = new MapListHandler();
			QueryRunner query = new QueryRunner();
			List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
			JSONArray json = new JSONArray(resultList);
			
			String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
			
			return data;
		}
}