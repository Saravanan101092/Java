
package com.staples.pim.delegate.wholesalers.listenerrunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

import com.staples.pim.base.common.bean.FTPConnectionBean;
import com.staples.pim.base.common.listenerandrunner.BatchJobListener;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.wholesalers.common.Util;

public class WholesalerDotcomFeedJobListner extends BatchJobListener {

	@AfterJob
	public void afterJob(JobExecution jobExecution) {

		JobParameters jobParameters = jobExecution.getJobParameters();

		String wholesalerDotcomFeedDoneDir = jobParameters.getString(RunSchedulerWholesalerDotcom.WHOLESALER_DOTCOM_FEED_DONE_DIR);
		String wholesalerDotcomFeedBadDir = jobParameters.getString(RunSchedulerWholesalerDotcom.WHOLESALER_DOTCOM_FEED_BAD_DIR);

		String inputFile = jobParameters.getString(IntgSrvAppConstants.JP_INPUT_FILE);
		String inputFileNameAndRecordCount = Util.getInstance().getFileName(inputFile) + Util.getInstance().getFileLineCount(inputFile);
		String outputFile = jobParameters.getString(IntgSrvAppConstants.JP_OUTPUT_FILE_STEP_XML);
		String errorReportFile = jobParameters.getString(RunSchedulerWholesalerDotcom.WHOLESALER_DOTCOM_FEED_REPORT_FILE);

		List<String> outputFileList = getOutputFiles(jobExecution, outputFile);

		/*
		 * If Job success then Input file moving to Done directory and Output
		 * STEP XML copied into STEP Inbound directory
		 */
		try {
			if (BatchStatus.COMPLETED.equals(jobExecution.getStatus())) {
				traceLogger.info(clazzname, "afterJob", "job COMPLETED");

				/**
				 * File Transfer 1. Output STEP xml transfer to STEP Inbound
				 * directory 2. Source feed moving to FINE_DONE Directory
				 */
				FTPConnectionBean ftpConnectionBean = getSFTPDestDetails();
				Util.getInstance().fileSFTPToStepInboundDir(ftpConnectionBean, outputFileList, RunSchedulerWholesalerDotcom.PUBLISH_ID);
				moveToDestnationDir(inputFile, wholesalerDotcomFeedDoneDir);
				traceLogger.info(clazzname, "afterJob", "Output STEP XML moved to :" + wholesalerDotcomFeedDoneDir);
			}
			/*
			 * If Job failed then Input file moving to Bad directory
			 */
			else if (BatchStatus.FAILED.equals(jobExecution.getStatus())) {
				/**
				 * File Transfer 1. Source feed moving to FILE_BAD Directory
				 */
				moveToDestnationDir(inputFile, wholesalerDotcomFeedBadDir);
				traceLogger.info(clazzname, "afterJob", "Output STEP XML moved to :" + wholesalerDotcomFeedBadDir);
				String msg = "Batch Status Failed - Feed file (" + inputFile + ") moved to " + wholesalerDotcomFeedBadDir + " directory";
				traceLogger.error(clazzname, "afterJob", msg);
				ehfLogger.error(clazzname, "afterJob", msg);
				RunSchedulerWholesalerDotcom.itemTraceLogger.error(msg);
				IntgSrvUtils.alertByEmail(new Exception(msg), clazzname, RunSchedulerWholesalerDotcom.PUBLISH_ID);
			}

			Util.getInstance().sendSummaryEmailReport("WholesalerDotcom", inputFileNameAndRecordCount, outputFileList, errorReportFile,
					jobExecution.getStatus());

		} catch (IOException exception) {
			traceLogger.error(clazzname, "afterJob", exception);
			ehfLogger.error(clazzname, "afterJob", exception);
			RunSchedulerWholesalerDotcom.itemTraceLogger.error(exception.getMessage());
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerWholesalerDotcom.PUBLISH_ID);
		}
	}

	/**
	 * Used to get the list of output files
	 * 
	 * @param jobExecution
	 * @param outputFile
	 * @return
	 */
	private List<String> getOutputFiles(JobExecution jobExecution, String outputFile) {

		List<String> outputFileList = new ArrayList<String>();

		Set<Entry<String, Object>> parameterMap = jobExecution.getExecutionContext().entrySet();

		for (Entry<String, Object> entry : parameterMap) {
			if (entry.getKey().contains(RunSchedulerWholesalerDotcom.FEED_FILE_BEGIN) && entry.getKey().contains(outputFile)
					&& entry.getKey().endsWith(".xml")) {
				outputFileList.add(String.valueOf(entry.getValue()));
			}
		}
		return Util.getInstance().fileSort(outputFileList);
	}

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {

		traceLogger.info(clazzname, "beforeJob", "ENTER/.../EXIT: RunSchedulerWholesalerDotcom - initialization <- job params");
	}

	private void moveToDestnationDir(String outputFileString, String targetDirString) throws IOException {

		File srcFile = new File(outputFileString);
		File newFile = new File(targetDirString + String.valueOf(System.currentTimeMillis()) + "_" + srcFile.getName());

		// Appending epoch time stamp
		if (srcFile.renameTo(newFile)) {
			IntgSrvUtils.printConsole("Renamed");
		} else {
			IntgSrvUtils.printConsole("Not able to rename");
			ehfLogger.error(clazzname, "afterJob", "Not able to rename");
		}
	}

	private FTPConnectionBean getSFTPDestDetails() {

		FTPConnectionBean connectionsBean = new FTPConnectionBean();
		connectionsBean.setHostName(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.WHOLESALER_FEED_SFTP_HOST_STEP));
		connectionsBean.setUserId(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.WHOLESALER_FEED_SFTP_USERNAME_STEP));
		connectionsBean.setPassword(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.WHOLESALER_FEED_SFTP_PASSWORD_STEP));
		connectionsBean.setDestinationUrl(IntgSrvPropertiesReader
				.getProperty(IntgSrvAppConstants.WHOLESALER_DOTCOM_FEED_SFTP_TARGET_DIR_STEP));
		return connectionsBean;
	}
}