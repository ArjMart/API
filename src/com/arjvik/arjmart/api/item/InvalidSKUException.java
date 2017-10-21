package com.arjvik.arjmart.api.item;

public class InvalidSKUException extends Exception {
	private static final long serialVersionUID = 1L;
	private int SKU;
	
	public InvalidSKUException() {
	}

	public InvalidSKUException(int SKU) {
		super(Integer.toString(SKU));
		this.SKU = SKU;
	}

	public InvalidSKUException(Throwable cause) {
		super(cause);
	}

	public InvalidSKUException(int SKU, Throwable cause) {
		super(Integer.toString(SKU), cause);
		this.SKU = SKU;
	}

	public InvalidSKUException(int SKU, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(Integer.toString(SKU), cause, enableSuppression, writableStackTrace);
		this.SKU = SKU;
	}

	public int getSKU() {
		return SKU;
	}
}
