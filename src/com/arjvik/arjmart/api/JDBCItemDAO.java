package com.arjvik.arjmart.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCItemDAO implements ItemDAO {
	
	private static JDBCItemDAO instance;

	private static final int MAX_RECORDS = 100;
	private Logger logger;
	
	private JDBCItemDAO() {
		logger = Logger.getLogger(this.getClass().getName());
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Error registering JDBC driver", e);
		}
	}
	
	public static synchronized JDBCItemDAO getInstance(){
		if(instance == null)
			instance = new JDBCItemDAO();
		return instance;
	}

	@Override
	public Item getItem(int SKU) throws DatabaseException{
		try{
			Connection connection = ConnectionFactory.getConnection();
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
			Connection connection = ConnectionFactory.getConnection();
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
			Connection connection = ConnectionFactory.getConnection();
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
}
