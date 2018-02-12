package com.arjvik.arjmart.api.order;

public class OrderLineNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private int OrderID;
	private int OrderLineID;
	
	public OrderLineNotFoundException(int OrderID, int OrderLineID) {
		super(OrderID + "/" + OrderLineID);
		this.OrderID = OrderID;
		this.OrderLineID = OrderLineID;
	}

	public OrderLineNotFoundException(int OrderID, int OrderLineID, Throwable cause) {
		super(OrderID + "/" + OrderLineID, cause);
		this.OrderID = OrderID;
		this.OrderLineID = OrderLineID;
	}
	
	public int getOrderID() {
		return OrderID;
	}
	
	public int getOrderLineID() {
		return OrderLineID;
	}
}
