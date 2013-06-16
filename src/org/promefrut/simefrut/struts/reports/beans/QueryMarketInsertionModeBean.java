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
public class QueryMarketInsertionModeBean extends BaseDAO {
	
	public QueryMarketInsertionModeBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductsCollection(String initialDate, String finalDate, String offererCountry, String destinationCountry) throws Exception, Error {
		String sql = new String();
		
		sql ="select distinct \n"
				+ "prod_id prodId,\n"
				+ "trim(prod_desc) prodDesc\n"
				+ "from ("+getQueryBase(initialDate, finalDate, offererCountry, destinationCountry)+") consulta";
		
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
			+ "from "+ ESQUEMA +".commerce_faostat_fact\n";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getOffererCollection(String initialDate, String finalDate ) throws Exception, Error {
		String sql = new String();
		
		sql = "select distinct \n"
			+ "orictr_id ctrId, \n"
			+ "orictr_desc ctrDescSpa,\n"
			+ "orictr_desc_eng ctrDescEng\n"
			+ "from ("+getQueryBase(initialDate, finalDate, null, null)+") consulta";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getDestinationCollection(String initialDate, String finalDate, String offererCountry ) throws Exception, Error {
		String sql = new String();
		
		sql = "select distinct \n"
			+ "ctr_id ctrId, \n"
			+ "ctr_desc ctrDescSpa,\n"
			+ "ctr_desc_eng ctrDescEng\n"
			+ "from ("+getQueryBase(initialDate, finalDate, offererCountry, null)+") consulta";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	private String getQueryBase(String initialDate, String finalDate, String offererCountry, String destinationCountry){
		String sql = new String();
		
		sql="select yo.prod_id, yo.prod_desc , \n" +
				"	yo.ctr_id, yo.ctr_desc , yo.ctr_desc_eng, \n" +
				"	yo.orictr_id, yo.orictr_desc , yo.orictr_desc_eng, \n" +
				"	"+initialDate+" initial_year, "+finalDate+" final_year \n" +
				"from ( \n" +
				"	SELECT \n" +
				"	  sum(commerce.fao_dollar) ImportDollarYt, \n" +
				"	  year_sk anio, \n" +
				"	  country.ctr_sk, \n" +
				"	  faoprod.faoprod_sk prod_sk, \n" +
				"	  destination.ctr_sk orictr_sk \n" +
				"	FROM \n" +
				"	  public.commerce_faostat_fact commerce, \n" +
				"	  public.product_faostat_dim faoprod, \n" +
				"	  public.country_dim country, \n" +
				"	  public.country_dim destination \n" +
				"	WHERE \n" +
				"	  commerce.faoprod_sk = faoprod.faoprod_sk AND \n" +
				"	  commerce.ctr_sk = country.ctr_sk AND \n" +
				"	  commerce.orictr_sk = destination.ctr_sk \n" +
				"	  and commerce.fao_type = 'I' \n" +
				"	  and commerce.year_sk  = "+finalDate+" \n";
					
			if(!StringUtils.isEmpty(destinationCountry)){
				sql+= "	  AND country.ctr_id in ("+destinationCountry+") \n";
			}

			if(!StringUtils.isEmpty(offererCountry)){
				sql+= "	  AND destination.ctr_id in ("+offererCountry+") \n";
			}
					
			sql+="	 group by \n" +
				"	  year_sk , \n" +
				"	  country.ctr_sk, \n" +
				"	  faoprod.faoprod_sk, \n" +
				"	  destination.ctr_sk \n" +
				") yt, ( \n" +
				"	SELECT \n" +
				"	  sum(commerce.fao_dollar) ImportDollarYo, \n" +
				"	  year_sk anio, \n" +
				"	  country.ctr_sk, \n" +
				"	  country.ctr_desc_eng, \n" +
				"	  faoprod.faoprod_sk prod_sk, \n" +
				"	  destination.ctr_sk orictr_sk, \n" +
				"	  destination.ctr_desc_eng orictr_desc_eng, \n" +
				"	  country.ctr_desc, \n" +
				"	  country.ctr_id, \n" +
				"	  faoprod.prod_code prod_id, \n" +
				"	  faoprod.prod_desc, \n" +
				"	  destination.ctr_id orictr_id, \n" +
				"	  destination.ctr_desc orictr_desc \n" +
				"	FROM \n" +
				"	  public.commerce_faostat_fact commerce, \n" +
				"	  public.product_faostat_dim faoprod, \n" +
				"	  public.country_dim country, \n" +
				"	  public.country_dim destination \n" +
				"	WHERE \n" +
				"	  commerce.faoprod_sk = faoprod.faoprod_sk AND \n" +
				"	  commerce.ctr_sk = country.ctr_sk AND \n" +
				"	  commerce.orictr_sk = destination.ctr_sk \n" +
				"	  and commerce.fao_type = 'I' \n" +
				"	  and commerce.year_sk  = "+initialDate+" \n";
					
			if(!StringUtils.isEmpty(destinationCountry)){
				sql+= "	  AND country.ctr_id in ("+destinationCountry+") \n";
			}

			if(!StringUtils.isEmpty(offererCountry)){
				sql+= "	  AND destination.ctr_id in ("+offererCountry+") \n";
			}
					
			sql+="	 group by \n" +
				"	 year_sk , \n" +
				"	  country.ctr_sk, \n" +
				"	  country.ctr_desc_eng, \n" +
				"	  faoprod.faoprod_sk, \n" +
				"	  destination.ctr_sk , \n" +
				"	  destination.ctr_desc_eng, \n" +
				"	  country.ctr_desc, \n" +
				"	  country.ctr_id, \n" +
				"	  faoprod.prod_code, \n" +
				"	  faoprod.prod_desc, \n" +
				"	  destination.ctr_id, \n" +
				"	  destination.ctr_desc \n" +
				" ) yo,( \n" +
				"	SELECT \n" +
				"	  sum(commerce.fao_dollar) ImportDollarYt, \n" +
				"	  year_sk anio, \n" +
				"	  country.ctr_sk, \n" +
				"	  faoprod.faoprod_sk prod_sk \n" +
				"	FROM \n" +
				"	  public.commerce_faostat_fact commerce, \n" +
				"	  public.product_faostat_dim faoprod, \n" +
				"	  public.country_dim country \n" +
				"	WHERE \n" +
				"	  commerce.faoprod_sk = faoprod.faoprod_sk AND \n" +
				"	  commerce.ctr_sk = country.ctr_sk \n" +
				"	  and commerce.fao_type = 'I' \n" +
				"	  and commerce.year_sk  = "+finalDate+" \n";
					
			if(!StringUtils.isEmpty(destinationCountry)){
				sql+= "	  AND country.ctr_id in ("+destinationCountry+") \n";
			}
				
			sql+="	 group by \n" +
				"	  year_sk , \n" +
				"	  country.ctr_sk, \n" +
				"	  faoprod.faoprod_sk \n" +
				") totalyt, ( \n" +
				"	SELECT \n" +
				"	  sum(commerce.fao_dollar) ImportDollarYo, \n" +
				"	  year_sk anio, \n" +
				"	  country.ctr_sk, \n" +
				"	  faoprod.faoprod_sk prod_sk \n" +
				"	FROM \n" +
				"	  public.commerce_faostat_fact commerce, \n" +
				"	  public.product_faostat_dim faoprod, \n" +
				"	  public.country_dim country \n" +
				"	WHERE \n" +
				"	  commerce.faoprod_sk = faoprod.faoprod_sk AND \n" +
				"	  commerce.ctr_sk = country.ctr_sk \n" +
				"	  and commerce.fao_type = 'I' \n" +
				"	  and commerce.year_sk  = "+initialDate+" \n";

			if(!StringUtils.isEmpty(destinationCountry)){
				sql+= "	  AND country.ctr_id in ("+destinationCountry+") \n";
			}
					
			sql+="	 group by \n" +
				"	 year_sk , \n" +
				"	 country.ctr_sk, \n" +
				"	  faoprod.faoprod_sk \n" +
				" ) totalyo \n" +
				" where yo.ctr_sk = yt.ctr_sk \n" +
				" and yo.prod_sk = yt.prod_sk \n" +
				" and yo.orictr_sk = yt.orictr_sk \n" +
				" and totalyo.ctr_sk = yo.ctr_sk \n" +
				" and totalyo.prod_sk = yo.prod_sk \n" +
				" and totalyt.ctr_sk = yt.ctr_sk \n" +
				" and totalyt.prod_sk = yt.prod_sk \n"+
				" and yo.ImportDollarYo >0 \n"+
				" and yt.ImportDollarYt >0 \n";
			
		return sql;
	}
}