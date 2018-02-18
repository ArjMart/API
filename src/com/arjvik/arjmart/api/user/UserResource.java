package com.arjvik.arjmart.api.user;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import com.arjvik.arjmart.api.DatabaseException;

public class UserResource {
	
	private UserDAO userDAO;
	
	@Inject
	public UserResource(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	@GET
	@Path("{ID}")
	public Response getUser(@PathParam("ID") int ID) throws UserNotFoundException, DatabaseException {
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
	public Response editCreditCardNumber(User user, @PathParam("ID") int id) throws UserAlreadyExistsException, DatabaseException {
		userDAO.editUserCreditCardNumber(id, user);
		user.setID(id);
		return Response.noContent().build();
	}
	
	@POST
	@Path("authenticate")
	public Response authenticate(User user) throws DatabaseException {
		if(userDAO.authenticate(user))
			return Response.ok().build();
		else return Response.status(Status.UNAUTHORIZED).build();
	}

}
