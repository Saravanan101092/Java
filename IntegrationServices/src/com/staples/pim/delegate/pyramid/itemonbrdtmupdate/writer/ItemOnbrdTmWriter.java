
package com.staples.pim.delegate.pyramid.itemonbrdtmupdate.writer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.pyramid.itemonbrdtmupdate.model.ItemOnbrdTmFeedBean;
import com.staples.pim.delegate.pyramid.itemonbrdtmupdate.model.ItemOnbrdTmTaxonomyLevelRelation;
import com.staples.pim.delegate.pyramid.itemonbrdtmupdate.model.ItemOnbrdTmTaxonomyUnit;
import com.staples.pim.delegate.pyramid.itemonbrdtmupdate.runner.RunSchedulerItemOnbrdTm;

public class ItemOnbrdTmWriter implements ItemWriter<ItemOnbrdTmFeedBean>, StepExecutionListener {

	private ItemOnbrdTmTaxonomyLevelRelation	relationBean	= new ItemOnbrdTmTaxonomyLevelRelation();

	protected IntgSrvLogger						ehfLogger		= IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
	protected IntgSrvLogger						traceLogger		= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
	protected String							clazzname		= this.getClass().getName();

	@Override
	public void write(List<? extends ItemOnbrdTmFeedBean> itemOnbrdTmFeedBean) throws Exception {

		for (ItemOnbrdTmFeedBean feedBean : itemOnbrdTmFeedBean) {

			/*
			 * Web Super Category process
			 */
			ItemOnbrdTmTaxonomyUnit superCategory = new ItemOnbrdTmTaxonomyUnit("", feedBean.getSuperCategoryName(),
					feedBean.getGeneratedSuperCategoryId());

			relationBean.getWebSuperCategory().add(superCategory);

			/*
			 * Web Category process
			 */
			ItemOnbrdTmTaxonomyUnit category = new ItemOnbrdTmTaxonomyUnit(feedBean.getSuperCategoryName(), feedBean.getCategoryName(),
					feedBean.getGeneratedCategoryId());

			relationBean.getWebCategory().add(category);

			/*
			 * Web Dept process
			 */
			ItemOnbrdTmTaxonomyUnit dept = new ItemOnbrdTmTaxonomyUnit(feedBean.getCategoryName(), feedBean.getDepartmentName(),
					feedBean.getGeneratedDepartmentId());

			relationBean.getWebDepartment().add(dept);

			/*
			 * Web class process
			 */
			ItemOnbrdTmTaxonomyUnit webClass = new ItemOnbrdTmTaxonomyUnit(feedBean.getDepartmentName(), feedBean.getClassName(),
					feedBean.getGeneratedClassId());

			relationBean.getWebClass().add(webClass);

		}
	}

	private void refreshTaxonomyDataToIOBTMExcel(ItemOnbrdTmTaxonomyLevelRelation relationBean, String pyrIOBFeedProcessDir,
			String pyrIOBFeedProcessExcelName) {

		try {

			int currentCell = 0;
			int rowIndex = 1;
			Workbook workbook = new XSSFWorkbook(new FileInputStream(pyrIOBFeedProcessDir + pyrIOBFeedProcessExcelName));

			Sheet sheet = workbook.getSheet("Category");

			if (sheet != null) {
				int sheetNumber = workbook.getSheetIndex("Category");
				workbook.removeSheetAt(sheetNumber);
			}
			sheet = workbook.createSheet("Category");
			workbook.setSheetHidden(workbook.getSheetIndex("Category"), true);

			for (ItemOnbrdTmTaxonomyUnit superCategory : relationBean.getWebSuperCategory()) {
				Row row = sheet.getRow((short) rowIndex);
				if (row == null) {
					row = sheet.createRow(rowIndex);
				}
				row.createCell(currentCell).setCellValue(superCategory.getName());
				row.createCell(currentCell + 1).setCellValue(superCategory.getId());
				rowIndex++;

			}
			rowIndex = 1;
			currentCell += 2;
			for (ItemOnbrdTmTaxonomyUnit webCategory : relationBean.getWebCategory()) {
				Row row = sheet.getRow((short) rowIndex);
				if (row == null) {
					row = sheet.createRow(rowIndex);
				}
				row.createCell(currentCell).setCellValue(webCategory.getParentName());
				row.createCell(currentCell + 1).setCellValue(webCategory.getName());
				row.createCell(currentCell + 2).setCellValue(webCategory.getId());
				rowIndex++;
			}
			currentCell += 3;
			rowIndex = 1;
			for (ItemOnbrdTmTaxonomyUnit webDepartment : relationBean.getWebDepartment()) {
				Row row = sheet.getRow((short) rowIndex);
				if (row == null) {
					row = sheet.createRow(rowIndex);
				}
				row.createCell(currentCell).setCellValue(webDepartment.getParentName());
				row.createCell(currentCell + 1).setCellValue(webDepartment.getName());
				row.createCell(currentCell + 2).setCellValue(webDepartment.getId());
				rowIndex++;
			}
			currentCell += 3;
			rowIndex = 1;
			for (ItemOnbrdTmTaxonomyUnit webClass : relationBean.getWebClass()) {
				Row row = sheet.getRow((short) rowIndex);
				if (row == null) {
					row = sheet.createRow(rowIndex);
				}
				row.createCell(currentCell).setCellValue(webClass.getParentName());
				row.createCell(currentCell + 1).setCellValue(webClass.getName());
				row.createCell(currentCell + 2).setCellValue(webClass.getId());
				rowIndex++;

			}

			/*
			 * Writing Excel file
			 */
			FileOutputStream fileOutputStream = new FileOutputStream(pyrIOBFeedProcessDir + pyrIOBFeedProcessExcelName);
			workbook.write(fileOutputStream);
			fileOutputStream.close();
		} catch (Exception exception) {
			String infoLogString = "Exception" + exception;
			ehfLogger.error(infoLogString);
			traceLogger.error(infoLogString);
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerItemOnbrdTm.PUBLISH_ID);
		}

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		JobParameters JobParameters = stepExecution.getJobParameters();
		String pyrIOBFeedProcessDir = JobParameters.getString("pyrIOBFeedProcessDir");
		String pyrIOBFeedProcessExcelName = JobParameters.getString("pyrIOBFeedProcessExcelName");

		refreshTaxonomyDataToIOBTMExcel(relationBean, pyrIOBFeedProcessDir, pyrIOBFeedProcessExcelName);
		refreshVendorDataFromSBConfig(pyrIOBFeedProcessDir, pyrIOBFeedProcessExcelName);

		return org.springframework.batch.core.ExitStatus.COMPLETED;

	}

	private void refreshVendorDataFromSBConfig(String pyrIOBFeedProcessDir, String pyrIOBFeedProcessExcelName) {

		Map<String, List<String>> lovMap = DatamigrationCommonUtil.getLOVProperties();
		try {

			int currentCell = 0;
			int rowIndex = 1;
			Workbook workbook = new XSSFWorkbook(new FileInputStream(pyrIOBFeedProcessDir + pyrIOBFeedProcessExcelName));

			Sheet sheet = workbook.getSheet("PYMVendorDetails");

			if (sheet != null) {
				int sheetNumber = workbook.getSheetIndex("PYMVendorDetails");
				workbook.removeSheetAt(sheetNumber);
			}
			sheet = workbook.createSheet("PYMVendorDetails");
			workbook.setSheetHidden(workbook.getSheetIndex("PYMVendorDetails"), true);

			List<String> vendorDetails = lovMap.get("PYMVendorDetails");

			for (String vendor : vendorDetails) {
				if (vendor.length() > 1) {
					currentCell = 0;
					String vendorInfo[] = vendor.split(":");
					Row row = sheet.getRow((short) rowIndex);
					if (row == null) {
						row = sheet.createRow(rowIndex);
					}
					// Setting Name
					row.createCell(currentCell).setCellValue(vendorInfo[0]);
					// Setting Name, Id and Email Id
					row.createCell(currentCell + 2).setCellValue(vendorInfo[0]);
					row.createCell(currentCell + 3).setCellValue(vendorInfo[1]);
					row.createCell(currentCell + 4).setCellValue(vendorInfo[2]);
					rowIndex++;
				}
			}
			/*
			 * Writing Excel file
			 */
			FileOutputStream fileOutputStream = new FileOutputStream(pyrIOBFeedProcessDir + pyrIOBFeedProcessExcelName);
			workbook.write(fileOutputStream);
			fileOutputStream.close();
		} catch (Exception exception) {
			String infoLogString = "Exception" + exception;
			ehfLogger.error(infoLogString);
			traceLogger.error(infoLogString);
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerItemOnbrdTm.PUBLISH_ID);
		}

	}

	@Override
	public void beforeStep(StepExecution stepExecution) {

	}
}
