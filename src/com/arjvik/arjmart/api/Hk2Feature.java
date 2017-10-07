package com.arjvik.arjmart.api;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.arjvik.arjmart.api.item.ItemAttributeDAO;
import com.arjvik.arjmart.api.item.ItemDAO;
import com.arjvik.arjmart.api.item.JDBCItemAttributeDAO;
import com.arjvik.arjmart.api.item.JDBCItemDAO;

@Provider
public class Hk2Feature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new AbstractBinder(){
        	@Override
        	protected void configure() {
        		bind(JDBCItemDAO.class).to(ItemDAO.class).in(Singleton.class);
        		bind(JDBCItemAttributeDAO.class).to(ItemAttributeDAO.class).in(Singleton.class);
        	}
        });
        return true;
    }
}