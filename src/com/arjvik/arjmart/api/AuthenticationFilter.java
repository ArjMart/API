package com.arjvik.arjmart.api;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
@Authorized
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	@Context
	ResourceInfo info;
	
	AuthenticationDAO authenticationDAO;
	
	@Inject
	public AuthenticationFilter(AuthenticationDAO authenticationDAO) {
		this.authenticationDAO = authenticationDAO;
	}
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Authorized authorized = info.getResourceMethod().getAnnotation(Authorized.class);
		try {
			int userID = authenticationDAO.authenticate();
			requestContext.setProperty("userID", userID);
			if(!authorized.value().equals(Authorized.NO_ROLE)){
				authenticationDAO.authorize(authorized.value());
			}
		} catch (AuthenticationFailedException e) {
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).header("WWW-Autheticate","BASIC").build());
		} catch (AuthorizationFailedException e) {
			requestContext.abortWith(Response.status(Status.FORBIDDEN).build());
		}
		
	}
}
