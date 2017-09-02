package com.arjvik.arjmart.api;

import java.io.IOException;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.json.JSONArray;

import com.arjvik.arjmart.api.JSONObject;

@Provider
@Priority(Priorities.ENTITY_CODER+1)
public class PrettyPrintEnvelopeFilter implements ContainerResponseFilter {

	private static final int NUM_SPACES_TO_INDENT = 2;
	
	@Context
	private UriInfo uriInfo;
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		Object obj = responseContext.getEntity();
		if(!(obj instanceof JSONObject))
			return;
		JSONObject json = (JSONObject) obj;
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		if(queryParams.containsKey("callback")||queryParams.containsKey("envelope")){
			JSONObject envelope = new JSONObject();
			int status = responseContext.getStatus();
			responseContext.setStatusInfo(Status.OK);
			envelope.put("status", status);
			MultivaluedMap<String, String> headers = responseContext.getStringHeaders();
			JSONObject jsonHeaders = new JSONObject();
			for (String headerName : headers.keySet()) {
				List<String> value = headers.get(headerName);
				if(value.size()==0){
					jsonHeaders.put(headerName, JSONObject.NULL);
				}else if(value.size()==1){
					jsonHeaders.put(headerName, value.get(0));
				}else{
					JSONArray valueArray = new JSONArray();
					for (String string : value) {
						valueArray.put(string);
					}
					jsonHeaders.put(headerName, valueArray);
				}
			}
			envelope.put("headers", jsonHeaders);
			envelope.put("response", json);
			String stringEnvelope;
			if(uriInfo.getQueryParameters().containsKey("pretty")){
				stringEnvelope = envelope.toString(NUM_SPACES_TO_INDENT);
			}else{
				stringEnvelope = envelope.toString();
			}
			if(queryParams.containsKey("callback")){
				responseContext.setEntity(queryParams.getFirst("callback")+"("+stringEnvelope+")");
			}else{
				responseContext.setEntity(stringEnvelope);
			}
		}else{
			if(queryParams.containsKey("pretty")){
				responseContext.setEntity(json.toString(NUM_SPACES_TO_INDENT));
			}else{
				responseContext.setEntity(json.toString());
			}
		}
	}

}
