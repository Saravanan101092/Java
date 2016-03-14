package com.staples.pim.delegate.copyattributes.taxonomy.runner;

import java.util.Date;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.copyattributes.taxonomy.processor.CopyAttributesTaxonomyProcessor;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;


/**
 * @author 843868
 *
 */
public class CopyAttributesTaxonomyScheduler extends RunScheduler{

	public static final String	FREEFORM_TRACELOGGER_COPYATTRIBUTES_TAXONOMY	= "tracelogger.copyattributes.taxonomy";

	static IntgSrvLogger		logger							= IntgSrvLogger.getInstance(FREEFORM_TRACELOGGER_COPYATTRIBUTES_TAXONOMY);
	public static String		PUBLISH_ID						= "OFNE001";

	/**
	 * 
	 */
	public void run() {

		logger.info(new Date().toString() + " CopyAttributes Taxonomy Scheduler invoked.");
		DatamigrationCommonUtil.printConsole("CopyAttributes Taxonomy Scheduler invoked.");
		CopyAttributesTaxonomyProcessor taxonomyProcessor = new CopyAttributesTaxonomyProcessor();
		taxonomyProcessor.processCopyAttributesTaxonomy();
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}
