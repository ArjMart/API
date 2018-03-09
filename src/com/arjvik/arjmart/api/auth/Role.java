package com.arjvik.arjmart.api.auth;

public final class Role {
	public static final String NO_ROLE = "NO_ROLE";
	public static final String SUPER_ADMIN = "SuperAdmin";
	public static final String ITEM_MANAGER = "ItemManager";
	public static final String PRICE_MANAGER = "PriceManager";
	public static final String INVENTORY_MANAGER = "InventoryManager";
	public static final String ORDER_MANAGER = "OrderManager";
	public static final String USER_MANAGER = "UserManager";
	
	private static final String OWNER_OF_ORDER = "Owner of order %d";
	private static final String USER_ID_N = "User %d";
	
	private Role(){
		
	}

	public static String ownerOfOrder(int orderID) {
		return String.format(OWNER_OF_ORDER, orderID);
	}
	
	public static String user(int userID) {
		return String.format(USER_ID_N, userID);
	}
	
}
