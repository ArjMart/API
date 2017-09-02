package com.arjvik.arjmart.api;

import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import de.jupf.staticlog.Log;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {
	
	private static final int MAX_RECORDS = 100;
	
	private ItemDAO itemDAO;
	private ItemAttributeDAO itemAttributeDAO;
	private ConnectionFactory connectionFactory;
	
	@Inject
	public ItemResource(ItemDAO itemDAO, ItemAttributeDAO itemAttributeDAO, ConnectionFactory connectionFactory) {
		this.itemDAO = itemDAO;
		this.itemAttributeDAO = itemAttributeDAO;
		this.connectionFactory = connectionFactory;
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
		JSONArray jsonItems = new JSONArray();
		for(Item item : items) {
			JSONObject jsonItem= new JSONObject()
					.put("SKU", item.getSKU());
			String name = item.getName();
			if(name!=null)
				jsonItem.put("Name", name);
			else
				jsonItem.put("Name", JSONObject.NULL);
			if(item.getDescription()!=null)
				jsonItem.put("Description", item.getDescription());
			else
				jsonItem.put("Description", JSONObject.NULL);
			if(item.getThumbnail()!=null)
				jsonItem.put("Thumbnail", item.getThumbnail());
			else
				jsonItem.put("Thumbnail", JSONObject.NULL);
			jsonItems.put(jsonItem);
		}
		JSONObject json = new JSONObject()
				.put("items", jsonItems)
				.put("count", items.size());
		ResponseBuilder response = Response.ok(json);
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
		JSONArray jsonItems = new JSONArray();
		for(Item item : items) {
			JSONObject jsonItem= new JSONObject()
					.put("SKU", item.getSKU());
			if(item.getName()!=null)
				jsonItem.put("Name", item.getName());
			else
				jsonItem.put("Name", JSONObject.NULL);
			if(item.getDescription()!=null)
				jsonItem.put("Description", item.getDescription());
			else
				jsonItem.put("Description", JSONObject.NULL);
			if(item.getThumbnail()!=null)
				jsonItem.put("Thumbnail", item.getThumbnail());
			else
				jsonItem.put("Thumbnail", JSONObject.NULL);
			jsonItems.put(jsonItem);
		}
		JSONObject json = new JSONObject()
				.put("items", jsonItems)
				.put("count", items.size());
		ResponseBuilder response = Response.ok(json);
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
	public Response getItem(@PathParam("SKU") int SKU) throws DatabaseException {
		Item item = itemDAO.getItem(SKU);
		if(item==null)
			return Response.status(Status.NOT_FOUND).entity(null).build();
		JSONObject json = new JSONObject()
				.put("SKU", SKU);
		if(item.getName()!=null)
			json.put("Name", item.getName());
		else
			json.put("Name", JSONObject.NULL);
		if(item.getDescription()!=null)
			json.put("Description", item.getDescription());
		else
			json.put("Description", JSONObject.NULL);
		if(item.getThumbnail()!=null)
			json.put("Thumbnail", item.getThumbnail());
		else
			json.put("Thumbnail", JSONObject.NULL);
		return Response.ok().entity(json).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}")
	public Response postAddSKU(String body, @PathParam("SKU") int SKU) throws DatabaseException {
		Item item = new Item();
		item.setSKU(SKU);
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		try {
			item.setName(jsonObject.getString("Name"));
		}catch(JSONException e) {}
		try {
			item.setDescription(jsonObject.getString("Description"));
		}catch(JSONException e) {}
		try {
			item.setDescription(jsonObject.getString("Thumbnail"));
		}catch(JSONException e) {}
		boolean created = itemDAO.addItem(item);
		if(!created)
			return Response.status(Status.CONFLICT).entity(null).build();
		JSONObject json = new JSONObject()
				.put("SKU", SKU);
		if(item.getName()!=null)
			json.put("Name", item.getName());
		else
			json.put("Name", JSONObject.NULL);
		if(item.getDescription()!=null)
			json.put("Description", item.getDescription());
		else
			json.put("Description", JSONObject.NULL);
		if(item.getThumbnail()!=null)
			json.put("Thumbnail", item.getThumbnail());
		else
			json.put("Thumbnail", JSONObject.NULL);
		return Response.created(UriBuilder.fromMethod(ItemResource.class, "postAddSKU").build(SKU)).entity(json).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}")
	public Response putEditItem(String body, @PathParam("SKU") int SKU) throws DatabaseException {
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		Item item = new Item();
		Item defaultItem = itemDAO.getItem(SKU);
		if(defaultItem==null)
			return Response.status(Status.NOT_FOUND).entity(null).build();
		try {
			item.setSKU(jsonObject.getInt("SKU"));
			Log.debug("SKU from JSON");
		}catch(JSONException e) {
			item.setSKU(SKU);
			Log.debug("SKU from Param");
		}
		try {
			item.setName(jsonObject.getString("Name"));
			Log.debug("Name from JSON");
		}catch(JSONException e) {
			if(jsonObject.has("Name")){
				item.setName(null);
				Log.debug("Name from null");
			}else{
				item.setName(defaultItem.getName());
				Log.debug("Name from default");
			}
		}
		try {
			item.setDescription(jsonObject.getString("Description"));
			Log.debug("Description from JSON");
		}catch(JSONException e) {
			if(jsonObject.has("Description")){
				item.setDescription(null);
				Log.debug("Description from null");
			}else{
				item.setDescription(defaultItem.getDescription());
				Log.debug("Description from default");
			}
		}
		try {
			item.setThumbnail(jsonObject.getString("Thumbnail"));
			Log.debug("Thumbnail from JSON");
		}catch(JSONException e) {
			if(jsonObject.has("Thumbnail")){
				item.setThumbnail(null);
				Log.debug("Thumbnail from null");
			}else{
				item.setThumbnail(defaultItem.getDescription());
				Log.debug("Thumbnail from default");
			}
		}
		boolean updated = itemDAO.updateItem(SKU, item);
		if(updated){
			JSONObject json = new JSONObject()
					.put("SKU", SKU);
			if(item.getName()!=null)
				json.put("Name", item.getName());
			else
				json.put("Name", JSONObject.NULL);
			if(item.getDescription()!=null)
				json.put("Description", item.getDescription());
			else
				json.put("Description", JSONObject.NULL);
			if(item.getThumbnail()!=null)
				json.put("Thumbnail", item.getThumbnail());
			else
				json.put("Thumbnail", JSONObject.NULL);
			return Response.ok().entity(json).build();
		}else{
			return Response.status(Status.NOT_FOUND).entity(null).build();
		}
	}
	
	@DELETE
	@Path("{SKU}")
	public Response deleteItem(@PathParam("SKU") int SKU) throws DatabaseException {
		boolean deleted = itemDAO.deleteItem(SKU);
		if(deleted)
			return Response.noContent().build();
		else
			return Response.status(Status.NOT_FOUND).entity(null).build();
	}
	
	// ATTRIBUTE STARTS HERE
	
	@GET
	@Path("{SKU}/attribute")
	public Response getAllAttribute(@PathParam("SKU") int SKU) throws DatabaseException {
		List<ItemAttribute> attributes = itemAttributeDAO.getItemAttributeBySKU(SKU);
		JSONObject json = new JSONObject();
		JSONArray jsonAttributes = new JSONArray();
		for (ItemAttribute attribute : attributes) {
			JSONObject jsonAttribute = new JSONObject()
					.put("ID", attribute.getID())
					.put("SKU", SKU);
			if(attribute.getColor()!=null)
				jsonAttribute.put("Color", attribute.getColor());
			else
				jsonAttribute.put("Color", JSONObject.NULL);
			if(attribute.getSize()!=null)
				jsonAttribute.put("Size", attribute.getSize());
			else
				jsonAttribute.put("Size", JSONObject.NULL);
			jsonAttributes.put(jsonAttribute);
		}
		json.put("attributes", jsonAttributes)
			.put("count", attributes.size());
		return Response.ok(json).build();
	}
	
	@GET
	@Path("attribute/{ID}")
	public Response getAttribute(@PathParam("ID") int ID) throws DatabaseException {
		ItemAttribute itemAttribute = itemAttributeDAO.getItemAttribute(ID);
		if(itemAttribute==null)
			return Response.status(Status.NOT_FOUND).entity(null).build();
		JSONObject json = new JSONObject()
				.put("ID", ID)
				.put("SKU", itemAttribute.getSKU());
		if(itemAttribute.getColor()!=null)
			json.put("Color", itemAttribute.getColor());
		else
			json.put("Color", JSONObject.NULL);
		if(itemAttribute.getSize()!=null)
			json.put("Size", itemAttribute.getSize());
		else
			json.put("Size", JSONObject.NULL);
		return Response.ok(json).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}/attribute/{ID}")
	public Response postAddAttribute(String body, @PathParam("SKU") int SKU, @PathParam("ID") int ID) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("insert into ItemAttributeMaster (ItemAttributeID, SKU, Color, Size) values (?, ?, ?, ?)");
		PreparedStatement idCheck = connection.prepareStatement("select count(*) from ItemAttributeMaster where ItemAttributeID=?");
		idCheck.setInt(1, ID);
		ResultSet results = idCheck.executeQuery();
		results.next();
		if(results.getInt(1)==1) {
			return putEditAttribute(body, ID);
		}
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		statement.setInt(1, ID);
		statement.setInt(2, SKU);
		try {
			String name = jsonObject.getString("Color");
			statement.setString(3, name);
		}catch(JSONException e) {
			statement.setNull(3, Types.VARCHAR);
		}
		try {
			String name = jsonObject.getString("Size");
			statement.setString(4, name);
		}catch(JSONException e) {
			statement.setNull(4, Types.VARCHAR);
		}
		statement.executeUpdate();
		JSONObject json = new JSONObject()
				.put("sucess", "added attribute successfuly")
				.put("ID", ID)
				.put("URI", "/item/"+SKU+"/attribute/"+ID);
		return Response.created(URI.create("/item/"+SKU+"/attribute/"+ID)).entity(json).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}/attribute")
	public Response postAddAttributeNoID(String body, @PathParam("SKU") int SKU) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("insert into ItemAttributeMaster (SKU, Color, Size) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		statement.setInt(1, SKU);
		try {
			String name = jsonObject.getString("Color");
			statement.setString(2, name);
		}catch(JSONException e) {
			statement.setNull(2, Types.VARCHAR);
		}
		try {
			String name = jsonObject.getString("Size");
			statement.setString(3, name);
		}catch(JSONException e) {
			statement.setNull(3, Types.VARCHAR);
		}
		statement.executeUpdate();
		ResultSet keys = statement.getGeneratedKeys();
		keys.next();
		int ID = keys.getInt(1);
		JSONObject json = new JSONObject()
				.put("sucess", "added attribute successfuly")
				.put("ID", ID)
				.put("URI", "/item/"+SKU+"/attribute/"+ID);
		return Response.created(URI.create("/item/"+SKU+"/attribute/"+ID)).entity(json).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("attribute/{ID}")
	public Response putEditAttribute(String body, @PathParam("ID") int ID) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement idCheck = connection.prepareStatement("select count(*) from ItemAttributeMaster where ItemAttributeID=?");
		idCheck.setInt(1, ID);
		ResultSet results = idCheck.executeQuery();
		results.next();
		if(results.getInt(1)!=1) {
			JSONObject json = new JSONObject()
					.put("error", "ID not found")
					.put("token", ID);
			return Response.status(Status.NOT_FOUND).entity(json).build();
		}
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		try {
			String color = jsonObject.getString("Color");
			PreparedStatement statement = connection.prepareStatement("update ItemAttributeMaster set Color=? where ItemAttributeID=?");
			statement.setString(1, color);
			statement.setInt(2, ID);
			statement.executeUpdate();
		}catch(JSONException e) {
			if(jsonObject.has("Color")) {
				PreparedStatement statement = connection.prepareStatement("update ItemAttributeMaster set Color=? where ItemAttributeID=?");
				statement.setNull(1, Types.VARCHAR);
				statement.setInt(2, ID);
				statement.executeUpdate();
			}
		}
		try {
			String size = jsonObject.getString("Size");
			PreparedStatement statement = connection.prepareStatement("update ItemAttributeMaster set Size=? where ItemAttributeID=?");
			statement.setString(1, size);
			statement.setInt(2, ID);
			statement.executeUpdate();
		}catch(JSONException e) {
			if(jsonObject.has("Size")) {
				PreparedStatement statement = connection.prepareStatement("update ItemAttributeMaster set Size=? where ItemAttributeID=?");
				statement.setNull(1, Types.VARCHAR);
				statement.setInt(2, ID);
				statement.executeUpdate();
			}
		}
		JSONObject json = new JSONObject()
				.put("sucess", "updated attribute successfuly")
				.put("ID", ID).put("URI", "/item/attribute/"+ID);
		return Response.ok().entity(json).build();
	}
	
	@DELETE
	@Path("attribute/{ID}")
	public Response deleteAttribute(@PathParam("ID") int ID) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("delete from ItemAttributeMaster where ItemAttributeID=?");
		statement.setInt(1, ID);
		statement.executeUpdate();
		JSONObject json = new JSONObject()
				.put("sucess", "attribute deleted successfuly")
				.put("ID", ID);
		return Response.ok().entity(json).build();
	}
}
