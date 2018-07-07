package com.arjvik.arjmart.api.jms;

import com.arjvik.arjmart.api.location.Inventory;

public interface PipelineRunner {

	public void runIncommingShipmentPipeline(Inventory inventory) throws PipelineException;

}
