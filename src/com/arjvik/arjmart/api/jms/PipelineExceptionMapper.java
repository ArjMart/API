package com.arjvik.arjmart.api.jms;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class PipelineExceptionMapper implements ExceptionMapper<PipelineException> {

	@Override
	public Response toResponse(PipelineException e) {
		e.printStackTrace();
		return Response.serverError().build();
	}

}
