package com.arjvik.arjmart.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class JDBCItemDAO implements ItemDAO {
	
	private ConnectionFactory connectionFactory;
	
	@Inject
	public JDBCItemDAO(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public Item getItem(int SKU) throws DatabaseException{
		try{
			Connection connection = connectionFactory.getConnection();
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
	public List<Item> getAllItems(int start, int limit) throws DatabaseException {
		try{
			List<Item> items = new ArrayList<>();
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from ItemMaster order by SKU limit ? offset ?");
			statement.setInt(1, limit);
			statement.setInt(2, start);
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
	public List<Item> searchItems(int start, int limit, String query) throws DatabaseException {
		try{
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from ItemMaster where ItemName like ? escape '|' limit ? offset ?");
			List<Item> items = new ArrayList<>();
			String escapedQuery="%"+query.replace("%", "|%").replace("_", "|_").replace(' ', '%')+"%";
			statement.setString(1, escapedQuery);
			statement.setInt(2, limit);
			statement.setInt(3, start);
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
	public boolean addItem(Item item) throws DatabaseException {
		try {
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("insert into ItemMaster (SKU, ItemName, ItemDescription, ItemThumbnails) values (?, ?, ?, ?)");
			statement.setInt(1, item.getSKU());
			if(item.getName()!=null)
				statement.setString(2, item.getName());
			else
				statement.setNull(2, Types.VARCHAR);
			if(item.getDescription()!=null)
				statement.setString(3, item.getDescription());
			else
				statement.setNull(3, Types.VARCHAR);
			if(item.getThumbnail()!=null)
				statement.setString(4, item.getThumbnail());
			else
				statement.setNull(4, Types.VARCHAR);
			statement.executeUpdate();
			return true;
		} catch (SQLIntegrityConstraintViolationException e) {
			//updateItem(item.getSKU(), item);
			return false;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		
	}

	@Override
	public boolean updateItem(int SKU, Item item) throws DatabaseException  {
		try {
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("update ItemMaster set SKU = ?, ItemName = ?, ItemDescription = ?, ItemThumbnails = ? where SKU = ?");
			statement.setInt(1, item.getSKU());
			if(item.getName()!=null)
				statement.setString(2, item.getName());
			else
				statement.setNull(2, Types.VARCHAR);
			if(item.getDescription()!=null)
				statement.setString(3, item.getDescription());
			else
				statement.setNull(3, Types.VARCHAR);
			if(item.getThumbnail()!=null)
				statement.setString(4, item.getThumbnail());
			else
				statement.setNull(4, Types.VARCHAR);
			statement.setInt(5, SKU);
			return statement.executeUpdate()>0;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean deleteItem(int SKU) throws DatabaseException  {
		try{
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("delete from ItemMaster where SKU=?");
			statement.setInt(1, SKU);
			return statement.executeUpdate()>0;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
}
