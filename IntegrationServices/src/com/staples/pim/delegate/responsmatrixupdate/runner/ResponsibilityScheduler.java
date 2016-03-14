
package com.staples.pim.delegate.responsmatrixupdate.runner;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.RESPONSIBILITY_MATRIX_INPUT_FOLDER;

import java.io.File;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.responsmatrixupdate.processor.ResponsibilityInboundProcessor;

public class ResponsibilityScheduler extends RunScheduler {

	IntgSrvLogger			logger		= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER);
	public static String	PUBLISH_ID	= "GALE103";

	public void run() {

		logger.info("Responsibility matrix scheduler invoked");
		DatamigrationCommonUtil.printConsole("Responsibility matrix scheduler invoked");
		String inputFolder = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(RESPONSIBILITY_MATRIX_INPUT_FOLDER));
		File folder = new File(inputFolder);
		File[] sortedFiles = DatamigrationCommonUtil.sortFilesBasedOnFIFO(folder, ResponsibilityScheduler.PUBLISH_ID);
		if (sortedFiles != null && sortedFiles.length > 0) {
			for (int i = 0; i < sortedFiles.length; i++) {
				if (sortedFiles[i].getName().endsWith(".xsv")) {
					logger.info("File being processed : " + sortedFiles[i].getName());
					DatamigrationCommonUtil.printConsole(sortedFiles[i]);
					if (sortedFiles[i].length() > 0) {
						ResponsibilityInboundProcessor.responsibilityMatrixProcessor(sortedFiles[i]);
					} else {
						DatamigrationCommonUtil.moveFileToFileBad(sortedFiles[i], ResponsibilityScheduler.PUBLISH_ID);
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