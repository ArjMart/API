package com.arjvik.arjmart.api.item;

public class ItemAttributeNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private int SKU;
	private int ID;
	
	public ItemAttributeNotFoundException() {
	}

	public ItemAttributeNotFoundException(int SKU, int ID) {
		super(Integer.toString(ID));
		this.SKU = SKU;
		this.ID = ID;
	}

	public ItemAttributeNotFoundException(Throwable cause) {
		super(cause);
	}

	public ItemAttributeNotFoundException(int SKU, int ID, Throwable cause) {
		super(Integer.toString(ID), cause);
		this.SKU = SKU;
		this.ID = ID;
	}

	public ItemAttributeNotFoundException(int SKU, int ID, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(Integer.toString(ID), cause, enableSuppression, writableStackTrace);
		this.SKU = SKU;
		this.ID = ID;
	}
	
	public int getSKU() {
		return SKU;
	}

	public int getID() {
		return ID;
	}
}
