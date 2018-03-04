package com.arjvik.arjmart.api.auth;

import com.arjvik.arjmart.api.DatabaseException;

public interface AuthenticationDAO {

	int authenticate(String credentials) throws AuthenticationFailedException, DatabaseException;

	void authorize(int userID, String role) throws AuthorizationFailedException, DatabaseException;

}
