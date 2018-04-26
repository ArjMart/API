package com.arjvik.arjmart.api.order;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.arjvik.arjmart.api.DatabaseException;
import com.arjvik.arjmart.api.auth.AuthorizationFailedException;
import com.arjvik.arjmart.api.auth.Authorized;
import com.arjvik.arjmart.api.auth.InjectPrivileged;
import com.arjvik.arjmart.api.auth.Role;
import com.arjvik.arjmart.api.auth.InjectUserID;
import com.arjvik.arjmart.api.auth.Privileged;
import com.arjvik.arjmart.api.user.UserNotFoundException;
import com.arjvik.arjmart.api.order.checkout.CheckoutDAO;
import com.arjvik.arjmart.api.order.checkout.InvalidOrderStateException;
import com.arjvik.arjmart.api.order.checkout.PaymentException;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
	
	private OrderDAO orderDAO;
	private OrderLineDAO orderLineDAO;
	private CheckoutDAO checkoutDAO;
	
	@Inject
	public OrderResource(OrderDAO orderDAO, OrderLineDAO orderLineDAO, CheckoutDAO checkoutDAO) {
		this.orderDAO = orderDAO;
		this.orderLineDAO = orderLineDAO;
		this.checkoutDAO = checkoutDAO;
	}
	
	@GET
	@Authorized(Role.ORDER_MANAGER)
	public Response getAllOrders() throws DatabaseException {
		List<Order> orders = orderDAO.getAllOrders();
		return Response.ok(orders).build();
	}
	
	@GET
	@Path("status/{status}")
	@Authorized(Role.ORDER_MANAGER)
	public Response getAllOrdersWithStatus(@PathParam("status") String status) throws DatabaseException {
		List<Order> orders = orderDAO.getAllOrdersWithStatus(status);
		return Response.ok(orders).build();
	}
	
	@GET
	@Path("{ID}")
	@Authorized
	@Privileged(Role.ORDER_MANAGER)
	public Response getOrder(@PathParam("ID") int ID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, OrderNotFoundException, DatabaseException {
		Order order = orderDAO.getOrder(ID);
		if(!isPrivileged && userID.intValue() != order.getUserID())
			throw new AuthorizationFailedException(userID, Role.ownerOfOrder(ID));
		return Response.ok(order).build();
	}
	
	@GET
	@Path("{ID}/total")
	@Authorized
	@Privileged(Role.ORDER_MANAGER)
	public Response getOrderTotal(@PathParam("ID") int ID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, OrderNotFoundException, DatabaseException {
		Order order = orderDAO.getOrder(ID);
		if(!isPrivileged && userID.intValue() != order.getUserID())
			throw new AuthorizationFailedException(userID, Role.ownerOfOrder(ID));
		OrderTotal total = orderDAO.getOrderTotal(ID);
		return Response.ok(total).build();
	}
	
	@POST
	@Authorized
	public Response getOrAddCart(@Context @InjectUserID Integer userID) throws UserNotFoundException, DatabaseException {
		Order order = orderDAO.getOrAddOrder(userID);
		return Response.created(UriBuilder.fromMethod(OrderResource.class, "getOrder").build(order.getOrderID())).entity(order).build();
	}
	
	@PUT
	@Path("{ID}")
	@Authorized
	@Privileged(Role.ORDER_MANAGER)
	public Response changeOrderStatus(Status orderStatus, @PathParam("ID") int ID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, OrderNotFoundException, DatabaseException {
		if(!isPrivileged){
			Order order = orderDAO.getOrder(ID);
			if(userID.intValue() != order.getUserID())
				throw new AuthorizationFailedException(userID, Role.ownerOfOrder(ID));
		}
		orderDAO.updateOrderStatus(ID, orderStatus);
		return Response.ok(orderStatus).build();
	}
	
	@DELETE
	@Path("{ID}")
	@Authorized
	@Privileged(Role.ORDER_MANAGER)
	public Response deleteOrder(@PathParam("ID") int ID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, OrderNotFoundException, DatabaseException {
		if(!isPrivileged){
			Order order = orderDAO.getOrder(ID);
			if(userID.intValue() != order.getUserID())
				throw new AuthorizationFailedException(userID, Role.ownerOfOrder(ID));
		}
		orderDAO.deleteOrder(ID);
		return Response.noContent().build();
	}
	
	@GET
	@Path("{OrderID}/lines")
	@Authorized
	@Privileged(Role.ORDER_MANAGER)
	public Response getOrderLines(@PathParam("OrderID") int orderID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, OrderNotFoundException, DatabaseException {
		if(!isPrivileged){
			Order order = orderDAO.getOrder(orderID);
			if(userID.intValue() != order.getUserID())
				throw new AuthorizationFailedException(userID, Role.ownerOfOrder(orderID));
		}
		List<OrderLine> lines = orderLineDAO.getOrderLines(orderID);
		return Response.ok(lines).build();
	}
	
	@GET
	@Path("{OrderID}/lines/{OrderLineID}")
	@Authorized
	@Privileged(Role.ORDER_MANAGER)
	public Response getOrderLine(@PathParam("OrderID") int orderID, @PathParam("OrderLineID") int orderLineID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, OrderNotFoundException, OrderLineNotFoundException, DatabaseException {
		if(!isPrivileged){
			Order order = orderDAO.getOrder(orderID);
			if(userID.intValue() != order.getUserID())
				throw new AuthorizationFailedException(userID, Role.ownerOfOrder(orderID));
		}
		OrderLine line = orderLineDAO.getOrderLine(orderID, orderLineID);
		return Response.ok(line).build();
	}
	
	@POST
	@Path("{OrderID}/lines")
	@Authorized
	@Privileged(Role.ORDER_MANAGER)
	public Response addOrderLine(OrderLine orderLine, @PathParam("OrderID") int orderID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, OrderNotFoundException, OrderLineCombinationAlreadyExistsException, DatabaseException {
		if(!isPrivileged){
			Order order = orderDAO.getOrder(orderID);
			if(userID.intValue() != order.getUserID())
				throw new AuthorizationFailedException(userID, Role.ownerOfOrder(orderID));
		}
		orderLine.setOrderID(orderID);
		int id = orderLineDAO.addOrderLine(orderLine);
		orderLine.setOrderLineID(id);
		orderLine.setStatus("Pending");
		return Response.created(UriBuilder.fromMethod(OrderResource.class, "getOrderLine").build(orderID, id)).entity(orderLine).build();
	}
	
	@PUT
	@Path("{OrderID}/lines/{OrderLineID}")
	@Authorized
	@Privileged(Role.ORDER_MANAGER)
	public Response editOrderLine(Quantity quantity, @PathParam("OrderID") int orderID, @PathParam("OrderLineID") int orderLineID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, OrderNotFoundException, OrderLineNotFoundException, DatabaseException {
		if(!isPrivileged){
			Order order = orderDAO.getOrder(orderID);
			if(userID.intValue() != order.getUserID())
				throw new AuthorizationFailedException(userID, Role.ownerOfOrder(orderID));
		}
		orderLineDAO.updateOrderLineQuantity(orderID, orderLineID, quantity);
		return Response.ok(quantity).build();
	}
	
	@PUT
	@Path("{OrderID}/lines/{OrderLineID}/status")
	@Authorized
	@Privileged(Role.ORDER_MANAGER)
	public Response editOrderLineStatus(Status status, @PathParam("OrderID") int orderID, @PathParam("OrderLineID") int orderLineID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, OrderNotFoundException, OrderLineNotFoundException, DatabaseException {
		if(!isPrivileged){
			Order order = orderDAO.getOrder(orderID);
			if(userID.intValue() != order.getUserID())
				throw new AuthorizationFailedException(userID, Role.ownerOfOrder(orderID));
		}
		orderLineDAO.updateOrderLineStatus(orderID, orderLineID, status);
		return Response.ok(status).build();
	}
	
	@DELETE
	@Path("{OrderID}/lines/{OrderLineID}")
	@Authorized
	@Privileged(Role.ORDER_MANAGER)
	public Response deleteOrderLine(@PathParam("OrderID") int orderID, @PathParam("OrderLineID") int orderLineID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, OrderNotFoundException, OrderLineNotFoundException, DatabaseException {
		if(!isPrivileged){
			Order order = orderDAO.getOrder(orderID);
			if(userID.intValue() != order.getUserID())
				throw new AuthorizationFailedException(userID, Role.ownerOfOrder(orderID));
		}
		orderLineDAO.deleteOrderLine(orderID, orderLineID);
		return Response.noContent().build();
	}
	
	//CHECKOUT
	@POST
	@Path("{OrderID}/checkout")
	@Authorized
	@Privileged(Role.ORDER_MANAGER)
	public Response checkout(@PathParam("OrderID") int orderID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, OrderNotFoundException, InvalidOrderStateException, PaymentException, DatabaseException {
		if(!isPrivileged){
			Order order = orderDAO.getOrder(orderID);
			if(userID.intValue() != order.getUserID())
				throw new AuthorizationFailedException(userID, Role.ownerOfOrder(orderID));
		}
		checkoutDAO.checkout(orderID);
		return Response.noContent().build();
	}

}
