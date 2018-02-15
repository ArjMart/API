package com.arjvik.arjmart.api.order.checkout;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PaymentExceptionMapper implements ExceptionMapper<PaymentException> {

	@Override
	public Response toResponse(PaymentException e) {
		return Response.status(Status.PAYMENT_REQUIRED).entity(new PaymentExceptionBean(e.getPrice(), e.getCreditCard(), "payment error")).build();
	}

}