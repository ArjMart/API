package com.arjvik.arjmart.api.item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.json.JSONTokener;

import com.arjvik.arjmart.api.ConnectionFactory;
import com.arjvik.arjmart.api.DatabaseException;
import com.arjvik.arjmart.api.JSONObject;

public class JDBCItemAttributeDAO implements ItemAttributeDAO {

	private ConnectionFactory connectionFactory;
	private ItemDAO itemDAO;
	
	@Inject
	public JDBCItemAttributeDAO(ConnectionFactory connectionFactory, ItemDAO itemDAO) {
		this.connectionFactory = connectionFactory;
		this.itemDAO = itemDAO;
	}
	
	@Override
	public ItemAttribute getItemAttribute(int ID) throws ItemAttributeNotFoundException, DatabaseException {
		try{
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from ItemAttributeMaster where ItemAttributeID=?");
			statement.setInt(1, ID);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
				throw new ItemAttributeNotFoundException(ID);
			int SKU = resultSet.getInt("SKU");
			String color = resultSet.getString("Color");
			String size = resultSet.getString("Size");
			return new ItemAttribute(ID, SKU, color, size);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public List<ItemAttribute> getItemAttributeBySKU(int SKU) throws ItemNotFoundException, DatabaseException  {
		itemDAO.getItem(SKU);
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
	public int addItemAttribute(ItemAttribute itemAttribute) throws ItemNotFoundException, DatabaseException {
		try{
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("insert into ItemAttributeMaster (SKU, Color, Size) values (?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, itemAttribute.getSKU());
			statement.setString(2, itemAttribute.getColor());
			statement.setString(3, itemAttribute.getSize());
			statement.executeUpdate();
			ResultSet keys = statement.getGeneratedKeys();
			keys.next();
			return keys.getInt(1);
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new ItemNotFoundException(itemAttribute.getSKU(),e);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void updateItemAttribute(int ID, ItemAttribute itemAttribute) throws ItemAttributeNotFoundException, ItemNotFoundException, DatabaseException {
		getItemAttribute(ID);
		try {
			Connection connection = connectionFactory.getConnection();
			//PreparedStatement statement = connection.prepareStatement("update ItemAttributeMaster set ItemAttributeID = ?, SKU = ?, Color = ?, Size = ? where ItemAttributeID = ?");
			PreparedStatement statement = connection.prepareStatement("update ItemAttributeMaster set SKU = ?, Color = ?, Size = ? where ItemAttributeID = ?");
			//statement.setInt(1, itemAttribute.getID());
			statement.setInt(1, itemAttribute.getSKU());
			if(itemAttribute.getColor()!=null)
				statement.setString(2, itemAttribute.getColor());
			else
				statement.setNull(2, Types.VARCHAR);
			if(itemAttribute.getSize()!=null)
				statement.setString(3, itemAttribute.getSize());
			else
				statement.setNull(3, Types.VARCHAR);
			statement.setInt(4, ID);
			statement.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new ItemNotFoundException(itemAttribute.getSKU(),e);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void deleteItemAttribute(int ID) throws ItemAttributeNotFoundException, DatabaseException {
		try{
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("delete from ItemAttributeMaster where ItemAttributeID = ?");
			statement.setInt(1, ID);
			if(!(statement.executeUpdate()>0))
				throw new ItemAttributeNotFoundException(ID);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

}
