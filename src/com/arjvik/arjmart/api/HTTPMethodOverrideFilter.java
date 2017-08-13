package com.arjvik.arjmart.api;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;

@PreMatching
@Priority(Priorities.HEADER_DECORATOR)
public class HTTPMethodOverrideFilter implements ContainerResponseFilter {
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		if(requestContext.getMethod()!="POST")
			return;
		String method = requestContext.getHeaderString("X-Http-Method-Override");
		if(method!=null){
			requestContext.setMethod(method);
		}
	}
}
