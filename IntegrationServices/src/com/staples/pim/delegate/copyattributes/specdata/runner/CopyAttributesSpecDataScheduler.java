package com.staples.pim.delegate.copyattributes.specdata.runner;

import java.util.Date;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.copyattributes.specdata.processor.CopyAttributesSpecDataProcessor;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;


/**
 * @author 843868
 *
 */
public class CopyAttributesSpecDataScheduler extends RunScheduler{

	public static final String	FREEFORM_TRACELOGGER_COPYATTRIBUTES_SPECDATA	= "tracelogger.copyattributes.specdata";

	static IntgSrvLogger		logger							= IntgSrvLogger.getInstance(FREEFORM_TRACELOGGER_COPYATTRIBUTES_SPECDATA);
	public static String		PUBLISH_ID						= "OFNE001";

	/**
	 * 
	 */
	public void run() {

		logger.info(new Date().toString() + " CopyAttributes SpecData Scheduler invoked.");
		DatamigrationCommonUtil.printConsole("CopyAttributes SpecData Scheduler invoked.");
	
		CopyAttributesSpecDataProcessor specdataProcessor = new CopyAttributesSpecDataProcessor();
		specdataProcessor.processCopyAttributesSpecdata();
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}