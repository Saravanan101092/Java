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
 * File name     :   
 * Creation Date :   
 * @author  
 * @version 1.0
 */ 


package com.staples.pim.base.common.logging;

import java.util.HashMap;
import java.net.URL; 
import org.apache.log4j.xml.DOMConfigurator; 

import com.staples.pim.base.util.IntgSrvUtils;

public class IntgSrvLogManager {
	
	private static final String CONFIG_DIR_DEFAULT = "/opt/stibo/SpringBatch/configurations";
	private static final String LOG_CONFIG_FILE = "log4j.xml";
	private static final String LOG_CONFIG_LOC = "LOG4jXML_DIR";
	private static HashMap loggerHashMap = new HashMap();
	
	
	private static IntgSrvLoggerFactoryImpl peLoggerFactory = new IntgSrvLoggerFactoryImpl();
	
	
	static {
		try {
			String log4jdir = System.getenv(LOG_CONFIG_LOC);
			String log4jfile = LOG_CONFIG_FILE;
			if (log4jdir != null) {
				log4jfile = IntgSrvUtils.reformatFilePath(log4jdir + "/" + LOG_CONFIG_FILE);
			}
			else {
				log4jfile = IntgSrvUtils.reformatFilePath(CONFIG_DIR_DEFAULT + "/" + LOG_CONFIG_FILE);
			}
			System.out.println("log4jdir = " + log4jdir);
			System.out.println("log4jfile = " + log4jfile);
			URL stringURL = IntgSrvUtils.locate(log4jfile);
			DOMConfigurator.configure(stringURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param loggerName
	 * @return
	 */
	
	static IntgSrvLogger getLogger(String loggerName) {
		return getLogger(loggerName, peLoggerFactory);
	}
	/**
	 * @param loggerName
	 * @param peLoggerFactoryInst
	 * @return
	 */
	static IntgSrvLogger getLogger(String loggerName,
			IntgSrvLoggerFactoryImpl peLoggerFactoryInst) {
		IntgSrvLogger logger = (IntgSrvLogger) loggerHashMap.get(loggerName);
		if (logger == null) {
			synchronized (IntgSrvLogger.class) {
				logger = (IntgSrvLogger) loggerHashMap.get(loggerName);
				if (logger == null) {
					logger = peLoggerFactoryInst
							.produceLoggerInstance(loggerName);
					loggerHashMap.put(loggerName, logger);
				}
			}
		}
		return logger;
	}
	
	public static void main(String[] args) {
		/*
		String log4jdir = System.getenv(LOG_CONFIG_LOC);
		String  log4jfile = CommonUtils.reformatFilePath(log4jdir + "/" + LOG_CONFIG_FILE);
		System.out.println("log4jdir = " + log4jdir);
		System.out.println("log4jfile = " + log4jfile);
		*/
		IntgSrvLogManager.getLogger("test");
	}
}
