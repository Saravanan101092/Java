
package com.staples.pim.delegate.productupdate.runner;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.productupdate.processor.ProductInboundProcessor;

/**
 *  
 *
 */
public class ProductInboundScheduler extends RunScheduler {

	IntgSrvLogger			logger		= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER);
	public static String	PUBLISH_ID	= "PIME101";

	/**
	 * 
	 */
	public void run() {

		logger.info("Product Onetime scheduler invoked");
		DatamigrationCommonUtil.printConsole("Product Onetime scheduler invoked");
		ProductInboundProcessor oneTimeProcessor = new ProductInboundProcessor();
		oneTimeProcessor.productInboundProcessor();
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}

}
