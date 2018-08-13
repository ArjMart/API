package com.arjvik.arjmart.api.jms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.glassfish.jersey.internal.inject.DisposableSupplier;

public class JMSSessionProvider implements DisposableSupplier<Session> {
	
	private Connection connection;
	private Session session;
	
	private static Properties properties = null;
	
	@Inject
	public JMSSessionProvider() {
		try {
			if(properties == null) {
				properties = new Properties();
				InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("jms.properties");
				if (in == null)
					throw new RuntimeException("Could not read Order Queue connection info, check if jms.properties exists in classpath");
				properties.load(in);
			}
			String CONNECTION_URL = properties.getProperty("jmsurl");
			String USERNAME = properties.getProperty("jmsusername");
			String PASSWORD = properties.getProperty("jmspassword");
			if (CONNECTION_URL == null)
				throw new RuntimeException("Could not read Order Queue connection info, check format of jms.properties ");
			session = getSession(CONNECTION_URL, USERNAME, PASSWORD);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (JMSException e) {
			throw new RuntimeException("Unable to connect to the Order Queue, check jms.properties", e);
		}
	}

	private Session getSession(String CONNECTION_URL, String USERNAME, String PASSWORD) throws JMSException {
		ConnectionFactory connectionFactory;
		if(USERNAME == null || USERNAME.isEmpty())
			connectionFactory = new ActiveMQConnectionFactory(CONNECTION_URL);
		else
			connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, CONNECTION_URL);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		this.connection = connection;
		return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	@Override
	public Session get() {
		return session;
	}

	@Override
	public void dispose(Session session) {
		try {
			session.close();
			connection.close();
		} catch (NullPointerException ignored) {
			//Do nothing on a NullPointerException, that means we can't store an instance of connection to close
		} catch (JMSException e) {
			throw new RuntimeException("Unable to dispose of JMS session by closing connection", e);
		}
	}

}