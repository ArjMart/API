package com.arjvik.arjmart.api;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.arjvik.arjmart.api.item.ItemAttributeDAO;
import com.arjvik.arjmart.api.item.ItemDAO;
import com.arjvik.arjmart.api.item.JDBCItemAttributeDAO;
import com.arjvik.arjmart.api.item.JDBCItemDAO;

public class DAOBinder extends AbstractBinder {
	@Override
	protected void configure() {
		bind(JDBCItemDAO.class).to(ItemDAO.class).in(Singleton.class);
		bind(JDBCItemAttributeDAO.class).to(ItemAttributeDAO.class).in(Singleton.class);
	}
}