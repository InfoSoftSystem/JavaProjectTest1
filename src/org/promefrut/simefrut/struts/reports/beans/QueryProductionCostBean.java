package org.promefrut.simefrut.struts.reports.beans;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class QueryProductionCostBean extends BaseDAO {
	
	public QueryProductionCostBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductsCollection(String initialDate, String finalDate, String countries) throws Exception, Error {
		String sql = "select distinct \n"
				+ "prod_id prodId,\n"
				+ "trim(prod_desc) prodDesc\n"
				+ "from "+ ESQUEMA +".country_dim a, "+ ESQUEMA +".production_fact b, "+ ESQUEMA +".product_dim c \n"
				+ "where a.ctr_sk = b.ctr_sk\n"
				+ "  and b.prod_sk = c.prod_sk\n"
				+ "  and b.year_sk between "+initialDate+" and "+ finalDate
				+ "  and a.ctr_id in ("+countries+")"
				+ "  and (b.maintenance_cost is not null or establishment_cost is not null )";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getYearsCollection() throws Exception, Error {
		String sql = "select distinct\n"
			+ "year_sk yearSk\n"
			+ "from "+ ESQUEMA +".production_fact\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getCountriesCollection(String initialDate, String finalDate ) throws Exception, Error {
		String sql = "select distinct \n"
			+ "a.ctr_sk ctrSk,\n"
			+ "ctr_id ctrId, \n"
			+ "ctr_desc ctrDescSpa,\n"
			+ "ctr_desc_eng ctrDescEng\n"
			+ "from "+ ESQUEMA +".country_dim a, "+ ESQUEMA +".production_fact b\n"
			+ "where a.ctr_sk = b.ctr_sk\n"
			+ "  and b.year_sk between "+initialDate+" and "+ finalDate
			+ "  and (b.maintenance_cost is not null or establishment_cost is not null )";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
}