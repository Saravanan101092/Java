
package com.staples.pim.delegate.assetImport.reader;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.assetImport.model.Asset;
import com.staples.pim.delegate.assetImport.runner.RunSchedulerAssetImport;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;

public class AssetExcelReader {

	protected IntgSrvLogger		ehfLogger		= IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	protected IntgSrvLogger		traceLogger		= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	protected String			clazzname		= this.getClass().getName();
	public static IntgSrvLogger	itemTraceLogger	= IntgSrvLogger.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_ASSET_IMPORT);

	public List<Asset> getAssetListFromExcel(String filePath) {

		List<Asset> assetList = new ArrayList<Asset>();
		FileInputStream inputStream = null;
		DataFormatter formatter = new DataFormatter();
		try {
			inputStream = new FileInputStream(filePath);

			org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(inputStream);
			int numberOfSheets = workbook.getNumberOfSheets();

			for (int i = 0; i < numberOfSheets; i++) {
				org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(i);
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()) {
					Asset asset = new Asset();
					Row row = (Row) rowIterator.next();

					if (row.getRowNum() == 0) {
						continue;
					} else {
						IntgSrvUtils.printConsole(row.getRowNum() + "");
						Iterator<Cell> cellIterator = row.cellIterator();

						while (cellIterator.hasNext()) {
							Cell cell = (Cell) cellIterator.next();

							if (cell.getColumnIndex() == 0) {
								asset.setSkuNo(formatter.formatCellValue(cell));
							} else if (cell.getColumnIndex() == 1) {
								asset.setAssetId(formatter.formatCellValue(cell));
							} else if (cell.getColumnIndex() == 2) {
								asset.setAssetSequence(formatter.formatCellValue(cell));
							} else if (cell.getColumnIndex() == 3) {
								asset.setAssetURL(formatter.formatCellValue(cell));
							}

						}
						if (asset.isValidAsset()) {
							String url = asset.getAssetURL();
							url = validateURL(url);
							if ("" != url) {
								asset.setAssetURL(url);
								assetList.add(asset);
							}
						}
					}
				}
			}
			inputStream.close();

		}

		catch (Exception exception) {

			traceLogger.error(clazzname, "close()", exception);
			ehfLogger.error(clazzname, "close()", exception);
			RunSchedulerAssetImport.itemTraceLogger.error(exception.getMessage());
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerAssetImport.PUBLISH_ID);
		}
		return assetList;
	}

	public static String validateURL(String url) {

		int index = 0;
		String imageUrl = "_sc7";
		String validURL = "";
		String dollar = "$", jpg = ".jpg", std = "$std$", gif = ".gif", png = ".png", jpeg = ".jpeg";

		if (url.endsWith(dollar)) {
			if (url.contains(std)) {
				validURL = url;
			}
		} else if (url.endsWith(jpg)) {
			index = url.lastIndexOf(jpg);
			url = url.substring(0, index);
			validURL = url + imageUrl;
		} else if (url.endsWith(gif)) {
			index = url.lastIndexOf(gif);
			url = url.substring(0, index);
			validURL = url + imageUrl;
		} else if (url.endsWith(jpeg)) {
			index = url.lastIndexOf(jpeg);
			url = url.substring(0, index);
			validURL = url + imageUrl;
		} else if (url.endsWith(png)) {
			index = url.lastIndexOf(png);
			url = url.substring(0, index);
			validURL = url + imageUrl;
		}
		return validURL;
	}
}
