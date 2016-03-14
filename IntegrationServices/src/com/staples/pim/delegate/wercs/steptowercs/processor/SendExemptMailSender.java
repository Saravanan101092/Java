package com.staples.pim.delegate.wercs.steptowercs.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.EmailUtil;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;


public class SendExemptMailSender {
	
	public String templateXLFileToner =IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty("WERCS_TONER_TEMPLATE_FILE"));
	public String templateXLFileWholeSaler = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty("WERCS_WHOLESALER_TEMPLATE_FILE"));
	public String outputFilepath = IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty("WERCS_EXEMPTEXCEL_OUTPUTPATH"));
	public String sendExemptMailSubject = IntgSrvPropertiesReader.getProperty("WERCS_EXEMPTMAIL_SUBJECT");
	public String sendExemptMailBody = IntgSrvPropertiesReader.getProperty("WERCS_EXEMPTMAIL_BODY");
	
	public int PIPID_ROW = Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_PIPID_ROW"));
	public int PIPID_CELL = Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_PIPID_CELL"));
	public int UPC_ROWNO = Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_UPC_ROW"));
	public int UPC_CELLNO = Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_UPC_CELL"));
	public int SHEETNO = Integer.parseInt(IntgSrvPropertiesReader.getProperty("WERCS_EXEMPTMAIL_SHEETNO"));
	
	public final String TONER_STR = "Toner"; 
	public final String xlextension = ".xlsx";
	public final String EXEMPT_PREFIX = "Exempt_";
	
	private static final String					TRACELOGGER_WERCS_STEPTOWERCS			= "tracelogger.wercs.steptowercs";
	static IntgSrvLogger						logger									= IntgSrvLogger
			.getInstance(TRACELOGGER_WERCS_STEPTOWERCS);
	
	
	public void createAndSendExemptMail(String upc, String pipID, String supplierID,String toner_wholesaler){
		
		File file = createXLDocument(upc,pipID,toner_wholesaler);
		
		//getSuppliermailID from database//FIXME
		
		//taking mail ID from configuration file for dev and testing purpose
		if(file!=null){
			String sendexemptToAddress = IntgSrvPropertiesReader.getProperty("WERCS_SUPPLIER_EXEMPT_TOADDRESS");
			if(sendexemptToAddress !=null && sendexemptToAddress !=""){
				sendExcelFile(file,sendexemptToAddress);
			}else{
				DatamigrationCommonUtil.printConsole("No valid mail ID to send supplier exempt mail. Check config-common.properties file");
				logger.error("No valid mail ID to send supplier exempt mail. Check config-common.properties file");
			}
		}
	}
	
	public File createXLDocument(String upc,String pipID,String toner_wholesaler){
		FileInputStream fsIP;
		File outputFile = null;
		try {
			
			if(TONER_STR.equalsIgnoreCase(toner_wholesaler)){
				logger.info("Toner xl template selected");
				fsIP = new FileInputStream(new File(templateXLFileToner));
			}else{
				logger.info("Wholesaler xl template selected");
				fsIP = new FileInputStream(new File(templateXLFileWholeSaler));
			}
		// Read the spreadsheet that needs to be updated
		XSSFWorkbook wb = new XSSFWorkbook(fsIP);
		// Access the workbook
		XSSFSheet worksheet = wb.getSheetAt(SHEETNO);
		// Access the worksheet, so that we can update / modify it.
		worksheet.getRow(PIPID_ROW).getCell(PIPID_CELL).setCellValue(pipID);;
		// Access the second cell in second row to update the value
		worksheet.getRow(UPC_ROWNO).getCell(UPC_CELLNO).setCellValue(upc);
		// Get current cell value value and overwrite the value
		fsIP.close(); // Close the InputStream
		outputFile = new File(outputFilepath+EXEMPT_PREFIX+new Date().getTime()+xlextension);
		FileOutputStream output_file_stream = new FileOutputStream(outputFile);
		// Open FileOutputStream to write updates
		wb.write(output_file_stream); // write changes
		output_file_stream.close(); // close the stream
		
		logger.info("excel file written : "+outputFile.getName());
		
		} catch (FileNotFoundException e) {
			logger.error("Error while creating Excel file"+e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
		} catch (IOException e) {
			logger.error("Error while creating Excel file"+e.getMessage());
			e.printStackTrace();
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
		}
		return outputFile;
	} 
	
	public void sendExcelFile(File file,String supplierMail){
		
		logger.info("Sending Excel file to "+supplierMail);
		
		EmailUtil emailUtil = new EmailUtil();
		emailUtil.sendEmail(sendExemptMailSubject, sendExemptMailBody, supplierMail, file);
		
		logger.info("Excel file sent.");
	}
	
	public static void main(String[] args){
//		new SendExemptMailSender().createXLDocument("asdf", "65456");
	}
}
