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
 * File name     	:   WriteTxtFileToFileSystem
 * Creation Date 	:   
 * @author  		:  Sima Zaslavsky
 * @version 1.0
 */

package com.staples.pim.delegate.commonusecases.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkICD;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;

public class CreateFlatFileOnFileSystem {

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

	public CreateFlatFileOnFileSystem() {

		// TODO Auto-generated constructor stub
	}

	public int writeOutFile(StepTransmitterBean serviceBean) {

		// FileWriter; Print Writer
		int status = 0;
		StringBuffer stringBuffer = new StringBuffer();
		File outFile = null;
		BufferedWriter bWriter = null;
		String headerLine = (String) serviceBean.getItem(IntgSrvAppConstants.HEADER_LINE_TO_MAP);
		String messageText = serviceBean.getMessage();

		try {
			String url = serviceBean.getBaseURL();
			String b = serviceBean.getFileName();
			outFile = new File(serviceBean.getBaseURL().trim() + serviceBean.getFileName().trim());
			outFile.createNewFile();
			if (!outFile.canWrite()) {
				status = 1;
			}

			bWriter = new BufferedWriter(new FileWriter(outFile));

			// build the file

			if (headerLine != null) {
				stringBuffer.append(headerLine.trim());
				bWriter.write(stringBuffer.toString());
				stringBuffer.delete(0, stringBuffer.length());
			}
			if (messageText != null) {
				bWriter.newLine();
				stringBuffer.append(messageText.trim());
				bWriter.write(stringBuffer.toString());
			}

			bWriter.flush();

		} catch (FileNotFoundException e) {
			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			status = 1;
			// String errorLogString = ehfHandler.getErrorLog(new Date(),
			// traceId, EHF_ERROR_PATH4, e, EHF_ELEM_SPRINGBATCH_USER, usecase,
			// codeModule, ehfICD);
			// ehfLogger.error(errorLogString);
			traceLogger.error(clazzname, "run", "Exception: " + ehfICD.toStringEHFExceptionStackTrace(e));
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, clazzname, serviceBean.getPublishId());
		} catch (IOException ex) {
			status = 1;
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
			status = 1;
			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			// String errorLogString = ehfHandler.getErrorLog(new Date(),
			// traceId, EHF_ERROR_PATH4, ef, EHF_ELEM_SPRINGBATCH_USER, usecase,
			// codeModule, ehfICD);
			// ehfLogger.error(errorLogString);
			traceLogger.error(clazzname, "run", "Exception: " + ehfICD.toStringEHFExceptionStackTrace(ef));
			ef.printStackTrace();
			IntgSrvUtils.alertByEmail(ef, clazzname, serviceBean.getPublishId());
		} finally {
			try {
				bWriter.flush();
				bWriter.close();
			} catch (IOException ex) {
				status = 1;
				ex.printStackTrace();
				IntgSrvUtils.alertByEmail(ex, clazzname, serviceBean.getPublishId());
			}
		}
		return status;
	}

}
