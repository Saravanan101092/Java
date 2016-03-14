
package com.staples.pim.delegate.datamigration.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;

/**
 * Issue (PCMP-2359): Data Migration Items (PIM core items) are flowing out of
 * STEP.
 * 
 * Issue Description: New PIM core items are flowing out of STEP. As the items
 * are getting approved first time, it is flowing into all the outbound as we
 * enabled the event triggering definitions at Item and SKU level and gets
 * published. As these items are not flown through workflows, lifecycle flag was
 * not set and failed in the spring batch and caused exception. As of now we
 * dont have control on STEP or Spring Batch.
 * 
 * Solution: We should have control to process STEP XML on the Spring Batch. We
 * have an attribute called Product Grouping (A0537); Spring Batch can decide it
 * can process the STEP XML or not based on this attribute configuration from
 * the Spring Batch Configuration file.
 */

public class DataMigAcceptItemManager {

	/**
	 * Private constructor. Prevents instantiation from other classes.
	 */
	private DataMigAcceptItemManager() {

	}

	/**
	 * Initializes singleton.
	 */
	private static class SingletonHolder {

		private static final DataMigAcceptItemManager	obj;
		static {
			try {
				obj = new DataMigAcceptItemManager();
			} catch (Exception exception) {
				throw new RuntimeException("Singleton, an error occurred!", exception);
			}
		}
	}

	public static DataMigAcceptItemManager getInstance() {

		return SingletonHolder.obj;
	}

	public static final String	DM_ITEMS_NOT_ACCEPT_ACTION_DELETE_FLAG	= "DM_ITEMS_NOT_ACCEPT_ACTION_DELETE_FLAG";
	public static final String	DM_ACCEPT_ITEMS_FROM_SOURCESYSTEMS		= "DM_ACCEPT_ITEMS_FROM_SOURCESYSTEMS";
	public static final String	FDPO_ACCEPT_ITEMS_FROM_SOURCESYSTEMS	= "FDPO_ACCEPT_ITEMS_FROM_SOURCESYSTEMS";

	/*
	 * Methods are to resolve Issue (PCMP-2359): Data Migration Items (PIM core
	 * items) are flowing out of STEP.
	 */
	public boolean getAcceptFlag(File currentFile) throws IOException, JAXBException {

		return (getProductGroupingFlag(currentFile));

	}

	public boolean getFDPOAcceptFlag(String fileContent) throws JAXBException {

		return (getProductGroupingFlagForFDPO(fileContent));

	}

	private boolean getProductGroupingFlagForFDPO(String fileContent) throws JAXBException {

		boolean productGroupingAcceptFlag = false;

		String productGroupValue = getProductGroupValue(fileContent);

		/*
		 * Checking the Source Systems
		 */
		List<String> acceptSourceSysList = getSourceSysFromConfigForFDPO();

		for (String sourceSystem : acceptSourceSysList) {
			if (sourceSystem.equalsIgnoreCase(productGroupValue)) {
				productGroupingAcceptFlag = true;
				break;
			}
		}
		return productGroupingAcceptFlag;
	}

	private List<String> getSourceSysFromConfigForFDPO() {

		String sourceSysStr = IntgSrvPropertiesReader.getProperty(FDPO_ACCEPT_ITEMS_FROM_SOURCESYSTEMS);
		return Arrays.asList(sourceSysStr.split("~"));
	}

	/**
	 * Get Product Grouping Flag
	 * 
	 * @param currentFile
	 * @return
	 * @throws IOException
	 * @throws JAXBException
	 */
	private boolean getProductGroupingFlag(File currentFile) throws IOException, JAXBException {

		String fileContent = new String(Files.readAllBytes(currentFile.toPath()));

		return getProductGroupingFlag(fileContent);

	}

	private boolean getProductGroupingFlag(String fileContent) throws JAXBException {

		boolean productGroupingAcceptFlag = false;

		String productGroupValue = getProductGroupValue(fileContent);

		/*
		 * Checking the Source Systems
		 */
		List<String> acceptSourceSysList = getSourceSysFromConfig();

		for (String sourceSystem : acceptSourceSysList) {
			if (sourceSystem.equalsIgnoreCase(productGroupValue)) {
				productGroupingAcceptFlag = true;
				break;
			}
		}
		return productGroupingAcceptFlag;
	}

	private List<String> getSourceSysFromConfig() {

		String sourceSysStr = IntgSrvPropertiesReader.getProperty(DM_ACCEPT_ITEMS_FROM_SOURCESYSTEMS);
		return Arrays.asList(sourceSysStr.split("~"));
	}

	/**
	 * Ignore Action
	 * 
	 * @param currentFile
	 * @throws IOException
	 */
	public void ignoreAction(File currentFile) throws IOException {

		boolean deleteFlag = Boolean.valueOf(IntgSrvPropertiesReader.getProperty(DM_ITEMS_NOT_ACCEPT_ACTION_DELETE_FLAG));

		if (deleteFlag && currentFile.isFile()) {
			currentFile.delete();
		} else {
			archiveFile(currentFile);
		}
	}

	/**
	 * Archive File
	 * 
	 * @param currentFile
	 * @throws IOException
	 */
	private void archiveFile(File currentFile) throws IOException {

		File dest = new File(currentFile.getParentFile().getParentFile().getPath() + "/File_Bad/DM_Item_" + currentFile.getName());

		Files.copy(currentFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		currentFile.delete();
	}

	/**
	 * Parse STEP XML and getting Product Group value
	 * 
	 * @param fileContent
	 * @return
	 * @throws JAXBException
	 */
	private String getProductGroupValue(String fileContent) throws JAXBException {

		String productGroupValue = new String();
		List<ValueType> valueList = new ArrayList<ValueType>();
		ObjectFactory objectFactory = new ObjectFactory();
		ValuesType ValuesType = objectFactory.createValuesType();

		STEPProductInformation stepProdInfo = getStepProductInformation(fileContent);
		List<ProductType> products = stepProdInfo.getProducts().getProduct();

		/*
		 * Getting ValuesType from Item
		 */
		for (ProductType product : products) {
			if (product.getUserTypeID().equalsIgnoreCase("ITEM")) {
				for (Object obj : product.getProductOrSequenceProductOrSuppressedProductCrossReference()) {
					if (obj != null && obj instanceof ValuesType) {
						ValuesType = (ValuesType) obj;
						break;
					}
				}
			}
		}

		/*
		 * Getting all ValueType
		 */
		for (Object obj : ValuesType.getValueOrMultiValueOrValueGroup()) {
			if (obj != null && obj instanceof ValueType) {
				valueList.add((ValueType) obj);
			}
		}

		/*
		 * Getting Product Group value
		 */
		for (ValueType ValueType : valueList) {
			if ("A0537".equalsIgnoreCase(ValueType.getAttributeID())) {
				productGroupValue = ValueType.getContent();
				IntgSrvUtils.printConsole("ProductGroupValue: " + productGroupValue);
				break;
			}
		}
		return productGroupValue;
	}

	/**
	 * Get StepProductInformation
	 * 
	 * @param message
	 * @return
	 * @throws JAXBException
	 */
	private STEPProductInformation getStepProductInformation(String message) throws JAXBException {

		JAXBContext jaxbContext = STEPProductInformationJAXBContextSingleton.getInstance();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		STEPProductInformation stepProdInfo = (STEPProductInformation) jaxbUnmarshaller
				.unmarshal(new InputSource(new StringReader(message)));

		return stepProdInfo;
	}
}