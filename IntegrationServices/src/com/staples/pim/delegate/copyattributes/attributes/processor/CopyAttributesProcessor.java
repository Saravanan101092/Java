
package com.staples.pim.delegate.copyattributes.attributes.processor;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pcm.stepcontract.beans.AttributeGroupLinkType;
import com.staples.pcm.stepcontract.beans.AttributeGroupListType;
import com.staples.pcm.stepcontract.beans.AttributeGroupType;
import com.staples.pcm.stepcontract.beans.AttributeListType;
import com.staples.pcm.stepcontract.beans.AttributeType;
import com.staples.pcm.stepcontract.beans.ClassificationType;
import com.staples.pcm.stepcontract.beans.ClassificationsType;
import com.staples.pcm.stepcontract.beans.ListOfValueLinkType;
import com.staples.pcm.stepcontract.beans.MetaDataType;
import com.staples.pcm.stepcontract.beans.MultiValueType;
import com.staples.pcm.stepcontract.beans.NameType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.TrueFalseType;
import com.staples.pcm.stepcontract.beans.UserTypeLinkType;
import com.staples.pcm.stepcontract.beans.ValidationType;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.YesNoType;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.copyattributes.attributes.runner.CopyAttributesScheduler;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

public class CopyAttributesProcessor {

	public static final String						FREEFORM_TRACELOGGER_COPYATTRIBUTES_ATTRIBUTE	= "tracelogger.copyattributes.attributes";

	static IntgSrvLogger							logger											= IntgSrvLogger
																											.getInstance(FREEFORM_TRACELOGGER_COPYATTRIBUTES_ATTRIBUTE);

	public static IntgSrvLogger						ehfLogger										= IntgSrvLogger
																											.getInstance("ehflogger.copyattributes");

	private static ErrorHandlingFrameworkICD		ehfICD											= null;
	private static ErrorHandlingFrameworkHandler	ehfHandler										= new ErrorHandlingFrameworkHandler();
	private static String							traceId											= null;
	String											logMessage										= null;

	public static final String						COPY_ATTRIBUTES_INPUT_FOLDER					= "/opt/stibo/integration/hotfolder/CopyAttributesIncoming/AttributeIncoming/File_Unprocessed";
	public static final String						COPY_ATTRIBUTES_OUTPUT_FOLDER					= "COPY_ATTRIBUTES_OUTPUT_FOLDER";

	public static final String						COPY_ATTRIBUTES_DELIMITER						= "COPY_ATTRIBUTES_DELIMITER";
	public static final String						delimiter										= IntgSrvPropertiesReader
																											.getProperty(COPY_ATTRIBUTES_DELIMITER);
	public static final String						COPY_ATTRIBUTES_HEADERS							= "COPY_ATTRIBUTES_HEADER";
	public static final String						HEADERS_String									= IntgSrvPropertiesReader
																											.getProperty(COPY_ATTRIBUTES_HEADERS);
	public static final String[]					headers											= HEADERS_String.split(delimiter, -1);

	public static final String						CATID											= "CATID";
	public static final String						CATNAME											= "CATNAME";
	public static final String						ATTRIBID										= "ATTRIBID";
	public static final String						ATTRIBDESC										= "ATTRIBDESC";
	public static final String						LINENO											= "LINENO";
	public static final String						REQUIRED										= "REQUIRED";
	public static final String						HEADERVALUE										= "HEADERVALUE";
	public static final String						CAPTURECONTROLTYPEID							= "CAPTURECONTROLTYPEID";
	public static final String						CAPTURECONTROLTYPEDESC							= "CAPTURECONTROLTYPEDESC";
	public static final String						MAXCHARS										= "MAXCHARS";
	public static final String						LISTID											= "LISTID";
	public static final String						MINVALUE										= "MINVALUE";
	public static final String						MAXVALUE										= "MAXVALUE";
	public static final String						DEFAULTVALUE									= "DEFAULTVALUE";
	public static final String						STATUS											= "STATUS";
	public static final String						EXAMPLETIP										= "EXAMPLETIP";
	public static final String						APPRREQ											= "APPRREQ";
	public static final String						LOCATIONLVLFLAG									= "LOCATIONLVLFLAG";
	public static final String						ATTRIBUTE_GROUP_ID								= "ATTRIBUTE_GROUP_ID";

	public static final String						HEADER											= "Header";
	public static final String						TEXTBOX											= "Textbox";
	public static final String						COMBOBOX										= "ComboBox";
	public static final String						RADIOBUTTON										= "RadioButton";
	public static final String						FLOAT											= "Float";
	public static final String						LISTBOX											= "Listbox";
	public static final String						INTEGER											= "Integer";

	public static final String						NORMAL											= "Normal";
	public static final String						ITEM											= "Item";

	public static final String						COPY_PREFIX										= "Copy-";
	public static final String						COPY_PREFIX_HEADER								= "CopyGroup-";
	public static final String						CLASSIFICATION_ROOT								= "Classification 1 root";
	public static final String						COPYCATEGORY									= "CopyCategory";
	public static final String						COPY_CATEGORY_ROOT								= "CopyCategoryRoot";
	public static final String						COPY_CATEGORY									= "Copy Category";
	public static final String						COPYCATEGORY_PREFIX								= "CopyCategory-";
	public static final String						COPYCATEGORIES									= "CopyCategories";
	public static final String 						COPYCATEGORY_ATTRIBUTES							= "CopyCategoryAttributes";
	
	public static final String 						DELIMITER_TILDA									= "~";
	
	public void processCopyAttributes() {

		try {
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(SPRINGBATCH_ECO_SYSTEM, SPRINGBATCH_COMPONENT_ID,
					SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId("");// FIXME
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();
			File folder = new File(IntgSrvUtils.reformatFilePath(COPY_ATTRIBUTES_INPUT_FOLDER));

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
			logger.info("Processing input CopyAttributes file " + file.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "Processing input CopyAttributes file " + file.getName(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			if (file.getName().endsWith(".xsv") || file.getName().endsWith(".dsv")) {

				int lineno = 0;
				String tempString;
				String AttributeGroupID = "";
				List<Map<String, String>> attributesRecordMapsLOV = new ArrayList<Map<String, String>>();

				Map<String, Map<String, String>> attributesRecordMaps = new HashMap<String, Map<String, String>>();
				Map<String, Map<String, String>> attributesRecordMapsHEADER = new HashMap<String, Map<String, String>>();
				Map<String,List<String>> attributeHierarchyMap = new HashMap<String,List<String>>();
				
				while ((tempString = reader.readLine()) != null) {
					if (!tempString.equals("")) {
						if (lineno != 0) {
							String[] values = tempString.split(delimiter, -1);
							Map<String, String> thisRecord = DatamigrationCommonUtil.getRecordAsMap(headers, values, logger);
							if (thisRecord != null) {
								if (thisRecord.get(HEADERVALUE).equalsIgnoreCase(DatamigrationAppConstants.Y_STR)) {
									AttributeGroupID = thisRecord.get(ATTRIBID);
								}
								thisRecord.put(ATTRIBUTE_GROUP_ID, AttributeGroupID);
								String controlDesc = thisRecord.get(CAPTURECONTROLTYPEDESC);
								if (controlDesc.equalsIgnoreCase(HEADER)) {
									attributesRecordMapsHEADER.put(thisRecord.get(ATTRIBID), thisRecord);
								} else if (controlDesc.equalsIgnoreCase(RADIOBUTTON)) {
									attributesRecordMapsLOV.add(thisRecord);
								} else if (controlDesc.equalsIgnoreCase(FLOAT) || controlDesc.equalsIgnoreCase(INTEGER)
										|| controlDesc.equalsIgnoreCase(TEXTBOX) || controlDesc.equalsIgnoreCase(COMBOBOX)
										|| controlDesc.equalsIgnoreCase(LISTBOX)) {
									attributesRecordMaps.put(AttributeGroupID + DELIMITER_TILDA + thisRecord.get(ATTRIBID), thisRecord);
								}
								
								String attributeHierarchyKey = thisRecord.get(CATID)+DELIMITER_TILDA+thisRecord.get(CATNAME);
								String attributeHierarchyValue = thisRecord.get(ATTRIBID)+DELIMITER_TILDA+thisRecord.get(ATTRIBDESC);
								
								if(attributeHierarchyMap.containsKey(attributeHierarchyKey)){
									attributeHierarchyMap.get(attributeHierarchyKey).add(attributeHierarchyValue);
								}else{
									List<String> attributeValues = new ArrayList<String>();
									attributeValues.add(attributeHierarchyValue);
									attributeHierarchyMap.put(attributeHierarchyKey, attributeValues);
								}
								
							} else {
								logger.error("Record omitted. Incorrect no of values :" + thisRecord);
								DatamigrationCommonUtil.appendWriterFile(
										file.getParentFile().getParentFile() + "/Report/Report_" + file.getName(), tempString
												+ "~Has incorrect number of Values in record :" + values.length);
							}
						}
						lineno++;
					}
				}
				reader.close();
				fileReader.close();
				removeDuplicateLOVAttributes(attributesRecordMapsLOV, attributesRecordMaps);
				removeDuplicateNumber_IntegerAttributes(attributesRecordMaps);
				
				createAndSendStepProductInformation(file, attributesRecordMaps, attributesRecordMapsLOV, attributesRecordMapsHEADER, attributeHierarchyMap);
			} else {
				logger.info("Invalid file format " + file.getName());
				DatamigrationCommonUtil.printConsole("Invalid file format " + file.getName());
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}

	}

	public void removeDuplicateLOVAttributes(List<Map<String, String>> attributesRecordMapsLOV,
			Map<String, Map<String, String>> attributesRecordMaps) {

		Set<String> attributesAsText = new HashSet<String>();
		for(String attributesKey : attributesRecordMaps.keySet()){
			String[] temp = attributesKey.split(DELIMITER_TILDA, -1);
			attributesAsText.add(temp[1]);
		}
		
		List<Map<String, String>> temp = new ArrayList<Map<String, String>>();
		for (Map<String, String> lovMap : attributesRecordMapsLOV) {
			if (attributesAsText.contains(lovMap.get(ATTRIBID))) {
				temp.add(lovMap);
			}
		}
		attributesRecordMapsLOV.removeAll(temp);
		temp.clear();attributesAsText.clear();
		
	}

	public void removeDuplicateNumber_IntegerAttributes(Map<String, Map<String, String>> attributesRecordMaps){

		Map<String, Map<String, String>> integer_Number_Attributes = new HashMap<String, Map<String, String>>();
		Map<String, Map<String, String>> text_Attributes = new HashMap<String, Map<String, String>>();

		//		separate text attributes and number attributes

		for(String key : attributesRecordMaps.keySet()){
			String[] attributeIinfo = key.split(DELIMITER_TILDA, -1);
			String attribId = attributeIinfo[1];
			
			Map<String,String> thisRecord = attributesRecordMaps.get(key);
			String controlDesc = thisRecord.get(CAPTURECONTROLTYPEDESC);	
			if (controlDesc.equalsIgnoreCase(FLOAT) || controlDesc.equalsIgnoreCase(INTEGER)){
				integer_Number_Attributes.put(attribId, thisRecord);
			}else if(controlDesc.equalsIgnoreCase(TEXTBOX) || controlDesc.equalsIgnoreCase(COMBOBOX)
					|| controlDesc.equalsIgnoreCase(LISTBOX)){
				text_Attributes.put(attribId, thisRecord);
			}
		}
		
		for(String numberMapKey : integer_Number_Attributes.keySet()){
			if(text_Attributes.containsKey(numberMapKey)){
				Map<String,String> numberRecord = integer_Number_Attributes.get(numberMapKey);
				numberRecord.put(CAPTURECONTROLTYPEDESC, TEXTBOX);
			}
		}

	}

	
	public void createAndSendStepProductInformation(File file, Map<String, Map<String, String>> taxonomyRecordMaps,
			List<Map<String, String>> taxonomyRecordMapsLOV, Map<String, Map<String, String>> taxonomyRecordMapsHEADER, Map<String, List<String>> attributeHierarchyMap) {

		// Copy Attributes
		if (taxonomyRecordMaps != null && !taxonomyRecordMaps.isEmpty()) {
			STEPProductInformation stepProductInformation = createStepProductInformationForAttributes(taxonomyRecordMaps);

			logger.info("STEPProductInformation object for copy attributes constructed for Marshalling");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "STEPProductInformation object for copy attributes constructed for Marshalling",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			File outputFileAttr = DatamigrationCommonUtil.marshallObject(stepProductInformation, file, COPY_ATTRIBUTES_OUTPUT_FOLDER, "");

			logger.info("Output STEP xml for attributes generated " + outputFileAttr.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "Output STEP xml for attributes generated " + outputFileAttr.getName(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			DatamigrationCommonUtil.sendFile(outputFileAttr, file, file.getParentFile().getParentFile() + "/File_Done/",
					"13_COPY_ATTRIBUTES", false, "");// FIXME - publish ID

			logger.info("SFTP file transfer success for " + outputFileAttr.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "SFTP file transfer success for " + outputFileAttr.getName(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);
		} else {
			logger.info("No attribute data in file :"+file.getName());
		}
		// Copy Attribute Lov
		if (taxonomyRecordMapsLOV != null && !taxonomyRecordMapsLOV.isEmpty()) {
			STEPProductInformation stepProductInformationLOV = createStepProductInformationForAttributesLOV(taxonomyRecordMapsLOV);

			logger.info("STEPProductInformation object for copy attributes lov constructed for Marshalling");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "STEPProductInformation object for copy attributes lov constructed for Marshalling",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			String lovFileName = file.getPath().substring(0, file.getPath().length() - 4) + "_LOVs.xml";
			File outputFileAttrLov = DatamigrationCommonUtil.marshallObject(stepProductInformationLOV, new File(lovFileName),
					COPY_ATTRIBUTES_OUTPUT_FOLDER, "");

			logger.info("Output STEP xml generated for copy attributes lov" + outputFileAttrLov.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "Output STEP xml generated for copy attributes lov " + outputFileAttrLov.getName(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			DatamigrationCommonUtil.sendFile(outputFileAttrLov, file, file.getParentFile().getParentFile() + "/File_Done/",
					"13_COPY_ATTRIBUTES_LOV", false, "");// FIXME - publish ID

			logger.info("SFTP file transfer success for " + outputFileAttrLov.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "SFTP file transfer success for " + outputFileAttrLov.getName(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);
		} else {
			logger.info("No LOV data in file :"+file.getName());
		}

		// Copy Attributes Headers
		if (taxonomyRecordMapsHEADER != null && !taxonomyRecordMapsHEADER.isEmpty()) {
			STEPProductInformation stepProductInformationHEADER = createStepProductInformationForAttributesHEADERS(taxonomyRecordMapsHEADER);
			logger.info("STEPProductInformation object for copy attributes header constructed for Marshalling");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "STEPProductInformation object for copy attributes header constructed for Marshalling",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			String headerFileName = file.getPath().substring(0, file.getPath().length() - 4) + "_HEADERs.xml";
			File outputFileAttrHeader = DatamigrationCommonUtil.marshallObject(stepProductInformationHEADER, new File(headerFileName),
					COPY_ATTRIBUTES_OUTPUT_FOLDER, "");

			logger.info("Output STEP xml generated copy attributes header " + outputFileAttrHeader.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "Output STEP xml generated for copy attributes header " + outputFileAttrHeader.getName(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			DatamigrationCommonUtil.sendFile(outputFileAttrHeader, file, file.getParentFile().getParentFile() + "/File_Done/",
					"13_COPY_ATTRIBUTES_HEADER", false, "");

			logger.info("SFTP file transfer success for " + outputFileAttrHeader.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "SFTP file transfer success for " + outputFileAttrHeader.getName(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

		} else {
			logger.info("No Header data in file :"+file.getName());
			DatamigrationCommonUtil.moveFileToFileBad(file, CopyAttributesScheduler.PUBLISH_ID);
		}
		
		//Entity Feed
		if(!attributeHierarchyMap.isEmpty() && attributeHierarchyMap!=null){
			STEPProductInformation stepProductInformationHierarchy = createSTEPProductInformationEntityFeed(attributeHierarchyMap);
			logger.info("STEPProductInformation object for copy attributes Hierarchy constructed for Marshalling");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "STEPProductInformation object for copy attributes Hierarchy constructed for Marshalling",
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			String headerFileName = file.getPath().substring(0, file.getPath().length() - 4) + "_Entity.xml";
			File outputFileAttrHeader = DatamigrationCommonUtil.marshallObject(stepProductInformationHierarchy, new File(headerFileName),
					COPY_ATTRIBUTES_OUTPUT_FOLDER, "");

			logger.info("Output STEP xml generated copy attributes Hierarchy " + outputFileAttrHeader.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "Output STEP xml generated for copy attributes Hierarchy " + outputFileAttrHeader.getName(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);

			DatamigrationCommonUtil.sendFile(outputFileAttrHeader, file, file.getParentFile().getParentFile() + "/File_Done/",
					"13_COPY_ATTRIBUTES_ENTITY", true, "");

			logger.info("SFTP file transfer success for " + outputFileAttrHeader.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "SFTP file transfer success for " + outputFileAttrHeader.getName(),
					EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
					ehfICD);
			ehfLogger.info(logMessage);
		}
		
	}

	public STEPProductInformation createStepProductInformationForAttributes(Map<String, Map<String, String>> attributesRecordMaps) {

		logger.info("Inside method " + DatamigrationCommonUtil.getMethodName());
		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = objectFactory.createSTEPProductInformation();

		AttributeListType attributeList = objectFactory.createAttributeListType();

		for (String taxonomyMapKey : attributesRecordMaps.keySet()) {

			Map<String, String> taxonomyMap = attributesRecordMaps.get(taxonomyMapKey);
			AttributeType attribute = objectFactory.createAttributeType();
			attribute.setDerived(TrueFalseType.FALSE);
			attribute.setExternallyMaintained(TrueFalseType.FALSE);
			attribute.setMandatory(TrueFalseType.FALSE);
			attribute.setFullTextIndexed(TrueFalseType.FALSE);
			attribute.setMultiValued(TrueFalseType.FALSE);
			attribute.setProductMode(NORMAL);
			attribute.setID(COPY_PREFIX + taxonomyMap.get(ATTRIBID));

			NameType name = objectFactory.createNameType();
			name.setQualifierID("eng");
			name.setContent(taxonomyMap.get(ATTRIBDESC));
			attribute.getName().add(name);

			ValidationType validation = objectFactory.createValidationType();
			String type = taxonomyMap.get(CAPTURECONTROLTYPEDESC);
			if (type.equalsIgnoreCase(TEXTBOX) || type.equalsIgnoreCase(COMBOBOX) || type.equalsIgnoreCase(LISTBOX)) {
				validation.setBaseType("Text");
			} else if (type.equalsIgnoreCase(FLOAT)) {
				validation.setBaseType("Number");
			} else if (type.equalsIgnoreCase(INTEGER)) {
				validation.setBaseType("Integer");
			}
			validation.setInputMask("");
			validation.setMaxLength("");
			validation.setMaxValue("");
			validation.setMinValue("1");
			attribute.setValidation(validation);

			AttributeGroupLinkType attributeGroupLink = objectFactory.createAttributeGroupLinkType();
			attributeGroupLink.setAttributeGroupID(COPY_PREFIX_HEADER + taxonomyMap.get(ATTRIBUTE_GROUP_ID));
			attribute.getAttributeGroupLink().add(attributeGroupLink);

			UserTypeLinkType usertypeLink = objectFactory.createUserTypeLinkType();
			usertypeLink.setUserTypeID(ITEM);
			attribute.getUserTypeLink().add(usertypeLink);
			attributeList.getAttribute().add(attribute);
		}

		stepProductInformation.setAttributeList(attributeList);
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
																// value
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
																		// coded
																		// value
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);
		stepProductInformation.setUseContextLocale(false);
		return stepProductInformation;
	}

	public STEPProductInformation createStepProductInformationForAttributesLOV(List<Map<String, String>> attributesRecordMapsLOV) {

		logger.info("Inside method " + DatamigrationCommonUtil.getMethodName());
		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = objectFactory.createSTEPProductInformation();
		AttributeListType attributeList = objectFactory.createAttributeListType();

		for (Map<String, String> lovMap : attributesRecordMapsLOV) {
			AttributeType attribute = objectFactory.createAttributeType();
			attribute.setDerived(TrueFalseType.FALSE);
			attribute.setExternallyMaintained(TrueFalseType.FALSE);
			attribute.setMandatory(TrueFalseType.FALSE);
			attribute.setFullTextIndexed(TrueFalseType.FALSE);
			attribute.setMultiValued(TrueFalseType.FALSE);
			attribute.setProductMode(NORMAL);
			attribute.setID(COPY_PREFIX + lovMap.get(ATTRIBID));

			NameType name = objectFactory.createNameType();
			name.setQualifierID("eng");
			name.setContent(lovMap.get(ATTRIBDESC));
			attribute.getName().add(name);

			ListOfValueLinkType listofvaluelink = objectFactory.createListOfValueLinkType();
			listofvaluelink.setListOfValueID("Y_N");
			attribute.setListOfValueLink(listofvaluelink);

			AttributeGroupLinkType attributeGroupLink = objectFactory.createAttributeGroupLinkType();
			attributeGroupLink.setAttributeGroupID(COPY_PREFIX_HEADER + lovMap.get(ATTRIBUTE_GROUP_ID));
			attribute.getAttributeGroupLink().add(attributeGroupLink);

			UserTypeLinkType usertypeLink = objectFactory.createUserTypeLinkType();
			usertypeLink.setUserTypeID(ITEM);
			attribute.getUserTypeLink().add(usertypeLink);
			attributeList.getAttribute().add(attribute);
		}

		stepProductInformation.setAttributeList(attributeList);
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
																// value
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
																		// coded
																		// value
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);
		stepProductInformation.setUseContextLocale(false);
		return stepProductInformation;
	}

	public STEPProductInformation createStepProductInformationForAttributesHEADERS(
			Map<String, Map<String, String>> attributesRecordMapsHEADERS) {

		logger.info("Inside method " + DatamigrationCommonUtil.getMethodName());
		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = objectFactory.createSTEPProductInformation();

		AttributeGroupListType attributeGroupList = objectFactory.createAttributeGroupListType();

		for (String headerMapKey : attributesRecordMapsHEADERS.keySet()) {
			Map<String, String> headerMap = attributesRecordMapsHEADERS.get(headerMapKey);
			AttributeGroupType attributeGroup = objectFactory.createAttributeGroupType();
			attributeGroup.setExcludeFromProfiling(TrueFalseType.FALSE);
			attributeGroup.setID(COPY_PREFIX_HEADER + headerMap.get(ATTRIBID));
			attributeGroup.setParentID("CopyAttributes");
			attributeGroup.setShowInWorkbench(TrueFalseType.TRUE);

			NameType name = objectFactory.createNameType();
			name.setContent(headerMap.get(ATTRIBDESC));
			attributeGroup.getName().add(name);
			attributeGroupList.getAttributeGroup().add(attributeGroup);

		}

		stepProductInformation.setAttributeGroupList(attributeGroupList);
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);
		stepProductInformation.setUseContextLocale(false);

		return stepProductInformation;
	}

	public STEPProductInformation createSTEPProductInformationEntityFeed(Map<String,List<String>> attributeHierarchyMap){
		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = objectFactory.createSTEPProductInformation();
		
		ClassificationsType classifications = objectFactory.createClassificationsType();
		ClassificationType rootClassification = objectFactory.createClassificationType();
		rootClassification.setParentID(CLASSIFICATION_ROOT);
		rootClassification.setID(COPYCATEGORY);
		rootClassification.setUserTypeID(COPY_CATEGORY_ROOT);
		
		NameType rootName = objectFactory.createNameType();
		rootName.setContent(COPY_CATEGORY);
		rootClassification.getNameOrAttributeLinkOrSequenceProduct().add(rootName);
		
		for(String key : attributeHierarchyMap.keySet()){
			
			ClassificationType classification = objectFactory.createClassificationType();
			String[] categoryInfo = key.split(DELIMITER_TILDA,-1);
			String categoryID = categoryInfo[0];
			String categoryName = categoryInfo[1];
			
			classification.setID(COPYCATEGORY_PREFIX+categoryID);
			classification.setUserTypeID(COPYCATEGORIES);
			
			NameType name = objectFactory.createNameType();
			name.setContent(categoryName);
			classification.getNameOrAttributeLinkOrSequenceProduct().add(name);
			
		/*	MetaDataType metadata = objectFactory.createMetaDataType();
			ValueType value = objectFactory.createValueType();
			value.setAttributeID(COPYCATEGORY_ATTRIBUTES);
			value.setContent(getCopyCategoryAttributesValue(attributeHierarchyMap.get(key)));
			metadata.getValueOrMultiValueOrValueGroup().add(value);*/
			
			MetaDataType metadata = objectFactory.createMetaDataType();
			
			MultiValueType multiValue = objectFactory.createMultiValueType();
			multiValue.setAttributeID(COPYCATEGORY_ATTRIBUTES);
			for(String attributeinfoString : attributeHierarchyMap.get(key)){
				
				String[] attributeinfo = attributeinfoString.split(DELIMITER_TILDA, -1);
				String attributeid = attributeinfo[0];
				String attributeDesc = attributeinfo[1];
				
				ValueType value = objectFactory.createValueType();
				value.setContent(attributeDesc+"("+attributeid+")");
				multiValue.getValueOrValueGroup().add(value);
			}
			metadata.getValueOrMultiValueOrValueGroup().add(multiValue);
			classification.getNameOrAttributeLinkOrSequenceProduct().add(metadata);
			rootClassification.getNameOrAttributeLinkOrSequenceProduct().add(classification);
		}
		
		classifications.getClassification().add(rootClassification);
		stepProductInformation.setClassifications(classifications);
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);
		stepProductInformation.setUseContextLocale(false);
//		Commented as per Raja's request 
//		stepProductInformation.setAutoApprove(YesNoType.Y);
		
		return stepProductInformation;
	}
	
	public String getCopyCategoryAttributesValue(List<String> attributesList){
		String copyCategoryAttributes="";
		
		for(String attributeinfoString : attributesList){
			String[] attributeinfo = attributeinfoString.split(DELIMITER_TILDA, -1);
			String attributeid = attributeinfo[0];
			String attributeDesc = attributeinfo[1];
			
			copyCategoryAttributes+=attributeDesc+"("+attributeid+")"+DatamigrationAppConstants.COMMA;
		}
		copyCategoryAttributes = copyCategoryAttributes.substring(0, copyCategoryAttributes.length()-1);
		return copyCategoryAttributes;
	}
	
	public static void main(String[] args) {

		CopyAttributesProcessor copyattributesProcessor = new CopyAttributesProcessor();
		copyattributesProcessor.processCopyAttributes();
	}

}
