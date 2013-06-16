package org.promefrut.simefrut.struts.commons.beans;


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;


public class SessionManager {
	private Connection connection;
	private DataSource dataSource;

	public SessionManager(DataSource ds) {
		this.dataSource = ds;
	}

	private void initConnection() throws SQLException {
		if(connection == null || connection.isClosed()){
			connection = (dataSource != null) ? dataSource.getConnection() : null;
		}
	    connection.setAutoCommit(false);
	}

	public Connection getConnection() throws SQLException {
		initConnection();
		return connection;
	}
	
	public void setConnection(Connection c) throws SQLException {
		this.connection=c;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void rollback() {
		try {

			if(connection != null && !connection.isClosed()) {
				connection.rollback();
			}
		} catch(SQLException e) {
			//No hacemos nada
		}
	}

	public void commit() {
		try {

			if(connection != null && !connection.isClosed()) {
				connection.commit();
			}
		} catch(SQLException e) {
			//No hacemos nada
		}
	}

	public void close() {
		try {
			if(connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch(SQLException e) {
			e.printStackTrace(); // no hacemos nada
		}
	}
}
