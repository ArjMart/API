package com.arjvik.arjmart.api.order.checkout;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@SuppressWarnings("unused")
public class PaymentExceptionMapper implements ExceptionMapper<PaymentException> {

	@Override
	public Response toResponse(PaymentException e) {
		return Response.status(Status.PAYMENT_REQUIRED).entity(new Object(){
			private String error;
			private double price;
			private String creditCard;

			public Object initialize(double price, String creditCard, String error) {
				this.price = price;
				this.creditCard = creditCard;
				this.error = error;
				return this;
			}
			public String getError() {
				return error;
			}
			public double getPrice() {
				return price;
			}
			public String getCreditCard() {
				return creditCard;
			}
		}.initialize(e.getPrice(), e.getCreditCard(), "payment error")).build();
	}

}