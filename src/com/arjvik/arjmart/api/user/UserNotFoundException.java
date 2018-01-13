package com.arjvik.arjmart.api.user;

public class UserNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private int ID;
	
	public UserNotFoundException() {
	}

	public UserNotFoundException(int ID) {
		super(Integer.toString(ID));
		this.ID = ID;
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}

	public UserNotFoundException(int ID, Throwable cause) {
		super(Integer.toString(ID), cause);
		this.ID = ID;
	}

	public UserNotFoundException(int ID, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(Integer.toString(ID), cause, enableSuppression, writableStackTrace);
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}
}
