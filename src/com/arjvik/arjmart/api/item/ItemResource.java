package com.arjvik.arjmart.api.item;

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
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;

import com.arjvik.arjmart.api.DatabaseException;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {
	
	private static final int MAX_RECORDS = 100;
	
	private ItemDAO itemDAO;
	private ItemAttributeDAO itemAttributeDAO;
	
	@Inject
	public ItemResource(ItemDAO itemDAO, ItemAttributeDAO itemAttributeDAO) {
		this.itemDAO = itemDAO;
		this.itemAttributeDAO = itemAttributeDAO;
	}
	
	@GET
	public Response getAll(@DefaultValue("0") @QueryParam("start") int start, @DefaultValue("-1") @QueryParam("limit") int limit, @QueryParam("query") String query) throws DatabaseException {
		if(query!=null)
			return getSearch(start, limit, query);
		if(limit!=-1){
			limit = Math.min(Math.max(limit, 0), MAX_RECORDS);
		}else{
			limit = MAX_RECORDS;
		}
		List<Item> items = itemDAO.getAllItems(start,limit);
		ResponseBuilder response = Response.ok(items);
		if(start>0){
			int newStart = Math.max(0, start-limit);
			Link previous = Link.fromResource(ItemResource.class).param("start", Integer.toString(newStart)).param("limit", Integer.toString(limit)).build();
			response.links(previous);
		}if(items.size()<limit){
			Link next = Link.fromResource(ItemResource.class).param("start", Integer.toString(start+limit)).param("limit", Integer.toString(limit)).build();
			response.links(next);
		}
		return response.build();
	}
	
	public Response getSearch(int start, int limit, String query) throws DatabaseException {
		if(limit!=-1){
			limit = Math.min(Math.max(limit, 0), MAX_RECORDS);
		}else{
			limit = MAX_RECORDS;
		}
		List<Item> items = itemDAO.searchItems(start, limit, query);
		ResponseBuilder response = Response.ok(items);
		if(start>0){
			Link previous = Link.fromUriBuilder(UriBuilder.fromResource(ItemResource.class).queryParam("start", Math.max(0, start-limit)).queryParam("limit", limit).queryParam("query", query)).rel("previous").build();
			response.links(previous);
		}if(items.size()<limit){
			Link next = Link.fromUriBuilder(UriBuilder.fromResource(ItemResource.class).queryParam("start", start+limit).queryParam("limit", limit).queryParam("query", query)).rel("next").build();
			response.links(next);
		}
		return response.build();
	}
	
	@GET
	@Path("{SKU}")
	public Response getItem(@PathParam("SKU") int SKU) throws ItemNotFoundException, DatabaseException {
		Item item = itemDAO.getItem(SKU);
		return Response.ok().entity(item).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}")
	public Response postAddSKU(Item item, @PathParam("SKU") int SKU) throws ItemAlreadyExistsException, DatabaseException {
		item.setSKU(SKU);
		itemDAO.addItem(item);
		return Response.created(UriBuilder.fromMethod(ItemResource.class, "postAddSKU").build(SKU)).entity(item).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}")
	public Response putEditItem(Item item, @PathParam("SKU") int SKU) throws ItemNotFoundException, DatabaseException {
		itemDAO.updateItem(SKU, item);
		return Response.ok().entity(item).build();
	}
	
	@DELETE
	@Path("{SKU}")
	public Response deleteItem(@PathParam("SKU") int SKU) throws ItemNotFoundException, DatabaseException {
		itemDAO.deleteItem(SKU);
		return Response.noContent().build();
	}
	
	// ATTRIBUTE STARTS HERE
	
	@GET
	@Path("{SKU}/attribute")
	public Response getAllAttribute(@PathParam("SKU") int SKU) throws ItemNotFoundException, DatabaseException {
		List<ItemAttribute> attributes = itemAttributeDAO.getItemAttributeBySKU(SKU);
		return Response.ok(attributes).build();
	}
	
	@GET
	@Path("attribute/{ID}")
	public Response getAttribute(@PathParam("ID") int ID) throws ItemAttributeNotFoundException, DatabaseException {
		ItemAttribute itemAttribute = itemAttributeDAO.getItemAttribute(ID);
		return Response.ok(itemAttribute).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}/attribute")
	public Response postAddAttribute(ItemAttribute itemAttribute, @PathParam("SKU") int SKU) throws DatabaseException {
		int ID = itemAttributeDAO.addItemAttribute(itemAttribute);
		itemAttribute.setID(ID);
		return Response.created(UriBuilder.fromUri("{SKU}/attribute/{ID}").build(SKU,ID)).entity(itemAttribute).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("attribute/{ID}")
	public Response putEditAttribute(ItemAttribute itemAttribute, @PathParam("ID") int ID) throws ItemAttributeNotFoundException, ItemNotFoundException, DatabaseException {
		itemAttributeDAO.updateItemAttribute(ID, itemAttribute);
		return Response.ok(itemAttribute).build();
	}
	
	@DELETE
	@Path("attribute/{ID}")
	public Response deleteAttribute(@PathParam("ID") int ID) throws ItemAttributeNotFoundException, DatabaseException {
		itemAttributeDAO.deleteItemAttribute(ID);
		return Response.noContent().build();
	}
}
