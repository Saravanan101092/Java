
package com.staples.pim.delegate.copyandprint.inbound.domain;

import static com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1;
import static com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2;
import static com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3;
import static com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4;
import static com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH5;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0012_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0019_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A1363_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.DEBUG_XML_GENERATE_FLAG;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.DEFAULT_PARENT_ID_ITEM_FOLDER_NAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_ATTR_TRANSACTION_TYPE_QUEUETOSPRING;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_CNP;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.INBOUND_MAPPING_FILE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ITEMCREATE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ITEM_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.N_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PARENT_ID_ITEM_FOLDER_NAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PUBLISH_ID_NEW;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_COMPONENT_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ECO_SYSTEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ICD_CLASSNAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.TRUE_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.USE_CONTEXT_LOCALE_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.Y_STR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pcm.stepcontract.beans.NameType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.MQConnectionManager;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.copyandprint.domain.CustomException;
import com.staples.pim.delegate.copyandprint.inbound.domain.Mapping.Attributes.Attribute;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

/**
 * @author 522462 This class contains methods to generate the Step XML from
 *         PIMCore XML file
 */

public class StepInboundProcessor {

	public final String							A0024_STR		= "A0024";

	public final String							A0213_STR		= "A0213";

	public final String							A0254_STR		= "A0254";

	public final String							A0026_STR		= "A0026";

	public final String							A0412_STR		= "A0412";

	public final String							A0085_STR		= "A0085";

	public final String							A0086_STR		= "A0086";

	public final String							A0025_STR		= "A0025";

	public final String							A0411_STR		= "A0411";

	public final String							A0190_STR		= "A0190";

	public final String							A0408_STR		= "A0408";

	public final String							A0210_STR		= "A0210";

	public final String							A0029_STR		= "A0029";

	public final String							A0248_STR		= "A0248";

	public final String							A0214_STR		= "A0214";

	public final String							A0251_STR		= "A0251";

	public final String							A0197_STR		= "A0197";

	public final String							A0037_STR		= "A0037";

	public final String							A0011_STR		= "A0011";

	public final String							A0212_STR		= "A0212";

	public final String							A0043_STR		= "A0043";

	public final String							A0189_STR		= "A0189";

	public final String							A0027_STR		= "A0027";

	public final String							A0230_STR		= "A0230";

	// For PCMP 766-Channel Specific Implementation
	public final String							A0013_STR		= "A0013";

	// For PCMP 766-Channel Specific Implementation
	public final String							A0018_STR		= "A0018";

	// For PCMP 766-Channel Specific Implementation
	public final String							A0045_STR		= "A0045";

	// For PCMP 766-Channel Specific Implementation
	public final String							A0046_STR		= "A0046";

	// For PCMP 766-Channel Specific Implementation
	public final String							A0067_STR		= "A0067";

	// For PCMP 766-Channel Specific Implementation
	public final String							A0075_STR		= "A0075";

	// For PCMP 766-Channel Specific Implementation
	public final String							A0077_STR		= "A0077";

	// For PCMP 766-Channel Specific Implementation
	public final String							A0078_STR		= "A0078";

	// For PCMP 766-Channel Specific Implementation
	public final String							A0018_RET_STR	= "A0018_RET";

	// For PCMP 766-Channel Specific Implementation
	public final String							A0018_NAD_STR	= "A0018_NAD";

	// For PCMP 766-Channel Specific Implementation
	public final String							RET_STR			= "RET";

	// For PCMP 766-Channel Specific Implementation
	public final String							NAD_STR			= "NAD";

	// For PCMP 766-Channel Specific Implementation
	public final String							SCC_STR			= "SCC";

	public final String							A0191_STR		= "A0191";

	public final String							A0015_STR		= "A0015";

	public final String							OH_STR			= "OH";

	public final String							NI_STR			= "NI";

	public final String							A0085_A0086_STR	= "A0085_A0086";

	// public static PCMLogger ehfLogger = PCMLogger.getInstance(EHF_LOGGER);

	// static PCMLogger logger = PCMLogger.getInstance(FREEFORM_TRACE_LOGGER);

	static IntgSrvLogger						logger			= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER_CNP);
	public static IntgSrvLogger					ehfLogger		= IntgSrvLogger.getInstance(EHF_LOGGER);

	public static ErrorHandlingFrameworkICD		ehfICD			= null;

	public static ErrorHandlingFrameworkHandler	ehfHandler		= new ErrorHandlingFrameworkHandler();

	public static String						traceId			= null;

	ObjectFactory								objectFactory;

	/**
	 * This method is used to update the step product information object for
	 * create the step XML.
	 * 
	 * @param attributeMap
	 * @param transType
	 * @return
	 */
	public STEPProductInformation updatedStepProdInformation(Map<String, String> attributeMap, String transType) {

		STEPProductInformation stepProductInformation = null;
		ProductsType products = null;
		ProductType product = null;
		ValuesType values = null;
		ValueType value = null;
		if (objectFactory == null) {
			objectFactory = new ObjectFactory();
		}
		stepProductInformation = objectFactory.createSTEPProductInformation();
		products = objectFactory.createProductsType();
		product = objectFactory.createProductType();
		values = objectFactory.createValuesType();

		for (Entry<String, String> mapObj : attributeMap.entrySet()) {
			if(!IntgSrvUtils.isNullOrEmpty(mapObj.getValue())) {
			value = objectFactory.createValueType();
			value.setAttributeID(mapObj.getKey());
			value.setContent(mapObj.getValue());
			values.getValueOrMultiValueOrValueGroup().add(value);
			}
		}
		//For Golden Gate Bridge-since all products are considered Item Create
		if (ITEMCREATE.equalsIgnoreCase(transType)) {
			if (!IntgSrvUtils.isEmptyString(IntgSrvPropertiesReader.getProperty(PARENT_ID_ITEM_FOLDER_NAME))) {
				product.setParentID(IntgSrvPropertiesReader.getProperty(PARENT_ID_ITEM_FOLDER_NAME));

			} else {
				product.setParentID(DEFAULT_PARENT_ID_ITEM_FOLDER_NAME);
			}
		}
		//product.setParentID(DEFAULT_PARENT_ID_ITEM_FOLDER_NAME);
		// A1363 is not received from PIMCore, its newly created in STEP for
		// holding Galaxy SKU number
		value = objectFactory.createValueType();
		value.setAttributeID(A1363_STR);
		value.setContent(attributeMap.get(A0012_STR));
		values.getValueOrMultiValueOrValueGroup().add(value);

		product.setUserTypeID(ITEM_STR);// Hard coded value
		product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
//		NameType nametype = objectFactory.createNameType();
//		nametype.setContent(attributeMap.get(A0019_STR));
//		product.getName().add(nametype);

		products.getProduct().add(product);
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
																								// Date
																								// time
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
																// value
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
																		// coded
																		// value
		stepProductInformation.setUseContextLocale(USE_CONTEXT_LOCALE_VALUE);// Hard
																				// coded
																				// value
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded
																	// value
		stepProductInformation.setProducts(products);
		return stepProductInformation;
	}

	/**
	 * This method is used to do the transformation for PIM Core values to Step
	 * expected format.
	 * 
	 * @param attributeMap
	 * @return
	 */
	public Map<String, String> transformationProcess(Map<String, String> attributeMap) {

		if (attributeMap != null && !attributeMap.isEmpty()) {

			if (attributeMap.get(A0213_STR) != null && attributeMap.get(A0254_STR) != null) {

				String a0213_temp = attributeMap.get(A0213_STR);
				// Added for trim the left side Zero(s)
				if (IntgSrvUtils.isNumeric(a0213_temp)) {
					a0213_temp = "" + IntgSrvUtils.toInt(a0213_temp);
				}
				String A0213 = a0213_temp + " : " + attributeMap.get(A0254_STR);
				attributeMap.put(A0213_STR, A0213);
			}

			// A0026= A0026+A0412.replace(A0026,:)
			//LOV Transformation removed for Classcode as part of DataMigration impact (MH)
//			if (attributeMap.get(A0026_STR) != null) {
//				// String A0026 =
//				// getConcatenateValue(attributeMap.get(A0026_STR),attributeMap.get(A0412_STR),
//				// Boolean.FALSE);
//				String A0026 = DatamigrationCommonUtil.getValuesFromLOV(A0026_STR, attributeMap.get(A0026_STR), Boolean.TRUE);
//				attributeMap.put(A0026_STR, A0026);
//			}

			// A0085 = read from properties file (country code)
			if (attributeMap.get(A0085_STR) != null) {
				// "country-code-name"
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0085_A0086_STR, attributeMap.get(A0085_STR), Boolean.TRUE);
				attributeMap.put(A0085_STR, value);
			}

			// A0086 = read from properties file (country code)
			if (attributeMap.get(A0086_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0085_A0086_STR, attributeMap.get(A0086_STR), Boolean.TRUE);
				attributeMap.put(A0086_STR, value);
			}

			// A0025 = A0025
			if (attributeMap.get(A0025_STR) != null) {
				String A0025 = DatamigrationCommonUtil.getValuesFromLOV(A0025_STR, attributeMap.get(A0025_STR), Boolean.TRUE);
				attributeMap.put(A0025_STR, A0025);
			}

			// A0190 = read from properties file (distribution center)
			if (attributeMap.get(A0190_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0190_STR, attributeMap.get(A0190_STR), Boolean.TRUE);
				attributeMap.put(A0190_STR, value);
			}

			// A0024 = A0024 + A0408.replace(A0024,:)
			if (attributeMap.get(A0024_STR) != null) {
				String A0024 = DatamigrationCommonUtil.getValuesFromLOV(A0024_STR, attributeMap.get(A0024_STR), Boolean.TRUE);
				attributeMap.put(A0024_STR, A0024);
			}

			// A0210 = read from properties (Selling uom)
			if (attributeMap.get(A0210_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0210_STR, attributeMap.get(A0210_STR), Boolean.TRUE);
				attributeMap.put(A0210_STR, value);
			}

			// A0029 = A0029 + A0248.replace(A0029, :)
			if (attributeMap.get(A0029_STR) != null) {
				String A0029 = getConcatenateValue(attributeMap.get(A0029_STR), attributeMap.get(A0248_STR), Boolean.TRUE);
				attributeMap.put(A0029_STR, A0029);
			}

			// A0214 = A0214 + A0251.replace(A0214, :)
			if (attributeMap.get(A0214_STR) != null) {
				String A0214 = getConcatenateValue(attributeMap.get(A0214_STR), attributeMap.get(A0251_STR), Boolean.FALSE);
				if (attributeMap.get(A0214_STR).length() == 1) {
					A0214 = "0" + A0214;
				}
				attributeMap.put(A0214_STR, A0214);
			}

			if (attributeMap.get(A0197_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0197_STR, attributeMap.get(A0197_STR), Boolean.FALSE);
				attributeMap.put(A0197_STR, value);
			}
			if (attributeMap.get(A0191_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0191_STR, attributeMap.get(A0191_STR), Boolean.TRUE);
				attributeMap.put(A0191_STR, value);
			}
			if (attributeMap.get(A0037_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0037_STR, attributeMap.get(A0037_STR), Boolean.FALSE);
				attributeMap.put(A0037_STR, value);
			}
			if (attributeMap.get(A0011_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0011_STR, attributeMap.get(A0011_STR), Boolean.TRUE);
				attributeMap.put(A0011_STR, value);
			}
			if (attributeMap.get(A0212_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0212_STR, attributeMap.get(A0212_STR), Boolean.TRUE);
				attributeMap.put(A0212_STR, value);
			}
			if (attributeMap.get(A0043_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0043_STR, attributeMap.get(A0043_STR), Boolean.TRUE);
				attributeMap.put(A0043_STR, value);
			}
			if (attributeMap.get(A0189_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0189_STR, attributeMap.get(A0189_STR), Boolean.TRUE);
				attributeMap.put(A0189_STR, value);
			}
			if (attributeMap.get(A0027_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0027_STR, attributeMap.get(A0027_STR), Boolean.TRUE);
				attributeMap.put(A0027_STR, value);
			}
			if (attributeMap.get(A0230_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0230_STR, attributeMap.get(A0230_STR), Boolean.TRUE);
				attributeMap.put(A0230_STR, value);
			}

			// For PCMP 766-Channel Specific Implementation
			if (attributeMap.get(A0018_RET_STR) != null) {
				String value = attributeMap.get(A0018_RET_STR).toUpperCase();
				attributeMap.put(A0018_RET_STR, value);
			}
			// For PCMP 766-Channel Specific Implementation
			if (attributeMap.get(A0018_NAD_STR) != null) {
				String value = attributeMap.get(A0018_NAD_STR).toUpperCase();
				attributeMap.put(A0018_NAD_STR, value);
			}
			// A0015 if NI --> Y else if OH --> N
			if (attributeMap.get(A0015_STR) != null) {
				String value = attributeMap.get(A0015_STR);
				if (OH_STR.equalsIgnoreCase(value)) {
					value = N_STR;
				} else if (NI_STR.equalsIgnoreCase(value)) {
					value = Y_STR;
				} else {
					value = DatamigrationCommonUtil.converYESNOIntoChar(value);
				}
				attributeMap.put(A0015_STR, value);
			}
			//PCMP-2047 As IARebuyer (A0028 and A0304) is changed to LOV in STEP
			if (attributeMap.get("A0028") != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV("A0028", attributeMap.get("A0028"), Boolean.TRUE);
				attributeMap.put("A0028", value);
			}
			if (attributeMap.get("A0304") != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV("A0304", attributeMap.get("A0304"), Boolean.TRUE);
				attributeMap.put("A0304", value);
			}
		}
		if(attributeMap.get("A0026")!=null)
		{
				String valueA0026 = attributeMap.get("A0026");
				attributeMap.put("ClassCode", valueA0026);
				attributeMap.remove("A0026");
		}
		//PCMP-2511
		if (attributeMap.get("A0172") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0172", attributeMap.get("A0172"), Boolean.TRUE);
			attributeMap.put("A0172", value);
		}
		if (attributeMap.get("A0171") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0171", attributeMap.get("A0171"), Boolean.TRUE);
			attributeMap.put("A0171", value);
		}
		//Attributes removed as part of Data Migration
		attributeMap.remove("A0024");
		attributeMap.remove("A0025");
		// FIXME Should be remove when go live.. this temporary for testing
		// attributeMap.put(A0026_STR, "471 : PTO/STEP Core Print");

		return attributeMap;
	}
	
	/**
	 * This method is used to concatenate the two fan Id values
	 * 
	 * @param attrValue1
	 * @param attrValue2
	 * @return
	 */
	private static String getConcatenateValue(String attrValue1, String attrValue2, boolean isZeroLTrim) {

		//if (attrValue1 != null && attrValue2 != null) {
		if(!IntgSrvUtils.isNullOrEmpty(attrValue1) && !IntgSrvUtils.isNullOrEmpty(attrValue2)) {
			attrValue2 = attrValue2.replaceFirst(attrValue1, "").trim();
			if (isZeroLTrim && IntgSrvUtils.isNumeric(attrValue1)) {
				attrValue1 = "" + IntgSrvUtils.toInt(attrValue1);
			}
			attrValue1 = attrValue1 + " : " + attrValue2;
		}
		return attrValue1;
	}

	/**
	 * Generate the XML using marshaler
	 * 
	 * @param stepProductInformation
	 * @return
	 * @throws Exception
	 */
	public String generateStepXML(STEPProductInformation stepProductInformation) throws Exception {

		StringWriter stringWriter = new StringWriter();

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(STEPProductInformation.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			jaxbMarshaller.marshal(stepProductInformation, stringWriter);

			// Need to remove
			/*
			 * File file = new File(DEFAULT_STEP_XML_TEST);
			 * file.getParentFile().mkdirs();
			 * jaxbMarshaller.marshal(stepProductInformation, file);
			 */

			logger.info("Marshalling Done.");

		} catch (JAXBException e) {
			throw new Exception(e);
		}
		return stringWriter.toString();
	}

	/**
	 * Method used to generate XML file
	 * 
	 * @param commonVO
	 * @param jaxbMarshaller
	 */
	public void xmlFileCreateForDebug(STEPProductInformation stepProductInformation, String transType) {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(STEPProductInformation.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
			String path = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(IntgSrvAppConstants.CNP_INBOUND_XML_OUTPUT_DIRECTORY));
			;

			logger.info("Generate the xml file for debug.");
			File file = new File(path + "Step-" + transType + "-" + new Date().getTime() + ".xml");

			file.getParentFile().mkdirs();
			logger.info("File location is:" + file.getPath());
			jaxbMarshaller.marshal(stepProductInformation, file);
		} catch (Exception exp) {
			logger.warn("Problem in XML file creation...");
		}

	}

	/**
	 * @return objectFactory
	 */
	public ObjectFactory getObjectFactory() {

		return objectFactory;
	}

	/**
	 * @param objectFactory
	 */
	public void setObjectFactory(ObjectFactory objectFactory) {

		this.objectFactory = objectFactory;
	}

	/**
	 * This method used to load the In bound mapping XML.
	 */
	private void loadMappingXML() {

		DatamigrationCommonUtil.printConsole("Inbound mapping XML is loading..");
		logger.info("Inbound mapping XML is loading..");
		String path = DatamigrationCommonUtil.getConfigDir(null);
		MappingLoader.getInstanceLoadContext().loadMappingXML(path + INBOUND_MAPPING_FILE);
		DatamigrationCommonUtil.printConsole("Inbound mapping XML load completed..");
		logger.info("Inbound mapping XML load completed..");
		DatamigrationCommonUtil.printConsole("Mapping size:" + MappingLoader.mappingAttList.size());
		logger.info("Mapping size:" + MappingLoader.mappingAttList.size());
	}

	/**
	 * @param attObj
	 * @return
	 */
	private static String getLevelBasedXPath(Attribute attObj) {

		String xpath = attObj.getPimCore().getXPath();
		String nodeName = attObj.getPimCore().getReference().getNodeName();
		String nodeLevel = attObj.getPimCore().getReference().getNodeLevel();

		String endElement = "/";
		if (attObj.getPimCore().getEndElement().contains("@")) {
			endElement += attObj.getPimCore().getEndElement();
		} else {
			endElement += attObj.getPimCore().getEndElement() + "[1]";
		}

		String nodes[] = xpath.split("/");
		String tempXPath = "";
		for (String nodeVal : nodes) {

			if (nodeVal.equalsIgnoreCase(nodeName)) {
				tempXPath += "/" + nodeVal + "[" + nodeLevel + "]";
			} else if (!nodeVal.isEmpty()) {
				tempXPath += "/" + nodeVal + "[1]";
			}
		}

		return tempXPath + endElement;
	}

	/*
	 * This function is used to fetch and return the next level xpath of an
	 * attribute if the current levelBasedXPath is not available in the
	 * pimCoreMap
	 */
	public String traverseXpath(Mapping.Attributes.Attribute attObj, String levelBasedXPath, Map<String, String> pimCoreMap) {

		if (!pimCoreMap.containsKey(levelBasedXPath)) {
			int index = 0;
			while (true && index <= 4) {
				if (levelBasedXPath.contains("@"))
					levelBasedXPath = getXPathForType(levelBasedXPath, true) + "[" + (++index) + "]/"
							+ attObj.getPimCore().getEndElement().split("@")[1] + "[1]" + "/" + attObj.getPimCore().getEndElement();
				else
					levelBasedXPath = getXPathForType(levelBasedXPath, false) + "[" + (++index) + "]/"
							+ attObj.getPimCore().getEndElement() + "[1]";
				if (pimCoreMap.containsKey(levelBasedXPath)) {
					break;
				}
			}
		}
		return levelBasedXPath;
	}

	/**
	 * This method is used to map the xpath and FAN IDs.
	 * 
	 * @param pimCoreMap
	 * @return
	 */
	private Map<String, String> getStepAttributeValueFromPIMCore(Map<String, String> pimCoreMap) {

		Map<String, String> stepAttributeMap = new HashMap<String, String>();
		List<String> mappingList = new ArrayList<String>();
		DatamigrationCommonUtil.printConsole("PIM core XML total XPath:" + pimCoreMap.size());
		logger.info("PIM core XML total XPath:" + pimCoreMap.size());
		DatamigrationCommonUtil.printConsole("pim Core XML Details in Map:" + pimCoreMap);
		for (Mapping.Attributes.Attribute attObj : MappingLoader.mappingAttList) {
			String levelBasedXPath = getLevelBasedXPath(attObj);
			mappingList.add(levelBasedXPath);
			if (attObj.getStep().getAttributeID() == null || attObj.getStep().getAttributeID().isEmpty()) {
				DatamigrationCommonUtil.printConsole("FANID is not avalilable in mapping xml. levelBasedXPath is: " + levelBasedXPath
						+ ", Pim core value is=" + pimCoreMap.containsKey(levelBasedXPath));
				logger.info("FANID is not avalilable in mapping xml. levelBasedXPath is: " + levelBasedXPath + ", Pim core value is="
						+ pimCoreMap.containsKey(levelBasedXPath));
			}

			// For PCMP 766-Channel Specific Implementation
			// To fetch the next level xpath of an attribute if the current
			// levelBasedXPath is not available in the pimCoreMap
			String nextPath = traverseXpath(attObj, levelBasedXPath, pimCoreMap);
			if (pimCoreMap.containsKey(levelBasedXPath) || pimCoreMap.containsKey(nextPath)) {
				// For PCMP 766-Channel Specific Implementation
				// if loop for channel specific attributes, else for other
				// attributes
				if (attObj.getStep().getAttributeID().equals(A0013_STR) || attObj.getStep().getAttributeID().equals(A0018_STR)
						|| attObj.getStep().getAttributeID().equals(A0045_STR) || attObj.getStep().getAttributeID().equals(A0046_STR)
						|| attObj.getStep().getAttributeID().equals(A0067_STR) || attObj.getStep().getAttributeID().equals(A0075_STR)
						|| attObj.getStep().getAttributeID().equals(A0077_STR) || attObj.getStep().getAttributeID().equals(A0078_STR)) {
					getChannelSpecificValues(attObj, levelBasedXPath, pimCoreMap, stepAttributeMap);
				} else {
					stepAttributeMap.put(attObj.getStep().getAttributeID(), pimCoreMap.get(levelBasedXPath));
				}
			} else {
				// DatamigrationCommonUtil.printConsole("XPath "+levelBasedXPath+" - is not available for :"+attObj.getStep().getAttributeID());
				// logger.info("XPath "+levelBasedXPath+" - is not available for :"+attObj.getStep().getAttributeID());
			}
		}

		return stepAttributeMap;
	}

	/*
	 * This function is used to return the substring of XPath which can be used
	 * as a base for the channel specific xpaths parameters: xpath and a boolean
	 * variable which states if the given xpath has "@' in it or not
	 */
	public String getXPathForType(String xpath, Boolean flag) {

		String[] values = xpath.split("/");
		String baseXpath = null;
		int length = 0;
		if (flag == true) {
			length = values.length - 2;
		} else {
			length = values.length - 1;
		}
		for (int incr = 0; incr < length; incr++) {
			if (incr == 0) {
				baseXpath = values[incr];
			} else if (incr == length - 1) {
				baseXpath = baseXpath + "/" + values[incr].split("\\[")[0];
			} else {
				baseXpath = baseXpath + "/" + values[incr];
			}
		}
		return baseXpath;
	}

	/*
	 * This function is used to set the attribute ids and values to the
	 * stepAttributeMap for the 8 channel specific attributes The channel
	 * type(RET,SCC) is fetched for each level Xpath of the channel specific
	 * attributes and based on the type, the corresponding values are set to the
	 * stepAttributeMap
	 */
	public void getChannelSpecificValues(Mapping.Attributes.Attribute attObj, String levelBasedXPath, Map<String, String> pimCoreMap,
			Map<String, String> stepAttributeMap) {

		boolean flag = false;
		if (levelBasedXPath.contains("@")) {
			flag = true;
		}
		String path = getXPathForType(levelBasedXPath, flag);
		int i = 1;
		while (true) {
			String type = pimCoreMap.get(path + "[" + i + "]" + "/Type[1]");
			if (type != null) {
				if (type.equals(RET_STR)) {
					if (flag) {
						stepAttributeMap.put(attObj.getStep().getAttributeID() + "_" + type, pimCoreMap.get(path + "[" + i + "]/"
								+ attObj.getPimCore().getEndElement().split("@")[1] + "[1]" + "/" + attObj.getPimCore().getEndElement()));
					} else {
						stepAttributeMap.put(attObj.getStep().getAttributeID() + "_" + type, pimCoreMap.get(path + "[" + i + "]/"
								+ attObj.getPimCore().getEndElement() + "[1]"));
					}
				}
				if (type.equals(SCC_STR)) {
					if (flag) {
						stepAttributeMap.put(attObj.getStep().getAttributeID() + "_" + NAD_STR, pimCoreMap.get(path + "[" + i + "]/"
								+ attObj.getPimCore().getEndElement().split("@")[1] + "[1]" + "/" + attObj.getPimCore().getEndElement()));
					} else {
						stepAttributeMap.put(attObj.getStep().getAttributeID() + "_" + NAD_STR, pimCoreMap.get(path + "[" + i + "]/"
								+ attObj.getPimCore().getEndElement() + "[1]"));
					}
				}
				i++;
			} else {
				break;
			}

		}
	}

	/**
	 * @param message
	 * @return
	 */
	public Map<String, String> getPIMCoreXMLXPathAndValue(String message) {

		Map<String, String> xpathValueMap = null;
		PIMCoreContentHandler pimCoreContentHandler = null;
		logger.info("PIM core message" + message);
		logger.info("PIM Core XML parsing start..");
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			pimCoreContentHandler = new PIMCoreContentHandler(xr, new HashMap<String, String>());
			xr.setContentHandler(pimCoreContentHandler);
			xr.parse(new InputSource(new StringReader(message)));

			DatamigrationCommonUtil.printConsole("PIM Core XML Parsing completed." + pimCoreContentHandler.getxPathValueMap().size());
			logger.info("PIMCore XML Parsing completed." + pimCoreContentHandler.getxPathValueMap().size());

			xpathValueMap = pimCoreContentHandler.getxPathValueMap();

			if (xpathValueMap != null && !xpathValueMap.isEmpty()) {
				xpathValueMap = new HashMap<String, String>(xpathValueMap);
			} else {
				DatamigrationCommonUtil.printConsole("PIMCore XML Invalid.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			logger.error(e);
		}
		// Need to reset the xPathValueMap for next process.
		resetPIMCoreXMLMap(pimCoreContentHandler);

		return xpathValueMap;
	}

	/**
	 * This method used to clear the existing object for next process. (Static
	 * object used for XML Content handler process)
	 * 
	 * @param pimObj
	 */
	private static void resetPIMCoreXMLMap(PIMCoreContentHandler pimObj) {

		pimObj.setxPathValueMap(new HashMap<String, String>());
	}

	/**
	 * This method is used to process the conversion PIM core XML to Step XML.
	 * 
	 * @param message
	 *            message which is received from PIM Core (ESB) system
	 * @return XML string which is compatible for STEP.
	 * @throws CustomException 
	 * @throws ErrorHandlingFrameworkException
	 */
	public boolean stepInboundIntgProcess(String message, String transType) throws CustomException {

		String logMessage = null;
		try {

			// Removing the '&' character from the input message(if any) to
			// avoid SAXParserException
			message = message.replaceAll("&", "&amp;");

			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(SPRINGBATCH_ECO_SYSTEM, SPRINGBATCH_COMPONENT_ID,
					SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId(PUBLISH_ID_NEW);// FIXME
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();

			ehfICD.setAttributeTransactionType(EHF_ATTR_TRANSACTION_TYPE_QUEUETOSPRING);
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH4, EHF_MSGTYPE_INFO_NONSLA,
					"Step Inbound message received message from queue for " + transType, EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			// 1. Get Mapping xml details
			loadMappingXML();
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH4, EHF_MSGTYPE_INFO_NONSLA,
					"Inbound mapping XML load completed..", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
					DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			if (TRUE_STR.equalsIgnoreCase(IntgSrvPropertiesReader.getProperty(DEBUG_XML_GENERATE_FLAG))) {

				debugFileWrite(message, IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
						.getProperty(IntgSrvAppConstants.CNP_INBOUND_XML_INPUT_DIRECTORY)), transType, "PIMCore");
			}

			// 2. Parse PIMcore.xml
			Map<String, String> pimCoreMap = getPIMCoreXMLXPathAndValue(message);

			ehfICD.setAttributeRequestType("TransactionType :" + transType);// FIXME
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH5, EHF_MSGTYPE_INFO_NONSLA,
					"Retrieve the xPath values from PIMCore message...", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil
							.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			// 3. Get the Pimcore xpath from pimcore.xml(pimMap) and get the
			// Step attribute Id from mapping.xml (mappingMap)
			// as Map<String, String>(attributeId, attributeValue)
			Map<String, String> attributeMap = getStepAttributeValueFromPIMCore(pimCoreMap);
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
					"Retrieve the step attribute ID's and values from PIMCore Message..", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
			String classCode = attributeMap.get(A0026_STR);
			if (!("471".equalsIgnoreCase(classCode) || "476".equalsIgnoreCase(classCode) || "479".equalsIgnoreCase(classCode))) {
				logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
						"Non copy print item, so the item is skipped.", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil
								.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.info(logMessage);
				return true;
			}
			attributeMap = transformationProcess(attributeMap);

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
					"Transformation is completed.", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
					DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			// 4. Feed the pim values in stepinformation.java
			STEPProductInformation stepProductInformation = updatedStepProdInformation(attributeMap, transType);

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
					"Update PIMCore data into StepProdInformation Object..", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil
							.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			// 5. Generate the step.xml using marsheller

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
					"Step XML generation start...", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
					DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			String stepXml = generateStepXML(stepProductInformation);

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA, "Step XML generation end...",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);
			// 6. Publish the step.xml to step Queue
			try {

				logger.info("Converted XML for Step Queue : " + stepXml);
				DatamigrationCommonUtil.printConsole("Converted XML for Step Queue : " + stepXml);
				// Step XML publish call starts...

				if (stepXml != null) {
					ehfICD.setAttributeTransactionType("Spring-to-Queue");
					logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
							"Step XML publish start...", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
							DatamigrationCommonUtil.getClassName(), ehfICD);
					ehfLogger.info(logMessage);

					StepTransmitterBean serviceBean = new StepTransmitterBean();
					serviceBean.setMessage(stepXml);
					serviceBean.setMqChannel(IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.CNP_MQ_CHANNEL_STEP_IN));
					serviceBean.setMqHostName(IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.CNP_MQ_HOSTNAME_STEP_IN));
					serviceBean.setMqPport(Integer.parseInt(IntgSrvPropertiesReader
							.getProperty(DatamigrationAppConstants.CNP_MQ_PORT_STEP_IN)));
					serviceBean.setMqQueueManager(IntgSrvPropertiesReader
							.getProperty(DatamigrationAppConstants.CNP_MQ_QUEUEMANAGER_STEP_IN));
					serviceBean.setMqQueueName(IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.CNP_MQ_QUEUENAME_STEP_IN));
					try{
					MQConnectionManager mqconnectionmanager = MQConnectionManager.getInstance(serviceBean);
					mqconnectionmanager.putMessage(serviceBean);
					mqconnectionmanager.close();
					}catch(JMSException e){
						 CustomException c=new CustomException();
						 c.onException(e);
					}
					// FIXME
					// StepInboundPublisher.messageSender(stepXml,transType);

					logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
							"Step XML publish end.. Published successfully...", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil
									.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
					ehfLogger.info(logMessage);
					logger.info("Step XML has been sent.");

					DatamigrationCommonUtil.printConsole("Step XML has been sent.");

					if (TRUE_STR.equalsIgnoreCase(IntgSrvPropertiesReader.getProperty(DEBUG_XML_GENERATE_FLAG))) {
						xmlFileCreateForDebug(stepProductInformation, transType);
					}

				} else {
					logger.info("PIMCore message is not processed.");
				}
			} catch (Exception e) {

				DatamigrationCommonUtil.printConsole("Exception e....");
				logMessage = ehfHandler.getErrorLog(new Date(), traceId, EHF_ERROR_PATH2, e, EHF_SPRINGBATCH_ITEMUTILITY_USER,
						DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.error(logMessage);
				logger.error(e);
				logger.info("Not able to publish. So message will be rollback....");
				logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
						"Not able to publish. So message will be rollback....", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil
								.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.info(logMessage);
				e.printStackTrace();
				throw new Exception(e);
				// return Boolean.FALSE;
			}
		} catch (ErrorHandlingFrameworkException e) {
		} catch (Exception e) {
			logger.info("Exception : Please check error log.." + e.getMessage());
			logger.error(e);
		}

		logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH1, EHF_MSGTYPE_INFO_NONSLA,
				"PIMCore to STEP Intg process end.", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
				DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.info(logMessage);
		logger.info("End:" + DatamigrationCommonUtil.getClassAndMethodName());
		ehfICD.resetContextElements();
		logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH2, EHF_MSGTYPE_INFO_NONSLA, "Waiting for next message....",
				EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.info(logMessage);
		return Boolean.TRUE;
	}

	public static void debugFileWrite(String message, String debugFilePath, String transType, String systemName) {

		File folder = new File(debugFilePath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		logger.info("Generate the xml file for debug.");
		writerFile(debugFilePath + systemName + "-" + transType + "-" + new Date().getTime() + ".xml", message);
	}

	public static void writerFile(String fileName, String Clog) {

		try {
			if (fileName != null) {
				fileName = IntgSrvUtils.reformatFilePath(fileName);
				createNewFile(fileName);

				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName, false)));
				pw.write(Clog);
				pw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

	public static void createNewFile(String fileName) {

		try {
			File file = new File(fileName);
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
		} catch (Exception exp) {
			logger.error(exp);
		}
	}

	public static void main(String st[]) {

		try {
			System.out.println("Type[1]".split("\\[")[0]);

			// String testMessage =
			// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><item:PublishItem Action=\"ItemCreate\" Version=\"3.0\" xmlns:item=\"http://schemas.staples.com/esb/services/PublishItemV3_0\"><item:Item State=\"AfterImage\" Type=\"Service\"><TransactionList><Transaction><Id>PIM00200003620ffff2797121512af</Id><MessageList><Message><Id>PIME004</Id><Type>Service</Type></Message></MessageList><Type>New Item</Type><DatetimeList><Start><DateTime>2013-02-06-03.07.55.000200</DateTime></Start></DatetimeList><Text>PIME004</Text></Transaction></TransactionList><ID>200003620</ID><Nmb>119928</Nmb><RestrictedStateList/><CodeList><Code><Disposition><Code>W02</Code><Type>Warehouse</Type></Disposition><Disposition><Code>N02</Code><Type>NAD</Type></Disposition><Disposition><Code>S01</Code><Type>Store</Type></Disposition><Hierarchy><Division><Code>1</Code><Name><![CDATA[1 Furniture]]></Name></Division><Department><Code>17</Code><Name><![CDATA[17 Chair Mats]]></Name></Department><Class><Code>170</Code><Name><![CDATA[170 Chair Mats]]></Name></Class></Hierarchy><OriginCountry><Code>US</Code><Id>1</Id></OriginCountry><OriginCountry><Code/><Id>2</Id></OriginCountry><OriginCountry><Code/><Id>3</Id></OriginCountry><OriginCountry><Code/><Id>4</Id></OriginCountry><OriginCountry><Code/><Id>5</Id></OriginCountry><Planogram><Id>400</Id></Planogram><Proprietary><Code/></Proprietary><SubClass><Code>170</Code></SubClass><Stock><Code/></Stock></Code></CodeList><CostList><Cost><Consignment><Code>N</Code></Consignment><PO><Cost><Amount Amount=\"5\"><Type>COR</Type></Amount></Cost></PO></Cost></CostList><DescriptionList><Description><Additional><Description><Description>Bulk Business Service Test 16</Description><Type>COR</Type></Description></Additional><CashRegister/><Short/></Description></DescriptionList><DimensionList><Dimension><Case><Height>1</Height><Length>1</Length><Weight>1</Weight><Width>1</Width></Case><SUCP><Type>COR</Type><Quantity>1</Quantity></SUCP><Unit><Length>1</Length><Width>1</Width><Height>1</Height><Weight>1</Weight></Unit><WHSP><Quantity>1</Quantity></WHSP></Dimension></DimensionList><IndicatorList><Indicator><DSD>N</DSD><FormValidate>N</FormValidate><MasterShipper>N</MasterShipper><NonInventory>OH</NonInventory><PriceSensitive>N</PriceSensitive><DateSensitive>N</DateSensitive><DropShip>2</DropShip><Hazardous>N</Hazardous><Imprinted/><MSDSRequired>N</MSDSRequired><Recycled>N</Recycled><StateRestricted>N</StateRestricted><HighRisk>N</HighRisk><XItem>0</XItem><ContractXItem>N</ContractXItem></Indicator></IndicatorList><ItemList><Manufacturer><Nmb>119928</Nmb><Type>COR</Type></Manufacturer></ItemList><MSDS><URL Type=\"MSDSDoc\"/><DocumentName/></MSDS><PartyList><Party><VendorList><Primary><Nmb>41</Nmb><Name>TRIGEM AMERICA CORP</Name><Type>COR</Type></Primary></VendorList></Party></PartyList><PriceList><Price><List><Type>1</Type></List><ManufacturerList><Amount Amount=\"9\"><Type>COR</Type></Amount></ManufacturerList><Retail><Amount Amount=\"15\"><Type>COR</Type></Amount></Retail></Price></PriceList><QuantityList><Quantity><Purchase><UnitOfMeasure>EA</UnitOfMeasure></Purchase><Selling><UnitOfMeasure>EA</UnitOfMeasure><UnitOfMeasureQuantity>4</UnitOfMeasureQuantity></Selling></Quantity></QuantityList><ReferenceList><Reference><Nmb>04</Nmb><Name>Erin Quaratino</Name><Type>ContractProductManager</Type></Reference><Reference><Nmb>533</Nmb><Name>Rachel Kourey</Name><Type>ContractProductSpecialist</Type></Reference><Reference><Nmb>223</Nmb><Name>Carol Rees</Name><Type>Staples.comProductManager</Type></Reference><Reference><Nmb>576</Nmb><Name>Chris Cashman</Name><Type>Staples.comProductSpecialist</Type></Reference><Reference><Nmb>044</Nmb><Name>Melissa Yanagi</Name><Type>RetailProductManager</Type></Reference><Reference><Nmb>506</Nmb><Name>Nicole Mikelson</Name><Type>RetailProductSpecialist</Type></Reference><Reference><Nmb/><Name/><Type>NADInventoryAnalyst</Type></Reference><Reference><Nmb>70</Nmb><Name/><Type>RetailInventoryAnalyst</Type></Reference></ReferenceList><StatusList><Status><Contract><Code>A</Code></Contract><DotCom><Code>A</Code></DotCom><Quill><Code>A</Code></Quill><Retail><Code>A</Code></Retail></Status></StatusList><ThresholdList><Threshold><PostConsumerContent>0</PostConsumerContent></Threshold></ThresholdList><UPCList><UPC><Name>SellingUnit</Name><Nmb>N/A</Nmb><Type>A</Type></UPC><UPC><Name>InnerPack</Name><Nmb>N/A</Nmb></UPC><UPC><Name>MasterCasePack</Name><Nmb>N/A</Nmb></UPC><UPC><Name>Pallet</Name><Nmb>N/A</Nmb></UPC></UPCList><ItemIDList><ItemID><Nmb/><Type>Quill</Type></ItemID></ItemIDList><InventoryGroup><Code>1</Code></InventoryGroup></item:Item></item:PublishItem>";
			// String
			// sar="<?xml version=\"1.0\" encoding=\"UTF-8\"?><item:PublishItem Version=\"3.0\" Action=\"ItemCreate\"><item:Item Type=\"Standard\" State=\"AfterImage\"><CodeList><Code><Brand><Name>n/a</Name></Brand><Category><Name>Accessories</Name><Type>Copy</Type></Category><Disposition><Code>S01</Code><Type>Store</Type></Disposition><Disposition><Code>W03</Code><Type>Warehouse</Type></Disposition><Disposition><Code>N03</Code><Type>NAD</Type></Disposition><Disposition><Type>Inactivated</Type></Disposition><Disposition><Type>DC1</Type></Disposition><Disposition><Type>DC2</Type></Disposition><Disposition><Type>DC3</Type></Disposition><Disposition><Type>DC4</Type></Disposition><Disposition><Type>DC5</Type></Disposition><EnvironmentalDesign><Type>Design1</Type></EnvironmentalDesign><EnvironmentalDesign><Type>Design2</Type></EnvironmentalDesign><EnvironmentalDesign><Type>Other</Type></EnvironmentalDesign><GLN><Code>1</Code></GLN><GTIN><Code>1</Code></GTIN><Hierarchy><Class><Code>331</Code><Name>331 Mass Storage - Disk &amp; Tap</Name></Class><Department><Code>33</Code><Name>33 Peripherals</Name></Department><Division><Code>3</Code><Name>3 Computers &amp; Accessories</Name></Division></Hierarchy><OriginCountry><Code>AG</Code><Id>1</Id></OriginCountry><OriginCountry><Id>2</Id></OriginCountry><OriginCountry><Id>3</Id></OriginCountry><OriginCountry><Id>4</Id></OriginCountry><OriginCountry><Id>5</Id></OriginCountry><OtherEnvironmentCertification><Type>Certification1</Type></OtherEnvironmentCertification><OtherEnvironmentCertification><Type>Certification2</Type></OtherEnvironmentCertification><Planogram><Id>1</Id></Planogram><SellingDivision><Code>Y</Code><Type>Dotcom</Type></SellingDivision><SellingDivision><Code>Y</Code><Type>Contract</Type></SellingDivision><SellingDivision><Code>N</Code><Type>Retail</Type></SellingDivision><SellingDivision><Code>N</Code><Type>Promotional</Type></SellingDivision><SellingDivision><Code>Y</Code><Type>Direct</Type></SellingDivision><SkuSet><Code>0</Code></SkuSet><SpecialPackaging><Code>0</Code></SpecialPackaging><SubClass><Code>331</Code></SubClass></Code></CodeList><CostList><Cost><Consignment><Amount Amount=\"0\"/><Code>N</Code></Consignment><PO><Cost><Type>Future</Type></Cost><Cost><Amount Amount=\"12\"/><Type>SCC</Type></Cost><Cost><Amount Amount=\"12\"/><Type>RET</Type></Cost><Channel>SCC</Channel></PO></Cost></CostList><DateList><Additional><Date><Type>Release</Type></Date><Date><Type>RAExpiration</Type></Date></Additional></DateList><DescriptionList><Description><Additional><Description><Description>N/A</Description><Type>SCC</Type></Description><Description><Description>N/A</Description><Type>RET</Type></Description><Description><Type>Bulletin</Type></Description></Additional></Description></DescriptionList><DimensionList><Dimension><Case><Height>13</Height><Length>13</Length><Weight>13</Weight><Width>13</Width></Case><InnerPack><Height>12</Height><Length>12</Length><Weight>12</Weight><Width>12</Width></InnerPack><Pallet><Height>0</Height><Length>0</Length><Width>0</Width></Pallet><Unit><Height>11</Height><Length>11</Length><Weight>11</Weight><Width>11</Width></Unit><WHSP><Quantity>1</Quantity></WHSP><SUCP><Quantity>1</Quantity><Type>RET</Type></SUCP><SUCP><Quantity>1</Quantity><Type>SCC</Type></SUCP><Purchase><Weight>13</Weight></Purchase></Dimension></DimensionList><HazMatList><Hazmat><ClassRequirements><Type>SubsidiaryRisk1</Type></ClassRequirements><ClassRequirements><Type>SubsidiaryRisk2</Type></ClassRequirements><LabelRequirements><Type>Private</Type></LabelRequirements><LabelRequirements><Type>SubsidiaryRisk1</Type></LabelRequirements><LabelRequirements><Type>SubsidiaryRisk2</Type></LabelRequirements><LimitedQuantity>N</LimitedQuantity><PrivateLabel><Name>N</Name><Type>NAR</Type></PrivateLabel><PrivateLabel><Name>N</Name><Type>NAD</Type></PrivateLabel></Hazmat></HazMatList><ID>400698801</ID><IndicatorList><Indicator><AirQuality>N</AirQuality><AllowBackOrders>N</AllowBackOrders><APNonToxic>N</APNonToxic><BioBased>0</BioBased><ChemicalBan>N</ChemicalBan><ChlorineFree>N</ChlorineFree><CleanAirAct>N</CleanAirAct><CodeDateProduct>N</CodeDateProduct><ContractXItem>Y</ContractXItem><Corrosive>N</Corrosive><DateSensitive>N</DateSensitive><DSD>N</DSD><EcoEasy>N</EcoEasy><EcoLogo>N</EcoLogo><EnergyStar>N</EnergyStar><EnvironmentalDesignFeatures>N</EnvironmentalDesignFeatures><EnvironmentHazardous>N</EnvironmentHazardous><EPADesign>N</EPADesign><FDA>N</FDA><FlammableSolid>N</FlammableSolid><ForestStewardshipCouncil>N</ForestStewardshipCouncil><Formaldehyde>N</Formaldehyde><FormValidate>N</FormValidate><GreenSeal>N</GreenSeal><Hazardous>N</Hazardous><HighRisk>N</HighRisk><Latex>N</Latex><Liquid>N</Liquid><Liquid16Ounce>N</Liquid16Ounce><MarinePollutant>N</MarinePollutant><MasterShipper>N</MasterShipper><MSDSRequired>N</MSDSRequired><NonInventory>OH</NonInventory><OverTheCounter>N</OverTheCounter><Palletized>Floor Loaded</Palletized><PricePrompt>N</PricePrompt><PriceSensitive>N</PriceSensitive><PrintFlag>N</PrintFlag><ProcurementAllowed>N</ProcurementAllowed><Proposition65>N</Proposition65><Recycled>N</Recycled><Refillable>N</Refillable><RemanufacturedRefurbished>N</RemanufacturedRefurbished><SerialNumber>Y</SerialNumber><SignageRequired>N</SignageRequired><StateRegulations>N</StateRegulations><StateRestricted>N</StateRestricted><SustainableForestInitiative>N</SustainableForestInitiative><ThirdPartyCertification>N</ThirdPartyCertification><UNPackagingRequired>N</UNPackagingRequired><XItem>0</XItem><PreOrder>N</PreOrder></Indicator></IndicatorList><InventoryGroup><Code>2</Code></InventoryGroup><InventoryLocationList><InventoryLocation><QuantityList><PalletHeight><Quantity>0</Quantity><Type>SCC</Type></PalletHeight><PalletHeight><Quantity>0</Quantity><Type>RET</Type></PalletHeight><PalletTier><Quantity>0</Quantity><Type>SCC</Type></PalletTier><PalletTier><Quantity>0</Quantity><Type>RET</Type></PalletTier><TotalAvailable><Quantity>0</Quantity></TotalAvailable></QuantityList></InventoryLocation></InventoryLocationList><ItemIDList><ItemID><Nmb>335607</Nmb><Type>PIP</Type></ItemID><ItemID><Type>Quill</Type></ItemID></ItemIDList><ItemList><Manufacturer><Nmb>134542634352</Nmb><Type>SCC</Type></Manufacturer><Manufacturer><Nmb>134542634352</Nmb><Type>RET</Type></Manufacturer><Copy><Code>n/a</Code><Id>0</Id><Name>Use</Name><Nmb>405</Nmb></Copy><Copy><Code>n/a</Code><Id>0</Id><Name>Compelling headline instructions</Name><Nmb>74</Nmb></Copy><Copy><Code>n/a</Code><Id>0</Id><Name>Brand</Name><Nmb>77</Nmb></Copy><Copy><Code>n/a</Code><Id>0</Id><Name>Type </Name><Nmb>115</Nmb></Copy><Copy><Code>n/a</Code><Id>0</Id><Name>Product overview</Name><Nmb>1095</Nmb></Copy><Copy><Code>No</Code><Id>0</Id><Name>Energy Star compliant</Name><Nmb>1097</Nmb></Copy><Copy><Code>n/a</Code><Id>0</Id><Name>Material/Construction</Name><Nmb>1096</Nmb></Copy></ItemList><ManufacturerItem><Name>n/a</Name><PartNmb>n/a</PartNmb></ManufacturerItem><MSDS><URL Type=\"MSDSDoc\"/></MSDS><Nmb>1007062</Nmb><PartyList><Party><VendorList><Primary><Name>UNIDEN AMERICA CORP</Name><Nmb>3871</Nmb><Type>SCC</Type></Primary><Primary><Name>UNIDEN AMERICA CORP</Name><Nmb>3871</Nmb><Type>RET</Type></Primary></VendorList></Party></PartyList><PriceList><Price><List><Type>4</Type></List><ManufacturerList><Amount Amount=\"13\"/><Type>SCC</Type></ManufacturerList><ManufacturerList><Amount Amount=\"13\"/><Type>RET</Type></ManufacturerList><Retail><Amount Amount=\"55\"/></Retail></Price></PriceList><QuantityList><Quantity><Additional><Quantity><Quantity>0</Quantity><Type>InkTonerCatridge</Type></Quantity></Additional><InnerPack><Quantity>1</Quantity></InnerPack><Purchase><UnitOfMeasure>EA</UnitOfMeasure></Purchase><Selling><UnitOfMeasure>EA</UnitOfMeasure><UnitOfMeasureQuantity>1</UnitOfMeasureQuantity><UnitOfMeasureNAD>EA</UnitOfMeasureNAD></Selling></Quantity></QuantityList><ReferenceList><Reference><Name>Scott Barnard</Name><Nmb>270</Nmb><Type>RetailProductManager</Type></Reference><Reference><Name>Jim Cree</Name><Nmb>219</Nmb><Type>Staples.comProductManager</Type></Reference><Reference><Name>Elizabeth Mason</Name><Nmb>658</Nmb><Type>Staples.comProductSpecialist</Type></Reference><Reference><Name>Steve Suesens</Name><Nmb>273</Nmb><Type>ContractProductManager</Type></Reference><Reference><Name>Cheryl Carvalho</Name><Nmb>659</Nmb><Type>ContractProductSpecialist</Type></Reference><Reference><Name>Ashley Milford</Name><Nmb>655</Nmb><Type>RetailProductSpecialist</Type></Reference><Reference><Type>PlanogramSpecialist</Type></Reference><Reference><Nmb>70</Nmb><Type>NADInventoryAnalyst</Type></Reference><Reference><Type>PlanogramSpecialist</Type></Reference><Reference><Nmb>11</Nmb><Type>RetailInventoryAnalyst</Type></Reference><Reference><Type>RetailProductManager</Type></Reference><Reference><Type>ContractProductManager'</Type></Reference></ReferenceList><RestrictionList><Restriction><Name>N</Name></Restriction></RestrictionList><ServicePlanList><ServicePlan><Nmb>yyutengtestmorning</Nmb></ServicePlan></ServicePlanList><StatusList><Status><Retail><Code>I</Code></Retail></Status></StatusList><ThresholdList><Threshold><PostConsumerContent>0</PostConsumerContent><TotalRecyledContent>0</TotalRecyledContent></Threshold></ThresholdList><TransactionList><Transaction><DatetimeList><Start><DateTime>2015-05-28-10.03.04.000751</DateTime></Start></DatetimeList><Id>PIM00400698801ffffffff8453c879</Id><MessageList><Message><Id>PIME004</Id><Type>Std</Type></Message></MessageList><Text>PIME004</Text><Type>New Item</Type></Transaction></TransactionList><UPCList><UPC><Name>SellingUnit</Name><Nmb>N/A</Nmb><Type>A</Type></UPC><UPC><Name>InnerPack</Name><Nmb>N/A</Nmb></UPC><UPC><Name>Pallet</Name><Nmb>N/A</Nmb></UPC><UPC><Name>MasterCasePack</Name><Nmb>N/A</Nmb></UPC></UPCList><UserList><User><EmailID>yuteng.pan@staples.com</EmailID><Type>Requestor</Type></User></UserList></item:Item></item:PublishItem>";
			String test2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><item:PublishItem Version=\"3.0\" Action=\"ItemCreate\"><item:Item Type=\"Service\" State=\"AfterImage\"><CodeList><Code><Disposition><Code>W02</Code><Type>Warehouse</Type></Disposition><Disposition><Code>N02</Code><Type>NAD</Type></Disposition><Disposition><Code>S01</Code><Type>Store</Type></Disposition><Hierarchy><Class><Code>476</Code><Name>476 PTO/STEP Custom Print</Name></Class><Department><Code>63</Code><Name>63 CUSTOM PRINT</Name></Department><Division><Code>6</Code><Name>6 BUSINESS SERVICES</Name></Division></Hierarchy><OriginCountry><Code>US</Code><Id>1</Id></OriginCountry><OriginCountry><Id>2</Id></OriginCountry><OriginCountry><Id>3</Id></OriginCountry><OriginCountry><Id>4</Id></OriginCountry><OriginCountry><Id>5</Id></OriginCountry><Planogram><Id>scc</Id></Planogram><SubClass><Code>476</Code></SubClass></Code></CodeList><CostList><Cost><Consignment><Code>N</Code></Consignment><PO><Cost><Amount Amount=\"1\"/></Cost></PO></Cost></CostList><DescriptionList><Description><Additional><Description><Description>TEST ITEM00002</Description><Type>COR</Type></Description></Additional></Description></DescriptionList><DimensionList><Dimension><Case><Height>2</Height><Length>2</Length><Weight>2</Weight><Width>2</Width></Case><Unit><Height>1</Height><Length>1</Length><Weight>1</Weight><Width>1</Width></Unit><WHSP><Quantity>1</Quantity></WHSP><SUCP><Quantity>1</Quantity><Type>COR</Type></SUCP></Dimension></DimensionList><ID>200260004</ID><IndicatorList><Indicator><Aerosol>N</Aerosol><AirQuality>N</AirQuality><APNonToxic>N</APNonToxic><BuyBack>N</BuyBack><ChlorineFree>N</ChlorineFree><CodeDateProduct>N</CodeDateProduct><ContractXItem>N</ContractXItem><Corrosive>N</Corrosive><DateSensitive>N</DateSensitive><DSD>N</DSD><EcoEasy>N</EcoEasy><EcoLogo>N</EcoLogo><EnergyStar>N</EnergyStar><EnvironmentalDesignFeatures>Y</EnvironmentalDesignFeatures><EnvironmentHazardous>N</EnvironmentHazardous><EPADesign>N</EPADesign><FDA>N</FDA><FlammableSolid>N</FlammableSolid><ForestStewardshipCouncil>N</ForestStewardshipCouncil><Formaldehyde>N</Formaldehyde><FormValidate>N</FormValidate><GreenSeal>N</GreenSeal><Hazardous>N</Hazardous><HighRisk>N</HighRisk><Liquid>N</Liquid><Liquid16Ounce>N</Liquid16Ounce><MarinePollutant>N</MarinePollutant><MasterShipper>N</MasterShipper><MSDSRequired>N</MSDSRequired><NonInventory>OH</NonInventory><PriceSensitive>N</PriceSensitive><PrintFlag>N</PrintFlag><Rebuy>N</Rebuy><Recycled>N</Recycled><Refillable>N</Refillable><RemanufacturedRefurbished>N</RemanufacturedRefurbished><StateRestricted>N</StateRestricted><SustainableForestInitiative>N</SustainableForestInitiative><XItem>0</XItem></Indicator></IndicatorList><InventoryGroup><Code>1</Code></InventoryGroup><ItemIDList><ItemID><Type>Quill</Type></ItemID></ItemIDList><ItemList><Manufacturer><Nmb>1007252</Nmb><Type>COR</Type></Manufacturer></ItemList><MSDS><URL Type=\"MSDSDoc\"/><IssueDate><Date>NA</Date></IssueDate><ProperShipName>NA</ProperShipName><TechnicalName>NA</TechnicalName></MSDS><Nmb>1007252</Nmb><PartyList><Party><VendorList><Primary><Name>DCS - COLUMBIA, MD (2764)</Name><Nmb>72371</Nmb><Type>COR</Type></Primary></VendorList></Party></PartyList><PriceList><Price><Additional><Price><High>0</High><Low>0</Low></Price></Additional><List><Type>1</Type></List><ManufacturerList><Amount Amount=\"2\"/></ManufacturerList><Retail><Amount Amount=\"6\"/></Retail></Price></PriceList><QuantityList><Quantity><Purchase><UnitOfMeasure>PD</UnitOfMeasure></Purchase><Selling><UnitOfMeasure>EA</UnitOfMeasure><UnitOfMeasureQuantity>1</UnitOfMeasureQuantity><UnitOfMeasureNAD>EA</UnitOfMeasureNAD></Selling></Quantity></QuantityList><ReferenceList><Reference><Name>Troy Forget</Name><Nmb>132</Nmb><Type>ContractProductManager</Type></Reference><Reference><Name>Unassigned</Name><Nmb>999</Nmb><Type>ContractProductSpecialist</Type></Reference><Reference><Name>Bonnie McInnis</Name><Nmb>253</Nmb><Type>Staples.comProductManager</Type></Reference><Reference><Name>Jeremy Hutton</Name><Nmb>732</Nmb><Type>Staples.comProductSpecialist</Type></Reference><Reference><Name>Bonnie McInnis</Name><Nmb>253</Nmb><Type>RetailProductManager</Type></Reference><Reference><Name>Joshua Frank</Name><Nmb>734</Nmb><Type>RetailProductSpecialist</Type></Reference><Reference><Type>70</Type></Reference><Reference><Nmb>70</Nmb><Type>RetailInventoryAnalyst</Type></Reference><Reference><Type>70</Type></Reference><Reference><Type>70</Type></Reference><Reference><Nmb>134</Nmb><Type>NADInventoryAnalyst</Type></Reference></ReferenceList><ServicePlanList><ServicePlan><Code>0</Code></ServicePlan></ServicePlanList><StatusList><Status><Retail><Code>I</Code></Retail><DotCom><Code>I</Code></DotCom><Contract><Code>I</Code></Contract><Quill><Code>I</Code></Quill></Status></StatusList><ThresholdList><Threshold><PostConsumerContent>0</PostConsumerContent><TotalRecyledContent>0</TotalRecyledContent></Threshold></ThresholdList><TransactionList><Transaction><DatetimeList><Start><DateTime>2015-08-03-15.13.42.000639</DateTime></Start></DatetimeList><Id>PIM00200260004ff1ae65afd0dba69</Id><MessageList><Message><Id>PIME004</Id><Type>Service</Type></Message></MessageList><Text>PIME004</Text><Type>New Item</Type></Transaction></TransactionList><UPCList><UPC><Name>SellingUnit</Name><Nmb>N/A</Nmb><Type>A</Type></UPC><UPC><Name>InnerPack</Name><Nmb>N/A</Nmb></UPC><UPC><Name>MasterCasePack</Name><Nmb>N/A</Nmb></UPC><UPC><Name>Pallet</Name><Nmb>N/A</Nmb></UPC></UPCList></item:Item></item:PublishItem>";
			// String testUpdMsg =
			// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><item:PublishItem Action=\"ItemUpdate\" Version=\"3.0\" xmlns:item=\"http://schemas.staples.com/esb/services/PublishItemV3_0\"><item:Item State=\"AfterImage\" Type=\"Service\"><TransactionList><Transaction><Id>PIM00200002402ffff2793d76c4106</Id><MessageList><Message><Id>PIME005</Id><Type>Service</Type></Message></MessageList><Type>Update Item</Type><DatetimeList><Start><DateTime>2013-02-01-02.44.27.000572</DateTime></Start></DatetimeList><Text>PIME005</Text></Transaction></TransactionList><ID>200002402</ID><Nmb>119291</Nmb><RestrictedStateList/><CodeList><Code><Disposition><Code>W02</Code><Type>Warehouse</Type></Disposition><Disposition><Code>N02</Code><Type>NAD</Type></Disposition><Disposition><Code>S01</Code><Type>Store</Type></Disposition><Hierarchy><Division><Code>1</Code><Name><![CDATA[1 Furniture]]></Name></Division><Department><Code>17</Code><Name><![CDATA[17 Chair Mats]]></Name></Department><Class><Code>170</Code><Name><![CDATA[170 Chair Mats]]></Name></Class></Hierarchy><OriginCountry><Code>US</Code><Id>1</Id></OriginCountry><OriginCountry><Code/><Id>2</Id></OriginCountry><OriginCountry><Code/><Id>3</Id></OriginCountry><OriginCountry><Code/><Id>4</Id></OriginCountry><OriginCountry><Code/><Id>5</Id></OriginCountry><Planogram><Id>400</Id></Planogram><Proprietary><Code/></Proprietary><SubClass><Code>170</Code></SubClass><Stock><Code/></Stock></Code></CodeList><CostList><Cost><Consignment><Code>N</Code></Consignment><PO><Cost><Amount Amount=\"1\"><Type>COR</Type></Amount></Cost></PO></Cost></CostList><DescriptionList><Description><Additional><Description><Description>item update2</Description><Type>COR</Type></Description></Additional><CashRegister/><Short/></Description></DescriptionList><DimensionList><Dimension><Case><Height>1</Height><Length>1</Length><Weight>1</Weight><Width>1</Width></Case><SUCP><Type>COR</Type><Quantity>2</Quantity></SUCP><Unit><Length>1</Length><Width>1</Width><Height>1</Height><Weight>1</Weight></Unit><WHSP><Quantity>2</Quantity></WHSP></Dimension></DimensionList><IndicatorList><Indicator><DSD>N</DSD><FormValidate>N</FormValidate><MasterShipper>N</MasterShipper><NonInventory>Y</NonInventory><PriceSensitive>N</PriceSensitive><DateSensitive>N</DateSensitive><DropShip/><Hazardous>N</Hazardous><Imprinted/><MSDSRequired>N</MSDSRequired><Recycled>N</Recycled><StateRestricted>N</StateRestricted><HighRisk>N</HighRisk><XItem>0</XItem><ContractXItem>N</ContractXItem></Indicator></IndicatorList><ItemList><Manufacturer><Nmb>119291</Nmb><Type>COR</Type></Manufacturer></ItemList><MSDS><URL Type=\"MSDSDoc\"/><DocumentName/></MSDS><PartyList><Party><VendorList><Primary><Nmb>41</Nmb><Name>TRIGEM AMERICA CORP</Name><Type>COR</Type></Primary></VendorList></Party></PartyList><PriceList><Price><List><Type>1</Type></List><ManufacturerList><Amount Amount=\"4\"><Type>COR</Type></Amount></ManufacturerList><Retail><Amount Amount=\"5\"><Type>COR</Type></Amount></Retail></Price></PriceList><QuantityList><Quantity><Purchase><UnitOfMeasure>EA</UnitOfMeasure></Purchase><Selling><UnitOfMeasure>EA</UnitOfMeasure><UnitOfMeasureQuantity>1</UnitOfMeasureQuantity></Selling></Quantity></QuantityList><ReferenceList><Reference><Nmb>4</Nmb><Name>Erin Quaratino</Name><Type>ContractProductManager</Type></Reference><Reference><Nmb>533</Nmb><Name>Rachel Kourey</Name><Type>ContractProductSpecialist</Type></Reference><Reference><Nmb>223</Nmb><Name>Carol Rees</Name><Type>Staples.comProductManager</Type></Reference><Reference><Nmb>576</Nmb><Name>Chris Cashman</Name><Type>Staples.comProductSpecialist</Type></Reference><Reference><Nmb>44</Nmb><Name>Melissa Yanagi</Name><Type>RetailProductManager</Type></Reference><Reference><Nmb>506</Nmb><Name>Nicole Mikelson</Name><Type>RetailProductSpecialist</Type></Reference><Reference><Nmb/><Name/><Type>NADInventoryAnalyst</Type></Reference><Reference><Nmb>70</Nmb><Name/><Type>RetailInventoryAnalyst</Type></Reference></ReferenceList><StatusList><Status><Contract><Code>I</Code></Contract><DotCom><Code>I</Code></DotCom><Quill><Code>I</Code></Quill><Retail><Code>A</Code></Retail></Status></StatusList><ThresholdList><Threshold><PostConsumerContent>0</PostConsumerContent></Threshold></ThresholdList><UPCList><UPC><Name>SellingUnit</Name><Nmb>N/A</Nmb><Type>A</Type></UPC><UPC><Name>InnerPack</Name><Nmb>N/A</Nmb></UPC><UPC><Name>MasterCasePack</Name><Nmb>N/A</Nmb></UPC><UPC><Name>Pallet</Name><Nmb>N/A</Nmb></UPC></UPCList><ItemIDList><ItemID><Nmb/><Type>Quill</Type></ItemID></ItemIDList><InventoryGroup><Code>1</Code></InventoryGroup></item:Item><BeforeImage><PIMCoreServiceCatalogSpec><SKU><![CDATA[119291]]></SKU><Service_ID>200002402</Service_ID><Item_Description><![CDATA[item update2]]></Item_Description><Class_Code>170</Class_Code><Model_Number><![CDATA[119291]]></Model_Number><Non_Inventory><![CDATA[Y]]></Non_Inventory><Retail_Price>5.0</Retail_Price><Planogram_ID><![CDATA[400]]></Planogram_ID><Supplier_Information><Vendor_Number>41</Vendor_Number><PO_Cost>1.0</PO_Cost><List_Price>4.0</List_Price><List_Price_Type><![CDATA[1]]></List_Price_Type></Supplier_Information><Selling_Unit><Selling_Unit_Length>1.0</Selling_Unit_Length><Selling_Unit_Width>1.0</Selling_Unit_Width><Selling_Unit_Height>1.0</Selling_Unit_Height><Selling_Unit_Weight>1.0</Selling_Unit_Weight></Selling_Unit><Master_Case_Pack><Master_Case_Pack_Length>1.0</Master_Case_Pack_Length><Master_Case_Pack_Width>1.0</Master_Case_Pack_Width><Master_Case_Pack_Height>1.0</Master_Case_Pack_Height><Master_Case_Pack_Weight>1.0</Master_Case_Pack_Weight></Master_Case_Pack><Basic_Information><Sell_Unit_Of_Measure><![CDATA[EA]]></Sell_Unit_Of_Measure><Sell_Quantity>1</Sell_Quantity></Basic_Information><Supply_Chain_Info><Purchase_Unit_Of_Measure><![CDATA[EA]]></Purchase_Unit_Of_Measure><Warehouse_Ship_Pack_Units_Or_WHSP>2</Warehouse_Ship_Pack_Units_Or_WHSP><Master_Case_Pack_Units_Or_SUCP>2</Master_Case_Pack_Units_Or_SUCP></Supply_Chain_Info><Item_Reference><Ret_Inv_Num>70</Ret_Inv_Num></Item_Reference><Merchandising_Status><Retail_Merchandising_Status><![CDATA[A]]></Retail_Merchandising_Status><Dot_Com_Merchandising_Status><![CDATA[I]]></Dot_Com_Merchandising_Status><Contract_Merchandising_Status><![CDATA[I]]></Contract_Merchandising_Status><Quill_Merchandising_Status><![CDATA[I]]></Quill_Merchandising_Status></Merchandising_Status><Audit><Comments><![CDATA[bulk update]]></Comments><Create_Date>20130131</Create_Date><Close_Date>20130131</Close_Date><All_Approval_Completed_Date><![CDATA[01/31/13]]></All_Approval_Completed_Date><All_Approval_Completed_Time><![CDATA[02:59:25]]></All_Approval_Completed_Time><Created_By><![CDATA[oindril_esp]]></Created_By><Last_Update_Date><![CDATA[01/31/13]]></Last_Update_Date><Last_Updated_By><![CDATA[oindril_esp]]></Last_Updated_By><Last_Updated_By_Workflow><![CDATA[PIMCore Item Update Workflow For Service]]></Last_Updated_By_Workflow><Trace_ID><![CDATA[PIM-ECE2EE96-B84D-4BB8-A635-2F7E980A9304]]></Trace_ID><Request_Type><![CDATA[Update Item]]></Request_Type></Audit><Inventory_Group>1.0</Inventory_Group><Sell_Unit_UPC><![CDATA[N/A]]></Sell_Unit_UPC><Sub_Class>170</Sub_Class><Price_Sensitive><![CDATA[N]]></Price_Sensitive><Date_Sensitive_Flag><![CDATA[N]]></Date_Sensitive_Flag><Global_Or_Shipping><Country_Code_1><![CDATA[US]]></Country_Code_1></Global_Or_Shipping><Disposition_Codes><Store_Disposition><![CDATA[S01]]></Store_Disposition><DC_Disposition><![CDATA[W02]]></DC_Disposition><NAD_Disposition><![CDATA[N02]]></NAD_Disposition></Disposition_Codes><Consignment_Info><Is_Consignment><![CDATA[N]]></Is_Consignment></Consignment_Info><UPC_Information><UPC_Type><![CDATA[A]]></UPC_Type><Master_Case_Pack_UPC><![CDATA[N/A]]></Master_Case_Pack_UPC><Inner_Pack_UPC><![CDATA[N/A]]></Inner_Pack_UPC><Pallet_UPC><![CDATA[N/A]]></Pallet_UPC></UPC_Information><Pricing_Info><Contract_X_Item_Percent>0.0</Contract_X_Item_Percent><Contract_X_Item_Flag><![CDATA[N]]></Contract_X_Item_Flag></Pricing_Info><Misc_SKU_Info><Master_Shipper><![CDATA[N]]></Master_Shipper><DSD><![CDATA[N]]></DSD><Form_Validation><![CDATA[N]]></Form_Validation><High_Risk><![CDATA[N]]></High_Risk></Misc_SKU_Info><Recycled_Content><Is_Content><![CDATA[N]]></Is_Content><Post_Consumer_Percentage>0</Post_Consumer_Percentage></Recycled_Content><Hazmat><Is_Hazmat><![CDATA[N]]></Is_Hazmat><Is_MSDS_REQ><![CDATA[N]]></Is_MSDS_REQ></Hazmat><Regulatory_Compliance><Is_State_Restricted><![CDATA[N]]></Is_State_Restricted></Regulatory_Compliance></PIMCoreServiceCatalogSpec><CategoryInformation><MerchandisingHierarchy><Division_Code>1</Division_Code><Department_Code>17</Department_Code><Class_Code>170</Class_Code></MerchandisingHierarchy><ServiceItemTypeHierarchy><Service_Type>ESP / Installation and Warranty</Service_Type></ServiceItemTypeHierarchy></CategoryInformation></BeforeImage></item:PublishItem>";

			StepInboundProcessor stepInboundProcessor = new StepInboundProcessor();
			stepInboundProcessor.objectFactory = new ObjectFactory();
//			/*
//			 * //1. Loading mapping xml stepInboundProcessor.loadMappingXML();
//			 * //2. Parse PIMcore.xml Map<String, String> pimCoreMap =
//			 * stepInboundProcessor.getPIMCoreXMLXPathAndValue(testMessage);
//			 * //3. Get the Pimcore xpath from pimcore.xml(pimMap) and get the
//			 * Step attribute Id from mapping.xml (mappingMap) //as Map<String,
//			 * String>(attributeId, attributeValue) Map<String, String>
//			 * attributeMap =
//			 * stepInboundProcessor.getStepAttributeValueFromPIMCore
//			 * (pimCoreMap);
//			 * 
//			 * attributeMap = transformationProcess(attributeMap);
//			 * CommonUtil.printConsole(""+attributeMap); STEPProductInformation
//			 * stepProductInformation =
//			 * stepInboundProcessor.updatedStepProdInformation
//			 * (attributeMap,ITEMCREATE); //5. Generate the step.xml using
//			 * marsheller
//			 * 
//			 * String stepXml =
//			 * stepInboundProcessor.generateStepXML(stepProductInformation);
//			 */

			// CommonUtil.printConsole("stepXml:"+stepXml);
			test2=test2.replaceAll("&", "&amp;");
			stepInboundProcessor.stepInboundIntgProcess(test2,"ItemCreate");
			System.out.println(test2);
			// Map<String, String> pimCoreMap =
			// stepInboundProcessor.getPIMCoreXMLXPathAndValue(testMessage);
			// System.out.println(pimCoreMap);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}