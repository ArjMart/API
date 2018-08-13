package com.arjvik.arjmart.api.jms;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import com.arjvik.arjmart.api.location.Inventory;
import com.arjvik.arjmart.api.order.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JMSPipelineRunner implements PipelineRunner {

	private Provider<Session> sessionProvider;
	
	@Inject
	public JMSPipelineRunner(Provider<Session> sessionProvider) {
		this.sessionProvider = sessionProvider;
	}
	
	@Override
	public void runIncommingShipmentPipeline(Inventory inventory) throws PipelineException {
		try {
			Session session = sessionProvider.get();
			Destination destination = session.createQueue("IncomingShipment");
			MessageProducer producer = session.createProducer(destination);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(inventory);
			Message message = session.createTextMessage(json);
			producer.send(message);
			session.close();
		} catch (JMSException e) {
			throw new PipelineException(e);
		} catch (JsonProcessingException e) {
			throw new PipelineException(e);
		}
	}
	
	@Override
	public void runOrderPlacedPipeline(Order order) throws PipelineException {
		try {
			Session session = sessionProvider.get();
			Destination destination = session.createQueue("OrderPlaced");
			MessageProducer producer = session.createProducer(destination);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(order);
			Message message = session.createTextMessage(json);
			producer.send(message);
			session.close();
		} catch (JMSException e) {
			throw new PipelineException(e);
		} catch (JsonProcessingException e) {
			throw new PipelineException(e);
		}
	}

}
