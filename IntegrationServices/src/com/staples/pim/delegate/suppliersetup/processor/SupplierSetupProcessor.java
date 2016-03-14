
package com.staples.pim.delegate.suppliersetup.processor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

public class SupplierSetupProcessor {

	private IntgSrvLogger		traceLogger				= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	private String				clazzname				= this.getClass().getName();

	/*
	 * There are five different directory
	 */
	private static final String	DELETEUSER				= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.DELETEUSER);
	private static final String	ADDTEMPFOLDERGROUP		= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.ADDTEMPFOLDERGROUP);
	private static final String	ADDTEMPUSER				= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.ADDTEMPUSER);
	private static final String	DELETETEMPFOLDERGROUP	= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.DELETETEMPFOLDERGROUP);
	private static final String	ADDPERMFOLDERGROUP		= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.ADDPERMFOLDERGROUP);
	private static final String	ADDPERMUSER				= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.ADDPERMUSER);

	private static final String	inboundFolder			= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SUPPLIER_SETUP_INBOUND);
	private static final String	tmpMetaFolder			= IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
																.getProperty(IntgSrvAppConstants.TMP_META_FOLDER));
	/*
	 * There are three types of Meta-Data files
	 */
	private static final String	METAFILE_ADDTEMP_FORMAT	= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.METAFILE_ADDTEMP_FORMAT);
	private static final String	METAFILE_DELTEMP_FORMAT	= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.METAFILE_DELTEMP_FORMAT);
	private static final String	METAFILE_ADDPERM_FORMAT	= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.METAFILE_ADDPERM_FORMAT);

	private static final String	METAFILE_DELTEMP_DELAY	= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.METAFILE_DELTEMP_DELAY);
	/*
	 * STEP Server Details
	 */
	private static final String	username				= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SSO_SFTP_USER_NAME);
	private static final String	password				= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SSO_SFTP_PASSWORD);
	private static final String	hostname				= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SSO_SFTP_HOST_NAME);
	private static final int	port					= Integer.parseInt(IntgSrvPropertiesReader
																.getProperty(IntgSrvAppConstants.SSO_SFTP_PORT));
	public static final String	PUBLISH_ID				= "SUPPLIER_SETUP";

	public void processFiles() {

		Session session = null;
		try {
			String msgDesc = "Started in processing files.. inside processFiles method...";
			traceLogger.info(clazzname, "processFiles", msgDesc);

			session = getSFTPSession();

			List<String> deleteUserFileList = getFileNamesandMoveFiles(DELETEUSER, session);

			List<String> addPermUserFileList = getFileNamesandMoveFiles(ADDPERMUSER, session);
			List<String> addPermFolderGroupFileList = getFileNamesandMoveFiles(ADDPERMFOLDERGROUP, session);

			List<String> delTempFolderGrpFileList = getFileNamesandMoveFiles(DELETETEMPFOLDERGROUP, session);

			List<String> addTempUserFileList = getFileNamesandMoveFiles(ADDTEMPUSER, session);
			List<String> addTempFolderGroupFileList = getFileNamesandMoveFiles(ADDTEMPFOLDERGROUP, session);

			/*
			 * Read file names from AddTempFolderGroup hotfolder and create a
			 * meta file. Next, Read file names from AddTempUser hotfolder and
			 * append to meta file. Move the metafile and all the XMLs to the
			 * SupplieSetup hotfolder which is configured in the Inbound
			 * Endpoint.
			 */
			processAddTempFolderGroupFileList(session, addTempFolderGroupFileList, addTempUserFileList);
			/*
			 * Read file names from DeleteTempFolderGroup hotfolder and create a
			 * meta file. Move the metafile and all the XMLs to the
			 * SupplierSetup hotfolder which is configured in the Inbound
			 * Endpoint. Note: Metafile must be created for each XML in
			 * DeleteTempFolderGroup hotfolder until the folder is empty. There
			 * should be a 2 minute standing time between each metafile creation
			 * for files in this folder.
			 */
			processDelTempFolderGroupFileList(session, delTempFolderGrpFileList);
			/*
			 * Read file names from AddPermFolderGroup hotfolder and create a
			 * meta file. Next, Read file names from AddPermUser hotfolder and
			 * append to meta file. Move the metafile and all the XMLs to the
			 * SupplierSetup hotfolder which is configured in the Inbound
			 * Endpoint.
			 */
			processAddPermFolderGroupFileList(session, addPermFolderGroupFileList, addPermUserFileList);
			/*
			 * Read file names from deleteUser hotfolder and create a meta file.
			 * Move the metafile and all the XMLs to the SupplierSetup hotfolder
			 * which is configured in the Inbound Endpoint. Note: Metafile must
			 * be created for each XML in deleteUser hotfolder until the folder
			 * is empty. There should be a 2 minute standing time between each
			 * metafile creation for files in this folder.
			 */
			processDelTempFolderGroupFileList(session, deleteUserFileList);

		} catch (Exception exception) {
			String msgDesc = "Exception in processFiles::" + exception.getMessage();
			traceLogger.info(clazzname, "processFiles", msgDesc);
			IntgSrvUtils.alertByEmail(exception, clazzname, SupplierSetupProcessor.PUBLISH_ID);
		} finally {
			try {
				if (session != null) {
					session.disconnect();
				}
			} catch (Exception exception) {
				String msgDesc = "Exception in disconnecting session::" + exception.getMessage();
				traceLogger.info(clazzname, "processFiles", msgDesc);
				IntgSrvUtils.alertByEmail(exception, clazzname, SupplierSetupProcessor.PUBLISH_ID);
			}
		}
	}

	private void processDelTempFolderGroupFileList(Session session, List<String> delTempFolderGrpFileList) throws JSchException,
			SftpException, IOException, InterruptedException {

		Thread.currentThread().sleep(Integer.parseInt(METAFILE_DELTEMP_DELAY));

		for (String fileStr : delTempFolderGrpFileList) {
			String epochTime = String.valueOf(System.currentTimeMillis());

			String delTempMetaFileName = METAFILE_DELTEMP_FORMAT.replace("<EPOCHTIME>", epochTime);
			String metaFileSrc = tmpMetaFolder + delTempMetaFileName;
			String metaFileDestn = inboundFolder + delTempMetaFileName;

			List<String> fileNames = new ArrayList<String>();
			fileNames.add(fileStr);

			writeMetaFile(fileNames, metaFileSrc);
			copyMetaFile(metaFileSrc, metaFileDestn, session);

			Thread.currentThread().sleep(Integer.parseInt(METAFILE_DELTEMP_DELAY));
		}

	}

	private void processAddPermFolderGroupFileList(Session session, List<String> addPermFolderGroupFileList,
			List<String> addPermUserFileList) throws JSchException, SftpException, IOException {

		List<String> addPermMetaFileList = new ArrayList<String>();
		String epochTime = String.valueOf(System.currentTimeMillis());

		String addPermMetaFileName = METAFILE_ADDPERM_FORMAT.replace("<EPOCHTIME>", epochTime);
		String metaFileSrc = tmpMetaFolder + addPermMetaFileName;
		String metaFileDestn = inboundFolder + addPermMetaFileName;

		if (addPermFolderGroupFileList.size() != 0) {
			addPermMetaFileList.addAll(addPermFolderGroupFileList);
		}

		if (addPermUserFileList.size() != 0) {
			addPermMetaFileList.addAll(addPermUserFileList);
		}

		if (addPermMetaFileList.size() != 0) {
			writeMetaFile(addPermMetaFileList, metaFileSrc);
			copyMetaFile(metaFileSrc, metaFileDestn, session);
		}
	}

	private void processAddTempFolderGroupFileList(Session session, List<String> addTempFolderGroupFileList,
			List<String> addTempUserFileList) throws JSchException, SftpException, IOException {

		List<String> addTempMetaFileList = new ArrayList<String>();
		String epochTime = String.valueOf(System.currentTimeMillis());

		String addTempMetaFileName = METAFILE_ADDTEMP_FORMAT.replace("<EPOCHTIME>", epochTime);
		String metaFileSrc = tmpMetaFolder + addTempMetaFileName;
		String metaFileDestn = inboundFolder + addTempMetaFileName;

		if (addTempFolderGroupFileList.size() != 0) {
			addTempMetaFileList.addAll(addTempFolderGroupFileList);
		}

		if (addTempUserFileList.size() != 0) {
			addTempMetaFileList.addAll(addTempUserFileList);
		}

		if (addTempMetaFileList.size() != 0) {
			writeMetaFile(addTempMetaFileList, metaFileSrc);
			copyMetaFile(metaFileSrc, metaFileDestn, session);
		}
	}

	private void writeMetaFile(List<String> fileNames, String metaFileSrc) throws IOException {

		FileWriter fstream = new FileWriter(metaFileSrc, true);
		BufferedWriter out = new BufferedWriter(fstream);

		for (String fileName : fileNames) {
			out.write(fileName);
			out.newLine();
		}
		// close buffer writer
		out.close();
	}

	private void copyMetaFile(String srcFileString, String destDirString, Session session) throws IOException, JSchException, SftpException {

		ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
		sftp.connect();
		File file = new File(srcFileString);
		InputStream inputStream = new FileInputStream(file);
		sftp.put(inputStream, destDirString);
		sftp.disconnect();
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

	public List<String> getFileNamesandMoveFiles(String directoryName, Session session) throws JSchException, SftpException {

		IntgSrvUtils.printConsole("directoryName ::  " + directoryName);
		ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
		sftp.connect();
		String fileName = "";
		String srcFile = "";
		String destFile = "";
		List<String> fileNameList = new ArrayList<String>();
		sftp.cd(directoryName);
		Vector<ChannelSftp.LsEntry> list = sftp.ls(directoryName);
		int count = 0;
		for (ChannelSftp.LsEntry entry : list) {

			if (entry.getAttrs().isDir()) {
				continue;
			}
			count++;
			// entry.getLongname();

			fileName = entry.getFilename();
			fileNameList.add(fileName);
			srcFile = directoryName + fileName;
			destFile = inboundFolder + fileName;
			sftp.rename(srcFile, destFile);

		}
		IntgSrvUtils.printConsole("count::" + count);
		sftp.disconnect();
		return fileNameList;
	}

	public static void main(String args[]) {

		SupplierSetupProcessor supplierSetupProcessor = new SupplierSetupProcessor();
		supplierSetupProcessor.processFiles();

	}

}