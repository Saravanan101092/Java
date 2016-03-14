
package com.staples.pim.delegate.wayfair.taxonomyupdate;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.REFERENCE_FILE_DELIMITER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_COMPONENT_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ECO_SYSTEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ICD_CLASSNAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WAYFAIR_TAXONOMY_OUTPUT_FOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
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
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wayfair.taxonomyupdate.runner.TaxonomyScheduler;

/**
 * @author 843868
 * 
 */
public class TaxonomyProcessor {

	public static final String					TAXONOMY_INSTANCENO						= "11_TAXONOMY";

	public static final String					CATEGORY_ID_STR							= "SnQCategoryID";

	public static final String					QUILLWEBHIERARCHY						= "QuillWebHierarchy";

	public static final String					QUILL_WEB_HIERARCHY						= "Quill Web Hierarchy";

	public static final String					STAPLES_DOTCOM_HOMECAT					= "StaplesDotComHomeCat-1";

	public static final String					QUILL_WEB_HOME_CATEGORY_1				= "QuillWebHomeCategory-1";

	public static final String					CLASSIFICATION_USERTYPE_ROOT			= "Classification 1 user-type root";

	public static final String					QUILL_WEB_HOME_CATEGORY					= "QuillWebHomeCategory";

	public static final String					QUILLWEBROOT							= "QuillWebRoot";

	public static final String					CLASSIFICATION_1_ROOT					= "Classification 1 root";

	public static final String					QUILL_SUPER_CATEGORY					= "QuillSuperCategory";

	public static final String					QUILL_WEB_CATEGORY						= "QuillWebCategory";

	public static final String					QUILL_DOTCOM_DEPARTMENT					= "QuillWebDepartment";

	public static final String					PRODUCTS_STR							= "Products";

	public static final String					QUILL_WEB_CLASS							= "QuillWebClass";

	public static final String					CLASSIDS_FILENAME						= "classIds.xsv";

	public static final String					UNIQUE_CLASSID							= "UniqueClass_ID";

	public static final String					UNIQUE_DEPTD							= "UniqueDept_ID";

	public static final String					UNIQUE_CATEGORYID						= "UniqueCategory_ID";

	public static final String					WEB_HIERARCHY_ROOT						= "WebHierarchyRoot";

	public static final String					WEB_SITES								= "Web Sites";

	public static final String					STAPLES_DOTCOM_WEBHIERARCHY				= "StaplesDotComWebHierarchy";

	public static final String					STAPLES_DOTCOM_WEBROOT					= "StaplesDotComWebRoot";

	public static final String					STAPLES_DOTCOM_WEBHIERARCHY_NAME		= "Staples Dot Com Web Hierarchy";

	public static final String					STAPLES_DOTCOM_WEBHOME_CATEGORY			= "StaplesDotComWebHomeCategory";

	public static final String					STAPLES_DOTCOM_WEBSUPERCATEGORY			= "StaplesDotComWebSuperCategory";

	public static final String					STAPLES_DOTCOM_CATEGORY					= "StaplesDotComWebCategory";

	public static final String					STAPLES_DOTCOM_DEPARTMENT				= "StaplesDotComWebDepartment";

	public static final String					STAPLES_DOTCOM_CLASS					= "StaplesDotComWebClass";

	public static final String					SUPERCATEGORY_ID						= "ITM_SuperCategoryID";

	public static final String					SUPERCATEGORY							= "ITM_SuperCategoryName";

	public static final String					CATEGORY_ID								= "ITM_CategoryID";

	public static final String					CATEGORY								= "ITM_Category";

	public static final String					DEPARTMENT_ID							= "ITM_DepartmentID";

	public static final String					DEPARTMENT								= "ITM_DeptName";

	public static final String					CLASS_ID								= "ITM_ClassID";

	public static final String					CLASS									= "ITM_ClassName";

	public static final String					STAPLES_SUPERCATEGORYID_PREFIX			= "StaplesDotComSuperCat-";

	public static final String					STAPLES_CATEGORYID_PREFIX				= "StaplesDotComCat-";

	public static final String					STAPLES_DEPARTMENTID_PREFIX				= "StaplesDotComDept-";

	public static final String					STAPLES_CLASSID_PREFIX					= "StaplesDotComClass-";

	public static final String					QUILL_SUPERCATEGORYID_PREFIX			= "QULWebSuperCategory-";

	public static final String					QUILL_CATEGORYID_PREFIX					= "QULWebCategory-";

	public static final String					QUILL_DEPARTMENTID_PREFIX				= "QULWebDepartment-";

	public static final String					QUILL_CLASSID_PREFIX					= "QULWebClass-";

	public static final String					HIERARCHY_CODE							= "ITM_BusinessUnit";

	public static final String					TAXONOMY_DELETEDRECORDS_EMAIL_SUBJECT	= "Wayfair-Taxonomy deleted records";

	public static final String					TAXONOMY_FILEUNPROCESSED_FOLDER			= "/opt/stibo/integration/hotfolder/WayfairIncoming/Taxonomy/File_Unprocessed/";

	public static final String					OLD_REFERENCE_FILE_FOLDER				= "/opt/stibo/SpringBatch/Reference/wayfairlookup/taxonomy/old";

	public static final String					TAXONOMY_FILEDONE_FOLDER				= "/opt/stibo/integration/hotfolder/WayfairIncoming/Taxonomy/File_Done/";

	public static final String					TAXONOMY_FILEDONE_FOLDER_OT				= "/opt/stibo/integration/hotfolder/WayfairIncoming/OTTaxonomy/File_Done/";

	public static final String					REFERENCE_FOLDER_CURRENT_PATH			= "/opt/stibo/SpringBatch/Reference/wayfairlookup/taxonomy/current";

	public static final String					REFERENCE_FOLDER_OLD_PATH				= "/opt/stibo/SpringBatch/Reference/wayfairlookup/taxonomy/old";

	public static final String					TAXONOMY_MAIL_RECEIPENTS				= "wayfair.taxonomy.mail.toaddresses";

	public static final String					TAXONOMY_LOOKUP_FOLDER_PRODUCT_CURRENT	= "/opt/stibo/SpringBatch/Reference/wayfairlookup/product/current/";

	public static final String					TAXONOMY_FILEUNPROCESSED_FOLDER_OT		= "/opt/stibo/integration/hotfolder/WayfairIncoming/OTTaxonomy/File_Unprocessed/";

	public static final String					WAYFAIR_TAXONOMY_DELIMITER				= "wayfair.taxonomy.delimiter";

	public static final String					WAYFAIR_TAXONOMY_HEADERS				= "wayfair.taxonomy.headers";

	public static final String					WAYFAIR_TAXONOMY_HEADERS_CI				= "wayfair.taxonomy.headers.continuous.integration";

	public static final String					WAYFAIR_TAXONOMY_HEADERS_OT				= "";

	public static String						headerString;																												// =
																																											// IntgSrvPropertiesReader.getProperty(WAYFAIR_TAXONOMY_HEADERS);

	public static final String					headerdelimiter							= IntgSrvPropertiesReader
																								.getProperty(WAYFAIR_TAXONOMY_DELIMITER);

	public static String						delimiter;

	public static final String					ONETIME_DELIMITER						= IntgSrvPropertiesReader
																								.getProperty("wayfair.onetime.delimiter");

	public static String[]						headers;

	public static final String					FREEFORM_TRACELOGGER_TAXONOMY			= "tracelogger.wayfairtaxonomy";

	static IntgSrvLogger						logger									= IntgSrvLogger
																								.getInstance(FREEFORM_TRACELOGGER_TAXONOMY);

	public static IntgSrvLogger					ehfLogger								= IntgSrvLogger
																								.getInstance(DatamigrationAppConstants.EHF_LOGGER_WAYFAIR);

	public static ErrorHandlingFrameworkICD		ehfICD									= null;

	public static ErrorHandlingFrameworkHandler	ehfHandler								= new ErrorHandlingFrameworkHandler();

	public static String						traceId									= null;

	String										logMessage								= null;

	public ObjectFactory						objectFactory;

	public static boolean						isOneTime								= false;

	public static final String					ONETIME_ENCODING						= "wayfair.onetime.encoding";

	public static final String					ENCODING								= IntgSrvPropertiesReader
																								.getProperty(ONETIME_ENCODING);

	public static final String					TAXONOMY_REPORTSFILENAME				= "TaxonomyExecutionReports.txt";

	public static int							noOfRecords;
	public static int							succeededRecords;
	public static int							failedRecords;
	public static int							newRecords;
	public static int							deletedRecords;

	/**
	 * main method
	 */
	public void wayfairTaxonomyInboundProcessor() {

		try {

			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(SPRINGBATCH_ECO_SYSTEM, SPRINGBATCH_COMPONENT_ID,
					SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_IMAGETAXONOMY);
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();
			// check input hotfolder for new files. process one by one
			File CIFolder = new File(IntgSrvUtils.reformatFilePath(TAXONOMY_FILEUNPROCESSED_FOLDER));
			File onetimeFolder = new File(IntgSrvUtils.reformatFilePath(TAXONOMY_FILEUNPROCESSED_FOLDER_OT));
			File folder = null;

			if (DatamigrationCommonUtil.isFolderContainsFile(onetimeFolder)) {

				logger.info("ONETIME INTEGRATION FILES");
				folder = onetimeFolder;
				delimiter = ONETIME_DELIMITER;
				headerString = IntgSrvPropertiesReader.getProperty(WAYFAIR_TAXONOMY_HEADERS);
				headers = headerString.split(headerdelimiter, -1);
				isOneTime = true;
			} else if (DatamigrationCommonUtil.isFolderContainsFile(CIFolder)) {

				logger.info("CONTINUOUS INTEGRATION FILES");
				folder = CIFolder;
				delimiter = headerdelimiter;
				headerString = IntgSrvPropertiesReader.getProperty(WAYFAIR_TAXONOMY_HEADERS_CI);
				headers = headerString.split(delimiter, -1);
				isOneTime = false;
			}

			DatamigrationCommonUtil.printConsole("Taxonomy  >>Processing isOneTime: " + isOneTime + " Delimiter: " + delimiter);

			if (folder != null) {
				File[] files = DatamigrationCommonUtil.sortFilesBasedOnFIFO(folder, TaxonomyScheduler.PUBLISH_ID);
				if(files.length==0){
					logger.info("No files in hotfolder to process.");
				}
				for (int i = 0; i < files.length; i++) {

					// check if the file is an xsv or dsv file
					if ((files[i].getName().endsWith(DatamigrationAppConstants.XSV_EXTENSION))
							|| (files[i].getName().endsWith(DatamigrationAppConstants.DSV_EXTENSION))) {
						logger.info(new Date().toString() + "Processing file : " + files[i].getName());
						DatamigrationCommonUtil.printConsole("Taxonomy  >>Processing File : " + files[i].getName());

						if(DatamigrationCommonUtil.doContainValidFile(files[i],headers,delimiter,logger)){
							readIncomingFile(files[i]);
						}else{
							logger.error("File does not have any valid record.");
							DatamigrationCommonUtil.moveFileToFileBad(files[i], TaxonomyScheduler.PUBLISH_ID);
						}

					} else {
						logger.error("Invalid file : " + files[i].getName());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in wayfairTaxonomy : " + e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Exception " + e.getMessage(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.error(logMessage);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_IMAGETAXONOMY);
		} catch (ErrorHandlingFrameworkException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Exception " + e.getMessage(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.error(logMessage);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_IMAGETAXONOMY);
		}

	}

	/**
	 * @param newFile
	 * @param oldFile
	 * @return
	 * 
	 *         Modified records are present in both newfile and oldfile. They
	 *         are removed from the old file delta records to get only the
	 *         deleted feeds
	 */
	public Set<String> removeModifiedRecordsFromDeletedRecords(Set<String> newFile, Set<String> oldFile) {

		String[] newFileValues;
		String[] oldFileValues;
		Map<String, String> newFileRecordAsMap;
		Map<String, String> oldFileRecordAsMap;
		Set<String> modifiedRecords = new HashSet<String>();

		for (String newFileRecord : newFile) {

			newFileValues = newFileRecord.split(delimiter, -1);
			newFileRecordAsMap = DatamigrationCommonUtil.getRecordAsMap(headers, newFileValues, logger);
			for (String oldFileRecord : oldFile) {
				oldFileValues = oldFileRecord.split(delimiter, -1);
				oldFileRecordAsMap = DatamigrationCommonUtil.getRecordAsMap(headers, oldFileValues, logger);

				if ((newFileRecordAsMap != null) && (oldFileRecordAsMap != null)) {
					// concatenate all IDs to get check for changes in IDs
					String oldFileIDs = oldFileRecordAsMap.get(SUPERCATEGORY_ID) + oldFileRecordAsMap.get(CATEGORY_ID)
							+ oldFileRecordAsMap.get(DEPARTMENT_ID) + oldFileRecordAsMap.get(CLASS_ID);
					String newFileIDs = newFileRecordAsMap.get(SUPERCATEGORY_ID) + newFileRecordAsMap.get(CATEGORY_ID)
							+ newFileRecordAsMap.get(DEPARTMENT_ID) + newFileRecordAsMap.get(CLASS_ID);

					// If IDs are same then, It is concluded that the record is
					// not new and it is modified
					if (oldFileIDs.equalsIgnoreCase(newFileIDs)) {
						modifiedRecords.add(oldFileRecord);
					}
				}
			}
		}

		// remove modified records from old file
		logger.info("removing modified records : " + modifiedRecords.size());
		oldFile.removeAll(modifiedRecords);
		return oldFile;
	}

	/**
	 * @param newFile
	 * @param oldFile
	 * @return
	 * 
	 *         get Delta feed and create Stepxml with the delta feed
	 */
	public STEPProductInformation processDeltaFeed(File newFile, File oldFile) {

		Set<String> oldFileRecords;
		Set<String> newFileRecords;
		Map<String, Set<String>> deltaRecords;

		// Get delta records
		deltaRecords = DatamigrationCommonUtil.getDeltaFeedFromNewAndOldFiles(newFile, oldFile, logger);

		oldFileRecords = deltaRecords.get(DatamigrationAppConstants.WAYFAIR_REFERENCE_OLD);
		newFileRecords = deltaRecords.get(DatamigrationAppConstants.WAYFAIR_REFERENCE_CURRENT);
		newRecords = newFileRecords.size();

		// remove modified records
		oldFileRecords = removeModifiedRecordsFromDeletedRecords(newFileRecords, oldFileRecords);
		deletedRecords = oldFileRecords.size();

		logger.info("Delta generated. No of deleted records: " + oldFileRecords.size() + " No of new records : " + newFileRecords.size());
		// add deleted records to generat xml
		newFileRecords.addAll(oldFileRecords);

		// get records in a map
		Map<String, List<Map<String, String>>> allRecords = DatamigrationCommonUtil
				.getSetAsMaps(newFileRecords, delimiter, headers, logger, newFile);

		//Write deleted records into a file
		for(String deletedRecord : oldFileRecords){
			DatamigrationCommonUtil.appendWriterFile(newFile.getParentFile().getParentFile().getPath()+File.separator+"Report"+File.separator+"DeletedRecords_"+newFile.getName(), deletedRecord);
		}
		
		// Send mail for the deleted records
		if (!oldFileRecords.isEmpty()) {
			logger.info("sending mail for deleted records");
			DatamigrationCommonUtil.sendEmailForDeletedRecords(oldFileRecords, TAXONOMY_MAIL_RECEIPENTS,
					TAXONOMY_DELETEDRECORDS_EMAIL_SUBJECT);
		}

		// create STEPProductInformation Object
		if ((allRecords.get(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE).size() + allRecords.get(
				DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE).size()) > 0) {
			logger.info("creating step xml");
			STEPProductInformation stepprdInfo = createSTEPProductInformationObject(
					allRecords.get(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE),
					allRecords.get(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE));
			return stepprdInfo;

		} else {
			logger.info("File comparison yielded no delta. Input and output files are identical.");
			try {
				String filedonepath = newFile.getParentFile().getParentFile().toPath()+File.separator+"File_Done"+File.separator+newFile.getName();
				File fileDoneFile = new File(filedonepath);
				Files.copy(newFile.toPath(), fileDoneFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				System.gc();
				newFile.delete();
			} catch (IOException e) {
				logger.error("Exception caught while moving file to fileDone folder.");
			}
			return null;
		}
	}

	/**
	 * @param file
	 * 
	 *            process the incoming file
	 */
	public void readIncomingFile(File file) {

		noOfRecords = 0;
		succeededRecords = 0;
		failedRecords = 0;
		newRecords = 0;
		deletedRecords = 0;

		Date startTime = new Date();
		STEPProductInformation stepPrdInfo = null;

		if (isOneTime) {
			// processing one time feed
			stepPrdInfo = processOneTimeFeed(file);
		} else {

			// processings delta feed
			// get comparision file
			logger.info("getting reference file.");
			File referenceFile = DatamigrationCommonUtil.getReferenceFile(REFERENCE_FOLDER_CURRENT_PATH, REFERENCE_FOLDER_OLD_PATH);
			if (referenceFile != null) {
				logger.info("found reference file. Processing delta feed.");
				stepPrdInfo = processDeltaFeed(file, referenceFile);
			} else {
				logger.error("REFERENCE FILE NOT FOUND. Not able to generate delta.");
			}
		}

		if (stepPrdInfo != null) {

			// marshal object
			File outputFile = DatamigrationCommonUtil.marshallObject(stepPrdInfo, file, WAYFAIR_TAXONOMY_OUTPUT_FOLDER,
					TaxonomyScheduler.PUBLISH_ID);

			if (!isOneTime) {
				// copy file for future reference and delta generation
				DatamigrationCommonUtil.copyNewFileToReferenceFolder(file, REFERENCE_FOLDER_CURRENT_PATH, logger);
			} else {
				// copy file for future reference
				createOneTimeReferenceFile(file);
			}

			// write classIDs for reference in the Product feed
			logger.info("creating reference file with class IDs for Product feed");
			createTaxonomyReferenceFileForProduct(file);

			if (isOneTime) {
				// Send file through SFTP
				DatamigrationCommonUtil.sendFile(outputFile, file, TAXONOMY_FILEDONE_FOLDER_OT, TAXONOMY_INSTANCENO, true,
						TaxonomyScheduler.PUBLISH_ID);
				logger.info("output file successfully sent to sftp location");
			} else {
				// Send file through SFTP
				DatamigrationCommonUtil.sendFile(outputFile, file, TAXONOMY_FILEDONE_FOLDER, TAXONOMY_INSTANCENO, true,
						TaxonomyScheduler.PUBLISH_ID);
				logger.info("output file successfully sent to sftp location");
			}
		}
		Date endDate = new Date();
		DatamigrationCommonUtil.generateExecutionSummaryForWayFair(file, isOneTime, delimiter, noOfRecords, succeededRecords, failedRecords, newRecords, deletedRecords,
				startTime, endDate,"Taxonomy");
	}

	public void createOneTimeReferenceFile(File file) {

		logger.info("copying input file to reference folder");

		// Get the current and old folders that contain the reference files
		File currentFolder = new File(IntgSrvUtils.reformatFilePath(REFERENCE_FOLDER_CURRENT_PATH) + File.separator);
		File oldFolder = new File(currentFolder.getParentFile().getPath() + File.separator
				+ DatamigrationAppConstants.WAYFAIR_REFERENCE_OLD + File.separator);

		try {

			// delete the files in reference Old folder
			File[] fileInOldFolder = oldFolder.listFiles();
			if (fileInOldFolder != null) {
				for (int i = 0; i < fileInOldFolder.length; i++) {
					fileInOldFolder[i].delete();
				}
			}

			// move the files from current to old
			File[] filesInCurrentFolder = currentFolder.listFiles();
			if (filesInCurrentFolder != null) {
				DatamigrationCommonUtil.makeDirs(oldFolder, Boolean.FALSE);
				for (int i = 0; i < filesInCurrentFolder.length; i++) {
					Files.copy(filesInCurrentFolder[i].toPath(),
							new File(oldFolder.getPath() + File.separator + filesInCurrentFolder[i].getName()).toPath(),
							StandardCopyOption.REPLACE_EXISTING);
					filesInCurrentFolder[i].delete();
				}
			}

			// write the new file to current folder
			File referenceFileToBeWritten = new File(currentFolder.getPath() + File.separator + file.getName());
			DatamigrationCommonUtil.makeDirs(referenceFileToBeWritten, Boolean.TRUE);
			FileWriter referenceWriter = new FileWriter(referenceFileToBeWritten, true);
			FileInputStream inputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader;
			
			if (!DatamigrationCommonUtil.isPropertyValueNULL(ENCODING)) {
				inputStreamReader = new InputStreamReader(inputStream, ENCODING);
			} else {
				inputStreamReader = new InputStreamReader(inputStream);
			}
			BufferedReader br = new BufferedReader(inputStreamReader);
			String tempString;
			String[] values;
			Map<String, String> recordAsMap;

			while ((tempString = br.readLine()) != null) {
				if (!tempString.equals(DatamigrationAppConstants.EMPTY_STR)) {
					values = tempString.split(delimiter, -1);
					recordAsMap = DatamigrationCommonUtil.getRecordAsMap(headers, values, logger);
					if (recordAsMap != null) {
						referenceWriter.write(recordAsMap.get(SUPERCATEGORY) + headerdelimiter + recordAsMap.get(CATEGORY)
								+ headerdelimiter + recordAsMap.get(DEPARTMENT) + headerdelimiter + recordAsMap.get(CLASS)
								+ headerdelimiter + "" + headerdelimiter + recordAsMap.get(SUPERCATEGORY_ID) + headerdelimiter
								+ recordAsMap.get(CATEGORY_ID) + headerdelimiter + recordAsMap.get(DEPARTMENT_ID) + headerdelimiter
								+ recordAsMap.get(CLASS_ID) + headerdelimiter + recordAsMap.get(HIERARCHY_CODE)
								+ System.getProperty("line.separator"));
					}
				}
			}
			referenceWriter.close();
			br.close();
			inputStreamReader.close();
			inputStream.close();

		} catch (IOException e) {
			logger.error("Exception while copying file to reference folder :" + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_IMAGETAXONOMY);
		}
		logger.info("created reference file successfully");

	}

	/**
	 * @param file
	 * 
	 *            write the classID and Class Name for product feed reference
	 */
	public void createTaxonomyReferenceFileForProduct(File file) {

		// get the reference folders and filename
		File currentFolder = new File(IntgSrvUtils.reformatFilePath(TAXONOMY_LOOKUP_FOLDER_PRODUCT_CURRENT) + File.separator);
		File oldFolder = new File(currentFolder.getParentFile().getPath() + File.separator
				+ DatamigrationAppConstants.WAYFAIR_REFERENCE_OLD + File.separator);
		File referenceFileToBeWritten = new File(currentFolder.getPath()
				+ DatamigrationAppConstants.WAYFAIR_TAXONOMY_LOOKUP_PRODUCT_FILENAME);

		if (!currentFolder.exists()) {
			currentFolder.mkdirs();
		}
		if (!oldFolder.exists()) {
			oldFolder.mkdirs();
		}

		try {

			// delete the files in reference Old folder
			File[] fileInOldFolder = oldFolder.listFiles();
			for (int i = 0; i < fileInOldFolder.length; i++) {
				fileInOldFolder[i].delete();
			}

			// move the file from current to old
			File[] filesInCurrentFolder = currentFolder.listFiles();
			for (int i = 0; i < filesInCurrentFolder.length; i++) {
				Files.copy(filesInCurrentFolder[i].toPath(),
						new File(oldFolder.getPath() + File.separator + filesInCurrentFolder[i].getName()).toPath(),
						StandardCopyOption.REPLACE_EXISTING);
				filesInCurrentFolder[i].delete();
			}

			FileWriter writer = new FileWriter(referenceFileToBeWritten, true);
			FileInputStream inputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader;
			if (!DatamigrationCommonUtil.isPropertyValueNULL(ENCODING)) {
				inputStreamReader = new InputStreamReader(inputStream, ENCODING);
			} else {
				inputStreamReader = new InputStreamReader(inputStream);
			}
			BufferedReader br = new BufferedReader(inputStreamReader);
			String tempString;
			String[] values;
			Map<String, String> recordAsMap;

			// read input file line by line and write the required values in the
			// reference file
			while ((tempString = br.readLine()) != null) {
				if (!tempString.equals(DatamigrationAppConstants.EMPTY_STR)) {

					values = tempString.split(delimiter, -1);
					recordAsMap = DatamigrationCommonUtil.getRecordAsMap(headers, values, logger);

					if (recordAsMap != null) {
						String classId = getClassId(recordAsMap);
						recordAsMap.get(CLASS);

						// Write the values for lookup
						writer.write(recordAsMap.get(SUPERCATEGORY) + REFERENCE_FILE_DELIMITER + recordAsMap.get(CATEGORY)
								+ REFERENCE_FILE_DELIMITER + recordAsMap.get(DEPARTMENT) + REFERENCE_FILE_DELIMITER
								+ recordAsMap.get(CLASS) + headerdelimiter + classId + System.getProperty("line.separator"));
						recordAsMap.clear();
					}

				}
			}

			br.close();
			inputStreamReader.close();
			inputStream.close();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_IMAGETAXONOMY);
		}

	}

	/**
	 * @param file
	 * @return
	 * 
	 *         process the onetime feed
	 */
	public STEPProductInformation processOneTimeFeed(File file) {

		String tempString;
		Map<String, String> recordAsMap;
		STEPProductInformation stepprdinfo = null;
		String[] values;

		List<Map<String, String>> quillRecordsList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dotcomRecordsList = new ArrayList<Map<String, String>>();

		try {

			FileInputStream inputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader;
			if (!DatamigrationCommonUtil.isPropertyValueNULL(ENCODING)) {
				inputStreamReader = new InputStreamReader(inputStream, ENCODING);
			} else {
				inputStreamReader = new InputStreamReader(inputStream);
			}
			BufferedReader br = new BufferedReader(inputStreamReader);

			// read the input file line by line
			while ((tempString = br.readLine()) != null) {
				if (!tempString.equals(DatamigrationAppConstants.EMPTY_STR)) {
					noOfRecords++;
					try {
						values = tempString.split(delimiter, -1);
						recordAsMap = DatamigrationCommonUtil.getRecordAsMap(headers, values, logger);

						if (recordAsMap != null) {

							succeededRecords++;
							// get Unique IDs by concatenating the other IDs
							String classId = getClassId(recordAsMap);
							String deptID = getDeptId(recordAsMap);
							String categoryID = getCategoryId(recordAsMap);
							recordAsMap.put(UNIQUE_CLASSID, classId);
							recordAsMap.put(UNIQUE_DEPTD, deptID);
							recordAsMap.put(UNIQUE_CATEGORYID, categoryID);

							// add records to quill and dotcom records list
							if (DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE.equalsIgnoreCase(recordAsMap.get(HIERARCHY_CODE))) {
								quillRecordsList.add(recordAsMap);
							} else if (DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE.equalsIgnoreCase(recordAsMap
									.get(HIERARCHY_CODE))) {
								dotcomRecordsList.add(recordAsMap);
							}
						} else {
							failedRecords++;
							logger.error("Record omitted. Incorrect no of values :"+tempString);
							DatamigrationCommonUtil.appendWriterFile(file.getParentFile().getParentFile() + "/Report/Report_" + file.getName(), tempString
									+ "~Has incorrect number of Values in record :"+values.length);
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("Exception caught while processing record :" + tempString + " : " + e.getMessage());
						failedRecords++;
					}

				}
			}

			br.close();
			inputStreamReader.close();
			inputStream.close();
			
			// create STEPProductInformation object
			stepprdinfo = createSTEPProductInformationObject(dotcomRecordsList, quillRecordsList);
			if(dotcomRecordsList.size()+quillRecordsList.size() ==0){
				logger.error("No records generated from the file :"+file.getName()+" moving to file bad");
				DatamigrationCommonUtil.moveFileToFileBad(file, TaxonomyScheduler.PUBLISH_ID);
				return null;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			logger.error("Exception while processing file : " + file.getName() + " : " + e1.getMessage());
			IntgSrvUtils.alertByEmail(e1, DatamigrationCommonUtil.getClassName(), DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_IMAGETAXONOMY);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Exception while processing file : " + file.getName() + " : " + e.getMessage());
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_IMAGETAXONOMY);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception while processing file : " + file.getName() + " : " + e.getMessage());
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_IMAGETAXONOMY);
		}

		return stepprdinfo;
	}

	/**
	 * @param recordAsMap
	 * @return
	 * 
	 *         create class ID
	 */
	public String getClassId(Map<String, String> recordAsMap) {

		return recordAsMap.get(SUPERCATEGORY_ID) + recordAsMap.get(CATEGORY_ID) + recordAsMap.get(DEPARTMENT_ID)
				+ recordAsMap.get(CLASS_ID);
	}

	/**
	 * @param recordAsMap
	 * @return
	 * 
	 *         create Dept ID
	 */
	public String getDeptId(Map<String, String> recordAsMap) {

		return recordAsMap.get(SUPERCATEGORY_ID) + recordAsMap.get(CATEGORY_ID) + recordAsMap.get(DEPARTMENT_ID);
	}

	/**
	 * @param recordAsMap
	 * @return
	 * 
	 *         create Category ID
	 */
	public String getCategoryId(Map<String, String> recordAsMap) {

		return recordAsMap.get(SUPERCATEGORY_ID) + recordAsMap.get(CATEGORY_ID);
	}

	/**
	 * @param dotComRecords
	 * @param quillRecords
	 * @return
	 * 
	 *         create STEPProductInformation object using the dotcom and quill
	 *         records
	 */
	public STEPProductInformation createSTEPProductInformationObject(List<Map<String, String>> dotComRecords,
			List<Map<String, String>> quillRecords) {

		logger.info("creating Step xml for " + dotComRecords.size() + " dotcom records and " + quillRecords.size() + " quill records");
		DatamigrationCommonUtil.printConsole("WRITING THE STEPPRODUCTINFORMATION OBJECT...");

		objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = objectFactory.createSTEPProductInformation();
		ClassificationsType classifications = objectFactory.createClassificationsType();

		// get the classification tag for dotcom record
		if (dotComRecords.size() > 0) {
			classifications.getClassification().add(createClassificationsObjectForDotCom(dotComRecords));
		}

		// get the classification tag for quill record
		if (quillRecords.size() > 0) {
			classifications.getClassification().add(createClassificationsObjectForQuill(quillRecords));
		}

		stepProductInformation.setClassifications(classifications);
		// stepProductInformation.setAutoApprove(YesNoType.Y);
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
																// value
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
																		// coded
																		// value
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded
																	// value

		return stepProductInformation;
	}

	/**
	 * @param allRecordsList
	 * @return
	 * 
	 *         create classification tag for quill records
	 */
	public ClassificationType createClassificationsObjectForQuill(List<Map<String, String>> allRecordsList) {

		ClassificationType rootClassification = objectFactory.createClassificationType();
		rootClassification.setID(QUILLWEBHIERARCHY);
		rootClassification.setUserTypeID(QUILLWEBROOT);
		rootClassification.setParentID(WEB_HIERARCHY_ROOT);
		NameType rootName = objectFactory.createNameType();
		rootName.setContent(QUILL_WEB_HIERARCHY);
		rootClassification.getNameOrAttributeLinkOrSequenceProduct().add(rootName);

		ClassificationType homeCategoryClassification = objectFactory.createClassificationType();
		homeCategoryClassification.setID(QUILL_WEB_HOME_CATEGORY_1);
		homeCategoryClassification.setUserTypeID(QUILL_WEB_HOME_CATEGORY);
		NameType product = objectFactory.createNameType();
		product.setContent(PRODUCTS_STR);
		homeCategoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(product);

		for (Map<String, String> mapContainingRecord : allRecordsList) {

			// super category classifications
			ClassificationType superCategoryClassification = objectFactory.createClassificationType();
			superCategoryClassification.setUserTypeID(QUILL_SUPER_CATEGORY);
			superCategoryClassification.setID(QUILL_SUPERCATEGORYID_PREFIX + mapContainingRecord.get(SUPERCATEGORY_ID));
			ValueType valueSC = objectFactory.createValueType();
			valueSC.setAttributeID(CATEGORY_ID_STR);
			valueSC.setContent(mapContainingRecord.get(SUPERCATEGORY_ID));
			MetaDataType metadataSC = objectFactory.createMetaDataType();
			metadataSC.getValueOrMultiValueOrValueGroup().add(valueSC);
			superCategoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(metadataSC);
			NameType scNameType = objectFactory.createNameType();
			scNameType.setContent(mapContainingRecord.get(SUPERCATEGORY));
			superCategoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(scNameType);

			// category classification
			ClassificationType categoryClassification = objectFactory.createClassificationType();
			categoryClassification.setUserTypeID(QUILL_WEB_CATEGORY);
			categoryClassification.setID(QUILL_CATEGORYID_PREFIX + mapContainingRecord.get(UNIQUE_CATEGORYID));
			NameType categoryNameType = objectFactory.createNameType();
			categoryNameType.setContent(mapContainingRecord.get(CATEGORY));
			categoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(categoryNameType);
			ValueType valueC = objectFactory.createValueType();
			valueC.setAttributeID(CATEGORY_ID_STR);
			valueC.setContent(mapContainingRecord.get(CATEGORY_ID));
			MetaDataType metadataCategory = objectFactory.createMetaDataType();
			metadataCategory.getValueOrMultiValueOrValueGroup().add(valueC);
			categoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(metadataCategory);

			// department classification
			ClassificationType departmentClassification = objectFactory.createClassificationType();
			departmentClassification.setUserTypeID(QUILL_DOTCOM_DEPARTMENT);
			departmentClassification.setID(QUILL_DEPARTMENTID_PREFIX + mapContainingRecord.get(UNIQUE_DEPTD));
			NameType deptNameType = objectFactory.createNameType();
			deptNameType.setContent(mapContainingRecord.get(DEPARTMENT));
			departmentClassification.getNameOrAttributeLinkOrSequenceProduct().add(deptNameType);
			ValueType valueD = objectFactory.createValueType();
			valueD.setAttributeID(CATEGORY_ID_STR);
			valueD.setContent(mapContainingRecord.get(DEPARTMENT_ID));
			MetaDataType metadataDept = objectFactory.createMetaDataType();
			metadataDept.getValueOrMultiValueOrValueGroup().add(valueD);
			departmentClassification.getNameOrAttributeLinkOrSequenceProduct().add(metadataDept);

			// class classification
			ClassificationType classClassification = objectFactory.createClassificationType();
			classClassification.setUserTypeID(QUILL_WEB_CLASS);
			classClassification.setID(QUILL_CLASSID_PREFIX + mapContainingRecord.get(UNIQUE_CLASSID));
			NameType classNameType = objectFactory.createNameType();
			classNameType.setContent(mapContainingRecord.get(CLASS));
			classClassification.getNameOrAttributeLinkOrSequenceProduct().add(classNameType);
			MetaDataType metadataClass = objectFactory.createMetaDataType();
			ValueType valueClass = objectFactory.createValueType();
			valueClass.setAttributeID(CATEGORY_ID_STR);
			valueClass.setContent(mapContainingRecord.get(CLASS_ID));
			metadataClass.getValueOrMultiValueOrValueGroup().add(valueClass);

			classClassification.getNameOrAttributeLinkOrSequenceProduct().add(metadataClass);
			departmentClassification.getNameOrAttributeLinkOrSequenceProduct().add(classClassification);
			categoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(departmentClassification);
			superCategoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(categoryClassification);
			homeCategoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(superCategoryClassification);
		}

		rootClassification.getNameOrAttributeLinkOrSequenceProduct().add(homeCategoryClassification);
		return rootClassification;

	}

	/**
	 * @param allRecordsList
	 * @return
	 * 
	 *         create classification tag for dotcom records
	 */
	public ClassificationType createClassificationsObjectForDotCom(List<Map<String, String>> allRecordsList) {

		ClassificationType rootClassification = objectFactory.createClassificationType();
		rootClassification.setID(CLASSIFICATION_1_ROOT);
		rootClassification.setUserTypeID(CLASSIFICATION_USERTYPE_ROOT);
		NameType rootName = objectFactory.createNameType();
		rootName.setContent(CLASSIFICATION_1_ROOT);
		rootClassification.getNameOrAttributeLinkOrSequenceProduct().add(rootName);
		rootClassification.setSelected(false);

		// WebHierarchyRoot
		ClassificationType staplesDotComWebHierarchyRootClassification = objectFactory.createClassificationType();
		staplesDotComWebHierarchyRootClassification.setID(WEB_HIERARCHY_ROOT);
		staplesDotComWebHierarchyRootClassification.setUserTypeID(WEB_HIERARCHY_ROOT);
		staplesDotComWebHierarchyRootClassification.setSelected(false);
		NameType webHierarchyRootName = objectFactory.createNameType();
		webHierarchyRootName.setContent(WEB_SITES);
		staplesDotComWebHierarchyRootClassification.getNameOrAttributeLinkOrSequenceProduct().add(webHierarchyRootName);

		// StaplesDotComWebHierarchy
		ClassificationType staplesDotComWebHierarchyClassification = objectFactory.createClassificationType();
		staplesDotComWebHierarchyClassification.setID(STAPLES_DOTCOM_WEBHIERARCHY);
		staplesDotComWebHierarchyClassification.setUserTypeID(STAPLES_DOTCOM_WEBROOT);
		staplesDotComWebHierarchyClassification.setSelected(false);
		NameType staplesDotComWebHierarchyName = objectFactory.createNameType();
		staplesDotComWebHierarchyName.setContent(STAPLES_DOTCOM_WEBHIERARCHY_NAME);
		staplesDotComWebHierarchyClassification.getNameOrAttributeLinkOrSequenceProduct().add(staplesDotComWebHierarchyName);

		// StaplesDotComWebHomeCategory
		ClassificationType staplesDotComWebHomeCategoryClassification = objectFactory.createClassificationType();
		staplesDotComWebHomeCategoryClassification.setID(STAPLES_DOTCOM_HOMECAT);
		staplesDotComWebHomeCategoryClassification.setUserTypeID(STAPLES_DOTCOM_WEBHOME_CATEGORY);
		staplesDotComWebHomeCategoryClassification.setSelected(false);
		NameType staplesDotComWebHomeCategoryName = objectFactory.createNameType();
		staplesDotComWebHomeCategoryName.setContent(PRODUCTS_STR);
		staplesDotComWebHomeCategoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(staplesDotComWebHomeCategoryName);

		for (Map<String, String> mapContainingRecord : allRecordsList) {

			// super category classification
			ClassificationType superCategoryClassification = objectFactory.createClassificationType();
			superCategoryClassification.setUserTypeID(STAPLES_DOTCOM_WEBSUPERCATEGORY);
			superCategoryClassification.setID(STAPLES_SUPERCATEGORYID_PREFIX + mapContainingRecord.get(SUPERCATEGORY_ID));
			NameType scNameType = objectFactory.createNameType();
			scNameType.setContent(mapContainingRecord.get(SUPERCATEGORY));
			superCategoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(scNameType);
			ValueType valueSC = objectFactory.createValueType();
			valueSC.setAttributeID(CATEGORY_ID_STR);
			valueSC.setContent(mapContainingRecord.get(SUPERCATEGORY_ID));
			MetaDataType metadataSC = objectFactory.createMetaDataType();
			metadataSC.getValueOrMultiValueOrValueGroup().add(valueSC);
			superCategoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(metadataSC);

			// category classification
			ClassificationType categoryClassification = objectFactory.createClassificationType();
			categoryClassification.setUserTypeID(STAPLES_DOTCOM_CATEGORY);
			categoryClassification.setID(STAPLES_CATEGORYID_PREFIX + mapContainingRecord.get(UNIQUE_CATEGORYID));
			NameType categoryNameType = objectFactory.createNameType();
			categoryNameType.setContent(mapContainingRecord.get(CATEGORY));
			categoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(categoryNameType);
			ValueType valueC = objectFactory.createValueType();
			valueC.setAttributeID(CATEGORY_ID_STR);
			valueC.setContent(mapContainingRecord.get(CATEGORY_ID));
			MetaDataType metadataCategory = objectFactory.createMetaDataType();
			metadataCategory.getValueOrMultiValueOrValueGroup().add(valueC);
			categoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(metadataCategory);

			// department classification
			ClassificationType departmentClassification = objectFactory.createClassificationType();
			departmentClassification.setUserTypeID(STAPLES_DOTCOM_DEPARTMENT);
			departmentClassification.setID(STAPLES_DEPARTMENTID_PREFIX + mapContainingRecord.get(UNIQUE_DEPTD));
			NameType deptNameType = objectFactory.createNameType();
			deptNameType.setContent(mapContainingRecord.get(DEPARTMENT));
			departmentClassification.getNameOrAttributeLinkOrSequenceProduct().add(deptNameType);
			ValueType valueD = objectFactory.createValueType();
			valueD.setAttributeID(CATEGORY_ID_STR);
			valueD.setContent(mapContainingRecord.get(DEPARTMENT_ID));
			MetaDataType metadataDept = objectFactory.createMetaDataType();
			metadataDept.getValueOrMultiValueOrValueGroup().add(valueD);
			departmentClassification.getNameOrAttributeLinkOrSequenceProduct().add(metadataDept);

			// class classification
			ClassificationType classClassification = objectFactory.createClassificationType();
			classClassification.setUserTypeID(STAPLES_DOTCOM_CLASS);
			classClassification.setID(STAPLES_CLASSID_PREFIX + mapContainingRecord.get(UNIQUE_CLASSID));
			NameType classNameType = objectFactory.createNameType();
			classNameType.setContent(mapContainingRecord.get(CLASS));
			classClassification.getNameOrAttributeLinkOrSequenceProduct().add(classNameType);
			MetaDataType metadataClass = objectFactory.createMetaDataType();
			ValueType valueClass = objectFactory.createValueType();
			valueClass.setAttributeID(CATEGORY_ID_STR);
			valueClass.setContent(mapContainingRecord.get(CLASS_ID));
			metadataClass.getValueOrMultiValueOrValueGroup().add(valueClass);

			classClassification.getNameOrAttributeLinkOrSequenceProduct().add(metadataClass);
			departmentClassification.getNameOrAttributeLinkOrSequenceProduct().add(classClassification);
			categoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(departmentClassification);
			superCategoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(categoryClassification);
			staplesDotComWebHomeCategoryClassification.getNameOrAttributeLinkOrSequenceProduct().add(superCategoryClassification);
		}

		staplesDotComWebHierarchyClassification.getNameOrAttributeLinkOrSequenceProduct().add(staplesDotComWebHomeCategoryClassification);
		staplesDotComWebHierarchyRootClassification.getNameOrAttributeLinkOrSequenceProduct().add(staplesDotComWebHierarchyClassification);
		rootClassification.getNameOrAttributeLinkOrSequenceProduct().add(staplesDotComWebHierarchyRootClassification);

		return rootClassification;
	}

//	public void generateExecutionSummary(File file, boolean isOneTime, String delimiter, int noOfRecords, int succeededRecords,
//			int rejectedRecords, int newRecords, int deletedRecords, Date starttime, Date endtime) {
//
//		// get the execution report folder through
//		String reportsFolderName = file.getParentFile().getParentFile().getPath() + File.separator
//				+ DatamigrationAppConstants.REPORTS_FOLDER_NAME;
//		File ReportsFolder = new File(reportsFolderName);
//		if (!ReportsFolder.exists()) {
//			ReportsFolder.mkdirs();
//		}
//		File reportsFile = new File(ReportsFolder.getPath() + File.separator + TAXONOMY_REPORTSFILENAME);
//		try {
//			FileWriter writer = new FileWriter(reportsFile, true);
//
//			// Write file details
//			writer.write("\nFile Name : " + file.getName() + "\n");
//			if (isOneTime) {
//				writer.write("ONETIME INTEGRATION FILE \t");
//			} else {
//				writer.write("CONTINUOUS INTEGRATION FILE \t");
//			}
//			writer.write("Delimiter :" + delimiter + "\n");
//
//			// execution details
//			writer.write("Execution started on : " + starttime.toString() + "\n");
//			writer.write("Execution completed on : " + endtime.toString() + "\n");
//			writer.write("Turn around time(in ms): " + (endtime.getTime() - starttime.getTime()) + "\n");
//
//			if (isOneTime) {
//				writer.write("Total no of records in input file: " + noOfRecords + "\n");
//				writer.write("No of records successfully processed : " + succeededRecords + "\n");
//				writer.write("No of records rejected : " + rejectedRecords + "\n");
//			} else {
//				writer.write("No of new records : " + newRecords);
//				writer.write("No of deleted records : " + deletedRecords);
//			}
//			writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public static void main(String args[]) {

		TaxonomyProcessor taxonomyProcessor = new TaxonomyProcessor();
		taxonomyProcessor.wayfairTaxonomyInboundProcessor();
	}

}
