
package com.staples.pim.delegate.locationfeed.listenerrunner;

import java.io.File;
import java.io.IOException;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

import com.staples.pim.base.common.bean.FTPConnectionBean;
import com.staples.pim.base.common.listenerandrunner.BatchJobListener;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.SFTPManager;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;

public class LocationFeedJobListner extends BatchJobListener {

	@AfterJob
	public void afterJob(JobExecution jobExecution) {

		JobParameters jp = jobExecution.getJobParameters();

		String locationFeedDoneDir = jp.getString(RunSchedulerLocationFeed.LOCATION_FEED_TXT_DONE_DIR);
		String locationFeedBadDir = jp.getString(RunSchedulerLocationFeed.LOCATION_FEED_TXT_BAD_DIR);

		String inputFile = jp.getString(IntgSrvAppConstants.JP_INPUT_FILE);
		String outputFileAddUpdate = jp.getString(IntgSrvAppConstants.JP_OUTPUT_FILE_STEP_XML_ADDUPDATE);
		String outputFileDelete = jp.getString(IntgSrvAppConstants.JP_OUTPUT_FILE_STEP_XML_DELETE);

		/*
		 * If Job success then Input file moving to Done directory and Output
		 * STEP XML copied into STEP Inbound directory
		 */
		try {
			if (BatchStatus.COMPLETED.equals(jobExecution.getStatus())) {
				traceLogger.info(clazzname, "afterJob", "job COMPLETED");

				fileSFTPToStepInboundDir(outputFileAddUpdate);
				fileSFTPToStepInboundDir(outputFileDelete);

				moveToDestnationDir(inputFile, locationFeedDoneDir);
				traceLogger.info(clazzname, "afterJob", "Output STEP XML moved to :" + locationFeedDoneDir);
			}
			/*
			 * If Job failed then Input file moving to Bad directory
			 */
			else if (BatchStatus.FAILED.equals(jobExecution.getStatus())) {
				moveToDestnationDir(inputFile, locationFeedBadDir);
				traceLogger.info(clazzname, "afterJob", "Output STEP XML moved to :" + locationFeedBadDir);
				String msg = "Batch Status Failed - Feed file (" + inputFile + ") moved to " + locationFeedBadDir + " directory";
				IntgSrvUtils.alertByEmail(new Exception(msg), clazzname, RunSchedulerLocationFeed.PUBLISH_ID);
			}
		} catch (IOException exception) {
			traceLogger.error(clazzname, "afterJob", exception);
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerLocationFeed.PUBLISH_ID);
		}
	}

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {

		traceLogger.info(clazzname, "beforeJob", "ENTER/.../EXIT: LocationFeedJobListner - initialization <- job params");
	}

	private void moveToDestnationDir(String outputFileString, String locationFeedDoneDirString) throws IOException {

		File srcFile = new File(outputFileString);
		File newFile = new File(locationFeedDoneDirString + String.valueOf(System.currentTimeMillis()) + "_" + srcFile.getName());

		// Appending epoch time stamp
		if (srcFile.renameTo(newFile)) {
			IntgSrvUtils.printConsole("Renamed");
		} else {
			IntgSrvUtils.printConsole("Not able to rename");
		}
	}

	private void fileSFTPToStepInboundDir(String srcFileString) throws IOException {

		FTPConnectionBean ftpConnectionBean = getSFTPDestDetails();
		SFTPManager sftpManager = new SFTPManager(ftpConnectionBean);
		sftpManager.setPublishId(RunSchedulerLocationFeed.PUBLISH_ID);

		boolean isSentSuccessfully = sftpManager.uploadFile(srcFileString);

		if (isSentSuccessfully) {
			traceLogger.info(clazzname, "SFTP Success", "ENTER/.../EXIT: LocationFeedJobListner - initialization <- job params");
		} else {
			traceLogger.info(clazzname, "SFTP Failed", "ENTER/.../EXIT: LocationFeedJobListner - initialization <- job params");
		}
	}

	private FTPConnectionBean getSFTPDestDetails() {

		FTPConnectionBean connectionsBean = new FTPConnectionBean();
		connectionsBean.setHostName(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.LOCATION_FEED_SFTP_HOST_STEP));
		connectionsBean.setUserId(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.LOCATION_FEED_SFTP_USERNAME_STEP));
		connectionsBean.setPassword(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.LOCATION_FEED_SFTP_PASSWORD_STEP));
		connectionsBean.setDestinationUrl(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.LOCATION_FEED_SFTP_TARGET_DIR_STEP));
		return connectionsBean;
	}
}