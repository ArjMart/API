package com.arjvik.arjmart.api.order.checkout;

import de.jupf.staticlog.Log;

public class DummyPaymentDAO implements PaymentDAO {

	@Override
	public void pay(double price, String creditCardNumber) throws PaymentException {
		if(Math.round((price % 1)*100) == 69) {
			Log.info(String.format("Failed to charge $%f to %s (DummyPaymentDAO rejects payments ending in 69 cents)%n", price, creditCardNumber));
			throw new PaymentException(price, creditCardNumber);
		} else {
			Log.info(String.format("Charged $%f to %s%n", price, creditCardNumber));
		}
	}

}
