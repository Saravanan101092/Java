package com.staples.pim.delegate.wercs.steptopip.runner;

import org.springframework.batch.core.JobExecution;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.core.io.Resource;

import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.base.common.listenerandrunner.BatchJobListener;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.wercs.model.MasterTableVO;




public class WercsStepToPipJobListener extends BatchJobListener implements ItemReadListener<STEPProductInformation>,ItemProcessListener<STEPProductInformation, MasterTableVO>,SkipListener<STEPProductInformation, Throwable>{

	public static String			WERCS_STEP_PIP_DONE_DIR						= "WERCS_STEP_PIP_DONE_DIR";
	public static String			WERCS_STEP_PIP_BAD_DIR						= "WERCS_STEP_PIP_BAD_DIR";
	String							wercsStepPipDoneDir 						= IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(WERCS_STEP_PIP_DONE_DIR));
	String							wercsStepPipBadDir 							= IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(WERCS_STEP_PIP_BAD_DIR));
	public static final String[] mandatoryAttributes = {"A0080"};
	public static List<File> succeededFiles;
		@Override
		public void afterJob(JobExecution jobExecution) {
		//	System.out.println("listener :after job");
			
			
			IntgSrvUtils.printConsole("WERCS_STEP_PIP_DONE_DIR = " + wercsStepPipDoneDir);
			IntgSrvUtils.printConsole("WERCS_STEP_PIP_BAD_DIR = " + wercsStepPipBadDir);
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsStepPipDoneDir));	
			createWriterOutputDir(IntgSrvUtils.reformatFilePath(wercsStepPipBadDir));
			for(File file:succeededFiles)
			{
				File destinationFile = new File(wercsStepPipDoneDir + file.getName());
				System.out.println(destinationFile.getAbsolutePath());
				try 
				{
					Files.move(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
				catch (IOException e) 
				{
			//		System.out.println("Error while copying file to File_Done folder."+e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		protected void createWriterOutputDir(String directoryName) {
			File outDir= new File (directoryName);
			if (!outDir.exists()){
				if (outDir.mkdirs()) IntgSrvUtils.printConsole("LW-DEBUG: RunScheduler.createWriterOutputDirs | output dir CREATED: " + directoryName);
			}
		}

		@Override
		public void beforeJob(JobExecution jobExecution) {
	//		System.out.println("listener :before job");
			succeededFiles = new ArrayList<File>();
		}

		@Override
		public void afterProcess(STEPProductInformation arg0, MasterTableVO arg1) {

	//		System.out.println("listener: afterprocess");
		}


		@Override
		public void beforeProcess(STEPProductInformation arg0) {

	//		System.out.println("listener: beforerprocess");
		}


		@Override
		public void onProcessError(STEPProductInformation arg0, Exception arg1) {

	//		System.out.println("listener: onprocesserrorprocess");
		}

		@Override
		public void onSkipInProcess(STEPProductInformation arg0, Throwable arg1) {

	//		System.out.println("listener: onskiptinprocess : "+arg0.getProducts().getProduct().get(0).getID());
		}

		@Override
		public void onSkipInRead(Throwable arg0) {

	//		System.out.println("listener: onskipinread");
		}

		@Override
		public void onSkipInWrite(Throwable arg0, Throwable arg1) {

	//		System.out.println("listener: onskipinwrite");
		}

		@Override
		public void afterRead(STEPProductInformation stepProductInformation) {
	//		System.out.println("listener: afterRead");
			MultiResourceItemReader multiResourceItemReader = 	(MultiResourceItemReader)RunSchedulerStepToPIP.context.getBean("multiResourceReader");
			Resource currentResource = multiResourceItemReader.getCurrentResource();
			//validate input xml. check if all mandatory values are present
			if(!containsAllMandatoryValues(stepProductInformation))
			{
				try 
				{
					File badFile = currentResource.getFile();
					
					File destinationFile = new File(wercsStepPipBadDir + badFile.getName());
					System.out.println(destinationFile.getAbsolutePath());
					Files.move(badFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				try 
				{
					File succeededFile = currentResource.getFile();
					succeededFiles.add(succeededFile);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		@Override
		public void beforeRead() {
	//		System.out.println("listener: beforeRead");
			
		}

		@Override
		public void onReadError(Exception arg0) {
	//		System.out.println("listener: onreadError");
			
		}
		
		public boolean containsAllMandatoryValues(STEPProductInformation stepProductInformation){
			Map<String,String> valuesInXml = new HashMap<String,String>();

			ProductsType products = stepProductInformation.getProducts();
			List<ProductType> productList = products.getProduct();
			for(ProductType product:productList)
			{
				valuesInXml.put("STEP_ID", product.getID());
				for(Object productObj : product.getProductOrSequenceProductOrSuppressedProductCrossReference())
				{
					if("ValuesType".equalsIgnoreCase(productObj.getClass().getSimpleName()))
					{
						ValuesType values = (ValuesType) productObj;
						for(Object valueObj : values.getValueOrMultiValueOrValueGroup())
						{
							if("ValueType".equalsIgnoreCase(valueObj.getClass().getSimpleName()))
							{
								ValueType value = (ValueType) valueObj;
								valuesInXml.put(value.getAttributeID(), value.getContent());
							}

						}
					}
				}
			}
			
			for(String mandatoryAttribute:mandatoryAttributes)
			{
				if(IntgSrvUtils.isNullOrEmpty(valuesInXml.get(mandatoryAttribute)))
				{
					return false;
				}
			}
			return true;
		}
}
