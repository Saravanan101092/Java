
package com.staples.pim.delegate.locationfeed.writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import com.staples.pcm.stepcontract.beans.ClassificationType;
import com.staples.pcm.stepcontract.beans.ClassificationsType;
import com.staples.pcm.stepcontract.beans.DeleteClassificationType;
import com.staples.pcm.stepcontract.beans.DeleteClassificationsType;
import com.staples.pcm.stepcontract.beans.NameType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.locationfeed.listenerrunner.RunSchedulerLocationFeed;

public class LocationFeedXMLWriter<LocationFeedBean> implements ItemWriter<LocationFeedBean>, StepExecutionListener {

	private IntgSrvLogger				traceLogger							= IntgSrvLogger
																					.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	private String						clazzname							= this.getClass().getName();

	private Map<String, JobParameter>	jpMap;
	private String						outputFileAddUpdate;
	private String						outputFileDelete;
	private String						doneDir;

	private List<String>				currentLocationCodes				= new ArrayList<String>();
	private List<String>				existingLocationCodes				= new ArrayList<String>();
	private List<String>				deleteLocationCodes					= new ArrayList<String>();

	/*
	 * Used to create ADD/UPDATE STEP xml
	 */
	ObjectFactory						objectFactory						= new ObjectFactory();
	STEPProductInformation				stepPrdInfo							= objectFactory.createSTEPProductInformation();

	ClassificationsType					classifications						= objectFactory.createClassificationsType();
	ClassificationType					rootClassification					= objectFactory.createClassificationType();

	ClassificationType					locationStructureClassification		= objectFactory.createClassificationType();

	ClassificationType					distributionCentersClassification	= objectFactory.createClassificationType();
	ClassificationType					fulfilmentCentersClassification		= objectFactory.createClassificationType();
	ClassificationType					storesClassification				= objectFactory.createClassificationType();

	@Override
	public void write(List<? extends LocationFeedBean> arg0) throws Exception {

		for (LocationFeedBean bean : arg0) {

			com.staples.pim.delegate.locationfeed.model.LocationFeedBean locationBean = ((com.staples.pim.delegate.locationfeed.model.LocationFeedBean) bean);

			currentLocationCodes.add(locationBean.getLocationcode());
			/*
			 * Creating and Setting values to Inner Classification
			 */
			ClassificationType innerClassification = objectFactory.createClassificationType();

			String locationType = locationBean.getLocationtype();

			if ("D".equalsIgnoreCase(locationType)) {
				settingValuesFromBean(locationBean, innerClassification, "DC", "DistributionCenter");
				addInnerClassification(distributionCentersClassification, innerClassification, locationBean);

			} else if ("F".equalsIgnoreCase(locationType)) {
				settingValuesFromBean(locationBean, innerClassification, "FC", "FulfilmentCenter");
				addInnerClassification(fulfilmentCentersClassification, innerClassification, locationBean);
			} else if ("S".equalsIgnoreCase(locationType)) {
				settingValuesFromBean(locationBean, innerClassification, "Store", "Store");
				addInnerClassification(storesClassification, innerClassification, locationBean);
			}

		}
	}

	private void addInnerClassification(ClassificationType myParentClassification, ClassificationType myChildClassification,
			com.staples.pim.delegate.locationfeed.model.LocationFeedBean myLocationBean) {

		myParentClassification.getNameOrAttributeLinkOrSequenceProduct().add(myChildClassification);

	}

	@Override
	public void beforeStep(StepExecution stepExecution) {

		traceLogger.info(clazzname, "beforeStep", "ENTER: StepExecutionListener");
		try {
			IntgSrvUtils.printConsole("Calling beforeStep");

			/*
			 * Setting Default Informations - Start
			 */
			stepPrdInfo.setClassifications(classifications);
			setDefaultInfo(stepPrdInfo);

			setValueToClassification(rootClassification, "Classification 1 root", "Classification 1 user-type root",
					"Classification 1 root", Boolean.FALSE);
			setValueToClassification(locationStructureClassification, "LocationStructure", "LocationStructure", "Location Structure");
			setValueToClassification(distributionCentersClassification, "DistributionCenters", "DistributionCenters",
					"Distribution Centers");
			setValueToClassification(fulfilmentCentersClassification, "FulfilmentCenters", "FulfilmentCenters", "Fulfilment Centers");
			setValueToClassification(storesClassification, "Stores", "Stores", "Stores");

			classifications.getClassification().add(rootClassification);
			rootClassification.getNameOrAttributeLinkOrSequenceProduct().add(locationStructureClassification);
			locationStructureClassification.getNameOrAttributeLinkOrSequenceProduct().add(distributionCentersClassification);
			locationStructureClassification.getNameOrAttributeLinkOrSequenceProduct().add(fulfilmentCentersClassification);
			locationStructureClassification.getNameOrAttributeLinkOrSequenceProduct().add(storesClassification);

			/*
			 * Setting Default Informations - End
			 */

			jpMap = stepExecution.getJobParameters().getParameters();
			outputFileAddUpdate = jpMap.get(IntgSrvAppConstants.JP_OUTPUT_FILE_STEP_XML_ADDUPDATE).toString();
			outputFileDelete = jpMap.get(IntgSrvAppConstants.JP_OUTPUT_FILE_STEP_XML_DELETE).toString();
			doneDir = IntgSrvUtils.reformatFilePath(jpMap.get(RunSchedulerLocationFeed.LOCATION_FEED_TXT_DONE_DIR).toString());

			IntgSrvUtils.printConsole("STEP XML outputFilename=" + outputFileAddUpdate);
			IntgSrvUtils.printConsole("STEP XML outputFilename=" + outputFileDelete);

			String msgDesc = "SpringBatch LocationFeedXMLWriter initialized <- job params, StepExecutionContext";
			traceLogger.info(clazzname, "beforeStep", msgDesc);
		} catch (Throwable exception) {
			traceLogger.error(clazzname, "beforeStep", exception);
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerLocationFeed.PUBLISH_ID);
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		traceLogger.info(clazzname, "afterStep", "ENTER: StepExecutionListner");
		try {

			/*
			 * Generate Add/Update STEP XML
			 */
			GenerateSTEPXML(stepPrdInfo, outputFileAddUpdate);
			/*
			 * Generate Delete STEP XML
			 */
			STEPProductInformation deleteStepPrdInfo = getDeleteStepProdInfo();
			GenerateSTEPXML(deleteStepPrdInfo, outputFileDelete);

			String msgDesc = "SpringBatch LocationFeedXMLWriter completed";
			traceLogger.info(clazzname, "afterStep", msgDesc);

		} catch (Throwable exception) {
			traceLogger.error(clazzname, "afterStep", exception);
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerLocationFeed.PUBLISH_ID);
		}
		traceLogger.info(clazzname, "afterStep", "EXIT");

		return org.springframework.batch.core.ExitStatus.COMPLETED;
	}

	private STEPProductInformation getDeleteStepProdInfo() throws Exception {

		loadExistingLocationCode();
		deleteLocationCodes = getDeleteLocalCode();

		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation deleteStepPrdInfo = objectFactory.createSTEPProductInformation();
		setDefaultInfo(deleteStepPrdInfo);

		DeleteClassificationsType deleteClassificationsType = objectFactory.createDeleteClassificationsType();
		deleteStepPrdInfo.setDeleteClassifications(deleteClassificationsType);

		for (String code : deleteLocationCodes) {

			DeleteClassificationType deleteClassification = objectFactory.createDeleteClassificationType();
			deleteClassification.setID(code);
			deleteClassificationsType.getDeleteClassification().add(deleteClassification);
		}

		return deleteStepPrdInfo;
	}

	private List<String> getDeleteLocalCode() {

		/*
		 * Finding Location codes which are needs to be delete
		 */
		for (String currentLocationCode : currentLocationCodes) {
			existingLocationCodes.remove(currentLocationCode);
		}
		return existingLocationCodes;
	}

	private void loadExistingLocationCode() throws Exception {

		List<File> fileList = getFilesFromDir(doneDir);
		File latestFile = null;

		if (fileList != null && fileList.size() > 0) {
			latestFile = fileList.get(0);

			try {
				FileReader fileReader = new FileReader(latestFile.getAbsolutePath());
				BufferedReader bufferReader = new BufferedReader(fileReader);

				String singleLine = "";
				while ((singleLine = bufferReader.readLine()) != null) {
					// Line Validation
					if (singleLine != null && singleLine.length() > 0) {
						existingLocationCodes.add(singleLine.split("\t")[0]);
					}
				}
				bufferReader.close();
			} catch (IOException exception) {
				exception.printStackTrace();
				IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerLocationFeed.PUBLISH_ID);
			}
		}
	}

	private static List<File> getFilesFromDir(String dirAbsPath) throws Exception {

		File dir = new File(dirAbsPath);
		List<File> fileList = Arrays.asList(dir.listFiles());
		return sortBasedOnLastModified(fileList);
	}

	private static List<File> sortBasedOnLastModified(List<File> fileList) {

		LastModifiedFileComparator lastModifiedComparator = new LastModifiedFileComparator();
		Collections.sort(fileList, lastModifiedComparator);
		return fileList;
	}

	/**
	 * 
	 * @throws JAXBException
	 */
	private void GenerateSTEPXML(STEPProductInformation myStepPrdInfo, String myOutputFile) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(STEPProductInformation.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		File filetobewritten = new File(myOutputFile);
		jaxbMarshaller.marshal(myStepPrdInfo, filetobewritten);
	}

	/**
	 * Setting values to STEPProductInformation
	 * 
	 * @param stepPrdInfo
	 */
	private void setDefaultInfo(STEPProductInformation stepPrdInfo) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
		Date date = new Date();

		stepPrdInfo.setExportTime(dateFormat.format(date));
		stepPrdInfo.setContextID("EnglishUS");
		stepPrdInfo.setExportContext("EnglishUS");
		stepPrdInfo.setWorkspaceID("Main");
		stepPrdInfo.setUseContextLocale(false);
	}

	private void settingValuesFromBean(com.staples.pim.delegate.locationfeed.model.LocationFeedBean locationBean,
			ClassificationType innerClassification, String shortSrt, String longStr) {

		String locationCode = locationBean.getLocationcode();
		String locationAddress = locationBean.getLocationaddress1();

		setValueToClassification(innerClassification, locationCode, longStr, locationAddress);
	}

	/**
	 * Setting values to setValueToClassification
	 * 
	 * @param classification
	 */
	private void setValueToClassification(ClassificationType classification, String id, String userTypeID, String name, boolean flag) {

		setValueToClassification(classification, id, userTypeID, name);
		classification.setSelected(flag);
	}

	/**
	 * Setting values to setValueToClassification
	 * 
	 * @param classification
	 */
	private void setValueToClassification(ClassificationType classification, String id, String userTypeID, String name) {

		classification.setID(id);
		classification.setUserTypeID(userTypeID);
		NameType nameType = objectFactory.createNameType();
		nameType.setContent(name);
		classification.getNameOrAttributeLinkOrSequenceProduct().add(nameType);
	}
}