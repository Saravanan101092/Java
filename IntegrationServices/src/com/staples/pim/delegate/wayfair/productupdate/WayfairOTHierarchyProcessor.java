
package com.staples.pim.delegate.wayfair.productupdate;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRODUCT_MAXRECORD_IN_STEP_INPUT_FILE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_COMPONENT_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ECO_SYSTEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ICD_CLASSNAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pcm.stepcontract.beans.ClassificationReferenceType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.EmailUtil;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.productupdate.processor.ProductInboundTransformation;
import com.staples.pim.delegate.productupdate.processor.ProductInboundValidation;
import com.staples.pim.delegate.wayfair.productupdate.runner.WayfairProductScheduler;

public class WayfairOTHierarchyProcessor {

	private final String						WEBSITE_LINK							= "WebsiteLink";
	private final String						QUILWEBSITE_LINK						= "QuillWebsiteLink";
	ObjectFactory								objectFactory							= new ObjectFactory();
	ProductInboundTransformation				objInboundTransformation				= new ProductInboundTransformation();
	ProductInboundValidation					objProductInboundValidation				= new ProductInboundValidation();

	static IntgSrvLogger						logger									= IntgSrvLogger
																								.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_PRODUCT);
	/*
	 * public static IntgSrvLogger ehfLogger =
	 * IntgSrvLogger.getInstance(EHF_LOGGER); public static
	 * ErrorHandlingFrameworkICD ehfICD = null; public static
	 * ErrorHandlingFrameworkHandler ehfHandler = new
	 * ErrorHandlingFrameworkHandler(); public static String traceId = null;
	 */
	String										inDir									= IntgSrvUtils
																								.reformatFilePath(IntgSrvPropertiesReader
																										.getProperty(DatamigrationAppConstants.OT_HIERARCHY_INPUT_FOLDER));
	String										filebadDir								= IntgSrvUtils
																								.reformatFilePath(IntgSrvPropertiesReader
																										.getProperty(DatamigrationAppConstants.OT_HIERARCHY_BAD_FOLDER));

	private static final String					CLASS_ID_REFERENCE_FILE_CURRENT_PATH	= IntgSrvUtils
																								.reformatFilePath(IntgSrvPropertiesReader
																										.getProperty(DatamigrationAppConstants.CLASS_ID_REFERENCE_FILE_CURRENT_PATH));
	private static final String					CLASS_ID_REFERENCE_FILE_OLD_PATH		= IntgSrvUtils
																								.reformatFilePath(IntgSrvPropertiesReader
																										.getProperty(DatamigrationAppConstants.CLASS_ID_REFERENCE_FILE_OLD_PATH));

	private final String						SKU_PRODUCT_ID_PREFIX_WAYFAIR			= "DMSKU-";

	String										delimiter								= "<s-k-r>";																					// IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PRODUCT_INPUTFILE_DELIMITER);

	public static IntgSrvLogger					ehfLogger								= IntgSrvLogger
																								.getInstance(DatamigrationAppConstants.EHF_LOGGER_WAYFAIR);

	public static ErrorHandlingFrameworkICD		ehfICD									= null;

	public static ErrorHandlingFrameworkHandler	ehfHandler								= new ErrorHandlingFrameworkHandler();

	public static String						traceId									= null;

	String										logMessage								= null;

	
	public static void main(String[] args) {

		new WayfairOTHierarchyProcessor().oneTimeQuillHierarchyProcess();

	}

	public void oneTimeQuillHierarchyProcess() {

		
		
		Map<String, String> referenceMap = getClassIDsFromReferenceFile();

		long startTime = DatamigrationCommonUtil.getCurrentMilliSeconds();

		int maxRecordCountPerFile = IntgSrvUtils.toInt(IntgSrvPropertiesReader.getProperty(PRODUCT_MAXRECORD_IN_STEP_INPUT_FILE));

		String firstLine = null, secondLine = null;
		String[] attributeIds = { "A0547", "A0560", "A0561", "A0562", "A0563", "BUNO" };

		String transType = null;
		String fileName = null;
		String header = "";

		Map<String, String> errorRecordsMap = new HashMap<String, String>();
		try {
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(SPRINGBATCH_ECO_SYSTEM, SPRINGBATCH_COMPONENT_ID,
					SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_PRODUCT);
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();
			File folder = new File(inDir);

			// Sort File list
			File[] fileSortedBasedOnFIFO = DatamigrationCommonUtil.sortFilesBasedOnFIFO(folder, WayfairProductScheduler.PUBLISH_ID);

			// readFiles
			if (fileSortedBasedOnFIFO != null && fileSortedBasedOnFIFO.length > 0)
				for (File fileObj : fileSortedBasedOnFIFO) {

					List<Map<String, String>> stepAttributesList = new ArrayList<Map<String, String>>();
					fileName = fileObj.getName();
					DatamigrationCommonUtil.printConsole("ProductHierarchy >>Processing File : " + fileName);
					if (fileName.endsWith(".xsv") || fileName.endsWith(".dsv")) {

						DatamigrationCommonUtil.printConsole("fileName : " + fileName + " -> File length : " + fileObj.length());

						if (fileObj.length() > 0) {
							FileInputStream fis = new FileInputStream(fileObj);
							DatamigrationCommonUtil.printConsole(inDir + "\\" + fileName);
							logger.info("Input file : " + inDir + "\\" + fileName);
							BufferedReader br = new BufferedReader(new InputStreamReader(fis));
							header = "";
							errorRecordsMap = new HashMap<String, String>();
							for (int incr = 1; incr <= 1; incr++) {
								firstLine = br.readLine();
								if (firstLine != null) {

								} else {
									DatamigrationCommonUtil.printConsole("Header missing");
									DatamigrationCommonUtil.moveFileToFileBad(fileObj, WayfairProductScheduler.PUBLISH_ID);
									break;
								}
							}

							logger.info("Started to read input file");
							DatamigrationCommonUtil.printConsole("Started to read input file");
							logger.info("Header" + header);
							/*
							 * logMessage = ehfHandler.getInfoLog(new Date(),
							 * traceId, IntgSrvErrorHandlingFrameworkConstants.
							 * EHF_ERROR_PATH4, EHF_MSGTYPE_INFO_NONSLA,
							 * "Started to read input file-header " + header,
							 * EHF_SPRINGBATCH_ITEMUTILITY_USER,
							 * DatamigrationCommonUtil.getMethodName(),
							 * DatamigrationCommonUtil .getClassName(), ehfICD);
							 * ehfLogger.info(logMessage);
							 */

							secondLine = br.readLine();
							int fileCount = 0;
							boolean isFileCountInc = false;
							int successCount = 0;
							int failureCount = 0;
							while (true && attributeIds != null) {
								String[] attributeValues = null;
								int i = 0, j = 0;
								Map<String, String> stepAttributeMap = new HashMap<String, String>();

								if (secondLine != null) {
									if (!IntgSrvUtils.isEmptyString(secondLine)) {
										// secondLine =
										// secondLine.replace("<s-k-r>","|");
										attributeValues = secondLine.split(delimiter, -1);

										if (attributeValues.length == attributeIds.length) {

											while (i < attributeIds.length && j < attributeValues.length) {
												stepAttributeMap.put(DatamigrationCommonUtil.trimValues(attributeIds[i]),
														DatamigrationCommonUtil.trimValues(attributeValues[j]));
												i++;
												j++;
											}
											attributeValues = null;
											logger.info("Attribute values fetched from xsv file and stored in map");
											// DatamigrationCommonUtil.printConsole("successCount :"
											// + successCount);
											try {
												logger.info("Validation process started");

												// String errorMsg =
												// objProductInboundValidation.validationProcess(stepAttributeMap);

												logger.info("Validation process completed");
												// if
												// (IntgSrvUtils.isEmptyString(errorMsg))
												logger.info("Transformation process started");

												// stepAttributeMap =
												// objInboundTransformation.transformationProcess(stepAttributeMap);

												logger.info("Transformation process completed");

												// fetch class id from Lookup
												// file

												// "A0547","A0560","A0561","A0562","A0563","A0564","BUNO"

												stepAttributeMap = getHierarchyClassReferecence(stepAttributeMap, referenceMap);

												++successCount;

											} catch (Exception e) {
												logger.error(e);
												IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
														WayfairProductScheduler.PUBLISH_ID);
												e.printStackTrace();
												secondLine = br.readLine();
												continue;
											}

											stepAttributesList.add(stepAttributeMap);

											if (maxRecordCountPerFile != 0 && stepAttributesList.size() == maxRecordCountPerFile) {
												stepXMLGenerateTransfer(stepAttributesList, fileObj, ++fileCount, transType,
														secondLine != null);
												stepAttributesList = new ArrayList<Map<String, String>>();
												isFileCountInc = true;
											}

											stepAttributeMap = null;
											secondLine = br.readLine();
											continue;
										} else {
											++failureCount;
											String error = "No of values in headers and actual values are NOT equal!.. headers length:"
													+ attributeIds.length + " values length:" + attributeValues.length;
											errorRecordsMap.put("HeaderMismatch", error);
											DatamigrationCommonUtil.printConsole(error);
											// generateReportFile(fileName,
											// header, error, secondLine);
											secondLine = br.readLine();
											continue;
										}
									} else {
										secondLine = br.readLine();
										continue;
									}
								} else {
									logger.info("stepAttributesList size:::::::::::::::" + stepAttributesList.size());
									DatamigrationCommonUtil
											.printConsole("stepAttributesList size:::::::::::::" + stepAttributesList.size());
									break;
								}
							}
							br.close();

							if (stepAttributesList != null && !stepAttributesList.isEmpty()) {
								if (isFileCountInc) {
									++fileCount;
								}
								stepXMLGenerateTransfer(stepAttributesList, fileObj, fileCount, transType, true);
							}

							// Report generation for the success/failure records
							long endTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
							generateErrorReportSummary(errorRecordsMap, DatamigrationCommonUtil.getReportFolderPath(fileObj), fileName, successCount, (endTime - startTime),
									failureCount);

						} else {
							logger.error("File is empty");
							DatamigrationCommonUtil.moveFileToFileBad(fileObj, WayfairProductScheduler.PUBLISH_ID);
							DatamigrationCommonUtil.printConsole("File is empty");
							continue;
						}
						long endTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
						DatamigrationCommonUtil.printConsole("Product file: " + fileName + "  execution time in milliseconds is :"
								+ (endTime - startTime));
						startTime = endTime;
					} else {
						DatamigrationCommonUtil.printConsole("Invalid file format!");
						logger.info("Invalid file format!");
					}
				}

		} catch (Exception e) {
			logger.error(DatamigrationCommonUtil.getClassAndMethodName() + " exception caught: " + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayfairProductScheduler.PUBLISH_ID);
		} catch (ErrorHandlingFrameworkException e) {
			logger.error(DatamigrationCommonUtil.getClassAndMethodName() + " exception caught: " + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayfairProductScheduler.PUBLISH_ID);
		}

	}

	/**
	 * @param stepAttributeMap
	 * @param referenceMap
	 * @return
	 */
	private Map<String, String> getHierarchyClassReferecence(Map<String, String> stepAttributeMap, Map<String, String> referenceMap) {

		String lookupId = stepAttributeMap.get("A0560") + "~" + stepAttributeMap.get("A0561") + "~" + stepAttributeMap.get("A0562") + "~"
				+ stepAttributeMap.get("A0563");

		String classRefId = getValueFromReferenceMap(referenceMap, lookupId);
		stepAttributeMap.put("classRefId", classRefId);

		return stepAttributeMap;
	}

	private void stepXMLGenerateTransfer(List<Map<String, String>> splittedList, File file, int fileCount, String transType,
			boolean isFileCopy) throws Exception {

		DatamigrationCommonUtil.printConsole("splitted list size:" + splittedList.size());
		STEPProductInformation stepProductInformation = updatedStepProdInformation(splittedList, transType);
		logger.info("STEPProductInformation object constructed for marshalling");
		if (stepProductInformation.getProducts().getProduct().size() >= 1) {
			String inputFileName = file.getName();
			if (fileCount > 0) {
				inputFileName = inputFileName.substring(0, (inputFileName.length() - 4));
				inputFileName = inputFileName + "-" + fileCount + ".xml";
			}
			File stepXmlFileName = new File(inputFileName);
			File outputFile = DatamigrationCommonUtil.marshallObject(stepProductInformation, stepXmlFileName,
					DatamigrationAppConstants.OT_HIERARCHY_OUTPUT_FOLDER, WayfairProductScheduler.PUBLISH_ID);

			logger.info("Output STEP xml generated generated");
			String outputFolder = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
					.getProperty(DatamigrationAppConstants.OT_HIERARCHY_DONE_FOLDER));
			DatamigrationCommonUtil.sendFile(outputFile, file, outputFolder, "11_OT_HIE", isFileCopy,
					DatamigrationAppConstants.EHF_SPRINGBATCH_PUBLISH_ID_PROD);

			logger.info("Output STEP xml moved to the SFTP location");
		}

	}

	public STEPProductInformation updatedStepProdInformation(List<Map<String, String>> stepAttributesList, String transType) {

		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = null;
		ProductsType products = null;
		ProductType itemProduct = null;

		stepProductInformation = objectFactory.createSTEPProductInformation();
		products = objectFactory.createProductsType();

		ClassificationReferenceType classRefMerch = objectFactory.createClassificationReferenceType();

		for (int i = 0; i < stepAttributesList.size(); i++) {

			Map<String, String> attributeMap = stepAttributesList.get(i);

			String wayFairNo = attributeMap.get("A0547");
			String classRefId = attributeMap.get("classRefId");

			itemProduct = objectFactory.createProductType();

			if (!IntgSrvUtils.isNullOrEmpty(classRefId)) {

				if ("1".equalsIgnoreCase(attributeMap.get("BUNO"))) {
					classRefMerch.setType(WEBSITE_LINK);
					classRefMerch.setClassificationID("StaplesDotComClass-" + classRefId);
				} else {
					classRefMerch.setType(QUILWEBSITE_LINK);
					classRefMerch.setClassificationID("QULWebClass-" + classRefId);
				}

				itemProduct.getClassificationReference().add(classRefMerch);
			} else {
				// System.out.println("BU NO :: " + attributeMap.get("BUNO") +
				// " : " + classRefId + " :: " + wayFairNo);
			}

			itemProduct.setUserTypeID(DatamigrationAppConstants.SKU_STR);// Hard
																			// coded
																			// value
			itemProduct.setID(SKU_PRODUCT_ID_PREFIX_WAYFAIR + wayFairNo);
			itemProduct.setParentID("WayFairSKUs");

			products.getProduct().add(itemProduct);
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

		stepProductInformation.setProducts(products);
		// DatamigrationCommonUtil.printConsole("/nTotal no of products::::::::::"+stepProductInformation.getProducts().getProduct().size());
		logger.info("Total no of products::::::::::" + stepProductInformation.getProducts().getProduct().size());
		return stepProductInformation;
	}

	public void generateErrorReportSummary(Map<String, String> errorRecordsMap, String errorFilePath, String fileName, int successCount,
			long milliseconds, int failureCount) throws IOException, MessagingException {

		Map<String, Integer> consolidatedErrorReportMap = new HashMap<String, Integer>();
		boolean isErrorPresent = false;
		File consolidatedErrorReport = null;
		File[] attachmentFiles = new File[2];
		String issueSummary = "";
		File errorReportFile = new File(filebadDir + DatamigrationAppConstants.ERROR_REPORT_FILE_PREFIX + fileName.split("\\.")[0] + ".xsv");

		if (errorRecordsMap != null && !errorRecordsMap.isEmpty()) {
			consolidatedErrorReport = new File(errorFilePath + "SKU_ERRORS" + "_" + fileName.split("\\.")[0] + ".xsv");
			FileWriter errorWriter = new FileWriter(consolidatedErrorReport, true);

			// for(int incr=0;incr<=errorRecordsMap.size();incr++)
			for (Map.Entry<String, String> errorEntry : errorRecordsMap.entrySet()) {

				errorWriter.write(errorEntry.getKey() + "~|~" + errorEntry.getValue() + "\n");
				String errorMsg = errorEntry.getValue();
				String[] arrErrorMsg = errorMsg.split(DatamigrationAppConstants.RULE_ID_STR);
				for (int incr = 0; incr < arrErrorMsg.length; incr++) {
					if (consolidatedErrorReportMap.containsKey(arrErrorMsg[incr])) {
						int errorCount = consolidatedErrorReportMap.get(arrErrorMsg[incr]);
						consolidatedErrorReportMap.put(arrErrorMsg[incr], ++errorCount);
					} else {
						consolidatedErrorReportMap.put(arrErrorMsg[incr], 1);
					}
				}
			}
			errorWriter.close();
			isErrorPresent = true;
		}

		int totalErrorsCount = 0;
		File errorReportSummary = new File(errorFilePath + "Execution_Summary.txt");

		if (!errorReportSummary.getParentFile().exists()) {
			errorReportSummary.getParentFile().mkdirs();
		}

		FileWriter errorReportSummaryWriter = new FileWriter(errorReportSummary, true);
		errorReportSummaryWriter.write("Input file Name: " + fileName);
		errorReportSummaryWriter.write("\nExecution Date: " + DatamigrationCommonUtil.getCurrentDateForSTEP());
		errorReportSummaryWriter.write("\n");
		boolean isEmptyKey = false;
		if (consolidatedErrorReportMap != null && !consolidatedErrorReportMap.isEmpty()) {
			for (Map.Entry<String, Integer> errorEntry : consolidatedErrorReportMap.entrySet()) {
				if (IntgSrvUtils.isEmptyString(errorEntry.getKey())) {
					totalErrorsCount = errorEntry.getValue();
					isEmptyKey = true;
				} else {
					DatamigrationCommonUtil.printConsole("Error message : " + errorEntry.getKey() + "--> No.of occurence : "
							+ errorEntry.getValue());
					errorReportSummaryWriter.write("Error message : " + errorEntry.getKey() + "--> No.of occurence : "
							+ errorEntry.getValue() + "\n");
					issueSummary += errorEntry.getKey() + "\n";
				}
			}
		}

		DatamigrationCommonUtil.printConsole("Success records count : " + successCount);
		DatamigrationCommonUtil.printConsole("Failure records count : " + failureCount);
		DatamigrationCommonUtil.printConsole("Turn around (in ms): " + milliseconds);

		errorReportSummaryWriter.write("Success records count : " + successCount + "\n");
		errorReportSummaryWriter.write("Failure records count : " + failureCount + "\n");

		if (consolidatedErrorReportMap.size() != 0 && isEmptyKey) {
			DatamigrationCommonUtil.printConsole("Types of errors count : " + (consolidatedErrorReportMap.size() - 1));
			errorReportSummaryWriter.write("Types of errors count : " + (consolidatedErrorReportMap.size() - 1) + "\n");
		} else {
			DatamigrationCommonUtil.printConsole("Types of errors count : " + consolidatedErrorReportMap.size());
			errorReportSummaryWriter.write("Types of errors count : " + consolidatedErrorReportMap.size() + "\n");
		}

		errorReportSummaryWriter.write("Turn around time(in ms): " + milliseconds + "\n\n");
		errorReportSummaryWriter.close();

		String actualMsg = constructEmailBody(fileName, successCount, totalErrorsCount, issueSummary, isErrorPresent, milliseconds);
		if (isErrorPresent) {
			attachmentFiles[0] = errorReportFile;
			attachmentFiles[1] = consolidatedErrorReport;
		}
		sendEmail("Product Migration Status", actualMsg, IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.TO_ADDRESS),
				attachmentFiles, isErrorPresent, true);
	}

	public String constructEmailBody(String fileName, int successCount, int failureCount, String issueSummary, boolean isErrorPresent,
			long milliseconds) {

		String header = "";
		if (isErrorPresent)
			header = "PFA\n\n";
		// String
		// mailBody=header+"Product Feed Execution summary:\n\n"+"File name :"+fileName+"\n"+"Success records count : "+successCount+"\n"+"Failure records count : "+failureCount+"\n"+"Issue Summary"+issueSummary+"\n";
		String mailBody = "PRODUCT FEED EXECUTION SUMMARY \nFile name : " + fileName + "\nExecution time (in ms) : " + milliseconds
				+ "\nNo. of products success : " + successCount + "\nNo. of products failed : " + failureCount
				+ "\nError log : PFA \nIssue summary : \n" + issueSummary + "";
		// String mailBody="<h1>jigrji</h1>";
		return mailBody;
	}

	public void sendEmail(String emailSubject, String emailMessage, String toAddress, File[] attachmentFiles, boolean isErrorPresent,
			boolean hasHTMLBody) throws MessagingException {

		EmailUtil emailUtil = new EmailUtil();
		// if (isErrorPresent)
		// emailUtil.sendEmail(emailSubject, emailMessage, toAddress,
		// attachmentFiles);

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
				logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
						EHF_MSGTYPE_INFO_NONSLA, "Exception  " + e.getMessage(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
						DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.info(logMessage);
				IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayfairProductScheduler.PUBLISH_ID);

			} catch (IOException e) {
				logger.error("Exception while getting reference File : " + e.getMessage());
				e.printStackTrace();
				logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
						EHF_MSGTYPE_INFO_NONSLA, "Exception  " + e.getMessage(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
						DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.info(logMessage);
				IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayfairProductScheduler.PUBLISH_ID);
			}

		} else {
			logger.error("Reference File to Attribute IDs from attributemetadata Feed not found.");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "Reference File to Attribute IDs from attributemetadata Feed not found.",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);
		}

		return attributeIDsMap;
	}

	public String getValueFromReferenceMap(Map<String, String> referenceMap, String key) {

		for (String AttributeNames : referenceMap.keySet()) {
			if (key.equals(AttributeNames)) {
				return referenceMap.get(key);
			}
		}
		logger.error("Attribute ID not found in the reference file");
		logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
				EHF_MSGTYPE_INFO_NONSLA, "Attribute ID not found in the reference file", EHF_SPRINGBATCH_ITEMUTILITY_USER,
				DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.info(logMessage);
		return "";
	}

}
