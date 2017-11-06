package com.arjvik.arjmart.api.location;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.arjvik.arjmart.api.DatabaseException;

@Path("/location")
@Produces(MediaType.APPLICATION_JSON)
public class LocationResource {
	
	private LocationDAO locationDAO;
	
	@Inject
	public LocationResource(LocationDAO locationDAO){
		this.locationDAO = locationDAO;
	}
	
	@GET
	public Response getAll(@DefaultValue("-1") @QueryParam("limit") int limit) throws DatabaseException {
		List<Location> locations = locationDAO.getAllLocations();
		return Response.ok().entity(locations).build();
	}
	
	@GET
	@Path("{ID}")
	public Response getLocation(@PathParam("ID") int ID) throws LocationNotFoundException, DatabaseException{
		Location location = locationDAO.getLocation(ID);
		return Response.ok().entity(location).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postAddLocation(Location location) throws DatabaseException{
		int ID = locationDAO.addLocation(location);
		location.setID(ID);
		return Response.created(UriBuilder.fromMethod(LocationResource.class, "getLocation").build(ID)).entity(location).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{ID}")
	public Response putEditLocation(Location location, @PathParam("ID") int ID) throws LocationNotFoundException, DatabaseException {
		if(location.getID()==0)
			location.setID(ID);
		locationDAO.updateLocation(ID, location);
		return Response.ok().entity(location).build();
	}
	
	@DELETE
	@Path("{ID}")
	public Response deleteLocation(@PathParam("ID") int ID) throws LocationNotFoundException, DatabaseException{
		locationDAO.deleteLocation(ID);
		return Response.noContent().build();
	}
}
