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

package com.staples.pim.base.persistence.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.staples.pim.base.common.exception.IntgSrvBatchException;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.persistence.connection.IIntgSrvConnConstants;
import com.staples.pim.base.persistence.connection.IntgSrvJdbcConnectionFactory;
import com.staples.pim.base.util.InterruptBlockedThread;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.createupdateitem.listenerrunner.RunSchedulerItemCreateUpdate;
import com.staples.pim.delegate.locationlevel.bean.StepLocationLevelPushDownData;
 

/**
 * This class is used as a Callback handler for JDBC queries
 */
public abstract class IntgSrvJDBCUtils 
{ 	
	private Connection conn;
	protected Object data;
	private String query;
	protected ResultSet rs;
	//private String dbInfo = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM;  
	private String dbInfo = null;
	
	Thread t;
	
	private static IntgSrvLogger LOGGER = IntgSrvLogger.getInstance("FreeformTraceLogger");  
	/**
	 * No args constructor
	 *
	 */
	public IntgSrvJDBCUtils()
	{}
	/**
	 * Method used to return a Connection object
	 *
	 * @return The Connection object is returned
	 * @throws IntgSrvBatchException 
	 */
	private Connection getConnection() throws IntgSrvBatchException
	{
		try
		{
			if (conn == null || (conn != null && conn.isClosed()))
			{
				conn = IntgSrvJdbcConnectionFactory.getFactory().getConnection(dbInfo);
			}
		}
		catch (SQLException sqe)
		{

		}
		return conn;
	}
	/**
	 * Three argument constructor that is used to set the class variables
	 *
	 * @param conn The connection object
	 * @param data  The object data
	 * @param query The query to execute
	 * @throws IntgSrvBatchException 
	 */
	public IntgSrvJDBCUtils(Connection conn, Object data, String query) throws IntgSrvBatchException
	{
		super();
		this.conn = conn != null ? conn : getConnection();
		this.data = data;
		this.query = query;
	}
	/**
	 * Four argument constructor that is used to set the class variables including dbType
	 *
	 * @param conn The connection object
	 * @param data  The object data
	 * @param query The query to execute
	 * @throws IntgSrvBatchException 
	 */
	public IntgSrvJDBCUtils(Connection conn, Object data, String query, String dbInfo) throws IntgSrvBatchException
	{
		super();
		this.dbInfo = dbInfo;
		this.conn = conn != null ? conn : getConnection();
		this.data = data;
		this.query = query;
	}
	/**
	 * Protected abstract method to be implemented in the DAOImpl
	 *
	 * @param query the query to execute
	 * @param stmt the statement object
	 * @return returns a Callable statement object
	 * @throws SQLException sqlEx
	 */
	protected CallableStatement prepareCall(String query, CallableStatement stmt) throws SQLException
	{
		return null;
	}
	/**
	 * Protected abstract method to be implemented in the DAOImpl
	 *
	 * @param query the query to execute
	 * @param stmt the statement object
	 * @return returns a Callable statement object
	 * @throws SQLException sqlEx
	 */
	protected PreparedStatement prepareQuery(String query, PreparedStatement stmt) throws SQLException
	{
		return null;
	}
	/**
	 * Protected abstract method to be implemented in the DAOImpl
	 *
	 * @param stmt the statement object
	 * @return returns an object
	 * @throws SQLException sqlEx
	 */
	protected Object resultMapper(CallableStatement stmt) throws SQLException
	{
		return null;
	}
	/**
	 * Protected abstract method to be implemented in the DAOImpl
	 *
	 * @param stmt the statement object
	 * @return returns an object
	 * @throws SQLException sqlEx
	 */
	protected Object resultMapper(PreparedStatement stmt) throws SQLException
	{
		return null;
	}
	/**
	 * The execute method is responsible for executing the queries
	 *
	 * @return returns an object reference
	 * @throws IntgSrvBatchException 
	 */
	public Object execute() throws IntgSrvBatchException

	{
		CallableStatement stmt = null;
		Object obj = null;
		try
		{
			LOGGER.info("before execute " + query + " "+conn);
		   InterruptBlockedThread ibt = new InterruptBlockedThread(Thread.currentThread(),IIntgSrvConnConstants.LOCATION_LEVEL_QUERY_EXECUTION_MAX_TIME);
		   t = new Thread(ibt);
		   LOGGER.info("Monitored Thread " + Thread.currentThread().getName());
		   LOGGER.info("starting monitor thread...");
		   t.start();
		   System.out.println("monitor thread started");
		   StepLocationLevelPushDownData dataTO = (StepLocationLevelPushDownData) data;
			try
			{
				Thread.sleep(1);
				/* testing for blocked call
				int count = 0;
				while (count < 1000){
				   Thread.sleep(1000);
				   count ++;
				   System.out.println("slept 1000");
			    }
			    */
				boolean retry = true;
				int retryCount = 0;
				stmt = conn.prepareCall(query);
				while(retry)
				{
					try
					{
						++retryCount;
						prepareCall(query, stmt);
						stmt.execute();
						obj = resultMapper(stmt);
						retry = false;
					}
					catch(SQLException ex )
					{
						retry = retryCount>3?false:true;
						if (retryCount>3)
						{
							LOGGER.fatal("SQLException has occured in: " + query);
							String className = this.getClass().getName();
							//String category = StplErrorStatusCode.DAO_CATEGORY;
							//StplErrorStatusCode code = new StplErrorStatusCode(className, category, 901);
							throw new IntgSrvBatchException("SQLException in query " + query, ex);
						}
					}
				}
		   } catch (InterruptedException e) {
			   obj = new ArrayList();//return an empty list
			   LOGGER.debug("alert email will be sent");
			   LOGGER.debug(t.getName() + " interrupted:");
			   LOGGER.debug(e.toString());
			   LOGGER.debug("KeyLine="+dataTO.getKeyLine() + "; Dataline1="+dataTO.getDataline1());
			   
			   IntgSrvUtils.alertByEmail(e, this.getClass().getName(), "KeyLine="+dataTO.getKeyLine() + "; Dataline1="+dataTO.getDataline1());
			   LOGGER.debug("alert email sent!");
		   }
		   if (!t.interrupted()) {
			   t.interrupt();
		   }
		   // block until other threads finish
		   try {  
			   LOGGER.debug("main thread waiting joinning");
			   //Thread.currentThread().join();
			   t.join();
		   } catch(InterruptedException e) {
			   LOGGER.debug(Thread.currentThread().getName() + " interrupted:");
		   }

		}
		catch (Exception ex)
		{
			LOGGER.fatal("Exception has occured in: " + query);
			String className = this.getClass().getName();
			//String category = StplErrorStatusCode.DAO_CATEGORY;
			//StplErrorStatusCode code = new StplErrorStatusCode(className, category, 901);
			throw new IntgSrvBatchException("SQLException in query " + query, ex);
		}
		finally
		{
			IntgSrvJdbcConnectionFactory.getFactory().closeConnection(conn, stmt, rs);
		}
		return obj;
	}
	/**
	 * The execute method is responsible for executing the queries
	 *
	 * @param prepStmt a flag used when using prepared statement
	 * @return returns an object reference
	 * @throws IntgSrvBatchException 
	 */
	public Object execute(boolean prepStmt) throws IntgSrvBatchException
	{
		PreparedStatement stmt = null;
		Object obj = null;
		try
		{
			LOGGER.info("before execute " + query + " "+conn);
			long startTime = 0;
			//if (IStplDAOConstants.LOG_QUERY_EXECUTION_TIME)
			//{
				//startTime = System.currentTimeMillis();
			//}
			boolean retry = true;
			int retryCount = 0;
			while(retry)
			{
				try
				{
					++retryCount;
					stmt = conn.prepareCall(query);
					prepareQuery(query, stmt);
					stmt.execute();
					obj = resultMapper(stmt);
					retry = false;
				}
				catch(SQLException ex )
				{
					retry = retryCount>3?false:true;
					if (retryCount>3)
					{
						LOGGER.fatal("SQLException has occured in: " + query);
						String className = this.getClass().getName();
						//String category = StplErrorStatusCode.DAO_CATEGORY;
						//StplErrorStatusCode code = new StplErrorStatusCode(className, category, 901);
						throw new IntgSrvBatchException("SQLException in query " + query, ex);
					}
				}
			}

			//if (IStplDAOConstants.LOG_QUERY_EXECUTION_TIME)
			//{
				//logger.info("Time== " + (System.currentTimeMillis() - startTime));
			//}
		}
		catch (Exception ex)
		{
			LOGGER.fatal("Exception has occured in: " + query);
			String className = this.getClass().getName();
			//String category = StplErrorStatusCode.DAO_CATEGORY;
			//StplErrorStatusCode code = new StplErrorStatusCode(className, category, 901);
			throw new IntgSrvBatchException("SQLException in query " + query, ex);
		}
		finally
		{
			IntgSrvJdbcConnectionFactory.getFactory().closeConnection(conn, stmt, rs);
		}
		return obj;
	}
}
