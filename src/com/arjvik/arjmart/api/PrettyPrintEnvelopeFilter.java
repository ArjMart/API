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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.jaxrs.cfg.EndpointConfigBase;
import com.fasterxml.jackson.jaxrs.cfg.ObjectWriterInjector;
import com.fasterxml.jackson.jaxrs.cfg.ObjectWriterModifier;

@Provider
@Priority(Priorities.ENTITY_CODER)
public class PrettyPrintEnvelopeFilter implements ContainerResponseFilter {
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		Object entity = responseContext.getEntity();
		UriInfo uriInfo = requestContext.getUriInfo();
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		if(queryParams.containsKey("callback") || queryParams.containsKey("envelope")){
			entity = getEnvelope(responseContext.getStatus(), responseContext.getStringHeaders(), entity);
			responseContext.setStatusInfo(Status.OK);
			if(queryParams.containsKey("callback")){
				entity = new CallbackEntity(queryParams.getFirst("callback"), entity);
			}
			responseContext.setEntity(entity);
		}
		if(queryParams.containsKey("pretty")){
			ObjectWriterInjector.set(new ObjectWriterModifier() {
				@Override
				public ObjectWriter modify(EndpointConfigBase<?> ecb, MultivaluedMap<String, Object> m, Object o, ObjectWriter ow, JsonGenerator jg) throws IOException {
					jg.useDefaultPrettyPrinter();
		            return ow;
				}
			});
		}
	}

	private EnvelopeEntity getEnvelope(int status, MultivaluedMap<String, String> headers, Object entity) {
		EnvelopeEntity envelope = new EnvelopeEntity(status, entity);
		for (String key : headers.keySet()) {
			List<String> value = headers.get(key);
			if(value.size()>1)
				envelope.putHeader(key, value);
			else
				envelope.putHeader(key, value.get(0));
		}
		return envelope;
	}
}
