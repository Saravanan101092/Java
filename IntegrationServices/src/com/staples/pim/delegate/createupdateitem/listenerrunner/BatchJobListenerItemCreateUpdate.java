
package com.staples.pim.delegate.createupdateitem.listenerrunner;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.listenerandrunner.BatchJobListener;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;

public class BatchJobListenerItemCreateUpdate extends BatchJobListener {

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {

		traceLogger.info(clazzname, "beforeJob", "ENTER/.../EXIT: JobExecutionListener - initialization <- job params");
		IntgSrvUtils.printConsole("job start");
		try {
			init();
			Map<String, JobParameter> jpMap = jobExecution.getJobParameters().getParameters();
			String timestampSTEPExport = jpMap.get(IntgSrvAppConstants.JP_SETP_EXPORT_TIME).toString();
			String publishId = jpMap.get(IntgSrvAppConstants.JP_PUBLISH_ID).toString();
			String transactionType = jpMap.get(IntgSrvAppConstants.JP_TRANSACTION_TYPE_FILETOFILE).toString();
			String traceIdJobRunner = jpMap.get(IntgSrvAppConstants.JP_EHF_TRACEID_JOBRUNNER).toString();
			String traceIdTransform = jpMap.get(IntgSrvAppConstants.JP_EHF_TRACEID_TRANSFORM).toString();
			String inputFile = jobExecution.getJobParameters().getString(IntgSrvAppConstants.JP_INPUT_FILE).toString();

			// Additional info attributes need to use the explicit APIs of
			// ErrroHandlingFrameworkICD
			traceId = traceIdTransform;
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_KEY,
					IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_TRANSFORM);
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_STEP_EXPORT_TIME, timestampSTEPExport);
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_JOBRUNNER, traceIdJobRunner);
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_INPUT_FILENAME, inputFile);
			ehfICD.setAttributeTransactionType(transactionType);
			ehfICD.setAttributePublishId(RunSchedulerItemCreateUpdate.publishIds);

			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			String msgDesc = "SpringBatch JobExecutionListener - initialization <- job params";
			String infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
					IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
			ehfLogger.info(infoLogString);
		} catch (Throwable exception) {
			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			String errorLogString = ehfHandler.getErrorLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					exception, IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
			ehfLogger.error(errorLogString);
			traceLogger.error(clazzname, "beforeJob", ehfICD.toStringEHFExceptionStackTrace(exception));
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerItemCreateUpdate.publishIds);
		}
		// traceLogger.info(clazzname, "beforeJob", "EXIT");

	}

	@AfterJob
	public void afterJob(JobExecution jobExecution) {

		traceLogger.info(clazzname, "afterJob", "ENTER: JobExecutionListener");
		String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
		String codeModule = getClass().getName();
		String fNameArchive = null;
		String fNameBad = null;

		JobInstance jobInst = jobExecution.getJobInstance();
		String fName = jobExecution.getJobParameters().getString(IntgSrvAppConstants.INPUT_FILE);

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			fNameArchive = fName.replace(IntgSrvAppConstants.FILE_UNPROCESSED, IntgSrvAppConstants.FILE_DONE);
			IntgSrvUtils.printConsole("fName=" + fName);
			IntgSrvUtils.printConsole("fNameArchive=" + fNameArchive);
			traceLogger.info(clazzname, "afterJob", "BatchStatus.COMPLETED, archive file: " + fNameArchive);

			File f = new File(fName);
			IntgSrvUtils.printConsole("file name=" + f.getName());
			IntgSrvUtils.printConsole("file full path=" + f.getAbsolutePath());
			File fDest = new File(fNameArchive);

			try {
				IntgSrvUtils.copyFileUsingFileStreams(f, fDest);
				f.delete();
			} catch (IOException exception) {
				String errorLogString = ehfHandler.getErrorLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
						exception, IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
				ehfLogger.error(errorLogString);
				traceLogger.error(clazzname, "afterJob", ehfICD.toStringEHFExceptionStackTrace(exception));
				exception.printStackTrace();
				IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerItemCreateUpdate.publishIds);
			}

			// IntgSrvUtils.printConsole("file moved to archive directory!");
			// Notifying when job successfully ends

		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
			// Notifying when job ends with failure

			fNameBad = fName.replace(IntgSrvAppConstants.FILE_UNPROCESSED, IntgSrvAppConstants.FILE_BAD);
			traceLogger.info(clazzname, "afterJob", "BatchStatus.FAILED, bad file: " + fNameBad);

			File f = new File(fName);
			IntgSrvUtils.printConsole("file name=" + f.getName());
			IntgSrvUtils.printConsole("file full path=" + f.getAbsolutePath());
			File fDest = new File(fNameBad);

			try {
				IntgSrvUtils.copyFileUsingFileStreams(f, fDest);
				f.delete();
			} catch (IOException exception) {
				String errorLogString = ehfHandler.getErrorLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
						exception, IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
				ehfLogger.error(errorLogString);
				traceLogger.error(clazzname, "afterJob", ehfICD.toStringEHFExceptionStackTrace(exception));
				exception.printStackTrace();
				IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerItemCreateUpdate.publishIds);
			}
			IntgSrvUtils.printConsole("job fail");
			String msg = "Batch Status Failed - Feed file moved to " + fDest;
			IntgSrvUtils.alertByEmail(new Exception(msg), clazzname, RunSchedulerItemCreateUpdate.publishIds);
		}
		ehfICD.setAttributePublishId(RunSchedulerItemCreateUpdate.publishIds);
		String msgDesc = "Integration Services JobExecutionListner EXIT ==>> JOB_EXECUTION_EXIT_STATUS={" + jobExecution.toString() + "}";
		String infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
				ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
				IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
		ehfLogger.info(infoLogString);
		traceLogger.info(clazzname, "afterJob", msgDesc);

	}
}
