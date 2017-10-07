package com.arjvik.arjmart.api;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class CallbackEntity implements JsonSerializable{
	private String callback;
	private Object entity;
	
	public CallbackEntity() {
		
	}
	public CallbackEntity(String callback, Object entity) {
		this.callback = callback;
		this.entity = entity;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public Object getEntity() {
		return entity;
	}
	public void setEntity(Object entity) {
		this.entity = entity;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		result = prime * result + ((callback == null) ? 0 : callback.hashCode());
		return result;
	}
	@Override
	public String toString() {
		return new StringBuilder()
				   .append(callback)
				   .append("(")
				   .append(entity.toString())
				   .append(")")
				   .toString();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CallbackEntity other = (CallbackEntity) obj;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		if (callback == null) {
			if (other.callback != null)
				return false;
		} else if (!callback.equals(other.callback))
			return false;
		return true;
	}
	@Override
	public void serialize(JsonGenerator jg, SerializerProvider sProvider) throws IOException {
		jg.writeRaw(callback);
		jg.writeRaw("(");
		jg.writeObject(entity);
		jg.writeRaw(")");
	}
	@Override
	public void serializeWithType(JsonGenerator jGenerator, SerializerProvider sProvider, TypeSerializer tSerializer) throws IOException {
		serialize(jGenerator,sProvider);
	}
	
}
