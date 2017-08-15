package com.arjvik.arjmart.api.filters;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(Priorities.HEADER_DECORATOR)
public class HTTPMethodOverrideFilter implements ContainerRequestFilter {
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(!requestContext.getMethod().equals("POST"))
			return;
		String method = requestContext.getHeaderString("X-Http-Method-Override");
		if(method!=null){
			requestContext.setMethod(method);
		}
	}
}
