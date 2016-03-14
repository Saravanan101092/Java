
package com.staples.pim.delegate.merchupdate.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0024_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0025_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0026_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0484_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0521_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0522_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0523_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0524_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ACTION_CODE_DELETE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.MERCHANDISING_FILEDONE_FOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.MERCHANDISING_OUTPUT_FOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.MERCHANDISING_OUTPUT_FOLDER_DELETE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.BufferedReader;
import java.io.File;
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
import com.staples.pcm.stepcontract.beans.ClassificationType;
import com.staples.pcm.stepcontract.beans.ClassificationsType;
import com.staples.pcm.stepcontract.beans.KeyValueType;
import com.staples.pcm.stepcontract.beans.MetaDataType;
import com.staples.pcm.stepcontract.beans.NameType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.merchupdate.runner.MerchScheduler;

public class MerchInboundProcessor {

	static IntgSrvLogger						logger		= IntgSrvLogger
																	.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_MERCH);

	public static IntgSrvLogger					ehfLogger	= IntgSrvLogger.getInstance(EHF_LOGGER);
	public static ErrorHandlingFrameworkICD		ehfICD		= null;
	public static ErrorHandlingFrameworkHandler	ehfHandler	= new ErrorHandlingFrameworkHandler();
	public static String						traceId		= null;

	private static final String					header		= "A0484~A0533~A0521~A0524~A0522~A0523~A0534~A0535~A0536~A0529";

	/**
	 * @param fileName
	 * @return
	 */
	public static Map<String, List<Map<String, String>>> readMerchIncomingFile(File fileName) {

		boolean useDefaultHeader = true;
		Map<String, List<Map<String, String>>> mapContainingMerchLists = new HashMap<String, List<Map<String, String>>>();
		List<Map<String, String>> listMerchVOs = new ArrayList<Map<String, String>>();
		List<Map<String, String>> listMerchDeleteVOs = new ArrayList<Map<String, String>>();
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			String tempString;
			long lineNumber = 0;
			String headers[] = null;
			String headerStr = "";

			String delimiter = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MERCH_INPUTFILE_DELIMITER);

			if (useDefaultHeader) {
				DatamigrationCommonUtil.printConsole("Using default header.. " + header);
				headers = header.split(delimiter, -1);
				while ((tempString = br.readLine()) != null) {
					if (!tempString.equals("")) {
						if (lineNumber > 0) {
							String values[] = tempString.split(delimiter, -1);
							DatamigrationCommonUtil.printConsole(tempString);
							Map<String, String> mapObject = DatamigrationCommonUtil.getValuesInMap(headers, values);
							if (mapObject != null && !mapObject.isEmpty()) {
								if (ACTION_CODE_DELETE.equalsIgnoreCase(values[0])) {
									listMerchDeleteVOs.add(mapObject);
								} else {
									listMerchVOs.add(mapObject);
								}
							} else {
								DatamigrationCommonUtil.generateReportFile(fileName.getParentFile().getParent() + "/File_Bad/", fileName
										.getName(), headerStr, "No.of values in this record is not correct!", tempString);
							}
						}
						lineNumber++;
					}
				}
			} else {
				while ((tempString = br.readLine()) != null) {
					if (!tempString.equals("")) {
						if (lineNumber < 5) {
							if (lineNumber == 3) {
								DatamigrationCommonUtil.printConsole(tempString);
								headers = tempString.split(delimiter, -1);
								headerStr = tempString;
							}
							// continue;
						} else {
							String values[] = tempString.split(delimiter, -1);
							DatamigrationCommonUtil.printConsole(tempString);
							Map<String, String> mapObject = DatamigrationCommonUtil.getValuesInMap(headers, values);
							if (mapObject != null && !mapObject.isEmpty()) {
								if (ACTION_CODE_DELETE.equalsIgnoreCase(values[0])) {
									listMerchDeleteVOs.add(mapObject);
								} else {
									listMerchVOs.add(mapObject);
								}
							} else {
								DatamigrationCommonUtil.generateReportFile(fileName.getParentFile().getParent() + "/File_Bad/", fileName
										.getName(), headerStr, "No.of values in this record is not correct!", tempString);
							}
						}
						lineNumber++;
					}
				}
			}
			fr.close();
		} catch (IOException e) {
			logger.error("Caught an exception in readMerchIncomingFile() :" + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), MerchScheduler.PUBLISH_ID);
		}
		mapContainingMerchLists.put("MerchRecords", listMerchVOs);
		mapContainingMerchLists.put("MerchDRecords", listMerchDeleteVOs);
		return mapContainingMerchLists;

	}

	/**
	 * @param fileName
	 */
	public static void merchHierarchyProcessor(File fileName) throws Exception {

		String logMessage = null;
		try {

			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_SPRINGBATCH_PUBLISH_ID_MERCH);// FIXME
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();
			ehfICD.setAttributeTransactionType(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRANSACTION_TYPE_FILETOFILE);

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Started to read input file for merchandising hierarchy", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			Map<String, List<Map<String, String>>> mapContainingMerchLists = readMerchIncomingFile(fileName);

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH5,
					EHF_MSGTYPE_INFO_NONSLA, "Attribute values fetched from Galaxy xsv file and stored in map",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			List<Map<String, String>> listMerchVOs = mapContainingMerchLists.get("MerchRecords");
			List<Map<String, String>> listMerchDeleteVOs = mapContainingMerchLists.get("MerchDRecords");

			if ((listMerchVOs != null && !listMerchVOs.isEmpty()) || (listMerchDeleteVOs != null && !listMerchDeleteVOs.isEmpty())) {

				if (listMerchVOs.size() > 0) {
					STEPProductInformation stepPrdInfo = merchStepXMLGenerator(listMerchVOs, fileName, false);

					logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
							EHF_MSGTYPE_INFO_NONSLA, "STEPProductInformation object for merch update constructed for marshalling",
							EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil
									.getClassName(), ehfICD);
					ehfLogger.info(logMessage);

					File outputFile = DatamigrationCommonUtil.marshallObject(stepPrdInfo, fileName, MERCHANDISING_OUTPUT_FOLDER,
							MerchScheduler.PUBLISH_ID);

					logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
							EHF_MSGTYPE_INFO_NONSLA, "STEP xml generated for merch update", EHF_SPRINGBATCH_ITEMUTILITY_USER,
							DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
					ehfLogger.info(logMessage);

					DatamigrationCommonUtil.sendFile(outputFile, fileName, MERCHANDISING_FILEDONE_FOLDER, "6_U", true,
							DatamigrationAppConstants.EHF_SPRINGBATCH_PUBLISH_ID_MERCH);

					logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
							EHF_MSGTYPE_INFO_NONSLA, "STEP xml for merch update moved to the SFTP location",
							EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil
									.getClassName(), ehfICD);
					ehfLogger.info(logMessage);
				}
				if (listMerchDeleteVOs.size() > 0) {
					STEPProductInformation stepPrdInfo = merchStepXMLGenerator(listMerchDeleteVOs, fileName, true);

					logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
							EHF_MSGTYPE_INFO_NONSLA, "STEPProductInformation object for merch delete constructed for marshalling",
							EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil
									.getClassName(), ehfICD);
					ehfLogger.info(logMessage);

					File outputFile = DatamigrationCommonUtil.marshallObject(stepPrdInfo, fileName, MERCHANDISING_OUTPUT_FOLDER_DELETE,
							MerchScheduler.PUBLISH_ID);

					logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
							EHF_MSGTYPE_INFO_NONSLA, "STEP xml generated for merch delete", EHF_SPRINGBATCH_ITEMUTILITY_USER,
							DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
					ehfLogger.info(logMessage);

					DatamigrationCommonUtil.sendFile(outputFile, fileName, MERCHANDISING_FILEDONE_FOLDER, "6_D", true,
							DatamigrationAppConstants.EHF_SPRINGBATCH_PUBLISH_ID_MERCH);

					logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
							EHF_MSGTYPE_INFO_NONSLA, "STEP xml for merch delete moved to the SFTP location",
							EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil
									.getClassName(), ehfICD);
					ehfLogger.info(logMessage);
				}
			} else {
				DatamigrationCommonUtil.moveFileToFileBad(fileName, MerchScheduler.PUBLISH_ID);
			}
		} catch (ErrorHandlingFrameworkException e) {
			logger.error("Caught an exception in readMerchIncomingFile() :" + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), MerchScheduler.PUBLISH_ID);
		}
	}

	public static void main(String[] args) {

		File file = new File(
				"C:/opt/stibo/SpringBatchCopynPrint/hotfolder/MerchIncoming/File_Unprocessed/sample2_2015_12_17_14_59_52_052674.xsv");
		// merchHierarchyProcessor(file);
	}

	/**
	 * @param listOfMerchHierarchy
	 * @param fileName
	 * @return
	 */
	public static STEPProductInformation merchStepXMLGenerator(List<Map<String, String>> listOfMerchHierarchy, File fileName,
			boolean isDelete) throws Exception {

		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepPrdInfo = objectFactory.createSTEPProductInformation();
		ClassificationsType classifications = objectFactory.createClassificationsType();

		ClassificationType classfication = null;

		for (Map<String, String> merchHierarchyMap : listOfMerchHierarchy) {
			logger.info("Processing Record : " + merchHierarchyMap);
			classfication = createClassfication(merchHierarchyMap, isDelete);
			classifications.getClassification().add(classfication);
		}

		stepPrdInfo.setClassifications(classifications);
		stepPrdInfo.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		// Date
		// time
		stepPrdInfo.setContextID(CONTEXT_ID_VALUE); // Hard coded value
		stepPrdInfo.setExportContext(EXPORT_CONTEXT_VALUE);// Hard coded value
		stepPrdInfo.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded value
		return stepPrdInfo;
	}

	// classfication.

	/**
	 * @param merchHierarchyMap
	 * @return
	 */
	private static ClassificationType createClassfication(Map<String, String> merchHierarchyMap, boolean isdelete) throws Exception {

		ObjectFactory objectFactory = new ObjectFactory();

		ClassificationType classification = objectFactory.createClassificationType();
		KeyValueType keyValue = objectFactory.createKeyValueType();
		NameType name = objectFactory.createNameType();
		MetaDataType metaData = objectFactory.createMetaDataType();
		ValueType valueType = objectFactory.createValueType();

		String categoryLevel = getSTEPFormat(merchHierarchyMap.get(A0523_STR));
		String categoryName = merchHierarchyMap.get(A0522_STR);
		String parentCategoryID = merchHierarchyMap.get(A0524_STR);
		String CategoryID = merchHierarchyMap.get(A0521_STR);
		String actionCode = merchHierarchyMap.get(A0484_STR);
		if (categoryLevel.equalsIgnoreCase("DIVISION")) {
			ClassificationType classificationRoot = objectFactory.createClassificationType();
			classificationRoot.setID("Classification 1 root");
			classificationRoot.setUserTypeID("Classification 1 user-type root");

			ClassificationType merchhierarchyClass = objectFactory.createClassificationType();
			merchhierarchyClass.setID("Merchandising_Hierarchy");
			merchhierarchyClass.setUserTypeID("MerchandisingRoot");
			keyValue.setKeyID(getSTEPFormat(categoryLevel) + "_Code");// Category
			// Level
			keyValue.setContent(CategoryID);

			name.setContent(categoryName);

			valueType.setAttributeID(A0024_STR);
			valueType.setContent(CategoryID);
			metaData.getValueOrMultiValueOrValueGroup().add(valueType);
			classification.setKeyValue(keyValue);
			classification.setUserTypeID(getSTEPFormat(categoryLevel));
			classification.getNameOrAttributeLinkOrSequenceProduct().add(name);
			classification.getNameOrAttributeLinkOrSequenceProduct().add(metaData);
			if (isdelete) {
				return classification;
			} else {
				merchhierarchyClass.getNameOrAttributeLinkOrSequenceProduct().add(classification);
				classificationRoot.getNameOrAttributeLinkOrSequenceProduct().add(merchhierarchyClass);
				return classificationRoot;
			}
		} else if (categoryLevel.equalsIgnoreCase("DEPARTMENT") || categoryLevel.equalsIgnoreCase("CLASS")) {

			if (!isdelete) {

				if (categoryLevel.equalsIgnoreCase("DEPARTMENT")) {
					keyValue.setKeyID("Division_Code");
					classification.setUserTypeID("Division");
				} else if (categoryLevel.equalsIgnoreCase("CLASS")) {
					keyValue.setKeyID("Department_Code");
					classification.setUserTypeID("Department");
				}
				// Category Level
				keyValue.setContent(parentCategoryID);

				if (categoryLevel.equalsIgnoreCase("DEPARTMENT")) {
					valueType.setAttributeID(A0024_STR);
				} else if (categoryLevel.equalsIgnoreCase("CLASS")) {
					valueType.setAttributeID(A0025_STR);
				}

				valueType.setContent(parentCategoryID);
				metaData.getValueOrMultiValueOrValueGroup().add(valueType);

				classification.setKeyValue(keyValue);

				classification.getNameOrAttributeLinkOrSequenceProduct().add(metaData);
			}
			ClassificationType innerClassification = objectFactory.createClassificationType();
			KeyValueType innerKeyvalue = objectFactory.createKeyValueType();
			NameType innerName = objectFactory.createNameType();
			MetaDataType innerMetadata = objectFactory.createMetaDataType();
			ValueType innerValuetype = objectFactory.createValueType();

			innerKeyvalue.setKeyID(getSTEPFormat(categoryLevel) + "_Code");
			innerKeyvalue.setContent(CategoryID);

			innerName.setContent(categoryName);

			if (categoryLevel.equalsIgnoreCase("DEPARTMENT")) {
				innerValuetype.setAttributeID(A0025_STR);
			} else if (categoryLevel.equalsIgnoreCase("CLASS")) {
				innerValuetype.setAttributeID(A0026_STR);
			}

			innerValuetype.setContent(CategoryID);

			innerMetadata.getValueOrMultiValueOrValueGroup().add(innerValuetype);
			innerClassification.setUserTypeID(getSTEPFormat(categoryLevel));
			innerClassification.setKeyValue(innerKeyvalue);
			if (!actionCode.equalsIgnoreCase("M")) {
				innerClassification.getNameOrAttributeLinkOrSequenceProduct().add(innerName);
			}
			innerClassification.getNameOrAttributeLinkOrSequenceProduct().add(innerMetadata);

			if (isdelete || parentCategoryID.equalsIgnoreCase("0") || actionCode.equalsIgnoreCase("U")) {
				return innerClassification;
			} else {
				classification.getNameOrAttributeLinkOrSequenceProduct().add(innerClassification);
				return classification;
			}
		}

		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String getSTEPFormat(String value) {

		if (value.equalsIgnoreCase("DIVISION") || value.equalsIgnoreCase("DIV")) {
			return "Division";
		} else if (value.equalsIgnoreCase("DEPARTMENT") || value.equalsIgnoreCase("DEPT")) {
			return "Department";
		} else if (value.equalsIgnoreCase("CLASS") || value.equalsIgnoreCase("CLS")) {
			return "Class";
		}
		return "";
	}
}