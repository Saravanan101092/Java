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

package com.staples.pim.base.common.errorhandling; 

import static com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD.*;


public class IntgSrvErrorHandlingFrameworkConstants {

	public static final String SPRINGBATCH_ECO_SYSTEM = "PCM - Product Content Management";
	public static final String SPRINGBATCH_COMPONENT_ID = "Spring Batch";
	public static final String SPRINGBATCH_ICD_CLASSNAME = "com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkICD"; 
	
	// to use default 
	public static final String EHF_ERROR_PATH1 = EHF_ERROR_PATH1_DEFAULT;
	public static final String EHF_ERROR_PATH2 = EHF_ERROR_PATH2_DEFAULT;
	public static final String EHF_ERROR_PATH3 = EHF_ERROR_PATH3_DEFAULT;
	public static final String EHF_ERROR_PATH4 = EHF_ERROR_PATH4_DEFAULT;
	public static final String EHF_ERROR_PATH5 = EHF_ERROR_PATH5_DEFAULT;	 
	
	//EHF Element and Attribute value constants:
	public static final String EHF_ELEM_SPRINGBATCH_USER = "springbatchjob";
	public static final String EHF_ELEM_VALUE_UNAVAILABLE = "N/A";
	
	//EHF Free Form Attribute keys:
	public static final String EHF_ATTR_STEP_EXPORT_TIME = "STEPExportTime";
	public static final String EHF_ATTR_INPUT_FILENAME = "InputFileName";
	public static final String EHF_ATTR_OUTPUT_FILENAME = "OutputFileName";
	public static final String EHF_ATTR_COMMENTS = "Comments";
	
	public static final String EHF_ATTR_TRACEID_TYPE_KEY = "TraceIdType";
	public static final String EHF_ATTR_TRACEID_TYPE_JOBRUNNER = "JobRunner";
	public static final String EHF_ATTR_TRACEID_TYPE_TRANSFORM = "Transform";
	public static final String EHF_ATTR_TRACEID_TYPE_ITEM = "Item"; //default if not specified
	
	public static final String EHF_ATTR_TRACEID_JOBRUNNER = "TraceIdJobRunner";
	public static final String EHF_ATTR_TRACEID_TRANSFORM = "TraceIdTransform";
	
	public static final String EHF_ATTR_TRANSACTION_TYPE_FILETOFILE = "FileToFile";
	public static final String EHF_ATTR_TRANSACTION_TYPE_DBTOFILE = "DBToFile";
	public static final String EHF_ATTR_TRANSACTION_TYPE_FILETODB = "FileToDB";
	public static final String EHF_ATTR_TRANSACTION_TYPE_DBTODB = "DBToDB";
	
	
	


}
