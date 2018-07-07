package com.arjvik.arjmart.api.jms;

import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import com.arjvik.arjmart.api.location.Inventory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JMSPipelineRunner implements PipelineRunner {

	private Session session;
	
	@Inject
	public JMSPipelineRunner(Session session) {
		this.session = session;
	}
	
	@Override
	public void runIncommingShipmentPipeline(Inventory inventory) throws PipelineException {
		try {
			Destination destination = session.createQueue("IncomingShipment");
			MessageProducer producer = session.createProducer(destination);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(inventory);
			Message message = session.createTextMessage(json);
			producer.send(message);
		} catch (JMSException e) {
			throw new PipelineException(e);
		} catch (JsonProcessingException e) {
			throw new PipelineException(e);
		}
	}

}
