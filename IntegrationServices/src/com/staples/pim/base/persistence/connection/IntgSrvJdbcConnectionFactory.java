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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.CallableStatement;
import com.staples.pim.base.common.exception.IntgSrvBatchException;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.IntgSrvJdbcConnFactJdbcOdbcDriverImpl;

public abstract class IntgSrvJdbcConnectionFactory {
	


	protected static IntgSrvLogger logger = IntgSrvLogger.getInstance("FreeformTraceLogger");//null;
	
	protected static IntgSrvJdbcConnectionFactory factoryImpl = null;
	/**
	 * Method getFactory: returns implementation instance of StplJdbcConnectionFactory
	 *
	 * @param implClass the implementation class
	 * @param dbType the type of database
	 * @return StplJdbcConnectionFactory an instance of the connection factory
	 * @throws StplConnectionException an instance of the StplConnectionException object
	 */
	public static IntgSrvJdbcConnectionFactory getFactory() throws IntgSrvBatchException
	{
		 try
		 {
			String implClass = IntgSrvPropertiesReader.getProperty(IIntgSrvConnConstants.CONNECTION_CLASS);
			factoryImpl = (IntgSrvJdbcConnectionFactory) (Class.forName(implClass).newInstance());
			// factoryImpl = new IntgSrvJdbcConnFactJdbcOdbcDriverImpl();
		 }
		 catch (IllegalAccessException e)
		 {
			 logger.fatal("Illegal eccess exception has occured: " + e);
			 throw new IntgSrvBatchException("IllegalAccessException", e);
		 }
		 catch (InstantiationException e)
		 {
			 logger.fatal("Instantiation is not possible! " + e);
			 throw new IntgSrvBatchException("InstantiationException", e);
		 }
		 catch (ClassNotFoundException e)
		  {
			 logger.fatal("Class not found exception has occurred! " + e);
			 throw new IntgSrvBatchException("ClassNotFoundException", e);
		 }
		return factoryImpl;
	}
	/**
	 * closeConnection: closes connection obj and Callable statement
	 *
	 * @param sqlCon instance of Connection class
	 * @param cStmt instance of the statement class
	 * @throws IntgSrvBatchException 
	 */
	public void closeConnection(Connection sqlCon, CallableStatement cStmt) throws IntgSrvBatchException
	{
		closeCallableStatement(cStmt);
		closeConnection(sqlCon);
	}
	/**
	 * closeConnection: closes connection obj and Callable statement
	 *
	 * @param sqlCon instance of Connection class
	 * @param cStmt instance of the statement class
	 * @throws IntgSrvBatchException 
	 */
	public void closeConnection(Connection sqlCon, PreparedStatement pStmt) throws IntgSrvBatchException
	{
		closePreparedStatement(pStmt);
		closeConnection(sqlCon);
	}
	//end of closeConnection
	/**
	 * closeConnection: closes connection obj and Callable statement
	 *
	 * @param sqlCon instance of Connection class
	 * @param cstmt instance of statement class
	 * @param rs instance of resultset interface
	 * @throws IntgSrvBatchException 
	 */
	public void closeConnection(Connection sqlCon, CallableStatement cstmt, ResultSet rs) throws IntgSrvBatchException
	{
		closeResultSet(rs);
		closeCallableStatement(cstmt);
		closeConnection(sqlCon);
	}
	/**
	 * closeConnection: closes connection obj and Callable statement
	 *
	 * @param sqlCon instance of Connection class
	 * @param cstmt instance of statement class
	 * @param rs instance of resultset interface
	 * @throws IntgSrvBatchException 
	 */
	public void closeConnection(Connection sqlCon, PreparedStatement pStmt, ResultSet rs) throws IntgSrvBatchException
	{
		closeResultSet(rs);
		closePreparedStatement(pStmt);
		closeConnection(sqlCon);
	}
	/**
	 * Method closeConnection: closes connection obj and Callable statement
	 *
	 * @param sqlCon instance of Connection
	 * @throws IntgSrvBatchException 
	 */
	public void closeConnection(Connection sqlCon) throws IntgSrvBatchException
	{
		if (sqlCon != null)
		{
			try
			{
				sqlCon.close();
			}
			catch (SQLException e)
			{
				logger.fatal("SQLException! Unable to close JDBC connection!");
				throw new IntgSrvBatchException("Unable to close JDBC Connection", e);
			}
		}
	}
	//closeConnection
	/**
	 * Method closeCallableStatement: closes the callable statement
	 *
	 * @param cStmt instance of statement interface
	 * @throws IntgSrvBatchException 
	 */
	public void closeCallableStatement(CallableStatement cStmt) throws IntgSrvBatchException
	{
		if (cStmt != null)
		{
			try
			{
				cStmt.close();
			}
			catch (SQLException e)
			{
				logger.fatal("Unable to close callable statement!" + e);
				throw new IntgSrvBatchException("Unable to close callable statement", e);
			}
		}
	}
	//end of closePreparedStatement
	/**
	 * Method closePreparedStatement: closes the prepared statement
	 *
	 * @param pStmt instance of statement interface
	 * @throws IntgSrvBatchException 
	 */
	public void closePreparedStatement(PreparedStatement pStmt) throws IntgSrvBatchException
	{
		if (pStmt != null)
		{
			try
			{
				pStmt.close();
			}
			catch (SQLException e)
			{
				logger.fatal("Unable to close callable statement!" + e);
				throw new IntgSrvBatchException("Unable to close callable statement", e);
			}
		}
	}
	//	end of closePreparedStatement
	/**
	 * Methos closeResultSet: closes the result set
	 *
	 * @param rs instance of the resultset interface
	 * @throws IntgSrvBatchException 
	 */
	public void closeResultSet(ResultSet rs) throws IntgSrvBatchException
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			}
			catch (SQLException e)
			{
				logger.fatal("Unable to close JDBC ResultSet!" + e);
				throw new IntgSrvBatchException("Unable to close JDBC ResultSet", e);
			}
		}
	}
	//end of closeResultSet
	/**
	 * Abstract method that returns a Connection object
	 * 
	 * @return returns Connection object
	 * @throws StplConnectionException instance of StplConnectionException class 
	 */
	public abstract Connection getConnection(String dsInfo) throws IntgSrvBatchException;



}
