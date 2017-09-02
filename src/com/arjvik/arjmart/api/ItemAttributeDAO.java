package com.arjvik.arjmart.api;

import java.util.List;

public interface ItemAttributeDAO {
	public ItemAttribute getItemAttribute(int ID) throws DatabaseException;

	public List<ItemAttribute> getItemAttributeBySKU(int SKU) throws DatabaseException;
	
	public boolean addItemAttribute(ItemAttribute itemAttribute) throws DatabaseException;
	
	public boolean updateItemAttribute(int ID, ItemAttribute itemAttribute) throws DatabaseException;
	
	public boolean deleteItemAttribute(int ID, ItemAttribute itemAttribute) throws DatabaseException;
}
