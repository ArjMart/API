package com.arjvik.arjmart.api.user;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UserAlreadyExistsExceptionMapper implements ExceptionMapper<UserAlreadyExistsException> {

	@Override
	public Response toResponse(UserAlreadyExistsException e) {
		return Response.status(Status.CONFLICT).entity(new Object(){
			private String error;
			private String email;
			
			public Object initialize(String email, String error) {
				this.email = email;
				this.error = error;
				return this;
			}
			@SuppressWarnings("unused")
			public String getError() {
				return error;
			}
			@SuppressWarnings("unused")
			public String getEmail() {
				return email;
			}
		}.initialize(e.getEmail(), "user already exists with specified email address")).build();
	}

}