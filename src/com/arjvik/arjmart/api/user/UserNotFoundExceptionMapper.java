package com.arjvik.arjmart.api.user;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class UserNotFoundExceptionMapper implements ExceptionMapper<UserNotFoundException> {

	@Override
	public Response toResponse(UserNotFoundException e) {
		// TODO Auto-generated method stub
		return null;
	}

}
