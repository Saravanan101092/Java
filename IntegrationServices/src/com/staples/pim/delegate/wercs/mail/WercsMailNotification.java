package com.staples.pim.delegate.wercs.mail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.common.SBDatabaseHandlingService;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;
import com.staples.pim.delegate.wercs.mail.runner.RunSchedulerMailNotifications;
import com.staples.pim.delegate.wercs.model.MasterTableVO;
import com.staples.pim.delegate.wercs.steptowercs.runner.RunSchedulerStepToWercs;


public class WercsMailNotification {

	private static final String					TRACELOGGER_WERCS_WERCSMAIL			= "tracelogger.wercs.wercsmail";
	static IntgSrvLogger						logger									= IntgSrvLogger
			.getInstance(TRACELOGGER_WERCS_WERCSMAIL);

	public static final String RULES_TRIPPED_MAIL_TOADDRESS = "RULES_TRIPPEDMAIL_TOADDRESS";
	public static final String RT_DYNAMIC_CONTENT_STR = "DYNAMIC_VALUE_RT";
	public static final String DYNAMIC_CONTENT_STR = "DYNAMIC_VALUE_NOT";

	public static final int initialNotificationTimeFrameIC=Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_EMAIL_INITIAL_NOT_MIN_IC"));
	public static final int reminderNotificationTimeFrameIC=Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_EMAIL_REMINDER_NOT_MIN_IC"));
	public static final int finalNotificationTimeFrameIC=Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_EMAIL_FINALREMINDER_MIN_IC"));
	public static final int rejectedNotificationTimeFrameIC=Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_TIMETO_REJECT_ITEM"));

	public static final int initialNotificationTimeFrameIU=Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_EMAIL_INITIAL_NOT_MIN_IU"));
	public static final int reminderNotificationTimeFrameIU=Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_EMAIL_REMINDER_NOT_MIN_IU"));
	public static final int finalNotificationTimeFrameIU=Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_EMAIL_FINALREMINDER_MIN_IU"));
	public static final int rejectedNotificationTimeFrameIU=Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_TIMETO_REJECT_ITEM_IU"));

	public static final String rulesTrippedDynamicContent = IntgSrvPropertiesReader.getProperty(RT_DYNAMIC_CONTENT_STR);
	public static final String notificationDynamicContent = IntgSrvPropertiesReader.getProperty(DYNAMIC_CONTENT_STR);
	public static final String dynamicContentDelimiter = "~\\|~";
	public static final String dynamicValueDelimiter = "#\\|#";
	public static final String WERCS_ADMIN_MAILID= IntgSrvPropertiesReader.getProperty("WERCS_ADMIN_MAILID");

	public void sendRemainderMail(){

	}

	public void sendFinalNotificationMail(){

	}

	public void sendRejectedNotification(){

	}

	public void sendMAILNotification(List<MasterTableVO> initialNotificationItems,String initialEmailTemplate,String subject,String notificationType){

		SBDatabaseHandlingService dbAccess = new SBDatabaseHandlingService(RunSchedulerMailNotifications.datasource);

		//group based on supplierID
		Map<String,List<MasterTableVO>> groupedNotificationItems = groupItemsBasedOnSupplier(initialNotificationItems);
		for(String supplierID : groupedNotificationItems.keySet()){

			List<MasterTableVO> supplierWiseNotItems = groupedNotificationItems.get(supplierID);
			String dynamicContents = "";
			String toaddresses = "";
			String ccaddresses="";
			String requestorid="";
			String psid="";
			String requestorName ="";
			String merchantid="";

			for(MasterTableVO masterTableVO:supplierWiseNotItems){

				requestorid=masterTableVO.getRequestorID();
				psid=masterTableVO.getPsId();
				merchantid = masterTableVO.getMerchantId();
				requestorName= masterTableVO.getRequestorName();
				
				String tempContent = notificationDynamicContent.replaceFirst(dynamicValueDelimiter+"SUPPLIER"+dynamicValueDelimiter, masterTableVO.getSupplierName());
				tempContent = tempContent.replaceFirst(dynamicValueDelimiter+"ITEMNO"+dynamicValueDelimiter, masterTableVO.getPipid());
				tempContent = tempContent.replaceFirst(dynamicValueDelimiter+"UPC"+dynamicValueDelimiter, masterTableVO.getUPCNo());
				tempContent = tempContent.replaceFirst(dynamicValueDelimiter+"MODELNO"+dynamicValueDelimiter, masterTableVO.getModelno());
				tempContent = tempContent.replaceFirst(dynamicValueDelimiter+"REGSTATUS"+dynamicValueDelimiter, Integer.toString(masterTableVO.getRegistrationStatus()));
				dynamicContents+=tempContent;
				
				if(WercsAppConstants.WERCS_NOTIFICATION_INITIAL.equalsIgnoreCase(notificationType)){
					masterTableVO.setEventID(1);
				}else if(WercsAppConstants.WERCS_NOTIFICATION_REMINDER.equalsIgnoreCase(notificationType)){
					masterTableVO.setEventID(2);
				}else if(WercsAppConstants.WERCS_NOTIFICATION_FINAL.equalsIgnoreCase(notificationType)){
					masterTableVO.setEventID(3);
				}else{
					masterTableVO.setEventID(10);
				}
			}

			toaddresses+=WERCS_ADMIN_MAILID+WercsAppConstants.EMAILID_SEPERATOR+dbAccess.getUserEmailID(requestorid, logger);
			
			if(WercsAppConstants.WERCS_NOTIFICATION_INITIAL.equalsIgnoreCase(notificationType)){
				ccaddresses+=dbAccess.getUserEmailID(psid, logger);
			}else{
				ccaddresses+=dbAccess.getUserEmailID(psid, logger)+WercsAppConstants.EMAILID_SEPERATOR+dbAccess.getUserEmailID(merchantid, logger);
			}

			logger.info("Sending Mail. Toaddresses="+toaddresses+" CCaddresses="+ccaddresses);
			initialEmailTemplate = initialEmailTemplate.replaceFirst(dynamicContentDelimiter+DYNAMIC_CONTENT_STR+dynamicContentDelimiter, dynamicContents);
			
			if(WercsAppConstants.WERCS_NOTIFICATION_INITIAL.equalsIgnoreCase(notificationType) || WercsAppConstants.WERCS_NOTIFICATION_REMINDER.equalsIgnoreCase(notificationType)){
				initialEmailTemplate = initialEmailTemplate.replaceFirst(dynamicContentDelimiter+"REQUESTER"+dynamicContentDelimiter, requestorName);
			}else{
				initialEmailTemplate = initialEmailTemplate.replaceFirst(dynamicContentDelimiter+"SupplierContactName"+dynamicContentDelimiter, requestorName);
			}
			
			boolean isMailSendSuccessful = new WercsMailSender().sendEmail(subject, initialEmailTemplate, toaddresses, ccaddresses);

			if(isMailSendSuccessful){
				logger.info("Mail sent successfully");
				//updateDB after sending mail
				dbAccess.updateMailSentStatus(supplierWiseNotItems, logger);
				logger.info("Mail sent status updated in DB");
			}else{
				logger.error("Mail not sent.");
			}
		}
	}

	public List<MasterTableVO> getRulesTrippedDetailsFromDB(){
		SBDatabaseHandlingService dbAccess = new SBDatabaseHandlingService(RunSchedulerMailNotifications.datasource);
		logger.info("Getting items for send rules tripped mail from database.");
		List<MasterTableVO> rulesTrippedItems=dbAccess.getRulesTrippedDetails(logger);
		return rulesTrippedItems;
	}

	public void sendRulesTrippedMail(List<MasterTableVO> rulesTrippedItems){

		//Send rules tripped mail
		String emailTemplate = IntgSrvPropertiesReader.getProperty("RULES_TRIPPED_MAIL_NOTIFICATION");
		String dynamicContents="";
		for(MasterTableVO masterTableVO :rulesTrippedItems){
			String tempContent="";
			String upc = masterTableVO.getUPCNo();
			String itemdesc = masterTableVO.getItemdesc();
			tempContent=rulesTrippedDynamicContent.replaceFirst(dynamicValueDelimiter+"UPC"+dynamicValueDelimiter, upc);
			tempContent=tempContent.replaceFirst(dynamicValueDelimiter+"ITEMDESC"+dynamicValueDelimiter, itemdesc);
			dynamicContents+=tempContent;

			masterTableVO.setEventID(10);
		}

		emailTemplate=emailTemplate.replaceFirst(dynamicContentDelimiter+RT_DYNAMIC_CONTENT_STR+dynamicContentDelimiter, dynamicContents);
		String toAddress = IntgSrvPropertiesReader.getProperty(RULES_TRIPPED_MAIL_TOADDRESS);
		
		logger.info("Sending rules tripped mail. ToAddress="+toAddress);
		boolean isMailSendSuccessful = new WercsMailSender().sendEmail("Rules Tripped Status received", emailTemplate, toAddress, "");

		if(isMailSendSuccessful){
			
			logger.info("Mail Sent Successfully");
			//updateDB after sending mail
			SBDatabaseHandlingService dbAccess = new SBDatabaseHandlingService(RunSchedulerMailNotifications.datasource);
			dbAccess.updateMailSentStatus(rulesTrippedItems, logger);
			logger.info("Mail sent status updated in DB");
		}else{
			logger.error("Mail not sent successfully");
		}
		
	}

	public void processRulesTrippedMailNotifications(){

		logger.info("Processing rules tripped mail notifications");
		List<MasterTableVO> rulesTrippedItems = getRulesTrippedDetailsFromDB();
		if(rulesTrippedItems.size()>0){
			sendRulesTrippedMail(rulesTrippedItems);
		}else{
			logger.info("No items to send rules tripped mail.");
		}
	}

	public List<MasterTableVO> getRegStatusDetailsFromDB(){
		logger.info("Getting items for which mail notifications has to be sent.");
		SBDatabaseHandlingService dbAccess = new SBDatabaseHandlingService(RunSchedulerMailNotifications.datasource);
		List<MasterTableVO> mailNotificationItems=dbAccess.getRegStatusMailNotificationFromDB(logger);
		return mailNotificationItems;
	}

	public void processMailNotificationRegStatus(){
		
		List<MasterTableVO> allNotificationItems = getRegStatusDetailsFromDB();
		Map<String,List<MasterTableVO>> classifiedNotItems = classifyOnDateDifference(allNotificationItems);

		List<MasterTableVO> initialNotificationItems = classifiedNotItems.get(WercsAppConstants.WERCS_NOTIFICATION_INITIAL);
		if(initialNotificationItems.size()>0){
			String initialEmailTemplate = IntgSrvPropertiesReader.getProperty("INITIAL_MAIL_NOTIFICATION");
			String subject = IntgSrvPropertiesReader.getProperty("WERCS_INITIALNOT_SUBJECT");
			
			logger.info("Sending initial Notifications. Subject="+subject+"Template="+initialEmailTemplate);
			sendMAILNotification(initialNotificationItems,initialEmailTemplate,subject,WercsAppConstants.WERCS_NOTIFICATION_INITIAL);
		}

		List<MasterTableVO> reminderNotificationItems = classifiedNotItems.get(WercsAppConstants.WERCS_NOTIFICATION_REMINDER);
		if(reminderNotificationItems.size()>0){
			String initialEmailTemplate = IntgSrvPropertiesReader.getProperty("REMINDER_MAIL_NOTIFICATION");
			String subject = IntgSrvPropertiesReader.getProperty("WERCS_REMINDERNOT_SUBJECT");
			
			logger.info("Sending reminder Notifications. Subject="+subject+"Template="+initialEmailTemplate);
			sendMAILNotification(reminderNotificationItems,initialEmailTemplate,subject,WercsAppConstants.WERCS_NOTIFICATION_REMINDER);
		}

		List<MasterTableVO> finalNotificationItems = classifiedNotItems.get(WercsAppConstants.WERCS_NOTIFICATION_FINAL);
		if(finalNotificationItems.size()>0){
			String initialEmailTemplate = IntgSrvPropertiesReader.getProperty("FINAL_MAIL_NOTIFICATION");
			String subject = IntgSrvPropertiesReader.getProperty("WERCS_FINALNOT_SUBJECT");
			
			logger.info("Sending final Notifications. Subject="+subject+"Template="+initialEmailTemplate);
			sendMAILNotification(finalNotificationItems,initialEmailTemplate,subject,WercsAppConstants.WERCS_NOTIFICATION_FINAL);
		}

		List<MasterTableVO> rejectedNotificationItems = classifiedNotItems.get(WercsAppConstants.WERCS_NOTIFICATION_REJECTED);
		if(rejectedNotificationItems.size()>0){
			String initialEmailTemplate = IntgSrvPropertiesReader.getProperty("REJECTED_MAIL_NOTIFICATION");
			String subject = IntgSrvPropertiesReader.getProperty("WERCS_REJECTEDNOT_SUBJECT");
			
			logger.info("Sending rejected Notifications. Subject="+subject+"Template="+initialEmailTemplate);
			sendMAILNotification(rejectedNotificationItems,initialEmailTemplate,subject,WercsAppConstants.WERCS_NOTIFICATION_REJECTED);
		}
	}

	public Map<String,List<MasterTableVO>> groupItemsBasedOnSupplier(List<MasterTableVO> notificationItems){

		logger.info("Grouping items based on suppliers");
		
		Map<String,List<MasterTableVO>> groupedItems = new HashMap<String,List<MasterTableVO>>();

		for(MasterTableVO masterTableVO:notificationItems){
			String supplierID = masterTableVO.getSupplierId();
			if(groupedItems.containsKey(supplierID)){
				groupedItems.get(supplierID).add(masterTableVO);
			}else{
				List<MasterTableVO> masterTableList = new ArrayList<MasterTableVO>();
				masterTableList.add(masterTableVO);
				groupedItems.put(supplierID, masterTableList);
			}
		}

		logger.info("Total no of suppliers:"+groupedItems.size());
		return groupedItems;
	}

	public Map<String,List<MasterTableVO>> classifyOnDateDifference(List<MasterTableVO> allNotificationItems){
		
		logger.info("Classifying the items based on date difference.");
		logger.info("Total items retrieved from database="+allNotificationItems.size());
		
		List<MasterTableVO> initialNotificationItems = new ArrayList<MasterTableVO>();
		List<MasterTableVO> reminderNotificationItems = new ArrayList<MasterTableVO>();
		List<MasterTableVO> finalNotificationItems = new ArrayList<MasterTableVO>();
		List<MasterTableVO> rejectedNotificationItems = new ArrayList<MasterTableVO>();

		Calendar currentTime = Calendar.getInstance();

		for(MasterTableVO masterTableVO : allNotificationItems){

			int eventID = masterTableVO.getEventID();
			String transactionType = masterTableVO.getTranstype();

			Calendar rejectedCalendarTime = Calendar.getInstance();
			rejectedCalendarTime.setTimeInMillis(masterTableVO.getCreatedDate().getTime());

			Calendar finalCalendarTime = Calendar.getInstance();
			finalCalendarTime.setTimeInMillis(masterTableVO.getCreatedDate().getTime());

			Calendar reminderCalendarTime = Calendar.getInstance();
			reminderCalendarTime.setTimeInMillis(masterTableVO.getCreatedDate().getTime());

			if(transactionType.equals(WercsAppConstants.ITEMCREATE_STR)){
				rejectedCalendarTime.set(Calendar.MINUTE, rejectedCalendarTime.get(Calendar.MINUTE)+rejectedNotificationTimeFrameIC);
				finalCalendarTime.set(Calendar.MINUTE, finalCalendarTime.get(Calendar.MINUTE)+finalNotificationTimeFrameIC);
				reminderCalendarTime.set(Calendar.MINUTE, reminderCalendarTime.get(Calendar.MINUTE)+reminderNotificationTimeFrameIC);
			}else{
				rejectedCalendarTime.set(Calendar.MINUTE, rejectedCalendarTime.get(Calendar.MINUTE)+rejectedNotificationTimeFrameIU);
				finalCalendarTime.set(Calendar.MINUTE, finalCalendarTime.get(Calendar.MINUTE)+finalNotificationTimeFrameIU);
				reminderCalendarTime.set(Calendar.MINUTE, reminderCalendarTime.get(Calendar.MINUTE)+reminderNotificationTimeFrameIU);
			}

			Calendar createdCalendarTime = Calendar.getInstance();
			createdCalendarTime.setTimeInMillis(masterTableVO.getCreatedDate().getTime());

			if(currentTime.after(rejectedCalendarTime) && eventID!=10){
				rejectedNotificationItems.add(masterTableVO);
			}else if(currentTime.after(finalCalendarTime) && currentTime.before(rejectedCalendarTime) && eventID!=3){
				finalNotificationItems.add(masterTableVO);
			}else if(currentTime.after(reminderCalendarTime) && currentTime.before(finalCalendarTime) && eventID!=2){
				reminderNotificationItems.add(masterTableVO);
			}else if(currentTime.after(createdCalendarTime) && currentTime.before(reminderCalendarTime) && eventID!=1){
				initialNotificationItems.add(masterTableVO);
			}else{
				logger.info("Mail already sent for the item(UPC):"+masterTableVO.getUPCNo());
			}
		}

		logger.info("No of items for which initial notifications are to be sent = "+initialNotificationItems.size());
		logger.info("No of items for which reminder notifications are to be sent = "+reminderNotificationItems.size());
		logger.info("No of items for which final notifications are to be sent = "+finalNotificationItems.size());
		logger.info("No of items for which rejected notifications are to be sent = "+rejectedNotificationItems.size());
		
		Map<String,List<MasterTableVO>> classifiedNotItems = new HashMap<String, List<MasterTableVO>>();
		classifiedNotItems.put(WercsAppConstants.WERCS_NOTIFICATION_INITIAL, initialNotificationItems);
		classifiedNotItems.put(WercsAppConstants.WERCS_NOTIFICATION_REMINDER, reminderNotificationItems);
		classifiedNotItems.put(WercsAppConstants.WERCS_NOTIFICATION_FINAL, finalNotificationItems);
		classifiedNotItems.put(WercsAppConstants.WERCS_NOTIFICATION_REJECTED, rejectedNotificationItems);
		return classifiedNotItems;
	}

	public void processMailNotifications(){
		DatamigrationCommonUtil.printConsole("process mail notifications");
		String isTest = IntgSrvPropertiesReader.getProperty("WERCS_MAIL_RUNEVERYTIME");
		
		if(isTest.equalsIgnoreCase("TRUE")){
			logger.info("Testing mode. Runs every min");
			processMailNotificationRegStatus();
		}else{
			Calendar currentTime = Calendar.getInstance();
			int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
			if((currentHour>18) &&(currentHour<20)){
				logger.info("Processing mail notifications for all Reg Statuses");
				processMailNotificationRegStatus();
			}
		}
		processRulesTrippedMailNotifications();
	}

	public static void main(String[] args){
//		new RunSchedulerMailNotifications().run();
//		new WercsMailNotification().processMailNotificationRegStatus();
	}

}
