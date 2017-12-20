package com.arjvik.arjmart.api.item;

import com.arjvik.arjmart.api.DatabaseException;

public interface ItemPriceDAO {
	public ItemPrice getItemPrice(int SKU, int ItemAttributeID) throws DatabaseException;
	
	public void setItemPrice(int SKU, int ItemAttributeID, ItemPrice itemPrice) throws ItemAttributeNotFoundException, DatabaseException;
}
