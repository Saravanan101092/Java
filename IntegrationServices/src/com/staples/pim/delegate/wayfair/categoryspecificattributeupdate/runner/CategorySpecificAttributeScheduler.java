
package com.staples.pim.delegate.wayfair.categoryspecificattributeupdate.runner;

import java.util.Date;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wayfair.categoryspecificattributeupdate.CategorySpecificAttributeInboundProcessor;

public class CategorySpecificAttributeScheduler extends RunScheduler {

	public static final String	FREEFORM_TRACELOGGER_ATTRIBUTEFEED	= "tracelogger.wayfairattributefeed";

	static IntgSrvLogger		logger								= IntgSrvLogger.getInstance(FREEFORM_TRACELOGGER_ATTRIBUTEFEED);
	public static String		PUBLISH_ID							= "TLDE002";

	/**
	 * 
	 */
	public void run() {

		logger.info(new Date().toString() + " Category Specific Attributes scheduler invoked");
		DatamigrationCommonUtil.printConsole("Category Specific Attributes scheduler invoked");
		CategorySpecificAttributeInboundProcessor catSpecAttrProcessor = new CategorySpecificAttributeInboundProcessor();
		catSpecAttrProcessor.attributeInboundProcessor();
		logger.info("CategorySpecific attribute files processing complete");
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}
