package com.arjvik.arjmart.api.order;

public class OrderNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private int ID;
	
	public OrderNotFoundException(int ID) {
		super(Integer.toString(ID));
		this.ID = ID;
	}

	public OrderNotFoundException(int ID, Throwable cause) {
		super(Integer.toString(ID), cause);
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}
}
