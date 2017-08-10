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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {
	
	private ItemDAO itemDAO;
	private ConnectionFactory connectionFactory;
	
	@Inject
	public ItemResource(ItemDAO itemDAO, ConnectionFactory connectionFactory) {
		this.itemDAO = itemDAO;
		this.connectionFactory = connectionFactory;
	}
	
	@GET
	public Response getAll(@DefaultValue("-1") @QueryParam("limit") int limit, @QueryParam("query") String query) throws DatabaseException {
		if(query!=null)
			return getSearch(limit, query);
		List<Item> items = itemDAO.getAllItems(limit);
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
		return Response.ok(json.toString()).build();
	}
	
	public Response getSearch(int limit, String query) throws DatabaseException {
		List<Item> items = itemDAO.searchItems(limit, query);
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
		return Response.ok(json.toString()).build();
	}
	
	@GET
	@Path("{SKU}")
	public Response getItem(@PathParam("SKU") int SKU) throws DatabaseException {
		Item item = itemDAO.getItem(SKU);
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
		return Response.ok().entity(json.toString()).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}")
	public Response putAddSKU(String body, @PathParam("SKU") int SKU) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("insert into ItemMaster (SKU, ItemName, ItemDescription, ItemThumbnails) values (?, ?, ?, ?)");
		PreparedStatement skuCheck = connection.prepareStatement("select count(*) from ItemMaster where SKU=?");
		skuCheck.setInt(1, SKU);
		ResultSet results = skuCheck.executeQuery();
		results.next();
		if(results.getInt(1)==1) {
			return postEditItem(body, SKU);
		}
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		statement.setInt(1, SKU);
		try {
			String name = jsonObject.getString("Name");
			statement.setString(2, name);
		}catch(JSONException e) {
			statement.setNull(2, Types.VARCHAR);
		}
		try {
			String description = jsonObject.getString("Description");
			statement.setString(3, description);
		}catch(JSONException e) {
			statement.setNull(3, Types.VARCHAR);
		}
		try {
			String thumbnail = jsonObject.getString("Thumbnail");
			statement.setString(4, thumbnail);
		}catch(JSONException e) {
			statement.setNull(4, Types.VARCHAR);
		}
		statement.executeUpdate();
		JSONObject json = new JSONObject()
				.put("sucess", "added item successfuly")
				.put("SKU", SKU)
				.put("URI", "/item/"+SKU);
		return Response.created(URI.create("/item/"+SKU)).entity(json.toString()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}")
	public Response postEditItem(String body, @PathParam("SKU") int SKU) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement skuCheck = connection.prepareStatement("select count(*) from ItemMaster where SKU=?");
		skuCheck.setInt(1, SKU);
		ResultSet results = skuCheck.executeQuery();
		results.next();
		if(results.getInt(1)!=1) {
			JSONObject json = new JSONObject()
					.put("error", "SKU not found")
					.put("token", SKU);
			return Response.status(Status.NOT_FOUND).entity(json.toString()).build();
		}
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		try {
			String name = jsonObject.getString("Name");
			PreparedStatement statement = connection.prepareStatement("update ItemMaster set ItemName=? where SKU=?");
			statement.setString(1, name);
			statement.setInt(2, SKU);
			statement.executeUpdate();
		}catch(JSONException e) {
			if(jsonObject.has("Name")) {
				PreparedStatement statement = connection.prepareStatement("update ItemMaster set ItemName=? where SKU=?");
				statement.setNull(1, Types.VARCHAR);
				statement.setInt(2, SKU);
				statement.executeUpdate();
			}
		}
		try {
			String description = jsonObject.getString("Description");
			PreparedStatement statement = connection.prepareStatement("update ItemMaster set ItemDescription=? where SKU=?");
			statement.setString(1, description);
			statement.setInt(2, SKU);
			statement.executeUpdate();
		}catch(JSONException e) {
			if(jsonObject.has("Description")) {
				PreparedStatement statement = connection.prepareStatement("update ItemMaster set ItemDescription=? where SKU=?");
				statement.setNull(1, Types.VARCHAR);
				statement.setInt(2, SKU);
				statement.executeUpdate();
			}
		}
		try {
			String thumbnails = jsonObject.getString("Thumbnail");
			PreparedStatement statement = connection.prepareStatement("update ItemMaster set ItemThumbnails=? where SKU=?");
			statement.setString(1, thumbnails);
			statement.setInt(2, SKU);
			statement.executeUpdate();
		}catch(JSONException e) {
			if(jsonObject.has("Thumbnail")) {
				PreparedStatement statement = connection.prepareStatement("update ItemMaster set ItemThumbnails=? where SKU=?");
				statement.setNull(1, Types.VARCHAR);
				statement.setInt(2, SKU);
				statement.executeUpdate();
			}
		}
		JSONObject json = new JSONObject()
				.put("sucess", "updated item successfuly")
				.put("SKU", SKU)
				.put("URI", "/item/"+SKU);
		return Response.ok().entity(json.toString()).build();
	}
	
	@DELETE
	@Path("{SKU}")
	public Response deleteItem(@PathParam("SKU") int SKU) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("delete from ItemMaster where SKU=?");
		statement.setInt(1, SKU);
		statement.executeUpdate();
		JSONObject json = new JSONObject()
				.put("sucess", "item deleted successfuly")
				.put("SKU", SKU);
		return Response.ok().entity(json.toString()).build();
	}
	
	@GET
	@Path("{SKU}/attribute")
	public Response getAllAttribute(@PathParam("SKU") int SKU) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from ItemAttributeMaster where SKU=?");
		statement.setInt(1, SKU);
		ResultSet resultSet = statement.executeQuery();
		JSONObject json = new JSONObject();
		JSONArray attributes = new JSONArray();
		int rowCount=0;
		while (resultSet.next()) {
			JSONObject attribute= new JSONObject()
					.put("ID", resultSet.getInt("ItemAttributeID"));
			String color = resultSet.getString("Color");
			if(color!=null)
				attribute.put("Color", color);
			else
				attribute.put("Color", JSONObject.NULL);
			String size = resultSet.getString("Size");
			if(size!=null)
				attribute.put("Size", size);
			else
				attribute.put("size", JSONObject.NULL);
			
			attributes.put(attribute);
			rowCount++;
		}
		json.put("attributes", attributes)
			.put("count", rowCount);
		return Response.ok(json.toString()).build();
	}
	
	@GET
	@Path("{SKU}/attribute/{ID}")
	public Response getAttribute(@PathParam("SKU") int SKU, @PathParam("ID") int ID) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from ItemAttributeMaster where ItemAttributeID=?");
		statement.setInt(1, ID);
		ResultSet resultSet = statement.executeQuery();
		JSONObject json = new JSONObject()
				.put("ID", resultSet.getInt("ItemAttributeID"))
				.put("SKU", resultSet.getInt("SKU"));
		String color = resultSet.getString("Color");
		if(color!=null)
			json.put("Color", color);
		else
			json.put("Color", JSONObject.NULL);
		String size = resultSet.getString("Size");
		if(size!=null)
			json.put("Size", size);
		else
			json.put("size", JSONObject.NULL);
		return Response.ok(json.toString()).build();
	}
	
	@GET
	@Path("attribute/{ID}")
	public Response getAttribute0(@PathParam("ID") int ID) throws SQLException {
		return getAttribute(-1, ID);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}/attribute/{ID}")
	public Response putAddAttribute(String body, @PathParam("SKU") int SKU, @PathParam("ID") int ID) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("insert into ItemAttributeMaster (ItemAttributeID, SKU, Color, Size) values (?, ?, ?, ?)");
		PreparedStatement idCheck = connection.prepareStatement("select count(*) from ItemAttributeMaster where ItemAttributeID=?");
		idCheck.setInt(1, ID);
		ResultSet results = idCheck.executeQuery();
		results.next();
		if(results.getInt(1)==1) {
			return postEditAttribute(body, SKU, ID);
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
		return Response.created(URI.create("/item/"+SKU+"/attribute/"+ID)).entity(json.toString()).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}/attribute")
	public Response putAddAttributeNoID(String body, @PathParam("SKU") int SKU) throws SQLException {
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
		return Response.created(URI.create("/item/"+SKU+"/attribute/"+ID)).entity(json.toString()).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}/attribute/{ID}")
	public Response postEditAttribute(String body, @PathParam("SKU") int SKU, @PathParam("ID") int ID) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement idCheck = connection.prepareStatement("select count(*) from ItemAttributeMaster where ItemAttributeID=?");
		idCheck.setInt(1, ID);
		ResultSet results = idCheck.executeQuery();
		results.next();
		if(results.getInt(1)!=1) {
			JSONObject json = new JSONObject()
					.put("error", "ID not found")
					.put("token", ID);
			return Response.status(Status.NOT_FOUND).entity(json.toString()).build();
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
				.put("ID", ID);
		if(SKU!=-1)
			json.put("URI", "/item/"+SKU+"/attribute/"+ID);
		else
			json.put("URI", "/item/attribute/"+ID);
		return Response.ok().entity(json.toString()).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("attribute/{ID}")
	public Response postEditAttribute0(String body, @PathParam("ID") int ID) throws SQLException {
		return postEditAttribute(body, -1, ID);
	}
	
	@DELETE
	@Path("{SKU}/attribute/{ID}")
	public Response deleteAttribute(@PathParam("SKU") int SKU, @PathParam("ID") int ID) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("delete from ItemAttributeMaster where ItemAttributeID=?");
		statement.setInt(1, ID);
		statement.executeUpdate();
		JSONObject json = new JSONObject()
				.put("sucess", "attribute deleted successfuly")
				.put("ID", ID);
		return Response.ok().entity(json.toString()).build();
	}
	
	@DELETE
	@Path("attribute/{ID}")
	public Response deleteAttribute0(@PathParam("ID") int ID) throws SQLException {
		return deleteAttribute(-1, ID);
	}
}
