
package com.staples.pim.delegate.wayfair.imageupdate.runner;

import static com.staples.pim.base.util.IntgSrvAppConstants.FREEFORM_TRACE_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WAYFAIRIMAGE_INPUT_FOLDER;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wayfair.imageupdate.WayfairimageInboundProcessor;

public class WayfairimageScheduler extends RunScheduler {

	IntgSrvLogger			logger		= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER);
	public static String	PUBLISH_ID	= "TLDE004";
	public static String	XSV			= ".xsv";
	public static String	DSV			= ".dsv";

	/**	
	 * 
	 */
	public void run() {

		DatamigrationCommonUtil.printConsole("wayfairimageupdate scheduler invoked");
		logger.info("wayfairimage scheduler invoked");
		String inputFolder = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(WAYFAIRIMAGE_INPUT_FOLDER));
		DatamigrationCommonUtil.printConsole("inputFolder : " + inputFolder);
		File folder = new File(inputFolder);
		File[] sortedFiles = DatamigrationCommonUtil.sortFilesBasedOnFIFO(folder, WayfairimageScheduler.PUBLISH_ID);

		List<File> fileList = new ArrayList<File>();
		fileList = Arrays.asList(sortedFiles);

		for (File inputfile : fileList) {
			logger.info(new Date().toString() + " Image Ref Processing file : " + inputfile.getName());
			DatamigrationCommonUtil.printConsole("Image Ref  >>Processing File : "+inputfile.getName());
			if (inputfile.getName().endsWith(XSV) || inputfile.getName().endsWith(DSV)) {
				DatamigrationCommonUtil.printConsole(inputfile.getName());
				logger.info("Image... File being processed : " + inputfile.getName());
				DatamigrationCommonUtil.printConsole("Image... File being processed : " + inputfile.getName());

				try {
					WayfairimageInboundProcessor.wayfairimageupdateProcessor(inputfile);
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
					IntgSrvUtils.alertByEmail(e, clazzname, WayfairimageScheduler.PUBLISH_ID);
				}
			} else {
				DatamigrationCommonUtil.printConsole("Invalid file format");
				//DatamigrationCommonUtil.moveFileToFileBad(inputfile, WayfairimageScheduler.PUBLISH_ID);
				//System.out.println("Image ... Bad file. Moved to File_Bad folder");
			}
		}
	}

	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}
