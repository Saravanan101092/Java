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

package com.staples.pim.base.common.writer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants; 
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;

public abstract class ProductWriter<Product> implements ItemWriter<Product>,ItemStream,StepExecutionListener { 

	public ProductWriter()   {
		// original productExcelWriter
	}
	
	protected String outputFilename = null;; 

	protected Workbook workbook = null; 
	protected CellStyle dataCellStyle = null; 
	protected int currRow = 0; 
   
    protected IntgSrvLogger ehfLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
    protected IntgSrvLogger traceLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
    protected IntgSrvLogger ehfItemLoggerExcel = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_EXCEL);
    protected ErrorHandlingFrameworkICD ehfICD;
	protected ErrorHandlingFrameworkHandler ehfHandler = new ErrorHandlingFrameworkHandler();
	protected String clazzname = this.getClass().getName();
	protected String traceId;
	
	protected void init() throws ErrorHandlingFrameworkException {
		ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
						IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM, 
						IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID, 
						IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
	}
	
	
	public String getOutputFilename() {
	    	traceLogger.info(clazzname, "getOutputFilename", "ENTER/EXIT: outputFilename = " + outputFilename);
			return outputFilename;
		}

	public void setOutputFilename(String value) {
	    	traceLogger.info(clazzname, "setOutputFilename", "ENTER/EXIT: value = " + value);
			this.outputFilename = value;
		} 
	  		
	protected void addTitleToSheet(Sheet sheet) {  
	  
	        Workbook wb = sheet.getWorkbook(); 
	  
	        CellStyle style = wb.createCellStyle(); 
	        Font font = wb.createFont(); 
	  
	        font.setFontHeightInPoints((short) 14); 
	        font.setFontName("Arial"); 
	        font.setBoldweight(Font.BOLDWEIGHT_BOLD); 
	        style.setAlignment(CellStyle.ALIGN_CENTER); 
	        style.setFont(font); 
	  
	        Row row = sheet.createRow(currRow); 
	        row.setHeightInPoints(16); 
	        SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("yyyyMMdd_HHmmsss");  
	        String currDate = dateformatyyyyMMdd.format(new Date());
	  
	        Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING); 
	        cell.setCellValue("Data as of " + currDate); 
	        cell.setCellStyle(style); 
	  
	        CellRangeAddress range = new CellRangeAddress(0, 0, 0, 7); 
	        sheet.addMergedRegion(range); 
	        currRow++;  
	    } 
	    
	protected void initDataStyle() {  
	        dataCellStyle = workbook.createCellStyle(); 
	        Font font = workbook.createFont(); 
	  
	        font.setFontHeightInPoints((short) 10); 
	        font.setFontName("Arial"); 
	        dataCellStyle.setAlignment(CellStyle.ALIGN_LEFT); 
	        dataCellStyle.setFont(font);  
	    } 
	    
	protected void createStringCell(Row row, String val, int col) { 
	        Cell cell = row.createCell(col); 
	        cell.setCellType(Cell.CELL_TYPE_STRING); 
	        cell.setCellValue(val); 
	    } 
	  
	protected void createNumericCell(Row row, Double val, int col) { 
	        Cell cell = row.createCell(col); 
	        cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
	        cell.setCellValue(val); 
	    } 
	    
	public void close() throws ItemStreamException {
	    	//this.delegate.close();
	 		traceLogger.info(clazzname, "close", "ENTER/EXIT ItemWriter<Product>,ItemStream");
	    }
	public void open(ExecutionContext arg0) throws ItemStreamException {
	    	//this.delegate.open(arg0);
	 		traceLogger.info(clazzname, "open", "ENTER/EXIT: ItemWriter<Product>,ItemStream");
	    }
	public void update(ExecutionContext arg0) throws ItemStreamException {
	    	//this.delegate.update(arg0);
	 		traceLogger.info(clazzname, "update", "ENTER/EXIT: ItemWriter<Product>,ItemStream");
	    }   	    
	    
	   
	public abstract void write(List<? extends Product> items) throws Exception; 
	   
	public abstract ExitStatus afterStep(StepExecution stepExecution);	    
	   
	public abstract void beforeStep(StepExecution stepExecution);  
	   
	 
}
