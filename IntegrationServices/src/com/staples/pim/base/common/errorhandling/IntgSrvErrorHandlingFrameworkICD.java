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

import java.util.ArrayList;
import java.util.Arrays;


import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;

import static com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants.*;

public class IntgSrvErrorHandlingFrameworkICD extends ErrorHandlingFrameworkICD {
	
	
	public IntgSrvErrorHandlingFrameworkICD(String ecoSystem, String componentId) {
		super(ecoSystem, componentId);
	}
	 
	public String getNewTraceId() {
		String traceId = createNewTraceId();
		setTraceId(traceId);
		return traceId;
	}
	
	public void validateErrorPath(String errorPath) {
		ArrayList<String> validErrorPaths = new ArrayList<String> (Arrays.asList(
				EHF_ERROR_PATH1,
				EHF_ERROR_PATH2,
				EHF_ERROR_PATH3,
				EHF_ERROR_PATH4,
				EHF_ERROR_PATH5
		));	
		if(!isValidValue(errorPath, validErrorPaths)) 
			getAttributes().put(EHF_ATTR_ICD_INVALID_ERRORPATH, errorPath);
		
	}	

}
