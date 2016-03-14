package com.staples.pim.delegate.wercs.steptowercs.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;

import com.staples.pcm.stepcontract.beans.ClassificationType;
import com.staples.pcm.stepcontract.beans.ClassificationsType;
import com.staples.pcm.stepcontract.beans.MetaDataType;
import com.staples.pcm.stepcontract.beans.MultiValueType;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.common.SBDatabaseHandlingService;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;
import com.staples.pim.delegate.wercs.model.MasterTableVO;
import com.staples.pim.delegate.wercs.steptowercs.runner.RunSchedulerStepToWercs;



public class StepToWercsIntgProcessor implements ItemProcessor<STEPProductInformation, MasterTableVO>{

	private static final String					TRACELOGGER_WERCS_STEPTOWERCS			= "tracelogger.wercs.steptowercs";
	static IntgSrvLogger						logger									= IntgSrvLogger
			.getInstance(TRACELOGGER_WERCS_STEPTOWERCS);


	public SBDatabaseHandlingService databaseAccessor;

	@Override
	public MasterTableVO process(STEPProductInformation stepProductInformation) throws Exception {

		DatamigrationCommonUtil.printConsole("WERCS processor.");

		MasterTableVO masterTableVO = getMasterTableVO(stepProductInformation);
		if(masterTableVO!=null){
			//2. Make an entry in the master table for the upc
			String upc = masterTableVO.getUPCNo();
			masterTableVO.setUPCNo(DatamigrationCommonUtil.addLeadingCharacter(upc, 14, '0'));
			masterTableVO.setEventID(0);
			databaseAccessor = new SBDatabaseHandlingService(RunSchedulerStepToWercs.datasource);
			boolean result=databaseAccessor.masterTableInsertUpdate(masterTableVO,logger);
			if(result==false){
				return null;
			}
			
			if(WercsAppConstants.RULES_TRIPPED_STR.equalsIgnoreCase(masterTableVO.getWercsTrigger())){

				logger.info("Rules Tripped Mail received for upc:"+upc);
				SBDatabaseHandlingService dbAccess = new SBDatabaseHandlingService(RunSchedulerStepToWercs.datasource);
				dbAccess.auditTableInsert(masterTableVO.getUPCNo(),masterTableVO.getPipid(),505,logger);
//				dbAccess.updateMasterTableEventID("NotSent",masterTableVO.getPipid(),logger);
//				DatamigrationCommonUtil.printConsole("RulesTripped: "+masterTableVO.getUPCNo()+" updated in DB");
				return null;
			}
			return masterTableVO;
		}else{
			return null;
		}
	}

	public MasterTableVO getMasterTableVO(STEPProductInformation stepProductInformation){

		if(WercsItemListener.containsAllMandatoryValues(stepProductInformation)){
			MasterTableVO mastertableVO = new MasterTableVO();
			Map<String,String> valuesInXml = new HashMap<String,String>();

			ClassificationsType classifications = stepProductInformation.getClassifications();
			if(classifications != null){
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
			
			String channelSpecificFlag = valuesInXml.get("A0410");
			
			mastertableVO.setUPCNo(valuesInXml.get("A0080"));
			mastertableVO.setStepid(valuesInXml.get(WercsAppConstants.STEPID));
			mastertableVO.setPipid(valuesInXml.get("A0405"));

			mastertableVO.setSkuno(valuesInXml.get("A1363"));//hardcoded
			mastertableVO.setModelno(valuesInXml.get("A0013_RET"));
			mastertableVO.setSupplierName(valuesInXml.get("A2038"));
			mastertableVO.setRequestorName("Supplier");
			mastertableVO.setRequestorID("Supplier");

			mastertableVO.setItemdesc(valuesInXml.get("A2036"));
		
			if(IntgSrvUtils.isNullOrEmpty(valuesInXml.get("A1363"))){
				mastertableVO.setTranstype("ItemCreate");
			}else{
				mastertableVO.setTranstype("ItemUpdate");
			}
			
			mastertableVO.setWercsTrigger(valuesInXml.get("WERCS_Out_Trigger"));
			mastertableVO.setRegulatoryStatus("0");
			mastertableVO.setRegistrationStatus(-1);
			mastertableVO.setSupplierId(valuesInXml.get("A0075"));
			mastertableVO.setStepExemptFlag(valuesInXml.get("A2028"));

			if("RET".equals(channelSpecificFlag)){
				
				mastertableVO.setPsId(valuesInXml.get("A0249"));
				mastertableVO.setMerchantId(valuesInXml.get("A0029"));
			}else{
				mastertableVO.setPsId(valuesInXml.get("A0252"));
				mastertableVO.setMerchantId(valuesInXml.get("A0214"));
			}

			mastertableVO.setToner_Wholesaler(valuesInXml.get("A2035"));
			return mastertableVO;
		}else{
			return null;
		}
	}

	public Map<String,String> getClassLevelAttributes(Map<String,String> valueInXml, List<Object> classificationList){

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
