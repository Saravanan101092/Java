
package com.staples.pim.delegate.copyandprint.domain;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.MQ_MAX_RETRY_COUNT;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.THREAD_SLEEP_INTERVAL_MS;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.ContextLoaderFactory;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtilConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

public class CustomException extends Throwable implements ExceptionListener {

	static IntgSrvLogger	logger				= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER);

	public static int		retryCount			= 0;

	public String			instanceName;
	
	public boolean			isMessageResendReqd=false;

	// Thread sleep interval to check the MQ availability. The value is in
	// millisecond ie. 1000ms=1second, 300000ms = 5minutes
	int						threadSleepInterval	= IntgSrvUtils
														.toInt((String) IntgSrvPropertiesReader.getProperty(THREAD_SLEEP_INTERVAL_MS));

	// Re-try maximum count for Galaxy MQ availability check
	int						retryCountMQCheck	= IntgSrvUtils.toInt((String) IntgSrvPropertiesReader.getProperty(MQ_MAX_RETRY_COUNT));

	public String getInstanceName() {

		return instanceName;
	}

	public void setInstanceName(String instanceName) {

		this.instanceName = instanceName;
	}
	
	public boolean getIsMessageResendReqd() {

		return isMessageResendReqd;
	}

	public boolean setIsMessageResendReqd(boolean isMessageResendReqd) {

		return  isMessageResendReqd;
	}

	public void onException(JMSException arg0) {

		System.err.println("Exception occurred " + arg0);
		DatamigrationCommonUtil.printConsole("Instance Name:" + instanceName);
		logger.warn("Instance Name:" + instanceName);
		logger.warn("JMS Exception:" + arg0);
		logger.info("Retry count:" + retryCount + " Max rety limit:" + retryCountMQCheck);
		retryCount++;

		if (retryCount == retryCountMQCheck) {

			DatamigrationCommonUtil.printConsole("Inside sleep block...");
			retryCount = 0;
			DatamigrationCommonUtil.printConsole("Listener re-try limit is reached");
			logger.info("Listener re-try limit is reached. Spring batch ItemUtility going to sleep for next " + threadSleepInterval
					+ " milliseconds");

			try {
				DatamigrationCommonUtil.printConsole("before thread sleep");

				// SpringBatchItemUtility sleep for sometime
				Thread.sleep(threadSleepInterval);

				DatamigrationCommonUtil.printConsole("after thread sleep");
				logger.info("Listener re-try is started..");

			} catch (InterruptedException e) {
				logger.info("Exception in Thread sleep :", e);
			}

			// Here load the context.xml file and refreshed to make the
			// connectivity again.
			DatamigrationCommonUtil.printConsole("Context refresh start");
			if (instanceName.equalsIgnoreCase("copyandprint")) {
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.COPYANDPRINT_CONTEXT_FILE);
			} else if (instanceName.equalsIgnoreCase("itemutility")) {
				ContextLoaderFactory.getInstanceLoadContext().loadContext(IntgSrvUtilConstants.ITEMUTILITY_CONTEXT_FILE);
			}
			// ContextLoader.getInstanceLoadContext().loadContext(EMPTY_STR);
			DatamigrationCommonUtil.printConsole("Context refresh end");
			logger.info("Context file is refreshed");
		}
	}

}
