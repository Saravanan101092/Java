package com.staples.pim.base.persistence.daofactory;

import java.util.List;

import com.staples.pim.base.common.exception.IntgSrvBatchException;
import com.staples.pim.delegate.locationlevel.bean.StepLocationLevelPushDownData;

public interface ILocationLevelPushDownDAOLev {


	
	/**
	 * The method invokes the stored procedure to update the location level attributes on Retail iSeries server
	 * @return List of  
	 * @throws IntgSrvBatchException 
	 */
	  
	@SuppressWarnings("rawtypes")
	public List locationLevelPushDownRetail(StepLocationLevelPushDownData inputTO)throws IntgSrvBatchException  ;
	
	/**
	 * The method invokes the stored procedure to update the location level attributes on Nad iSeries server
	 * @return List of  
	 * @throws IntgSrvBatchException 
	 */
	  
	// @SuppressWarnings("rawtypes")
	public List locationLevelPushDownNad(StepLocationLevelPushDownData inputTO)throws IntgSrvBatchException  ;
}
