package com.arjvik.arjmart.api.location;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@SuppressWarnings("unused")
public class LocationNotFoundExceptionMapper implements ExceptionMapper<LocationNotFoundException> {

	@Override
	public Response toResponse(LocationNotFoundException e) {
		return Response.status(Status.NOT_FOUND).entity(new Object(){
			private String error;
			private int ID;

			public Object initialize(int ID, String error) {
				this.ID = ID;
				this.error = error;
				return this;
			}
			public String getError() {
				return error;
			}
			public int getID() {
				return ID;
			}
		}.initialize(e.getID(),"location not found")).build();
	}
	
}