package com.arjvik.arjmart.api.order;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.arjvik.arjmart.api.DatabaseException;
import com.arjvik.arjmart.api.user.UserNotFoundException;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
	
	private OrderDAO orderDAO;
	private OrderLineDAO orderLineDAO;
	
	@Inject
	public OrderResource(OrderDAO orderDAO, OrderLineDAO orderLineDAO) {
		this.orderDAO = orderDAO;
		this.orderLineDAO = orderLineDAO;
	}
	
	@GET
	public Response getAllOrders() throws DatabaseException {
		List<Order> orders = orderDAO.getAllOrders();
		return Response.ok(orders).build();
	}
	
	@GET
	@Path("status/{status}")
	public Response getAllOrdersWithStatus(@PathParam("status") String status) throws DatabaseException {
		List<Order> orders = orderDAO.getAllOrdersWithStatus(status);
		return Response.ok(orders).build();
	}
	
	@GET
	@Path("{ID}")
	public Response getOrder(@PathParam("ID") int ID) throws OrderNotFoundException, DatabaseException {
		Order order = orderDAO.getOrder(ID);
		return Response.ok(order).build();
	}
	
	@POST
	public Response addOrder(Order order) throws UserNotFoundException, DatabaseException {
		order = orderDAO.addOrder(order);
		return Response.created(UriBuilder.fromMethod(OrderResource.class, "getOrder").build(order.getOrderID())).entity(order).build();
	}
	
	@PUT
	@Path("{ID}")
	public Response changeOrderStatus(OrderStatus orderStatus, @PathParam("ID") int ID) throws OrderNotFoundException, DatabaseException {
		orderDAO.updateOrderStatus(ID, orderStatus);
		return Response.ok(orderStatus).build();
	}
	
	@DELETE
	@Path("{ID}")
	public Response deleteOrder(@PathParam("ID") int ID) throws OrderNotFoundException, DatabaseException {
		orderDAO.deleteOrder(ID);
		return Response.noContent().build();
	}
	
	@GET
	@Path("{orderID}/lines")
	public Response getOrderLine(@PathParam("orderID") int orderID) throws DatabaseException {
		List<OrderLine> lines = orderLineDAO.getOrderLines(orderID);
		return Response.ok(lines).build();
	}
	
	@GET
	@Path("{orderID}/lines/{orderLineID}")
	public Response getOrderLine(@PathParam("orderID") int orderID, @PathParam("orderLineID") int orderLineID) throws DatabaseException {
		OrderLine line = orderLineDAO.getOrderLine(orderID, orderLineID);
		return Response.ok(line).build();
	}
	
	@POST
	@Path("{orderID}/lines")
	public Response addOrderLine(OrderLine orderLine, @PathParam("OrderID") int orderID) throws DatabaseException {
		orderLine.setOrderID(orderID);
		int id = orderLineDAO.addOrderLine(orderLine);
		orderLine.setOrderLineID(id);
		return Response.created(UriBuilder.fromMethod(OrderResource.class, "getOrderLine").build(orderID, id)).entity(orderLine).build();
	}
	
	@PUT
	@Path("{orderID}/lines/{orderLineID}")
	public Response editOrderLine(OrderLine orderLine, @PathParam("OrderID") int orderID, @PathParam("orderLineID") int orderLineID) throws DatabaseException {
		orderLine.setOrderID(orderID);
		int id = orderLineDAO.updateOrderLine(orderID, orderLineID, orderLine); //Only quantity and status are allowed
		orderLine.setOrderLineID(id);
		return Response.ok(orderLine).build();
	}
	
	@DELETE
	@Path("{orderID}/lines/{orderLineID}")
	public Response deleteOrderLine(@PathParam("OrderID") int orderID, @PathParam("orderLineID") int orderLineID) throws DatabaseException {
		orderLineDAO.deleteOrderLine(orderID, orderLineID);
		return Response.noContent().build();
	}

}
