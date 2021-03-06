package com.arjvik.arjmart.api.location;

import java.util.List;

import com.arjvik.arjmart.api.DatabaseException;
import com.arjvik.arjmart.api.item.ItemAttributeNotFoundException;
import com.arjvik.arjmart.api.jms.PipelineException;

public interface InventoryDAO {

	public Inventory getInventory(int locationID, int SKU, int itemAttributeID) throws DatabaseException;

	public List<Inventory> getAllInventory(int locationID) throws DatabaseException;

	public List<Location> getAllLocations(int SKU, int itemAttributeID) throws DatabaseException;

	public void setInventory(Inventory inventory) throws LocationNotFoundException, ItemAttributeNotFoundException, PipelineException, DatabaseException;

	public Inventory addInventory(Inventory inventory) throws LocationNotFoundException, ItemAttributeNotFoundException, PipelineException, DatabaseException;

}
