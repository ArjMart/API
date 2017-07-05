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

@Path("/enterprise")
@Produces(MediaType.APPLICATION_JSON)
public class EnterpriseResource {
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
		PreparedStatement statement = connection.prepareStatement("select * from EnterpriseMaster limit ?");
		if(limit!=-1){
			statement.setInt(1, Math.min(Math.max(limit, 0), MAX_RECORDS));
		}else{
			statement.setInt(1, MAX_RECORDS);
		}
		ResultSet resultSet = statement.executeQuery();
		JSONObject json = new JSONObject();
		JSONArray enterprises = new JSONArray();
		int rowCount=0;
		while (resultSet.next()) {
			JSONObject enterprise= new JSONObject()
					.put("ID", resultSet.getInt("EnterpriseID"));
			String name = resultSet.getString("EnterpriseName");
			if(name!=null)
				enterprise.put("Name", name);
			else
				enterprise.put("Name",JSONObject.NULL);
			enterprises.put(enterprise);
			rowCount++;
		}
		json.put("enterprises", enterprises)
			.put("count", rowCount);
		return Response.ok(json.toString()).build();
	}
	
	@GET
	@Path("{ID}")
	public Response getEnterprise(@PathParam("ID") int ID) throws SQLException{
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from EnterpriseMaster where EnterpriseID=?");
		statement.setInt(1, ID);
		ResultSet resultSet = statement.executeQuery();
		if(!resultSet.next()){
			JSONObject json = new JSONObject()
					.put("error", "Enterprise ID not found")
					.put("token", ID);
			return Response.status(Status.NOT_FOUND).entity(json.toString()).build();
		}
		JSONObject json = new JSONObject()
				.put("ID", ID);
		String name = resultSet.getString("EnterpriseName");
		if(name!=null)
			json.put("Name", name);
		else
			json.put("Name",JSONObject.NULL);
		return Response.ok().entity(json.toString()).build();
	}
	
	private Connection getConnection() throws SQLException{
		Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PW);
		return connection;
	}
	
	@GET
	@Path("{ID}/{Property}")
	public Response getProperty(@PathParam("ID") int ID, @PathParam("Property") String enterpriseProperty) throws SQLException{
		Connection connection = getConnection();
		String sqlEnterpriseProperty;
		switch(enterpriseProperty.toLowerCase()){
		case "id":
			sqlEnterpriseProperty = "EnterpriseID";
			break;
		case "name":
			sqlEnterpriseProperty = "EnterpriseName";
			break;
		default:
			JSONObject json = new JSONObject()
					.put("error", "invalid property")
					.put("token", enterpriseProperty)
					.put("expected", 
							new JSONArray()
								.put("ID")
								.put("Name"));
			return Response.status(Status.BAD_REQUEST).entity(json.toString()).build();
		}
		PreparedStatement statement = connection.prepareStatement("select * from EnterpriseMaster where EnterpriseID=?");
		statement.setInt(1, ID);
		ResultSet resultSet = statement.executeQuery();
		if(!resultSet.next()){
			JSONObject json = new JSONObject()
					.put("error", "Enterprise ID not found")
					.put("token", ID);
			return Response.status(Status.NOT_FOUND).entity(json.toString()).build();
		}
		JSONObject json = new JSONObject();
		if(enterpriseProperty.equalsIgnoreCase("ID")){
			int value = resultSet.getInt(sqlEnterpriseProperty);
			if(value!=0)
				json.put(enterpriseProperty, value);
			else
				json.put(enterpriseProperty, JSONObject.NULL);
		}else{
			String value = resultSet.getString(sqlEnterpriseProperty);
			if(value!=null)
				json.put(enterpriseProperty, value);
			else
				json.put(enterpriseProperty, JSONObject.NULL);
		}
		return Response.ok().entity(json.toString()).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{ID}")
	public Response putAddEnterprise(String body, @PathParam("ID") int ID) throws SQLException{
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("insert into EnterpriseMaster (EnterpriseID,EnterpriseName) values (?,?)");
		PreparedStatement idCheck = connection.prepareStatement("select count(*) from EnterpriseMaster where EnterpriseID=?");
		idCheck.setInt(1, ID);
		ResultSet results = idCheck.executeQuery();
		results.next();
		if(results.getInt(1)==1){
			return postEditEnterprise(body,ID);
		}
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		statement.setInt(1, ID);
		try{
			String name = jsonObject.getString("Name");
			statement.setString(2, name);
		}catch(JSONException e){
			statement.setNull(2, Types.VARCHAR);
		}
		JSONObject json = new JSONObject()
				.put("sucess","added enterprise successfuly")
				.put("ID", ID)
				.put("URI", "/enterprise/"+ID);
		return Response.created(URI.create("/enterprise/"+ID)).entity(json.toString()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{ID}")
	public Response postEditEnterprise(String body, @PathParam("ID") int ID) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement idCheck = connection.prepareStatement("select count(*) from EnterpriseMaster where EnterpriseID=?");
		idCheck.setInt(1, ID);
		ResultSet results = idCheck.executeQuery();
		results.next();
		if(results.getInt(1)!=1){
			JSONObject json = new JSONObject()
					.put("error", "Enterprise ID not found")
					.put("token", ID);
			return Response.status(Status.NOT_FOUND).entity(json.toString()).build();
		}
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		try{
			String name = jsonObject.getString("Name");
			PreparedStatement statement = connection.prepareStatement("update EnterpriseMaster set EnterpriseName=? where EnterpriseID=?");
			statement.setString(1, name);
			statement.setInt(2, ID);
			statement.executeUpdate();
		}catch(JSONException e){
			if(jsonObject.has("Name")){
				PreparedStatement statement = connection.prepareStatement("update EnterpriseMaster set EnterpriseName=? where EnterpriseID=?");
				statement.setNull(1, Types.VARCHAR);
				statement.setInt(2, ID);
				statement.executeUpdate();
			}
		}
		JSONObject json = new JSONObject()
				.put("sucess","updated enterprise successfuly")
				.put("ID", ID)
				.put("URI", "/enterprise/"+ID);
		return Response.ok().entity(json.toString()).build();
	}
	
	@DELETE
	@Path("{ID}")
	public Response deleteEnterprise(@PathParam("ID") int ID) throws SQLException{
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("delete from EnterpriseMaster where EnterpriseID=?");
		statement.setInt(1, ID);
		statement.executeUpdate();
		JSONObject json = new JSONObject()
				.put("sucess", "enterprise deleted successfuly")
				.put("ID", ID);
		return Response.ok().entity(json.toString()).build();
	}
}