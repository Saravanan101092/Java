package com.staples.pim.delegate.wercs.steptowercs.retry.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.staples.pcm.stepcontract.beans.MultiValueType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.common.SBDatabaseHandlingService;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;
import com.staples.pim.delegate.wercs.common.WercsCommonUtil;
import com.staples.pim.delegate.wercs.model.MasterTableVO;
import com.staples.pim.delegate.wercs.steptowercs.processor.WERCSRestAPIProcessor;
import com.staples.pim.delegate.wercs.steptowercs.retry.runner.RunSchedulerWercsRetry;
import com.staples.pim.delegate.wercs.steptowercs.runner.RunSchedulerStepToWercs;


public class WercsRegStatusRetryProcessor {

	private static final String					TRACELOGGER_WERCS_WERCSRETRY			= "tracelogger.wercs.wercsretry";
	static IntgSrvLogger						logger									= IntgSrvLogger
			.getInstance(TRACELOGGER_WERCS_WERCSRETRY);
	
	public void processWercsRegStatusRetry(){
		
		
		List<MasterTableVO> masterTableVOList = getUpcsToRetry();
		if(masterTableVOList.size()>0){
			Map<String,MasterTableVO> masterTableVOMap = new WERCSRestAPIProcessor().processWercsAPIRequests(masterTableVOList);
			processResponsesFromWercs(masterTableVOMap);
		}else{
			logger.info("No items in DB to recheck registration status. Exitting process.");
			DatamigrationCommonUtil.printConsole("No items to retry!");
		}
	}
	
	public List<MasterTableVO> getUpcsToRetry(){
		
		logger.info("Getting database bean.");
		SBDatabaseHandlingService dbAccess = new SBDatabaseHandlingService(RunSchedulerWercsRetry.datasource);
		List<MasterTableVO> masterTableVOList = dbAccess.getWercsRetryUPCs(logger);
		return masterTableVOList;
	}
	
	public void processResponsesFromWercs(Map<String,MasterTableVO> masterTableVOs){
		
		//xmls to be sent to step
		Map<String,MasterTableVO> itemsToSTEP = new HashMap<String,MasterTableVO>();
		//rejected xmls to be sent to step
		List<MasterTableVO> rejectedItems = new ArrayList<MasterTableVO>();
		
		for(String upcNo : masterTableVOs.keySet()){
			
			MasterTableVO masterTableVO = masterTableVOs.get(upcNo);
			int registration_Status = masterTableVO.getRegistrationStatus();
			
			if(registration_Status!=-1){
				//if status = 1 send xml to step
				if(registration_Status==1){
					logger.info("RegistrationStatus received as 1 for "+upcNo);
					itemsToSTEP.put(upcNo, masterTableVO);
				}else{
					
					//current time 
					Calendar currentCalendarTime = Calendar.getInstance();
				
					//created time
					int rejectionTimespan;
					
					if(registration_Status==0 || registration_Status==2){
						if(WercsAppConstants.ITEMCREATE_STR.equalsIgnoreCase(masterTableVO.getTranstype())){
							rejectionTimespan = Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_TIMETO_REJECT_ITEM"));
						}else{
							rejectionTimespan = Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_TIMETO_REJECT_ITEM_IU"));
						}
					}else{
						rejectionTimespan = Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_TIMETO_REJECT_ITEM_349"));
					}
					
					logger.info("Rejection timespan retrieved from property:"+rejectionTimespan);
					
					Calendar createdCalendarTime = Calendar.getInstance();
					createdCalendarTime.setTimeInMillis(masterTableVO.getCreatedDate().getTime());
					createdCalendarTime.set(createdCalendarTime.MINUTE, createdCalendarTime.get(createdCalendarTime.MINUTE)+rejectionTimespan);
					
					if(currentCalendarTime.after(createdCalendarTime)){
					
						//means time span exceeded
						logger.info("Item rejected as retry timespan exeeded:"+upcNo);
						rejectedItems.add(masterTableVO);
					}else{
						
						//previous registration status set as soon as row retrieved from DB
						if(registration_Status!=masterTableVO.getPreviousRegistrationStatus()){
							logger.info("Item Status changed for "+upcNo);
							itemsToSTEP.put(upcNo, masterTableVO);
						}else{
							logger.info("Item status unchanged for "+upcNo);
							DatamigrationCommonUtil.printConsole("status unchanged for upc:"+upcNo);
						}
					}
				}
			}
		}
		
		//send status to step
		if(itemsToSTEP.size()>0){
			STEPProductInformation stepProdInfo = getSTEPProductInformationObjectForResponses(itemsToSTEP);
			File file = new File(IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty("WERCS_STEP_OUTPUT_FOLDER_RETRY"))+new Date().getTime()+".xml");
			File outputFile=DatamigrationCommonUtil.marshallObject(stepProdInfo, file, "WERCS_STEP_OUTPUT_FOLDER_RETRY", "WERCSToSTEP");
			if(outputFile!=null){
				logger.info("Response XML generated:"+outputFile.getPath());
				WercsCommonUtil.sendWercsResponseFile(outputFile, WercsAppConstants.STEPTOWERCS_APPLICATIONID, "",logger);
			}
		}else{
			DatamigrationCommonUtil.printConsole("No item with status 1 and no item with status change.");
		}
		
		//Reject xmls
		SBDatabaseHandlingService dbAccess = new SBDatabaseHandlingService(RunSchedulerWercsRetry.datasource);
		dbAccess.rejectedItemUpdate(rejectedItems,logger);
		logger.info("Updated rejected items in Database");
		
		if(rejectedItems.size()>0){
			
			logger.info("No of Items Rejected = "+rejectedItems.size());
			STEPProductInformation stepProdInfo = getSTEPProductInformationForRejected(rejectedItems);
			File file = new File(IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty("WERCS_STEP_OUTPUT_FOLDER_RETRY"))+"Rejected_"+new Date().getTime()+".xml");
			File outputFile=DatamigrationCommonUtil.marshallObject(stepProdInfo, file, "WERCS_STEP_OUTPUT_FOLDER_RETRY", "WERCSToSTEP");
			if(outputFile!=null){
				logger.info("Rejected items XML generated:"+outputFile.getPath());
				WercsCommonUtil.sendWercsResponseFile(outputFile, WercsAppConstants.STEPTOWERCS_APPLICATIONID, "",logger);
			}
		}else{
			DatamigrationCommonUtil.printConsole("No item is rejected.");
		}
	}
	
	public STEPProductInformation getSTEPProductInformationForRejected(List<MasterTableVO> rejectedItems){
		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = objectFactory.createSTEPProductInformation();
		ProductsType products = objectFactory.createProductsType();

		for(MasterTableVO masterTableVO : rejectedItems){
			
			ProductType product = objectFactory.createProductType();
			product.setID(masterTableVO.getStepid());
			product.setUserTypeID(WercsAppConstants.ITEM);
			product.setParentID(WercsAppConstants.WERCS_REJECTXML_PARENTID);
			
			ValuesType values = objectFactory.createValuesType();

			MultiValueType multiValue = objectFactory.createMultiValueType();
			multiValue.setAttributeID(WercsAppConstants.A0080_STR);
			
			ValueType upcValue = objectFactory.createValueType();
			upcValue.setContent(masterTableVO.getUPCNo());
			
			multiValue.getValueOrValueGroup().add(upcValue);
			values.getValueOrMultiValueOrValueGroup().add(multiValue);

			
			ValueType statusValue = objectFactory.createValueType();
			statusValue.setAttributeID(WercsAppConstants.A2034_STR);
			statusValue.setContent(DatamigrationCommonUtil.getValuesFromLOV(WercsAppConstants.REGISTRATION_STATUSLOVKEY,Integer.toString(masterTableVO.getRegistrationStatus()), false));
			values.getValueOrMultiValueOrValueGroup().add(statusValue);

			ValueType modelNOValue = objectFactory.createValueType();
			modelNOValue.setAttributeID(WercsAppConstants.A0013_RET_STR);
			modelNOValue.setContent(masterTableVO.getModelno());
			values.getValueOrMultiValueOrValueGroup().add(modelNOValue);
			
			ValueType rejectedStatusValue = objectFactory.createValueType();
			rejectedStatusValue.setAttributeID(WercsAppConstants.A2028_STR);
			rejectedStatusValue.setContent(WercsAppConstants.WERCS_REJECTION_STATUS);
			values.getValueOrMultiValueOrValueGroup().add(rejectedStatusValue);
//			A2028
			product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
			products.getProduct().add(product);
		}
		
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);
		stepProductInformation.setUseContextLocale(false);
		stepProductInformation.setProducts(products);
		return stepProductInformation;
	}
	
	public STEPProductInformation getSTEPProductInformationObjectForResponses(Map<String,MasterTableVO> masterTableVOMap){

		logger.info("No of items in response XML to STEP:"+masterTableVOMap.size());
		
		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = objectFactory.createSTEPProductInformation();
		ProductsType products = objectFactory.createProductsType();

		for(String upcNO : masterTableVOMap.keySet()){
			String upcNumber = upcNO;
			MasterTableVO masterTableVO = masterTableVOMap.get(upcNumber);

			String registrationStatus = Integer.toString(masterTableVO.getRegistrationStatus());

			ProductType product = objectFactory.createProductType();
			product.setID(masterTableVO.getStepid());
			product.setUserTypeID(WercsAppConstants.ITEM);
			
			ValuesType values = objectFactory.createValuesType();

			ValueType upcValue = objectFactory.createValueType();
			upcValue.setAttributeID(WercsAppConstants.A0080_STR);
			upcValue.setContent(upcNumber);
			values.getValueOrMultiValueOrValueGroup().add(upcValue);

			ValueType statusValue = objectFactory.createValueType();
			statusValue.setAttributeID(WercsAppConstants.A2034_STR);
			statusValue.setContent(DatamigrationCommonUtil.getValuesFromLOV(WercsAppConstants.REGISTRATION_STATUSLOVKEY,registrationStatus , false));
			values.getValueOrMultiValueOrValueGroup().add(statusValue);

			product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
			products.getProduct().add(product);
		}

		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);
		stepProductInformation.setUseContextLocale(false);
		stepProductInformation.setProducts(products);
		return stepProductInformation;
	}
	
}
