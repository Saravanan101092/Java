package com.staples.pim.delegate.pyramid.notifymerchants.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.EmailUtil;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.assetImport.model.Asset;
import com.staples.pim.delegate.assetImport.runner.RunSchedulerAssetImport;
import com.staples.pim.delegate.pyramid.itemonbrdtmupdate.model.ItemOnbrdTmFeedBean;
import com.staples.pim.delegate.pyramid.notifymerchants.model.PyramidItemBean;
import com.staples.pim.delegate.pyramid.notifymerchants.runner.RunSchedulerNotifyMerchants;

public class NotifyMerchantsItemWriter
		 {
	protected IntgSrvLogger	ehfLogger	= IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	protected IntgSrvLogger	traceLogger	= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	protected String		clazzname	= this.getClass().getName();
	public static final String	 outputDirectory= IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
			.getProperty(IntgSrvAppConstants.NOTIFY_MERCHANTS_OUTPUT_FOLDER));
	public static final String	 emailSubject= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.NOTIFY_MERCHANTS_EMAIL_SUBJECT);
	public static final String	 emailMessage= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.NOTIFY_MERCHANTS_EMAIL_MESSAGE);
	public static final String	 toAddress= IntgSrvPropertiesReader.getProperty(IntgSrvAppConstants.NOTIFY_MERCHANTS_TO_ADDRESS);
	public void writeAndEmail(Map<String, List<PyramidItemBean>> pyramidItemListbyMerchant) throws Exception {
		// TODO Auto-generated method stub
		try {
			System.out.println("inside writer");

			for (Entry<String, List<PyramidItemBean>> entry : pyramidItemListbyMerchant.entrySet()) {
				String pmName = entry.getKey();
				List<PyramidItemBean> itemListforMerchant = new ArrayList<PyramidItemBean>();
				itemListforMerchant=entry.getValue();
				//String toAddress= getEmailList(pmName);
				SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("yyyyMMdd_HHmmsss");
				String dateJob = dateformatyyyyMMdd.format(new Date());
				String epochTime = String.valueOf(System.currentTimeMillis());
				String outputFileName = outputDirectory + "AS400_Notifiaction"+ "_" + dateJob + "_" + epochTime + ".xlsx";
				
				System.out.println(entry.getKey() + "/" + entry.getValue());
				
				EmailUtil emailUtil = new EmailUtil();
				writeItemsToExcel(itemListforMerchant, outputFileName);
				File attachment=new File(outputFileName);
				/*String emailSubject="Vendor Submission Notification";
				String emailMessage="This email is to notify you that new items have been submitted to Pyramid by a vendor (see attached).  These items fall under your category, based on the AS400 Taxonomy.  If you have questions on the Classification and/or assortment, please contact the submitting Merchant."+"\n"+"\n"+"Thank you for your review.";*/
				

				emailUtil.sendEmail(emailSubject, emailMessage, toAddress, attachment);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getEmailList(String pmName) {
		String emailList ="Prabha.Nesaiyan@staples.com";
		return emailList;

	}

	public void writeItemsToExcel(List<PyramidItemBean> PyramidItemList, String outputFileName) {
		
		org.apache.poi.ss.usermodel.Workbook workbook = new XSSFWorkbook();
		Sheet imagesSheet = workbook.createSheet("ItemList");
		
		Row row1 = imagesSheet.createRow(0);
		
		row1.createCell(0).setCellValue("Item-ID");
		row1.createCell(1).setCellValue("Brand Name");
		row1.createCell(2).setCellValue("Mfg Part #");
		row1.createCell(3).setCellValue("Staples Internal Description");
		row1.createCell(4).setCellValue("AS400 Class");
		row1.createCell(5).setCellValue("Submitting Merchant");
		int rowIndex = 1;
		for (PyramidItemBean pyramidItemBean : PyramidItemList) {
			int cellIndex = 0;
			System.out.println(rowIndex);
			Row row = imagesSheet.createRow(rowIndex++);
			System.out.println(rowIndex+""+pyramidItemBean.getItemID());
			row.createCell(cellIndex++).setCellValue(pyramidItemBean.getItemID());
			row.createCell(cellIndex++).setCellValue(pyramidItemBean.getBrandName());
			row.createCell(cellIndex++).setCellValue(pyramidItemBean.getManufacturerPartNum());
			row.createCell(cellIndex++).setCellValue(pyramidItemBean.getFullDescription());
			row.createCell(cellIndex++).setCellValue(pyramidItemBean.getClassName());
			row.createCell(cellIndex++).setCellValue(pyramidItemBean.getStaplesDotComPM());
	
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(outputFileName);
			workbook.write(outputStream);
			outputStream.close();
			System.out.println(outputFileName + " is successfully written");

		} catch (Exception exception) {
			traceLogger.error(clazzname, "close()", exception);
			ehfLogger.error(clazzname, "close()", exception);
			RunSchedulerAssetImport.itemTraceLogger.error(exception.getMessage());
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerNotifyMerchants.PUBLISH_ID);
		}
	}

	
}
