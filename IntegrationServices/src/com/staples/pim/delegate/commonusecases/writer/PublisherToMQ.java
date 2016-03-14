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
 * File name     :   PublisherToMQ
 * Creation Date :   February, 2015
 * @author  	 :   Sima Zaslavsky
 * @version 1.0
 */ 

package com.staples.pim.delegate.commonusecases.writer;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.jms.JMSException;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkICD;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.MQConnectionManager;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.createupdateitem.listenerrunner.RunSchedulerItemCreateUpdate;


public class PublisherToMQ { 
	 
	 private ErrorHandlingFrameworkICD ehfICD;
     private ErrorHandlingFrameworkHandler ehfHandler = new ErrorHandlingFrameworkHandler();   
     private static IntgSrvLogger ehfItemLoggerFixLength = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_FIXLENGTH);
     private static IntgSrvLogger traceLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
     private static String clazzroot = "PublisherToMQ";
     
     private static final short FUTUREDATEDPO =  2;
 	 private static final short GETNEWINSTANCEFORFUTUREDATEDPO =  22; 
 	 private static final short POCOSTCODEBOTH =  4;
	
	public PublisherToMQ() { 
	}
	
	private void init() throws ErrorHandlingFrameworkException {
		   ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
					 IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM, 
					 IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID, 
					 IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
	}
	
	public void publishToMq(StepTransmitterBean serviceBean)
	{   
		String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
		String codeModule = this.getClass().getName();
		int transaction_Type = 0;
		int poCostCodeRet = 0;
		
		try {
			init();
		} catch (ErrorHandlingFrameworkException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		 
		String msgDesc = IntgSrvAppConstants.PUBLISH_CONFIRMATION_MSG_TO_MQ + 
							(String)serviceBean.getItem(IntgSrvAppConstants.PRODUCT_ID_TO_HASHMAP);  	
		String traceId =((IntgSrvErrorHandlingFrameworkICD) ehfICD).getNewTraceId();
		
		String infoLogString = ehfHandler.getInfoLog(new Date(), traceId, 				
							IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3, 
							ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, 
							msgDesc, 
							IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, 
							usecase, codeModule, ehfICD); 
		 
		ehfItemLoggerFixLength.debug(infoLogString);
		traceLogger.debug(clazzroot, "2Product.publishToMq", msgDesc);  
			
		/* We have to catch exception when instance exist but iSeries connection had been terminated */ 	
		try {
		serviceBean = sendMQMessage(serviceBean);
		 
		if (serviceBean.getStatus() == 1  && 
						(serviceBean.getTransactionType() != GETNEWINSTANCEFORFUTUREDATEDPO && 
								serviceBean.getTransactionType() != FUTUREDATEDPO))
		{
			/* if the message from the hot folder could not be delivered, write XML to the bad folder  */			 
			try {
	      		IntgSrvUtils.printConsole("publishToMq in error ::before copy file to Bad folder");
	      		IntgSrvUtils.copyFileUsingFileStreams(RunSchedulerItemCreateUpdate.currentFile, new File(RunSchedulerItemCreateUpdate.currentFile.getAbsolutePath().replace("_Unprocessed", "_Bad")));
	      		IntgSrvUtils.printConsole("publishToMq in error ::after copy file to Bad folder");
	      		} catch (IOException e) {
					// TODO Auto-generated catch block
	      			e.printStackTrace();
			    } 
		}
			
		if (serviceBean.getTransactionType() == GETNEWINSTANCEFORFUTUREDATEDPO)
		{
				int type = GETNEWINSTANCEFORFUTUREDATEDPO;
				// transaction_Type = FUTUREDATEDPO;
				while (type == GETNEWINSTANCEFORFUTUREDATEDPO)
				{
					serviceBean = this.getReconnectionAndSendMQMsg(serviceBean);  
				}
		}
		} catch (Exception ex) { 
				int  z =0; 
				int type = 0;
		if (transaction_Type == FUTUREDATEDPO)
		{
			transaction_Type = GETNEWINSTANCEFORFUTUREDATEDPO;
			for (z = 0; z< 3; z++)  
			{
						 try {
							 serviceBean =  sendMQMessage(serviceBean);
						 } catch (Exception ez)  {  
							 if (z == 2)
							 {
								 try {
									Thread.sleep(3600000);
									//Thread.sleep(4000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}								 
								 z =0;
							 } 
						 }
				    }
					 transaction_Type = FUTUREDATEDPO;
				}// ex.printStackTrace();
				 	transaction_Type = FUTUREDATEDPO;
	        	}        			 
			/*  end logic to ReInstatiate connection  */  
			 
			if (transaction_Type == GETNEWINSTANCEFORFUTUREDATEDPO)
			{
				transaction_Type = FUTUREDATEDPO;
			}
			 
	} 
	
	public StepTransmitterBean sendMQMessage(StepTransmitterBean serviceBean) throws JMSException 
	{	 
		traceLogger.info(clazzroot, "2Product.sendMQMessage", "ENTER/.../EXIT: key = " + 
									(String)serviceBean.getItem(IntgSrvAppConstants.PRODUCT_ID_TO_HASHMAP)); 
		MQConnectionManager instance = null;
		int status = 0;
		  
		instance = MQConnectionManager.getInstance(serviceBean);
			try {
			status = instance.putMessage(serviceBean);
				/* status 1 means we have an instance of the old connection that was terminated already */ 
			if (serviceBean.getTransactionType() == FUTUREDATEDPO
								|| serviceBean.getTransactionType() == GETNEWINSTANCEFORFUTUREDATEDPO)
			{
					
				if (status == 1) 
				{
			
					for (int k = 0; k<3; k++)
					{
						serviceBean.setTransactionType(GETNEWINSTANCEFORFUTUREDATEDPO);
						instance = MQConnectionManager.getInstance(serviceBean);
						status = instance.putMessage(serviceBean); 
						if (status == 0)
						{
							serviceBean.setTransactionType(FUTUREDATEDPO);
							break;
						}
						else
						{
							if (k == 2)
							{
								Thread.sleep(3600000);
								// Thread.sleep(6000);
								k =0;
							}
						}
					} 
				}
				else
				{
					serviceBean.setTransactionType(FUTUREDATEDPO);
				}
			}
			 
			}catch(Exception e){
				if (serviceBean.getTransactionType() == FUTUREDATEDPO)
				{				 
					serviceBean.setTransactionType(GETNEWINSTANCEFORFUTUREDATEDPO);
				}
				status = 1;
				serviceBean.setStatus(status);
			}   
		IntgSrvUtils.printConsole("exit sendMQMessage()"); 
	
		serviceBean.setStatus(status);
		return serviceBean; 
	 }
	
	 private StepTransmitterBean getReconnectionAndSendMQMsg(StepTransmitterBean serviceBean)
     { 
     	for (int z = 0; z< 3; z++)  
     	{
     		try 
     		{
     			serviceBean = sendMQMessage(serviceBean);
				if (serviceBean.getTransactionType() != GETNEWINSTANCEFORFUTUREDATEDPO)
				{
					break;
				}
				else 
				{
					if (z == 2)
					 {
						try {
							 Thread.sleep(3600000);
							 // Thread.sleep(4000);
							 z =0;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}						
					} 
				}
				 } catch (Exception ez)  {       					 
					if (z == 2)
					 {
						try {
							 Thread.sleep(3600000);
							 //Thread.sleep(4000);
							 z =0;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}						
					} 
				 }				 
		   }
     	
     	return serviceBean;
     }
	 
	 public void closeMQConnection(StepTransmitterBean transmitter) throws JMSException
	 {		 						
		MQConnectionManager.getInstance(transmitter).close();
	 }
}
