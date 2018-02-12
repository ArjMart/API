package com.arjvik.arjmart.api.order.checkout;

public class InvalidOrderStateException extends Exception {
	private static final long serialVersionUID = 1L;
	private int ID;
	private String status;
	
	public InvalidOrderStateException(int ID, String status) {
		super(Integer.toString(ID));
		this.ID = ID;
		this.status = status;
	}

	public InvalidOrderStateException(int ID, String status, Throwable cause) {
		super(Integer.toString(ID), cause);
		this.ID = ID;
		this.status = status;
	}

	public int getID() {
		return ID;
	}

	public String getStatus() {
		return status;
	}
}
