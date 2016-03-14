
package com.staples.pim.delegate.wayfair.attributeupdate;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_COMPONENT_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ECO_SYSTEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ICD_CLASSNAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WAYFAIR_ATTRIBUTE_OUTPUT_FOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pcm.stepcontract.beans.ClassificationType;
import com.staples.pcm.stepcontract.beans.ClassificationsType;
import com.staples.pcm.stepcontract.beans.MetaDataType;
import com.staples.pcm.stepcontract.beans.MultiValueType;
import com.staples.pcm.stepcontract.beans.NameType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wayfair.attributeupdate.runner.AttributeScheduler;
import com.staples.pim.delegate.wayfair.taxonomyupdate.TaxonomyProcessor;

/**
 * @author 843868
 * 
 */
public class AttributeMetadataProcessor {

	public static final String					STAPLESDOTCOMCLASS_PREFIX				= "StaplesDotComClass-";

	public static final String					SEARCHABLE_ATTRIBUTES					= "SearchableAttributes";

	public static final String					MANDATORY_ATTRIBUTES					= "MandatoryAttributes";

	public static final String					RANGE_ATTRIBUTES						= "RangeAttributes";

	public static final String					CATEGORYSPEC_ATTRIBUTES					= "CategorySpecAttributes";

	public static final String					ATTRIBUTE_GROUP							= "AttributeGroup";

	public static final String					RANGE_STR								= "Range";

	ObjectFactory								objectFactory;

	public static final String					ENTITY_FEED_SUFFIX						= "_Entity.xml";

	public static final String					FREEFORM_TRACELOGGER_ATTRIBUTEMETADATA	= "tracelogger.wayfairattributemetadata";

	static IntgSrvLogger						logger									= IntgSrvLogger
																								.getInstance(FREEFORM_TRACELOGGER_ATTRIBUTEMETADATA);
	public static IntgSrvLogger					ehfLogger								= IntgSrvLogger
																								.getInstance(DatamigrationAppConstants.EHF_LOGGER_WAYFAIR);

	public static ErrorHandlingFrameworkICD		ehfICD									= null;

	public static String						traceId									= null;

	String										logMessage								= null;

	public static ErrorHandlingFrameworkHandler	ehfHandler								= new ErrorHandlingFrameworkHandler();

	/**
	 * @param attributeMetadaList
	 * @param file
	 * 
	 *            Create attribute metadata Entity feed
	 */
	public void processAttributeMetaData(List<Map<String, String>> attributeMetadaList, File file) {

		try {
			ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(SPRINGBATCH_ECO_SYSTEM, SPRINGBATCH_COMPONENT_ID,
					SPRINGBATCH_ICD_CLASSNAME);

			ehfICD.setAttributePublishId(DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_METADATA);
			traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();

			logger.info("\tATTRIBUTE METADATA FEED REQUEST");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4,
					EHF_MSGTYPE_INFO_NONSLA, "ATTRIBUTE METADATA FEED request", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
			// construct ClassVO objects
			Map<String, ClassVO> classesMap = getClassVOObjects(attributeMetadaList);

			// construct STEPProductInformation object
			STEPProductInformation stepPrdInfo = getAttributeMetadataSTEPXml(classesMap);

			// marshall and send output file
			String outputFileName = file.getPath().substring(0, file.getPath().length() - 4) + ENTITY_FEED_SUFFIX;
			File outputFile = DatamigrationCommonUtil.marshallObject(stepPrdInfo, new File(outputFileName),
					WAYFAIR_ATTRIBUTE_OUTPUT_FOLDER, AttributeScheduler.PUBLISH_ID);

			logger.info("Step xml generated " + outputFile.getName());
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1,
					EHF_MSGTYPE_INFO_NONSLA, "Output STEP xml file generated" + outputFile.getName(), EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);

			if (AttributeProcessor.isOneTime) {
				DatamigrationCommonUtil.sendFile(outputFile, file, AttributeProcessor.WAYFAIR_ATTRIBUTE_METADATA_FILEDONE_FOLDER_OT,
						AttributeProcessor.ATTRIBUTE_ENTITY_INSTANCENO, true, AttributeScheduler.PUBLISH_ID);
			} else {
				DatamigrationCommonUtil.sendFile(outputFile, file, AttributeProcessor.WAYFAIR_ATTRIBUTE_METADATA_FILEDONE_FOLDER,
						AttributeProcessor.ATTRIBUTE_ENTITY_INSTANCENO, true, AttributeScheduler.PUBLISH_ID);
			}

			logger.info("Step xml transfered to hotfolder successfully");
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2,
					EHF_MSGTYPE_INFO_NONSLA, "Step xml sent to step hotfolder successfully", EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
		} catch (ErrorHandlingFrameworkException e) {
			logger.error(DatamigrationCommonUtil.getClassAndMethodName() + " exception caught: " + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_METADATA);
		} catch (Exception e) {
			logger.error(DatamigrationCommonUtil.getClassAndMethodName() + " exception caught: " + e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(),
					DatamigrationAppConstants.EHF_PUBLISH_ID_WAYFAIR_ATTRIBUTE_METADATA);
		}
	}

	/**
	 * @param attributeMetadaList
	 * @return
	 * 
	 *         get the list of maps and create ClassVO objects
	 */
	public Map<String, ClassVO> getClassVOObjects(List<Map<String, String>> attributeMetadaList) {

		logger.info("creating ClassVO objects");
		Map<String, ClassVO> classesMap = new HashMap<String, ClassVO>();
		ClassVO classVO;

		// parse through each map
		for (Map<String, String> thisAttribute : attributeMetadaList) {
			String concatenatedClassID = getClassId(thisAttribute);
			if (classesMap.containsKey(concatenatedClassID)) {
				// if ClassVO is already present just append the lists

				classVO = classesMap.get(concatenatedClassID);
				String className = thisAttribute.get(AttributeProcessor.ITM_CLASSNAME);
				String attributeName = thisAttribute.get(AttributeProcessor.ITM_ATTRIBNAME);
				String isSearchable = thisAttribute.get(AttributeProcessor.ITM_ATTRIBSEARCH);
				String isMandatory = thisAttribute.get(AttributeProcessor.ITM_ATTRIBREQUIRED);
				String attributeType = thisAttribute.get(AttributeProcessor.ITM_ATTRIBTYPE);
				String hierarchycode = thisAttribute.get(AttributeProcessor.ITM_BUSINESSUNIT);

				String AttributeNameandID = getAttributeStringForAttributeMetadata(attributeName,
						thisAttribute.get(AttributeProcessor.ITM_ATTRIBNAME_ID), hierarchycode);

				// classVO.setID(thisAttribute.get(AttributeProcessor.ITM_CLASSNAME_ID));
				// Category spec attributes
				if (!classVO.getCategorySpecAttribute().contains(AttributeNameandID)) {
					classVO.getCategorySpecAttribute().add(AttributeNameandID);
				}

				// searchable attributes
				if (DatamigrationAppConstants.Y_STR.equalsIgnoreCase(isSearchable)
						&& !classVO.getSearchableAttributes().contains(AttributeNameandID)) {
					classVO.getSearchableAttributes().add(AttributeNameandID);
				}

				// mandatory attributes
				if (DatamigrationAppConstants.Y_STR.equalsIgnoreCase(isMandatory)
						&& !classVO.getMandatoryAttributes().contains(AttributeNameandID)) {
					classVO.getMandatoryAttributes().add(AttributeNameandID);
				}

				// Range attributes
				if (RANGE_STR.equalsIgnoreCase(attributeType) && !classVO.getRangeAttributes().contains(AttributeNameandID)) {
					classVO.getRangeAttributes().add(AttributeNameandID);
				}

				classVO.setHierarchyCode(hierarchycode);

			} else {
				// If classVO is not present already, Create new

				classVO = new ClassVO();
				classVO.setID(concatenatedClassID);

				String className = thisAttribute.get(AttributeProcessor.ITM_CLASSNAME);
				String attributeName = thisAttribute.get(AttributeProcessor.ITM_ATTRIBNAME);
				String isSearchable = thisAttribute.get(AttributeProcessor.ITM_ATTRIBSEARCH);
				String isMandatory = thisAttribute.get(AttributeProcessor.ITM_ATTRIBREQUIRED);
				String attributeType = thisAttribute.get(AttributeProcessor.ITM_ATTRIBTYPE);
				String hierarchycode = thisAttribute.get(AttributeProcessor.ITM_BUSINESSUNIT);

				// set fixed values for class
				classVO.setName(className);
				classVO.setAttributeGroup(thisAttribute.get(AttributeProcessor.ITM_ATTRIBGROUP));
				classVO.setAttributeGroupID(thisAttribute.get(AttributeProcessor.ITM_ATTRIBUTEGROUP_ID));
				classVO.setCategoryID(thisAttribute.get(AttributeProcessor.ITM_CLASSNAME_ID));

				String AttributeNameandID = getAttributeStringForAttributeMetadata(attributeName,
						thisAttribute.get(AttributeProcessor.ITM_ATTRIBNAME_ID), hierarchycode);
				// set lists
				// category spec attributes
				classVO.getCategorySpecAttribute().add(AttributeNameandID);

				// searchable attributes
				if (DatamigrationAppConstants.Y_STR.equalsIgnoreCase(isSearchable)) {
					classVO.getSearchableAttributes().add(AttributeNameandID);
				}

				// mandatory attributes
				if (DatamigrationAppConstants.Y_STR.equalsIgnoreCase(isMandatory)) {
					classVO.getMandatoryAttributes().add(AttributeNameandID);
				}

				// range attributes
				if (RANGE_STR.equalsIgnoreCase(attributeType)) {
					classVO.getRangeAttributes().add(AttributeNameandID);
				}

				classVO.setHierarchyCode(hierarchycode);
				classesMap.put(classVO.getID(), classVO);
			}
		}
		logger.info("Total no of ClassVOs created=" + classesMap.size());
		return classesMap;
	}

	public String getAttributeStringForAttributeMetadata(String attributeName, String AttributeID, String hierarchyCode) {

		if (hierarchyCode.equalsIgnoreCase(DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE)) {
			return attributeName + "(" + AttributeProcessor.PDB_PREFIX + AttributeID + ")";
		} else if (hierarchyCode.equalsIgnoreCase(DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE)) {
			return attributeName + "(" + AttributeProcessor.QUL_PREFIX + AttributeID + ")";
		}
		logger.error("hierarchy code invalid. Setting pdb value by default");
		return attributeName + "(" + AttributeProcessor.PDB_PREFIX + AttributeID + ")";
	}

	/**
	 * @param classesMap
	 * @return
	 * 
	 *         create Step xml for entity feed
	 */
	public STEPProductInformation getAttributeMetadataSTEPXml(Map<String, ClassVO> classesMap) {

		logger.info("creating Step xml");

		objectFactory = new ObjectFactory();
		STEPProductInformation stepprdInfo = objectFactory.createSTEPProductInformation();
		ClassificationsType classifications = objectFactory.createClassificationsType();
		ClassificationType classification = null;

		// get ClassificationType object for each
		for (String classID : classesMap.keySet()) {
			if ((!classID.equalsIgnoreCase(DatamigrationAppConstants.EMPTY_STR)) && (!classID.equalsIgnoreCase("0"))) {
				ClassVO classVO = classesMap.get(classID);
				classification = getClassifications(classVO);
				classifications.getClassification().add(classification);
			}
		}

		stepprdInfo.setClassifications(classifications);
		stepprdInfo.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepprdInfo.setContextID(CONTEXT_ID_VALUE); // Hard coded value
		stepprdInfo.setExportContext(EXPORT_CONTEXT_VALUE);// Hard coded value
		stepprdInfo.setWorkspaceID(WORKSPACE_ID_VALUE);
		logger.info("Step xml generated.");
		return stepprdInfo;

	}

	/**
	 * @param classVO
	 * @return
	 * 
	 *         create a classification tag for each classVO
	 */
	public ClassificationType getClassifications(ClassVO classVO) {

		ClassificationType rootClassification = objectFactory.createClassificationType();

		if (DatamigrationAppConstants.WAYFAIR_DOTCOM_HIERARCHYCODE.equalsIgnoreCase(classVO.getHierarchyCode())) {

			rootClassification.setID(TaxonomyProcessor.STAPLES_CLASSID_PREFIX + classVO.getID());
			rootClassification.setUserTypeID(TaxonomyProcessor.STAPLES_DOTCOM_CLASS);
		} else if (DatamigrationAppConstants.WAYFAIR_QUILL_HIERARCHYCODE.equalsIgnoreCase(classVO.getHierarchyCode())) {

			rootClassification.setID(TaxonomyProcessor.QUILL_CLASSID_PREFIX + classVO.getID());
			rootClassification.setUserTypeID(TaxonomyProcessor.QUILL_WEB_CLASS);
		}

		// name value
		NameType nameType = objectFactory.createNameType();
		nameType.setContent(classVO.getName());
		rootClassification.getNameOrAttributeLinkOrSequenceProduct().add(nameType);

		MetaDataType metadata = objectFactory.createMetaDataType();

		// AttributeGroup
		ValueType attributeGroupValue = objectFactory.createValueType();
		attributeGroupValue.setAttributeID(ATTRIBUTE_GROUP);
		attributeGroupValue.setContent(classVO.getAttributeGroup() + DatamigrationAppConstants.HYPHEN + classVO.getAttributeGroupID());

		// searchable attributes
		MultiValueType searchableMultivalue = objectFactory.createMultiValueType();
		searchableMultivalue.setAttributeID(SEARCHABLE_ATTRIBUTES);
		for (String searchableAttribute : classVO.getSearchableAttributes()) {
			ValueType searchableAttributeValue = objectFactory.createValueType();
			searchableAttributeValue.setContent(searchableAttribute);
			searchableMultivalue.getValueOrValueGroup().add(searchableAttributeValue);
		}

		// mandatory attributes
		MultiValueType mandatoryMultivalue = objectFactory.createMultiValueType();
		mandatoryMultivalue.setAttributeID(MANDATORY_ATTRIBUTES);
		for (String mandatoryAttribute : classVO.getMandatoryAttributes()) {
			ValueType mandatoryAttributeValue = objectFactory.createValueType();
			mandatoryAttributeValue.setContent(mandatoryAttribute);
			mandatoryMultivalue.getValueOrValueGroup().add(mandatoryAttributeValue);
		}

		// range Attributes
		MultiValueType rangeMultivalue = objectFactory.createMultiValueType();
		rangeMultivalue.setAttributeID(RANGE_ATTRIBUTES);
		for (String rangeAttribute : classVO.getRangeAttributes()) {
			ValueType rangeAttributeValue = objectFactory.createValueType();
			rangeAttributeValue.setContent(rangeAttribute);
			rangeMultivalue.getValueOrValueGroup().add(rangeAttributeValue);
		}

		// categorySpec Attributes
		MultiValueType categorySpecMultivalue = objectFactory.createMultiValueType();
		categorySpecMultivalue.setAttributeID(CATEGORYSPEC_ATTRIBUTES);
		for (String categorySpecAttribute : classVO.getCategorySpecAttribute()) {
			ValueType categorySpecValue = objectFactory.createValueType();
			categorySpecValue.setContent(categorySpecAttribute);
			categorySpecMultivalue.getValueOrValueGroup().add(categorySpecValue);
		}

		metadata.getValueOrMultiValueOrValueGroup().add(searchableMultivalue);
		metadata.getValueOrMultiValueOrValueGroup().add(mandatoryMultivalue);
		metadata.getValueOrMultiValueOrValueGroup().add(attributeGroupValue);
		metadata.getValueOrMultiValueOrValueGroup().add(rangeMultivalue);
		metadata.getValueOrMultiValueOrValueGroup().add(categorySpecMultivalue);

		rootClassification.getNameOrAttributeLinkOrSequenceProduct().add(metadata);
		return rootClassification;
	}

	/**
	 * @param recordAsMap
	 * @return
	 * 
	 *         create class ID
	 */
	public String getClassId(Map<String, String> recordAsMap) {

		return recordAsMap.get(AttributeProcessor.ITM_SUPERCATEGORYNAME_ID) + recordAsMap.get(AttributeProcessor.ITM_CATEGORY_ID) + recordAsMap.get(AttributeProcessor.ITM_DEPTNAME_ID)
				+ recordAsMap.get(AttributeProcessor.ITM_CLASSNAME_ID);
	}
}
