
package com.staples.pim.delegate.wayfair.productupdate;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER_WAYFAIR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ITEM_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_COMPONENT_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ECO_SYSTEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ICD_CLASSNAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WAYFAIR_PRODUCT_OUTPUT_FOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pcm.stepcontract.beans.ClassificationReferenceType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.ProductCrossReferenceType;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.productupdate.processor.ProductInboundTransformation;
import com.staples.pim.delegate.productupdate.processor.ProductInboundValidation;
import com.staples.pim.delegate.wayfair.productupdate.runner.WayfairProductScheduler;

public class WayfairProductProcessor {

	public static final String					WAYFAIR_PRODUCT_INSTANCE_NO				= "11";

	private final String						ITEM_TO_VENDOR							= "ItemToVendor";

	private final String						SUPPLIER_LINK							= "SupplierLink";

	private final String						ITEM_PRODUCT_PARENTID_WAYFAIR			= "WayFairItems";

	private final String						SKU_PRODUCT_PARENTID_WAYFAIR			= "WayFairSKUs";

	private final String						SKU_PRODUCT_ID_PREFIX_WAYFAIR			= "DMSKU-";

	private final String						ITEM_PRODUCT_ID_PREFIX_WAYFAIR			= "DMItem-";

	private final String						VENDORITEM_PRODUCT_PARENTID_WAYFAIR		= "VendorPackagingHierarchy";

	public static final String					VENDORITEM_PRODUCT_ID_PREFIX_WAYFAIR	= "DMVendorItem-";

	private final String						VENDOR_ITEM								= "VendorItem";

	private final String						PRIMARY_ITEM							= "PrimaryItem";

	private final String						WEBSITE_LINK							= "WebsiteLink";

	private final String						QUILWEBSITE_LINK						= "QuillWebsiteLink";

	private static final String					CLASS_ID_REFERENCE_FILE_CURRENT_PATH	= IntgSrvUtils
																								.reformatFilePath(IntgSrvPropertiesReader
																										.getProperty(DatamigrationAppConstants.CLASS_ID_REFERENCE_FILE_CURRENT_PATH));

	private static final String					CLASS_ID_REFERENCE_FILE_OLD_PATH		= IntgSrvUtils
																								.reformatFilePath(IntgSrvPropertiesReader
																										.getProperty(DatamigrationAppConstants.CLASS_ID_REFERENCE_FILE_OLD_PATH));

	private static final String					TRACELOGGER_WAYFAIR_PRODUCT				= "tracelogger.wayfairproduct";

	ObjectFactory								objectFactory							= new ObjectFactory();
	ProductInboundTransformation				objInboundTransformation				= new ProductInboundTransformation();
	ProductInboundValidation					objProductInboundValidation				= new ProductInboundValidation();

	static IntgSrvLogger						logger									= IntgSrvLogger
																								.getInstance(TRACELOGGER_WAYFAIR_PRODUCT);
	public static IntgSrvLogger					ehfLogger								= IntgSrvLogger.getInstance(EHF_LOGGER_WAYFAIR);
	public static ErrorHandlingFrameworkICD		ehfICD									= null;
	public static ErrorHandlingFrameworkHandler	ehfHandler								= new ErrorHandlingFrameworkHandler();
	public static String						traceId									= null;

	public static final String					WAYFAIR_PRODUCT_DELIMITER				= "wayfair.product.delimiter";

	public static final String					WAYFAIR_PRODUCT_HEADERS					= "wayfair.product.header";

	public static final String					WAYFAIR_PRODUCT_FANIDS					= "wayfair.product.fanids";

	public static final String					WAYFAIR_PRODUCT_MAX_RECORDS_PER_FILE	= "wayfair.product.maxrecordsper.file";

	public static final String					headerString							= IntgSrvPropertiesReader
																								.getProperty(WAYFAIR_PRODUCT_HEADERS);

	public static final String					delimiter								= IntgSrvPropertiesReader
																								.getProperty(WAYFAIR_PRODUCT_DELIMITER);

	public static final String[]				headers									= headerString.split(delimiter, -1);

	public static final String					fanidString								= IntgSrvPropertiesReader
																								.getProperty(WAYFAIR_PRODUCT_FANIDS);

	public static final String[]				fanIDs									= fanidString.split(delimiter, -1);

	String										inDir									= IntgSrvUtils
																								.reformatFilePath(IntgSrvPropertiesReader
																										.getProperty(DatamigrationAppConstants.WAYFAIR_PRODUCT_INPUT_FOLDER));
	String										filebadDir								= IntgSrvUtils
																								.reformatFilePath(IntgSrvPropertiesReader
																										.getProperty(DatamigrationAppConstants.WAYFAIR_PRODUCT_BAD_FOLDER));

	// public static final String WAYFAIR_PRODUCT_WAYFAIR_FILEUNPROCESSED =
	// "/opt/stibo/integration/hotfolder/WayFairIncoming/Product/File_Unprocessed/";

	// public static final String WAYFAIR_PRODUCT_WAYFAIR_FILEDONE =
	// "/opt/stibo/integration/hotfolder/WayFairIncoming/Product/File_Done/";
	String										logMessage								= null;

	public static int							successCount;
	public static int							failureCount;
	
	public void wayfairProductInboundProcessor() {

		try {
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(SPRINGBATCH_ECO_SYSTEM, SPRINGBATCH_COMPONENT_ID,
					SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_PRODUCT);
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();
			File folder = new File(IntgSrvUtils.reformatFilePath(inDir));
			File[] files = DatamigrationCommonUtil.sortFilesBasedOnFIFO(folder, WayfairProductScheduler.PUBLISH_ID);
			if (files != null && files.length > 0) {

				DatamigrationCommonUtil.printConsole("Files length : " + files.length);
				for (int i = 0; i < files.length; i++) {
					logger.info("Processing File : " + files[i].getName());
					DatamigrationCommonUtil.printConsole("Product >>Processing File : " + files[i].getName());
					if (files[i].getName().endsWith(".xsv") || files[i].getName().endsWith(".dsv")) {
						readIncomingFile(files[i]);
					}
				}
			}
		} catch (ErrorHandlingFrameworkException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Exception " + e.getMessage(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.error(logMessage);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayfairProductScheduler.PUBLISH_ID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Exception " + e.getMessage(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.error(logMessage);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayfairProductScheduler.PUBLISH_ID);
		}
	}

	public void readIncomingFile(File file) {

		successCount = 0;
		failureCount = 0;
		long startTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
		
		FileReader fileReader;
		String tempString;
		int lineno = 0;
		int fileCount = 1;
		int maxRecordsPerFile = Integer.parseInt(IntgSrvPropertiesReader.getProperty(WAYFAIR_PRODUCT_MAX_RECORDS_PER_FILE));

		// String[] fanIDs=
		// {"A0547","A0013","A0123","A0018","A0126","A0178","A0613","A0316","A0488","A0122","A0234","A0243","A0244","D1137","A0548","A0549","A0550","A0551","A0210","A0211","A0552","A0212","A0218","A0553","A0346","A0554","A0237","A0238","A0239","A0240","A0080","A0220","A0308","A0310","A0307","A0309","A0067","A0068","A0069","A0070","A0071","A0082","A0046","A0313","A0311","A0312","A0314","A0315","A0085","A0086","A0087","A0088","A0089","A0323","A0324","A0090","A0091","A0092","A0093","A0555","A0094","A0095","A0120","A0097","A0144","A0146","A0098","A0145","A0147","A0099","A0100","A0101","A0104","A0103","A0102","A0105","A0107","A0108","A0113","A0115","A0116","A0117","A0111","A0114","A0106","A0112","A0109","A0110","A0118","A0119","A0096","A0121","A0128","A0130","A0134","A0135","A0136","A0124","A0127","A0137","A0138","A0139","A0140","A0141","A0142","A0143","A0300","A0148","A0150","A0151","A0149","A0152","A0154","A0155","A0156","A0157","A0158","A0159","A0160","A0161","A0162","A0163","A0164","A0165","A0166","A0167","A0168","A0169","A0339","A0171","A0172","A0173","A0174","A0175","D0050","D0051","D0052","D0053","D0086","A0560","A0561","A0562","A0563","A0564","A0565","D0014","D0228","A0210","D0015","D0019","D0012","D0020","D0021","D0022","D0023","D0024","D0025","D0026","D0027","D0028","D0029","D0030","D0031","D0032","D0033","D0034","D0035","D0036","D0037","D0038","D0039","A0566","D0173","D0174","D0175","A0538","A0539"};
		// String[] fanIDs=
		// {"A0547","A0484","A0013","A0123","A0018","A0126","A0178","A0613","A0316","A0488","A0122","A0234","A0243","A0244","D1137","A0548","A0549","A0550","A0551","A0210","A0211","A0552","A0212","A0218","A0553","A0346","A0554","A0237","A0238","A0239","A0240","A0080","A0220","A0308","A0310","A0307","A0309","A0067","A0068","A0069","A0070","A0071","A0082","A0046","A0313","A0311","A0312","A0314","A0315","A0085","A0086","A0087","A0088","A0089","A0323","A0324","A0090","A0091","A0092","A0093","A0555","A0094","A0095","A0120","A0097","A0144","A0146","A0098","A0145","A0147","A0099","A0100","A0101","A0104","A0103","A0102","A0105","A0107","A0108","A0113","A0115","A0116","A0117","A0111","A0114","A0106","A0112","A0109","A0110","A0118","A0119","A0096","A0121","A0128","A0130","A0134","A0135","A0136","A0124","A0127","A0137","A0138","A0139","A0140","A0141","A0142","A0143","A0300","A0148","A0150","A0151","A0149","A0152","A0154","A0155","A0156","A0157","A0158","A0159","A0160","A0161","A0162","A0163","A0164","A0165","A0166","A0167","A0168","A0169","A0339","A0171","A0172","A0173","A0174","A0175","D0050","D0051","D0052","D0053","D0086","A0560","A0561","A0562","A0563","A0564","A0565","D0014","D0228","A0210","D0015","D0019","D0012","D0020","D0021","D0022","D0023","D0024","D0025","D0026","D0027","D0028","D0029","D0030","D0031","D0032","D0033","D0034","D0035","D0036","D0037","D0038","D0039","A0566","D0173","D0174","D0175","A0538","A0539"};
		// fanidString.split(delimiter, -1);
		String[] values;
		Map<String, String> mapContainingRecord;
		List<Map<String, String>> allProductsList = new ArrayList<Map<String, String>>();
		try {
			fileReader = new FileReader(file.getPath());
			BufferedReader br = new BufferedReader(fileReader);

			while ((tempString = br.readLine()) != null) {
				if (!tempString.equals("")) {

					if (lineno == 0) {
					} else {
						values = tempString.split(delimiter, -1);

						if (headers.length == values.length) {
							mapContainingRecord = getRecordAsMap(fanIDs, values);

							// Transformation
							mapContainingRecord = WayfairProductTransformation.transformationForProduct(mapContainingRecord);
							successCount++;
							allProductsList.add(mapContainingRecord);
							// System.out.println("lineno:"+lineno+" : "+mapContainingRecord);

							if (allProductsList.size() == maxRecordsPerFile) {
								createAndTransferSTEPXML(allProductsList, file, fileCount, "", false);
								allProductsList = new ArrayList<Map<String, String>>();
								fileCount++;
							}
						} else {
							failureCount++;
							DatamigrationCommonUtil.appendWriterFile(file.getParentFile().getParentFile() + "/Report/Report_" + file.getName(), tempString
									+ "~Has incorrect number of Values in record :"+values.length);
							logger.error("No of values in the records does not match the no of values expected in header");
						}
					}
					lineno++;
				}
			}
			br.close();
			if (allProductsList.size() > 0) {
				createAndTransferSTEPXML(allProductsList, file, fileCount, "", true);
			}
		
			long endTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
			DatamigrationCommonUtil.generateExecutionSummary("Wayfair Product Processor ", DatamigrationCommonUtil.getReportFolderPath(file), file.getName(), successCount,
									(endTime - startTime), failureCount);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			IntgSrvUtils.alertByEmail(e1, DatamigrationCommonUtil.getClassName(), WayfairProductScheduler.PUBLISH_ID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayfairProductScheduler.PUBLISH_ID);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<String, String> getRecordAsMap(String[] headers, String[] values) {

		if (headers.length != values.length) {
			DatamigrationCommonUtil.printConsole("No of values in headers do not match the no of values in the record");
		}

		Map<String, String> mapContainingRecord = new HashMap<String, String>();

		for (int i = 0; i < headers.length; i++) {
			mapContainingRecord.put(headers[i], values[i]);
		}

		return mapContainingRecord;
	}

	/**
	 * This function is used to construct the STEPProductInformation object used
	 * for marshaling.
	 * 
	 * @param stepAttributesList
	 *            :A list of map objects is passed. Each map contains fan ids as
	 *            keys and corresponding attribute values as values.Each map
	 *            corresponds to a record(item).
	 * 
	 * @param transType
	 * @return
	 */

	public void createAndTransferSTEPXML(List<Map<String, String>> stepAttributesList, File file, int fileCount, String transType,
			boolean isFileMoveActivated) {

		// creating step xml object
		logger.info("creating Step xml containing " + stepAttributesList.size() + " records");
		STEPProductInformation stepprdinfo = updatedStepProdInformation(stepAttributesList, "null");

		// create file name based on file count
		String inputFileName = file.getName();
		if (fileCount > 1) {
			inputFileName = inputFileName.substring(0, (inputFileName.length() - 4));
			inputFileName = inputFileName + "-" + fileCount + ".xml";

		}

		// marshalling step xml
		File fileName = new File(inputFileName);
		logger.info("Marshalling step xml");
		File outputFile = DatamigrationCommonUtil.marshallObject(stepprdinfo, fileName, WAYFAIR_PRODUCT_OUTPUT_FOLDER,
				WayfairProductScheduler.PUBLISH_ID);
		logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
				EHF_MSGTYPE_INFO_NONSLA, "STEP xml generated " + outputFile.getName(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
				DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.error(logMessage);
		// sending Step xml to sftp location
		logger.info("sending step xml to sftp location");
		DatamigrationCommonUtil.sendFile(outputFile, file, IntgSrvUtils
				.reformatFilePath(IntgSrvPropertiesReader
						.getProperty(DatamigrationAppConstants.WAYFAIR_PRODUCT_DONE_FOLDER)),
				WAYFAIR_PRODUCT_INSTANCE_NO, isFileMoveActivated, WayfairProductScheduler.PUBLISH_ID);
		logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
				EHF_MSGTYPE_INFO_NONSLA, "STEP xml moved to SFTP location", EHF_SPRINGBATCH_ITEMUTILITY_USER,
				DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.error(logMessage);
	}

	public STEPProductInformation updatedStepProdInformation(List<Map<String, String>> stepAttributesList, String transType) {

		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = null;
		ProductsType products = null;
		ProductType itemProduct = null;
		ProductType vendorProduct = null;
		// ProductType product = null;
		ProductType skuProduct = null;
		String supplierID = null;

		stepProductInformation = objectFactory.createSTEPProductInformation();
		products = objectFactory.createProductsType();

		ClassificationReferenceType classRefMerch = objectFactory.createClassificationReferenceType();
		ClassificationReferenceType classRefSupplier = objectFactory.createClassificationReferenceType();
		ProductCrossReferenceType prodCrossRefItem = objectFactory.createProductCrossReferenceType();
		ProductCrossReferenceType prodCrossRefSKU = objectFactory.createProductCrossReferenceType();

		Map<String, String> referenceMap = getClassIDsFromReferenceFile();

		for (int i = 0; i < stepAttributesList.size(); i++) {
			ValueType valueProduct = null;
			ValueType valueVendor = null;
			ValueType valueSKU = null;

			ValuesType valuesProduct = null;
			ValuesType valuesVendor = null;
			ValuesType valuesSKU = null;

			valuesProduct = objectFactory.createValuesType();
			valuesVendor = objectFactory.createValuesType();
			valuesSKU = objectFactory.createValuesType();
			prodCrossRefItem = objectFactory.createProductCrossReferenceType();
			prodCrossRefSKU = objectFactory.createProductCrossReferenceType();
			classRefMerch = objectFactory.createClassificationReferenceType();
			classRefSupplier = objectFactory.createClassificationReferenceType();

			Map<String, String> attributeMap = stepAttributesList.get(i);

			String skuId = attributeMap.get("A0547");

			/*
			 * String itemName = attributeMap.get("A0547"); String isNonStocked
			 * =
			 * DatamigrationCommonUtil.converYESNOIntoChar(attributeMap.get("A0015"
			 * ));
			 */
			// product = objectFactory.createProductType();
			itemProduct = objectFactory.createProductType();
			vendorProduct = objectFactory.createProductType();
			skuProduct = objectFactory.createProductType();

			for (Entry<String, String> mapObj : attributeMap.entrySet()) {

				valueVendor = objectFactory.createValueType();
				valueSKU = objectFactory.createValueType();
				valueProduct = objectFactory.createValueType();
				if (mapObj.getKey().equalsIgnoreCase("A0484")) {
					continue;
				} else if (mapObj.getKey().equalsIgnoreCase("D0053") && !IntgSrvUtils.isNullOrEmpty(mapObj.getValue())) {

					String lookupId = attributeMap.get("D0050") + "~" + attributeMap.get("D0051") + "~" + attributeMap.get("D0052") + "~"
							+ attributeMap.get("D0053");
					// System.out.println(".Com lookupId: "+lookupId);
					String classRefId = getValueFromReferenceMap(referenceMap, lookupId);
					// System.out.println(".Com classRefId: "+classRefId);
					if (!IntgSrvUtils.isNullOrEmpty(classRefId)) {

						classRefMerch.setType(WEBSITE_LINK);
						classRefMerch.setClassificationID("StaplesDotComClass-" + classRefId);
						skuProduct.getClassificationReference().add(classRefMerch);
					}

					/*
					 * valueProduct.setAttributeID("ClassCode");
					 * valueProduct.setContent(classRefId);
					 * valuesProduct.getValueOrMultiValueOrValueGroup
					 * ().add(valueProduct);
					 */

				} else if (mapObj.getKey().equalsIgnoreCase("A0563") && !IntgSrvUtils.isNullOrEmpty(mapObj.getValue())) {

					String lookupId = attributeMap.get("A0560") + "~" + attributeMap.get("A0561") + "~" + attributeMap.get("A0562") + "~"
							+ attributeMap.get("A0563");
					// System.out.println("Quill lookupId: "+lookupId);
					String classRefId = getValueFromReferenceMap(referenceMap, lookupId);
					// System.out.println("Quill classRefId: "+classRefId);
					if (!IntgSrvUtils.isNullOrEmpty(classRefId)) {

						classRefMerch.setType(QUILWEBSITE_LINK);
						classRefMerch.setClassificationID("QULWebClass-" + classRefId);
						skuProduct.getClassificationReference().add(classRefMerch);
					}
				} else if (mapObj.getKey().equalsIgnoreCase("A0082")) {
					valueVendor.setAttributeID("A0082");
					valueVendor.setContent(mapObj.getValue());
					valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0080")) {
					valueVendor.setAttributeID("A0080");
					valueVendor.setContent(mapObj.getValue());
					valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0084")) {
					valueVendor.setAttributeID("A0084");
					valueVendor.setContent(mapObj.getValue());
					valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0083")) {
					valueVendor.setAttributeID("A0083");
					valueVendor.setContent(mapObj.getValue());
					valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0013_RET")) {
					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0013_NAD")) {

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0075_RET")) {
					valueVendor.setAttributeID("A0075_RET");
					valueVendor.setContent(mapObj.getValue());
					valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0075_NAD")) {

					valueVendor.setAttributeID("A0075_NAD");
					valueVendor.setContent(mapObj.getValue());
					valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0012")) {
					valueSKU.setAttributeID(mapObj.getKey());
					valueSKU.setContent(mapObj.getValue());
					valuesSKU.getValueOrMultiValueOrValueGroup().add(valueSKU);
				} else if (mapObj.getKey().equalsIgnoreCase("A0019")) {
					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0504")) {

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} // A0565
				else if (mapObj.getKey().equalsIgnoreCase("A0565")) {
					valueSKU.setAttributeID(mapObj.getKey());
					valueSKU.setContent(mapObj.getValue());
					valuesSKU.getValueOrMultiValueOrValueGroup().add(valueSKU);
				} else if (mapObj.getKey().equalsIgnoreCase("D0015")) {
					valueSKU.setAttributeID(mapObj.getKey());
					valueSKU.setContent(mapObj.getValue());
					valuesSKU.getValueOrMultiValueOrValueGroup().add(valueSKU);
				} else if (mapObj.getKey().equalsIgnoreCase("D0014")) {
					valueSKU.setAttributeID(mapObj.getKey());
					valueSKU.setContent(mapObj.getValue());
					valuesSKU.getValueOrMultiValueOrValueGroup().add(valueSKU);
				} else if (mapObj.getKey().equalsIgnoreCase("A0302")) {

					valueVendor.setAttributeID(mapObj.getKey());
					valueVendor.setContent(mapObj.getValue());
					valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);
				} else if (mapObj.getKey().equalsIgnoreCase("D0086")) {
					valueSKU.setAttributeID(mapObj.getKey());
					valueSKU.setContent(mapObj.getValue());
					valuesSKU.getValueOrMultiValueOrValueGroup().add(valueSKU);
				} 
				else if (mapObj.getKey().equalsIgnoreCase("D0173")) {
					valueSKU.setAttributeID(mapObj.getKey());
					valueSKU.setContent(mapObj.getValue());
					valuesSKU.getValueOrMultiValueOrValueGroup().add(valueSKU);
				} 
				else if (mapObj.getKey().equalsIgnoreCase("D0174")) {
					valueSKU.setAttributeID(mapObj.getKey());
					valueSKU.setContent(mapObj.getValue());
					valuesSKU.getValueOrMultiValueOrValueGroup().add(valueSKU);
				} 
				else if (mapObj.getKey().equalsIgnoreCase("D0175")) {
					valueSKU.setAttributeID(mapObj.getKey());
					valueSKU.setContent(mapObj.getValue());
					valuesSKU.getValueOrMultiValueOrValueGroup().add(valueSKU);
				} 
				else {
					// Error message condition added for re-load the error
					// report after fixing the data issues
					if (!IntgSrvUtils.isNullOrEmpty(mapObj.getValue()) && !"ErrorMessage".equalsIgnoreCase(mapObj.getValue())) {
						valueProduct.setAttributeID(mapObj.getKey());
						valueProduct.setContent(mapObj.getValue());
						valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
					}
				}

			}

			prodCrossRefItem.setProductID(VENDORITEM_PRODUCT_ID_PREFIX_WAYFAIR + attributeMap.get("A0075_RET") + "-" + skuId);
			prodCrossRefItem.setType(VENDOR_ITEM);

			prodCrossRefSKU.setProductID(ITEM_PRODUCT_ID_PREFIX_WAYFAIR + skuId);
			prodCrossRefSKU.setType(PRIMARY_ITEM);
			supplierID = attributeMap.get("A0075_RET") + "Products";

			if (!IntgSrvUtils.isNullOrEmpty(supplierID)) {
				classRefSupplier.setClassificationID(supplierID);
				classRefSupplier.setType(SUPPLIER_LINK);
				itemProduct.getClassificationReference().add(classRefSupplier);
			}

			/*
			 * valueProduct = objectFactory.createValueType();
			 * valueProduct.setAttributeID(A1363_STR);
			 * valueProduct.setContent(attributeMap.get(A0012_STR));
			 * valuesProduct
			 * .getValueOrMultiValueOrValueGroup().add(valueProduct);
			 */

			itemProduct.setUserTypeID(ITEM_STR);// Hard coded value
			itemProduct.setID(ITEM_PRODUCT_ID_PREFIX_WAYFAIR + skuId);

			itemProduct.setParentID(ITEM_PRODUCT_PARENTID_WAYFAIR);
			itemProduct.setSelected(Boolean.FALSE);
			itemProduct.setReferenced(Boolean.TRUE);

			itemProduct.getProductOrSequenceProductOrSuppressedProductCrossReference().add(valuesProduct);

			itemProduct.getProductOrSequenceProductOrSuppressedProductCrossReference().add(prodCrossRefItem);

			skuProduct.setUserTypeID(DatamigrationAppConstants.SKU_STR);
			skuProduct.setID(SKU_PRODUCT_ID_PREFIX_WAYFAIR + skuId);

			skuProduct.setParentID(SKU_PRODUCT_PARENTID_WAYFAIR);

			skuProduct.getProductOrSequenceProductOrSuppressedProductCrossReference().add(valuesSKU);
			skuProduct.getProductOrSequenceProductOrSuppressedProductCrossReference().add(prodCrossRefSKU);

			vendorProduct.setUserTypeID(ITEM_TO_VENDOR);
			// vendorProduct.getName().add(vendornNme);
			vendorProduct.setID(VENDORITEM_PRODUCT_ID_PREFIX_WAYFAIR + attributeMap.get("A0075_RET") + "-" + skuId);
			vendorProduct.setParentID(VENDORITEM_PRODUCT_PARENTID_WAYFAIR);
			vendorProduct.setSelected(Boolean.FALSE);
			vendorProduct.setReferenced(Boolean.TRUE);
			vendorProduct.getClassificationReference().add(classRefSupplier);
			vendorProduct.getProductOrSequenceProductOrSuppressedProductCrossReference().add(valuesVendor);
			products.getProduct().add(vendorProduct);

			products.getProduct().add(itemProduct);

			products.getProduct().add(skuProduct);

			attributeMap = null;
		}
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP());
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
																// value
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
																		// coded
																		// value
		stepProductInformation.setUseContextLocale(false);// Hard coded value
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded
																	// value

		// stepProductInformation.setAutoInitiate(YesNoType.N);
		stepProductInformation.setProducts(products);
		// DatamigrationCommonUtil.printConsole("/nTotal no of products::::::::::"+stepProductInformation.getProducts().getProduct().size());
		logger.info("Total no of products::::::::::" + stepProductInformation.getProducts().getProduct().size());
		return stepProductInformation;
	}

	public static void main(String[] args) {

		new WayfairProductProcessor().wayfairProductInboundProcessor();

	}

	public Map<String, String> getClassIDsFromReferenceFile() {

		String delimiter = "<s-k-r>";
		Map<String, String> attributeIDsMap = new HashMap<String, String>();
		File referenceFile = DatamigrationCommonUtil.getReferenceFile(CLASS_ID_REFERENCE_FILE_CURRENT_PATH,
				CLASS_ID_REFERENCE_FILE_OLD_PATH);

		FileReader fileReader;
		BufferedReader reader;
		String tempString;
		String[] values;
		if (referenceFile != null) {
			try {
				fileReader = new FileReader(referenceFile);
				reader = new BufferedReader(fileReader);

				while ((tempString = reader.readLine()) != null) {
					if (!tempString.equals(DatamigrationAppConstants.EMPTY_STR)) {
						values = tempString.split(delimiter, -1);
						attributeIDsMap.put(values[0], values[1]);
					}
				}

				reader.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				logger.error("Exception while getting reference File : " + e.getMessage());
				IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayfairProductScheduler.PUBLISH_ID);
			} catch (IOException e) {
				logger.error("Exception while getting reference File : " + e.getMessage());
				e.printStackTrace();
				IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayfairProductScheduler.PUBLISH_ID);
			}

		} else {
			logger.error("Reference File to Attribute IDs from attributemetadata Feed not found.");
		}

		return attributeIDsMap;
	}

	public String getValueFromReferenceMap(Map<String, String> referenceMap, String key) {

		for (String AttributeNames : referenceMap.keySet()) {
			if (key.equalsIgnoreCase(AttributeNames)) {
				return referenceMap.get(key);
			}
		}
		logger.error("Attribute ID not found in the reference file");
		return "";
	}

}
