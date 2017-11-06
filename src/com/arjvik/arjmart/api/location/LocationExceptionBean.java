package com.arjvik.arjmart.api.location;

public class LocationExceptionBean {
	private String error;
	private int ID;
	
	public LocationExceptionBean() {
		
	}
	public LocationExceptionBean(int ID) {
		this.ID = ID;
	}
	public LocationExceptionBean(int ID, String error) {
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
		this.ID = ID;
	}
}
