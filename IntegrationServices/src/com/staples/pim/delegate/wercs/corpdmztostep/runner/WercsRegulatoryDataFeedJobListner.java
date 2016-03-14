
package com.staples.pim.delegate.wercs.corpdmztostep.runner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

import com.staples.pim.base.common.listenerandrunner.BatchJobListener;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;
import com.staples.pim.delegate.wercs.common.WercsCommonUtil;
import com.staples.pim.delegate.wercs.corpdmztostep.processor.CorpdmzToStepMSDSProcessor;
import com.staples.pim.base.util.IntgSrvUtils;

public class WercsRegulatoryDataFeedJobListner extends BatchJobListener {

	@AfterJob
	public void afterJob(JobExecution jobExecution) {

		JobParameters jobParameters = jobExecution.getJobParameters();

		String wercsregulatorydataFeedDoneDir = jobParameters.getString(RunSchedulerCorpdmzToStep.WERCSREGULATORYDATA_DONE_FOLDER);
		String wercsregulatorydataFeedBadDir = jobParameters.getString(RunSchedulerCorpdmzToStep.WERCSREGULATORYDATA_BAD_FOLDER);

		String inputFile = jobParameters.getString(WercsAppConstants.JP_INPUTCORPDMZTOSTEP_FILE);
		String inputFileNameAndRecordCount = WercsCommonUtil.getInstance().getFileName(inputFile)
				+ WercsCommonUtil.getInstance().getFileLineCount(inputFile);
		String outputFile = jobParameters.getString(WercsAppConstants.JP_OUTPUTCORPDMZTOSTEP_FILE_STEP_XML);
		String errorReportFile = jobParameters.getString(RunSchedulerCorpdmzToStep.CORPDMZTOSTEP_FEED_REPORT_FILE);

		List<String> outputFileList = getOutputFiles(jobExecution, outputFile);
		File inputfile = new File(inputFile);
		if (BatchStatus.COMPLETED.equals(jobExecution.getStatus())) {
			traceLogger.info(clazzname, "afterJob", "job COMPLETED");
			for (String OutputFile : outputFileList) {
				File outputfile = new File(OutputFile);

				/**
				 * File Transfer 1. Output STEP xml transfer to STEP Inbound
				 * directory 2. Source feed moving to FINE_DONE Directory
				 */
				DatamigrationCommonUtil.sendFile(outputfile, inputfile, wercsregulatorydataFeedDoneDir, "CorpDMZtoSTEP", true,
						WercsAppConstants.EHF_PUBLISH_ID_WERCSCORPDMZTOSTEP);

				traceLogger.info(clazzname, "afterJob", "Output STEP XML moved to :" + wercsregulatorydataFeedDoneDir);
			}
		}
		/*
		 * If Job failed then Input file moving to Bad directory
		 */
		else if (BatchStatus.FAILED.equals(jobExecution.getStatus())) {
			/**
			 * File Transfer 1. Source feed moving to FILE_BAD Directory
			 */

			try {
				moveToDestnationDir(inputFile, wercsregulatorydataFeedBadDir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			traceLogger.info(clazzname, "afterJob", "Output STEP XML moved to :" + wercsregulatorydataFeedBadDir);
			String msg = "Batch Status Failed - Feed file (" + inputFile + ") moved to " + wercsregulatorydataFeedBadDir + " directory";
			traceLogger.error(clazzname, "afterJob", msg);
			ehfLogger.error(clazzname, "afterJob", msg);
			RunSchedulerCorpdmzToStep.logger.error(msg);
			IntgSrvUtils.alertByEmail(new Exception(msg), clazzname, RunSchedulerCorpdmzToStep.PUBLISH_ID);

		}

		WercsCommonUtil.getInstance().sendSummaryEmailReport("WERCSCoprdmztoSTEP", inputFileNameAndRecordCount, outputFileList,
				errorReportFile, jobExecution.getStatus());
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
			if (entry.getKey().contains(RunSchedulerCorpdmzToStep.REGULATORYDATAFEED_FILE_BEGIN) && entry.getKey().contains(outputFile)
					&& entry.getKey().endsWith(".xml")) {
				outputFileList.add(String.valueOf(entry.getValue()));
			}
		}
		return WercsCommonUtil.getInstance().fileSort(outputFileList);
	}

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {

		traceLogger.info(clazzname, "beforeJob", "ENTER/.../EXIT: RunSchedulerCorpdmzToStep - initialization <- job params");
	}

	public void moveToDestnationDir(String outputFileString, String targetDirString) throws IOException {

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

	public void moveMSDSToDestnationDir(File outputFileString, String targetDirString) {

		if (outputFileString.getName().contains(CorpdmzToStepMSDSProcessor.PREFIX_FOR_MSDSDOC)) {
			File file = new File(targetDirString + outputFileString.getName());
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (outputFileString.exists()) {
				try {
					Files.copy(outputFileString.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
					System.gc();
					outputFileString.delete();
				} catch (IOException e) {
					e.printStackTrace();
					IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), RunSchedulerCorpdmzToStep.PUBLISH_ID);
				}
			}
		} else {
			File file = new File(targetDirString + CorpdmzToStepMSDSProcessor.PREFIX_FOR_MSDSDOC + "-" + outputFileString.getName());
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (outputFileString.exists()) {
				try {
					Files.copy(outputFileString.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
					System.gc();
					outputFileString.delete();
				} catch (IOException e) {
					e.printStackTrace();
					IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), RunSchedulerCorpdmzToStep.PUBLISH_ID);
				}

			}
		}

	}
}
