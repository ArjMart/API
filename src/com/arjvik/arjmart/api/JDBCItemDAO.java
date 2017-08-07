package com.arjvik.arjmart.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.json.JSONArray;

public class JDBCItemDAO implements ItemDAO {
	
	private static JDBCItemDAO instance;
	
	private static final String DB_URL = "jdbc:mysql://db1.clwnpjvhytsb.us-west-2.rds.amazonaws.com:3306/arjmart",
			DB_USER = "root";

	private static final int MAX_RECORDS = 100;
	private transient String DB_PW;
	private Logger logger;
	
	private JDBCItemDAO(ServletContext servletContext) {
		logger = Logger.getLogger(this.getClass().getName());
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Error registering JDBC driver", e);
		}
		try{
			if(System.getProperties().containsKey("com.arjvik.arjmart.api.DB_PW")){
				DB_PW = System.getProperty("com.arjvik.arjmart.api.DB_PW");
			}else{
				String path = servletContext.getRealPath("/WEB-INF/passwords.txt");
				File file = new File(path);
				Scanner scanner = new Scanner(file);
				DB_PW = scanner.nextLine();
				scanner.close();
				System.setProperty("com.arjvik.arjmart.api.DB_PW", DB_PW);
			}
		} catch(FileNotFoundException e){
			DB_PW=null;
			logger.log(Level.SEVERE, "Error reading DB Password", e);
		}
	}
	
	public static synchronized JDBCItemDAO getInstance(ServletContext servletContext){
		if(instance == null)
			instance = new JDBCItemDAO(servletContext);
		return instance;
	}

	@Override
	public Item getItem(int SKU) throws DatabaseException{
		try{
			Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from ItemMaster where SKU=?");
			statement.setInt(1, SKU);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next()){
				return null;
			}	
			return new Item(SKU, resultSet.getString("ItemName"), resultSet.getString("ItemDescription"), resultSet.getString("ItemThumbnails"));
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public List<Item> getAllItems(int limit) throws DatabaseException {
		try{
			List<Item> items = new ArrayList<>();
			Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from ItemMaster limit ?");
			if(limit!=-1){
				statement.setInt(1, Math.min(Math.max(limit, 0), MAX_RECORDS));
			}else{
				statement.setInt(1, MAX_RECORDS);
			}
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				items.add(new Item(resultSet.getInt("SKU"), resultSet.getString("ItemName"), resultSet.getString("ItemDescription"), resultSet.getString("ItemThumbnails")));
			}
			return items;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public List<Item> searchItems(int limit, String query) throws DatabaseException {
		try{
			Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from ItemMaster where ItemName like ? escape '|' limit ?");
			List<Item> items = new ArrayList<>();
			//String escapedQuery="%"+query.replace("%", "|%").replace("_", "|_").replace(' ', '%')+"%";
			statement.setString(1, query);
			if(limit!=-1){
				statement.setInt(2, Math.min(Math.max(limit, 0), MAX_RECORDS));
			}else{
				statement.setInt(2, MAX_RECORDS);
			}
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				items.add(new Item(resultSet.getInt("SKU"), resultSet.getString("ItemName"), resultSet.getString("ItemDescription"), resultSet.getString("ItemThumbnails")));
			}
			return items;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void updateItem(int SKU, Item item) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addItem(Item item) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteItem(int SKU) {
		// TODO Auto-generated method stub
		
	}
	
	private Connection getConnection() throws SQLException{
		return ConnectionFactory.getConnection(DB_PW);
	}
}
