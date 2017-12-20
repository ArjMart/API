package com.arjvik.arjmart.api.order;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
	
	private OrderDAO orderDAO;
	private OrderLineDAO orderLineDAO;
	
	@Inject
	public OrderResource(OrderDAO orderDAO, OrderLineDAO orderLineDAO){
		this.orderDAO = orderDAO;
		this.orderLineDAO = orderLineDAO;
	}
	
	

}
