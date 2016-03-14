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
 * File name     :  LocationLevelPushDownWriter.java
 * Creation Date :  7/13/2015 
 * @author       :  Sima Zaslavsky
 * @version 1.0
 */ 

package com.staples.pim.delegate.locationlevel.writer;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
  
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.persistence.daofactory.ILocationLevelPushDownDAOLev; 
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvLocationLevelUtils; 
import com.staples.pim.delegate.locationlevel.bean.StepLocationLevelAttrib;
import com.staples.pim.delegate.locationlevel.bean.StepLocationLevelPushDownData; 


 

 
public class LocationLevelPushDownWriter<StepLocationLevelAttrib> implements ItemWriter<StepLocationLevelAttrib>, StepExecutionListener {

	private IntgSrvLogger traceLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	private String clazzname = this.getClass().getName();

	private Map<String, JobParameter> jpMap;
	private String queryNameRetail = null;
	private String queryNameSCC = null;
	
	private ILocationLevelPushDownDAOLev locationLevelPushDownDao = null;
	  
	
	StepLocationLevelPushDownData locationLevelData_100 = null;
	StepLocationLevelPushDownData locationLevelData_101 = null;
	StepLocationLevelPushDownData locationLevelData_102 = null;
	
	
	 
	
	
	@SuppressWarnings("unchecked")
	@Override 
	public void write(List<? extends StepLocationLevelAttrib> arg0) throws Exception {
		 
		  
		HashMap<Integer, List<com.staples.pim.delegate.locationlevel.bean.StepLocationLevelAttrib>> attributesCollectionSort = null;
		
		for (StepLocationLevelAttrib bean : arg0) 
		{			
			com.staples.pim.delegate.locationlevel.bean.StepLocationLevelAttrib locationLevelAttribBean = 
				((com.staples.pim.delegate.locationlevel.bean.StepLocationLevelAttrib) bean);

			attributesCollectionSort = (HashMap<Integer, List<com.staples.pim.delegate.locationlevel.bean.StepLocationLevelAttrib>>)
								IntgSrvLocationLevelUtils.sortBulkUpload(locationLevelAttribBean, attributesCollectionSort); 
		
	    }
		
		
		for (int i = 100; i < attributesCollectionSort.size(); i++ )
		{
			
			List<StepLocationLevelAttrib> listOfData = (List<StepLocationLevelAttrib>)attributesCollectionSort.get(i);
			
			switch (i)
			{
			 case 100 :
				 locationLevelData_100 = IntgSrvLocationLevelUtils.getDataForRebuyerUpload(
						 							(List<com.staples.pim.delegate.locationlevel.bean.StepLocationLevelAttrib>) listOfData);
						 							 
				 break;
				 
			 case 101 :
				 locationLevelData_101 = IntgSrvLocationLevelUtils.getDataForVendorRetUpload(
						 							(List<com.staples.pim.delegate.locationlevel.bean.StepLocationLevelAttrib>) listOfData);
				 break;
				 
			 case 102 :
				 locationLevelData_102 = IntgSrvLocationLevelUtils.getDataForVendorCorUpload(
						 							(List<com.staples.pim.delegate.locationlevel.bean.StepLocationLevelAttrib>) listOfData);
				 break; 
			
			}
		} 
		
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		traceLogger.info(clazzname, "beforeStep", "ENTER: StepExecutionListener");
		try {
			System.out.println("Calling beforeStep");

			/*
			 * Setting Default Informations - Start
			 */
			 
			// set locationLevelData_100 with query name  SIMA !!!!!!!!!!!!!!!!!!!
			/*
			 * Setting Default Informations - End
			 */

			jpMap = stepExecution.getJobParameters().getParameters();
			queryNameRetail = jpMap.get(IntgSrvAppConstants.JP_LOCATION_LEVEL_QUERY_NAME_RETAIL).toString();
			queryNameSCC = jpMap.get(IntgSrvAppConstants.JP_LOCATION_LEVEL_QUERY_NAME_SCC).toString();
			  
			String msgDesc = "IntegrationServices LocationLevelPushDownWriter initialized <- job params, StepExecutionContext";
			traceLogger.info(clazzname, "beforeStep", msgDesc);
		} catch (Throwable e) {
			traceLogger.error(clazzname, "beforeStep", e);
		}
	
		
	}
	
	@Override
	public ExitStatus afterStep(StepExecution arg0) {
		 
		
		// invoke DAO class to run QRY
		//try {
			//locationLevelPushDownDao = IntgSrvDAOFactory.getLocationLevelDAO();
		//} catch (IntgSrvBatchException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		
		//if (locationLevelData_100 != null)
		//{			
			//locationLevelPushDownDao.locationLevelPushDownRetail(locationLevelData_100);
		//}
		
		//if (locationLevelData_101 != null)
		//{			
			//locationLevelPushDownDao.locationLevelPushDownRetail(locationLevelData_101);
		//}
		
		//if (locationLevelData_102 != null)
		//{			
			//locationLevelPushDownDao.locationLevelPushDownRetail(locationLevelData_102);
			// locationLevelPushDownDao.locationLevelPushDownScc(locationLevelData_102);
			
		//}
		return null;
	}
 

}
