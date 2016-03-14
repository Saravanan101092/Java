
package com.staples.pim.delegate.wercs.corpdmztostep.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.staples.pim.base.common.bean.FTPConnectionBean;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.common.SBDatabaseHandlingService;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;
import com.staples.pim.delegate.wercs.common.WercsCommonUtil;
import com.staples.pim.delegate.wercs.corpdmztostep.runner.RunSchedulerCorpdmzToStep;
import com.staples.pim.delegate.wercs.corpdmztostep.runner.WercsRegulatoryDataFeedJobListner;

public class CorpdmzToStepMSDSProcessor {

	public static IntgSrvLogger	logger							= IntgSrvLogger
																		.getInstance(WercsAppConstants.FREEFORM_TRACE_LOGGER_WERCS_CORPDMZTOSTEP);
	public static String		SKUValue						= null;
	public static String		URL								= "http://sds.staples.com/msds/";
	public static String		filename						= null;
	public static String		filename1						= null;
	public static String		UPCNo							= null;
	public static String		SKUNO							= null;
	public static boolean		success;
	public static String		wercsregulatorydataFeedDoneDir	= IntgSrvUtils
																		.reformatFilePath(IntgSrvPropertiesReader
																				.getProperty(RunSchedulerCorpdmzToStep.WERCSREGULATORYDATA_DONE_FOLDER));
	public static String		PREFIX_FOR_MSDSDOC				= "SKUNOTAVAILABLE";

	public static void MSDSCopdmzToStepMSDSProcessor(File input) throws IOException {

		logger.info("Processing MSDS Document files");
		File MSDSDocFiles = null;
		File MSDSDocFiles1 = null;
		File MSDSDocDir = new File(input.getParentFile().getPath());
		List<File> listOfMSDSDoc = new ArrayList<File>();
		listOfMSDSDoc.add(input);
		System.out.println("list of MSDSDoc Files" + listOfMSDSDoc);
		for (int i = 0; i < listOfMSDSDoc.size(); i++) {

			filename = listOfMSDSDoc.get(i).getName().split("\\_")[1];
			UPCNo = filename.split("\\.")[0];

			SBDatabaseHandlingService dbAccess = new SBDatabaseHandlingService(RunSchedulerCorpdmzToStep.datasource);
					
			SKUValue = dbAccess.getWercsUPCforMSDSDoc(UPCNo, listOfMSDSDoc.get(i).getName(), logger);

			if (SKUValue != null) {
				if (listOfMSDSDoc.get(i).getName().startsWith(PREFIX_FOR_MSDSDOC)) {
					String FileName = listOfMSDSDoc.get(i).getName();
					filename = FileName.split("\\-")[1];
					MSDSDocFiles1 = new File(MSDSDocDir + "/" + listOfMSDSDoc.get(i).getName());
					MSDSDocFiles1.renameTo(new File(MSDSDocDir + "/" + listOfMSDSDoc.get(i).getName().replace(FileName, filename)));
					MSDSDocFiles = new File(MSDSDocDir + "/" + listOfMSDSDoc.get(i).getName());
					success = MSDSDocFiles.renameTo(new File(MSDSDocDir + "/" + listOfMSDSDoc.get(i).getName().replace(UPCNo, SKUValue)));
				} else {
					MSDSDocFiles = new File(MSDSDocDir + "/" + listOfMSDSDoc.get(i).getName());
					success = MSDSDocFiles.renameTo(new File(MSDSDocDir + "/" + listOfMSDSDoc.get(i).getName().replace(UPCNo, SKUValue)));
				}
				if (success) {
					DatamigrationCommonUtil.printConsole("Filename Conversion is Done");
					File RenamedfilesDir = new File(listOfMSDSDoc.get(i).getParentFile().getPath());
					File[] RenamedlistOfFiles = RenamedfilesDir.listFiles();
					sendfiletoSFTP(RenamedlistOfFiles, SKUValue);
				} else {
					DatamigrationCommonUtil.printConsole("Renaming is failed");
				}
			} else {
				DatamigrationCommonUtil.printConsole("SKU is not avaliable");
				WercsRegulatoryDataFeedJobListner msdsdocumentProcessor = new WercsRegulatoryDataFeedJobListner();
				msdsdocumentProcessor.moveMSDSToDestnationDir(listOfMSDSDoc.get(i), RunSchedulerCorpdmzToStep.wercsmsdsdocbackupDir);
			}
		}
	}

	private static void sendfiletoSFTP(File[] ListofRenamedFiles, String SKUValue) throws IOException {

		logger.info("Sending MSDSDoc files to SFTP Server and Destination Folder");

		for (File RenamedFile : ListofRenamedFiles) {
			if (RenamedFile.getName().contains(SKUValue)) {
				List<String> outputFileList = new ArrayList<String>();
				outputFileList.add(RenamedFile.getPath());
				FTPConnectionBean ftpConnectionBean = getSFTPDestDetails();
				try {
					WercsCommonUtil.getInstance().fileSFTPToStepInboundDir(ftpConnectionBean, outputFileList,
							RunSchedulerCorpdmzToStep.PUBLISH_ID);
				} catch (IOException e) {
					logger.error("Caught an exception in movingMSDSDoctoSFTP :" + e.getMessage());
					IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), RunSchedulerCorpdmzToStep.PUBLISH_ID);
					e.printStackTrace();
				}
				try {
					WercsRegulatoryDataFeedJobListner msdsdocumentProcessor = new WercsRegulatoryDataFeedJobListner();
					msdsdocumentProcessor.moveToDestnationDir(RenamedFile.getPath(), wercsregulatorydataFeedDoneDir);
				} catch (IOException e) {
					logger.error("Caught an exception in movingMSDSDoctoDestinationFolder :" + e.getMessage());
					IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), RunSchedulerCorpdmzToStep.PUBLISH_ID);
					e.printStackTrace();

				}
				if (RenamedFile.getName().startsWith(PREFIX_FOR_MSDSDOC)) {
					filename1 = RenamedFile.getName().split("\\.")[0];
					filename = filename1.split("\\-")[1];
					SKUNO = filename.split("\\_")[1];
				} else {
					filename = RenamedFile.getName().split("\\.")[0];
					SKUNO = filename.split("\\_")[1];
				}
				createCSVFile(SKUNO, filename + ".csv", "", URL + RenamedFile.getName());

			} else {
				DatamigrationCommonUtil.printConsole("Filename dont have the SKUNumber");
			}
		}
	}

	private static void createCSVFile(String SKUNo, String MSDSDocFilename, String AssertID, String URL) {

		logger.info("Creation CSV File for MSDSDoc");
		try {

			File csvfileDir = new File(wercsregulatorydataFeedDoneDir + MSDSDocFilename);
			FileWriter writer = new FileWriter(csvfileDir);

			writer.append("ID");
			writer.append(',');
			writer.append("SKU");
			writer.append(',');
			writer.append("AssetID");
			writer.append(',');
			writer.append("AssetURL");
			writer.append('\n');

			writer.append("Item-2376539");
			writer.append(',');
			writer.append(SKUNo);
			writer.append(',');
			writer.append(AssertID);
			writer.append(',');
			writer.append(URL);

			writer.flush();
			writer.close();
			List<String> outputlist = new ArrayList<String>();
			outputlist.add(csvfileDir.getPath());
			DatamigrationCommonUtil.sendFile(csvfileDir, csvfileDir, wercsregulatorydataFeedDoneDir, "CorpDMZtoSTEP", true,
					WercsAppConstants.EHF_PUBLISH_ID_WERCSCORPDMZTOSTEP);

		}

		catch (IOException e) {
			logger.error("Caught an exception in CreationofCSVFile :" + e.getMessage());
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), RunSchedulerCorpdmzToStep.PUBLISH_ID);
			e.printStackTrace();
		}
	}

	private static FTPConnectionBean getSFTPDestDetails() {

		FTPConnectionBean connectionsBean = new FTPConnectionBean();
		connectionsBean.setHostName(IntgSrvPropertiesReader.getProperty(WercsAppConstants.CORPDMZTOSTEP_FEED_SFTP_HOST_STEP));
		connectionsBean.setUserId(IntgSrvPropertiesReader.getProperty(WercsAppConstants.CORPDMZTOSTEP_FEED_SFTP_USERNAME_STEP));
		connectionsBean.setPassword(IntgSrvPropertiesReader.getProperty(WercsAppConstants.CORPDMZTOSTEP_FEED_SFTP_PASSWORD_STEP));
		connectionsBean.setDestinationUrl(IntgSrvPropertiesReader.getProperty(WercsAppConstants.CORPDMZTOSTEP_FEED_SFTP_TARGET_DIR_STEP));
		return connectionsBean;
	}

}
