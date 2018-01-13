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

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {
	
	private static final int MAX_RECORDS = 100;
	
	private ItemDAO itemDAO;
	private ItemAttributeDAO itemAttributeDAO;
	private ItemPriceDAO itemPriceDAO;
	
	@Inject
	public ItemResource(ItemDAO itemDAO, ItemAttributeDAO itemAttributeDAO, ItemPriceDAO itemPriceDAO) {
		this.itemDAO = itemDAO;
		this.itemAttributeDAO = itemAttributeDAO;
		this.itemPriceDAO = itemPriceDAO;
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
		System.err.println(items);
		ResponseBuilder response = Response.ok(items);
		if(start>0){
			int newStart = Math.max(0, start-limit);
			Link previous = Link.fromResource(ItemResource.class).param("start", Integer.toString(newStart)).param("limit", Integer.toString(limit)).build();
			response.links(previous);
		}
		if(items.size()<limit){
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
	public Response addSKU(Item item, @PathParam("SKU") int SKU) throws InvalidSKUException, ItemAlreadyExistsException, DatabaseException {
		if(SKU==0)
			throw new InvalidSKUException(0);
		item.setSKU(SKU);
		itemDAO.addItem(item);
		return Response.created(UriBuilder.fromMethod(ItemResource.class, "getItem").build(SKU)).entity(item).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}")
	public Response editItem(Item item, @PathParam("SKU") int SKU) throws ItemNotFoundException, DatabaseException {
		if(item.getSKU()==0)
			item.setSKU(SKU);
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
	@Path("{SKU}/attributes")
	public Response getAllAttribute(@PathParam("SKU") int SKU) throws ItemNotFoundException, DatabaseException {
		List<ItemAttribute> attributes = itemAttributeDAO.getItemAttributeBySKU(SKU);
		return Response.ok(attributes).build();
	}
	
	@GET
	@Path("{SKU}/attributes/{ID}")
	public Response getAttribute(@PathParam("SKU") int SKU, @PathParam("ID") int ID) throws ItemAttributeNotFoundException, DatabaseException {
		ItemAttribute itemAttribute = itemAttributeDAO.getItemAttribute(SKU, ID);
		return Response.ok(itemAttribute).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}/attributes")
	public Response addAttribute(ItemAttribute itemAttribute, @PathParam("SKU") int SKU) throws ItemNotFoundException, DatabaseException  {
		itemAttribute.setSKU(SKU);
		int ID = itemAttributeDAO.addItemAttribute(itemAttribute);
		itemAttribute.setID(ID);
		return Response.created(UriBuilder.fromMethod(ItemResource.class, "getAttribute").build(SKU,ID)).entity(itemAttribute).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}/attributes/{ID}")
	public Response editAttribute(ItemAttribute itemAttribute, @PathParam("SKU") int SKU, @PathParam("ID") int ID) throws ItemAttributeNotFoundException, ItemNotFoundException, DatabaseException {
		itemAttribute.setSKU(SKU);
		itemAttribute.setID(ID);
		itemAttributeDAO.updateItemAttribute(SKU, ID, itemAttribute);
		return Response.ok(itemAttribute).build();
	}
	
	@DELETE
	@Path("{SKU}/attributes/{ID}")
	public Response deleteAttribute(@PathParam("SKU") int SKU, @PathParam("ID") int ID) throws ItemAttributeNotFoundException, DatabaseException {
		itemAttributeDAO.deleteItemAttribute(SKU,ID);
		return Response.noContent().build();
	}
	
	// PRICE STARTS HERE
	
	@GET
	@Path("{SKU}/attributes/{ID}/price")
	public Response getPrice(@PathParam("SKU") int SKU, @PathParam("ID") int ItemAttributeID) throws DatabaseException {
		ItemPrice itemPrice = itemPriceDAO.getItemPrice(SKU, ItemAttributeID);
		return Response.ok(itemPrice).build();
	}
	
	@PUT
	@Path("{SKU}/attributes/{ID}/price")
	public Response setPrice(ItemPrice itemPrice, @PathParam("SKU") int SKU, @PathParam("ID") int ItemAttributeID) throws ItemAttributeNotFoundException, DatabaseException {
		itemPriceDAO.setItemPrice(SKU, ItemAttributeID, itemPrice);
		return Response.ok(itemPrice).build();
	}
}
