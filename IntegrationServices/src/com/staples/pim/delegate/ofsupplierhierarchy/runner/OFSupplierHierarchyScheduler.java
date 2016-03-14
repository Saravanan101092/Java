package com.staples.pim.delegate.ofsupplierhierarchy.runner;

import java.util.Date;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.ofsupplierhierarchy.processor.OFSupplierHierarchyProcessor;


public class OFSupplierHierarchyScheduler extends RunScheduler{

	public static final String	FREEFORM_TRACELOGGER_ORACLE_FINANCIAL	= "tracelogger.oracle.financial";

	static IntgSrvLogger		logger							= IntgSrvLogger.getInstance(FREEFORM_TRACELOGGER_ORACLE_FINANCIAL);
	public static String		PUBLISH_ID						= "OFNE001";

	/**
	 * 
	 */
	public void run() {

		logger.info(new Date().toString() + " Oracle financial scheduler invoked");
		DatamigrationCommonUtil.printConsole(" Oracle financial scheduler invoked");
		OFSupplierHierarchyProcessor oracleFinancialProcessor = new OFSupplierHierarchyProcessor();
		oracleFinancialProcessor.processOracleFinancial();
		logger.info("Oracle financial file processing complete.");
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}
}
