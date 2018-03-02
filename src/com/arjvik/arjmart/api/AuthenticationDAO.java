package com.arjvik.arjmart.api;

public interface AuthenticationDAO {

	int authenticate(String credentials) throws AuthenticationFailedException;

	void authorize(int userID, String role) throws AuthorizationFailedException;

}
