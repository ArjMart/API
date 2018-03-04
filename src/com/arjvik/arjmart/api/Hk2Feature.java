package com.arjvik.arjmart.api;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

import com.arjvik.arjmart.api.auth.AuthenticationDAO;
import com.arjvik.arjmart.api.auth.JDBCAuthenticationDAO;
import com.arjvik.arjmart.api.auth.UserID;
import com.arjvik.arjmart.api.auth.UserIDProvider;
import com.arjvik.arjmart.api.item.ItemAttributeDAO;
import com.arjvik.arjmart.api.item.ItemDAO;
import com.arjvik.arjmart.api.item.ItemPriceDAO;
import com.arjvik.arjmart.api.item.JDBCItemAttributeDAO;
import com.arjvik.arjmart.api.item.JDBCItemDAO;
import com.arjvik.arjmart.api.item.JDBCItemPriceDAO;
import com.arjvik.arjmart.api.location.InventoryDAO;
import com.arjvik.arjmart.api.location.JDBCInventoryDAO;
import com.arjvik.arjmart.api.location.JDBCLocationDAO;
import com.arjvik.arjmart.api.location.LocationDAO;
import com.arjvik.arjmart.api.order.JDBCOrderDAO;
import com.arjvik.arjmart.api.order.JDBCOrderLineDAO;
import com.arjvik.arjmart.api.order.OrderDAO;
import com.arjvik.arjmart.api.order.OrderLineDAO;
import com.arjvik.arjmart.api.order.checkout.CheckoutDAO;
import com.arjvik.arjmart.api.order.checkout.DummyPaymentDAO;
import com.arjvik.arjmart.api.order.checkout.JDBCCheckoutDAO;
import com.arjvik.arjmart.api.order.checkout.PaymentDAO;
import com.arjvik.arjmart.api.user.JDBCUserDAO;
import com.arjvik.arjmart.api.user.UserDAO;

@Provider
public class Hk2Feature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new AbstractBinder() {
			@Override
        	protected void configure() {
        		bind(ConnectionFactory.class).to(ConnectionFactory.class).in(Singleton.class);
        		bind(JDBCItemDAO.class).to(ItemDAO.class).in(Singleton.class);
        		bind(JDBCItemAttributeDAO.class).to(ItemAttributeDAO.class).in(Singleton.class);
        		bind(JDBCItemPriceDAO.class).to(ItemPriceDAO.class).in(Singleton.class);
        		bind(JDBCLocationDAO.class).to(LocationDAO.class).in(Singleton.class);
        		bind(JDBCInventoryDAO.class).to(InventoryDAO.class).in(Singleton.class);
        		bind(JDBCOrderDAO.class).to(OrderDAO.class).in(Singleton.class);
        		bind(JDBCOrderLineDAO.class).to(OrderLineDAO.class).in(Singleton.class);
        		bind(JDBCCheckoutDAO.class).to(CheckoutDAO.class).in(Singleton.class);
        		bind(JDBCUserDAO.class).to(UserDAO.class).in(Singleton.class);
        		bind(JDBCAuthenticationDAO.class).to(AuthenticationDAO.class).in(Singleton.class);
        		bind(DummyPaymentDAO.class).to(PaymentDAO.class).in(Singleton.class);
        		bind(HashCodeETagProvider.class).to(ETagProvider.class).in(Singleton.class);
        		bindFactory(UserIDProvider.class).to(Integer.class).qualifiedBy(new UserID.Instance()).in(RequestScoped.class);
        	}
        });
        return true;
    }
}
