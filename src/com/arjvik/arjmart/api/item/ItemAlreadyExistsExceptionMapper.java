package com.arjvik.arjmart.api.item;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ItemAlreadyExistsExceptionMapper implements ExceptionMapper<ItemAlreadyExistsException> {

	@Override
	public Response toResponse(ItemAlreadyExistsException e) {
		return Response.status(Status.CONFLICT).entity(new ItemExceptionBean(e.getSKU(),"item already exists")).build();
	}

}
