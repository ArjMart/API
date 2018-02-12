package com.arjvik.arjmart.api.order.checkout;

public class DummyPaymentDAO implements PaymentDAO {

	@Override
	public void pay(double price, String creditCardNumber) throws PaymentException {
		if(price % 1 == 0.69) {
			System.out.printf("Failed to charge $%d to %s (DummyPaymentDAO rejects payments ending in 69 cents)%n", price, creditCardNumber);
			throw new PaymentException(price, creditCardNumber);
		} else {
			System.out.printf("Charged $%d to %s%n", price, creditCardNumber);
		}
	}

}
