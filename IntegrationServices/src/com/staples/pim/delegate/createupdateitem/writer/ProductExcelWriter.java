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

package com.staples.pim.delegate.createupdateitem.writer;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.pim.base.common.bean.STEPProductInformation;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkICD;
import com.staples.pim.base.common.writer.ProductWriter;
import com.staples.pim.base.domain.ProductAttributes;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.createupdateitem.listenerrunner.RunSchedulerItemCreateUpdate;

@SuppressWarnings("hiding")
@Component("ProductExcelWriter")
@Scope("step")
public class ProductExcelWriter<Product> extends ProductWriter<Product> {

	private int							currRow	= 0;
	private Map<String, JobParameter>	jpMap	= null;

	public ProductExcelWriter() {

		// TODO Auto-generated constructor stub
	}

	public String[] getChannelSpecificAttributes(String[] headerString) {

		List<String> channelSpecificAttributes = new ArrayList<String>();
		channelSpecificAttributes.add("A0013");
		channelSpecificAttributes.add("A0018");
		channelSpecificAttributes.add("A0045");
		channelSpecificAttributes.add("A0046");
		channelSpecificAttributes.add("A0067");
		channelSpecificAttributes.add("A0075");
		channelSpecificAttributes.add("A0077");
		channelSpecificAttributes.add("A0078");
		channelSpecificAttributes.add("A0430");
		for (String channelSpAttribute : channelSpecificAttributes) {
			for (int i = 0; i < headerString.length; i++) {
				headerString[i] = headerString[i].replaceAll(channelSpAttribute + "_RET", channelSpAttribute);
				headerString[i] = headerString[i].replaceAll(channelSpAttribute + "_NAD", channelSpAttribute);
				headerString[i] = headerString[i].replaceAll(channelSpAttribute + "_SUPC", channelSpAttribute);
				headerString[i] = headerString[i].replaceAll(channelSpAttribute + "_CUPC", channelSpAttribute);
				headerString[i] = headerString[i].replaceAll(channelSpAttribute + "_IUPC", channelSpAttribute);
				headerString[i] = headerString[i].replaceAll(channelSpAttribute + "_PUPC", channelSpAttribute);
			}
		}
		return headerString;
	}

	private void addHeaders(Sheet sheet) {

		Workbook wb = sheet.getWorkbook();

		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();

		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Row row = sheet.createRow(2);
		int col = 0;

		if (null == ProductAttributes.iDsEXCEL) {
			ProductAttributes productAttr = new ProductAttributes();
		}
		String[] excelHeaders = getChannelSpecificAttributes(ProductAttributes.iDsEXCEL);
		for (String header : excelHeaders) {
			Cell cell = row.createCell(col);
			cell.setCellValue(header);
			cell.setCellStyle(style);
			col++;
		}
		currRow++;
	}

	// @BeforeStep
	@Override
	public void beforeStep(StepExecution stepExecution) {

		traceLogger.info(clazzname, "beforeStep", "ENTER: StepExecutionListener");
		String tmpfilename = ehfItemLoggerExcel.getLogFilename(IntgSrvAppConstants.EHF_LOGGER_APPENDER_PRODUCTITEMS_EXCEL);
		traceLogger.info(clazzname, "beforeStep", "ehfItemLoggerExcel logfilename = " + tmpfilename);

		try {
			init();

			jpMap = stepExecution.getJobParameters().getParameters();
			String timestampSTEPExport = jpMap.get(IntgSrvAppConstants.JP_SETP_EXPORT_TIME).toString();
			String transactionType = jpMap.get(IntgSrvAppConstants.JP_TRANSACTION_TYPE_FILETOFILE).toString();
			String traceIdJobRunner = jpMap.get(IntgSrvAppConstants.JP_EHF_TRACEID_JOBRUNNER).toString();
			String traceIdTransform = jpMap.get(IntgSrvAppConstants.JP_EHF_TRACEID_TRANSFORM).toString();
			String inputFile = jpMap.get(IntgSrvAppConstants.JP_INPUT_FILE).toString();
			outputFilename = jpMap.get(IntgSrvAppConstants.JP_OUTPUT_FILE_EXCEL).toString();

			// EHFLogger: the log entry
			// Additional info attributes need to use the explicit APIs of
			// ErrroHandlingFrameworkICD
			traceId = traceIdTransform;
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_KEY,
					IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_TRANSFORM);
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_INPUT_FILENAME, inputFile);
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_STEP_EXPORT_TIME, timestampSTEPExport);
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_JOBRUNNER, traceIdJobRunner);
			ehfICD.setAttributeTransactionType(transactionType);
			ehfICD.setAttributePublishId(IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_EXCEL);
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_OUTPUT_FILENAME, outputFilename);

			IntgSrvUtils.printConsole("Calling beforeStep");
			currRow = 0;
			IntgSrvUtils.printConsole("Excel outputFilename=" + outputFilename);

			workbook = new SXSSFWorkbook(100);
			Sheet sheet = workbook.createSheet("Pricing Attribute Values");
			sheet.createFreezePane(0, 3, 0, 3);
			sheet.setDefaultColumnWidth(20);

			addTitleToSheet(sheet);
			currRow++;
			addHeaders(sheet);
			initDataStyle();

			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			String msgDesc = "SpringBatch ProductExcelWriter initialized <- job params, StepExecutionContext";
			String infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
					IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);

			ehfLogger.info(infoLogString);
			traceLogger.info(clazzname, "beforeStep", msgDesc);
		} catch (Throwable exception) {
			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			String errorLogString = ehfHandler.getErrorLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					exception, IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);

			ehfLogger.error(errorLogString);
			ehfItemLoggerExcel.error(errorLogString);
			traceLogger.error(clazzname, "beforeStep", ehfICD.toStringEHFExceptionStackTrace(exception));
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerItemCreateUpdate.publishIds);
		}
		traceLogger.info(clazzname, "beforeStep", "EXIT: spreadsheet title and header written, currRow = " + currRow);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		IntgSrvUtils.printConsole("Calling afterStep");
		traceLogger.info(clazzname, "afterStep", "ENTER: StepExecutionListner");
		FileOutputStream fos;

		try {

			String inputFile = jpMap.get(IntgSrvAppConstants.JP_INPUT_FILE).toString();
			String timestampSTEPExport = jpMap.get(IntgSrvAppConstants.JP_SETP_EXPORT_TIME).toString();
			String transactionType = jpMap.get(IntgSrvAppConstants.JP_TRANSACTION_TYPE_FILETOFILE).toString();
			String traceIdJobRunner = jpMap.get(IntgSrvAppConstants.JP_EHF_TRACEID_JOBRUNNER).toString();
			String traceIdTransform = jpMap.get(IntgSrvAppConstants.JP_EHF_TRACEID_TRANSFORM).toString();

			// EHFLogger: the log entry
			// Additional info attributes need to use the explicit APIs of
			// ErrroHandlingFrameworkICD
			traceId = traceIdTransform;
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_KEY,
					IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_TRANSFORM);
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_INPUT_FILENAME, inputFile);
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_STEP_EXPORT_TIME, timestampSTEPExport);
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_JOBRUNNER, traceIdJobRunner);
			ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_OUTPUT_FILENAME, outputFilename);
			ehfICD.setAttributeTransactionType(transactionType);
			ehfICD.setAttributePublishId(IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_EXCEL);

			fos = new FileOutputStream(outputFilename);
			workbook.write(fos);
			fos.close();

			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			String msgDesc = "SpringBatch ProductExcelWriter Complete with ==>> STEP_EXECUTION_EXIT_STATUS={" + stepExecution.toString()
					+ "}";
			String infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
					IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
			ehfLogger.info(infoLogString);
			ehfItemLoggerExcel.info("[ - END - ]");
			traceLogger.info(clazzname, "afterStep", msgDesc);
		} catch (Throwable exception) {
			// TODO Auto-generated catch block
			ehfICD.setAttributePublishId(IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_EXCEL);
			String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
			String codeModule = getClass().getName();
			String errorLogString = ehfHandler.getErrorLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
					exception, IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
			ehfLogger.error(errorLogString);
			ehfItemLoggerExcel.error(errorLogString);
			traceLogger.error(clazzname, "afterStep", ehfICD.toStringEHFExceptionStackTrace(exception));
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerItemCreateUpdate.publishIds);
		}
		traceLogger.info(clazzname, "afterStep", "EXIT");
		return null;
	}

	@Override
	public void write(List<? extends Product> items) throws Exception {

		traceLogger.info(clazzname, "write", "ENTER: ItemWriter<Product>,ItemStream");
		int numItems = items.size();

		IntgSrvUtils.printConsole("in excel write(List<? extends STEPProductInformation> items ) numItems = " + numItems);
		ehfItemLoggerExcel.info("[ Number of Items: " + numItems + " ]");

		String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
		String codeModule = getClass().getName();
		String msgDesc = "SpringBatch ProductExcelWriter to write total number of items: " + numItems;
		String infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
				ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
				IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
		ehfLogger.info(infoLogString);
		traceLogger.info(clazzname, "write", msgDesc);

		ehfICD.setAttributeFreeForm(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_KEY,
				IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_TYPE_ITEM);
		ehfICD.removeAttribute(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_INPUT_FILENAME);
		ehfICD.removeAttribute(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_OUTPUT_FILENAME);
		ehfICD.removeAttribute(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_STEP_EXPORT_TIME);
		ehfICD.removeAttribute(IntgSrvErrorHandlingFrameworkConstants.EHF_ATTR_TRACEID_JOBRUNNER);
		ehfICD.removeAttribute(ErrorHandlingFrameworkICD.EHF_ATTR_TRANSACTION_TYPE);

		if (workbook == null) {
			IntgSrvUtils.printConsole("workbook is NULL");
			traceLogger.info(clazzname, "write", "workbook is NULL");
		} else {
			IntgSrvUtils.printConsole("workbook is NOT NULL");
			traceLogger.info(clazzname, "write", "workbook is NOT NULL");
		}
		Sheet sheet = workbook.getSheetAt(0);
		if (sheet == null) {
			IntgSrvUtils.printConsole("sheet is NULL");
			traceLogger.info(clazzname, "write", "sheet is NULL");
		} else {
			IntgSrvUtils.printConsole("sheet is NOT NULL");
			traceLogger.info(clazzname, "write", "sheet is NOT NULL");
		}

		HashMap<String, String> allValuesHashMap = new ProductAttributes().getAttrHashMapForEXCEL();
		int numProductAttr = 0;
		for (Product product : items) {
			currRow++;
			IntgSrvUtils.printConsole("currRow=" + currRow);
			Row row = sheet.createRow(currRow);

			traceId = ((IntgSrvErrorHandlingFrameworkICD) ehfICD).getNewTraceId();
			String requestId = IntgSrvUtils.getUniqueID();
			String publishId = IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_EXCEL;

			// LW-TBD ???
			// transform header metadata
			allValuesHashMap.put("traceid", traceId);
			allValuesHashMap.put(IntgSrvAppConstants.A0000, requestId);
			allValuesHashMap.put(IntgSrvAppConstants.A0001, IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_EXCEL);
			ehfICD.setAttributeRequestId(requestId);
			ehfICD.setAttributePublishId(IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_EXCEL);
			ehfICD.setAttributeItemType(null);
			ehfICD.setAttributeRequestType(null);

			List<STEPProductInformation.Products.Product.Values.Value> valueList = ((STEPProductInformation.Products.Product) product)
					.getValues().getValue();
			IntgSrvUtils.printConsole("---->> ProductExcelwriter valueList size=" + valueList.size());
			numProductAttr = valueList.size();
			traceLogger.info(clazzname, "write", "init header metadata, item " + (currRow - 3) + ": NEW requestId = " + requestId
					+ ", incoming number of attributes = " + numProductAttr);

			int i = 0;
			Iterator<STEPProductInformation.Products.Product.Values.Value> it = valueList.iterator();
			while (it.hasNext()) {
				STEPProductInformation.Products.Product.Values.Value valueAttr = (STEPProductInformation.Products.Product.Values.Value) it
						.next();
				// strHeaderLine = strHeaderLine + valueAttr.getAttributeID() +
				// strDelimiter;
				// strResult = strResult + valueAttr.getValue() + strDelimiter;
				// IntgSrvUtils.printConsole("strResult="+strResult);
				String iD = valueAttr.getAttributeID();
				// IntgSrvUtils.printConsole("excel iD="+iD + ", AttrValue = " +
				// valueAttr.getValue());

				// query header metadata
				if (iD.equals("A0000"))
					ehfICD.setAttributeRequestId(valueAttr.getValue());
				if (iD.equals("A0001"))
					ehfICD.setAttributePublishId(valueAttr.getValue());
				if (iD.equals("A0002"))
					ehfICD.setAttributeItemType(valueAttr.getValue());
				if (iD.equals("A0003"))
					ehfICD.setAttributeRequestType(valueAttr.getValue());

				if (valueAttr.getValue() == null || valueAttr.getValue() == "") {
					allValuesHashMap.put(iD, "[NoValue]");
				} else {
					allValuesHashMap.put(iD, valueAttr.getValue());
				}
				// traceLogger.debug(clazzname, "write", "Incoming attribute: "
				// + i++ + " - [ " + iD + ", " + allValuesHashMap.get(iD) +
				// " ]");
				ehfItemLoggerExcel.debug(clazzname, "write", "Incoming attribute: " + i++ + " - [ " + iD + ", " + allValuesHashMap.get(iD)
						+ " ]");
			} // while attrs

			int idx = 0;
			for (String iD : ProductAttributes.iDsEXCEL) {
				createStringCell(row, (String) allValuesHashMap.get(iD), idx);
				idx++;
			}

			/*
			 * createStringCell(row, data.getName(), 1);
			 */

		} // for items
		msgDesc = "IntegrationServices ProductExcelWriter processed item: number of attrs {incoming, processed, defined} = " + "{ "
				+ numProductAttr + ", " + allValuesHashMap.size() + ", " + ProductAttributes.iDsEXCEL.length + "}";
		infoLogString = ehfHandler.getInfoLog(new Date(), traceId, IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3,
				ErrorHandlingFrameworkICD.EHF_MSGTYPE_INFO_NONSLA, msgDesc,
				IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, usecase, codeModule, ehfICD);
		ehfLogger.info(infoLogString);
		ehfItemLoggerExcel.info(infoLogString);
		traceLogger.info(clazzname, "write", msgDesc);

		// reset/remove ehfICD item level info
		ehfICD.setAttributePublishId(IntgSrvAppConstants.SPRINGBATCH_PUBLISH_ID_EXCEL);
		ehfICD.removeAttribute(ErrorHandlingFrameworkICD.EHF_ATTR_REQUEST_ID);
		ehfICD.removeAttribute(ErrorHandlingFrameworkICD.EHF_ATTR_REQUEST_TYPE);
		ehfICD.removeAttribute(ErrorHandlingFrameworkICD.EHF_ATTR_ITEM_TYPE);
		traceLogger.info(clazzname, "write", "EXIT");
	}
}
