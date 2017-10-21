package com.arjvik.arjmart.api.item;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ItemAttributeNotFoundExceptionMapper implements ExceptionMapper<ItemAttributeNotFoundException> {

	@Override
	public Response toResponse(ItemAttributeNotFoundException e) {
		return Response.status(Status.NOT_FOUND).entity(new ItemAttributeExceptionBean(e.getID(),"item attribute not found")).build();
	}

}
