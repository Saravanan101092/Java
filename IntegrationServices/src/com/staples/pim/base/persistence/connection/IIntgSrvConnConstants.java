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


/**
 * This interface includes the different constants used for Connection setup
 */
public interface IIntgSrvConnConstants {

	/**
	 * CONNECTION_CLASS
	 */
	String CONNECTION_CLASS = "intgsrv.ds.connectionclass";
	 
	/**
	 * DB_URL
	 */
	String DB_URL = ".url";
	/**
	 * DB_USER
	 */
	String DB_USER = ".user";
	/**
	 * DB_PASSWORD
	 */
	String DB_PASSWORD = ".password";
	/**
	 * DB_DRIVER
	 */
	String DB_DRIVER = ".driver";
	/**
	 * AS400SERVER_GALAXY_PIM - Default
	 */
	String AS400SERVER_GALAXY_PIM = "ds.as400ServerGalaxy.pim";
	/**
	 * AS400SERVER_SUNBEAM_PIM - Default
	 */
	String AS400SERVER_SUNBEAM_PIM = "ds.as400ServerSunbeam.pim";
	/**
	 * ORACLE_XYZ
	 */
	String ORACLE_XYZ = "ds.Oracle.xyz";  
	/**
	 * GALAXY_XYZ
	 */
	String GALAXY_XYZ = "ds.Galaxy.xyz";  
	/**
	 * PROC
	 */
	String PROC = ".proc";
	/**
	 * LOCATION_LEVEL_QUERY_EXECUTION_MAX_TIME
	 */
	int LOCATION_LEVEL_QUERY_EXECUTION_MAX_TIME = 120000;

}
