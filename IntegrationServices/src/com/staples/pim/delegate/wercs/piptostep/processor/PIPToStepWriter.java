package com.staples.pim.delegate.wercs.piptostep.processor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.core.io.Resource;

import com.staples.pcm.stepcontract.beans.ClassificationReferenceType;
import com.staples.pcm.stepcontract.beans.KeyValueType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.datamigration.utils.STEPProductInformationJAXBContextSingleton;
import com.staples.pim.delegate.wercs.common.WercsCommonUtil;
import com.staples.pim.delegate.wercs.piptostep.listenerrunner.RunSchedulerPIPToStep;


public class PIPToStepWriter <WercsCollectionBean> implements ResourceAwareItemWriterItemStream<WercsCollectionBean>, StepExecutionListener,SkipListener<WercsCollectionBean, Throwable>{

	protected IntgSrvLogger			ehfLogger										= IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	protected IntgSrvLogger			traceLogger										= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	protected String				clazzname										= this.getClass().getName();

	private ObjectFactory			objectFactory									= new ObjectFactory();
	private STEPProductInformation	stepPrdInfo;
	private ProductsType			productsType;
	//public STEPProductInformation	stepproductInfo;


	public static final String		CONTEXT_ID_VALUE								= "EnglishUS";
	public static final String		EXPORT_CONTEXT_VALUE							= "EnglishUS";
	public static final boolean		USE_CONTEXT_LOCALE_VALUE						= false;
	public static final String		WORKSPACE_ID_VALUE								= "Main";
	public static String			WERCS_PIP_STEP_OUTPUT_DIR						= "WERCS_PIP_STEP_OUTPUT_DIR";
	private final String			SUPPLIER_LINK									= "SupplierLink";
	private final String			MERCHANDISING_LINK								= "Merchandising Link";
	
	
	private List<File>				outputFileList									= new ArrayList<File>();
	private File					outputFile;
	private File					tempOutputFile;
	private String					SkipRoWDetailsFileString;
	private String 					actionCode;
	private String 					skuNo;
	private String 					stepId;
	private String					pipItemNo;
	private String 					modified_id;
	
	
	@Override
	public void write(List<? extends WercsCollectionBean> wercsinfoList) throws Exception {

		IntgSrvUtils.printConsole("List Size: " + wercsinfoList.size());
		traceLogger.info("PIPToSTEPWriter write() List Size: " + wercsinfoList.size());
		String supplierID = null;
		String merchId = null;
		String supplierCheck = null;
		
		for(WercsCollectionBean wercsinfoObject : wercsinfoList){
			
			com.staples.pim.delegate.wercs.model.WercsCollectionBean bean = (com.staples.pim.delegate.wercs.model.WercsCollectionBean)wercsinfoObject;
			
			/*
			 * Creating Product Type
			 */
			
			
			ProductType productTypeItem = objectFactory.createProductType();
			actionCode = bean.getAttributeValueMap().get("A0484");
			skuNo = bean.getAttributeValueMap().get("A0012");
			stepId = bean.getAttributeValueMap().get("A0258");
			pipItemNo = bean.getAttributeValueMap().get("A0405");
			
			
			
			/*
			 * Set id if xsv file has step id or pip item number
			 * Set key id if xsv file has sku number 
			 */
			
			/*
			 * Should verify create or update based on action code 
			 */
			 if(actionCode.equalsIgnoreCase("A"))
 {

				if (!IntgSrvUtils.isNullOrEmpty(stepId)) {
					modified_id = "Item-" + stepId;
				} else if (!IntgSrvUtils.isNullOrEmpty(pipItemNo)) {
					modified_id = "Item-" + pipItemNo;
				}

				if (!IntgSrvUtils.isNullOrEmpty(modified_id)) {
					setProductDetails(productTypeItem,
							"WERCSItemsInRegulatoryReview", modified_id,
							"Item", false, true, bean, true);
				} else {
					System.out.println("PIP id or STEP id are mandatory");
				}

			}else if(actionCode.equalsIgnoreCase("U"))
			{
				
				setProductDetails(productTypeItem, "", skuNo, "Item", false, true, bean,false);
			}
			
			
			
			
		
			
			/*
			 * Setting Product Details
			 */
			ClassificationReferenceType classRefMerch = objectFactory.createClassificationReferenceType();
			ClassificationReferenceType classRefSupplier = objectFactory.createClassificationReferenceType();
			merchId = DatamigrationCommonUtil.getClassificationIDValue(bean.getAttributeValueMap().get("A0026"), "MerchClassID");
			if (!IntgSrvUtils.isNullOrEmpty(merchId)) 
			{				
				classRefMerch.setClassificationID(merchId);
				classRefMerch.setType(MERCHANDISING_LINK);
				productTypeItem.getClassificationReference().add(classRefMerch);
			}
			
			supplierCheck = bean.getAttributeValueMap().get("A0410");
			
			if(supplierCheck.equalsIgnoreCase("RET"))
			{
				supplierID = bean.getAttributeValueMap().get("A0075_RET");
			}
			else if(supplierCheck.equalsIgnoreCase("SCC"))	//Should be NAD, have to check
			{
				supplierID = bean.getAttributeValueMap().get("A0075_NAD");
			}
			
			if (!IntgSrvUtils.isNullOrEmpty(supplierID)) {
				classRefSupplier.setClassificationID(supplierID+"Products");
				classRefSupplier.setType(SUPPLIER_LINK);
				productTypeItem.getClassificationReference().add(classRefSupplier);
			}
				
			productsType.getProduct().add(productTypeItem);
			
			/*
			 * Setting Attribute values
			 */
			setAttributeValuesToProduct(productTypeItem, bean.getAttributeValueMap().entrySet());
			
		}
	}
	
	
	private void setProductDetails(ProductType productTypeItem, String ParentID, String id, String userTypeID, boolean selected,boolean referenced, com.staples.pim.delegate.wercs.model.WercsCollectionBean bean,boolean checkStatus) {

		KeyValueType keyValue = objectFactory.createKeyValueType();
		
		if(!ParentID.equalsIgnoreCase(""))
		{
			productTypeItem.setParentID(ParentID);
		}
		if(!checkStatus)
		{
			keyValue.setKeyID(id);
			productTypeItem.setKeyValue(keyValue);
		}
		else
		{
			productTypeItem.setID(id);
		}
		
		productTypeItem.setUserTypeID(userTypeID);
		productTypeItem.setSelected(selected);
		productTypeItem.setReferenced(referenced);
		
		
	}
	
	private void setAttributeValuesToProduct(ProductType productType, Set<Map.Entry<String, String>> allEntry) {

		ValuesType valuesType = objectFactory.createValuesType();
		productType.getProductOrSequenceProductOrSuppressedProductCrossReference().add(valuesType);

		for (Entry<String, String> entry : allEntry) {
			if (!IntgSrvUtils.isNullOrEmpty(entry.getValue()) /* && entry.getValue().length() > 0 && !"0".equalsIgnoreCase(entry.getValue()) && !".00".equalsIgnoreCase(entry.getValue())*/) 
			{
				ValueType valueType = objectFactory.createValueType();
				valueType.setAttributeID(entry.getKey());
				valueType.setContent(entry.getValue());
				valuesType.getValueOrMultiValueOrValueGroup().add(valueType);
			}
		}
	}
	
	@Override
	public void close() throws ItemStreamException {

		IntgSrvUtils.printConsole("close");
				
		try {
			JAXBContext jaxbContext = STEPProductInformationJAXBContextSingleton.getInstance();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(stepPrdInfo, outputFile);

		} catch (Exception exception) {
			traceLogger.error(clazzname, "close()", exception);
			ehfLogger.error(clazzname, "close()", exception);
			RunSchedulerPIPToStep.itemTraceLogger.error(exception.getMessage());
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerPIPToStep.PUBLISH_ID);
		}
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		
		
		IntgSrvUtils.printConsole("open : " + executionContext);

		stepPrdInfo = objectFactory.createSTEPProductInformation();
		productsType = objectFactory.createProductsType();
		stepPrdInfo.setProducts(productsType);

		WercsCommonUtil.getInstance().setDefaultInfo(stepPrdInfo);

		/*
		 * Adding for - Getting the output files to afterJob method
		 */
		outputFileList.add(outputFile);

		tempOutputFile.delete();
	}

	@Override
	public void update(ExecutionContext arg0) throws ItemStreamException {
		
	}

	@Override
	public void setResource(Resource resource) {
		try {
			String path = resource.getFile().getAbsolutePath() + ".xml";
			File file = new File(path);

			this.outputFile = file;
			this.tempOutputFile = resource.getFile();

		} catch (IOException exception) {
			traceLogger.error(clazzname, "close()", exception);
			ehfLogger.error(clazzname, "close()", exception);
			RunSchedulerPIPToStep.itemTraceLogger.error(exception.getMessage());
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerPIPToStep.PUBLISH_ID);
		}
		
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {

		JobParameters JobParameters = stepExecution.getJobParameters();
		this.SkipRoWDetailsFileString = JobParameters.getString(RunSchedulerPIPToStep.WERCS_PIP_STEP_REPORT_FILE);
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		
		IntgSrvUtils.printConsole("Calling afterStep");

		for (File file : outputFileList) {
			ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
			jobExecutionContext.put(file.getAbsolutePath(), file.getAbsolutePath());
		}

		return org.springframework.batch.core.ExitStatus.COMPLETED;
	}
	
	@Override
	public void onSkipInProcess(WercsCollectionBean PIPToSTEPBean, Throwable throwable) {
		
		IntgSrvUtils.printConsole("onSkipInProcess PIPToSTEPBean: " + PIPToSTEPBean);
		IntgSrvUtils.printConsole("onSkipInProcess: " + throwable);
		traceLogger.info("PIPToSTEPWriter onSkipInProcess() : " + String.valueOf(throwable));
		ehfLogger.info(clazzname, "onSkipInProcess", String.valueOf(throwable));
		RunSchedulerPIPToStep.itemTraceLogger.info("PIPToSTEP Writer onSkipInProcess()" + String.valueOf(throwable));
		
	}
	
	@Override
	public void onSkipInRead(Throwable throwable) {
		
		try {
			File file = new File(SkipRoWDetailsFileString);
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(String.valueOf(throwable));
			bufferedWriter.newLine();
			bufferedWriter.close();
			traceLogger.info("PIPToStepXMLWriter onSkipInRead() : " + String.valueOf(throwable));
			ehfLogger.info(clazzname, "onSkipInRead", String.valueOf(throwable));
			RunSchedulerPIPToStep.itemTraceLogger.info("PIPToStepXMLWriter onSkipInWrite()" + String.valueOf(throwable));

		} catch (IOException exception) {
			traceLogger.error(clazzname, "close()", exception);
			ehfLogger.error(clazzname, "close()", exception);
			RunSchedulerPIPToStep.itemTraceLogger.error(exception.getMessage());
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerPIPToStep.PUBLISH_ID);
		}
		
	}
	

	@Override
	public void onSkipInWrite(Throwable throwable1, Throwable throwable2) {

		IntgSrvUtils.printConsole("onSkipInWrite: " + throwable1);
		IntgSrvUtils.printConsole("onSkipInWrite: " + throwable2);
		traceLogger.info("PIPToSTEPWriter onSkipInWrite() : " + String.valueOf(throwable1));
		ehfLogger.info(clazzname, "onSkipInWrite", throwable1);
		RunSchedulerPIPToStep.itemTraceLogger.info("PIPToSTEPXMLWriter onSkipInWrite()" + throwable1.getMessage());
	}
	
}
