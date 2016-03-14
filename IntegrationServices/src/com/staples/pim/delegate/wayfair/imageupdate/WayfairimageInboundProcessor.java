
package com.staples.pim.delegate.wayfair.imageupdate;

import static com.staples.pim.base.util.IntgSrvAppConstants.FILE_DONE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WAYFAIRIMAGE_OUTPUT_FOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.staples.pim.delegate.wayfair.imageupdate.runner.WayfairimageScheduler;

public class WayfairimageInboundProcessor {

	static IntgSrvLogger						logger					= IntgSrvLogger
																				.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_WAYFAIRIMAGE);
	public static IntgSrvLogger					ehfLogger				= IntgSrvLogger.getInstance(EHF_LOGGER);
	public static ErrorHandlingFrameworkICD		ehfICD					= null;
	public static ErrorHandlingFrameworkHandler	ehfHandler				= new ErrorHandlingFrameworkHandler();
	public static String						traceId					= null;
	private static final String					WAYFAIR_IMAGE_HEADER	= "wayfair.images.headers";
	private static final String					ParentID				= "WayFairItems";
	private static final String					UserTypeID				= "Item";
	private static final String					ItemID					= "DMItem-";
	public static final String					headerString			= IntgSrvPropertiesReader.getProperty(WAYFAIR_IMAGE_HEADER);

	public static final String 					WAYFAIR_IMAGEFEED 		= "Wayfair Image feed ";
	public static final String ONETIME_ENCODING = "wayfair.onetime.encoding";
	
	public static final String ENCODING = IntgSrvPropertiesReader.getProperty(ONETIME_ENCODING);

	public static int							successCount;
	public static int							failureCount;
	public static void wayfairimageupdateProcessor(File fileName) throws Exception {

		String logMessage = null;
		int fileCount = 1;
		int maxRecordsPerFile = Integer.parseInt(IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.WAYFAIR_PRODUCT_MAX_RECORDS_PER_FILE));

		try {
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIRIMAGE);// FIXME
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();
			ehfICD.setAttributeTransactionType(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRANSACTION_TYPE_FILETOFILE);

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Started to read input file for Wayfair images", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
			logger.info("Process is Started");
			logger.info("Started to read input file");
			Map<String, List<Map<String, String>>> mapContainingwayfairimageupdateLists = readwayfairimageupdateIncomingFile(fileName);
			logger.info("Got input file hearders and values in the MAP");

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH5,
					EHF_MSGTYPE_INFO_NONSLA, "Attribute values fetched from Wayfair images xsv file and stored in map",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			if ((mapContainingwayfairimageupdateLists != null && !mapContainingwayfairimageupdateLists.isEmpty())) {
				
				Map<String, List<Map<String, String>>> splittedMapObject = new HashMap<String, List<Map<String,String>>>();
				for(Entry<String, List<Map<String, String>>> obj : mapContainingwayfairimageupdateLists.entrySet()){
					splittedMapObject.put(obj.getKey(), obj.getValue());
					if (splittedMapObject.size() == maxRecordsPerFile) {
						createAndTransferSTEPXML(splittedMapObject, fileName, fileCount, "", false);
						splittedMapObject = new HashMap<String, List<Map<String,String>>>();
						fileCount++;
					}
				}
				if (splittedMapObject.size() > 0) {
					createAndTransferSTEPXML(splittedMapObject, fileName, fileCount, "", true);
				}
				
			}else {
				DatamigrationCommonUtil.moveFileToFileBad(fileName, WayfairimageScheduler.PUBLISH_ID);
			}
		} catch (ErrorHandlingFrameworkException e) {
			logger.error("Caught an exception in readwayfairimageupdateIncomingFile() :" + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayfairimageScheduler.PUBLISH_ID);
		} catch(Exception e){
			logger.error("Caught an exception in readwayfairimageupdateIncomingFile() :" + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayfairimageScheduler.PUBLISH_ID);
		}
	}

	private static void createAndTransferSTEPXML(Map<String, List<Map<String, String>>> splittedMapObject, File fileName, int fileCount,
			String string, boolean b) {

		try{
			logger.info("Map is Created");
			if (splittedMapObject.size() > 0) {
				String inputFileName = fileName.getName();
				if (fileCount > 1) {
					inputFileName = inputFileName.substring(0, (inputFileName.length() - 4));
					inputFileName = inputFileName + "-" + fileCount + ".xml";

				}
				logger.info("Creation of STEPXML has been started with the MAP");
				STEPProductInformation stepPrdInfo = wayfairimageupdateStepXMLGenerator(splittedMapObject, fileName);
				logger.info("Total no of products : " + stepPrdInfo.getProducts().getProduct().size());
				logger.info("Creation of STEPXML is completed");
				String logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
						EHF_MSGTYPE_INFO_NONSLA,
						"STEPProductInformation object for wayfairimages update update constructed for marshalling",
						EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
						DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.info(logMessage);
				logger.info("Started to writing the STEPXML file into output folder");
				File outputFile = DatamigrationCommonUtil.marshallObject(stepPrdInfo, new File(inputFileName), WAYFAIRIMAGE_OUTPUT_FOLDER,
						WayfairimageScheduler.PUBLISH_ID);
		
				logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
						EHF_MSGTYPE_INFO_NONSLA, "STEP xml generated for wayfairimages update update",
						EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
						DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.info(logMessage);
				logger.info("Sending STEPXML files to Server(Hotfolder)");
				DatamigrationCommonUtil.sendFile(outputFile, fileName, fileName.getParentFile().getParentFile().getPath() + "/"
						+ FILE_DONE + "/", "11_IMAGES", true, DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIRIMAGE);
		
				logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
						EHF_MSGTYPE_INFO_NONSLA, "STEP xml for wayfairimages update update moved to the SFTP location",
						EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
						DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.info(logMessage);
				logger.info("Process is Ended");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	public static Map<String, List<Map<String, String>>> readwayfairimageupdateIncomingFile(File fileName) throws IOException, MessagingException {

		successCount = 0;
		failureCount = 0;
		long startTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
	
		
		boolean useDefaultHeader = true;
		Map<String, List<Map<String, String>>> mapObject = new HashMap<String, List<Map<String, String>>>();

		try {
			FileInputStream inputStream = new FileInputStream(fileName);
			InputStreamReader inputStreamReader;
			if(!DatamigrationCommonUtil.isPropertyValueNULL(ENCODING)){
				inputStreamReader = new InputStreamReader(inputStream, ENCODING);
			}else{
				inputStreamReader = new InputStreamReader(inputStream);
			}
			BufferedReader br = new BufferedReader(inputStreamReader);

			String tempString;
			long lineNumber = 0;
			String headers[] = null;
			logger.info("Started to create map");
			String delimiter = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.WAYFAIRIMAGE_INPUTFILE_DELIMITER);
			DatamigrationCommonUtil.printConsole("Delimer :: "+delimiter);
			
			if (useDefaultHeader) {
				headers = headerString.split(delimiter, -1);
				while ((tempString = br.readLine()) != null) {
					
					if (!IntgSrvUtils.isNullOrEmpty(tempString)) {
						if (lineNumber > 0) {
							String values[] = tempString.split(delimiter, -1);
							mapObject = getValuesInMap(headers, values, mapObject, "WS_SKU_NO", fileName);
						}
						lineNumber++;
					}
				}
			}
			br.close();
			inputStreamReader.close();
			inputStream.close();
		} catch (IOException e) {
			logger.error("Caught an exception in readwayfairimageupdateIncomingFile() :" + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), WayfairimageScheduler.PUBLISH_ID);
		}

		long endTime = DatamigrationCommonUtil.getCurrentMilliSeconds();
		DatamigrationCommonUtil.generateExecutionSummary(WAYFAIR_IMAGEFEED, DatamigrationCommonUtil.getReportFolderPath(fileName), fileName.getName(), successCount,
								(endTime - startTime), failureCount);
		
		return mapObject;
	}

	public static STEPProductInformation wayfairimageupdateStepXMLGenerator(
			Map<String, List<Map<String, String>>> listOfwayfairimageupdate, File fileName) throws Exception {

		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepPrdInfo = objectFactory.createSTEPProductInformation();
		ProductsType Products = objectFactory.createProductsType();
		logger.info("Processing Record(MAP)");
		String Fanids[] = { "D0055", "D0056", "D0057", "D0058", "D0059", "A0540", "A0541", "A0542", "A0543" };
		if (listOfwayfairimageupdate != null && !listOfwayfairimageupdate.isEmpty()) {
			for (Entry<String, List<Map<String, String>>> wayfairimageupdateMap : listOfwayfairimageupdate.entrySet()) {
				String productId = wayfairimageupdateMap.getKey();
				String ItemId = ItemID + productId;
				ProductType product = objectFactory.createProductType();
				ValuesType values = objectFactory.createValuesType();
				for (Map<String, String> eachProductDet : wayfairimageupdateMap.getValue()) {
					ValueType value = objectFactory.createValueType();
					String image[] = eachProductDet.get("IMAGE_NAME").split("\\.");
					value.setContent(image[0]);
					int seqNo = IntgSrvUtils.toInt(eachProductDet.get("IMAGE_SEQUENCE_NO"));
					value.setAttributeID(Fanids[(seqNo - 1)]);
					values.getValueOrMultiValueOrValueGroup().add(value);
				}

				product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
				product.setID(ItemId);
				product.setParentID(ParentID);
				product.setUserTypeID(UserTypeID);
				product.setSelected(false);
				product.setReferenced(true);

				Products.getProduct().add(product);
			}
		} else {
			DatamigrationCommonUtil.printConsole("List is empty or null");
		}

		stepPrdInfo.setProducts(Products);
		stepPrdInfo.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepPrdInfo.setContextID(CONTEXT_ID_VALUE); // Hard coded value
		stepPrdInfo.setExportContext(EXPORT_CONTEXT_VALUE);// Hard coded value
		stepPrdInfo.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded value
//		stepPrdInfo.setAutoApprove(YesNoType.Y);
		return stepPrdInfo;
	}
	public static Map<String, List<Map<String, String>>> getValuesInMap(String[] headers, String[] values,
			Map<String, List<Map<String, String>>> mapObject, String groupingId, File file) {

		Map<String, String> rowValuesMap = DatamigrationCommonUtil.getValuesInMap(headers, values);
		if(rowValuesMap!=null && rowValuesMap.size()>2){
			String groupIdValue = rowValuesMap.get(groupingId);
			
			if(groupIdValue!=null && groupIdValue.startsWith("WYF"))
			{
				List<Map<String, String>> listBasedOnGroupID = null;
				if (mapObject.get(groupIdValue) != null) {

					listBasedOnGroupID = mapObject.get(groupIdValue);

				} else {
					listBasedOnGroupID = new ArrayList<Map<String, String>>();
				}
				listBasedOnGroupID.add(rowValuesMap);
				successCount++;
				mapObject.put(groupIdValue, listBasedOnGroupID);
			}else{
				failureCount++;
			}
		}else{
			if(values!=null && values.length>0){
				DatamigrationCommonUtil.appendWriterFile(file.getParentFile().getParentFile() + "/Report/Report_" + file.getName(), "~Has incorrect number of Values in record :"+values.length);
			}
			failureCount++;
		}
		return mapObject;

	}
}
