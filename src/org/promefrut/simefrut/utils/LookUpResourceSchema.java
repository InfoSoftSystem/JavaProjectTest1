package org.promefrut.simefrut.utils;


import java.io.IOException;

import java.util.Properties;

public class LookUpResourceSchema {
	private static Properties eschema = new Properties();

	public static final String APP_NAME = "SIMEFRUT";
	public static final String PUBLIC_URL = "http://LOCALHOST";

	public static final String DATA_SOURCE = "simefrutDS";
	public static final String DATA_SOURCE_DW = "jdbc/simefrutDS";
	public static final String DATA_SOURCE_NAME = "Datasource SIMEFRUT";
	public static final int DATA_SOURCE_MAX_CONNECTION= 5;

	public static final String APPLICATION_RESOURCE = "org.promefrut.simefrut.resource.ApplicationResources";
	public static final String EN_SV_PROPERTY = "/org/promefrut/simr/resource/ApplicationResources_en_SV.properties";
	public static final String EN_US_PROPERTY = "/org/promefrut/simr/resource/ApplicationResources_en_US.properties";

	static {
		try {
			eschema.load(LookUpResourceSchema.class.getResourceAsStream(EN_SV_PROPERTY));
		} catch(IOException e) {
			System.out.println("No se pudo cargar  \"" + EN_SV_PROPERTY + "\"");
			try {
				eschema.load(LookUpResourceSchema.class.getResourceAsStream(EN_US_PROPERTY));
			} catch(IOException es) {
				System.out.println("No se pudo cargar ningun properties");
			}
		}
	}
}
