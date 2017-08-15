package com.arjvik.arjmart.api.hk2;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.arjvik.arjmart.api.ItemDAO;
import com.arjvik.arjmart.api.JDBCItemDAO;

public class DAOBinder extends AbstractBinder {
	@Override
	protected void configure() {
		bind(JDBCItemDAO.class).to(ItemDAO.class).in(Singleton.class);
	}
}