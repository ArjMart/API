package com.arjvik.arjmart.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	private static final String DB_URL = "jdbc:mysql://db1.clwnpjvhytsb.us-west-2.rds.amazonaws.com:3306/arjmart",
			DB_USER = "root";
	
	public static Connection getConnection(String databasePassword) throws SQLException{
		Connection connection = DriverManager.getConnection(DB_URL, DB_USER, databasePassword);
		return connection;
	}
}
