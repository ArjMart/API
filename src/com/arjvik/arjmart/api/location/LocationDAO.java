package com.arjvik.arjmart.api.location;

import java.util.List;

import com.arjvik.arjmart.api.DatabaseException;


public interface LocationDAO {
	public Location getLocation(int ID) throws LocationNotFoundException, DatabaseException;
	
	public List<Location> getAllLocations() throws DatabaseException;
	
	public int addLocation(Location location) throws DatabaseException;
	
	public void updateLocation(int ID, Location location) throws LocationNotFoundException, DatabaseException;
	
	public void deleteLocation(int ID) throws LocationNotFoundException, DatabaseException;
}
