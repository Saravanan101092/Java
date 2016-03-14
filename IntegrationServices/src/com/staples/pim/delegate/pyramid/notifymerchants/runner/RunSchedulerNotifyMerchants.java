package com.staples.pim.delegate.pyramid.notifymerchants.runner;

import java.io.File;
import java.util.HashMap;
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
import com.staples.pim.delegate.pyramid.notifymerchants.processor.NotifyMerchantsProcessor;
import com.staples.pim.delegate.pyramid.notifymerchants.reader.NotifyMerchantsItemReader;
import com.staples.pim.delegate.wayfair.taxonomyupdate.TaxonomyProcessor;

public class RunSchedulerNotifyMerchants extends RunScheduler {
	/*
	 * Logger Configurations
	 */
	public static IntgSrvLogger itemTraceLogger = IntgSrvLogger
			.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER_NOTIFY_MERCHANTS);
	
	public static final String	inputDir		= IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(IntgSrvAppConstants.NOTIFY_MERCHANTS_UNPROCESSED_FOLDER));
	

	/*
	 * Job related Details
	 */
	public static String NOTIFY_MERCHANTS_JOB = "notifyMerchants";

	public static String infoLogString;

	public static String PUBLISH_ID = "NOTIFY_MERCHANTS";

	@Override
	public void run() {

		System.out.println("RunSchedulerNotifyMerchant RunScheduler start...");
		try {

	
			
			File dir = new File(inputDir);

			File[] files = dir.listFiles();

			if (files == null || files.length == 0) {
				System.out.println("No files found to notify merchants...");
			} else {
				NotifyMerchantsProcessor notifyMerchantsProcessor=new NotifyMerchantsProcessor ();
				notifyMerchantsProcessor.processFiles();
			}

		} catch (Throwable exception) {

			traceLogger.error(clazzname, "MY_RUN", "Exception: " + exception);
			infoLogString = "Exception" + exception;
			ehfLogger.error(infoLogString);
			itemTraceLogger.error(infoLogString);
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerNotifyMerchants.PUBLISH_ID);
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}

	}

	
	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		// TODO Auto-generated method stub
		return null;
	}

}
