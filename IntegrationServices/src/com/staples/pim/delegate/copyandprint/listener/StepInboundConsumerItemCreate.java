
package com.staples.pim.delegate.copyandprint.listener;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_CNP;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ITEMCREATE;

import javax.jms.TextMessage;

import com.staples.pim.base.common.listenerandrunner.MessageQueueListener;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.copyandprint.inbound.domain.StepInboundProcessor;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

/**
 * This class contains methods to listen the ItemCreate message from PIMCore-ESB
 * Queue
 * 
 * @author 522462
 */

public class StepInboundConsumerItemCreate extends MessageQueueListener {

	static IntgSrvLogger		logger		= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER_CNP);
	public static IntgSrvLogger	ehfLogger	= IntgSrvLogger.getInstance(EHF_LOGGER);				;

	StepInboundProcessor		stepInboundProcessor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.jms.listener.SessionAwareMessageListener#onMessage
	 * (javax.jms.Message, javax.jms.Session)
	 */
	// public void onMessage(Message m, Session ses){
	//		
	// try {
	// TextMessage message = (TextMessage) m;
	//			
	// boolean isAck = processMessage(message);
	//			 
	// DatamigrationCommonUtil.printConsole("Acknowledge Status for Itemcreate : "+isAck);
	// logger.info("Acknowledge status for Itemcreate :"+isAck);
	//			
	// if(isAck){
	// ses.commit();
	// logger.info("Successfully Committed for Itemcreate");
	// }else{
	//				
	// int deliveryCount = m.getIntProperty(JMS_DELIVERY_COUNT);
	// int maxCount =
	// CommonUtils.toInt(DatamigrationCommonUtil.getPropertiesValue(ROLLBACK_MAX_LIMIT));
	//				
	// DatamigrationCommonUtil.printConsole("DeliveryCount :"+deliveryCount
	// +" : max count :"+maxCount);
	// logger.info("DeliveryCount :"+deliveryCount +" : max count :"+maxCount);
	//				
	// if (deliveryCount <= maxCount) {
	// logger.info("Roll back called for Itemcreate.");
	// ses.rollback();
	// } else{
	// //FIXME once get confirmed the back out queue details
	// String logMessage = StepInboundProcessor.ehfHandler.getErrorLog(new
	// Date(), StepInboundProcessor.traceId, EHF_ERROR_PATH2, new
	// Exception("Roll back maximum limit reached....Exception: Not able to publish the message to destination queue"),
	// EHF_SPRINGBATCH_ITEMUTILITY_USER,
	// DatamigrationCommonUtil.getMethodName(),
	// DatamigrationCommonUtil.getClassName(), StepInboundProcessor.ehfICD);
	// StepInboundProcessor.ehfLogger.error(logMessage);
	// logger.info("Roll back maximum limit reached...Exception: Not able to publish the message to destination queue");
	// ses.commit();
	// }
	// //throw new
	// RuntimeException("Message could not be processed. Roll back transaction");
	//				
	// }
	// } catch (JMSException e1) {
	// DatamigrationCommonUtil.printConsole(e1.getMessage());
	// logger.info(e1.getMessage());
	// logger.error(e1);
	// }
	// DatamigrationCommonUtil.printConsole("End");
	// }

	/**
	 * This method used to call the Step In bound integration process for
	 * converting PIMCore XML to Step XML.
	 * 
	 * @param message
	 *            message from PIMCore - ESB system
	 */
	protected boolean processMessage(TextMessage message) {

		try {
			logger.info("Actual message from PIMCore for ItemCreate : " + message.getText());
			DatamigrationCommonUtil.printConsole("Actual message from PIMCore for ItemCreate : " + message.getText());
			return stepInboundProcessor.stepInboundIntgProcess(message.getText(), ITEMCREATE);
		} catch (Throwable e) {
			DatamigrationCommonUtil.printConsole(DatamigrationCommonUtil.getClassAndMethodName() + "Throwable........");
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
	 * @param stepInboundProcessor
	 *            the stepInboundProcessor to set
	 */
	public void setStepInboundProcessor(StepInboundProcessor stepInboundProcessor) {

		this.stepInboundProcessor = stepInboundProcessor;
	}

}
