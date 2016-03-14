package com.staples.pim.base.persistence.daofactory;

import java.sql.CallableStatement;
import java.sql.Connection; 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.staples.pim.base.common.exception.IntgSrvBatchException;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.persistence.connection.IIntgSrvConnConstants;
import com.staples.pim.base.persistence.util.IntgSrvJDBCUtils; 
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.locationlevel.bean.StepLocationLevelPushDownData;

public class LocationLevelPushDownDAOImpl implements ILocationLevelPushDownDAOLev{


	/**
	 * Used for logging
	 */	 
	private static IntgSrvLogger LOGGER = IntgSrvLogger.getInstance("FreeformTraceLogger");  
	/**
	 * Data Base Info for JNDI lookup
	 */
	 @SuppressWarnings("unused")
	private String dbInfoRet = IIntgSrvConnConstants.AS400SERVER_GALAXY_PIM;
	 
	private String dbInfoScc = IIntgSrvConnConstants.AS400SERVER_SUNBEAM_PIM;
	 
	/**
	* Connection Object
	*/
	private Connection conn = null;
		
	/**
	* Default Constructor
	*/
	public LocationLevelPushDownDAOImpl() 
	{
		super();
			 
	}
	/**
	* Constructor
	* 
	* @param conn - Connection Object
	*/
	public LocationLevelPushDownDAOImpl(Connection conn)
	{
		super();
		this.conn = conn;
	}
		
	/**
	* Business Logic starts here
	*/	
	
	/**
	 * The method invokes the stored procedure to update the location level attributes on iSeries server
	 * @return List of  
	 * @throws IntgSrvBatchException 
	 */
	@SuppressWarnings("rawtypes")
	public List locationLevelPushDownRetail(StepLocationLevelPushDownData inputTO) throws IntgSrvBatchException
	{
		LOGGER.info("In locationLevelPushDownRetail: push down..."); 
		
		StepLocationLevelPushDownData stepLocationLevelTO = inputTO; 
		
		IntgSrvJDBCUtils jdbcUtils =
			new IntgSrvJDBCUtils(
					conn, 
					stepLocationLevelTO, 
					IntgSrvUtils.prepareQuery(stepLocationLevelTO.getProcNameRet()),
					dbInfoRet) 
		{
			 
			/*
			 * Prepare the Prepared Statement  
			 */
			   public CallableStatement prepareCall(String query, CallableStatement stmt) throws SQLException
			   {
				   StepLocationLevelPushDownData dataTO = (StepLocationLevelPushDownData) data;			   		
			   		stmt.setString(1, dataTO.getKeyLine());
			   		LOGGER.debug("Input KeyLIne is " + (dataTO.getKeyLine()).toUpperCase());
			   		stmt.setString(2, dataTO.getDataline1());
			   		LOGGER.debug("Input dataLine is " + (dataTO.getDataline1()).toUpperCase());
			   		stmt.setString(3, dataTO.getDataline2());
			   		stmt.setString(4, dataTO.getDataline3());
			   		stmt.setString(5, dataTO.getDataline4());
			   		stmt.setString(6, dataTO.getDataline5());
			   		stmt.setString(7, dataTO.getDataline6());
			   		stmt.setString(8, dataTO.getDataline7());
			   		stmt.setString(9, dataTO.getDataline8());
			   		stmt.setString(10, dataTO.getDataline9());
			   		stmt.setString(11, dataTO.getDataline10());
			   		stmt.setString(12, dataTO.getDataline11());
			   		stmt.setString(13, dataTO.getDataline12());
			   					   		 
			   		stmt.registerOutParameter(14, java.sql.Types.CHAR);
					stmt.registerOutParameter(15, java.sql.Types.CHAR);
					stmt.registerOutParameter(16, java.sql.Types.CHAR);
					stmt.registerOutParameter(17, java.sql.Types.CHAR);
					stmt.registerOutParameter(18, java.sql.Types.CHAR);
					stmt.registerOutParameter(19, java.sql.Types.CHAR);
					stmt.registerOutParameter(20, java.sql.Types.CHAR);
					stmt.registerOutParameter(21, java.sql.Types.CHAR);
					stmt.registerOutParameter(22, java.sql.Types.CHAR);
					stmt.registerOutParameter(23, java.sql.Types.CHAR);
					stmt.registerOutParameter(24, java.sql.Types.CHAR);
					stmt.registerOutParameter(25, java.sql.Types.CHAR);
					stmt.registerOutParameter(26, java.sql.Types.CHAR);
					stmt.registerOutParameter(27, java.sql.Types.CHAR);
					stmt.registerOutParameter(28, java.sql.Types.CHAR);					
			   		
			   		return stmt;
			   }	
			/*
			 * Maps the ResultSet to the Transfer Object
			 */
			@SuppressWarnings("unchecked")
			public List resultMapper(CallableStatement stmt) throws SQLException
			{				
				List  listOfStrings  = new ArrayList();
				try
				{   
					//String c = ((String)stmt.getObject(14)).trim();
					//System.out.println(c);
					 
					listOfStrings.add(0, ((String)stmt.getObject(14)).trim());
					listOfStrings.add(1, ((String)stmt.getObject(15)).trim());
					listOfStrings.add(2, ((String)stmt.getObject(16)).trim());
					listOfStrings.add(3, ((String)stmt.getObject(17)).trim());
					listOfStrings.add(4, ((String)stmt.getObject(18)).trim());
					listOfStrings.add(5, ((String)stmt.getObject(19)).trim());
					listOfStrings.add(6, ((String)stmt.getObject(20)).trim());
					listOfStrings.add(7, ((String)stmt.getObject(21)).trim());
					listOfStrings.add(8, ((String)stmt.getObject(22)).trim());
					listOfStrings.add(9, ((String)stmt.getObject(23)).trim());
					listOfStrings.add(10, ((String)stmt.getObject(24)).trim());
					listOfStrings.add(11, ((String)stmt.getObject(25)).trim());
					listOfStrings.add(12, ((String)stmt.getObject(26)).trim());
					listOfStrings.add(13, ((String)stmt.getObject(27)).trim());
					listOfStrings.add(14, ((String)stmt.getObject(28)).trim());
					LOGGER.debug("resultMapper::Object14=" + stmt.getObject(14));
					LOGGER.debug("resultMapper::listOfStrings=" + listOfStrings);
				}
				catch(SQLException sqlexc)
				{
					//   logger.fatal("mapResults_4 :: Unable to extract object from the statement. " + sqlexc.getMessage(), sqlexc);
					sqlexc.printStackTrace();
					//throw new IntgSrvBatchException(IntgSrvAppConstants.UNABLE_EXTRACT_OBJECT_FROM_CALLABLE_STATEMENT_ERROR_MSG_TEXT + 
							//IntgSrvAppConstants.TILDA_DELIM    +
												   //sqlexc.getMessage().trim() +
												  // IntgSrvAppConstants.TILDA_DELIM);
				}
				
				return listOfStrings;
			}
		};
		try
		{
    		/*
    		 * Execute Query
    		 */
    		return (List)jdbcUtils.execute();
    		
 		} 
		catch (Exception ex)
		{
 			throw new IntgSrvBatchException("Exception trying to Update location LevelUpdateRetChannel", ex);
		}
		 
		
	}  

	/**
	 * The method invokes the stored procedure to update the location level attributes on iSeries server
	 * @return List of  
	 * @throws IntgSrvBatchException 
	 */
	@SuppressWarnings("rawtypes")
	public List locationLevelPushDownNad(StepLocationLevelPushDownData inputTO) throws IntgSrvBatchException
	{
		LOGGER.info("In locationLevelPushDownNad: push down..."); 
		
		StepLocationLevelPushDownData stepLocationLevelTO = inputTO; 
		
		IntgSrvJDBCUtils jdbcUtils =
			new IntgSrvJDBCUtils(
					conn, 
					stepLocationLevelTO, 
					IntgSrvUtils.prepareQuery(stepLocationLevelTO.getProcNameNad()),
					dbInfoScc) 
		{
			 
			/*
			 * Prepare the Prepared Statement  
			 */
			   public CallableStatement prepareCall(String query, CallableStatement stmt) throws SQLException
			   {
				   StepLocationLevelPushDownData dataTO = (StepLocationLevelPushDownData) data;			   		
			   		stmt.setString(1, dataTO.getKeyLine());
			   		LOGGER.debug("Input KeyLIne is " + (dataTO.getKeyLine()).toUpperCase());
			   		stmt.setString(2, dataTO.getDataline1());
			   		LOGGER.debug("Input dataLine is " + (dataTO.getDataline1()).toUpperCase());
			   		stmt.setString(3, dataTO.getDataline2());
			   		stmt.setString(4, dataTO.getDataline3());
			   		stmt.setString(5, dataTO.getDataline4());
			   		stmt.setString(6, dataTO.getDataline5());
			   		stmt.setString(7, dataTO.getDataline6());
			   		stmt.setString(8, dataTO.getDataline7());
			   		stmt.setString(9, dataTO.getDataline8());
			   		stmt.setString(10, dataTO.getDataline9());
			   		stmt.setString(11, dataTO.getDataline10());
			   		stmt.setString(12, dataTO.getDataline11());
			   		stmt.setString(13, dataTO.getDataline12());
			   					   		 
			   		stmt.registerOutParameter(14, java.sql.Types.CHAR);
					stmt.registerOutParameter(15, java.sql.Types.CHAR);
					stmt.registerOutParameter(16, java.sql.Types.CHAR);
					stmt.registerOutParameter(17, java.sql.Types.CHAR);
					stmt.registerOutParameter(18, java.sql.Types.CHAR);
					stmt.registerOutParameter(19, java.sql.Types.CHAR);
					stmt.registerOutParameter(20, java.sql.Types.CHAR);
					stmt.registerOutParameter(21, java.sql.Types.CHAR);
					stmt.registerOutParameter(22, java.sql.Types.CHAR);
					stmt.registerOutParameter(23, java.sql.Types.CHAR);
					stmt.registerOutParameter(24, java.sql.Types.CHAR);
					stmt.registerOutParameter(25, java.sql.Types.CHAR);
					stmt.registerOutParameter(26, java.sql.Types.CHAR);
					stmt.registerOutParameter(27, java.sql.Types.CHAR);
					stmt.registerOutParameter(28, java.sql.Types.CHAR);					
			   		
			   		return stmt;
			   }	
			/*
			 * Maps the ResultSet to the Transfer Object
			 */
			@SuppressWarnings("unchecked")
			public List resultMapper(CallableStatement stmt) throws SQLException
			{				
				List  listOfStrings  = new ArrayList();
				try
				{   
					//String c = ((String)stmt.getObject(14)).trim();
					//System.out.println(c);
					 
					listOfStrings.add(0, ((String)stmt.getObject(14)).trim());
					listOfStrings.add(1, ((String)stmt.getObject(15)).trim());
					listOfStrings.add(2, ((String)stmt.getObject(16)).trim());
					listOfStrings.add(3, ((String)stmt.getObject(17)).trim());
					listOfStrings.add(4, ((String)stmt.getObject(18)).trim());
					listOfStrings.add(5, ((String)stmt.getObject(19)).trim());
					listOfStrings.add(6, ((String)stmt.getObject(20)).trim());
					listOfStrings.add(7, ((String)stmt.getObject(21)).trim());
					listOfStrings.add(8, ((String)stmt.getObject(22)).trim());
					listOfStrings.add(9, ((String)stmt.getObject(23)).trim());
					listOfStrings.add(10, ((String)stmt.getObject(24)).trim());
					listOfStrings.add(11, ((String)stmt.getObject(25)).trim());
					listOfStrings.add(12, ((String)stmt.getObject(26)).trim());
					listOfStrings.add(13, ((String)stmt.getObject(27)).trim());
					listOfStrings.add(14, ((String)stmt.getObject(28)).trim());
					LOGGER.debug("resultMapper::Object14=" + stmt.getObject(14));
					LOGGER.debug("resultMapper::listOfStrings=" + listOfStrings);
					
				}
				catch(SQLException sqlexc)
				{
					//   logger.fatal("mapResults_4 :: Unable to extract object from the statement. " + sqlexc.getMessage(), sqlexc);
					sqlexc.printStackTrace();
					//throw new IntgSrvBatchException(IntgSrvAppConstants.UNABLE_EXTRACT_OBJECT_FROM_CALLABLE_STATEMENT_ERROR_MSG_TEXT + 
							//IntgSrvAppConstants.TILDA_DELIM    +
												   //sqlexc.getMessage().trim() +
												  // IntgSrvAppConstants.TILDA_DELIM);
				}
				
				return listOfStrings;
			}
		};
		try
		{
    		/*
    		 * Execute Query
    		 */
    		return (List)jdbcUtils.execute();
    		
 		} 
		catch (Exception ex)
		{
 			throw new IntgSrvBatchException("Exception trying to Update location LevelUpdateNadChannel", ex);
		}
		 
		
	}  

}
