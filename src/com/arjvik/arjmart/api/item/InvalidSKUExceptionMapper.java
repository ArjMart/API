package com.arjvik.arjmart.api.item;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@SuppressWarnings("unused")
public class InvalidSKUExceptionMapper implements ExceptionMapper<InvalidSKUException> {

	@Override
	public Response toResponse(InvalidSKUException e) {
		return Response.status(Status.CONFLICT).entity(new Object(){
			private String error;
			private int SKU;
			
			public Object initialize(int SKU, String error) {
				this.SKU = SKU;
				this.error = error;
				return this;
			}
			public String getError() {
				return error;
			}
			public int getSKU() {
				return SKU;
			}
		}.initialize(e.getSKU(),"SKU must be non-zero")).build();
	}

}
