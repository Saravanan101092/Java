
package com.staples.pim.delegate.assetImport.processor;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.staples.pim.base.common.bean.FTPConnectionBean;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.SFTPManager;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.assetImport.model.Asset;
import com.staples.pim.delegate.assetImport.reader.AssetExcelReader;
import com.staples.pim.delegate.assetImport.runner.RunSchedulerAssetImport;
import com.staples.pim.delegate.assetImport.writer.AssetExcelWriter;

public class AssetImportProcessor {

	private IntgSrvLogger	traceLogger				= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	private String			clazzname				= this.getClass().getName();
	protected IntgSrvLogger	ehfLogger				= IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);

	private static String	assetImportInputFolder	= IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
															.getProperty(IntgSrvAppConstants.ASSET_IMPORT_INPUT_FOLDER));
	private static String	assetImportOutputFolder	= IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
															.getProperty(IntgSrvAppConstants.ASSET_IMPORT_OUTPUT_FOLDER));

	private static String	assetImportDoneFolder	= IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
															.getProperty(IntgSrvAppConstants.ASSET_IMPORT_DONE_FOLDER));
	public static String	FEED_FILE_EXTN			= IntgSrvPropertiesReader.getProperty("ASSET_IMPORT_FILE_EXTN");
	public static String	FEED_FILE_BEGIN			= IntgSrvPropertiesReader.getProperty("ASSET_IMPORT_FILE_BEGIN");

	public void processFiles() {

		try {
			String msgDesc = "Started in processing files.. inside processFiles method...";
			traceLogger.info(clazzname, "processFiles", msgDesc);

			AssetExcelReader assetImportFeedReader = new AssetExcelReader();
			AssetExcelWriter assetImportFeedExcelWriter = new AssetExcelWriter();
			File dir = new File(assetImportInputFolder);

			File[] files = dir.listFiles();
			List<File> fileList = getAssetImport(Arrays.asList(files));

			if (fileList.size() == 0) {
				traceLogger.info(clazzname, "run", "No input file found");
			} else {

				IntgSrvUtils.printConsole("Number of files:" + fileList.size() + " in directory:" + dir.getName());
				int numfiles = files.length;
				traceLogger.info(clazzname, "run", "Number of input files: " + numfiles);
				for (int i = 0; i < fileList.size(); i++) {

					if (fileList.get(i).isFile()) {
						List<Asset> assetList = new ArrayList<Asset>();
						File currentFile = fileList.get(i);
						String filePath = currentFile.getAbsolutePath();
						assetList = assetImportFeedReader.getAssetListFromExcel(filePath);

						SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("yyyyMMdd_HHmmsss");
						String dateJob = dateformatyyyyMMdd.format(new Date());
						String epochTime = String.valueOf(System.currentTimeMillis());

						String outputFileName = assetImportOutputFolder + IntgSrvAppConstants.SLASH + IntgSrvAppConstants.ASSET_FILE_NAME
								+ "_" + dateJob + "_" + epochTime + ".xlsx";

						assetImportFeedExcelWriter.writeAssetListTOExcel(assetList, outputFileName);

						if (!outputFileName.isEmpty()) {
							fileSFTPToStepInboundDir(outputFileName);
						}
						moveToDestnationDir(filePath);
					}
				}
			}

		} catch (Exception exception) {
			String msgDesc = "Exception in processFiles::" + exception.getMessage();
			traceLogger.info(clazzname, "processFiles", msgDesc);
			ehfLogger.error(clazzname, "processFiles", msgDesc);
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerAssetImport.PUBLISH_ID);
		}
	}

	private List<File> getAssetImport(List<File> sourceList) {

		List<File> assetImportFileList = new ArrayList<File>();

		for (File file : sourceList) {
			String fileName = file.getName();
			if (file.isFile() && fileName.startsWith(FEED_FILE_BEGIN) && fileName.endsWith(FEED_FILE_EXTN)) {
				assetImportFileList.add(file);
			}
		}
		return assetImportFileList;
	}

	private void fileSFTPToStepInboundDir(String srcFileString) throws IOException {

		FTPConnectionBean ftpConnectionBean = getSFTPDestDetails();
		SFTPManager sftpManager = new SFTPManager(ftpConnectionBean);
		sftpManager.setPublishId(RunSchedulerAssetImport.PUBLISH_ID);

		boolean isSentSuccessfully = sftpManager.uploadFile(srcFileString);

		if (isSentSuccessfully) {
			traceLogger.info(clazzname, "SFTP Success", "ENTER/.../EXIT: ASSET_IMPORT - initialization <- job params");
		} else {
			traceLogger.info(clazzname, "SFTP Failed", "ENTER/.../EXIT: ASSET_IMPORT - initialization <- job params");
		}
	}

	private FTPConnectionBean getSFTPDestDetails() {

		FTPConnectionBean connectionsBean = new FTPConnectionBean();
		connectionsBean.setHostName(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.ASSET_IMPORT_SFTP_HOST_STEP));
		connectionsBean.setUserId(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.ASSET_IMPORT_SFTP_USERNAME_STEP));
		connectionsBean.setPassword(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.ASSET_IMPORT_SFTP_PASSWORD_STEP));
		connectionsBean.setDestinationUrl(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.ASSET_IMPORT_SFTP_TARGET_DIR_STEP));
		return connectionsBean;
	}

	private void moveToDestnationDir(String outputFileString) throws IOException {

		File srcFile = new File(outputFileString);
		File newFile = new File(assetImportDoneFolder + srcFile.getName());

		// Appending epoch time stamp
		if (srcFile.renameTo(newFile)) {
			IntgSrvUtils.printConsole("Renamed");
		} else {
			IntgSrvUtils.printConsole("Not able to rename");
		}
	}
}