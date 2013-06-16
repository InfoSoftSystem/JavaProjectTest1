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
public class QueryCIFOBBean extends BaseDAO {

	public QueryCIFOBBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getDestinationCountriesCollection(String initialDate, String finalDate, String countries) throws Exception, Error {
		String sql = "select distinct \n"
			//+ "origin.ctr_sk ctrSk,\n"
			+ "origin.ctr_id ctrId, \n"
			+ "origin.ctr_desc ctrDescSpa,\n"
			+ "origin.ctr_desc_eng ctrDescEng\n"
			+ "from "+ ESQUEMA +".country_dim origin, "
					 + ESQUEMA +".commerce_fact b,\n"
					 + ESQUEMA +".country_dim c "
			+ "where origin.ctr_sk = b.orictr_sk\n"
			+ "  and c.ctr_sk = b.ctr_sk\n"
			+ "  and b.year_sk between "+initialDate+" and "+ finalDate
			+ "  and c.ctr_id in("+ countries +")";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductsCollection(String initialDate, String finalDate, String originCountries, String destinationCountry) throws Exception, Error {
		String sql = "select distinct \n"
				+ "prod_id prodId,\n"
				+ "trim(prod_desc) prodDesc\n"
				+ "from "+ ESQUEMA +".country_dim a, "
						+ ESQUEMA +".commerce_fact b, " 
						+ ESQUEMA +".product_dim c, \n"
						+ ESQUEMA +".country_dim d \n"
				+ "where a.ctr_sk = b.ctr_sk\n"
				+ "  and b.prod_sk = c.prod_sk\n"
				+ "  and b.orictr_sk = d.ctr_sk\n"
				+ "  and b.year_sk between "+initialDate+" and "+ finalDate
				+ "  and a.ctr_id in ("+originCountries+")"
				+ "  and d.ctr_id in("+ destinationCountry +")";
		
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
			+ "from "+ ESQUEMA +".commerce_fact\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getOriginCountriesCollection(String initialDate, String finalDate ) throws Exception, Error {
		String sql = "select distinct \n"
			+ "a.ctr_sk ctrSk,\n"
			+ "a.ctr_id ctrId, \n"
			+ "a.ctr_desc ctrDescSpa,\n"
			+ "a.ctr_desc_eng ctrDescEng\n"
			+ "from "+ ESQUEMA +".country_dim a, "
					+ ESQUEMA +".commerce_fact b "
			+ "where a.ctr_sk = b.ctr_sk\n"
			+ "  and b.year_sk between "+initialDate+" and "+ finalDate;
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
}