
package com.staples.pim.delegate.wercs.common;

import java.util.Map;

import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

public class WercsIntegrationValidation {

	/**
	 * Rule Id :9377 - Append suffix C c.c. to the value|e.g. 47C c.c)
	 * 
	 * @param value
	 * @return
	 */
	public String validateA0136(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			return value + "C c.c";
		}
		return value;
	}

	/**
	 * Rule Id :9385 - If A0657: Uniform Fire Code= "Flammable solid", set to'Y'
	 * 
	 * Rule Id :9386 - If A0657: Uniform Fire Code is blank, set to 'N'
	 * 
	 * @param valueA0113
	 * @param valueA0657
	 * @return
	 */
	public String validateA0113(String valueA0113, String valueA0657) {

		if (!IntgSrvUtils.isNullOrEmpty(valueA0657)) {
			if ("Flammable solid".equalsIgnoreCase(valueA0657)) {
				valueA0113 = "Y";
			}
		} else {
			valueA0113 = "N";
		}
		return valueA0113;
	}

	/**
	 * Rule Id :9413 - If value in the data feed is 'Does not exceed' set to N
	 * 
	 * Rule Id :9414 - If value in the data feed is 'exceed' set to Y
	 * 
	 * Rule Id :9415 - If blank, set to N
	 * 
	 * @param valueA0113
	 * @param valueA0657
	 * @return
	 */
	public String validateA0138(String value) {

		if ("Does not exceed".equalsIgnoreCase(value)) {
			value = "N";
		}
		if ("exceed".equalsIgnoreCase(value)) {
			value = "Y";
		}
		if (value.isEmpty()) {
			value = "N";
		}
		return value;
	}

	/**
	 * Rule Id :9419 - If value in the data feed is 'Does not exceed' set to N
	 * 
	 * Rule Id :9420 - If value in the data feed is 'exceed' set to Y Rule Id
	 * :9421 - If blank, set to N
	 * 
	 * @param value
	 * @return
	 */
	public String validateA0140(String value) {

		if (value != null) {
			if ("Does not exceed".equalsIgnoreCase(value)) {
				value = "N";
			}
			if ("exceed".equalsIgnoreCase(value)) {
				value = "Y";
			}
			if (value.isEmpty()) {
				value = "N";
			}
		}
		return value;
	}

	/**
	 * Rule Id :9425 - If value in the data feed is 'Does not exceed' set to N
	 * 
	 * Rule Id :9426 - If value in the data feed is 'exceed' set to Y Rule Id
	 * :9427 - If blank, set to N
	 * 
	 * @param value
	 * @return
	 */
	public String validateA0137(String value) {

		if (value != null) {
			if ("Does not exceed".equalsIgnoreCase(value)) {
				value = "N";
			}
			if ("exceed".equalsIgnoreCase(value)) {
				value = "Y";
			}
			if (value.isEmpty()) {
				value = "N";
			}
		}
		return value;
	}

	/**
	 * Rule Id :9431 - If value in the data feed is 'Does not exceed' set to N
	 * 
	 * Rule Id :9432 - If value in the data feed is 'exceed' set to Y Rule Id
	 * :9433 - If blank, set to N
	 * 
	 * @param value
	 * @return
	 */
	public String validateA0662(String value) {

		if (value != null) {
			if ("Does not exceed".equalsIgnoreCase(value)) {
				value = "N";
			}
			if ("exceed".equalsIgnoreCase(value)) {
				value = "Y";
			}
			if (value.isEmpty()) {
				value = "N";
			}
		}
		return value;
	}

	/**
	 * Rule Id :9441 - If A0099: ID Number = UN3480 or UN348 or UN3090 or UN3091
	 * set to 'Y' and do not invoke the rules 9442, 9443, 9444, 9445
	 * 
	 * Rule Id :9442 - If A0997: Proper Shipping Name for DOT Base
	 * Classification not blanks, set to Y
	 * 
	 * @param valueA0090
	 * @param valueA0099
	 * @param valueA0997
	 * @return
	 */
	public String validateA0090(String valueA0090, String valueA0099, String valueA0997) {

		if (!IntgSrvUtils.isNullOrEmpty(valueA0099)) {
			if (("UN3480".equalsIgnoreCase(valueA0099)) || ("UN348".equalsIgnoreCase(valueA0099))
					|| ("UN3090".equalsIgnoreCase(valueA0099)) || ("UN3091".equalsIgnoreCase(valueA0099))) {
				valueA0090 = "Y";
				return valueA0090;
			}
		}
		if (!valueA0997.isEmpty()) {
			valueA0090 = "Y";
		}
		return valueA0090;
	}

	/**
	 * Rule Id :9447 - "Pipe Delimited: 1. A0099: ID Number (UN code) 2. A0095:
	 * Proper Shipping Name 3. A0097: Hazardous Class , 4. A0144: Subsidiary
	 * risk class 1 5. A0100: Packing Group (when applicable)"
	 * 
	 * @param valueA0997
	 * @param attributeMap
	 */
	public void validateA0997(String valueA0997, Map<String, String> attributeMap) {

		if (!IntgSrvUtils.isNullOrEmpty(valueA0997)) {

			String[] strArr = valueA0997.split("\\|");
			attributeMap.put("A0099", strArr[0]);
			attributeMap.put("A0095", strArr[1]);
			attributeMap.put("A0097", strArr[2]);
			attributeMap.put("A0144", strArr[3]);
			attributeMap.put("A0100", strArr[4]);
		}
	}

	/**
	 * Rule Id :9464 - If not blank convert to Y, else convert to N
	 * 
	 * @param value
	 * @return
	 */
	public String validateA0107(String value) {

		if (value != null) {
			if (value.isEmpty()) {
				value = "N";
			} else {
				value = "Y";
			}
		}
		return value;
	}

	/**
	 * Rule Id :9468 - Allowed Values: Y, N
	 * 
	 * @param valueA0135
	 * @return
	 */
	public String validateA0135(String valueA0135) {

		String valueforA0135 = DatamigrationCommonUtil.converYESNOIntoChar(valueA0135);
		return valueforA0135;
	}

	/**
	 * Rule Id :9471 - If A0090: Hazardous Material Flag = Y and A0134: limited
	 * quantity = Y and A0787 = 11516 then set A0098: Primary hazard label
	 * required = 'S4 SP-11516'
	 * 
	 * @param valueA0090
	 * @param valueA0134
	 * @param A0787
	 * @return
	 */
	public String validateA0787(String valueA0090, String valueA0134, String valueA0787, String valueA0098) {

		if (("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0090)))
				&& ("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0134))) && "11516".equalsIgnoreCase(valueA0787)) {
			valueA0098 = "S4 SP-11516";
		}
		return valueA0098;
	}

	/**
	 * 
	 * Rule Id :9477 - If A0090: Hazardos Material Flag = Y and A0134: Limited
	 * Quantity = N, set to Y
	 * 
	 * @param valueA0090
	 * @param valueA0134
	 * @return
	 */
	public String validateA0105(String valueA0090, String valueA0134, String valueA0105) {

		if (("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0090)))
				&& ("N".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0134)))) {
			valueA0105 = "Y";
		}
		return valueA0105;
	}

	/**
	 * Rule Id :9479 - If A0657: Uniform Fire Code = 'Flammable liquid' set to
	 * Y, else set to N
	 * 
	 * @param valueA0657
	 * @return
	 */
	public String validateA0111(String valueA0657, String valueA0111) {

		if (valueA0657 != null) {
			if ("Flammable liquid".equalsIgnoreCase(valueA0657)) {
				valueA0111 = "Y";
			} else {
				valueA0111 = "N";
			}
		}
		return valueA0111;
	}

	/**
	 * Rule Id :9481 - If A0657: Uniform Fire Code = 'Combustible liquid' set to
	 * Y, else set to N
	 * 
	 * @param valueA0657
	 * @return
	 */
	public String validateA0114(String valueA0657, String valueA0114) {

		if (valueA0657 != null) {
			if ("Combustible liquid".equalsIgnoreCase(valueA0657)) {
				valueA0114 = "Y";
			} else {
				valueA0114 = "N";
			}
		}
		return valueA0114;
	}

	/**
	 * Rule Id :9483 - If A0657: Uniform Fire Code = ' Corrosive' set to Y, else
	 * set to N
	 * 
	 * @param valueA0657
	 * @return
	 */
	public String validateA0115(String valueA0657, String valueA0115) {

		if (valueA0657 != null) {
			if ("Corrosive".equalsIgnoreCase(valueA0657)) {
				valueA0115 = "Y";
			} else {
				valueA0115 = "N";
			}
		}
		return valueA0115;
	}

	/**
	 * Rule Id :9485 - If A2012: survey question 6 = Y set to Y, else set to N
	 * 
	 * @param valueA2012
	 * @return
	 */
	public String validateA0116(String valueA2012, String valueA0116) {

		if (valueA2012 != null) {
			if ("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA2012))) {
				valueA0116 = "Y";
			} else {
				valueA0116 = "N";
			}
		}
		return valueA0116;
	}

	/**
	 * Rule Id :9487 - "If UPCSize (A0109) > 16 ounces and A0116: Liquid =Y, set
	 * to Y "
	 * 
	 * @param valueA0117
	 * @param valueA0109
	 * @param valueA0116
	 * @return
	 */
	public String validateA0117(String valueA0117, String valueA0109, String valueA0116) {

		try {
			if (!IntgSrvUtils.isNullOrEmpty(valueA0109) && !IntgSrvUtils.isNullOrEmpty(valueA0116)) {
			}
			if ((Integer.parseInt(valueA0109)) > 16 && "Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0116))) {
				valueA0117 = "Y";
			}
		} catch (Exception e) {
			return valueA0117;
		}
		return valueA0117;
	}

	/**
	 * Rule Id :9495 - If 1 convert to Y, if 0 convert to N
	 * 
	 * @param value
	 * @return
	 */
	public String validateA0644(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			if ("0".equalsIgnoreCase(value)) {
				value = "N";
			}
			if ("1".equalsIgnoreCase(value)) {
				value = "Y";
			}
		}
		return value;
	}

	/**
	 * Rule Id :9518 - If A0090: Hazardous Material Flag = Y and A0134: limited
	 * quantity = Y and A0787: DOT Special Permit not 11516 then set A0098:
	 * Primary hazard label required = 'S1 LIMITED QUANTITY DIAMOND LABEL'
	 * 
	 * 
	 * @param valueA0090
	 * @param valueA0134
	 * @param valueA0098
	 * @param valueA0787
	 * @return
	 */
	public String validateA0134(String valueA0090, String valueA0134, String valueA0098, String valueA0787) {

		if ("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0090))
				&& "Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0134)) && "11516".equalsIgnoreCase(valueA0787)) {
			valueA0098 = "S1 LIMITED QUANTITY DIAMOND LABEL";
		}
		return valueA0098;
	}

	// FIXME 9460,9461
	/**
	 * Rule Id :9459 - If A0090: Hazardos Material Flag = Y, and A2003: Limited
	 * Quantity for Ground = Y, and A0788: DOT Special Permit is 11516, then set
	 * to "S4 SP-11516"
	 * 
	 * Rule Id :9460 - If A0090: Hazardos Material Flag = Y, and (A0643: Product
	 * Itself is a Battery or A0644: Battery Containing Product Indicator) and
	 * A0099: ID Number=UN3090 or UN3091 then set to
	 * "S3 Lithium (Metal) battery cargo only"
	 * 
	 * Rule Id :9461 - If A0090: Hazardos Material Flag = Y, and (A0643: Product
	 * Itself is a Battery or A0644: Battery Containing Product Indicator) and
	 * A0099: ID Number=UN3480 or UN3481 then set to
	 * "S6 LITHIUM ION HAZCOM PEEL AWAY"
	 * 
	 * Rule Id :9462 - If A0090: Hazardos Material Flag = Y, and A2003: Limited
	 * Quantity for Ground = Y, and A0135: Marine Pollutant is Y, then set to
	 * "S5 MARINE POLLUTANT"
	 * 
	 * 
	 * @param valueA0090
	 * @param valueA2003
	 * @param valueA0788
	 * @param valueA0643
	 * @param valueA0644
	 * @param valueA0099
	 * @param valueA0098
	 * @return
	 */
	public String validateA0098(String valueA0090, String valueA2003, String valueA0788, String valueA0643, String valueA0644,
			String valueA0099, String valueA0135, String valueA0098) {

		if ("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0090))
				&& "Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA2003)) && "11516".equalsIgnoreCase(valueA0788)) {
			valueA0098 = "S4 SP-11516";
		}
		if ("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0090))
				&& ("UN3090".equalsIgnoreCase(valueA0099) || "UN3091".equalsIgnoreCase(valueA0099))) {
			valueA0098 = "S3 Lithium (Metal) battery cargo only";
		}
		if ("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0090))
				&& ("UN3480".equalsIgnoreCase(valueA0099) || "UN3481".equalsIgnoreCase(valueA0099))) {
			valueA0098 = "S6 LITHIUM ION HAZCOM PEEL AWAY";
		}
		if ("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0090))
				&& "Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA2003))
				&& "Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0135))) {
			valueA0098 = "S5 MARINE POLLUTANT";
		}
		return valueA0098;
	}

	/**
	 * Rule Id :9387 - If A0657: Uniform Fire Code = "Aerosols: Level 1",set to
	 * 1
	 * 
	 * Rule Id :9388 - If A0657: Uniform Fire Code = "Aerosols: Level 2",set to
	 * 2
	 * 
	 * Rule Id :9389 - If A0657 : Uniform Fire Code= "Aerosols: Level 3",set to
	 * 3
	 * 
	 * Rule Id :9390 - If A0657: Uniform Fire Code = "Aerosols", set to 'Y'
	 * 
	 * Rule Id :9391 - If non of the "Aerosols * " or blank set to 'N'
	 * 
	 * 
	 * 
	 * @param valueA0657
	 * @param valueA0104
	 * @return
	 */
	public String validateA0104(String valueA0657, String valueA0104) {

		if (valueA0657 != null) {
			if ("Aerosols: Level 1".equalsIgnoreCase(valueA0657)) {
				valueA0657 = "1";
			} else if ("Aerosols: Level 2".equalsIgnoreCase(valueA0657)) {
				valueA0657 = "2";
			} else if ("Aerosols: Level 3".equalsIgnoreCase(valueA0657)) {
				valueA0657 = "3";
			} else if ("Aerosols".equalsIgnoreCase(valueA0657)) {
				valueA0657 = "Y";
			} else {
				valueA0657 = "N";
			}
		}
		return valueA0104;
	}

	// FIXME 9402
	/**
	 * 
	 * Rule Id :9401 - If A0140 or A0137 or A0138 or A0662= Y set to Y
	 * 
	 * Rule Id :9402 - If non of the "Aerosols * " or blank set to 'N'
	 * 
	 * Rule Id :A0124 - If A0140 and A0137 and A0138 and A0662 blanks then set
	 * to Blank
	 * 
	 * @param valueA0140
	 * @param valueA0137
	 * @param valueA0138
	 * @param valueA0662
	 * @param valueA0124
	 * @return
	 */
	public String validateA0124(String valueA0140, String valueA0137, String valueA0138, String valueA0662, String valueA0124) {

		if ("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0140))
				|| "Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0137))
				|| "Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0138))
				|| "Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0662))) {
			valueA0124 = "Y";
		}
		if (valueA0140.isEmpty() && valueA0137.isEmpty() && valueA0138.isEmpty() && valueA0662.isEmpty()) {
			valueA0124 = "";
		}
		return valueA0124;
	}

	/**
	 * Rule Id :9408 - If A0140 = Y set to
	 * AL|AK|AS|AZ|AR|CA|CO|CT|DE|DC|FL|GA|GU
	 * |HI|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI
	 * |MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|OH
	 * |OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|VI|WA|WV|WI|WY
	 * 
	 * Rule Id :9407 - If A0138 = Y set to
	 * CA|CT|DE|ME|MD|MA|MI|NJ|NY|RI|IL|IN|NH|VA|OH|PA
	 * 
	 * Rule Id :9406 - If A0137 or A0662 = CA set to CA
	 * 
	 * Rule Id :9409 - If A0124 is blank set to blank
	 * 
	 * Rule Id :9410 - Rules 9408 invoke first; Rule 9407 invoke after rule
	 * 9408; Rule 9406 invoke after 9408
	 * 
	 * @param valueA0137
	 * @param valueA0138
	 * @param valueA0140
	 * @param valueA0124
	 * @param valueA0662
	 * @param valueA0125
	 * @return
	 */
	public String validateA0125(String valueA0137, String valueA0138, String valueA0140, String valueA0124, String valueA0662,
			String valueA0125) {

		if ("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0140))) {
			valueA0125 = "AL|AK|AS|AZ|AR|CA|CO|CT|DE|DC|FL|GA|GU|HI|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|VI|WA|WV|WI|WY";
		}
		if ("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0138))) {
			valueA0125 = "CA|CT|DE|ME|MD|MA|MI|NJ|NY|RI|IL|IN|NH|VA|OH|PA";
		}
		if ("CA".equalsIgnoreCase(valueA0137) || "CA".equalsIgnoreCase(valueA0662)) {
			valueA0125 = "CA";
		}
		if (valueA0124.isEmpty()) {
			valueA0125 = "";
		}
		return valueA0125;
	}

	/**
	 * Rule Id:9476 - If A0090: Hazardos Material Flag = Y and A0134: Limited
	 * Quantity = N, set to Y
	 * 
	 * @param valueA0090
	 * @param valueA0134
	 * @param valueA0102
	 * @return
	 */
	public String validateA0102(String valueA0090, String valueA0134, String valueA0102) {

		if ("Y".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0090))
				&& "N".equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(valueA0134))) {
			valueA0102 = "Y";
		}
		return valueA0102;
	}

	/**
	 * Rule Id:NA - Set with value of A0998: DOT Technical Name
	 * 
	 * @param valueA0998
	 * @return
	 */
	public String validateA0120(String valueA0998) {

		return valueA0998;
	}

	/**
	 * Rule Id:NA - If A0099: ID Number is UN3090 or UN3091 then set to 'S8
	 * LITHIUM METAL HAZCOM PEEL AWAY'
	 * 
	 * Rule Id:NA - If A0099: ID Number is UN3480 or UN3481 then set to 'S7
	 * LITHIUM ION HANDLING LABEL'
	 * 
	 * @param valueA0099
	 * @param valueA0145
	 * @return
	 */
	public String validateA0145(String valueA0099, String valueA0145) {

		if ("UN3090".equalsIgnoreCase(valueA0099) || "UN3091".equalsIgnoreCase(valueA0099)) {
			valueA0145 = "S8 LITHIUM METAL HAZCOM PEEL AWAY";
		}
		if ("UN3480".equalsIgnoreCase(valueA0099) || "UN3481".equalsIgnoreCase(valueA0099)) {
			valueA0145 = "S7 LITHIUM ION HANDLING LABEL";
		}
		return valueA0145;
	}

	/**
	 * Rule Id :NA - If A0099: ID Number is UN3090 or UN3091 then set to 'S9
	 * LITHIUM METAL HANDLING LABEL'
	 * 
	 * @param valueA0099
	 * @param valueA0147
	 * @return
	 */
	public String validateA0147(String valueA0099, String valueA0147) {

		if ("UN3090".equalsIgnoreCase(valueA0099) || "UN3091".equalsIgnoreCase(valueA0099)) {
			valueA0147 = "S8 LITHIUM METAL HAZCOM PEEL AWAY";
		}
		return valueA0147;
	}

	/**
	 * Rule Id :NA - The A0621 value 1066723 is not between Min value 10 and Max
	 * value 4000
	 * 
	 * @param valueA0621
	 * @return
	 */
	private String validateA0621(String valueA0621) {

		if (!(valueA0621.length() >= 10)) {
			valueA0621 = "000" + valueA0621;
		}

		return valueA0621;
	}

	/**
	 * Rule Id :9396 - Allowed Values Y,N,Blank
	 * 
	 * @param valueA0141
	 * @return
	 */

	private String validateA0141(String valueA0141) {

		String valueforA0141 = DatamigrationCommonUtil.converYESNOIntoChar(valueA0141);
		return valueforA0141;
	}

	/**
	 * If 0 then set to "0: Not combustible" If 1 then set to
	 * "1: Combustible If heated" If 2 then set to
	 * "2: Combustible If liquid flash point of 100 to 200F" If 3 then set to
	 * "3: Flammable liquid flash point below 100F" If 4 then set to
	 * "4: Flammable gas or extremely flammable liquid"
	 * 
	 * @param value
	 * @return
	 */
	public String validateA0635(String value) {

		if ("0".equalsIgnoreCase(value)) {
			return "0: Not combustible";
		}
		if ("1".equalsIgnoreCase(value)) {
			return "1: Combustible If heated";
		}
		if ("2".equalsIgnoreCase(value)) {
			return "2: Combustible If liquid flash point of 100 to 200F";
		}
		if ("3".equalsIgnoreCase(value)) {
			return "3: Flammable liquid flash point below 100F";
		}
		if ("4".equalsIgnoreCase(value)) {
			return "4: Flammable gas or extremely flammable liquid";
		}
		return value;
	}

	public Map<String, String> validationProcess(Map<String, String> attributeMap) {

		if (attributeMap.containsKey("A0136")) {
			String value = validateA0136(attributeMap.get("A0136"));
			attributeMap.put("A0136", value);

		}
		if (attributeMap.containsKey("A0113") && attributeMap.containsKey("A0657")) {
			String value = validateA0113(attributeMap.get("A0113"), attributeMap.get("A0657"));
			attributeMap.put("A0113", value);

		}
		if (attributeMap.containsKey("A0138")) {
			String value = validateA0138(attributeMap.get("A0138"));
			attributeMap.put("A0138", value);

		}
		if (attributeMap.containsKey("A0140")) {
			String value = validateA0140(attributeMap.get("A0140"));
			attributeMap.put("A0140", value);

		}
		if (attributeMap.containsKey("A0137")) {
			String value = validateA0137(attributeMap.get("A0137"));
			attributeMap.put("A0137", value);

		}
		if (attributeMap.containsKey("A0662")) {
			String value = validateA0662(attributeMap.get("A0662"));
			attributeMap.put("A0662", value);

		}
		if (attributeMap.containsKey("A0090") && (attributeMap.containsKey("A0099") || attributeMap.containsKey("A0997"))) {
			String value = validateA0090(attributeMap.get("A0090"), attributeMap.get("A0099"), attributeMap.get("A0997"));
			attributeMap.put("A0090", value);
		}
		if (attributeMap.containsKey("A0997")) {
			validateA0997(attributeMap.get("A0997"), attributeMap);
		}
		if (attributeMap.containsKey("A0107")) {
			String value = validateA0107(attributeMap.get("A0107"));
			attributeMap.put("A0107", value);

		}
		if (attributeMap.containsKey("A0107")) {
			String value = validateA0107(attributeMap.get("A0107"));
			attributeMap.put("A0107", value);

		}
		if (attributeMap.containsKey("A0135")) {
			String value = validateA0135(attributeMap.get("A0135"));
			attributeMap.put("A0135", value);

		}
		if (attributeMap.containsKey("A0090") && attributeMap.containsKey("A0134")
				&& (attributeMap.containsKey("A0787") && attributeMap.containsKey("A0098"))) {
			String value = validateA0787(attributeMap.get("A0090"), attributeMap.get("A0134"), attributeMap.get("A0787"),
					attributeMap.get("A0098"));
			attributeMap.put("A0787", value);
		}
		if (attributeMap.containsKey("A0090") && attributeMap.containsKey("A0134") && (attributeMap.containsKey("A0105"))) {
			String value = validateA0105(attributeMap.get("A0090"), attributeMap.get("A0134"), attributeMap.get("A0105"));
			attributeMap.put("A0105", value);
		}

		if (attributeMap.containsKey("A0111") && attributeMap.containsKey("A0657")) {
			String value = validateA0111(attributeMap.get("A0111"), attributeMap.get("A0657"));
			attributeMap.put("A0111", value);

		}
		if (attributeMap.containsKey("A0114") && attributeMap.containsKey("A0657")) {
			String value = validateA0114(attributeMap.get("A0114"), attributeMap.get("A0657"));
			attributeMap.put("A0114", value);

		}
		if (attributeMap.containsKey("A0115") && attributeMap.containsKey("A0657")) {
			String value = validateA0115(attributeMap.get("A0115"), attributeMap.get("A0657"));
			attributeMap.put("A0115", value);

		}
		if (attributeMap.containsKey("A0116") && attributeMap.containsKey("A2012")) {
			String value = validateA0116(attributeMap.get("A2012"), attributeMap.get("A0116"));
			attributeMap.put("A0116", value);

		}
		if (attributeMap.containsKey("A0117") && (attributeMap.containsKey("A0109") || attributeMap.containsKey("A0116"))) {
			String value = validateA0117(attributeMap.get("A0117"), attributeMap.get("A0109"), attributeMap.get("A0116"));
			attributeMap.put("A0117", value);
		}
		if (attributeMap.containsKey("A0644")) {
			String value = validateA0644(attributeMap.get("A0644"));
			attributeMap.put("A0644", value);

		}
		if (attributeMap.containsKey("A0090") && attributeMap.containsKey("A0134")
				&& (attributeMap.containsKey("A0098") && attributeMap.containsKey("A0787"))) {
			String value = validateA0134(attributeMap.get("A0090"), attributeMap.get("A0134"), attributeMap.get("A0098"),
					attributeMap.get("A0787"));
			attributeMap.put("A0098", value);
		}
		if (attributeMap.containsKey("A0657") && attributeMap.containsKey("A0104")) {
			String value = validateA0104(attributeMap.get("A0657"), attributeMap.get("A0104"));
			attributeMap.put("A0104", value);

		}

		if (attributeMap.containsKey("A0125")
				&& (attributeMap.containsKey("A0137") || attributeMap.containsKey("A0138") || attributeMap.containsKey("A0140") || attributeMap
						.containsKey("A0124") && attributeMap.containsKey("A0662"))) {
			String value = validateA0125(attributeMap.get("A0137"), attributeMap.get("A0138"), attributeMap.get("A0140"),
					attributeMap.get("A0124"), attributeMap.get("A0662"), attributeMap.get("A0125"));
			attributeMap.put("A0125", value);
		}

		if (attributeMap.containsKey("A0102") && (attributeMap.containsKey("A0090") || attributeMap.containsKey("A0134"))) {
			String value = validateA0102(attributeMap.get("A0090"), attributeMap.get("A0134"), attributeMap.get("A0102"));
			attributeMap.put("A0102", value);
		}
		if (attributeMap.containsKey("A0120")) {
			String value = validateA0120(attributeMap.get("A0120"));
			attributeMap.put("A0120", value);

		}
		if (attributeMap.containsKey("A0099") && attributeMap.containsKey("A0045")) {
			String value = validateA0145(attributeMap.get("A0099"), attributeMap.get("A0145"));
			attributeMap.put("A0145", value);

		}
		if (attributeMap.containsKey("A0099") && attributeMap.containsKey("A0147")) {
			String value = validateA0147(attributeMap.get("A0099"), attributeMap.get("A0147"));
			attributeMap.put("A0147", value);

		}
		if (attributeMap.containsKey("A0635")) {
			String value = validateA0635(attributeMap.get("A0635"));
			attributeMap.put("A0635", value);

		}
		if (attributeMap.containsKey("A0621")) {
			String value = validateA0621(attributeMap.get("A0621"));
			attributeMap.put("A0621", value);

		}
		if (attributeMap.containsKey("A0141")) {
			String value = validateA0141(attributeMap.get("A0141"));
			attributeMap.put("A0141", value);

		}

		return attributeMap;

	}
}
