package com.arjvik.arjmart.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.json.JSONException;

@Provider
public class JSONExceptionMapper implements ExceptionMapper<JSONException> {

	@Override
	public Response toResponse(JSONException exception) {
		return Response.status(Status.BAD_REQUEST).build();
	}

}
