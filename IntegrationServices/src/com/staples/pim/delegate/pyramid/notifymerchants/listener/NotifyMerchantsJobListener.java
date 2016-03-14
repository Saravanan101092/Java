package com.staples.pim.delegate.pyramid.notifymerchants.listener;

import java.io.File;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;

import com.staples.pim.base.common.listenerandrunner.BatchJobListener;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.pyramid.notifymerchants.runner.RunSchedulerNotifyMerchants;

public class NotifyMerchantsJobListener extends BatchJobListener {

	@Override
	public void afterJob(JobExecution jobExecution) {

		// TODO - 3. Output IOB TEMPLATE needs to maintained in SB Server for
		// tracking purpose

		System.out.println("NotifyMerchants job start....after job()");

		JobParameters jobParameters = jobExecution.getJobParameters();

		/*
		 * String pyrIOBFeedProcessDir =
		 * jobParameters.getString("pyrIOBFeedProcessDir"); String outputFile =
		 * jobParameters.getString("pyrIOBFeedProcessExcelName"); String
		 * outputFileAbsPath = pyrIOBFeedProcessDir + outputFile;
		 */

		try {

		String tempDirectory= "C:/opt/stibo/integration/hotfolder/PyrNotifyMerchantsIncoming/File_Temp/";
		 String outputDirectory= "C:/opt/stibo/integration/hotfolder/PyrNotifyMerchantsIncoming/File_Done/";
			 if (BatchStatus.COMPLETED.equals(jobExecution.getStatus())) {
		 File dir = new File(tempDirectory); 
		 File[] files = dir.listFiles(); if (files == null ){
			 System.out.println("File list is null!"); traceLogger.info(clazzname, "run",	"No files found in Temp folder");
			 } else 
			 { 
				 System.out.println( "Number of files:"+files.length + " in directory:" +dir.getName()); 
				 int numfiles = files.length;
				 traceLogger.info(clazzname, "run", "Number of files: " +  numfiles); 
				 for (int i = 0; i < files.length; i++) { 
					 if (files[i].isFile()){
						 String	 destFileName=outputDirectory+files[i].getName();
						 File
			  destFile=new File (destFileName); files[i].renameTo(destFile); }}
			 } }
			
			 else
			 if (BatchStatus.FAILED.equals(jobExecution.getStatus())) {
				String msg = "Batch Status Failed - Notify Merchants";
				ehfLogger.error(msg);
				IntgSrvUtils.alertByEmail(new Exception(msg), clazzname, RunSchedulerNotifyMerchants.PUBLISH_ID);
			}

		} catch (Exception exception) {
			traceLogger.error(clazzname, "MY_RUN", "Exception: " + exception);
			String infoLogString = "Exception" + exception;
			ehfLogger.error(infoLogString);
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerNotifyMerchants.PUBLISH_ID);
		}
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {

		System.out.println("NotifyMerchants job start....before job()");
		JobParameters jobParameters = jobExecution.getJobParameters();
		String inputDirectory = "C:/opt/stibo/integration/hotfolder/PyrNotifyMerchantsIncoming/File_Unprocessed/";
		// String inputDirectory =
		// jobParameters.getString("NotifyMerchantsUnprocessedDir");
		String tempDirectory = "C:/opt/stibo/integration/hotfolder/PyrNotifyMerchantsIncoming/File_Temp/";
		// String inputDirectory =
		// jobParameters.getString("NotifyMerchantsTempDir");

		File dir = new File(inputDirectory);
		File[] files = dir.listFiles();
		if (files == null) {
			System.out.println("File list is null!");
			traceLogger.info(clazzname, "run", "No input file found in before job ... ");
		} else {
			System.out.println("Number of files:" + files.length + " in directory:" + dir.getName() + "..before job..");
			int numfiles = files.length;
			traceLogger.info(clazzname, "run", "Number of input files: " + numfiles);
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					String destFileName = tempDirectory + files[i].getName();
					File destFile = new File(destFileName);
					files[i].renameTo(destFile);
				}
			}
		}
	}
}