package com.arjvik.arjmart.api.order.checkout;

public interface PaymentDAO {
	
	public void pay(double price, String creditCardNumber) throws PaymentException;
	
}
