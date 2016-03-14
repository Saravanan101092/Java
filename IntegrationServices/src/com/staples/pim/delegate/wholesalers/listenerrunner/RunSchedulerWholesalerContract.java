
package com.staples.pim.delegate.wholesalers.listenerrunner;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkICD;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtilConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;

@Component
public class RunSchedulerWholesalerContract extends RunScheduler {

	/**
	 * Logger Configurations
	 */
	public static IntgSrvLogger		itemTraceLogger									= IntgSrvLogger
																							.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_WHOLESALER_CONTRACT);
	public HashMap<String, String>	ehfItemLogHeadingData							= new HashMap<String, String>();
	/*
	 * REQUIRED DIRECTORIES
	 */
	public static String			WHOLESALER_CONTRACT_FEED_INPUT_DIR				= "WHOLESALER_CONTRACT_FEED_INPUT_DIR";
	public static String			WHOLESALER_CONTRACT_FEED_DONE_DIR				= "WHOLESALER_CONTRACT_FEED_DONE_DIR";
	public static String			WHOLESALER_CONTRACT_FEED_BAD_DIR				= "WHOLESALER_CONTRACT_FEED_BAD_DIR";
	public static String			WHOLESALER_CONTRACT_FEED_REPORT_DIR				= "WHOLESALER_CONTRACT_FEED_REPORT_DIR";
	public static String			WHOLESALER_CONTRACT_FEED_STEP_XML_OUTPUT_DIR	= "WHOLESALER_CONTRACT_FEED_STEP_XML_OUTPUT_DIR";

	public static String			WHOLESALER_CONTRACT_FEED_REPORT_FILE			= "WHOLESALER_CONTRACT_FEED_REPORT_FILE";

	public static String			PUBLISH_ID										= "SCTE001";
	/*
	 * JOB NAMES
	 */
	public static String			WHOLESALER_CONTRACT_JOB							= "WholesalerContractJob";

	/*
	 * Feed File information
	 */
	public static String			FEED_FILE_EXTN									= IntgSrvPropertiesReader
																							.getProperty("WHOLESALER_CONTRACT_FEED_FILE_EXTN");
	public static String			FEED_FILE_BEGIN									= IntgSrvPropertiesReader
																							.getProperty("WHOLESALER_CONTRACT_FEED_FILE_BEGIN");
	public static String			FEED_FILE_NAME_DATE_FORMAT						= IntgSrvPropertiesReader
																							.getProperty("WHOLESALER_CONTRACT_FEED_FILE_NAME_DATE_FORMAT");
	public static String			infoLogString;

	/**
	 * run Method
	 */
	public void run() {

		traceLogger.info(clazzname, "run", "ENTER: RunSchedulerWholesalerContract runner");

		try {

			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
			traceId = ((IntgSrvErrorHandlingFrameworkICD) ehfICD).getNewTraceId();
			ehfItemLogHeadingData.put(ErrorHandlingFrameworkICD.EHF_ATTR_PUBLISH_ID, PUBLISH_ID);
			ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_JOBRUNNER, traceId);
			ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_TRANSFORM, traceId);

			infoLogString = "RunSchedulerWholesalerContract job runner starts... ";
			traceLogger.info(clazzname, "run", infoLogString);

			String wholesalerContractFeedInputDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(WHOLESALER_CONTRACT_FEED_INPUT_DIR));
			String wholesalerContractFeedDoneDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(WHOLESALER_CONTRACT_FEED_DONE_DIR));
			String wholesalerContractFeedBadDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(WHOLESALER_CONTRACT_FEED_BAD_DIR));
			String wholesalerContractFeedReportDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(WHOLESALER_CONTRACT_FEED_REPORT_DIR));
			String wholesalerContractFeedOutputDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(WHOLESALER_CONTRACT_FEED_STEP_XML_OUTPUT_DIR));

			IntgSrvUtils.printConsole("WHOLESALER_CONTRACT_FEED_INPUT_DIR = " + wholesalerContractFeedInputDir);
			IntgSrvUtils.printConsole("WHOLESALER_CONTRACT_FEED_DONE_DIR = " + wholesalerContractFeedDoneDir);
			IntgSrvUtils.printConsole("WHOLESALER_CONTRACT_FEED_BAD_DIR = " + wholesalerContractFeedBadDir);
			IntgSrvUtils.printConsole("WHOLESALER_CONTRACT_FEED_REPORT_DIR = " + wholesalerContractFeedReportDir);
			IntgSrvUtils.printConsole("WHOLESALER_CONTRACT_FEED_STEP_XML_OUTPUT_DIR = " + wholesalerContractFeedOutputDir);

			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wholesalerContractFeedInputDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wholesalerContractFeedDoneDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wholesalerContractFeedBadDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wholesalerContractFeedReportDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wholesalerContractFeedOutputDir));

			String jobContext = IntgSrvUtils.reformatFilePath(IntgSrvUtilConstants.CONFIG_DIR_LOCATION_DEFAULT + "/"
					+ IntgSrvUtilConstants.WHOLESALER_CONTRACT_CONTEXT_FILE);

			infoLogString = "To load context context-WholesalerContract.xml: " + jobContext;
			traceLogger.info(clazzname, "MY_RUN", infoLogString);
			ehfLogger.info(infoLogString);

			ApplicationContext context = new FileSystemXmlApplicationContext("file:" + jobContext);

			JobLauncher launcher = (JobLauncher) context.getBean(IntgSrvAppConstants.JOB_LAUNCHER);
			Job job = (Job) context.getBean(WHOLESALER_CONTRACT_JOB);

			infoLogString = "RunSchedulerWholesalerContract context loaded :" + jobContext;
			traceLogger.info(clazzname, "MY_RUN", infoLogString);
			ehfLogger.info(infoLogString);

			File dir = new File(wholesalerContractFeedInputDir);
			File[] files = dir.listFiles();
			List<File> fileList = getWholesalerContractFeeds(Arrays.asList(files));

			if (fileList == null || fileList.size() == 0) {
				IntgSrvUtils.printConsole("Wholesaler Contract Feeds not found");
				traceLogger.info(clazzname, "run", "Wholesaler Contract Feeds not found");
			} else {

				/*
				 * Sort based on time stamp from the file name
				 */
				List<File> sortedFileList = sortBasedOnFileNameTimestamp(fileList);

				IntgSrvUtils.printConsole("Number of files:" + sortedFileList.size() + " in directory:" + sortedFileList);

				for (File file : sortedFileList) {
					if (file.isFile()) {

						String epochTime = String.valueOf(System.currentTimeMillis());
						String inputFile = file.getAbsolutePath();

						String outputFile = wholesalerContractFeedOutputDir + epochTime + "_" + file.getName().replace(FEED_FILE_EXTN, "");
						String outputReportFile = wholesalerContractFeedReportDir + epochTime + "_" + file.getName();

						ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_INPUT_FILENAME, inputFile);
						ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_OUTPUT_FILENAME, outputFile);
						ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_STEP_EXPORT_TIME, epochTime);
						writeEHFItemLogHeading(itemTraceLogger, ehfItemLogHeadingData);

						inputFile = IntgSrvUtils.reformatFilePath(inputFile);
						outputFile = IntgSrvUtils.reformatFilePath(outputFile);
						outputReportFile = IntgSrvUtils.reformatFilePath(outputReportFile);

						IntgSrvUtils.printConsole("inputFile=" + inputFile);
						IntgSrvUtils.printConsole("OutputFile: " + outputFile);

						IntgSrvUtils.printConsole(inputFile + "; start processing...");

						JobParameter jpInputFile = new JobParameter(inputFile);
						JobParameter jpOutputFile = new JobParameter(outputFile);
						JobParameter jpReportFile = new JobParameter(outputReportFile);

						JobParameter jplocationFeedInputDir = new JobParameter(wholesalerContractFeedInputDir);
						JobParameter jplocationFeedDoneDir = new JobParameter(wholesalerContractFeedDoneDir);
						JobParameter jplocationFeedBadDir = new JobParameter(wholesalerContractFeedBadDir);
						JobParameter jplocationFeedOutputDir = new JobParameter(wholesalerContractFeedOutputDir);

						Map<String, JobParameter> map = new HashMap<String, JobParameter>();
						map.put(IntgSrvAppConstants.JP_INPUT_FILE, jpInputFile);
						map.put(IntgSrvAppConstants.JP_OUTPUT_FILE_STEP_XML, jpOutputFile);
						map.put(WHOLESALER_CONTRACT_FEED_REPORT_FILE, jpReportFile);

						map.put(WHOLESALER_CONTRACT_FEED_INPUT_DIR, jplocationFeedInputDir);
						map.put(WHOLESALER_CONTRACT_FEED_DONE_DIR, jplocationFeedDoneDir);
						map.put(WHOLESALER_CONTRACT_FEED_BAD_DIR, jplocationFeedBadDir);
						map.put(WHOLESALER_CONTRACT_FEED_STEP_XML_OUTPUT_DIR, jplocationFeedOutputDir);

						infoLogString = "RunSchedulerWholesalerContract starts transformation on file: " + inputFile;
						traceLogger.info(clazzname, "run", infoLogString);
						ehfLogger.info(infoLogString);

						JobExecution result = launcher.run(job, new JobParameters(map));

						infoLogString = "RunSchedulerWholesalerContract completed transformation on file: " + inputFile;
						traceLogger.info(clazzname, "run", infoLogString);
						ehfLogger.info(infoLogString);

						infoLogString = "RunSchedulerWholesalerContract exits transformation on the file " + inputFile
								+ " and JOB_EXECUTION_EXIT_STATUS={" + result.toString() + "}";
						traceLogger.info(clazzname, "run", infoLogString);
						ehfLogger.info(infoLogString);

					} // if file exists
				} // for files
			}
		} catch (Throwable exception) {
			traceLogger.error(clazzname, "MY_RUN", "Exception: " + exception);
			ehfLogger.error(infoLogString);
			itemTraceLogger.error(infoLogString);
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerWholesalerContract.PUBLISH_ID);
		}
		traceLogger.info(clazzname, "run", "EXIT: WAIT FOR NEXT RUN...");
	}

	private List<File> getWholesalerContractFeeds(List<File> sourceList) {

		List<File> contractFileList = new ArrayList<File>();

		for (File file : sourceList) {
			String fileName = file.getName();
			if (file.isFile() && fileName.startsWith(FEED_FILE_BEGIN) && fileName.endsWith(FEED_FILE_EXTN)) {
				contractFileList.add(file);
			}
		}
		return contractFileList;
	}

	private List<File> sortBasedOnFileNameTimestamp(List<File> fileList) {

		ComparatorBasedOnFileNameTimestamp comparatorObj = new ComparatorBasedOnFileNameTimestamp();
		Collections.sort(fileList, comparatorObj);

		return fileList;
	}

	/**
	 * Creating required directories if not exist
	 */
	protected void createWriterOutputDir(String directoryName) {

		File directory = new File(directoryName);
		if (!directory.exists()) {
			if (directory.mkdirs())
				IntgSrvUtils.printConsole("RunSchedulerWholesalerContract.createWriterOutputDirs | Dir CREATED:" + directoryName);
		}
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}

	/**
	 * 
	 * Comparator to sort the files from Incoming directory - Don't ignore any
	 * files
	 */

	private class ComparatorBasedOnFileNameTimestamp implements Comparator<File> {

		@Override
		public int compare(File file1, File file2) {

			String file1TimestampString = file1.getName().replace(FEED_FILE_EXTN, DatamigrationAppConstants.EMPTY_STR);
			file1TimestampString = file1TimestampString.substring(file1TimestampString.indexOf("_") + 1, file1TimestampString.length());

			String file2TimestampString = file2.getName().replace(FEED_FILE_EXTN, DatamigrationAppConstants.EMPTY_STR);
			file2TimestampString = file2TimestampString.substring(file2TimestampString.indexOf("_") + 1, file2TimestampString.length());

			SimpleDateFormat sdf = new SimpleDateFormat(FEED_FILE_NAME_DATE_FORMAT);

			Date file1Date = null;
			Date file2Date = null;
			try {
				file1Date = sdf.parse(file1TimestampString);
				file2Date = sdf.parse(file2TimestampString);
			} catch (ParseException e) {
				infoLogString = "Date parsing issue while sorting the file based on file name " + e.getMessage();
				traceLogger.error(clazzname, "MY_RUN", "Exception: " + e);
				ehfLogger.error(infoLogString);
				itemTraceLogger.error(infoLogString);
				IntgSrvUtils.alertByEmail(e, clazzname, RunSchedulerWholesalerContract.PUBLISH_ID);
			}

			long file1epoch = file1Date.getTime();
			long file2epoch = file2Date.getTime();

			if (file1epoch < file2epoch) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}