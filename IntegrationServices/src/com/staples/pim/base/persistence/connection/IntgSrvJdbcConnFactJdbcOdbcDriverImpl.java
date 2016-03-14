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
 * File name     :   IntgSrvJDBCUtils
 * Creation Date :   July 12, 2015
 * @author        :  Sima Zaslavsky 
 * @version 1.0
 */ 

package com.staples.pim.base.persistence.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.staples.pim.base.common.exception.IntgSrvBatchException;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;



/**
 * This is a factory class used for retrieving connection using jdbc 
 */
public class IntgSrvJdbcConnFactJdbcOdbcDriverImpl extends
		IntgSrvJdbcConnectionFactory {

	/**
	 * @param 
	 * @return The Connection object is returned
	 * @exception Throws a IntgSrvBatchException
	 * @see 
	 * 
	 */
	protected static IntgSrvLogger logger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	/**
	 * Method used to get a Connection object 
	 * 
	 * @return returns a Connection object
	 * @throws IntgSrvBatchException 
	 */
	public Connection getConnection(String dsInfo) throws IntgSrvBatchException
	{
		String dbUrl = IntgSrvPropertiesReader.getProperty(
						new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.DB_URL).toString());
		String dbUser = IntgSrvPropertiesReader.getProperty(
				new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.DB_USER).toString());
		String dbPassword = IntgSrvPropertiesReader.getProperty(
				new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.DB_PASSWORD).toString());
		Connection sqlCon = null;
		try
		{
			// System.out.println(IntgSrvPropertiesReader.getProperty(
					//  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.DB_DRIVER).toString()));
			 
			// System.out.println("[" + IntgSrvPropertiesReader.getProperty(
				//	  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.DB_DRIVER).toString()) + "]"); 
			 
		//	 String a = IntgSrvPropertiesReader.getProperty(
		//			  new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.DB_DRIVER).toString());
			 
		//	 System.out.println("[" + a + "]");
			 
		//	 System.out.println("[" + a.trim() + "]");
			 
			 // Class.forName(a.trim());
			 
			 
			   Class.forName(IntgSrvPropertiesReader.getProperty(
					   new StringBuffer().append(dsInfo).append(IIntgSrvConnConstants.DB_DRIVER).toString()));
			 
			  
			 
			// Class.forName("com.ibm.as400.access.AS400JDBCDriver");
			sqlCon = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		//	   System.out.println("sqlCon = " + dbUrl + "," +  dbUser + "," + dbPassword + ";");
			  //sqlCon = DriverManager.getConnection(
                     //"jdbc:as400://COSMOS;naming=sql;errors=full",
                    // "MMUSER4",
                    //"MMUSER4");
		}
		 catch (ClassNotFoundException ex)
		 {
			 logger.fatal("Class not found exception: " + ex);
			 throw new IntgSrvBatchException("Class not found.", ex);
		 }
		catch (SQLException ex)
		{
			logger.fatal("Failed to connect : " + ex);
			throw new IntgSrvBatchException("Failed to connect .", ex);
		}
		return sqlCon;
	}
	/**
	 */
	public IntgSrvJdbcConnFactJdbcOdbcDriverImpl() {
		return;
	}


}
