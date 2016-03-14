
package com.staples.pim.delegate.assetImport.writer;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.assetImport.model.Asset;
import com.staples.pim.delegate.assetImport.runner.RunSchedulerAssetImport;

public class AssetExcelWriter {

	protected IntgSrvLogger	ehfLogger	= IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	protected IntgSrvLogger	traceLogger	= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	protected String		clazzname	= this.getClass().getName();

	public void writeAssetListTOExcel(List<Asset> assetList, String outputFileName) {

		org.apache.poi.ss.usermodel.Workbook workbook = new XSSFWorkbook();
		Sheet imagesSheet = workbook.createSheet("AssetList");
		int rowIndex = 0;
		for (Asset asset : assetList) {
			Row row = imagesSheet.createRow(rowIndex++);
			int cellIndex = 0;
			row.createCell(cellIndex++).setCellValue(asset.getSkuNo());
			row.createCell(cellIndex++).setCellValue(asset.getAssetId());
			row.createCell(cellIndex++).setCellValue(asset.getAssetSequence());
			row.createCell(cellIndex++).setCellValue(asset.getAssetURL());
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(outputFileName);
			workbook.write(outputStream);
			outputStream.close();
			IntgSrvUtils.printConsole(outputFileName + " is successfully written");

		} catch (Exception exception) {
			traceLogger.error(clazzname, "close()", exception);
			ehfLogger.error(clazzname, "close()", exception);
			RunSchedulerAssetImport.itemTraceLogger.error(exception.getMessage());
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerAssetImport.PUBLISH_ID);
		}
	}
}
