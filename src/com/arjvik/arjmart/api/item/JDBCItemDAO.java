package com.arjvik.arjmart.api.item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.arjvik.arjmart.api.ConnectionFactory;
import com.arjvik.arjmart.api.DatabaseException;

public class JDBCItemDAO implements ItemDAO {
	
	private ConnectionFactory connectionFactory;
	
	@Inject
	public JDBCItemDAO(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public Item getItem(int SKU) throws ItemNotFoundException, DatabaseException{
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select * from ItemMaster where SKU=?");
			statement.setInt(1, SKU);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
				throw new ItemNotFoundException(SKU);
			return new Item(SKU, resultSet.getString("ItemName"), resultSet.getString("ItemDescription"), resultSet.getString("ItemThumbnails"));
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public List<Item> getAllItems(int start, int limit) throws DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			List<Item> items = new ArrayList<>();
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
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select * from ItemMaster where ItemName like ? escape '|' or ItemDescription like ? escape '|' order by SKU limit ? offset ?");
			List<Item> items = new ArrayList<>();
			String escapedQuery="%"+query.replace("%", "|%").replace("_", "|_").replace(' ', '%')+"%";
			statement.setString(1, escapedQuery);
			statement.setString(2, escapedQuery);
			statement.setInt(3, limit);
			statement.setInt(4, start);
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
	public ItemCount getItemCount() throws DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select count(*) from ItemMaster");
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			return new ItemCount(resultSet.getInt(1));
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public ItemCount getItemSearchCount(String query) throws DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select count(*) from ItemMaster where ItemName like ? escape '|' or ItemDescription like ? escape '|'");
			String escapedQuery="%"+query.replace("%", "|%").replace("_", "|_").replace(' ', '%')+"%";
			statement.setString(1, escapedQuery);
			statement.setString(2, escapedQuery);
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			return new ItemCount(resultSet.getInt(1));
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public void addItem(Item item) throws ItemAlreadyExistsException, DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
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
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new ItemAlreadyExistsException(item.getSKU(),e);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		
	}

	@Override
	public void updateItem(int SKU, Item item) throws ItemNotFoundException, DatabaseException  {
		getItem(SKU);
		try (Connection connection = connectionFactory.getConnection()) {
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
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void deleteItem(int SKU) throws ItemNotFoundException, DatabaseException  {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("delete from ItemMaster where SKU=?");
			statement.setInt(1, SKU);
			if(!(statement.executeUpdate()>0))
				throw new ItemNotFoundException(SKU);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
}
