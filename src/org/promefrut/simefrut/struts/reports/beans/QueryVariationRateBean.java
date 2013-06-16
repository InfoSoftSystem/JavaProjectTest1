package org.promefrut.simefrut.struts.reports.beans;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

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
public class QueryVariationRateBean extends BaseDAO {
	
	public QueryVariationRateBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getProductsCollection(String variable, String periods, String countries) throws Exception, Error {
		String sql = new String();
		String sqlGlobal = new String();
		String initialDate = new String(), finalDate= new String();
		
		StringTokenizer tk = new StringTokenizer(periods,",");
		int i = 0;
		while(tk.hasMoreTokens()){
			String fechas[] = tk.nextToken().split("-");
			initialDate = fechas[0];
			finalDate = fechas[1];
			
			if(i>0){
				sqlGlobal+="\n union \n";
			}
			
			if("ProductionVol".equals(variable) || "HarvestedArea".equals(variable) || "ProductionCost".equals(variable)){
				
				String columna;
				
				if("ProductionVol".equals(variable)){
					columna = "production_vol";
					
				}else if("HarvestedArea".equals(variable)){
					columna = "Harvested_Area";
					
				}else{
					columna = "production_cost";
				}
				
				sql = "select distinct \n"+
						"	yo.prod_id prodId,\n" +
						"	trim(yo.prod_desc) prodDesc\n" +
						" from ( \n" +
						" 	SELECT \n" +
						" 	  sum(production."+columna+") valor, \n" +
						" 	  year.year_sk anio, \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc \n" +
						" 	FROM \n" +
						" 	  public.production_fact production, \n" +
						" 	  public.year_dim year, \n" +
						" 	  public.product_dim product, \n" +
						" 	  public.country_dim country \n" +
						"  	WHERE \n" +
						" 	  production.prod_sk = product.prod_sk AND \n" +
						" 	  production.ctr_sk = country.ctr_sk AND \n" +
						" 	  production.year_sk = year.year_sk \n" +
						" 	  and production.year_sk  = "+finalDate+" \n" +
						" 	  AND country.ctr_id in ("+countries+") \n" +
						" 	 group by  \n" +
						" 	 year.year_sk , \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc \n" +
						" ) yt, ( \n" +
						" 	SELECT \n" +
						" 	  sum(production."+columna+") valor, \n" + 
						" 	  year.year_sk anio, \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc \n" +
						" 	FROM \n" +
						" 	  public.production_fact production, \n" +
						" 	  public.year_dim year, \n" +
						" 	  public.product_dim product, \n" +
						" 	  public.country_dim country \n" +
						" 	WHERE \n" +
						" 	  production.prod_sk = product.prod_sk AND \n" +
						" 	  production.ctr_sk = country.ctr_sk AND \n" +
						" 	  production.year_sk = year.year_sk \n" +
						" 	  and production.year_sk  = "+initialDate+" \n" +
						" 	  AND country.ctr_id in ("+countries+") \n" +
						" 	 group by  \n" +
						"  	 year.year_sk , \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc  \n" +
						" ) yo \n" +
						"  where yo.ctr_id = yt.ctr_id \n" +
						"  and yo.prod_id = yt.prod_id \n" +
						"  and yo.valor is not null \n" +
						"  and yt.valor is not null \n";
				
				
				
			}else if("ExportDollar".equals(variable) || "ExportKilo".equals(variable) || "ImportDollar".equals(variable) || "ImportKilo".equals(variable)){
				String tipo, columna;
				
				if("ExportDollar".equals(variable) || "ExportKilo".equals(variable)){
					tipo = "E";
					
					if("ExportDollar".equals(variable)){
						columna = "vol_dollar";
					}else{
						columna = "vol_kilo"; 
					}
				}else{
					tipo = "I";
					
					if("ImportDollar".equals(variable)){
						columna = "vol_dollar";
					}else{
						columna = "vol_kilo";
					}
				}
				
				sql = "select distinct \n"+
						"	yo.prod_id prodId,\n" +
						"	trim(yo.prod_desc) prodDesc\n" +
						" from ( \n" +
						" 	SELECT \n" +
						" 	  sum(commerce."+columna+") ExportKilo, \n" +
						" 	  year.year_sk anio, \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc \n" +
						" 	FROM \n" +
						" 	  public.commerce_fact commerce, \n" +
						" 	  public.year_dim year, \n" +
						" 	  public.product_dim product, \n" +
						" 	  public.country_dim country \n" +
						"  	WHERE \n" +
						" 	  commerce.prod_sk = product.prod_sk AND \n" +
						" 	  commerce.ctr_sk = country.ctr_sk AND \n" +
						" 	  commerce.year_sk = year.year_sk \n" +
						" 	  and commerce.comm_type = '"+tipo+"' \n" +
						" 	  and commerce.year_sk  = "+finalDate+" \n" +
						" 	  AND country.ctr_id in ("+countries+") \n" +
						" 	 group by  \n" +
						" 	 year.year_sk , \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc \n" +
						" ) yt, ( \n" +
						" 	SELECT \n" +
						" 	  sum(commerce."+columna+") ExportKilo, \n" +
						" 	  year.year_sk anio, \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc \n" +
						" 	FROM \n" +
						" 	  public.commerce_fact commerce, \n" +
						" 	  public.year_dim year, \n" +
						" 	  public.product_dim product, \n" +
						" 	  public.country_dim country \n" +
						" 	WHERE \n" +
						" 	  commerce.prod_sk = product.prod_sk AND \n" +
						" 	  commerce.ctr_sk = country.ctr_sk AND \n" +
						" 	  commerce.year_sk = year.year_sk \n" +
						" 	  and commerce.comm_type = '"+tipo+"' \n" +
						" 	  and commerce.year_sk  = "+initialDate+" \n" +
						" 	  AND country.ctr_id in ("+countries+") \n" +
						" 	 group by  \n" +
						"  	 year.year_sk , \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc  \n" +
						" ) yo \n" +
						"  where yo.ctr_id = yt.ctr_id \n" +
						"  and yo.prod_id = yt.prod_id \n" +
						"  and yo.ExportKilo is not null \n" +
						"  and yt.ExportKilo is not null \n";
				
			}else if("PromedioXUnidad".equals(variable)){
				
				sql = " select distinct" +
						"	yo.prod_id prodId, \n" +
						"	trim(yo.prod_desc) prodDesc\n" +
						getPriceQuery(initialDate, finalDate, countries, null, null, null, null);
			}
		

			sqlGlobal+= sql;
			i++;
		}
		
		sqlGlobal = "Select distinct * from ("+sqlGlobal+") consulta ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sqlGlobal, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getProductTypesCollection(String variable, String periods, String countries, String prodId) throws Exception, Error {
		String sql = new String();
		String sqlGlobal = new String();
		String initialDate = new String(), finalDate= new String();
		
		StringTokenizer tk = new StringTokenizer(periods,",");
		int i = 0;
		while(tk.hasMoreTokens()){
			String fechas[] = tk.nextToken().split("-");
			initialDate = fechas[0];
			finalDate = fechas[1];
			
			if(i>0){
				sqlGlobal+="\n union \n";
			}
			
			if("PromedioXUnidad".equals(variable)){
				
				sql = " select distinct" +
						"	yo.ptype_id ptypeId, \n" +
						"	trim(yo.ptype_desc) ptypeDesc\n" +
						getPriceQuery(initialDate, finalDate, countries, prodId, null, null, null)+
						"  and yo.ptype_id is not null";
			}

			sqlGlobal += sql;
			i++;
		}
		
		sqlGlobal = "Select distinct * from ("+sqlGlobal+") consulta ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sqlGlobal, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getVarietiesCollection(String variable, String periods, String countries, String prodId, String ptypeId) throws Exception, Error {
		String sql = new String();
		String sqlGlobal = new String();
		String initialDate = new String(), finalDate= new String();
		
		StringTokenizer tk = new StringTokenizer(periods,",");
		int i = 0;
		while(tk.hasMoreTokens()){
			String fechas[] = tk.nextToken().split("-");
			initialDate = fechas[0];
			finalDate = fechas[1];
			
			if(i>0){
				sqlGlobal+="\n union \n";
			}
			
			if("PromedioXUnidad".equals(variable)){
				
				sql = " select distinct" +
						"	yo.var_id varId, \n" +
						"	trim(yo.var_desc) varDesc\n" +
						getPriceQuery(initialDate, finalDate, countries, prodId, ptypeId, null, null)+
						"  and yo.var_id is not null";
			}

			sqlGlobal += sql;
			i++;
		}
		
		sqlGlobal = "Select distinct * from ("+sqlGlobal+") consulta ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sqlGlobal, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getQualitiesCollection(String variable, String periods, String countries, String prodId, String ptypeId, String varId) throws Exception, Error {
		String sql = new String();
		String sqlGlobal = new String();
		String initialDate = new String(), finalDate= new String();
		
		StringTokenizer tk = new StringTokenizer(periods,",");
		int i = 0;
		
		while(tk.hasMoreTokens()){
			String fechas[] = tk.nextToken().split("-");
			initialDate = fechas[0];
			finalDate = fechas[1];
			
			if(i>0){
				sqlGlobal+="\n union \n";
			}
			
			if("PromedioXUnidad".equals(variable)){
				
				sql = " select distinct" +
						"	yo.qua_id quaId, \n" +
						"	trim(yo.qua_desc) quaDesc\n" +
						getPriceQuery(initialDate, finalDate, countries, prodId, ptypeId, varId, null);
			}

			sqlGlobal+= sql;
			i++;
		}
		
		sqlGlobal = "Select distinct * from ("+sqlGlobal+") consulta ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sqlGlobal, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getSizesCollection(String variable, String periods, String countries, String prodId, String ptypeId, String varId, String quaId) throws Exception, Error {
		String sql = new String();
		String sqlGlobal = new String();
		String initialDate = new String(), finalDate= new String();
		
		StringTokenizer tk = new StringTokenizer(periods,",");
		int i = 0;
		while(tk.hasMoreTokens()){
			String fechas[] = tk.nextToken().split("-");
			initialDate = fechas[0];
			finalDate = fechas[1];
			
			if(i>0){
				sqlGlobal+="\n union \n";
			}
			
			if("PromedioXUnidad".equals(variable)){
				
				sql = " select distinct" +
						"	yo.size_id sizeId, \n" +
						"	trim(yo.size_desc) sizeDesc\n" +
						getPriceQuery(initialDate, finalDate, countries, prodId, ptypeId, varId, quaId);
			}

			sqlGlobal+= sql;
			i++;
		}
		
		sqlGlobal = "Select distinct * from ("+sqlGlobal+") consulta ";
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sqlGlobal, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getYearsCollection(String variable) throws Exception, Error {
		String sql = new String();
		
		if("ProductionVol".equals(variable) || "HarvestedArea".equals(variable) || "ProductionCost".equals(variable)){
			sql = "select distinct\n"
				+ "year_sk yearSk\n"
				+ "from "+ ESQUEMA +".production_fact\n";
			
			if("ProductionVol".equals(variable)){
				sql+="  where production_vol is not null"; 
				
			}else if("ProductionCost".equals(variable)){
				sql+="  where production_cost is not null";
				
			}else if("HarvestedArea".equals(variable)){
				sql+="  where Harvested_Area is not null";
			}
			
			sql+= " order by yearSk desc";
			
		}else if("ExportDollar".equals(variable) || "ExportKilo".equals(variable) || "ImportDollar".equals(variable) || "ImportKilo".equals(variable)){
			sql = "select distinct\n"
				+ "year_sk yearSk\n"
				+ "from "+ ESQUEMA +".commerce_fact\n" 
				+ " order by yearSk desc";
			
		}else if("PromedioXUnidad".equals(variable)){
			sql ="select distinct extract(year from date_field) yearSk\n"
				+ "from "+ ESQUEMA +".price_fact a, "+ ESQUEMA +".date_dim b\n"
				+ " where a.date_sk = b.date_sk\n"
				+ " order by yearSk desc";
		}
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getCountriesCollection(String variable, String periods ) throws Exception, Error {
		String sql = new String();
		String sqlGlobal = new String();
		String initialDate = new String(), finalDate= new String();
		
		StringTokenizer tk = new StringTokenizer(periods,",");
		int i = 0;
		while(tk.hasMoreTokens()){
			String fechas[] = tk.nextToken().split("-");
			initialDate = fechas[0];
			finalDate = fechas[1];
			
			if(i>0){
				sqlGlobal+="\n union \n";
			}
		
			if("ProductionVol".equals(variable) || "HarvestedArea".equals(variable) || "ProductionCost".equals(variable)){
				
				String columna;
				
				if("ProductionVol".equals(variable)){
					columna = "production_vol";
					
				}else if("HarvestedArea".equals(variable)){
					columna = "Harvested_Area";
					
				}else{
					columna = "production_cost";
				}
				
				sql = "select distinct \n"+
						"	yo.ctr_id ctrId, \n" + 
						"	yo.ctr_desc ctrDescSpa,\n" + 
						"	yo.ctr_desc_eng ctrDescEng\n" + 
						" from ( \n" +
						" 	SELECT \n" +
						" 	  sum(production."+columna+") valor, \n" +
						" 	  year.year_sk anio, \n" +
						"	  country.ctr_desc_eng, \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc \n" +
						" 	FROM \n" +
						" 	  public.production_fact production, \n" +
						" 	  public.year_dim year, \n" +
						" 	  public.product_dim product, \n" +
						" 	  public.country_dim country \n" +
						"  	WHERE \n" +
						" 	  production.prod_sk = product.prod_sk AND \n" +
						" 	  production.ctr_sk = country.ctr_sk AND \n" +
						" 	  production.year_sk = year.year_sk \n" +
						" 	  and production.year_sk  = "+finalDate+" \n"+
						" 	 group by  \n" +
						" 	 year.year_sk , \n" +
						"	  country.ctr_desc_eng, \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc \n" +
						" ) yt, ( \n" +
						" 	SELECT \n" +
						" 	  sum(production."+columna+") valor, \n" +
						" 	  year.year_sk anio, \n" +
						"	  country.ctr_desc_eng, \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc \n" +
						" 	FROM \n" +
						" 	  public.production_fact production, \n" +
						" 	  public.year_dim year, \n" +
						" 	  public.product_dim product, \n" +
						" 	  public.country_dim country \n" +
						" 	WHERE \n" +
						" 	  production.prod_sk = product.prod_sk AND \n" +
						" 	  production.ctr_sk = country.ctr_sk AND \n" +
						" 	  production.year_sk = year.year_sk \n" +
						" 	  and production.year_sk  = "+initialDate+" \n" +
						" 	 group by  \n" +
						"  	 year.year_sk , \n" +
						"	  country.ctr_desc_eng, \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc  \n" +
						" ) yo \n" +
						"  where yo.ctr_id = yt.ctr_id \n" +
						"  and yo.prod_id = yt.prod_id \n" +
						"  and yo.valor is not null \n" +
						"  and yt.valor is not null \n";
				
			}else if("ExportDollar".equals(variable) || "ExportKilo".equals(variable) || "ImportDollar".equals(variable) || "ImportKilo".equals(variable)){
				
				String tipo, columna;
				
				if("ExportDollar".equals(variable) || "ExportKilo".equals(variable)){
					tipo = "E";
					
					if("ExportDollar".equals(variable)){
						columna = "vol_dollar";
					}else{
						columna = "vol_kilo";
					}
				}else{
					tipo = "I";
					
					if("ImportDollar".equals(variable)){
						columna = "vol_dollar";
					}else{
						columna = "vol_kilo";
					}
				}
				
				sql = "select distinct \n"+
						"	yt.ctr_id ctrId, \n" +
						"	yt.ctr_desc ctrDescSpa,\n" +
						"	yt.ctr_desc_eng ctrDescEng\n" +
						" from ( \n" +
						" 	SELECT \n" +
						" 	  sum(commerce."+columna+") ExportKilo, \n" +
						" 	  year.year_sk anio, \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						"	  country.ctr_desc_eng, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc \n" +
						" 	FROM \n" +
						" 	  public.commerce_fact commerce, \n" +
						" 	  public.year_dim year, \n" +
						" 	  public.product_dim product, \n" +
						" 	  public.country_dim country \n" +
						"  	WHERE \n" +
						" 	  commerce.prod_sk = product.prod_sk AND \n" +
						" 	  commerce.ctr_sk = country.ctr_sk AND \n" +
						" 	  commerce.year_sk = year.year_sk \n" +
						" 	  and commerce.comm_type = '"+tipo+"' \n" +
						" 	  and commerce.year_sk  = "+finalDate+" \n" +
						" 	 group by  \n" +
						" 	 year.year_sk , \n" +
						"	  country.ctr_desc_eng, \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc \n" +
						" ) yt, ( \n" +
						" 	SELECT \n" +
						" 	  sum(commerce."+columna+") ExportKilo, \n" +
						" 	  year.year_sk anio, \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						"	  country.ctr_desc_eng, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc \n" +
						" 	FROM \n" +
						" 	  public.commerce_fact commerce, \n" +
						" 	  public.year_dim year, \n" +
						" 	  public.product_dim product, \n" +
						" 	  public.country_dim country \n" +
						" 	WHERE \n" +
						" 	  commerce.prod_sk = product.prod_sk AND \n" +
						" 	  commerce.ctr_sk = country.ctr_sk AND \n" +
						" 	  commerce.year_sk = year.year_sk \n" +
						" 	  and commerce.comm_type = '"+tipo+"' \n" +
						" 	  and commerce.year_sk  = "+initialDate+" \n" +
						" 	 group by  \n" +
						"  	 year.year_sk , \n" +
						"	  country.ctr_desc_eng, \n" +
						" 	  country.ctr_desc, \n" +
						" 	  country.ctr_id, \n" +
						" 	  product.prod_id, \n" +
						" 	  product.prod_desc  \n" +
						" ) yo \n" +
						"  where yo.ctr_id = yt.ctr_id \n" +
						"  and yo.prod_id = yt.prod_id \n" +
						"  and yo.ExportKilo is not null \n" +
						"  and yt.ExportKilo is not null \n";
				
			}else if("PromedioXUnidad".equals(variable)){
				
				sql = " select distinct" +
						"	yo.ctr_id ctrId, \n" +
						"	yo.ctr_desc ctrDescSpa,\n" +
						"	yo.ctr_desc_eng ctrDescEng\n" +
						getPriceQuery(initialDate, finalDate, null, null, null, null, null);
				
						/*" from ( \n"+
						"	SELECT \n"+
						"	  round(sum((price.price_inf_uni + price.price_sup_uni)/2)/count(1),2) valor, \n"+
						"	  extract (YEAR FROM fecha.date_field)::integer anio, \n"+
						"	  country.ctr_desc_eng, " +
						"	  country.ctr_desc, \n"+
						"	  country.ctr_id, \n"+
						"	  product.prod_id, \n"+
						"	  product.prod_desc, \n"+
						"	  ptype.ptype_id ,\n"+ 
						"	  ptype.ptype_desc , \n"+
						"	  product.var_id, \n"+
						"	  product.var_desc, \n"+
						"	  quality.qua_id, \n"+
						"	  quality.qua_desc, \n"+
						"	  size.size_id, \n"+
						"	  size.size_desc, \n"+
						"	  origen.ctr_id orictr_id, \n"+
						"	  origen.ori_country, \n"+
						"	  tunit.tunit_id, \n"+
						"	  tunit_kilo, \n"+
						"	  tunit.tunit_desc \n"+
						"	FROM \n"+
						"	  public.price_fact price, \n"+
						"	  public.date_dim fecha, \n"+
						"	  public.country_dim country, \n"+
						"	  public.product_variety_dim product, \n"+
						"	  public.size_dim size, \n"+
						"	  public.quality_dim quality, \n"+
						"	  public.origin_dim origen, \n"+
						"	  public.trad_unit_dim tunit, \n"+
						"	  public.product_type_dim ptype \n" +
						"	WHERE \n"+
						"	  tunit.tunit_sk = price.tunit_sk AND \n"+
						"	  fecha.date_sk = price.date_sk AND \n"+
						"	  country.ctr_sk = price.ctr_sk AND \n"+
						"	  product.prdvar_sk = price.prdvar_sk AND \n"+
						"	  ptype.ptype_sk = price.ptype_sk AND \n"+
						"	  size.size_sk = price.size_sk AND \n"+
						"	  quality.qua_sk = price.qua_sk AND \n"+
						"	  origen.ori_sk = price.ori_sk \n"+
						"	  and extract (YEAR FROM fecha.date_field)::integer  = "+finalDate+" \n"+
						"	 group by \n"+
						"	  extract (YEAR FROM fecha.date_field)::integer, \n"+
						"	  country.ctr_desc_eng, " +
						"	  country.ctr_desc, \n"+
						"	  country.ctr_id, \n"+
						"	  product.prod_id, \n"+
						"	  product.prod_desc, \n"+
						"	  ptype.ptype_id ,\n"+ 
						"	  ptype.ptype_desc, \n"+
						"	  product.var_id, \n"+
						"	  product.var_desc, \n"+
						"	  quality.qua_id, \n"+
						"	  quality.qua_desc, \n"+
						"	  size.size_id, \n"+
						"	  size.size_desc, \n"+
						"	  origen.ctr_id, \n"+
						"	  origen.ori_country, \n"+
						"	  tunit.tunit_id, \n"+
						"	  tunit_kilo, \n"+
						"	  tunit.tunit_desc \n"+
						") yt, ( \n"+
						"	SELECT \n"+
						"	  round(sum((price.price_inf_uni + price.price_sup_uni)/2)/count(1),2) valor, \n"+
						"	  extract (YEAR FROM fecha.date_field)::integer anio, \n"+
						"	  country.ctr_desc_eng, " +
						"	  country.ctr_desc, \n"+
						"	  country.ctr_id, \n"+
						"	  product.prod_id, \n"+
						"	  product.prod_desc, \n"+
						"	  ptype.ptype_id ,\n"+ 
						"	  ptype.ptype_desc , \n"+
						"	  product.var_id, \n"+
						"	  product.var_desc, \n"+
						"	  quality.qua_id, \n"+
						"	  quality.qua_desc, \n"+
						"	  size.size_id, \n"+
						"	  size.size_desc, \n"+
						"	  origen.ctr_id orictr_id, \n"+
						"	  origen.ori_country, \n"+
						"	  tunit.tunit_id, \n"+
						"	  tunit_kilo, \n"+
						"	  tunit.tunit_desc \n"+
						"	FROM \n"+
						"	  public.price_fact price, \n"+
						"	  public.date_dim fecha, \n"+
						"	  public.country_dim country, \n"+
						"	  public.product_variety_dim product, \n"+
						"	  public.size_dim size, \n"+
						"	  public.quality_dim quality, \n"+
						"	  public.origin_dim origen, \n"+
						"	  public.trad_unit_dim tunit, \n"+
						"	  public.product_type_dim ptype \n" +
						"	WHERE \n"+
						"	  tunit.tunit_sk = price.tunit_sk AND \n"+
						"	  fecha.date_sk = price.date_sk AND \n"+
						"	  country.ctr_sk = price.ctr_sk AND \n"+
						"	  product.prdvar_sk = price.prdvar_sk AND \n"+
						"	  ptype.ptype_sk = price.ptype_sk AND \n"+
						"	  size.size_sk = price.size_sk AND \n"+
						"	  quality.qua_sk = price.qua_sk AND \n"+
						"	  origen.ori_sk = price.ori_sk \n"+
						"	  and extract (YEAR FROM fecha.date_field)::integer  = "+initialDate+" \n"+
						"	 group by \n"+
						"	  extract (YEAR FROM fecha.date_field)::integer, \n"+
						"	  country.ctr_desc_eng, " +
						"	  country.ctr_desc, \n"+
						"	  country.ctr_id, \n"+
						"	  product.prod_id, \n"+
						"	  product.prod_desc, \n"+
						"	  ptype.ptype_id ,\n"+ 
						"	  ptype.ptype_desc, \n"+
						"	  product.var_id, \n"+
						"	  product.var_desc, \n"+
						"	  quality.qua_id, \n"+
						"	  quality.qua_desc, \n"+
						"	  size.size_id, \n"+
						"	  size.size_desc, \n"+
						"	  origen.ctr_id, \n"+
						"	  origen.ori_country, \n"+
						"	  tunit.tunit_id, \n"+
						"	  tunit_kilo, \n"+
						"	  tunit.tunit_desc \n"+
						" ) yo \n"+
						" where yo.ctr_id = yt.ctr_id \n"+
						" and yo.prod_id = yt.prod_id \n"+
						" and yo.var_id = yt.var_id \n"+
						" and yo.ptype_id = yt.ptype_id \n"+
						" and yo.qua_id = yt.qua_id \n"+
						" and yo.size_id = yt.size_id \n"+
						" and yo.orictr_id = yt.orictr_id \n"+
						" and yo.tunit_id = yt.tunit_id \n" +
						" and yo.valor is not null \n" +
						" and yt.valor is not null ";*/
			}


			sqlGlobal+= sql;
			i++;
		}
		
		sqlGlobal = "Select distinct * from ("+sqlGlobal+") consulta ";
		
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sqlGlobal, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+(json.toString()==null?"":json.toString())+"}";
		
		return data;
	}
	
	private String getPriceQuery(String initialDate, String finalDate, String countries, String prodId, String ptypeId, String varId, String quaId){
		String sql = new String();
		
		sql = " from ( \n"+
				"	SELECT \n"+
				"	  round(sum((price.price_inf_uni + price.price_sup_uni)/2)/count(1),2) valor, \n"+
				"	  extract (YEAR FROM fecha.date_field)::integer anio, \n"+
				"	  country.ctr_desc_eng, " +
				"	  country.ctr_desc, \n"+
				"	  country.ctr_id, \n"+
				"	  product.prod_id, \n"+
				"	  product.prod_desc, \n"+
				"	  ptype.ptype_id ,\n" + 
				"	  ptype.ptype_desc , \n"+				
				"	  product.var_id, \n"+
				"	  product.var_desc, \n"+
				"	  quality.qua_id, \n"+
				"	  quality.qua_desc, \n"+
				"	  size.size_id, \n"+
				"	  size.size_desc, \n"+
				"	  origen.ctr_id orictr_id, \n"+
				"	  origen.ori_country, \n"+
				"	  tunit.tunit_id, \n"+
				"	  tunit_kilo, \n"+
				"	  tunit.tunit_desc \n"+
				"	FROM \n"+
				"	  public.price_fact price, \n"+
				"	  public.date_dim fecha, \n"+
				"	  public.country_dim country, \n"+
				"	  public.product_variety_dim product, \n"+
				"	  public.size_dim size, \n"+
				"	  public.quality_dim quality, \n"+
				"	  public.origin_dim origen, \n"+
				"	  public.trad_unit_dim tunit, \n"+
				"	  public.product_type_dim ptype \n" +
				"	WHERE \n"+
				"	  tunit.tunit_sk = price.tunit_sk AND \n"+
				"	  fecha.date_sk = price.date_sk AND \n"+
				"	  country.ctr_sk = price.ctr_sk AND \n"+
				"	  product.prdvar_sk = price.prdvar_sk AND \n"+
				"	  size.size_sk = price.size_sk AND \n"+
				"	  quality.qua_sk = price.qua_sk AND \n"+
				"	  origen.ori_sk = price.ori_sk \n"+
				"     and price.ptype_sk = ptype.ptype_sk \n ";
			
		if(!StringUtils.isEmpty(countries)){
			sql += "	  and country.ctr_id in ("+countries+") \n";
		}
		
		if(!StringUtils.isEmpty(prodId)){
			sql += "	  and product.prod_id in ("+prodId+") \n";
		}
		
		if(!StringUtils.isEmpty(ptypeId)){
			sql += "	  and ptype.ptype_id in ("+ptypeId+") \n";
		}
		
		if(!StringUtils.isEmpty(varId)){
			sql += "	  and product.var_id in ("+varId+") \n";
		}
		
		if(!StringUtils.isEmpty(quaId)){
			sql += "	  and quality.qua_id in ("+quaId+") \n";
		}
		
		if(!StringUtils.isEmpty(finalDate)){
			sql += "	  and extract (YEAR FROM fecha.date_field)::integer  = "+finalDate+" \n";
		}
		
		sql +="	 group by \n"+
				"	  extract (YEAR FROM fecha.date_field)::integer, \n"+
				"	  country.ctr_desc_eng, " +
				"	  country.ctr_desc, \n"+
				"	  country.ctr_id, \n"+
				"	  product.prod_id, \n"+
				"	  product.prod_desc, \n"+
				"	  ptype.ptype_id ,\n" + 
				"	  ptype.ptype_desc , \n"+
				"	  product.var_id, \n"+
				"	  product.var_desc, \n"+
				"	  quality.qua_id, \n"+
				"	  quality.qua_desc, \n"+
				"	  size.size_id, \n"+
				"	  size.size_desc, \n"+
				"	  origen.ctr_id, \n"+
				"	  origen.ori_country, \n"+
				"	  tunit.tunit_id, \n"+
				"	  tunit_kilo, \n"+
				"	  tunit.tunit_desc \n"+
				") yt, ( \n"+
				"	SELECT \n"+
				"	  round(sum((price.price_inf_uni + price.price_sup_uni)/2)/count(1),2) valor, \n"+
				"	  extract (YEAR FROM fecha.date_field)::integer anio, \n"+
				"	  country.ctr_desc_eng, " +
				"	  country.ctr_desc, \n"+
				"	  country.ctr_id, \n"+
				"	  product.prod_id, \n"+
				"	  product.prod_desc, \n"+
				"	  ptype.ptype_id ,\n" + 
				"	  ptype.ptype_desc , \n"+				
				"	  product.var_id, \n"+
				"	  product.var_desc, \n"+
				"	  quality.qua_id, \n"+
				"	  quality.qua_desc, \n"+
				"	  size.size_id, \n"+
				"	  size.size_desc, \n"+
				"	  origen.ctr_id orictr_id, \n"+
				"	  origen.ori_country, \n"+
				"	  tunit.tunit_id, \n"+
				"	  tunit_kilo, \n"+
				"	  tunit.tunit_desc \n"+
				"	FROM \n"+
				"	  public.price_fact price, \n"+
				"	  public.date_dim fecha, \n"+
				"	  public.country_dim country, \n"+
				"	  public.product_variety_dim product, \n"+
				"	  public.size_dim size, \n"+
				"	  public.quality_dim quality, \n"+
				"	  public.origin_dim origen, \n"+
				"	  public.trad_unit_dim tunit, \n"+
				"	  public.product_type_dim ptype \n" +
				"	WHERE \n"+
				"	  tunit.tunit_sk = price.tunit_sk AND \n"+
				"	  fecha.date_sk = price.date_sk AND \n"+
				"	  country.ctr_sk = price.ctr_sk AND \n"+
				"	  product.prdvar_sk = price.prdvar_sk AND \n"+
				"	  size.size_sk = price.size_sk AND \n"+
				"	  quality.qua_sk = price.qua_sk AND \n"+
				"	  origen.ori_sk = price.ori_sk \n" +
				"     and price.ptype_sk = ptype.ptype_sk \n ";
		
	if(!StringUtils.isEmpty(countries)){
		sql += "	  and country.ctr_id in ("+countries+") \n";
	}
	
	if(!StringUtils.isEmpty(prodId)){
		sql += "	  and product.prod_id in ("+prodId+") \n";
	}

	if(!StringUtils.isEmpty(ptypeId)){
		sql += "	  and ptype.ptype_id in ("+ptypeId+") \n";
	}
	
	if(!StringUtils.isEmpty(varId)){
		sql += "	  and product.var_id in ("+varId+") \n";
	}
	
	if(!StringUtils.isEmpty(quaId)){
		sql += "	  and quality.qua_id in ("+quaId+") \n";
	}
	
	if(!StringUtils.isEmpty(finalDate)){
		sql += "	  and extract (YEAR FROM fecha.date_field)::integer  = "+initialDate+" \n";
	}
		sql +=	"	 group by \n"+
				"	  extract (YEAR FROM fecha.date_field)::integer, \n"+
				"	  country.ctr_desc_eng, " +
				"	  country.ctr_desc, \n"+
				"	  country.ctr_id, \n"+
				"	  product.prod_id, \n"+
				"	  product.prod_desc, \n"+
				"	  ptype.ptype_id ,\n" + 
				"	  ptype.ptype_desc , \n"+
				"	  product.var_id, \n"+
				"	  product.var_desc, \n"+
				"	  quality.qua_id, \n"+
				"	  quality.qua_desc, \n"+
				"	  size.size_id, \n"+
				"	  size.size_desc, \n"+
				"	  origen.ctr_id, \n"+
				"	  origen.ori_country, \n"+
				"	  tunit.tunit_id, \n"+
				"	  tunit_kilo, \n"+
				"	  tunit.tunit_desc \n"+
				" ) yo \n"+
				" where yo.ctr_id = yt.ctr_id \n"+
				" and yo.prod_id = yt.prod_id \n"+
				" and coalesce(yo.ptype_id,0) = coalesce(yt.ptype_id,0) \n"+
				" and coalesce(yo.var_id,0) = coalesce(yt.var_id,0) \n"+
				" and yo.qua_id = yt.qua_id \n"+
				" and yo.size_id = yt.size_id \n"+
				" and yo.orictr_id = yt.orictr_id \n"+
				" and yo.tunit_id = yt.tunit_id \n" +
				" and yo.valor is not null \n" +
				" and yt.valor is not null ";
		
		return sql;
	}
}