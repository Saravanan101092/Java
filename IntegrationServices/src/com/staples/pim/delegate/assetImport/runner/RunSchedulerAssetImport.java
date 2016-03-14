
package com.staples.pim.delegate.assetImport.runner;

import org.springframework.stereotype.Component;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.assetImport.processor.AssetImportProcessor;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;

@Component
public class RunSchedulerAssetImport extends RunScheduler {

	public static IntgSrvLogger	itemTraceLogger	= IntgSrvLogger.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_ASSET_IMPORT);

	private static String		infoLogString;
	public static String		PUBLISH_ID		= "ASSET_IMPORT";

	public void run() {

		traceLogger.info(clazzname, "run", "ENTER: RunSchedulerAssetImport runner");
		try {

			String msgDesc = "RunSchedulerAssetImport job runner starts... ";
			traceLogger.info(clazzname, "run", msgDesc);
			AssetImportProcessor supplierSetupProcessor = new AssetImportProcessor();
			supplierSetupProcessor.processFiles();
			msgDesc = "RunSchedulerSupplierSetup completed META FILE creation and moved the files to Inbound directory..";
			traceLogger.info(clazzname, "run", msgDesc);

		} catch (Exception exception) {
			traceLogger.error(clazzname, "MY_RUN", "Exception: " + exception);
			infoLogString = "Exception" + exception;
			ehfLogger.error(infoLogString);
			itemTraceLogger.error(infoLogString);
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerAssetImport.PUBLISH_ID);
		}
		traceLogger.info(clazzname, "run", "EXIT: WAIT FOR NEXT RUN...");
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}