package org.promefrut.simefrut.struts.commons.beans;



import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.promefrut.simefrut.utils.PostgreSQLConnection;

public abstract class BaseDAO {
	protected SessionManager sessionManager;
	public String ESQUEMA;
	public String ESQUEMA_TABLA;
	protected ResourceBundle bundle=null;
	
	public BaseDAO(SessionManager session, ResourceBundle bundle) {
		this.sessionManager = session;
	    this.bundle = bundle;
		initialize();
	}
	
	public BaseDAO(SessionManager session) {
		this.sessionManager = session;
		initialize();
	}

	public BaseDAO() {
		initialize();
	}

	public void initialize() {
		this.ESQUEMA = PostgreSQLConnection.getDBSchema();
		this.ESQUEMA_TABLA = PostgreSQLConnection.getDBSchema().toUpperCase() + ".";
	}

	public void setSessionManager(SessionManager session) {
		this.sessionManager = session;
	}

	/**
	 * Funcion para remover los simbolos \r \n \t ' &
	 * @param cadena
	 * @return
	 */
	public String remueveSimbolos(String cadena) {
		String newCadena = "";
		char cr;
		char lf;
		char tab;
		cr = '\r';
		lf = '\n';
		tab = '\t';

		if(cadena != null) {
			newCadena = cadena.replaceAll("'", "").replaceAll("\\\n", " ").replaceAll("\"", "").replaceAll("&", " ")
							 .replace(cr, ' ').replace(lf, ' ').replace(tab, ' ');
		} else {
			newCadena = cadena;
		}
		return newCadena;
	}
        
        /**
         * Se obtienen las cantidades de una cosulta sql
         * @param sql
         * @return
         * @throws SQLException
         * @throws Exception
         * @throws Error
         */
         public int obtenerCantidad(String sql) throws SQLException, Exception, Error {
             QueryRunner query = new QueryRunner();
             ScalarHandler handler = new ScalarHandler();      
             
             BigDecimal result = (BigDecimal) query.query(sessionManager.getConnection(), sql, handler);
             return result.intValue();
         }
             
         public float obtenerCantidadDecimal(String sql) throws SQLException, Exception, Error {
              QueryRunner query = new QueryRunner();
              ScalarHandler handler = new ScalarHandler();      
               
              BigDecimal result = (BigDecimal) query.query(sessionManager.getConnection(), sql, handler);
              return result.floatValue();
         }
        
        /** 
         * Metodo en el que se envia el sql y retorna un valor String
         * siempre tiene que poseer como alias valorString
         * @param sql
         * @return
         * @throws SQLException
         * @throws Exception
         * @throws Error
         */
        public String extraerString(String sql) throws SQLException, Exception, Error{
             String valorString="";
             Statement stmt = null;
             ResultSet rs = null;
             
             try
                 {
                       stmt=sessionManager.getConnection().createStatement();                    
                       rs=stmt.executeQuery(sql);
                       while(rs.next()){
                           valorString =rs.getString("valorString");
                       }               
                 }
                 catch (Exception e)
                 {
                    e.printStackTrace();
                 }
                 finally
                 {
                       if(rs!=null){rs.close();}
                       if(stmt!=null){stmt.close();}
                 }
                 return valorString;
         }
         
         public String extraerStringConcatenado(String sql) throws SQLException, Exception, Error{
             String valorString="";
             Statement stmt = null;
             ResultSet rs = null;
             
             try
                 {
                       stmt=sessionManager.getConnection().createStatement();                    
                       rs=stmt.executeQuery(sql);
                       while(rs.next()){
                           valorString += rs.getString("valorString")+",";
                       }               
                 }
                 catch (Exception e)
                 {
                    e.printStackTrace();
                 }
                 finally
                 {
                       if(rs!=null){rs.close();}
                       if(stmt!=null){stmt.close();}
                 }
                 return valorString;
         }
        
        
     /**
      * Metodo para la extraccion de un list A traves de un MapListHandler con parametros o sin parametros
      * 
     * @param sql
     * @param parametros
     * @return
     * @throws SQLException
     * @throws Exception
     * @throws Error
     */
     public List<?> extraerLista(String sql, Object parametros[]) throws SQLException, Exception, Error {
         List<?> data=null;
         MapListHandler handler = new MapListHandler();
         QueryRunner query = new QueryRunner();
         if(parametros.length > 0) 
            data = (List<?>) query.query(sessionManager.getConnection(), sql, parametros, handler);
         else
            data = (List<?>) query.query(sessionManager.getConnection(), sql, handler);
          
        return data;
     }
     
    //Metodo para la extraccion de un String de acuerdo a un MapHandler
      /**
     * @param sql
     * @param parametros
     * @return
     * @throws Exception
     * @throws Error
     */
     @SuppressWarnings("rawtypes")
     @Deprecated
     public String getValorString(String sql, Object parametros[]) throws Exception, Error{
          String valorString="";
          MapHandler mapeador = new MapHandler();                                                                         
          QueryRunner query = new QueryRunner();
          
          if(parametros.length > 0)  
             valorString = ((HashMap)query.query(sessionManager.getConnection(), sql, parametros, mapeador)).get("idUsuario").toString();
          else
             valorString = ((HashMap)query.query(sessionManager.getConnection(), sql, mapeador)).get("idUsuario").toString();
          
          return valorString;
      }
      
     //Metodo para la extraccion de un valor entero ejemplo una cantidad
      /**
     * @param sql
     * @param parametros
     * @return
     * @throws SQLException
     * @throws Exception
     * @throws Error
     */
      @Deprecated
      public int getValorEntero(String sql, Object parametros[]) throws SQLException, Exception, Error {
          BigDecimal result;
          QueryRunner query = new QueryRunner();
          ScalarHandler handler = new ScalarHandler();      
          
          if(parametros.length > 0)  
              result = (BigDecimal) query.query(sessionManager.getConnection(), sql, parametros,handler);
          else
              result = (BigDecimal) query.query(sessionManager.getConnection(), sql, handler);
              
          return result.intValue();    
      }
        

}
