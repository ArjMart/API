package com.arjvik.arjmart.api.exceptionmappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.arjvik.arjmart.api.dao.DatabaseException;

@Provider
public class DatabaseExceptionMapper implements ExceptionMapper<DatabaseException> {

	@Override
	public Response toResponse(DatabaseException exception) {
		return Response.serverError().build();
	}

}
