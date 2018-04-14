package com.arjvik.arjmart.api.auth;

public class AuthenticationFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public AuthenticationFailedException() {
		super();
	}

	public AuthenticationFailedException(Throwable cause) {
		super(cause);
	}

}
