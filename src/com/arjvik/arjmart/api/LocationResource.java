package com.arjvik.arjmart.api;

import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
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

import com.arjvik.arjmart.api.jdbc.ConnectionFactory;

@Path("/location")
@Produces(MediaType.APPLICATION_JSON)
public class LocationResource {
	private static final int MAX_RECORDS = 100;
	
	private ConnectionFactory connectionFactory;
	
	@Inject
	public LocationResource(ConnectionFactory connectionFactory){
		this.connectionFactory = connectionFactory;
	}
	
	@GET
	public Response getAll(@DefaultValue("-1") @QueryParam("limit") int limit) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from LocationMaster limit ?");
		if(limit!=-1){
			statement.setInt(1, Math.min(Math.max(limit, 0), MAX_RECORDS));
		}else{
			statement.setInt(1, MAX_RECORDS);
		}
		ResultSet resultSet = statement.executeQuery();
		JSONObject json = new JSONObject();
		JSONArray locations = new JSONArray();
		int rowCount=0;
		while (resultSet.next()) {
			JSONObject location = new JSONObject()
					.put("ID", resultSet.getInt("LocationID"));
			int eid = resultSet.getInt("EnterpriseID");
			location.put("Enterprise-ID", eid);
			if(eid!=0)
				location.put("Enterprise-ID", eid);
			else
				location.put("Enterprise-ID", JSONObject.NULL);
			String address = resultSet.getString("Address");
			if(address!=null)
				location.put("Address", address);
			else
				location.put("Address", JSONObject.NULL);
			locations.put(location);
			rowCount++;
		}
		json.put("locations", locations)
			.put("count", rowCount);
		return Response.ok(json).build();
	}
	
	@GET
	@Path("{ID}")
	public Response getLocation(@PathParam("ID") int ID) throws SQLException{
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from LocationMaster where LocationID=?");
		statement.setInt(1, ID);
		ResultSet resultSet = statement.executeQuery();
		if(!resultSet.next()){
			JSONObject json = new JSONObject()
					.put("error", "Location ID not found")
					.put("token", ID);
			return Response.status(Status.NOT_FOUND).entity(json).build();
		}
		JSONObject json = new JSONObject()
				.put("ID", ID);
		int eid = resultSet.getInt("EnterpriseID");
		json.put("Enterprise-ID", eid);
		if(eid!=0)
			json.put("Enterprise-ID", eid);
		else
			json.put("Enterprise-ID", JSONObject.NULL);
		String address = resultSet.getString("Address");
		if(address!=null)
			json.put("Address", address);
		else
			json.put("Address", JSONObject.NULL);
		return Response.ok().entity(json).build();
	}
	
	@GET
	@Path("{ID}/{Property}")
	public Response getProperty(@PathParam("ID") int ID, @PathParam("Property") String locationProperty) throws SQLException{
		Connection connection = connectionFactory.getConnection();
		String sqlEnterpriseProperty;
		switch(locationProperty.toLowerCase()){
		case "id":
			sqlEnterpriseProperty = "LocationID";
			break;
		case "enterprise-id":
			sqlEnterpriseProperty = "EnterpriseID";
			break;
		default:
			JSONObject json = new JSONObject()
					.put("error", "invalid property")
					.put("token", locationProperty)
					.put("expected", 
							new JSONArray()
								.put("ID")
								.put("Enterprise-ID")
								.put("Address"));
			return Response.status(Status.BAD_REQUEST).entity(json).build();
		}
		PreparedStatement statement = connection.prepareStatement("select * from LocationMaster where LocationID=?");
		statement.setInt(1, ID);
		ResultSet resultSet = statement.executeQuery();
		if(!resultSet.next()){
			JSONObject json = new JSONObject()
					.put("error", "Location ID not found")
					.put("token", ID);
			return Response.status(Status.NOT_FOUND).entity(json).build();
		}
		JSONObject json = new JSONObject();
		if(locationProperty.equalsIgnoreCase("ID")||locationProperty.equalsIgnoreCase("Enterprise-ID")){
			int value = resultSet.getInt(sqlEnterpriseProperty);
			if(value!=0)
				json.put(locationProperty, value);
			else
				json.put(locationProperty, JSONObject.NULL);
		}else{
			String value = resultSet.getString(sqlEnterpriseProperty);
			if(value!=null)
				json.put(locationProperty, value);
			else
				json.put(locationProperty, JSONObject.NULL);
		}
		return Response.ok().entity(json).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{ID}")
	public Response putAddLocation(String body, @PathParam("ID") int ID) throws SQLException{
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("insert into LocationMaster (LocationID, EnterpriseID, Address) values (?, ?, ?)");
		PreparedStatement idCheck = connection.prepareStatement("select count(*) from LocationMaster where LocationID=?");
		idCheck.setInt(1, ID);
		ResultSet results = idCheck.executeQuery();
		results.next();
		if(results.getInt(1)==1){
			return postEditLocation(body, ID);
		}
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		statement.setInt(1, ID);
		try{
			int eid = jsonObject.getInt("Enterprise-ID");
			PreparedStatement eidCheck = connection.prepareStatement("select count(*) from EnterpriseMaster where EnterpriseID=?");
			eidCheck.setInt(1, eid);
			ResultSet eidresults = eidCheck.executeQuery();
			eidresults.next();
			if(eidresults.getInt(1)!=1){
				JSONObject json = new JSONObject()
						.put("error", "Enterprise ID not found")
						.put("token", eid);
				return Response.status(Status.NOT_FOUND).entity(json).build();
			}
			statement.setInt(2, eid);
		}catch(JSONException e){
			JSONObject json = new JSONObject()
					.put("error", "Enterprise ID not provided");
			return Response.status(Status.BAD_REQUEST).entity(json).build();
		}
		try{
			String address = jsonObject.getString("Address");
			statement.setString(3, address);
		}catch(JSONException e){
			statement.setNull(3, Types.VARCHAR);
		}
		statement.executeUpdate();
		JSONObject json = new JSONObject()
				.put("sucess", "added location successfuly")
				.put("ID", ID)
				.put("URI", "/location/"+ID);
		return Response.created(URI.create("/location/"+ID)).entity(json).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putAddLocationNoID(String body) throws SQLException{
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("insert into LocationMaster (EnterpriseID, Address) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		try{
			int eid = jsonObject.getInt("Enterprise-ID");
			PreparedStatement eidCheck = connection.prepareStatement("select count(*) from EnterpriseMaster where EnterpriseID=?");
			eidCheck.setInt(1, eid);
			ResultSet eidresults = eidCheck.executeQuery();
			eidresults.next();
			if(eidresults.getInt(1)!=1){
				JSONObject json = new JSONObject()
						.put("error", "Enterprise ID not found")
						.put("token", eid);
				return Response.status(Status.NOT_FOUND).entity(json).build();
			}
			statement.setInt(1, eid);
		}catch(JSONException e){
			JSONObject json = new JSONObject()
					.put("error", "Enterprise ID not provided");
			return Response.status(Status.BAD_REQUEST).entity(json).build();
		}
		try{
			String address = jsonObject.getString("Address");
			statement.setString(2, address);
		}catch(JSONException e){
			statement.setNull(2, Types.VARCHAR);
		}
		statement.executeUpdate();
		ResultSet keys = statement.getGeneratedKeys();
		keys.next();
		int ID = keys.getInt(1);
		JSONObject json = new JSONObject()
				.put("sucess", "added location successfuly")
				.put("ID", ID)
				.put("URI", "/location/"+ID);
		return Response.created(URI.create("/location/"+ID)).entity(json).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{ID}")
	public Response postEditLocation(String body, @PathParam("ID") int ID) throws SQLException {
		Connection connection = connectionFactory.getConnection();
		PreparedStatement idCheck = connection.prepareStatement("select count(*) from LocationMaster where LocationID=?");
		idCheck.setInt(1, ID);
		ResultSet results = idCheck.executeQuery();
		results.next();
		if(results.getInt(1)!=1){
			JSONObject json = new JSONObject()
					.put("error", "Location ID not found")
					.put("token", ID);
			return Response.status(Status.NOT_FOUND).entity(json).build();
		}
		JSONObject jsonObject = new JSONObject(new JSONTokener(body));
		try{
			int eid = jsonObject.getInt("Enterprise-ID");
			PreparedStatement eidCheck = connection.prepareStatement("select count(*) from EnterpriseMaster where EnterpriseID=?");
			eidCheck.setInt(1, eid);
			ResultSet eidresults = eidCheck.executeQuery();
			eidresults.next();
			if(eidresults.getInt(1)==1){
				JSONObject json = new JSONObject()
						.put("error", "Enterprise ID not found")
						.put("token", eid);
				return Response.status(Status.NOT_FOUND).entity(json).build();
			}
			PreparedStatement statement = connection.prepareStatement("update LocationMaster set EnterpriseID=? where LocationID=?");
			statement.setInt(1, eid);
			statement.setInt(2, ID);
			statement.executeUpdate();
		}catch(JSONException e){}
		try{
			String address = jsonObject.getString("Address");
			PreparedStatement statement = connection.prepareStatement("update LocationMaster set Address=? where LocationID=?");
			statement.setString(1, address);
			statement.setInt(2, ID);
			statement.executeUpdate();
		}catch(JSONException e){
			if(jsonObject.has("Name")){
				PreparedStatement statement = connection.prepareStatement("update LocationMaster set Address=? where LocationID=?");
				statement.setNull(1, Types.VARCHAR);
				statement.setInt(2, ID);
				statement.executeUpdate();
			}
		}
		JSONObject json = new JSONObject()
				.put("sucess", "updated location successfuly")
				.put("ID", ID)
				.put("URI", "/location/"+ID);
		return Response.ok().entity(json).build();
	}
	
	@DELETE
	@Path("{ID}")
	public Response deleteEnterprise(@PathParam("ID") int ID) throws SQLException{
		Connection connection = connectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement("delete from LocationMaster where LocationID=?");
		statement.setInt(1, ID);
		statement.executeUpdate();
		JSONObject json = new JSONObject()
				.put("sucess", "location deleted successfuly")
				.put("ID", ID);
		return Response.ok().entity(json).build();
	}
}
