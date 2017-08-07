package com.arjvik.arjmart.api;

import java.util.List;

public interface ItemDAO {
	public Item getItem(int SKU) throws DatabaseException;
	
	public List<Item> getAllItems(int limit) throws DatabaseException;
	
	public List<Item> searchItems(int limit, String escapedQuery) throws DatabaseException;
	
	public void updateItem(int SKU, Item item) throws DatabaseException;
	
	public void addItem(Item item) throws DatabaseException;
	
	public void deleteItem(int SKU) throws DatabaseException;
}
