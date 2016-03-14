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

package com.staples.pim.base.persistence.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
	
	// is not used currently, we have it only for test

	Connection connection = null;
	private static Object serviceMonitorConnection = new Object();
	
	public DBConnectionManager(String driverName) {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) { 
            e.printStackTrace();
            return;
        }
    }

	
	public Connection getDBConnection(String connectionURL, String userName, String userPassword) {      
	
		if (connection == null)
		{
			synchronized (serviceMonitorConnection)
			{
				try {
					connection = DriverManager.getConnection(connectionURL, userName, userPassword);
            
				} catch (SQLException e) {
					e.printStackTrace();  
					//  logger Failed to make connection
				}
			}
		}
		 
		return connection;
	}

	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}

}


 