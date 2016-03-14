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

package com.staples.pim.delegate.createupdateitem.listenerrunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


import org.apache.poi.ss.usermodel.Cell;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.pim.base.common.bean.FTPConnectionBean;
import com.staples.pim.base.common.bean.STEPProductInformation;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkICD;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.domain.ProductAttributesInProcess;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.SFTPManager;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvLocationLevelUtils;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.commonusecases.writer.PublisherToMQ;
import com.staples.pim.delegate.datamigration.utils.DataMigAcceptItemManager;
import com.staples.pim.delegate.locationlevel.bean.StepLocationLevelAttrib;
import com.staples.pim.delegate.locationlevel.bean.LocationDataAudit;
import com.staples.pim.delegate.locationlevel.service.LocationLevelPushDownService;

public class RunSchedulerItemCreateUpdate extends RunScheduler {

	public static HashMap<String, String>		attrIDNameHashMap				= new HashMap();
	public static HashMap<String, String>		SKUID_ItemIDHashMap				= new HashMap();
	public static HashMap						assetHashMap					= new HashMap();
	public static HashMap<String, HashMap>		classificationHashMap			= new HashMap();
	public static File							currentFile						= null;
	public static boolean						bContainsSKU					= false;
	public static List<StepLocationLevelAttrib>	listOfStepLocationLevelAttrib	= new ArrayList<StepLocationLevelAttrib>();
	public static List<LocationDataAudit> listOfLocationDataAudit = new ArrayList<LocationDataAudit>(); 
	public static String						publishIds						= IntgSrvAppConstants.BRACKET_LEFT
																						+ IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_XSV
																						+ IntgSrvAppConstants.COMMA
																						+ IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_FIXLENGTH
																						+ IntgSrvAppConstants.COMMA
																						+ IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_EXCEL
																						+ IntgSrvAppConstants.BRACKET_WRITE;

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	private DataSource ds = null;

	public void run() {

		traceLogger.info(clazzname, "run", "ENTER: scheduled job runner");

		try {
			init();
			listOfStepLocationLevelAttrib	= new ArrayList<StepLocationLevelAttrib>();//reset
			listOfLocationDataAudit = new ArrayList<LocationDataAudit>();//reset
			
			SimpleDateFormat dateformatMMdd = new SimpleDateFormat("MM_dd_HH_mm_ss_SSS");
			String dateScheduleMMdd = dateformatMMdd.format(new Date());

			// always pair up traceId creation with the traceId type set
			String traceIdJobRunner = ((IntgSrvErrorHandlingFrameworkICD) ehfICD).getNewTraceId();
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_KEY,
					IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_JOBRUNNER);
			traceId = traceIdJobRunner;

			// Additional info attributes need to use the explicit APIs of
			// ErrroHandlingFrameworkICD
			ehfICD.setAttributePublishId(IntgSrvAppConstants.STEP_PUBLISH_ID);
			ehfICD.setAttributeTransactionType(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRANSACTION_TYPE_FILETOFILE);
			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			String msgDesc = "SpringBatch scheduled job runner starts... ";
			String infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
					IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
			ehfLogger.info(infoLogString);
			traceLogger.info(clazzname, "run", msgDesc);

			msgDesc = "SpringBatch conf.properties loaded";
			infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
					IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
			ehfLogger.info(infoLogString);
			traceLogger.info(clazzname, "run", msgDesc);

			String inputDirectoryStepMessages = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(IntgSrvAppConstants.XML_INPUT_DIRECTORY));

			String xsvFilesTargetDirectory = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(IntgSrvAppConstants.XSV_OUTPUT_DIRECTORY));

			String fixLengthFilesTargetDirectory = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(IntgSrvAppConstants.FIX_OUTPUT_DIRECTORY));

			String excelFilesTargetDirectory = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(IntgSrvAppConstants.EXCEL_OUTPUT_DIRECTORY));

			String xsvProductsFileName = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.XSV_PRODUCT_FILE_NAME_OUTPUT);

			String xsvAttributesFileName = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.XSV_ATTRIBUTE_FILE_NAME_OUTPUT);

			// IntgSrvUtils.printConsole("strXSV_OUTPUT_FILE_NAME="+strXSV_OUTPUT_FILE_NAME);
			traceLogger.info(clazzname, "run", "property - input file dir: " + inputDirectoryStepMessages);
			traceLogger.info(clazzname, "run", "property - output file dir - xsv: " + xsvFilesTargetDirectory);
			traceLogger.info(clazzname, "run", "property - output file dir - fix: " + fixLengthFilesTargetDirectory);
			traceLogger.info(clazzname, "run", "property - output file dir - excel: " + excelFilesTargetDirectory);

			createWriterOutputDir(xsvFilesTargetDirectory);
			createWriterOutputDir(fixLengthFilesTargetDirectory);
			createWriterOutputDir(excelFilesTargetDirectory);

			StepTransmitterBean transmitter = new StepTransmitterBean();

			transmitter.setInputDirectory(inputDirectoryStepMessages);
			transmitter.setTargetDirectoryXSV(xsvFilesTargetDirectory);
			transmitter.setTargetDirectoryFixLength(fixLengthFilesTargetDirectory);
			transmitter.setTargetDirectoryExcel(excelFilesTargetDirectory);
			transmitter.setFileNameForProduct(xsvProductsFileName);
			transmitter.setFileNameForAttribute(xsvAttributesFileName);
			transmitter.setDateScheduleMMdd(dateScheduleMMdd);

			attrIDNameHashMap = new HashMap();
			transmitter.addItem(IntgSrvAppConstants.ATTR_ID_NAME_HASH_MAP_TO_HASHMAP, attrIDNameHashMap);
			assetHashMap = new HashMap();
			transmitter.addItem(IntgSrvAppConstants.ASSET_HASH_MAP_TO_HASHMAP, assetHashMap);
			classificationHashMap = new HashMap();
			transmitter.addItem(IntgSrvAppConstants.CLASSIFICATION_HASH_MAP_TO_HASHMAP, classificationHashMap);

			boolean bContainsSKU = false;

			transmitter = this.jobLaunch(transmitter);

			if (listOfStepLocationLevelAttrib.isEmpty()) {
				IntgSrvUtils.printConsole("Location Level list is empty");
			} else {
				SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("yyyyMMdd_HHmmsss");
				String dateSchedule = dateformatyyyyMMdd.format(new Date());
				// before call stored procedure, write audit data to db
				processLocationDataAudit(dateSchedule);
				
				// call stored procedure
				System.out.println("Location Level list size=" + listOfStepLocationLevelAttrib.size());
				HashMap<Integer, List<List>> locationLevelToPushDownCollectionResp = LocationLevelPushDownService.getInstance().invokeStoredProcedure(listOfStepLocationLevelAttrib);
	    		Set<Integer> keySet = locationLevelToPushDownCollectionResp.keySet();
	            Iterator itKey = keySet.iterator();  
	            List listOfResult = new ArrayList();
	            while (itKey.hasNext()) {  
	            	Integer useCaseID = (Integer) itKey.next();
	            	List useCaseResultList = locationLevelToPushDownCollectionResp.get(useCaseID);
	            	Iterator<List<String>> itListResult = useCaseResultList.iterator();
		    		while(itListResult.hasNext())
		    		{
		    			List<String> listResult = itListResult.next();
		    			Iterator<String> itResult = listResult.iterator();
		    			while(itResult.hasNext()){
		    				listOfResult.add(itResult.next());
		    			}
		    		}            	
	            }
				//List listOfResult = (List) LocationLevelPushDownService.getInstance().invokeStoredProcedure(listOfStepLocationLevelAttrib);
				//SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("yyyyMMdd_HHmmsss");
				//String dateSchedule = dateformatyyyyMMdd.format(new Date());
				String locationLevelResultExcelFilePath = excelFilesTargetDirectory + "LocationLevel_"+dateSchedule+".xls";
				System.out.println("listOfResult="+listOfResult);
				// write result to LocationStatusAudit table
				processLocationStatusAudit(listOfResult,dateSchedule);
				
				// write result to excel file
				IntgSrvLocationLevelUtils.writeLocationLevelPushdownResultToExcel(listOfResult, RunSchedulerItemCreateUpdate.SKUID_ItemIDHashMap,locationLevelResultExcelFilePath);
				
				//SFTP excel file to STEP
				sFTPLocationLevelPushdownResult("LocationLevel_"+dateSchedule+".xls");
			}

			this.proceedToTheDestination(transmitter);

		} catch (Throwable e) {
			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			String errorLogString = ehfHandler.getErrorLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4, e,
					IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
			ehfLogger.error(errorLogString);
			traceLogger.error(clazzname, "run", "Exception: " + ehfICD.toStringEHFExceptionStackTrace(e));
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, clazzname, RunSchedulerItemCreateUpdate.publishIds);
		}
		traceLogger.info(clazzname, "run", "EXIT: WAIT FOR NEXT RUN...");
	}

	public StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		try {
			String traceIdJobRunner = ((IntgSrvErrorHandlingFrameworkICD) ehfICD).getNewTraceId();
			traceId = traceIdJobRunner;
			transmitter.addItem(IntgSrvAppConstants.TRACE_ID_JOB_RUNNER_TO_HASHMAP, traceIdJobRunner);

			traceLogger.info(clazzname, "run", "to load context job-ItemCreateUpdate.xml: "
					+ IntgSrvAppConstants.BATCH_JOB_XML_CREATE_UPDATE);

			String path = IntgSrvUtils.getConfigDir();
			// ApplicationContext context = new
			// FileSystemXmlApplicationContext("/configurations" + "/" +
			// IntgSrvAppConstants.BATCH_JOB_XML_CREATE_UPDATE);
			// IntgSrvUtils.printConsole("CommonUtils.reformatFilePath(path IntgSrvAppConstants.BATCH_JOB_XML_CREATE_UPDATE) :: "+CommonUtils.reformatFilePath(path
			// + "/" + IntgSrvAppConstants.BATCH_JOB_XML_CREATE_UPDATE));
			ApplicationContext context = new FileSystemXmlApplicationContext("file:" + path + "/"
					+ IntgSrvAppConstants.BATCH_JOB_XML_CREATE_UPDATE);
			// local testing // ApplicationContext context = new
			// FileSystemXmlApplicationContext(path + "/" +
			// IntgSrvAppConstants.BATCH_JOB_XML_CREATE_UPDATE);

			JobLauncher launcher = (JobLauncher) context.getBean(IntgSrvAppConstants.JOB_LAUNCHER);
			Job job = (Job) context.getBean(IntgSrvAppConstants.ITEM_CREATE_UPDATE_JOB);

			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			String msgDesc = "Integration Services context loaded job-ItemCreateUpdate.xml: "
					+ IntgSrvAppConstants.BATCH_JOB_XML_CREATE_UPDATE;
			String infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
					IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
			ehfLogger.info(infoLogString);
			traceLogger.info(clazzname, "run", msgDesc);

			SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("yyyyMMdd_HHmmsss");
			// String dateSchedule = dateformatyyyyMMdd.format(new Date());
			SimpleDateFormat dateformatMMdd = new SimpleDateFormat("MM_dd_HH_mm_ss_SSS");
			String dateScheduleMMdd = dateformatMMdd.format(new Date());
			transmitter.addItem(IntgSrvAppConstants.DATE_MMdd_TO_HASHMAP, dateScheduleMMdd);

			File dir = new File(transmitter.getInputDirectory());
			File[] files = dir.listFiles();

			if (files == null) {
				IntgSrvUtils.printConsole("file list is null!");
				traceLogger.info(clazzname, "run", "No input file found");
			} else {
				IntgSrvUtils.printConsole("Number of files:" + files.length + " in directory:" + dir.getName());
				int numfiles = files.length;
				traceLogger.info(clazzname, "run", "Number of input files: " + numfiles);

				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile()) {

						currentFile = files[i];

						/*
						 * This Accept flag is used for specify do not process
						 * the Data Migration items, Also the Config
						 * Specification from SB Configuration file
						 */
						boolean acceptFlag = DataMigAcceptItemManager.getInstance().getAcceptFlag(currentFile);

						if (!acceptFlag) {
							traceLogger.info("DM Items found - File Ignored: " + currentFile.getName());
							DataMigAcceptItemManager.getInstance().ignoreAction(currentFile);
							continue;
						}
						
						transmitter.addItem(IntgSrvAppConstants.CURRENT_FILE_TO_HASHMAP, currentFile);
						transmitter = ProductAttributesInProcess.setClassificationsAndAssets(transmitter, true);
						if (null != transmitter.getItem(IntgSrvAppConstants.BOTH_ITEM_AND_SKU_TO_HASHMAP)) {
							bContainsSKU = (Boolean) transmitter.getItem(IntgSrvAppConstants.BOTH_ITEM_AND_SKU_TO_HASHMAP);
						}
						if (null != transmitter.getItem(IntgSrvAppConstants.ASSETS_MAP_TO_HASHMAP)) {
							assetHashMap = (HashMap) transmitter.getItem(IntgSrvAppConstants.ASSETS_MAP_TO_HASHMAP);
						}
						if (null != transmitter.getItem(IntgSrvAppConstants.CLASSIFICATION_MAP_TO_HASHMAP)) {
							classificationHashMap = (HashMap) transmitter.getItem(IntgSrvAppConstants.CLASSIFICATION_MAP_TO_HASHMAP);
						}
						if (null != transmitter.getItem(IntgSrvAppConstants.ATTR_ID_NAME_HASH_MAP_TO_HASHMAP)) {
							attrIDNameHashMap = (HashMap) transmitter.getItem(IntgSrvAppConstants.ATTR_ID_NAME_HASH_MAP_TO_HASHMAP);
						}

						String dateJob = dateformatyyyyMMdd.format(new Date());
						transmitter.addItem(IntgSrvAppConstants.JOB_DATE_yyyyMMddTO_HASHMAP, dateJob);
						String inputFile = files[i].getAbsolutePath();

						String targetProductFileXSVUrl = null;
						String targetAttributeFileXSVUrl = null;
						String targetAttributeFileXSVTempUrl = null;
						String targetFileFixLengthUrl = null;
						String targetFileExcelUrl = null;

						if (transmitter.getTargetDirectoryXSV() != null) {
							targetProductFileXSVUrl = transmitter.getTargetDirectoryXSV() + transmitter.getFileNameForProduct()
									+ dateScheduleMMdd + IntgSrvAppConstants.XSV_FILE_EXTENSION;

							targetAttributeFileXSVUrl = transmitter.getTargetDirectoryXSV() + transmitter.getFileNameForAttribute()
									+ dateScheduleMMdd + IntgSrvAppConstants.XSV_FILE_EXTENSION;

							targetAttributeFileXSVTempUrl = transmitter.getTargetDirectoryXSV() + transmitter.getFileNameForAttribute()
									+ dateScheduleMMdd + IntgSrvAppConstants.XSV_FILE_EXTENSION_TEMP;

							transmitter.addItem(IntgSrvAppConstants.TARGET_PRODUCT_FILE_XSV_URL_TO_HASHMAP, targetProductFileXSVUrl);
							transmitter.addItem(IntgSrvAppConstants.TARGET_ATTRIBUTE_FILE_XSV_URL_TO_HASHMAP, targetAttributeFileXSVUrl);
							transmitter.addItem(IntgSrvAppConstants.TARGET_ATTRIBUTE_FILE_XSV_TEMP_URL_TO_HASHMAP,
									targetAttributeFileXSVTempUrl);
						}

						if (transmitter.getTargetDirectoryFixLength() != null) {
							targetFileFixLengthUrl = transmitter.getTargetDirectoryFixLength() + dateJob + IntgSrvAppConstants.DASH
									+ files[i].getName().replace(".xml", ".txt");
							transmitter.addItem(IntgSrvAppConstants.TARGET_FILE_FIX_LENGTH_URL_TO_HASHMAP, targetFileFixLengthUrl);
						}
						if (transmitter.getTargetDirectoryExcel() != null) {
							targetFileExcelUrl = transmitter.getTargetDirectoryExcel() + dateJob + "_"
									+ files[i].getName().replace(".xml", ".xlsx");
							transmitter.addItem(IntgSrvAppConstants.TARGET_FILE_EXCEL_URL_TO_HASHMAP, targetFileExcelUrl);
						}

						ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_JOBRUNNER, traceIdJobRunner); // remember
																																			// the
																																			// job
																																			// runner
																																			// traceId
																																			// in
																																			// EHF
																																			// attributes
						String traceIdTransform = ((IntgSrvErrorHandlingFrameworkICD) ehfICD).getNewTraceId();

						ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_KEY,
								IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_TRANSFORM);
						traceId = traceIdTransform;
						transmitter.addItem(IntgSrvAppConstants.TRACE_ID_TRANSFORM_TO_HASHMAP, traceIdTransform);

						ehfICD.setAttributePublishId(publishIds);

						String timestampSTEPExport = getSTEPExportTime(inputFile);
						transmitter.addItem(IntgSrvAppConstants.TIME_STAMP_STEP_EXPORT_TO_HASHMAP, timestampSTEPExport);

						ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_STEP_EXPORT_TIME, timestampSTEPExport);
						ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_INPUT_FILENAME, inputFile);

						String logfileBaseUrl = "/opt/stibo/SpringBatch/logs/ehf/product_items/downstream/"; // default

						String rollName = "ProductItems_" + files[i].getName().replace(" ", "_").replace(".xml", "_xml") + "_"
								+ traceIdJobRunner + ".log";
						String log4jFilename = ehfItemLoggerXSV.getLogFilename(IntgSrvAppConstants.EHF_LOGGER_APPENDER_PRODUCTITEMS_XSV);
						if (log4jFilename != null && log4jFilename.contains("tmp"))
							logfileBaseUrl = log4jFilename.substring(0, log4jFilename.indexOf("tmp"));
						resetEHFLogFileForItemLoggers(logfileBaseUrl, rollName);

						/* Entry to the Log files */
						this.processItemlog(transmitter);

						/* job parameters for input and output files */
						Map<String, JobParameter> map = this.setJobParameters(transmitter);

						msgDesc = "SpringBatch starts transformation on file: [" + (i + 1) + "/" + numfiles + ", " + files[i].getName()
								+ "]";
						infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
								ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
								IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
						ehfLogger.info(infoLogString);
						traceLogger.info(clazzname, "run", msgDesc);

						JobExecution result = launcher.run(job, new JobParameters(map));

						msgDesc = "SpringBatch completed transformation on file: [" + (i + 1) + "/" + numfiles + ", " + files[i].getName()
								+ "]";
						infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
								ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
								IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
						ehfLogger.info(infoLogString);
						traceLogger.info(clazzname, "run", msgDesc);

						msgDesc = "SpringBatch exits transformation on the file with ==>> JOB_EXECUTION_EXIT_STATUS={" + result.toString()
								+ "}";
						infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
								ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
								IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
						ehfLogger.info(infoLogString);
						traceLogger.info(clazzname, "run", msgDesc);
					} // if file exists

				} // for files
				/* close open connection */
				if (numfiles > 0) {

					String publishIds = IntgSrvAppConstants.BRACKET_LEFT + IntgSrvAppConstants.STEE103 + IntgSrvAppConstants.COMMA
							+ IntgSrvAppConstants.STEE109 + IntgSrvAppConstants.COMMA + IntgSrvAppConstants.STEE110
							+ IntgSrvAppConstants.BRACKET_WRITE;
					transmitter.setPublishId(publishIds);

					this.setMQProperties(transmitter);
					PublisherToMQ mqPublisher = new PublisherToMQ();
					mqPublisher.closeMQConnection(transmitter);
				}

			} // else

		} catch (Throwable e) {
			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			String errorLogString = ehfHandler.getErrorLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3, e,
					IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
			ehfLogger.error(errorLogString);
			traceLogger.error(clazzname, "run", "Exception: " + ehfICD.toStringEHFExceptionStackTrace(e));
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, clazzname, RunSchedulerItemCreateUpdate.publishIds);
		}
		traceLogger.info(clazzname, "run", "EXIT: WAIT FOR NEXT RUN...");

		return transmitter;
	}

	/* sima VALIDATE from original strange logic _Temp.xsv */

	public void proceedToTheDestination(StepTransmitterBean transmitter) {

		String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
		String codeModule = getClass().getName();

		String fullPathToProductXsvFile = (String) transmitter.getItem(IntgSrvAppConstants.TARGET_PRODUCT_FILE_XSV_URL_TO_HASHMAP);

		if (fullPathToProductXsvFile != null) {

			try {

				File oldXSV = new File(fullPathToProductXsvFile);
				if (oldXSV.exists()) {
					List<String> filesURLToUploadList = ProductAttributesInProcess.addRecordCount(transmitter, fullPathToProductXsvFile);
					oldXSV.delete();
					if (null != filesURLToUploadList) {
						String newPathToProductXsvFile = (String) filesURLToUploadList.get(0);
						String newPathToAttributeXsvFile = (String) filesURLToUploadList.get(1);

						oldXSV.delete();

						/* send Product and Attribute files to the SFTP server */
						FTPConnectionBean connectionsBean = new FTPConnectionBean();

						connectionsBean.setHostName(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.FTP_HOST));
						connectionsBean.setUserId(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.FTP_USER));
						connectionsBean.setPassword(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.FTP_PASSWORD));
						connectionsBean.setDestinationUrl(IntgSrvPropertiesReader
								.getProperty(IntgSrvAppConstants.FTP_DESTINATION_DIRECTORY));
						connectionsBean.setOriginatedURL(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.FTP_ORIGINATED_DIRECTORY));

						SFTPManager sftpManager = new SFTPManager(connectionsBean);
						// sftpManager.uploadFile(newPathToProductXsvFile.replaceFirst(transmitter.getTargetDirectoryXSV(),""));
						// sftpManager.uploadFile(newPathToAttributeXsvFile.replaceFirst(transmitter.getTargetDirectoryXSV(),""));
						sftpManager.setPublishId(publishIds);
						sftpManager.uploadFile(newPathToProductXsvFile);
						sftpManager.uploadFile(newPathToAttributeXsvFile);
					}
				}

				String msgDesc = "SpringBatch scheduled job runner exits. WAIT FOR NEXT RUN...";
				traceId = ehfICD.getAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_JOBRUNNER);
				ehfICD.removeAttribute(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_JOBRUNNER);

				ehfICD.setAttributePublishId(publishIds);
				ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_KEY,
						IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_JOBRUNNER);
				String infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
						ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
						IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
				ehfLogger.info(infoLogString);
				traceLogger.info(clazzname, "run", msgDesc);

			} catch (IOException e) {
				usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
				codeModule = getClass().getName();
				String errorLogString = ehfHandler.getErrorLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
						e, IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
				ehfLogger.error(errorLogString);
				traceLogger.error(clazzname, "run", "Exception: " + ehfICD.toStringEHFExceptionStackTrace(e));
				e.printStackTrace();
				IntgSrvUtils.alertByEmail(e, clazzname, RunSchedulerItemCreateUpdate.publishIds);
			}

		}

	}

	public void processItemlog(StepTransmitterBean transmitter) {

		HashMap<String, String> ehfItemLogHeadingData = new HashMap<String, String>();

		ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_INPUT_FILENAME,
				((File) transmitter.getItem(IntgSrvAppConstants.CURRENT_FILE_TO_HASHMAP)).getAbsolutePath());
		ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_STEP_EXPORT_TIME,
				(String) transmitter.getItem(IntgSrvAppConstants.TIME_STAMP_STEP_EXPORT_TO_HASHMAP));
		ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_JOBRUNNER,
				(String) transmitter.getItem(IntgSrvAppConstants.TRACE_ID_JOB_RUNNER_TO_HASHMAP));
		ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TRANSFORM,
				(String) transmitter.getItem(IntgSrvAppConstants.TRACE_ID_TRANSFORM_TO_HASHMAP));

		ehfItemLogHeadingData.put(ErrorHandlingFrameworkICD.EHF_ATTR_PUBLISH_ID, IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_XSV);
		ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_OUTPUT_FILENAME,
				(String) transmitter.getItem(IntgSrvAppConstants.TARGET_PRODUCT_FILE_XSV_URL_TO_HASHMAP));
		writeEHFItemLogHeading(ehfItemLoggerXSV, ehfItemLogHeadingData);

		ehfItemLogHeadingData.put(ErrorHandlingFrameworkICD.EHF_ATTR_PUBLISH_ID, IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_FIXLENGTH);
		ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_OUTPUT_FILENAME,
				(String) transmitter.getItem(IntgSrvAppConstants.TARGET_FILE_FIX_LENGTH_URL_TO_HASHMAP));
		writeEHFItemLogHeading(ehfItemLoggerFixLength, ehfItemLogHeadingData);

		ehfItemLogHeadingData.put(ErrorHandlingFrameworkICD.EHF_ATTR_PUBLISH_ID, IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_EXCEL);
		ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_OUTPUT_FILENAME,
				(String) transmitter.getItem(IntgSrvAppConstants.TARGET_FILE_EXCEL_URL_TO_HASHMAP));
		writeEHFItemLogHeading(ehfItemLoggerExcel, ehfItemLogHeadingData);

	}

	public Map<String, JobParameter> setJobParameters(StepTransmitterBean transmitter) {

		IntgSrvUtils.printConsole("dateJob=" + transmitter.getItem(IntgSrvAppConstants.JOB_DATE_yyyyMMddTO_HASHMAP));
		IntgSrvUtils.printConsole(((File) transmitter.getItem(IntgSrvAppConstants.CURRENT_FILE_TO_HASHMAP)).getPath() + "; start processing...");

		Map<String, JobParameter> map = new HashMap<String, JobParameter>();

		JobParameter jpInputFile = null;
		String inputFile = ((File) transmitter.getItem(IntgSrvAppConstants.CURRENT_FILE_TO_HASHMAP)).getAbsolutePath();
		if (null != inputFile) {
			jpInputFile = new JobParameter(inputFile);
			map.put(IntgSrvAppConstants.JP_INPUT_FILE, jpInputFile);
		}
		JobParameter jpOutputFileXSV = null;
		if (null != transmitter.getItem(IntgSrvAppConstants.TARGET_PRODUCT_FILE_XSV_URL_TO_HASHMAP)) {
			jpOutputFileXSV = new JobParameter((String) transmitter.getItem(IntgSrvAppConstants.TARGET_PRODUCT_FILE_XSV_URL_TO_HASHMAP));
			map.put(IntgSrvAppConstants.JP_OUTPUT_FILE_XSV, jpOutputFileXSV);
		}
		JobParameter jpOutputFileFixLength = null;
		if (null != transmitter.getItem(IntgSrvAppConstants.TARGET_FILE_FIX_LENGTH_URL_TO_HASHMAP)) {
			jpOutputFileFixLength = new JobParameter(
					(String) transmitter.getItem(IntgSrvAppConstants.TARGET_FILE_FIX_LENGTH_URL_TO_HASHMAP));
			map.put(IntgSrvAppConstants.JP_OUTPUT_FILE_FIXLENGTH, jpOutputFileFixLength);
		}
		JobParameter jpOutputFileExcel = null;
		if (null != transmitter.getItem(IntgSrvAppConstants.TARGET_FILE_EXCEL_URL_TO_HASHMAP)) {
			jpOutputFileExcel = new JobParameter((String) transmitter.getItem(IntgSrvAppConstants.TARGET_FILE_EXCEL_URL_TO_HASHMAP));
			map.put(IntgSrvAppConstants.JP_OUTPUT_FILE_EXCEL, jpOutputFileExcel);
		}
		JobParameter datePARAM = null;
		if (null != transmitter.getItem(IntgSrvAppConstants.JOB_DATE_yyyyMMddTO_HASHMAP)) {
			datePARAM = new JobParameter((String) transmitter.getItem(IntgSrvAppConstants.JOB_DATE_yyyyMMddTO_HASHMAP));
			map.put(IntgSrvAppConstants.JP_DATE, datePARAM);
		}
		JobParameter jpEHFSTEPExportTime = null;
		if (null != transmitter.getItem(IntgSrvAppConstants.TIME_STAMP_STEP_EXPORT_TO_HASHMAP)) {
			jpEHFSTEPExportTime = new JobParameter((String) transmitter.getItem(IntgSrvAppConstants.TIME_STAMP_STEP_EXPORT_TO_HASHMAP));
			map.put(IntgSrvAppConstants.JP_SETP_EXPORT_TIME, jpEHFSTEPExportTime);
		}

		JobParameter jpEHFPublishId = new JobParameter(IntgSrvAppConstants.STEP_PUBLISH_ID);
		JobParameter jpEHFTransactionType = new JobParameter(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRANSACTION_TYPE_FILETOFILE);
		JobParameter jpEHFTraceIdJobRunner = new JobParameter(
				(String) transmitter.getItem(IntgSrvAppConstants.TRACE_ID_JOB_RUNNER_TO_HASHMAP));
		JobParameter jpEHFTraceIdTransform = new JobParameter(
				(String) transmitter.getItem(IntgSrvAppConstants.TRACE_ID_TRANSFORM_TO_HASHMAP));

		map.put(IntgSrvAppConstants.JP_PUBLISH_ID, jpEHFPublishId);
		map.put(IntgSrvAppConstants.JP_TRANSACTION_TYPE_FILETOFILE, jpEHFTransactionType);
		map.put(IntgSrvAppConstants.JP_EHF_TRACEID_JOBRUNNER, jpEHFTraceIdJobRunner);
		map.put(IntgSrvAppConstants.JP_EHF_TRACEID_TRANSFORM, jpEHFTraceIdTransform);

		return map;
	}

	private void setMQProperties(StepTransmitterBean transmitter) {

		String mqHostName = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MQ_HOSTNAME);
		transmitter.setMqHostName(mqHostName);

		int mqPort = Integer.parseInt(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MQ_PORT));
		transmitter.setMqPport(mqPort);

		String mqQueueManager = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MQ_QUEUE_MANAGER);
		transmitter.setMqQueueManager(mqQueueManager);

		String mqChannel = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MQ_CHANNEL);
		transmitter.setMqChannel(mqChannel);

		String mqQueueName = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MQ_QUEUE_NAME);
		transmitter.setMqQueueName(mqQueueName);

		int mqTimeout = Integer.parseInt(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MQ_TIMEOUT));
		transmitter.setMqTimeout(mqTimeout);
	}
	public void sFTPLocationLevelPushdownResult(String fileName){
		/* send Location Level Pushdown Result excel file to the STEP server */
		String publishIds = IntgSrvAppConstants.BRACKET_LEFT + 
		IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_XSV 				+ 
		IntgSrvAppConstants.COMMA 									+ 
		IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_FIXLENGTH 		+ 
		IntgSrvAppConstants.COMMA 									+ 
		IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_EXCEL 			+ 
		IntgSrvAppConstants.BRACKET_WRITE;
		
		String localDir = IntgSrvPropertiesReader.getProperty(
				IntgSrvAppConstants.FTP_LOCALDIRECTORY_LOCATIONLEVELPUSHDOWNRESULT);

		FTPConnectionBean  connectionsBean = new FTPConnectionBean();

		connectionsBean.setHostName(IntgSrvPropertiesReader.getProperty(
							IntgSrvAppConstants.FTP_HOST_WORDFILTER));
		connectionsBean.setUserId(IntgSrvPropertiesReader.getProperty(
						IntgSrvAppConstants.FTP_USER_WORDFILTER));
		connectionsBean.setPassword(IntgSrvPropertiesReader.getProperty(
						IntgSrvAppConstants.FTP_PASSWORD_WORDFILTER));
		connectionsBean.setDestinationUrl(IntgSrvPropertiesReader.getProperty(
						IntgSrvAppConstants.FTP_TARGETDIRECTORY_LOCATIONLEVELPUSHDOWNRESULT));
		connectionsBean.setOriginatedURL(localDir);
		System.out.println("LocationLevelResult file="+localDir);
	    SFTPManager sftpManager = new SFTPManager(connectionsBean); 			    
	    sftpManager.setPublishId(publishIds);
	    sftpManager.uploadFile(localDir + fileName);
		
	}
	public void processLocationDataAudit(String dateSchedule){
 		Connection con = null;
 		Statement stmt = null;
 		try {
			con = ds.getConnection();
			//stmt = con.createStatement();
			PreparedStatement preparedStatement = null;
			PreparedStatement preparedStatement2 = null;
			java.sql.Timestamp timestamp = getCurrentTimeStamp();


			String insertTableSQL = "INSERT INTO SB_USER.LOCATION_DATA_AUDIT"
				+ "(Correlation_ID, SKU_ID, STEP_Item_ID, Chanel, Location_Attribute_FAN, Attribute_Name, Attribute_Value, Location_Type, Location_ID, CREATE_TIMESTAMP) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?)";
			String insertTableSQL2 = "INSERT INTO SB_USER.LOCATION_STATUS_AUDIT"
				+ "(Correlation_ID, SKU_ID, Status, Error_ID, Error_Message, CREATE_TIMESTAMP) VALUES"
				+ "(?,?,?,?,?,?)";
			HashMap<String,String> hmSKUStatus = new HashMap();// hold data for LocationStatusAudit table
			Iterator<LocationDataAudit> it = listOfLocationDataAudit.iterator();
			while(it.hasNext()){
				LocationDataAudit locationDataAudit = it.next();
				preparedStatement = con.prepareStatement(insertTableSQL);
				preparedStatement.setString(1, dateSchedule + "_" + locationDataAudit.getSKU_ID());
				preparedStatement.setInt(2, locationDataAudit.getSKU_ID());
				preparedStatement.setString(3, locationDataAudit.getSTEP_Item_ID());
				preparedStatement.setString(4, locationDataAudit.getChannel());
				preparedStatement.setString(5, locationDataAudit.getLocation_Attribute_FAN());
				preparedStatement.setString(6, locationDataAudit.getAttribute_Name());
				preparedStatement.setString(7, locationDataAudit.getAttribute_Value());
				preparedStatement.setString(8, locationDataAudit.getLocation_Type());
				preparedStatement.setString(9, locationDataAudit.getLocation_ID());
				preparedStatement.setTimestamp(10, timestamp);
	
	
				
				int updatedRecordNum = -1;
				updatedRecordNum = preparedStatement.executeUpdate();
				hmSKUStatus.put(""+locationDataAudit.getSKU_ID(), "Request");
				System.out.println("insert to table updatedRecordNum="+updatedRecordNum);
				traceLogger.info(clazzname, "processLocationDataAudit", "insert to table updatedRecordNum="+updatedRecordNum);
			}
    		Set<String> keySet = hmSKUStatus.keySet();
            Iterator itKey = keySet.iterator();  
            while (itKey.hasNext()) {  
            	String sSKUID = (String) itKey.next();
            	String sStatus = (String) hmSKUStatus.get(sSKUID);
				preparedStatement2 = con.prepareStatement(insertTableSQL2);
				preparedStatement2.setString(1, dateSchedule + "_" + sSKUID);
				preparedStatement2.setInt(2, Integer.parseInt(sSKUID));
				preparedStatement2.setString(3, sStatus);
				preparedStatement2.setString(4, "");
				preparedStatement2.setString(5, "");
				preparedStatement2.setTimestamp(6, timestamp);
				preparedStatement2.executeUpdate();
            }
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("SQLException e="+e);
			traceLogger.info(clazzname, "processLocationDataAudit", e);
		 
		} finally {
		    if (stmt != null) {
		    	try {
				stmt.close();
				con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	public void processLocationStatusAudit(List listOfStrings,String dateSchedule){
 		Connection con = null;
 		Statement stmt = null;
 		try {
			con = ds.getConnection();
			//stmt = con.createStatement();
			//PreparedStatement preparedStatement = null;
			PreparedStatement preparedStatement2 = null;
			java.sql.Timestamp timestamp = getCurrentTimeStamp();


			String insertTableSQL2 = "INSERT INTO SB_USER.LOCATION_STATUS_AUDIT"
				+ "(Correlation_ID, SKU_ID, Status, Error_ID, Error_Message, CREATE_TIMESTAMP) VALUES"
				+ "(?,?,?,?,?,?)";

			
	        //start to process list of String
			Iterator itKey1 = listOfStrings.iterator();  
			
			//iterate through list
			while (itKey1.hasNext()) {  
				String result = (String) itKey1.next();
				result = result.trim();
				System.out.println("Location Level pushdown result="+result);
				if (result.startsWith("<<")){// remove first "<<"
					result = result.substring(2);
				}
				else if (result.contains("<<") && result.endsWith(">>")){
					result = result.substring(result.indexOf("<<")+2, result.length());
					System.out.println("Location Level pushdown has extra info="+result);
				}

				if (result.endsWith(">>")){// remove last ">>"
					result = result.substring(0, result.length()-2);
				}
				result = result.replace(">><<", "^");
			    StringTokenizer st = new StringTokenizer(result,"^");//>><<
				while (st.hasMoreElements()) {
					String SKUResult = (String) st.nextElement();
					SKUResult = SKUResult.trim();
					int col = 0;
					String sSKUID = "";
					String sStatus = "";
					String sError_ID = "";
					String sError_Message = "";
					StringTokenizer st2 = new StringTokenizer(SKUResult,"!!");
					while (st2.hasMoreElements()) {
						String attributeResult = (String) st2.nextElement();
						System.out.println(attributeResult);
						attributeResult = attributeResult.replace("~~", "~ ~");
						StringTokenizer st3 = new StringTokenizer(attributeResult,"~");
						sSKUID = "";
						sStatus = "";
						sError_ID = "";
						sError_Message = "";
						col = 0;
						while (st3.hasMoreElements()) {
							String colData = (String) st3.nextElement();
							if (colData.equals(" ")) colData = "";
							if (col ==0){
								sSKUID = colData;
							}
							else if (col ==1){
								sStatus = colData;
							}
							else if (col ==2){
								sError_ID = colData;
							}
							else if (col ==3){
								sError_Message = colData;
							}
							col++;
						}
						preparedStatement2 = con.prepareStatement(insertTableSQL2);
						preparedStatement2.setString(1, dateSchedule + "_" + sSKUID);
						preparedStatement2.setInt(2, Integer.parseInt(sSKUID));
						preparedStatement2.setString(3, sStatus);
						preparedStatement2.setString(4, sError_ID);
						preparedStatement2.setString(5, sError_Message);
						preparedStatement2.setTimestamp(6, timestamp);
						preparedStatement2.executeUpdate();
						
					}
					
				}

			}
			

			con.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception e="+e);
			traceLogger.info(clazzname, "processLocationDataAudit", e);
		 
		} finally {
		    if (stmt != null) {
		    	try {
				stmt.close();
				con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	private static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}
}
