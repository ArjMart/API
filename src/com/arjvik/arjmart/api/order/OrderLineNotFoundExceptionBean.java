package com.arjvik.arjmart.api.order;

public class OrderLineNotFoundExceptionBean {
	private String error;
	private int orderID;
	private int orderLineID;
	
	public OrderLineNotFoundExceptionBean() {
		
	}
	public OrderLineNotFoundExceptionBean(int orderID, int OrderLineID) {
		this.orderID = orderID;
		this.orderLineID = OrderLineID;
	}
	public OrderLineNotFoundExceptionBean(int orderID, int OrderLineID, String error) {
		this.orderID = orderID;
		this.orderLineID = OrderLineID;
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
	public int getOrderLineID() {
		return orderLineID;
	}
	public void setOrderLineID(int orderLineID) {
		this.orderLineID = orderLineID;
	}
}
