package com.arjvik.arjmart.api.hk2;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.arjvik.arjmart.api.jdbc.ConnectionFactory;

public class ConnectionFactoryBinder extends AbstractBinder {
	@Override
	protected void configure() {
		bind(ConnectionFactory.class).to(ConnectionFactory.class).in(Singleton.class);
	}
}
