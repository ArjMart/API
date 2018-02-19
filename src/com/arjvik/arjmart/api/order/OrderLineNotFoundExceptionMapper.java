package com.arjvik.arjmart.api.order;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@SuppressWarnings("unused")
public class OrderLineNotFoundExceptionMapper implements ExceptionMapper<OrderLineNotFoundException> {

	@Override
	public Response toResponse(OrderLineNotFoundException e) {
		return Response.status(Status.NOT_FOUND).entity(new Object(){
			private String error;
			private int orderID;
			private int orderLineID;

			public Object initialize(int orderID, int OrderLineID, String error) {
				this.orderID = orderID;
				this.orderLineID = OrderLineID;
				this.error = error;
				return this;
			}
			public String getError() {
				return error;
			}
			public int getOrderID() {
				return orderID;
			}
			public int getOrderLineID() {
				return orderLineID;
			}
		}.initialize(e.getOrderID(), e.getOrderLineID(), "order line not found")).build();
	}
}
