package com.arjvik.arjmart.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class JDBCItemAttributeDAO implements ItemAttributeDAO {

	private ConnectionFactory connectionFactory;
	
	@Inject
	public JDBCItemAttributeDAO(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	@Override
	public ItemAttribute getItemAttribute(int ID) throws DatabaseException {
		try{
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from ItemAttributeMaster where ItemAttributeID=?");
			statement.setInt(1, ID);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next()){
				return null;
			}
			int SKU = resultSet.getInt("SKU");
			String color = resultSet.getString("Color");
			String size = resultSet.getString("Size");
			return new ItemAttribute(ID, SKU, color, size);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public List<ItemAttribute> getItemAttributeBySKU(int SKU) throws DatabaseException {
		try {
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from ItemAttributeMaster where SKU=?");
			statement.setInt(1, SKU);
			ResultSet resultSet = statement.executeQuery();
			List<ItemAttribute> attributes = new ArrayList<>();
			while (resultSet.next()) {
				int ID = resultSet.getInt("ItemAttributeID");
				String color = resultSet.getString("Color");
				String size = resultSet.getString("Size");
				attributes.add(new ItemAttribute(ID, SKU, color, size));
			}
			return attributes;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean addItemAttribute(ItemAttribute itemAttribute) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateItemAttribute(int ID, ItemAttribute itemAttribute) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteItemAttribute(int ID, ItemAttribute itemAttribute) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

}
