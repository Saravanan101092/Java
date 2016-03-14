
package com.staples.pim.delegate.suppliersetup.runner;

import org.springframework.stereotype.Component;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.suppliersetup.processor.SupplierSetupProcessor;

@Component
public class RunSchedulerSupplierSetup extends RunScheduler {

	public void run() {

		traceLogger.info(clazzname, "run", "ENTER: RunSchedulerSupplierSetup runner");
		try {

			String tmpMetaFolder = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.TMP_META_FOLDER));
			createWriterOutputDir(tmpMetaFolder);

			String msgDesc = "RunSchedulerSupplierSetup job runner starts... ";
			traceLogger.info(clazzname, "run", msgDesc);
			SupplierSetupProcessor supplierSetupProcessor = new SupplierSetupProcessor();
			supplierSetupProcessor.processFiles();
			msgDesc = "RunSchedulerSupplierSetup completed META FILE creation and moved the files to Inbound directory..";
			traceLogger.info(clazzname, "run", msgDesc);

		} catch (Throwable exception) {
			traceLogger.error(clazzname, "MY_RUN", "Exception: " + exception);
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, SupplierSetupProcessor.PUBLISH_ID);
		}
		traceLogger.info(clazzname, "run", "EXIT: WAIT FOR NEXT RUN...");
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String args[]) {

		RunSchedulerSupplierSetup runSchedulerSupplierSetup = new RunSchedulerSupplierSetup();
		runSchedulerSupplierSetup.run();
	}
}