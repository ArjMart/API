package com.arjvik.arjmart.api.location;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LocationNotFoundExceptionMapper implements ExceptionMapper<LocationNotFoundException> {

	@Override
	public Response toResponse(LocationNotFoundException e) {
		return Response.status(Status.NOT_FOUND).entity(new LocationExceptionBean(e.getID(),"location not found")).build();
	}
	
}