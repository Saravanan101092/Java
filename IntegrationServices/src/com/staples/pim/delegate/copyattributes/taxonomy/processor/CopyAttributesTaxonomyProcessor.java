
package com.staples.pim.delegate.copyattributes.taxonomy.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_COMPONENT_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ECO_SYSTEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ICD_CLASSNAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pcm.stepcontract.beans.ClassificationCrossReferenceType;
import com.staples.pcm.stepcontract.beans.ClassificationType;
import com.staples.pcm.stepcontract.beans.ClassificationsType;
import com.staples.pcm.stepcontract.beans.MetaDataType;
import com.staples.pcm.stepcontract.beans.NameType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

public class CopyAttributesTaxonomyProcessor {

	public static final String						FREEFORM_TRACELOGGER_COPYATTRIBUTES_TAXONOMY	= "tracelogger.copyattributes.taxonomy";

	static IntgSrvLogger							logger											= IntgSrvLogger
																											.getInstance(FREEFORM_TRACELOGGER_COPYATTRIBUTES_TAXONOMY);

	public static IntgSrvLogger						ehfLogger										= IntgSrvLogger
																											.getInstance("ehflogger.copyattributes");
	private static ErrorHandlingFrameworkICD		ehfICD											= null;
	private static ErrorHandlingFrameworkHandler	ehfHandler										= new ErrorHandlingFrameworkHandler();
	private static String							traceId											= null;
	String											logMessage										= null;

	public static final String						COPY_ATTRIBUTES_TAXONOMY_INPUT_FOLDER			= "/opt/stibo/integration/hotfolder/CopyAttributesIncoming/TaxonomyIncoming/File_Unprocessed/";
	public static final String						delimiter										= IntgSrvPropertiesReader
																											.getProperty("COPY_ATTRIBUTES_TAXONOMY_DELIMITER");
	public static final String						HeaderString									= IntgSrvPropertiesReader
																											.getProperty("COPY_ATTRIBUTES_TAXONOMY_HEADER");
	public static final String[]					headers											= HeaderString.split(delimiter, -1);
	public static final String						COPY_ATTRIBUTES_TAXONOMY_OUTPUT_FOLDER			= "COPY_ATTRIBUTES_TAXONOMY_OUTPUT_FOLDER";

	public static final String						COPYCATEGORY									= "CopyCategory";
	public static final String						COPYCATEGORY_PREFIX								= "CopyCategory-";
	public static final String						COPYCATEGORYROOT								= "CopyCategoryRoot";
	public static final String						CLASSIFICATIONROOT								= "Classification 1 root";
	public static final String						COPYCATEGORY_NAME								= "Copy Category";
	public static final String						COPYCATEGORYID									= "CopyCategoryID";
	public static final String						COPYCATEGORIES									= "CopyCategories";
	public static final String						COPYCATEGORY_MERCH_REFERENCE					= "CopyCategoryToMerchHierarchyReference";
	public static final String						COPYCATEGORYTOCLASSREFERENCE					= "CopyCategoryToClassReference";

	// CATID~|~CLASSID~|~CATNAME~|~STATUS
	public static final String						CATID											= "CATID";
	public static final String						CLASSID											= "CLASSID";
	public static final String						CATNAME											= "CATNAME";
	public static final String						STATUS											= "STATUS";

	public static final String						CATEGORY_DELIMITER								= "~";

	public static final String						MERCHCLASSID_LOVKEY								= "MerchClassID";

	public void processCopyAttributesTaxonomy() {

		try {
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(SPRINGBATCH_ECO_SYSTEM, SPRINGBATCH_COMPONENT_ID,
					SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId("");// FIXME
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();
			File folder = new File(IntgSrvUtils.reformatFilePath(COPY_ATTRIBUTES_TAXONOMY_INPUT_FOLDER));

			if (folder != null) {
				File[] files = folder.listFiles();
				for (int i = 0; i < files.length; i++) {
					System.out.println("Processing input file.." + files[i].getName());
					processIncomingFile(files[i]);
				}
			}
		} catch (ErrorHandlingFrameworkException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	public void processIncomingFile(File file) {

		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			logger.info("Processing input CopyAttributes spec data file " + file.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Processing input CopyAttributes spec datafile " + file.getName(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);
			if (file.getName().endsWith(".xsv") || file.getName().endsWith(".dsv")) {
				int lineno = 0;
				String tempString;

				// Map<String,Map<String,String>> taxonomyMaps = new
				// HashMap<String,Map<String,String>>();

				Map<String, Set<String>> classIDsLists = new HashMap<String, Set<String>>();

				while ((tempString = reader.readLine()) != null) {
					if (!tempString.equals("")) {
						if (lineno != 0) {
							String[] values = tempString.split(delimiter, -1);
							Map<String, String> thisRecord = DatamigrationCommonUtil.getRecordAsMap(headers, values, logger);
							if (thisRecord != null) {
								String key = thisRecord.get(CATID) + CATEGORY_DELIMITER + thisRecord.get(CATNAME);
								if (classIDsLists.containsKey(key)) {
									classIDsLists.get(key).add(thisRecord.get(CLASSID));
								} else {
									Set<String> classIDs = new TreeSet<String>();
									classIDs.add(thisRecord.get(CLASSID));
									classIDsLists.put(key, classIDs);
								}

							} else {
								logger.error("Record omitted. Incorrect no of values :" + thisRecord);
								DatamigrationCommonUtil.appendWriterFile(
										file.getParentFile().getParentFile() + "/Report/Report_" + file.getName(), tempString
												+ "~Has incorrect number of Values in record :" + values.length);
							}
						}
						lineno++;
					}
				}
				reader.close();
				fileReader.close();
				createAndSendSTEPxml(classIDsLists, file);

			} else {
				logger.info("Invalid file format " + file.getName());
				DatamigrationCommonUtil.printConsole("Invalid file format " + file.getName());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void createAndSendSTEPxml(Map<String, Set<String>> taxonomyMaps, File file) {

		if (taxonomyMaps != null && !taxonomyMaps.isEmpty()) {
			STEPProductInformation stepproductInformation = createSTEPProductInformationObject(taxonomyMaps);

			logger.info("STEPProductInformation object for copy attributes taxonomy data constructed for Marshalling");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "STEPProductInformation object for copy attributes taxonomy data constructed for Marshalling",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			File outputFile = DatamigrationCommonUtil.marshallObject(stepproductInformation, file, COPY_ATTRIBUTES_TAXONOMY_OUTPUT_FOLDER,
					"");

			logger.info("Output STEP xml for copy attributes taxonomy data generated ");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "Output STEP xml for attributes generated " + outputFile.getName(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			DatamigrationCommonUtil.sendFile(outputFile, file, file.getParentFile().getParentFile() + "/File_Done/",
					"13_COPY_ATTRIBUTES_TAXONOMY", true, "");// FIXME - publish
																// ID

			logger.info("SFTP file transfer success for " + outputFile.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "SFTP file transfer success for " + outputFile.getName(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
		} else {
			logger.info(" invalid file" + file + " moved to File_Bad");
			DatamigrationCommonUtil.moveFileToFileBad(file, "");// FIXME publish
																// ID

		}
	}

	public STEPProductInformation createSTEPProductInformationObject(Map<String, Set<String>> taxonomyMaps) {

		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = objectFactory.createSTEPProductInformation();

		ClassificationsType classifications = objectFactory.createClassificationsType();

		ClassificationType rootClassification = objectFactory.createClassificationType();
		rootClassification.setID(COPYCATEGORY);
		rootClassification.setUserTypeID(COPYCATEGORYROOT);
		rootClassification.setParentID(CLASSIFICATIONROOT);

		NameType rootName = objectFactory.createNameType();
		rootName.setContent(COPYCATEGORY_NAME);
		rootClassification.getNameOrAttributeLinkOrSequenceProduct().add(rootName);

		for (String taxonomyMapKey : taxonomyMaps.keySet()) {
			Set<String> classIDs = taxonomyMaps.get(taxonomyMapKey);

			String[] categoryInfo = taxonomyMapKey.split(CATEGORY_DELIMITER);
			String categoryID = categoryInfo[0];
			String categoryName = categoryInfo[1];

			ClassificationType classification = objectFactory.createClassificationType();
			classification.setID(COPYCATEGORY_PREFIX + categoryID);
			classification.setUserTypeID(COPYCATEGORIES);

			NameType name = objectFactory.createNameType();
			name.setContent(categoryName);
			classification.getNameOrAttributeLinkOrSequenceProduct().add(name);

			// CopyCategoryToClassReference removed as per dinesh' request
			// String classidsString = "";
			for (String classId : classIDs) {

				// classidsString+=classId+DatamigrationAppConstants.COMMA;
				String classlink = DatamigrationCommonUtil.getValuesFromLOV(MERCHCLASSID_LOVKEY, classId, false);

				ClassificationCrossReferenceType classificationCrossReferenceType = objectFactory.createClassificationCrossReferenceType();
				if (!IntgSrvUtils.isNullOrEmpty(classlink)) {
					if (classlink.contains(":")) {
						String[] temp = classlink.split(":", -1);
						classlink = temp[1];
					}

					classificationCrossReferenceType.setClassificationID(classlink);
					classificationCrossReferenceType.setType(COPYCATEGORY_MERCH_REFERENCE);
					classification.getNameOrAttributeLinkOrSequenceProduct().add(classificationCrossReferenceType);
				}
			}

			// classidsString = classidsString.substring(0,
			// classidsString.length()-1);

			MetaDataType metadata = objectFactory.createMetaDataType();

			ValueType categoryvalue = objectFactory.createValueType();
			categoryvalue.setAttributeID(COPYCATEGORYID);
			categoryvalue.setContent(categoryID);
			metadata.getValueOrMultiValueOrValueGroup().add(categoryvalue);

			// ValueType classvalue = objectFactory.createValueType();
			// classvalue.setAttributeID(COPYCATEGORYTOCLASSREFERENCE);
			// classvalue.setContent(classidsString);
			// metadata.getValueOrMultiValueOrValueGroup().add(classvalue);

			classification.getNameOrAttributeLinkOrSequenceProduct().add(metadata);

			rootClassification.getNameOrAttributeLinkOrSequenceProduct().add(classification);
		}

		classifications.getClassification().add(rootClassification);
		stepProductInformation.setClassifications(classifications);
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
																// value
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
																		// coded
																		// value
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);
		stepProductInformation.setUseContextLocale(false);
		return stepProductInformation;
	}

	public static void main(String[] args) {

		CopyAttributesTaxonomyProcessor copyAttributesTaxonomyProcessor = new CopyAttributesTaxonomyProcessor();
		copyAttributesTaxonomyProcessor.processCopyAttributesTaxonomy();
		// String taxonomyMapKey = "abc~";
		// String[] categoryInfo = taxonomyMapKey.split(CATEGORY_DELIMITER,-1);
		// String categoryID = categoryInfo[0];
		// String categoryName = categoryInfo[1];

	}

}
