package com.arjvik.arjmart.api.order.checkout;

public class PaymentException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private double price;
	private String creditCard;
	
	public PaymentException(double price, String creditCard) {
		super(Double.toString(price));
		this.price = price;
		this.creditCard = creditCard;
	}

	public PaymentException(double price, String creditCard, Throwable cause) {
		super(Double.toString(price), cause);
		this.price = price;
		this.creditCard = creditCard;
	}

	public double getPrice() {
		return price;
	}

	public String getCreditCard() {
		return creditCard;
	}
}
