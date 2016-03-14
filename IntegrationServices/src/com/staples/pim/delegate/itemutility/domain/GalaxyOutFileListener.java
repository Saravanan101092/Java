
package com.staples.pim.delegate.itemutility.domain;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_ITEMUTLITY;

import javax.jms.TextMessage;

import com.staples.pim.base.common.listenerandrunner.MessageQueueListener;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.itemutility.processor.StepXMLGenerator;

/**
 * This class contains methods to listen the Galaxy Queue
 * 
 * @author 522462
 * 
 */

public class GalaxyOutFileListener extends MessageQueueListener {

	static IntgSrvLogger	logger	= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER_ITEMUTLITY);

	StepXMLGenerator		stepXMLGenerator;

	public StepXMLGenerator getStepXMLGenerator() {

		return stepXMLGenerator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message) This method
	 * to listen the Galaxy Queue
	 */
	/*
	 * public void onMessage(Message m) { TextMessage message = (TextMessage) m;
	 * processMessage(message); }
	 */

	// public void onMessage(Message m, Session ses){
	//		
	// try {
	// TextMessage message = (TextMessage) m;
	//			
	// boolean isAck = processMessage(message);
	//			
	// DatamigrationCommonUtil.printConsole("Acknowledge Status : "+isAck);
	// logger.info("Acknowledge status :"+isAck);
	//			
	// if(isAck){
	// ses.commit();
	// logger.info("Successfully Committed");
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
	// logger.info("Roll back called..");
	// ses.rollback();
	// } else{
	// //FIXME once get confirmed the back out queue details
	// String logMessage = StepXMLGenerator.ehfHandler.getErrorLog(new Date(),
	// StepXMLGenerator.traceId, EHF_ERROR_PATH2, new
	// Exception("Roll back maximum limit reached....Exception: Not able to publish the message to destination queue"),
	// EHF_SPRINGBATCH_ITEMUTILITY_USER_ITEMUTILITY,
	// DatamigrationCommonUtil.getMethodName(),
	// DatamigrationCommonUtil.getClassName(), StepXMLGenerator.ehfICD);
	// StepXMLGenerator.ehfLogger.error(logMessage);
	// logger.info("Roll back maximum limit reached...Exception: Not able to publish the message to destination queue");
	// }
	// //throw new
	// RuntimeException("Message could not be processed. Roll back transaction");
	//				
	// }
	// } catch (JMSException e1) {
	// logger.info(e1.getMessage());
	// logger.error(e1);
	// }
	// DatamigrationCommonUtil.printConsole("End");
	// }

	/**
	 * This method used to call the parsing method for galaxy out file and call
	 * the send method for step system.
	 * 
	 * @param message
	 *            message from galaxy system
	 */
	protected boolean processMessage(TextMessage message) {

		try {
			logger.info("Actual message from Galaxy Queue : " + message.getText());
			DatamigrationCommonUtil.printConsole("Galaxy message : " + message.getText());
			return stepXMLGenerator.getStepInputFromGalaxy(message.getText());
		} catch (Throwable e) {
			e.printStackTrace();
			DatamigrationCommonUtil.printConsole("processMessage.......Throwable........");
			logger.info(e.getMessage());
			logger.error(e);
			return Boolean.FALSE;
		}
	}

	/**
	 * @param stepXMLGenerator
	 */
	public void setStepXMLGenerator(StepXMLGenerator stepXMLGenerator) {

		this.stepXMLGenerator = stepXMLGenerator;
	}

}
