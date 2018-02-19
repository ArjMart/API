package com.arjvik.arjmart.api.order;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@SuppressWarnings("unused")
public class OrderLineCombinationAlreadyExistsExceptionMapper implements ExceptionMapper<OrderLineCombinationAlreadyExistsException> {

	@Override
	public Response toResponse(OrderLineCombinationAlreadyExistsException e) {
		return Response.status(Status.CONFLICT).entity(new Object(){
			private String error;
			private int orderID;
			private int SKU;
			private int itemAttributeID;
			
			public Object initialize(int orderID, int SKU, int itemAttributeID, String error) {
				this.orderID = orderID;
				this.SKU = SKU;
				this.itemAttributeID = itemAttributeID;
				this.error = error;
				return this;
			}
			public String getError() {
				return error;
			}
			public int getOrderID() {
				return orderID;
			}
			public int getSKU() {
				return SKU;
			}
			public int getItemAttributeID() {
				return itemAttributeID;
			}
		}.initialize(e.getOrderID(), e.getSKU(), e.getItemAttributeID(), "order line combination already exists")).build();
	}
}
