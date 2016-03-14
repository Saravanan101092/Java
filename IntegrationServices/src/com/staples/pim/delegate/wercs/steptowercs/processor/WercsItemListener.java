package com.staples.pim.delegate.wercs.steptowercs.processor;

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
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.core.io.Resource;

import com.staples.pcm.stepcontract.beans.ClassificationType;
import com.staples.pcm.stepcontract.beans.ClassificationsType;
import com.staples.pcm.stepcontract.beans.MetaDataType;
import com.staples.pcm.stepcontract.beans.MultiValueType;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.base.common.listenerandrunner.BatchJobListener;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;
import com.staples.pim.delegate.wercs.model.MasterTableVO;
import com.staples.pim.delegate.wercs.steptowercs.runner.RunSchedulerStepToWercs;



public class WercsItemListener extends BatchJobListener implements ItemReadListener<STEPProductInformation>,ItemProcessListener<STEPProductInformation, MasterTableVO>,SkipListener<STEPProductInformation, Throwable>{

	public static final String[] mandatoryAttributes = {"A2038","WERCS_Out_Trigger","A0080","A0405","A0013_RET","A2038","STEP_ID","A0075","A0410","A2036"};

	public static List<File> succeededFiles;
	
	public static final String fileDoneFolder = IntgSrvPropertiesReader.getProperty("WERCS_STEP_FILEDONE_FOLDER");
	public static final String fileBad = "File_Bad";
	
	private static final String					TRACELOGGER_WERCS_STEPTOWERCS			= "tracelogger.wercs.steptowercs";
	static IntgSrvLogger						logger									= IntgSrvLogger
			.getInstance(TRACELOGGER_WERCS_STEPTOWERCS);
	
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		DatamigrationCommonUtil.printConsole("listener :after job");
		logger.info("afterJob()");
		String fileDoneFolderPath = IntgSrvUtils.reformatFilePath(fileDoneFolder);
		
		File fileDoneFolder = new File(fileDoneFolderPath);
		if(!fileDoneFolder.exists()){
			logger.info("Creating FileDone folder");
			fileDoneFolder.mkdirs();
		}
		for(File file:succeededFiles){
			File destinationFile = new File(fileDoneFolderPath+file.getName());
			try {
				logger.info("Moving file:"+file.getName()+" to fileDone folder");
				Files.move(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				DatamigrationCommonUtil.printConsole("Error while copying file to File_Done folder."+e.getMessage());
				e.printStackTrace();
				logger.error("Exception caught while moving file to fileDone folder. File:"+file.getName());
				IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
			}
		}
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		DatamigrationCommonUtil.printConsole("listener :before job");
		succeededFiles = new ArrayList<File>();
	}

	@Override
	public void afterProcess(STEPProductInformation arg0, MasterTableVO arg1) {

	}


	@Override
	public void beforeProcess(STEPProductInformation arg0) {

	}


	@Override
	public void onProcessError(STEPProductInformation arg0, Exception arg1) {

		DatamigrationCommonUtil.printConsole("listener: onprocesserrorprocess");
	}

	@Override
	public void onSkipInProcess(STEPProductInformation arg0, Throwable arg1) {

		DatamigrationCommonUtil.printConsole("listener: onskiptinprocess : "+arg0.getProducts().getProduct().get(0).getID());
	}

	@Override
	public void onSkipInRead(Throwable arg0) {

		DatamigrationCommonUtil.printConsole("listener: onskipinread");
	}

	@Override
	public void onSkipInWrite(Throwable arg0, Throwable arg1) {

		DatamigrationCommonUtil.printConsole("listener: onskipinwrite");
	}

	@Override
	public void afterRead(STEPProductInformation stepProductInformation) {
		DatamigrationCommonUtil.printConsole("listener: afterRead");
		logger.info("afterRead(). Getting currentResource");
		MultiResourceItemReader multiResourceItemReader = 	(MultiResourceItemReader)RunSchedulerStepToWercs.context.getBean("multiResourceReader");
		Resource currentResource = multiResourceItemReader.getCurrentResource();
		//validate input xml. check if all mandatory values are present
		if(!containsAllMandatoryValues(stepProductInformation)){
			try {
				File badFile = currentResource.getFile();
				File fileBadFolder = new File(badFile.getParentFile().getParentFile().getAbsolutePath()+File.separator+fileBad);
				if(!fileBadFolder.exists()){
					fileBadFolder.mkdirs();
				}
				File destination = new File(fileBadFolder.getAbsolutePath()+File.separator+badFile.getName());
				logger.info("Moving file to fileBad folder. File:"+badFile.getName());
				Files.move(badFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

			} catch (IOException e) {
				logger.info("Exception caught while moving file to fileBad.");
				e.printStackTrace();
				IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
			}
		}else{
			try {
				File succeededFile = currentResource.getFile();
				succeededFiles.add(succeededFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void beforeRead() {
		DatamigrationCommonUtil.printConsole("listener: beforeRead");
	}

	@Override
	public void onReadError(Exception arg0) {
		DatamigrationCommonUtil.printConsole("listener: onreadError");
	}
	
	public static boolean containsAllMandatoryValues(STEPProductInformation stepProductInformation){
		Map<String,String> valuesInXml = new HashMap<String,String>();

		logger.info("Validating the Input file");
		
		ClassificationsType classifications = stepProductInformation.getClassifications();
		if(classifications !=null){
			for(ClassificationType classification:classifications.getClassification())
			{
				valuesInXml = getClassLevelAttributes(valuesInXml,classification.getNameOrAttributeLinkOrSequenceProduct());
			}
		}
		ProductsType products = stepProductInformation.getProducts();
		List<ProductType> productList = products.getProduct();
		for(ProductType product:productList){
			valuesInXml.put(WercsAppConstants.STEPID, product.getID());
			for(Object productObj : product.getProductOrSequenceProductOrSuppressedProductCrossReference()){
				if(WercsAppConstants.VALUESTYPE.equalsIgnoreCase(productObj.getClass().getSimpleName())){
					ValuesType values = (ValuesType) productObj;
					for(Object valueObj : values.getValueOrMultiValueOrValueGroup()){
						if(WercsAppConstants.VALUETYPE.equalsIgnoreCase(valueObj.getClass().getSimpleName())){
							ValueType value = (ValueType) valueObj;
							valuesInXml.put(value.getAttributeID(), value.getContent());
						}else if(WercsAppConstants.MULTIVALUETYPE.equalsIgnoreCase(valueObj.getClass().getSimpleName())){
							MultiValueType multiValue = (MultiValueType)valueObj;
							String attrid = multiValue.getAttributeID();
							List<Object> multiValueObjects = multiValue.getValueOrValueGroup();
							for(Object multivalueObj : multiValueObjects){
								if(WercsAppConstants.VALUETYPE.equalsIgnoreCase(multivalueObj.getClass().getSimpleName())){
									ValueType mvalue = (ValueType) multivalueObj;
									valuesInXml.put(attrid, mvalue.getContent());	
								}
							}
						}

					}
				}
			}
		}
		
		if(valuesInXml.get("WERCS_Out_Trigger").equalsIgnoreCase(WercsAppConstants.RULES_TRIPPED_STR)){
			if(IntgSrvUtils.isNullOrEmpty(valuesInXml.get("A0080")) || IntgSrvUtils.isNullOrEmpty(valuesInXml.get("A2036"))){
				logger.error("Mandatory Attribute missing");
				return false;
			}
		}else{
			for(String mandatoryAttribute:mandatoryAttributes){
				if(IntgSrvUtils.isNullOrEmpty(valuesInXml.get(mandatoryAttribute))){
					logger.error("Mandatory Attribute missing:"+mandatoryAttribute);
					return false;
				}
			}
		}
		logger.info("File Validated");
		return true;
	}
	
	public static Map<String,String> getClassLevelAttributes(Map<String,String> valueInXml, List<Object> classificationList){

		for(Object object:classificationList){
			if("ClassificationType".equalsIgnoreCase(object.getClass().getSimpleName())){
				ClassificationType classification = (ClassificationType)object;
				if("Class".equalsIgnoreCase(classification.getUserTypeID())){
					List<Object> classLevelObjs = classification.getNameOrAttributeLinkOrSequenceProduct();
					for(Object classLevelObj : classLevelObjs){
						if("MetaDataType".equalsIgnoreCase(classLevelObj.getClass().getSimpleName())){
							MetaDataType metadata = (MetaDataType)classLevelObj;
							List<Object> valueObjs = metadata.getValueOrMultiValueOrValueGroup();
							for(Object valueObj:valueObjs){
								if("ValueType".equalsIgnoreCase(valueObj.getClass().getSimpleName())){
									ValueType value = (ValueType)valueObj;
									valueInXml.put(value.getAttributeID(), value.getContent());
								}
							}
						}
					}
				}else{
					valueInXml=getClassLevelAttributes(valueInXml, classification.getNameOrAttributeLinkOrSequenceProduct());
				}
			}
		}
		return valueInXml;
	}
}