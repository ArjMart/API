package com.arjvik.arjmart.api.item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.inject.Inject;

import com.arjvik.arjmart.api.ConnectionFactory;
import com.arjvik.arjmart.api.DatabaseException;

public class JDBCItemPriceDAO implements ItemPriceDAO {
	
	private ConnectionFactory connectionFactory;
	
	@Inject
	public JDBCItemPriceDAO(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}


	@Override
	public ItemPrice getItemPrice(int ItemAttributeID) throws DatabaseException {
		try{
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("select Price from ItemPrice where ItemAttributeID = ?");
			statement.setInt(1, ItemAttributeID);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
				return new ItemPrice(-1);
			return new ItemPrice(resultSet.getDouble(1));
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void setItemPrice(int ItemAttributeID, ItemPrice itemPrice) throws ItemAttributeNotFoundException, DatabaseException {
		try {
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("insert into ItemPrice values (?,?) on duplicate key update Price = ?");
			statement.setInt(1, ItemAttributeID);
			statement.setDouble(2, itemPrice.getPrice());
			statement.setDouble(3, itemPrice.getPrice());
			statement.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new ItemAttributeNotFoundException(ItemAttributeID, e);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

}
