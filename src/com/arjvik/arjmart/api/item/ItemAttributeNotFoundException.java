package com.arjvik.arjmart.api.item;

public class ItemAttributeNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private int ID;
	
	public ItemAttributeNotFoundException() {
	}

	public ItemAttributeNotFoundException(int ID) {
		super(Integer.toString(ID));
		this.ID = ID;
	}

	public ItemAttributeNotFoundException(Throwable cause) {
		super(cause);
	}

	public ItemAttributeNotFoundException(int ID, Throwable cause) {
		super(Integer.toString(ID), cause);
		this.ID = ID;
	}

	public ItemAttributeNotFoundException(int ID, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(Integer.toString(ID), cause, enableSuppression, writableStackTrace);
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}
}
