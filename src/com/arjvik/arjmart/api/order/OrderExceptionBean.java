package com.arjvik.arjmart.api.order;

public class OrderExceptionBean {
	private String error;
	private int orderID;
	
	public OrderExceptionBean() {
		
	}
	public OrderExceptionBean(int orderID) {
		this.orderID = orderID;
	}
	public OrderExceptionBean(int orderID, String error) {
		this.orderID = orderID;
		this.error = error;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getOrderID() {
		return orderID;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
}
