package com.arjvik.arjmart.api.jms;

import com.arjvik.arjmart.api.location.Inventory;
import com.arjvik.arjmart.api.order.Order;

public interface PipelineRunner {

	public void runIncommingShipmentPipeline(Inventory inventory) throws PipelineException;

	public void runOrderPlacedPipeline(Order order) throws PipelineException;

}
