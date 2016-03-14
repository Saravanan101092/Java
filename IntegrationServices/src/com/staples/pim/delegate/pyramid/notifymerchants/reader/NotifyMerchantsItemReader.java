package com.staples.pim.delegate.pyramid.notifymerchants.reader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.item.ItemReader;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.pyramid.notifymerchants.model.PyramidItemBean;
import com.staples.pim.delegate.pyramid.notifymerchants.runner.RunSchedulerNotifyMerchants;

public class NotifyMerchantsItemReader  {

	protected IntgSrvLogger ehfLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	protected IntgSrvLogger traceLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	protected String clazzname = this.getClass().getName();
	public static IntgSrvLogger itemTraceLogger = IntgSrvLogger
			.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER_NOTIFY_MERCHANTS);

	
	public List<PyramidItemBean> read(String inputDirectory) throws Exception {
		System.out.println("Inide reader..");
		List<PyramidItemBean> combinedItemList = new ArrayList<PyramidItemBean>();
		File currentFile = null;
		List<PyramidItemBean> itemList = null;
	//	String inputDirectory = "C:/opt/stibo/integration/hotfolder/PyrNotifyMerchantsIncoming/File_Temp/";
		File dir = new File(inputDirectory);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			System.out.println("file list is null!");
			traceLogger.info(clazzname, "run", "No input file found,...reader");
		} else {
			System.out.println("Reader::::Number of files:" + files.length + " in directory:" + dir.getName());
			int numfiles = files.length;
			traceLogger.info(clazzname, "run", "Number of input files: " + numfiles);
			for (int i = 0; i < files.length; i++) {
				itemList = new ArrayList<PyramidItemBean>();
				if (files[i].isFile()) {

					currentFile = files[i];
					itemList = readItemsFromExcel(currentFile.getAbsolutePath());
					System.out.println("reader::: Size of List::" + i + ":::" + itemList.size());
					if (itemList.size() != 0) {
						combinedItemList.addAll(itemList);
					}

				}
			}
		}
		return combinedItemList;
	}

	public List<PyramidItemBean> readItemsFromExcel(String filePath) {
		List<PyramidItemBean> itemListfromExcel = new ArrayList<PyramidItemBean>();

		FileInputStream inputStream = null;
		PyramidItemBean pyramidItemBean = null;

		try {
			inputStream = new FileInputStream(filePath);
			int p = 1;
			org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(inputStream);

			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				pyramidItemBean = new PyramidItemBean();
				Row row = (Row) rowIterator.next();

				if (row.getRowNum() == 0) {
					continue;
				} else {
					System.out.println(row.getRowNum() + "");
					Iterator<Cell> cellIterator = row.cellIterator();

					while (cellIterator.hasNext()) {
						Cell cell = (Cell) cellIterator.next();

						if (cell.getColumnIndex() == 0) {
							System.out.println("IDDDDD" + cell.getStringCellValue());
							pyramidItemBean.setItemID(cell.getStringCellValue());
						} else if (cell.getColumnIndex() == 1) {
							pyramidItemBean.setBrandName(cell.getStringCellValue());
						} else if (cell.getColumnIndex() == 2) {
							pyramidItemBean.setManufacturerPartNum(cell.getStringCellValue());
						} else if (cell.getColumnIndex() == 3) {
							pyramidItemBean.setFullDescription(cell.getStringCellValue());
						} else if (cell.getColumnIndex() == 4) {
							pyramidItemBean.setClassName(cell.getStringCellValue());
						} else if (cell.getColumnIndex() == 5) {
							pyramidItemBean.setStaplesDotComPM(cell.getStringCellValue());
						}

					}

				}
				if (pyramidItemBean != null) {
					p++;
					System.out.println("Adding to the list" + p);
					itemListfromExcel.add(pyramidItemBean);
				}
			}

			inputStream.close();

		}

		catch (Exception exception) {

			traceLogger.error(clazzname, "close()", exception);
			ehfLogger.error(clazzname, "close()", exception);
			itemTraceLogger.error(exception.getMessage());
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerNotifyMerchants.PUBLISH_ID);
		}
		return itemListfromExcel;
	}

}
