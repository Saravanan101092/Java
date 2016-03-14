
package com.staples.pim.delegate.responsmatrixupdate.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0026_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0521_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.RESPMATRIX_FILEDONE_FOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.RESPONSIBILITY_MATRIX_OUTPUT_FOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pcm.stepcontract.beans.ClassificationType;
import com.staples.pcm.stepcontract.beans.ClassificationsType;
import com.staples.pcm.stepcontract.beans.KeyValueType;
import com.staples.pcm.stepcontract.beans.MetaDataType;
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
import com.staples.pim.delegate.responsmatrixupdate.runner.ResponsibilityScheduler;

/**
 *  
 *
 */
public class ResponsibilityInboundProcessor {

	static IntgSrvLogger						logger			= IntgSrvLogger
																		.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_RESP);
	public static IntgSrvLogger					ehfLogger		= IntgSrvLogger.getInstance(EHF_LOGGER);
	public static ErrorHandlingFrameworkICD		ehfICD			= null;
	public static ErrorHandlingFrameworkHandler	ehfHandler		= new ErrorHandlingFrameworkHandler();
	public static String						traceId			= null;
	private static String[]						headers;
	public static String[]						RespAttributes	= { "A0029", "A0248", "A0213", "A0254", "A0214", "A0251", "A0249", "A0250",
			"A0255", "A0256", "A0252", "A0253"					};
	public static final String					header			= "A0012~A0533~A0521~A0029>A0248~A0213>A0254~A0214>A0251~A0249>A0250~A0255>A0256~A0252>A0253~A0484~A0529";
	public final static String					Class			= "Class";
	public final static String					Class_Code		= "Class_Code";

	/**
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, String>> readIncomingFile(File filePath) throws IOException {

		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		boolean useDefaultHeader = true;
		String tempString;
		long lineNumber = 0;

		String delimiter = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.RESPONSIBILITY_MATRIX_INPUTFILE_DELIMITER);
		DatamigrationCommonUtil.printConsole(delimiter);
		List<Map<String, String>> allRespRows = new ArrayList<Map<String, String>>();

		if (useDefaultHeader) {
			DatamigrationCommonUtil.printConsole("Using default headers..The headers are " + header);
			logger.info("Using default headers..the headers are " + header);
			headers = header.split(delimiter, -1);
			while ((tempString = br.readLine()) != null) {
				if (!tempString.equals("")) {
					if (lineNumber > 0) {
						String values[] = tempString.split(delimiter, -1);
						allRespRows.add(DatamigrationCommonUtil.getValuesWithDelimiterInMap(headers, values));
						DatamigrationCommonUtil.printConsole("the entries are " + tempString);
					}
					lineNumber++;
				}
			}
		} else {
			while ((tempString = br.readLine()) != null) {
				if (!tempString.equals("")) {
					if (lineNumber < 5) {
						if (lineNumber == 3) {
							DatamigrationCommonUtil.printConsole("the headers are " + tempString);
							logger.info("the headers are " + tempString);
							headers = tempString.split(delimiter, -1);
						}
					} else {
						String values[] = tempString.split(delimiter, -1);
						allRespRows.add(DatamigrationCommonUtil.getValuesWithDelimiterInMap(headers, values));
						DatamigrationCommonUtil.printConsole("the entries are" + tempString);
					}
					lineNumber++;
				}
			}
		}
		logger.info("No of lines processed in the file : " + lineNumber);
		fr.close();
		return allRespRows;

	}

	public static STEPProductInformation buildXML(List<Map<String, String>> listContainingMaps) {

		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepPrdInfo = objectFactory.createSTEPProductInformation();
		ClassificationsType classifications = objectFactory.createClassificationsType();
		ClassificationType classification;
		Map<String, String> thisMap;
		ValueType valuetype;
		for (int i = 0; i < listContainingMaps.size(); i++) {
			thisMap = listContainingMaps.get(i);
			classification = objectFactory.createClassificationType();
			classification.setUserTypeID(Class);

			KeyValueType keyvalue = objectFactory.createKeyValueType();
			keyvalue.setKeyID(Class_Code);
			keyvalue.setContent(thisMap.get(A0521_STR));
			classification.setKeyValue(keyvalue);

			MetaDataType metadata = objectFactory.createMetaDataType();
			valuetype = objectFactory.createValueType();
			valuetype.setAttributeID(A0026_STR);
			valuetype.setContent(thisMap.get(A0521_STR));
			metadata.getValueOrMultiValueOrValueGroup().add(valuetype);

			for (String respAttribute : RespAttributes) {
				valuetype = objectFactory.createValueType();
				valuetype.setAttributeID(respAttribute);
				valuetype.setContent(thisMap.get(respAttribute));
				metadata.getValueOrMultiValueOrValueGroup().add(valuetype);
			}
			classification.getNameOrAttributeLinkOrSequenceProduct().add(metadata);
			classifications.getClassification().add(classification);
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

	/**
	 * @param file
	 */
	public static void responsibilityMatrixProcessor(File file) {

		String logMessage = null;
		try {

			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_SPRINGBATCH_PUBLISH_ID_RESP);// FIXME
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();

			ehfICD.setAttributeTransactionType(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRANSACTION_TYPE_FILETOFILE);

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Started to read input file for responsibility matrix", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			List<Map<String, String>> listOfMaps = readIncomingFile(file);

			logger.info("Processing file : " + file.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH5,
					EHF_MSGTYPE_INFO_NONSLA, "Attribute values fetched from Galaxy xsv file and stored in map",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			if (listOfMaps != null && !listOfMaps.isEmpty()) {
				STEPProductInformation stepprd = buildXML(listOfMaps);

				logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
						EHF_MSGTYPE_INFO_NONSLA,
						"STEPProductInformation object for responsibility matrix update constructed for marshalling",
						EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
						ehfICD);
				ehfLogger.info(logMessage);

				File filetobewritten = DatamigrationCommonUtil.marshallObject(stepprd, file, RESPONSIBILITY_MATRIX_OUTPUT_FOLDER,
						ResponsibilityScheduler.PUBLISH_ID);

				logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
						EHF_MSGTYPE_INFO_NONSLA, "STEP xml generated for responsibility matrix update", EHF_SPRINGBATCH_ITEMUTILITY_USER,
						DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.info(logMessage);

				DatamigrationCommonUtil.sendFile(filetobewritten, file, RESPMATRIX_FILEDONE_FOLDER, "7", true,
						DatamigrationAppConstants.EHF_SPRINGBATCH_PUBLISH_ID_RESP);

				logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
						EHF_MSGTYPE_INFO_NONSLA, "STEP xml for responsibility matrix update moved to the SFTP location",
						EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
						ehfICD);
				ehfLogger.info(logMessage);

			} else {
				logger.info("No records in file");
				DatamigrationCommonUtil.printConsole("No records in file");
				DatamigrationCommonUtil.moveFileToFileBad(file, ResponsibilityScheduler.PUBLISH_ID);
				logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
						EHF_MSGTYPE_INFO_NONSLA, "No records in file", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil
								.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.info(logMessage);
			}

		} catch (IOException e) {
			logger.error("Exception in responsibility matrix processor: " + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ResponsibilityScheduler.PUBLISH_ID);

		} catch (ErrorHandlingFrameworkException e) {
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ResponsibilityScheduler.PUBLISH_ID);
		}
	}

	public static void main(String args[]) {

		responsibilityMatrixProcessor(new File(
				"C:/opt/stibo/integration/hotfolder/RespMatrixIncoming/File_Unprocessed/saravanan_2015_10_10_10_10_10_052675.xsv"));
	}
}