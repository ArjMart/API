package com.arjvik.arjmart.api.user;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UserAlreadyExistsExceptionMapper implements ExceptionMapper<UserAlreadyExistsException> {

	@Override
	public Response toResponse(UserAlreadyExistsException e) {
		return Response.status(Status.CONFLICT).entity(new UserExceptionBean(e.getID(),"user already exists")).build();
	}

}