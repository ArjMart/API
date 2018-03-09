package com.arjvik.arjmart.api.auth;

public class AuthorizationFailedException extends Exception {
	private static final long serialVersionUID = 1L;
	private int userID;
	private String requiredRole;

	public AuthorizationFailedException(int userID, String requiredRole) {
		super("userID="+userID+" requiredRole="+requiredRole);
		this.userID = userID;
		this.requiredRole = requiredRole;
	}

	public AuthorizationFailedException(int userID, String requiredRole, Throwable cause) {
		super("userID="+userID+" requiredRole="+requiredRole, cause);
		this.userID = userID;
		this.requiredRole = requiredRole;
	}

	public int getUserID() {
		return userID;
	}

	public String getRequiredRole() {
		return requiredRole;
	}

}
