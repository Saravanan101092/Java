package com.staples.pim.delegate.wercs.common;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.batch.core.BatchStatus;

import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.YesNoType;
import com.staples.pim.base.common.bean.FTPConnectionBean;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.SFTPManager;
import com.staples.pim.base.util.EmailUtil;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wholesalers.common.FileNameIndexNoComparator;
import com.staples.pim.delegate.wholesalers.common.Util;




public class WercsCommonUtil {
	
	protected IntgSrvLogger	ehfLogger	= IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	protected IntgSrvLogger	traceLogger	= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	
	/**
	 * Private constructor. Prevents instantiation from other classes.
	 */	
	private WercsCommonUtil() {
		
	}
	
	/**
	 * Initializes singleton.
	 */
	private static class SingletonHolder {

		private static final WercsCommonUtil	obj;
		static {
			try {
				obj = new WercsCommonUtil();
			} catch (Exception exception) {
				throw new RuntimeException("Singleton SummaryReportManager, an error occurred!", exception);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static WercsCommonUtil getInstance() {

		return SingletonHolder.obj;
	}
	
	/**
	 * 
	 * @param fileAbsPath
	 * @return
	 */
	public String getFileName(String fileAbsPath) {

		File file = new File(fileAbsPath);
		return file.getName();
	}
	
	/**
	 * 
	 * @param fileAbsPath
	 * @return
	 */
	public boolean isFile(String fileAbsPath) {

		File file = new File(fileAbsPath);
		return file.isFile();
	}

	/**
	 * 
	 * @param inputFile
	 * @return
	 */
	public String getFileLineCount(String inputFile) {

		int lines = 0;
		try {
			File file = new File(inputFile);
			LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
			lineNumberReader.skip(Long.MAX_VALUE);
			lines = lineNumberReader.getLineNumber();
			lineNumberReader.close();

		} catch (Exception exception) {
			traceLogger.error(exception.getMessage());
			ehfLogger.error(exception.getMessage());
		}
		return " (" + lines + " Records)";
	}
	
	/**
	 * 
	 * @param inputFileNameAndRecordCount
	 * @param inputFileNameAndRecordCount2
	 * @param outputFileList
	 * @param errorReportFile
	 * @param batchStatus
	 */
	public void sendSummaryEmailReport(String instanceType, String inputFileNameAndRecordCount, List<String> outputFileList,
			String errorReportFile, BatchStatus batchStatus) {

		String newLine = "\n";
		String sub = "Data Migration SummaryReport - " + IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SPRINGBATCH_ENV);

		StringBuffer mailConBuf = new StringBuffer("");
		try {
			mailConBuf.append(newLine + "Hi All, " + newLine);
			mailConBuf.append(newLine + "Data Migration Summary Report - " + instanceType + ", Please find below details:" + newLine);

			mailConBuf.append(newLine + "Integration System: " + InetAddress.getLocalHost().getHostName());
			mailConBuf.append(newLine + "Batch Status: " + String.valueOf(batchStatus));
			mailConBuf.append(newLine + "Feed File: " + inputFileNameAndRecordCount + newLine);
			mailConBuf.append(newLine + "Output STEP xml Files: ");
			for (String str : outputFileList) {

				mailConBuf.append(newLine + Util.getInstance().getFileName(str));
			}
			mailConBuf.append(newLine + "Error out Records with Details: ");
			if (Util.getInstance().isFile(errorReportFile)) {
				mailConBuf.append(newLine + errorReportFile + Util.getInstance().getFileLineCount(errorReportFile));
			} else {
				mailConBuf.append(newLine + "There are no Error out Records from the Feed.");
			}

			mailConBuf.append(newLine + newLine + "Thanks.");

			EmailUtil emailUtil = new EmailUtil();
			emailUtil.sendEmail(sub, String.valueOf(mailConBuf));

		} catch (Exception exception) {
			traceLogger.error(exception.getMessage());
			ehfLogger.error(exception.getMessage());
		}
	}
	
	public void fileSFTPToStepInboundDir(FTPConnectionBean ftpConnectionBean, List<String> outputFileList, String publishId)
			throws IOException {

		String desUrls = ftpConnectionBean.getDestinationUrl();
		List<String> desUrlList = Arrays.asList(desUrls.split("~"));

		int outputFileListSize = outputFileList.size();
		int dirListSize = desUrlList.size();

		/*
		 * Counters
		 */
		int outputFileCount = 0;
		int desUrlCount = 0;

		for (; outputFileCount < outputFileListSize;) {

			ftpConnectionBean.setDestinationUrl(desUrlList.get(desUrlCount));
			SFTPManager sftpManager = new SFTPManager(ftpConnectionBean);
			sftpManager.setPublishId(publishId);

			boolean isSentSuccessfully = sftpManager.uploadFile(outputFileList.get(outputFileCount));
			if (isSentSuccessfully) {
				traceLogger.info("SFTP Success, Source File:" + outputFileList.get(outputFileCount));

			} else {
				ehfLogger.error("SFTP Failed, Source File:" + outputFileList.get(outputFileCount));
				traceLogger.error("SFTP Failed, Source File:" + outputFileList.get(outputFileCount));
			}
			outputFileCount++;
			desUrlCount++;
			desUrlCount = (desUrlCount < dirListSize) ? desUrlCount : 0;
		}
	}
	
	public List<String> fileSort(List<String> inputFileList) {

		List<String> outputFileList = new ArrayList<String>();
		List<File> fileList = new ArrayList<File>();
		FileNameIndexNoComparator comparator = new FileNameIndexNoComparator();

		for (String str : inputFileList) {
			File file = new File(str);
			fileList.add(file);
		}
		Collections.sort(fileList, comparator);

		for (File file : fileList) {
			outputFileList.add(file.getAbsolutePath());
		}
		return outputFileList;
	}
	
	public void setDefaultInfo(STEPProductInformation stepPrdInfo) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
		Date date = new Date();

		stepPrdInfo.setExportTime(dateFormat.format(date));
		stepPrdInfo.setContextID("EnglishUS");
		stepPrdInfo.setExportContext("EnglishUS");
		stepPrdInfo.setWorkspaceID("Main");
		stepPrdInfo.setUseContextLocale(false);
		stepPrdInfo.setAutoInitiate(YesNoType.N);
	}

	//WERCS
		public static void sendWercsResponseFile(File outputFile, String applicationId,String publishID,IntgSrvLogger logger) {

			String hostName = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SFTP_HOST_STEP);
			if (hostName != null && !"null".equalsIgnoreCase(hostName)) {

				FTPConnectionBean ftpConnectionBean = DatamigrationCommonUtil.getSFTPConnectConfigBean(applicationId, outputFile.getPath());

				// PRODUCT_SFTP_FAIL_THREAD_SLEEP_INTERVAL
				SFTPManager sftpManager = new SFTPManager(ftpConnectionBean);
				sftpManager.setPublishId(publishID);

				boolean isSentSuccessfully = false;
				while (!isSentSuccessfully) {
					isSentSuccessfully = sftpManager.uploadFile(outputFile.getPath());
					if (!isSentSuccessfully) {
						long thread_sleep_interval;
						String threadsleepintervalString = IntgSrvPropertiesReader
								.getProperty(DatamigrationAppConstants.PRODUCT_SFTP_FAIL_THREAD_SLEEP_INTERVAL);
						if (threadsleepintervalString != null) {
							thread_sleep_interval = Long.parseLong(threadsleepintervalString);
						} else {
							thread_sleep_interval = 60000;
						}
						try {
							Thread.sleep(thread_sleep_interval);
						} catch (InterruptedException e) {
							logger.error("SFTP failed:"+outputFile.getName());
							e.printStackTrace();
							IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), publishID);
						}
					}
				}

				logger.info("SFTP transfer success");
				DatamigrationCommonUtil.printConsole("SFTP transfer success:" + isSentSuccessfully);

			}
		}


}
