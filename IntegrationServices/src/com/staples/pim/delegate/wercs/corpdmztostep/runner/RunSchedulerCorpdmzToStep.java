
package com.staples.pim.delegate.wercs.corpdmztostep.runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import oracle.jdbc.pool.OracleDataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

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
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;
import com.staples.pim.delegate.wercs.corpdmztostep.processor.CorpdmzToStepMSDSProcessor;

public class RunSchedulerCorpdmzToStep extends RunScheduler {

	public static IntgSrvLogger			logger								= IntgSrvLogger
																					.getInstance(WercsAppConstants.FREEFORM_TRACE_LOGGER_WERCS_CORPDMZTOSTEP);
	public HashMap<String, String>		ehfItemLogHeadingData				= new HashMap<String, String>();
	/*
	 * REQUIRED DIRECTORIES
	 */
	public static String				WERCSREGULATORYDATA_INPUT_FOLDER	= "WERCSREGULATORYDATA_INPUT_FOLDER";
	public static String				WERCSREGULATORYDATA_DONE_FOLDER		= "WERCSREGULATORYDATA_DONE_FOLDER";
	public static String				WERCSREGULATORYDATA_BAD_FOLDER		= "WERCSREGULATORYDATA_BAD_FOLDER";
	public static String				WERCSREGULATORYDATA_REPORT_FOLDER	= "WERCSREGULATORYDATA_REPORT_FOLDER";
	public static String				WERCSREGULATORYDATA_OUTPUT_FOLDER	= "WERCSREGULATORYDATA_OUTPUT_FOLDER";
	public static String				MSDS_BACKUP_FOLDER					= "MSDS_BACKUP_FOLDER";

	public static String				CORPDMZTOSTEP_FEED_REPORT_FILE		= "CORPDMZTOSTEP_FEED_REPORT_FILE";

	public static String				PUBLISH_ID							= "WRSE001";
	/*
	 * JOB NAMES
	 */
	public static String				WERCS_REGULATORYDATA_JOB			= "WercsRegulatoryDataJob";

	/*
	 * Feed File information
	 */
	public static String				REGULATORYDATAFEED_FILE_EXTN		= IntgSrvPropertiesReader
																					.getProperty("WERCS_REGULATORYDATA_FEED_FILE_EXTN");

	public static String				CORPDMZTOSTEPFEED_FILE_EXTN			= IntgSrvPropertiesReader
																					.getProperty("WERCS_CORPDMZTOSTEP_FEED_FILE_EXTN");
	public static String				MSDSDOCFEED_FILE_EXTN				= IntgSrvPropertiesReader
																					.getProperty("WERCS_MSDSDOC_FEED_FILE_EXTN");
	public static String				REGULATORYDATAFEED_FILE_BEGIN		= IntgSrvPropertiesReader
																					.getProperty("WERCS_REGULATORYDATA_FEED_FILE_BEGIN");
	public static String				MSDSFEED_FILE_BEGIN					= IntgSrvPropertiesReader
																					.getProperty("WERCS_MSDS_FEED_FILE_BEGIN");
	public static String				FEED_FILE_NAME_DATE_FORMAT			= IntgSrvPropertiesReader
																					.getProperty("WERCS_REGULATORYDATA_FEED_FILE_NAME_DATE_FORMAT");
	public static String				infoLogString;

	public static ApplicationContext	context;

	public static String				wercsmsdsdocbackupDir				= IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
																					.getProperty(MSDS_BACKUP_FOLDER));

	public static String				wercsregulatorydataFeedDoneDir		= IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
																					.getProperty(WERCSREGULATORYDATA_DONE_FOLDER));

	public static OracleDataSource datasource;
	
	public OracleDataSource getDatasource() {

		return datasource;
	}

	public void setDatasource(OracleDataSource datasource) {

		this.datasource = datasource;
	}
	
	@Override
	public void run() {

		traceLogger.info(clazzname, "run", "ENTER: RunSchedulerCorpdmzToStep runner");

		try {

			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
			traceId = ((IntgSrvErrorHandlingFrameworkICD) ehfICD).getNewTraceId();
			ehfItemLogHeadingData.put(ErrorHandlingFrameworkICD.EHF_ATTR_PUBLISH_ID, PUBLISH_ID);
			ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_JOBRUNNER, traceId);
			ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_TRANSFORM, traceId);

			infoLogString = "RunSchedulerCorpdmzToStep job runner starts... ";
			traceLogger.info(clazzname, "run", infoLogString);

			String wercsregulatorydataFeedInputDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(WERCSREGULATORYDATA_INPUT_FOLDER));

			String wercsregulatorydataFeedBadDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(WERCSREGULATORYDATA_BAD_FOLDER));
			String wercsregulatorydataFeedReportDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(WERCSREGULATORYDATA_REPORT_FOLDER));
			String wercsregulatorydataFeedOutputDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(WERCSREGULATORYDATA_OUTPUT_FOLDER));

			IntgSrvUtils.printConsole("WERCS_REGULATORYDATA_FEED_INPUT_DIR = " + wercsregulatorydataFeedInputDir);
			IntgSrvUtils.printConsole("WERCS_REGULATORYDATA_FEED_DONE_DIR = " + wercsregulatorydataFeedDoneDir);
			IntgSrvUtils.printConsole("WERCS_REGULATORYDATA_FEED_BAD_DIR = " + wercsregulatorydataFeedBadDir);
			IntgSrvUtils.printConsole("WERCS_REGULATORYDATA_FEED_REPORT_DIR = " + wercsregulatorydataFeedReportDir);
			IntgSrvUtils.printConsole("WERCS_REGULATORYDATA_FEED_STEP_XML_OUTPUT_DIR = " + wercsregulatorydataFeedOutputDir);

			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsregulatorydataFeedInputDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsregulatorydataFeedDoneDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsregulatorydataFeedBadDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsregulatorydataFeedReportDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsregulatorydataFeedOutputDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsmsdsdocbackupDir));

			String jobContext = IntgSrvUtils.getConfigDir() + "/" + IntgSrvUtilConstants.WERCS_REGULATORYDATA_CONTEXT_FILE;

			infoLogString = "To load context context-wercscorpdmztostep.xml: " + jobContext;
			traceLogger.info(clazzname, "MY_RUN", infoLogString);
			ehfLogger.info(infoLogString);

			DatamigrationCommonUtil.printConsole("Entered into Job :" + jobContext);
			context = new FileSystemXmlApplicationContext("file:" + jobContext);

			JobLauncher launcher = (JobLauncher) context.getBean(IntgSrvAppConstants.JOB_LAUNCHER);
			Job job = (Job) context.getBean(WERCS_REGULATORYDATA_JOB);

			infoLogString = "RunSchedulerCorpdmzToStep context loaded :" + jobContext;
			traceLogger.info(clazzname, "MY_RUN", infoLogString);
			ehfLogger.info(infoLogString);

			File Backupdir = new File(wercsmsdsdocbackupDir);
			File[] files1 = Backupdir.listFiles();
			for (int i = 0; i < files1.length; i++) {
				WercsRegulatoryDataFeedJobListner msdsdocumentProcessor = new WercsRegulatoryDataFeedJobListner();
				msdsdocumentProcessor.moveMSDSToDestnationDir(files1[i], wercsregulatorydataFeedInputDir);
			}
			File dir = new File(wercsregulatorydataFeedInputDir);
			File[] files = dir.listFiles();
			List<File> fileList = getWercsCorpdmztoSTEPFeeds(Arrays.asList(files));

			if (fileList == null || fileList.size() == 0) {
				IntgSrvUtils.printConsole("WERCS CoprdmztoSTEP Feeds not found");
				traceLogger.info(clazzname, "run", "WERCS CoprdmztoSTEP Feeds not found");
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

						String outputFile = wercsregulatorydataFeedOutputDir + epochTime + "_"
								+ file.getName().replace(REGULATORYDATAFEED_FILE_EXTN, "");
						String outputReportFile = wercsregulatorydataFeedReportDir + epochTime + "_" + file.getName();

						ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_INPUT_FILENAME, inputFile);
						ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_OUTPUT_FILENAME, outputFile);
						ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_STEP_EXPORT_TIME, epochTime);
						writeEHFItemLogHeading(logger, ehfItemLogHeadingData);

						inputFile = IntgSrvUtils.reformatFilePath(inputFile);
						outputFile = IntgSrvUtils.reformatFilePath(outputFile);
						outputReportFile = IntgSrvUtils.reformatFilePath(outputReportFile);

						IntgSrvUtils.printConsole("inputFile=" + inputFile);
						IntgSrvUtils.printConsole("OutputFile: " + outputFile);

						IntgSrvUtils.printConsole(inputFile + "; start processing...");

						JobParameter jpInputFile = new JobParameter(inputFile);
						JobParameter jpOutputFile = new JobParameter(outputFile);
						JobParameter jpReportFile = new JobParameter(outputReportFile);

						JobParameter jplocationFeedInputDir = new JobParameter(wercsregulatorydataFeedInputDir);
						JobParameter jplocationFeedDoneDir = new JobParameter(wercsregulatorydataFeedDoneDir);
						JobParameter jplocationFeedBadDir = new JobParameter(wercsregulatorydataFeedBadDir);
						JobParameter jplocationFeedOutputDir = new JobParameter(wercsregulatorydataFeedOutputDir);

						Map<String, JobParameter> map = new HashMap<String, JobParameter>();
						map.put(WercsAppConstants.JP_INPUTCORPDMZTOSTEP_FILE, jpInputFile);
						map.put(WercsAppConstants.JP_OUTPUTCORPDMZTOSTEP_FILE_STEP_XML, jpOutputFile);
						map.put(CORPDMZTOSTEP_FEED_REPORT_FILE, jpReportFile);

						map.put(WERCSREGULATORYDATA_INPUT_FOLDER, jplocationFeedInputDir);
						map.put(WERCSREGULATORYDATA_DONE_FOLDER, jplocationFeedDoneDir);
						map.put(WERCSREGULATORYDATA_BAD_FOLDER, jplocationFeedBadDir);
						map.put(WERCSREGULATORYDATA_OUTPUT_FOLDER, jplocationFeedOutputDir);

						infoLogString = "RunSchedulerCorpdmzToStep starts transformation on file: " + inputFile;
						traceLogger.info(clazzname, "run", infoLogString);
						ehfLogger.info(infoLogString);

						JobExecution result = launcher.run(job, new JobParameters(map));

						infoLogString = "RunSchedulerCorpdmzToStep completed transformation on file: " + inputFile;
						traceLogger.info(clazzname, "run", infoLogString);
						ehfLogger.info(infoLogString);

						infoLogString = "RunSchedulerCorpdmzToStep exits transformation on the file " + inputFile
								+ " and JOB_EXECUTION_EXIT_STATUS={" + result.toString() + "}";
						traceLogger.info(clazzname, "run", infoLogString);
						ehfLogger.info(infoLogString);

					} // if file exists
				} // for files
			}
		} catch (Throwable exception) {
			traceLogger.error(clazzname, "MY_RUN", "Exception: " + exception);
			ehfLogger.error(infoLogString);
			logger.error(infoLogString);
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerCorpdmzToStep.PUBLISH_ID);
		}
		traceLogger.info(clazzname, "run", "EXIT: WAIT FOR NEXT RUN...");
	}

	private List<File> getWercsCorpdmztoSTEPFeeds(List<File> sourceList) {

		List<File> fileList = new ArrayList<File>();

		for (File file : sourceList) {
			if (file.isFile() && file.getName().endsWith(CORPDMZTOSTEPFEED_FILE_EXTN)) {
				Unzipfile(file, file.getParentFile().getPath());
				WercsRegulatoryDataFeedJobListner msdsdocumentProcessor = new WercsRegulatoryDataFeedJobListner();
				try {
					msdsdocumentProcessor.moveToDestnationDir(file.getPath(), RunSchedulerCorpdmzToStep.wercsregulatorydataFeedDoneDir);
				} catch (IOException e) {
					logger.error("Caught an exception in movingZIPFoldertoDestinationFolder :" + e.getMessage());
					IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), RunSchedulerCorpdmzToStep.PUBLISH_ID);
					e.printStackTrace();
				}
			}
			fileList = getWercsFeeds(file.getParentFile().getPath());

		}
		return fileList;
	}

	private List<File> getWercsFeeds(String path) {

		File dir = new File(path);
		File[] files = dir.listFiles();
		List<File> wercsFileList = new ArrayList<File>();
		for (File listoffiles : Arrays.asList(files)) {
			try {
				if (listoffiles.getName().endsWith(REGULATORYDATAFEED_FILE_EXTN)
						&& listoffiles.getName().startsWith(REGULATORYDATAFEED_FILE_BEGIN)) {
					wercsFileList.add(listoffiles);
				} else if (listoffiles.getName().endsWith(MSDSDOCFEED_FILE_EXTN)) {
					DatamigrationCommonUtil.printConsole("Reading PDF File");
					CorpdmzToStepMSDSProcessor.MSDSCopdmzToStepMSDSProcessor(listoffiles);
				} else {
					DatamigrationCommonUtil.printConsole("Invalid File Format");
				}
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return wercsFileList;
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
				IntgSrvUtils.printConsole("RunSchedulerCorpdmzToStep.createWriterOutputDirs | Dir CREATED:" + directoryName);
		}
	}

	public String Unzipfile(File inputfile, String parent) {

		try {
			ZipFile zipFile = new ZipFile(inputfile);
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();

				String name = zipEntry.getName();
				long size = zipEntry.getSize();
				long compressedSize = zipEntry.getCompressedSize();
				System.out.printf("name: %-20s | size: %6d | compressed size: %6d\n", name, size, compressedSize);

				File file = new File(name);
				if (name.endsWith("/")) {
					file.mkdirs();
					continue;
				}

				File folder = new File(parent);
				if (!folder.exists()) {
					folder.mkdir();
				}

				File newFile = new File(parent + File.separator + name);
				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(newFile);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}

				is.close();
				fos.close();

			}
			zipFile.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return parent;

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

			String file1TimestampString = file1.getName().replace(REGULATORYDATAFEED_FILE_EXTN, DatamigrationAppConstants.EMPTY_STR);
			file1TimestampString = file1TimestampString.substring(15, file1TimestampString.length());

			String file2TimestampString = file2.getName().replace(REGULATORYDATAFEED_FILE_EXTN, DatamigrationAppConstants.EMPTY_STR);
			file2TimestampString = file2TimestampString.substring(15, file2TimestampString.length());
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
				logger.error(infoLogString);
				IntgSrvUtils.alertByEmail(e, clazzname, RunSchedulerCorpdmzToStep.PUBLISH_ID);
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

	public static void main(String[] args) {

		RunSchedulerCorpdmzToStep r = new RunSchedulerCorpdmzToStep();
		r.run();
	}
}
