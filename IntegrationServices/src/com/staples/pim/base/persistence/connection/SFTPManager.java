/**
 * -----------------------------------------------------------------------
 * STAPLES, INC
 * -----------------------------------------------------------------------
 * (C) Copyright 2007 Staples, Inc.          All rights reserved.
 *
 * NOTICE:  All information contained herein or attendant hereto is,
 *          and remains, the property of Staples Inc.  Many of the
 *          intellectual and technical concepts contained herein are
 *          proprietary to Staples Inc. Any dissemination of this
 *          information or reproduction of this material is strictly
 *          forbidden unless prior written permission is obtained
 *          from Staples Inc.
 * -----------------------------------------------------------------------
 */
/*
 * File name     :   
 * Creation Date :   
 * @author  
 * @version 1.0
 */

package com.staples.pim.base.persistence.connection;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import com.staples.pim.base.common.bean.FTPConnectionBean;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;

public class SFTPManager {

	private String				hostName		= null;
	private String				userId			= null;
	private String				password		= null;
	private String				destinationURL	= null;
	private String				originatedURL	= null;
	private String				pathDelimiter	= null;
	private int					resendCount		= 0;
	private String				fileName		= null;
	private String				publishId		= null;

	private static final String	alert			= "SFTP failed alert - "
														+ IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.SPRINGBATCH_ENV);
	String						emailMessage	= null;

	public SFTPManager(FTPConnectionBean connectionBean) {

		hostName = connectionBean.getHostName();
		userId = connectionBean.getUserId();
		password = connectionBean.getPassword();
		destinationURL = connectionBean.getDestinationUrl();
		originatedURL = connectionBean.getOriginatedURL();
	}

	public List<String> getFileNameList(String pathname) {

		String[] arrayOfFileNames = null;
		List<String> arrayOfComplFileNames = new ArrayList<String>();
		String baseURL = null;

		File directory = new File(pathname);

		if (directory.isDirectory()) {
			arrayOfFileNames = directory.list();
			baseURL = directory.getAbsolutePath();
		}

		/* set complete fully clarify file name */
		for (int i = 0; i < arrayOfFileNames.length; i++) {
			arrayOfComplFileNames.add(baseURL + pathDelimiter + arrayOfFileNames[i]);
		}

		return arrayOfComplFileNames;

	}

	public void setFileTypeForProcess(List<String> listOfFilesForFTP) {

	}

	public boolean uploadFile(String fileToFTP) {

		fileName = fileToFTP;
		boolean bSuccess = true;
		StandardFileSystemManager manager = new StandardFileSystemManager();
		// check if the file exists
		// String filepath = IntgSrvUtils.reformatFilePath(originatedURL +
		// fileToFTP);
		String filepath = IntgSrvUtils.reformatFilePath(fileToFTP);
		String actualFileName = FilenameUtils.getName(filepath); // appache
		IntgSrvUtils.printConsole("filepath=" + filepath);
		// String filepath =
		// "C:\\opt\\stibo\\SpringBatch\\outputs\\xsv\\stibo_product_09_02_12_41_13_013.xsv";
		try {
			File file = new File(filepath);
			if (!file.exists())
				throw new RuntimeException("Error. Local file not found");
			// Initializes the file manager
			manager.init();
			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);
			if (userId.equals(IntgSrvAppConstants.STEP_USER_INT) || userId.equals(IntgSrvAppConstants.STEP_USER_PCM)) {
				SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
				SftpFileSystemConfigBuilder.getInstance().setPreferredAuthentications(opts, "publickey,keyboard-interactive,password");
			} else {
				SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
				// SftpFileSystemConfigBuilder.getInstance().setPreferredAuthentications(opts,
				// "publickey,keyboard-interactive,password");
			}
			// Create the SFTP URI using the host name, userid, password, remote
			// path and file name
			String sftpUri = IntgSrvAppConstants.PROTOCOL + userId + IntgSrvAppConstants.COLON + password + IntgSrvAppConstants.AT_SIGN
					+ hostName + destinationURL +
					// fileToFTP;
					actualFileName;

			IntgSrvUtils.printConsole(sftpUri);
			// Create local file object
			FileObject localFile = manager.resolveFile(file.getAbsolutePath());
			IntgSrvUtils.printConsole("localFile resolved");
			// Create remote file object
			FileObject remoteFile = manager.resolveFile(sftpUri, opts);
			IntgSrvUtils.printConsole("remoteFile resolved");
			// Copy local file to sftp server
			remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);// Selectors.SELECT_SELF
			IntgSrvUtils.printConsole("File upload successful");
		} catch (Exception ex) {
			if (resendCount < 3) {
				resendCount++;
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					IntgSrvUtils.printConsole("InterruptedException e=" + e);
				}
				bSuccess = uploadFile(fileToFTP);
			} else {
				/*
				 * try { IntgSrvUtils.copyFileUsingFileStreams(new
				 * File(filepath), new
				 * File(IntgSrvUtils.reformatFilePath(originatedURL +"resend/"+
				 * fileToFTP))); } catch (IOException e) { // TODO
				 * Auto-generated catch block e.printStackTrace();
				 * System.out.println("IOException e="+e); }
				 */
				ex.printStackTrace();
				emailMessage = "Hello,\n MQ get Exception:\n" + ex;
				IntgSrvUtils.alertByEmail(alert, getMailMessage(ex.getMessage()));
				bSuccess = false;
			}
		} finally {
			if (manager != null) {
				manager.close();
				IntgSrvUtils.printConsole("Manager closed");
			}
		}
		return bSuccess;

	}

	public boolean uploadFile(String fileToFTP, String privateKeyFile) {

		fileName = fileToFTP;
		boolean bSuccess = true;
		StandardFileSystemManager manager = new StandardFileSystemManager();
		// check if the file exists
		// String filepath = IntgSrvUtils.reformatFilePath(originatedURL +
		// fileToFTP);
		String filepath = IntgSrvUtils.reformatFilePath(fileToFTP);
		String actualFileName = FilenameUtils.getName(filepath); // appache
		IntgSrvUtils.printConsole("filepath=" + filepath);
		// String filepath =
		// "C:\\opt\\stibo\\SpringBatch\\outputs\\xsv\\stibo_product_09_02_12_41_13_013.xsv";
		try {
			File file = new File(filepath);
			if (!file.exists())
				throw new RuntimeException("Error. Local file not found");
			// Initializes the file manager
			manager.init();
			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);
			// if (userId.equals(IntgSrvAppConstants.STEP_USER_INT) ||
			// userId.equals(IntgSrvAppConstants.STEP_USER_PCM)){
			//
			SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
			SftpFileSystemConfigBuilder.getInstance().setIdentities(opts, new File[] { new File(privateKeyFile) });
			SftpFileSystemConfigBuilder.getInstance().setPreferredAuthentications(opts, "publickey,keyboard-interactive,password");
			// }
			// else {
			// SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
			// true);
			// //SftpFileSystemConfigBuilder.getInstance().setPreferredAuthentications(opts,
			// "publickey,keyboard-interactive,password");
			// }
			// Create the SFTP URI using the host name, userid, password, remote
			// path and file name
			String sftpUri = IntgSrvAppConstants.PROTOCOL + userId + IntgSrvAppConstants.AT_SIGN + hostName + destinationURL +
			// fileToFTP;
					actualFileName;

			IntgSrvUtils.printConsole(sftpUri);
			// Create local file object
			FileObject localFile = manager.resolveFile(file.getAbsolutePath());
			IntgSrvUtils.printConsole("localFile resolved");
			// Create remote file object
			FileObject remoteFile = manager.resolveFile(sftpUri, opts);
			IntgSrvUtils.printConsole("remoteFile resolved");
			// Copy local file to sftp server
			remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);// Selectors.SELECT_SELF
			IntgSrvUtils.printConsole("File upload successful");
		} catch (Exception ex) {
			if (resendCount < 3) {
				resendCount++;
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					IntgSrvUtils.printConsole("InterruptedException e=" + e);
				}
				bSuccess = uploadFile(fileToFTP);
			} else {
				/*
				 * try { IntgSrvUtils.copyFileUsingFileStreams(new
				 * File(filepath), new
				 * File(IntgSrvUtils.reformatFilePath(originatedURL +"resend/"+
				 * fileToFTP))); } catch (IOException e) { // TODO
				 * Auto-generated catch block e.printStackTrace();
				 * System.out.println("IOException e="+e); }
				 */
				ex.printStackTrace();
				emailMessage = "Hello,\n MQ get Exception:\n" + ex;
				IntgSrvUtils.alertByEmail(alert, getMailMessage(ex.getMessage()));
				bSuccess = false;
			}
		} finally {
			if (manager != null) {
				manager.close();
				IntgSrvUtils.printConsole("Manager closed");
			}
		}
		return bSuccess;

	}

	private String getMailMessage(String stackTrace) {

		StringBuffer buffer = new StringBuffer("");
		try {
			buffer.append("Hi All, \n\n");
			buffer.append("SFTP Failed on Spring Batch, Please find below details: \n\n");

			buffer.append("Source System: " + InetAddress.getLocalHost().getHostName() + "\n");
			if (hostName != null) {
				buffer.append("Target Host: " + hostName + "\n");
			}
			if (userId != null) {
				buffer.append("UserName: " + userId + "\n");
			}
			if (fileName != null) {
				buffer.append("File Name: " + fileName + "\n");
			}
			if (destinationURL != null) {
				buffer.append("Target Directory: " + destinationURL + "\n");
			}
			if (originatedURL != null) {
				buffer.append("Source Directory: " + originatedURL + "\n");
			}
			if (publishId != null) {
				buffer.append("Publish Id: " + publishId + "\n");
			}
			buffer.append("Exception StackTrace: " + stackTrace);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public void setPublishId(String pubId) {

		this.publishId = pubId;
	}

	public boolean downloadFile(String fileToDownload) {

		StandardFileSystemManager manager = new StandardFileSystemManager();

		try {

			// Initializes the file manager
			manager.init();

			// Setup our SFTP configuration
			FileSystemOptions opts = new FileSystemOptions();
			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
			// SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts,
			// true);
			SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

			if (userId.equals(IntgSrvAppConstants.STEP_USER_INT) || userId.equals(IntgSrvAppConstants.STEP_USER_PCM)) {
				SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
				SftpFileSystemConfigBuilder.getInstance().setPreferredAuthentications(opts, "publickey,keyboard-interactive,password");
			} else {
				SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
				// SftpFileSystemConfigBuilder.getInstance().setPreferredAuthentications(opts,
				// "publickey,keyboard-interactive,password");
			}

			// Create the SFTP URI using the host name, userid, password, remote
			// path and file name
			String sftpUri = "sftp://" + userId + ":" + password + "@" + hostName + "/" + destinationURL + fileToDownload;

			// Create local file object
			String filepath = originatedURL + fileToDownload;
			File file = new File(filepath);
			FileObject localFile = manager.resolveFile(file.getAbsolutePath());

			// Create remote file object
			FileObject remoteFile = manager.resolveFile(sftpUri, opts);

			// Copy local file to sftp server
			localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
			System.out.println("File download successful");

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (manager != null) {
				manager.close();
				IntgSrvUtils.printConsole("Manager closed");
			}
		}
		return true;
	}
}
