package com.staples.pim.delegate.wercs.steptowercs.retry.runner;

import java.util.Calendar;

import oracle.jdbc.pool.OracleDataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.steptowercs.retry.processor.WercsRegStatusRetryProcessor;


public class RunSchedulerWercsRetry  extends RunScheduler {

	private static final String					TRACELOGGER_WERCS_WERCSRETRY			= "tracelogger.wercs.wercsretry";
	static IntgSrvLogger						logger									= IntgSrvLogger
			.getInstance(TRACELOGGER_WERCS_WERCSRETRY);
	
	public static OracleDataSource datasource;
	
	public static OracleDataSource pipdatasource;
	
	public OracleDataSource getPipdatasource() {
		
		return pipdatasource;
	}

	public void setPipdatasource(OracleDataSource pipdatasource) {
	
		this.pipdatasource = pipdatasource;
	}
	
	public OracleDataSource getDatasource() {

		return datasource;
	}

	public void setDatasource(OracleDataSource datasource) {

		this.datasource = datasource;
	}
	
	@Override
	public void run() {
		
		logger.info("Wercs Retry triggered "+Calendar.getInstance().getTime().toString());
		DatamigrationCommonUtil.printConsole("RunSchedulerStepToWercs triggered");
		logger.info("Context loaded");
		
		new WercsRegStatusRetryProcessor().processWercsRegStatusRetry();
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}

	public static void main(String[] args){
		
		ApplicationContext contextmaain = new FileSystemXmlApplicationContext("file:C:/integrationservicesworkspace/IntegrationServices/configurations/job-Scheduler-WercsRetry.xml");
	}
}
