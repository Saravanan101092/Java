/**
 * -----------------------------------------------------------------------
 * STAPLES, INC
 * -----------------------------------------------------------------------
 * (C) Copyright 2007 Staples, Inc.          All rights reserved.
 *
 * NOTICE:  All information contained herein or attendant hereto is,
 *          and remains, the property of Staples Inc.  Many of the
 *          intellectual and technical concepts contained herein are
 *          proprietary to Staples Inc. Any dissemination of this
 *          information or reproduction of this material is strictly
 *          forbidden unless prior written permission is obtained
 *          from Staples Inc.
 * -----------------------------------------------------------------------
 */
/*
 * File name     :   
 * Creation Date :   
 * @author  
 * @version 1.0
 */

package com.staples.pim.delegate.futuredatedpo.listener;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jms.TextMessage;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.Unmarshaller;

import com.staples.pim.base.common.bean.STEPProductInformation;
import com.staples.pim.base.common.bean.STEPProductInformation.Products.Product.Values.MultiValue;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.listenerandrunner.MessageQueueListener;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.commonusecases.formatter.FormaterToFixLength;
import com.staples.pim.delegate.commonusecases.writer.CreateFlatFileOnFileSystem;
import com.staples.pim.delegate.commonusecases.writer.CreateXMLFileOnFileSystem;
import com.staples.pim.delegate.commonusecases.writer.PublisherToMQ;
import com.staples.pim.delegate.datamigration.utils.DataMigAcceptItemManager;

public class FutureDatedPOListener extends MessageQueueListener {

	private static final short	XMLFILE_PUBLISH					= 5;
	private static final short	XMLFILE_PROCESSED				= 6;
	private static final short	XMLFILE_BAD						= 7;
	private static final short	DELIMITEDFILE					= 8;
	private static final short	FUTUREDATEDPO					= 2;
	private static final short	GETNEWINSTANCEFORFUTUREDATEDPO	= 22;
	private static final short	POCOSTCODEBOTH					= 4;

	public FutureDatedPOListener() {

		// TODO Auto-generated constructor stub
	}

	/**
	 * This method used to call the parsing method for galaxy out file and call
	 * the send method for step system.
	 * 
	 * @param message
	 *            message from galaxy system
	 */
	// @SuppressWarnings("unused")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected boolean processMessage(TextMessage message) {

		IntgSrvLogger logger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
		ByteArrayInputStream is = null;
		String msgPipDelim = null;
		String msgFixLength = null;
		String skuLifeCycleValue = null;
		int fileType = 0;
		List<String> listOfMessagesToPublish = null;
		List<List> listOfMessagesInTheListToPublish = null;
		boolean isSuccess = true;
		StepTransmitterBean serviceBean = null;
		StepTransmitterBean serviceBeanTO = null;
		String productID = null;
		int status = 0;

		CreateFlatFileOnFileSystem fileGenerator = new CreateFlatFileOnFileSystem();
		CreateXMLFileOnFileSystem xmlFileOnFileSystem = new CreateXMLFileOnFileSystem();
		PublisherToMQ publisherToMq = new PublisherToMQ();

		try {

			String messageText = message.getText();
			logger.debug("Actual message from Step Queue : " + message.getText());
			IntgSrvUtils.printConsole("Text = " + message.getText());

			/*
			 * This Ignore flag and Ignore action is to used for to specify do
			 * not process the Data Migration items, Also the Config
			 * Specification from SB Configuration file
			 */
			boolean acceptFlag = DataMigAcceptItemManager.getInstance().getFDPOAcceptFlag(messageText);
			logger.info("FDPO Items AcceptFlag:" + String.valueOf(acceptFlag));
			logger.info("JMS Message :" + messageText);

			if (!acceptFlag) {
				logger.info("FDPO Items found - JMS Message Ignored");
				return isSuccess;
			}

			/* convert String into InputStream */
			is = new ByteArrayInputStream(messageText.getBytes());
			logger.debug("ByteArrayInputStream is " + is);

			/* parse the XML and load to the java object */
			StreamSource ss = new StreamSource(is);
			Unmarshaller unmarshaller = this.getUnmarshaller();
			logger.debug("unmarshaller =   " + unmarshaller);
			STEPProductInformation spi = (STEPProductInformation) unmarshaller.unmarshal(ss);

			List<STEPProductInformation.Products.Product> products = spi.getProducts().product;

			Iterator<STEPProductInformation.Products.Product> itVendorProducts = products.iterator();

			// ItemToVendor details fetched and stored in a list.
			List<MultiValue> vendorUPCMultiValues = new ArrayList<MultiValue>();
			while (itVendorProducts.hasNext()) {
				STEPProductInformation.Products.Product vendorProduct = (STEPProductInformation.Products.Product) itVendorProducts.next();
				if (vendorProduct.getUserTypeID().equalsIgnoreCase("ItemToVendor")) {
					vendorUPCMultiValues = vendorProduct.getValues().getMultiValue();
				}
			}

			Iterator<STEPProductInformation.Products.Product> itProducts = products.iterator();
			while (itProducts.hasNext()) {
				STEPProductInformation.Products.Product product = (STEPProductInformation.Products.Product) itProducts.next();

				// filter object which UserTypeID is "Item"
				if (!product.getUserTypeID().equalsIgnoreCase("ITEM") && !product.getUserTypeID().equalsIgnoreCase("ItemToVendor")) {
					// Add item to vendor details to the product
					if (product.getUserTypeID().equalsIgnoreCase("SKU")) {
						product.getValues().getMultiValue().addAll(vendorUPCMultiValues);
					}
					FormaterToFixLength converterToFixlength = new FormaterToFixLength();

					serviceBean = converterToFixlength.buildFixLengthString(product.getProductCrossReference(), product
							.getProductCrossReference().getProduct().getValues().getValue(), product.getProductCrossReference()
							.getProduct().getValues().getMultiValue(), product.getValues().getValue(), product.getValues().getMultiValue(),
							product.getID());

					listOfMessagesInTheListToPublish = (List<List>) serviceBean.getItem(IntgSrvAppConstants.LIST_OF_MESSAGES_TO_HASHMAP);

					skuLifeCycleValue = (String) serviceBean.getItem(IntgSrvAppConstants.SKU_LIFE_CYCLE_TO_HASHMAP);
					productID = (String) serviceBean.getItem(IntgSrvAppConstants.PRODUCT_ID_TO_HASHMAP);

					for (int q = 0; q < listOfMessagesInTheListToPublish.size(); q++) {
						listOfMessagesToPublish = listOfMessagesInTheListToPublish.get(q);

						msgFixLength = listOfMessagesToPublish.get(0);
						msgPipDelim = listOfMessagesToPublish.get(1);

						/*
						 * publish generated message to MQ if Future dated PO
						 * otherwise publish to the BAD folder on the file
						 * system
						 */
						if (skuLifeCycleValue.equalsIgnoreCase(IntgSrvAppConstants.FUTURE_DATED_PO)) {

							fileType = XMLFILE_PUBLISH;
							serviceBean.setMessage(msgFixLength);
							serviceBean.setTransactionType(FUTUREDATEDPO);
							serviceBeanTO = this.setServiceBean(fileType, serviceBean);
							serviceBeanTO.setPublishId(IntgSrvAppConstants.STEE109);
							publisherToMq.publishToMq(serviceBeanTO);

							/*
							 * generate outputs to save incoming XML file to the
							 * file system just ones
							 */
							if (q == 0) {
								if (msgFixLength == null || msgFixLength.equalsIgnoreCase("")) {
									fileType = XMLFILE_BAD;
								} else {
									fileType = XMLFILE_PROCESSED;
								}
								serviceBean.setMessage(messageText);
								serviceBeanTO = this.setServiceBean(fileType, serviceBean);
								status = xmlFileOnFileSystem.writeXMLFile(serviceBeanTO);
							}
							/* process transformed message to the file system */
							if (fileType == XMLFILE_PROCESSED) {
								fileType = DELIMITEDFILE;
								serviceBean.setMessage(msgPipDelim);
								serviceBeanTO = this.setServiceBean(fileType, serviceBean);
								status = fileGenerator.writeOutFile(serviceBeanTO);
								if (status > 0)
									isSuccess = false;
							}
						} else {
							/*
							 * write inbound STEP XML file to the BAD folder on
							 * the file system
							 */
							fileType = XMLFILE_BAD;
							serviceBean.setMessage(messageText);
							serviceBeanTO = this.setServiceBean(fileType, serviceBean);
							status = xmlFileOnFileSystem.writeXMLFile(serviceBeanTO);
							if (status > 0)
								isSuccess = false;
						}
					} // end for
				}
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			isSuccess = false;
			IntgSrvUtils.alertByEmail(e, clazzname, IntgSrvAppConstants.STEE109);
		}
		return isSuccess;
	}

	private StepTransmitterBean setServiceBean(int fileType, StepTransmitterBean serviceBean) {

		String baseURL = null;
		String fileExtension = null;
		String fileName = null;

		StepTransmitterBean serviceBeanLoc = serviceBean;

		Date today = Calendar.getInstance().getTime();
		String todaysDateStr = IntgSrvUtils.getStringFromDate(today, "yyyy-MM-dd-hh.mm.ss");

		switch (fileType) {

			case 5:
				String mqHostName = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MQ_HOSTNAME);
				serviceBeanLoc.setMqHostName(mqHostName);

				int mqPort = Integer.parseInt(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MQ_PORT));
				serviceBeanLoc.setMqPport(mqPort);

				String mqQueueManager = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MQ_QUEUE_MANAGER);
				serviceBeanLoc.setMqQueueManager(mqQueueManager);

				String mqChannel = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MQ_CHANNEL);
				serviceBeanLoc.setMqChannel(mqChannel);

				String mqQueueName = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MQ_QUEUE_NAME_FDPO);
				serviceBeanLoc.setMqQueueName(mqQueueName);

				int mqTimeout = Integer.parseInt(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.MQ_TIMEOUT));
				serviceBeanLoc.setMqTimeout(mqTimeout);

				break;

			case 6:

				fileName = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.STIBO_MQ_RECIEVED_FILE_NAME);
				baseURL = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.XML_INPUT_ARCHIVE_DIRECTORY);
				serviceBeanLoc.setBaseURL(baseURL);
				fileExtension = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.FILE_EXTENSION_XML);
				serviceBeanLoc.setFileName(fileName + todaysDateStr + fileExtension);
				break;

			case 7:

				fileName = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.STIBO_MQ_RECIEVED_FILE_NAME);
				baseURL = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.XML_INPUT_ARCHIVE_FAILED_DIRECTORY);

				serviceBeanLoc.setBaseURL(baseURL);
				fileExtension = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.FILE_EXTENSION_XML);
				serviceBeanLoc.setFileName(fileName + todaysDateStr + fileExtension);
				break;

			case 8:
				baseURL = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.FIX_OUTPUT_DIRECTORY);
				serviceBeanLoc.setBaseURL(baseURL);
				fileExtension = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.FILE_EXTENSION_TEXT);
				fileName = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.STIBO_MQ_RECIEVED_FILE_NAME);
				serviceBeanLoc.setFileName(fileName + todaysDateStr + fileExtension);
				String headerLine = IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.FIX_HEADER_LINE_ATTRIBUTE_IDS);
				serviceBeanLoc.addItem(IntgSrvAppConstants.HEADER_LINE_TO_MAP, headerLine);
				break;
		}

		return serviceBeanLoc;
	}
}
