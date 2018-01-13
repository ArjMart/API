package com.arjvik.arjmart.api.order;

public class OrderNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private int ID;
	
	public OrderNotFoundException() {
	}

	public OrderNotFoundException(int ID) {
		super(Integer.toString(ID));
		this.ID = ID;
	}

	public OrderNotFoundException(Throwable cause) {
		super(cause);
	}

	public OrderNotFoundException(int ID, Throwable cause) {
		super(Integer.toString(ID), cause);
		this.ID = ID;
	}

	public OrderNotFoundException(int ID, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(Integer.toString(ID), cause, enableSuppression, writableStackTrace);
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}
}
