
package com.staples.pim.delegate.wholesalers.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import com.staples.pcm.stepcontract.beans.NameType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.ProductCrossReferenceType;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.STEPProductInformationJAXBContextSingleton;
import com.staples.pim.delegate.wholesalers.common.Util;
import com.staples.pim.delegate.wholesalers.listenerrunner.RunSchedulerWholesalerContract;

public class WholesalerContractFeedXMLWriter<WholesalerContractFeedBean> implements
		ResourceAwareItemWriterItemStream<WholesalerContractFeedBean>, StepExecutionListener,
		SkipListener<WholesalerContractFeedBean, Throwable> {

	protected IntgSrvLogger			ehfLogger		= IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	protected IntgSrvLogger			traceLogger		= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	protected String				clazzname		= this.getClass().getName();

	private ObjectFactory			objectFactory	= new ObjectFactory();
	private STEPProductInformation	stepPrdInfo;
	private ProductsType			productsType;

	private File					outputFile;
	private File					tempOutputFile;
	private String					SkipRoWDetailsFileString;

	private List<File>				outputFileList	= new ArrayList<File>();

	@Override
	public void write(List<? extends WholesalerContractFeedBean> wholesalerContractFeedBeanList) throws Exception {

		IntgSrvUtils.printConsole("List Size: " + wholesalerContractFeedBeanList.size());
		traceLogger.info("WholesalerContractFeedXMLWriter write() List Size: " + wholesalerContractFeedBeanList.size());

		for (WholesalerContractFeedBean wholesalerContractFeedBean : wholesalerContractFeedBeanList) {

			com.staples.pim.delegate.wholesalers.model.WholesalerContractFeedBean bean = ((com.staples.pim.delegate.wholesalers.model.WholesalerContractFeedBean) wholesalerContractFeedBean);

			/*
			 * Creating Product Type
			 */
			ProductType productTypeItem = objectFactory.createProductType();
			ProductType productTypeSKU = objectFactory.createProductType();
			ProductType productTypeVendor = objectFactory.createProductType();

			/*
			 * Setting Product Details
			 */
			setProductDetails(productTypeItem, "WholesalerItems", "DMItem-" + bean.getItemNameForSTEPxml(), "Item", false, true, bean);
			setProductDetails(productTypeSKU, "WholesalerSKUs", "DMSKU-", "SKU", bean);
			setProductDetails(productTypeVendor, "VendorPackagingHierarchy", "DMVendorItem-" + bean.getVendorNameForSTEPxml(),
					"ItemToVendor", false, true, bean);

			productsType.getProduct().add(productTypeVendor);
			productsType.getProduct().add(productTypeItem);
			productsType.getProduct().add(productTypeSKU);

			/*
			 * Setting Attribute values
			 */
			setAttributeValuesToProduct(productTypeItem, bean.getItemAttributeValues().entrySet());
			setAttributeValuesToProduct(productTypeSKU, bean.getSKUAttributeValues().entrySet());
			setAttributeValuesToProduct(productTypeVendor, bean.getVendorAttributeValues().entrySet());

			/*
			 * Setting ClassificationReference
			 */
			setClassificationReference(productTypeItem, "SupplierLink", bean.getWholesalerSupplierDetails().getSupplierId());
			setClassificationReference(productTypeVendor, "SupplierLink", bean.getWholesalerSupplierDetails().getSupplierId());

			/*
			 * Setting Product Cross Reference
			 */
			setProductCrossReference(productTypeItem, "DMVendorItem-" + bean.getVendorNameForSTEPxml(), "VendorItem");
			setProductCrossReference(productTypeSKU, "DMItem-" + bean.getItemNameForSTEPxml(), "PrimaryItem");

			/*
			 * Setting Name
			 */
			// setName(productTypeItem, bean.getItemNameForSTEPxml());
		}
	}

	/**
	 * set Product Cross Reference
	 * 
	 * @param productTypeItem
	 * @param productId
	 * @param type
	 */
	private void setProductCrossReference(ProductType productTypeItem, String productId, String type) {

		ProductCrossReferenceType ref = objectFactory.createProductCrossReferenceType();
		ref.setProductID(productId);
		ref.setType(type);

		productTypeItem.getProductOrSequenceProductOrSuppressedProductCrossReference().add(ref);
	}

	/**
	 * Set Name
	 * 
	 * @param productTypeItem
	 * @param name
	 */
	private void setName(ProductType productTypeItem, String name) {

		NameType nameType = objectFactory.createNameType();
		nameType.setContent(name);

		productTypeItem.getName().add(nameType);
	}

	/**
	 * set Classification Reference
	 * 
	 * @param productTypeItem
	 * @param type
	 * @param classificationID
	 */
	private void setClassificationReference(ProductType productTypeItem, String type, String classificationID) {

		ClassificationReferenceType claRefType = objectFactory.createClassificationReferenceType();
		claRefType.setType(type);
		claRefType.setClassificationID(classificationID + "Products");

		productTypeItem.getClassificationReference().add(claRefType);
	}

	/**
	 * SKU Details
	 * 
	 * @param productTypeItem
	 * @param id
	 * @param userTypeID
	 * @param bean
	 */
	private void setProductDetails(ProductType productTypeItem, String ParentID, String id, String userTypeID,
			com.staples.pim.delegate.wholesalers.model.WholesalerContractFeedBean bean) {

		/*
		 * SKU Present and not present logic
		 */
		if (bean.getStaplesSKUNo() != null && bean.getStaplesSKUNo().length() > 0) {
			productTypeItem.setID(id + bean.getStaplesSKUNo());
		} else {
			productTypeItem.setID(id + bean.getItemNameForSTEPxml());
			productTypeItem.setParentID(ParentID);
		}
		productTypeItem.setUserTypeID(userTypeID);
	}

	/**
	 * ITEM AND Vendor Details
	 * 
	 * @param productTypeItem
	 * @param ParentID
	 * @param id
	 * @param userTypeID
	 * @param selected
	 * @param referenced
	 * @param bean
	 */
	private void setProductDetails(ProductType productTypeItem, String ParentID, String id, String userTypeID, boolean selected,
			boolean referenced, com.staples.pim.delegate.wholesalers.model.WholesalerContractFeedBean bean) {

		productTypeItem.setParentID(ParentID);
		productTypeItem.setID(id);
		productTypeItem.setUserTypeID(userTypeID);
		productTypeItem.setSelected(selected);
		productTypeItem.setReferenced(referenced);
	}

	private void setAttributeValuesToProduct(ProductType productType, Set<Map.Entry<String, String>> allEntry) {

		ValuesType valuesType = objectFactory.createValuesType();
		productType.getProductOrSequenceProductOrSuppressedProductCrossReference().add(valuesType);

		for (Entry<String, String> entry : allEntry) {
			if (entry.getValue() != null && entry.getValue().length() > 0 && !"0".equalsIgnoreCase(entry.getValue())
					&& !".00".equalsIgnoreCase(entry.getValue())) {
				ValueType valueType = objectFactory.createValueType();
				valueType.setAttributeID(entry.getKey());
				valueType.setContent(entry.getValue());
				valuesType.getValueOrMultiValueOrValueGroup().add(valueType);
			}
		}
	}

	@Override
	public void close() throws ItemStreamException {

		IntgSrvUtils.printConsole("close ");

		try {
			JAXBContext jaxbContext = STEPProductInformationJAXBContextSingleton.getInstance();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(stepPrdInfo, outputFile);

		} catch (Exception exception) {
			traceLogger.error(clazzname, "close()", exception);
			ehfLogger.error(clazzname, "close()", exception);
			RunSchedulerWholesalerContract.itemTraceLogger.error(exception.getMessage());
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerWholesalerContract.PUBLISH_ID);
		}
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {

		IntgSrvUtils.printConsole("open : " + executionContext);

		stepPrdInfo = objectFactory.createSTEPProductInformation();
		productsType = objectFactory.createProductsType();
		stepPrdInfo.setProducts(productsType);

		Util.getInstance().setDefaultInfo(stepPrdInfo);

		/*
		 * Adding for - Getting the output files to afterJob method
		 */
		outputFileList.add(outputFile);

		tempOutputFile.delete();
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {

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
			RunSchedulerWholesalerContract.itemTraceLogger.error(exception.getMessage());
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerWholesalerContract.PUBLISH_ID);
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {

		JobParameters JobParameters = stepExecution.getJobParameters();
		this.SkipRoWDetailsFileString = JobParameters.getString(RunSchedulerWholesalerContract.WHOLESALER_CONTRACT_FEED_REPORT_FILE);
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
	public void onSkipInProcess(WholesalerContractFeedBean wholesalerContractFeedBean, Throwable throwable) {

		IntgSrvUtils.printConsole("onSkipInProcess WholesalerContractFeedBean: " + wholesalerContractFeedBean);
		IntgSrvUtils.printConsole("onSkipInProcess: " + throwable);
		traceLogger.info("WholesalerContractFeedXMLWriter onSkipInProcess() : " + String.valueOf(throwable));
		ehfLogger.info(clazzname, "onSkipInProcess", String.valueOf(throwable));
		RunSchedulerWholesalerContract.itemTraceLogger.info("WholesalerContractFeedXMLWriter onSkipInWrite()" + String.valueOf(throwable));
	}

	@Override
	public void onSkipInRead(Throwable throwable) {

		try {
			File file = new File(SkipRoWDetailsFileString);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(String.valueOf(throwable));
			bufferedWriter.newLine();
			bufferedWriter.close();
			traceLogger.info("WholesalerContractFeedXMLWriter onSkipInRead() : " + String.valueOf(throwable));
			ehfLogger.info(clazzname, "onSkipInRead", String.valueOf(throwable));
			RunSchedulerWholesalerContract.itemTraceLogger.info("WholesalerContractFeedXMLWriter onSkipInWrite()"
					+ String.valueOf(throwable));

		} catch (IOException exception) {
			traceLogger.error(clazzname, "close()", exception);
			ehfLogger.error(clazzname, "close()", exception);
			RunSchedulerWholesalerContract.itemTraceLogger.error(exception.getMessage());
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerWholesalerContract.PUBLISH_ID);
		}
	}

	@Override
	public void onSkipInWrite(Throwable throwable1, Throwable throwable2) {

		IntgSrvUtils.printConsole("onSkipInWrite: " + throwable1);
		IntgSrvUtils.printConsole("onSkipInWrite: " + throwable2);
		traceLogger.info("WholesalerContractFeedXMLWriter onSkipInWrite() : " + String.valueOf(throwable1));
		ehfLogger.info(clazzname, "onSkipInWrite", throwable1);
		RunSchedulerWholesalerContract.itemTraceLogger.info("WholesalerContractFeedXMLWriter onSkipInWrite()" + throwable1.getMessage());
	}
}