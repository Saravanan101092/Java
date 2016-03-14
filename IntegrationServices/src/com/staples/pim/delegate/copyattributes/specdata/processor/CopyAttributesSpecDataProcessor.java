
package com.staples.pim.delegate.copyattributes.specdata.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_COMPONENT_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ECO_SYSTEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ICD_CLASSNAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pcm.stepcontract.beans.YesNoType;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

public class CopyAttributesSpecDataProcessor {

	public static final String						COPY_ATTRIBUTES_SPECDATA_INPUT_FOLDER			= "/opt/stibo/integration/hotfolder/CopyAttributesIncoming/SpecDataIncoming/File_Unprocessed";
	public static final String						delimiter										= IntgSrvPropertiesReader
																											.getProperty("COPY_ATTRIBUTES_SPECDATA_DELIMITER");
	public static final String						COPY_ATTRIBUTES_SPECDATA_OUTPUT_FOLDER			= "COPY_ATTRIBUTES_SPECDATA_OUTPUT_FOLDER";
	public static final String						HeaderString									= IntgSrvPropertiesReader
																											.getProperty("COPY_ATTRIBUTES_SPECDATA_HEADER");
	public static final String[]					headers											= HeaderString.split(delimiter, -1);

	public static final String						FREEFORM_TRACELOGGER_COPYATTRIBUTES_SPECDATA	= "tracelogger.copyattributes.specdata";

	static IntgSrvLogger							logger											= IntgSrvLogger
																											.getInstance(FREEFORM_TRACELOGGER_COPYATTRIBUTES_SPECDATA);

	public static IntgSrvLogger						ehfLogger										= IntgSrvLogger
																											.getInstance("ehflogger.copyattributes");

	private static ErrorHandlingFrameworkICD		ehfICD											= null;
	private static ErrorHandlingFrameworkHandler	ehfHandler										= new ErrorHandlingFrameworkHandler();
	private static String							traceId											= null;
	String											logMessage										= null;

	public static final String						COPY_HEADER_ID									= "COPY_HEADER_ID";
	public static final String						CATEGORY_ID										= "CATEGORY_ID";
	public static final String						CATNAME											= "CATNAME";
	public static final String						SKU												= "SKU";
	public static final String						ATTRIBID										= "ATTRIBID";
	public static final String						ATTRIBDESC										= "ATTRIBDESC";
	public static final String						SEQUENCENO										= "SEQUENCENO";
	public static final String						DATMOD											= "DATMOD";
	public static final String						STATUS											= "STATUS";
	public static final String						ATTRIBUTEVALUE									= "ATTRIBUTEVALUE";

	public static final String						DMITEM_PREFIX									= "DMItem-";
	public static final String						COPY_PREFIX										= "Copy-";
	public static final String						ITEM											= "Item";
	public static final String						NONSTOCKED_ITEMS								= "Non-StockedItems";
	public static SimpleDateFormat					dateFormat										= new SimpleDateFormat("dd-MMM-yy");

	public void processCopyAttributesSpecdata() {

		try {
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(SPRINGBATCH_ECO_SYSTEM, SPRINGBATCH_COMPONENT_ID,
					SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId("");// FIXME
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();
			File folder = new File(IntgSrvUtils.reformatFilePath(COPY_ATTRIBUTES_SPECDATA_INPUT_FOLDER));

			if (folder != null) {
				File[] files = folder.listFiles();
				for (int i = 0; i < files.length; i++) {
					System.out.println("Processing input file.." + files[i].getName());
					processIncomingFile(files[i]);
				}
			}
		} catch (ErrorHandlingFrameworkException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	public void processIncomingFile(File file) {

		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			logger.info("Processing input CopyAttributes spec data file " + file.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Processing input CopyAttributes spec data file " + file.getName(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);
			if (file.getName().endsWith(".xsv") || file.getName().endsWith(".dsv")) {
				int lineno = 0;
				String tempString;

				Map<String, SKUSpecDataVO> specDataVOMap = new HashMap<String, SKUSpecDataVO>();

				while ((tempString = reader.readLine()) != null) {
					try {

						if (!tempString.equals("")) {
							if (lineno != 0) {
								String[] values = tempString.split(delimiter, -1);
								Map<String, String> thisRecord = DatamigrationCommonUtil.getRecordAsMap(headers, values, logger);
								if (thisRecord != null) {
									String sku = thisRecord.get(SKU);
									sku = DatamigrationCommonUtil.addLeadingCharacter(sku, 6, '0');
									String attributeID = thisRecord.get(ATTRIBID);

									if (specDataVOMap.containsKey(sku)) {

										// SKU already present
										SKUSpecDataVO specdata = specDataVOMap.get(sku);

										if (specdata.getAttributes().containsKey(attributeID)) {

											// Attribute already present
											AttributewiseSpecDataVO attributewiseData = specdata.getAttributes().get(attributeID);
											Date lastmodified = getDateObject(thisRecord.get(DATMOD));
											if (attributewiseData.getDatewiseSpecData().getLastModified().equals(lastmodified)) {

												DatewiseSpecDataVO datewiseData = attributewiseData.getDatewiseSpecData();
//												Removing Transformation as per PCMP-2681
//												datewiseData.getSequenceNo_AttributeValueMap().put(thisRecord.get(SEQUENCENO),
//														transformValue(thisRecord.get(ATTRIBUTEVALUE)));
												datewiseData.getSequenceNo_AttributeValueMap().put(thisRecord.get(SEQUENCENO),
														thisRecord.get(ATTRIBUTEVALUE));
											} else if (lastmodified.after(attributewiseData.getDatewiseSpecData().getLastModified())) {

												DatewiseSpecDataVO datewiseData = new DatewiseSpecDataVO();
												datewiseData.setLastModified(lastmodified);
//												Removing Transformation as per PCMP-2681
//												datewiseData.getSequenceNo_AttributeValueMap().put(thisRecord.get(SEQUENCENO),
//														transformValue(thisRecord.get(ATTRIBUTEVALUE)));
												datewiseData.getSequenceNo_AttributeValueMap().put(thisRecord.get(SEQUENCENO),
														thisRecord.get(ATTRIBUTEVALUE));
												attributewiseData.setDatewiseSpecData(datewiseData);
											}
										} else {

											// New attribute. create new object
											AttributewiseSpecDataVO attributewiseData = new AttributewiseSpecDataVO();

											DatewiseSpecDataVO datewiseData = new DatewiseSpecDataVO();
											datewiseData.setLastModified(getDateObject(thisRecord.get(DATMOD)));
//											Removing Transformation as per PCMP-2681
//											datewiseData.getSequenceNo_AttributeValueMap().put(thisRecord.get(SEQUENCENO),
//													transformValue(thisRecord.get(ATTRIBUTEVALUE)));
											datewiseData.getSequenceNo_AttributeValueMap().put(thisRecord.get(SEQUENCENO),
													thisRecord.get(ATTRIBUTEVALUE));
											attributewiseData.setDatewiseSpecData(datewiseData);
											attributewiseData.setAttributeID(attributeID);
											// attributewiseData.setAttributeDesc(thisRecord.get(ATTRIBDESC));

											specdata.getAttributes().put(attributeID, attributewiseData);
										}

									} else {

										// SKU not present already. Create new
										// object
										SKUSpecDataVO specData = new SKUSpecDataVO();

										AttributewiseSpecDataVO attributewiseData = new AttributewiseSpecDataVO();

										DatewiseSpecDataVO datewiseData = new DatewiseSpecDataVO();
										datewiseData.setLastModified(getDateObject(thisRecord.get(DATMOD)));
//										Removing Transformation as per PCMP-2681
//										datewiseData.getSequenceNo_AttributeValueMap().put(thisRecord.get(SEQUENCENO),
//												transformValue(thisRecord.get(ATTRIBUTEVALUE)));
										datewiseData.getSequenceNo_AttributeValueMap().put(thisRecord.get(SEQUENCENO),
												thisRecord.get(ATTRIBUTEVALUE));
										attributewiseData.setDatewiseSpecData(datewiseData);
										attributewiseData.setAttributeID(attributeID);
										// attributewiseData.setAttributeDesc(thisRecord.get(ATTRIBDESC));

										specData.getAttributes().put(attributeID, attributewiseData);
										specData.setSKUid(sku);
										specDataVOMap.put(sku, specData);
									}

								} else {
									logger.error("Record omitted. Incorrect no of values :" + thisRecord);
									DatamigrationCommonUtil.appendWriterFile(file.getParentFile().getParentFile() + "/Report/Report_"
											+ file.getName(), tempString + "~Has incorrect number of Values in record :" + values.length);
								}

							}
							lineno++;
						}
					} catch (ParseException e) {
						logger.error(e);
						ehfLogger.error(e);
						e.printStackTrace();
					}
				}
				reader.close();fileReader.close();
				createAndSendSTEPxml(specDataVOMap, file);
			} else {
				logger.info("Invalid file format " + file.getName());
				DatamigrationCommonUtil.printConsole("Invalid file format " + file.getName());
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
			ehfLogger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e);
			ehfLogger.error(e);
			e.printStackTrace();
		}

	}

	public Date getDateObject(String dateString) throws ParseException {

		return dateFormat.parse(dateString);
	}

	public void createAndSendSTEPxml(Map<String, SKUSpecDataVO> specDataVOMap, File file) {

		if (specDataVOMap != null && !specDataVOMap.isEmpty()) {
			STEPProductInformation stepProductInfo = createSTEPProductInformationObject(specDataVOMap);
			logger.info("STEPProductInformation object for copy attributes spec data constructed for Marshalling");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "STEPProductInformation object for copy attributes spec data constructed for Marshalling",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			File outputFile = DatamigrationCommonUtil.marshallObject(stepProductInfo, file, COPY_ATTRIBUTES_SPECDATA_OUTPUT_FOLDER, "");

			logger.info("Output STEP xml for copy attributes spec data generated " + outputFile.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "Output STEP xml for copy attributes spec data generated " + outputFile.getName(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			DatamigrationCommonUtil.sendFile(outputFile, file, file.getParentFile().getParentFile() + "/File_Done/",
					"13_COPY_ATTRIBUTES_SPEC", true, "");// FIXME publish ID

			logger.info("SFTP file transfer success for " + outputFile.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "SFTP file transfer success for " + outputFile.getName(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
		} else {
			logger.info(" invalid file" + file + " moved to File_Bad");
			DatamigrationCommonUtil.moveFileToFileBad(file, "");// FIXME publish
																// ID
		}
	}

	public STEPProductInformation createSTEPProductInformationObject(Map<String, SKUSpecDataVO> specDataVOMap) {

		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = objectFactory.createSTEPProductInformation();

		ProductsType products = objectFactory.createProductsType();

		for (String key : specDataVOMap.keySet()) {
			SKUSpecDataVO skudatavo = specDataVOMap.get(key);
			ProductType product = objectFactory.createProductType();
			product.setID(DMITEM_PREFIX + skudatavo.getSKUid());
			product.setUserTypeID(ITEM);
			product.setReferenced(true);
			product.setSelected(false);
			
//			Removed as per Nithiya's request
//			product.setParentID(NONSTOCKED_ITEMS);

			ValuesType values = objectFactory.createValuesType();

			for (String attributeKey : skudatavo.getAttributes().keySet()) {
				AttributewiseSpecDataVO attributedatavo = skudatavo.getAttributes().get(attributeKey);
				ValueType value = objectFactory.createValueType();
				value.setAttributeID(COPY_PREFIX + attributedatavo.getAttributeID());
				value.setContent(attributedatavo.getAttributeValue());
				values.getValueOrMultiValueOrValueGroup().add(value);
			}

			product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
			products.getProduct().add(product);
		}

		stepProductInformation.setProducts(products);
		stepProductInformation.setAutoApprove(YesNoType.Y);
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hardcoded
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);
		stepProductInformation.setUseContextLocale(false);
		return stepProductInformation;
	}

	/*public String transformValue(String value){
		if(value != null && value.equalsIgnoreCase("1")){
			return DatamigrationAppConstants.Y_STR;
		}else if(value != null && value.equalsIgnoreCase("0")){
			return DatamigrationAppConstants.N_STR;
		}else if ("YES".equalsIgnoreCase(value) || "Y".equalsIgnoreCase(value)) {
			return DatamigrationAppConstants.Y_STR;
		}else if ("NO".equalsIgnoreCase(value) || "N".equalsIgnoreCase(value)) {
			return DatamigrationAppConstants.N_STR;
		}
			
		return value;
	}*/
	
	public static void main(String[] args) {

		CopyAttributesSpecDataProcessor copySpecDataPrc = new CopyAttributesSpecDataProcessor();
		copySpecDataPrc.processCopyAttributesSpecdata();

		// String dateString = "02-MAR-10";
		// SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yy");
		// try {
		// Date date = dateformat.parse(dateString);
		// System.out.println(date);
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }

	}

}
