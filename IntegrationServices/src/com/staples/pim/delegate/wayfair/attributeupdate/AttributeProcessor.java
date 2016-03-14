
package com.staples.pim.delegate.wayfair.attributeupdate;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.REFERENCE_FILE_DELIMITER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_COMPONENT_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ECO_SYSTEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ICD_CLASSNAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WAYFAIR_ATTRIBUTE_OUTPUT_FOLDER;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.MessagingException;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pcm.stepcontract.beans.AttributeGroupLinkType;
import com.staples.pcm.stepcontract.beans.AttributeListType;
import com.staples.pcm.stepcontract.beans.AttributeType;
import com.staples.pcm.stepcontract.beans.ListOfValueLinkType;
import com.staples.pcm.stepcontract.beans.ListOfValueType;
import com.staples.pcm.stepcontract.beans.ListsOfValuesType;
import com.staples.pcm.stepcontract.beans.NameType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.TrueFalseType;
import com.staples.pcm.stepcontract.beans.UserTypeLinkType;
import com.staples.pcm.stepcontract.beans.ValidationType;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wayfair.attributeupdate.runner.AttributeScheduler;

/**
 * @author 843868
 * 
 */
public class AttributeProcessor {

	public static final String					ATTRIBUTE_METADATA_INSTANCENO						= "11_ATTRIBUTES";
	public static final String					LOV_INSTANCENO										= "11_LOV";
	public static final String					ATTRIBUTE_ENTITY_INSTANCENO							= "11_ENTITY";
	public static final String					ITM_ATTRIBGROUP										= "ITM_AttribGroup";
	public static final String					ITM_SUPERCATEGORYNAME								= "ITM_SuperCategoryName";
	public static final String					ITM_CATEGORY										= "ITM_Category";
	public static final String					ITM_DEPTNAME										= "ITM_DeptName";
	public static final String					ITM_CLASSNAME										= "ITM_ClassName";
	public static final String					ITM_ATTRIBNAME										= "ITM_AttribName";
	public static final String					ITM_ATTRIBSEARCH									= "ITM_AttribSearch";
	public static final String					ITM_ATTRIBREQUIRED									= "ITM_AttribRequired";
	public static final String					ITM_ATTRIBTYPE										= "ITM_AttribType";
	public static final String					ITM_ATTRIBVALUE										= "ITM_AttribValue";
	public static final String					ITM_BUSINESSUNIT									= "ITM_BusinessUnit";
	public static final String					ITM_ATTRIBUTEGROUP_ID								= "ITM_AttributeGroupID";
	public static final String					ITM_CLASSNAME_ID									= "ITM_ClassID";
	public static final String					ITM_SUPERCATEGORYNAME_ID							= "ITM_SuperCategoryID";
	public static final String					ITM_CATEGORY_ID										= "ITM_CategoryID";
	public static final String					ITM_DEPTNAME_ID										= "ITM_DepartmentID";
	public static final String					ITM_ATTRIBNAME_ID									= "ITM_AttributeNameID";
	public static final String					ITM_ATTRIBVALUE_ID									= "ITM_AttributeValueID";

	public static final String					WAYFAIR_ATTRIBUTE_DELIMITER							= "wayfair.attribute.delimiter";
	public static final String					WAYFAIR_ATTRIBUTE_HEADER							= "wayfair.attributes.header";
	public static final String					WAYFAIR_ATTRIBUTE_HEADER_CI							= "wayfair.attributes.header.continuous.integration";

	public static final String					RANGE_STR											= "Range";
	public static final String					FREEFORM_STR										= "Freeform";
	public static final String					NORMAL												= "Normal";
	public static final String					TEXT												= "text";
	public static final String					QUALIFIER_ROOT										= "Qualifier root";
	public static final String					ENG_STR												= "eng";
	public static final String					ATTRIBUTE_GROUPLINK_ID_STAPLES						= "StaplesSpecs";
	public static final String					ATTRIBUTE_GROUPLINK_ID_QUILL						= "QuillSpecs";
	public static final String					QUILL_LOVS											= "QuillLOVs";
	public static final String					DOTCOM_LOVS											= "DotComLOVs";
	public static final String					LOV_PREFIX											= "Lov_";
	public static final String					PDB_LOV_PREFIX										= "PDBLOV-";
	public static final String					QUL_LOV_PREFIX										= "QULLOV-";
	public static final String					PDB_PREFIX											= "PDB-";
	public static final String					QUL_PREFIX											= "QUL-";

	public static final String					LOV_FEED_SUFFIX										= "_LOVs.xml";
	public static final String					WAYFAIR_ATTRIBUTEMETADATA_DELETE_EMAIL_SUBJECT		= "Wayfair-Attribute metadata deleted records";
	public static final String					WAYFAIR_ATTRIBUTE_METADATA_FILEDONE_FOLDER			= "/opt/stibo/integration/hotfolder/WayfairIncoming/AttributeMetaData/File_Done/";
	public static final String					WAYFAIR_ATTRIBUTE_METADATA_FILEDONE_FOLDER_OT		= "/opt/stibo/integration/hotfolder/WayfairIncoming/OTAttributeMetaData/File_Done/";
	public static final String					WAYFAIR_ATTRIBUTE_METADATA_REFERENCE_FOLDER_CURRENT	= "/opt/stibo/SpringBatch/Reference/wayfairlookup/attributemetadata/current";
	public static final String					WAYFAIR_ATTRIBUTE_METADATA_REFERENCE_FOLDER_OLD		= "/opt/stibo/SpringBatch/Reference/wayfairlookup/attributemetadata/old";

	public static String						headerString;
	public static final String					headerdelimiter										= IntgSrvPropertiesReader
																											.getProperty(WAYFAIR_ATTRIBUTE_DELIMITER);
	public static final String					ONETIME_DELIMITER									= IntgSrvPropertiesReader
																											.getProperty("wayfair.onetime.delimiter");
	public static String						delimiter;
	public static String[]						headers;

	public static final String					ATTRIBUTE_LOOKUP_FOLDER_ATTRIBUTEMETADATA_CURRENT	= "/opt/stibo/SpringBatch/Reference/wayfairlookup/attributefeed/current";

	public static final String					ATTRIBUTE_METADATA_FILEUNPROCESSED_FOLDER			= "/opt/stibo/integration/hotfolder/WayfairIncoming/AttributeMetaData/File_Unprocessed/";
	public static final String					ATTRIBUTE_METADATA_FILEUNPROCESSED_FOLDER_OT		= "/opt/stibo/integration/hotfolder/WayfairIncoming/OTAttributeMetaData/File_Unprocessed/";

	public static final String					ATTRIBUTE_METADATA_MAIL_RECEIPENT					= "wayfair.attributemetadata.mail.toaddresses";
	public static final String					FREEFORM_TRACELOGGER_ATTRIBUTEMETADATA				= "tracelogger.wayfairattributemetadata";
	static IntgSrvLogger						logger												= IntgSrvLogger
																											.getInstance(FREEFORM_TRACELOGGER_ATTRIBUTEMETADATA);
	public static IntgSrvLogger					ehfLogger											= IntgSrvLogger
																											.getInstance(DatamigrationAppConstants.EHF_LOGGER_WAYFAIR);

	public static ErrorHandlingFrameworkICD		ehfICD												= null;

	public static ErrorHandlingFrameworkHandler	ehfHandler											= new ErrorHandlingFrameworkHandler();

	public static String						traceId												= null;

	String										logMessage											= null;

	public ObjectFactory						objectFactory;
	public AttributeMetadataProcessor			attributeMetadataProcessor;
	public static boolean						isOneTime											= false;

	public static final String					ONETIME_ENCODING									= "wayfair.onetime.encoding";

	public static final String					ENCODING											= IntgSrvPropertiesReader
																											.getProperty(ONETIME_ENCODING);
	public static int							successCount;
	public static int							failureCount;
	public static int							TotalCount;
	public static long							starttime;
	public static int							newRecords;
	public static int							deletedRecords;

	/**
	 * Main method
	 */
	public void attributeInboundProcessor() {

		try {

			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(SPRINGBATCH_ECO_SYSTEM, SPRINGBATCH_COMPONENT_ID,
					SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_METADATA);
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();

			File oneTimeFolder = new File(IntgSrvUtils.reformatFilePath(ATTRIBUTE_METADATA_FILEUNPROCESSED_FOLDER_OT));
			File CIFolder = new File(IntgSrvUtils.reformatFilePath(ATTRIBUTE_METADATA_FILEUNPROCESSED_FOLDER));
			File folder = null;

			logger.info("\tATTRIBUTE METADATA FEED REQUEST");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "ATTRIBUTE METADATA FEED request", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
			// Check both onetime and continuous integration folder for inputs
			if (DatamigrationCommonUtil.isFolderContainsFile(oneTimeFolder)) {

				logger.info("ONETIME INTEGRATION FILES");
				folder = oneTimeFolder;
				delimiter = ONETIME_DELIMITER;
				headerString = IntgSrvPropertiesReader.getProperty(WAYFAIR_ATTRIBUTE_HEADER);
				headers = headerString.split(headerdelimiter, -1);
				isOneTime = true;
			} else if (DatamigrationCommonUtil.isFolderContainsFile(CIFolder)) {

				logger.info("CONTINUOUS INTEGRATION FILES");
				folder = CIFolder;
				delimiter = headerdelimiter;
				headerString = IntgSrvPropertiesReader.getProperty(WAYFAIR_ATTRIBUTE_HEADER_CI);
				headers = headerString.split(delimiter, -1);
				isOneTime = false;
			}else{
				logger.info("No files in hotfolder");
			}
			
			if (folder != null) {
				DatamigrationCommonUtil.printConsole("Attribute Metadata  >>Processing isOneTime: " + isOneTime + " Delimiter: "
						+ delimiter);
				File[] files = DatamigrationCommonUtil.sortFilesBasedOnFIFO(folder, AttributeScheduler.PUBLISH_ID);
				for (int i = 0; i < files.length; i++) {

					// check if the file is an xsv or dsv file
					if ((files[i].getName().endsWith(DatamigrationAppConstants.XSV_EXTENSION))
							|| (files[i].getName().endsWith(DatamigrationAppConstants.DSV_EXTENSION))) {
						logger.info(new Date().toString() + " Attribute Metadata Processing input file : " + files[i].getName());
						DatamigrationCommonUtil.printConsole("Attribute Metadata  >>Processing File : " + files[i].getName());
					
						if(DatamigrationCommonUtil.doContainValidFile(files[i],headers,delimiter,logger)){
							readIncomingFile(files[i]);
						}else{
							logger.error("File does not have any valid record.");
							DatamigrationCommonUtil.moveFileToFileBad(files[i], AttributeScheduler.PUBLISH_ID);
						}
					} else {
						logger.error("Invalid file : " + files[i].getName());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception caught in Attribute Processor");
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
		} catch (ErrorHandlingFrameworkException e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
		}
	}

	/**
	 * @param file
	 * 
	 *            create attribute metadata reference file containing attribute
	 *            group name, attribute name and their respective IDs
	 */
	public void createAttributeReferenceFileForAttributeFeed(File file) {

		// get reference folders
		File currentFolder = new File(IntgSrvUtils.reformatFilePath(ATTRIBUTE_LOOKUP_FOLDER_ATTRIBUTEMETADATA_CURRENT) + File.separator);
		File oldFolder = new File(currentFolder.getParentFile().getPath() + File.separator
				+ DatamigrationAppConstants.WAYFAIR_REFERENCE_OLD + File.separator);

		if (!currentFolder.exists()) {
			currentFolder.mkdirs();
		}
		if (!oldFolder.exists()) {
			oldFolder.mkdirs();
		}

		File referenceFileToBeWritten = new File(currentFolder.getPath()
				+ DatamigrationAppConstants.WAYFAIR_ATTRIBUTE_LOOKUP_ATTRIBUTEMETADATA_FILENAME);

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

			// Write the values for lookup
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

			// read the input file and get the required values
			while ((tempString = br.readLine()) != null) {
				if (!tempString.equals(DatamigrationAppConstants.EMPTY_STR)) {

					values = tempString.split(delimiter, -1);
					recordAsMap = DatamigrationCommonUtil.getRecordAsMap(headers, values, logger);
					if (recordAsMap != null) {
						// write attributegrp name,Id and attribute name, ID
						writer.write(recordAsMap.get(ITM_ATTRIBGROUP) + REFERENCE_FILE_DELIMITER + recordAsMap.get(ITM_ATTRIBNAME) + REFERENCE_FILE_DELIMITER + recordAsMap.get(ITM_BUSINESSUNIT)
								+ headerdelimiter + recordAsMap.get(ITM_ATTRIBUTEGROUP_ID) + REFERENCE_FILE_DELIMITER
								+ recordAsMap.get(ITM_ATTRIBNAME_ID) + System.getProperty("line.separator"));
						recordAsMap.clear();
					}

				}
			}
			writer.close();
			br.close();
			inputStreamReader.close();
			inputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Exception while creating reference file for: " + file.getName() + " : " + e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "Exception caught while processing file : " + file.getName() + " : " + e.getMessage(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.error(logMessage);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
		} catch (Exception e) {
			logger.error("Exception while creating reference file for : " + file.getName() + " : " + e.getMessage());
			e.printStackTrace();
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "Exception caught while processing file : " + file.getName() + " : " + e.getMessage(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.error(logMessage);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
		}

	}

	/**
	 * @param newFile
	 * @param oldFile
	 * 
	 *            compare the new and old files and get the delta feed
	 */
	public void processDeltaFeed(File newFile, File oldFile) {

		Set<String> oldFileRecords;
		Set<String> newFileRecords;
		Map<String, Set<String>> deltaRecords;

		// get delta feed
		logger.info("getting delta feed");
		deltaRecords = DatamigrationCommonUtil.getDeltaFeedFromNewAndOldFiles(newFile, oldFile, logger);
		oldFileRecords = deltaRecords.get(DatamigrationAppConstants.WAYFAIR_REFERENCE_OLD);
		newFileRecords = deltaRecords.get(DatamigrationAppConstants.WAYFAIR_REFERENCE_CURRENT);
		logger.info("No of Records that are deleted/modified : " + oldFileRecords.size());
		logger.info("No of Records that are new/modified : " + newFileRecords.size());
		newRecords = newFileRecords.size();

		logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
				EHF_MSGTYPE_INFO_NONSLA, "generating delta feed from input files", EHF_SPRINGBATCH_ITEMUTILITY_USER,
				DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.info(logMessage);
		// remove modified records
		logger.info("removing modified records");
		oldFileRecords = removeModifiedRecordsFromDeletedRecordsAttributeMetadata(newFileRecords, oldFileRecords);
		deletedRecords = oldFileRecords.size();
		// add deleted records to generated xml
		newFileRecords.addAll(oldFileRecords);

		// get sets as a map
		Map<String, List<Map<String, String>>> allRecords = DatamigrationCommonUtil.getSetAsMaps(newFileRecords, headerdelimiter, headers,
				logger, newFile);

		//Write deleted records into a file
		for(String deletedRecord : oldFileRecords){
				DatamigrationCommonUtil.appendWriterFile(newFile.getParentFile().getParentFile().getPath()+File.separator+"Report"+File.separator+"DeletedRecords_"+newFile.getName(), deletedRecord);
		}
		
		// send mail for deleted records
		if (!oldFileRecords.isEmpty()) {
			logger.info("sending mails for deleted records");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "sending mails for deleted records", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
			DatamigrationCommonUtil.sendEmailForDeletedRecords(oldFileRecords, ATTRIBUTE_METADATA_MAIL_RECEIPENT,
					WAYFAIR_ATTRIBUTEMETADATA_DELETE_EMAIL_SUBJECT);
		}

		// write a file containing reference data for attributefeed
		logger.info("creating attribute reference feed");
		logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
				EHF_MSGTYPE_INFO_NONSLA, "creating attribute reference feed", EHF_SPRINGBATCH_ITEMUTILITY_USER,
				DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.info(logMessage);
		createAttributeReferenceFileForAttributeFeed(newFile);

		// create xmls
		logger.info("creating STEP xmls");
		logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
				EHF_MSGTYPE_INFO_NONSLA, "creating STEP xmls", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
				DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.info(logMessage);
		
		if(allRecords.get(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE).isEmpty() && allRecords.get(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE).isEmpty()){
			try {
				String filedonepath = newFile.getParentFile().getParentFile().toPath()+File.separator+"File_Done"+File.separator+newFile.getName();
				File fileDoneFile = new File(filedonepath);
				Files.copy(newFile.toPath(), fileDoneFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				System.gc();
				newFile.delete();
			} catch (IOException e) {
				logger.error("Exception caught while moving file to fileDone folder.");
			}
		}else{
			createXMLsForDelta(allRecords, newFile);
		}
	}

	/**
	 * @param allRecords
	 * @param file
	 * 
	 *            create the xmls for Entity, LOVs and Data feeds
	 */
	public void createXMLsForDelta(Map<String, List<Map<String, String>>> allRecords, File file) {

		// get delta records in a single list
		List<Map<String, String>> deltaRecords = allRecords.get(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE);
		deltaRecords.addAll(allRecords.get(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE));

		Map<String, List<Map<String, String>>> quillLOVs = new HashMap<String, List<Map<String, String>>>();
		Map<String, List<Map<String, String>>> dotcomLOVs = new HashMap<String, List<Map<String, String>>>();
		Map<String, List<Map<String, String>>> freeformValuesStaples = new HashMap<String, List<Map<String, String>>>();
		Map<String, List<Map<String, String>>> freeformValuesQuill = new HashMap<String, List<Map<String, String>>>();
		List<Map<String, String>> attributeMetadataRecords = new ArrayList<Map<String, String>>();

		// parse through each record
		for (Map<String, String> thisRecord : deltaRecords) {

			// trim the attribute name to 40 chars for step constraints
			String attributeName = thisRecord.get(ITM_ATTRIBNAME);
			if (attributeName.length() > 40) {
				attributeName = attributeName.substring(0, 40);
				thisRecord.put(ITM_ATTRIBNAME, attributeName);
			}

			// get LOVs and Freeform values
			if (RANGE_STR.equalsIgnoreCase(thisRecord.get(ITM_ATTRIBTYPE))) {
				getLOVValues(thisRecord, dotcomLOVs, quillLOVs);
			} else if (FREEFORM_STR.equalsIgnoreCase(thisRecord.get(ITM_ATTRIBTYPE))) {
				getFreeFormValues(thisRecord, freeformValuesStaples, freeformValuesQuill);
			}

			attributeMetadataRecords.add(thisRecord);
		}

		// copy file for future references and delta generation
		DatamigrationCommonUtil.copyNewFileToReferenceFolder(file, WAYFAIR_ATTRIBUTE_METADATA_REFERENCE_FOLDER_CURRENT, logger);

		// create attribute data xml
		if ((!freeformValuesStaples.isEmpty()) || (!dotcomLOVs.isEmpty()) || (!quillLOVs.isEmpty())) {
			logger.info("creating Step xml for Attribute Data");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "creating Step xml for Attribute Data", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			getAttributesAsSTEPxml(file, freeformValuesStaples, freeformValuesQuill, dotcomLOVs, quillLOVs);
		} else {
			logger.info("File comparison yielded no delta for attribute data. No step xml for attribute data feed");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "File comparison yielded no delta for attribute data. No step xml for attribute data feed",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);
		}

		// create LOV xmls
		if ((!dotcomLOVs.isEmpty()) || (!quillLOVs.isEmpty())) {
			logger.info("creating Step xml for LOV feed");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "creating Step xml for LOV feed", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			getLOVsasSTEPxml(file, dotcomLOVs, quillLOVs);
		} else {
			logger.info("File comparison yielded no delta for attribute data. No step xml generated for LOV feed");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "File comparison yielded no delta for attribute data. No step xml generated for LOV feed",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);
		}

		// create entiry feed xml
		if (!attributeMetadataRecords.isEmpty()) {
			logger.info("creating Step xml for Attribute Entity feed");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "creating Step xml for Attribute Entity feed", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			attributeMetadataProcessor.processAttributeMetaData(attributeMetadataRecords, file);
		} else {
			logger.info("File comparison yielded no delta feed. No step xml generated");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "File comparison yielded no delta for attribute data. No step xml generated for LOV feed",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);
		}
	}

	/**
	 * @param newFileRecords
	 * @param oldFileRecords
	 * @return
	 * 
	 *         remove the modified records from the old file delta records set
	 */
	public Set<String> removeModifiedRecordsFromDeletedRecordsAttributeMetadata(Set<String> newFileRecords, Set<String> oldFileRecords) {

		String[] newFileValues;
		String[] oldFileValues;
		Map<String, String> newFileRecordAsMap;
		Map<String, String> oldFileRecordAsMap;
		Set<String> modifiedRecords = new HashSet<String>();

		for (String newFileRecord : newFileRecords) {

			newFileValues = newFileRecord.split(headerdelimiter, -1);
			newFileRecordAsMap = DatamigrationCommonUtil.getRecordAsMap(headers, newFileValues, logger);
			for (String oldFileRecord : oldFileRecords) {
				oldFileValues = oldFileRecord.split(headerdelimiter, -1);
				oldFileRecordAsMap = DatamigrationCommonUtil.getRecordAsMap(headers, oldFileValues, logger);

				if ((newFileRecordAsMap != null) && (oldFileRecordAsMap != null)) {
					// concatenate all the unique ids to create an unique ID for
					// verification
					String oldFileIDs = oldFileRecordAsMap.get(ITM_SUPERCATEGORYNAME_ID) + oldFileRecordAsMap.get(ITM_CATEGORY_ID)
							+ oldFileRecordAsMap.get(ITM_DEPTNAME_ID) + oldFileRecordAsMap.get(ITM_CLASSNAME_ID)
							+ oldFileRecordAsMap.get(ITM_ATTRIBUTEGROUP_ID) + oldFileRecordAsMap.get(ITM_ATTRIBNAME_ID)
							+ oldFileRecordAsMap.get(ITM_ATTRIBVALUE_ID);
					String newFileIDs = newFileRecordAsMap.get(ITM_SUPERCATEGORYNAME_ID) + newFileRecordAsMap.get(ITM_CATEGORY_ID)
							+ newFileRecordAsMap.get(ITM_DEPTNAME_ID) + newFileRecordAsMap.get(ITM_CLASSNAME_ID)
							+ newFileRecordAsMap.get(ITM_ATTRIBUTEGROUP_ID) + newFileRecordAsMap.get(ITM_ATTRIBNAME_ID)
							+ newFileRecordAsMap.get(ITM_ATTRIBVALUE_ID);

					// If both IDs are same, then the record is not deleted, It
					// is modified
					if (oldFileIDs.equalsIgnoreCase(newFileIDs)) {
						modifiedRecords.add(oldFileRecord);
					}
				}
			}
		}

		// remove modified records from deleted list
		DatamigrationCommonUtil.printConsole("modifiedRecords :: " + modifiedRecords);
		oldFileRecords.removeAll(modifiedRecords);
		DatamigrationCommonUtil.printConsole("oldFileRecords :: " + oldFileRecords);
		return oldFileRecords;
	}

	/**
	 * @param file
	 * 
	 *            process onetime feed
	 */
	public void processOneTimeFeed(File file) {

		successCount = 0;
		failureCount = 0;
		TotalCount = 0;
		starttime = DatamigrationCommonUtil.getCurrentMilliSeconds();
		String tempString;
		// int lineno=0;
		Map<String, String> thisRecord;
		String[] values;
		// Map<String,Set<String>> quillLOVs = new
		// HashMap<String,List<Map<String,String>>>();
		Map<String, List<Map<String, String>>> quillLOVs = new HashMap<String, List<Map<String, String>>>();
		Map<String, List<Map<String, String>>> dotcomLOVs = new HashMap<String, List<Map<String, String>>>();
		Map<String, List<Map<String, String>>> freeformValuesStaples = new HashMap<String, List<Map<String, String>>>();
		Map<String, List<Map<String, String>>> freeformValuesQuill = new HashMap<String, List<Map<String, String>>>();
		List<Map<String, String>> attributeMetadataRecords = new ArrayList<Map<String, String>>();

		try {
			FileInputStream inputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader;
			if (!DatamigrationCommonUtil.isPropertyValueNULL(ENCODING)) {
				inputStreamReader = new InputStreamReader(inputStream, ENCODING);
			} else {
				inputStreamReader = new InputStreamReader(inputStream);
			}
			BufferedReader br = new BufferedReader(inputStreamReader);

			// parse line by line
			while ((tempString = br.readLine()) != null) {

				if (!tempString.equals(DatamigrationAppConstants.EMPTY_STR)) {

					// get line in a map containing headers and values
					// respectively
					values = tempString.split(delimiter, -1);
					thisRecord = DatamigrationCommonUtil.getRecordAsMap(headers, values, logger);
					if (thisRecord != null) {
						// trim attribute names to 40 chars
						String attributeName = thisRecord.get(ITM_ATTRIBNAME);
						if (attributeName.length() > 40) {
							attributeName = attributeName.substring(0, 40);
							thisRecord.put(ITM_ATTRIBNAME, attributeName);
						}

						// attribute LOV and Freeform data
						if (RANGE_STR.equalsIgnoreCase(thisRecord.get(ITM_ATTRIBTYPE))) {
							getLOVValues(thisRecord, dotcomLOVs, quillLOVs);
						} else if (FREEFORM_STR.equalsIgnoreCase(thisRecord.get(ITM_ATTRIBTYPE))) {
							getFreeFormValues(thisRecord, freeformValuesStaples, freeformValuesQuill);
						}
						successCount++;
						attributeMetadataRecords.add(thisRecord);
					}else{
						failureCount++;
						logger.error("Record omitted. Incorrect no of values :"+tempString);
						DatamigrationCommonUtil.appendWriterFile(file.getParentFile().getParentFile() + "/Report/Report_" + file.getName(), tempString
								+ "~Has incorrect number of Values in record :"+values.length);
					}
					TotalCount++;
					// lineno++;
				}
			}
			
			logger.info("file parsing complete");
			br.close();
			inputStreamReader.close();
			inputStream.close();

			if(attributeMetadataRecords==null || attributeMetadataRecords.size()==0){
				logger.error("No records generated from the file : "+file.getName()+". Moving to file bad folder");
				DatamigrationCommonUtil.moveFileToFileBad(file, AttributeScheduler.PUBLISH_ID);
			}else{
			
			// copy file for future reference and delta creation
			logger.info("copying file to reference folder..");
			// DatamigrationCommonUtil.copyNewFileToReferenceFolder(file,
			// WAYFAIR_ATTRIBUTE_METADATA_REFERENCE_FOLDER_CURRENT, logger);
			createOneTimeReferenceFileForAttributeMetadata(file);

			// write attribute group name and ID, attribute name/ID in a file
			// for AttributeFeed instance
			logger.info("creating reference file for Attribute Feed");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "creating reference file for Attribute Feed", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
			createAttributeReferenceFileForAttributeFeed(file);

			// attribute data xml
			logger.info("generating STEP xml for attribute data");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "generating STEP xml for attribute data", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
			getAttributesAsSTEPxml(file, freeformValuesStaples, freeformValuesQuill, dotcomLOVs, quillLOVs);

			// LOVs step xml generation
			logger.info("generating STEP xml for attribute LOVs");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "generating STEP xml for attribute LOVs", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
			getLOVsasSTEPxml(file, dotcomLOVs, quillLOVs);

			// attribute entity step xml
			logger.info("generating STEP xml for attribute entity");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "generating STEP xml for attribute entity", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
			attributeMetadataProcessor.processAttributeMetaData(attributeMetadataRecords, file);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("Exception caught while processing file : " + file.getName() + " : " + e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "Exception caught while processing file : " + file.getName() + " : " + e.getMessage(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.error(logMessage);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Exception caught while processing file : " + file.getName() + " : " + e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "Exception caught while processing file : " + file.getName() + " : " + e.getMessage(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.error(logMessage);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception caught while processing file : " + file.getName() + " : " + e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "Exception caught while processing file : " + file.getName() + " : " + e.getMessage(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.error(logMessage);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
		}
		long endTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
		try {
			generateExecutionSummary("Attribute Processor ", DatamigrationCommonUtil.getReportFolderPath(file), file.getName(), successCount, (endTime-starttime), failureCount, TotalCount);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			
		}
	}

	public void createOneTimeReferenceFileForAttributeMetadata(File file) {

		logger.info("creating reference file");

		// Get the current and old folders that contain the reference files
		File currentFolder = new File(IntgSrvUtils.reformatFilePath(WAYFAIR_ATTRIBUTE_METADATA_REFERENCE_FOLDER_CURRENT) + File.separator);
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
					File newfile = new File(oldFolder.getPath() + File.separator + filesInCurrentFolder[i].getName());
					Files.copy(filesInCurrentFolder[i].toPath(), newfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					filesInCurrentFolder[i].delete();
				}
			}

			// write the new file to current folder
			File referenceFileToBeWritten = new File(currentFolder.getPath() + File.separator + file.getName());
			// Make dir if not available
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
						referenceWriter.write(recordAsMap.get(ITM_ATTRIBGROUP) + headerdelimiter + recordAsMap.get(ITM_SUPERCATEGORYNAME)
								+ headerdelimiter + recordAsMap.get(ITM_CATEGORY) + headerdelimiter + recordAsMap.get(ITM_DEPTNAME)
								+ headerdelimiter + "" + headerdelimiter + recordAsMap.get(ITM_CLASSNAME) + headerdelimiter
								+ recordAsMap.get(ITM_ATTRIBNAME) + headerdelimiter + recordAsMap.get(ITM_ATTRIBSEARCH) + headerdelimiter
								+ recordAsMap.get(ITM_ATTRIBREQUIRED) + headerdelimiter + recordAsMap.get(ITM_ATTRIBTYPE) + headerdelimiter
								+ recordAsMap.get(ITM_ATTRIBVALUE) + headerdelimiter + recordAsMap.get(ITM_BUSINESSUNIT) + headerdelimiter
								+ recordAsMap.get(ITM_SUPERCATEGORYNAME_ID) + headerdelimiter + recordAsMap.get(ITM_CATEGORY_ID)
								+ headerdelimiter + recordAsMap.get(ITM_DEPTNAME_ID) + headerdelimiter + recordAsMap.get(ITM_CLASSNAME_ID)
								+ headerdelimiter + recordAsMap.get(ITM_ATTRIBUTEGROUP_ID) + headerdelimiter
								+ recordAsMap.get(ITM_ATTRIBNAME_ID) + headerdelimiter + recordAsMap.get(ITM_ATTRIBVALUE_ID)
								+ System.getProperty("line.separator"));
					}
				}
			}
			referenceWriter.close();
			br.close();
			inputStreamReader.close();
			inputStream.close();

		} catch (IOException e) {
			logger.error("Exception while creating reference file : " + e.getMessage());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "Exception caught while processing file : " + file.getName() + " : " + e.getMessage(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.error(logMessage);
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_FEED);
		}
		logger.info("copied input file successfully");
		logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
				EHF_MSGTYPE_INFO_NONSLA, "copied input file successfully", EHF_SPRINGBATCH_ITEMUTILITY_USER,
				DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.error(logMessage);

	}

	/**
	 * @param file
	 * 
	 *            read incoming file
	 */
	public void readIncomingFile(File file) {

		objectFactory = new ObjectFactory();
		attributeMetadataProcessor = new AttributeMetadataProcessor();
		Date startTime = new Date();
		if (!isOneTime) {
			// get reference file
			logger.info("looking for reference file");
			File referenceFile = DatamigrationCommonUtil.getReferenceFile(WAYFAIR_ATTRIBUTE_METADATA_REFERENCE_FOLDER_CURRENT,
					WAYFAIR_ATTRIBUTE_METADATA_REFERENCE_FOLDER_OLD);
			if(referenceFile != null){
				logger.info("Processing delta feed.");
				processDeltaFeed(file, referenceFile);
			}else{
				logger.error("Reference File not found while processing file :"+file.getName());
			}
		} else {
			logger.info("Processing onetime feed");
			processOneTimeFeed(file);
		}
		Date endDate = new Date();
		DatamigrationCommonUtil.generateExecutionSummaryForWayFair(file, isOneTime, delimiter, TotalCount, successCount, failureCount,
				newRecords, deletedRecords, startTime, endDate, "AttributeMetadata");
	}

	/**
	 * @param values
	 * @param freeformValuesStaples
	 * 
	 * get freeform values in a map
	 */
	public void getFreeFormValues(Map<String, String> values, Map<String, List<Map<String, String>>> freeformValuesStaples,
			Map<String, List<Map<String, String>>> freeformValuesQuill) {

		String key = values.get(ITM_ATTRIBNAME_ID);
		String hierarchyCode = values.get(ITM_BUSINESSUNIT);
		if (hierarchyCode.equalsIgnoreCase(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE)) {
			if (freeformValuesStaples.containsKey(key)) {
				freeformValuesStaples.get(key).add(values);
			} else {
				List<Map<String, String>> freeFormValues = new ArrayList<Map<String, String>>();
				freeFormValues.add(values);
				freeformValuesStaples.put(key, freeFormValues);
			}
		} else if (hierarchyCode.equalsIgnoreCase(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE)) {
			if (freeformValuesQuill.containsKey(key)) {
				freeformValuesQuill.get(key).add(values);
			} else {
				List<Map<String, String>> freeFormValues = new ArrayList<Map<String, String>>();
				freeFormValues.add(values);
				freeformValuesQuill.put(key, freeFormValues);
			}
		}
	}

	/**
	 * @param values
	 * @param dotcomLOVs
	 * @param quillLOVs
	 * 
	 *            get lov values in a map
	 */
	public void getLOVValues(Map<String, String> values, Map<String, List<Map<String, String>>> dotcomLOVs,
			Map<String, List<Map<String, String>>> quillLOVs) {

		String key = values.get(ITM_ATTRIBNAME_ID);
		String hierarchycode = values.get(ITM_BUSINESSUNIT);

		// Quill / Dotcom
		if (hierarchycode.equalsIgnoreCase(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE)) {
			if (dotcomLOVs.containsKey(key)) {
				dotcomLOVs.get(key).add(values);
			} else {
				List<Map<String, String>> lovValues = new ArrayList<Map<String, String>>();
				lovValues.add(values);
				dotcomLOVs.put(key, lovValues);
			}
		} else if (hierarchycode.equalsIgnoreCase(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE)) {
			if (quillLOVs.containsKey(key)) {
				quillLOVs.get(key).add(values);
			} else {
				List<Map<String, String>> lovValues = new ArrayList<Map<String, String>>();
				lovValues.add(values);
				quillLOVs.put(key, lovValues);
			}
		}
	}

	public Set<String> getListAsSetToRemoveDuplicates(List<Map<String, String>> LOVList){
		Set<String> LOVSet = new TreeSet<String>();
		
		for(Map<String,String> LOVMap : LOVList){
			LOVSet.add(LOVMap.get(ITM_ATTRIBVALUE));
		}
		
		return LOVSet;
	}
	
	/**
	 * @param file
	 * @param dotcomLOVs
	 * @param quillLOVs
	 * 
	 *            create LOV step xml
	 */
	public void getLOVsasSTEPxml(File file, Map<String, List<Map<String, String>>> dotcomLOVs,
			Map<String, List<Map<String, String>>> quillLOVs) {

		STEPProductInformation stepProductInformation = objectFactory.createSTEPProductInformation();
		ListsOfValuesType LOVType = objectFactory.createListsOfValuesType();

		// Dotcom LOVs
		for (String key : dotcomLOVs.keySet()) {
			List<Map<String, String>> lovValues = dotcomLOVs.get(key);
			Set<String> LOVSet = getListAsSetToRemoveDuplicates(lovValues);
			ListOfValueType lovType = objectFactory.createListOfValueType();
			lovType.setAllowUserValueAddition(TrueFalseType.FALSE);
			lovType.setID(PDB_LOV_PREFIX + key);
			lovType.setParentID(DOTCOM_LOVS);
			lovType.setReferenced(true);
			lovType.setUseValueID(TrueFalseType.FALSE);

			NameType name = objectFactory.createNameType();
			name.setContent(lovValues.get(0).get(ITM_ATTRIBNAME));
			ValidationType validationType = objectFactory.createValidationType();
			validationType.setBaseType(TEXT);
			validationType.setInputMask(DatamigrationAppConstants.EMPTY_STR);
			validationType.setMaxLength("1000");
			validationType.setMaxValue(DatamigrationAppConstants.EMPTY_STR);
			validationType.setMinValue(DatamigrationAppConstants.EMPTY_STR);
			lovType.getName().add(name);
			lovType.setValidation(validationType);

			for (String lovValue : LOVSet) {
				if (!IntgSrvUtils.isNullOrEmpty(lovValue)) {
					ValueType value = objectFactory.createValueType();
					value.setQualifierID(QUALIFIER_ROOT);
					
					//Rama asked to change this
					if(lovValue.contains("<")){
						lovValue = lovValue.replaceAll("<", "<lt/>");
					}else if(lovValue.contains(">")){
						lovValue = lovValue.replaceAll(">", "<gt/>");
					}
					value.setContent(lovValue);
					lovType.getValue().add(value);
				}
			}

			LOVType.getListOfValue().add(lovType);
		}
		// Quill LOVs
		for (String key : quillLOVs.keySet()) {
			List<Map<String, String>> lovValues = quillLOVs.get(key);
			Set<String> LOVSet = getListAsSetToRemoveDuplicates(lovValues);
			ListOfValueType lovType = objectFactory.createListOfValueType();
			lovType.setAllowUserValueAddition(TrueFalseType.FALSE);
			lovType.setID(QUL_LOV_PREFIX + key);
			lovType.setParentID(QUILL_LOVS);
			lovType.setReferenced(true);
			lovType.setUseValueID(TrueFalseType.FALSE);

			NameType name = objectFactory.createNameType();
			name.setContent(lovValues.get(0).get(ITM_ATTRIBNAME));
			ValidationType validationType = objectFactory.createValidationType();
			validationType.setBaseType(TEXT);
			validationType.setInputMask(DatamigrationAppConstants.EMPTY_STR);
			validationType.setMaxLength("1000");
			validationType.setMaxValue(DatamigrationAppConstants.EMPTY_STR);
			validationType.setMinValue(DatamigrationAppConstants.EMPTY_STR);
			lovType.getName().add(name);
			lovType.setValidation(validationType);

			for (String lovValue : LOVSet) {
				if (!IntgSrvUtils.isNullOrEmpty(lovValue)) {
					ValueType value = objectFactory.createValueType();
					value.setQualifierID(QUALIFIER_ROOT);
					
					//Rama asked to change this
					if(lovValue.contains("<")){
						lovValue = lovValue.replaceAll("<", "<lt/>");
					}else if(lovValue.contains(">")){
						lovValue = lovValue.replaceAll(">", "<gt/>");
					}
					value.setContent(lovValue);
					lovType.getValue().add(value);
				}
			}

			LOVType.getListOfValue().add(lovType);
		}

		stepProductInformation.setListsOfValues(LOVType);
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
																// value
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
																		// coded
																		// value
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded
																	// value
		String outputFileName = file.getPath().substring(0, file.getPath().length() - 4) + LOV_FEED_SUFFIX;

		File outputFile = DatamigrationCommonUtil.marshallObject(stepProductInformation, new File(outputFileName),
				WAYFAIR_ATTRIBUTE_OUTPUT_FOLDER, AttributeScheduler.PUBLISH_ID);
		if (isOneTime) {
			DatamigrationCommonUtil.sendFile(outputFile, file, WAYFAIR_ATTRIBUTE_METADATA_FILEDONE_FOLDER_OT, LOV_INSTANCENO, false,
					AttributeScheduler.PUBLISH_ID);
		} else {
			DatamigrationCommonUtil.sendFile(outputFile, file, WAYFAIR_ATTRIBUTE_METADATA_FILEDONE_FOLDER, LOV_INSTANCENO, false,
					AttributeScheduler.PUBLISH_ID);
		}
		// WAYFAIR_ATTRIBUTE_METADATA_FILEDONE_FOLDER_OT

	}

	/**
	 * @param file
	 * @param freeformValues
	 * @param dotcomLOVs
	 * @param quillLOVs
	 * 
	 *            create Attribute data Step xml
	 */
	public void getAttributesAsSTEPxml(File file, Map<String, List<Map<String, String>>> freeformValues,
			Map<String, List<Map<String, String>>> freeformValuesQuill, Map<String, List<Map<String, String>>> dotcomLOVs,
			Map<String, List<Map<String, String>>> quillLOVs) {

		STEPProductInformation stepPrdInfo = objectFactory.createSTEPProductInformation();
		AttributeListType attributeList = objectFactory.createAttributeListType();

		// freeForm attributes STAPLES
		for (String key : freeformValues.keySet()) {
			List<Map<String, String>> attributesList = freeformValues.get(key);
			Map<String, String> AttributeMap = attributesList.get(0);

			AttributeType attribute = objectFactory.createAttributeType();
			attribute.setID(PDB_PREFIX + AttributeMap.get(ITM_ATTRIBNAME_ID));
			attribute.setMultiValued(TrueFalseType.FALSE);
			attribute.setProductMode(NORMAL);
			attribute.setFullTextIndexed(TrueFalseType.FALSE);
			attribute.setExternallyMaintained(TrueFalseType.FALSE);
			attribute.setDerived(TrueFalseType.FALSE);
			attribute.setSelected(TrueFalseType.TRUE);
			attribute.setReferenced(TrueFalseType.TRUE);
			
			NameType name = objectFactory.createNameType();
			name.setContent(AttributeMap.get(ITM_ATTRIBNAME));
			attribute.getName().add(name);

			ValidationType validation = objectFactory.createValidationType();
			validation.setBaseType(TEXT);
			validation.setMinValue(DatamigrationAppConstants.EMPTY_STR);
			validation.setMaxValue(DatamigrationAppConstants.EMPTY_STR);
			validation.setMaxLength("1000");
			validation.setInputMask(DatamigrationAppConstants.EMPTY_STR);
			attribute.setValidation(validation);

			AttributeGroupLinkType attributeGroupLink = objectFactory.createAttributeGroupLinkType();
			attributeGroupLink.setAttributeGroupID(ATTRIBUTE_GROUPLINK_ID_STAPLES);
			attribute.getAttributeGroupLink().add(attributeGroupLink);

			UserTypeLinkType userTypeLinkType = objectFactory.createUserTypeLinkType();
			userTypeLinkType.setUserTypeID(DatamigrationAppConstants.ITEM);
			attribute.getUserTypeLink().add(userTypeLinkType);

			attributeList.getAttribute().add(attribute);
		}

		// freeForm attributes QUILL
		for (String key : freeformValuesQuill.keySet()) {

			List<Map<String, String>> attributesList = freeformValuesQuill.get(key);
			Map<String, String> AttributeMap = attributesList.get(0);

			AttributeType attribute = objectFactory.createAttributeType();
			attribute.setID(QUL_PREFIX + AttributeMap.get(ITM_ATTRIBNAME_ID));
			attribute.setMultiValued(TrueFalseType.FALSE);
			attribute.setProductMode(NORMAL);
			attribute.setFullTextIndexed(TrueFalseType.FALSE);
			attribute.setExternallyMaintained(TrueFalseType.FALSE);
			attribute.setDerived(TrueFalseType.FALSE);
			attribute.setSelected(TrueFalseType.TRUE);
			attribute.setReferenced(TrueFalseType.TRUE);

			NameType name = objectFactory.createNameType();
			name.setContent(AttributeMap.get(ITM_ATTRIBNAME));
			attribute.getName().add(name);

			ValidationType validation = objectFactory.createValidationType();
			validation.setBaseType(TEXT);
			validation.setMinValue(DatamigrationAppConstants.EMPTY_STR);
			validation.setMaxValue(DatamigrationAppConstants.EMPTY_STR);
			validation.setMaxLength("1000");
			validation.setInputMask(DatamigrationAppConstants.EMPTY_STR);
			attribute.setValidation(validation);

			AttributeGroupLinkType attributeGroupLink = objectFactory.createAttributeGroupLinkType();
			attributeGroupLink.setAttributeGroupID(ATTRIBUTE_GROUPLINK_ID_QUILL);
			attribute.getAttributeGroupLink().add(attributeGroupLink);

			UserTypeLinkType userTypeLinkType = objectFactory.createUserTypeLinkType();
			userTypeLinkType.setUserTypeID(DatamigrationAppConstants.ITEM);
			attribute.getUserTypeLink().add(userTypeLinkType);

			attributeList.getAttribute().add(attribute);
		}

		// quill LOV attributes
		for (String key : quillLOVs.keySet()) {

			List<Map<String, String>> attributesList = quillLOVs.get(key);
			Map<String, String> AttributeMap = attributesList.get(0);

			AttributeType attribute = objectFactory.createAttributeType();
			attribute.setID(QUL_PREFIX + AttributeMap.get(ITM_ATTRIBNAME_ID));
			attribute.setMultiValued(TrueFalseType.FALSE);
			attribute.setProductMode(NORMAL);
			attribute.setFullTextIndexed(TrueFalseType.FALSE);
			attribute.setExternallyMaintained(TrueFalseType.FALSE);
			attribute.setDerived(TrueFalseType.FALSE);
			attribute.setHierarchicalFiltering(TrueFalseType.FALSE);
			attribute.setClassificationHierarchicalFiltering(TrueFalseType.FALSE);
			attribute.setSelected(TrueFalseType.TRUE);
			attribute.setReferenced(TrueFalseType.TRUE);

			NameType name = objectFactory.createNameType();
			name.setContent(AttributeMap.get(ITM_ATTRIBNAME));
			attribute.getName().add(name);

			ValidationType validation = objectFactory.createValidationType();
//			validation.setBaseType(TEXT);
			validation.setMinValue(DatamigrationAppConstants.EMPTY_STR);
			validation.setMaxValue(DatamigrationAppConstants.EMPTY_STR);
			validation.setMaxLength("1000");
			validation.setInputMask(DatamigrationAppConstants.EMPTY_STR);
			attribute.setValidation(validation);
			
			ListOfValueLinkType listOfValueLink = objectFactory.createListOfValueLinkType();
			listOfValueLink.setListOfValueID(QUL_LOV_PREFIX + key);
			attribute.setListOfValueLink(listOfValueLink);

			AttributeGroupLinkType attributeGroupLink = objectFactory.createAttributeGroupLinkType();
			attributeGroupLink.setAttributeGroupID(ATTRIBUTE_GROUPLINK_ID_QUILL);
			attribute.getAttributeGroupLink().add(attributeGroupLink);

			UserTypeLinkType userTypeLinkType = objectFactory.createUserTypeLinkType();
			userTypeLinkType.setUserTypeID(DatamigrationAppConstants.ITEM);
			attribute.getUserTypeLink().add(userTypeLinkType);

			attributeList.getAttribute().add(attribute);
		}

		// Dotcom LOV attributes
		for (String key : dotcomLOVs.keySet()) {

			List<Map<String, String>> attributesList = dotcomLOVs.get(key);
			Map<String, String> AttributeMap = attributesList.get(0);

			AttributeType attribute = objectFactory.createAttributeType();
			attribute.setID(PDB_PREFIX + AttributeMap.get(ITM_ATTRIBNAME_ID));
			attribute.setMultiValued(TrueFalseType.FALSE);
			attribute.setProductMode(NORMAL);
			attribute.setFullTextIndexed(TrueFalseType.FALSE);
			attribute.setExternallyMaintained(TrueFalseType.FALSE);
			attribute.setDerived(TrueFalseType.FALSE);
			attribute.setHierarchicalFiltering(TrueFalseType.FALSE);
			attribute.setClassificationHierarchicalFiltering(TrueFalseType.FALSE);
			attribute.setSelected(TrueFalseType.TRUE);
			attribute.setReferenced(TrueFalseType.TRUE);

			NameType name = objectFactory.createNameType();
			name.setContent(AttributeMap.get(ITM_ATTRIBNAME));
			attribute.getName().add(name);

			ListOfValueLinkType listOfValueLink = objectFactory.createListOfValueLinkType();
			listOfValueLink.setListOfValueID(PDB_LOV_PREFIX + key);
			attribute.setListOfValueLink(listOfValueLink);

			ValidationType validation = objectFactory.createValidationType();
//			validation.setBaseType(TEXT);
			validation.setMinValue(DatamigrationAppConstants.EMPTY_STR);
			validation.setMaxValue(DatamigrationAppConstants.EMPTY_STR);
			validation.setMaxLength("1000");
			validation.setInputMask(DatamigrationAppConstants.EMPTY_STR);
			attribute.setValidation(validation);
			
			AttributeGroupLinkType attributeGroupLink = objectFactory.createAttributeGroupLinkType();
			attributeGroupLink.setAttributeGroupID(ATTRIBUTE_GROUPLINK_ID_STAPLES);
			attribute.getAttributeGroupLink().add(attributeGroupLink);

			UserTypeLinkType userTypeLinkType = objectFactory.createUserTypeLinkType();
			userTypeLinkType.setUserTypeID(DatamigrationAppConstants.ITEM);
			attribute.getUserTypeLink().add(userTypeLinkType);

			attributeList.getAttribute().add(attribute);
		}

		stepPrdInfo.setAttributeList(attributeList);
		stepPrdInfo.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepPrdInfo.setContextID(CONTEXT_ID_VALUE); // Hard coded value
		stepPrdInfo.setExportContext(EXPORT_CONTEXT_VALUE);// Hard coded value
		stepPrdInfo.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded value
		File outputFile = DatamigrationCommonUtil.marshallObject(stepPrdInfo, file, WAYFAIR_ATTRIBUTE_OUTPUT_FOLDER,
				AttributeScheduler.PUBLISH_ID);
		if (isOneTime) {
			DatamigrationCommonUtil.sendFile(outputFile, file, WAYFAIR_ATTRIBUTE_METADATA_FILEDONE_FOLDER_OT,
					ATTRIBUTE_METADATA_INSTANCENO, false, AttributeScheduler.PUBLISH_ID);
		} else {
			DatamigrationCommonUtil.sendFile(outputFile, file, WAYFAIR_ATTRIBUTE_METADATA_FILEDONE_FOLDER, ATTRIBUTE_METADATA_INSTANCENO,
					false, AttributeScheduler.PUBLISH_ID);
		}
	}

	public void generateExecutionSummary(String inputFeedType, String reportPath, String fileName, int successCount,
			long milliseconds, int failureCount, int totalCount) throws IOException, MessagingException {

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

		String actualMsg = DatamigrationCommonUtil.constructEmailBody(inputFeedType, fileName, successCount, totalErrorsCount, issueSummary, false, milliseconds);

		DatamigrationCommonUtil.sendEmail(inputFeedType + "Migration Status", actualMsg, IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.TO_ADDRESS),
				attachmentFiles, false, true);

	}
	public static void main(String[] args) {

		new AttributeProcessor().attributeInboundProcessor();
	}

}
