package com.staples.pim.delegate.wercs.steptopip.runner;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtilConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;





public class RunSchedulerStepToPIP extends RunScheduler {
	
	/*
	 * REQUIRED DIRECTORIES
	 */
	public static String			WERCS_STEP_PIP_INPUT_DIR					= "WERCS_STEP_PIP_INPUT_DIR";
	
	
	/*
	 * JOB NAMES
	 */
	public static String			WERCS_STEP_PIP_JOB							= "stepTopip";
	
	/*
	 * Feed File information
	 */
	public static String			FEED_FILE_EXTN								= ".xml";
	public static String			FEED_FILE_NAME_DATE_FORMAT					= "yyyy_MM_dd_HH_mm_ss_SSSSSS";
	public static String			FEED_FILE_BEGIN								= IntgSrvPropertiesReader.getProperty("WERCS_STEP_PIP_FEED_FILE_BEGIN");
	
	public static ApplicationContext context;
	
	@Override
	public void run() {

		try {
			
			String wercsStepPipInputDir = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(WERCS_STEP_PIP_INPUT_DIR));
			IntgSrvUtils.printConsole("WERCS_STEP_PIP_INPUT_DIR = " + wercsStepPipInputDir);
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsStepPipInputDir));
			String jobContext = IntgSrvUtils.reformatFilePath(IntgSrvUtilConstants.CONFIG_DIR_LOCATION_DEFAULT + "/" + IntgSrvUtilConstants.STEP_PIP_CONTEXT_FILE);
			context = new FileSystemXmlApplicationContext("file:" + jobContext);
			
        	File inputFolder = new File(wercsStepPipInputDir);
            File[] inputFiles = inputFolder.listFiles();
        	List<File> fileList = getStepPipContractFeeds(Arrays.asList(inputFiles));
        	
        	if (fileList == null || fileList.size() == 0) 
			{
				IntgSrvUtils.printConsole("Step Pip Feeds not found");
				traceLogger.info(clazzname, "run", "Step Pip Feeds not found");
			}
        	else 
			{

				/*
				 * Sort based on time stamp from the file name
				 */
        		List<File> sortedFileList = sortBasedOnFileNameTimestamp(fileList);
            	
        		IntgSrvUtils.printConsole("Number of files:" + sortedFileList.size() + " in directory:" + sortedFileList);
            	
            	Resource[] resources = new Resource[sortedFileList.size()];
            	for(int i=0;i<sortedFileList.size();i++)
            	{
            		resources[i] = new FileSystemResource(sortedFileList.get(i).getAbsolutePath());
            	//	System.out.println(resources[i].getFilename());
            	}
            	
            	for (File file : sortedFileList) 
				{
					if (file.isFile()) 
					{
						MultiResourceItemReader multiReader =(MultiResourceItemReader)context.getBean("multiResourceReader");
		            	multiReader.setResources(resources);
    	
		                JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		                Job job = (Job) context.getBean(WERCS_STEP_PIP_JOB);
		                JobExecution execution = jobLauncher.run(job, new JobParameters());
		                
		                System.out.println("Exit status : " + execution.getStatus());
					}
				}
			}
 
        }
		catch (JobExecutionException e) 
		{
            System.out.println("Job ExamResult failed");
            e.printStackTrace();
        }
		
	}
	
	private List<File> getStepPipContractFeeds(List<File> sourceList) {

		List<File> contractFileList = new ArrayList<File>();

		for (File file : sourceList) 
		{
			String fileName = file.getName();
			if (file.isFile() && fileName.startsWith(FEED_FILE_BEGIN) && fileName.endsWith(FEED_FILE_EXTN)) {
				contractFileList.add(file);
			}
		}
		return contractFileList;
	}
	
	private List<File> sortBasedOnFileNameTimestamp(List<File> fileList)
	{

		ComparatorBasedOnFileNameTimestamp comparatorObj = new ComparatorBasedOnFileNameTimestamp();
		Collections.sort(fileList, comparatorObj);

		return fileList;
	}
	
	private class ComparatorBasedOnFileNameTimestamp implements Comparator<File> {

		@Override
		public int compare(File file1, File file2) {

			String file1TimestampString = file1.getName().replace(FEED_FILE_EXTN, DatamigrationAppConstants.EMPTY_STR);
			file1TimestampString = file1TimestampString.substring(findIndexOf(file1TimestampString,"_" , 2) + 1, file1TimestampString.length());
		
			String file2TimestampString = file2.getName().replace(FEED_FILE_EXTN, DatamigrationAppConstants.EMPTY_STR);
			file2TimestampString = file2TimestampString.substring(findIndexOf(file2TimestampString,"_" , 2) + 1, file2TimestampString.length());

			SimpleDateFormat sdf = new SimpleDateFormat(FEED_FILE_NAME_DATE_FORMAT);

			Date file1Date = null;
			Date file2Date = null;
			try {
				file1Date = sdf.parse(file1TimestampString);
				file2Date = sdf.parse(file2TimestampString);
			} catch (ParseException exception) {
				exception.printStackTrace();
	//			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerStepToPIP.PUBLISH_ID);
			}

			long file1epoch = file1Date.getTime();
			long file2epoch = file2Date.getTime();

			if (file1epoch < file2epoch) {
				return -1;
			} else {
				return 1;
			}
		}
	}
	
	public static int findIndexOf(String source, String pattern, int occurence) {

		int count = 0, current_position = 0, required_position = 0;

		while (count < occurence) {
			current_position = source.indexOf(pattern);
			if (current_position > -1) {
				source = source.substring(current_position + 1);
				required_position += current_position + 1;
				count++;
			} else {
				return -1;
			}
		}
		return required_position - 1;
	}

	@Override
	protected StepTransmitterBean jobLaunch(StepTransmitterBean transmitter) {

		return null;
	}

	
}
