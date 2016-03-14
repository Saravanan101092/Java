
package com.staples.pim.delegate.copyandprint.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ASSET_CROSS_REFERENCE_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CHILD_MATERIAL;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_CNP;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.IMAGE_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ITEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.MATERIAL_CATEGORY;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.METADATA_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.MULTIVALUE_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.N_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRIMARY_IMAGE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRIMARY_ITEM;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRIMARY_MATERIAL_HIERARCHY;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRIMARY_PRODUCTS;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRODUCT_CROSS_REFERENCE_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRODUCT_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SKUFOLDER;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.STRING_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.VALUES_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.VALUE_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.Y_STR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.delegate.copyandprint.beans.ConfigurationsVO;
import com.staples.pim.delegate.copyandprint.beans.ImagesVO;
import com.staples.pim.delegate.copyandprint.beans.InvalidAssociationsVO;
import com.staples.pim.delegate.copyandprint.beans.KitContentsVO;
import com.staples.pim.delegate.copyandprint.beans.SheetListVO;
import com.staples.pim.delegate.copyandprint.beans.StyleReferencesVO;
import com.staples.pim.delegate.copyandprint.beans.TemplateComponentsVO;
import com.staples.pim.delegate.copyandprint.beans.TemplatesVO;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pcm.stepcontract.beans.AssetCrossReferenceType;
import com.staples.pcm.stepcontract.beans.MetaDataType;
import com.staples.pcm.stepcontract.beans.MultiValueType;
import com.staples.pcm.stepcontract.beans.ProductCrossReferenceType;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

/**
 * @author 522462
 * 
 */
public class SpreadSheetGenerationProduct {

	/**
	 * Logger initialization
	 */
	static IntgSrvLogger						logger							= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER_CNP);
	public static IntgSrvLogger					ehfLogger						= IntgSrvLogger
																						.getInstance(DatamigrationAppConstants.EHF_LOGGER_CNP);

	/**
	 * @param args
	 */
	private Map<String, Map<String, String>>	defaultActiIndMap;
	private Map<String, List<String>>			invalidConfigMap;
	public Map<String, String>					itemValuesMap;
	public Map<String, String>					skuValuesMap;
	public Map<String, String>					applicablePagesMap;
	// public Map<String, String> assertValueMap;
	// Map<String, ConfigurationsVO> configAttMap = new HashMap<String,
	// ConfigurationsVO>();
	List<ConfigurationsVO>						configList						= new ArrayList<ConfigurationsVO>();

	// For parsing primary hierarchy materials
	public List<ConfigurationsVO>				materialTypeConfigList			= null;
	public List<ConfigurationsVO>				materialConfiList				= null;
	// public Map<String ,List<ConfigurationsVO>> finalConfiglistMaterial=null;
	public List<ConfigurationsVO>				finalConfiglistMaterial			= null;
	public List<ConfigurationsVO>				materialCatConfigList			= null;
	public List<ImagesVO>						assetValueList					= null;

	// Phase 2
	// These lists contains the list of Product ids for each Type namely
	// "ItemtoMaterial","CnPexceptionPages","CnP KitsReferences" respectively
	List<String>								itemToMaterialProductIDList		= new ArrayList<String>();
	List<String>								CnPExceptionPagesProductIDList	= new ArrayList<String>();
	List<String>								kitProductIDList				= new ArrayList<String>();
	// skuValuesWithPrdIDMap is used to store the attribute ids and their values
	// for each product along with their product id.
	Map<String, Map<String, String>>			skuValuesWithPrdIDMap			= new HashMap<String, Map<String, String>>();
	Map<String, Map<String, List<String>>>		invalidConfigMapWithPrdID		= new HashMap<String, Map<String, List<String>>>();
	Map<String, List<ImagesVO>>					assetValuesMapMapWithPrdID		= new HashMap<String, List<ImagesVO>>();
	// primaryItemID id the Product ID whose type is Primary Item
	public static String						primaryItemID;

	public final String							A1021_STR						= "A1021";
	public final String							A0012_STR						= "A0012";
	public final String							A1024_STR						= "A1024";
	public final String							A1025_STR						= "A1025";
	public final String							A1022_STR						= "A1022";
	public final String							A1027_STR						= "A1027";
	public final String							A0210_STR						= "A0210";
	public final String							A0211_STR						= "A0211";
	public final String							A1345_STR						= "A1345";
	public final String							A1059_STR						= "A1059";
	public final String							A1233_STR						= "A1233";
	public final String							A1061_STR						= "A1061";
	public final String							A1234_STR						= "A1234";
	public final String							A1062_STR						= "A1062";
	public final String							A1235_STR						= "A1235";
	public final String							A1357_STR						= "A1357";
	public final String							A1236_STR						= "A1236";
	public final String							A1358_STR						= "A1358";
	public final String							A1352_STR						= "A1352";
	public final String							A1360_STR						= "A1360";
	public final String							A1359_STR						= "A1359";
	public final String							A1057_STR						= "A1057";
	// Phase 2
	public final String							A1349_STR						= "A1349";
	public final String							A1350_STR						= "A1350";
	public final String							A1351_STR						= "A1351";
	public final String							A1421_STR						= "A1421";
	public final String							A1317_STR						= "A1317";
	public final String							A1318_STR						= "A1318";
	public final String							A1413_STR						= "A1413";
	public final String							A1420_STR						= "A1420";
	public final String							A1414_STR						= "A1414";
	public final String							A1418_STR						= "A1418";
	public final String							A1038_STR						= "A1038";
	public final String							A1428_STR						= "A1428";
	public final String							PRODUCT_NAME_STR				= "Product_Name";
	public final String							CONFIGURATION_STR				= "Configuration";
	public final String							ITEMTOMATERIAL_STR				= "ItemtoMaterial";
	public final String							CNP_EXCEPTIONPAGES_STR			= "CnP ExceptionPages";
	public final String							CNP_KITSREFERENCES_STR			= "CnP KitsReferences";
	public final String							PAGE_NUMBER_STR					= "A1430";

	/**
	 * @param stepXMLStr
	 * @return
	 */
	public SpreadSheetGenerationProduct() {

	}

	/**
	 * This method is used to fetch an attribute value given an attribute ID
	 * either from Item level or SKU Level
	 * 
	 * @param FanID
	 * @return
	 */
	public String getFanIDValue(String FanID) {

		if (itemValuesMap.containsKey(FanID)) {
			return itemValuesMap.get(FanID);
		}
		if (skuValuesMap.containsKey(FanID)) {
			return skuValuesMap.get(FanID);
		}

		return "";
	}

	/**
	 * To create the VO to populate the values in the Template tab in the
	 * product template sheet. The values are fetched from Product whose type is
	 * Primary Item under the SKU Folder
	 * 
	 * @param sheetListVO
	 */
	public void createProdTemplateVO(SheetListVO sheetListVO) {

		List<TemplatesVO> templatevolist = new ArrayList<TemplatesVO>();
		TemplatesVO templatesvo = new TemplatesVO();

		templatesvo.setTemplateId(skuValuesWithPrdIDMap.get(primaryItemID).get(A1021_STR));
		templatesvo.setTemplateSku(getFanIDValue(A0012_STR));
		templatesvo.setTemplateName(skuValuesWithPrdIDMap.get(primaryItemID).get(PRODUCT_NAME_STR));
		templatesvo.setTemplateDesc(skuValuesWithPrdIDMap.get(primaryItemID).get(A1025_STR));
		templatesvo.setTemplateType(skuValuesWithPrdIDMap.get(primaryItemID).get(A1027_STR));
		templatesvo.setSellUom(skuValuesWithPrdIDMap.get(primaryItemID).get(A0210_STR));
		templatesvo.setSellUomQty(skuValuesWithPrdIDMap.get(primaryItemID).get(A0211_STR));
		templatesvo.setBaseUom(skuValuesWithPrdIDMap.get(primaryItemID).get(A1057_STR));
		// templatesvo.setOrderQtyList(getFanIDValue(A1345_STR));
		templatesvo.setOrderQtyList(DatamigrationCommonUtil.getCommaSeparetedValues(invalidConfigMapWithPrdID.get(primaryItemID).get(
				A1345_STR)));
		templatesvo.setMinOrdrQty(skuValuesWithPrdIDMap.get(primaryItemID).get(A1059_STR));
		templatesvo.setMaxOrdrQty(skuValuesWithPrdIDMap.get(primaryItemID).get(A1233_STR));
		templatesvo.setPaymentReqd(skuValuesWithPrdIDMap.get(primaryItemID).get(A1061_STR));
		templatesvo.setArtWorkProvision(skuValuesWithPrdIDMap.get(primaryItemID).get(A1234_STR));
		templatesvo.setTurnTimeRange(skuValuesWithPrdIDMap.get(primaryItemID).get(A1235_STR));
		templatesvo.setProductionNotes(skuValuesWithPrdIDMap.get(primaryItemID).get(A1062_STR));
		templatesvo.setActiveInd(skuValuesWithPrdIDMap.get(primaryItemID).get(A1357_STR));
		templatesvo.setQuickDelvryInd(skuValuesWithPrdIDMap.get(primaryItemID).get(A1236_STR));
		// Commented for Phase2
		// templatesvo.setStyleId(DatamigrationCommonUtil.getCommaSeparetedValues(invalidConfigMap.get(A1022_STR)));
		// templatesvo.setItemType("Product Template"); //It may be dynamic in
		// future release A1414

		// Phase 2
		templatesvo.setItemType(skuValuesWithPrdIDMap.get(primaryItemID).get(A1414_STR));
		templatesvo.setPricingStategy(skuValuesWithPrdIDMap.get(primaryItemID).get(A1420_STR));
		templatesvo.setIsSaleableInd(skuValuesWithPrdIDMap.get(primaryItemID).get(A1418_STR));
		if (skuValuesWithPrdIDMap.get(primaryItemID).get(A1038_STR) != "") {
			templatesvo.setExceptionPagesAllowedInd(skuValuesWithPrdIDMap.get(primaryItemID).get(A1038_STR));
		}
		templatesvo.setLongDescription(skuValuesWithPrdIDMap.get(primaryItemID).get(A1428_STR));
		// templatesvo.setIsDCS(skuValuesWithPrdIDMap.get(primaryItemID).get("IsDCS"));//FIXME
		templatevolist.add(templatesvo);
		sheetListVO.setTemplatesList(templatevolist);
	}

	// Phase 2
	/**
	 * To create the VO to populate the values in the Style References tab in
	 * the product template sheet. The values are fetched from Product whose
	 * type is Primary Item under the SKU Folder
	 * 
	 * @param sheetListVO
	 */
	public void createStyleReferencesVO(SheetListVO sheetListVO) {

		List<StyleReferencesVO> styleRefVOlist = new ArrayList<StyleReferencesVO>();
		for (int incr = 0; incr < invalidConfigMapWithPrdID.get(primaryItemID).get(A1022_STR).size(); incr++) {
			StyleReferencesVO styleRefVO = new StyleReferencesVO();
			styleRefVO.setTemplateId(skuValuesWithPrdIDMap.get(primaryItemID).get(A1021_STR));
			if (invalidConfigMapWithPrdID.get(primaryItemID).get(A1022_STR).get(incr).contains("(")) {
				String style[] = invalidConfigMapWithPrdID.get(primaryItemID).get(A1022_STR).get(incr).split("\\(");
				styleRefVO.setstyleID(style[0]);
				if (style[1].contains(DatamigrationAppConstants.EPRICEBOOK)) {
					styleRefVO.sethierarchyID(DatamigrationAppConstants.EPRICEBOOK);
				} else if (style[1].contains(DatamigrationAppConstants.SOLUTIONBUILDER)) {
					styleRefVO.sethierarchyID(DatamigrationAppConstants.SOLUTIONBUILDER);
				}
			}
			styleRefVOlist.add(styleRefVO);
		}
		sheetListVO.setStyleReferenceVOList(styleRefVOlist);
	}

	// Phase 2
	/**
	 * To create the VO to populate the values in the Invalid Associations tab
	 * in the product template sheet. The values are fetched from Product whose
	 * type is Primary Item under the SKU Folder
	 * 
	 * @param sheetListVO
	 */
	public void createInvalidConfigurationsVO(SheetListVO sheetListVO) {

		List<InvalidAssociationsVO> invalidAssociationsVOlist = new ArrayList<InvalidAssociationsVO>();
		List<String> localInvalid = invalidConfigMapWithPrdID.get(primaryItemID).get(A1317_STR);// Local
																								// Invalid
		List<String> globalInvalid = invalidConfigMapWithPrdID.get(primaryItemID).get(A1318_STR);// Global
																									// Invalid
		List<String> exceptionInvalid = invalidConfigMapWithPrdID.get(primaryItemID).get(A1413_STR);// Exception
																									// Pages
																									// Invalid

		if (localInvalid != null) {
			for (String localInvalidStr : localInvalid) {
				if (getInvalidConfig(localInvalidStr) != null) {
					InvalidAssociationsVO invalidAssociationsVO = new InvalidAssociationsVO();
					invalidAssociationsVO.setTemplateId(skuValuesWithPrdIDMap.get(primaryItemID).get(A1021_STR));
					invalidAssociationsVO.setConfigAttrValId(getInvalidConfig(localInvalidStr).get(0));
					invalidAssociationsVO.setInvalidConfigAttrValId(getInvalidConfig(localInvalidStr).get(1));
					invalidAssociationsVO.setExceptionPagesApplicable("N");
					invalidAssociationsVOlist.add(invalidAssociationsVO);
				}
			}
		}

		if (globalInvalid != null) {
			for (String globalInvalidStr : globalInvalid) {
				if (getInvalidConfig(globalInvalidStr) != null) {
					InvalidAssociationsVO invalidAssociationsVO = new InvalidAssociationsVO();
					invalidAssociationsVO.setTemplateId("");
					invalidAssociationsVO.setConfigAttrValId(getInvalidConfig(globalInvalidStr).get(0));
					invalidAssociationsVO.setInvalidConfigAttrValId(getInvalidConfig(globalInvalidStr).get(1));
					invalidAssociationsVO.setExceptionPagesApplicable("N");
					invalidAssociationsVOlist.add(invalidAssociationsVO);
				}
			}
		}
		if (exceptionInvalid != null) {
			for (String exceptionInvalidStr : exceptionInvalid) {
				// Commented for PCMP-2280 no filtering of Exception Pages based
				// on Pricing Strategy
				// if (getInvalidConfig(exceptionInvalidStr) != null
				// &&
				// !skuValuesWithPrdIDMap.get(primaryItemID).get(A1420_STR).equalsIgnoreCase(CONFIGURATION_STR))
				// {
				if (getInvalidConfig(exceptionInvalidStr) != null) {
					InvalidAssociationsVO invalidAssociationsVO = new InvalidAssociationsVO();
					invalidAssociationsVO.setTemplateId(skuValuesWithPrdIDMap.get(primaryItemID).get(A1021_STR));
					invalidAssociationsVO.setConfigAttrValId(getInvalidConfig(exceptionInvalidStr).get(0));
					invalidAssociationsVO.setInvalidConfigAttrValId(getInvalidConfig(exceptionInvalidStr).get(1));
					invalidAssociationsVO.setExceptionPagesApplicable("Y");
					invalidAssociationsVOlist.add(invalidAssociationsVO);
				}
			}
		}

		sheetListVO.setInvalidAssocationsList(invalidAssociationsVOlist);
	}

	/**
	 * Given an invalid string say
	 * "Speciality Coverstock 80# Elite Gloss Cover(V54)*SpecialityText32#PearlIvory(V53)"
	 * , this method returns a list containing the IDS like V54,V53
	 * 
	 * @param invalidStr
	 * @return
	 */
	public List<String> getInvalidConfig(String invalidStr) {

		if (invalidStr != null) {
			String inValidArray[] = invalidStr.split("\\*");
			List<String> invalidListValue = new ArrayList<String>();
			for (String strArrVal : inValidArray) {
				String subStrVal = strArrVal.substring(strArrVal.indexOf("(") + 1, strArrVal.indexOf(")"));
				invalidListValue.add(subStrVal);
			}
			return invalidListValue;
		}
		return null;

	}

	/**
	 * This method returns the final sheetListVO
	 * 
	 * @param product
	 * @return
	 */
	public SheetListVO getProductTemplateList(STEPProductInformation product) throws Exception {

		try {

			resetMaps();
			SheetListVO sheetListVO = new SheetListVO();
			// After the first <Products> tag is parsed we get the <Product>
			// tag. From here we can iterate along the tags.
			ProductsType producttype = product.getProducts();
			List<Object> objList = producttype.getProduct().get(0).getProductOrSequenceProductOrSuppressedProductCrossReference();

			// Call the getProduct() method with the list of objects containing
			// Product datatype.
			loadStepXMLValuesInMap(objList);
			if (finalConfiglistMaterial != null) {
				getConfigTemplates(sheetListVO);
			} else {
				logger.error(DatamigrationCommonUtil.getClassAndMethodName() + "Configuration details are missing..");
			}

			getImageReferences(sheetListVO);
			createProdTemplateVO(sheetListVO);
			// Phase 2
			createStyleReferencesVO(sheetListVO);
			createInvalidConfigurationsVO(sheetListVO);

			if (DatamigrationAppConstants.KIT_TYPE.equalsIgnoreCase(skuValuesWithPrdIDMap.get(primaryItemID).get(A1414_STR))) {
				createKitTemplateVO(sheetListVO);
			}

			logger.info(DatamigrationCommonUtil.getClassAndMethodName() + "Maps generated");

			logger.info("DefaultActiIndMap : " + defaultActiIndMap);
			logger.info("InvalidConfigMap : " + invalidConfigMapWithPrdID.get(primaryItemID));
			logger.info("SKU ValuesMap : " + skuValuesWithPrdIDMap.get(primaryItemID));
			logger.info("*****SKU******  :  " + getFanIDValue(A0012_STR));

			DatamigrationCommonUtil.printConsole("DefaultActiIndMap : " + defaultActiIndMap);
			DatamigrationCommonUtil.printConsole("InvalidConfigMap : " + invalidConfigMapWithPrdID.get(primaryItemID));
			DatamigrationCommonUtil.printConsole("SKU ValuesMap : " + skuValuesWithPrdIDMap.get(primaryItemID));
			DatamigrationCommonUtil.printConsole("*****SKU******  :  " + getFanIDValue(A0012_STR));

			return sheetListVO;

		} catch (Exception e) {
			logger.error(DatamigrationCommonUtil.getClassAndMethodName() + " exception caught: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void resetMaps() {

		skuValuesMap = new HashMap<String, String>();
		itemValuesMap = new HashMap<String, String>();
		invalidConfigMap = new HashMap<String, List<String>>();
		defaultActiIndMap = new HashMap<String, Map<String, String>>();
		configList = new ArrayList<ConfigurationsVO>();
		materialTypeConfigList = new ArrayList<ConfigurationsVO>();
		materialConfiList = new ArrayList<ConfigurationsVO>();
		finalConfiglistMaterial = new ArrayList<ConfigurationsVO>();
		materialCatConfigList = new ArrayList<ConfigurationsVO>();
		assetValueList = new ArrayList<ImagesVO>();
		itemToMaterialProductIDList = new ArrayList<String>();
		CnPExceptionPagesProductIDList = new ArrayList<String>();
		kitProductIDList = new ArrayList<String>();
		skuValuesWithPrdIDMap = new HashMap<String, Map<String, String>>();
		invalidConfigMapWithPrdID = new HashMap<String, Map<String, List<String>>>();
		assetValuesMapMapWithPrdID = new HashMap<String, List<ImagesVO>>();
		applicablePagesMap = new HashMap<String, String>();
	}

	// Phase 2
	/**
	 * To create the VO to populate the values in the Kit Contents tab in the
	 * product template sheet. The values are fetched from Product whose type is
	 * CnP KitsReferences
	 * 
	 * @param sheetListVO
	 */
	private void createKitTemplateVO(SheetListVO sheetListVO) {

		List<KitContentsVO> kitContentsVOList = new ArrayList<KitContentsVO>();
		for (int j = 0; j < kitProductIDList.size(); j++) {
			KitContentsVO kitContentsVO = new KitContentsVO();
			kitContentsVO.setActiveInd(defaultActiIndMap.get(kitProductIDList.get(j)).get(A1358_STR));
			kitContentsVO.setKitContainedSku(skuValuesWithPrdIDMap.get(kitProductIDList.get(j)).get("A1363"));
			kitContentsVO.setKitContainedSKUQty(defaultActiIndMap.get(kitProductIDList.get(j)).get("A1419"));
			kitContentsVO.setTemplateId(skuValuesWithPrdIDMap.get(primaryItemID).get(A1021_STR));
			kitContentsVOList.add(kitContentsVO);
		}
		sheetListVO.setKitContentsList(kitContentsVOList);

	}

	/**
	 * To create the VO to populate the values in the Images tab in the product
	 * template sheet. The values are fetched from AssetCrossReference of
	 * PrimaryItem Product
	 * 
	 * @param sheetListVO
	 */
	private void getImageReferences(SheetListVO sheetListVO) {

		logger.info(DatamigrationCommonUtil.getClassAndMethodName() + " getting image references");
		// ImagesVO imageVO= null;
		// List<ImagesVO> list = new ArrayList<ImagesVO>();
		/*
		 * for(Map.Entry<String, String> keyVal : assertValueMap.entrySet()){
		 * String defInd =
		 * PRIMARY_IMAGE.equalsIgnoreCase(keyVal.getValue())?Y_STR:N_STR;
		 * imageVO = new ImagesVO(getFanIDValue(A1021_STR), "PRODUCT TEMPLATE",
		 * keyVal.getKey(), keyVal.getValue(), defInd); list.add(imageVO); }
		 */

		for (ImagesVO imgVO : assetValuesMapMapWithPrdID.get(primaryItemID)) {

			// String defInd =
			// PRIMARY_IMAGE.equalsIgnoreCase(imgVO.getImageType())?Y_STR:N_STR;
			// imgVO.setDefInd(defInd);
			imgVO.setEntityId(skuValuesWithPrdIDMap.get(primaryItemID).get(A1021_STR));
			imgVO.setEntityType(skuValuesWithPrdIDMap.get(primaryItemID).get(A1414_STR));// Entity
																							// Type
																							// set
																							// based
																							// on
																							// Item
																							// Type

		}

		sheetListVO.setImageList(assetValuesMapMapWithPrdID.get(primaryItemID));
	}

	/**
	 * @param objects
	 */
	public void loadStepXMLValuesInMap(List<Object> objects) {

		for (Object object : objects) {
			if (PRODUCT_TYPE.equalsIgnoreCase(object.getClass().getSimpleName())) {
				ProductType prodType = (ProductType) object;
				if (PRIMARY_MATERIAL_HIERARCHY.equalsIgnoreCase(prodType.getUserTypeID())) {
					getPrimaryMaterialDetails(prodType);
					continue;
				}
				if (PRIMARY_PRODUCTS.equalsIgnoreCase(prodType.getUserTypeID())) {
					getPrimaryProductsDetails(prodType.getProductOrSequenceProductOrSuppressedProductCrossReference());
					continue;
				}
				loadStepXMLValuesInMap(prodType.getProductOrSequenceProductOrSuppressedProductCrossReference());
			}
		}
	}

	/**
	 * To create the VO to populate the values in the Template Components tab
	 * and Template Configurations tab in the product template sheet.
	 * 
	 * @param sheetListVO
	 */
	private void getConfigTemplates(SheetListVO sheetListVO) {

		logger.info(DatamigrationCommonUtil.getClassAndMethodName());

		List<ConfigurationsVO> finalConfigVOList = new ArrayList<ConfigurationsVO>();
		List<ConfigurationsVO> itemToMaterialConfigVOList = new ArrayList<ConfigurationsVO>();
		List<TemplateComponentsVO> templateComponentsVOList = new ArrayList<TemplateComponentsVO>();
		for (ConfigurationsVO configVo : finalConfiglistMaterial) {
			int flag = 0;
			TemplateComponentsVO templateComponentsVO = new TemplateComponentsVO();
			templateComponentsVO.setTemplateId(skuValuesWithPrdIDMap.get(primaryItemID).get(A1021_STR));
			templateComponentsVO.setDefAttrValInd(defaultActiIndMap.get(configVo.getProductTagId()).get(A1352_STR));
			templateComponentsVO.setConfigAttrId(configVo.getConfigAttrId());
			// PCMP-2618 Post Phase2 CnP Sprint Changes-delete Custom Qty Prompt
			// and add Active Ind
			// templateComponentsVO.setCustomQtyPrompt(defaultActiIndMap.get(configVo.getProductTagId()).get(A1349_STR));
			templateComponentsVO.setActiveInd(defaultActiIndMap.get(configVo.getProductTagId()).get(A1358_STR));
			templateComponentsVO.setCustomQtyMinValue(defaultActiIndMap.get(configVo.getProductTagId()).get(A1350_STR));
			templateComponentsVO.setCustomQtyMaxValue(defaultActiIndMap.get(configVo.getProductTagId()).get(A1351_STR));
			templateComponentsVO.setConfigAttrValId(configVo.getConfigAttrValId());
			templateComponentsVO.setSupressMsgInd(defaultActiIndMap.get(configVo.getProductTagId()).get(A1421_STR));
			configVo.setDefAttrValInd(defaultActiIndMap.get(configVo.getProductTagId()).get(A1352_STR));
			configVo.setActiveId(defaultActiIndMap.get(configVo.getProductTagId()).get(A1358_STR));
			configVo.setTemplateId(skuValuesWithPrdIDMap.get(primaryItemID).get(A1021_STR));

			if (configVo.getProductTagId() != null && itemToMaterialProductIDList.contains(configVo.getProductTagId())) {
				flag += 1;
				templateComponentsVO.setExceptionPageConfigAttrInd("N");
				itemToMaterialConfigVOList.add(configVo);
				templateComponentsVOList.add(templateComponentsVO);
			}
			if (configVo.getProductTagId() != null && CnPExceptionPagesProductIDList.contains(configVo.getProductTagId())) {
				if (flag >= 1) {
					TemplateComponentsVO templateComponentsVO1 = new TemplateComponentsVO();
					templateComponentsVO1.setTemplateId(itemValuesMap.get(A1021_STR));
					templateComponentsVO1.setDefAttrValInd(defaultActiIndMap.get(configVo.getProductTagId()).get(A1352_STR));
					templateComponentsVO1.setConfigAttrId(configVo.getConfigAttrId());
					// PCMP-2618 Post Phase2 CnP Sprint Changes-delete Custom
					// Qty Prompt and add Active Ind
					// templateComponentsVO1.setCustomQtyPrompt(defaultActiIndMap.get(configVo.getProductTagId()).get(A1349_STR));
					templateComponentsVO1.setActiveInd(defaultActiIndMap.get(configVo.getProductTagId()).get(A1358_STR));
					templateComponentsVO1.setCustomQtyMinValue(defaultActiIndMap.get(configVo.getProductTagId()).get(A1350_STR));
					templateComponentsVO1.setCustomQtyMaxValue(defaultActiIndMap.get(configVo.getProductTagId()).get(A1351_STR));
					templateComponentsVO1.setConfigAttrValId(configVo.getConfigAttrValId());
					templateComponentsVO1.setSupressMsgInd("");
					templateComponentsVO1.setExceptionPageConfigAttrInd("Y");
					// templateComponentsVO1.setPageNumber(defaultActiIndMap.get(configVo.getProductTagId()).get(PAGE_NUMBER_STR));
					templateComponentsVO1.setPageNumber(applicablePagesMap.get(configVo.getProductTagId()));
					// Commented for PCMP-2280 no filtering of Exception Pages
					// based on Pricing Strategy
					// if
					// (!CONFIGURATION_STR.equalsIgnoreCase(skuValuesWithPrdIDMap.get(primaryItemID).get(A1420_STR)))
					// {
					templateComponentsVOList.add(templateComponentsVO1);
					// }
				} else {
					templateComponentsVO.setPageNumber(applicablePagesMap.get(configVo.getProductTagId()));
					templateComponentsVO.setExceptionPageConfigAttrInd("Y");
					// Commented for PCMP-2280 no filtering of Exception Pages
					// based on Pricing Strategy
					// if
					// (!CONFIGURATION_STR.equalsIgnoreCase(skuValuesWithPrdIDMap.get(primaryItemID).get(A1420_STR)))
					// {

					templateComponentsVOList.add(templateComponentsVO);
					// }
				}
				// itemToMaterialConfigVOList.add(configVo);
			}
			// templateComponentsVOList.add(templateComponentsVO);
		}

		// Filter Configuration Id generation process based on the Pricing
		// Strategy
		if (CONFIGURATION_STR.equalsIgnoreCase(skuValuesWithPrdIDMap.get(primaryItemID).get(A1420_STR))) {
			logger.info("Pricing Strategy is Configuration. Hence ID generation started");
			List<List<String>> invalidConfigDetailsList = getInvalidConfigurationDetails();

			String skuNumber = DatamigrationCommonUtil.getNotNullValue(getFanIDValue(A0012_STR), STRING_TYPE).toString();
			List<List<String>> generatedKeysList = new ConfigurationIDGenerator().getGeneratedKeys(itemToMaterialConfigVOList,
					invalidConfigDetailsList, skuNumber);
			finalConfigVOList.addAll(itemToMaterialConfigVOList);

			logger.info(DatamigrationCommonUtil.getClassAndMethodName() + "Final number of unique key combinations generated : "
					+ generatedKeysList.size());
			sheetListVO.setConfigList(finalConfigVOList);

			if (!generatedKeysList.isEmpty() && generatedKeysList != null)
				sheetListVO.setgeneratedKeys(generatedKeysList);
		}
		sheetListVO.setTemplateComponentsList(templateComponentsVOList);
	}

	/**
	 * To return the List of local and global invalid combinations
	 * 
	 * @return
	 */
	private List<List<String>> getInvalidConfigurationDetails() {

		List<String> inValidConfig = new ArrayList<String>();
		List<List<String>> returnList = new ArrayList<List<String>>();

		List<String> localInvalid = invalidConfigMapWithPrdID.get(primaryItemID).get(A1317_STR);
		List<String> globalInvalid = invalidConfigMapWithPrdID.get(primaryItemID).get(A1318_STR);

		if (localInvalid != null) {
			inValidConfig.addAll(localInvalid);
		}
		if (globalInvalid != null) {
			inValidConfig.addAll(globalInvalid);
		}

		for (String localInvalidStr : inValidConfig) {
			String inValidArray[] = localInvalidStr.split("\\*");
			List<String> invalidListValue = new ArrayList<String>();
			for (String strArrVal : inValidArray) {
				String subStrVal = strArrVal.substring(strArrVal.indexOf("(") + 1, strArrVal.indexOf(")"));
				invalidListValue.add(subStrVal);
				// invalidListValue.add(strArrVal);
			}
			returnList.add(invalidListValue);
		}

		logger.info(DatamigrationCommonUtil.getClassAndMethodName() + " invalid configurations list fetched" + returnList);
		return returnList;
	}

	/**
	 * To get the values under Primary Product Hierarchy
	 * 
	 * @param objects
	 * @return
	 */
	private Map<String, Map<String, String>> getPrimaryProductsDetails(List<Object> objects) {

		for (Object object : objects) {
			if (PRODUCT_TYPE.equalsIgnoreCase(object.getClass().getSimpleName())) {
				ProductType prodTypeObj = (ProductType) object;
				if (ITEM.equalsIgnoreCase(prodTypeObj.getUserTypeID())) {
					if (getProdCrossRefDefaultActiveInd(prodTypeObj.getProductOrSequenceProductOrSuppressedProductCrossReference()) != null
							&& !getProdCrossRefDefaultActiveInd(prodTypeObj.getProductOrSequenceProductOrSuppressedProductCrossReference())
									.isEmpty()) {
						defaultActiIndMap = getProdCrossRefDefaultActiveInd(prodTypeObj
								.getProductOrSequenceProductOrSuppressedProductCrossReference());
					}
					invalidConfigMap = getValuesTypeMutliValue(prodTypeObj.getProductOrSequenceProductOrSuppressedProductCrossReference());// can
					invalidConfigMapWithPrdID.put(prodTypeObj.getID(), invalidConfigMap);
					// get
					// values
					// list
					itemValuesMap = getValuesType(prodTypeObj.getProductOrSequenceProductOrSuppressedProductCrossReference());// can
					// get
					// values
					// list
					itemValuesMap.put(A1024_STR, prodTypeObj.getName().get(0).getContent());
					// assertValueMap =
					// getAssertDetails(prodTypeObj.getProductOrSequenceProductOrSuppressedProductCrossReference());
					assetValueList = getAssetDetails(prodTypeObj.getProductOrSequenceProductOrSuppressedProductCrossReference());
					if (!assetValueList.isEmpty() && assetValueList != null) {
						assetValuesMapMapWithPrdID.put(prodTypeObj.getID(), assetValueList);
					}
					continue;
				}
				if (SKUFOLDER.equalsIgnoreCase(prodTypeObj.getUserTypeID())) {
					skuValuesMap = getSKUInformation(prodTypeObj.getProductOrSequenceProductOrSuppressedProductCrossReference());
				}
				getPrimaryProductsDetails(prodTypeObj.getProductOrSequenceProductOrSuppressedProductCrossReference());
			}
		}
		return defaultActiIndMap;
	}

	/**
	 * To get the values under SKU Folder
	 * 
	 * @param list
	 * @return
	 */
	private Map<String, String> getSKUInformation(List<Object> list) {

		for (Object object : list) {
			if (PRODUCT_TYPE.equalsIgnoreCase(object.getClass().getSimpleName())) {
				ProductType prodTypeObj = (ProductType) object;
				if (!ITEM.equalsIgnoreCase(prodTypeObj.getUserTypeID())) {
					getSKUInformation(prodTypeObj.getProductOrSequenceProductOrSuppressedProductCrossReference());
				} else {
					Map<String, String> map = getValuesType(prodTypeObj.getProductOrSequenceProductOrSuppressedProductCrossReference());
					map.put(PRODUCT_NAME_STR, prodTypeObj.getName().get(0).getContent());
					skuValuesWithPrdIDMap.put(prodTypeObj.getID(), map);
					if (map != null) {
						skuValuesMap.putAll(map);
					}
				}
				List<Object> productObjects = prodTypeObj.getProductOrSequenceProductOrSuppressedProductCrossReference();
				for (Object prodObject : productObjects) {
					if (PRODUCT_CROSS_REFERENCE_TYPE.equalsIgnoreCase(prodObject.getClass().getSimpleName())) {
						ProductCrossReferenceType valuesObj = (ProductCrossReferenceType) prodObject;
						// if
						// (PRIMARY_ITEM.equalsIgnoreCase(valuesObj.getType()))
						// {
						getSKUInformation(valuesObj.getMetaDataOrProduct());
						// }
					}
				}
			}
			if (VALUES_TYPE.equalsIgnoreCase(object.getClass().getSimpleName())) {
				ValuesType valuesObj = (ValuesType) object;
				Map<String, String> map = getValueTypeFANIDValues(valuesObj.getValueOrMultiValueOrValueGroup());
				if (map != null) {
					skuValuesMap.putAll(map);
				}
			}
			if (PRODUCT_CROSS_REFERENCE_TYPE.equalsIgnoreCase(object.getClass().getSimpleName())) {
				ProductCrossReferenceType valuesObj = (ProductCrossReferenceType) object;
				if (PRIMARY_ITEM.equalsIgnoreCase(valuesObj.getType())) {
					primaryItemID = valuesObj.getProductID();
				}
				getSKUInformation(valuesObj.getMetaDataOrProduct());
			}
		}
		return skuValuesMap;
	}

	/**
	 * @param objects
	 * @return
	 */
	private Map<String, List<String>> getValuesTypeMutliValue(List<Object> objects) {

		Map<String, List<String>> mapValue = new HashMap<String, List<String>>();

		for (Object obj : objects) {
			if (VALUES_TYPE.equalsIgnoreCase(obj.getClass().getSimpleName())) {
				ValuesType prodCrossRef = (ValuesType) obj;
				mapValue = getMultivaluesForFANID(prodCrossRef.getValueOrMultiValueOrValueGroup());
			}
		}

		return mapValue;
	}

	/**
	 * @param objects
	 * @return
	 */
	private Map<String, Map<String, String>> getProdCrossRefDefaultActiveInd(List<Object> objects) {

		Map<String, Map<String, String>> prodCrossRefMap = new HashMap<String, Map<String, String>>();
		Map<String, String> defActIndMap = null;
		for (Object obj : objects) {
			if (PRODUCT_CROSS_REFERENCE_TYPE.equalsIgnoreCase(obj.getClass().getSimpleName())) {
				defActIndMap = new HashMap<String, String>();
				ProductCrossReferenceType prodCrossRef = (ProductCrossReferenceType) obj;
				Map<String, String> allMDValuesMap = getMetaDataTypeAttributeValues(prodCrossRef.getMetaDataOrProduct());
				defActIndMap.put(A1352_STR, allMDValuesMap.get(A1352_STR));
				defActIndMap.put(A1358_STR, allMDValuesMap.get(A1358_STR));
				// prodCrossRefMap.put(prodCrossRef.getProductID(),defActIndMap);
				prodCrossRefMap.put(prodCrossRef.getProductID(), allMDValuesMap);
				if (ITEMTOMATERIAL_STR.equalsIgnoreCase(prodCrossRef.getType())
						&& !itemToMaterialProductIDList.contains(prodCrossRef.getProductID())) {
					itemToMaterialProductIDList.add(prodCrossRef.getProductID());
				}
				if (CNP_EXCEPTIONPAGES_STR.equalsIgnoreCase(prodCrossRef.getType())
						&& !CnPExceptionPagesProductIDList.contains(prodCrossRef.getProductID())) {
					CnPExceptionPagesProductIDList.add(prodCrossRef.getProductID());
					applicablePagesMap.put(prodCrossRef.getProductID(), allMDValuesMap.get(PAGE_NUMBER_STR));
				}
				if (CNP_KITSREFERENCES_STR.equalsIgnoreCase(prodCrossRef.getType())
						&& !kitProductIDList.contains(prodCrossRef.getProductID())) {
					kitProductIDList.add(prodCrossRef.getProductID());
				}

			}
		}
		return prodCrossRefMap;
	}

	/**
	 * To fetch the values under Metadata tag
	 * 
	 * @param objects
	 * @return
	 */
	public Map<String, String> getMetaDataTypeAttributeValues(List<Object> objects) {

		for (Object obj : objects) {
			if (METADATA_TYPE.equalsIgnoreCase(obj.getClass().getSimpleName())) {
				MetaDataType metaDataRef = (MetaDataType) obj;
				return getValueTypeFANIDValues(metaDataRef.getValueOrMultiValueOrValueGroup());
			}
		}
		return null;
	}

	/**
	 * To fetch values from Value tag
	 * 
	 * @param objects
	 * @return
	 */
	public Map<String, String> getValueTypeFANIDValues(List<Object> objects) {

		Map<String, String> fanIDValuesMap = new HashMap<String, String>();
		for (Object values : objects) {
			if (VALUE_TYPE.equalsIgnoreCase(values.getClass().getSimpleName())) {
				ValueType valueTypeObj = (ValueType) values;
				fanIDValuesMap.put(valueTypeObj.getAttributeID(), valueTypeObj.getContent());
			}
		}
		return fanIDValuesMap;
	}

	/**
	 * To fetch the values under Values tag
	 * 
	 * @param objects
	 * @return
	 */
	private Map<String, String> getValuesType(List<Object> objects) {

		Map<String, String> fanIDValuesMap = new HashMap<String, String>();
		for (Object values : objects) {

			if (VALUES_TYPE.equalsIgnoreCase(values.getClass().getSimpleName())) {
				ValuesType valuesTypeObj = (ValuesType) values;
				Map<String, String> fanValues = getValueTypeFANIDValues(valuesTypeObj.getValueOrMultiValueOrValueGroup());
				if (fanValues != null) {
					fanIDValuesMap.putAll(fanValues);
				}

			}
			if (PRODUCT_CROSS_REFERENCE_TYPE.equalsIgnoreCase(values.getClass().getSimpleName())) {
				ProductCrossReferenceType productCrossObj = (ProductCrossReferenceType) values;
				// For getting child material value - A1358 (Active Ind)
				// getChildMaterial(productCrossObj.getMetaDataOrProduct());
			}
		}
		return fanIDValuesMap;
	}

	private void getChildMaterial(List<Object> metaDataOrProduct) {

		for (Object values : metaDataOrProduct) {
			if (PRODUCT_TYPE.equalsIgnoreCase(values.getClass().getSimpleName())) {
				ProductType prdType = (ProductType) values;
				getChildMaterial(prdType.getProductOrSequenceProductOrSuppressedProductCrossReference());
			}
			if (PRODUCT_CROSS_REFERENCE_TYPE.equalsIgnoreCase(values.getClass().getSimpleName())) {
				ProductCrossReferenceType productCrossObj = (ProductCrossReferenceType) values;
				if (CHILD_MATERIAL.equalsIgnoreCase(productCrossObj.getType())) {
					// defaultActiIndMap
					/*
					 * System.out.println("Chil material ........"+productCrossObj
					 * .getProductID());
					 * System.out.println("Chil material ........ "
					 * +productCrossObj.getMetaDataOrProduct());
					 */

					Map<String, String> allMDValuesMap = getMetaDataTypeAttributeValues(productCrossObj.getMetaDataOrProduct());
					Map<String, String> defActIndMap = new HashMap<String, String>();
					;
					defActIndMap.put(A1352_STR, allMDValuesMap.get(A1352_STR));
					defActIndMap.put(A1358_STR, allMDValuesMap.get(A1358_STR));

					defaultActiIndMap.put(productCrossObj.getProductID(), defActIndMap);
				} else {
				}
			}
		}
	}

	/**
	 * @param objects
	 * @return
	 */
	/*
	 * private Map<String, String> getAssertDetails( List<Object> objects) {
	 * Map<String, String> metaDataMap = new HashMap<String, String>(); for
	 * (Object values : objects) {
	 * if(ASSET_CROSS_REFERENCE_TYPE.equalsIgnoreCase
	 * (values.getClass().getSimpleName())) { AssetCrossReferenceType assertObj
	 * = (AssetCrossReferenceType) values;
	 * metaDataMap.put(assertObj.getAssetID(),assertObj.getType()); } } return
	 * metaDataMap; }
	 */

	// FIXING
	private List<ImagesVO> getAssetDetails(List<Object> objects) {

		List<ImagesVO> imagesList = new ArrayList<ImagesVO>();
		ImagesVO image = null;
		for (Object values : objects) {
			if (ASSET_CROSS_REFERENCE_TYPE.equalsIgnoreCase(values.getClass().getSimpleName())) {
				AssetCrossReferenceType assertObj = (AssetCrossReferenceType) values;
				Map<String, String> metaDataMap = StepOutboundProcessor.getAssetMetaDataValues(assertObj);
				image = new ImagesVO();
				image.setImageId(assertObj.getAssetID());
				if (metaDataMap != null) {
					image.setImageType(metaDataMap.get(IMAGE_TYPE));
				}
				String defInd = PRIMARY_IMAGE.equalsIgnoreCase(assertObj.getType()) ? Y_STR : N_STR;
				image.setDefInd(defInd);
				imagesList.add(image);
			}
		}
		return imagesList;
	}

	/**
	 * To fetch values under Multivalue tag
	 * 
	 * @param objects
	 * @return
	 */
	private Map<String, List<String>> getMultivaluesForFANID(List<Object> objects) {

		Map<String, List<String>> metaDataMap = new HashMap<String, List<String>>();
		List<String> list = null;
		for (Object values : objects) {
			if (MULTIVALUE_TYPE.equalsIgnoreCase(values.getClass().getSimpleName())) {
				MultiValueType multiValueObj = (MultiValueType) values;
				list = new ArrayList<String>();
				for (Object obj : multiValueObj.getValueOrValueGroup()) {
					ValueType valueTypeObj = (ValueType) obj;
					list.add(valueTypeObj.getContent());
				}
				metaDataMap.put(multiValueObj.getAttributeID(), list);
			}
		}
		return metaDataMap;
	}

	/**
	 * To fetch values under Primary Material Hierarchy
	 * 
	 * @param prodType
	 * @return
	 */
	private void getPrimaryMaterialDetails(ProductType prodType) {

		materialTypeConfigList = new ArrayList<ConfigurationsVO>();
		finalConfiglistMaterial = new ArrayList<ConfigurationsVO>();
		materialConfiList = new ArrayList<ConfigurationsVO>();
		getMaterialCategory(prodType.getProductOrSequenceProductOrSuppressedProductCrossReference());

	}

	/**
	 * @param productstype
	 * @return
	 */
	public List<ConfigurationsVO> getProductTypes(List<ProductType> productstype) {

		List<ConfigurationsVO> configlist = null;
		for (ProductType producttype : productstype) {
			configlist = getMaterialCategory(producttype.getProductOrSequenceProductOrSuppressedProductCrossReference());
		}
		return configlist;
	}

	/**
	 * To fetch values from Material Category Level. From here Material Type
	 * level is called next
	 * 
	 * @param producttype
	 * @param configlist
	 * @return
	 */
	public List<ConfigurationsVO> getMaterialCategoryContents(ProductType producttype, List<ConfigurationsVO> configlist) {

		logger.info(DatamigrationCommonUtil.getClassAndMethodName() + "getting Material Category " + producttype.getID());
		List<ConfigurationsVO> thisconfiglist = new ArrayList<ConfigurationsVO>();
		List<Object> objects = producttype.getProductOrSequenceProductOrSuppressedProductCrossReference();
		for (Object object : objects) {
			if (PRODUCT_TYPE.equals(object.getClass().getSimpleName())) {
				ProductType product = (ProductType) object;
				getMaterialTypeContents(product, product.getID());
			}
		}
		return thisconfiglist;
	}

	/**
	 * To fetch values from Material Type Level. From here Material level is
	 * called next
	 * 
	 * @param producttype
	 * @param catName
	 * @return
	 */
	public List<ConfigurationsVO> getMaterialTypeContents(ProductType producttype, String catName) {

		logger.info(DatamigrationCommonUtil.getClassAndMethodName() + "getting Material Type " + producttype.getID());
		ConfigurationsVO configurationsVO = null;
		List<Object> objects = producttype.getProductOrSequenceProductOrSuppressedProductCrossReference();

		for (Object object : objects) {
			if (VALUES_TYPE.equals(object.getClass().getSimpleName())) {
				configurationsVO = new ConfigurationsVO();
				ValuesType values = (ValuesType) object;
				List<Object> valueobjects = values.getValueOrMultiValueOrValueGroup();
				for (Object valobject : valueobjects) {
					if (VALUE_TYPE.equals(valobject.getClass().getSimpleName())) {
						ValueType value = (ValueType) valobject;
						if (A1360_STR.equals(value.getAttributeID())) {
							configurationsVO.setConfigAttrId(value.getContent());
							break;
						}
					}
				}
				materialTypeConfigList.add(configurationsVO);
			} else if (PRODUCT_TYPE.equals(object.getClass().getSimpleName())) {
				ProductType product = (ProductType) object;
				getMaterialContents(product, configurationsVO, catName);
			}
		}
		return materialTypeConfigList;
	}

	/**
	 * To fetch values from Material Level
	 * 
	 * @param producttype
	 * @param configvo
	 * @param catName
	 * @return
	 */
	public List<ConfigurationsVO> getMaterialContents(ProductType producttype, ConfigurationsVO configvo, String catName) {

		logger.info(DatamigrationCommonUtil.getClassAndMethodName() + " getting Material details " + producttype.getID());
		String prdID = producttype.getID();
		ConfigurationsVO configurationsVO = new ConfigurationsVO();
		String configAttId = configvo.getConfigAttrId();
		List<Object> objects = producttype.getProductOrSequenceProductOrSuppressedProductCrossReference();
		for (Object object : objects) {
			if (VALUES_TYPE.equals(object.getClass().getSimpleName())) {
				ValuesType values = (ValuesType) object;
				List<Object> valueobjects = values.getValueOrMultiValueOrValueGroup();
				for (Object valobject : valueobjects) {
					if (VALUE_TYPE.equals(valobject.getClass().getSimpleName())) {
						ValueType value = (ValueType) valobject;
						if (A1359_STR.equals(value.getAttributeID())) {
							configurationsVO.setConfigAttrValId(value.getContent());
							break;
						}
					}
				}
				configurationsVO.setProductTagId(prdID);
				configurationsVO.setConfigAttrId(configAttId);
				materialConfiList.add(configurationsVO);
			}
		}
		return materialConfiList;
	}

	/**
	 * @param objects
	 * @return
	 */
	public List<ConfigurationsVO> getMaterialCategory(List<Object> objects) {

		for (Object object : objects) {
			if (PRODUCT_TYPE.equals(object.getClass().getSimpleName())) {
				ProductType producttype = (ProductType) object;
				if (MATERIAL_CATEGORY.equals(producttype.getUserTypeID())) {
					materialCatConfigList = getMaterialCategoryContents(producttype, materialCatConfigList);
					if (materialConfiList != null) {
						finalConfiglistMaterial.addAll(materialConfiList);
					}
					materialConfiList = new ArrayList<ConfigurationsVO>();
				}
				materialCatConfigList = getMaterialCategory(producttype.getProductOrSequenceProductOrSuppressedProductCrossReference());
			}
		}
		return materialCatConfigList;
	}

}
