package com.arjvik.arjmart.api.item;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@SuppressWarnings("unused")
public class ItemAttributeNotFoundExceptionMapper implements ExceptionMapper<ItemAttributeNotFoundException> {

	@Override
	public Response toResponse(ItemAttributeNotFoundException e) {
		return Response.status(Status.NOT_FOUND).entity(new Object(){
			private String error;
			private int SKU;
			private int ID;

			public Object initialize(int SKU, int ID, String error) {
				this.SKU = SKU;
				this.ID = ID;
				this.error = error;
				return this;
			}
			public String getError() {
				return error;
			}
			public int getSKU() {
				return SKU;
			}
			public int getID() {
				return ID;
			}
		}.initialize(e.getSKU(),e.getID(),"item attribute not found")).build();
	}

}
