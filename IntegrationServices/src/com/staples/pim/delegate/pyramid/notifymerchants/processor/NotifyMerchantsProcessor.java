package com.staples.pim.delegate.pyramid.notifymerchants.processor;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.assetImport.model.Asset;
import com.staples.pim.delegate.assetImport.runner.RunSchedulerAssetImport;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.pyramid.itemonbrdtmupdate.model.ItemOnbrdTmFeedBean;
import com.staples.pim.delegate.pyramid.notifymerchants.model.PyramidItemBean;
import com.staples.pim.delegate.pyramid.notifymerchants.reader.NotifyMerchantsItemReader;
import com.staples.pim.delegate.pyramid.notifymerchants.writer.NotifyMerchantsItemWriter;

public class NotifyMerchantsProcessor {
	protected IntgSrvLogger	ehfLogger	= IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	protected IntgSrvLogger	traceLogger	= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	protected String		clazzname	= this.getClass().getName();
	
	public static final String	inputDirectory =IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
			.getProperty(IntgSrvAppConstants.NOTIFY_MERCHANTS_UNPROCESSED_FOLDER));
	public static final String	 workingDirectory = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
			.getProperty(IntgSrvAppConstants.NOTIFY_MERCHANTS_TEMP_FOLDER));
	public static final String	 doneDirectory= IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
			.getProperty(IntgSrvAppConstants.NOTIFY_MERCHANTS_DONE_FOLDER));
public void processFiles()
{
	
	try {
		NotifyMerchantsItemReader notifyMerchantsItemReader=new NotifyMerchantsItemReader ();
		moveFiles(inputDirectory,workingDirectory);
		List<PyramidItemBean> itemList = new ArrayList<PyramidItemBean>();
		itemList= notifyMerchantsItemReader.read(workingDirectory);
		Map<String, List<PyramidItemBean>> itemListForMerchant = new HashMap<String, List<PyramidItemBean>>();
		itemListForMerchant=mapItemstoMerchant(itemList);
		NotifyMerchantsItemWriter notifyMerchantsItemWriter=new NotifyMerchantsItemWriter();
		notifyMerchantsItemWriter.writeAndEmail(itemListForMerchant);
		moveFiles(workingDirectory,doneDirectory);
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	public Map<String, List<PyramidItemBean>> mapItemstoMerchant(List<PyramidItemBean> pyramidItemBeanList) throws Exception {
		System.out.println("Inside procesor");
		System.out.println("size of list:::"+pyramidItemBeanList.size());
		Map<String, List<PyramidItemBean>> itemListForMerchant = new HashMap<String, List<PyramidItemBean>>();
		for (PyramidItemBean pyramidItemBean : pyramidItemBeanList) {
			System.out.println("inside iterrator");
		   String key = pyramidItemBean.getStaplesDotComPM();
		   if (!itemListForMerchant.containsKey(key)) {
			   itemListForMerchant.put(key, new ArrayList<PyramidItemBean>());
			   System.out.println("key::::::"+key);
		   }
		   itemListForMerchant.get(key).add(pyramidItemBean);
			}

		return itemListForMerchant;

	}
	
	public void moveFiles(String sourceDir,String destDir) {
		
		File dir = new File(sourceDir);
		File[] files = dir.listFiles();
		if (files == null) {
			System.out.println("File list is null!");
			traceLogger.info(clazzname, "run", "No input file found in before job ... ");
		} else {
			System.out.println("Moving " + files.length + "files in directory:" + dir.getName() +"to "+ destDir);
			int numfiles = files.length;
			traceLogger.info(clazzname, "run", "Number of input files: " + numfiles);
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					String destFileName = destDir + files[i].getName();
					File destFile = new File(destFileName);
					files[i].renameTo(destFile);
				}
			}
		}
	}

}
