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
 * File name     	:   CreateXMLFileOnFileSystem 
 * Creation Date 	:   
 * @author  		: 	Sima Zaslavsky
 * @version 1.0
 */

package com.staples.pim.delegate.commonusecases.writer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkICD;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;

public class CreateXMLFileOnFileSystem {

	private IntgSrvLogger						ehfLogger				= IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	private IntgSrvLogger						traceLogger				= IntgSrvLogger
																				.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	private IntgSrvLogger						ehfItemLoggerXSV		= IntgSrvLogger
																				.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_XSV);
	private IntgSrvLogger						ehfItemLoggerFixLength	= IntgSrvLogger
																				.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_FIXLENGTH);
	private IntgSrvLogger						ehfItemLoggerExcel		= IntgSrvLogger
																				.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_EXCEL);
	private IntgSrvErrorHandlingFrameworkICD	ehfICD;
	private ErrorHandlingFrameworkHandler		ehfHandler				= new ErrorHandlingFrameworkHandler();
	private String								clazzname				= this.getClass().getName();
	private String								traceId;

	public CreateXMLFileOnFileSystem() {

	}

	public int writeXMLFile(StepTransmitterBean serviceBean) {

		int success = 0;
		String messageText = serviceBean.getMessage();
		String baseURL = serviceBean.getBaseURL();
		String filename = serviceBean.getFileName();
		Path pathXMLFile = Paths.get(baseURL + filename);
		try {

			Files.write(pathXMLFile, serviceBean.getMessage().getBytes(), StandardOpenOption.WRITE, StandardOpenOption.APPEND,
					StandardOpenOption.CREATE);
		} catch (FileNotFoundException e) {
			success = 1;
			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			// /String errorLogString = ehfHandler.getErrorLog(new Date(),
			// traceId, EHF_ERROR_PATH4, e, EHF_ELEM_SPRINGBATCH_USER, usecase,
			// codeModule, ehfICD);
			// ehfLogger.error(errorLogString);
			traceLogger.error(clazzname, "run", "Exception: " + ehfICD.toStringEHFExceptionStackTrace(e));
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, clazzname, serviceBean.getPublishId());
		} catch (IOException ex) {
			success = 1;
			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			// String errorLogString = ehfHandler.getErrorLog(new Date(),
			// traceId, EHF_ERROR_PATH4, ex, EHF_ELEM_SPRINGBATCH_USER, usecase,
			// codeModule, ehfICD);
			// ehfLogger.error(errorLogString);
			traceLogger.error(clazzname, "run", "Exception: " + ehfICD.toStringEHFExceptionStackTrace(ex));
			ex.printStackTrace();
			IntgSrvUtils.alertByEmail(ex, clazzname, serviceBean.getPublishId());
		} catch (Throwable ef) {
			success = 1;
			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			// String errorLogString = ehfHandler.getErrorLog(new Date(),
			// traceId, EHF_ERROR_PATH4, ef, EHF_ELEM_SPRINGBATCH_USER, usecase,
			// codeModule, ehfICD);
			// ehfLogger.error(errorLogString);
			traceLogger.error(clazzname, "run", "Exception: " + ehfICD.toStringEHFExceptionStackTrace(ef));
			ef.printStackTrace();
			IntgSrvUtils.alertByEmail(ef, clazzname, serviceBean.getPublishId());
		}
		return success;
	}
}
