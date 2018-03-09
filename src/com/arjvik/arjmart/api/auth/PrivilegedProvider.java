package com.arjvik.arjmart.api.auth;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

import org.glassfish.hk2.api.Factory;

public class PrivilegedProvider implements Factory<Boolean> {

	private boolean privileged;
	
	@Inject
	public PrivilegedProvider(ContainerRequestContext ctx){
		privileged = (boolean) ctx.getProperty("isPrivileged");
	}
	
	@Override
	public void dispose(Boolean arg0) {
		//Do nothing
	}

	@Override
	public Boolean provide() {
		return privileged;
	}

}
