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
import org.promefrut.simefrut.struts.catalogs.forms.CountryFAOSTATForm;
import org.promefrut.simefrut.struts.commons.beans.BaseDAO;
import org.promefrut.simefrut.struts.commons.beans.SessionManager;


/**
 * @author HWM
 *
 */
public class CountryFAOSTATBean extends BaseDAO {
	protected final String TABLA = super.ESQUEMA_TABLA + "country_FAO_equiv";
	protected static final String PREFIJO = "faoctr";
	
	public CountryFAOSTATBean(SessionManager session, ResourceBundle bundle) {
		super(session, bundle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find(String ctrId) throws SQLException, Exception, Error {
		String sql = "select "+ PREFIJO +"_SK faoctrSk,\n"
				+ "		"+ PREFIJO +"_id faoctrId,\n"
				+ " 	"+ PREFIJO +"_desc faoctrDesc, \n"
				+ "		country.ctr_id faoctrCtrId,\n"
				+ "		country.ctr_ISO3 faoctrCtrISO3,\n"
				+ "		country.ctr_desc faoctrCtrDesc,\n"
				+ "		country.ctr_desc_eng regctrDescEng,\n"
				+ "		CASE WHEN reg.audit_user_upd is null THEN 'Y' ELSE 'N' END auditStatus,"
				+ "		coalesce(reg.audit_user_upd, reg.audit_user_ins) audit_user, \n" 
				+ "		coalesce(reg.audit_date_upd, reg.audit_date_ins) audit_date\n"
				+ " from " + TABLA+" reg, "+super.ESQUEMA_TABLA+"COUNTRY_DIM country"
				+ " where country.ctr_sk = reg.ctr_sk \n"
				+ " and country.CTR_ID = "+ctrId
				+ " AND current_date between country.effective_date and country.expiry_date "
				+ " AND current_date between reg.effective_date and reg.expiry_date";

		MapListHandler handler = new MapListHandler();
		QueryRunner query = new QueryRunner();

		List<Map> resultList = (List<Map>)query.query(sessionManager.getConnection(), sql, handler);
		JSONArray json = new JSONArray(resultList);
		
		String data = "{\"totalCount\":\""+resultList.size()+"\",\"registers\":"+json.toString()+"}";
		return data;
	}

	public int insert(CountryFAOSTATForm form, String username) throws SQLException, Exception, Error {
		
		String sql = "select coalesce(max("+ PREFIJO +"_SK) + 1,1)\n"
					+ "         from " + TABLA ;
		QueryRunner query = new QueryRunner();
		
		ScalarHandler handler = new ScalarHandler();
		BigDecimal faoctrSk = (BigDecimal)query.query(sessionManager.getConnection(), sql, handler);
		
		sql = " insert into "+ TABLA + "\n"
					+ "("+ PREFIJO +"_SK,\n"
					+ PREFIJO + "_ID, \n"
					+ PREFIJO +"_DESC,\n"
					+ "    ctr_sk,\n"
					+ "    audit_user_ins, \n"
					+ "    audit_date_ins \n"
					+ ")\n"
					+ "values (?,?,?,(select ctr_sk \n"
						+ " from " + ESQUEMA +".COUNTRY_DIM \n"
						+ " where CTR_ID = ? "
						+ " AND current_date between effective_date and expiry_date),?,now()"
					+ ")";

		Object[] param = new Object[5];
		int i = 0;
		param[i++] = faoctrSk;
	    param[i++] = new Integer(form.getFaoctrId());
	    param[i++] = form.getFaoctrDesc().trim();
	    param[i++] = new Integer(form.getCtrId());
	    param[i++] = username;
	    
	    form.setFaoctrSk(String.valueOf(faoctrSk));

		i = query.update(sessionManager.getConnection(), sql, param);
		
		return i;
	}

	public int update(CountryFAOSTATForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ " set "+ PREFIJO +"_DESC= ?, \n"
					+ "    "+ PREFIJO +"_ID= ?, \n"
					+ "    audit_user_upd = ?, \n"
					+ "    audit_date_upd = now() \n"
					+ " where "+ PREFIJO +"_SK = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[4];
		int i = 0;
		param[i++] = form.getFaoctrDesc().trim();
	    param[i++] = new Integer(form.getFaoctrId());
	    param[i++] = username;
	    param[i++] = new BigDecimal(form.getFaoctrSk());

		i = query.update(sessionManager.getConnection(), sql, param);
		
		return i;
	}
	
	public String existsData(CountryFAOSTATForm form)throws Exception, Error{
		QueryRunner query = new QueryRunner();
		
		String sql = "select CTR_ISO3||' - '|| CTR_DESC " +
				" from " + TABLA +" a,  " + ESQUEMA +".COUNTRY_DIM b\n" +
				" where a.ctr_sk = b.ctr_sk \n" +
				"   AND "+ PREFIJO +"_id = "+ form.getFaoctrId().trim() +
				"   AND current_date between a.effective_date and a.expiry_date"+
				"   AND current_date between b.effective_date and b.expiry_date";
		
		if(!StringUtils.isBlank(form.getFaoctrSk())){
			sql+="	AND "+ PREFIJO +"_SK <> " + form.getFaoctrSk();
		}
		
		String reg = (String)query.query(sessionManager.getConnection(), sql, new ScalarHandler());
		
		return reg;
	}
	
	public int delete(CountryFAOSTATForm form, String username) throws SQLException, Exception, Error {
		String sql = " update " + TABLA + " \n"
					+ " set expiry_date= current_date-1, \n"
					+ "    audit_user_upd = ?, \n"
					+ "    audit_date_upd = now() \n"
					+ " where "+ PREFIJO +"_SK = ? ";

		QueryRunner query = new QueryRunner();
		Object[] param = new Object[2];
		int i = 0;
		param[i++] = username;
	    param[i++] = new BigDecimal(form.getFaoctrSk());

		i = query.update(sessionManager.getConnection(), sql, param);
		
		return i;
	}
}