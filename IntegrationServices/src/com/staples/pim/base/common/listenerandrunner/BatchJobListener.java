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

package com.staples.pim.base.common.listenerandrunner;

import java.io.File;
import java.io.IOException;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
// import org.springframework.batch.core.annotation.AfterJob; 
import org.springframework.stereotype.Component;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;


@Component
public abstract class BatchJobListener implements JobExecutionListener {
	
	protected IntgSrvLogger ehfLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	protected IntgSrvLogger traceLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	protected ErrorHandlingFrameworkICD ehfICD = null;
	protected ErrorHandlingFrameworkHandler ehfHandler = new ErrorHandlingFrameworkHandler();
	protected String clazzname = this.getClass().getName(); 
	protected String traceId = null;
	protected String publishIds = null;
	
	protected void init() throws ErrorHandlingFrameworkException {
		ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
													IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM, 
													IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID, 
													IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
	}
	 
	// @AfterJob
	public abstract void afterJob(JobExecution jobExecution);   
	
	 
	public abstract void beforeJob(JobExecution jobExecution); 

}
