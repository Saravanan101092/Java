
package com.staples.pim.delegate.wayfair.attributeupdate.runner;

import java.util.Date;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wayfair.attributeupdate.AttributeProcessor;

public class AttributeScheduler extends RunScheduler {

	public static final String	FREEFORM_TRACELOGGER_ATTRIBUTEMETADATA	= "tracelogger.wayfairattributemetadata";

	static IntgSrvLogger		logger									= IntgSrvLogger.getInstance(FREEFORM_TRACELOGGER_ATTRIBUTEMETADATA);

	public static String		PUBLISH_ID								= "TLDE006";

	/**
	 * 
	 */
	public void run() {

		logger.info(new Date().toString() + " Attribute scheduler invoked");
		DatamigrationCommonUtil.printConsole("Attribute scheduler invoked");
		AttributeProcessor attributeProcessor = new AttributeProcessor();
		attributeProcessor.attributeInboundProcessor();
		logger.info("Attribute metadata processing complete.");
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}
