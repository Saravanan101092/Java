package com.staples.pim.delegate.wercs.piptostep.listenerrunner;

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

import oracle.jdbc.pool.OracleDataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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


public class RunSchedulerPIPToStep extends RunScheduler {

	
	/*
	 * Logger Configurations
	 */
	public static IntgSrvLogger		itemTraceLogger								= IntgSrvLogger.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_PIP_TO_STEP);
	public HashMap<String, String>	ehfItemLogHeadingData						= new HashMap<String, String>();
	
	/*
	 * REQUIRED DIRECTORIES
	 */
	public static String			WERCS_PIP_STEP_INPUT_DIR					= "WERCS_PIP_STEP_INPUT_DIR";
	public static String			WERCS_PIP_STEP_OUTPUT_DIR					= "WERCS_PIP_STEP_OUTPUT_DIR";
	public static String			WERCS_PIP_STEP_REPORT_FILE					= "WERCS_PIP_STEP_REPORT_FILE";
	public static String			WERCS_PIP_STEP_DONE_DIR						= "WERCS_PIP_STEP_DONE_DIR";
	public static String			WERCS_PIP_STEP_BAD_DIR						= "WERCS_PIP_STEP_BAD_DIR";
	public static String			WERCS_PIP_STEP_REPORT_DIR					= "WERCS_PIP_STEP_REPORT_DIR";
	public static String			PUBLISH_ID									= "PIPE101";
	
	/*
	 * JOB NAMES
	 */
	public static String			WERCS_PIP_STEP_JOB							= "wercsPipStepJob";
	
	/*
	 * Feed File information
	 */
	public static String			FEED_FILE_EXTN								= ".xsv";
	public static String			FEED_FILE_NAME_DATE_FORMAT					= "yyyy_MM_dd_HH_mm_ss_SSSSSS";
	public static String			FEED_FILE_BEGIN								= IntgSrvPropertiesReader.getProperty("WERCS_PIP_STEP_FEED_FILE_BEGIN");
	public static String			infoLogString;
	
	public static OracleDataSource datasource;
	
	public OracleDataSource getDatasource() {

		return datasource;
	}

	public void setDatasource(OracleDataSource datasource) {

		this.datasource = datasource;
	}
	
	
	@Override
	public void run() {
		
			
		traceLogger.info(clazzname, "run", "ENTER: RunSchedulerPIPtoStep listenerrunner");

		try
		{
			
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
			traceId = ((IntgSrvErrorHandlingFrameworkICD) ehfICD).getNewTraceId();
			ehfItemLogHeadingData.put(ErrorHandlingFrameworkICD.EHF_ATTR_PUBLISH_ID, PUBLISH_ID);
			ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_JOBRUNNER, traceId);
			ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_TRANSFORM, traceId);
			
			infoLogString = "RunSchedulerPIPtoStep job runner starts... ";
			traceLogger.info(clazzname, "run", infoLogString);
			
									
			String wercsPipStepInputDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(WERCS_PIP_STEP_INPUT_DIR));
			String wercsPipStepOutputDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(WERCS_PIP_STEP_OUTPUT_DIR));
			String wercsPipStepDoneDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(WERCS_PIP_STEP_DONE_DIR));
			String wercsPipStepBadDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(WERCS_PIP_STEP_BAD_DIR));
			String wercsPipStepReportDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(WERCS_PIP_STEP_REPORT_DIR));
			
			
			IntgSrvUtils.printConsole("WERCS_PIP_STEP_INPUT_DIR = " + wercsPipStepInputDir);
			IntgSrvUtils.printConsole("WERCS_PIP_STEP_OUTPUT_DIR = " + wercsPipStepOutputDir);
			IntgSrvUtils.printConsole("WERCS_PIP_STEP_DONE_DIR = " + wercsPipStepDoneDir);
			IntgSrvUtils.printConsole("WERCS_PIP_STEP_BAD_DIR = " + wercsPipStepBadDir);
			IntgSrvUtils.printConsole("WERCS_PIP_STEP_REPORT_DIR = " + wercsPipStepReportDir);
			
			
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsPipStepInputDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsPipStepOutputDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsPipStepDoneDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsPipStepBadDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsPipStepReportDir));
			
			
			String jobContext = IntgSrvUtils.reformatFilePath(IntgSrvUtils.getConfigDir() + "/" + IntgSrvUtilConstants.PIP_STEP_CONTEXT_FILE);
			
			infoLogString = "To load context context-WERCS-PipStep.xml: " + jobContext;
			traceLogger.info(clazzname, "MY_RUN", infoLogString);
			ehfLogger.info(infoLogString);
			
				
			ApplicationContext context = new ClassPathXmlApplicationContext("file:" + jobContext);
		    JobLauncher launcher = (JobLauncher) context.getBean(IntgSrvAppConstants.JOB_LAUNCHER);
		    Job job = (Job) context.getBean(WERCS_PIP_STEP_JOB);
		    
		    infoLogString = "RunSchedulerPIPtoStep context loaded :" + jobContext;
			traceLogger.info(clazzname, "MY_RUN", infoLogString);
			ehfLogger.info(infoLogString);
		    
			File dir = new File(wercsPipStepInputDir);
			File[] files = dir.listFiles();
			List<File> fileList = getPipStepContractFeeds(Arrays.asList(files));
			System.out.println("dir : " +  dir);
		
			
			
			if (fileList == null || fileList.size() == 0) 
			{
				IntgSrvUtils.printConsole("Pip Step Feeds not found");
				traceLogger.info(clazzname, "run", "Pip Step Feeds not found");
			} 
			else 
			{

				/*
				 * Sort based on time stamp from the file name
				 */
				List<File> sortedFileList = sortBasedOnFileNameTimestamp(fileList);
				IntgSrvUtils.printConsole("Number of files:" + sortedFileList.size() + " in directory:" + sortedFileList);
				
				for (File file : sortedFileList) 
				{
					if (file.isFile()) 
					{
						String epochTime = String.valueOf(System.currentTimeMillis());
					    String inputFile = file.getAbsolutePath();
					    String outputFile = wercsPipStepOutputDir + file.getName().replace(FEED_FILE_EXTN, "");
					    String outputReportFile = wercsPipStepReportDir + epochTime + "_" + file.getName();
					   				  					    
					    ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_INPUT_FILENAME, inputFile);
						ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_OUTPUT_FILENAME, outputFile);
						ehfItemLogHeadingData.put(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_STEP_EXPORT_TIME, epochTime);
						writeEHFItemLogHeading(itemTraceLogger, ehfItemLogHeadingData);
					    
					    inputFile = IntgSrvUtils.reformatFilePath(inputFile);
						outputFile = IntgSrvUtils.reformatFilePath(outputFile);
						outputReportFile = IntgSrvUtils.reformatFilePath(outputReportFile);
		
						IntgSrvUtils.printConsole("InputFile=" + inputFile);
						IntgSrvUtils.printConsole("OutputFile: " + outputFile);
						IntgSrvUtils.printConsole(inputFile + "; start processing...");

						JobParameter jpInputFile = new JobParameter(inputFile);
						JobParameter jpOutputFile = new JobParameter(outputFile);
						JobParameter jpReportFile = new JobParameter(outputReportFile);

						JobParameter jplocationFeedInputDir = new JobParameter(wercsPipStepInputDir);
						JobParameter jplocationFeedDoneDir = new JobParameter(wercsPipStepDoneDir);
						JobParameter jplocationFeedBadDir = new JobParameter(wercsPipStepBadDir);
						JobParameter jplocationFeedOutputDir = new JobParameter(wercsPipStepOutputDir);
						
											
						Map<String, JobParameter> map = new HashMap<String, JobParameter>();
						map.put(IntgSrvAppConstants.JP_INPUT_FILE, jpInputFile);
						map.put(IntgSrvAppConstants.JP_OUTPUT_FILE_STEP_XML, jpOutputFile);
						map.put(WERCS_PIP_STEP_REPORT_FILE, jpReportFile);
					
						map.put(WERCS_PIP_STEP_INPUT_DIR, jplocationFeedInputDir);
						map.put(WERCS_PIP_STEP_OUTPUT_DIR, jplocationFeedOutputDir);
						map.put(WERCS_PIP_STEP_DONE_DIR, jplocationFeedDoneDir);
						map.put(WERCS_PIP_STEP_BAD_DIR, jplocationFeedBadDir);
						

						infoLogString = "RunSchedulerPIPToSTEP starts transformation on file: " + inputFile;
						traceLogger.info(clazzname, "run", infoLogString);
						ehfLogger.info(infoLogString);

						JobExecution result = launcher.run(job, new JobParameters(map));
						
						infoLogString = "RunSchedulerPIPToSTEP completed transformation on file: " + inputFile;
						traceLogger.info(clazzname, "run", infoLogString);
						ehfLogger.info(infoLogString);

						infoLogString = "RunSchedulerPIPToSTEP exits transformation on the file " + inputFile + " and JOB_EXECUTION_EXIT_STATUS={" + result.toString() + "}";
						traceLogger.info(clazzname, "run", infoLogString);
						ehfLogger.info(infoLogString);
					}// if file exists
				}// for files
			}
		}
		catch(Throwable exception)
		{
			traceLogger.error(clazzname, "MY_RUN", "Exception: " + exception);
			ehfLogger.error(infoLogString);
			itemTraceLogger.error(infoLogString);
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerPIPToStep.PUBLISH_ID);
		}
		traceLogger.info(clazzname, "run", "EXIT: WAIT FOR NEXT RUN...");
	}
	
	/**
	 * Will return the list of files only if it is a file, begins with "FEED_FILE_BEGIN", and ends with "FEED_FILE_EXTN" from list of sourceList files. 
	 */
	private List<File> getPipStepContractFeeds(List<File> sourceList) {

		List<File> contractFileList = new ArrayList<File>();

		for (File file : sourceList) 
		{
			String fileName = file.getName();
			if (file.isFile() && fileName.startsWith(FEED_FILE_BEGIN) && fileName.endsWith(FEED_FILE_EXTN)) {
				contractFileList.add(file);
			}
		}
		return contractFileList;
	}
	
	private List<File> sortBasedOnFileNameTimestamp(List<File> fileList)
	{

		ComparatorBasedOnFileNameTimestamp comparatorObj = new ComparatorBasedOnFileNameTimestamp();
		Collections.sort(fileList, comparatorObj);

		return fileList;
	}

	/**
	 * Creating required directories if not exist
	 */
	protected void createWriterOutputDir(String directoryName)
	{

		File directory = new File(directoryName);
		if (!directory.exists()) 
		{
			if (directory.mkdirs())
				IntgSrvUtils.printConsole("RunSchedulerPipToStep.createWriterOutputDirs | Dir CREATED:" + directoryName);
		}
	}
	
	/**
	 * Comparator to sort the files from Incoming directory - Don't ignore any
	 * files
	 */

	private class ComparatorBasedOnFileNameTimestamp implements Comparator<File> {

		@Override
		public int compare(File file1, File file2) {

			String file1TimestampString = file1.getName().replace(FEED_FILE_EXTN, DatamigrationAppConstants.EMPTY_STR);
			file1TimestampString = file1TimestampString.substring(findIndexOf(file1TimestampString,"_" , 2) + 1, file1TimestampString.length());
		
			String file2TimestampString = file2.getName().replace(FEED_FILE_EXTN, DatamigrationAppConstants.EMPTY_STR);
			file2TimestampString = file2TimestampString.substring(findIndexOf(file2TimestampString,"_" , 2) + 1, file2TimestampString.length());

			SimpleDateFormat sdf = new SimpleDateFormat(FEED_FILE_NAME_DATE_FORMAT);

			Date file1Date = null;
			Date file2Date = null;
			try {
				file1Date = sdf.parse(file1TimestampString);
				file2Date = sdf.parse(file2TimestampString);
			} catch (ParseException exception) {
				infoLogString = "Date parsing issue while sorting the file based on file name " + exception.getMessage();
				traceLogger.error(clazzname, "MY_RUN", "Exception: " + exception);
				ehfLogger.error(infoLogString);
				itemTraceLogger.error(infoLogString);
				IntgSrvUtils.printConsole(infoLogString);
				IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerPIPToStep.PUBLISH_ID);
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

	
	/**
	 * To get index of "_"  for getting file name for sorting
	 */
	
	public static int findIndexOf(String source, String pattern, int occurence) {
		
		/**
		 *  Source string will be the string on which operation is done.
		 *  Pattern will be whose occurrence we want (here "_" ) 
		 *  Occurrence will be at which occurring value we want
		 *  We will be returning the position of that pattern occurring in the source string
		 */

		int count = 0, current_position = 0, required_position = 0;

		while (count < occurence) {
			current_position = source.indexOf(pattern);
			if (current_position > -1) {
				source = source.substring(current_position + 1);
				required_position += current_position + 1;
				count++;
			} else {
				return -1;
			}
		}
		return required_position - 1;
	}
	
	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
	

}