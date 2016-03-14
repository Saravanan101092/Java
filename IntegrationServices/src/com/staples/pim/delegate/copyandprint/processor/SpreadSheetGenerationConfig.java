
package com.staples.pim.delegate.copyandprint.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ASSET_CROSS_REFERENCE_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.MATERIAL_CATEGORY;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.METADATA_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.N_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRIMARY_IMAGE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRODUCT_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.VALUES_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.VALUE_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.Y_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.IMAGE_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRODUCT_CROSS_REFERENCE_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.MULTIVALUE_TYPE;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.copyandprint.beans.ComponentPropertiesVO;
import com.staples.pim.delegate.copyandprint.beans.ConfigHierarchyVO;
import com.staples.pim.delegate.copyandprint.beans.ImagesVO;
import com.staples.pim.delegate.copyandprint.beans.MessagingTypeVO;
import com.staples.pim.delegate.copyandprint.beans.SheetListVO;
import com.staples.pcm.stepcontract.beans.AssetCrossReferenceType;
import com.staples.pcm.stepcontract.beans.MetaDataType;
import com.staples.pcm.stepcontract.beans.MultiValueType;
import com.staples.pcm.stepcontract.beans.ProductCrossReferenceType;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

public class SpreadSheetGenerationConfig {

	public List<ConfigHierarchyVO>		thisconfiglistMaterialtype;
	public List<ConfigHierarchyVO>		thisconfiglistMaterial;
	public List<ConfigHierarchyVO>		thisconfiglistMaterialCategory;
	public List<ConfigHierarchyVO>		finalConfiglistMaterial;
	public List<ImagesVO>				imagesList;
	public List<ConfigHierarchyVO>		thisconfiglist					= null;
	// Phase 2
	public List<ComponentPropertiesVO>	componentPropertiesMaterialList;
	public List<MessagingTypeVO>		messagingTypeVOMaterialList;
	public List<String>					cnpMessagingTypeProdIDs			= new ArrayList<String>();

	public final String					A1019_STR						= "A1019";
	public final String					CnPLifecycle_STR				= "CnPLifecycle";
	public final String					A1360_STR						= "A1360";
	public final String					A1359_STR						= "A1359";
	public final String					A1357_STR						= "A1357";
	public final String					A1301_STR						= "A1301";
	public final String					A1302_STR						= "A1302";
	public final String					A1312_STR						= "A1312";
	public final String					A1311_STR						= "A1311";
	public final String					A1313_STR						= "A1313";
	public final String					A1315_STR						= "A1315";

	public final String					A1314_STR						= "A1314";
	public final String					A1316_STR						= "A1316";
	public final String					A1353_STR						= "A1353";

	public final String					A1422_STR						= "A1422";
	public final String					A1424_STR						= "A1424";
	public final String					A1423_STR						= "A1423";
	public final String					TARGETTED_NAME_STR				= "TargettedName";

	// PCMP-2618 Post Phase2 CnP Sprint Changes
	public final String					A1447_STR						= "A1447";
	public final String					A1431_STR						= "A1431";
	public final String					A1432_STR						= "A1432";
	public final String					A1433_STR						= "A1433";
	public final String					A1130_STR						= "A1130";
	public final String					A1349_STR						= "A1349";
	public final String					DCS_ONLY_INDICATOR_STR			= "A1453";
	public final String					THIRD_PARTY_ONLY_INDICATOR		= "A1452";
	/**
	 * Logger initialization
	 */
	static IntgSrvLogger				logger							= IntgSrvLogger
																				.getInstance(DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_CNP);

	SpreadSheetGenerationProduct		objSpreadSheetGenerationProduct	= new SpreadSheetGenerationProduct();

	/**
	 * this method gets the marshalled object of the xml and generates the
	 * SheetListVO object
	 * 
	 * @param productInformation
	 * @return
	 * @throws Exception
	 */
	public SheetListVO getConfigHierarchyList(STEPProductInformation productInformation) throws Exception {

		logger.info(DatamigrationCommonUtil.getClassAndMethodName() + "Initializing lists.");
		thisconfiglistMaterialtype = new ArrayList<ConfigHierarchyVO>();
		finalConfiglistMaterial = new ArrayList<ConfigHierarchyVO>();
		thisconfiglistMaterial = new ArrayList<ConfigHierarchyVO>();
		thisconfiglistMaterialCategory = new ArrayList<ConfigHierarchyVO>();
		imagesList = new ArrayList<ImagesVO>();
		// Phase 2
		componentPropertiesMaterialList = new ArrayList<ComponentPropertiesVO>();
		messagingTypeVOMaterialList = new ArrayList<MessagingTypeVO>();

		try {
			List<ProductType> products = productInformation.getProducts().getProduct();
			getProductTypes(products);
		} catch (Exception e) {
			logger.error(DatamigrationCommonUtil.getClassAndMethodName() + "Caught an exception.." + e);
			e.printStackTrace();
		}
		finalConfiglistMaterial.addAll(thisconfiglistMaterialCategory);
		finalConfiglistMaterial.addAll(thisconfiglistMaterialtype);
		finalConfiglistMaterial.addAll(thisconfiglistMaterial);

		SheetListVO sheetlist = new SheetListVO();
		sheetlist.setConfigHierarchyList(finalConfiglistMaterial);
		sheetlist.setImageList(imagesList);
		sheetlist.setComponentPropertiesList(componentPropertiesMaterialList);
		sheetlist.setMessagingTypeList(messagingTypeVOMaterialList);

		return sheetlist;
	}

	/**
	 * @param productstype
	 * @return
	 */
	public List<ConfigHierarchyVO> getProductTypes(List<ProductType> productstype) {

		List<ConfigHierarchyVO> configlist = null;
		for (ProductType producttype : productstype) {
			configlist = getMaterialCategory(producttype.getProductOrSequenceProductOrSuppressedProductCrossReference(), configlist);
		}
		return configlist;
	}

	/**
	 * This method gets the ProductType object and parses it to fetch the
	 * required details for the ImagesVO object and returns the partially
	 * constructed ImagesVO object with two attributes.
	 * 
	 * @param producttype
	 * @return
	 */
	public ImagesVO getImageVO(ProductType producttype) {

		ImagesVO imagevo = new ImagesVO();
		List<Object> objects = producttype.getProductOrSequenceProductOrSuppressedProductCrossReference();
		for (Object object : objects) {
			if (VALUES_TYPE.equals(object.getClass().getSimpleName())) {
				ValuesType values = (ValuesType) object;
				List<Object> valueobjects = values.getValueOrMultiValueOrValueGroup();
				for (Object valobject : valueobjects) {
					if (VALUE_TYPE.equals(valobject.getClass().getSimpleName())) {
						ValueType value = (ValueType) valobject;
						if (A1019_STR.equals(value.getAttributeID())) {
							imagevo.setEntityId(value.getContent());
						} else if (CnPLifecycle_STR.equals(value.getAttributeID())) {
							imagevo.setEntityType(value.getContent());
						} else if (A1360_STR.equals(value.getAttributeID())) {
							imagevo.setEntityId(value.getContent());
						} else if (A1359_STR.equals(value.getAttributeID())) {
							imagevo.setEntityId(value.getContent());
						}
					}
				}
			}
		}
		return imagevo;
	}

	/**
	 * This method gets the required attributes from the material category level
	 * and call the getMaterialTypeContents() iteratively.
	 * 
	 * @param producttype
	 * @param configlist
	 * @return
	 */
	public List<ConfigHierarchyVO> getMaterialCategoryContents(ProductType producttype, List<ConfigHierarchyVO> configlist) {

		logger.info(DatamigrationCommonUtil.getClassAndMethodName() + " getting material category:" + producttype.getID());
		ConfigHierarchyVO configHierarchyVO = null;
		String parentId = "";
		List<Object> objects = producttype.getProductOrSequenceProductOrSuppressedProductCrossReference();
		for (Object object : objects) {
			if (VALUES_TYPE.equals(object.getClass().getSimpleName())) {
				configHierarchyVO = new ConfigHierarchyVO();
				ValuesType values = (ValuesType) object;
				List<Object> valueobjects = values.getValueOrMultiValueOrValueGroup();
				for (Object valobject : valueobjects) {
					if (VALUE_TYPE.equals(valobject.getClass().getSimpleName())) {
						ValueType value = (ValueType) valobject;
						if (A1019_STR.equals(value.getAttributeID())) {
							parentId = value.getContent();
							configHierarchyVO.setDetailId(parentId);
						} else if (A1357_STR.equals(value.getAttributeID())) {
							configHierarchyVO.setActiveInd(value.getContent());
						} else if (CnPLifecycle_STR.equals(value.getAttributeID())) {
							configHierarchyVO.setCnpLifeCycle(value.getContent());
						} else if (A1301_STR.equals(value.getAttributeID())) {
							configHierarchyVO.setDetailDesc(value.getContent());
						} else if (A1302_STR.equals(value.getAttributeID())) {
							configHierarchyVO.setDispSeq(value.getContent());
						}
					}
				}
			} else if (ASSET_CROSS_REFERENCE_TYPE.equals(object.getClass().getSimpleName())) {
				AssetCrossReferenceType assetCrossRefType = (AssetCrossReferenceType) object;
				ImagesVO imagevo = getImageVO(producttype);
				Map<String, String> metaDataMap = StepOutboundProcessor.getAssetMetaDataValues(assetCrossRefType);

				imagevo.setImageId(assetCrossRefType.getAssetID());
				String imagetype = assetCrossRefType.getType();
				imagevo.setImageType(metaDataMap.get(IMAGE_TYPE));
				if (PRIMARY_IMAGE.equals(imagetype)) {
					imagevo.setDefInd(Y_STR);
				} else {
					imagevo.setDefInd(N_STR);
				}
				imagesList.add(imagevo);
			} else if (PRODUCT_TYPE.equals(object.getClass().getSimpleName())) {
				ProductType product = (ProductType) object;
				getMaterialTypeContents(product, parentId);
			}
		}
		configHierarchyVO.setDetailName(producttype.getName().get(0).getContent());
		configHierarchyVO.setLevelId(1);
		thisconfiglistMaterialCategory.add(configHierarchyVO);
		return null;
	}

	/**
	 * This method gets the required attributes from the material type level and
	 * call the getMaterialContents() iteratively.
	 * 
	 * @param producttype
	 * @param parentid
	 * @return
	 */
	public List<ConfigHierarchyVO> getMaterialTypeContents(ProductType producttype, String parentid) {

		logger.info(DatamigrationCommonUtil.getClassAndMethodName() + " getting material type: " + producttype.getID());
		ConfigHierarchyVO configHierarchyVO = null;
		String Parent = "";
		List<Object> objects = producttype.getProductOrSequenceProductOrSuppressedProductCrossReference();
		for (Object object : objects) {
			if (VALUES_TYPE.equals(object.getClass().getSimpleName())) {
				configHierarchyVO = new ConfigHierarchyVO();
				ValuesType values = (ValuesType) object;
				List<Object> valueobjects = values.getValueOrMultiValueOrValueGroup();
				for (Object valobject : valueobjects) {
					if (VALUE_TYPE.equals(valobject.getClass().getSimpleName())) {
						ValueType value = (ValueType) valobject;
						if (A1360_STR.equals(value.getAttributeID())) {
							Parent = value.getContent();
							configHierarchyVO.setDetailId(value.getContent());
						} else if (A1312_STR.equals(value.getAttributeID())) {
							configHierarchyVO.setHasCostInd(value.getContent());
						} else if (A1357_STR.equals(value.getAttributeID())) {
							configHierarchyVO.setActiveInd(value.getContent());
						} else if (A1311_STR.equals(value.getAttributeID())) {
							configHierarchyVO.setDispInd(value.getContent());
						} else if (A1313_STR.equals(value.getAttributeID())) {
							configHierarchyVO.setDispSeq(value.getContent());
						} else if (A1353_STR.equals(value.getAttributeID())) {
							configHierarchyVO.setDetailDesc(value.getContent());
						} else if (CnPLifecycle_STR.equals(value.getAttributeID())) {
							configHierarchyVO.setCnpLifeCycle(value.getContent());
						}
						// else if ("IsDCS".equals(value.getAttributeID()))
						// {//FIXME
						// configHierarchyVO.setIsDCS(value.getContent());
						// }
					}
				}
			} else if (PRODUCT_TYPE.equals(object.getClass().getSimpleName())) {
				ProductType product = (ProductType) object;
				getMaterialContents(product, configHierarchyVO, Parent);
			} else if (ASSET_CROSS_REFERENCE_TYPE.equals(object.getClass().getSimpleName())) {
				AssetCrossReferenceType assetCrossRefType = (AssetCrossReferenceType) object;
				Map<String, String> metaDataMap = StepOutboundProcessor.getAssetMetaDataValues(assetCrossRefType);

				ImagesVO imagevo = getImageVO(producttype);
				imagevo.setImageId(assetCrossRefType.getAssetID());
				String imagetype = assetCrossRefType.getType();
				imagevo.setImageType(metaDataMap.get(IMAGE_TYPE));
				if (PRIMARY_IMAGE.equals(imagetype)) {
					imagevo.setDefInd(Y_STR);
				} else {
					imagevo.setDefInd(N_STR);
				}
				imagesList.add(imagevo);
			}
		}
		// configHierarchyVO.setDetailDesc(producttype.getName().get(0).getContent());
		configHierarchyVO.setLevelId(2);
		configHierarchyVO.setDetailParentId(parentid);
		configHierarchyVO.setDetailName(producttype.getName().get(0).getContent());
		thisconfiglistMaterialtype.add(configHierarchyVO);
		return thisconfiglistMaterialtype;
	}

	/**
	 * This method gets the required attributes from the material tag.
	 * 
	 * @param producttype
	 * @param configvo
	 * @param parentid
	 * @return
	 */
	public List<ConfigHierarchyVO> getMaterialContents(ProductType producttype, ConfigHierarchyVO configvo, String parentid) {

		logger.info(DatamigrationCommonUtil.getClassAndMethodName() + " getting material: " + producttype.getID());

		ConfigHierarchyVO configHierarchyVO = null;
		String prdID = producttype.getID();
		String prdName = producttype.getName().get(0).getContent();
		String configAttrValId = null;
		// propertyFanIDS[] contains the attribute IDs which are involved in
		// property generation
		// PCMP-2618 Post Phase2 CnP Sprint Changes
		String propertyFanIDS[] = { "A1261", "A1265", "A1113", "A1243", "A1239", "A1145", "A1284", "A1280", "A1304", "A1131", "A1386",
				"A1388", "A1387", "A1410", "A1389", "A1429", "A1110", "A1264", "A1274", "A1108", "A1263", "A1266", "A1111", "A1105",
				"A1275", "A1259", "A1260", "A1267", "A1273", "A1271", "A1272", "A1268", "A1262", "A1283", "A1293", "A1152", "A1282",
				"A1285", "A1150", "A1278", "A1279", "A1286", "A1292", "A1290", "A1291", "A1297", "A1148", "A1298", "A1299", "A1153",
				"A1287", "A1281", "A1242", "A1252", "A1136", "A1256", "A1139", "A1241", "A1244", "A1141", "A1254", "A1237", "A1238",
				"A1245", "A1251", "A1138", "A1249", "A1250", "A1246", "A1240", "A1121", "A1258" };
		List<Object> objects = producttype.getProductOrSequenceProductOrSuppressedProductCrossReference();

		// Only the materials which are not referenced by other materials are
		// considered to populate the Config Hierarchy Tab
		if (!producttype.isReferenced()) {
			configHierarchyVO = new ConfigHierarchyVO();
		}
		for (Object object1 : objects) {
			if (VALUES_TYPE.equals(object1.getClass().getSimpleName())) {
				ValuesType values1 = (ValuesType) object1;
				List<Object> valueobjects1 = values1.getValueOrMultiValueOrValueGroup();
				for (Object valobject1 : valueobjects1) {
					if (VALUE_TYPE.equals(valobject1.getClass().getSimpleName())) {
						ValueType value1 = (ValueType) valobject1;
						if (A1359_STR.equals(value1.getAttributeID())) {
							configAttrValId = value1.getContent();
							break;
						}
					}
				}
			}
		}
		for (Object object : objects) {
			if (VALUES_TYPE.equals(object.getClass().getSimpleName())) {
				ValuesType values = (ValuesType) object;
				List<Object> valueobjects = values.getValueOrMultiValueOrValueGroup();
				for (Object valobject : valueobjects) {
					if (!producttype.isReferenced()) {
						if (VALUE_TYPE.equals(valobject.getClass().getSimpleName())) {
							ValueType value = (ValueType) valobject;
							// Only the materials which are not referenced by
							// other materials are considered to populate the
							// Config Hierarchy Tab
							// if(!producttype.isReferenced()) {
							if (A1359_STR.equals(value.getAttributeID())) {
								configHierarchyVO.setDetailId(value.getContent());
							} else if (A1357_STR.equals(value.getAttributeID())) {
								configHierarchyVO.setActiveInd(value.getContent());
							} else if (CnPLifecycle_STR.equals(value.getAttributeID())) {
								configHierarchyVO.setCnpLifeCycle(value.getContent());
							} else if (A1315_STR.equals(value.getAttributeID())) {
								configHierarchyVO.setCustomQtyInd(value.getContent());
							} else if (A1314_STR.equals(value.getAttributeID())) {
								configHierarchyVO.setDetailDesc(value.getContent());
							} else if (A1316_STR.equals(value.getAttributeID())) {// Phase
																					// 2-Disp
																					// seq
																					// for
																					// material
																					// too
								configHierarchyVO.setDispSeq(value.getContent());
							}
							// PCMP-2618 Post Phase2 CnP Sprint Changes (8 new
							// attributes)
							else if (A1447_STR.equals(value.getAttributeID())) {
								configHierarchyVO.setCustomInputFormat(value.getContent());
							} else if (A1431_STR.equals(value.getAttributeID())) {
								configHierarchyVO.setMeasurementScope(value.getContent());
							} else if (A1432_STR.equals(value.getAttributeID())) {
								configHierarchyVO.setQuantityMultiplier(value.getContent());
							} else if (A1433_STR.equals(value.getAttributeID())) {
								configHierarchyVO.setQuantityDivider(value.getContent());
							} else if (A1130_STR.equals(value.getAttributeID())) {
								configHierarchyVO.setBaseUOM(value.getContent());
							} else if (A1349_STR.equals(value.getAttributeID())) {
								configHierarchyVO.setCustomInputPrompt(value.getContent());
							} else if (DCS_ONLY_INDICATOR_STR.equals(value.getAttributeID())) {
								configHierarchyVO.setDcsOnlyIndicator(value.getContent());
							} else if (THIRD_PARTY_ONLY_INDICATOR.equals(value.getAttributeID())) {
								configHierarchyVO.setThirdPartyOnlyIndicator(value.getContent());
							}
							configHierarchyVO.setProductTagId(prdID);
							configHierarchyVO.setDetailParentId(parentid);
							configHierarchyVO.setLevelId(3);
							configHierarchyVO.setDetailName(producttype.getName().get(0).getContent());

							// Commented as Properties made as LOV
							// else if (value.getAttributeID().contains("_")) {
							// ComponentPropertiesVO componentPropertiesVO=new
							// ComponentPropertiesVO();
							// componentPropertiesVO.setConfigAttrValId(configAttrValId);
							// componentPropertiesVO.setConfigAttrValName(prdName);
							// componentPropertiesVO.setPropertyName(value.getAttributeID().split("\\_")[1]);
							// componentPropertiesVO.setPropertyValue(value.getContent());
							// componentPropertiesMaterialList.add(componentPropertiesVO);
							// }

							for (String fanID : propertyFanIDS) {
								if (fanID.equalsIgnoreCase(value.getAttributeID())) {
									ComponentPropertiesVO componentPropertiesVO = new ComponentPropertiesVO();
									componentPropertiesVO.setConfigAttrValId(configAttrValId);
									componentPropertiesVO.setConfigAttrValName(prdName);
									String propName = DatamigrationCommonUtil.getValuesFromLOV("ComponentProperties",
											value.getAttributeID(), Boolean.FALSE);
									if (!IntgSrvUtils.isNullOrEmpty(propName)) {
										componentPropertiesVO.setPropertyName(propName.split(":")[1]);
									}
									componentPropertiesVO.setPropertyValue(value.getContent());
									// PCMP-2618 Post Phase2 CnP Sprint Changes
									componentPropertiesVO.setPropertyFanId(value.getAttributeID());
									componentPropertiesMaterialList.add(componentPropertiesVO);
									break;
								}
							}
						}
						if (MULTIVALUE_TYPE.equals(valobject.getClass().getSimpleName())) {
							MultiValueType multiValueObj = (MultiValueType) valobject;
							for (String fanID : propertyFanIDS) {
								if (fanID.equalsIgnoreCase(multiValueObj.getAttributeID())) {
									int flag = 0;
									ComponentPropertiesVO componentPropertiesVO = new ComponentPropertiesVO();
									componentPropertiesVO.setConfigAttrValId(configAttrValId);
									componentPropertiesVO.setConfigAttrValName(prdName);
									// componentPropertiesVO.setPropertyName(multiValueObj.getAttributeID().split("_")[1]);
									String propName = DatamigrationCommonUtil.getValuesFromLOV("ComponentProperties",
											multiValueObj.getAttributeID(), Boolean.FALSE);
									if (!IntgSrvUtils.isNullOrEmpty(propName)) {
										componentPropertiesVO.setPropertyName(propName.split(":")[1]);
									}
									for (Object obj : multiValueObj.getValueOrValueGroup()) {
										flag += 1;
										ValueType valueTypeObj = (ValueType) obj;
										if (!IntgSrvUtils.isNullOrEmpty(valueTypeObj.getContent())) {
											if (flag > 1) {
												componentPropertiesVO.setPropertyValue(componentPropertiesVO.getPropertyValue() + ":"
														+ valueTypeObj.getContent());
											} else {
												componentPropertiesVO.setPropertyValue(valueTypeObj.getContent());
											}
										}
									}
									// PCMP-2618 Post Phase2 CnP Sprint Changes
									componentPropertiesVO.setPropertyFanId(multiValueObj.getAttributeID());
									componentPropertiesMaterialList.add(componentPropertiesVO);
									break;
								}
							}

						}
					}
				}
				if (configHierarchyVO != null) {
					thisconfiglistMaterial.add(configHierarchyVO);
				}

			} else if (ASSET_CROSS_REFERENCE_TYPE.equals(object.getClass().getSimpleName())) {
				AssetCrossReferenceType assetCrossRefType = (AssetCrossReferenceType) object;
				ImagesVO imagevo = getImageVO(producttype);

				Map<String, String> metaDataMap = StepOutboundProcessor.getAssetMetaDataValues(assetCrossRefType);
				imagevo.setImageId(assetCrossRefType.getAssetID());
				String imagetype = assetCrossRefType.getType();
				imagevo.setImageType(metaDataMap.get(IMAGE_TYPE));
				if (PRIMARY_IMAGE.equals(imagetype)) {
					imagevo.setDefInd(Y_STR);
				} else {
					imagevo.setDefInd(N_STR);
				}

				imagesList.add(imagevo);
			} else if (PRODUCT_CROSS_REFERENCE_TYPE.equals(object.getClass().getSimpleName())) {
				ProductCrossReferenceType productCrossObj = (ProductCrossReferenceType) object;
				if (productCrossObj.getType().equalsIgnoreCase("CnPMessagingType")) {
					cnpMessagingTypeProdIDs.add(prdID);
					MessagingTypeVO messagingTypeVO = new MessagingTypeVO();
					List<Object> objs = productCrossObj.getMetaDataOrProduct();
					Map<String, String> allMDValuesMap = new HashMap<String, String>();
					getUpsellDetails(objs, allMDValuesMap);
					messagingTypeVO.setConfigAttrValId(configAttrValId);
					messagingTypeVO.setMessageType(allMDValuesMap.get(A1422_STR));
					messagingTypeVO.setMsgDescription(allMDValuesMap.get(A1424_STR));
					messagingTypeVO.setmsgTitle(allMDValuesMap.get(A1423_STR));// FIXME
					messagingTypeVO.setTargetedValName(allMDValuesMap.get(TARGETTED_NAME_STR));
					// PCMP-2235 upsell messages can be tagged to material type
					// too in some cases. In such scenarios, the id need to be
					// taken from A1360 instead of A1359
					if (allMDValuesMap.get(A1359_STR) != null) {
						messagingTypeVO.setTargetedValId(allMDValuesMap.get(A1359_STR));
					} else {
						messagingTypeVO.setTargetedValId(allMDValuesMap.get(A1360_STR));
					}
					messagingTypeVO.setConfigAttrValName(prdName);
					messagingTypeVOMaterialList.add(messagingTypeVO);
				}
			}
		}
		return thisconfiglistMaterial;
	}

	public Map<String, String> getUpsellDetails(List<Object> object, Map<String, String> allMDValuesMap) {

		for (Object obj : object) {
			if (METADATA_TYPE.equalsIgnoreCase(obj.getClass().getSimpleName())) {
				MetaDataType metaDataRef = (MetaDataType) obj;
				List<Object> valueObjs = metaDataRef.getValueOrMultiValueOrValueGroup();
				for (Object values : valueObjs) {
					if (VALUE_TYPE.equalsIgnoreCase(values.getClass().getSimpleName())) {
						ValueType valueTypeObj = (ValueType) values;
						allMDValuesMap.put(valueTypeObj.getAttributeID(), valueTypeObj.getContent());
					}

				}
			}
			if (PRODUCT_TYPE.equalsIgnoreCase(obj.getClass().getSimpleName())) {
				ProductType objProductType = (ProductType) obj;
				allMDValuesMap.put(TARGETTED_NAME_STR, objProductType.getName().get(0).getContent());
				allMDValuesMap = getUpsellDetails(objProductType.getProductOrSequenceProductOrSuppressedProductCrossReference(),
						allMDValuesMap);
			}
			if (VALUES_TYPE.equals(obj.getClass().getSimpleName())) {
				ValuesType values = (ValuesType) obj;
				List<Object> valueobjects = values.getValueOrMultiValueOrValueGroup();
				for (Object valobject : valueobjects) {
					if (VALUE_TYPE.equalsIgnoreCase(valobject.getClass().getSimpleName())) {
						ValueType valueTypeObj = (ValueType) valobject;
						allMDValuesMap.put(valueTypeObj.getAttributeID(), valueTypeObj.getContent());
					}
				}
			}

		}
		return allMDValuesMap;
	}

	/**
	 * This method gets the contents of the material tag by calling the
	 * getMaterialCategory() method iteratively.
	 * 
	 * @param objects
	 * @param configlist
	 * @return
	 */
	public List<ConfigHierarchyVO> getMaterialCategory(List<Object> objects, List<ConfigHierarchyVO> configlist) {

		List<ConfigHierarchyVO> thisconfiglist = configlist;
		for (Object object : objects) {
			if (PRODUCT_TYPE.equals(object.getClass().getSimpleName())) {
				ProductType producttype = (ProductType) object;
				if (MATERIAL_CATEGORY.equals(producttype.getUserTypeID())) {
					thisconfiglist = getMaterialCategoryContents(producttype, thisconfiglist);
				}
				thisconfiglist = getMaterialCategory(producttype.getProductOrSequenceProductOrSuppressedProductCrossReference(),
						thisconfiglist);
			}
		}
		return thisconfiglist;
	}

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		// get the xml file to be parsed
		File fileIn = new File("C:/Users/843868/Desktop/Step_XML_Export_28012015/MATERIAL.xml");
		try {
			// Unmarshall the xml and get the data in STEPProductInformation
			// object
			STEPProductInformation product = null;
			JAXBContext jaxbContext = JAXBContext.newInstance("com.staples.pcm.stepcontract.beans");
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			product = (STEPProductInformation) jaxbUnmarshaller.unmarshal(fileIn);
			SpreadSheetGenerationConfig step = new SpreadSheetGenerationConfig();
			step.getConfigHierarchyList(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
