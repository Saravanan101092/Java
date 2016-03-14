
package com.staples.pim.delegate.vendorupcupdate.runner;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.VENDOR_UPC_INPUT_FOLDER;

import java.io.File;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.vendorupcupdate.processor.UPCInboundProcessor;

public class UPCScheduler extends RunScheduler {

	IntgSrvLogger			logger		= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER);
	public static String	PUBLISH_ID	= "GALE104";

	public void run() {

		DatamigrationCommonUtil.printConsole("UPC scheduler invoked");
		logger.info("UPC scheduler invoked");
		String inputFolder = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(VENDOR_UPC_INPUT_FOLDER));
		File folder = new File(inputFolder);
		File[] sortedFiles = DatamigrationCommonUtil.sortFilesBasedOnFIFO(folder, UPCScheduler.PUBLISH_ID);
		if (sortedFiles != null && sortedFiles.length > 0) {
			for (int i = 0; i < sortedFiles.length; i++) {
				if (sortedFiles[i].getName().endsWith(".txt")) {
					if (sortedFiles[i].length() > 0) {
						logger.info("File being processed : " + sortedFiles[i].getName());
						DatamigrationCommonUtil.printConsole(sortedFiles[i]);
						UPCInboundProcessor.upcInboundProcessor(sortedFiles[i]);
					} else {
						DatamigrationCommonUtil.moveFileToFileBad(sortedFiles[i], UPCScheduler.PUBLISH_ID);
						logger.info("Bad file. Moved to File_Bad folder");
					}
				} else {
					DatamigrationCommonUtil.printConsole("Invalid file format");
					logger.info("Invalid file format");

				}
			}
		}
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}