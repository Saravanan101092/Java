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
 * File name     :   ProductAttributesProces
 * Creation Date :   
 * @author  
 * @version 1.0
 */ 

package com.staples.pim.base.util;

import java.io.File;
import java.util.Date;
import java.util.Properties;   

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;



public class EmailUtil { 
	 
	private String SMTP_HOST_NAME = null;
	private String SMTP_AUTH_USER = null;
	private String SMTP_AUTH_PWD  = null;
	private String FROM_ADDRESS = null;
	private String TO_ADDRESS = null;
	
	
    private Session mailSession;
    
    private Properties smtpProperties = new Properties();
    private Authenticator authenticator = new SMTPAuthenticator();
	private IntgSrvLogger traceLogger = IntgSrvLogger.getInstance("FreeformTraceLogger");
	private String clazzname = this.getClass().getName();
	
	
	public EmailUtil() {
		try {
			traceLogger.debug(clazzname, "SFTPUtil", "set host,user,password, target diretory"); 
		     
		    SMTP_HOST_NAME = IntgSrvPropertiesReader.getProperty(
					IntgSrvAppConstants.SMTP_HOST_NAME); 
		    SMTP_AUTH_USER = IntgSrvPropertiesReader.getProperty(
					IntgSrvAppConstants.SMTP_AUTH_USER); 
		    SMTP_AUTH_PWD = IntgSrvPropertiesReader.getProperty(
					IntgSrvAppConstants.SMTP_AUTH_PWD); 
		    FROM_ADDRESS = IntgSrvPropertiesReader.getProperty(
					IntgSrvAppConstants.FROM_ADDRESS); 
		    TO_ADDRESS = IntgSrvPropertiesReader.getProperty(
					IntgSrvAppConstants.TO_ADDRESS);   
		    
			IntgSrvUtils.printConsole("SMTP_HOST_NAME="+SMTP_HOST_NAME);
			IntgSrvUtils.printConsole("SMTP_AUTH_USER="+SMTP_AUTH_USER);
			traceLogger.debug(clazzname, "EmailUtil", "SMTP_HOST_NAME="+SMTP_HOST_NAME);
	        smtpProperties.put("mail.transport.protocol", "smtp");
	        smtpProperties.put("mail.smtp.host", SMTP_HOST_NAME);
	        smtpProperties.put("mail.smtp.auth", "false");

			//creatFtpClient();
		} catch (Exception ex) {
			//LW-TODO: add EHF log entry here
			System.out.println(ex);
			traceLogger.error(clazzname, "EmailUtil", ex);
			IntgSrvUtils.printConsole("\nFAILURE 1\n");
		}
	   
   }
   
    /**
     * Utility method to send text email messages.
     * 
     * @param emailSubject - subject for the email
     * @param emailMessage - message to be sent
     * */
     public void sendEmail(String emailSubject, String emailMessage){
       
     	mailSession = Session.getInstance(smtpProperties, authenticator); 
        mailSession.setDebug(true); // uncomment if wish to run in debug mode..
        Transport transport;
 		try {
 			transport = mailSession.getTransport();
 			MimeMessage message = new MimeMessage(mailSession);
 			message.setSubject(emailSubject);
 			message.setContent(emailMessage, "text/plain");
 			
 			message.setFrom(new InternetAddress(FROM_ADDRESS));
 	        if(!"".equals(TO_ADDRESS) && TO_ADDRESS != null){
 	        	String[] toListArray = TO_ADDRESS.split(";");
 	        	for(int i=0;i<toListArray.length;i++){
 	        		message.addRecipient(Message.RecipientType.TO,new InternetAddress(toListArray[i]));
 	        	}
 	        	
 	        } else{
 	        	message.addRecipient(Message.RecipientType.TO,new InternetAddress(TO_ADDRESS));	
 	        }
         	transport.connect();        
         	transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
         
 			transport.close();
 		}
 		catch (NoSuchProviderException e) {
 			e.printStackTrace();
 			//System.out.println(e);
 		}
 		catch (MessagingException e) {
 			e.printStackTrace();
 			//System.out.println(e);
 		}catch (Exception e) {
 			e.printStackTrace();
 			//System.out.println(e);
 		}
     }

    /**
     * Utility method to send text email messages.
     * 
     * @param emailSubject - subject for the email
     * @param emailMessage - message to be sent
     * @param toAddress - target email address
     * */
     public void sendEmail(String emailSubject, String emailMessage,String toAddress){
       
     	mailSession = Session.getInstance(smtpProperties, authenticator); 
        mailSession.setDebug(true); // uncomment if wish to run in debug mode..
        Transport transport;
 		try {
 			transport = mailSession.getTransport();
 			MimeMessage message = new MimeMessage(mailSession);
 			message.setSubject(emailSubject);
 			message.setContent(emailMessage, "text/plain");
 			
 			message.setFrom(new InternetAddress(FROM_ADDRESS));
 	        if(!"".equals(toAddress) && toAddress != null){
 	        	String[] toListArray = toAddress.split(";");
 	        	for(int i=0;i<toListArray.length;i++){
 	        		message.addRecipient(Message.RecipientType.TO,new InternetAddress(toListArray[i]));
 	        	}
 	        	
 	        } else{
 	        	message.addRecipient(Message.RecipientType.TO,new InternetAddress(TO_ADDRESS));	
 	        }
         	transport.connect();        
         	transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
         
 			transport.close();
 		}
 		catch (NoSuchProviderException e) {
 			e.printStackTrace();
 		}
 		catch (MessagingException e) {
 			e.printStackTrace();
 		}catch (Exception e) {
 			e.printStackTrace();
 		}
     }
     
     /**
      * Utility method to send text email messages with an attachment file.
      * Even multiple files can be zipped together and sent as a single attachment file.
      * @param emailSubject - for the email
      * @param emailMessage - message to be sent
      * @param toAddress - target email address
      * @param attachmentFile - file to be emailed as an attachment.
      * */
     public void sendEmail(String emailSubject, String emailMessage,String toAddress, File attachmentFile){
     	 Session mailSession = Session.getInstance(smtpProperties, authenticator);
     	 Multipart multiPart = new MimeMultipart();
     	 MimeBodyPart messagePart = new MimeBodyPart();
     	 MimeBodyPart attachment = new MimeBodyPart();
     	 FileDataSource fileDataSource = null;
          //mailSession.setDebug(true); // uncomment if wish to run in debug mode..
          Transport transport;
          try {
  			transport = mailSession.getTransport();
  			MimeMessage message = new MimeMessage(mailSession);
  			message.setSubject(emailSubject);
  			message.setFrom(new InternetAddress(FROM_ADDRESS));
  			if(!"".equals(toAddress) && toAddress != null){
  				String[] toListArray = toAddress.split(";");
 	        	for(int i=0;i<toListArray.length;i++){
 	        		message.addRecipient(Message.RecipientType.TO,new InternetAddress(toListArray[i]));
 	        	}
  			} else {
  				message.addRecipient(Message.RecipientType.TO,new InternetAddress(TO_ADDRESS));	
  			}

  			messagePart.setText(emailMessage);
  			fileDataSource = new FileDataSource(attachmentFile);
  			attachment.setDataHandler(new DataHandler(fileDataSource));
  			attachment.setFileName(fileDataSource.getName());
          
  			multiPart.addBodyPart(messagePart);
  			multiPart.addBodyPart(attachment);
  			message.setContent(multiPart);
  			message.setSentDate(new Date());
  			transport.connect();
  			transport.sendMessage(message,
  			message.getRecipients(Message.RecipientType.TO));
  			transport.close();
  		}
  		catch (NoSuchProviderException e) {
  			e.printStackTrace();
  		}
  		catch (MessagingException e) {
  			e.printStackTrace();
  		}catch (Exception e) {
 			e.printStackTrace();
 		}
     }

     
     /**
      * Utility method to send text email messages with multiple attachment files.
      * @param emailSubject - for the email
      * @param emailMessage - message to be sent
      * @param toAddress - target email address
      * @param attachmentFiles - files to be emailed as an attachment.
      * */
     public void sendEmail(String emailSubject, String emailMessage,String toAddress, File[] attachmentFiles){
     	 Session mailSession = Session.getInstance(smtpProperties, authenticator);
     	 Multipart multiPart = new MimeMultipart();
     	 MimeBodyPart messagePart = new MimeBodyPart();
     	 MimeBodyPart attachment =null;
     	 FileDataSource fileDataSource = null;
          //mailSession.setDebug(true); // uncomment if wish to run in debug mode..
          Transport transport;
  		 
          try {
  			transport = mailSession.getTransport();
  			MimeMessage message = new MimeMessage(mailSession);
  			message.setSubject(emailSubject);
  			message.setFrom(new InternetAddress(FROM_ADDRESS));
  			if(!"".equals(toAddress) && toAddress != null){
  				String[] toListArray = toAddress.split(";");
 	        	for(int i=0;i<toListArray.length;i++){
 	        		message.addRecipient(Message.RecipientType.TO,new InternetAddress(toListArray[i]));
 	        	}
  			} else{
  				message.addRecipient(Message.RecipientType.TO,new InternetAddress(TO_ADDRESS));	
  			}

  			messagePart.setText(emailMessage);
  			multiPart.addBodyPart(messagePart);
  			for(File attachmentFile:attachmentFiles){
  				attachment = new MimeBodyPart();
  				fileDataSource = new FileDataSource(attachmentFile);
  				attachment.setDataHandler(new DataHandler(fileDataSource));
  				attachment.setFileName(fileDataSource.getName());
  				multiPart.addBodyPart(attachment);
  			}
  			message.setContent(multiPart);
  			message.setSentDate(new Date());
  			transport.connect();
  			transport.sendMessage(message,
             message.getRecipients(Message.RecipientType.TO));
  			transport.close();
  		}
  		catch (NoSuchProviderException e) {
  			e.printStackTrace();
  		}
  		catch (MessagingException e) {
  			e.printStackTrace();
  		}catch (Exception e) {
 			e.printStackTrace();
 		}
     }
     /**
      * Implementation for the Java Mail Authenticator Class.
      * */
     private class  SMTPAuthenticator extends javax.mail.Authenticator {
         public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            //System.out.println("SMTPAuthenticator");
            //System.out.println("username :"+username+"password :"+password);
            return new PasswordAuthentication(username, password);
         }
     }
	
     public static void main(String[] args) {
 		System.out.println("Email test start ...");
 		EmailUtil emailUtil = new EmailUtil();
 		//String fileToFTP = "stibo_product_09_02_12_41_13_013.xsv";
 		//fTPUtil.uploadFile(fileToFTP);
 		String mailList = "sima.zaslavsky@staples.com";
 		emailUtil.sendEmail("Test", "Hello,\n This is a test, please ignore it\n",mailList);
 		System.out.println("Email test end!");
 	}
}
