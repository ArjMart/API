package com.arjvik.arjmart.api;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class ETagFilter implements ContainerRequestFilter, ContainerResponseFilter {

	private final CacheControl cc = new CacheControl(){{
		setMaxAge(3600);
		setPrivate(true);
		setNoStore(true);
	}};
	
	ETagProvider etagProvider;
	
	@Inject
	public ETagFilter(ETagProvider etagProvider){
		this.etagProvider = etagProvider;
	}
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(requestContext.getHeaders().containsKey(HttpHeaders.IF_NONE_MATCH)){
			requestContext.setProperty("If-None-Match", requestContext.getHeaders().getFirst(HttpHeaders.IF_NONE_MATCH).replaceAll("\"", ""));
		}
	}
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		if(requestContext.getMethod()!="GET" || responseContext.getStatus()!=200)
			return;
		String ETag = etagProvider.generateETag(responseContext.getEntity());
		if(ETag==null)
			return;
		if(requestContext.getPropertyNames().contains("If-None-Match")){
			if(requestContext.getProperty("If-None-Match").equals(ETag)){
				responseContext.setEntity(null);
				responseContext.setStatusInfo(Status.NOT_MODIFIED);
				MultivaluedMap<String,Object> headers = responseContext.getHeaders();
				for (String string : headers.keySet()) {
					headers.remove(string);
				}
			}
		}
		responseContext.getHeaders().putSingle(HttpHeaders.ETAG, "\""+ETag+"\"");
		responseContext.getHeaders().putSingle(HttpHeaders.CACHE_CONTROL, cc.toString());
	}

}
