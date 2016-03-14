package com.staples.pim.delegate.wercs.mail.runner;

import oracle.jdbc.pool.OracleDataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.wercs.mail.WercsMailNotification;


public class RunSchedulerMailNotifications  extends RunScheduler {

	private static final String					TRACELOGGER_WERCS_WERCSMAIL			= "tracelogger.wercs.wercsmail";
	static IntgSrvLogger						logger									= IntgSrvLogger
			.getInstance(TRACELOGGER_WERCS_WERCSMAIL);
	
	public static OracleDataSource datasource;
	
	public OracleDataSource getDatasource() {

		return datasource;
	}

	public void setDatasource(OracleDataSource datasource) {

		this.datasource = datasource;
	}
	
	
	@Override
	public void run() {
		
		logger.info("Mail notifications process triggered.");
		logger.info("Database context loaded successfully.");
		new WercsMailNotification().processMailNotifications();
		
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}

	public static void main(String[] args){
		new RunSchedulerMailNotifications().run();
	}
}
