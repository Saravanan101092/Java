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

import java.io.BufferedReader; 
import java.io.File;
import java.io.FileReader; 
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Arrays; 
import java.util.HashMap;  
import java.util.Iterator;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler; 
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.errorhandling.*;
import com.staples.pim.base.common.logging.IntgSrvLogger; 
import com.staples.pim.base.domain.ProductAttributesInProcess;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;

public abstract class RunScheduler {

	public RunScheduler() {
		// TODO Auto-generated constructor stub
	}	 
	
	protected IntgSrvLogger ehfLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	protected IntgSrvLogger traceLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER); 
	protected IntgSrvLogger ehfItemLoggerXSV = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_XSV);
	protected IntgSrvLogger ehfItemLoggerCSV = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_CSV);
	protected IntgSrvLogger ehfItemLoggerFixLength = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_FIXLENGTH);
	protected IntgSrvLogger ehfItemLoggerExcel = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_EXCEL);
	protected ErrorHandlingFrameworkICD ehfICD;
	protected ErrorHandlingFrameworkHandler ehfHandler = new ErrorHandlingFrameworkHandler(); 
	
	protected String clazzname = this.getClass().getName();
	protected String traceId; 
	
	protected ArrayList<String> ehfItemsLogHeadingFields = new ArrayList<String> (Arrays.asList(
			IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_INPUT_FILENAME,
			IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_STEP_EXPORT_TIME,
			IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_JOBRUNNER,
			IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TRANSFORM,
			ErrorHandlingFrameworkICD.EHF_ATTR_PUBLISH_ID,
			IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_OUTPUT_FILENAME
	));
	
	protected void init() throws ErrorHandlingFrameworkException {			
		ProductAttributesInProcess.init();
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM, 
														IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME,
														IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
	}	
	
	public abstract void run();
	

	protected void createWriterOutputDir(String directoryName) {
		File outDir= new File (directoryName);
		if (!outDir.exists()){
			if (outDir.mkdirs()) IntgSrvUtils.printConsole("LW-DEBUG: RunScheduler.createWriterOutputDirs | output dir CREATED: " + directoryName);
		}
	}
	
	protected String getSTEPExportTime(String exportFullPathFilename) throws IOException {
		String line = null;
		String exportTimeText = null;
		
		BufferedReader br = new BufferedReader(new FileReader(exportFullPathFilename));
		while ((line = br.readLine()) != null) {
			if (line.contains("<STEP-ProductInformation") && line.contains("ExportTime=")){
				exportTimeText = line;
				break;
			}
		}
		
		br.close();
		String timeFmt = "_YYYY-MM-DD_HH:MM:SS_";
		int idx = exportTimeText.indexOf("ExportTime=") + "ExportTime=".length();
		String timestampSTEPExport = exportTimeText.substring(idx,idx+timeFmt.length());
		 
		return timestampSTEPExport;
	}
	
	protected void resetEHFLogFileForItemLoggers(String filePathPrefix, String rollName) { 

		String logfilename = filePathPrefix + "/xsv/" + rollName;
		ehfItemLoggerXSV.setLogFilename(IntgSrvAppConstants.EHF_LOGGER_APPENDER_PRODUCTITEMS_XSV, logfilename); 
		
		ehfItemLoggerCSV.setLogFilename(IntgSrvAppConstants.EHF_LOGGER_APPENDER_PRODUCTITEMS_XSV, logfilename);
		
		logfilename = filePathPrefix + "/fixlength/" + rollName;		 
		ehfItemLoggerFixLength.setLogFilename(IntgSrvAppConstants.EHF_LOGGER_APPENDER_PRODUCTITEMS_FIXLENGTH, logfilename);
		
		logfilename = filePathPrefix + "/excel/" + rollName;		 
		ehfItemLoggerExcel.setLogFilename(IntgSrvAppConstants.EHF_LOGGER_APPENDER_PRODUCTITEMS_EXCEL, logfilename);	 
	}
	
	protected void writeEHFItemLogHeading(IntgSrvLogger logger, HashMap<String, String> itemsLogHeadingMap) { 
		
		logger.info("[ - BEGIN - ]");
		for (Iterator<String> itr = ehfItemsLogHeadingFields.iterator(); itr.hasNext();){
			String key = itr.next();
			String value = itemsLogHeadingMap.get(key);
			logger.info("[ " + key + ": " + value + " ]");
		}	  
	} 

	protected abstract StepTransmitterBean jobLaunch(StepTransmitterBean transmitter);  
	
}
