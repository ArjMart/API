package com.arjvik.arjmart.api.order;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class OrderNotFoundExceptionMapper implements ExceptionMapper<OrderNotFoundException> {

	@Override
	public Response toResponse(OrderNotFoundException e) {
		return Response.status(Status.NOT_FOUND).entity(new OrderExceptionBean(e.getID(), "order not found")).build();
	}

}
