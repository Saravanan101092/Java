package com.staples.pim.delegate.copyandprint.listener;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_CNP;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ITEMUPDATE;

import javax.jms.TextMessage;

import com.staples.pim.base.common.listenerandrunner.MessageQueueListener;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.copyandprint.inbound.domain.StepInboundProcessor;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

public class StepInboundConsumerItemUpdate extends MessageQueueListener {
	
	static IntgSrvLogger		logger		= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER_CNP);
	public static IntgSrvLogger	ehfLogger	= IntgSrvLogger.getInstance(EHF_LOGGER);
	StepInboundProcessor stepInboundProcessor;
	

//	public void onMessage(Message m, Session ses){
//		
//		try {
//			TextMessage message = (TextMessage) m;
//			
//			boolean isAck = processMessage(message); 
//			 
//			CommonUtil.printConsole("Acknowledge Status for Itemupdate : "+isAck);
//			logger.info("Acknowledge status for ItemUpdate :"+isAck);
//			
//			if(isAck){
//				ses.commit();
//				logger.info("Successfully Committed for ItemUpdate");
//			}else{
//				
//				int deliveryCount = m.getIntProperty(JMS_DELIVERY_COUNT);
//				int maxCount = CommonUtils.toInt(CommonUtil.getPropertiesValue(ROLLBACK_MAX_LIMIT));
//				
//				CommonUtil.printConsole("DeliveryCount :"+deliveryCount +" : max count :"+maxCount);
//				logger.info("DeliveryCount :"+deliveryCount +" : max count :"+maxCount);
//				
//				if (deliveryCount <= maxCount) {
//					logger.info("Roll back called for Itemupdate.");
//					ses.rollback();
//				} else{
//					//FIXME once get confirmed the back out queue details
//					String logMessage = StepInboundProcessor.ehfHandler.getErrorLog(new Date(), StepInboundProcessor.traceId, EHF_ERROR_PATH2, new Exception("Roll back maximum limit reached....Exception: Not able to publish the message to destination queue"), EHF_SPRINGBATCH_ITEMUTILITY_USER, CommonUtil.getMethodName(), CommonUtil.getClassName(), StepInboundProcessor.ehfICD);
//					StepInboundProcessor.ehfLogger.error(logMessage);
//					logger.info("Roll back maximum limit reached...Exception: Not able to publish the message to destination queue");
//					ses.commit();
//				}
//				 //throw new RuntimeException("Message could not be processed. Roll back transaction");
//				
//			} 
//		} catch (JMSException e1) {
//			CommonUtil.printConsole(e1.getMessage());
//			logger.info(e1.getMessage());
//			logger.error(e1);
//		}
//		CommonUtil.printConsole("End");
//	}
	
	/**
	 * This method used to call the Step In bound integration process for converting PIMCore XML to Step XML.
	 * @param message
	 *            message from PIMCore - ESB system
	 */
	protected boolean processMessage(TextMessage message) {

		try {
			logger.info("Actual message from PIMCore for Itemupdate :"
					+ message.getText());
			DatamigrationCommonUtil.printConsole("Actual message from PIMCore for Itemupdate :" + message.getText());
			return stepInboundProcessor.stepInboundIntgProcess(message.getText(),ITEMUPDATE);
		}catch (Throwable e) {
			DatamigrationCommonUtil.printConsole(DatamigrationCommonUtil.getClassAndMethodName()+"Throwable........");
			logger.info(e.getMessage());
			logger.error(e);
			return Boolean.FALSE;
		}
	}

	/**
	 * @return the stepInboundProcessor
	 */
	public StepInboundProcessor getStepInboundProcessor() {
		return stepInboundProcessor;
	}

	/**
	 * @param stepInboundProcessor the stepInboundProcessor to set
	 */
	public void setStepInboundProcessor(StepInboundProcessor stepInboundProcessor) {
		this.stepInboundProcessor = stepInboundProcessor;
	}

	/**
	 * @param stepXMLGenerator
	 */
}
