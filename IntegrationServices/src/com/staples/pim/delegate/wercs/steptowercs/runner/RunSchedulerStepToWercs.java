package com.staples.pim.delegate.wercs.steptowercs.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.pool.OracleDataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.RunScheduler;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;


public class RunSchedulerStepToWercs extends RunScheduler {

	private static final String					TRACELOGGER_WERCS_STEPTOWERCS			= "tracelogger.wercs.steptowercs";
	static IntgSrvLogger						logger									= IntgSrvLogger
			.getInstance(TRACELOGGER_WERCS_STEPTOWERCS);
	
	public static final String jobContextFile = "/job-StepToWercs.xml";
	public static final String jobLauncherBean = "jobLauncher";
	public static final String jobBean = "stepToWercs";
	public static final String WERCS_INPUT_FOLDER = "WERCS_STEP_INPUT_FOLDER";
	
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
	
	public static ApplicationContext context;
	@Override
	public void run() {

		DatamigrationCommonUtil.printConsole("RunSchedulerStepToWercs triggered");
		context = new FileSystemXmlApplicationContext("file:"+IntgSrvUtils.reformatFilePath(IntgSrvUtils.getConfigDir()+jobContextFile));
	    
        JobLauncher jobLauncher = (JobLauncher) context.getBean(jobLauncherBean);
        Job job = (Job) context.getBean(jobBean);
        
        File inputFolder = new File(IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(WERCS_INPUT_FOLDER)));
        File[] inputFiles = inputFolder.listFiles();
        
        try {
        	Resource[] resources = new Resource[inputFiles.length];
        	
        	for(int i=0;i<inputFiles.length;i++){   
        		resources[i] = new FileSystemResource(inputFiles[i]);
        	}
        	
        	MultiResourceItemReader multiReader =(MultiResourceItemReader)context.getBean(WercsAppConstants.MULTIRESOURCE_BEAN);
        	multiReader.setResources(resources);
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            DatamigrationCommonUtil.printConsole("Job Exit Status : "+ execution.getStatus());
            List<Throwable> exceptions =execution.getAllFailureExceptions();
            for(Throwable th : exceptions){
            	DatamigrationCommonUtil.printConsole(th.getMessage());
            	th.printStackTrace();
            }
 
        } catch (JobExecutionException e) {
            DatamigrationCommonUtil.printConsole("Job ExamResult failed");
        	IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
            e.printStackTrace();
        }
		
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}

	public static void main(String args[]){
		new RunSchedulerStepToWercs().run();
	}
}
