package com.arjvik.arjmart.api.item;

import java.util.List;

import com.arjvik.arjmart.api.DatabaseException;

public interface ItemDAO {
	public Item getItem(int SKU) throws ItemNotFoundException, DatabaseException;
	
	public List<Item> getAllItems(int start, int limit) throws DatabaseException;
	
	public List<Item> searchItems(int start, int limit, String escapedQuery) throws DatabaseException;
	
	public ItemCount getItemCount() throws DatabaseException;
	
	public ItemCount getItemSearchCount(String query) throws DatabaseException;
	
	public void addItem(Item item) throws ItemAlreadyExistsException, DatabaseException;
	
	public void updateItem(int SKU, Item item) throws ItemNotFoundException, DatabaseException;
	
	public void deleteItem(int SKU) throws ItemNotFoundException, DatabaseException;

}
