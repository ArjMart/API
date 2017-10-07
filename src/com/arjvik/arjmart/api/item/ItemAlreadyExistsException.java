package com.arjvik.arjmart.api.item;

public class ItemAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;
	private int SKU;
	
	public ItemAlreadyExistsException() {
	}

	public ItemAlreadyExistsException(int SKU) {
		super(Integer.toString(SKU));
		this.SKU = SKU;
	}

	public ItemAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public ItemAlreadyExistsException(int SKU, Throwable cause) {
		super(Integer.toString(SKU), cause);
		this.SKU = SKU;
	}

	public ItemAlreadyExistsException(int SKU, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(Integer.toString(SKU), cause, enableSuppression, writableStackTrace);
		this.SKU = SKU;
	}

	public int getSKU() {
		return SKU;
	}
}
