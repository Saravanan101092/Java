
package com.staples.pim.delegate.wayfair.activeskuupdate.runner;

import java.util.Date;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wayfair.activeskuupdate.ActiveSKUProcessor;

public class ActiveSkuScheduler extends RunScheduler {

	public static final String	FREEFORM_TRACELOGGER_ACTIVESKU	= "tracelogger.wayfairactivesku";

	static IntgSrvLogger		logger							= IntgSrvLogger.getInstance(FREEFORM_TRACELOGGER_ACTIVESKU);
	public static String		PUBLISH_ID						= "TLDE007";

	/**
	 * 
	 */
	public void run() {

		logger.info(new Date().toString() + " ActiveSKU scheduler invoked");
		DatamigrationCommonUtil.printConsole("ActiveSKU scheduler invoked");
		ActiveSKUProcessor activeSKUProcessor = new ActiveSKUProcessor();
		activeSKUProcessor.wayfairActiveSKUProcessor();
		logger.info("Active sku file processing complete.");
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}
