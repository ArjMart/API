package com.arjvik.arjmart.api.location;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.arjvik.arjmart.api.DatabaseException;
import com.arjvik.arjmart.api.auth.Authorized;
import com.arjvik.arjmart.api.auth.Role;
import com.arjvik.arjmart.api.item.ItemAttributeNotFoundException;
import com.arjvik.arjmart.api.jms.PipelineException;

@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocationResource {
	
	private LocationDAO locationDAO;
	private InventoryDAO inventoryDAO;
	
	@Inject
	public LocationResource(LocationDAO locationDAO, InventoryDAO inventoryDAO){
		this.locationDAO = locationDAO;
		this.inventoryDAO = inventoryDAO;
	}
	
	@GET
	public Response getAll() throws DatabaseException {
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
	@Authorized(Role.INVENTORY_MANAGER)
	public Response addLocation(Location location) throws DatabaseException{
		int ID = locationDAO.addLocation(location);
		location.setID(ID);
		return Response.created(UriBuilder.fromMethod(LocationResource.class, "getLocation").build(ID)).entity(location).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{ID}")
	@Authorized(Role.INVENTORY_MANAGER)
	public Response editLocation(Location location, @PathParam("ID") int ID) throws LocationNotFoundException, DatabaseException {
		if(location.getID()==0)
			location.setID(ID);
		locationDAO.updateLocation(ID, location);
		return Response.ok().entity(location).build();
	}
	
	@DELETE
	@Path("{ID}")
	@Authorized(Role.INVENTORY_MANAGER)
	public Response deleteLocation(@PathParam("ID") int ID) throws LocationNotFoundException, DatabaseException {
		locationDAO.deleteLocation(ID);
		return Response.noContent().build();
	}
	
	// INVENTORY STARTS HERE
	
	@GET
	@Path("{ID}/inventory/")
	public Response getAllInventory(@PathParam("ID") int ID) throws DatabaseException {
		return Response.ok(inventoryDAO.getAllInventory(ID)).build();
	}
	
	@GET
	@Path("inventory/{SKU}/{itemAttributeID}")
	public Response getAllLocations(@PathParam("SKU") int SKU, @PathParam("itemAttributeID") int itemAttributeID) throws DatabaseException {
		return Response.ok(inventoryDAO.getAllLocations(SKU, itemAttributeID)).build();
	}
	
	@GET
	@Path("{ID}/inventory/{SKU}/{itemAttributeID}")
	public Response getInventory(@PathParam("SKU") int SKU, @PathParam("ID") int ID, @PathParam("itemAttributeID") int itemAttributeID) throws DatabaseException {
		return Response.ok(inventoryDAO.getInventory(ID, SKU, itemAttributeID)).build();
	}
	
	@PUT
	@Path("{ID}/inventory/{SKU}/{itemAttributeID}")
	@Authorized(Role.INVENTORY_MANAGER)
	public Response setInventory(Inventory inventory, @PathParam("SKU") int SKU, @PathParam("ID") int ID, @PathParam("itemAttributeID") int itemAttributeID) throws LocationNotFoundException, ItemAttributeNotFoundException, PipelineException, DatabaseException {
		inventory.setLocationID(ID);
		inventory.setSKU(SKU);
		inventory.setItemAttributeID(itemAttributeID);
		inventoryDAO.setInventory(inventory);
		return Response.ok(inventory).build();
	}
	
	@POST
	@Path("{ID}/inventory/{SKU}/{itemAttributeID}")
	@Authorized(Role.INVENTORY_MANAGER)
	public Response addInventory(Inventory inventory, @PathParam("SKU") int SKU, @PathParam("ID") int ID, @PathParam("itemAttributeID") int itemAttributeID) throws LocationNotFoundException, ItemAttributeNotFoundException, PipelineException, DatabaseException {
		inventory.setLocationID(ID);
		inventory.setSKU(SKU);
		inventory.setItemAttributeID(itemAttributeID);
		inventory = inventoryDAO.addInventory(inventory);
		return Response.ok(inventory).build();
	}
}
