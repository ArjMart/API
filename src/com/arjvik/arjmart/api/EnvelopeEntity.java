package com.arjvik.arjmart.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class EnvelopeEntity{
	private int status;
	private Map<String, Object> headers;
	private Object response;
	
	public EnvelopeEntity() {
		
	}
	public EnvelopeEntity(int status, Object response) {
		this(status, new HashMap<>(), response);
	}
	private EnvelopeEntity(int status, Map<String, Object> headers, Object response) {
		this.status = status;
		this.headers = headers;
		this.response = response;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void putHeader(String key, String value) {
		headers.put(key, value);
	}
	public void putHeader(String key, List<String> value) {
		headers.put(key, value);
	}
	public Map<String, Object> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((headers == null) ? 0 : headers.hashCode());
		result = prime * result + ((response == null) ? 0 : response.hashCode());
		result = prime * result + status;
		return result;
	}
	@Override
	public String toString() {
		return "EnvelopeEntity [status=" + status + ", headers=" + headers + ", response=" + response + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnvelopeEntity other = (EnvelopeEntity) obj;
		if (headers == null) {
			if (other.headers != null)
				return false;
		} else if (!headers.equals(other.headers))
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
}