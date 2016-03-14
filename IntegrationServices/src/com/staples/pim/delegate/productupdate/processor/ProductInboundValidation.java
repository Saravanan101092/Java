
package com.staples.pim.delegate.productupdate.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EMPTY_STR;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PRODUCT_VALIDATIONS_ENABLE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.RULE_ID_STR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

/**
 * @author 843881
 * 
 */
public class ProductInboundValidation {

	/**
	 * To check if a given value is within specified range
	 * 
	 * @param value
	 *            -actual value
	 * @param min
	 *            -minimum value
	 * @param max
	 *            -maximum value
	 * @return
	 */
	public boolean isMinMaxCheck(String value, String min, String max) {

		if (IntgSrvUtils.isDouble(value) && Double.parseDouble(value) <= Double.parseDouble(max)
				&& Double.parseDouble(value) >= Double.parseDouble(min)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * To check if a given string is free of quotes
	 * 
	 * @param str
	 *            -The string to be checked
	 * @return
	 */
	public boolean isQuotesFree(String str) {

		if (str.contains("\"") || str.contains("\'")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * To check if a given value is non-negative
	 * 
	 * @param str
	 *            -The value to be checked
	 * @return
	 */
	public boolean isNonNegative(String str) {

		if (IntgSrvUtils.isDouble(str) && Double.parseDouble(str) >= 0.0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * To check if a given value is greater than zero
	 * 
	 * @param str
	 *            -The value to be checked
	 * @return
	 */
	public static boolean isGreaterThanZero(String str) {

		if (!IntgSrvUtils.isNullOrEmpty(str) && IntgSrvUtils.toDouble(str) > 0.0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * To check if a given string does not contain NA(NA, N/A, N.A., N.A or N-A)
	 * values
	 * 
	 * @param str
	 * @return
	 */
	public boolean isNAValue(String str) {

		if ((str != null)
				&& (str.equalsIgnoreCase("NA") || str.equalsIgnoreCase("N.A.") || str.equalsIgnoreCase("N.A")
						|| str.equalsIgnoreCase("N-A") || str.equalsIgnoreCase("N/A"))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sell Unit UPC and Inner Pack UPC and Master Case Pack UPC and Pallet UPC
	 * can not be the same, unless they are all N/A.
	 * 
	 * @param SellUnitUPC
	 * @param InnerPackUPC
	 * @param MasterCasePackUPC
	 * @param PalletUPC
	 * @return
	 */
	public boolean masterCasePackUPCValidation(String SellUnitUPC, String InnerPackUPC, String MasterCasePackUPC, String PalletUPC) {

		if ((isNAValue(SellUnitUPC) && isNAValue(InnerPackUPC) && isNAValue(MasterCasePackUPC) && isNAValue(PalletUPC))
				|| (IntgSrvUtils.isNullOrEmpty(SellUnitUPC) && IntgSrvUtils.isNullOrEmpty(InnerPackUPC)
						&& IntgSrvUtils.isNullOrEmpty(MasterCasePackUPC) && IntgSrvUtils.isNullOrEmpty(PalletUPC))) {
			return true;
		} else {
			if (SellUnitUPC.equalsIgnoreCase(InnerPackUPC) && InnerPackUPC.equalsIgnoreCase(MasterCasePackUPC)
					&& MasterCasePackUPC.equalsIgnoreCase(PalletUPC)) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * To check if a given value's length is not greater than maximum length
	 * 
	 * @param str
	 *            -The value to be checked
	 * @param length
	 *            -The maximum length
	 * @return
	 */
	public boolean isNotGreaterThanMaxLength(String str, int length) {

		str = str.replaceAll("[^\\w\\s]", "");
		if (str.length() <= length) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * To check if a given value has the exact specified length
	 * 
	 * @param str
	 *            -The value to be checked
	 * @param length
	 *            -The exact length
	 * @return
	 */
	public boolean isExactLength(String str, int length) {

		str = str.replaceAll("[^\\w\\s]", "");
		if (!IntgSrvUtils.isNullOrEmpty(str) && str.length() == length) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * To check if a given value is decimal
	 * 
	 * @param str
	 *            -The value to be checked
	 * @return
	 */
	public boolean isDecimal(String str) {

		if (Double.parseDouble(str) % 1 != 0) {

			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param inDate
	 * @return
	 */
	public boolean isValidDate(String inDate) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	/**
	 * This method is used to check the validation rules. On failure of any
	 * validation rule, appropriate error message is captured
	 * 
	 * @param attributeMap
	 * @return error message
	 * @throws ParseException
	 */
	public String validationProcess(Map<String, String> attributeMap) throws ParseException {

		String errorMsg = "";
		removeUnwantedAttributes(attributeMap);

		// DatamigrationCommonUtil.printConsole("PCF data validation enable status is :"+IntgSrvPropertiesReader.getProperty(PRODUCT_VALIDATIONS_ENABLE));

		if ("true".equalsIgnoreCase(IntgSrvPropertiesReader.getProperty(PRODUCT_VALIDATIONS_ENABLE))) {

			if (attributeMap.containsKey("A0012"))
				errorMsg += validateA0012(attributeMap.get("A0012"));
			else
				errorMsg += RULE_ID_STR + " 7048-The attribute A0012 is not present in input";

			if (attributeMap.containsKey("A0013_RET"))
				errorMsg += validateA0013RET(attributeMap.get("A0013_RET"));
			else
				errorMsg += RULE_ID_STR + " 7053-The attribute A0013_RET is not present in input";

			if (attributeMap.containsKey("A0013_NAD"))
				errorMsg += validateA0013NAD(attributeMap.get("A0013_NAD"));
			else
				errorMsg += RULE_ID_STR + " 7053-The attribute A0013_NAD is not present in input";

			if (attributeMap.containsKey("A0018_RET"))
				errorMsg += validateA0018RET(attributeMap.get("A0018_RET"));
			else
				errorMsg += RULE_ID_STR + " 7092-The attribute A0018_RET is not present in input";

			if (attributeMap.containsKey("A0018_NAD"))
				errorMsg += validateA0018NAD(attributeMap.get("A0018_NAD"));
			else
				errorMsg += RULE_ID_STR + " 7092-The attribute A0018_NAD is not present in input";

			if (attributeMap.containsKey("A0026"))
				errorMsg += validateA0026(attributeMap.get("A0026"));

			if (attributeMap.containsKey("A0030"))
				errorMsg += validateA0030(attributeMap.get("A0030"));
			else
				errorMsg += RULE_ID_STR + " 7159-The attribute A0030 is not present in STEP";

			if (attributeMap.containsKey("A0045_RET"))
				errorMsg += validateA0045RET(attributeMap.get("A0045_RET"));

			if (attributeMap.containsKey("A0045_NAD"))
				errorMsg += validateA0045NAD(attributeMap.get("A0045_NAD"));

			if (attributeMap.containsKey("A0046_RET"))
				errorMsg += validateA0046RET(attributeMap.get("A0046_RET"));

			if (attributeMap.containsKey("A0046_NAD"))
				errorMsg += validateA0046NAD(attributeMap.get("A0046_NAD"));

			if (attributeMap.containsKey("A0051") && attributeMap.containsKey("A0017"))
				errorMsg += validateA0051(attributeMap.get("A0051"), attributeMap.get("A0017"));

			if (attributeMap.containsKey("A0052"))
				errorMsg += validateA0052(attributeMap.get("A0052"));
			else
				errorMsg += RULE_ID_STR + " 6195-The attribute A0052 is not present in input";

			if (attributeMap.containsKey("A0067_RET"))
				errorMsg += validateA0067RET(attributeMap.get("A0067_RET"));
			else
				errorMsg += RULE_ID_STR + " 6209-The attribute A0067_RET is not present in input";

			if (attributeMap.containsKey("A0067_NAD"))
				errorMsg += validateA0067NAD(attributeMap.get("A0067_NAD"));
			else
				errorMsg += RULE_ID_STR + "6209-The attribute A0067_NAD is not present in input";

			if (attributeMap.containsKey("A0068"))
				errorMsg += validateA0068(attributeMap.get("A0068"));
			else
				errorMsg += RULE_ID_STR + " 6226-The attribute A0068 is not present in input";

			if (attributeMap.containsKey("A0069"))
				errorMsg += validateA0069(attributeMap.get("A0069"));
			else
				errorMsg += RULE_ID_STR + " 6234-The attribute A0069 is not present in input";

			if (attributeMap.containsKey("A0070"))
				errorMsg += validateA0070(attributeMap.get("A0070"));
			else
				errorMsg += RULE_ID_STR + " 6242-The attribute A0070 is not present in input";

			if (attributeMap.containsKey("A0071"))
				errorMsg += validateA0071(attributeMap.get("A0071"));

			if (attributeMap.containsKey("A0075_RET"))
				errorMsg += validateA0075RET(attributeMap.get("A0075_RET"));
			else
				errorMsg += RULE_ID_STR + " 6272-The attribute A0075_RET is not present in input";

			if (attributeMap.containsKey("A0075_NAD"))
				errorMsg += validateA0075NAD(attributeMap.get("A0075_NAD"));
			else
				errorMsg += RULE_ID_STR + " 6272-The attribute A0075_NAD is not present in input";

			if (attributeMap.containsKey("A0077_RET"))
				errorMsg += validateA0077RET(attributeMap.get("A0077_RET"));
			else
				errorMsg += RULE_ID_STR + " 6282-The attribute A0077_RET is not present in input";

			if (attributeMap.containsKey("A0077_NAD"))
				errorMsg += validateA0077NAD(attributeMap.get("A0077_NAD"));
			else
				errorMsg += RULE_ID_STR + " 6282-The attribute A0077_NAD is not present in input";

			if (attributeMap.containsKey("A0078_RET"))
				errorMsg += validateA0078RET(attributeMap.get("A0078_RET"));
			else
				errorMsg += RULE_ID_STR + " 6298-The attribute A0078_RET is not present in input";

			if (attributeMap.containsKey("A0078_NAD"))
				errorMsg += validateA0078NAD(attributeMap.get("A0078_NAD"));
			else
				errorMsg += RULE_ID_STR + " 6298-The attribute A0078_NAD is not present in input";

			if (attributeMap.containsKey("A0080"))
				errorMsg += validateA0080(attributeMap.get("A0080"));
			else
				errorMsg += RULE_ID_STR + " 6310-The attribute A0080 is not present in input";

			if (attributeMap.containsKey("A0083"))
				errorMsg += validateA0083(attributeMap.get("A0083"));

			if (attributeMap.containsKey("A0084"))
				errorMsg += validateA0084(attributeMap.get("A0080"), attributeMap.get("A0083"), attributeMap.get("A0082"), attributeMap
						.get("A0084"));

			// For PCMP-1348
			// if(attributeMap.containsKey("A0092"))
			// errorMsg +=
			// validateA0092(attributeMap.get("A0092"),attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0095"))
				errorMsg += validateA0095(attributeMap.get("A0095"));

			if (attributeMap.containsKey("A0096"))
				errorMsg += validateA0096(attributeMap.get("A0096"));

			if (attributeMap.containsKey("A0097"))
				errorMsg += validateA0097(attributeMap.get("A0097"));

			if (attributeMap.containsKey("A0098"))
				errorMsg += validateA0098(attributeMap.get("A0098"));

			if (attributeMap.containsKey("A0099"))
				errorMsg += validateA0099(attributeMap.get("A0099"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0100"))
				errorMsg += validateA0100(attributeMap.get("A0100"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0102"))
				errorMsg += validateA0102(attributeMap.get("A0102"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0104"))
				errorMsg += validateA0104(attributeMap.get("A0104"), attributeMap.get("A0090"));

			// Commented for PCMP-1327
			// if(attributeMap.containsKey("A0105"))
			// errorMsg +=
			// validateA0105(attributeMap.get("A0105"),attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0107"))
				errorMsg += validateA0107(attributeMap.get("A0107"), attributeMap.get("A0091"));

			if (attributeMap.containsKey("A0109"))
				errorMsg += validateA0109(attributeMap.get("A0109"), attributeMap.get("A0091"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0110"))
				errorMsg += validateA0110(attributeMap.get("A0110"), attributeMap.get("A0090"));

			if (attributeMap.get("A0090").equalsIgnoreCase("Y") && attributeMap.get("A0091").equalsIgnoreCase("Y")
					&& !attributeMap.containsKey("A0111"))
				errorMsg += RULE_ID_STR + " 6488-A0111 is not present in input when A0090 and A0091 are Y";

			if (attributeMap.get("A0090").equalsIgnoreCase("Y") && attributeMap.get("A0091").equalsIgnoreCase("Y")
					&& !attributeMap.containsKey("A0112"))
				errorMsg += RULE_ID_STR + " 6493-A0112 is not present in input when A0090 and A0091 are Y";

			if (attributeMap.get("A0090").equalsIgnoreCase("Y") && attributeMap.get("A0091").equalsIgnoreCase("Y")
					&& !attributeMap.containsKey("A0113"))
				errorMsg += RULE_ID_STR + " 6498-A0113 is not present in input when A0090 and A0091 are Y";

			if (attributeMap.get("A0090").equalsIgnoreCase("Y") && attributeMap.get("A0091").equalsIgnoreCase("Y")
					&& !attributeMap.containsKey("A0115"))
				errorMsg += RULE_ID_STR + " 6504-A0115 is not present in input when A0090 and A0091 are Y";

			// Commented for PCMP-1324
			// if(attributeMap.containsKey("A0114"))
			// errorMsg +=
			// validateA0114(attributeMap.get("A0114"),attributeMap.get("A0091"));

			// Commented for PCMP-1351
			// if(attributeMap.containsKey("A0117"))
			// errorMsg +=
			// validateA0117(attributeMap.get("A0117"),attributeMap.get("A0091"));

			// Commented for PCMP-1350
			// if(attributeMap.containsKey("A0118"))
			// errorMsg +=
			// validateA0118(attributeMap.get("A0118"),attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0119"))
				errorMsg += validateA0119(attributeMap.get("A0119"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0120"))
				errorMsg += validateA0120(attributeMap.get("A0120"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0121"))
				errorMsg += validateA0121(attributeMap.get("A0121"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0124") && attributeMap.get("A0124").equalsIgnoreCase("Y")) {
				if (attributeMap.containsKey("A0125"))
					errorMsg += validateA0125(attributeMap.get("A0125"));
				else
					errorMsg += RULE_ID_STR + " 6544-A0125 is not present in input when A0124 is Y";
			}
			if (attributeMap.containsKey("A0127"))
				errorMsg += validateA0127(attributeMap.get("A0127"), attributeMap.get("A0124"));

			if (attributeMap.containsKey("A0130"))
				errorMsg += validateA0130(attributeMap.get("A0130"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0134"))
				errorMsg += validateA0134(attributeMap.get("A0134"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0135"))
				errorMsg += validateA0135(attributeMap.get("A0135"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0136"))
				errorMsg += validateA0136(attributeMap.get("A0136"), attributeMap.get("A0090"), attributeMap.get("A0116"));

			if (attributeMap.containsKey("A0137"))
				errorMsg += validateA0137(attributeMap.get("A0137"), attributeMap.get("A0124"));

			if (attributeMap.containsKey("A0138"))
				errorMsg += validateA0138(attributeMap.get("A0138"), attributeMap.get("A0124"));

			if (attributeMap.containsKey("A0140"))
				errorMsg += validateA0140(attributeMap.get("A0140"), attributeMap.get("A0124"));

			if (attributeMap.containsKey("A0141"))
				errorMsg += validateA0141(attributeMap.get("A0141"), attributeMap.get("A0124"));

			if (attributeMap.containsKey("A0142"))
				errorMsg += validateA0142(attributeMap.get("A0142"), attributeMap.get("A0124"));

			if (attributeMap.containsKey("A0143"))
				errorMsg += validateA0143(attributeMap.get("A0143"), attributeMap.get("A0124"), attributeMap.get("A0142"));

			if (attributeMap.containsKey("A0144"))
				errorMsg += validateA0144(attributeMap.get("A0144"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0145"))
				errorMsg += validateA0145(attributeMap.get("A0145"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0146"))
				errorMsg += validateA0146(attributeMap.get("A0146"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0147"))
				errorMsg += validateA0147(attributeMap.get("A0147"), attributeMap.get("A0090"));

			if (attributeMap.containsKey("A0148"))
				errorMsg += validateA0148(attributeMap.get("A0148"));
			else
				errorMsg += RULE_ID_STR + " 6591-The attribute A0148 is not present in input";

			if (attributeMap.containsKey("A0150"))
				errorMsg += validateA0150(attributeMap.get("A0150"));

			if (attributeMap.containsKey("A0151"))
				errorMsg += validateA0151(attributeMap.get("A0151"));

			if (attributeMap.containsKey("A0175"))
				errorMsg += validateA0175(attributeMap.get("A0175"));

			if (attributeMap.containsKey("A0178"))
				errorMsg += validateA0178(attributeMap.get("A0178"));

			if (attributeMap.containsKey("A0189"))
				errorMsg += validateA0189(attributeMap.get("A0189"));
			else
				errorMsg += RULE_ID_STR + " 6746-The attribute A0189 is not present in input";

			if (attributeMap.containsKey("A0190"))
				errorMsg += validateA0190(attributeMap.get("A0190"));
			else
				errorMsg += RULE_ID_STR + " 6749-The attribute A0190 is not present in input";

			if (attributeMap.containsKey("A0191"))
				errorMsg += validateA0191(attributeMap.get("A0191"));
			else
				errorMsg += RULE_ID_STR + " 6753-The attribute A0191 is not present in input";

			if (attributeMap.containsKey("A0195"))
				errorMsg += validateA0195(attributeMap.get("A0195"));

			if (!attributeMap.containsKey("A0197"))
				errorMsg += RULE_ID_STR + " 6762-The attribute A0197 is not present in input";

			// Need to check the FDPO cost or other transaction
			// if(attributeMap.containsKey("A0198"))
			// errorMsg += validateA0198(attributeMap.get("A0198"));
			// else
			// errorMsg
			// +=RULE_ID_STR+" 6770-The attribute A0198 is not present in input";

			if (attributeMap.containsKey("A0200"))
				errorMsg += validateA0200(attributeMap.get("A0200"));

			// Need to check the transaction type whether its FDPO
			// if(attributeMap.containsKey("A0201"))
			// errorMsg += validateA0201(attributeMap.get("A0201"));
			// else
			// errorMsg
			// +=RULE_ID_STR+" 6784-The attribute A0201 is not present in input";

			if (attributeMap.containsKey("A0205"))
				errorMsg += validateA0205(attributeMap.get("A0205"));

			if (attributeMap.containsKey("A0210"))
				errorMsg += validateA0210(attributeMap.get("A0210"));
			else
				errorMsg += RULE_ID_STR + " 6823-The attribute A0210 is not present in input";

			if (attributeMap.containsKey("A0211"))
				errorMsg += validateA0211(attributeMap.get("A0211"));
			else
				errorMsg += RULE_ID_STR + " 6845-The attribute A0211 is not present in input";

			if (attributeMap.containsKey("A0212"))
				errorMsg += validateA0212(attributeMap.get("A0212"));
			else
				errorMsg += RULE_ID_STR + " 6852-The attribute A0212 is not present in input";

			if (attributeMap.containsKey("A0220"))
				errorMsg += validateA0220(attributeMap.get("A0220"));

			// Commented for PCMP-1325
			// if(attributeMap.containsKey("A0244") &&
			// attributeMap.containsKey("A0243"))
			// errorMsg +=
			// validateA0244(attributeMap.get("A0244"),attributeMap.get("A0243"));

			if (attributeMap.containsKey("A0304"))
				errorMsg += validateA0304(attributeMap.get("A0304"));

			if (!attributeMap.containsKey("A0037"))
				errorMsg += RULE_ID_STR + " 7174-The attribute A0037 is not present in input";

			if (!attributeMap.containsKey("A0090"))
				errorMsg += RULE_ID_STR + " 6393-The attribute A0090 is not present in input";

			if (attributeMap.containsKey("A0091"))
				errorMsg += validateA0091(attributeMap.get("A0091"), attributeMap.get("A0090"));
			else
				errorMsg += RULE_ID_STR + " 6408-The attribute A0091 is not present in input";

			if (!attributeMap.containsKey("A0124"))
				errorMsg += RULE_ID_STR + " 6536-The attribute A0124 is not present in input";

			if (attributeMap.containsKey("A0342")) {
				errorMsg += validateA0342(attributeMap.get("A0342"));
			}

			if (attributeMap.containsKey("A0038")) {
				errorMsg += validateA0038(attributeMap.get("A0038"));
			}

			if (attributeMap.containsKey("A0234")) {
				errorMsg += validateA0234(attributeMap.get("A0234"));
			}

			if (attributeMap.containsKey("A0277")) {
				errorMsg += validateA0277(attributeMap.get("A0277"));
			}
			if (attributeMap.containsKey("A0294")) {
				errorMsg += validateA0294(attributeMap.get("A0294"));
			}
			if (attributeMap.containsKey("A0295")) {
				errorMsg += validateA0295(attributeMap.get("A0295"));
			}
			if (attributeMap.containsKey("A0296")) {
				errorMsg += validateA0296(attributeMap.get("A0296"));
			}
			if (attributeMap.containsKey("A0297")) {
				errorMsg += validateA0297(attributeMap.get("A0297"));
			}
			if (attributeMap.containsKey("A0298")) {
				errorMsg += validateA0298(attributeMap.get("A0298"));
			}
			if (attributeMap.containsKey("A0507") && attributeMap.containsKey("A0506")) {
				errorMsg += validateA0507(attributeMap.get("A0507"), attributeMap.get("A0506"));
			}

			// PCMP-1370 - validation removed because of PCMP-1396 -
			// transformation added
			/*
			 * if(attributeMap.containsKey("A0499")) errorMsg +=
			 * validateA0499(attributeMap
			 * .get("A0499"),attributeMap.get("A0500"));
			 */

			// PCMP-1370 - validation removed because of PCMP-1396 -
			// transformation added
			// if(attributeMap.containsKey("A0499"))
			// errorMsg +=
			// validateA0499(attributeMap.get("A0499"),attributeMap.get("A0500"));
			// PCMP-1466 Removed street date validation when A0499 is Y
			/*
			 * if(attributeMap.containsKey("A0499") &&
			 * attributeMap.containsKey("A0500")){ errorMsg +=
			 * validateA0500(attributeMap
			 * .get("A0499"),attributeMap.get("A0500")); }
			 */

			// New validation refer-PCMP-1549 and PCMP-1550
			// Validation commented for PCMP-1857
			/*
			 * if(attributeMap.containsKey("A0018_RET") &&
			 * attributeMap.containsKey("A0018_NAD")){
			 * if(IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0018_RET")) &&
			 * IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0018_NAD"))){
			 * errorMsg
			 * +=RULE_ID_STR+" 7092-Both A0018_RET and A0018_NAD has null values"
			 * ; } }
			 */

			if (attributeMap.containsKey("A0075_RET") && attributeMap.containsKey("A0075_NAD")) {
				if (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0075_RET")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0075_NAD"))) {
					errorMsg += RULE_ID_STR + " 6269-Both A0075_RET and A0075_NAD has null values";
				}
			}
		}
		return errorMsg;
	}

	/**
	 * This method is used to remove some unwanted attributes.
	 * 
	 * @param attributeMap
	 * @throws ParseException
	 */
	public void removeUnwantedAttributes(Map<String, String> attributeMap) throws ParseException {

		// Responsibility Matrix attributes
		attributeMap.remove("A0214");
		attributeMap.remove("A0213");
		attributeMap.remove("A0029");
		// attributeMap.remove("A0028");
		attributeMap.remove("A0304");
		attributeMap.remove("A0251");
		attributeMap.remove("A0254");
		attributeMap.remove("A0248");
		attributeMap.remove("A0249");
		attributeMap.remove("A0250");
		attributeMap.remove("A0252");
		attributeMap.remove("A0253");
		attributeMap.remove("A0255");
		attributeMap.remove("A0256");
		attributeMap.remove("A0305");

		// Division and Department
		attributeMap.remove("A0024");
		attributeMap.remove("A0025");

		// Not present in STEP
		attributeMap.remove("A0041");
		attributeMap.remove("A0398");
		attributeMap.remove("A0131");
		attributeMap.remove("A0322");
		// For PCMP-1394
		attributeMap.remove("A0401");
		// Removed on the basis of Prem confirmation - Rama (07/15)
		// For PCMP-1420
		attributeMap.remove("A0007");
		// PCMP-1491 As per Sharanya inputs,attribute is removed
		attributeMap.remove("A0391");

	}

	/**
	 * @param SKU
	 *            Number validation for RuleID 7048-isMandatory validation for
	 *            RuleID 7052-isNotNegative
	 */
	public String validateA0012(String value) {

		if (IntgSrvUtils.isNullOrEmpty(value)) {
			return RULE_ID_STR + "7048-A0012 has null values";
		}
		if (!isNonNegative(value)) {
			return RULE_ID_STR + " 7052-A0012 is negative";
		}
		return EMPTY_STR;
	}

	/**
	 * @param Vendor
	 *            Model Number validation for RuleID 7053-isMandatory validation
	 *            for RuleID 7068-doesNotAcceptvalues"or'
	 */
	public String validateA0013RET(String value) {

		// PCMP-1398 - validation removed and applied transformation
		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 7053-A0013_RET has null values";
		// }
		if (!IntgSrvUtils.isNullOrEmpty(value) && !isQuotesFree(value)) {
			return RULE_ID_STR + " 7068-A0013_RET has quotes";
		}
		return EMPTY_STR;
	}

	/**
	 * @param Vendor
	 *            Model Number validation for RuleID 7053-isMandatory validation
	 *            for RuleID 7068-doesNotAcceptvalues"or'
	 */
	public String validateA0013NAD(String value) {

		// PCMP-1398 - validation removed and applied transformation
		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 7053-A0013_NAD has null values";
		// }
		if (!IntgSrvUtils.isNullOrEmpty(value) && !isQuotesFree(value)) {
			return RULE_ID_STR + " 7068-A0013_NAD has quotes";
		}
		return EMPTY_STR;
	}

	/**
	 * @param Full
	 *            Description validation for RuleID 7092-isMandatory validation
	 *            for RuleID 7097-doesNotAccept values " or '
	 */
	public String validateA0018RET(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 7092-A0018_RET has null values";
		// }
		if (!isQuotesFree(value)) {
			return RULE_ID_STR + " 7097-A0018_RET has quotes";
		}
		return EMPTY_STR;
	}

	/**
	 * @param Full
	 *            Description validation for RuleID 7092-isMandatory validation
	 *            for RuleID 7097-doesNotAccept values " or '
	 */
	public String validateA0018NAD(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 7092-A0018_NAD has null values";
		// }
		if (!isQuotesFree(value)) {
			return RULE_ID_STR + " 7097-A0018_NAD has quotes";
		}
		return EMPTY_STR;
	}

	/**
	 * @param Class
	 *            validation for RuleID 7134-isMandatory
	 */
	public String validateA0026(String value) {

		if (IntgSrvUtils.isNullOrEmpty(value)) {
			return RULE_ID_STR + " 7134-A0026 has null values";
		}
		return EMPTY_STR;
	}

	/**
	 * @param Retail
	 *            IA (Rebuyer) Number validation for RuleID 7144-isMandatory
	 *            validation for RuleID 7146-isNumeric
	 */
	public String validateA0028(String value) {

		if (IntgSrvUtils.isNullOrEmpty(value)) {
			return RULE_ID_STR + " 7144-A0028 has null values";
		}
		if (!IntgSrvUtils.isDouble(value)) {
			return RULE_ID_STR + " 7146-A0028 is not numeric";
		}
		return EMPTY_STR;
	}

	/**
	 * @param Plan
	 *            -o-gram Id (POG ID) validation for RuleID 7159-isMandatory
	 */
	public String validateA0030(String value) {

		if (IntgSrvUtils.isNullOrEmpty(value)) {
			return RULE_ID_STR + " 7159-A0030 has null values";
		}
		return EMPTY_STR;
	}

	/**
	 * @param Pallet
	 *            Ti validation for RuleID 6182-isNumeric validation for RuleID
	 *            6183-isNotNegative
	 */
	public String validateA0045RET(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if (!IntgSrvUtils.isDouble(value)) {
				return RULE_ID_STR + " 6182-A0045_RET is not numeric";
			}
			if (!isNonNegative(value)) {
				return RULE_ID_STR + " 6183-A0045_RET is negative";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param Pallet
	 *            Ti validation for RuleID 6182-isNumeric validation for RuleID
	 *            6183-isNotNegative
	 */
	public String validateA0045NAD(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if (!IntgSrvUtils.isDouble(value)) {
				return RULE_ID_STR + " 6182-A0045_NAD is not numeric";
			}
			if (!isNonNegative(value)) {
				return RULE_ID_STR + " 6183-A0045_NAD is negative";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param Pallet
	 *            Hi validation for RuleID 6186-isNumeric validation for RuleID
	 *            6187-isNotNegative
	 */
	public String validateA0046RET(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if (!IntgSrvUtils.isDouble(value)) {
				return RULE_ID_STR + " 6186-A0046_RET is not numeric";
			}
			if (!isNonNegative(value)) {
				return RULE_ID_STR + " 6187-A0046_RET is negative";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param Pallet
	 *            Hi validation for RuleID 6186-isNumeric validation for RuleID
	 *            6187-isNotNegative
	 */
	public String validateA0046NAD(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if (!IntgSrvUtils.isDouble(value)) {
				return RULE_ID_STR + " 6186-A0046_NAD is not numeric";
			}
			if (!isNonNegative(value)) {
				return RULE_ID_STR + " 6187-A0046_NAD is negative";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param Consignment
	 *            Cost validation for RuleID 6190-isMandatory when A0017 is "Y"
	 *            validation for RuleID 6192-isNotNegative
	 */
	public String validateA0051(String valueA0051, String valueA0017) {

		// Commented for PCMP-1326
		// if(IntgSrvUtils.isNullOrEmpty(valueA0051))
		// {
		// if(valueA0017.equalsIgnoreCase("Y") )
		// {
		// return RULE_ID_STR+" 6190-A0051 is null when A0017 is Y";
		// }
		// }
		if (!IntgSrvUtils.isNullOrEmpty(valueA0051)) {
			if (!isNonNegative(valueA0051)) {
				return RULE_ID_STR + " 6192-A0051 is negative";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param Retail
	 *            Store Sell Price validation for RuleID 6195-isMandatory
	 *            validation for RuleID 6196-isPositive,isNumeric
	 */
	public String validateA0052(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6195-A0052 has null values";
		// }
		// if(!IntgSrvUtils.isDouble(value))
		// {
		// return RULE_ID_STR+" 6196-A0052 is not numeric";
		// }
		// if(!isGreaterThanZero(value))
		// {
		// return RULE_ID_STR+" 6196-A0052 should be greater than zero";
		// }

		return EMPTY_STR;
	}

	/**
	 * @param Selling
	 *            Units per Master Case Pack (SUCP) validation for RuleID
	 *            6209-isMandatory validation for RuleID 6210-isPositive
	 *            validation for RuleID 6224-isPositiveItemCreate(Mass or
	 *            Screen) validation for RuleID 6225-isWithinRangeFrom1To9999999
	 */
	public String validateA0067RET(String value) {

		// Commented for PCMP-1328
		/*
		 * if(IntgSrvUtils.isNullOrEmpty(value)) { return
		 * RULE_ID_STR+" 6209-A0067_RET has null values"; }
		 */
		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			/*
			 * if(!isGreaterThanZero(value)) { return
			 * RULE_ID_STR+" 6210-A0067_RET should be greater than zero"; }
			 */
			if (isGreaterThanZero(value) && !isMinMaxCheck(value, "1", "9999999")) {
				return RULE_ID_STR + " 6225-A0067_RET is not within range 1 to 9999999";
			}
		}

		return EMPTY_STR;
	}

	/**
	 * @param Selling
	 *            Units per Master Case Pack (SUCP) validation for RuleID
	 *            6209-isMandatory validation for RuleID 6210-isPositive
	 *            validation for RuleID 6224-isPositiveItemCreate(Mass or
	 *            Screen) validation for RuleID 6225-isWithinRangeFrom1To9999
	 */
	public String validateA0067NAD(String value) {

		/*
		 * if(IntgSrvUtils.isNullOrEmpty(value)) { return
		 * RULE_ID_STR+" 6209-A0067_NAD has null values"; }
		 */
		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			/*
			 * if(!isGreaterThanZero(value)) { return
			 * RULE_ID_STR+" 6210-A0067_NAD should be greater than zero"; }
			 */
			if (isGreaterThanZero(value) && !isMinMaxCheck(value, "1", "9999999")) {
				return RULE_ID_STR + " 6225-A0067_NAD is not within range 1 to 9999999";
			}
		}

		return EMPTY_STR;
	}

	/**
	 * @param Master
	 *            Case Pack Length validation for RuleID 6226-isMandatory
	 *            validation for RuleID 6227-isPositive
	 */
	public String validateA0068(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6226-A0068 has null values";
		// }
		// if(!isGreaterThanZero(value))
		// {
		// return RULE_ID_STR+" 6227-A0068 should be greater than zero";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param Master
	 *            Case Pack Width validation for RuleID 6234-isMandatory
	 *            validation for RuleID 6235-isPositive
	 */
	public String validateA0069(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6234-A0069 has null values";
		// }
		// if(!isGreaterThanZero(value))
		// {
		// return RULE_ID_STR+" 6235-A0069 should be greater than zero";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param Master
	 *            Case Pack Height validation for RuleID 6242-isMandatory
	 *            validation for RuleID 6243-isPositive
	 */
	public String validateA0070(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6242-A0070 has null values";
		// }
		// if(!isGreaterThanZero(value))
		// {
		// return RULE_ID_STR+" 6243-A0070 should be greater than zero";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param Master
	 *            Case Pack Weight validation for RuleID 6252-hasPrecisionof2
	 *            and haslengthof7 validation for RuleID 6263-isNotNegative
	 */
	public String validateA0071(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if (!isNotGreaterThanMaxLength(value, 7)) {
				return RULE_ID_STR + " 6252-A0071 has length greater than 7";
			}
			// Commented for PCMP-1862
			/*
			 * if(!isNonNegative((value))) { return
			 * RULE_ID_STR+" 6263-A0071 is negative"; }
			 */
		}
		return EMPTY_STR;
	}

	/**
	 * @param Vendor
	 *            Number validation for RuleID 6269-isMandatory validation for
	 *            RuleID 6272-isNotNegative
	 */
	public String validateA0075RET(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6269-A0075_RET has null values";
		// }
		if (!value.equalsIgnoreCase("") && !isNonNegative(value)) {
			return RULE_ID_STR + " 6272-A0075_RET is negative";
		}
		return EMPTY_STR;
	}

	/**
	 * @param Vendor
	 *            Number validation for RuleID 6269-isMandatory validation for
	 *            RuleID 6272-isNotNegative
	 */
	public String validateA0075NAD(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6269-A0075_NAD has null values";
		// }
		if (!value.equalsIgnoreCase("") && !isNonNegative(value)) {
			return RULE_ID_STR + " 6272-A0075_NAD is negative";
		}
		return EMPTY_STR;
	}

	/**
	 * @param List
	 *            Price validation for RuleID 6282-isMandatory validation for
	 *            RuleID 6285-isPositive validation for RuleID
	 *            6297-isPositiveForItemCreate(Mass or Screen)
	 */
	public String validateA0077RET(String value) {

		// Commented for PCMP-1329
		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6282-A0077_RET has null values";
		// }
		// if(!isGreaterThanZero(value))
		// {
		// return RULE_ID_STR+" 6297-A0077_RET should be greater than zero";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param List
	 *            Price validation for RuleID 6282-isMandatory validation for
	 *            RuleID 6285-isPositive validation for RuleID
	 *            6297-isPositiveForItemCreate(Mass or Screen)
	 */
	public String validateA0077NAD(String value) {

		// Commented for PCMP-1329
		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6282-A0077_NAD has null values";
		// }
		// if(!isGreaterThanZero(value))
		// {
		// return RULE_ID_STR+" 6297-A0077_NAD should be greater than zero";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param PO
	 *            Cost validation for RuleID 6298-isMandatory validation for
	 *            RuleID 6299-isPositive validation for RuleID
	 *            7183-isdefaultvalueequalto0.01 if item is Consignment and
	 *            islargestPOcostequalto$99999.99
	 */
	public String validateA0078RET(String value) {

		// Commented for PCMP-1322
		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6298-A0078_RET has null values";
		// }
		// if(!isGreaterThanZero(value))
		// {
		// return RULE_ID_STR+" 6299-A0078_RET should be greater than zero";
		// }

		return EMPTY_STR;
	}

	/**
	 * @param PO
	 *            Cost validation for RuleID 6298-isMandatory validation for
	 *            RuleID 6299-isPositive validation for RuleID
	 *            7183-isdefaultvalueequalto0.01 if item is Consignment and
	 *            islargestPOcostequalto$99999.99
	 */
	public String validateA0078NAD(String value) {

		// Commented for PCMP-1322
		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6298-A0078_NAD has null values";
		// }
		// if(!isGreaterThanZero(value))
		// {
		// return RULE_ID_STR+" 6299-A0078_NAD should be greater than zero";
		// }

		return EMPTY_STR;
	}

	/**
	 * @param Selling
	 *            Unit UPC validation for RuleID 6310-isMandatory validation for
	 *            RuleID 6311-isNumeric validation for RuleID
	 *            6324-isNumericOrisN/AForItemCreate(Mass or Screen)
	 */
	public String validateA0080(String value) {

		/*
		 * if(IntgSrvUtils.isNullOrEmpty(value)) { return
		 * RULE_ID_STR+" 6310-A0080 has null values"; }
		 */
		if (!(IntgSrvUtils.isEmptyString(value) || IntgSrvUtils.isDouble(DatamigrationCommonUtil.splitValidUPC(value)) || isNAValue(value) || value
				.equalsIgnoreCase("N"))) {
			return RULE_ID_STR + " 6324-A0080 must be numeric or N/A";
		}
		return EMPTY_STR;
	}

	/**
	 * @param Master
	 *            Case Pack UPC validation for RuleID 6339-areItemCreate(Mass or
	 *            Screen)SellUnitUPCInnerPackUPCand
	 *            MasterCasePackUPCandPalletUPCNotSameifNotN/A validation for
	 *            RuleID 6344-
	 *            doPIPPIMCoreQuillGalaxyhaveUPCvalidationinplaceandisOnlyN
	 *            /AAtWhichPIPPIMCoreQuillGalaxyWillBypassUPCValidation
	 */
	public String validateA0082(String value) {

		return EMPTY_STR;
	}

	/**
	 * @param Inner
	 *            Pack UPC validation for RuleID 6353-areItemCreate(Mass or
	 *            Screen)SellUnitUPCInnerPackUPCand
	 *            MasterCasePackUPCandPalletUPCNotSameifNotN/A validation for
	 *            RuleID 6357-
	 *            doPIPPIMCoreQuillGalaxyhaveUPCvalidationinplaceandisOnlyN
	 *            /AAtWhichPIPPIMCoreQuillGalaxyWillBypassUPCValidation
	 *            validation for RuleID 6358-must be numeric or NA
	 */
	public String validateA0083(String value) {

		if (!(IntgSrvUtils.isEmptyString(value) || IntgSrvUtils.isDouble(value) || isNAValue(value))) {

			DatamigrationCommonUtil.printConsole(" Inside failure validationa A0083 : " + value);
			return RULE_ID_STR + " 6358-A0083 must be numeric or N/A";
		}
		return EMPTY_STR;
	}

	/**
	 * @param Pallet
	 *            UPC validation for RuleID 6367-areItemCreate(Mass or
	 *            Screen)SellUnitUPCInnerPackUPCand
	 *            MasterCasePackUPCandPalletUPCNotSameifNotN/A validation for
	 *            RuleID 6370-
	 *            doPIPPIMCoreQuillGalaxyhaveUPCvalidationinplaceandisOnlyN
	 *            /AAtWhichPIPPIMCoreQuillGalaxyWillBypassUPCValidation
	 *            validation for RuleID
	 *            6371-isNumericOrIsN/AForItemCreate(Screen and Mass)
	 */
	public String validateA0084(String valueA0080, String valueA0083, String valueA0082, String valueA0084) {

		if (!masterCasePackUPCValidation(valueA0080, valueA0083, valueA0082, valueA0084)) {
			return RULE_ID_STR + " 6367-SellUnitUPC, InnerPackUPC,MasterCasePackUPC and PalletUPC NotSame if Not N/A";
		}

		if (!(IntgSrvUtils.isEmptyString(valueA0084) || IntgSrvUtils.isDouble(valueA0084) || isNAValue(valueA0084))) {

			return RULE_ID_STR + " 6371-A0084 must be numeric or N/A";
		}

		return EMPTY_STR;
	}

	/**
	 * @param Country
	 *            Code 1 validation for RuleID
	 *            6372-isMinimumOneCountryCodePopulated
	 */
	public String validateA0085(String valueA0085, String valueA0086) {

		return EMPTY_STR;
	}

	/**
	 * Validation for RuleID 6414-isMandatory
	 * 
	 * @param valueA0092
	 * @param valueA0090
	 * @return
	 */
	public String validateA0092(String valueA0092, String valueA0090) {

		// Commented for PCMP-1348
		// if(valueA0090.equalsIgnoreCase("Y") &&
		// IntgSrvUtils.isNullOrEmpty(valueA0092))
		// {
		// return RULE_ID_STR+"6414- A0092 has null values when A0090 is Y";
		// }

		return EMPTY_STR;
	}

	/**
	 * @param Proper
	 *            Shipping Name validation for RuleID 6433-
	 */
	public String validateA0095(String value) {

		// if(!IntgSrvUtils.isEmptyString(value))
		// {
		// if(value!=null && isNAValue(value))
		// {
		// return RULE_ID_STR+" 6433-A0095 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * @param Technical
	 *            Data Sheet Name validation for RuleID 8257-
	 */
	public String validateA0096(String value) {

		// if(!IntgSrvUtils.isEmptyString(value))
		// {
		// if(isNAValue(value))
		// {
		// return RULE_ID_STR+" 8257-A0096 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * @param Hazardous
	 *            Class validation for RuleID 6443-
	 */
	public String validateA0097(String value) {

		// if(!IntgSrvUtils.isEmptyString(value))
		// {
		// if(isNAValue(value))
		// {
		// return RULE_ID_STR+" 6443-A0097 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * @param Primary
	 *            Hazard Label Required validation for RuleID 6446-
	 */
	public String validateA0098(String value) {

		// if(!IntgSrvUtils.isEmptyString(value))
		// {
		// if(isNAValue(value))
		// {
		// return RULE_ID_STR+" 6446-A0098 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * @param ID
	 *            NUMBER validation for RuleID 6451-
	 */
	public String validateA0099(String valueA0099, String valueA0090) {

		if (valueA0090.equalsIgnoreCase("Y") && valueA0099 == null) {
			return RULE_ID_STR + " 6450-A0099 has null values when A0090 is Y";
		}
		// if(!IntgSrvUtils.isEmptyString(valueA0099))
		// {
		// if(isNAValue(valueA0099))
		// {
		// return RULE_ID_STR+" 6451-A0099 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * @param Packing
	 *            Group validation for RuleID 7188-should not be NA
	 */
	public String validateA0100(String valueA0100, String valueA0090) {

		if (valueA0090.equalsIgnoreCase("y") && valueA0100 == null) {
			return RULE_ID_STR + " 6450-A0100 is null when A0090 is Y";
		}
		// if(!IntgSrvUtils.isEmptyString(valueA0100))
		// {
		// if(isNAValue(valueA0100))
		// {
		// return RULE_ID_STR+" 7188-A0100 ha NA values";
		// }
		// }

		return EMPTY_STR;
	}

	/**
	 * @param valueA0102
	 * @param valueA0090
	 * @return
	 */
	public String validateA0102(String valueA0102, String valueA0090) {

		if (valueA0090.equalsIgnoreCase("Y") && valueA0102 == null) {
			return RULE_ID_STR + " 6456-A0102 is null when A0090 is Y";
		}

		return EMPTY_STR;
	}

	/**
	 * @param valueA0104
	 * @param valueA0090
	 * @return
	 */
	public String validateA0104(String valueA0104, String valueA0090) {

		if (valueA0090.equalsIgnoreCase("y") && valueA0104 == null) {
			return RULE_ID_STR + " 6464-A0104 is null when A0090 is Y";
		}

		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6552-If Y, then A0124:State Restricted becomes
	 * mandatory with a value of Y.
	 * 
	 * @param valueA0127
	 * @param valueA0124
	 * @return
	 */
	public String validateA0127(String valueA0127, String valueA0124) {

		if (valueA0127.equalsIgnoreCase("y") && IntgSrvUtils.isNullOrEmpty(valueA0124) && !valueA0124.equalsIgnoreCase("Y")) {
			return RULE_ID_STR + " 6552-A0124 does not contain the value Y";
		}

		return EMPTY_STR;
	}

	/**
	 * @param Recycle
	 *            validation for RuleID 6591-isMandatory
	 */
	public String validateA0148(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6591-A0148 has null values";
		// }
		return EMPTY_STR;
	}

	/**
	 * @paramTotal Recycled Content % validation for RuleID 6609-isNumeric
	 *             validation for RuleID 6610-isMaxLengthequalto3 validation for
	 *             RuleID 6612-isPositive validation for RuleID
	 *             6613-isNotNegative validation for RuleID 6617-hasNoDecimal
	 */
	public String validateA0150(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if (!IntgSrvUtils.isDouble(value)) {
				return RULE_ID_STR + " 6609-A0150 is not a number";
			}
			if (!isNotGreaterThanMaxLength(value, 3)) {
				return RULE_ID_STR + " 6610-A0150 has length greater than 3";
			}
			// if(!isGreaterThanZero(value))
			// {
			// return RULE_ID_STR+" 6612-A0150 should be greater than zero";
			// }
			if (!isNonNegative(value)) {
				return RULE_ID_STR + " 6613-A0150 is negative";
			}

			if (isDecimal(value)) {
				return RULE_ID_STR + " 6617-A0150 has decimal";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @paramTotal Post Consumer Content % validation for RuleID 6620-isNumeric
	 *             validation for RuleID 6622-isMaxLengthequalto3 validation for
	 *             RuleID 6623-isNotNegative
	 */
	public String validateA0151(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if (!IntgSrvUtils.isDouble(value)) {
				return RULE_ID_STR + " 6620-A0151 is not a number";
			}
			if (!isNotGreaterThanMaxLength(value, 3)) {
				return RULE_ID_STR + " 6622-A0151 has length greater than 3";
			}
			if (!isNonNegative(value)) {
				return RULE_ID_STR + " 6623-A0151 should be greater than zero";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param Bio
	 *            Based % validation for RuleID
	 *            6694-isValueRangeBetween0and100forItemCreate/Update
	 */
	public String validateA0175(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if (!isMinMaxCheck(value, "0", "100")) {
				return RULE_ID_STR + " 6694-A0175 is not within range 0-100";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param UNSPSC
	 *            Code validation for RuleID 6697-isNotNegative validation for
	 *            RuleID 6702-isNumeric validation for RuleID
	 *            6703-isLengthEqualTo8DigitsIfEntered
	 */
	public String validateA0178(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			// PCMP-1318 - Need to remove the below line after fixing this issue
			// in PIMCore
			value = DatamigrationCommonUtil.replaceDotTrimExpon(value);
			if (!isNonNegative(value)) {
				return RULE_ID_STR + " 6697-A0178 is negative";
			}
			if (!IntgSrvUtils.isDouble(value)) {
				return RULE_ID_STR + " 6702-A0178 is not a number";
			}
			if (!ProductInboundTransformation.changeDoubleToInteger(value).equalsIgnoreCase("0") && !isExactLength(value, 8)) {
				return RULE_ID_STR + " 6703-A0178's length is not 8";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param Change
	 *            Effective Date (Private label field) - Retail validation for
	 *            RuleID 6711-isvalidISODate
	 */
	public String validateA0180(String value) {

		return EMPTY_STR;
	}

	/**
	 * @param Change
	 *            Effective Date (Private label field) - NAD validation for
	 *            RuleID 6731-isvalidISODate
	 */
	public String validateA0184(String value) {

		return EMPTY_STR;
	}

	/**
	 * @param Store
	 *            Disposition validation for RuleID 6746-isMandatory
	 */
	public String validateA0189(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6746-A0189 has null values";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param Distribution
	 *            Center Disposition validation for RuleID 6749-isMandatory
	 */
	public String validateA0190(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6749-A0190 has null values";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param NAD
	 *            Disposition validation for RuleID 6753-isMandatory
	 */
	public String validateA0191(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6753-A0191 has null values";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param Recycle
	 *            Value - California Recycle Size validation for RuleID
	 *            6759-isNotNegative
	 */
	public String validateA0195(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if (!isNonNegative(value)) {
				return RULE_ID_STR + " 6759-A0195 is negative";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param List
	 *            Price Type validation for RuleID 6762-isMandatory
	 */
	public String validateA0197(String value) {

		// if(IntgSrvUtils.isNullOrEmpty(value))
		// {
		// return RULE_ID_STR+" 6762-A0197 has null values";
		// }

		return EMPTY_STR;
	}

	/**
	 * @param Future
	 *            Dated PO Cost / List Price Effective Date validation for
	 *            RuleID 6770-isMandatory Validation for RuleID
	 *            6772-hasValidDateFormatOfYYYY-MM-DD
	 */
	public String validateA0198(String value) {

		DatamigrationCommonUtil.printConsole("A0198:" + value);
		if (IntgSrvUtils.isNullOrEmpty(value)) {
			return RULE_ID_STR + " 6770-A0198 has null values";
		}
		// if(!isValidDate(value))
		// {
		// return RULE_ID_STR+" 6772-A0198 is not a valid date";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param Future
	 *            Dated PO Cost validation for RuleID 6779-hasPrecisionof2
	 *            Validation for RuleID 6784-isPositive
	 */
	public String validateA0200(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			// if(!isPrecisionTwo(value, 2))
			// {
			// return RULE_ID_STR+" 6779-A0200 does not have precision 2";
			// }
			if (!isGreaterThanZero(value)) {
				return RULE_ID_STR + " 6784-A0200 should be greater than zero";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param Future
	 *            Dated PO Cost Open Flag validation for RuleID 6801-isMandatory
	 */
	public String validateA0201(String value) {

		DatamigrationCommonUtil.printConsole("A0201: " + value);
		if (IntgSrvUtils.isNullOrEmpty(value)) {
			return RULE_ID_STR + " 6801-A0201 has null values";
		}
		return EMPTY_STR;
	}

	/**
	 * @param Change
	 *            For Level aka SKU Level validation for RuleID
	 *            8330-hasLengthOf11
	 */
	public String validateA0205(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if (!isExactLength(value, 11)) {
				return RULE_ID_STR + " 8330-A0205's length is not 11";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param Sell
	 *            Unit of Measure validation for RuleID 6823-isMandatory
	 */
	public String validateA0210(String value) {

		// Commented for PCMP-1859
		/*
		 * if(IntgSrvUtils.isNullOrEmpty(value)) { return
		 * RULE_ID_STR+" 6823-A0210 has null values"; }
		 */
		return EMPTY_STR;
	}

	/**
	 * @param Sell
	 *            Unit of Measure Quantity validation for RuleID
	 *            6845-isMandatory validation for RuleID 6846-isPositive
	 */
	public String validateA0211(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if (!isGreaterThanZero(value)) {
				return RULE_ID_STR + " 6846-A0211 should be greater than zero";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param Purchase
	 *            Unit of Measure validation for RuleID 6852-isMandatory
	 */
	public String validateA0212(String value) {

		// Commented for PCMP-1859
		/*
		 * if(IntgSrvUtils.isNullOrEmpty(value)) { return
		 * RULE_ID_STR+" 6852-A0212 has null values"; }
		 */
		return EMPTY_STR;
	}

	/**
	 * @param Inner
	 *            Pack Quantity validation for RuleID 6873-isNotNegative
	 */
	public String validateA0220(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if (!isNonNegative(value)) {
				return RULE_ID_STR + " 6873-A0220 is negative";
			}
		}
		return EMPTY_STR;
	}

	/**
	 * @param value
	 * @return
	 */
	// Commented for PCMP-1325
	public String validateA0244(String valueA0244, String valueA0243) {

		//
		// if(!IntgSrvUtils.isNullOrEmpty(valueA0243) &&
		// IntgSrvUtils.isNullOrEmpty(valueA0244))
		// {
		// return
		// RULE_ID_STR+" 6968-A0244 has null values when A0243 is populated";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param Imprinted
	 *            validation for RuleID 7340-isDefaultValueEqualTo1
	 */
	public String validateA0229(String value) {

		return EMPTY_STR;
	}

	/**
	 * @param NAD
	 *            IA (Rebuyer) Number validation for RuleID 7221-isMandatory
	 */
	public String validateA0304(String value) {

		if (IntgSrvUtils.isNullOrEmpty(value)) {
			return RULE_ID_STR + " 7221-A0304 has null values";
		}

		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6450-isMandatory when when A0090 is "Y"
	 * 
	 * @param valueA0105
	 * @param valueA0090
	 * @return
	 */
	// Commented for PCMP-1327
	public String validateA0105(String valueA0105, String valueA0090) {

		//
		// if(valueA0090.equalsIgnoreCase("Y") &&
		// IntgSrvUtils.isNullOrEmpty(valueA0105))
		// {
		// return RULE_ID_STR+" 6450-A0105 has null values when A0090 is Y";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param valueA0107
	 * @param valueA0091
	 * @return
	 */
	public String validateA0107(String valueA0107, String valueA0091) {

		// Removed for PCMP-1858 - as it is no longer implemented in Galaxy
		/*
		 * if(valueA0091.equalsIgnoreCase("Y") &&
		 * IntgSrvUtils.isNullOrEmpty(valueA0107)) { return
		 * RULE_ID_STR+" 6472-A0107 has null values when A0091 is Y"; }
		 */
		return EMPTY_STR;
	}

	/**
	 * @param valueA0109
	 * @param valueA0091
	 * @param valueA0090
	 *            validation for RuleID 6475-isMandatory when when A0090 is "Y"
	 *            or A0091 is "Y" validation for RuleID 6481-does not contain NA
	 *            values
	 * @return
	 */
	public String validateA0109(String valueA0109, String valueA0091, String valueA0090) {

		// PCMP-1392 - #6475- A0109: Rule is depreciated, and can be removed.
		/*
		 * if((valueA0091.equalsIgnoreCase("yes") ||
		 * valueA0091.equalsIgnoreCase("y") || valueA0090.equalsIgnoreCase("Y"))
		 * && valueA0109==null) { return
		 * RULE_ID_STR+" 6475-A0109 has null values"; }
		 */
		// if(isNAValue(valueA0109))
		// {
		// return RULE_ID_STR+" 6481-A0109 has NA values";
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6483-isMandatory when when A0090 is "Y" validation
	 * for RuleID 6484-does not contain NA values
	 * 
	 * @param valueA0110
	 * @param valueA0090
	 * @return
	 */
	public String validateA0110(String valueA0110, String valueA0090) {

		// PCMP-1728 A0110:True Product Unit is not valid
		/*
		 * if((valueA0090.equalsIgnoreCase("Y")) && valueA0110==null) { return
		 * RULE_ID_STR+" 6483-A0110 has null values"; }
		 */
		// if(isNAValue(valueA0110))
		// {
		// return RULE_ID_STR+" 6484-A0110 has NA values";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param valueA0111
	 * @param valueA0090
	 * @param valueA0091
	 * @return
	 */
	public String validateA0111(String valueA0111, String valueA0090, String valueA0091) {

		// if(valueA0090.equalsIgnoreCase("Y") &&
		// valueA0091.equalsIgnoreCase("Y") &&
		// IntgSrvUtils.isNullOrEmpty(valueA0111))
		// {
		// return
		// RULE_ID_STR+" 6488-A0111 has null values when A0090 and A0091 are Y";
		// }
		// if(isNAValue(valueA0110))
		// {
		// return RULE_ID_STR+" 6484-A0110 has NA values";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param valueA0113
	 * @param valueA0090
	 * @param valueA0091
	 * @return
	 */
	public String validateA0113(String valueA0113, String valueA0090, String valueA0091) {

		if (valueA0090.equalsIgnoreCase("Y") && valueA0090.equalsIgnoreCase("Y") && IntgSrvUtils.isNullOrEmpty(valueA0113)) {
			return RULE_ID_STR + " 6498-A0113 has null values when A0090 and A0091 are Y";
		}
		// if(isNAValue(valueA0110))
		// {
		// return RULE_ID_STR+" 6484-A0110 has NA values";
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6499-isMandatory when when A0091 is "Y"
	 * 
	 * @param valueA0114
	 * @param valueA0091
	 * @return
	 */
	// Commented for PCMP-1324
	public String validateA0114(String valueA0114, String valueA0091) {

		//
		// if((valueA0091.equalsIgnoreCase("yes") ||
		// valueA0091.equalsIgnoreCase("y")))
		// {
		// if(IntgSrvUtils.isNullOrEmpty(valueA0114)){
		// return RULE_ID_STR+" 6499-A0114 has null values when A0091 is Y";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * @param valueA0117
	 * @param valueA0091
	 * @return
	 */
	public String validateA0117(String valueA0117, String valueA0091) {

		// Commented for PCMP-1351
		// if((valueA0091.equalsIgnoreCase("yes") ||
		// valueA0091.equalsIgnoreCase("y")) &&
		// IntgSrvUtils.isNullOrEmpty(valueA0117))
		// {
		// return RULE_ID_STR+" 6509-A0117 has null values when A0091 is Y";
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6515-isMandatory when when A0090 is "Y" validation
	 * for RuleID 8255-does not contain NA values
	 * 
	 * @param valueA0118
	 * @param valueA0090
	 * @return
	 */
	public String validateA0118(String valueA0118, String valueA0090) {

		// Commented for PCMP-1350
		// if(valueA0090.equalsIgnoreCase("Y") &&
		// IntgSrvUtils.isNullOrEmpty(valueA0118))
		// {
		// return RULE_ID_STR+" 6515-A0118 has null values";
		// }
		// if(isNAValue(valueA0118))
		// {
		// return RULE_ID_STR+" 8255-A0118 has NA values";
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6521-isMandatory when when A0090 is "Y" validation
	 * for RuleID 8256-does not contain NA values
	 * 
	 * @param valueA0119
	 * @param valueA0090
	 * @return
	 */
	public String validateA0119(String valueA0119, String valueA0090) {

		// Commented for PCMP-1350
		// if(valueA0090.equalsIgnoreCase("Y") &&
		// IntgSrvUtils.isNullOrEmpty(valueA0119))
		// {
		// return RULE_ID_STR+" 6521-A0119 has null values";
		// }
		// if(isNAValue(valueA0119))
		// {
		// return RULE_ID_STR+" 8256-A0119 has NA values";
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6526-isMandatory when when A0090 is "Y" validation
	 * for RuleID 6527-does not contain NA values
	 * 
	 * @param valueA0120
	 * @param valueA0090
	 * @return
	 */
	public String validateA0120(String valueA0120, String valueA0090) {

		if (valueA0090.equalsIgnoreCase("Y") && valueA0120 == null) {
			return RULE_ID_STR + " 6526-A0120 has null values";
		}
		// if(!IntgSrvUtils.isEmptyString(valueA0120))
		// {
		// if(isNAValue(valueA0120))
		// {
		// return RULE_ID_STR+" 6527-A0120 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6531-isMandatory when when A0090 is "Y" validation
	 * for RuleID 8258-does not contain NA values
	 * 
	 * @param valueA0121
	 * @param valueA0090
	 * @return
	 */
	public String validateA0121(String valueA0121, String valueA0090) {

		// Commented for PCMP-1350
		// if(valueA0090.equalsIgnoreCase("Y") &&
		// IntgSrvUtils.isNullOrEmpty(valueA0121))
		// {
		// return RULE_ID_STR+" 6531-A0121 has null values";
		// }
		// if(isNAValue(valueA0121))
		// {
		// return RULE_ID_STR+" 8258-A0121 has NA values";
		// }
		return EMPTY_STR;
	}

	/**
	 * @param valueA0125
	 * @return
	 */
	public String validateA0125(String valueA0125) {

		if (IntgSrvUtils.isNullOrEmpty(valueA0125)) {
			return RULE_ID_STR + " 6544-A0125 has null values when A0124 is Y";
		}
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6558-isMandatory when when A0090 is "Y" validation
	 * for RuleID 6559-does not contain NA values
	 * 
	 * @param valueA0130
	 * @param valueA0090
	 * @return
	 */
	public String validateA0130(String valueA0130, String valueA0090) {

		// PCMP-1726 A0130:Product Container Type is not valid.
		/*
		 * if((valueA0090.equalsIgnoreCase("Y")) && valueA0130==null) { return
		 * RULE_ID_STR+" 6558-A0130 has null values"; }
		 */
		// if(!IntgSrvUtils.isEmptyString(valueA0130))
		// {
		// if(isNAValue(valueA0130))
		// {
		// return RULE_ID_STR+" 6559-A0130 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6562-isMandatory when when A0090 is "Y" validation
	 * for RuleID 6563-does not contain NA values
	 * 
	 * @param valueA0134
	 * @param valueA0090
	 * @return
	 */
	public String validateA0134(String valueA0134, String valueA0090) {

		if ((valueA0090.equalsIgnoreCase("Y")) && valueA0134 == null) {
			return RULE_ID_STR + " 6562-A0134 has null values";
		}
		// if(!IntgSrvUtils.isEmptyString(valueA0134))
		// {
		// if(isNAValue(valueA0134))
		// {
		// return RULE_ID_STR+" 6563-A0134 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6566-isMandatory when when A0090 is "Y" validation
	 * for RuleID 6567-does not contain NA values
	 * 
	 * @param valueA0135
	 * @param valueA0090
	 * @return
	 */
	public String validateA0135(String valueA0135, String valueA0090) {

		if ((valueA0090.equalsIgnoreCase("Y")) && valueA0135 == null) {
			return RULE_ID_STR + " 6566-A0135 has null values";
		}
		// if(!IntgSrvUtils.isEmptyString(valueA0135))
		// {
		// if(isNAValue(valueA0135))
		// {
		// return RULE_ID_STR+" 6567-A0135 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6570-isMandatory when when A0090 is "Y" validation
	 * for RuleID 6571-does not contain NA values
	 * 
	 * @param valueA0136
	 * @param valueA0090
	 * @return
	 */
	public String validateA0136(String valueA0136, String valueA0090, String valueA0116) {

		if ((valueA0090.equalsIgnoreCase("Y")) && valueA0116.equalsIgnoreCase("Y") && valueA0136 == null) {
			return RULE_ID_STR + " 6570-A0136 has null values";
		}
		// if(!IntgSrvUtils.isEmptyString(valueA0136))
		// {
		// if(isNAValue(valueA0136))
		// {
		// return RULE_ID_STR+" 6571-A0136 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6573-isMandatory when when A0124 is "Y"
	 * 
	 * @param valueA0137
	 * @param valueA0124
	 * @return
	 */
	public String validateA0137(String valueA0137, String valueA0124) {

		// Commented for PCMP-1552 and transformation added
		// if((valueA0124.equalsIgnoreCase("Y")) &&
		// IntgSrvUtils.isNullOrEmpty(valueA0137))
		// {
		// return RULE_ID_STR+" 6573-A0137 has null values when A0124 is Y";
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6575-isMandatory when when A0124 is "Y"
	 * 
	 * @param valueA0138
	 * @param valueA0124
	 * @return
	 */
	public String validateA0138(String valueA0138, String valueA0124) {

		// Commented for PCMP-1552
		// if((valueA0124.equalsIgnoreCase("Y")) &&
		// IntgSrvUtils.isNullOrEmpty(valueA0138))
		// {
		// return RULE_ID_STR+" 6575-A0138 has null values when A0124 is Y";
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6580-isMandatory when when A0124 is "Y"
	 * 
	 * @param valueA0141
	 * @param valueA0124
	 * @return
	 */
	public String validateA0141(String valueA0141, String valueA0124) {

		// Commented for PCMP-1552
		// if((valueA0124.equalsIgnoreCase("Y")) &&
		// IntgSrvUtils.isNullOrEmpty(valueA0141))
		// {
		// return RULE_ID_STR+" 6580-A0141 has null values when A0124 is Y";
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6578-isMandatory when when A0124 is "Y"
	 * 
	 * @param valueA0140
	 * @param valueA0124
	 * @return
	 */
	public String validateA0140(String valueA0140, String valueA0124) {

		// Commented for PCMP-1552
		// if((valueA0124.equalsIgnoreCase("Y")) &&
		// IntgSrvUtils.isNullOrEmpty(valueA0140))
		// {
		// return RULE_ID_STR+" 6578-A0140 has null values when A0124 is Y";
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6582-isMandatory when when A0124 is "Y"
	 * 
	 * @param valueA0142
	 * @param valueA0124
	 * @return
	 */
	public String validateA0142(String valueA0142, String valueA0124) {

		// Commented for PCMP-1552
		// if((valueA0124.equalsIgnoreCase("Y")) &&
		// IntgSrvUtils.isNullOrEmpty(valueA0142))
		// {
		// return RULE_ID_STR+" 6582-A0142 has null values when A0124 is Y";
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6584-isMandatory when when A0124 is
	 * "Y and when A0142 is "Y "
	 * 
	 * @param valueA0143
	 * @param valueA0124
	 * @param valueA0142
	 * @return
	 */
	public String validateA0143(String valueA0143, String valueA0124, String valueA0142) {

		if ((valueA0124.equalsIgnoreCase("Y")) && (valueA0142.equalsIgnoreCase("Y")) && IntgSrvUtils.isNullOrEmpty(valueA0143)) {
			return RULE_ID_STR + " 6584-A0143 has null values when A0124 and A0142 is Y";
		}
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6586-isMandatory when when A0090 is "Y" validation
	 * for RuleID 6587-does not contain NA values
	 * 
	 * @param valueA0144
	 * @param valueA0090
	 * @return
	 */
	public String validateA0144(String valueA0144, String valueA0090) {

		// PCMP-1724
		/*
		 * if(valueA0090.equalsIgnoreCase("Y") && valueA0144==null) { return
		 * RULE_ID_STR+" 6586-A0144 has null values"; }
		 */

		// if(!IntgSrvUtils.isEmptyString(valueA0144))
		// {
		// if(isNAValue(valueA0144))
		// {
		// return RULE_ID_STR+" 6587-A0144 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 6589-isMandatory when when A0090 is "Y" validation
	 * for RuleID 6590-does not contain NA values
	 * 
	 * @param valueA0145
	 * @param valueA0090
	 * @return
	 */
	public String validateA0145(String valueA0145, String valueA0090) {

		if (valueA0090.equalsIgnoreCase("Y") && valueA0145 == null) {
			return RULE_ID_STR + " 6589-A0145 has null values";
		}
		// if(!IntgSrvUtils.isEmptyString(valueA0145))
		// {
		// if(isNAValue(valueA0145))
		// {
		// return RULE_ID_STR+" 6590-A0145 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 7192-isMandatory when when A0090 is "Y" validation
	 * for RuleID 7193-does not contain NA values
	 * 
	 * @param valueA0146
	 * @param valueA0090
	 * @return
	 */
	public String validateA0146(String valueA0146, String valueA0090) {

		if (valueA0090.equalsIgnoreCase("Y") && valueA0146 == null) {
			return RULE_ID_STR + " 7192-A0146 has null values";
		}
		// if(!IntgSrvUtils.isEmptyString(valueA0146))
		// {
		// if(isNAValue(valueA0146))
		// {
		// return RULE_ID_STR+" 7193-A0146 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * validation for RuleID 7195-isMandatory when when A0090 is "Y" validation
	 * for RuleID 7196-does not contain NA values
	 * 
	 * @param valueA0147
	 * @param valueA0090
	 * @return
	 */
	public String validateA0147(String valueA0147, String valueA0090) {

		if (valueA0090.equalsIgnoreCase("Y") && valueA0147 == null) {
			return RULE_ID_STR + " 7195-A0147 has null values";
		}
		// if(!IntgSrvUtils.isEmptyString(valueA0147))
		// {
		// if(isNAValue(valueA0147))
		// {
		// return RULE_ID_STR+" 7196-A0147 has NA values";
		// }
		// }
		return EMPTY_STR;
	}

	public String validateA0342(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			// Length changed from 30 to 50 for PCMP-1397
			if (!isNotGreaterThanMaxLength(value, 50)) {
				return RULE_ID_STR + " NA-A0342 has length greater than 50";
			}
		}
		return EMPTY_STR;
	}

	public String validateA0091(String valueA0091, String valueA0090) {

		// if(valueA0090.equalsIgnoreCase("Y"))
		// {
		// if(!IntgSrvUtils.isNullOrEmpty(valueA0091) &&
		// !valueA0091.equalsIgnoreCase("Y"))
		// {
		// return RULE_ID_STR+" 6402-A0091 is not Y when A0090 is Y";
		// }
		// }
		return EMPTY_STR;
	}

	/**
	 * @param A0038
	 *            validation for RuleID 7176-isMandatory
	 */
	public String validateA0038(String value) {

		if (IntgSrvUtils.isNullOrEmpty(value)) {
			return RULE_ID_STR + " 7176-A0038 is mandatory";
		}

		return EMPTY_STR;
	}

	/**
	 * @param A0234
	 *            validation for RuleID 7197-isMandatory
	 */
	public String validateA0234(String value) {

		if (IntgSrvUtils.isNullOrEmpty(value)) {
			return RULE_ID_STR + " 7197-A0234 is mandatory";
		}

		return EMPTY_STR;
	}

	/**
	 * @param A0277
	 *            validation for RuleID 7007-Cannot be negative
	 */
	public String validateA0277(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value) && !isNonNegative(value)) {
			return RULE_ID_STR + " 7007-A0277 is negative";
		}
		return EMPTY_STR;
	}

	/**
	 * @param A0294
	 *            validation for RuleID 7205-Cannot be negative
	 */
	public String validateA0294(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value) && !isNonNegative(value)) {
			return RULE_ID_STR + " 7205-A0294 is negative";
		}
		return EMPTY_STR;
	}

	/**
	 * @param A0295
	 *            validation for RuleID 7207-Cannot be negative
	 */
	public String validateA0295(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value) && !isNonNegative(value)) {
			return RULE_ID_STR + " 7207-A0295 is negative";
		}
		return EMPTY_STR;
	}

	/**
	 * @param A0296
	 *            validation for RuleID 7209-Cannot be negative
	 */
	public String validateA0296(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value) && !isNonNegative(value)) {
			return RULE_ID_STR + " 7209-A0296 is negative";
		}
		return EMPTY_STR;
	}

	/**
	 * @param A0297
	 *            validation for RuleID 7211-Cannot be negative
	 */
	public String validateA0297(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value) && !isNonNegative(value)) {
			return RULE_ID_STR + " 7209-A0297 is negative";
		}
		return EMPTY_STR;
	}

	/**
	 * @param A0298
	 *            validation for RuleID 7213-Cannot be negative
	 */
	public String validateA0298(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value) && !isNonNegative(value)) {
			return RULE_ID_STR + " 7213-A0298 is negative";
		}
		return EMPTY_STR;
	}

	// PCMP-1466 Removed street date validation when A0499 is Y
	public String validateA0500(String valueA0499, String valueA0500) {

		/*
		 * if(valueA0499.equalsIgnoreCase("Y")) { SimpleDateFormat dateFormat =
		 * new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); try { Date date =
		 * dateFormat.parse(valueA0500); if(date.compareTo(new Date())<=0) {
		 * System
		 * .out.println("A0500 is not a future date when A0499 is Y:"+valueA0500
		 * ); return "A0500 is not a future date when A0499 is Y"; } } catch
		 * (ParseException e) { // TODO Auto-generated catch block
		 * //e.printStackTrace();
		 * System.out.println("A0500 is Not a valid date when A0499 is Y"
		 * +valueA0500); return "A0500 is not a valid date when A0499 is Y"; } }
		 */
		return EMPTY_STR;
	}

	// PCMP-1553
	public String validateA0507(String valueA0507, String valueA0506) {

		// Commented for PCMP-1860
		/*
		 * if(!IntgSrvUtils.isNullOrEmpty(valueA0506) &&
		 * "Y".equalsIgnoreCase(DatamigrationCommonUtil
		 * .converYESNOIntoChar(valueA0506)) &&
		 * IntgSrvUtils.isNullOrEmpty(valueA0507)){ return
		 * RULE_ID_STR+" 9021-A0507 is empty when A0506 is Y"; }
		 */
		return EMPTY_STR;
	}

	public static void main(String[] args) {

		ProductInboundValidation obj = new ProductInboundValidation();
		// Double d=Double.parseDouble("5");
		// System.out.println(d.intValue());
		// System.out.println("trur"+obj.validateA0080("1212231212~"));
		System.out.println("A0500::::::" + obj.validateA0500("Y", ""));
		// Date date=new Date("Tue Jul 21 13:10:46 IST 2015");
		// System.out.println("current date"+date);
		// System.out.println("comparison"+(date.compareTo(new Date())));
		// System.out.println(obj.isGreaterThanZero("23.0"));
		// System.out.println(obj.validateA0342("abcdefghijklmnopqrstuvwxyzabcd..."));
		// System.out.println(Double.parseDouble("23"));
		// System.out.println(obj.isValidDate("1992-78-78"));
		// System.out.println(obj.changeDateFormat("11/11/1992"));
		// System.out.println(masterCasePackUPCValidation("apple","apple","fttydtr","apple"));
	}
}
