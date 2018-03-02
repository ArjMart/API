package com.arjvik.arjmart.api;

public interface AuthenticationDAO {

	int authenticate() throws AuthenticationFailedException;

	void authorize(String value) throws AuthorizationFailedException;

}
