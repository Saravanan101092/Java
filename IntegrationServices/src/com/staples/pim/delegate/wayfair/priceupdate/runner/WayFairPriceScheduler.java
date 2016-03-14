
package com.staples.pim.delegate.wayfair.priceupdate.runner;

import static com.staples.pim.base.util.IntgSrvAppConstants.FREEFORM_TRACE_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WAYFAIRPRICE_INPUT_FOLDER;

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
import com.staples.pim.delegate.wayfair.priceupdate.WayFairPriceInboundProcessor;

public class WayFairPriceScheduler extends RunScheduler {

	IntgSrvLogger			logger		= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER);
	public static String	PUBLISH_ID	= "TLDE003";
	public static String	XSV			= ".xsv";
	public static String	DSV			= ".dsv";

	/**	
	 * 
	 */
	public void run() {

		DatamigrationCommonUtil.printConsole("wayfairPrice scheduler invoked");
		logger.info("wayfairPricing delta scheduler invoked");
		String inputFolder = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(WAYFAIRPRICE_INPUT_FOLDER));
		DatamigrationCommonUtil.printConsole("InputFolder Path : " + inputFolder);
		File folder = new File(inputFolder);
		File[] sortedFiles = DatamigrationCommonUtil.sortFilesBasedOnFIFO(folder, WayFairPriceScheduler.PUBLISH_ID);

		List<File> fileList = new ArrayList<File>();
		fileList = Arrays.asList(sortedFiles);

		for (File inputfile : fileList) {
			logger.info(new Date().toString() + " Price Processing file : " + inputfile.getName());
			DatamigrationCommonUtil.printConsole("Price  >>Processing File : "+inputfile.getName());
			if (inputfile.getName().endsWith(XSV) || inputfile.getName().endsWith(DSV)) {
				DatamigrationCommonUtil.printConsole(inputfile.getName());
				logger.info("File being processed : " + inputfile.getName());

				try {
					WayFairPriceInboundProcessor.wayfairPriceProcessor(inputfile);
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
					IntgSrvUtils.alertByEmail(e, clazzname, WayFairPriceScheduler.PUBLISH_ID);
				}
			} else {
				DatamigrationCommonUtil.printConsole("Invalid file format");
			}
			//DatamigrationCommonUtil.moveFileToFileBad(inputfile, WayFairPriceScheduler.PUBLISH_ID);
			//logger.info("Bad file. Moved to File_Bad folder");
		}
	}

	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}
