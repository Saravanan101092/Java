
package com.staples.pim.delegate.copyandprint.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CATEGORY;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRODUCTTYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.STYLE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.NAME_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.ASSET_CROSS_REFERENCE_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CLASSIFICATION_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.Y_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.N_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.METADATA_TYPE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRIMARY_IMAGE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.LEVEL_ONE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.LEVEL_TWO;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.LEVEL_THREE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.IMAGE_TYPE; //Phase 2 changes
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EPRICEBOOKHIERARCHY;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.SOLUTIONBUILDERHIERARCHY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.staples.pim.delegate.copyandprint.beans.ImagesVO;
import com.staples.pim.delegate.copyandprint.beans.SheetListVO;
import com.staples.pim.delegate.copyandprint.beans.WebHierarchyVO;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pcm.stepcontract.beans.AssetCrossReferenceType;
import com.staples.pcm.stepcontract.beans.ClassificationType;
import com.staples.pcm.stepcontract.beans.ClassificationsType;
import com.staples.pcm.stepcontract.beans.MetaDataType;
import com.staples.pcm.stepcontract.beans.NameType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;

/**
 * @author 522462
 * 
 */
public class SpreadSheetGenerationWeb {

	int					levelCount			= 0;
	static String		previousDetailId	= "";
	public final String	A1007_STR			= "A1007";
	public final String	A1013_STR			= "A1013";
	public final String	A1012_STR			= "A1012";
	public final String	A1355_STR			= "A1355";
	public final String	A1354_STR			= "A1354";
	public final String	A1018_STR			= "A1018";
	public final String	A1004_STR			= "A1004";
	public final String	A1010_STR			= "A1010";
	public final String	A1016_STR			= "A1016";
	public final String	A1428_STR			= "A1428";
	public final String	A1425_STR			= "A1425";
	public final String	A1426_STR			= "A1426";
	public final String	A1427_STR			= "A1427";

	// Phase 2 changes
	// public String hierarchyID=null;
	/**
	 * This method returns the final sheetlistVO
	 * @param stepXMLStr
	 * @return
	 */
	public SheetListVO getWebHierarchyList(STEPProductInformation product) {

		SheetListVO sheetListVO = new SheetListVO();
		sheetListVO.setWebHierarchyList(new ArrayList<WebHierarchyVO>());
		sheetListVO.setImageList(new ArrayList<ImagesVO>());
		previousDetailId = "";
		try {
			ClassificationsType classficationType = product.getClassifications();
			for (ClassificationType clsTypeObj : classficationType.getClassification()) {
				// Phase 2 changes-extra parameter hierarchyId
				sheetListVO = getWebHierarchyClassficationsList(clsTypeObj, sheetListVO, getWebHierarchyID(clsTypeObj.getUserTypeID()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sheetListVO;
	}

	/**
	 * @param clsTypeObj
	 * @param hierarchyID
	 * @param webHierarchyList
	 * @param imageList
	 * @return
	 */
	public SheetListVO getWebHierarchyClassficationsList(ClassificationType clsTypeObj, SheetListVO sheetListVO, String hierarchyID) {

		// Hierarchy Detail ID =
		// Classifications/Classification/Classification/@ID
		String detailId = null;
		// Hierarchy Detail Name =
		// Classifications/Classification/Classification/Name
		String detailName = null;
		// Hierarchy Detail Short Desc =
		// Classifications/Classification/Classification/MetaData/Value/@AttributeID
		// value of A1004
		String detailDesc = null;
		// Hierarchy Detail Display Ind TBD
		String detailDispInd = null;
		// Hierarchy Level ID = 1 - category 2-Producttype 3- Style
		Integer levelId = null;
		// Hierarchy Level Name
		// =Classifications/Classification/Classification/@UserTypeID
		String levelName = null;
		// Hierarchy Detail Parent Id =
		// Classifications/Classification/Classification/@ID of Previous level
		String detailParentId = null;

		String longDesc=null;
		
		String dispSeq=null;
		// Phase 2 changes
		// String temp=null;

		// detailId = clsTypeObj.getID();
		detailParentId = previousDetailId;
		// clsTypeObj.getParentID();

		levelId = getWebHierarchyLevelID(clsTypeObj.getUserTypeID());
		levelName = getWebHierarchyLevelName(clsTypeObj.getUserTypeID());
		// Phase 2 changes
		// temp=getWebHierarchyID(clsTypeObj.getUserTypeID());
		// if(temp!=null)
		// {
		// hierarchyID=temp;
		// }

		// Map<String, String> assetCrossMap = new HashMap<String, String>();
		ArrayList<ImagesVO> imageList = new ArrayList<ImagesVO>();
		for (Object nameAttSeqList : clsTypeObj.getNameOrAttributeLinkOrSequenceProduct()) {

			if (NAME_TYPE.equalsIgnoreCase(nameAttSeqList.getClass().getSimpleName())) {

				detailName = ((NameType) nameAttSeqList).getContent();
				// detailDesc = ((NameType)nameAttSeqList).getContent();
			} else if (CLASSIFICATION_TYPE.equalsIgnoreCase(nameAttSeqList.getClass().getSimpleName())) {

				ClassificationType classType = (ClassificationType) nameAttSeqList;
				// Phase 2 changes-extra parameter hierarchyId
				getWebHierarchyClassficationsList(classType, sheetListVO, hierarchyID);
			} else if (ASSET_CROSS_REFERENCE_TYPE.equalsIgnoreCase(nameAttSeqList.getClass().getSimpleName())) {
				// Fix for issue # CP-4.. Asset detail is restricted for
				// ePriceBookHierarchy
				if (!clsTypeObj.getUserTypeID().equalsIgnoreCase(DatamigrationAppConstants.EPRICEBOOKHIERARCHY)) {
					AssetCrossReferenceType assCrossRefObj = (AssetCrossReferenceType) nameAttSeqList;
					ImagesVO imgVo = new ImagesVO();
					imgVo.setImageId(assCrossRefObj.getAssetID());

					Map<String, String> assetMap = StepOutboundProcessor.getAssetMetaDataValues(assCrossRefObj);
					if (assetMap != null) {
						imgVo.setImageType(assetMap.get(IMAGE_TYPE));
					}

					String defInd = PRIMARY_IMAGE.equalsIgnoreCase(assCrossRefObj.getType()) ? Y_STR : N_STR;
					imgVo.setDefInd(defInd);
					imageList.add(imgVo);
					// assetCrossMap.put(assCrossRefObj.getAssetID(),
					// assCrossRefObj.getType());
				}
			} else if (METADATA_TYPE.equalsIgnoreCase(nameAttSeqList.getClass().getSimpleName())) {

				DatamigrationCommonUtil.printConsole("<=== MetaDataType ===> ");
				Map<String, String> metaDataMap = getWebMetaDataValues(((MetaDataType) nameAttSeqList).getValueOrMultiValueOrValueGroup());
				DatamigrationCommonUtil.printConsole("Meta Data:" + metaDataMap);
				detailDispInd = getWebDetailDispInd(metaDataMap, levelId);
				detailId = getWebDetailId(metaDataMap, levelId);
				detailDesc = getWebDetailsDesc(metaDataMap, levelId);
				longDesc=metaDataMap.get(A1428_STR);//FIXME fan id to be given
				dispSeq=getWebDispSeq(metaDataMap, levelId);
				// we can get the asset cross reference value from assetCrossMap
				// for(Entry<String,String> keyVal : assetCrossMap.entrySet()){
				for (ImagesVO imgVO : imageList) {
					// ImageVO constructor arguments are entityId, entityType,
					// imageId, imageType, defInd

					// ImagesVO imgVo =new ImagesVO(detailId, "WEB HIERARCHY",
					// imgVO.getImageId(), imgVO.getImageType(), defInd);
					ImagesVO imgVo = new ImagesVO(detailId, levelName, imgVO.getImageId(), imgVO.getImageType(), imgVO.getDefInd());
					sheetListVO.getImageList().add(imgVo);
				}
				if (!clsTypeObj.getUserTypeID().equalsIgnoreCase(DatamigrationAppConstants.EPRICEBOOKHIERARCHY) && !clsTypeObj.getUserTypeID().equalsIgnoreCase(DatamigrationAppConstants.SOLUTIONBUILDERHIERARCHY)) {
					// Phase 2 changes-extra parameter hierarchyId
					WebHierarchyVO obj = new WebHierarchyVO(detailId, detailName, detailDesc, longDesc,dispSeq,detailDispInd, levelId, levelName,
							detailParentId, hierarchyID);
					sheetListVO.getWebHierarchyList().add(obj);
				}
			}
			previousDetailId = detailId;
		}
		return sheetListVO;
	}

	// Phase 2 changes-extra column "Hierarchy ID" in Web Hierarchy sheet
	/**
	 * @param hierarchyID
	 * @return
	 */
	private String getWebHierarchyID(String hierarchyID) {

		if (EPRICEBOOKHIERARCHY.equalsIgnoreCase(hierarchyID)) {
			return DatamigrationAppConstants.EPRICEBOOK;
		} else if (SOLUTIONBUILDERHIERARCHY.equalsIgnoreCase(hierarchyID)) {
			return DatamigrationAppConstants.SOLUTIONBUILDER;
		}
		return null;
	}

	/**
	 * To get the Detail ID
	 * @param metaDataMap
	 * @param levelId
	 * @return
	 */
	private String getWebDetailId(Map<String, String> metaDataMap, Integer levelId) {

		if (levelId == LEVEL_ONE) {
			return metaDataMap.get(A1007_STR);
		} else if (levelId == LEVEL_TWO) {
			return metaDataMap.get(A1013_STR);
		} else if (levelId == LEVEL_THREE) {
			return metaDataMap.get(A1012_STR);
		}
		return null;
	}

	/**
	 * To get the Detail Disp Ind
	 * @param metaDataMap
	 * @param levelId
	 * @return
	 */
	private String getWebDetailDispInd(Map<String, String> metaDataMap,
			Integer levelId) {
		// Comment removed for Category and Product for PCMP-2234 as part of
		// Phase 2
		if ((levelId == LEVEL_ONE && Y_STR
				.equalsIgnoreCase(DatamigrationCommonUtil
						.converYESNOIntoChar((metaDataMap.get(A1355_STR)))))
				|| (levelId == LEVEL_TWO && Y_STR
						.equalsIgnoreCase(DatamigrationCommonUtil
								.converYESNOIntoChar((metaDataMap
										.get(A1354_STR)))))
				|| (levelId == LEVEL_THREE && Y_STR
						.equalsIgnoreCase(DatamigrationCommonUtil
								.converYESNOIntoChar(metaDataMap.get(A1018_STR))))) {
			return Y_STR;
		}
		return N_STR;
	}

	/**
	 * To get the Detail Description
	 * @param metaDataMap
	 * @param levelId
	 * @return
	 */
	private String getWebDetailsDesc(Map<String, String> metaDataMap, Integer levelId) {

		if (levelId == LEVEL_ONE) {
			return metaDataMap.get(A1004_STR);
		} else if (levelId == LEVEL_TWO) {
			return metaDataMap.get(A1010_STR);
		} else if (levelId == LEVEL_THREE) {
			return metaDataMap.get(A1016_STR);
		}
		return null;
	}

	/**
	 * To get the Level Id
	 * @param levelName
	 * @return
	 */
	private static Integer getWebHierarchyLevelID(String levelName) {

		if (CATEGORY.equalsIgnoreCase(levelName) || DatamigrationAppConstants.SBCATEGORY.equalsIgnoreCase(levelName)) {
			return LEVEL_ONE;
		} else if (PRODUCTTYPE.equalsIgnoreCase(levelName) || DatamigrationAppConstants.SBPRODUCT.equalsIgnoreCase(levelName)) {
			return LEVEL_TWO;
		} else if (STYLE.equalsIgnoreCase(levelName) || DatamigrationAppConstants.SBSTYLE.equalsIgnoreCase(levelName)) {
			return LEVEL_THREE;
		}
		return 0;
	}

	/**
	 * To get the Level Name
	 * @param levelName
	 * @return
	 */
	private String getWebHierarchyLevelName(String levelName) {

		if (CATEGORY.equalsIgnoreCase(levelName) || DatamigrationAppConstants.SBCATEGORY.equalsIgnoreCase(levelName)) {
			return "CATEGORY";
		} else if (PRODUCTTYPE.equalsIgnoreCase(levelName) || DatamigrationAppConstants.SBPRODUCT.equalsIgnoreCase(levelName)) {
			return "PRODUCT";
		} else if (STYLE.equalsIgnoreCase(levelName) || DatamigrationAppConstants.SBSTYLE.equalsIgnoreCase(levelName)) {
			return "STYLE";
		}
		return null;
	}
	
	/**
	 * @param metaDataMap
	 * @param levelId
	 * @return
	 */
	private String getWebDispSeq(Map<String, String> metaDataMap, Integer levelId) {

		if (levelId == LEVEL_ONE) {
			return metaDataMap.get(A1425_STR);
		} else if (levelId == LEVEL_TWO) {
			return metaDataMap.get(A1426_STR);
		} else if (levelId == LEVEL_THREE) {
			return metaDataMap.get(A1427_STR);
		}
		return null;
	}

	/**
	 * To get the values under Metadata tag
	 * @param valueOrMultiValueOrValueGroup
	 * @return
	 */
	private static Map<String, String> getWebMetaDataValues(List<Object> valueOrMultiValueOrValueGroup) {

		Map<String, String> metaDataMap = new HashMap<String, String>();
		for (Object values : valueOrMultiValueOrValueGroup) {
			ValueType valueTypeObj = (ValueType) values;
			metaDataMap.put(valueTypeObj.getAttributeID(), valueTypeObj.getContent());
		}
		return metaDataMap;
	}

}
