package com.arjvik.arjmart.api.auth;

import java.util.function.Supplier;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

public class UserIDProvider implements Supplier<Integer> {

	private int userID;
	
	@Inject
	public UserIDProvider(ContainerRequestContext ctx){
		userID = (int) ctx.getProperty("userID");
	}
	
	@Override
	public Integer get() {
		return userID;
	}

}
