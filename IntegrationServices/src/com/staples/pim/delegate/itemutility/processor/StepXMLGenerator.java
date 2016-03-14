
package com.staples.pim.delegate.itemutility.processor;

import static com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA;
import static com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_SLA;
import static com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH1;
import static com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH2;
import static com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3;
import static com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH4;
import static com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH5;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0003_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0012_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0077_NAD_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0077_RET_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0078_NAD_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0078_RET_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0410_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0497_NAD_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0497_RET_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0017_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0051_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0020_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0015_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0030_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0052_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0385_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0012_SUNBEAM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.OH;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.NI;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.N_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.Y_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.COR_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.DEBUG_XML_GENERATE_FLAG;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_ATTR_TRANSACTION_TYPE_QUEUETODB;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_ATTR_TRANSACTION_TYPE_QUEUETOSPRING;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_SPRINGBATCH_ITEMUTILITY_USER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EMPTY_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FLOAT_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_ITEMUTLITY;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.HYPHEN;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.INTEGER_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ITEMUTILITY_GALAXYOUT_MINLENGTH;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ITEM_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.KEY_VALUE_KEY_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.LIST_PRICE_UPDATE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PO_COST_UPDATE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONSIGNMENT_CODE_UPDATE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONSIGNMENT_COST_UPDATE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.MERCH_STATUS_UPDATE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRICE_UPDATE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.POG_ID_UPDATE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SKU_TYPE_UPDATE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.STOCK_CODE_UPDATE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PUBLISH_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.RET_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SCC_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_COMPONENT_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ECO_SYSTEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SPRINGBATCH_ICD_CLASSNAME;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.STRING_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.TRUE_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.USE_CONTEXT_LOCALE_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.XML_STR;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.poi.util.SystemOutLogger;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pcm.stepcontract.beans.KeyValueType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pcm.stepcontract.beans.YesNoType;
import com.staples.pim.base.common.bean.StepTransmitterBean;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.MQConnectionManager;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.itemutility.beans.CommonVO;
import com.staples.pim.delegate.itemutility.beans.PropertiesVO;
import com.staples.pim.delegate.itemutility.beans.ReportVO;

/**
 * @author 522462 This class contains methods to generate the Step XML from
 *         Galaxy fixed length file
 */

public class StepXMLGenerator {

	CommonVO commonVO;

	public CommonVO getCommonVO() {

		return commonVO;
	}

	public void setCommonVO(CommonVO commonVO) {

		this.commonVO = commonVO;
	}

	public static IntgSrvLogger ehfLogger = IntgSrvLogger.getInstance(EHF_LOGGER);

	static IntgSrvLogger logger = IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER_ITEMUTLITY);

	public static ErrorHandlingFrameworkICD ehfICD = null;

	public static ErrorHandlingFrameworkHandler ehfHandler = new ErrorHandlingFrameworkHandler();

	public static String traceId = null;

	JdbcBatchItemWriter<ReportVO>	channelSpecDBWriter;
	JdbcBatchItemWriter<ReportVO>	hiddenAttrDBWriter;
	JdbcBatchItemWriter<ReportVO>	internalAttrDBWriter;
	JdbcBatchItemWriter<ReportVO>	vendorProvidedAttrDBWriter;

	public JdbcBatchItemWriter<ReportVO> getChannelSpecDBWriter() {

		return channelSpecDBWriter;
	}

	public void setChannelSpecDBWriter(JdbcBatchItemWriter<ReportVO> channelSpecDBWriter) {

		this.channelSpecDBWriter = channelSpecDBWriter;
	}

	public JdbcBatchItemWriter<ReportVO> getHiddenAttrDBWriter() {

		return hiddenAttrDBWriter;
	}

	public void setHiddenAttrDBWriter(JdbcBatchItemWriter<ReportVO> hiddenAttrDBWriter) {

		this.hiddenAttrDBWriter = hiddenAttrDBWriter;
	}

	public JdbcBatchItemWriter<ReportVO> getInternalAttrDBWriter() {

		return internalAttrDBWriter;
	}

	public void setInternalAttrDBWriter(JdbcBatchItemWriter<ReportVO> internalAttrDBWriter) {

		this.internalAttrDBWriter = internalAttrDBWriter;
	}

	public JdbcBatchItemWriter<ReportVO> getVendorProvidedAttrDBWriter() {

		return vendorProvidedAttrDBWriter;
	}

	public void setVendorProvidedAttrDBWriter(JdbcBatchItemWriter<ReportVO> vendorProvidedAttrDBWriter) {

		this.vendorProvidedAttrDBWriter = vendorProvidedAttrDBWriter;
	}

	// ObjectFactory objectFactory;
	ObjectFactory objectFactory;

	/**
	 * Split the Fixed length string as header,values using delimiter.
	 * 
	 * @param message
	 *            message which is received from Galaxy system
	 * @return XML string which is compatible for STEP.
	 * @throws ErrorHandlingFrameworkException
	 */
	public boolean getStepInputFromGalaxy(String message) throws ErrorHandlingFrameworkException, Exception {

		String logMessage = null;
		boolean returnAck = Boolean.TRUE;
		ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(SPRINGBATCH_ECO_SYSTEM, SPRINGBATCH_COMPONENT_ID,
				SPRINGBATCH_ICD_CLASSNAME);

		ehfICD.setAttributePublishId(PUBLISH_ID);
		traceId = ((ErrorHandlingFrameworkICD) ehfICD).createNewTraceId();

		String stepXml = null;
		boolean isValidGalaxy = Boolean.FALSE;
		try {
			List<ReportVO> dbValue = null;

			// write fix length file in debug folder
			if (TRUE_STR.equalsIgnoreCase(IntgSrvPropertiesReader.getProperty(DEBUG_XML_GENERATE_FLAG))) {
				String fixlengthPath = IntgSrvUtils
						.reformatFilePath(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.ITEMUTILITY_INBOUND_FL_INPUT_DIRECTORY))
						+ "fixlength-" + new Date().getTime() + ".txt";
				File file = new File(fixlengthPath);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				FileWriter outputStream = new FileWriter(fixlengthPath);

				outputStream.write(message);
				outputStream.close();
			}

			// Attribute Name and position details to PropertiesVO
			int lengthOfGalaxyOut = IntgSrvUtils.toInt((String) IntgSrvPropertiesReader.getProperty(ITEMUTILITY_GALAXYOUT_MINLENGTH));

			ehfICD.setAttributeTransactionType(EHF_ATTR_TRANSACTION_TYPE_QUEUETOSPRING);
			logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH4, EHF_MSGTYPE_INFO_NONSLA,
					"Received message from galaxy queue", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
					DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.info(logMessage);
			// Validate the galaxy message minimum length
			if (!IntgSrvUtils.isEmptyString(message) && message.length() >= lengthOfGalaxyOut) {
				logger.info("Galaxy message actual length is : " + message.length());

				// Parse the galaxy message and put into Common value object
				CommonVO commonVO = fixedWidthStringToObject(message);

				ehfICD.setAttributeRequestType(commonVO.getTransactionType());
				logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH5, EHF_MSGTYPE_INFO_NONSLA,
						"Retrieve the attribute values from galaxy message...", EHF_SPRINGBATCH_ITEMUTILITY_USER,
						DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
				ehfLogger.info(logMessage);
				/**
				 * Mandatory Validation to check the transaction type, this
				 * should be PO Cost Update or List Price Update
				 */
				if (isValidTransaction(commonVO.getTransactionType())) {
					logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH5, EHF_MSGTYPE_INFO_NONSLA,
							"Transaction type is valid...", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
							DatamigrationCommonUtil.getClassName(), ehfICD);
					ehfLogger.info(logMessage);
					try {
						logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH5, EHF_MSGTYPE_INFO_NONSLA,
								"Mandatory, data type and length validation start...", EHF_SPRINGBATCH_ITEMUTILITY_USER,
								DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
						ehfLogger.info(logMessage);

						isValidGalaxy = GalaxyOutboundValidator.isValidGalaxyMessage(commonVO);

						logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH5, EHF_MSGTYPE_INFO_NONSLA,
								"Mandatory, datatype and length validation end...", EHF_SPRINGBATCH_ITEMUTILITY_USER,
								DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
						ehfLogger.info(logMessage);
					} catch (Throwable e) {
						// Must log this error into EHF. Since its galaxy out
						// bound message issue. Validation got failed
						logMessage = ehfHandler.getErrorLog(new Date(), traceId, EHF_ERROR_PATH5, e, EHF_SPRINGBATCH_ITEMUTILITY_USER,
								DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
						ehfLogger.error(logMessage);
						DatamigrationCommonUtil.printConsole("Throwable catch...");
						e.printStackTrace();
					}
					if (isValidGalaxy) {

						try {
							logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH5, EHF_MSGTYPE_INFO_NONSLA,
									"Validation Status :" + isValidGalaxy, EHF_SPRINGBATCH_ITEMUTILITY_USER,
									DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
							ehfLogger.info(logMessage);

							/**
							 * based on the transaction type, should send either
							 * List price or PO Cost.
							 */
							removeUnwantedAttribute(commonVO);

							logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
									"Update Galaxy data into StepProdInformation Object..", EHF_SPRINGBATCH_ITEMUTILITY_USER,
									DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
							ehfLogger.info(logMessage);
							commonVO = updateStepProdInformation(commonVO);

							logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
									"Step XML generation start...", EHF_SPRINGBATCH_ITEMUTILITY_USER,
									DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
							ehfLogger.info(logMessage);

							stepXml = generateStepXML(commonVO);

							logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
									"Step XML generation end...", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
									DatamigrationCommonUtil.getClassName(), ehfICD);
							ehfLogger.info(logMessage);
							logger.info("XML : Galaxy -> Step : " + stepXml);
							// Step XML publish call starts...

							if (stepXml != null) {
								ehfICD.setAttributeTransactionType("Spring-to-Queue");
								logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
										"Step XML publish start...", EHF_SPRINGBATCH_ITEMUTILITY_USER,
										DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
								ehfLogger.info(logMessage);
								logger.info("Converted XML for Step Queue : " + stepXml);

								// StepMessageSender.messageSender(stepXml);
								StepTransmitterBean serviceBean = new StepTransmitterBean();
								serviceBean.setMessage(stepXml);
								serviceBean.setMqChannel(
										IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.ITEMUTILITY_MQ_CHANNEL_STEP));
								serviceBean.setMqHostName(
										IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.ITEMUTILITY_MQ_HOSTNAME_STEP));
								serviceBean.setMqPport(Integer
										.parseInt(IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.ITEMUTILITY_MQ_PORT_STEP)));
								serviceBean.setMqQueueManager(
										IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.ITEMUTILITY_MQ_QUEUEMANAGER_STEP));
								serviceBean.setMqQueueName(
										IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.ITEMUTILITY_MQ_QUEUENAME_STEP));
								MQConnectionManager mqconnectionmanager = MQConnectionManager.getInstance(serviceBean);
								mqconnectionmanager.putMessage(serviceBean);
								mqconnectionmanager.close();
								logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
										"Step XML published end.. Published successfully...", EHF_SPRINGBATCH_ITEMUTILITY_USER,
										DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
								ehfLogger.info(logMessage);
								logger.info("Step XML has been sent.");
								DatamigrationCommonUtil.printConsole("stepXml : " + stepXml);

								DatamigrationCommonUtil.printConsole("Step XML has been sent.");

							} else {
								logger.info("Galaxy message not process.");
							}
						} catch (Exception e) {

							DatamigrationCommonUtil.printConsole("Exception e....");
							logMessage = ehfHandler.getErrorLog(new Date(), traceId, EHF_ERROR_PATH2, e, EHF_SPRINGBATCH_ITEMUTILITY_USER,
									DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
							ehfLogger.error(logMessage);
							logger.error(e);
							logger.info("Message will be rollback....");
							logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH3, EHF_MSGTYPE_INFO_NONSLA,
									"Message will be rollback....", EHF_SPRINGBATCH_ITEMUTILITY_USER,
									DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
							ehfLogger.info(logMessage);
							e.printStackTrace();
							// throw new Exception(e);
							return Boolean.FALSE;
						}

						// Database update coding start.....
						ehfICD.setAttributeTransactionType(EHF_ATTR_TRANSACTION_TYPE_QUEUETODB);
						logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH1, EHF_MSGTYPE_INFO_NONSLA,
								"Validating & formatting the values for DB update...", EHF_SPRINGBATCH_ITEMUTILITY_USER,
								DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
						ehfLogger.info(logMessage);
						logger.info("Validating & formatting the values for DB update...");

						dbValue = getValueForDBUpdate(commonVO);

						DatamigrationCommonUtil.printConsole("After Validating & formatting the values in ReportVO :" + dbValue);
						logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH2, EHF_MSGTYPE_INFO_NONSLA,
								"Database update start...", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
								DatamigrationCommonUtil.getClassName(), ehfICD);
						ehfLogger.info(logMessage);
						logger.info("Database update start...");

						logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH2, EHF_MSGTYPE_INFO_NONSLA,
								"Update either list price or PO cost based on request type", EHF_SPRINGBATCH_ITEMUTILITY_USER,
								DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
						ehfLogger.info(logMessage);

						String transType = commonVO.getTransactionType();

						if ((LIST_PRICE_UPDATE.equalsIgnoreCase(transType)) || (PO_COST_UPDATE.equalsIgnoreCase(transType))) {

							channelSpecDBWriter.write(dbValue);
						} else if ((CONSIGNMENT_CODE_UPDATE.equalsIgnoreCase(transType))
								|| (CONSIGNMENT_COST_UPDATE.equalsIgnoreCase(transType))) {
							vendorProvidedAttrDBWriter.write(dbValue);
						} else if (MERCH_STATUS_UPDATE.equalsIgnoreCase(transType)) {
							hiddenAttrDBWriter.write(dbValue);
						} else if (((PRICE_UPDATE.equalsIgnoreCase(transType)) || (POG_ID_UPDATE.equalsIgnoreCase(transType))
								|| (SKU_TYPE_UPDATE.equalsIgnoreCase(transType)) || (STOCK_CODE_UPDATE.equalsIgnoreCase(transType)))) {
							internalAttrDBWriter.write(dbValue);
						}
						logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH2, EHF_MSGTYPE_INFO_NONSLA,
								"Database update end...", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
								DatamigrationCommonUtil.getClassName(), ehfICD);
						ehfLogger.info(logMessage);
						logger.info("DB Update Done");

					} else {
						logMessage = ehfHandler.getErrorLog(new Date(), traceId, EHF_ERROR_PATH1, new Exception("validation failed..."),
								EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
								DatamigrationCommonUtil.getClassName(), ehfICD);
						ehfLogger.error(logMessage);
						logger.info("validation failed.");
					}
				} else {

					logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH4, EHF_MSGTYPE_INFO_SLA,
							"Request type is :" + commonVO.getTransactionType()
									+ " : this should be PO Cost Update or List Price Update. So no need to consider this request..",
							EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
							DatamigrationCommonUtil.getClassName(), ehfICD);
					ehfLogger.info(logMessage);
					logger.info("Not a valid transaction.");
					logger.info(
							"Request type is :" + commonVO.getTransactionType() + " : this should be PO Cost Update or List Price Update.");
					// FIXME acknowledge process...
				}
			} else {

				logMessage = ehfHandler.getErrorLog(new Date(), traceId, EHF_ERROR_PATH1,
						new Exception("Invalid galaxy message. Message length should be equal or greater than :" + lengthOfGalaxyOut),
						EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(),
						ehfICD);
				ehfLogger.error(logMessage);
				logger.info("Invalid galaxy message. Message length should be equal or greater than :" + lengthOfGalaxyOut);
			}
		} catch (Exception e) {
			DatamigrationCommonUtil.printConsole("Common catch...");
			logMessage = ehfHandler.getErrorLog(new Date(), traceId, EHF_ERROR_PATH3, e, EHF_SPRINGBATCH_ITEMUTILITY_USER,
					DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
			ehfLogger.error(logMessage);
			DatamigrationCommonUtil.printConsole(e.getMessage());
			e.printStackTrace();
			logger.info(e.getMessage());
			// logger.info("Invalid file format. Getting exception while
			// parsing.");
			logger.error(e);
		}

		logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH1, EHF_MSGTYPE_INFO_NONSLA,
				"Galaxy to STEP Intg process end.", EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(),
				DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.info(logMessage);
		logger.info("End :getStepInputFromGalaxy");

		ehfICD.resetContextElements();
		logMessage = ehfHandler.getInfoLog(new Date(), traceId, EHF_ERROR_PATH2, EHF_MSGTYPE_INFO_NONSLA, "Waiting for next message....",
				EHF_SPRINGBATCH_ITEMUTILITY_USER, DatamigrationCommonUtil.getMethodName(), DatamigrationCommonUtil.getClassName(), ehfICD);
		ehfLogger.info(logMessage);
		return returnAck;
	}

	/**
	 * If value is po cost update or list cost update return true, otherwise
	 * false
	 * 
	 * @param requestType
	 * @return Boolean
	 */
	private boolean isValidTransaction(String requestType) {

		logger.info("requestType:: " + requestType + ":");
		if (LIST_PRICE_UPDATE.equalsIgnoreCase(requestType) || PO_COST_UPDATE.equalsIgnoreCase(requestType)
				|| CONSIGNMENT_CODE_UPDATE.equalsIgnoreCase(requestType) || CONSIGNMENT_COST_UPDATE.equalsIgnoreCase(requestType)
				|| MERCH_STATUS_UPDATE.equalsIgnoreCase(requestType) || PRICE_UPDATE.equalsIgnoreCase(requestType)
				|| POG_ID_UPDATE.equalsIgnoreCase(requestType) || SKU_TYPE_UPDATE.equalsIgnoreCase(requestType)
				|| STOCK_CODE_UPDATE.equalsIgnoreCase(requestType)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * This method used to remove the attributes based on transaction type
	 * 
	 * @param commonVO
	 */
	private void removeUnwantedAttribute(CommonVO commonVO) {

		Map<String, String> attributeMap = new HashMap<String, String>();
		if (LIST_PRICE_UPDATE.equalsIgnoreCase(commonVO.getTransactionType()) && RET_STR.equalsIgnoreCase(commonVO.getChannel())) {
			attributeMap.put(A0077_RET_ID, commonVO.getAttributeMap().get(A0077_RET_ID));
		} else if (LIST_PRICE_UPDATE.equalsIgnoreCase(commonVO.getTransactionType()) && SCC_STR.equalsIgnoreCase(commonVO.getChannel())) {
			attributeMap.put(A0077_NAD_ID, commonVO.getAttributeMap().get(A0077_NAD_ID));
		} else if (LIST_PRICE_UPDATE.equalsIgnoreCase(commonVO.getTransactionType()) && COR_STR.equalsIgnoreCase(commonVO.getChannel())) {
			commonVO.getAttributeMap().remove(A0078_RET_ID);
			commonVO.getAttributeMap().remove(A0078_NAD_ID);
			attributeMap.put(A0077_RET_ID, commonVO.getAttributeMap().get(A0077_RET_ID));
			attributeMap.put(A0077_NAD_ID, commonVO.getAttributeMap().get(A0077_NAD_ID));
		} else if (PO_COST_UPDATE.equalsIgnoreCase(commonVO.getTransactionType()) && RET_STR.equalsIgnoreCase(commonVO.getChannel())) {
			attributeMap.put(A0078_RET_ID, commonVO.getAttributeMap().get(A0078_RET_ID));
		} else if (PO_COST_UPDATE.equalsIgnoreCase(commonVO.getTransactionType()) && SCC_STR.equalsIgnoreCase(commonVO.getChannel())) {
			attributeMap.put(A0078_NAD_ID, commonVO.getAttributeMap().get(A0078_NAD_ID));
		} else if (PO_COST_UPDATE.equalsIgnoreCase(commonVO.getTransactionType()) && COR_STR.equalsIgnoreCase(commonVO.getChannel())) {
			attributeMap.put(A0078_RET_ID, commonVO.getAttributeMap().get(A0078_RET_ID));
			attributeMap.put(A0078_NAD_ID, commonVO.getAttributeMap().get(A0078_NAD_ID));
		} else if (CONSIGNMENT_CODE_UPDATE.equalsIgnoreCase(commonVO.getTransactionType())) {
			attributeMap.put(A0017_ID, commonVO.getAttributeMap().get(A0017_ID));
		} else if (CONSIGNMENT_COST_UPDATE.equalsIgnoreCase(commonVO.getTransactionType())) {
			attributeMap.put(A0051_ID, commonVO.getAttributeMap().get(A0051_ID));
		} else if (MERCH_STATUS_UPDATE.equalsIgnoreCase(commonVO.getTransactionType())) {
			attributeMap.put(A0020_ID, commonVO.getAttributeMap().get(A0020_ID));
		} else if (PRICE_UPDATE.equalsIgnoreCase(commonVO.getTransactionType())) {
			attributeMap.put(A0052_ID, commonVO.getAttributeMap().get(A0052_ID));
		} else if (POG_ID_UPDATE.equalsIgnoreCase(commonVO.getTransactionType())) {
			attributeMap.put(A0030_ID, commonVO.getAttributeMap().get(A0030_ID));
		} else if (SKU_TYPE_UPDATE.equalsIgnoreCase(commonVO.getTransactionType())) {
			attributeMap.put(A0015_ID, commonVO.getAttributeMap().get(A0015_ID));
		} else if (STOCK_CODE_UPDATE.equalsIgnoreCase(commonVO.getTransactionType())) {
			attributeMap.put(A0385_ID, commonVO.getAttributeMap().get(A0385_ID));
		}

		commonVO.setAttributeMap(attributeMap);
	}

	/**
	 * This method used to parse the galaxy message and put into Common value
	 * object
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	private CommonVO fixedWidthStringToObject(String message) throws Exception {

		CommonVO commonVO = new CommonVO();
		Map<String, String> attributeMap = new HashMap<String, String>();

		Map<String, PropertiesVO> propertyMap = DatamigrationCommonUtil.getPropertiesVO();

		logger.info("Attribute Prop List : " + propertyMap);

		PropertiesVO propertVo = null;

		for (Map.Entry<String, PropertiesVO> entry : propertyMap.entrySet()) {
			propertVo = entry.getValue();

			// removeNullValuesForDB() used to get the non null values
			String attVal = (String) DatamigrationCommonUtil
					.getNotNullValue(message.substring(propertVo.getStartPosition() - 1, propertVo.getEndPosition()), STRING_TYPE);

			DatamigrationCommonUtil
					.printConsole("Attribute Name:" + propertVo.getAttributeName() + ": Start:" + propertVo.getStartPosition() + ": End:"
							+ propertVo.getEndPosition() + ": Attribute : " + attVal + ":Attribute length:" + attVal.length());

			// This four value is mandatory to applying the condition in
			// transformation process.
			if (A0012_ID.equalsIgnoreCase(propertVo.getAttributeName())) {
				if (!"".equals(attVal)) {
					String skuNum = Integer.toString((Integer.parseInt(attVal.trim())));

					commonVO.setSkuNo(skuNum);
				}
			} else if (A0003_ID.equalsIgnoreCase(propertVo.getAttributeName())) {

				commonVO.setTransactionType(attVal.trim());

			} else if (A0410_ID.equalsIgnoreCase(propertVo.getAttributeName())) {
				commonVO.setChannel(attVal.trim());

			}
			/*
			 * else if(A0075_ID.equalsIgnoreCase(propertVo.getAttributeName())){
			 * commonVO.setVendorNo(attVal.trim()); } else
			 * if(A0013_ID.equalsIgnoreCase(propertVo.getAttributeName())){
			 * commonVO.setModelNo(attVal.trim()); }
			 */else {

				attributeMap.put(propertVo.getAttributeName(), attVal.trim());
			}
		}
		if (STOCK_CODE_UPDATE.equals(commonVO.getTransactionType())) {
			String skuNum = Integer.toString((Integer.parseInt(attributeMap.get(A0012_SUNBEAM))));

			commonVO.setSkuNo(skuNum);
			attributeMap.remove(A0012_SUNBEAM);
		}
		if (SKU_TYPE_UPDATE.equals(commonVO.getTransactionType())) {
			String skuType = attributeMap.get(A0015_ID);

			if (OH.equals(skuType)) {
				attributeMap.put(A0015_ID, N_STR);
			} else if (NI.equals(skuType)) {
				attributeMap.put(A0015_ID, Y_STR);
			}
		}
		commonVO.setAttributeMap(attributeMap);

		return commonVO;
	}

	/**
	 * This method used to update the required attributes to
	 * STEPProductInformation
	 * 
	 * @param commonVO
	 * @return
	 */
	private CommonVO updateStepProdInformation(CommonVO commonVO) {

		STEPProductInformation stepProductInformation = null;
		ProductsType products = null;
		ProductType product = null;
		ValuesType values = null;
		ValueType value = null;
		KeyValueType keyValue = null;

		stepProductInformation = objectFactory.createSTEPProductInformation();
		products = objectFactory.createProductsType();
		product = objectFactory.createProductType();
		values = objectFactory.createValuesType();
		keyValue = objectFactory.createKeyValueType();

		for (Entry<String, String> mapObj : commonVO.getAttributeMap().entrySet()) {
			value = objectFactory.createValueType();
			if (A0077_RET_ID.equalsIgnoreCase(mapObj.getKey()) || A0077_NAD_ID.equalsIgnoreCase(mapObj.getKey())
					|| A0078_RET_ID.equalsIgnoreCase(mapObj.getKey()) || A0078_NAD_ID.equalsIgnoreCase(mapObj.getKey())
					|| A0051_ID.equalsIgnoreCase(mapObj.getKey()) || A0052_ID.equalsIgnoreCase(mapObj.getKey())) {

				double costPrice = GalaxyOutboundValidator.changeCostAndPriceValueFormat(mapObj.getValue(), mapObj.getKey());
				if (A0078_RET_ID.equalsIgnoreCase(mapObj.getKey())) {
					/* changed from A0078 to A0497 for Sprint1 */
					value.setAttributeID(A0497_RET_ID);
					value.setContent(EMPTY_STR + costPrice);
				} else if (A0078_NAD_ID.equalsIgnoreCase(mapObj.getKey())) {
					/* changed from A0078 to A0497 for Sprint1 */
					value.setAttributeID(A0497_NAD_ID);
					value.setContent(EMPTY_STR + costPrice);
				} else {
					value.setAttributeID(mapObj.getKey());
					value.setContent(EMPTY_STR + costPrice);
				}
			} else {
				// value = objectFactory
				// .createSTEPProductInformationProductsProductValuesValue();
				value.setAttributeID(mapObj.getKey());
				value.setContent(mapObj.getValue());
			}
			// Value added into ValueS list
			values.getValueOrMultiValueOrValueGroup().add(value);
		}
		// If you need channel(A0410) in step XML, please enable below commented
		// lines
		/*
		 * value = objectFactory
		 * .createSTEPProductInformationProductsProductValuesValue();
		 * value.setAttributeID(A0003_ID);
		 * value.setValue(commonVO.getChannel());
		 */

		keyValue.setKeyID(KEY_VALUE_KEY_ID);// Hard coded value
		keyValue.setContent(commonVO.getSkuNo());
		product.setUserTypeID(ITEM_STR);// Hard coded value
		product.setKeyValue(keyValue);
		product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
		products.getProduct().add(product);
		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
																								// Date
																								// time
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
																// value
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
																		// coded
																		// value
		stepProductInformation.setUseContextLocale(USE_CONTEXT_LOCALE_VALUE);// Hard
																				// coded
																				// value
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);// Hard coded
																	// value
		stepProductInformation.setProducts(products);
		stepProductInformation.setAutoApprove(YesNoType.Y);// Hard coded value
		commonVO.setStepProductInformation(stepProductInformation);
		return commonVO;

	}

	/**
	 * Generate the XML using marshaler
	 * 
	 * @param stepProductInformation
	 * @return
	 * @throws Exception
	 */
	private String generateStepXML(CommonVO commonVO) throws Exception {

		STEPProductInformation stepProductInformation = commonVO.getStepProductInformation();
		StringWriter stringWriter = new StringWriter();

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(STEPProductInformation.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			jaxbMarshaller.marshal(stepProductInformation, stringWriter);

			if (TRUE_STR.equalsIgnoreCase(IntgSrvPropertiesReader.getProperty(DEBUG_XML_GENERATE_FLAG))) {
				xmlFileCreateForDebug(commonVO, jaxbMarshaller);
			}

			logger.info("Marshalling Done.");
			DatamigrationCommonUtil.printConsole("Generated STEP XML is: " + stringWriter.toString());

		} catch (JAXBException e) {
			throw new Exception(e);
		}
		return stringWriter.toString();
	}

	/**
	 * Method used to generate XML file
	 * 
	 * @param commonVO
	 * @param jaxbMarshaller
	 */
	private void xmlFileCreateForDebug(CommonVO commonVO, Marshaller jaxbMarshaller) {

		STEPProductInformation stepProductInformation = commonVO.getStepProductInformation();
		try {

			String path = IntgSrvUtils
					.reformatFilePath(IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.ITEMUTILITY_INBOUND_XML_OUTPUT_DIRECTORY));

			logger.info("Generate the xml file for debug.");
			/*
			 * File file = new File(path + XML_STR + commonVO.getVendorNo() +
			 * HYPHEN + commonVO.getModelNo() + HYPHEN + new Date().getTime() +
			 * ".xml");
			 */
			File file = new File(path + commonVO.getSkuNo() + HYPHEN + new Date().getTime() + ".xml");
			file.getParentFile().mkdirs();
			logger.info("File location is:" + file.getPath());
			jaxbMarshaller.marshal(stepProductInformation, file);
		} catch (Exception exp) {
			logger.warn("Problem in XML file creation...");
		}

	}

	/**
	 * To get the ReportVO from galaxy out and update to DB
	 * 
	 * @param commonVO
	 * @return List<ReportVO>
	 */
	private List<ReportVO> getValueForDBUpdate(CommonVO commonVO) {

		List<ValueType> value = new ArrayList<ValueType>();
		List<Object> objContainingValues = commonVO.getStepProductInformation().getProducts().getProduct().get(0)
				.getProductOrSequenceProductOrSuppressedProductCrossReference();
		for (Object valuesObj : objContainingValues) {
			if ("ValuesType".equalsIgnoreCase(valuesObj.getClass().getSimpleName())) {
				ValuesType valuesType = (ValuesType) valuesObj;
				List<Object> objContainingValue = valuesType.getValueOrMultiValueOrValueGroup();
				for (Object valueObject : objContainingValue) {
					if ("ValueType".equalsIgnoreCase(valueObject.getClass().getSimpleName())) {
						ValueType valuetype = (ValueType) valueObject;
						value.add(valuetype);
					}
				}
			}
		}
		ReportVO reportVO = new ReportVO();
		List<ReportVO> reportList = new ArrayList<ReportVO>();

		/* :A0003, :A0077, :A0078, :A0410, :A0013, :A0075 */
		/* changed from A0078 to A0497 for Sprint1 */
		reportVO.setA0003((String) DatamigrationCommonUtil.getNotNullValue(commonVO.getTransactionType(), STRING_TYPE));

		if (commonVO.getChannel() != null) {
			reportVO.setA0410((String) DatamigrationCommonUtil.getNotNullValue(commonVO.getChannel(), STRING_TYPE));
		}

		/*
		 * reportVO.setA0013((String) DatamigrationCommonUtil.getNotNullValue(
		 * commonVO.getModelNo(), STRING_TYPE)); reportVO.setA0075((Integer)
		 * DatamigrationCommonUtil.getNotNullValue( commonVO.getVendorNo(),
		 * INTEGER_TYPE));
		 */
		reportVO.setA0012((Integer) DatamigrationCommonUtil.getNotNullValue(commonVO.getSkuNo(), INTEGER_TYPE));

		for (ValueType obj : value) {

			if (A0077_RET_ID.equalsIgnoreCase(obj.getAttributeID())) {
				reportVO.setA0077_RET((Double) DatamigrationCommonUtil.getNotNullValue(obj.getContent(), FLOAT_TYPE));
			} else if (A0077_NAD_ID.equalsIgnoreCase(obj.getAttributeID())) {
				reportVO.setA0077_NAD((Double) DatamigrationCommonUtil.getNotNullValue(obj.getContent(), FLOAT_TYPE));
			} else if (A0497_RET_ID.equalsIgnoreCase(obj.getAttributeID())) {

				reportVO.setA0497_RET((Double) DatamigrationCommonUtil.getNotNullValue(obj.getContent(), FLOAT_TYPE));
			} else if (A0497_NAD_ID.equalsIgnoreCase(obj.getAttributeID())) {

				reportVO.setA0497_NAD((Double) DatamigrationCommonUtil.getNotNullValue(obj.getContent(), FLOAT_TYPE));
			} else if (A0017_ID.equalsIgnoreCase(obj.getAttributeID())) {

				reportVO.setA0017((String) DatamigrationCommonUtil.getNotNullValue(obj.getContent(), STRING_TYPE));

			} else if (A0051_ID.equalsIgnoreCase(obj.getAttributeID())) {

				reportVO.setA0051((Double) DatamigrationCommonUtil.getNotNullValue(obj.getContent(), FLOAT_TYPE));
			} else if (A0020_ID.equalsIgnoreCase(obj.getAttributeID())) {

				reportVO.setA0020((String) DatamigrationCommonUtil.getNotNullValue(obj.getContent(), STRING_TYPE));
			} else if (A0015_ID.equalsIgnoreCase(obj.getAttributeID())) {

				reportVO.setA0015((String) DatamigrationCommonUtil.getNotNullValue(obj.getContent(), STRING_TYPE));
			} else if (A0030_ID.equalsIgnoreCase(obj.getAttributeID())) {

				reportVO.setA0030((String) DatamigrationCommonUtil.getNotNullValue(obj.getContent(), STRING_TYPE));

			} else if (A0052_ID.equalsIgnoreCase(obj.getAttributeID())) {

				reportVO.setA0052((Double) DatamigrationCommonUtil.getNotNullValue(obj.getContent(), FLOAT_TYPE));

			} else if (A0385_ID.equalsIgnoreCase(obj.getAttributeID())) {

				reportVO.setA0385((String) DatamigrationCommonUtil.getNotNullValue(obj.getContent(), STRING_TYPE));

			}
			reportList.add(reportVO);
		}
		return reportList;
	}

	/**
	 * @return objectFactory
	 */
	public ObjectFactory getObjectFactory() {

		return objectFactory;
	}

	/**
	 * @param objectFactory
	 */
	public void setObjectFactory(ObjectFactory objectFactory) {

		this.objectFactory = objectFactory;
	}

	public static void main(String st[]) {

		try {
			// new
			// StepXMLGenerator().getStepInputFromGalaxy("HEADER
			// PCM020001035201410280522098080GALE021 Item Utility PO Cost Update
			// 2014-10-28-05.32.03.071000
			// 0000100001000010000100001000010000100001000010000100001000010000000000000000000000000000000000000
			// 020001035123456 00 500000000 000 000000000000
			// 000000000000000000000000000000000000000000000000000000 00 00000
			// 0000000000
			// 0000000000000000000000000000000000000000000000000000000000000000000000000000000000000
			// 010801 000002950000004550 000000 000000 000000 000000
			// 0000000000000 000000 000 00000000 0001-01-01 0001-01-01
			// 0001-01-01 0000000 00000 RET COR 000005575 ");

			// CommonUtil.printConsole( new
			// StepXMLGenerator().ehfProcess("PO Cost
			// update","QueueToSpringBatch","Listener
			// started...","getStepXML","StepXMLGenerator",
			// null));

			// new StepXMLGenerator().getStepInputFromGalaxy("HEADER
			// PCM020005116201509290704524900GALE021 Item Utility SKU Type
			// Update 2015-09-29-07.05.01.182000
			// 0000100001000010000100001000010000100001000010000100001000010000000000000000000000000000000000000
			// 020005116RECLASS.@!2 00NI 700000000 000 000000000000
			// 000000000000000000000000000000000000000000000000000000 00
			// 000000000000000000 00000 0000000000
			// 0000000000000000000000000000000000000000000000000000000000000000000000000000000000000
			// 000021 000000000000000000 000000 000000 000000 000000
			// 0000000000000 000000 000 00000000 0001-01-01 0001-01-01
			// 0001-01-01 0000000 00000 00000000000 RET ");

			new StepXMLGenerator().getStepInputFromGalaxy(
					"HEADER    PCM020005116201509290704524900GALE021   Item Utility        SKU Type Update               2015-09-29-07.05.01.182000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    0000100001000010000100001000010000100001000010000100001000010000000000000000000000000000000000000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             020005116RECLASS.@!2    00NI                                                               700000000         000                000000000000                                      000000000000000000000000000000000000000000000000000000 00                                           000000000000000000                                                                                                                                                                                                                                                                                                                                                                                                                                                        00000      0000000000 0000000000000000000000000000000000000000000000000000000000000000000000000000000000000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       000021                                   000000000000000000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    000000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   000000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   000000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   000000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  0000000000000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     000000                                                                                                                                                                                                                                                      000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          00000000 0001-01-01     0001-01-01                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        0001-01-01                        0000000      00000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             00000000000                                                                                                                                                                                  RET                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ");

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}