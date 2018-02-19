package com.arjvik.arjmart.api.user;

public class UserAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;
	private String email;
	
	public UserAlreadyExistsException(String email) {
		super(email);
		this.email = email;
	}

	public UserAlreadyExistsException(String email, Throwable cause) {
		super(email, cause);
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
}
