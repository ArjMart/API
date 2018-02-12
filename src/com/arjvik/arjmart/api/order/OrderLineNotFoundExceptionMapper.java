package com.arjvik.arjmart.api.order;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class OrderLineNotFoundExceptionMapper implements ExceptionMapper<OrderLineNotFoundException> {

	@Override
	public Response toResponse(OrderLineNotFoundException e) {
		return Response.status(Status.NOT_FOUND).entity(new OrderLineNotFoundExceptionBean(e.getOrderID(), e.getOrderLineID(), "order line not found")).build();
	}
}
