package com.arjvik.arjmart.api;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Priority;
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

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(requestContext.getHeaders().containsKey(HttpHeaders.IF_NONE_MATCH)){
			requestContext.setProperty("If-None-Match", requestContext.getHeaders().getFirst(HttpHeaders.IF_NONE_MATCH).replaceAll("\"", ""));
		}
	}
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		try {
			if(requestContext.getMethod()!="GET")
				return;
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String entity = ((String) responseContext.getEntity());
			if(entity==null)
				return;
			byte[] etagBytes = md.digest(entity.getBytes());
			String ETag = toHexString(etagBytes);
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
			CacheControl cc = new CacheControl();
			cc.setMaxAge(300);
			cc.setPrivate(true);
			cc.setNoStore(true);
			responseContext.getHeaders().putSingle(HttpHeaders.CACHE_CONTROL, cc.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private static String toHexString(final byte[] data) {
        final int l = data.length;
        final char[] toDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return new String(out);
    }
}
