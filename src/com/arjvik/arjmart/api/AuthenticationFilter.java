package com.arjvik.arjmart.api;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(requestContext.getHeaderString("X-API-Key")==null)
			return;
		Map<String, String> json = new LinkedHashMap<>();
		json.put("error", "insufficient authentication");
		json.put("token", requestContext.getHeaderString("X-API-Key"));
		requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity(json.toString()).header("WWW-Authenticate", "X-API-Key").build());
	}
}
