package com.arjvik.arjmart.api.auth;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@SuppressWarnings("unused")
public class AuthorizationFailedExceptionMapper implements ExceptionMapper<AuthorizationFailedException> {

	@Override
	public Response toResponse(AuthorizationFailedException e) {
		return Response.status(Status.FORBIDDEN).entity(new Object(){
			private String error;
			private int userID;
			private String requiredRole;
			
			public Object initialize(int userID, String requiredRole, String error) {
				this.userID = userID;
				this.requiredRole = requiredRole;
				this.error = error;
				return this;
			}
			public String getError() {
				return error;
			}
			public int getUserID() {
				return userID;
			}
			public String getRequiredRole() {
				return requiredRole;
			}
		}.initialize(e.getUserID(), e.getRequiredRole(), "User not allowed to perform action")).build();
	}

}
