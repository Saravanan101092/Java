
package com.staples.pim.delegate.copyandprint.listener;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_CNP;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.TRUE_STR;

import java.util.Date;

import javax.jms.TextMessage;

import com.staples.pim.base.common.listenerandrunner.MessageQueueListener;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.copyandprint.processor.StepOutboundProcessor;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

/**
 * This class contains methods to listen the Step Queue for outbound integration
 * 
 * @author 522462
 * 
 */

public class StepOutboundConsumer extends MessageQueueListener {

	static IntgSrvLogger		logger		= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER_CNP);
	public static IntgSrvLogger	ehfLogger	= IntgSrvLogger.getInstance(EHF_LOGGER);				;

	StepOutboundProcessor		stepOutboundProcessor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message) This method
	 * to listen the Step Queue
	 */

	// public void onMessage(Message m, Session ses){
	//
	// try {
	// TextMessage message = (TextMessage) m;
	//
	// boolean isAck = processMessage(message);
	//
	// DatamigrationCommonUtil.printConsole("Acknowledge Status for Step Outbound : "+isAck);
	// logger.info("Acknowledge status for Step Outbound:"+isAck);
	//
	// if(isAck){
	// ses.commit();
	// logger.info("Successfully Committed for Step Outbound");
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
	// logger.info("Roll back called for Step Outbound.");
	// ses.rollback();
	// } else{
	// //FIXME once get confirmed the back out queue details
	// String logMessage = StepOutboundProcessor.ehfHandler.getErrorLog(new
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
	 * This method used to call the parsing method for galaxy out file and call
	 * the send method for step system.
	 * 
	 * @param message
	 *            message from galaxy system
	 */
	protected boolean processMessage(TextMessage message) {

		try {
			logger.info("\nActual message from STEP for outbound integration : " + message.getText());
			DatamigrationCommonUtil.printConsole("Actual message from STEP for outbound integration : " + message.getText());

			/*
			 * This Ignore flag and Ignore action is to used for to specify do
			 * not process the Data Migration items, Also the Config
			 * Specification from SB Configuration file
			 */
//			boolean ignoreFlag = DataMigIgnoreItemManager.getInstance().getIgnoreFlag(message.getText());
//			logger.info("DM Items ignoreFlag:" + String.valueOf(ignoreFlag));
			if(!IntgSrvUtils.isNullOrEmpty(message.getText())) {
				if(!message.getText().contains("CnPLifecycle")) {
					logger.info("Non CnP Items found in the above message- JMS Message Ignored\n");
					DatamigrationCommonUtil.printConsole("Non CnP Items found in the above message- JMS Message Ignored\n");
					if (TRUE_STR.equalsIgnoreCase(IntgSrvPropertiesReader.getProperty("debug.xml.generate.flag"))) {
						debugFileWrite(message.getText());
					}
					return true;
				}
			}
//			if (ignoreFlag) {
//				logger.info("DM Items found - JMS Message Ignored");
//				return true;
//			}

			return stepOutboundProcessor.stepOutBoundIntgProcess(message.getText());
			// return Boolean.TRUE;
		} catch (Throwable e) {
			DatamigrationCommonUtil.printConsole(DatamigrationCommonUtil.getClassAndMethodName() + "Throwable........");
			logger.info(e.getMessage());
			logger.error(e);
			return Boolean.FALSE;
		}
	}
	
	public static void debugFileWrite(String message) {

		logger.info("Writing non cnp file to FileBad.");
		String path = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
				.getProperty("CNP_OUTBOUND_XML_INPUT_DIRECTORY_FILE_BAD"));
		DatamigrationCommonUtil.writerFile(path + "DM_Item_"+new Date().getTime() + ".xml", message);
	}

	/**
	 * @return the stepOutboundProcessor
	 */
	public StepOutboundProcessor getStepOutboundProcessor() {

		return stepOutboundProcessor;
	}

	/**
	 * @param stepOutboundProcessor
	 *            the stepOutboundProcessor to set
	 */
	public void setStepOutboundProcessor(StepOutboundProcessor stepOutboundProcessor) {

		this.stepOutboundProcessor = stepOutboundProcessor;
	}

}
