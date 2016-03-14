
package com.staples.pim.delegate.copyandprint.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.COMPONENT_PROPERTIES;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.COMPONENT_PROPERTIES_HEADERS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONFIGURATIONS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONFIGURATIONS_HEADERS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONFIG_HIERARCHY;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONFIG_HIERARCHY_HEADERS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EHF_LOGGER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_CNP;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.IMAGES;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.IMAGES_HEADERS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.INVALID_ASOCIATIONS_HEADERS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.INVALID_ASSOCIATIONS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.KIT_CONTENTS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.KIT_CONTENTS_HEADERS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.KIT_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.MESSAGING_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.MESSAGING_TYPE_HEADERS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SFTP_HOSTNAME_VICTOR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.STYLE_REFERENCES;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.STYLE_REFERENCES_HEADERS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.TEMPLATES;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.TEMPLATES_HEADERS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.TEMPLATE_COMPONENTS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.TEMPLATE_COMPONENTS_HEADERS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WEB_HIERARCHY;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WEB_HIERARCHY_HEADERS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import com.staples.pim.base.common.bean.FTPConnectionBean;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.persistence.connection.SFTPManager;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.copyandprint.beans.ComponentPropertiesVO;
import com.staples.pim.delegate.copyandprint.beans.ConfigHierarchyVO;
import com.staples.pim.delegate.copyandprint.beans.ConfigurationsVO;
import com.staples.pim.delegate.copyandprint.beans.ImagesVO;
import com.staples.pim.delegate.copyandprint.beans.InvalidAssociationsVO;
import com.staples.pim.delegate.copyandprint.beans.KitContentsVO;
import com.staples.pim.delegate.copyandprint.beans.MessagingTypeVO;
import com.staples.pim.delegate.copyandprint.beans.SheetListVO;
import com.staples.pim.delegate.copyandprint.beans.StyleReferencesVO;
import com.staples.pim.delegate.copyandprint.beans.TemplateComponentsVO;
import com.staples.pim.delegate.copyandprint.beans.TemplatesVO;
import com.staples.pim.delegate.copyandprint.beans.WebHierarchyVO;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

/**
 * @author dhapa001
 * 
 */
public class StepOutboundWriter {

	static IntgSrvLogger		logger		= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER_CNP);
	public static IntgSrvLogger	ehfLogger	= IntgSrvLogger.getInstance(EHF_LOGGER);

	public void createExcel(SheetListVO sheetListVO, String workBookName, String[] sheetNames) throws Exception {

		DatamigrationCommonUtil.printConsole("Output file path:" + workBookName);
		// DatamigrationCommonUtil.printConsole("SheetListVO: " +
		// DatamigrationCommonUtil.voToString(sheetListVO));
		logger.info("Output file path:" + workBookName);
		// logger.info("SheetListVO: " +
		// DatamigrationCommonUtil.voToString(sheetListVO));

		try {
			FileOutputStream fileOut = new FileOutputStream(workBookName);

			HSSFWorkbook workbook = new HSSFWorkbook();

			createSheet(workbook, sheetListVO, sheetNames);

			workbook.write(fileOut);

			fileOut.flush();
			fileOut.close();
			// File transfer to Victor SFTP location

			if (!IntgSrvUtils.isEmptyString(IntgSrvPropertiesReader.getProperty(SFTP_HOSTNAME_VICTOR))) {

				File file = new File(IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader
						.getProperty(IntgSrvAppConstants.CNP_OUTBOUND_EXCEL_OUTPUT_DIRECTORY)));

				if (!file.exists()) {
					file.mkdirs();
				}
				File outputFile = new File(workBookName);
				FTPConnectionBean connectionsBean = new FTPConnectionBean();
				connectionsBean.setDestinationUrl(IntgSrvPropertiesReader
						.getProperty(DatamigrationAppConstants.CNP_SFTP_TARGETDIRECTORY_VICTOR));
				connectionsBean.setHostName(IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.SFTP_HOSTNAME_VICTOR));
				connectionsBean.setOriginatedURL(workBookName);
				connectionsBean.setPassword(IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.CNP_SFTP_PASSWORD_VICTOR));
				connectionsBean.setUserId(IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.CNP_SFTP_USERNAME_VICTOR));
				DatamigrationCommonUtil.printConsole(DatamigrationCommonUtil.voToString(connectionsBean));
				SFTPManager sftpManager = new SFTPManager(connectionsBean);
				sftpManager.setPublishId(DatamigrationAppConstants.PUBLISH_ID_NEW);

				boolean sftpresult = sftpManager.uploadFile(outputFile.getPath(),
						(IntgSrvPropertiesReader.getProperty(DatamigrationAppConstants.CNP_SFTP_PRIVATEKEY_VICTOR)));
				logger.info("sftp result: " + sftpresult);
				System.out.println(sftpresult);
				// DatamigrationCommonUtil.sendFileToSFTP(workBookName,
				// VICTOR_OUTPUT_CHANNEL);
			}
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
			logger.error(fe);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}

	}

	/**
	 * To create the respective sheets in the workbook
	 * 
	 * @param workbook
	 * @param listVO
	 * @param sheetNames
	 */
	private void createSheet(HSSFWorkbook workbook, SheetListVO listVO, String[] sheetNames) {

		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		style.setFillForegroundColor(HSSFColor.YELLOW.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// font.setColor(HSSFColor.WHITE.index);
		style.setFont(font);

		for (String sheetName : sheetNames) {

			// HSSFSheet sheet = workbook.createSheet(sheetName);
			// create header row
			DatamigrationCommonUtil.printConsole("SheetName: " + sheetName);

			if (WEB_HIERARCHY.equals(sheetName)) {
				HSSFSheet sheet = workbook.createSheet(sheetName);
				createHeaders(WEB_HIERARCHY_HEADERS, sheet, style);
				createWebHierarchyRows(listVO.getWebHierarchyList(), sheet);
			} else if (CONFIG_HIERARCHY.equals(sheetName)) {

				HSSFSheet sheet = workbook.createSheet(sheetName);
				createHeaders(CONFIG_HIERARCHY_HEADERS, sheet, style);
				if (listVO.getConfigHierarchyList() != null) {

					createConfigHierarchyRows(listVO.getConfigHierarchyList(), sheet);
				}

			} else if (TEMPLATES.equals(sheetName)) {
				HSSFSheet sheet = workbook.createSheet(sheetName);
				createHeaders(TEMPLATES_HEADERS, sheet, style);
				if (listVO.getTemplatesList() != null) {
					createTemplateHierarchyRows(listVO.getTemplatesList(), sheet);
				}
			} else if (CONFIGURATIONS.equals(sheetName)) {
				if (isPricingStrategy(listVO)) {
					HSSFSheet sheet = workbook.createSheet(sheetName);
					createHeaders(CONFIGURATIONS_HEADERS, sheet, style);
					// createConfigRows(listVO.getConfigList(), sheet);
					createConfigRows(listVO, sheet);
				}
			} else if (IMAGES.equals(sheetName)) {
				HSSFSheet sheet = workbook.createSheet(sheetName);
				createHeaders(IMAGES_HEADERS, sheet, style);
				createImagesRows(listVO.getImageList(), sheet);
			}

			else if (TEMPLATE_COMPONENTS.equals(sheetName)) {
				HSSFSheet sheet = workbook.createSheet(sheetName);
				createHeaders(TEMPLATE_COMPONENTS_HEADERS, sheet, style);
				if (listVO.getTemplatesList() != null) {
					createTemplateComponentRows(listVO.getTemplateComponentsList(), sheet);
				}
			} else if (INVALID_ASSOCIATIONS.equals(sheetName)) {
				HSSFSheet sheet = workbook.createSheet(sheetName);
				createHeaders(INVALID_ASOCIATIONS_HEADERS, sheet, style);
				createInvalidAssociationsRows(listVO.getInvalidAssocationsList(), sheet);
			} else if (STYLE_REFERENCES.equals(sheetName)) {
				HSSFSheet sheet = workbook.createSheet(sheetName);
				createHeaders(STYLE_REFERENCES_HEADERS, sheet, style);
				createStyleReferencesRows(listVO.getStyleReferenceVOList(), sheet);
			} else if (KIT_CONTENTS.equals(sheetName)) {
				if (isKits(listVO)) {
					HSSFSheet sheet = workbook.createSheet(sheetName);
					createHeaders(KIT_CONTENTS_HEADERS, sheet, style);
					createKitContentsRows(listVO.getKitContentsList(), sheet);
				}
			} else if (COMPONENT_PROPERTIES.equals(sheetName)) {
				if (isMaterial(listVO)) {
					HSSFSheet sheet = workbook.createSheet(sheetName);
					createHeaders(COMPONENT_PROPERTIES_HEADERS, sheet, style);
					createComponentPropertiesRows(listVO.getComponentPropertiesList(), sheet);
				}
			} else if (MESSAGING_TYPE.equals(sheetName)) {
				if (isMaterial(listVO)) {
					HSSFSheet sheet = workbook.createSheet(sheetName);
					createHeaders(MESSAGING_TYPE_HEADERS, sheet, style);
					createMessagingTypeRows(listVO.getMessagingTypeList(), sheet);
				}
			}
		}
	}

	/**
	 * To create headers for a tab in a sheet
	 * 
	 * @param hdrList
	 * @param webHierarchySheet
	 * @param style
	 */
	private void createHeaders(String[] hdrList, HSSFSheet webHierarchySheet, CellStyle style) {

		HSSFRow header = webHierarchySheet.createRow(0);
		for (int i = 0; i < hdrList.length; i++) {
			header.createCell(i).setCellValue(hdrList[i]);
			header.getCell(i).setCellStyle(style);
		}
	}

	/**
	 * For "Web Hierarchy" tab in Web Hierarchy sheet
	 * 
	 * @param weHierarchyList
	 * @param webHierarchySheet
	 */
	private void createWebHierarchyRows(List<WebHierarchyVO> weHierarchyList, HSSFSheet webHierarchySheet) {

		int rowCount = 1;

		for (WebHierarchyVO webHierarchyVO : weHierarchyList) {
			HSSFRow aRow = webHierarchySheet.createRow(rowCount++);

			aRow.createCell(0).setCellValue(webHierarchyVO.getDetailId());
			aRow.createCell(1).setCellValue(webHierarchyVO.getDetailName());
			aRow.createCell(2).setCellValue(webHierarchyVO.getDetailDesc());
			aRow.createCell(3).setCellValue(webHierarchyVO.getLongDesc());
			aRow.createCell(4).setCellValue(webHierarchyVO.geDispSeq());
			aRow.createCell(5).setCellValue(webHierarchyVO.getDetailDispInd());
			if (webHierarchyVO.getLevelId() != null) {
				aRow.createCell(6).setCellValue(webHierarchyVO.getLevelId());
			}
			aRow.createCell(7).setCellValue(webHierarchyVO.getLevelName());
			aRow.createCell(8).setCellValue(webHierarchyVO.getDetailParentId());
			// Phase 2
			aRow.createCell(9).setCellValue(webHierarchyVO.getHierarchyId());
			/*
			 * for(int colNum = 0; colNum<aRow.getLastCellNum();colNum++) {
			 * webHierarchySheet.autoSizeColumn(colNum); }
			 */
		}
	}

	/**
	 * For "Config Hierarchy" tab in Config Hierarchy sheet
	 * 
	 * @param configHierarchyList
	 * @param sheet
	 */
	private void createConfigHierarchyRows(List<ConfigHierarchyVO> configHierarchyList, HSSFSheet sheet) {

		int rowCount = 1;

		for (ConfigHierarchyVO configHierarchyVO : configHierarchyList) {

			// System.out.println(DatamigrationCommonUtil.getClassAndMethodName()+DatamigrationCommonUtil.voToString(configHierarchyVO));
			HSSFRow aRow = sheet.createRow(rowCount++);

			aRow.createCell(0).setCellValue(configHierarchyVO.getDetailId());
			aRow.createCell(1).setCellValue(configHierarchyVO.getDetailName());
			aRow.createCell(2).setCellValue(configHierarchyVO.getDetailDesc());
			setNumericCellValue(aRow.createCell(3), configHierarchyVO.getDispSeq());
			aRow.createCell(4).setCellValue(DatamigrationCommonUtil.converYESNOIntoChar(configHierarchyVO.getActiveInd()));
			aRow.createCell(5).setCellValue(DatamigrationCommonUtil.converYESNOIntoChar(configHierarchyVO.getDispInd()));
			aRow.createCell(6).setCellValue(DatamigrationCommonUtil.converYESNOIntoChar(configHierarchyVO.getHasCostInd())); // Drives
			// Cost
			// Ind
			aRow.createCell(7).setCellValue(DatamigrationCommonUtil.converYESNOIntoChar(configHierarchyVO.getCustomQtyInd()));
			// PCMP-2618 Post Phase2 CnP Sprint Changes
			aRow.createCell(8).setCellValue(configHierarchyVO.getCustomInputFormat());
			aRow.createCell(9).setCellValue(configHierarchyVO.getCustomInputPrompt());
			aRow.createCell(10).setCellValue(configHierarchyVO.getDcsOnlyIndicator());
			aRow.createCell(11).setCellValue(configHierarchyVO.getThirdPartyOnlyIndicator());
			aRow.createCell(12).setCellValue(configHierarchyVO.getBaseUOM());
			aRow.createCell(13).setCellValue(configHierarchyVO.getMeasurementScope());
			aRow.createCell(14).setCellValue(configHierarchyVO.getQuantityMultiplier());
			aRow.createCell(15).setCellValue(configHierarchyVO.getQuantityDivider());
			if (configHierarchyVO.getLevelId() != null) {
				aRow.createCell(16).setCellValue(configHierarchyVO.getLevelId());
			}
			// aRow.createCell(9).setCellValue(configHierarchyVO.getLevelName());
			aRow.createCell(17).setCellValue(configHierarchyVO.getCnpLifeCycle());
			aRow.createCell(18).setCellValue(configHierarchyVO.getDetailParentId());
			// aRow.createCell(11).setCellValue(configHierarchyVO.getIsDCS());
			/*
			 * for(int colNum = 0; colNum<aRow.getLastCellNum();colNum++) {
			 * sheet.autoSizeColumn(colNum);
			 * 
			 * }
			 */

		}
	}

	/**
	 * For "Template Configurations" tab in Product Template sheet
	 * 
	 * @param configList
	 * @param sheet
	 * @param workbook
	 * @param style
	 */
	// private void createConfigRows(List<ConfigurationsVO> configList,
	// HSSFSheet sheet) {
	//
	// int rowCount = 1;
	// if (configList != null) {
	// for (ConfigurationsVO configVO : configList) {
	// HSSFRow aRow = sheet.createRow(rowCount++);
	// aRow.createCell(0).setCellValue(configVO.getTemplateId());
	// aRow.createCell(1).setCellValue(configVO.getTemplateConfigId());
	// aRow.createCell(2).setCellValue(configVO.getConfigAttrId());
	// aRow.createCell(3).setCellValue(configVO.getConfigAttrValId());
	// aRow.createCell(4).setCellValue(DatamigrationCommonUtil.converYESNOIntoChar(configVO.getDefAttrValInd()));
	// aRow.createCell(5).setCellValue(DatamigrationCommonUtil.converYESNOIntoChar(configVO.getActiveId()));
	//
	// /*
	// * for (int colNum = 0; colNum < aRow.getLastCellNum();
	// * colNum++) { sheet.autoSizeColumn(colNum); }
	// */
	//
	// }
	// } else {
	// logger.info("Config List is empty");
	// DatamigrationCommonUtil.printConsole("Config List is empty");
	// }
	//
	// }

	private void createConfigRows(SheetListVO listVO, HSSFSheet sheet) {

		int rowCount = 1;
		int extraTab = 0;
		if (listVO.getGeneratedKeys() != null) {

			List<List<String>> generatedKeyList = listVO.getGeneratedKeys();
			Map<String, ConfigurationsVO> materialsIdWithDetailsMap = new HashMap<String, ConfigurationsVO>();
			for (ConfigurationsVO configVO : listVO.getConfigList()) {
				materialsIdWithDetailsMap.put(configVO.getConfigAttrValId(), configVO);
			}
			for (int i = 0; i < generatedKeyList.size(); i++) {
				String key = "";
				List<String> currentKey = generatedKeyList.get(i);
				for (int j = 0; j < currentKey.size(); j++) {
					key += currentKey.get(j);
				}
				for (String currentId : generatedKeyList.get(i)) {

//					if (rowCount < 65535) {
						HSSFRow aRow = sheet.createRow(rowCount++);
						aRow.createCell(0).setCellValue(materialsIdWithDetailsMap.get(currentId).getTemplateId());
						aRow.createCell(1).setCellValue(materialsIdWithDetailsMap.get(currentId).getTemplateId().substring(1) + key);
						aRow.createCell(2).setCellValue(materialsIdWithDetailsMap.get(currentId).getConfigAttrId());
						aRow.createCell(3).setCellValue(materialsIdWithDetailsMap.get(currentId).getConfigAttrValId());
						aRow.createCell(4).setCellValue(
								DatamigrationCommonUtil.converYESNOIntoChar(materialsIdWithDetailsMap.get(currentId).getDefAttrValInd()));
						aRow.createCell(5).setCellValue(
								DatamigrationCommonUtil.converYESNOIntoChar(materialsIdWithDetailsMap.get(currentId).getActiveId()));

//					} else {
//
//					}
				}

			}
			logger.info("Template Configuration Rows written into excel");
		} else {
			logger.info("Template Configuration List is empty");
			DatamigrationCommonUtil.printConsole("Template Configuration List is empty");
		}

	}

	public void writeConfigRows(HSSFSheet sheet, int rowCount, String currentId, Map<String, ConfigurationsVO> materialsIdWithDetailsMap,
			String key) {

		HSSFRow aRow = sheet.createRow(rowCount++);
		aRow.createCell(0).setCellValue(materialsIdWithDetailsMap.get(currentId).getTemplateId());
		aRow.createCell(1).setCellValue(materialsIdWithDetailsMap.get(currentId).getTemplateId().substring(1) + key);
		aRow.createCell(2).setCellValue(materialsIdWithDetailsMap.get(currentId).getConfigAttrId());
		aRow.createCell(3).setCellValue(materialsIdWithDetailsMap.get(currentId).getConfigAttrValId());
		aRow.createCell(4).setCellValue(
				DatamigrationCommonUtil.converYESNOIntoChar(materialsIdWithDetailsMap.get(currentId).getDefAttrValInd()));
		aRow.createCell(5)
				.setCellValue(DatamigrationCommonUtil.converYESNOIntoChar(materialsIdWithDetailsMap.get(currentId).getActiveId()));
	}

	/**
	 * For Images tab in all sheets
	 * 
	 * @param imagesList
	 * @param webHierarchySheet
	 */
	private void createImagesRows(List<ImagesVO> imagesList, HSSFSheet webHierarchySheet) {

		int rowCount = 1;
		if (imagesList != null) {
			for (ImagesVO imageVO : imagesList) {
				HSSFRow aRow = webHierarchySheet.createRow(rowCount++);
				aRow.createCell(0).setCellValue(imageVO.getEntityId());
				aRow.createCell(1).setCellValue(imageVO.getEntityType());
				aRow.createCell(2).setCellValue(imageVO.getImageId());
				aRow.createCell(3).setCellValue(imageVO.getImageType());
				aRow.createCell(4).setCellValue(imageVO.getDefInd());

				/*
				 * for (int colNum = 0; colNum < aRow.getLastCellNum();
				 * colNum++) { webHierarchySheet.autoSizeColumn(colNum); }
				 */

			}
		} else {
			logger.info("Image List is empty");
			DatamigrationCommonUtil.printConsole("Image List is empty");
		}
	}

	/**
	 * For "Templates" tab in Product Template sheet
	 * 
	 * @param templatesList
	 * @param sheet
	 */
	private void createTemplateHierarchyRows(List<TemplatesVO> templatesList, HSSFSheet sheet) {

		int rowCount = 1;
		for (TemplatesVO templatesVO : templatesList) {
			HSSFRow aRow = sheet.createRow(rowCount++);

			aRow.createCell(0).setCellValue(templatesVO.getTemplateId());

			if (templatesVO.getTemplateSku() != null) {
				setNumericCellValue(aRow.createCell(1), templatesVO.getTemplateSku());
			}
			aRow.createCell(2).setCellValue(templatesVO.getItemType());
			aRow.createCell(3).setCellValue(templatesVO.getTemplateName());
			aRow.createCell(4).setCellValue(templatesVO.getTemplateDesc());
			// aRow.createCell(5).setCellValue(templatesVO.getStyleId());
			aRow.createCell(5).setCellValue(templatesVO.getLongDescription());
			aRow.createCell(6).setCellValue(templatesVO.getTemplateType());
			aRow.createCell(7).setCellValue(templatesVO.getPricingStategy());
			aRow.createCell(8).setCellValue(templatesVO.getIsSaleableInd());
			aRow.createCell(9).setCellValue(templatesVO.getSellUom());
			if (templatesVO.getTemplateSku() != null) {
				setNumericCellValue(aRow.createCell(10), templatesVO.getSellUomQty());
			}
			aRow.createCell(11).setCellValue(templatesVO.getBaseUom());
			aRow.createCell(12).setCellValue(templatesVO.getOrderQtyList());
			// setNumericCellValue(aRow.createCell(10),templatesVO.getOrderQtyList());

			if (templatesVO.getTemplateSku() != null) {
				setNumericCellValue(aRow.createCell(13), templatesVO.getMinOrdrQty());
			}
			if (templatesVO.getTemplateSku() != null) {
				setNumericCellValue(aRow.createCell(14), templatesVO.getMaxOrdrQty());
			}

			aRow.createCell(15).setCellValue(DatamigrationCommonUtil.converYESNOIntoChar(templatesVO.getPaymentReqd()));
			aRow.createCell(16).setCellValue(templatesVO.getArtWorkProvision());
			aRow.createCell(17).setCellValue(templatesVO.getTurnTimeRange());
			aRow.createCell(18).setCellValue(DatamigrationCommonUtil.converYESNOIntoChar(templatesVO.getQuickDelvryInd()));
			aRow.createCell(19).setCellValue(templatesVO.getProductionNotes());
			aRow.createCell(20).setCellValue(DatamigrationCommonUtil.converYESNOIntoChar(templatesVO.getActiveInd()));
			if (templatesVO.getExceptionPagesAllowedInd() != null && templatesVO.getExceptionPagesAllowedInd() != "") {
				aRow.createCell(21).setCellValue(DatamigrationCommonUtil.converYESNOIntoChar(templatesVO.getExceptionPagesAllowedInd()));
			}
			// aRow.createCell(21).setCellValue(templatesVO.getIsDCS());
			/*
			 * for (int colNum = 0; colNum < aRow.getLastCellNum(); colNum++) {
			 * sheet.autoSizeColumn(colNum); }
			 */
		}
	}

	// Phase 2
	/**
	 * For "Template Components" tab in Product Template sheet
	 * 
	 * @param templateComponentList
	 * @param sheet
	 */
	private void createTemplateComponentRows(List<TemplateComponentsVO> templateComponentList, HSSFSheet sheet) {

		int rowCount = 1;
		if (templateComponentList != null) {
			for (TemplateComponentsVO templateComponentVO : templateComponentList) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				aRow.createCell(0).setCellValue(templateComponentVO.getTemplateId());
				aRow.createCell(1).setCellValue(templateComponentVO.getConfigAttrId());
				aRow.createCell(2).setCellValue(templateComponentVO.getConfigAttrValId());
				aRow.createCell(3).setCellValue(DatamigrationCommonUtil.converYESNOIntoChar(templateComponentVO.getDefAttrValInd()));
				aRow.createCell(4).setCellValue(templateComponentVO.getSupressMsgInd());
				// PCMP-2618 Post Phase2 CnP Sprint Changes-delete Custom Qty
				// Prompt and add Active Ind
				// aRow.createCell(5).setCellValue(templateComponentVO.getCustomQtyPrompt());
				aRow.createCell(5).setCellValue(templateComponentVO.getActiveInd());
				aRow.createCell(6).setCellValue(templateComponentVO.getCustomQtyMinValue());
				aRow.createCell(7).setCellValue(templateComponentVO.getCustomQtyMaxValue());
				aRow.createCell(8).setCellValue(templateComponentVO.getPageNumber());
				aRow.createCell(9).setCellValue(templateComponentVO.getExceptionPageConfigAttrInd());

				/*
				 * for (int colNum = 0; colNum < aRow.getLastCellNum();
				 * colNum++) { sheet.autoSizeColumn(colNum); }
				 */

			}
		} else {
			logger.info("Template Component List List is empty");
			DatamigrationCommonUtil.printConsole("Template Component List List is empty");
		}

	}

	// Phase 2
	/**
	 * For "Invalid Associations" tab in Product Template sheet
	 * @param invalidAssociationsList
	 * @param sheet
	 */
	private void createInvalidAssociationsRows(List<InvalidAssociationsVO> invalidAssociationsList, HSSFSheet sheet) {

		int rowCount = 1;
		if (invalidAssociationsList != null) {
			for (InvalidAssociationsVO invalidAssociationsVO : invalidAssociationsList) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				aRow.createCell(0).setCellValue(invalidAssociationsVO.getTemplateId());
				aRow.createCell(1).setCellValue(invalidAssociationsVO.getConfigAttrValId());
				aRow.createCell(2).setCellValue(invalidAssociationsVO.getInvalidConfigAttrValId());
				aRow.createCell(3).setCellValue(invalidAssociationsVO.getExceptionPagesApplicable());
				/*
				 * for (int colNum = 0; colNum < aRow.getLastCellNum();
				 * colNum++) { sheet.autoSizeColumn(colNum); }
				 */

			}
		} else {
			logger.info("Invalid Associations List List is empty");
			DatamigrationCommonUtil.printConsole("Invalid Associations List List is empty");
		}

	}

	// Phase 2
	/**
	 * For "Style References" tab in Product Template sheet
	 * @param styleReferencesVOList
	 * @param sheet
	 */
	private void createStyleReferencesRows(List<StyleReferencesVO> styleReferencesVOList, HSSFSheet sheet) {

		int rowCount = 1;
		if (styleReferencesVOList != null) {
			for (StyleReferencesVO styleReferencesVO : styleReferencesVOList) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				aRow.createCell(0).setCellValue(styleReferencesVO.getTemplateId());
				aRow.createCell(1).setCellValue(styleReferencesVO.getstyleID());
				aRow.createCell(2).setCellValue(styleReferencesVO.gethierarchyID());
				/*
				 * for (int colNum = 0; colNum < aRow.getLastCellNum();
				 * colNum++) { sheet.autoSizeColumn(colNum); }
				 */

			}
		} else {
			logger.info("Style References List is empty");
			DatamigrationCommonUtil.printConsole("Style References List is empty");
		}
	}

	// Phase 2
	/**
	 * For "Kit Contents" tab in Product Template sheet
	 * @param kitContentsVOList
	 * @param sheet
	 */
	private void createKitContentsRows(List<KitContentsVO> kitContentsVOList, HSSFSheet sheet) {

		int rowCount = 1;
		if (kitContentsVOList != null) {
			for (KitContentsVO kitContentsVO : kitContentsVOList) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				aRow.createCell(0).setCellValue(kitContentsVO.getTemplateId());
				aRow.createCell(1).setCellValue(kitContentsVO.getKitContainedSku());
				aRow.createCell(2).setCellValue(kitContentsVO.getActiveInd());
				aRow.createCell(3).setCellValue(kitContentsVO.getKitContainedSKUQty());
				/*
				 * for (int colNum = 0; colNum < aRow.getLastCellNum();
				 * colNum++) { sheet.autoSizeColumn(colNum); }
				 */
			}
		} else {
			logger.info("Kit contents List is empty");
			DatamigrationCommonUtil.printConsole("Kit contents List is empty");
		}
	}

	// Phase 2
	/**
	 * For "Component Properties" tab in Config Hierarchy sheet
	 * @param componentPropertiesList
	 * @param sheet
	 */
	private void createComponentPropertiesRows(List<ComponentPropertiesVO> componentPropertiesList, HSSFSheet sheet) {

		int rowCount = 1;
		if (componentPropertiesList != null) {
			for (ComponentPropertiesVO componentPropertiesVO : componentPropertiesList) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				aRow.createCell(0).setCellValue(componentPropertiesVO.getConfigAttrValId());
				aRow.createCell(1).setCellValue(componentPropertiesVO.getConfigAttrValName());
				// PCMP-2618 Post Phase2 CnP Sprint Changes
				aRow.createCell(2).setCellValue(componentPropertiesVO.getPropertyFanId());
				aRow.createCell(3).setCellValue(componentPropertiesVO.getPropertyName());
				aRow.createCell(4).setCellValue(componentPropertiesVO.getPropertyValue());
				/*
				 * for (int colNum = 0; colNum < aRow.getLastCellNum();
				 * colNum++) { sheet.autoSizeColumn(colNum); }
				 */
			}
		} else {
			logger.info("Kit contents List is empty");
			DatamigrationCommonUtil.printConsole("Kit contents List is empty");
		}
	}

	// Phase 2
	/**
	 * For "Messaging Type" tab in Config Hierarchy sheet
	 * @param messagingTypeList
	 * @param sheet
	 */
	private void createMessagingTypeRows(List<MessagingTypeVO> messagingTypeList, HSSFSheet sheet) {

		int rowCount = 1;
		if (messagingTypeList != null) {
			for (MessagingTypeVO messagingTypeVO : messagingTypeList) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				aRow.createCell(0).setCellValue(messagingTypeVO.getConfigAttrValId());
				aRow.createCell(1).setCellValue(messagingTypeVO.getConfigAttrValName());
				aRow.createCell(2).setCellValue(messagingTypeVO.getMessageType());
				aRow.createCell(3).setCellValue(messagingTypeVO.getTargetedValId());
				aRow.createCell(4).setCellValue(messagingTypeVO.getTargetedValName());
				aRow.createCell(5).setCellValue(messagingTypeVO.getMsgTitle());
				aRow.createCell(6).setCellValue(messagingTypeVO.getMsgDescription());
				/*
				 * for (int colNum = 0; colNum < aRow.getLastCellNum();
				 * colNum++) { sheet.autoSizeColumn(colNum); }
				 */
			}
		} else {
			logger.info("Kit contents List is empty");
			DatamigrationCommonUtil.printConsole("Kit contents List is empty");
		}
	}

	/**
	 * @param hssfCell
	 * @param value
	 */
	private void setNumericCellValue(HSSFCell hssfCell, String value) {

		if (IntgSrvUtils.isNumeric(value)) {
			hssfCell.setCellValue(IntgSrvUtils.toInt(value));
		} else {
			hssfCell.setCellValue(value);
		}
	}

	// Phase 2
	/**
	 * To check if the level is Material
	 * @param listVO
	 * @return
	 */
	public boolean isMaterial(SheetListVO listVO) {
		for (int i = 0; i < listVO.getConfigHierarchyList().size(); i++) {
			if (listVO.getConfigHierarchyList().get(i).getLevelId() == 3) {
				return true;
			}
		}
		return false;
	}

	/**
	 * To check if the ItemType is "Kit Template"
	 * @param listVO
	 * @return
	 */
	public boolean isKits(SheetListVO listVO) {

		for (int i = 0; i < listVO.getTemplatesList().size(); i++) {
			if (KIT_TYPE.equalsIgnoreCase(listVO.getTemplatesList().get(i).getItemType())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * To check if the Pricing Strategy value is "Configuration"
	 * @param listVO
	 * @return
	 */
	public boolean isPricingStrategy(SheetListVO listVO) {

		for (int i = 0; i < listVO.getTemplatesList().size(); i++) {
			if ("Configuration".equalsIgnoreCase(listVO.getTemplatesList().get(i).getPricingStategy())) {
				return true;
			}
		}
		return false;
	}
}
