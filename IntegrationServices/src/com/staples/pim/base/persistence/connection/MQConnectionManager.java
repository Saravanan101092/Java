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
 * File name     :   MQConnectionManager
 * Creation Date :   
 * @author  	 :   
 * @version 1.0
 */

package com.staples.pim.base.persistence.connection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Session;

import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueSender;
import com.ibm.mq.jms.MQQueueSession;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

public class MQConnectionManager {

	private MQQueueConnection			connection		= null;
	private MQQueueSession				session			= null;
	private MQQueue						queue			= null;
	private MQQueueSender				sender			= null;
	private static IntgSrvLogger		traceLogger		= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	private String						clazzname		= this.getClass().getName();

	private static MQConnectionManager	instance		= null;
	// private static MQConnectionManager instanceFDPOcon = null;
	// private static MQConnectionManager instanceCommonCon = null;

	private static final String			alert			= "MQ failed alert - "
																+ IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SPRINGBATCH_ENV);
	String								emailMessage	= null;

	// start merged for sprint_A3
	private int							resendCount		= 0;
	// end merged for sprint_A3

	private Map<String, String>			mailMsg			= new LinkedHashMap<String, String>();

	@SuppressWarnings("deprecation")
	protected MQConnectionManager(StepTransmitterBean serviceBean) throws JMSException {

		traceLogger.debug(clazzname, "MQConnectionManager", "create singleton instance: connection, session, queue, sneder");
		try {
			// This is used to hold information to send mail alert from Catch
			// block if any exception
			mailMsg.put("Host Name: ", serviceBean.getMqHostName());
			mailMsg.put("Port: ", String.valueOf(serviceBean.getMqPport()));
			mailMsg.put("Queue Manager: ", serviceBean.getMqQueueManager());
			mailMsg.put("Channel: ", serviceBean.getMqChannel());
			mailMsg.put("Queue Name: ", serviceBean.getMqQueueName());
			if (serviceBean.getPublishId() != null) {
				mailMsg.put("Publish Id: ", serviceBean.getPublishId());
			}
			MQQueueConnectionFactory cf = new MQQueueConnectionFactory();

			cf.setHostName(serviceBean.getMqHostName());
			cf.setPort(serviceBean.getMqPport());
			cf.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
			cf.setQueueManager(serviceBean.getMqQueueManager());
			cf.setChannel(serviceBean.getMqChannel());
			connection = (MQQueueConnection) cf.createQueueConnection();
			session = (MQQueueSession) connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = (MQQueue) session.createQueue(serviceBean.getMqQueueName());
			sender = (MQQueueSender) session.createSender(queue);
			traceLogger.debug(clazzname, "MQConnectionManager", "mq.queuename=" + serviceBean.getMqQueueName());
		} catch (JMSException jmsex) {
			System.out.println(jmsex);
			IntgSrvUtils.alertByEmail(alert, getMailMessage(jmsex.getMessage()));
			throw jmsex;
		}
	}

	public static MQConnectionManager getInstance(StepTransmitterBean serviceBean) throws JMSException {

		if (instance == null) {
			IntgSrvUtils.printConsole("MQConnectionManager instance == null, to create a new one");
			traceLogger.debug("MQConnectionManager", "getInstance", "instance == null, to create a new one");
			instance = new MQConnectionManager(serviceBean);
			if (instance == null) {
				IntgSrvUtils.printConsole("ERROR MQConnectionManager instance == null failed to create a new one");
				traceLogger.debug("MQConnectionManager", "getInstance", "ERROR instance == null failed to create a new one");

			}
		} else {
			IntgSrvUtils.printConsole("instance already exists");
			traceLogger.debug("MQConnectionManager", "getInstance", "instance already exists");
		}

		return instance;
	}

	public int putMessage(StepTransmitterBean serviceBean) {

		mailMsg.put("GET/PUT Message: ", "PUT");

		traceLogger.info("Message is published to : " + serviceBean.getMqQueueName() + " Channel : " + serviceBean.getMqChannel()
				+ "  Manager : " + serviceBean.getMqQueueManager());
		int status = 0;
		try {
			if (session == null || sender == null || connection == null || instance == null) {
				IntgSrvUtils.printConsole("call MQUtil()!");
				traceLogger.debug(clazzname, "putMessage", "call MQUtil()!");
				instance = new MQConnectionManager(serviceBean);
				traceLogger.debug(clazzname, "putMessage", "after call MQUtil()!");
				IntgSrvUtils.printConsole("after call MQUtil()!");
			}

			if (session != null) {
				JMSTextMessage message = (JMSTextMessage) session.createTextMessage(serviceBean.getMessage());

				message.setStringProperty("SPLS_TID_Tidver", "TID02");
				message.setStringProperty("SPLS_TID_Env", "DV1");
				message.setStringProperty("SPLS_TID_Trantype", "Non Stock Item");
				message.setStringProperty("SPLS_TID_Tranparm", "Tran Param");
				message.setStringProperty("SPLS_TID_Custparm", "Cust Param");
				message.setStringProperty("SPLS_TID_Tranver", "1.0");
				message.setStringProperty("SPLS_TID_AuditFlag", "Y");
				message.setStringProperty("SPLS_TID_AppID", "PCM");
				message.setStringProperty("SPLS_TID_PRefID", (String) serviceBean.getItem(IntgSrvAppConstants.PRODUCT_ID_TO_HASHMAP));
				try {
					String voString = DatamigrationCommonUtil.voToString(message, serviceBean.getPublishId());
					DatamigrationCommonUtil.printConsole("JMSTextMessage :: " + voString);
					traceLogger.info("JMSTextMessage :: " + voString);
				} catch (Exception e) {
				}
				sender.send(message);
				IntgSrvUtils.printConsole("Sent message:\n" + message.getStringProperty("SPLS_TID_PRefID"));
			} else {
				IntgSrvUtils.printConsole("MQ session is NULL: msg did not send!");
				traceLogger.debug(clazzname, "putMessage", "MQ session is NULL: msg did not send!");
			}

			// sender.close();
			// session.close();
			// connection.close();

			IntgSrvUtils.printConsole("\nSUCCESS\n");
		} catch (JMSException jmsex) {
			System.out.println(jmsex);
			IntgSrvUtils.printConsole("\nFAILURE 1\n");
			status = 1;
			emailMessage = "Hello,\n MQ get Exception:\n" + jmsex;
			IntgSrvUtils.alertByEmail(alert, getMailMessage(jmsex.getMessage()));

		} catch (Exception ex) {
			System.out.println(ex);
			IntgSrvUtils.printConsole("\nFAILURE 2\n");
			status = 1;
			emailMessage = "Hello,\n MQ get Exception:\n" + ex;
			IntgSrvUtils.alertByEmail(alert, getMailMessage(ex.getMessage()));
		}

		return status;
	}

	public void close() {

		try {
			if (sender != null) {
				sender.close();
				IntgSrvUtils.printConsole("sender closed");
				sender = null;
			}
			if (session != null) {
				session.close();
				IntgSrvUtils.printConsole("session closed");
				session = null;
			}
			if (connection != null) {
				connection.close();
				IntgSrvUtils.printConsole("connection closed");
				connection = null;
			}
			instance = null;
			IntgSrvUtils.printConsole("MQ instance set to NULL");
		} catch (JMSException jmsex) {
			System.out.println(jmsex);
			IntgSrvUtils.printConsole("\nFAILURE 1\n");
			emailMessage = "Hello,\n MQ get Exception:\n" + jmsex;
			IntgSrvUtils.alertByEmail(alert, getMailMessage(jmsex.getMessage()));
		} catch (Exception ex) {
			System.out.println(ex);
			IntgSrvUtils.printConsole("\nFAILURE 2\n");
			emailMessage = "Hello,\n MQ get Exception:\n" + ex;
			IntgSrvUtils.alertByEmail(alert, getMailMessage(ex.getMessage()));
		}
	}

	private String getMailMessage(String stackTrace) {

		StringBuffer buffer = new StringBuffer("");
		try {
			buffer.append("Hi All, \n\n");
			buffer.append("MQ Exception on Spring Batch, Please find below details: \n\n");

			buffer.append("Source System: " + InetAddress.getLocalHost().getHostName() + "\n");

			Set<String> keys = mailMsg.keySet();
			for (String key : keys) {
				buffer.append(key + mailMsg.get(key) + "\n");
			}

			buffer.append("Exception StackTrace: " + stackTrace);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
