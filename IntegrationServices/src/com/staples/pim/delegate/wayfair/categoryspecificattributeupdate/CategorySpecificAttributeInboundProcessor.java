
package com.staples.pim.delegate.wayfair.categoryspecificattributeupdate;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EMPTY_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.REFERENCE_FILE_DELIMITER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_COMPONENT_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ECO_SYSTEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ICD_CLASSNAME;
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

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
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
import com.staples.pim.delegate.wayfair.categoryspecificattributeupdate.runner.CategorySpecificAttributeScheduler;

public class CategorySpecificAttributeInboundProcessor {

	public static final String					XML_EXTENSION										= ".xml";

	public static final String					UNDERSCORE											= "_";

	public static final String					CATGEORY_SPEC_ATTRIBUTES_INSTANCE_NO				= "11_CATEGORYSPEC_ATTRIBUTES";

	public static final String					FREEFORM_TRACELOGGER_ATTRIBUTEFEED					= "tracelogger.wayfairattributefeed";

	static IntgSrvLogger						logger												= IntgSrvLogger
																											.getInstance(FREEFORM_TRACELOGGER_ATTRIBUTEFEED);

	public static final String					CATEGORY_SPECIFIC_ATTRIBUTES_FILEUNPROCESSED_FOLDER	= "/opt/stibo/integration/hotfolder/WayfairIncoming/Attribute/File_Unprocessed/";

	public static final String					WAYFAIR_CATEGORY_ATTRIBUTE_HEADER					= "wayfair.category.attributes.header";

	public static final String					WAYFAIR_ATTRIBUTEFEED_DELIMITER						= "wayfair.attributefeed.delimiter";

	public static final String					delimiter											= IntgSrvPropertiesReader
																											.getProperty(WAYFAIR_ATTRIBUTEFEED_DELIMITER);

	public static final String					headerString										= IntgSrvPropertiesReader
																											.getProperty(WAYFAIR_CATEGORY_ATTRIBUTE_HEADER);

	public static final String[]				headers												= headerString.split(delimiter, -1);

	public static final String					WSITEMNO											= "WSItemNo";

	public static final String					ATTRIBUTE_GROUP_STAPLES								= "AttributeGroupStaples";

	public static final String					ATTRIBUTE_NAME_STAPLES								= "AttributeNameStaples";

	public static final String					ATTRIBUTE_VALUES_STAPLES							= "AttributeValueStaples";

	public static final String					ATTRIBUTE_GROUP_QUILL								= "AttributeGroupQuill";

	public static final String					ATTRIBUTE_NAME_QUILL								= "AttributeNameQuill";

	public static final String					ATTRIBUTE_VALUES_QUILL								= "AttributeValueQuill";

	public static final String					ATTRIBUTE_ID_REFERENCE_FILE_CURRENT_PATH			= "/opt/stibo/SpringBatch/Reference/wayfairlookup/attributefeed/current";

	public static final String					ATTRIBUTE_ID_REFERENCE_FILE_OLD_PATH				= "/opt/stibo/SpringBatch/Reference/wayfairlookup/attributefeed/old";

	public static final String					WAYFAIR_ATTRIBUTEFEED_FILEDONE						= "/opt/stibo/integration/hotfolder/WayfairIncoming/Attribute/File_Done/";

	public static IntgSrvLogger					ehfLogger											= IntgSrvLogger
																											.getInstance(DatamigrationAppConstants.EHF_LOGGER_WAYFAIR);

	public static ErrorHandlingFrameworkICD		ehfICD												= null;

	public static ErrorHandlingFrameworkHandler	ehfHandler											= new ErrorHandlingFrameworkHandler();

	public static String						traceId												= null;

	String										logMessage											= null;

	public static final String					WAYFAIR_CATEGORYSPEC_ATTRIBUTE_MAX_RECORD			= "wayfair.categoryspec.attribute.max.records";

	public static final String		DMITEM_HYPHEN										= "DMItem-";
	
	public static final String		PDB_HYPHEN											= "PDB-";
	
	public static final String		QUILL_HYPHEN										= "QUL-";

	private final static String						WAYFAIRATTRIBUTEFEED					= "Wayfair Attribute Feed ";
	static String									filebadDir							= IntgSrvUtils
																								.reformatFilePath(IntgSrvPropertiesReader
																										.getProperty(DatamigrationAppConstants.WAYFAIRATTRIBUTE_BAD_FOLDER));
	
	public static int							successCount;
	public static int							failureCount;
	public static long startTime; 

	/**
	 * 
	 */
	public void attributeInboundProcessor() {

		try {

			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(SPRINGBATCH_ECO_SYSTEM, SPRINGBATCH_COMPONENT_ID,
					SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "ACTIVE SKU FEED request", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			File folder = new File(IntgSrvUtils.reformatFilePath(CATEGORY_SPECIFIC_ATTRIBUTES_FILEUNPROCESSED_FOLDER));
			File[] files = DatamigrationCommonUtil.sortFilesBasedOnFIFO(folder, CategorySpecificAttributeScheduler.PUBLISH_ID);
			if(files==null || files.length==0){
				logger.info("No files in hotfolder to process");
			}
			for (int i = 0; i < files.length; i++) {
				try {
					// Check if the file is an xsv or a dsv file
					if ((files[i].getName().endsWith(DatamigrationAppConstants.XSV_EXTENSION))
							|| (files[i].getName().endsWith(DatamigrationAppConstants.DSV_EXTENSION))) {
						logger.info(new Date().toString() + " Cat.Spec Attribute Processing file : " + files[i].getName());
						DatamigrationCommonUtil.printConsole("Cat.Spec Attribute  >>Processing File : " + files[i].getName());
						readIncomingFile(files[i]);
					} else {
						logger.error("invalid file : " + files[i].getName());
					}
				} catch (Exception e) {
					logger.error("Exception while processing file : " + files[i].getName() + " : " + e.getMessage());
					e.printStackTrace();
					IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
							DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
				}
			}
		} catch (ErrorHandlingFrameworkException e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
		}
	}

	/**
	 * @return
	 */
	public Map<String, String> getAttributeIDsFromReferenceFile() {

		Map<String, String> attributeIDsMap = new HashMap<String, String>();
		File referenceFile = DatamigrationCommonUtil.getReferenceFile(ATTRIBUTE_ID_REFERENCE_FILE_CURRENT_PATH,
				ATTRIBUTE_ID_REFERENCE_FILE_OLD_PATH);

		FileReader fileReader;
		BufferedReader reader;
		String tempString;
		String[] values;
		if (referenceFile != null) {
			try {
				fileReader = new FileReader(referenceFile);
				reader = new BufferedReader(fileReader);
				logger.info("started getting IDs");
				while ((tempString = reader.readLine()) != null) {
					if (!tempString.equals(DatamigrationAppConstants.EMPTY_STR)) {
						values = tempString.split(delimiter, -1);
						attributeIDsMap.put(values[0], values[1]);
					}
				}
				logger.info("got IDs from reference files. Total:" + attributeIDsMap.size());
				reader.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				logger.error("Exception while getting reference File : " + e.getMessage());
				logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
						EHF_MSGTYPE_INFO_NONSLA, "Exception : " + e.getMessage(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
						DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.error(logMessage);
				IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
						DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
			} catch (IOException e) {
				logger.error("Exception while getting reference File : " + e.getMessage());
				logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
						EHF_MSGTYPE_INFO_NONSLA, "Exception : " + e.getMessage(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
						DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.error(logMessage);
				e.printStackTrace();
				IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
						DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
			}

		} else {
			logger.error("Reference File to Attribute IDs from attributemetadata Feed not found.");
		}

		return attributeIDsMap;
	}

	/**
	 * @param referenceMap
	 * @param key
	 * @return
	 */
	public String getValueFromReferenceMap(Map<String, String> referenceMap, String key, File file) {

		for (String AttributeNames : referenceMap.keySet()) {
			if (key.equals(AttributeNames)) {
				String value = referenceMap.get(key);
				if (value != null) {
					return value;
				}
			}
		}
//		DatamigrationCommonUtil.appendWriterFile(file.getParentFile().getParentFile() + "/Report/Report_" + file.getName(), "Attribute ID not found in the reference file for Attribute Name : " + key);
		logger.error("Attribute ID not found in the reference file for key : " + key);
		return "";
	}

	/**
	 * @param file
	 * 
	 *            reads and processes input file
	 */
	public void readIncomingFile(File file) {

		successCount = 0;
		failureCount = 0;
		startTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
		FileReader fileReader;
		String tempString;
		int lineno = 0;
		int fileCount = 1;
		int maxRecordsPerFile = Integer.parseInt(IntgSrvPropertiesReader.getProperty(WAYFAIR_CATEGORYSPEC_ATTRIBUTE_MAX_RECORD));
		logger.info("Max records per file = " + maxRecordsPerFile);
		String[] values;
		List<Map<String, String>> quillCategorySpecAttributesList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> staplesCategorySpecAttributesList = new ArrayList<Map<String, String>>();
		Map<String, String> mapContainingRecord;
		try {
			fileReader = new FileReader(file.getPath());
			BufferedReader br = new BufferedReader(fileReader);

			// get attribute IDs from reference file
			logger.info("getting attribute IDs from reference file");
			Map<String, String> referenceMap = getAttributeIDsFromReferenceFile();
			logger.info("got attribute IDs from reference file");
			// parse file line by line
			while ((tempString = br.readLine()) != null) {
				if (!tempString.equals("")) {
					// logger.info("#"+(lineno)+" :"+tempString);
					if (lineno == 0) {
						// headers=tempString.split(delimiter,-1);
					} else {
						values = tempString.split(delimiter, -1);
						// logger.info("values splitted");
						mapContainingRecord = DatamigrationCommonUtil.getRecordAsMap(headers, values, logger);
						// logger.info("Map created, size: "+mapContainingRecord.size());
						if (mapContainingRecord != null) {
							successCount++;
							if (!((EMPTY_STR.equalsIgnoreCase(mapContainingRecord.get(ATTRIBUTE_GROUP_STAPLES)))
									&& (EMPTY_STR.equalsIgnoreCase(mapContainingRecord.get(ATTRIBUTE_NAME_STAPLES))) && (EMPTY_STR
										.equalsIgnoreCase(mapContainingRecord.get(ATTRIBUTE_VALUES_STAPLES))))) {
								staplesCategorySpecAttributesList.add(mapContainingRecord);
							} else if (!((EMPTY_STR.equalsIgnoreCase(mapContainingRecord.get(ATTRIBUTE_GROUP_QUILL)))
									&& (EMPTY_STR.equalsIgnoreCase(mapContainingRecord.get(ATTRIBUTE_NAME_QUILL))) && (EMPTY_STR
										.equalsIgnoreCase(mapContainingRecord.get(ATTRIBUTE_VALUES_QUILL))))) {
								quillCategorySpecAttributesList.add(mapContainingRecord);
							}
						}else{
							failureCount++;
							logger.error("Record omitted. Incorrect no of values :"+tempString);
							DatamigrationCommonUtil.appendWriterFile(file.getParentFile().getParentFile() + "/Report/Report_" + file.getName(), tempString
									+ "~Has incorrect number of Values in record :"+values.length);
						}

						if (staplesCategorySpecAttributesList.size() + quillCategorySpecAttributesList.size() == maxRecordsPerFile) {
							logger.info("max file limit reached. no of staples records=" + staplesCategorySpecAttributesList.size()
									+ " no of quill records=" + quillCategorySpecAttributesList.size());
							createAndSendSTEPxml(file, staplesCategorySpecAttributesList, quillCategorySpecAttributesList, referenceMap,
									fileCount, false);
							staplesCategorySpecAttributesList.clear();
							quillCategorySpecAttributesList.clear();
							fileCount++;
						}

					}
					lineno++;
				}
			}	
			long endTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
			

			br.close();
			fileReader.close();

			if (staplesCategorySpecAttributesList.size() + quillCategorySpecAttributesList.size() > 0) {
				logger.info("creating final STEP xml " + fileCount);
				createAndSendSTEPxml(file, staplesCategorySpecAttributesList, quillCategorySpecAttributesList, referenceMap, fileCount,
						true);
			}else if(staplesCategorySpecAttributesList.size() + quillCategorySpecAttributesList.size() == 0){
				logger.error("No records generated from the input file :"+file.getName()+" moving to file bad folder.");
				DatamigrationCommonUtil.moveFileToFileBad(file, CategorySpecificAttributeScheduler.PUBLISH_ID);
			}
			
			//report generation
			DatamigrationCommonUtil.generateExecutionSummary(WAYFAIRATTRIBUTEFEED, DatamigrationCommonUtil.getReportFolderPath(file), file.getName(), successCount,
					(endTime - startTime), failureCount);
			
		} catch (FileNotFoundException e) {
			logger.error("Exception while processing file : " + file.getName() + " : " + e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "Exception while processing file : " + file.getName() + " : " + e.getMessage(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.error(logMessage);
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
		} catch (IOException e) {
			logger.error("Exception while processing file : " + file.getName() + " : " + e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "Exception while processing file : " + file.getName() + " : " + e.getMessage(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.error(logMessage);
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
		} catch (Exception e) {
			logger.error("Exception while processing file : " + file.getName() + " : " + e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "Exception while processing file : " + file.getName() + " : " + e.getMessage(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.error(logMessage);
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
		}
	}

	public void createAndSendSTEPxml(File file, List<Map<String, String>> staplesCategorySpecAttributesList,
			List<Map<String, String>> quillCategorySpecAttributesList, Map<String, String> referenceMap, int fileCount,
			boolean fileMoveActive) {

		logger.info("creating step xml object.");
		// create step xml object
		STEPProductInformation stepPrdInfo = createSTEPProductInformationObject(staplesCategorySpecAttributesList,
				quillCategorySpecAttributesList, referenceMap, file);

		String filename = file.getName();
		filename = filename.substring(0, filename.length() - 4) + UNDERSCORE + fileCount + XML_EXTENSION;

		// marshall object
		logger.info("Marshalling output xml");
		File outputFile = DatamigrationCommonUtil.marshallObject(stepPrdInfo, new File(filename),
				DatamigrationAppConstants.WAYFAIR_ATTRIBUTE_FEED_OUTPUT_FOLDER, CategorySpecificAttributeScheduler.PUBLISH_ID);
		logger.info("output xml generated : " + outputFile.getName());

		// send file to sftp location
		logger.info("sending step xml to sftp location");
		DatamigrationCommonUtil.sendFile(outputFile, file, WAYFAIR_ATTRIBUTEFEED_FILEDONE, CATGEORY_SPEC_ATTRIBUTES_INSTANCE_NO,
				fileMoveActive, CategorySpecificAttributeScheduler.PUBLISH_ID);
		logger.info("file transfered successfully.");

	}

	/**
	 * @param staplesCategorySpecAttributesList
	 * @param quillCategorySpecAttributesList
	 * @param referenceMap
	 * @return
	 */
	public STEPProductInformation createSTEPProductInformationObject(List<Map<String, String>> staplesCategorySpecAttributesList,
			List<Map<String, String>> quillCategorySpecAttributesList, Map<String, String> referenceMap, File file) {

		logger.info("creating Step xml");

		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepPrdInfo = objectFactory.createSTEPProductInformation();
		ProductsType products = objectFactory.createProductsType();

		// add staples objects
		for (Map<String, String> staplesMap : staplesCategorySpecAttributesList) {
			ProductType product = objectFactory.createProductType();
			product.setID(DMITEM_HYPHEN + staplesMap.get(WSITEMNO));
			product.setParentID("WayFairItems");
			product.setUserTypeID("Item");
			product.setSelected(false);
			product.setReferenced(true);

			ValuesType values = objectFactory.createValuesType();
			ValueType value = objectFactory.createValueType();

			String key = staplesMap.get(ATTRIBUTE_GROUP_STAPLES) + REFERENCE_FILE_DELIMITER + staplesMap.get(ATTRIBUTE_NAME_STAPLES) + REFERENCE_FILE_DELIMITER + DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE;
			String IDs = getValueFromReferenceMap(referenceMap, key ,file);
			if (!IntgSrvUtils.isNullOrEmpty(IDs)) {
				String[] referenceValues = IDs.split(REFERENCE_FILE_DELIMITER, -1);
				if (referenceValues.length > 1) {
					value.setAttributeID(PDB_HYPHEN + referenceValues[1]);
				} else {
					successCount--;
					failureCount++;
					DatamigrationCommonUtil.appendWriterFile(file.getParentFile().getParentFile() + "/Report/Report_" + file.getName(), staplesMap
							.get(WSITEMNO)
							+ "~" + referenceValues[0] + "doesn't have Attribute Id");
				}

				value.setContent(staplesMap.get(ATTRIBUTE_VALUES_STAPLES));

				values.getValueOrMultiValueOrValueGroup().add(value);
				product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
				products.getProduct().add(product);
			}else{
				successCount--;
				failureCount++;
				DatamigrationCommonUtil.appendWriterFile(file.getParentFile().getParentFile() + "/Report/Report_" + file.getName(), staplesMap
						.get(WSITEMNO)
						+ "~ Reference file doesn't have the key :"+key);
			}
		}

		// add quill objects
		for (Map<String, String> staplesMap : quillCategorySpecAttributesList) {
			ProductType product = objectFactory.createProductType();
			product.setID(DMITEM_HYPHEN + staplesMap.get(WSITEMNO));
			product.setParentID("WayFairItems");
			product.setUserTypeID("Item");
			product.setSelected(false);
			product.setReferenced(true);

			ValuesType values = objectFactory.createValuesType();
			ValueType value = objectFactory.createValueType();

			String key = staplesMap.get(ATTRIBUTE_GROUP_QUILL) + REFERENCE_FILE_DELIMITER + staplesMap.get(ATTRIBUTE_NAME_QUILL) + REFERENCE_FILE_DELIMITER + DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE;
			String IDs = getValueFromReferenceMap(referenceMap, key, file);
			if (!IntgSrvUtils.isNullOrEmpty(IDs)) {
				String[] referenceValues = IDs.split(REFERENCE_FILE_DELIMITER, -1);
				if (referenceValues.length > 1) {
					value.setAttributeID(QUILL_HYPHEN + referenceValues[1]);
				} else {
					successCount--;
					failureCount++;
					DatamigrationCommonUtil.appendWriterFile(file.getParentFile().getParentFile() + "/Report/" + file.getName(), staplesMap
							.get(WSITEMNO)
							+ "~" + referenceValues[0] + "doesn't have reference Id");
				}
				value.setContent(staplesMap.get(ATTRIBUTE_VALUES_QUILL));

				values.getValueOrMultiValueOrValueGroup().add(value);
				product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
				products.getProduct().add(product);
				
			}else{
			successCount--;
			failureCount++;
			DatamigrationCommonUtil.appendWriterFile(file.getParentFile().getParentFile() + "/Report/Report_" + file.getName(), staplesMap
					.get(WSITEMNO)
					+ "~ Reference file doesn't have the key :"+key);
		}
		}

		stepPrdInfo.setProducts(products);
		stepPrdInfo.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepPrdInfo.setContextID(CONTEXT_ID_VALUE); // Hard coded value
		stepPrdInfo.setExportContext(EXPORT_CONTEXT_VALUE);// Hard coded value
		stepPrdInfo.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded value
		logger.info("Step xml created");
		return stepPrdInfo;
	}

	public static void main(String[] args) {

		new CategorySpecificAttributeInboundProcessor().attributeInboundProcessor();

	}

}
