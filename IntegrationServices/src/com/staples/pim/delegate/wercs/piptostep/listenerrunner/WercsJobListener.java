package com.staples.pim.delegate.wercs.piptostep.listenerrunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;

import com.staples.pim.base.common.bean.FTPConnectionBean;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.wercs.common.WercsCommonUtil;
import com.staples.pim.base.common.logging.IntgSrvLogger;


public class WercsJobListener implements JobExecutionListener {

	
	public static IntgSrvLogger traceLogger 	= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	protected String clazzname					= this.getClass().getName(); 
	protected String traceId 					= null;
	protected String publishIds 				= null;
	protected IntgSrvLogger ehfLogger 			= IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		
		System.out.println("afterjob()");
		traceLogger.info(clazzname, "afterJob", "Job process completed");
		
		JobParameters jobParameters = jobExecution.getJobParameters();

		String wercsPipStepDoneDir = jobParameters.getString(RunSchedulerPIPToStep.WERCS_PIP_STEP_DONE_DIR);
		String wercsPipStepBadDir = jobParameters.getString(RunSchedulerPIPToStep.WERCS_PIP_STEP_BAD_DIR);

		String inputFile = jobParameters.getString(IntgSrvAppConstants.JP_INPUT_FILE);
		String inputFileNameAndRecordCount = WercsCommonUtil.getInstance().getFileName(inputFile) + WercsCommonUtil.getInstance().getFileLineCount(inputFile);
		String outputFile = jobParameters.getString(IntgSrvAppConstants.JP_OUTPUT_FILE_STEP_XML);
		String errorReportFile = jobParameters.getString(RunSchedulerPIPToStep.WERCS_PIP_STEP_REPORT_FILE);

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
				WercsCommonUtil.getInstance().fileSFTPToStepInboundDir(ftpConnectionBean, outputFileList, RunSchedulerPIPToStep.PUBLISH_ID);
				moveToDestnationDir(inputFile, wercsPipStepDoneDir);
				traceLogger.info(clazzname, "afterJob", "Output STEP XML moved to :" + wercsPipStepDoneDir);
			}
			/*
			 * If Job failed then Input file moving to Bad directory
			 */
			 
			else if (BatchStatus.FAILED.equals(jobExecution.getStatus())) {
				/**
				 * File Transfer 1. Source feed moving to FILE_BAD Directory
				 */
				 
				moveToDestnationDir(inputFile, wercsPipStepBadDir);
				traceLogger.info(clazzname, "afterJob", "Output STEP XML moved to :" + wercsPipStepBadDir);
				String msg = "Batch Status Failed - Feed file (" + inputFile + ") moved to " + wercsPipStepBadDir + " directory";
				traceLogger.error(clazzname, "afterJob", msg);
				ehfLogger.error(clazzname, "afterJob", msg);
				RunSchedulerPIPToStep.itemTraceLogger.error(msg);
				IntgSrvUtils.alertByEmail(new Exception(msg), clazzname, RunSchedulerPIPToStep.PUBLISH_ID);
			}

			WercsCommonUtil.getInstance().sendSummaryEmailReport("WERCS_PIPToSTEP", inputFileNameAndRecordCount, outputFileList, errorReportFile,jobExecution.getStatus());

		} catch (IOException exception) {
			traceLogger.error(clazzname, "afterJob", exception);
			ehfLogger.error(clazzname, "afterJob", exception);
			RunSchedulerPIPToStep.itemTraceLogger.error(exception.getMessage());
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerPIPToStep.PUBLISH_ID);
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
			if (entry.getKey().contains(RunSchedulerPIPToStep.FEED_FILE_BEGIN) && entry.getKey().contains(outputFile) && entry.getKey().endsWith(".xml")) {
				outputFileList.add(String.valueOf(entry.getValue()));
			}
		}
		return WercsCommonUtil.getInstance().fileSort(outputFileList);
	}


	@Override
	public void beforeJob(JobExecution jobExecution) {
		traceLogger.info(clazzname, "beforeJob", "ENTER/.../EXIT: RunSchedulerPIPToStep - initialization <- job params");
		
	}
	
	/**
	 * Move the files to done or bad folder and rename it
	 */
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
	
	/**
	 * Get SFTP details for file transferring
	 */
	
	private FTPConnectionBean getSFTPDestDetails() {

		FTPConnectionBean connectionsBean = new FTPConnectionBean();
		connectionsBean.setHostName(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SFTP_HOST_STEP));
		connectionsBean.setUserId(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SFTP_USERNAME_STEP));
		connectionsBean.setPassword(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SFTP_PASSWORD_STEP));
		connectionsBean.setDestinationUrl(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.WERCS_PIP_STEP_SFTP_TARGET_DIR_STEP));
		return connectionsBean;
	}

}
