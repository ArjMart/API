package com.arjvik.arjmart.api;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.inject.Inject;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import de.jupf.staticlog.Log;

public class ConnectionFactory {
			
	private static int counter = 1;
	private HikariDataSource dataSource;

	@Inject
	public ConnectionFactory() {
		try {
			Log.info("Initializing ConnectionFactory");
			Class.forName("com.mysql.jdbc.Driver");
			Properties properties = new Properties();
			InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("jdbc.properties");
			if (in == null)
				throw new RuntimeException("Could not read DB connection info, check if jdbc.properties exists in classpath");
			properties.load(in);
			String DB_URL = properties.getProperty("dburl");
			String DB_USER = properties.getProperty("dbuser");
			String DB_PASSWORD = properties.getProperty("dbpassword");
			if (DB_URL == null || DB_USER == null || DB_PASSWORD == null)
				throw new RuntimeException("Could not read DB connection info, check format of jdbc.properties ");
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(DB_URL);
			config.setUsername(DB_USER);
			config.setPassword(DB_PASSWORD);
			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			dataSource = new HikariDataSource(config);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Error registering MySQL JDBC Driver", e);
		}
	}

	public Connection getConnection() throws SQLException {
		long start = System.currentTimeMillis();
		Connection connection = dataSource.getConnection();
		Log.info(String.format("Getting connection %d takes %.3fs%n", counter++, (System.currentTimeMillis() - start) / 1000d));
		return connection;
	}
}
