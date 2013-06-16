package org.promefrut.simefrut.struts.maintenances.beans;

import java.math.BigDecimal;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class ImportCommerceComtradeBean extends BaseDAO {
	
	public ImportCommerceComtradeBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}
	
	public void insertImportDollar(String Year, String ReporterCountry, String ImportDollar, String user) throws Exception, Error{
		String sql = new String();
		QueryRunner query = new QueryRunner();
		
		if(!StringUtils.isBlank(ImportDollar)){
			sql="SAVEPOINT my_savepoint";
			//query.update(sessionManager.getConnection(), sql);
			
			sql = "insert into COMMERCE_COMTRADE_FACT(comt_id, year_sk, ctr_sk, comt_dollar, comt_type, audit_user_ins) " +
					"VALUES((select coalesce(max(comt_id)+1,1) from COMMERCE_COMTRADE_FACT)," +
					Year+", " +
					"(select a.ctr_sk from country_dim a, country_comtrade_equiv b where a.ctr_sk = b.ctr_sk and b.ctrade_id = "+ReporterCountry+" and current_date between a.effective_date and a.expiry_date and current_date between b.effective_date and b.expiry_date),"+
					ImportDollar+"::numeric, 'I','"+user+"');";
			query.update(sessionManager.getConnection(), sql);
		}
	}
	
	public void insertExportDollar(String Year, String ReporterCountry, String ExportDollar, String user) throws Exception, Error{
		String sql = new String();
		QueryRunner query = new QueryRunner();
		
		if(!StringUtils.isBlank(ExportDollar)){
			sql="SAVEPOINT my_savepoint";//When there're so many save point, postgresql throws an error. 
			//query.update(sessionManager.getConnection(), sql);
			
			sql = "insert into COMMERCE_COMTRADE_FACT(comt_id, year_sk, ctr_sk, comt_dollar, comt_type, audit_user_ins) " +
					"VALUES((select coalesce(max(comt_id)+1,1) from COMMERCE_COMTRADE_FACT)," +
					Year+", " +
					"(select a.ctr_sk from country_dim a, country_comtrade_equiv b where a.ctr_sk = b.ctr_sk and b.ctrade_id = "+ReporterCountry+" and current_date between a.effective_date and a.expiry_date and current_date between b.effective_date and b.expiry_date),"+
					ExportDollar+"::numeric, 'E','"+user+"');";
			
			query.update(sessionManager.getConnection(), sql);
		}
	}
	
	public void rollbackToSavePoint()throws Exception, Error{
		QueryRunner query = new QueryRunner();
		
		String sql="ROLLBACK TO SAVEPOINT my_savepoint";
		
		query.update(sessionManager.getConnection(), sql);
	}
	
	public boolean validateCountry(String ReporterCountry)throws Exception, Error{
		QueryRunner query = new QueryRunner();
		
		String sql = "select count(1) " +
				"from country_dim a, " +
				"	country_comtrade_equiv b " +
				"where a.ctr_sk = b.ctr_sk " +
				"  and b.ctrade_id = "+ReporterCountry + 
				"  and current_date between a.effective_date and a.expiry_date " +
				"  and current_date between b.effective_date and b.expiry_date ";
		
		Long reg = (Long)query.query(sessionManager.getConnection(), sql, new ScalarHandler());
		
		if(reg.intValue() ==0){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean existsData(String year)throws Exception, Error{
		QueryRunner query = new QueryRunner();
		
		String sql = "select count(1) " +
				"from COMMERCE_COMTRADE_FACT " +
				"where year_sk = "+ year;
		
		Long reg = (Long)query.query(sessionManager.getConnection(), sql, new ScalarHandler());
		
		if(reg.intValue() ==0){
			return false;
		}else{
			return true;
		}
	}
	
	public void deleteData(String yearList)throws Exception, Error{
		QueryRunner query = new QueryRunner();
		
		String sql = "delete from COMMERCE_COMTRADE_FACT " +
				"where year_sk in ("+yearList+")";
		query.update(sessionManager.getConnection(), sql);
	}
	
	public int getLastYearLoad()throws Exception, Error{
		QueryRunner query = new QueryRunner();
		
		String sql = "select max(year_sk) " +
				"from "+ESQUEMA+".COMMERCE_COMTRADE_FACT ";
		
		BigDecimal reg = (BigDecimal)query.query(sessionManager.getConnection(), sql, new ScalarHandler());
		
		if(reg != null){
			return reg.intValue();
		}else{
			return 0;
		}
	}
}