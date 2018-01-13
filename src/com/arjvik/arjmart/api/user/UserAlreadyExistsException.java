package com.arjvik.arjmart.api.user;

public class UserAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;
	private int ID;
	
	public UserAlreadyExistsException() {
	}

	public UserAlreadyExistsException(int ID) {
		super(Integer.toString(ID));
		this.ID = ID;
	}

	public UserAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public UserAlreadyExistsException(int ID, Throwable cause) {
		super(Integer.toString(ID), cause);
		this.ID = ID;
	}

	public UserAlreadyExistsException(int ID, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(Integer.toString(ID), cause, enableSuppression, writableStackTrace);
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}
}
