package com.arjvik.arjmart.api.order.checkout;

public class InvalidOrderStateExceptionBean {
	private String error;
	private int orderID;
	private String status;
	
	public InvalidOrderStateExceptionBean() {
		
	}
	public InvalidOrderStateExceptionBean(int orderID, String status) {
		this.orderID = orderID;
		this.setStatus(status);
	}
	public InvalidOrderStateExceptionBean(int orderID, String status, String error) {
		this.orderID = orderID;
		this.setStatus(status);
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
