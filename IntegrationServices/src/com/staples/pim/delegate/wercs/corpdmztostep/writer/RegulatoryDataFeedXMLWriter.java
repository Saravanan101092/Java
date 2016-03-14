
package com.staples.pim.delegate.wercs.corpdmztostep.writer;

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

import com.staples.pcm.stepcontract.beans.KeyValueType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pcm.stepcontract.beans.YesNoType;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.datamigration.utils.STEPProductInformationJAXBContextSingleton;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;
import com.staples.pim.delegate.wercs.corpdmztostep.runner.RunSchedulerCorpdmzToStep;
import com.staples.pim.delegate.wercs.common.WercsCommonUtil;

public class RegulatoryDataFeedXMLWriter<WercsCollectionBean> implements ResourceAwareItemWriterItemStream<WercsCollectionBean>,
		StepExecutionListener, SkipListener<WercsCollectionBean, Throwable> {

	public static IntgSrvLogger		logger			= IntgSrvLogger
															.getInstance(WercsAppConstants.FREEFORM_TRACE_LOGGER_WERCS_CORPDMZTOSTEP);
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
	public void write(List<? extends WercsCollectionBean> wercsRegulatoryDataFeedBeanList) throws Exception {

		DatamigrationCommonUtil.printConsole("List Size: " + wercsRegulatoryDataFeedBeanList.size());
		traceLogger.info("RegulatoryDataFeedXMLWriter write() List Size: " + wercsRegulatoryDataFeedBeanList.size());

		for (WercsCollectionBean wercsRegulatoryDataFeedBean : wercsRegulatoryDataFeedBeanList) {

			com.staples.pim.delegate.wercs.model.WercsCollectionBean bean = ((com.staples.pim.delegate.wercs.model.WercsCollectionBean) wercsRegulatoryDataFeedBean);

			/*
			 * Creating Product Type
			 */
			ProductType productTypeItem = objectFactory.createProductType();

			/*
			 * Setting Product Details
			 */
			setProductDetails(productTypeItem, "WERCSItemsInRegulatoryReview", bean.getKEYID(), "Item", false, true, bean);
			productsType.getProduct().add(productTypeItem);

			/*
			 * Setting Attribute values
			 */
			setAttributeValuesToProduct(productTypeItem, bean.getAttributeValueMap().entrySet());

		}
	}

	private void setProductDetails(ProductType productTypeItem, String ParentID, String id, String userTypeID, boolean selected,
			boolean referenced, com.staples.pim.delegate.wercs.model.WercsCollectionBean bean) {

		KeyValueType keyValue = objectFactory.createKeyValueType();

		if (!ParentID.equalsIgnoreCase("")) {
			productTypeItem.setParentID(ParentID);
		}
		if (id.startsWith("Item-")) {
			productTypeItem.setID(id);
		} else {
			keyValue.setKeyID("Galaxy SKU Number");
			keyValue.setContent(id);
			productTypeItem.setKeyValue(keyValue);

		}
		productTypeItem.setUserTypeID(userTypeID);
		productTypeItem.setSelected(selected);
		productTypeItem.setReferenced(referenced);
		// productTypeItem.setAutoInitiate(YesNoType.Y);

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
			RunSchedulerCorpdmzToStep.logger.error(exception.getMessage());
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerCorpdmzToStep.PUBLISH_ID);
		}
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {

		DatamigrationCommonUtil.printConsole("open : " + executionContext);

		stepPrdInfo = objectFactory.createSTEPProductInformation();
		productsType = objectFactory.createProductsType();
		stepPrdInfo.setProducts(productsType);
		stepPrdInfo.setAutoInitiate(YesNoType.N);
		WercsCommonUtil.getInstance().setDefaultInfo(stepPrdInfo);

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
			RunSchedulerCorpdmzToStep.logger.error(exception.getMessage());
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerCorpdmzToStep.PUBLISH_ID);
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {

		DatamigrationCommonUtil.printConsole("Calling beforeStep");

		JobParameters JobParameters = stepExecution.getJobParameters();
		this.SkipRoWDetailsFileString = JobParameters.getString(RunSchedulerCorpdmzToStep.CORPDMZTOSTEP_FEED_REPORT_FILE);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		DatamigrationCommonUtil.printConsole("Calling afterStep");

		for (File file : outputFileList) {
			ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
			jobExecutionContext.put(file.getAbsolutePath(), file.getAbsolutePath());
		}
		return org.springframework.batch.core.ExitStatus.COMPLETED;
	}

	@Override
	public void onSkipInProcess(WercsCollectionBean wercsFeedBean, Throwable throwable) {

		DatamigrationCommonUtil.printConsole("onSkipInProcess wercsFeedBean: " + wercsFeedBean);
		DatamigrationCommonUtil.printConsole("onSkipInProcess: " + throwable);
		traceLogger.info("RegulatoryDataFeedXMLWriter onSkipInProcess() : " + String.valueOf(throwable));
		ehfLogger.info(clazzname, "onSkipInProcess", String.valueOf(throwable));
		RunSchedulerCorpdmzToStep.logger.info("RegulatoryDataFeedXMLWriter onSkipInWrite()" + String.valueOf(throwable));
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
			traceLogger.info("RegulatoryDataFeedXMLWriter onSkipInRead() : " + String.valueOf(throwable));
			ehfLogger.info(clazzname, "onSkipInRead", String.valueOf(throwable));
			RunSchedulerCorpdmzToStep.logger.info("RegulatoryDataFeedXMLWriter onSkipInWrite()" + String.valueOf(throwable));

		} catch (IOException exception) {
			traceLogger.error(clazzname, "close()", exception);
			ehfLogger.error(clazzname, "close()", exception);
			RunSchedulerCorpdmzToStep.logger.error(exception.getMessage());
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerCorpdmzToStep.PUBLISH_ID);
		}
	}

	@Override
	public void onSkipInWrite(Throwable throwable1, Throwable throwable2) {

		DatamigrationCommonUtil.printConsole("onSkipInWrite: " + throwable1);
		DatamigrationCommonUtil.printConsole("onSkipInWrite: " + throwable2);
		traceLogger.info("RegulatoryDataFeedXMLWriter onSkipInWrite() : " + String.valueOf(throwable1));
		ehfLogger.info(clazzname, "onSkipInWrite", throwable1);
		RunSchedulerCorpdmzToStep.logger.info("RegulatoryDataFeedXMLWriter onSkipInWrite()" + throwable1.getMessage());
	}
}