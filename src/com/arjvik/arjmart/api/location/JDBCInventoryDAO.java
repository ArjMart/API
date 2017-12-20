package com.arjvik.arjmart.api.location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.arjvik.arjmart.api.ConnectionFactory;
import com.arjvik.arjmart.api.DatabaseException;
import com.arjvik.arjmart.api.item.ItemAttributeNotFoundException;

public class JDBCInventoryDAO implements InventoryDAO {

	private ConnectionFactory connectionFactory;
	
	@Inject
	public JDBCInventoryDAO(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}


	@Override
	public Inventory getInventory(int locationID, int SKU, int itemAttributeID) throws DatabaseException {
		try(Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select * from Inventory where LocationID = ? and SKU = ? and ItemAttributeID = ?");
			statement.setInt(1, locationID);
			statement.setInt(2, SKU);
			statement.setInt(3, itemAttributeID);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
				return new Inventory(locationID, SKU, itemAttributeID, 0);
			return new Inventory(locationID, SKU, itemAttributeID, resultSet.getInt("InventoryAmount"));
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public List<Inventory> getAllInventory(int locationID) throws DatabaseException {
		try(Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select * from Inventory where LocationID = ? and InventoryAmount>0");
			statement.setInt(1, locationID);
			ResultSet resultSet = statement.executeQuery();
			List<Inventory> inventories = new ArrayList<Inventory>();
			while(resultSet.next())
				inventories.add(new Inventory(locationID, resultSet.getInt("SKU"), resultSet.getInt("ItemAttributeID"), resultSet.getInt("InventoryAmount")));
			return inventories;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public List<Location> getAllLocations(int SKU, int itemAttributeID) throws DatabaseException {
		try(Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select LocationMaster.LocationID, LocationMaster.Address from Inventory left join LocationMaster"
																	+ "on Inventory.LocationID=LocationMaster.LocationID where Inventory.SKU = ? and Inventory.ItemAttributeID = ? and Inventory.InventoryAmount>0");
			statement.setInt(1, SKU);
			statement.setInt(2, itemAttributeID);
			ResultSet resultSet = statement.executeQuery();
			List<Location> locations = new ArrayList<Location>();
			while(resultSet.next())
				locations.add(new Location(resultSet.getInt("LocationID"), resultSet.getString("Address")));
			return locations;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void setInventory(Inventory inventory) throws LocationNotFoundException, ItemAttributeNotFoundException, DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("insert into Inventory (SKU, ItemAttributeId, LocationID, InventoryAmount) values (?,?,?,?) on duplicate key update InventoryAmount = ?");
			statement.setInt(1, inventory.getSKU());
			statement.setInt(2, inventory.getItemAttributeID());
			statement.setInt(3, inventory.getLocationID());
			statement.setDouble(4, inventory.getQuantity());
			statement.setDouble(5, inventory.getQuantity());
			statement.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new ItemAttributeNotFoundException(inventory.getSKU(), inventory.getItemAttributeID(), e);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

}
