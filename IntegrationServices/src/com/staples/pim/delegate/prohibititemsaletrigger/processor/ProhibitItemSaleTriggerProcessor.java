package com.staples.pim.delegate.prohibititemsaletrigger.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;

public class ProhibitItemSaleTriggerProcessor {

	private IntgSrvLogger		traceLogger				= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	private String				clazzname				= this.getClass().getName();

	/*
	 * Source/Destination Directory
	 */
	private static final String	PROHIBIT_ITEM_SALE_TEMP	= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PROHIBIT_ITEM_SALE_TRIGGER_TEMP);
	private static final String	inboundFolder			= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PROHIBIT_ITEM_SALE_TRIGGER_INBOUND);

	/*
	 * STEP Server Details for Prohibit Item Sale Trigger
	 */
	private static final String	username				= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SSO_SFTP_USER_NAME);
	private static final String	password				= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SSO_SFTP_PASSWORD);
	private static final String	hostname				= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SSO_SFTP_HOST_NAME);
	private static final int	port					= Integer.parseInt(IntgSrvPropertiesReader
																.getProperty(IntgSrvAppConstants.SSO_SFTP_PORT));
	
	public static final String	PUBLISH_ID				= "PROHIBIT_ITEM_SALE_TRIGGER";

	public void processFiles() {

		Session session = null;
		try {
			String msgDesc = "Started in processing file.. inside processFiles method...";
			traceLogger.info(clazzname, "processFiles", msgDesc);

			session = getSFTPSession();
			
			/*
			 * open the sftp session and move all the files from source
			 * to destination
			 */
			processAddTempFolderGroupFileList(session);
			
		} catch (Exception exception) {
			String msgDesc = "Exception in processFiles::" + exception.getMessage();
			traceLogger.info(clazzname, "processFiles", msgDesc);
			IntgSrvUtils.alertByEmail(exception, clazzname, ProhibitItemSaleTriggerProcessor.PUBLISH_ID);
		} finally {
			try {
				if (session != null) {
					session.disconnect();
				}
			} catch (Exception exception) {
				String msgDesc = "Exception in disconnecting session::" + exception.getMessage();
				traceLogger.info(clazzname, "processFiles", msgDesc);
				IntgSrvUtils.alertByEmail(exception, clazzname, ProhibitItemSaleTriggerProcessor.PUBLISH_ID);
			}
		}
	}


	private void processAddTempFolderGroupFileList(Session session) throws JSchException, SftpException, IOException {
		getFileNamesandMoveFiles(PROHIBIT_ITEM_SALE_TEMP, session);
	}

	public Session getSFTPSession() throws JSchException, SftpException {

		JSch conn = new JSch();
		Session session = conn.getSession(username, hostname, port);
		session.setPassword(password);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
		session.connect();
		IntgSrvUtils.printConsole("Connected SESSION NAME ::: " + session);
		return session;
	}

	public void getFileNamesandMoveFiles(String directoryName, Session session) throws JSchException, SftpException, FileNotFoundException {

		IntgSrvUtils.printConsole("directoryName ::  " + directoryName);
		ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
		sftp.connect();
		String fileName = "";
		String srcFile = "";
		String destFile = "";
		sftp.cd(directoryName);
		@SuppressWarnings("unchecked")
		Vector<ChannelSftp.LsEntry> list = sftp.ls(directoryName);
		int count = 0;
		for (ChannelSftp.LsEntry entry : list) {
			if (entry.getAttrs().isDir()) {
				continue;
			}
			count++;
			fileName = entry.getFilename();
			srcFile = directoryName + fileName;
			destFile = inboundFolder + fileName;
			InputStream inputStream = sftp.get(srcFile); 
//			File file = new File(srcFile);
//			InputStream inputStream = new FileInputStream(file);
			System.out.println("hi");
//			sftp.put(srcFile,destFile,ChannelSftp.OVERWRITE);
			sftp.put(inputStream,destFile);
		}
		IntgSrvUtils.printConsole("count::" + count);
		sftp.disconnect();
	}

	public static void main(String args[]) {

		ProhibitItemSaleTriggerProcessor prohibitItemSaleTriggerProcessor = new ProhibitItemSaleTriggerProcessor();
		prohibitItemSaleTriggerProcessor.processFiles();

	}

}