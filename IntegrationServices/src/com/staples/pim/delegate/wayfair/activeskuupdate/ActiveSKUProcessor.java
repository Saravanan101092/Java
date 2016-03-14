
package com.staples.pim.delegate.wayfair.activeskuupdate;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_COMPONENT_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ECO_SYSTEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ICD_CLASSNAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WAYFAIR_ACTIVESKU_OUTPUT_FOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

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
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wayfair.activeskuupdate.runner.ActiveSkuScheduler;

/**
 * @author 843868
 * 
 */
public class ActiveSKUProcessor {

	public static final String					WAYFAIR_ACTIVESKU_INSTANCENO			= "11_ACTIVESKU";

	public static final String					XML_EXTENSION							= ".xml";

	public static final String					UNDERSCORE								= "_";

	public static final String					WAYFAIR_ACTIVESKU_HEADER				= "wayfair.activesku.header";

	public static final String					WAYFAIR_ACTIVESKU_DELIMITER				= "wayfair.activesku.delimiter";

	public static final String					ITM_BUSINESSUNIT						= "ITM_BusinessUnit";

	public static final String					PRODUCTID_PREFIX						= "DMItem-";

	public static final String					VEN_ITEMNUMBER							= "VEN_ItemNmb";

	public static final String					ITM_ACTIVEURL							= "ITM_ActiveURL";

	public static final String					WAYFAIRITEMS							= "WayFairItems";

	public static final String					A1451_STR								= "A0569";

	public static final String					A0501_STR								= "A0501";

	public static final String					delimiter								= IntgSrvPropertiesReader
																								.getProperty(WAYFAIR_ACTIVESKU_DELIMITER);

	public static final String					headerString							= IntgSrvPropertiesReader
																								.getProperty(WAYFAIR_ACTIVESKU_HEADER);

	public static final String					ACTIVESKU_REFERENCE_FOLDER_CURRENT_PATH	= "/opt/stibo/SpringBatch/Reference/wayfairlookup/activesku/current";

	public static final String					ACTIVESKU_REFERENCE_FOLDER_OLD_PATH		= "/opt/stibo/SpringBatch/Reference/wayfairlookup/activesku/old";

	public static final String[]				headers									= headerString.split(delimiter, -1);

	public static final String					ACTIVESKU_FILEDONE_FOLDER				= "/opt/stibo/integration/hotfolder/WayfairIncoming/ActiveSKU/File_Done/";

	public static final String					ACTIVESKU_FILEUNPROCESSED_FOLDER		= "/opt/stibo/integration/hotfolder/WayfairIncoming/ActiveSKU/File_Unprocessed/";

	public static final String					FREEFORM_TRACELOGGER_ACTIVESKU			= "tracelogger.wayfairactivesku";

	public static final String					MAX_RECORDS_PER_FILE					= "wayfair.activesku.maxrecordsperfile";

	static IntgSrvLogger						logger									= IntgSrvLogger
																								.getInstance(FREEFORM_TRACELOGGER_ACTIVESKU);
	public static IntgSrvLogger					ehfLogger								= IntgSrvLogger
																								.getInstance(DatamigrationAppConstants.EHF_LOGGER_WAYFAIR);

	public static ErrorHandlingFrameworkICD		ehfICD									= null;

	public static ErrorHandlingFrameworkHandler	ehfHandler								= new ErrorHandlingFrameworkHandler();

	public static String						traceId									= null;

	String										logMessage								= null;

	public ObjectFactory						objectFactory;

	public static int							successCount;
	public static int							failureCount;
	public static int							TotalCount;
	public static long							starttime;
	public static int							newRecords;
	public static int							deletedRecords;
	
	/**
	 * Main method read hotfolder, sort and check for files
	 * 
	 * @throws ErrorHandlingFrameworkException
	 */
	public void wayfairActiveSKUProcessor() {

		try {
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(SPRINGBATCH_ECO_SYSTEM, SPRINGBATCH_COMPONENT_ID,
					SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ACTIVE_SKU);
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();

			File folder = new File(IntgSrvUtils.reformatFilePath(ACTIVESKU_FILEUNPROCESSED_FOLDER));
			File[] files = DatamigrationCommonUtil.sortFilesBasedOnFIFO(folder, ActiveSkuScheduler.PUBLISH_ID);
			if (files.length == 0) {
				logger.info("No files in hotfolder !");
			}
			logger.info("\tACTIVE SKU FEED REQUEST");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "ACTIVE SKU FEED request", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			for (int i = 0; i < files.length; i++) {
				// check if the file is an xsv or dsv file
				if ((files[i].getName().endsWith(DatamigrationAppConstants.XSV_EXTENSION))
						|| (files[i].getName().endsWith(DatamigrationAppConstants.DSV_EXTENSION))) {

					logger.info(new Date().toString() + " Processing file : " + files[i].getName());
					if (DatamigrationCommonUtil.doContainValidFile(files[i], headers, delimiter, logger)) {
						readIncomingFile(files[i]);
					} else {
						logger.error("File does not have any valid record.");
						DatamigrationCommonUtil.moveFileToFileBad(files[i], ActiveSkuScheduler.PUBLISH_ID);
					}
				} else {
					logger.error("Invalid file : " + files[i].getName());
				}
			}
		} catch (ErrorHandlingFrameworkException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Exception " + e.getMessage(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.error(logMessage);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ACTIVE_SKU);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Exception " + e.getMessage(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.error(logMessage);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ACTIVE_SKU);
		}
	}

	/**
	 * @param newFile
	 * @param referenceFile
	 * 
	 *            process delta feeds
	 */
	public void processDeltaFeed(File newFile, File referenceFile) {

		Set<String> oldFileRecords;
		Set<String> newFileRecords;
		Map<String, Set<String>> deltaRecords = null;

		// get delta feed by comparing input files new and old
		logger.info("generating delta feed from input files");
		logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
				EHF_MSGTYPE_INFO_NONSLA, "generating delta feed from input files", EHF_SPRINGBATCH_ITEMUTILITY_USER,
				DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.info(logMessage);

		deltaRecords = DatamigrationCommonUtil.getDeltaFeedFromNewAndOldFiles(newFile, referenceFile, logger);

		oldFileRecords = deltaRecords.get(DatamigrationAppConstants.WAYFAIR_REFERENCE_OLD);
		newFileRecords = deltaRecords.get(DatamigrationAppConstants.WAYFAIR_REFERENCE_CURRENT);

		// remove modified records
		oldFileRecords = removeModifiedRecordsFromDeletedRecordsActiveSKU(newFileRecords, oldFileRecords);

		logger.info("Delta generation complete. Deleted records=" + oldFileRecords.size() + " New records=" + newFileRecords.size());
		logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
				EHF_MSGTYPE_INFO_NONSLA, "Delta generation complete. Deleted records=" + oldFileRecords.size() + " New records="
						+ newFileRecords.size(), EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
				DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.info(logMessage);
		// get records in a map
		Map<String, List<Map<String, String>>> allRecords = DatamigrationCommonUtil.getSetAsMaps(newFileRecords, delimiter, headers,
				logger, newFile);

		// get deleted records as map
		Map<String, List<Map<String, String>>> deletedRecords = DatamigrationCommonUtil.getSetAsMaps(oldFileRecords, delimiter, headers,
				logger, newFile);

		// create STEPProductInformation object
		if ((!allRecords.get(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE).isEmpty())
				|| (!allRecords.get(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE).isEmpty())
				|| (!deletedRecords.get(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE).isEmpty())
				|| (!deletedRecords.get(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE).isEmpty())) {
			logger.info("creating STEP xml from delta NewRecords=" + allRecords.size() + " DeletedRecords=" + deletedRecords.size());

			STEPProductInformation stepPrdInfo = createDeltaSTEPProductInformation(allRecords, deletedRecords);

			// copy input file to reference folder for future reference and
			// delta creation
			DatamigrationCommonUtil.copyNewFileToReferenceFolder(newFile, ACTIVESKU_REFERENCE_FOLDER_CURRENT_PATH, logger);

			// marshall and send output file
			logger.info("generating and sending Step xml.");
			File outputFile = DatamigrationCommonUtil.marshallObject(stepPrdInfo, newFile, WAYFAIR_ACTIVESKU_OUTPUT_FOLDER,
					ActiveSkuScheduler.PUBLISH_ID);

			logger.info("Step xml generated " + outputFile.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "Output STEP xml file generated" + outputFile.getName(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			DatamigrationCommonUtil.sendFile(outputFile, newFile, ACTIVESKU_FILEDONE_FOLDER, WAYFAIR_ACTIVESKU_INSTANCENO, true,
					ActiveSkuScheduler.PUBLISH_ID);
			logger.info("Step xml sent to step hotfolder successfully");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "Step xml sent to step hotfolder successfully", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
		} else {
			logger.info("File comparison yielded no delta. No step xml for Activesku feed generated.");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "File comparison yielded no delta. No step xml for Activesku feed generated.",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			try {
				String filedonepath = newFile.getParentFile().getParentFile().toPath() + File.separator + "File_Done" + File.separator
						+ newFile.getName();
				File fileDoneFile = new File(IntgSrvUtils.reformatFilePath(filedonepath));
				Files.copy(newFile.toPath(), fileDoneFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				System.gc();
				newFile.delete();
			} catch (IOException e) {
				logger.error("Exception caught while moving file to fileDone folder.");
			}
		}
	}

	/**
	 * @param file
	 * 
	 *            read input files
	 */
	public void readIncomingFile(File file) {

		successCount = 0;
		failureCount = 0;
		TotalCount = 0;
		starttime = DatamigrationCommonUtil.getCurrentMilliSeconds();
		objectFactory = new ObjectFactory();
		Date startTime = new Date();
		// get reference file
		logger.info("getting reference file from reference folders.");
		File referenceFile = DatamigrationCommonUtil.getReferenceFile(ACTIVESKU_REFERENCE_FOLDER_CURRENT_PATH,
				ACTIVESKU_REFERENCE_FOLDER_OLD_PATH);

		// if reference file is not available, then feed is considered as
		// onetime
		if (referenceFile != null) {
			logger.info("Got the reference file : " + referenceFile.getName());
			processDeltaFeed(file, referenceFile);
		} else {
			logger.warn("Reference file could not be found.");
			processOneTimeFeed(file);
		}
		Date endDate = new Date();
		DatamigrationCommonUtil.generateExecutionSummaryForWayFair(file, referenceFile == null, delimiter, TotalCount, successCount,
				failureCount, newRecords, deletedRecords, startTime, endDate, "Active_SKU");
	}

	/**
	 * @param newFile
	 * @param oldFile
	 * @return
	 * 
	 *         remove modified records from deleted feed
	 */
	public Set<String> removeModifiedRecordsFromDeletedRecordsActiveSKU(Set<String> newFile, Set<String> oldFile) {

		String[] newFileValues;
		Map<String, String> newFileRecordAsMap;
		Set<String> modifiedRecords = new HashSet<String>();

		// parse through new file records
		for (String newFileRecord : newFile) {
			newFileValues = newFileRecord.split(delimiter, -1);
			newFileRecordAsMap = DatamigrationCommonUtil.getRecordAsMap(headers, newFileValues, logger);
			if (newFileRecordAsMap != null) {
				String ID = newFileRecordAsMap.get(VEN_ITEMNUMBER);
				for (String oldFileRecord : oldFile) {

					// if both lists contain same ID, then it is not deleted but
					// it is modified
					if (oldFileRecord.contains(ID)) {
						modifiedRecords.add(newFileRecord);
					}
				}
			}
		}
		logger.info("No of modified records=" + modifiedRecords.size());
		// remove modifed records from oldfile records
		oldFile.removeAll(modifiedRecords);
		return oldFile;
	}

	/**
	 * @param filebeingprocessed
	 * 
	 *            process one time feed
	 */
	public void processOneTimeFeed(File filebeingprocessed) {

		DatamigrationCommonUtil.printConsole("PROCESSING FILE.." + filebeingprocessed.getName());
		FileReader fileReader;
		String tempString;
		// int lineno=0;
		String[] values;
		int fileCount = 0;
		int maxRecordsPerFile = Integer.parseInt(IntgSrvPropertiesReader.getProperty(MAX_RECORDS_PER_FILE));
		List<Map<String, String>> quillRecordsList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dotcomRecordsList = new ArrayList<Map<String, String>>();
		Map<String, String> recordAsMap = null;

		try {
			fileReader = new FileReader(filebeingprocessed.getPath());
			BufferedReader br = new BufferedReader(fileReader);

			// parse line by line
			while ((tempString = br.readLine()) != null) {
				if (!tempString.equals(DatamigrationAppConstants.EMPTY_STR)) {

					// get line as a map with headers
					values = tempString.split(delimiter, -1);
					recordAsMap = DatamigrationCommonUtil.getRecordAsMap(headers, values, logger);
					logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
							EHF_MSGTYPE_INFO_NONSLA, "Attribute values fetched and stored in Map", EHF_SPRINGBATCH_ITEMUTILITY_USER,
							DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
					ehfLogger.info(logMessage);

					if (recordAsMap != null) {
						// add quill records and dotcom records separately
						if (recordAsMap.get(ITM_BUSINESSUNIT).equalsIgnoreCase(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE)) {
							quillRecordsList.add(recordAsMap);
						} else if (recordAsMap.get(ITM_BUSINESSUNIT).equalsIgnoreCase(
								DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE)) {
							dotcomRecordsList.add(recordAsMap);
						}
						successCount++;
					} else {
						failureCount++;
						logger.error("Record omitted. Incorrect no of values :" + tempString);
						DatamigrationCommonUtil.appendWriterFile(filebeingprocessed.getParentFile().getParentFile() + "/Report/Report_"
								+ filebeingprocessed.getName(), tempString + "~Has incorrect number of Values in record :" + values.length);
					}
					// each file shall only contain 50000 records
					if ((quillRecordsList.size() + dotcomRecordsList.size()) >= maxRecordsPerFile) {
						fileCount++;
						logger.info("Records limit per file reached. Creating Step xml no:" + fileCount);
						logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
								EHF_MSGTYPE_INFO_NONSLA, "Records limit per file reached. Creating Step xml no:" + fileCount,
								EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
								DatamigrationCommonUtil.getClassName(), ehfICD);
						ehfLogger.info(logMessage);
						// create STEP xml and transfer
						createAndSendSTEPXml(quillRecordsList, dotcomRecordsList, filebeingprocessed, fileCount, false);
						quillRecordsList.clear();
						dotcomRecordsList.clear();
					}
				}
				TotalCount++;
				// lineno++;
				// DatamigrationCommonUtil.printConsole(lineno);
			}
			br.close();
			fileReader.close();

			// copy file to reference folder for future reference
			DatamigrationCommonUtil.copyNewFileToReferenceFolder(filebeingprocessed, ACTIVESKU_REFERENCE_FOLDER_CURRENT_PATH, logger);

			if ((quillRecordsList.size() + dotcomRecordsList.size()) > 0) {
				fileCount++;
				// create STEP xml and transfer
				logger.info("creating final STEP xml");
				logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
						EHF_MSGTYPE_INFO_NONSLA, "creating final STEP xml:" + fileCount, EHF_SPRINGBATCH_ITEMUTILITY_USER,
						DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.info(logMessage);
				createAndSendSTEPXml(quillRecordsList, dotcomRecordsList, filebeingprocessed, fileCount, true);
			} else if ((quillRecordsList.size() + dotcomRecordsList.size()) == 0) {

				logger.info("No records generated from the file :" + filebeingprocessed.getName() + " moving to file bad folder");
				DatamigrationCommonUtil.moveFileToFileBad(filebeingprocessed, ActiveSkuScheduler.PUBLISH_ID);
			}

			DatamigrationCommonUtil.printConsole("All records processed..");
			long endTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
			generateExecutionSummary("Active sku Feed ", DatamigrationCommonUtil.getReportFolderPath(filebeingprocessed),
					filebeingprocessed.getName(), successCount, (endTime - starttime), failureCount, TotalCount);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception while processing file : " + filebeingprocessed.getName() + " : " + e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "Exception while processing file : " + filebeingprocessed.getName() + " : " + e.getMessage(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ACTIVE_SKU);
		}
	}

	/**
	 * @param quillRecords
	 * @param dotcomRecords
	 * @param filebeingProcessed
	 * @param fileCount
	 * @param isFileMoveActive
	 * 
	 *            create and transfer STEP xml
	 */
	public void createAndSendSTEPXml(List<Map<String, String>> quillRecords, List<Map<String, String>> dotcomRecords,
			File filebeingProcessed, int fileCount, boolean isFileMoveActive) {

		// create STEPProductInformation Object
		STEPProductInformation stepPrdInfo = createOnetimeSTEPProductInformation(quillRecords, dotcomRecords);

		// marshall object
		String filename = filebeingProcessed.getName();
		filename = filename.substring(0, filename.length() - 4) + UNDERSCORE + fileCount + XML_EXTENSION;
		File outputFile = DatamigrationCommonUtil.marshallObject(stepPrdInfo, new File(filename), WAYFAIR_ACTIVESKU_OUTPUT_FOLDER,
				ActiveSkuScheduler.PUBLISH_ID);

		// transfer file to STEP hotfolder
		logger.info("Sending file to SFTP");
		DatamigrationCommonUtil.sendFile(outputFile, filebeingProcessed, ACTIVESKU_FILEDONE_FOLDER, WAYFAIR_ACTIVESKU_INSTANCENO,
				isFileMoveActive, ActiveSkuScheduler.PUBLISH_ID);
	}

	/**
	 * @param quillRecords
	 * @param dotcomRecords
	 * @return
	 * 
	 *         create STEPProductInformation object for onetimefeed
	 */
	public STEPProductInformation createOnetimeSTEPProductInformation(List<Map<String, String>> quillRecords,
			List<Map<String, String>> dotcomRecords) {

		STEPProductInformation stepPrdInfo = objectFactory.createSTEPProductInformation();
		ProductsType products = objectFactory.createProductsType();

		// add quill products
		for (Map<String, String> thisRecord : quillRecords) {
			ProductType product = objectFactory.createProductType();
			product.setID(PRODUCTID_PREFIX + thisRecord.get(VEN_ITEMNUMBER));
			product.setUserTypeID(DatamigrationAppConstants.ITEM);
			product.setParentID(WAYFAIRITEMS);
			product.setSelected(false);
			product.setReferenced(true);

			// Commented based on krishna request
			// NameType name = objectFactory.createNameType();
			// name.setContent(PRODUCTID_PREFIX+thisRecord.get(VEN_ITEMNUMBER));
			// product.getName().add(name);

			ValuesType values = objectFactory.createValuesType();
			ValueType value = objectFactory.createValueType();
			value.setAttributeID(A1451_STR);
			value.setContent(DatamigrationAppConstants.Y_STR);
			values.getValueOrMultiValueOrValueGroup().add(value);
			product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
			products.getProduct().add(product);
		}

		// add dotcom products
		for (Map<String, String> thisRecord : dotcomRecords) {
			ProductType product = objectFactory.createProductType();
			product.setID(PRODUCTID_PREFIX + thisRecord.get(VEN_ITEMNUMBER));
			product.setUserTypeID(DatamigrationAppConstants.ITEM);
			product.setParentID(WAYFAIRITEMS);
			product.setSelected(false);
			product.setReferenced(true);

			// Commented based on krishna request
			// NameType name = objectFactory.createNameType();
			// name.setContent(PRODUCTID_PREFIX+thisRecord.get(VEN_ITEMNUMBER));
			// product.getName().add(name);
			ValuesType values = objectFactory.createValuesType();
			ValueType value = objectFactory.createValueType();
			value.setAttributeID(A0501_STR);
			value.setContent(DatamigrationAppConstants.Y_STR);
			values.getValueOrMultiValueOrValueGroup().add(value);
			product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
			products.getProduct().add(product);
		}

		stepPrdInfo.setProducts(products);
		stepPrdInfo.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepPrdInfo.setContextID(CONTEXT_ID_VALUE); // Hard coded value
		stepPrdInfo.setExportContext(EXPORT_CONTEXT_VALUE);// Hard coded value
		stepPrdInfo.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded value
		return stepPrdInfo;

	}

	/**
	 * @param newRecords
	 * @param deletedRecords
	 * @return
	 * 
	 *         create STEPProductInformation object for delta feed
	 */
	public STEPProductInformation createDeltaSTEPProductInformation(Map<String, List<Map<String, String>>> newRecords,
			Map<String, List<Map<String, String>>> deletedRecords) {

		STEPProductInformation stepPrdInfo = objectFactory.createSTEPProductInformation();
		ProductsType products = objectFactory.createProductsType();

		// Add new Products
		// add quill products
		for (Map<String, String> thisRecord : newRecords.get(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE)) {
			ProductType product = objectFactory.createProductType();
			product.setID(PRODUCTID_PREFIX + thisRecord.get(VEN_ITEMNUMBER));
			product.setUserTypeID(DatamigrationAppConstants.ITEM);
			product.setParentID(WAYFAIRITEMS);
			product.setSelected(false);
			product.setReferenced(true);

			// Commented based on krishna request
			// NameType name = objectFactory.createNameType();
			// name.setContent(PRODUCTID_PREFIX+thisRecord.get(VEN_ITEMNUMBER));
			// product.getName().add(name);

			ValuesType values = objectFactory.createValuesType();
			ValueType value = objectFactory.createValueType();
			value.setAttributeID(A1451_STR);
			value.setContent(DatamigrationAppConstants.Y_STR);
			values.getValueOrMultiValueOrValueGroup().add(value);
			product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
			products.getProduct().add(product);
		}

		// add dotcom products
		for (Map<String, String> thisRecord : newRecords.get(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE)) {
			ProductType product = objectFactory.createProductType();
			product.setID(PRODUCTID_PREFIX + thisRecord.get(VEN_ITEMNUMBER));
			product.setUserTypeID(DatamigrationAppConstants.ITEM);
			product.setParentID(WAYFAIRITEMS);
			product.setSelected(false);
			product.setReferenced(true);

			// Commented based on krishna request
			// NameType name = objectFactory.createNameType();
			// name.setContent(PRODUCTID_PREFIX+thisRecord.get(VEN_ITEMNUMBER));
			// product.getName().add(name);

			ValuesType values = objectFactory.createValuesType();
			ValueType value = objectFactory.createValueType();
			value.setAttributeID(A0501_STR);
			value.setContent(DatamigrationAppConstants.Y_STR);
			values.getValueOrMultiValueOrValueGroup().add(value);
			product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
			products.getProduct().add(product);
		}

		// Add deleted quill records
		for (Map<String, String> thisRecord : deletedRecords.get(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE)) {
			ProductType product = objectFactory.createProductType();
			product.setID(PRODUCTID_PREFIX + thisRecord.get(VEN_ITEMNUMBER));
			product.setUserTypeID(DatamigrationAppConstants.ITEM);
			product.setParentID(WAYFAIRITEMS);
			product.setSelected(false);
			product.setReferenced(true);
			NameType name = objectFactory.createNameType();
			name.setContent(PRODUCTID_PREFIX + thisRecord.get(VEN_ITEMNUMBER));
			product.getName().add(name);
			ValuesType values = objectFactory.createValuesType();
			ValueType value = objectFactory.createValueType();
			value.setAttributeID(A1451_STR);
			value.setContent(DatamigrationAppConstants.N_STR);
			values.getValueOrMultiValueOrValueGroup().add(value);
			product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
			products.getProduct().add(product);
		}

		// add deleted dotcom products
		for (Map<String, String> thisRecord : deletedRecords.get(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE)) {
			ProductType product = objectFactory.createProductType();
			product.setID(PRODUCTID_PREFIX + thisRecord.get(VEN_ITEMNUMBER));
			product.setUserTypeID(DatamigrationAppConstants.ITEM);
			product.setParentID(WAYFAIRITEMS);
			product.setSelected(false);
			product.setReferenced(true);
			NameType name = objectFactory.createNameType();
			name.setContent(PRODUCTID_PREFIX + thisRecord.get(VEN_ITEMNUMBER));
			product.getName().add(name);
			ValuesType values = objectFactory.createValuesType();
			ValueType value = objectFactory.createValueType();
			value.setAttributeID(A0501_STR);
			value.setContent(DatamigrationAppConstants.N_STR);
			values.getValueOrMultiValueOrValueGroup().add(value);
			product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
			products.getProduct().add(product);
		}

		stepPrdInfo.setProducts(products);
		stepPrdInfo.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepPrdInfo.setContextID(CONTEXT_ID_VALUE); // Hard coded value
		stepPrdInfo.setExportContext(EXPORT_CONTEXT_VALUE);// Hard coded value
		stepPrdInfo.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded value
		return stepPrdInfo;

	}

	public void generateExecutionSummary(String inputFeedType, String reportPath, String fileName, int successCount, long milliseconds,
			int failureCount, int totalCount) throws IOException, MessagingException {

		File[] attachmentFiles = new File[2];
		String issueSummary = "";

		int totalErrorsCount = 0;
		File errorReportSummary = new File(reportPath + "Execution_Summary.txt");

		if (!errorReportSummary.getParentFile().exists()) {
			errorReportSummary.getParentFile().mkdirs();
		}

		FileWriter errorReportSummaryWriter = new FileWriter(errorReportSummary, true);
		errorReportSummaryWriter.write("Input file Name: " + fileName);
		errorReportSummaryWriter.write("\nExecution Date: " + DatamigrationCommonUtil.getCurrentDateForSTEP());
		errorReportSummaryWriter.write("\n");

		DatamigrationCommonUtil.printConsole("Success records count : " + successCount);
		DatamigrationCommonUtil.printConsole("Failure records count : " + failureCount);
		DatamigrationCommonUtil.printConsole("Turn around (in ms): " + milliseconds);

		errorReportSummaryWriter.write("Total records count : " + totalCount + "\n");
		errorReportSummaryWriter.write("Success records count : " + successCount + "\n");
		errorReportSummaryWriter.write("Failure records count : " + failureCount + "\n");
		
		errorReportSummaryWriter.write("Turn around time(in ms): " + milliseconds + "\n\n");
		errorReportSummaryWriter.close();

		String actualMsg = DatamigrationCommonUtil.constructEmailBody(inputFeedType, fileName, successCount, totalErrorsCount,
				issueSummary, false, milliseconds);

		DatamigrationCommonUtil.sendEmail(inputFeedType + "Migration Status", actualMsg,
				IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.TO_ADDRESS), attachmentFiles, false, true);

	}

	public static void main(String[] args) {

		ActiveSKUProcessor activeskuprocessor = new ActiveSKUProcessor();
		activeskuprocessor.wayfairActiveSKUProcessor();
	}

}
