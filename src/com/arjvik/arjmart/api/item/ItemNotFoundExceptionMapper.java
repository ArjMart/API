package com.arjvik.arjmart.api.item;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ItemNotFoundExceptionMapper implements ExceptionMapper<ItemNotFoundException> {

	@Override
	public Response toResponse(ItemNotFoundException e) {
		return Response.status(Status.NOT_FOUND).entity(new ItemExceptionBean(e.getSKU(),"item not found")).build();
	}
	
}
