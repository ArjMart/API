package com.arjvik.arjmart.api.order.checkout;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidOrderStateExceptionMapper implements ExceptionMapper<InvalidOrderStateException> {

	@Override
	public Response toResponse(InvalidOrderStateException e) {
		return Response.status(Status.NOT_FOUND).entity(new InvalidOrderStateExceptionBean(e.getID(), e.getStatus(), "invalid order state - must be 'Cart' for checkout")).build();
	}

}
