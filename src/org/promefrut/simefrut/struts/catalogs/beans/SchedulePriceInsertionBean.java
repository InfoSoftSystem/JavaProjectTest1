package org.promefrut.simefrut.struts.catalogs.beans;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.json.JSONArray;
import org.promefrut.simefrut.struts.administration.forms.UserForm;
import org.promefrut.simefrut.struts.catalogs.forms.SchedulePriceInsertionForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class SchedulePriceInsertionBean extends BaseDAO {
	protected final String TABLA = super.ESQUEMA_TABLA + "scheduled_price_insertion";
	protected static final String PREFIJO = "spi";
	
	public SchedulePriceInsertionBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find(Integer ctrId) throws SQLException, Exception, Error {
		String sql = "select "+ PREFIJO +"_ID spiId,\n"
				+ " 	ctr.CTR_ID ctrId,\n"
				+ " 	ctr.CTR_Desc ctrDescSpa,\n"
				+ " 	ctr.CTR_Desc_eng ctrDescEng,\n"
				+ "		"+ PREFIJO +"_value spiValueSpa,"
				+ "		replace(replace(replace(replace(replace(replace(replace("+ PREFIJO +"_value,'Lunes','Monday'),'Martes','Tuesday'),'Miercoles','Wednesday'),'Jueves','Thursday'),'Viernes','Friday'),'Sabado','Saturday'),'Domingo','Sunday') spiValueEng,"
				+ "		case when position('Lunes' in "+ PREFIJO +"_value) >0 then true else false end monday,"
				+ "		case when position('Martes' in "+ PREFIJO +"_value) >0 then true else false end tuesday,"
				+ "		case when position('Miercoles' in "+ PREFIJO +"_value) >0 then true else false end wednesday,"
				+ "		case when position('Jueves' in "+ PREFIJO +"_value) >0 then true else false end thursday,"
				+ "		case when position('Viernes' in "+ PREFIJO +"_value) >0 then true else false end friday,"
				+ "		case when position('Sabado' in "+ PREFIJO +"_value) >0 then true else false end saturday,"
				+ "		case when position('Domingo' in "+ PREFIJO +"_value) >0 then true else false end sunday,"
				+ "		CASE WHEN aa.audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
				+ "		coalesce(aa.audit_user_upd, aa.audit_user_ins) audit_user, \n" 
				+ "		coalesce(aa.audit_date_upd, aa.audit_date_ins) audit_date\n"
				+ " from " + TABLA+" aa, "
				+ ESQUEMA+".COUNTRIES_CTG ctr \n"
				+ " where aa.ctr_id = ctr.ctr_id \n"
				+ "   AND aa."+ PREFIJO +"_date_end is null \n";
		
		//If the number is less than 0 it means that the current user can see data from other countries
		if(ctrId.intValue()>0){
			sql += " \n AND ctr.CTR_ID = ? \n";
		}
		sql += "ORDER BY \n"
			+ " "+ PREFIJO +"_ID DESC \n";

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		
		Object[] param = new Object[1];
		int i = 0;
		param[i++] = ctrId;
		
		List<Map> resultList = null;

		if(ctrId.intValue()>0){
			resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, param, handler);
		}else{
			resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		}
		
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		//String resultJSON = "Ext.data.JsonP.callback1("+data+")";
		return data;
	}

	public int insert(SchedulePriceInsertionForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_ID) + 1,1)\n"
					+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		BigDecimal spiId = (BigDecimal)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into "+ TABLA + "\n"
					+ "("+ PREFIJO +"_ID,\n"
					+ PREFIJO +"_VALUE,\n"
					+ PREFIJO +"_TYPE,\n"
					+ "	   CTR_ID,\n"
					+ PREFIJO +"_DATE_INI,\n"
					+ PREFIJO +"_DATE_END,\n"
					+ "    audit_user_ins, \n"
					+ "    audit_date_ins \n"
					+ ")\n"
					+ "values (?,?,'weekly',?,CURRENT_DATE,null,?,now()"
					+ ")";

		Object[] param = new Object[4];
		int i = 0;
		param[i++] = spiId;
		param[i++] = form.getSpiValue().trim();
		param[i++] = form.getCtrId();
		param[i++] = username;
	    
	    form.setSpiId(new Integer(spiId.intValue()));

		i = query.update(sessionManager.getConnection(), sql, param);
		
		return i;
	}

	public int update(SchedulePriceInsertionForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ " set "
					/*+ PREFIJO +"_VALUE = ?, \n"
					+ "		CTR_ID=?,\n"*/
					+ PREFIJO +"_DATE_END = CURRENT_DATE - 1,\n"
					+ "		audit_user_upd = ?, \n"
					+ "		audit_date_upd = now() \n"
					+ " where "+ PREFIJO +"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[2];
		int i = 0;
		//param[i++] = form.getTunitCode().trim();
		//param[i++] = form.getSpiValue().trim();
	    //param[i++] = form.getCtrId();
		param[i++] = username;
	    param[i++] = form.getSpiId();

		i = query.update(sessionManager.getConnection(), sql, param);
		
		return i;
	}

	/**
	 * @deprecated
	 * 
	 * @param form
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 * @throws Error
	 */
	public int delete(SchedulePriceInsertionForm form) throws SQLException, Exception, Error {
		String sql = "DELETE FROM " + TABLA
					+ " WHERE "+ PREFIJO +"_ID = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[1];
		
		param[0] = form.getSpiId();
		
		return query.update(sessionManager.getConnection(), sql, param);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getCountriesCollection(UserForm user) throws Exception, Error {
		String sql = "select \n"
			+ "ctr_id ctrId, \n"
			+ "ctr_desc ctrDescSpa,\n"
			+ "ctr_desc_eng ctrDescEng\n"
			+ "from "+ ESQUEMA +".COUNTRIES_CTG\n"
			+ "where ctr_status = 'A'\n";
		
		if(!user.getNoCountry()){
			sql+= " and ctr_id = " + user.getCtrId();
		}
		
		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();
		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		return json.toString()==null?"":json.toString();
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object>countRegisters(SchedulePriceInsertionForm form) throws SQLException, Exception, Error {
		Object[] param = null;

		String sql = "select "+ PREFIJO +"_ID spiId,\n"
				+ " 	ctr.CTR_ID ctrId,\n"
				+ " 	ctr.CTR_Desc ctrDescSpa,\n"
				+ " 	ctr.CTR_Desc_eng ctrDescEng\n"
				+ " from " + TABLA+" aa , "
				+ ESQUEMA+".COUNTRIES_CTG ctr \n"
				+ " where aa.ctr_id = ctr.ctr_id \n"
				+ "   and aa.ctr_id = ? \n"
				+ "   AND aa."+ PREFIJO +"_DATE_END IS NULL";
		
		if(form.getSpiId().intValue() != 0){
			sql += "   and aa.spi_id <> " +String.valueOf(form.getSpiId());
		}
		
		param = new Object[1];
		
		MapHandler handler = new MapHandler();
		QueryRunner query = new QueryRunner();
		
		int i = 0;
		param[i++] = form.getCtrId();
				
		Map<String,Object> resultList = null;

		resultList = (Map<String,Object>)query.query(sessionManager.getConnection(), sql, param, handler);
		
		if(resultList==null){
			resultList = new HashMap<String,Object>();
		}
		
		return resultList;
	}
}
