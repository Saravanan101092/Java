
package com.staples.pim.delegate.pyramid.itemonbrdtmupdate.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;

import com.staples.pim.base.common.bean.FTPConnectionBean;
import com.staples.pim.base.common.listenerandrunner.BatchJobListener;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.SFTPManager;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.pyramid.itemonbrdtmupdate.runner.RunSchedulerItemOnbrdTm;

public class ItemOnbrdTmJobListener extends BatchJobListener {

	public void afterJob(JobExecution jobExecution) {

		// TODO - 3. Output IOB TEMPLATE needs to maintained in SB Server for
		// tracking purpose

		IntgSrvUtils.printConsole("PyramidIOBTemplate job start....after job()");

		JobParameters jobParameters = jobExecution.getJobParameters();

		String pyrIOBFeedProcessDir = jobParameters.getString("pyrIOBFeedProcessDir");
		String outputFile = jobParameters.getString("pyrIOBFeedProcessExcelName");
		String outputFileAbsPath = pyrIOBFeedProcessDir + outputFile;

		try {
			if (BatchStatus.COMPLETED.equals(jobExecution.getStatus())) {

				/**
				 * File Transfer 1. Output STEP xml transfer to STEP Inbound
				 * directory 2. Source feed moving to FINE_DONE Directory
				 */
				FTPConnectionBean ftpConnectionBean = getSFTPUploadDetails();
				SFTPManager sftpManager = new SFTPManager(ftpConnectionBean);
				sftpManager.setPublishId(RunSchedulerItemOnbrdTm.PUBLISH_ID);
				boolean sftpStatus = sftpManager.uploadFile(outputFileAbsPath);
				IntgSrvUtils.printConsole("IOB Templete uploaded status: " + sftpStatus);
			}

			else if (BatchStatus.FAILED.equals(jobExecution.getStatus())) {
				String msg = "Batch Status Failed - Taxonomy Feed file for IOB Template update (from Taxonomy File_Done directory)";
				ehfLogger.error(msg);
				IntgSrvUtils.alertByEmail(new Exception(msg), clazzname, RunSchedulerItemOnbrdTm.PUBLISH_ID);
			}

		} catch (Exception exception) {
			traceLogger.error(clazzname, "MY_RUN", "Exception: " + exception);
			String infoLogString = "Exception" + exception;
			ehfLogger.error(infoLogString);
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerItemOnbrdTm.PUBLISH_ID);
		}
	}

	private FTPConnectionBean getSFTPUploadDetails() {

		FTPConnectionBean connectionsBean = new FTPConnectionBean();
		connectionsBean.setHostName(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PYR_IOB_TM_SFTP_HOST_STEP));
		connectionsBean.setUserId(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PYR_IOB_TM_SFTP_USERNAME_STEP));
		connectionsBean.setPassword(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PYR_IOB_TM_SFTP_PASSWORD_STEP));
		connectionsBean.setDestinationUrl(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PYR_IOB_TM_SFTP_UPLOAD_TARGET_DIR_STEP));
		return connectionsBean;
	}

	private FTPConnectionBean getSFTPDownloadDetails() {

		FTPConnectionBean connectionsBean = new FTPConnectionBean();
		connectionsBean.setHostName(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PYR_IOB_TM_SFTP_HOST_STEP));
		connectionsBean.setUserId(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PYR_IOB_TM_SFTP_USERNAME_STEP));
		connectionsBean.setPassword(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PYR_IOB_TM_SFTP_PASSWORD_STEP));
		connectionsBean.setDestinationUrl(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PYR_IOB_TM_SFTP_UPLOAD_TARGET_DIR_STEP));
		connectionsBean.setOriginatedURL(IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PYR_IOB_TM_SFTP_DOWNLOAD_TARGET_DIR_STEP)));
		return connectionsBean;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {

		System.out.println("PyramidIOBTemplate job start....before job()");
		JobParameters jobParameters = jobExecution.getJobParameters();

		String fileToDownload = jobParameters.getString("pyrIOBFeedProcessExcelName");

		FTPConnectionBean ftpConnectionBean = getSFTPDownloadDetails();
		SFTPManager sftpManager = new SFTPManager(ftpConnectionBean);
		sftpManager.setPublishId(RunSchedulerItemOnbrdTm.PUBLISH_ID);
		boolean sftpStatus = sftpManager.downloadFile(fileToDownload);
		if (!sftpStatus) {
			String msg = "Cannot download IOBStaticTemplate.xlsm, because it does not exist";
			ehfLogger.error(msg);
			IntgSrvUtils.alertByEmail(new Exception(msg), clazzname, RunSchedulerItemOnbrdTm.PUBLISH_ID);
		}
		IntgSrvUtils.printConsole("IOB Templete uploaded status: " + sftpStatus);

	}

}
