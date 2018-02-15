package com.arjvik.arjmart.api.order;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class OrderLineCombinationAlreadyExistsExceptionMapper implements ExceptionMapper<OrderLineCombinationAlreadyExistsException> {

	@Override
	public Response toResponse(OrderLineCombinationAlreadyExistsException e) {
		return Response.status(Status.CONFLICT).entity(new OrderLineCombinationAlreadyExistsExceptionBean(e.getOrderID(), e.getSKU(), e.getItemAttributeID(), "order line combination already exists")).build();
	}
}
