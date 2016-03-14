
package com.staples.pim.delegate.vendorupcupdate.processor;

import static com.staples.pim.base.util.IntgSrvAppConstants.VENDOR_UPC_INPUTFILE_DELIMITER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.UPC_FILEDONE_FOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.VENDOR_UPC_OUTPUT_FOLDER;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pcm.stepcontract.beans.KeyValueType;
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
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.vendorupcupdate.runner.UPCScheduler;

public class UPCInboundProcessor {

	static IntgSrvLogger						logger									= IntgSrvLogger
																								.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_VENDORUPC);
	public static IntgSrvLogger					ehfLogger								= IntgSrvLogger.getInstance(EHF_LOGGER);
	public static ErrorHandlingFrameworkICD		ehfICD									= null;
	public static ErrorHandlingFrameworkHandler	ehfHandler								= new ErrorHandlingFrameworkHandler();
	public static String						traceId									= null;
	private static final String[]				headersOneTime							= { "SKU ID", "VendorNo", "UPC",
			"ActiveToPurchase", "UPCType", "CreateDate", "LastModData", "ModUser"		};
	private static final String[]				headersDaily							= { "Action Code", "SKU ID", "VendorNo", "UPC",
			"ActiveToPurchase", "UPCType", "CreateDate", "LastModData", "ModUser"		};
	private static final String					VENDOR_UPC_OUTPUTFILE_CSV_DELIMITER		= ",";
	private static final String					VENDOR_UPC_ACTIVETOPURCHASE_DELIMITER	= ";";
	public static boolean						isOneTime								= true;

	public static void upcInboundProcessor(File file) {

		String logMessage = null;
		try {
			isOneTime = isOneTimeFeed(file);

			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID,
					IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_SPRINGBATCH_PUBLISH_ID_VENDOR_UPC);// FIXME
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();

			ehfICD.setAttributeTransactionType(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRANSACTION_TYPE_FILETOFILE);

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Started to read input file for vendor UPC", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			File outputFile = readIncomingAndGenerateCSV(file);

			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					EHF_MSGTYPE_INFO_NONSLA, "STEP xml generated for vendor UPC update", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
			if (outputFile != null) {
				DatamigrationCommonUtil.sendFile(outputFile, file, UPC_FILEDONE_FOLDER, "9", true,
						DatamigrationAppConstants.EHF_SPRINGBATCH_PUBLISH_ID_VENDOR_UPC);
			}
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "STEP xml for vendor UPC update moved to the SFTP location", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
		} catch (IOException e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), UPCScheduler.PUBLISH_ID);
		} catch (ErrorHandlingFrameworkException e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), UPCScheduler.PUBLISH_ID);
		}
	}

	public static boolean isOneTimeFeed(File file) {

		FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String tempString;
			String delimiter = IntgSrvPropertiesReader.getProperty(VENDOR_UPC_INPUTFILE_DELIMITER);
			while ((tempString = br.readLine()) != null) {
				if (!tempString.equals("")) {
					String values[] = tempString.split(delimiter, -1);
					if (values[0].equalsIgnoreCase("U") || values[0].equalsIgnoreCase("A") || values[0].equalsIgnoreCase("D")) {
						return false;
					} else {
						return true;
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), UPCScheduler.PUBLISH_ID);
		} catch (IOException e) {
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), UPCScheduler.PUBLISH_ID);
		}
		return true;
	}

	public static List<Map<String, String>> readIncomingFile(File file) throws IOException {

		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		String tempString;
		String delimiter = IntgSrvPropertiesReader.getProperty(VENDOR_UPC_INPUTFILE_DELIMITER);
		DatamigrationCommonUtil.printConsole(delimiter);
		List<Map<String, String>> allRespRows = new ArrayList<Map<String, String>>();
		while ((tempString = br.readLine()) != null) {
			if (!tempString.equals("")) {
				String values[] = tempString.split(delimiter, -1);
				if (isOneTime) {
					allRespRows.add(DatamigrationCommonUtil.getValuesInMap(headersOneTime, values));
				} else {
					allRespRows.add(DatamigrationCommonUtil.getValuesInMap(headersDaily, values));
				}
			}
		}
		fr.close();
		return allRespRows;
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static File readIncomingAndGenerateCSV(File file) throws IOException {

		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		String tempString;
		String delimiter = IntgSrvPropertiesReader.getProperty(VENDOR_UPC_INPUTFILE_DELIMITER);
		DatamigrationCommonUtil.printConsole(delimiter);

		List<String> orderList = new ArrayList<String>();
		Map<String, String> resultMap = new HashMap<String, String>();
		while ((tempString = br.readLine()) != null) {
			if (!tempString.equals("")) {
				String key = null;
				String values[] = tempString.split(delimiter, -1);
				if (values.length > 1) {
					if (isOneTime) {
						key = values[0] + VENDOR_UPC_OUTPUTFILE_CSV_DELIMITER + values[1];
						generateSTEPInputValues(values, resultMap, isOneTime);
					} else {

						key = values[1] + VENDOR_UPC_OUTPUTFILE_CSV_DELIMITER + values[2];
						generateSTEPInputValues(values, resultMap, isOneTime);
					}
				}
				if (!orderList.contains(key)) {
					orderList.add(key);
				}
			}
		}
		fr.close();

		if (!resultMap.isEmpty()) {
			return writeVendorUPCCsv(file, orderList, resultMap);
		} else {
			DatamigrationCommonUtil.moveFileToFileBad(file, UPCScheduler.PUBLISH_ID);
			return null;
		}
	}

	/**
	 * @param filePath
	 * @param orderList
	 * @param resultMap
	 * @throws IOException
	 */
	private static File writeVendorUPCCsv(File filePath, List<String> orderList, Map<String, String> resultMap) throws IOException {

		String header = "SKU ID" + VENDOR_UPC_OUTPUTFILE_CSV_DELIMITER + "VendorNo" + VENDOR_UPC_OUTPUTFILE_CSV_DELIMITER + "UPC";
		String inputFileName = filePath.getName();

		inputFileName = inputFileName.substring(0, (inputFileName.length() - 4));
		DatamigrationCommonUtil.printConsole(IntgSrvPropertiesReader.getProperty(VENDOR_UPC_OUTPUT_FOLDER));
		String outputFileName = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(VENDOR_UPC_OUTPUT_FOLDER))
				+ inputFileName + ".csv";

		logger.info("Writing the xml file : " + outputFileName);
		File filetobewritten = new File(outputFileName);
		if (!filetobewritten.getParentFile().exists()) {
			filetobewritten.getParentFile().mkdirs();
		}

		if (!filetobewritten.exists()) {
			header = header + "\n";
		} else {
			header = "";
		}
		FileWriter csvWriter = new FileWriter(filetobewritten, true);
		csvWriter.write(header);
		for (String keyValue : orderList) {
			csvWriter.write(keyValue + VENDOR_UPC_OUTPUTFILE_CSV_DELIMITER + resultMap.get(keyValue) + "\n");
		}

		csvWriter.close();
		return filetobewritten;
	}

	private static Map<String, String> generateSTEPInputValues(String[] values, Map<String, String> map, boolean isOneTime) {

		String key = null;
		String currVal = null;

		if (isOneTime) {
			key = values[0] + VENDOR_UPC_OUTPUTFILE_CSV_DELIMITER + values[1];
			currVal = values[2] + ":" + values[3];
		} else {
			key = values[1] + VENDOR_UPC_OUTPUTFILE_CSV_DELIMITER + values[2];
			currVal = values[3] + ":" + values[4];
		}

		if (map.containsKey(key)) {
			currVal = map.get(key) + VENDOR_UPC_ACTIVETOPURCHASE_DELIMITER + currVal;
		}

		map.put(key, currVal);

		return map;
	}

	public static STEPProductInformation buildXML(List<Map<String, String>> listContainingAllRecords) {

		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepprdinfo = objectFactory.createSTEPProductInformation();
		ProductsType products = objectFactory.createProductsType();
		Map<String, String> map;
		String SkuID;

		for (int i = 0; i < listContainingAllRecords.size(); i++) {
			map = listContainingAllRecords.get(i);
			SkuID = map.get(headersOneTime[0]);
			ProductType productItem = objectFactory.createProductType();
			productItem.setUserTypeID("Item");
			KeyValueType keyvaluetypeitem = objectFactory.createKeyValueType();
			keyvaluetypeitem.setKeyID("Galaxy SKU Number");
			keyvaluetypeitem.setContent(SkuID);
			productItem.setKeyValue(keyvaluetypeitem);
			products.getProduct().add(productItem);

			ProductType productItemtoVendor = objectFactory.createProductType();
			productItemtoVendor.setUserTypeID("ItemToVendor");
			KeyValueType keyvalueitemtovendor = objectFactory.createKeyValueType();
			keyvalueitemtovendor.setContent(SkuID);
			keyvalueitemtovendor.setKeyID("Galaxy SKU Number");

			NameType name = objectFactory.createNameType();
			name.setContent("VendorItem-" + SkuID);
			productItemtoVendor.getName().add(name);
			ValuesType values = objectFactory.createValuesType();
			ValueType value;
			for (int j = 1; j < headersOneTime.length; j++) {
				value = objectFactory.createValueType();
				value.setAttributeID(headersOneTime[j]);
				value.setContent(map.get(headersOneTime[j]));
				values.getValueOrMultiValueOrValueGroup().add(value);
			}
			productItemtoVendor.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
			products.getProduct().add(productItemtoVendor);

			ProductType productSKU = objectFactory.createProductType();
			productSKU.setUserTypeID("SKU");
			KeyValueType keyvaluetypeSku = objectFactory.createKeyValueType();
			keyvaluetypeSku.setKeyID("SKU Number Key");
			keyvaluetypeSku.setContent(SkuID);
			productSKU.setKeyValue(keyvaluetypeSku);
			products.getProduct().add(productSKU);

		}
		stepprdinfo.setProducts(products);
		return stepprdinfo;
	}
}