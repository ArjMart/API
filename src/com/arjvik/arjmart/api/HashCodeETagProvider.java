package com.arjvik.arjmart.api;

public class HashCodeETagProvider implements ETagProvider {

	@Override
	public String generateETag(Object entity){
		if(entity==null)
			return null;
		return Integer.toHexString(entity.hashCode());
	}

}
