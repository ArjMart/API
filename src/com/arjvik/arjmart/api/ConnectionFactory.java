package com.arjvik.arjmart.api;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.inject.Inject;

public class ConnectionFactory {
	
	private String DB_URL, DB_USER, DB_PASSWORD;
	
	@Inject
	public ConnectionFactory() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Properties properties = new Properties();
			InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("jdbc.properties");
			if(in==null)
				throw new RuntimeException("Could not read DB connection info, check if jdbc.properties exists in classpath");
			properties.load(in);
			DB_URL = properties.getProperty("dburl");
			DB_USER = properties.getProperty("dbuser");
			DB_PASSWORD = properties.getProperty("dbpassword");
			if(DB_URL == null || DB_USER == null || DB_PASSWORD == null)
				throw new RuntimeException("Could not read DB connection info, check format of jdbc.properties ");
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Error registering MySQL JDBC Driver",e);
		}
	}
	
	public Connection getConnection() throws SQLException{
		Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		return connection;
	}
}
