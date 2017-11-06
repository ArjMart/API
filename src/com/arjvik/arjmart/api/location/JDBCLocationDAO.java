package com.arjvik.arjmart.api.location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.arjvik.arjmart.api.ConnectionFactory;
import com.arjvik.arjmart.api.DatabaseException;

public class JDBCLocationDAO implements LocationDAO {

	private ConnectionFactory connectionFactory;
	
	@Inject
	public JDBCLocationDAO(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	
	@Override
	public Location getLocation(int ID) throws LocationNotFoundException, DatabaseException {
		try {
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from LocationMaster where LocationID=?");
			statement.setInt(1, ID);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
				throw new LocationNotFoundException(ID);
			return new Location(ID,resultSet.getString("Address"));
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public List<Location> getAllLocations() throws DatabaseException {
		try {
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from LocationMaster limit ?");
			ResultSet resultSet = statement.executeQuery();
			List<Location> locations = new ArrayList<>();
			while (resultSet.next()) {
				locations.add(new Location(resultSet.getInt("LocationID"), resultSet.getString("Address")));
			}
			return locations;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public int addLocation(Location location) throws DatabaseException {
		try {
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("insert into LocationMaster (Address) values (?)", Statement.RETURN_GENERATED_KEYS);
			if(location.getAddress()!=null)
				statement.setString(1, location.getAddress());
			else
				statement.setNull(1, Types.VARCHAR);
			statement.executeUpdate();
			ResultSet keys = statement.getGeneratedKeys();
			keys.next();
			return keys.getInt(1);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void updateLocation(int ID, Location location) throws LocationNotFoundException, DatabaseException {
		getLocation(ID);
		try {
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("update LocationMaster set LocationID=?, Address=? where LocationID=?");
			statement.setInt(1, location.getID());
			if(location.getAddress()!=null)
				statement.setString(2, location.getAddress());
			else
				statement.setNull(2, Types.VARCHAR);
			statement.setInt(3, ID);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void deleteLocation(int ID) throws LocationNotFoundException, DatabaseException {
		try {
			Connection connection = connectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement("delete from LocationMaster where LocationID=?");
			statement.setInt(1, ID);
			if(!(statement.executeUpdate()>0))
				throw new LocationNotFoundException(ID);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

}
