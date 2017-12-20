package com.arjvik.arjmart.api.item;

public class ItemAttributeExceptionBean {
	private String error;
	private int SKU;
	private int ID;
	
	public ItemAttributeExceptionBean() {
		
	}
	public ItemAttributeExceptionBean(int SKU, int ID) {
		this.SKU = SKU;
		this.ID = ID;
	}
	public ItemAttributeExceptionBean(int SKU, int ID, String error) {
		this.SKU = SKU;
		this.ID = ID;
		this.error = error;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getSKU() {
		return SKU;
	}
	public void setSKU(int SKU) {
		this.SKU = SKU;
	}
	public int getID() {
		return ID;
	}
	public void setID(int ID) {
		this.ID = ID;
	}
}
