package com.staples.pim.delegate.copyattributes.attributes.runner;


import java.util.Date;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.copyattributes.attributes.processor.CopyAttributesProcessor;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;


/**
 * @author 843868
 *
 */
public class CopyAttributesScheduler extends RunScheduler{

	public static final String	FREEFORM_TRACELOGGER_COPYATTRIBUTES_ATTRIBUTE	= "tracelogger.copyattributes.attributes";

	static IntgSrvLogger		logger							= IntgSrvLogger.getInstance(FREEFORM_TRACELOGGER_COPYATTRIBUTES_ATTRIBUTE);
	public static String		PUBLISH_ID						= "OFNE001";

	/**
	 * 
	 */
	public void run() {

		logger.info(new Date().toString() + " CopyAttributes Attribute Scheduler invoked.");
		DatamigrationCommonUtil.printConsole("CopyAttributes Attribute Scheduler invoked.");
		CopyAttributesProcessor copyAttributeProcessor = new CopyAttributesProcessor();
		copyAttributeProcessor.processCopyAttributes();
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}