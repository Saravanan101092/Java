/**
 * -----------------------------------------------------------------------
 * STAPLES, INC
 * -----------------------------------------------------------------------
 * (C) Copyright 2007 Staples, Inc.          All rights reserved.
 *
 * NOTICE:  All information contained herein or attendant hereto is,
 *          and remains, the property of Staples Inc.  Many of the
 *          intellectual and technical concepts contained herein are
 *          proprietary to Staples Inc. Any dissemination of this
 *          information or reproduction of this material is strictly
 *          forbidden unless prior written permission is obtained
 *          from Staples Inc.
 * -----------------------------------------------------------------------
 */
/*
 * File name     :   MessageQueueListenerWithSessionAware 
 * Creation Date :   02-03-215
 * @author  	 :  Sima Zaslavsky
 * @version 1.0
 */ 

package com.staples.pim.base.common.listenerandrunner;
 
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.Unmarshaller;

import org.springframework.jms.listener.SessionAwareMessageListener;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;

 

@SuppressWarnings("rawtypes")
public abstract class MessageQueueListenerWithSessionAware implements SessionAwareMessageListener {
	
	IntgSrvLogger logger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	
	private Unmarshaller unmarshaller = null;  
	
	public MessageQueueListenerWithSessionAware() {
		// TODO Auto-generated constructor stub
	}
	
	public Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}

	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	
	public void onMessage(Message m, Session session){
		
		try {
			TextMessage message = (TextMessage) m;
			
			boolean isSuccess = processMessage(message);  
			 
			this.processSession(isSuccess);  
		 	 
		} catch (Exception e1) {
			logger.info(e1.getMessage());
			logger.error(e1);
		} 		
	}
	
	@SuppressWarnings("unused")
	protected abstract boolean processMessage(TextMessage message);  
	
	public abstract void processSession(boolean isSuccess);		
		 
}
