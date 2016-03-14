
package com.staples.pim.delegate.merchupdate.runner;

import static com.staples.pim.base.util.IntgSrvAppConstants.FREEFORM_TRACE_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.MERCHANDISING_INPUT_FOLDER;

import java.io.File;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.merchupdate.processor.MerchInboundProcessor;

public class MerchScheduler extends RunScheduler {

	IntgSrvLogger			logger		= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER);
	public static String	PUBLISH_ID	= "GALE102";

	/**
	 * 
	 */
	public void run() {

		DatamigrationCommonUtil.printConsole("merch scheduler invoked");
		logger.info("merchandising hierarchy scheduler invoked");
		String inputFolder = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(MERCHANDISING_INPUT_FOLDER));
		DatamigrationCommonUtil.printConsole("inputFolder -----> " + inputFolder);
		File folder = new File(inputFolder);
		File[] sortedFiles = DatamigrationCommonUtil.sortFilesBasedOnFIFO(folder, MerchScheduler.PUBLISH_ID);
		if (sortedFiles != null && sortedFiles.length > 0) {

			for (int i = 0; i < sortedFiles.length; i++) {
				if (sortedFiles[i].getName().endsWith(".xsv")) {
					if (sortedFiles[i].length() > 0) {
						DatamigrationCommonUtil.printConsole(sortedFiles[i].getName());
						logger.info("File being processed : " + sortedFiles[i].getName());
						try {
							MerchInboundProcessor.merchHierarchyProcessor(sortedFiles[i]);
						} catch (Exception e) {
							logger.error(e);
							e.printStackTrace();
							IntgSrvUtils.alertByEmail(e, clazzname, MerchScheduler.PUBLISH_ID);
						}
					} else {
						DatamigrationCommonUtil.moveFileToFileBad(sortedFiles[i], MerchScheduler.PUBLISH_ID);
						logger.info("Bad file. Moved to File_Bad folder");
					}
				} else {
					DatamigrationCommonUtil.printConsole("Invalid file format");
					logger.info("Invalid file format");
				}
			}
		} else {
			DatamigrationCommonUtil.printConsole("sortedFiles is empty or null");
			logger.info("sortedFiles is empty or null");
		}
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}