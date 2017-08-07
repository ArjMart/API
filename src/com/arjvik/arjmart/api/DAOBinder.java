package com.arjvik.arjmart.api;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class DAOBinder extends AbstractBinder {
	@Override
	protected void configure() {
		bind(JDBCItemDAO.class).to(ItemDAO.class).in(Singleton.class);
	}
}