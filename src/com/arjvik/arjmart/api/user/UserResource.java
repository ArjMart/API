package com.arjvik.arjmart.api.user;

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
import com.arjvik.arjmart.api.auth.InjectUserID;
import com.arjvik.arjmart.api.auth.Privileged;
import com.arjvik.arjmart.api.auth.Role;
import com.arjvik.arjmart.api.order.Order;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
	
	private UserDAO userDAO;
	
	@Inject
	public UserResource(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	@GET
	@Authorized
	public Response getUserID(@Context @InjectUserID Integer userID) {
		return Response.ok(new UserID(userID)).build();
	}
	
	@GET
	@Path("{ID}")
	@Authorized
	@Privileged(Role.USER_MANAGER)
	public Response getUser(@PathParam("ID") int ID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, UserNotFoundException, DatabaseException {
		if(!isPrivileged && userID.intValue() != ID)
			throw new AuthorizationFailedException(userID, Role.user(ID));
		User user = userDAO.getUser(ID);
		return Response.ok(user).build();
	}
	
	@POST
	public Response addUser(User user) throws UserAlreadyExistsException, DatabaseException {
		int ID = userDAO.addUser(user);
		user.setID(ID);
		return Response.created(UriBuilder.fromMethod(UserResource.class, "getUser").build(ID)).entity(user).build();
	}
	
	@PUT
	@Path("{ID}")
	@Authorized
	@Privileged(Role.USER_MANAGER)
	public Response editCreditCardNumber(User user, @PathParam("ID") int ID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, UserNotFoundException, DatabaseException {
		if(!isPrivileged && userID.intValue() != ID)
			throw new AuthorizationFailedException(userID, Role.user(ID));
		userDAO.editUserCreditCardNumber(ID, user);
		user.setID(ID);
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("{ID}")
	@Authorized
	@Privileged(Role.USER_MANAGER)
	public Response deleteUser(@PathParam("ID") int ID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, UserNotFoundException, DatabaseException {
		if(!isPrivileged && userID.intValue() != ID)
			throw new AuthorizationFailedException(userID, Role.user(ID));
		userDAO.deleteUser(ID);
		return Response.noContent().build();
	}
	
	@GET
	@Path("{ID}/orders")
	@Authorized
	@Privileged(Role.ORDER_MANAGER)
	public Response getUserOrders(@PathParam("ID") int ID, @Context @InjectUserID Integer userID, @Context @InjectPrivileged Boolean isPrivileged) throws AuthorizationFailedException, UserNotFoundException, DatabaseException {
		if(!isPrivileged && userID.intValue() != ID)
			throw new AuthorizationFailedException(userID, Role.user(ID));
		List<Order> orders = userDAO.getUserOrders(ID);
		return Response.ok(orders).build();
	}

}
