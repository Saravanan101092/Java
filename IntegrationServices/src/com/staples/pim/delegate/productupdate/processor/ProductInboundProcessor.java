
package com.staples.pim.delegate.productupdate.processor;

import static com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0012_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A1363_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CNP_MQ_CHANNEL_STEP_IN;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CNP_MQ_HOSTNAME_STEP_IN;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CNP_MQ_PORT_STEP_IN;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CNP_MQ_QUEUEMANAGER_STEP_IN;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CNP_MQ_QUEUENAME_STEP_IN;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.DEBUG_XML_GENERATE_FLAG;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ITEM_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRODUCT_FILEDONE_FOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRODUCT_MAXRECORD_IN_STEP_INPUT_FILE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRODUCT_MERCHID_MAPPING_TRANSFORMATION_ENABLE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRODUCT_OUTPUT_FOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.TRUE_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
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
import com.staples.pcm.stepcontract.beans.NameType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.ProductCrossReferenceType;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pcm.stepcontract.beans.YesNoType;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.MQConnectionManager;
import com.staples.pim.base.util.EmailUtil;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.copyandprint.inbound.domain.StepInboundProcessor;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.productupdate.runner.ProductInboundScheduler;

/**
 * This class is used to process the input Product feed from PIMCore and
 * generate corresponding STEP xml
 * 
 */
public class ProductInboundProcessor {

	private final String						ITEM_TO_VENDOR								= "ItemToVendor";

	private final String						VENDOR_ITEM_PREFIX							= "VendorItem-";

	private final String						SUPPLIER_LINK								= "SupplierLink";

	private final String						SKU_PRODUCT_PARENTID_NON_STOCKED			= "Non-StockedSKUs";

	private final String						ITEM_PRODUCT_PARENTID_NON_STOCKED			= "Non-StockedItems";

	private final String						SKU_PRODUCT_PARENTID_STOCKED				= "StockedSKUs";

	private final String						ITEM_PRODUCT_PARENTID_STOCKED				= "StockedItems";

	private final String						SKU_PRODUCT_ID_PREFIX_NON_STOCKED			= "DMSKU-";

	private final String						ITEM_PRODUCT_ID_PREFIX_NON_STOCKED			= "DMItem-";

	private final String						VENDORITEM_PRODUCT_PARENTID					= "VendorPackagingHierarchy";

	public static final String					VENDORITEM_PRODUCT_ID_PREFIX_NON_STOCKED	= "DMVendorItem-";

	private final String						VENDOR_ITEM									= "VendorItem";

	private final String						PRIMARY_ITEM								= "PrimaryItem";

	private final String						MERCHANDISING_LINK							= "Merchandising Link";

	private final String						TEST_SUPPLIER_PRODUCTS						= "TestSupplierProducts";

	ObjectFactory								objectFactory								= new ObjectFactory();
	ProductInboundTransformation				objInboundTransformation					= new ProductInboundTransformation();
	ProductInboundValidation					objProductInboundValidation					= new ProductInboundValidation();

	static IntgSrvLogger						logger										= IntgSrvLogger
																									.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_PRODUCT);
	public static IntgSrvLogger					ehfLogger									= IntgSrvLogger.getInstance(EHF_LOGGER);
	public static ErrorHandlingFrameworkICD		ehfICD										= null;
	public static ErrorHandlingFrameworkHandler	ehfHandler									= new ErrorHandlingFrameworkHandler();
	public static String						traceId										= null;

	String										inDir										= IntgSrvUtils
																									.reformatFilePath(IntgSrvPropertiesReader
																											.getProperty(DatamigrationAppConstants.PRODUCT_INPUT_FOLDER));
	String										filebadDir									= IntgSrvUtils
																									.reformatFilePath(IntgSrvPropertiesReader
																											.getProperty(DatamigrationAppConstants.PRODUCT_BAD_FOLDER));

	/**
	 * This function is used to read the input xsv file record by record and
	 * fetch the fan id and values
	 * 
	 * @throws ErrorHandlingFrameworkException
	 */
	public void getValuesForFanIdFromXSV() {

		String firstLine = null, secondLine = null;
		String[] attributeIds = null;

		String delimiter = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PRODUCT_INPUTFILE_DELIMITER);
		String transType = null;
		String fileName = null;
		String header = "";
		String logMessage = null;
		Map<String, String> errorRecordsMap = new HashMap<String, String>();

		try {
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_SPRINGBATCH_PUBLISH_ID_PROD);// FIXME
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();

			ehfICD.setAttributeTransactionType(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRANSACTION_TYPE_FILETOFILE);

			File folder = new File(inDir);
			File[] fileSortedBasedOnFIFO = DatamigrationCommonUtil.sortFilesBasedOnFIFO(folder, ProductInboundScheduler.PUBLISH_ID);
			long startTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
			if (fileSortedBasedOnFIFO != null && fileSortedBasedOnFIFO.length > 0) {

				for (int k = 0; k < fileSortedBasedOnFIFO.length; k++) {
					List<Map<String, String>> stepAttributesList = new ArrayList<Map<String, String>>();
					fileName = fileSortedBasedOnFIFO[k].getName();
					if (fileName.endsWith(".xsv")) {

						DatamigrationCommonUtil.printConsole("fileName : " + fileName + " -> File length : "
								+ fileSortedBasedOnFIFO[k].length());

						if (fileSortedBasedOnFIFO[k].length() > 0) {
							FileInputStream fis = new FileInputStream(fileSortedBasedOnFIFO[k]);
							DatamigrationCommonUtil.printConsole(inDir + "\\" + fileName);
							logger.info("Input file : " + inDir + "\\" + fileName);
							BufferedReader br = new BufferedReader(new InputStreamReader(fis));
							header = "";
							errorRecordsMap = new HashMap<String, String>();
							for (int incr = 1; incr <= 5; incr++) {
								firstLine = br.readLine();
								if (firstLine != null) {

									if (incr == 4) {
										attributeIds = firstLine.split(delimiter);
										// this IF logic is implemented for
										// reload the error product after fixing
										// the data issue.
										if (!firstLine.contains("ErrorMessage")) {
											header += firstLine + "~|~ErrorMessage\n";
										} else {
											header += firstLine + "\n";
										}
									} else {
										header += firstLine + "\n";
									}

								} else {
									DatamigrationCommonUtil.printConsole("Header missing");
									DatamigrationCommonUtil.moveFileToFileBad(fileSortedBasedOnFIFO[k], ProductInboundScheduler.PUBLISH_ID);
									break;
								}
							}

							logger.info("Started to read input file");
							DatamigrationCommonUtil.printConsole("Started to read input file");
							logger.info("Header" + header);
							logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
									EHF_MSGTYPE_INFO_NONSLA, "Started to read input file-header " + header,
									EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil
											.getClassName(), ehfICD);
							ehfLogger.info(logMessage);

							secondLine = br.readLine();
							int fileCount = 0;
							int maxRecordCountPerFile = IntgSrvUtils.toInt(IntgSrvPropertiesReader
									.getProperty(PRODUCT_MAXRECORD_IN_STEP_INPUT_FILE));
							boolean isFileCountInc = false;
							int successCount = 0;
							int failureCount = 0;
							while (true && attributeIds != null) {
								String[] attributeValues = null;
								int i = 0, j = 0;
								Map<String, String> stepAttributeMap = new HashMap<String, String>();

								if (secondLine != null) {
									if (!IntgSrvUtils.isEmptyString(secondLine)) {
										attributeValues = secondLine.split(delimiter, -1);

										if (attributeValues.length == attributeIds.length) {

											while (i < attributeIds.length && j < attributeValues.length) {
												stepAttributeMap.put(DatamigrationCommonUtil.trimValues(attributeIds[i]),
														DatamigrationCommonUtil.trimValues(attributeValues[j]));
												i++;
												j++;
											}
											attributeValues = null;
											logger.info("Attribute values fetched from Galaxy xsv file and stored in map");
											logMessage = ehfHandler.getInfoLog(new Date(), traceId,
													IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH5, EHF_MSGTYPE_INFO_NONSLA,
													"Attribute values fetched from Galaxy xsv file and stored in map",
													EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
													DatamigrationCommonUtil.getClassName(), ehfICD);
											ehfLogger.info(logMessage);
											try {
												logger.info("Validation process started");
												logMessage = ehfHandler.getInfoLog(new Date(), traceId,
														IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
														"Validation process started..", EHF_SPRINGBATCH_ITEMUTILITY_USER,
														DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
														ehfICD);
												ehfLogger.info(logMessage);

												String skuID = stepAttributeMap.get("A0012");

												String errorMsg = objProductInboundValidation.validationProcess(stepAttributeMap);

												logger.info("Validation process completed");
												logMessage = ehfHandler.getInfoLog(new Date(), traceId,
														IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
														"Validation process completed..", EHF_SPRINGBATCH_ITEMUTILITY_USER,
														DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
														ehfICD);
												ehfLogger.info(logMessage);
												if (IntgSrvUtils.isEmptyString(errorMsg)) {
													logger.info("Transformation process started");
													logMessage = ehfHandler.getInfoLog(new Date(), traceId,
															IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
															EHF_MSGTYPE_INFO_NONSLA, "Transformation process started..",
															EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
															DatamigrationCommonUtil.getClassName(), ehfICD);
													ehfLogger.info(logMessage);

													stepAttributeMap = objInboundTransformation.transformationProcess(stepAttributeMap);

													logger.info("Transformation process completed");
													logMessage = ehfHandler.getInfoLog(new Date(), traceId,
															IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
															EHF_MSGTYPE_INFO_NONSLA, "Transformation process completed..",
															EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
															DatamigrationCommonUtil.getClassName(), ehfICD);
													ehfLogger.info(logMessage);
													++successCount;

												} else {

													errorRecordsMap.put(skuID, errorMsg);
													DatamigrationCommonUtil.printConsole(skuID + " errorMsg :::: " + errorMsg);

													generateReportFile(fileName, header, errorMsg, secondLine);
													logger.info("Error report generated");
													logMessage = ehfHandler.getInfoLog(new Date(), traceId,
															IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
															EHF_MSGTYPE_INFO_NONSLA, "Error report generated..",
															EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
															DatamigrationCommonUtil.getClassName(), ehfICD);
													ehfLogger.info(logMessage);
													secondLine = br.readLine();
													++failureCount;
													continue;
												}

											} catch (Exception e) {
												logger.error(e);
												IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
														ProductInboundScheduler.PUBLISH_ID);
												logMessage = ehfHandler.getErrorLog(new Date(), traceId,
														IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2, e,
														EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
														DatamigrationCommonUtil.getClassName(), ehfICD);
												ehfLogger.info(logMessage);
												e.printStackTrace();
												secondLine = br.readLine();
												continue;
											}

											stepAttributesList.add(stepAttributeMap);

											if (maxRecordCountPerFile != 0 && stepAttributesList.size() == maxRecordCountPerFile) {
												stepXMLGenerateTransfer(stepAttributesList, fileSortedBasedOnFIFO[k], ++fileCount,
														transType, secondLine != null);
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
											generateReportFile(fileName, header, error, secondLine);
											secondLine = br.readLine();
											continue;
										}
									} else {
										secondLine = br.readLine();
										continue;
									}
								} else {
									logger.info("stepAttributesList size:::::::::::::::" + stepAttributesList.size());
									logMessage = ehfHandler.getInfoLog(new Date(), traceId,
											IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
											"stepAttributesList size:::::::::::::::" + stepAttributesList.size(),
											EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
											DatamigrationCommonUtil.getClassName(), ehfICD);
									ehfLogger.info(logMessage);
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
								stepXMLGenerateTransfer(stepAttributesList, fileSortedBasedOnFIFO[k], fileCount, transType, true);
							}

							// Report generation for the success/failure records
							long endTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
							generateErrorReportSummary(errorRecordsMap, filebadDir, fileName, successCount, (endTime - startTime),
									failureCount);

						} else {
							logger.error("File is empty");
							DatamigrationCommonUtil.moveFileToFileBad(fileSortedBasedOnFIFO[k], ProductInboundScheduler.PUBLISH_ID);
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
			} else {
				DatamigrationCommonUtil.printConsole("fileSortedBasedOnFIFO object is null/empty");
			}

		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ProductInboundScheduler.PUBLISH_ID);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ProductInboundScheduler.PUBLISH_ID);
		} catch (ErrorHandlingFrameworkException e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ProductInboundScheduler.PUBLISH_ID);
		}
	}

	// new method version
	// two-------------------------------------------------------------------------------
	public void productInboundProcessor() {

		try {
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_SPRINGBATCH_PUBLISH_ID_PROD);
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();

			ehfICD.setAttributeTransactionType(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRANSACTION_TYPE_FILETOFILE);

			File inputFolder = new File(inDir);
			File[] fileSortedBasedOnFIFO = DatamigrationCommonUtil.sortFilesBasedOnFIFO(inputFolder, ProductInboundScheduler.PUBLISH_ID);

			DatamigrationCommonUtil.printConsole("Total no of files in input folder=" + fileSortedBasedOnFIFO.length);
			if (fileSortedBasedOnFIFO != null && fileSortedBasedOnFIFO.length > 0) {

				for (int filesIterator = 0; filesIterator < fileSortedBasedOnFIFO.length; filesIterator++) {

					if (fileSortedBasedOnFIFO[filesIterator].getName().endsWith(".xsv")) {

						if (fileSortedBasedOnFIFO[filesIterator].length() > 0) {

							readInputXSV(fileSortedBasedOnFIFO[filesIterator]);
						} else {

							DatamigrationCommonUtil.printConsole("File length is Zero!!");
							DatamigrationCommonUtil.moveFileToFileBad(fileSortedBasedOnFIFO[filesIterator],
									ProductInboundScheduler.PUBLISH_ID);
						}
					}
				}

			}
		} catch (ErrorHandlingFrameworkException e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ProductInboundScheduler.PUBLISH_ID);
		}
	}

	/**
	 * @param fileBeingProcessed
	 */
	public void readInputXSV(File fileBeingProcessed) {

		FileReader fileReader;
		long lineNumber = 0;
		BufferedReader reader;
		String tempString;
		String allHeaders = "";
		String[] header = null;
		String[] values;
		String skuID;
		String transType = null;

		long startTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
		int succeededRecords = 0;
		int failedRecords = 0;
		int headerMismatchRecord = 0;
		int noOfRecordsProcessed = 0;
		int outputFilePart = 1;
		int cnpFileCount=0;
		Map<String, String> errorMap = new HashMap<String, String>();
		Map<String, String> productMap;

		List<Map<String, String>> allProducts = new ArrayList<Map<String, String>>();

		String delimiter = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.PRODUCT_INPUTFILE_DELIMITER);
		int maxRecordCountPerFile = IntgSrvUtils.toInt(IntgSrvPropertiesReader.getProperty(PRODUCT_MAXRECORD_IN_STEP_INPUT_FILE));

		try {

			fileReader = new FileReader(fileBeingProcessed);
			reader = new BufferedReader(fileReader);
			DatamigrationCommonUtil.printConsole("file being processed=" + fileBeingProcessed.getName());
			while ((tempString = reader.readLine()) != null) {
				if (!tempString.equals("")) {
					if (lineNumber < 5) {
						allHeaders += tempString + "\n";
						// DatamigrationCommonUtil.printConsole(allHeaders);
						// get headers
						if (lineNumber == 3) {

							header = tempString.split(delimiter, -1);
							// DatamigrationCommonUtil.printConsole("the headers are "+tempString);
						}
					} else {
						try {
							// actual records processing
							productMap = new HashMap<String, String>();

							// System.out.println("Current record.."+lineNumber);
							values = tempString.split(delimiter, -1);
							if ((header != null) && (header.length == values.length)) {
								// get values as Map
								productMap = getRecordAsMap(values, header, productMap);
								// get SKU
								skuID = productMap.get("A0012");
								if(!IntgSrvUtils.isNullOrEmpty(productMap.get("A0484")))
									{
									if("A".equalsIgnoreCase(productMap.get("A0484"))) {
									transType=DatamigrationAppConstants.ITEMCREATE;
									}
									if("U".equalsIgnoreCase(productMap.get("A0484"))) {
										transType=DatamigrationAppConstants.ITEMUPDATE;
										}
									
								}
								if (productMap.containsKey("A0026")
										&& ("471".equalsIgnoreCase(productMap.get("A0026"))
												|| "476".equalsIgnoreCase(productMap.get("A0026")) || "479".equalsIgnoreCase(productMap
												.get("A0026")))) {
									String logMessage = null;
									//String transType = "CreateUpdate";
									StepInboundProcessor inboundProcessor = new StepInboundProcessor();
									//DM Transformations applid to CnP as well
									//productMap = inboundProcessor.transformationProcess(productMap);								
									productMap = objInboundTransformation.transformationProcess(productMap);
									
									if(productMap.get("A0026")!=null)
									{
											String valueA0026 = productMap.get("A0026");
											productMap.put("ClassCode", valueA0026);
											productMap.remove("A0026");
									}
									logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
											"Transformation is completed.", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil
													.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
									ehfLogger.info(logMessage);

									// 4. Feed the pim values in
									// stepinformation.java
									STEPProductInformation stepProductInformation = inboundProcessor.updatedStepProdInformation(productMap,
											transType);
									
									logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
											"Update PIMCore data into StepProdInformation Object..", EHF_SPRINGBATCH_ITEMUTILITY_USER,
											DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
									ehfLogger.info(logMessage);

									// 5. Generate the step.xml using marsheller

									logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
											"Step XML generation start...", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil
													.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
									ehfLogger.info(logMessage);

//									String stepXml = inboundProcessor.generateStepXML(stepProductInformation);
									File outputFile;
									if(cnpFileCount>0) {
										File inputFile=new File(IntgSrvUtils.reformatFilePath(fileBeingProcessed.getName().substring(0, (fileBeingProcessed.getName().length() - 4))+"-"+cnpFileCount+".xsv"));
										outputFile = DatamigrationCommonUtil.marshallObject(stepProductInformation, inputFile, DatamigrationAppConstants.PRODUCT_OUTPUT_FOLDER_CNP, DatamigrationAppConstants.PUBLISH_ID_UPDATE);
									}
									else {
										//File inputFile=new File(IntgSrvUtils.reformatFilePath(fileBeingProcessed.getName()));
									outputFile = DatamigrationCommonUtil.marshallObject(stepProductInformation, fileBeingProcessed, DatamigrationAppConstants.PRODUCT_OUTPUT_FOLDER_CNP, DatamigrationAppConstants.PUBLISH_ID_UPDATE);
									}
									
									logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
											"Step XML generation end...", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil
													.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
									ehfLogger.info(logMessage);
									// 6. Publish the step.xml to step Queue
									try {

//										logger.info("Converted XML for Step Queue : " + stepXml);
//										DatamigrationCommonUtil.printConsole("Converted XML for Step Queue : " + stepXml);
										// Step XML publish call starts...

										if (outputFile != null) {
											ehfICD.setAttributeTransactionType("Spring-to-Queue");
											logMessage = ehfHandler
													.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
															"Step XML publish start...", EHF_SPRINGBATCH_ITEMUTILITY_USER,
															DatamigrationCommonUtil.getMethodName(),
															DatamigrationCommonUtil.getClassName(), ehfICD);
											ehfLogger.info(logMessage);

											// construct service item object
								/*			StepTransmitterBean serviceBean = new StepTransmitterBean();
											serviceBean.setMessage(stepXml);
											serviceBean.setMqChannel(IntgSrvPropertiesReader.getProperty(CNP_MQ_CHANNEL_STEP_IN));
											serviceBean.setMqHostName(IntgSrvPropertiesReader.getProperty(CNP_MQ_HOSTNAME_STEP_IN));
											serviceBean.setMqPport(Integer.parseInt(IntgSrvPropertiesReader
													.getProperty(CNP_MQ_PORT_STEP_IN)));
											serviceBean.setMqQueueManager(IntgSrvPropertiesReader.getProperty(CNP_MQ_QUEUEMANAGER_STEP_IN));
											serviceBean.setMqQueueName(IntgSrvPropertiesReader.getProperty(CNP_MQ_QUEUENAME_STEP_IN));
											// StepInboundPublisher.messageSender(stepXml,transType);

											// get connection manager
											MQConnectionManager mqconnectionmanager = MQConnectionManager.getInstance(serviceBean);
											mqconnectionmanager.putMessage(serviceBean);
											mqconnectionmanager.close();*/
											if(DatamigrationAppConstants.ITEMCREATE.equalsIgnoreCase(transType)) {
											DatamigrationCommonUtil.sendFile(outputFile, fileBeingProcessed, PRODUCT_FILEDONE_FOLDER, "4_I", true, DatamigrationAppConstants.PUBLISH_ID_UPDATE);
											}
											else if(DatamigrationAppConstants.ITEMUPDATE.equalsIgnoreCase(transType)) {
												DatamigrationCommonUtil.sendFile(outputFile, fileBeingProcessed, PRODUCT_FILEDONE_FOLDER, "4_U", true, DatamigrationAppConstants.PUBLISH_ID_UPDATE);
												}
											logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3,
													EHF_MSGTYPE_INFO_NONSLA, "Step XML publish end.. Published successfully...",
													EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
													DatamigrationCommonUtil.getClassName(), ehfICD);
											ehfLogger.info(logMessage);
											logger.info("Step XML has been sent.");

											DatamigrationCommonUtil.printConsole("Step XML has been sent.");

											if (TRUE_STR.equalsIgnoreCase(IntgSrvPropertiesReader.getProperty(DEBUG_XML_GENERATE_FLAG))) {
												inboundProcessor.xmlFileCreateForDebug(stepProductInformation, transType);
											}
											cnpFileCount+=1;
										} else {
											logger.info("PIMCore message is not processed.");
										}
									} catch (Exception e) {
									}
									// ------------------------

								} else {
									// System.out.println("validating record..");
									String errorMsg = objProductInboundValidation.validationProcess(productMap);
									if (IntgSrvUtils.isEmptyString(errorMsg)) {
										// System.out.println("transforming.."+noOfRecordsProcessed);
										productMap = objInboundTransformation.transformationProcess(productMap);
										// System.out.println("transformed record"+succeededRecords);
										succeededRecords++;
										allProducts.add(productMap);
										noOfRecordsProcessed++;

										if (noOfRecordsProcessed == maxRecordCountPerFile) {
											// System.out.println("transferring..");
											stepXMLGenerateTransfer(allProducts, fileBeingProcessed, outputFilePart, "", false);
											outputFilePart++;
											noOfRecordsProcessed = 0;
											allProducts = new ArrayList<Map<String, String>>();
										}
									} else {
										errorMap.put(skuID, errorMsg);
										// System.out.println("Record failed.."+errorMsg);
										failedRecords++;
										generateReportFile(fileBeingProcessed.getName(), allHeaders, errorMsg, tempString);
									}
								}
							} else {
								// System.out.println("No of headers do not match the No of values");
								String error = "No of headers do not match the No of values";
								errorMap.put("HeaderMismatch" + (++headerMismatchRecord), error);
								generateReportFile(fileBeingProcessed.getName(), allHeaders, error, tempString);
								failedRecords++;
							}

						} catch (ParseException e) {
							errorMap.put("Exception", e.getMessage());
							generateReportFile(fileBeingProcessed.getName(), allHeaders, e.getMessage(), tempString);
							failedRecords++;
							e.printStackTrace();
							IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ProductInboundScheduler.PUBLISH_ID);
						} catch (Exception e) {
							errorMap.put("Exception", e.getMessage());
							generateReportFile(fileBeingProcessed.getName(), allHeaders, e.getMessage(), tempString);
							failedRecords++;
							e.printStackTrace();
							IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ProductInboundScheduler.PUBLISH_ID);
						}

					}
					lineNumber++;
				}
			}

			// close open streams
			reader.close();
			fileReader.close();
			if (noOfRecordsProcessed > 0) {

				stepXMLGenerateTransfer(allProducts, fileBeingProcessed, outputFilePart, "", true);
			} else if (noOfRecordsProcessed == 0 && outputFilePart > 1) {
				// moveFile to filedone
				File fileDonePath = new File(IntgSrvUtils.reformatFilePath(PRODUCT_FILEDONE_FOLDER) + fileBeingProcessed.getName());
				if (!fileDonePath.getParentFile().exists()) {
					fileDonePath.getParentFile().mkdirs();
				}
				if (fileBeingProcessed.exists()) {
					try {
						Files.copy(fileBeingProcessed.toPath(), fileDonePath.toPath(), StandardCopyOption.REPLACE_EXISTING);
						fileBeingProcessed.delete();
					} catch (IOException e) {
						e.printStackTrace();
						IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ProductInboundScheduler.PUBLISH_ID);
					}
				}
			} else if (succeededRecords == 0 && fileBeingProcessed.exists()) {
				DatamigrationCommonUtil.moveFileToFileBad(fileBeingProcessed, ProductInboundScheduler.PUBLISH_ID);
			}

			long endTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
			generateErrorReportSummary(errorMap, filebadDir, fileBeingProcessed.getName(), succeededRecords, (endTime - startTime),
					failedRecords);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ProductInboundScheduler.PUBLISH_ID);
		} catch (MessagingException e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ProductInboundScheduler.PUBLISH_ID);
		} catch (IOException e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ProductInboundScheduler.PUBLISH_ID);
		} catch (Exception e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), ProductInboundScheduler.PUBLISH_ID);
		}
	}

	public Map<String, String> getRecordAsMap(String[] values, String[] header, Map<String, String> productMap) {

		for (int headerIterator = 0; headerIterator < header.length; headerIterator++) {
			productMap.put(header[headerIterator], values[headerIterator]);
		}
		return productMap;
	}

	// ----------------------------------------------------------------------------------------------------
	/**
	 * @param splittedList
	 * @param file
	 * @param fileCount
	 * @param transType
	 */
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
			File outputFile = DatamigrationCommonUtil.marshallObject(stepProductInformation, stepXmlFileName, PRODUCT_OUTPUT_FOLDER,
					ProductInboundScheduler.PUBLISH_ID);

			logger.info("Output STEP xml generated generated");
			String logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "Output STEP xml generated generated..", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			DatamigrationCommonUtil.sendFile(outputFile, file, PRODUCT_FILEDONE_FOLDER, "8", isFileCopy,
					DatamigrationAppConstants.EHF_SPRINGBATCH_PUBLISH_ID_PROD);

			logger.info("Output STEP xml moved to the SFTP location");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "Output STEP xml moved to the SFTP location..", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
		}

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
	public STEPProductInformation updatedStepProdInformation(List<Map<String, String>> stepAttributesList, String transType) {

		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = null;
		ProductsType products = null;
		ProductType itemProduct = null;
		ProductType vendorProduct = null;
		// ProductType product = null;
		ProductType skuProduct = null;
		String merchId = null;
		String supplierID = null;

		stepProductInformation = objectFactory.createSTEPProductInformation();
		products = objectFactory.createProductsType();

		ClassificationReferenceType classRefMerch = objectFactory.createClassificationReferenceType();
		ClassificationReferenceType classRefSupplier = objectFactory.createClassificationReferenceType();
		ProductCrossReferenceType prodCrossRefItem = objectFactory.createProductCrossReferenceType();
		ProductCrossReferenceType prodCrossRefSKU = objectFactory.createProductCrossReferenceType();

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
			String vendorId = attributeMap.get("A0075_RET");
			String skuId = attributeMap.get("A0012");
			
			/* Action code set to A by default */
			//String actionCode = attributeMap.get("A0484");
			String actionCode = DatamigrationAppConstants.ACTION_CODE_A;
			
			// PCMP-1408 - name is set it as SKU#
			// String itemName = attributeMap.get("A0018_RET");
			String itemName = attributeMap.get("A0012");
			String isNonStocked = DatamigrationCommonUtil.converYESNOIntoChar(attributeMap.get("A0015"));
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
				} else if (mapObj.getKey().equalsIgnoreCase("A0026")) {
					if (IntgSrvPropertiesReader.getProperty(PRODUCT_MERCHID_MAPPING_TRANSFORMATION_ENABLE).equalsIgnoreCase("true")) {
						merchId = DatamigrationCommonUtil.getClassificationIDValue(mapObj.getValue(), "MerchClassID");
						if (!IntgSrvUtils.isNullOrEmpty(merchId)) {
							/*
							 * valueProduct.setAttributeID("ClassCode");
							 * valueProduct.setContent(mapObj.getValue());
							 * valuesProduct
							 * .getValueOrMultiValueOrValueGroup().add
							 * (valueProduct); } else {
							 */
							classRefMerch.setClassificationID(merchId);
							classRefMerch.setType(MERCHANDISING_LINK);
							itemProduct.getClassificationReference().add(classRefMerch);
						}
						valueProduct.setAttributeID("ClassCode");
						valueProduct.setContent(mapObj.getValue());
						valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);

					} else {
						valueProduct.setAttributeID("ClassCode");
						valueProduct.setContent(mapObj.getValue());
						valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
					}

				} else if (mapObj.getKey().equalsIgnoreCase("A0082")
						&& actionCode.equalsIgnoreCase(DatamigrationAppConstants.ACTION_CODE_A)) {
					valueVendor.setAttributeID("A0082");
					valueVendor.setContent(mapObj.getValue());
					valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0080")
						&& actionCode.equalsIgnoreCase(DatamigrationAppConstants.ACTION_CODE_A)) {
					valueVendor.setAttributeID("A0080");
					valueVendor.setContent(mapObj.getValue());
					valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0084")
						&& actionCode.equalsIgnoreCase(DatamigrationAppConstants.ACTION_CODE_A)) {
					valueVendor.setAttributeID("A0084");
					valueVendor.setContent(mapObj.getValue());
					valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0083")
						&& actionCode.equalsIgnoreCase(DatamigrationAppConstants.ACTION_CODE_A)) {
					valueVendor.setAttributeID("A0083");
					valueVendor.setContent(mapObj.getValue());
					valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0013_RET")
						&& actionCode.equalsIgnoreCase(DatamigrationAppConstants.ACTION_CODE_A)) {
					// valueVendor.setAttributeID("A0013_RET");
					// valueVendor.setContent(mapObj.getValue());
					// valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);
					//
					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0013_NAD")
						&& actionCode.equalsIgnoreCase(DatamigrationAppConstants.ACTION_CODE_A)) {
					// valueVendor.setAttributeID("A0013_NAD");
					// valueVendor.setContent(mapObj.getValue());
					// valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0075_RET")
						&& actionCode.equalsIgnoreCase(DatamigrationAppConstants.ACTION_CODE_A)) {
					valueVendor.setAttributeID("A0075_RET");
					valueVendor.setContent(mapObj.getValue());
					valuesVendor.getValueOrMultiValueOrValueGroup().add(valueVendor);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0075_NAD")
						&& actionCode.equalsIgnoreCase(DatamigrationAppConstants.ACTION_CODE_A)) {

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
					// valueSKU.setAttributeID(mapObj.getKey());
					// valueSKU.setContent(mapObj.getValue());
					// valuesSKU.getValueOrMultiValueOrValueGroup().add(valueSKU);
					// PCMP-1401
					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else if (mapObj.getKey().equalsIgnoreCase("A0504")) {
					// For PCMP-1370
					// valueSKU.setAttributeID(mapObj.getKey());
					// valueSKU.setContent(mapObj.getValue());
					// valuesSKU.getValueOrMultiValueOrValueGroup().add(valueSKU);

					valueProduct.setAttributeID(mapObj.getKey());
					valueProduct.setContent(mapObj.getValue());
					valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
				} else {
					// Error message condition added for re-load the error
					// report after fixing the data issues
					if ((IntgSrvUtils.isNullOrEmpty(mapObj.getValue())&&(mapObj.getKey().equals("A0229")))||(!IntgSrvUtils.isNullOrEmpty(mapObj.getValue()) && !"ErrorMessage".equalsIgnoreCase(mapObj.getValue()))) {
						valueProduct.setAttributeID(mapObj.getKey());
						valueProduct.setContent(mapObj.getValue());
						valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);
					}
				}

			}

			prodCrossRefItem.setProductID(VENDORITEM_PRODUCT_ID_PREFIX_NON_STOCKED + vendorId + "-" + skuId);
			prodCrossRefItem.setType(VENDOR_ITEM);

			prodCrossRefSKU.setProductID(ITEM_PRODUCT_ID_PREFIX_NON_STOCKED + skuId);
			prodCrossRefSKU.setType(PRIMARY_ITEM);

			// Load supplier link from list-of-values.properties
			// System.out.println(attributeMap.get("A0075_RET"));
			//supplierID = DatamigrationCommonUtil.getClassificationIDValue(attributeMap.get("A0075_RET"), "SupplierID");
			supplierID = attributeMap.get("A0075_RET");
			if (!IntgSrvUtils.isNullOrEmpty(supplierID)) {
				classRefSupplier.setClassificationID(supplierID+"Products");
				classRefSupplier.setType(SUPPLIER_LINK);
				itemProduct.getClassificationReference().add(classRefSupplier);
			}
			/*
			 * else {
			 * classRefSupplier.setClassificationID(TEST_SUPPLIER_PRODUCTS); }
			 */

			// A1363 is not received from PIMCore, its newly created in STEP for
			// holding Galaxy SKU number
			valueProduct = objectFactory.createValueType();
			valueProduct.setAttributeID(A1363_STR);
			valueProduct.setContent(attributeMap.get(A0012_STR));
			valuesProduct.getValueOrMultiValueOrValueGroup().add(valueProduct);

			NameType name = objectFactory.createNameType();
			// name.setContent(attributeMap.get(A0019_STR));
			name.setContent(itemName);
			itemProduct.getName().add(name);
			itemProduct.setUserTypeID(ITEM_STR);// Hard coded value
			itemProduct.setID(ITEM_PRODUCT_ID_PREFIX_NON_STOCKED + skuId);
			if (actionCode.equalsIgnoreCase(DatamigrationAppConstants.ACTION_CODE_A)) {
				if ("Y".equalsIgnoreCase(isNonStocked)) {
					itemProduct.setParentID(ITEM_PRODUCT_PARENTID_NON_STOCKED);
				} else if ("N".equalsIgnoreCase(isNonStocked)) {
					itemProduct.setParentID(ITEM_PRODUCT_PARENTID_STOCKED);
				} else {
					DatamigrationCommonUtil.printConsole("Non inventory(A0015) is not a Y/N for SKU#:" + skuId);
				}

				itemProduct.setSelected(Boolean.FALSE);
				itemProduct.setReferenced(Boolean.TRUE);
			}
			/*
			 * if(actionCode.equalsIgnoreCase("U")) { KeyValueType
			 * keyVal=objectFactory.createKeyValueType();
			 * keyVal.setKeyID("Galaxy SKU Number"); keyVal.setContent(skuId);
			 * itemProduct.setKeyValue(keyVal); }
			 */

			itemProduct.getProductOrSequenceProductOrSuppressedProductCrossReference().add(valuesProduct);

			// itemProduct.getClassificationReference().add(classRefSupplier);
			itemProduct.getProductOrSequenceProductOrSuppressedProductCrossReference().add(prodCrossRefItem);

			NameType skunNme = objectFactory.createNameType();
			// skunNme.setContent(SKU_PRODUCT_ID_PREFIX_NON_STOCKED+skuId);
			// Name set to A0018_RET's value
			skunNme.setContent(itemName);
			skuProduct.getName().add(skunNme);
			skuProduct.setUserTypeID(DatamigrationAppConstants.SKU_STR);
			skuProduct.setID(SKU_PRODUCT_ID_PREFIX_NON_STOCKED + skuId);
			if (actionCode.equalsIgnoreCase(DatamigrationAppConstants.ACTION_CODE_A)) {
				if ("Y".equalsIgnoreCase(isNonStocked)) {
					skuProduct.setParentID(SKU_PRODUCT_PARENTID_NON_STOCKED);
				} else if ("N".equalsIgnoreCase(isNonStocked)) {
					skuProduct.setParentID(SKU_PRODUCT_PARENTID_STOCKED);
				}

			}
			/*
			 * if(actionCode.equalsIgnoreCase("U")) { KeyValueType
			 * keyVal=objectFactory.createKeyValueType();
			 * keyVal.setKeyID("SKU Number Key"); keyVal.setContent(skuId);
			 * skuProduct.setKeyValue(keyVal); }
			 */

			skuProduct.getProductOrSequenceProductOrSuppressedProductCrossReference().add(valuesSKU);
			skuProduct.getProductOrSequenceProductOrSuppressedProductCrossReference().add(prodCrossRefSKU);

			// product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(itemProduct);
			// product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(vendorProduct);
			// product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(skuProduct);
			// products.getProduct().add(product);

			if (actionCode.equalsIgnoreCase(DatamigrationAppConstants.ACTION_CODE_A)) {
				NameType vendornNme = objectFactory.createNameType();
				// vendornNme.setContent(vendorId+"-"+itemName);
				vendornNme.setContent(itemName);
				vendorProduct.setUserTypeID(ITEM_TO_VENDOR);
				vendorProduct.getName().add(vendornNme);
				vendorProduct.setID(VENDORITEM_PRODUCT_ID_PREFIX_NON_STOCKED + vendorId + "-" + skuId);
				vendorProduct.setParentID(VENDORITEM_PRODUCT_PARENTID);
				vendorProduct.setSelected(Boolean.FALSE);
				vendorProduct.setReferenced(Boolean.TRUE);
				vendorProduct.getClassificationReference().add(classRefSupplier);
				vendorProduct.getProductOrSequenceProductOrSuppressedProductCrossReference().add(valuesVendor);
				products.getProduct().add(vendorProduct);
			}/*
			 * if(actionCode.equalsIgnoreCase("U")) { KeyValueType
			 * keyVal=objectFactory.createKeyValueType();
			 * keyVal.setKeyID("Galaxy SKU Number"); keyVal.setContent(skuId);
			 * vendorProduct.setKeyValue(keyVal); }
			 */

			products.getProduct().add(itemProduct);

			products.getProduct().add(skuProduct);

			attributeMap = null;
		}
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		// Date
		// time
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
		// value
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
		// coded
		// value
		stepProductInformation.setUseContextLocale(false);// Hard coded value
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded
		// value

		stepProductInformation.setAutoInitiate(YesNoType.N);
		stepProductInformation.setProducts(products);
		// DatamigrationCommonUtil.printConsole("/nTotal no of products::::::::::"+stepProductInformation.getProducts().getProduct().size());
		logger.info("Total no of products::::::::::" + stepProductInformation.getProducts().getProduct().size());
		return stepProductInformation;
	}

	/**
	 * This method is used to generate report file in case validations fail for
	 * any record.
	 * 
	 * @param fileName
	 *            : The error report file name
	 * @param header
	 *            : The first five lines (header) from input
	 * @param errorMsg
	 *            : The actual error message
	 * @param actualRecord
	 *            : The record in which the validations failed
	 * @throws IOException
	 */
	public void generateReportFile(String fileName, String header, String errorMsg, String actualRecord) throws IOException {

		File file = new File(filebadDir + DatamigrationAppConstants.ERROR_REPORT_FILE_PREFIX + fileName.split("\\.")[0] + ".xsv");

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		if (!file.exists()) {
			header = header + "\n";
		} else {
			header = "";
		}
		FileWriter errorWriter = new FileWriter(file, true);
		errorWriter.write(header);
		errorWriter.write(actualRecord + "~|~" + errorMsg + "\n");
		errorWriter.close();
	}

	/**
	 * @param errorRecordsMap
	 * @param errorFilePath
	 * @param fileName
	 * @param successCount
	 * @param milliseconds
	 * @throws IOException
	 * @throws MessagingException
	 */
	public void generateErrorReportSummary(Map<String, String> errorRecordsMap, String errorFilePath, String fileName, int successCount,
			long milliseconds, int failureCount) throws IOException, MessagingException {

		try {
			Map<String, Integer> consolidatedErrorReportMap = new HashMap<String, Integer>();
			boolean isErrorPresent = false;
			File consolidatedErrorReport = null;
			File[] attachmentFiles = new File[2];
			String issueSummary = "";
			File errorReportFile = new File(filebadDir + DatamigrationAppConstants.ERROR_REPORT_FILE_PREFIX + fileName.split("\\.")[0]
					+ ".xsv");

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
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		if (isErrorPresent)
			emailUtil.sendEmail(emailSubject, emailMessage, toAddress, attachmentFiles);
		// else
		// emailUtil.sendEmail(emailSubject, emailMessage, toAddress);

		// JavaMailSender mailSender=new JavaMailSenderImpl();
		// MimeMessage message = mailSender.createMimeMessage();
		// MimeMessageHelper helper = new MimeMessageHelper(message, true);
		// helper.setFrom("priyanka191292@gmail.com");
		// helper.setTo(toAddress);
		// helper.setSubject("Birthday wishes");
		// helper.setText("Hi"+emailMessage);
		// mailSender.send(message);
		// System.out.println("mail sent");
	}

	public static void main(String[] args) throws IOException {

		ProductInboundProcessor objOnetimeProcessor = new ProductInboundProcessor();
		objOnetimeProcessor.productInboundProcessor();
	}

}
