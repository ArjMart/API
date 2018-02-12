package com.arjvik.arjmart.api.user;

public class UserAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;
	private int ID;
	
	public UserAlreadyExistsException(int ID) {
		super(Integer.toString(ID));
		this.ID = ID;
	}

	public UserAlreadyExistsException(int ID, Throwable cause) {
		super(Integer.toString(ID), cause);
		this.ID = ID;
	}
	
	public int getID() {
		return ID;
	}
}
