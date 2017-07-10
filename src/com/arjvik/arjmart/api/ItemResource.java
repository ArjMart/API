package com.arjvik.arjmart.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {
	private static final int MAX_RECORDS = 100;
	private final String DB_URL = "jdbc:mysql://db1.clwnpjvhytsb.us-west-2.rds.amazonaws.com:3306/arjmart",
			DB_USER = "root";
	private transient String DB_PW;
	private static Logger logger;
	@Context
	ServletContext servletContext;
	
	@PostConstruct
	public void init() {
		logger = Logger.getLogger(this.getClass().getName());
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE,"Error registering JDBC driver", e);
		}
		try{
			if(System.getProperties().containsKey("com.arjvik.arjmart.api.DB_PW")){
				DB_PW = System.getProperty("com.arjvik.arjmart.api.DB_PW");
			}else{
				String path = servletContext.getRealPath("/WEB-INF/passwords.txt");
				File file = new File(path);
				Scanner scanner = new Scanner(file);
				DB_PW = scanner.nextLine();
				scanner.close();
				System.setProperty("com.arjvik.arjmart.api.DB_PW", DB_PW);
			}
		} catch(FileNotFoundException e){
			DB_PW=null;
			logger.log(Level.SEVERE,"Error reading DB Password", e);
		}
	}
	
	@GET
	public Response getAll(@DefaultValue("-1") @QueryParam("limit") int limit) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from ItemMaster limit ?");
		if(limit!=-1){
			statement.setInt(1, Math.min(Math.max(limit, 0), MAX_RECORDS));
		}else{
			statement.setInt(1, MAX_RECORDS);
		}
		ResultSet resultSet = statement.executeQuery();
		JSONObject json = new JSONObject();
		JSONArray items = new JSONArray();
		int rowCount=0;
		while (resultSet.next()) {
			JSONObject item= new JSONObject()
					.put("SKU", resultSet.getInt("SKU"));
			String name = resultSet.getString("ItemName");
			if(name!=null)
				item.put("Name", name);
			else
				item.put("Name",JSONObject.NULL);
			String description = resultSet.getString("ItemDescription");
			if(description!=null)
				item.put("Description", description);
			else
				item.put("Description",JSONObject.NULL);
			String thumbnail = resultSet.getString("ItemThumbnails");
			if(thumbnail!=null)
				item.put("Thumbnail", thumbnail);
			else
				item.put("Thumbnail",JSONObject.NULL);
			items.put(item);
			rowCount++;
		}
		json.put("items", items)
			.put("count", rowCount);
		return Response.ok(json.toString()).build();
	}
	
	@GET
	public Response getSearch(@QueryParam("query") String query, @DefaultValue("-1") @QueryParam("limit") int limit) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from ItemMaster where ItemName like ? escape '|' limit ?");
		String escapedQuery="%"+query.replace("%", "|%").replace("_", "|_").replace(' ', '%')+"%";
		statement.setString(1, escapedQuery);
		if(limit!=-1){
			statement.setInt(2, Math.min(Math.max(limit, 0), MAX_RECORDS));
		}else{
			statement.setInt(2, MAX_RECORDS);
		}
		ResultSet resultSet = statement.executeQuery();
		JSONObject json = new JSONObject();
		JSONArray items = new JSONArray();
		int rowCount=0;
		while (resultSet.next()) {
			JSONObject item= new JSONObject()
					.put("SKU", resultSet.getInt("SKU"));
			String name = resultSet.getString("ItemName");
			if(name!=null)
				item.put("Name", name);
			else
				item.put("Name",JSONObject.NULL);
			String description = resultSet.getString("ItemDescription");
			if(description!=null)
				item.put("Description", description);
			else
				item.put("Description",JSONObject.NULL);
			String thumbnail = resultSet.getString("ItemThumbnails");
			if(thumbnail!=null)
				item.put("Thumbnail", thumbnail);
			else
				item.put("Thumbnail",JSONObject.NULL);
			items.put(item);
			rowCount++;
		}
		json.put("items", items)
			.put("count", rowCount);
		return Response.ok(json.toString()).build();
	}
	
	@GET
	@Path("{SKU}")
	public Response getItem(@PathParam("SKU") int SKU) throws SQLException{
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from ItemMaster where SKU=?");
		statement.setInt(1, SKU);
		ResultSet resultSet = statement.executeQuery();
		if(!resultSet.next()){
			JSONObject json = new JSONObject()
					.put("error", "SKU not found")
					.put("token", SKU);
			return Response.status(Status.NOT_FOUND).entity(json.toString()).build();
		}
		JSONObject json = new JSONObject()
				.put("SKU", SKU);
		String name = resultSet.getString("ItemName");
		if(name!=null)
			json.put("Name", name);
		else
			json.put("Name",JSONObject.NULL);
		String description = resultSet.getString("ItemDescription");
		if(description!=null)
			json.put("Description", description);
		else
			json.put("Description",JSONObject.NULL);
		String thumbnail = resultSet.getString("ItemThumbnails");
		if(thumbnail!=null)
			json.put("Thumbnail", thumbnail);
		else
			json.put("Thumbnail",JSONObject.NULL);
		return Response.ok().entity(json.toString()).build();
	}
	
	private Connection getConnection() throws SQLException{
		Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PW);
		return connection;
	}
	
	@GET
	@Path("{SKU}/{Property}")
	public Response getProperty(@PathParam("SKU") int SKU, @PathParam("Property") String itemProperty) throws SQLException{
		Connection connection = getConnection();
		String sqlItemProperty;
		switch(itemProperty.toLowerCase()){
		case "sku":
			sqlItemProperty = "SKU";
			break;
		case "name":
			sqlItemProperty = "ItemName";
			break;
		case "description":
			sqlItemProperty = "ItemDescription";
			break;
		case "thumbnail":
			sqlItemProperty = "ItemThumbnails";
			break;
		default:
			JSONObject json = new JSONObject()
					.put("error", "invalid property")
					.put("token", itemProperty)
					.put("expected", 
							new JSONArray()
								.put("SKU")
								.put("Name")
								.put("Description")
								.put("Thumbnail"));
			return Response.status(Status.BAD_REQUEST).entity(json.toString()).build();
		}
		PreparedStatement statement = connection.prepareStatement("select * from ItemMaster where SKU=?");
		statement.setInt(1, SKU);
		ResultSet resultSet = statement.executeQuery();
		if(!resultSet.next()){
			JSONObject json = new JSONObject()
					.put("error", "SKU not found")
					.put("token", SKU);
			return Response.status(Status.NOT_FOUND).entity(json.toString()).build();
		}
		JSONObject json = new JSONObject();
		if(itemProperty.equalsIgnoreCase("SKU")){
			int value = resultSet.getInt(sqlItemProperty);
			if(value!=0)
				json.put(itemProperty, value);
			else
				json.put(itemProperty, JSONObject.NULL);
		}else{
			String value = resultSet.getString(sqlItemProperty);
			if(value!=null)
				json.put(itemProperty, value);
			else
				json.put(itemProperty, JSONObject.NULL);
		}
		return Response.ok().entity(json.toString()).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}")
	public Response putAddSKU(String body, @PathParam("SKU") int SKU) throws SQLException{
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("insert into ItemMaster (SKU,ItemName,ItemDescription,ItemThumbnails) values (?,?,?,?)");
		PreparedStatement skuCheck = connection.prepareStatement("select count(*) from ItemMaster where SKU=?");
		skuCheck.setInt(1, SKU);
		ResultSet results = skuCheck.executeQuery();
		results.next();
		if(results.getInt(1)==1){
			return postEditItem(body,SKU);
		}
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		statement.setInt(1, SKU);
		try{
			String name = jsonObject.getString("Name");
			statement.setString(2, name);
		}catch(JSONException e){
			statement.setNull(2, Types.VARCHAR);
		}
		try{
			String description = jsonObject.getString("Description");
			statement.setString(3, description);
		}catch(JSONException e){
			statement.setNull(3, Types.VARCHAR);
		}
		try{
			String thumbnail = jsonObject.getString("Thumbnail");
			statement.setString(4, thumbnail);
		}catch(JSONException e){
			statement.setNull(4, Types.VARCHAR);
		}
		statement.executeUpdate();
		JSONObject json = new JSONObject()
				.put("sucess","added item successfuly")
				.put("SKU", SKU)
				.put("URI", "/item/"+SKU);
		return Response.created(URI.create("/item/"+SKU)).entity(json.toString()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{SKU}")
	public Response postEditItem(String body, @PathParam("SKU") int SKU) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement skuCheck = connection.prepareStatement("select count(*) from ItemMaster where SKU=?");
		skuCheck.setInt(1, SKU);
		ResultSet results = skuCheck.executeQuery();
		results.next();
		if(results.getInt(1)!=1){
			JSONObject json = new JSONObject()
					.put("error", "SKU not found")
					.put("token", SKU);
			return Response.status(Status.NOT_FOUND).entity(json.toString()).build();
		}
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		try{
			String name = jsonObject.getString("Name");
			PreparedStatement statement = connection.prepareStatement("update ItemMaster set ItemName=? where SKU=?");
			statement.setString(1, name);
			statement.setInt(2, SKU);
			statement.executeUpdate();
		}catch(JSONException e){
			if(jsonObject.has("Name")){
				PreparedStatement statement = connection.prepareStatement("update ItemMaster set ItemName=? where SKU=?");
				statement.setNull(1, Types.VARCHAR);
				statement.setInt(2, SKU);
				statement.executeUpdate();
			}
		}
		try{
			String description = jsonObject.getString("Description");
			PreparedStatement statement = connection.prepareStatement("update ItemMaster set ItemDescription=? where SKU=?");
			statement.setString(1, description);
			statement.setInt(2, SKU);
			statement.executeUpdate();
		}catch(JSONException e){
			if(jsonObject.has("Description")){
				PreparedStatement statement = connection.prepareStatement("update ItemMaster set ItemDescription=? where SKU=?");
				statement.setNull(1, Types.VARCHAR);
				statement.setInt(2, SKU);
				statement.executeUpdate();
			}
		}
		try{
			String thumbnails = jsonObject.getString("Thumbnail");
			PreparedStatement statement = connection.prepareStatement("update ItemMaster set ItemThumbnails=? where SKU=?");
			statement.setString(1, thumbnails);
			statement.setInt(2, SKU);
			statement.executeUpdate();
		}catch(JSONException e){
			if(jsonObject.has("Thumbnail")){
				PreparedStatement statement = connection.prepareStatement("update ItemMaster set ItemThumbnails=? where SKU=?");
				statement.setNull(1, Types.VARCHAR);
				statement.setInt(2, SKU);
				statement.executeUpdate();
			}
		}
		JSONObject json = new JSONObject()
				.put("sucess","updated item successfuly")
				.put("SKU", SKU)
				.put("URI", "/item/"+SKU);
		return Response.ok().entity(json.toString()).build();
	}
	
	@DELETE
	@Path("{SKU}")
	public Response deleteItem(@PathParam("SKU") int SKU) throws SQLException{
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("delete from ItemMaster where SKU=?");
		statement.setInt(1, SKU);
		statement.executeUpdate();
		JSONObject json = new JSONObject()
				.put("sucess", "item deleted successfuly")
				.put("SKU", SKU);
		return Response.ok().entity(json.toString()).build();
	}
}
