/**
 * 
 */
package org.promefrut.simefrut.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Henry Willy Melara
 *
 */
public class PostgreSQLConnection {
	private static Properties eschema = new Properties();

	public static final String APP_NAME = "SIMEFRUT";
	public static final String DATA_SOURCE = "jdbc/simrDS";

	public static final String POSTGRESQL_SERVER_PROPERTY = "/org/promefrut/simefrut/resource/PostgreSQLServer.properties";

	static {
		try {
			eschema.load(LookUpResourceSchema.class.getResourceAsStream(POSTGRESQL_SERVER_PROPERTY));
		} catch(IOException e) {
			System.out.println("No se pudo cargar  \"" + POSTGRESQL_SERVER_PROPERTY + "\"");
		}
	}
	
	public static String getConexionUrl() {
		return eschema.getProperty("conexion.url");
	}
	
	public static String getConexionUsuario() {
		return eschema.getProperty("conexion.usuario");
	}
	
	public static String getConexionPassword() {
		return eschema.getProperty("conexion.password");
	}
	
	public static String getDatabase() {
		return eschema.getProperty("conexion.database");
	}
	
	public static String getDBSchema() {
		return eschema.getProperty("conexion.schema");
	}
}
