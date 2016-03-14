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

package com.staples.pim.delegate.skuflagging.listenerrunner;

import javax.sql.DataSource;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

import com.staples.pim.base.common.listenerandrunner.BatchJobListener;

public class SkuFlaggingListener extends BatchJobListener {
	
	private DataSource ds = null;

	public SkuFlaggingListener() {
		// original DBtoCSVJoblistener
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	
	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		
		// implementatio nwould be here 
	}
	
	
	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		
		// implementatio nwould be here 
	}
	
}
