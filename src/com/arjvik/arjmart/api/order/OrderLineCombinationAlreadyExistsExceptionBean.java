package com.arjvik.arjmart.api.order;

public class OrderLineCombinationAlreadyExistsExceptionBean {
	private String error;
	private int orderID;
	private int SKU;
	private int itemAttributeID;
	
	public OrderLineCombinationAlreadyExistsExceptionBean() {
		
	}
	public OrderLineCombinationAlreadyExistsExceptionBean(int orderID, int SKU, int itemAttributeID) {
		this.orderID = orderID;
		this.SKU = SKU;
		this.itemAttributeID = itemAttributeID;
	}
	public OrderLineCombinationAlreadyExistsExceptionBean(int orderID, int SKU, int itemAttributeID, String error) {
		this.orderID = orderID;
		this.SKU = SKU;
		this.itemAttributeID = itemAttributeID;
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
	public int getSKU() {
		return SKU;
	}
	public void setSKU(int SKU) {
		this.SKU = SKU;
	}
	public int getItemAttributeID() {
		return itemAttributeID;
	}
	public void setItemAttributeID(int itemAttributeID) {
		this.itemAttributeID = itemAttributeID;
	}
}
