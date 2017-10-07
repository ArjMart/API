package com.arjvik.arjmart.api.item;

public class ItemAttributeExceptionBean {
	private String error;
	private int ID;
	
	public ItemAttributeExceptionBean() {
		
	}
	public ItemAttributeExceptionBean(int ID) {
		this.ID = ID;
	}
	public ItemAttributeExceptionBean(int ID, String error) {
		this.ID = ID;
		this.error = error;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getID() {
		return ID;
	}
	public void setID(int ID) {
		ID = ID;
	}
}
