package com.arjvik.arjmart.api.order.checkout;

public class PaymentExceptionBean {
	private String error;
	private double price;
	private String creditCard;
	
	public PaymentExceptionBean() {
		
	}
	public PaymentExceptionBean(double price, String creditCard) {
		this.price = price;
		this.setCreditCard(creditCard);
	}
	public PaymentExceptionBean(double price, String creditCard, String error) {
		this.price = price;
		this.setCreditCard(creditCard);
		this.error = error;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}
}
