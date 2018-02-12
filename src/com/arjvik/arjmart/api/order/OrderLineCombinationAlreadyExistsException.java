package com.arjvik.arjmart.api.order;

public class OrderLineCombinationAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;
	private int OrderID;
	private int SKU;
	private int itemAttributeID;

	public OrderLineCombinationAlreadyExistsException(int OrderID, int SKU, int itemAttributeID) {
		super(OrderID + ", " + SKU + "/" + itemAttributeID);
		this.OrderID = OrderID;
		this.SKU = SKU;
		this.itemAttributeID = itemAttributeID;
	}

	public OrderLineCombinationAlreadyExistsException(int OrderID, int SKU, int itemAttributeID, Throwable cause) {
		super(OrderID + ", " + SKU + "/" + itemAttributeID, cause);
		this.OrderID = OrderID;
		this.SKU = SKU;
		this.itemAttributeID = itemAttributeID;
	}

	public int getOrderID() {
		return OrderID;
	}
	
	public int getSKU() {
		return SKU;
	}
	
	public int getItemAttributeID() {
		return itemAttributeID;
	}
}
