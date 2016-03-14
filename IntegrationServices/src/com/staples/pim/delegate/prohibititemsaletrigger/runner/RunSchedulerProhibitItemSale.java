
package com.staples.pim.delegate.prohibititemsaletrigger.runner;

import org.springframework.stereotype.Component;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.prohibititemsaletrigger.processor.ProhibitItemSaleTriggerProcessor;

@Component
public class RunSchedulerProhibitItemSale extends RunScheduler {

	public void run() {

		traceLogger.info(clazzname, "run", "ENTER: RunSchedulerProhibitItemSaleTrigger runner");
		try {

			String tmpFolder = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PROHIBIT_ITEM_SALE_TRIGGER_INBOUND));
			createWriterOutputDir(tmpFolder);

			String msgDesc = "RunSchedulerProhibitItemSaleTrigger job runner starts... ";
			traceLogger.info(clazzname, "run", msgDesc);
			ProhibitItemSaleTriggerProcessor prohibitItemSaleTriggerProcessor = new ProhibitItemSaleTriggerProcessor();
			prohibitItemSaleTriggerProcessor.processFiles();
			msgDesc = "RunSchedulerProhibitItemSaleTrigger completed moving the file to Inbound directory..";
			traceLogger.info(clazzname, "run", msgDesc);

		} catch (Throwable exception) {
			traceLogger.error(clazzname, "MY_RUN", "Exception: " + exception);
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, ProhibitItemSaleTriggerProcessor.PUBLISH_ID);
		}
		traceLogger.info(clazzname, "run", "EXIT: WAIT FOR NEXT RUN...");
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String args[]) {

		RunSchedulerProhibitItemSale runSchedulerProhibitItemSaleTrigger = new RunSchedulerProhibitItemSale();
		runSchedulerProhibitItemSaleTrigger.run();
	}
}