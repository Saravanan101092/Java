package com.staples.pim.delegate.wercs.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;


public class WercsMailSender {

	private String SMTP_HOST_NAME = null;
	private String SMTP_AUTH_USER = null;
	private String SMTP_AUTH_PWD  = null;
	private String FROM_ADDRESS = null;
	private String TO_ADDRESS = null;

	private Session mailSession;

	private Properties smtpProperties = new Properties();
	private Authenticator authenticator = new SMTPAuthenticator();

	private static final String					TRACELOGGER_WERCS_WERCSMAIL			= "tracelogger.wercs.wercsmail";
	static IntgSrvLogger						logger									= IntgSrvLogger
			.getInstance(TRACELOGGER_WERCS_WERCSMAIL);
	
	public WercsMailSender(){

		try {
		     
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
	        smtpProperties.put("mail.transport.protocol", "smtp");
	        smtpProperties.put("mail.smtp.host", SMTP_HOST_NAME);
	        smtpProperties.put("mail.smtp.auth", "false");

			//creatFtpClient();
		} catch (Exception ex) {
			//LW-TODO: add EHF log entry here
			DatamigrationCommonUtil.printConsole(ex);
			IntgSrvUtils.printConsole("\nFAILURE 1\n");
		}
	   
   
	}
	
	/**
	 * Utility method to send text email messages.
	 * 
	 * @param emailSubject - subject for the email
	 * @param emailMessage - message to be sent
	 * @param toAddress - target email address
	 * */
	public boolean sendEmail(String emailSubject, String emailMessage,String toAddress,String ccAddress){

		mailSession = Session.getInstance(smtpProperties, authenticator); 
		mailSession.setDebug(true); // uncomment if wish to run in debug mode..
		Transport transport;
		try {
			transport = mailSession.getTransport();
			MimeMessage message = new MimeMessage(mailSession);
			message.setSubject(emailSubject);
			message.setContent(emailMessage, "text/html");

			message.setFrom(new InternetAddress(FROM_ADDRESS));
			if(!"".equals(toAddress) && toAddress != null){
				String[] toListArray = toAddress.split(";");
				for(int i=0;i<toListArray.length;i++){
					message.addRecipient(Message.RecipientType.TO,new InternetAddress(toListArray[i]));
				}

			} else{
				logger.error("To address is empty");
				message.addRecipient(Message.RecipientType.TO,new InternetAddress(TO_ADDRESS));	
			}
			
			if(!"".equals(ccAddress) && ccAddress != null){
				String[] toListArray = ccAddress.split(";");
				for(int i=0;i<toListArray.length;i++){
					message.addRecipient(Message.RecipientType.CC,new InternetAddress(toListArray[i]));
				}

			}
			
			transport.connect();        
			transport.sendMessage(message,message.getAllRecipients());
			
			transport.close();
			return true;
		}
		catch (NoSuchProviderException e) {
			logger.error("Exception caught while sending mail:"+e.getMessage());
			e.printStackTrace();
			return false;
		}
		catch (MessagingException e) {
			logger.error("Exception caught while sending mail:"+e.getMessage());
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			logger.error("Exception caught while sending mail:"+e.getMessage());
			e.printStackTrace();
			return false;
		}
		
	}

	/**
	 * Implementation for the Java Mail Authenticator Class.
	 * */
	private class  SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
	
	public static void main(String[] args){
		
		WercsMailSender sender = new WercsMailSender();
		sender.sendEmail("WERCS test mail", "<html><body> Dear ~|~REQUESTER~|~,<br/><br/> It has been determined that the item below is regulated. All products sold to Staples containing regulated materials will require registration with The WERCS. The WERCS helps Staples manage regulatory compliance requirements; they will review the product attributes and provide us with classification data to ensure the correct handling, transportation, storage, and disposal of products.<br/><br/><table border=1><tr><td><b>SUPPLIER</b></td><td><b>ITEM NO#</b></td><td><b>UPC</b></td><td><b>MODEL NO</b></td><td><b>Registration status</b></td></tr>~|~DYNAMIC_VALUE_NOT~|~</table><br/><br/> For your convenience a link to the WERCSmart Retail Portal has been provided. Please register the product within 2 business days. <a href=\"https://secure.supplierWERCS.com\">https://secure.supplierWERCS.com</a><br/> Questions regarding supplier participation in this initiative should be sent to WERCSinfo@staples.com. Staples WERCSmart team is ready to assist you on this initiative.<br/><br/> Thank you. </body></html>", "priyanka.venkat@staples.com", "saravanan.sampath@staples.com;sankar.suganya@staples.com");
		
	}
}
