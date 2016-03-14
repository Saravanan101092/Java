
package com.staples.pim.delegate.wayfair.priceupdate;

import static com.staples.pim.base.util.IntgSrvAppConstants.FILE_BAD;
import static com.staples.pim.base.util.IntgSrvAppConstants.FILE_DONE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0547;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER_WAYFAIR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WAYFAIRPRICE_OUTPUT_FOLDER;
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
import java.util.Set;

import javax.mail.MessagingException;

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
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.merchupdate.runner.MerchScheduler;
import com.staples.pim.delegate.wayfair.priceupdate.runner.WayFairPriceScheduler;
import com.staples.pim.delegate.wayfair.productupdate.WayfairProductTransformation;

public class WayFairPriceInboundProcessor {

	private static IntgSrvLogger					logger								= IntgSrvLogger
																								.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_WAYFAIRPRICE);
	private static IntgSrvLogger					ehfLogger							= IntgSrvLogger.getInstance(EHF_LOGGER_WAYFAIR);
	private static ErrorHandlingFrameworkICD		ehfICD								= null;
	private static ErrorHandlingFrameworkHandler	ehfHandler							= new ErrorHandlingFrameworkHandler();
	private static String							traceId								= null;
	public static final String						WAYFAIR_PRICING_HEADER				= "wayfair.pricing.headers";
	public static final String						headerString						= IntgSrvPropertiesReader
																								.getProperty(WAYFAIR_PRICING_HEADER);
	private static final String						ParentID							= "WayFairItems";
	private static final String						UserTypeID							= "Item";
	private final static String						ITEM_PRODUCT_ID_PREFIX_WAYFAIRPRICE	= "DMItem-";

	public static int							successCount;
	public static int							failureCount;
	
	public static void wayfairPriceProcessor(File fileName) throws Exception {

		String logMessage = null;

		try {
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIRPRICE);// FIXME
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();
			ehfICD.setAttributeTransactionType(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRANSACTION_TYPE_FILETOFILE);

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Started to read input file for WayfairPricing", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			logger.info("Process is Started");
			logger.info("Started to read input file");
			Map<String, List<Map<String, String>>> mapContainingWayfairPriceLists = readwayfairPriceIncomingFile(fileName);
			logger.info("Got input file hearders and values in the MAP");

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH5,
					EHF_MSGTYPE_INFO_NONSLA, "Attribute values fetched from Wayfairpricing xsv file and stored in map",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			
		} catch (ErrorHandlingFrameworkException e) {
			logger.error("Caught an exception in readwayfairPriceIncomingFile() :" + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), MerchScheduler.PUBLISH_ID);
		}
	}

	public static Map<String, List<Map<String, String>>> readwayfairPriceIncomingFile(File fileName) {

		successCount = 0;
		failureCount = 0;
		long startTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
		
		boolean useDefaultHeader = true;
		int fileCount = 1;
		int maxRecordsPerFile = Integer.parseInt(IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.WAYFAIR_PRODUCT_MAX_RECORDS_PER_FILE));
		Map<String, List<Map<String, String>>> mapContainingWayfairPriceLists = new HashMap<String, List<Map<String, String>>>();
		List<Map<String, String>> listWayfairPriceVOs = new ArrayList<Map<String, String>>();

		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			String tempString;
			long lineNumber = 0;
			String headers[] = null;
			String headerStr = "";
			logger.info("Started to create map");
			String delimiter = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.WAYFAIRPRICE_INPUTFILE_DELIMITER);
			DatamigrationCommonUtil.printConsole("Pricing Delimiter :: "+delimiter);
			if (useDefaultHeader) {
				headers = headerString.split(delimiter, -1);
				while ((tempString = br.readLine()) != null) {
					if (!"".equals(tempString)) {
						if (lineNumber > 0) {
							String values[] = tempString.split(delimiter, -1);
							Map<String, String> mapObject = DatamigrationCommonUtil.getValuesInMap(headers, values);
							DatamigrationCommonUtil.printConsole("trace 0 : "+mapObject);
							if (mapObject != null && !mapObject.isEmpty()) {
								listWayfairPriceVOs.add(mapObject);
								successCount++;
							} else {
								failureCount++;
								DatamigrationCommonUtil.generateReportFile(fileName.getParentFile().getParentFile().getPath() + "/"
										+ FILE_BAD + "/", fileName.getName(), headerStr, "No.of values in this record is not correct!",
										tempString);
							}
						}
						DatamigrationCommonUtil.printConsole(" trace ..."+lineNumber);
						lineNumber++;
						if (listWayfairPriceVOs.size() == maxRecordsPerFile) {
							createAndTransferSTEPXML(listWayfairPriceVOs, fileName, fileCount, "", false);
							listWayfairPriceVOs = new ArrayList<Map<String, String>>();
							fileCount++;
						}
					}
				}
				DatamigrationCommonUtil.printConsole("lineNumber:"+lineNumber);
			}
			if (listWayfairPriceVOs.size() > 0) {
				createAndTransferSTEPXML(listWayfairPriceVOs, fileName, fileCount, "", true);
			}
			fr.close();
		} catch (IOException e) {
			logger.error("Caught an exception in readwayfairPriceIncomingFile() :" + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayFairPriceScheduler.PUBLISH_ID);
		} catch(Exception e){
			e.printStackTrace();
		}
		mapContainingWayfairPriceLists.put("WayfairPriceRecords", listWayfairPriceVOs);
		
		long endTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
		try {
			DatamigrationCommonUtil.generateExecutionSummary("Wayfair Pricing feed ", DatamigrationCommonUtil.getReportFolderPath(fileName), fileName.getName(), successCount,
									(endTime - startTime), failureCount);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mapContainingWayfairPriceLists;
	}

	private static void createAndTransferSTEPXML(List<Map<String, String>> listWayfairPriceVOs, File fileName, int fileCount,
			String string, boolean b) {
		
		try{

			logger.info("Map is Created");
			if ((listWayfairPriceVOs != null && !listWayfairPriceVOs.isEmpty())) {

				if (listWayfairPriceVOs.size() > 0) {
					// listWayfairPriceVOs =
					// transformationProcess(listWayfairPriceVOs);
					logger.info("Creation of STEPXML has been  started with the MAP");
					String inputFileName = fileName.getName();
					if (fileCount > 1) {
						inputFileName = inputFileName.substring(0, (inputFileName.length() - 4));
						inputFileName = inputFileName + "-" + fileCount + ".xml";

					}

					STEPProductInformation stepPrdInfo = wayfairPriceStepXMLGenerator(listWayfairPriceVOs, fileName);
					logger.info("Total no of product : " + stepPrdInfo.getProducts().getProduct().size());
					logger.info("Creation of STEPXML is completed");

					String logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
							EHF_MSGTYPE_INFO_NONSLA, "STEPProductInformation object for Wayfairpricing update constructed for marshalling",
							EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
							DatamigrationCommonUtil.getClassName(), ehfICD);
					ehfLogger.info(logMessage);

					logger.info("Started to writing the STEPXML file into output folder");
					File outputFile = DatamigrationCommonUtil.marshallObject(stepPrdInfo, new File(inputFileName), WAYFAIRPRICE_OUTPUT_FOLDER,
							WayFairPriceScheduler.PUBLISH_ID);

					logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
							EHF_MSGTYPE_INFO_NONSLA, "STEP xml generated for wayfairpricing update", EHF_SPRINGBATCH_ITEMUTILITY_USER,
							DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
					ehfLogger.info(logMessage);

					logger.info("Sending STEPXML files to Server(Hotfolder)");
					DatamigrationCommonUtil.sendFile(outputFile, fileName, fileName.getParentFile().getParentFile().getPath() + "/"
							+ FILE_DONE + "/", "11_PRICING", true, DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIRPRICE);

					logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
							EHF_MSGTYPE_INFO_NONSLA, "STEP xml for wayfairpricing update moved to the SFTP location",
							EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
							DatamigrationCommonUtil.getClassName(), ehfICD);
					ehfLogger.info(logMessage);

					logger.info("Process is Ended");
				}

				else {
					DatamigrationCommonUtil.moveFileToFileBad(fileName, WayFairPriceScheduler.PUBLISH_ID);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static STEPProductInformation wayfairPriceStepXMLGenerator(List<Map<String, String>> listOfWayfairPrice, File fileName)
			throws Exception {

		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepPrdInfo = objectFactory.createSTEPProductInformation();
		ProductsType Products = objectFactory.createProductsType();
		ProductType product = objectFactory.createProductType();
		logger.info("Processing Record(MAP)");
		for (Map<String, String> wayfairPriceMap : listOfWayfairPrice) {
			if (wayfairPriceMap != null && !wayfairPriceMap.isEmpty()) {
				wayfairPriceMap = WayfairProductTransformation.transformationForPricing(wayfairPriceMap);
				product = createProduct(wayfairPriceMap);
				Products.getProduct().add(product);
			} else {
				DatamigrationCommonUtil.printConsole("Map is empty or null");
			}
		}

		stepPrdInfo.setProducts(Products);
		stepPrdInfo.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepPrdInfo.setContextID(CONTEXT_ID_VALUE); // Hard coded value
		stepPrdInfo.setExportContext(EXPORT_CONTEXT_VALUE);// Hard coded value
		stepPrdInfo.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded value
//		stepPrdInfo.setAutoApprove(YesNoType.Y);
		return stepPrdInfo;
	}

	private static ProductType createProduct(Map<String, String> wayfairPriceMap) throws Exception {

		ObjectFactory objectFactory = new ObjectFactory();

		ProductType product = objectFactory.createProductType();
		ValuesType Values = objectFactory.createValuesType();

		String categoryLevel = wayfairPriceMap.get(A0547);
		String ItemId = ITEM_PRODUCT_ID_PREFIX_WAYFAIRPRICE + categoryLevel;

		wayfairPriceMap.remove(A0547);
		/*wayfairPriceMap.remove(categoryLevel);*/

		Set<String> mySet = wayfairPriceMap.keySet();
		for (String myStr : mySet) {

			ValueType Value = objectFactory.createValueType();
			Value.setAttributeID(myStr);
			Value.setContent((String) wayfairPriceMap.get(myStr));
			Values.getValueOrMultiValueOrValueGroup().add(Value);
		}

		product.setParentID(ParentID);
		product.setUserTypeID(UserTypeID);
		product.setID(ItemId);
		product.setSelected(Boolean.FALSE);
		product.setReferenced(Boolean.TRUE);
		product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(Values);

		return product;
	}
	/*
	 * private static List<Map<String, String>> transformationProcess(
	 * List<Map<String, String>> listWayfairPriceVOs) {
	 * 
	 * List<Map<String, String>> listWayfairPriceVOs1 = new
	 * ArrayList<Map<String, String>>(); for (Map<String, String>
	 * wayfairPriceMap : listWayfairPriceVOs) { for (int i = 0; i <
	 * wayfairPriceMap.size(); i++) { if (wayfairPriceMap.get(A0197) != null) {
	 * String value = DatamigrationCommonUtil.getValuesFromLOV( A0197,
	 * wayfairPriceMap.get(A0197), Boolean.FALSE); wayfairPriceMap.put(A0197,
	 * value); listWayfairPriceVOs1.add(wayfairPriceMap); } } } return
	 * listWayfairPriceVOs1;
	 * 
	 * }
	 */

}