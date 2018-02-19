package com.arjvik.arjmart.api.user;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UserNotFoundExceptionMapper implements ExceptionMapper<UserNotFoundException> {

	@Override
	public Response toResponse(UserNotFoundException e) {
		return Response.status(Status.NOT_FOUND).entity(new Object(){
			private String error;
			private int userID;
			
			public Object initialize(int userID, String error) {
				this.userID = userID;
				this.error = error;
				return this;
			}
			@SuppressWarnings("unused")
			public String getError() {
				return error;
			}
			@SuppressWarnings("unused")
			public int getUserID() {
				return userID;
			}
		}.initialize(e.getID(), "user not found")).build();
	}

}
