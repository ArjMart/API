package com.arjvik.arjmart.api.user;

public class UserExceptionBean {
	private String error;
	private int userID;
	
	public UserExceptionBean() {
		
	}
	public UserExceptionBean(int userID) {
		this.userID = userID;
	}
	public UserExceptionBean(int userID, String error) {
		this.userID = userID;
		this.error = error;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
}
