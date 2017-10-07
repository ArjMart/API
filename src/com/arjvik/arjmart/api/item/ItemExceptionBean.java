package com.arjvik.arjmart.api.item;

public class ItemExceptionBean {
	private String error;
	private int SKU;
	
	public ItemExceptionBean() {
		
	}
	public ItemExceptionBean(int SKU) {
		this.SKU = SKU;
	}
	public ItemExceptionBean(int SKU, String error) {
		this.SKU = SKU;
		this.error = error;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getSKU() {
		return SKU;
	}
	public void setSKU(int sKU) {
		SKU = sKU;
	}
}
