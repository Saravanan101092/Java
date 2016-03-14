
package com.staples.pim.delegate.wayfair.taxonomyupdate.runner;

import java.util.Date;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wayfair.taxonomyupdate.TaxonomyProcessor;

public class TaxonomyScheduler extends RunScheduler {

	public static final String	FREEFORM_TRACELOGGER_TAXONOMY	= "tracelogger.wayfairtaxonomy";

	static IntgSrvLogger		logger							= IntgSrvLogger.getInstance(FREEFORM_TRACELOGGER_TAXONOMY);
	public static String		PUBLISH_ID						= "TLDE005";

	public void run() {

		DatamigrationCommonUtil.printConsole(new Date().toString() + " Taxonomy scheduler invoked");
		logger.info("Taxonomy scheduler invoked");
		TaxonomyProcessor taxonomyProcessor = new TaxonomyProcessor();
		taxonomyProcessor.wayfairTaxonomyInboundProcessor();
		logger.info("Taxonomy input files processing complete.");
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}
