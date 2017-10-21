package com.arjvik.arjmart.api.item;

import java.util.List;

import com.arjvik.arjmart.api.DatabaseException;

public interface ItemAttributeDAO {
	public ItemAttribute getItemAttribute(int ID) throws ItemAttributeNotFoundException, DatabaseException;

	public List<ItemAttribute> getItemAttributeBySKU(int SKU) throws ItemNotFoundException, DatabaseException;
	
	public int addItemAttribute(ItemAttribute itemAttribute) throws ItemNotFoundException, DatabaseException;
	
	public void updateItemAttribute(int ID, ItemAttribute itemAttribute) throws ItemAttributeNotFoundException, ItemNotFoundException, DatabaseException;
	
	public void deleteItemAttribute(int ID) throws ItemAttributeNotFoundException, DatabaseException;
}
