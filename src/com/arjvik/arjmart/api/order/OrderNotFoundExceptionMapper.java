package com.arjvik.arjmart.api.order;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@SuppressWarnings("unused")
public class OrderNotFoundExceptionMapper implements ExceptionMapper<OrderNotFoundException> {

	@Override
	public Response toResponse(OrderNotFoundException e) {
		return Response.status(Status.NOT_FOUND).entity(new Object(){
			private String error;
			private int orderID;

			public Object initialize(int orderID, String error) {
				this.orderID = orderID;
				this.error = error;
				return this;
			}
			public String getError() {
				return error;
			}
			public int getOrderID() {
				return orderID;
			}
		}.initialize(e.getID(), "order not found")).build();
	}

}
