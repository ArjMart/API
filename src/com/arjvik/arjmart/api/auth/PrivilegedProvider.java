package com.arjvik.arjmart.api.auth;

import java.util.function.Supplier;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

public class PrivilegedProvider implements Supplier<Boolean> {

	private boolean privileged;
	
	@Inject
	public PrivilegedProvider(ContainerRequestContext ctx){
		privileged = (boolean) ctx.getProperty("isPrivileged");
	}

	@Override
	public Boolean get() {
		return privileged;
	}

}
