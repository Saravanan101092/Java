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
 * File name     :   IntgSrvPropertiesReader 
 * Creation Date :   
 * @author  	 :  Sima Zaslavsky
 * @version 1.0
 */ 


package com.staples.pim.base.loader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtilConstants;
import com.staples.pim.base.util.IntgSrvUtils;

public class IntgSrvPropertiesReader 
{
	private static Properties properties = null;
	 
	
	/**
	 * Method to load the targeted properties file
	 *
	 */ 
	 
	private static void loadProperties()
	{
			
		if (properties == null)
		{	
			String configDirLocation = null;
			java.io.InputStream is = null;
			InputStreamReader reader;
			String sysEnvConfigDir = System.getenv(IntgSrvAppConstants.SPRING_BATCH_CONFIG_DIR);
			//   check this out String sysEnvConfigDir = System.getProperty("user.dir");
			if (sysEnvConfigDir != null) { 
				configDirLocation = sysEnvConfigDir;
			}
			else
			{
				configDirLocation = IntgSrvUtilConstants.CONFIG_DIR_LOCATION_DEFAULT;
			}
			
			try
			{
				properties = new Properties();				
				is =
					new FileInputStream(IntgSrvUtils.reformatFilePath(configDirLocation + "/" + 
							IntgSrvUtilConstants.CONFIG_COMMON_PROPERTIES_FILE));
				reader = new InputStreamReader(is, "UTF-8");
				properties.load(reader);
				is =
					new FileInputStream(IntgSrvUtils.reformatFilePath(configDirLocation + "/" +  
							IntgSrvUtilConstants.CONFIG_ENV_PROPERTIES_FILE));
				reader = new InputStreamReader(is, "UTF-8");
				properties.load(reader);
				//is =
					//new FileInputStream(IntgSrvUtils.reformatFilePath(configDirLocation + "/" + 
							//IntgSrvUtilConstants.QUEUE_DETAILS_PROPERTIES_FILE));
				//properties.load(is);
				 
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				if (is != null)
				{
					try
					{
						is.close();
					}
					catch (IOException exp)
					{
						System.out.println("Failed to close input stream");
					}
				}
			}
		}
	}
	
	 
	 
	/**
	 * Method to get the property value corresponding to a key passed as a parameter
	 * 
	 * @param propertyName the name of the property to return
	 * @return String containing the name of the property
	 */
	public static String getProperty(String propertyName)
	{
		loadProperties();
		String propertyValue = properties.getProperty(propertyName);
		if (propertyValue == null)
		{
			propertyValue = "NULL";
		}
		return propertyValue;
	}
	
	
	/**
	 * main method to test the class
	 * @param args - command line arguments
	 * */
	public static void main(String[] args) {
		System.out.println("test start ..."); 
		
		loadProperties();
		String propertyValue = getProperty("mq.queuename_futurePODate");
		System.out.println("propertyValue = " + propertyValue);

		System.out.println("test end!"); 
	} 
}
