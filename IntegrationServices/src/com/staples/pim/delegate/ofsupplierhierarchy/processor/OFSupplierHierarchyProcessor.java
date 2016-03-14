
package com.staples.pim.delegate.ofsupplierhierarchy.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.staples.pcm.stepcontract.beans.ClassificationType;
import com.staples.pcm.stepcontract.beans.ClassificationsType;
import com.staples.pcm.stepcontract.beans.NameType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.PrivilegeRuleType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.TrueFalseType;
import com.staples.pcm.stepcontract.beans.UserGroupLinkType;
import com.staples.pcm.stepcontract.beans.UserGroupListType;
import com.staples.pcm.stepcontract.beans.UserGroupType;
import com.staples.pcm.stepcontract.beans.UserListType;
import com.staples.pcm.stepcontract.beans.UserType;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.ofsupplierhierarchy.runner.OFSupplierHierarchyScheduler;

public class OFSupplierHierarchyProcessor {

	/**
	 * @param args
	 */
	private static String			delimiter								= IntgSrvPropertiesReader
																					.getProperty("ORACLE_FINANCIAL_DELIMITER");
	private static int				maxRecordCountPerFile					= Integer.parseInt(IntgSrvPropertiesReader.getProperty("ORACLE_FINANCIAL_MAXFILECOUNT"));
	public static final String		oracleFinancialInputFolder				= IntgSrvUtils
																					.reformatFilePath("/opt/stibo/integration/hotfolder/VendorIncoming/File_Unprocessed");

	public static final String		VENDOR_NUMBER							= "VENDOR_NUMBER";
	public static final String		VENDOR_NAME								= "VENDOR_NAME";
	public static final String		EMAIL_ADDRESS							= "EMAIL_ADDRESS";
	public static final String		LAST_MODIFIED_DATE						= "LAST_MODIFIED_DATE";
	public static final String		HeaderString							= IntgSrvPropertiesReader
																					.getProperty("ORACLE_FINANCIAL_HEADER");
	public static final String[]	headers									= HeaderString.split(delimiter, -1);
	public static final String		ORACLE_FINANCIAL_OUTPUTFOLDER			= "ORACLE_FINANCIAL_OUTPUTFOLDER";
	public static final String		FREEFORM_TRACELOGGER_ORACLE_FINANCIAL	= "tracelogger.oracle.financial";
	public static final String DELTA_NEW = "new";
	public static final String DELTA_DELETED = "deleted";
	static IntgSrvLogger			logger									= IntgSrvLogger
																					.getInstance(FREEFORM_TRACELOGGER_ORACLE_FINANCIAL);
	public static Calendar			currentExecutionTimeMinusOneDay;

	public static final String		referenceFolderCurrentPath				= "/opt/stibo/SpringBatch/Reference/wayfairlookup/oraclefinancial/current";
	public static final String		referenceFolderOldPath					= "/opt/stibo/SpringBatch/Reference/wayfairlookup/oraclefinancial/old";
	public static final String		ORACLE_FINANCIAL_FILEDONE_FOLDER		= "/opt/stibo/integration/hotfolder/VendorIncoming/File_Done/";
	public static final String		ORACLE_FINANCIAL_MAIL_RECEPIENTS		= "ORACLE_FINANCIAL_MAIL_RECEPIENTS";
	public List<String> outputFilesFolderList;
	public List<String> outputFilesGroupList;
	public static void main(String[] args) {

		OFSupplierHierarchyProcessor oracleFinancialProcessor = new OFSupplierHierarchyProcessor();
		oracleFinancialProcessor.processOracleFinancial();
	}

	public void processOracleFinancial() {

		outputFilesFolderList = new ArrayList<String>();
		outputFilesGroupList = new ArrayList<String>();
		DatamigrationCommonUtil.printConsole("Looking up input folder.");
		File inputFolder = new File(oracleFinancialInputFolder);
		File[] inputFiles = inputFolder.listFiles();
		for (int i = 0; i < inputFiles.length; i++) {
			logger.info("Processing input File:" + inputFiles[i].getName());
			if(inputFiles[i].getName().endsWith(".txt")){
				processingIncomingFile(inputFiles[i]);
			}else{
				logger.error("Invalid file, invalid extension. Moving to filebad");
				DatamigrationCommonUtil.moveFileToFileBad(inputFiles[i], OFSupplierHierarchyScheduler.PUBLISH_ID);
			}
		}
		createAndSendSTEPMetaFile(outputFilesFolderList,outputFilesGroupList);
	}

	public void processingIncomingFile(File file) {

		File referenceFile = DatamigrationCommonUtil.getReferenceFile(referenceFolderCurrentPath, referenceFolderOldPath);
		if (referenceFile != null) {
			try {
				logger.info("Generating Delta Feed");
				Map<String, Set<String>> deltaRecords = generateDeltaFeed(file, referenceFile);
				Set<String> newRecords = deltaRecords.get(DELTA_NEW);
				Set<String> deletedRecords = deltaRecords.get(DELTA_DELETED);

				logger.info("Delta generation complete. New Records="+newRecords.size()+" Deleted Records="+deletedRecords.size());
				String reportFileName = file.getParentFile().getParentFile().getPath()+File.separator+DatamigrationAppConstants.REPORTS_FOLDER_NAME+File.separator+"reports.txt";
				DatamigrationCommonUtil.appendWriterFile(reportFileName, "FileName :"+file.getName()+System.lineSeparator()+"Date :"+new Date().toString());
				DatamigrationCommonUtil.appendWriterFile(reportFileName, "New Records:"+newRecords.size()+System.lineSeparator()+"Deleted Records :"+deletedRecords.size());

				if(newRecords.size()!=0){
					List<Map<String, String>> allProducts = getSetAsMap(newRecords);
					DatamigrationCommonUtil.appendWriterFile(reportFileName, "No of valid Records :"+allProducts.size()+System.lineSeparator());

					logger.info("File copied to reference folder");
					DatamigrationCommonUtil.copyNewFileToReferenceFolder(file, referenceFolderCurrentPath, logger);
					updatedStepProdInformation(allProducts, 1, file, true);
				}else{
					logger.info("Delta generation yielded no new records. Hence no STEP xml will be generated.");
					
					DatamigrationCommonUtil.copyNewFileToReferenceFolder(file, referenceFolderCurrentPath, logger);
					logger.info("File copied to reference folder");
				}

				if(deletedRecords.size()!=0){
					String deletedRecordsFileName = file.getParentFile().getParentFile().getPath() + File.separator+DatamigrationAppConstants.REPORTS_FOLDER_NAME+File.separator
							+ file.getName().substring(0, (file.getName().length() - 4)) + "_deleted.txt";
					for (String deletedRecord : deletedRecords) {
						DatamigrationCommonUtil.appendWriterFile(deletedRecordsFileName, deletedRecord);
					}
					logger.info("Deleted Records file created : "+deletedRecordsFileName);
					DatamigrationCommonUtil.sendEmailForDeletedRecords(deletedRecords, ORACLE_FINANCIAL_MAIL_RECEPIENTS,
							"Oracle financial deleted records");
				}else{
					logger.info("Delta generation yielded No deleted records !");
				}
				
				if((newRecords.size() == 0) && (deletedRecords.size()==0)){
					logger.info("Delta generation yielded no new data. Moving file to filedone folder.");
					File fileDoneFile = new File(IntgSrvUtils.reformatFilePath(ORACLE_FINANCIAL_FILEDONE_FOLDER) + File.separator
							+ file.getName());
					Files.copy(file.toPath(), fileDoneFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					file.delete();
				}
			} catch (IOException e) {
				logger.error("Exception occurred while moving file to fileDone :"+file.getName());
				e.printStackTrace();
			} catch (Exception e){
				logger.error("Exception occurred while processing file :"+file.getName());
			}
		} else {

			logger.info("Reference File not found. Processing file as onetime file");
			List<Map<String, String>> allProducts = new ArrayList<Map<String, String>>();
		
			try {
				String tempString;
				FileReader fileReader = new FileReader(file.getPath());
				BufferedReader br = new BufferedReader(fileReader);
				int fileCount=1;
				int successCount=0;
				int failureCount=0;
				
				while ((tempString = br.readLine()) != null) {
					if (!tempString.equals("")) {
						tempString = tempString.substring(0, tempString.lastIndexOf(delimiter));
						String[] values = tempString.split(delimiter, -1);
						Map<String, String> thisRecord = DatamigrationCommonUtil.getRecordAsMap(headers, values, logger);
						if(thisRecord!=null){
							successCount++;
							allProducts.add(thisRecord);
							if (allProducts.size() == maxRecordCountPerFile) {
								logger.info("Maximum record limit per file reached. writing file "+fileCount);
								updatedStepProdInformation(allProducts, fileCount, file, false);
								fileCount++;
								allProducts.clear();
							}
						}else{
							failureCount++;
						}
					}
				}
				br.close();
				fileReader.close();
				String reportFileName = file.getParentFile().getParentFile().getPath()+File.separator+DatamigrationAppConstants.REPORTS_FOLDER_NAME+File.separator+"reports.txt";
				DatamigrationCommonUtil.appendWriterFile(reportFileName, "FileName :"+file.getName()+System.lineSeparator()+"Date :"+new Date().toString());
				DatamigrationCommonUtil.appendWriterFile(reportFileName, "SuccessCount:"+successCount+System.lineSeparator() +"FailureCount :"+failureCount+System.lineSeparator());
			
				if (allProducts.size() > 0) {
					logger.info("Writing last file "+fileCount);
					DatamigrationCommonUtil.copyNewFileToReferenceFolder(file, referenceFolderCurrentPath, logger);
					logger.info("File copied to reference folder");
					updatedStepProdInformation(allProducts, fileCount, file, true);
					
				} else if (successCount == 0 && file.exists()) {
					logger.info("No valid records in file. Moving file to fileBad folder.");
					DatamigrationCommonUtil.moveFileToFileBad(file, OFSupplierHierarchyScheduler.PUBLISH_ID);
					
				} else if (file.exists()) {
					
					logger.info("Moving file to filedone folder.");
					File fileDoneFile = new File(IntgSrvUtils.reformatFilePath(ORACLE_FINANCIAL_FILEDONE_FOLDER) + File.separator
							+ file.getName());
					Files.copy(file.toPath(), fileDoneFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					DatamigrationCommonUtil.copyNewFileToReferenceFolder(file, referenceFolderCurrentPath, logger);
					file.delete();
					logger.info("File copied to reference folder");
				}
				
			} catch (IOException e) {
				logger.error("Exception caught while processing file."+file.getName()+" Exception:"+e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public List<Map<String, String>> getSetAsMap(Set<String> set) {

		List<Map<String, String>> listOfMap = new ArrayList<Map<String, String>>();

		for (String record : set) {
			String[] values = record.split(delimiter, -1);
			Map<String, String> map = DatamigrationCommonUtil.getRecordAsMap(headers, values, logger);
			if (map != null) {
				listOfMap.add(map);
			}
		}

		return listOfMap;
	}

	public Map<String, Set<String>> generateDeltaFeed(File currentFile, File referenceFile) {

		Map<String, Set<String>> deltaFeed = new HashMap<String, Set<String>>();

		Set<String> oldFileRecords = getFileInASet(referenceFile);
		Set<String> newFileRecords = getFileInASet(currentFile);
		Set<String> similars = new HashSet<String>(oldFileRecords);
		similars.retainAll(newFileRecords);
		oldFileRecords.removeAll(similars); // now set1 contains distinct
		newFileRecords.removeAll(similars); // now set2 contains distinct

		deltaFeed.put(DELTA_NEW, newFileRecords);
		deltaFeed.put(DELTA_DELETED, oldFileRecords);
		return deltaFeed;
	}

	public Set<String> getFileInASet(File file) {

		Set<String> set = new HashSet<String>();

		try {
			String tempString;
			FileReader fileReader = new FileReader(file.getPath());
			BufferedReader br = new BufferedReader(fileReader);

			while ((tempString = br.readLine()) != null) {
				if (!tempString.equals("")) {
					tempString = tempString.substring(0, tempString.lastIndexOf(delimiter));
					set.add(tempString);
				}
			}
			br.close();
			fileReader.close();
		} catch (IOException e) {
			logger.error("Exception caught while creating a SET from a file."+ e.getMessage());
			e.printStackTrace();
		}
		return set;
	}

	private boolean isValidRecord(String[] values) {

		if (values != null && values.length > 1 && !"No match".equalsIgnoreCase(values[1])) {
			return true;
		}

		return false;
	}

	private void updatedStepProdInformation(List<Map<String, String>> allProducts, int fileCount, File file, boolean isFileMoveActive) {

		try {

			ObjectFactory objectFactory = new ObjectFactory();
			STEPProductInformation stepProductInformation = null;

			stepProductInformation = objectFactory.createSTEPProductInformation();
			stepProductInformation.setUserGroupList(getSupplierGroupList(allProducts));
			logger.info("Creating GroupList step XML");
			generateStepXML(stepProductInformation, ORACLE_FINANCIAL_OUTPUTFOLDER + "/groupList_" + getCurrentDateForSTEP("yyyyMMddHHmmss")
					+ "_" + fileCount + ".xml", file, false);

			stepProductInformation = objectFactory.createSTEPProductInformation();
			stepProductInformation.setClassifications(getSupplierFolder(allProducts));
			logger.info("Creating folderList step xml");
			generateStepXML(stepProductInformation, ORACLE_FINANCIAL_OUTPUTFOLDER + "/folderList_"
					+ getCurrentDateForSTEP("yyyyMMddHHmmss") + "_" + fileCount + ".xml", file, isFileMoveActive);

		} catch (Exception e) {
			logger.error("Exception while creating STEPProductinformation object :" + e.getMessage());
			e.printStackTrace();
		}

	}

	private ClassificationsType getSupplierFolder(List<Map<String, String>> allProducts) {

		ObjectFactory objectFactory = new ObjectFactory();
		ClassificationsType classifications = objectFactory.createClassificationsType();

		ClassificationType classfication1 = objectFactory.createClassificationType();
		classfication1.setID("Classification 1 root");
		classfication1.setUserTypeID("Classification 1 user-type root");
		classfication1.setSelected(Boolean.FALSE);
		NameType name = objectFactory.createNameType();
		name.setContent("Classification 1 root");
		classfication1.getNameOrAttributeLinkOrSequenceProduct().add(name);

		ClassificationType classfication2 = objectFactory.createClassificationType();

		classfication2.setID("Supplier Hierarchy");
		classfication2.setUserTypeID("SupplierRoot");
		classfication2.setSelected(Boolean.FALSE);
		NameType name2 = objectFactory.createNameType();
		name2.setContent("Supplier Hierarchy");
		classfication2.getNameOrAttributeLinkOrSequenceProduct().add(name2);
		classfication1.getNameOrAttributeLinkOrSequenceProduct().add(classfication2);

		ClassificationType classfication3 = null;
		ClassificationType classfication4 = null;
		ClassificationType classfication5 = null;
		ClassificationType classfication6 = null;
		ClassificationType classfication7 = null;

		Map<String, String> attributeMap = null;

		for (int i = 0; i < allProducts.size(); i++) {
			attributeMap = allProducts.get(i);

			String vendorName = attributeMap.get(VENDOR_NAME);
			String VendorNumber = attributeMap.get(VENDOR_NUMBER);
			String vendorNameFirstChar = getVendorNameFirstChar(vendorName);
			classfication3 = objectFactory.createClassificationType();

			classfication3.setID("Index-" + vendorNameFirstChar);
			classfication3.setUserTypeID("SupplierIndex");
			classfication3.setSelected(Boolean.FALSE);
			NameType name3 = objectFactory.createNameType();
			name3.setContent(vendorNameFirstChar);
			classfication3.getNameOrAttributeLinkOrSequenceProduct().add(name3);
			classfication2.getNameOrAttributeLinkOrSequenceProduct().add(classfication3);

			classfication4 = objectFactory.createClassificationType();

			classfication4.setID(VendorNumber);
			classfication4.setUserTypeID("SupplierResource");
			NameType name4 = objectFactory.createNameType();
			name4.setContent(vendorName);
			classfication4.getNameOrAttributeLinkOrSequenceProduct().add(name4);
			classfication3.getNameOrAttributeLinkOrSequenceProduct().add(classfication4);

			classfication5 = objectFactory.createClassificationType();
			classfication5.setID(VendorNumber + "Products");
			classfication5.setUserTypeID("SupplierProducts");
			NameType name5 = objectFactory.createNameType();
			name5.setContent("Products");
			classfication5.getNameOrAttributeLinkOrSequenceProduct().add(name5);
			classfication4.getNameOrAttributeLinkOrSequenceProduct().add(classfication5);

			classfication6 = objectFactory.createClassificationType();
			classfication6 = objectFactory.createClassificationType();
			classfication6.setID(VendorNumber + "Assets");
			classfication6.setUserTypeID("SupplierAssets");
			NameType name6 = objectFactory.createNameType();
			name6.setContent("Assets");
			classfication6.getNameOrAttributeLinkOrSequenceProduct().add(name6);
			classfication4.getNameOrAttributeLinkOrSequenceProduct().add(classfication6);

			classfication7 = objectFactory.createClassificationType();
			classfication7 = objectFactory.createClassificationType();
			classfication7.setID(VendorNumber + "Batches");
			classfication7.setUserTypeID("SupplierBatches");
			NameType name7 = objectFactory.createNameType();
			name7.setContent("Batches");
			classfication7.getNameOrAttributeLinkOrSequenceProduct().add(name7);
			classfication4.getNameOrAttributeLinkOrSequenceProduct().add(classfication7);

		}

		classifications.getClassification().add(classfication1);
		return classifications;
	}

	private String getVendorNameFirstChar(String value) {

		if (value != null && !value.isEmpty()) {

			return "" + value.charAt(0);
		}
		return null;
	}

	private UserGroupListType getSupplierGroupList(List<Map<String, String>> allProducts) {

		ObjectFactory objectFactory = new ObjectFactory();
		UserGroupListType userGroupList = objectFactory.createUserGroupListType();

		Map<String, String> attributeMap = null;
		NameType name = null;
		name = objectFactory.createNameType();
		name.setContent("Suppliers");
		UserGroupType userGroup = objectFactory.createUserGroupType();
		userGroup.setID("Suppliers");
		userGroup.setReadOnly(TrueFalseType.FALSE);
		userGroup.getName().add(name);
		for (int i = 0; i < allProducts.size(); i++) {
			attributeMap = allProducts.get(i);

			String vendorName = attributeMap.get(VENDOR_NAME);
			String vendorNumber = attributeMap.get(VENDOR_NUMBER);

			name = objectFactory.createNameType();
			name.setContent(vendorName);

			UserGroupType userGroupIn = objectFactory.createUserGroupType();
			userGroupIn.getName().add(name);
			userGroupIn.setID(vendorNumber);
			userGroupIn.setVendorRoot(vendorNumber);
			userGroupIn.setReadOnly(TrueFalseType.FALSE);
			userGroup.getUserGroup().add(userGroupIn);

		}

		PrivilegeRuleType privilegeRule1 = objectFactory.createPrivilegeRuleType();
		privilegeRule1.setActionSetID("All user actions");
		privilegeRule1.setProductID("Product hierarchy root");
		userGroup.getPrivilegeRule().add(privilegeRule1);

		PrivilegeRuleType privilegeRule2 = objectFactory.createPrivilegeRuleType();
		privilegeRule2.setActionSetID("Supplier setup actions");
		userGroup.getPrivilegeRule().add(privilegeRule2);

		PrivilegeRuleType privilegeRule3 = objectFactory.createPrivilegeRuleType();
		privilegeRule3.setActionSetID("All user actions");
		privilegeRule3.setClassificationID("Classification 1 root");
		userGroup.getPrivilegeRule().add(privilegeRule3);

		userGroupList.getUserGroup().add(userGroup);

		return userGroupList;

	}

	private UserListType getSupplierUserList(List<Map<String, String>> allProducts) {

		ObjectFactory objectFactory = new ObjectFactory();
		UserListType userList = objectFactory.createUserListType();
		UserType user = null;
		UserGroupLinkType userGroupLink = null;
		Map<String, String> attributeMap = null;
		NameType name = null;
		for (int i = 0; i < allProducts.size(); i++) {
			attributeMap = allProducts.get(i);

			String vendorName = attributeMap.get(VENDOR_NAME);
			String VendorNumber = attributeMap.get(VENDOR_NUMBER);

			user = objectFactory.createUserType();
			user.setForceAuthentication(TrueFalseType.FALSE);
			user.setID(VendorNumber + "@" + vendorName);
			user.setPassword("exchange");
			name = objectFactory.createNameType();
			name.setContent(VendorNumber + "@" + vendorName);
			userGroupLink = objectFactory.createUserGroupLinkType();
			userGroupLink.setUserGroupID(vendorName);
			user.getName().add(name);
			user.getUserGroupLink().add(userGroupLink);
			userList.getUser().add(user);
			/*
			 * for (Entry<String, String> mapObj : attributeMap.entrySet()) {
			 * 
			 * }
			 */

		}
		return userList;
	}

	public Map<String, String> getRecordAsMap(String[] values, String[] headers, Map<String, String> productMap) {

		for (int headerIterator = 0; headerIterator < headers.length; headerIterator++) {
			productMap.put(headers[headerIterator], values[headerIterator]);
		}
		return productMap;
	}

	public void generateStepXML(STEPProductInformation stepProductInformation, String path, File inputfile, boolean isFileMoveActive)
			throws Exception {

		stepProductInformation.setExportTime(getCurrentDateForSTEP("yyyy-MM-dd HH:mm:ss"));
		stepProductInformation.setContextID("EnglishUS");
		stepProductInformation.setExportContext("EnglishUS");
		stepProductInformation.setUseContextLocale(false);
		stepProductInformation.setWorkspaceID("Main");
		File file = new File(path);
		File outputFile = DatamigrationCommonUtil.marshallObject(stepProductInformation, file, ORACLE_FINANCIAL_OUTPUTFOLDER, "");
		logger.info("Sending file to step hot folder."+outputFile.getName());
		if(outputFile.getName().startsWith("folderList_")){
			outputFilesFolderList.add(outputFile.getName());
		}else if(outputFile.getName().startsWith("groupList_")){
			outputFilesGroupList.add(outputFile.getName());
		}
		
		DatamigrationCommonUtil.sendFile(outputFile, inputfile,
		 ORACLE_FINANCIAL_FILEDONE_FOLDER, "oraclefinancial", isFileMoveActive, OFSupplierHierarchyScheduler.PUBLISH_ID );
	}

	public void createAndSendSTEPMetaFile(List<String> outputFilesFolderList,List<String> outputFilesGroupList){

		if(!outputFilesFolderList.isEmpty() && !outputFilesGroupList.isEmpty()){
			File metaFile = new File(IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(ORACLE_FINANCIAL_OUTPUTFOLDER))+File.separator+"meta1.txt");
			for(String folderList : outputFilesFolderList){
				DatamigrationCommonUtil.appendWriterFile(metaFile.getPath(), folderList);
			}
			for(String groupList : outputFilesGroupList){
				DatamigrationCommonUtil.appendWriterFile(metaFile.getPath(), groupList);
			}

			DatamigrationCommonUtil.sendFile(metaFile, new File(metaFile.getName()), ORACLE_FINANCIAL_FILEDONE_FOLDER, "oraclefinancial", false, OFSupplierHierarchyScheduler.PUBLISH_ID);
		}
	}
	
	private String getCurrentDateForSTEP(String formatStr) {

		return new SimpleDateFormat(formatStr, Locale.getDefault()).format(new Date());
	}

	private String removeEmptySpace(String str) {

		if (str != null && !str.isEmpty()) {
			return str.replaceAll("\\s+", "");
		}
		return str;
	}

	
}
