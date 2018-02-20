package com.arjvik.arjmart.api;

public interface ETagProvider {
	
	public String generateETag(Object entity);

}
