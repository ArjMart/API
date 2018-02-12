package com.arjvik.arjmart.api.order.checkout;

import com.arjvik.arjmart.api.DatabaseException;
import com.arjvik.arjmart.api.order.OrderNotFoundException;

public interface CheckoutDAO {
	
	public void checkout(int orderID) throws OrderNotFoundException, InvalidOrderStateException, PaymentException, DatabaseException;
			
}
