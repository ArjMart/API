package com.arjvik.arjmart.api.dao;

import java.util.List;

import com.arjvik.arjmart.api.domain.Item;

public interface ItemDAO {
	public Item getItem(int SKU) throws DatabaseException;
	
	public List<Item> getAllItems(int start, int limit) throws DatabaseException;
	
	public List<Item> searchItems(int start, int limit, String escapedQuery) throws DatabaseException;
	
	public boolean updateItem(int SKU, Item item) throws DatabaseException;
	
	public boolean addItem(Item item) throws DatabaseException;
	
	public boolean deleteItem(int SKU) throws DatabaseException;
}
