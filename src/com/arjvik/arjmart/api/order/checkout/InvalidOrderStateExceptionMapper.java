package com.arjvik.arjmart.api.order.checkout;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@SuppressWarnings("unused")
public class InvalidOrderStateExceptionMapper implements ExceptionMapper<InvalidOrderStateException> {

	@Override
	public Response toResponse(InvalidOrderStateException e) {
		return Response.status(Status.CONFLICT).entity(new Object(){
			private String error;
			private int orderID;
			private String status;

			public Object initialize(int orderID, String status, String error) {
				this.orderID = orderID;
				this.status = status;
				this.error = error;
				return this;
			}
			public String getError() {
				return error;
			}
			public int getOrderID() {
				return orderID;
			}
			public String getStatus() {
				return status;
			}
		}.initialize(e.getID(), e.getStatus(), "invalid order state - must be 'Cart' for checkout")).build();
	}

}
