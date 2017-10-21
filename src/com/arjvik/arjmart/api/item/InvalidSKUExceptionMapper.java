package com.arjvik.arjmart.api.item;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidSKUExceptionMapper implements ExceptionMapper<InvalidSKUException> {

	@Override
	public Response toResponse(InvalidSKUException e) {
		return Response.status(Status.CONFLICT).entity(new ItemExceptionBean(e.getSKU(),"SKU must be non-zero")).build();
	}

}
