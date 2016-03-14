
package com.staples.pim.base.common.listenerandrunner;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.oxm.Unmarshaller;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;

public abstract class MessageQueueListener implements MessageListener {

	private Unmarshaller		unmarshaller	= null;
	private static final short	XMLFILE			= 5;
	private static final short	DELIMITEDFILE	= 6;
	protected String			clazzname		= this.getClass().getName();

	public MessageQueueListener() {

	}

	public Unmarshaller getUnmarshaller() {

		return unmarshaller;
	}

	public void setUnmarshaller(Unmarshaller unmarshaller) {

		this.unmarshaller = unmarshaller;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message) This method
	 * to listen the Galaxy Queue
	 */
	// @Override
	public void onMessage(Message m) {

		IntgSrvLogger logger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
		TextMessage message = (TextMessage) m;
		logger.debug("Message received before processMessage");
		processMessage(message);
		logger.debug("After processMessage");
	}

	/**
	 * This method used to call the parsing method for galaxy out file and call
	 * the send method for step system.
	 * 
	 * @param message
	 *            message from galaxy system
	 */
	// @SuppressWarnings("unused")
	protected abstract boolean processMessage(TextMessage message);

}
