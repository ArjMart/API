package com.arjvik.arjmart.api;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ConnectionFactoryBinder extends AbstractBinder {
	@Override
	protected void configure() {
		bind(ConnectionFactory.class).to(ConnectionFactory.class).in(Singleton.class);
	}
}
