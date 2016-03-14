
package com.staples.pim.delegate.wayfair.productupdate.runner;

import java.util.Date;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wayfair.productupdate.WayfairOTBulletProcessor;
import com.staples.pim.delegate.wayfair.productupdate.WayfairOTHierarchyProcessor;
import com.staples.pim.delegate.wayfair.productupdate.WayfairProductProcessor;

public class WayfairProductScheduler extends RunScheduler {

	public static final String	FREEFORM_TRACELOGGER_PRODUCT	= "tracelogger.wayfairproduct";

	static IntgSrvLogger		logger							= IntgSrvLogger.getInstance(FREEFORM_TRACELOGGER_PRODUCT);
	public static String		PUBLISH_ID						= "TLDE001";

	/**
	 * 
	 */
	public void run() {

		logger.info(new Date().toString() + " Product scheduler invoked");
		DatamigrationCommonUtil.printConsole("Product scheduler invoked");
		new WayfairProductProcessor().wayfairProductInboundProcessor();
		new WayfairOTBulletProcessor().wayfairProductInboundProcessor();
		new WayfairOTHierarchyProcessor().oneTimeQuillHierarchyProcess();
		logger.info("Wayfair Product Files processed");
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}
