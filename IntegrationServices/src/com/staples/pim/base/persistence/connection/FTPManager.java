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

import org.apache.commons.net.ftp.FTPClient; 

import com.staples.pim.base.common.bean.FTPConnectionBean;

public class FTPManager {  
	 
	private String hostName = null;
	private String userId = null;
	private String password = null;
	private String destinationURL = null;
	private String originatedURL = null;
	private static FTPManager ftpManager = null;
	FTPClient ftpClient = null;
	
	boolean isConnected = false;
	boolean isLoginSucceed = false;
	boolean error = false;
	

	//private static PCMLogger LOGGER = PCMLogger.getInstance("Util",
					//SecureFTPManager.class.getName());
	
	public FTPManager(FTPConnectionBean ftpConnectionBean) {
		 hostName = ftpConnectionBean.getHostName();
		 userId = ftpConnectionBean.getUserId();
		 password = ftpConnectionBean.getPassword();
		 destinationURL = ftpConnectionBean.getDestinationUrl(); 
		 originatedURL = ftpConnectionBean.getOriginatedURL();
		 
		 ftpClient = new FTPClient();
		 this.getConnection();		
	}
	
	/**
	 * factory method for this class
	 * @return StplFTPClient
	 */
	public static FTPManager getInstance(FTPConnectionBean connectionBean)
	{	  
		try {
			ftpManager = new FTPManager(connectionBean);		
		}catch (Exception e) {
			e.printStackTrace();
			// loger
		}
		return ftpManager;
	}	
	
	/**
	 * the method to provide server connection and login
	 * @return
	 */
	private void getConnection()
	{	 
		isConnected = this.getServerConnection();	
		if (isConnected)
			isLoginSucceed = this.getServerLogin();		
	}
	
	/**
	 * this method gets FTP server connection
	 * @return
	 */
	private boolean getServerConnection()
	{
		boolean isConnected = false;
		
		return isConnected;
	}
	 
	
	/**
	 * this method gets FTP server connection
	 * @return
	 */
	private boolean getServerLogin()
	{ 
	    try
	    {
	       if (!ftpClient.login(userId, password))
	       {
	           ftpClient.logout();
	           isLoginSucceed = false;
	        } 
	        else
	            isLoginSucceed = true;
	        } 
	        catch (Exception e) 
			{ 
	           e.printStackTrace();
	           System.exit(1);
			}
	       return isLoginSucceed;
	}
	
	/**
	 * sets the file type to be transferred to FTP.ASCII_FILE_TYPE (value 0 ) 
	 * or FTP.BINARY_FILE_TYPE (value 2)
	 * @param fileType
	 */
	public void setTransferedFileType(int fileType)
	{
		try {
			ftpClient.setFileType(fileType);
		} catch (Exception e) {			 
			e.printStackTrace();
		}	
	}

	public void sendFile(String filePath)
			throws Exception {
		
		
	}

	public boolean completeTransfer()
	{
		boolean taskCompleted = false;
		try
		{
			ftpClient.logout();
			ftpClient.disconnect();
			 
		} catch (Exception e) 
		{  
			e.printStackTrace();
			// loger
		}
		return taskCompleted;
	} 
}
