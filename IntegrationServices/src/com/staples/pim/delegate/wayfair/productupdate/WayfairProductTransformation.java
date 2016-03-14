
package com.staples.pim.delegate.wayfair.productupdate;

import java.util.Map;

import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

/**
 * @author 522462
 *
 */
public class WayfairProductTransformation {

	public static final String FULL_DESCRIPTION="REFER VENDOR DESCRIPTION-A0076";
	public static final String WAYFAIR_LLC ="WAYFAIR LLC";
	public static final String STANDARD ="Standard";
	/**
	 * @param attributeMap
	 * @return
	 */
	public static Map<String, String> transformationForProduct(Map<String, String> attributeMap) {

		attributeMap = setDefaultValues(attributeMap);

		attributeMap = lovTransformations(attributeMap);
		
//		attributeMap = longDiscriptionTransformation(attributeMap);

		attributeMap = setIsActiveStatus(attributeMap);

		attributeMap = channelAttrTransformations(attributeMap);

		attributeMap = removeUnwantedAttributes(attributeMap);

		return attributeMap;
	}
	
	/**
	 * @param attributeMap
	 * @return
	 */
	public static Map<String, String> transformationForPricing(Map<String, String> attributeMap) {


		attributeMap = lovTransformations(attributeMap);

		attributeMap = channelAttrTransformations(attributeMap);

		attributeMap = removeUnwantedAttributes(attributeMap);

		return attributeMap;
	}

	/**
	 * @param attributeMap
	 * @return
	 */
	private static Map<String, String> removeUnwantedAttributes(Map<String, String> attributeMap) {

		// Values are moved to Channel specific attribute, so remove the
		// unwanted attributes
		attributeMap.remove("A0013");
		attributeMap.remove("A0018");
		attributeMap.remove("A0046");
		attributeMap.remove("A0067");
		attributeMap.remove("A0077");
		attributeMap.remove("A0078");
		//PCMP-2449
		attributeMap.remove("D0052");
		attributeMap.remove("D0051");
		attributeMap.remove("D0050");
		attributeMap.remove("A0564");
		attributeMap.remove("A0561");
		attributeMap.remove("A0560");
		attributeMap.remove("A0562");
		attributeMap.remove("D0053");
	
		

		return attributeMap;
	}

	/**
	 * @param attributeMap
	 * @return
	 */
	private static Map<String, String> channelAttrTransformations(Map<String, String> attributeMap) {

		if (attributeMap.get("A0013") != null && !IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0013"))) {
			attributeMap.put("A0013_RET", attributeMap.get("A0013"));
			attributeMap.put("A0013_NAD", attributeMap.get("A0013"));
		}
		if (attributeMap.get("A0018") != null && !IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0018"))) {
			attributeMap.put("A0018_RET", attributeMap.get("A0018"));
			attributeMap.put("A0018_NAD", attributeMap.get("A0018"));
		}
		if (attributeMap.get("A0046") != null && !IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0046"))) {
			attributeMap.put("A0046_RET", attributeMap.get("A0046"));
			attributeMap.put("A0046_NAD", attributeMap.get("A0046"));
		}
		if (attributeMap.get("A0067") != null && !IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0067"))) {
			attributeMap.put("A0067_RET", attributeMap.get("A0067"));
			attributeMap.put("A0067_NAD", attributeMap.get("A0067"));
		}
		if (attributeMap.get("A0077") != null && !IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0077"))) {
			attributeMap.put("A0077_RET", attributeMap.get("A0077"));
			attributeMap.put("A0077_NAD", attributeMap.get("A0077"));
		}
		if (attributeMap.get("A0078") != null && !IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0078"))) {
			attributeMap.put("A0078_RET", attributeMap.get("A0078"));
			attributeMap.put("A0078_NAD", attributeMap.get("A0078"));
		}

		return attributeMap;
	}

	/**
	 * @param attributeMap
	 * @return
	 */
	private static Map<String, String> setIsActiveStatus(Map<String, String> attributeMap) {

		String activeStatus = attributeMap.get("A0484");

		if (activeStatus!=null && DatamigrationAppConstants.ACTION_CODE_N.equalsIgnoreCase(activeStatus.trim())
				|| DatamigrationAppConstants.ACTION_CODE_U.equalsIgnoreCase(activeStatus)) {
			attributeMap.put("A0617", DatamigrationAppConstants.Y_STR);
		} else if (DatamigrationAppConstants.ACTION_CODE_D.equalsIgnoreCase(activeStatus)) {
			attributeMap.put("A0617", DatamigrationAppConstants.N_STR);
		}

		return attributeMap;
	}

	/**
	 * @param attributeMap
	 * @return
	 */
	private static Map<String, String> lovTransformations(Map<String, String> attributeMap) {

		if (attributeMap.get("A0210") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0210", attributeMap.get("A0210"), Boolean.TRUE);
			attributeMap.put("A0210", value);
		}

		if (attributeMap.get("A0212") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0212", attributeMap.get("A0212"), Boolean.TRUE);
			attributeMap.put("A0212", value);
		}
		
		if (attributeMap.get("A0171") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0171", attributeMap.get("A0171"), Boolean.TRUE);
			attributeMap.put("A0171", value);
		}
		
		if (attributeMap.get("A0172") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0172", attributeMap.get("A0172"), Boolean.TRUE);
			attributeMap.put("A0172", value);
		}

		if (attributeMap.get("A0146") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0146", attributeMap.get("A0146"), Boolean.TRUE);
			attributeMap.put("A0146", value);
		}
		if (attributeMap.get("A0144") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0144", attributeMap.get("A0144"), Boolean.TRUE);
			attributeMap.put("A0144", value);
		}
		if (attributeMap.get("A0197") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0197", attributeMap.get("A0197"), Boolean.TRUE);
			attributeMap.put("A0197", value);
		}

		//PCMP-2472 country code lov issue
		if (attributeMap.get("A0085") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0085", attributeMap.get("A0085"), Boolean.TRUE);
			attributeMap.put("A0085", value);
		}
		
		if (attributeMap.get("A0086") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0086", attributeMap.get("A0086"), Boolean.TRUE);
			attributeMap.put("A0086", value);
		}
		
		if (attributeMap.get("A0087") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0086", attributeMap.get("A0087"), Boolean.TRUE);
			attributeMap.put("A0087", value);
		}
		
		if (attributeMap.get("A0088") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0086", attributeMap.get("A0088"), Boolean.TRUE);
			attributeMap.put("A0088", value);
		}
		
		if (attributeMap.get("A0089") != null) {
			String value = DatamigrationCommonUtil.getValuesFromLOV("A0086", attributeMap.get("A0089"), Boolean.TRUE);
			attributeMap.put("A0089", value);
		}
		
		return attributeMap;
	}

	/**
	 * @param attributeMap
	 * @return
	 */
	private static Map<String, String> setDefaultValues(Map<String, String> attributeMap) {

		// WayFair Vendor details
		attributeMap.put("A0075_RET", "142191");

		attributeMap.put("A0075_NAD", "142191");

		attributeMap.put("A0302", WAYFAIR_LLC);
		
		attributeMap.put("A0018", FULL_DESCRIPTION);
		//WayFair product Item type
		attributeMap.put("A0002", STANDARD);

		if (attributeMap.get("A0173") != null) {

			attributeMap.put("A0173", DatamigrationCommonUtil.converYESNOIntoChar(attributeMap.get("A0173")));

		}
		if (attributeMap.get("D1137") != null) {
			if ("Y".equalsIgnoreCase(attributeMap.get("D1137"))) {
				attributeMap.put("D1137", "Yes");
			}
			if ("N".equalsIgnoreCase(attributeMap.get("D1137"))) {
				attributeMap.put("D1137", "No");
			}
		}

		return attributeMap;
	}
}
