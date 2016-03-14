
package com.staples.pim.delegate.pyramid.itemonbrdtmupdate.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtilConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.base.util.LastModifiedFileComparator;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.wayfair.taxonomyupdate.TaxonomyProcessor;

/**
 * 
 * 
 *
 */
public class RunSchedulerItemOnbrdTm extends RunScheduler {

	/*
	 * Logger Configurations
	 */
	public static IntgSrvLogger	itemTraceLogger				= IntgSrvLogger
																	.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_PYR_ITEMONBRDTM_UPDATE);
	/*
	 * Required Directory Configurations
	 */
	public static final String	TAXONOMY_FILEDONE_FOLDER_OT		= IntgSrvUtils.reformatFilePath(TaxonomyProcessor.TAXONOMY_FILEDONE_FOLDER_OT);
	public static final String	TAXONOMY_FILEDONE_FOLDER	= IntgSrvUtils.reformatFilePath(TaxonomyProcessor.TAXONOMY_FILEDONE_FOLDER);
	public static final String	PYR_IOB_TM_PROCESS_DIR		= IntgSrvUtils
																	.reformatFilePath(IntgSrvPropertiesReader
																			.getProperty(IntgSrvAppConstants.PYR_IOB_TM_SFTP_DOWNLOAD_TARGET_DIR_STEP));
	public static String		pyrIOBFeedProcessExcelName	= IntgSrvPropertiesReader.getProperty("PYR_IOB_TEMPLATE_NAME");

	/*
	 * Job related Details
	 */
	public static String		PYRAMID_IOB_TM_JOB			= "itemOnbrdTmUpdate";
	public static String		FEED_FILE_BEGIN				= "snq_taxonomy_";

	public static String		infoLogString;

	public static String		PUBLISH_ID					= "IOBTEMPLATEUPDATE";

	@Override
	public void run() {

		com.staples.pim.base.util.IntgSrvUtils.printConsole("RunSchedulerItemOnbrdTm RunScheduler start...");
		try {

			createWriterOutputDir(TAXONOMY_FILEDONE_FOLDER_OT);
			createWriterOutputDir(TAXONOMY_FILEDONE_FOLDER);
			createWriterOutputDir(PYR_IOB_TM_PROCESS_DIR);

			String jobContext = IntgSrvUtils.reformatFilePath(IntgSrvUtilConstants.CONFIG_DIR_LOCATION_DEFAULT + "/"
					+ IntgSrvUtilConstants.PYRAMID_IOB_CONTEXT_FILE);

			IntgSrvUtils.printConsole("To load context context-PyramidIOBTemplate.xml: " + jobContext);

			infoLogString = "To load context context-PyramidIOBTemplate.xml: " + jobContext;
			traceLogger.info(clazzname, "MY_RUN", infoLogString);

			ApplicationContext context = new FileSystemXmlApplicationContext("file:" + jobContext);

			JobLauncher launcher = (JobLauncher) context.getBean(IntgSrvAppConstants.JOB_LAUNCHER);
			Job job = (Job) context.getBean(PYRAMID_IOB_TM_JOB);

			IntgSrvUtils.printConsole("Got the job-PYRIOBTemplateJob");

			File dir = new File(TAXONOMY_FILEDONE_FOLDER_OT);
			File dir1 = new File(TAXONOMY_FILEDONE_FOLDER);
			
			File[] files = dir.listFiles();
			File[] files1 = dir1.listFiles();
			
			List<File> fileList = getOTTaxonomyFeeds(Arrays.asList(files));
			fileList.addAll(getOTTaxonomyFeeds(Arrays.asList(files1)));

			if (fileList == null || fileList.size() == 0) {
				IntgSrvUtils.printConsole("Taxonomy Feeds not found on Taxonomy Done Directory");
			} else {

				LastModifiedFileComparator comparator = new LastModifiedFileComparator();
				Collections.sort(fileList, comparator);

				File taxonomyFeed = fileList.get(fileList.size()-1);
				IntgSrvUtils.printConsole("Taxonomy File which is going to process for IOB Templeate :" + taxonomyFeed);

				JobParameter jpInputFile = new JobParameter(taxonomyFeed.getAbsolutePath());
				JobParameter jpProcessFileDir = new JobParameter(PYR_IOB_TM_PROCESS_DIR);
				JobParameter jpProcessFileName = new JobParameter(pyrIOBFeedProcessExcelName);

				Map<String, JobParameter> map = new HashMap<String, JobParameter>();
				map.put(IntgSrvAppConstants.JP_INPUT_FILE, jpInputFile);
				map.put("pyrIOBFeedProcessDir", jpProcessFileDir);
				map.put("pyrIOBFeedProcessExcelName", jpProcessFileName);

				IntgSrvUtils.printConsole("IOB Templeate update launching job");
				JobExecution result = launcher.run(job, new JobParameters(map));

				IntgSrvUtils.printConsole(result.getStatus().toString());
			}

		} catch (Throwable exception) {

			traceLogger.error(clazzname, "MY_RUN", "Exception: " + exception);
			infoLogString = "Exception" + exception;
			ehfLogger.error(infoLogString);
			itemTraceLogger.error(infoLogString);
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerItemOnbrdTm.PUBLISH_ID);
		}

	}

	/**
	 * Creating required directories if not exist
	 */
	protected void createWriterOutputDir(String directoryName) {

		File directory = new File(directoryName);
		if (!directory.exists()) {
			if (directory.mkdirs())
				IntgSrvUtils.printConsole("RunSchedulerPYRIOBTemplate.createWriterOutputDirs | Dir CREATED:" + directoryName);
		}
	}

	/**
	 * 
	 * @param sourceList
	 * @return
	 */
	private List<File> getOTTaxonomyFeeds(List<File> sourceList) {

		List<File> taxonomyFileList = new ArrayList<File>();
		for (File file : sourceList) {
			String fileName = file.getName();
			if (file.isFile() && fileName.startsWith(FEED_FILE_BEGIN)) {
				taxonomyFileList.add(file);
			}
		}
		return taxonomyFileList;
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		// TODO Auto-generated method stub
		return null;
	}

}
