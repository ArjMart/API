package com.arjvik.arjmart.api.auth;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

import org.glassfish.hk2.api.Factory;

public class UserIDProvider implements Factory<Integer> {

	private int userID;
	
	@Inject
	public UserIDProvider(ContainerRequestContext ctx){
		userID = (int) ctx.getProperty("userID");
	}
	
	@Override
	public void dispose(Integer arg0) {
		//Do nothing
	}

	@Override
	public Integer provide() {
		return userID;
	}

}
