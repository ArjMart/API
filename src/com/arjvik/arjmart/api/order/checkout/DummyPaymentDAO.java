package com.arjvik.arjmart.api.order.checkout;

public class DummyPaymentDAO implements PaymentDAO {

	@Override
	public void pay(double price, String creditCardNumber) throws PaymentException {
		if(Math.round((price % 1)*100) == 69) {
			System.out.printf("Failed to charge $%f to %s (DummyPaymentDAO rejects payments ending in 69 cents)%n", price, creditCardNumber);
			throw new PaymentException(price, creditCardNumber);
		} else {
			System.out.printf("Charged $%f to %s%n", price, creditCardNumber);
		}
	}

}
