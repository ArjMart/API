package com.arjvik.arjmart.api;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(requestContext.getHeaderString("X-API-Key")==null)
			return;
		JSONObject json = new JSONObject()
				.put("error", "insufficient authentication")
				.put("token", requestContext.getHeaderString("X-API-Key"));
		requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity(json.toString()).build());
	}

}
