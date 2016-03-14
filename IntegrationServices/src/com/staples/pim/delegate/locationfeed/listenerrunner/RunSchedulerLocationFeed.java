
package com.staples.pim.delegate.locationfeed.listenerrunner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtilConstants;
import com.staples.pim.base.util.IntgSrvUtils;

@Component
public class RunSchedulerLocationFeed extends RunScheduler {

	public static String	LOCATION_FEED_TXT_INPUT_DIR		= "LOCATION_FEED_TXT_INPUT_DIR";
	public static String	LOCATION_FEED_TXT_DONE_DIR		= "LOCATION_FEED_TXT_DONE_DIR";
	public static String	LOCATION_FEED_TXT_BAD_DIR		= "LOCATION_FEED_TXT_BAD_DIR";
	public static String	LOCATION_FEED_XML_OUTPUT_DIR	= "LOCATION_FEED_XML_OUTPUT_DIR";

	public static String	PUBLISH_ID						= "GALE105";

	public void run() {

		traceLogger.info(clazzname, "run", "ENTER: RunSchedulerLocationFeed runner");

		try {

			String msgDesc = "RunSchedulerLocationFeed job runner starts... ";
			traceLogger.info(clazzname, "run", msgDesc);

			String locationFeedInputDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(LOCATION_FEED_TXT_INPUT_DIR));
			String locationFeedDoneDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(LOCATION_FEED_TXT_DONE_DIR));
			String locationFeedBadDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(LOCATION_FEED_TXT_BAD_DIR));
			String locationFeedOutputDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(LOCATION_FEED_XML_OUTPUT_DIR));

			IntgSrvUtils.printConsole("LOCATION_FEED_TXT_INPUT_DIR = " + locationFeedInputDir);
			IntgSrvUtils.printConsole("LOCATION_FEED_TXT_DONE_DIR = " + locationFeedDoneDir);
			IntgSrvUtils.printConsole("LOCATION_FEED_TXT_BAD_DIR = " + locationFeedBadDir);
			IntgSrvUtils.printConsole("LOCATION_FEED_XML_OUTPUT_DIR = " + locationFeedOutputDir);

			createWriterOutputDir(IntgSrvUtils.reformatFilePath(locationFeedInputDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(locationFeedDoneDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(locationFeedBadDir));
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(locationFeedOutputDir));

			String jobContext = IntgSrvUtils.reformatFilePath(IntgSrvUtilConstants.CONFIG_DIR_LOCATION_DEFAULT + "/"
					+ IntgSrvUtilConstants.JOB_LOCATION_FEED_CONTEXT);

			traceLogger.info(clazzname, "MY_RUN", "to load context job-LocationFeed.xml: " + jobContext);

			ApplicationContext context = new FileSystemXmlApplicationContext("file:" + jobContext);

			JobLauncher launcher = (JobLauncher) context.getBean(IntgSrvAppConstants.JOB_LAUNCHER);
			Job job = (Job) context.getBean("fixedLenToXMLJob");

			msgDesc = "RunSchedulerLocationFeed context loaded job-LocationFeed.xml: " + jobContext;
			traceLogger.info(clazzname, "run", msgDesc);

			SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("yyyyMMdd_HHmmsss");

			File dir = new File(locationFeedInputDir);
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

						String dateJob = dateformatyyyyMMdd.format(new Date());
						String epochTime = String.valueOf(System.currentTimeMillis());
						String inputFile = files[i].getAbsolutePath();

						String outputFileAddUpdate = locationFeedOutputDir + dateJob + "_AddUpdate_" + epochTime + "_"
								+ files[i].getName().replace(".txt", ".xml");

						String outputFileDelete = locationFeedOutputDir + dateJob + "_Delete_" + epochTime + "_"
								+ files[i].getName().replace(".txt", ".xml");

						inputFile = IntgSrvUtils.reformatFilePath(inputFile);
						outputFileAddUpdate = IntgSrvUtils.reformatFilePath(outputFileAddUpdate);
						outputFileDelete = IntgSrvUtils.reformatFilePath(outputFileDelete);

						IntgSrvUtils.printConsole("inputFile=" + inputFile);
						IntgSrvUtils.printConsole("OutputFileAddUpdate: " + outputFileAddUpdate);
						IntgSrvUtils.printConsole("OutputFileAddUpdate: " + outputFileDelete);

						IntgSrvUtils.printConsole("dateJob=" + dateJob);
						IntgSrvUtils.printConsole(files[i].getPath() + "; start processing...");

						JobParameter datePARAM = new JobParameter(dateJob);
						JobParameter jpInputFile = new JobParameter(inputFile);
						JobParameter jpOutputFileAddUpdate = new JobParameter(outputFileAddUpdate);
						JobParameter jpOutputFileDelete = new JobParameter(outputFileDelete);

						JobParameter jplocationFeedInputDir = new JobParameter(locationFeedInputDir);
						JobParameter jplocationFeedDoneDir = new JobParameter(locationFeedDoneDir);
						JobParameter jplocationFeedBadDir = new JobParameter(locationFeedBadDir);
						JobParameter jplocationFeedOutputDir = new JobParameter(locationFeedOutputDir);

						Map<String, JobParameter> map = new HashMap<String, JobParameter>();
						map.put(IntgSrvAppConstants.JP_INPUT_FILE, jpInputFile);
						map.put(IntgSrvAppConstants.JP_OUTPUT_FILE_STEP_XML_ADDUPDATE, jpOutputFileAddUpdate);
						map.put(IntgSrvAppConstants.JP_OUTPUT_FILE_STEP_XML_DELETE, jpOutputFileDelete);
						map.put(IntgSrvAppConstants.JP_DATE, datePARAM);

						map.put(LOCATION_FEED_TXT_INPUT_DIR, jplocationFeedInputDir);
						map.put(LOCATION_FEED_TXT_DONE_DIR, jplocationFeedDoneDir);
						map.put(LOCATION_FEED_TXT_BAD_DIR, jplocationFeedBadDir);
						map.put(LOCATION_FEED_XML_OUTPUT_DIR, jplocationFeedOutputDir);

						msgDesc = "RunSchedulerLocationFeed starts transformation on file: [" + (i + 1) + "/" + numfiles + ", "
								+ files[i].getName() + "]";
						traceLogger.info(clazzname, "run", msgDesc);

						JobExecution result = launcher.run(job, new JobParameters(map));

						msgDesc = "RunSchedulerLocationFeed completed transformation on file: [" + (i + 1) + "/" + numfiles + ", "
								+ files[i].getName() + "]";
						traceLogger.info(clazzname, "run", msgDesc);

						msgDesc = "RunSchedulerLocationFeed exits transformation on the file with ==>> JOB_EXECUTION_EXIT_STATUS={"
								+ result.toString() + "}";
						traceLogger.info(clazzname, "run", msgDesc);
					} // if file exists
				} // for files
			}
		} catch (Throwable exception) {
			traceLogger.error(clazzname, "MY_RUN", "Exception: " + exception);
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerLocationFeed.PUBLISH_ID);
		}
		traceLogger.info(clazzname, "run", "EXIT: WAIT FOR NEXT RUN...");
	}

	protected void createWriterOutputDir(String directoryName) {

		File directory = new File(directoryName);
		if (!directory.exists()) {
			if (directory.mkdirs())
				IntgSrvUtils.printConsole("LW-DEBUG: RunSchedulerLocationFeed.createWriterOutputDirs | Dir CREATED:" + directoryName);
		}
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		// TODO Auto-generated method stub
		return null;
	}
}
