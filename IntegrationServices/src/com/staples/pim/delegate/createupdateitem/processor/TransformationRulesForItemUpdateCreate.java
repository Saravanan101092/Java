/**
 * -----------------------------------------------------------------------
 * STAPLES, INC
 * -----------------------------------------------------------------------
 * (C) Copyright 2007 Staples, Inc.          All rights reserved.
 *
 * NOTICE:  All information contained herein or attendant hereto is,
 *          and remains, the property of Staples Inc.  Many of the
 *          intellectual and technical concepts contained herein are
 *          proprietary to Staples Inc. Any dissemination of this
 *          information or reproduction of this material is strictly
 *          forbidden unless prior written permission is obtained
 *          from Staples Inc.
 * -----------------------------------------------------------------------
 */
/*
 * File name     :   TransformationRulesForItemUpdateCreate
 * Creation Date :   
 * @author  
 * @version 1.0
 */

package com.staples.pim.delegate.createupdateitem.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;

public class TransformationRulesForItemUpdateCreate {

	public static HashMap<String, String> overrideValuesForItemUpdateCreate(HashMap<String, String> allValuesHashMap) throws ParseException {

		HashMap<String, String> allValuesHashMapLoc = allValuesHashMap;

		if ((allValuesHashMap.get(IntgSrvAppConstants.A0500) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0500).equals(""))) {

			String inputDateString = (String) allValuesHashMap.get(IntgSrvAppConstants.A0500);

			SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

			java.util.Date date = inputFormat.parse(inputDateString);
			String formattedDateStr = (outputFormat.format(date));

			allValuesHashMap.put(IntgSrvAppConstants.A0500, formattedDateStr);
		}

		if ((allValuesHashMap.get(IntgSrvAppConstants.A0018_RET) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0018_RET).equals("") && ((String) allValuesHashMap
				.get(IntgSrvAppConstants.A0018_RET)).length() > 10)
				&& (allValuesHashMap.get(IntgSrvAppConstants.A0019) == null || allValuesHashMap.get(IntgSrvAppConstants.A0019).equals(""))) {
			allValuesHashMap.put(IntgSrvAppConstants.A0019, ((String) allValuesHashMap.get(IntgSrvAppConstants.A0018_RET)).substring(0, 9));
		}
		if ((allValuesHashMap.get(IntgSrvAppConstants.A0018_RET) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0018_RET).equals("") && ((String) allValuesHashMap
				.get(IntgSrvAppConstants.A0018_RET)).length() > 18)
				&& (allValuesHashMap.get(IntgSrvAppConstants.A0021) == null || allValuesHashMap.get(IntgSrvAppConstants.A0021).equals(""))) {
			allValuesHashMap
					.put(IntgSrvAppConstants.A0021, ((String) allValuesHashMap.get(IntgSrvAppConstants.A0018_RET)).substring(0, 17));
		}
		// Rule 7077 - Coming from Step - If 'Y', set it to 'NI' If 'N' set it
		// to 'OH'
		if (allValuesHashMap.get(IntgSrvAppConstants.A0015) != null
				&& allValuesHashMap.get(IntgSrvAppConstants.A0015).equals(IntgSrvAppConstants.YES)) {
			allValuesHashMap.put(IntgSrvAppConstants.A0015, IntgSrvAppConstants.NI);
		} else {
			allValuesHashMap.put(IntgSrvAppConstants.A0015, IntgSrvAppConstants.OH);
		}
		// CR 70, This should be a required field if Hazmat is Yes but the user
		// should be able to enter "N/A".
		// The system should not store "N/A" but consider it as a blank and send
		// a blank to downstream system.
		if (allValuesHashMap.get(IntgSrvAppConstants.A0090) != null
				&& (allValuesHashMap.get(IntgSrvAppConstants.A0090).equals("Yes") || allValuesHashMap.get(IntgSrvAppConstants.A0090)
						.equals("Y"))) {
			if (allValuesHashMap.get(IntgSrvAppConstants.A0096) != null && allValuesHashMap.get(IntgSrvAppConstants.A0096).equals("N/A")) {
				allValuesHashMap.put(IntgSrvAppConstants.A0096, "");
			}
			if (allValuesHashMap.get(IntgSrvAppConstants.A0097) != null && allValuesHashMap.get(IntgSrvAppConstants.A0097).equals("N/A")) {
				allValuesHashMap.put(IntgSrvAppConstants.A0097, "");
			}
			if (allValuesHashMap.get(IntgSrvAppConstants.A0098) != null && allValuesHashMap.get(IntgSrvAppConstants.A0098).equals("N/A")) {
				allValuesHashMap.put(IntgSrvAppConstants.A0098, "");
			}
			if (allValuesHashMap.get(IntgSrvAppConstants.A0099) != null && allValuesHashMap.get(IntgSrvAppConstants.A0099).equals("N/A")) {
				allValuesHashMap.put(IntgSrvAppConstants.A0099, "");
			}
			if (allValuesHashMap.get(IntgSrvAppConstants.A0100) != null && allValuesHashMap.get(IntgSrvAppConstants.A0100).equals("N/A")) {
				allValuesHashMap.put(IntgSrvAppConstants.A0100, "");
			}
		}
		// CR 70,A0240 convert pounds to ounces
		if (allValuesHashMap.get(IntgSrvAppConstants.A0240) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0240).equals("")) {
			String strValueA0240 = (String) allValuesHashMap.get(IntgSrvAppConstants.A0240);
			double dValueA0240 = Double.parseDouble(strValueA0240) * 16;
			allValuesHashMap.put(IntgSrvAppConstants.A0240, "" + dValueA0240);
		}
		// CR 87,copy A0497 to A0078
		if (allValuesHashMap.get(IntgSrvAppConstants.A0497) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0497).equals("")) {
			allValuesHashMap.put(IntgSrvAppConstants.A0078, allValuesHashMap.get(IntgSrvAppConstants.A0497));
		}
		if (allValuesHashMap.get(IntgSrvAppConstants.A0497_RET) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0497_RET).equals("")) {
			allValuesHashMap.put(IntgSrvAppConstants.A0078_RET, allValuesHashMap.get(IntgSrvAppConstants.A0497));
		}
		if (allValuesHashMap.get(IntgSrvAppConstants.A0497_NAD) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0497_NAD).equals("")) {
			allValuesHashMap.put(IntgSrvAppConstants.A0078_NAD, allValuesHashMap.get(IntgSrvAppConstants.A0497));
		}
		// Rule 7072 (A0013 Vendor Model Number) UPPER CASE
		if (allValuesHashMap.get(IntgSrvAppConstants.A0013) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0013).equals("")) {
			allValuesHashMap.put(IntgSrvAppConstants.A0013, ((String) allValuesHashMap.get(IntgSrvAppConstants.A0013)).toUpperCase());
		}

		if (allValuesHashMap.get(IntgSrvAppConstants.A0013_RET) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0013_RET).equals("")) {
			allValuesHashMap.put(IntgSrvAppConstants.A0013_RET,
					((String) allValuesHashMap.get(IntgSrvAppConstants.A0013_RET)).toUpperCase());
		}

		if (allValuesHashMap.get(IntgSrvAppConstants.A0013_NAD) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0013_NAD).equals("")) {
			allValuesHashMap.put(IntgSrvAppConstants.A0013_NAD,
					((String) allValuesHashMap.get(IntgSrvAppConstants.A0013_NAD)).toUpperCase());
		}
		// Rule 7101 (A0018 Full Description) UPPER CASE
		if (allValuesHashMap.get(IntgSrvAppConstants.A0018) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0018).equals("")) {
			allValuesHashMap.put(IntgSrvAppConstants.A0018, ((String) allValuesHashMap.get(IntgSrvAppConstants.A0018)).toUpperCase());
		}
		if (allValuesHashMap.get(IntgSrvAppConstants.A0018_RET) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0018_RET).equals("")) {
			allValuesHashMap.put(IntgSrvAppConstants.A0018_RET,
					((String) allValuesHashMap.get(IntgSrvAppConstants.A0018_RET)).toUpperCase());
		}
		if (allValuesHashMap.get(IntgSrvAppConstants.A0018_NAD) != null && !allValuesHashMap.get(IntgSrvAppConstants.A0018_NAD).equals("")) {
			allValuesHashMap.put(IntgSrvAppConstants.A0018_NAD,
					((String) allValuesHashMap.get(IntgSrvAppConstants.A0018_NAD)).toUpperCase());
		}
		// Rule 8181: If R, convert to empty and send proper spaces downstream.
		if (allValuesHashMap.get(IntgSrvAppConstants.A0106) != null
				&& allValuesHashMap.get(IntgSrvAppConstants.A0106).equals(IntgSrvAppConstants.R)) {
			allValuesHashMap.put(IntgSrvAppConstants.A0106, "");
		}
		// Rule 8174: If R, convert to empty and send proper spaces downstream.
		if (allValuesHashMap.get(IntgSrvAppConstants.A0108) != null
				&& allValuesHashMap.get(IntgSrvAppConstants.A0108).equals(IntgSrvAppConstants.R)) {
			allValuesHashMap.put(IntgSrvAppConstants.A0108, "");
		}
		// Rule 8188 If R, convert to empty and send proper spaces downstream.
		if (allValuesHashMap.get(IntgSrvAppConstants.A0128) != null
				&& allValuesHashMap.get(IntgSrvAppConstants.A0128).equals(IntgSrvAppConstants.R)) {
			allValuesHashMap.put(IntgSrvAppConstants.A0128, "");
		}
		// Rule 6819: If A0205 - Change For Level is "SKU Level" then N, else Y;
		// Required down stream, does not come from STEP
		if (allValuesHashMap.get(IntgSrvAppConstants.A0205) != null) {
			if (allValuesHashMap.get(IntgSrvAppConstants.A0205).equalsIgnoreCase(IntgSrvAppConstants.SKU_LEVEL)) {
				allValuesHashMap.put(IntgSrvAppConstants.A0206, IntgSrvAppConstants.NO);
			} else {
				allValuesHashMap.put(IntgSrvAppConstants.A0206, IntgSrvAppConstants.YES);
			}
		}
		//PCMP-2047 As IRebuyer is changed to LOV in STEP
		if (!IntgSrvUtils.isNullOrEmpty(allValuesHashMap.get("A0028"))) {
			if (allValuesHashMap.get("A0028").contains(":")) {
				//allValuesHashMap.put("A0028",allValuesHashMap.get("A0028").split(":")[0]);
				allValuesHashMap.put("A0028",allValuesHashMap.get("A0028").substring(0, allValuesHashMap.get("A0028").indexOf(":")).trim());
			} 
		}
		if (!IntgSrvUtils.isNullOrEmpty(allValuesHashMap.get("A0304"))) {
			if (allValuesHashMap.get("A0304").contains(":")) {
				//allValuesHashMap.put("A0304",allValuesHashMap.get("A0304").split(":")[0]);
				allValuesHashMap.put("A0304",allValuesHashMap.get("A0304").substring(0, allValuesHashMap.get("A0304").indexOf(":")).trim());
			} 
		}
		return allValuesHashMapLoc;
	}

}
