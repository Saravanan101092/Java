
package com.staples.pim.delegate.productupdate.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EMPTY_STR;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Map;

import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.productupdate.runner.ProductInboundScheduler;

/**
 * @author 522462
 * 
 */
public class ProductInboundTransformation {

	public static final String	Y_STR				= "Y";

	private final String		A0013_NAD_STR		= "A0013_NAD";

	private final String		A0013_RET_STR		= "A0013_RET";

	public final String			N_STR				= "N";

	private final String		A0144_STR			= "A0144";

	private final String		A0146_STR			= "A0146";

	private final String		A0224_STR			= "A0224";

	public final String			A0090_STR			= "A0090";

	public final String			A0080_STR			= "A0080";

	public final String			A0082_STR			= "A0082";

	public final String			A0083_STR			= "A0083";

	public final String			A0084_STR			= "A0084";

	public final String			A0024_STR			= "A0024";

	public final String			A0213_STR			= "A0213";

	public final String			A0254_STR			= "A0254";

	public final String			A0026_STR			= "A0026";

	public final String			A0412_STR			= "A0412";

	public final String			A0085_STR			= "A0085";

	public final String			A0086_STR			= "A0086";

	public final String			A0025_STR			= "A0025";

	public final String			A0411_STR			= "A0411";

	public final String			A0190_STR			= "A0190";

	public final String			A0408_STR			= "A0408";

	public final String			A0210_STR			= "A0210";

	public final String			A0029_STR			= "A0029";

	public final String			A0248_STR			= "A0248";

	public final String			A0214_STR			= "A0214";

	public final String			A0251_STR			= "A0251";

	public final String			A0197_STR			= "A0197";

	public final String			A0037_STR			= "A0037";

	public final String			A0011_STR			= "A0011";

	public final String			A0212_STR			= "A0212";

	public final String			A0043_STR			= "A0043";

	public final String			A0189_STR			= "A0189";

	public final String			A0027_STR			= "A0027";

	public final String			A0230_STR			= "A0230";

	public final String			A0018_RET_STR		= "A0018_RET";

	public final String			A0018_NAD_STR		= "A0018_NAD";

	public final String			A0191_STR			= "A0191";

	public final String			A0015_STR			= "A0015";

	public final String			OH_STR				= "OH";

	public final String			NI_STR				= "NI";

	public final String			A0085_A0086_STR		= "A0085_A0086";

	// data migration
	public final String			A0097_STR			= "A0097";

	public final String			A0098_STR			= "A0098";

	public final String			A0110_STR			= "A0110";

	public final String			A0130_STR			= "A0130";

	public final String			A0168_STR			= "A0168";

	public final String			A0169_STR			= "A0169";

	public final String			A0171_STR			= "A0171";

	public final String			A0181_STR			= "A0181";

	public final String			A0182_STR			= "A0182";

	public final String			A0185_STR			= "A0185";

	public final String			A0186_STR			= "A0186";

	public final String			A0194_STR			= "A0194";

	public final String			A0243_STR			= "A0243";

	public final String			A0244_STR			= "A0244";

	private String				invalidDateFormat	= "01-01-0001";

	/**
	 * @param attributeMap
	 * @return
	 * @throws ParseException
	 */
	public Map<String, String> transformationProcess(Map<String, String> attributeMap) throws ParseException {

		setDefaultValues(attributeMap);

		if (attributeMap != null && !attributeMap.isEmpty()) {

			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0213_STR)) && !IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0254_STR))) {

				String a0213_temp = attributeMap.get(A0213_STR);
				// Added for trim the left side Zero(s)
				if (IntgSrvUtils.isNumeric(a0213_temp)) {
					a0213_temp = "" + IntgSrvUtils.toInt(a0213_temp);
				}
				String A0213 = a0213_temp + " : " + attributeMap.get(A0254_STR);
				attributeMap.put(A0213_STR, A0213);
			}

			// A0085 = read from properties file (country code)
			if (attributeMap.get(A0085_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0085_STR, attributeMap.get(A0085_STR), Boolean.TRUE);
				attributeMap.put(A0085_STR, value);
			}

			// A0086 = read from properties file (country code)
			if (attributeMap.get(A0086_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0086_STR, attributeMap.get(A0086_STR), Boolean.TRUE);
				attributeMap.put(A0086_STR, value);
			}

			// A0190 = read from properties file (distribution center)
			if (attributeMap.get(A0190_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0190_STR, attributeMap.get(A0190_STR), Boolean.TRUE);
				attributeMap.put(A0190_STR, value);
			}

			// A0210 = read from properties (Selling uom)
			if (attributeMap.get(A0210_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0210_STR, attributeMap.get(A0210_STR), Boolean.TRUE);
				attributeMap.put(A0210_STR, value);
			}

			// A0029 = A0029 + A0248.replace(A0029, :)
			if (attributeMap.get(A0029_STR) != null) {
				String A0029 = DatamigrationCommonUtil.getConcatenateValue(attributeMap.get(A0029_STR), attributeMap.get(A0248_STR),
						Boolean.TRUE);
				attributeMap.put(A0029_STR, A0029);
			}

			// A0214 = A0214 + A0251.replace(A0214, :)
			if (attributeMap.get(A0214_STR) != null) {
				String A0214 = DatamigrationCommonUtil.getConcatenateValue(attributeMap.get(A0214_STR), attributeMap.get(A0251_STR),
						Boolean.FALSE);
				if (attributeMap.get(A0214_STR).length() == 1) {
					A0214 = "0" + A0214;
				}
				attributeMap.put(A0214_STR, A0214);
			}

			if (attributeMap.get(A0197_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0197_STR, attributeMap.get(A0197_STR), Boolean.FALSE);
				attributeMap.put(A0197_STR, value);
			}
			if (attributeMap.get(A0191_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0191_STR, attributeMap.get(A0191_STR), Boolean.TRUE);
				attributeMap.put(A0191_STR, value);
			}
			if (attributeMap.get(A0037_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0037_STR, attributeMap.get(A0037_STR), Boolean.FALSE);
				attributeMap.put(A0037_STR, value);
			}
			if (attributeMap.get(A0011_STR) != null) {
				//PCMP-2807-applying split on incoming attribute to handle 1 NON RESTRICTED,2 RESTRICTED,R REMOVE,'3 NON RESTRICTED COMMON MIX,4 Staples Industrial HR
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0011_STR, attributeMap.get(A0011_STR).split(" ")[0], Boolean.TRUE);
				attributeMap.put(A0011_STR, value);
			}
			if (attributeMap.get(A0212_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0212_STR, attributeMap.get(A0212_STR), Boolean.TRUE);
				attributeMap.put(A0212_STR, value);
			}
			if (attributeMap.get(A0043_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0043_STR, attributeMap.get(A0043_STR), Boolean.TRUE);
				attributeMap.put(A0043_STR, value);
			}
			if (attributeMap.get(A0189_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0189_STR, attributeMap.get(A0189_STR), Boolean.TRUE);
				attributeMap.put(A0189_STR, value);
			}
			if (attributeMap.get(A0027_STR) != null) {
				// PCMP- XXXX - As rama & krishna requested, A0027 is defaulted
				// with A0026
				// String value =
				// getValuesFromLOV(A0027_STR,attributeMap.get(A0027_STR),Boolean.TRUE);
				// attributeMap.put(A0027_STR, value);
				attributeMap.put(A0027_STR, attributeMap.get(A0026_STR));
			}
			if (attributeMap.get(A0230_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0230_STR, attributeMap.get(A0230_STR), Boolean.TRUE);
				attributeMap.put(A0230_STR, value);
			}

			if (attributeMap.get(A0018_RET_STR) != null) {
				String value = attributeMap.get(A0018_RET_STR).toUpperCase();
				attributeMap.put(A0018_RET_STR, value);
			}
			if (attributeMap.get(A0018_NAD_STR) != null) {
				String value = attributeMap.get(A0018_NAD_STR).toUpperCase();
				attributeMap.put(A0018_NAD_STR, value);
			}
			// A0015 if NI --> Y else if OH --> N
			if (attributeMap.get(A0015_STR) != null) {
				String value = attributeMap.get(A0015_STR);
				if (OH_STR.equalsIgnoreCase(value)) {
					value = N_STR;
				} else if (NI_STR.equalsIgnoreCase(value)) {
					value = Y_STR;
				} else {
					value = DatamigrationCommonUtil.converYESNOIntoChar(value);
				}
				attributeMap.put(A0015_STR, value);
			}

			if (attributeMap.get(A0097_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0097_STR, attributeMap.get(A0097_STR), Boolean.TRUE);
				attributeMap.put(A0097_STR, value);
			}
			/*
			 * if(attributeMap.get(A0098_STR)!=null) { String value =
			 * getValuesFromLOV
			 * (A0098_STR,attributeMap.get(A0098_STR),Boolean.TRUE);
			 * attributeMap.put(A0098_STR, value); }
			 */

			// PCMP-1472 - done upper case transformation
			if (attributeMap.get(A0110_STR) != null) {
				// String value =
				// getValuesFromLOV(A0110_STR,attributeMap.get(A0110_STR),Boolean.TRUE);

				attributeMap.put(A0110_STR, attributeMap.get(A0110_STR).toUpperCase());
			}
			if (attributeMap.get(A0130_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOVForCaseMatch(A0130_STR, attributeMap.get(A0130_STR));
				attributeMap.put(A0130_STR, value);
			}
			if (attributeMap.get(A0168_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0168_STR, attributeMap.get(A0168_STR), Boolean.TRUE);
				attributeMap.put(A0168_STR, value);
			}
			if (attributeMap.get(A0169_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0169_STR, attributeMap.get(A0169_STR), Boolean.TRUE);
				attributeMap.put(A0169_STR, value);
			}
			if (attributeMap.get(A0171_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0171_STR, attributeMap.get(A0171_STR), Boolean.TRUE);
				attributeMap.put(A0171_STR, value);
			}
			if (attributeMap.get(A0181_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0181_STR, attributeMap.get(A0181_STR), Boolean.TRUE);
				attributeMap.put(A0181_STR, value);
			}
			if (attributeMap.get(A0182_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0182_STR, attributeMap.get(A0182_STR), Boolean.TRUE);
				attributeMap.put(A0182_STR, value);
			}
			if (attributeMap.get(A0185_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0185_STR, attributeMap.get(A0185_STR), Boolean.TRUE);
				attributeMap.put(A0185_STR, value);
			}
			if (attributeMap.get(A0186_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0186_STR, attributeMap.get(A0186_STR), Boolean.TRUE);
				attributeMap.put(A0186_STR, value);
			}
			if (attributeMap.get(A0194_STR) != null) {
				if (attributeMap.get("A0195") != null
						&& (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0195")) || (DatamigrationCommonUtil.toDouble(attributeMap
								.get("A0195")) > 0.0))) {
					attributeMap.put(A0194_STR, "none");
				} else {
					attributeMap.put(A0194_STR, "DSP");
				}
				/*
				 * String value =
				 * getValuesFromLOV(A0194_STR,attributeMap.get(A0194_STR
				 * ),Boolean.TRUE); attributeMap.put(A0194_STR, value);
				 */
			}
			if (attributeMap.get(A0243_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0243_STR, attributeMap.get(A0243_STR), Boolean.TRUE);
				attributeMap.put(A0243_STR, value);
			}
			if (attributeMap.get(A0244_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0244_STR, attributeMap.get(A0244_STR), Boolean.TRUE);
				attributeMap.put(A0244_STR, value);
			}
			if (attributeMap.get("A0144") != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0144_STR, attributeMap.get(A0144_STR), Boolean.TRUE);
				attributeMap.put(A0144_STR, value);
			}
			/*
			 * if(attributeMap.get("A0145")!=null) { String value =
			 * getValuesFromLOV("A0145",attributeMap.get("A0145"),Boolean.TRUE);
			 * attributeMap.put("A0145", value); }
			 */
			if (attributeMap.get(A0146_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0146_STR, attributeMap.get(A0146_STR), Boolean.TRUE);
				attributeMap.put(A0146_STR, value);
			}
			/*
			 * if(attributeMap.get("A0147")!=null) { String value =
			 * getValuesFromLOV("A0147",attributeMap.get("A0147"),Boolean.TRUE);
			 * attributeMap.put("A0147", value); }
			 */
			if (attributeMap.get(A0224_STR) != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV(A0224_STR, attributeMap.get(A0224_STR), Boolean.TRUE);
				attributeMap.put(A0224_STR, value);
			}
			// PCMP-1460 lower case flag is changed to upper case
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0090_STR))) {
				String value = DatamigrationCommonUtil.converYESNOIntoChar(attributeMap.get(A0090_STR));
				attributeMap.put(A0090_STR, value);
			}
			// For PCMP-1718 Rule 6680: A0339:Environmental Design Features must
			// be Y
			if (Y_STR.equalsIgnoreCase(attributeMap.get("A0173"))
					|| Y_STR.equalsIgnoreCase(attributeMap.get("A0174"))
					|| (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0171")) && !N_STR.equalsIgnoreCase(DatamigrationCommonUtil
							.converYESNOIntoChar(attributeMap.get("A0171"))))
					|| (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0172")) && !N_STR.equalsIgnoreCase(DatamigrationCommonUtil
							.converYESNOIntoChar(attributeMap.get("A0172"))))) {
				attributeMap.put("A0339", Y_STR);
			}
			//PCMP-2047 As IARebuyer (A0028 and A0304) is changed to LOV in STEP
			if (attributeMap.get("A0028") != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV("A0028", attributeMap.get("A0028"), Boolean.TRUE);
				attributeMap.put("A0028", value);
			}
			if (attributeMap.get("A0304") != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV("A0304", attributeMap.get("A0304"), Boolean.TRUE);
				attributeMap.put("A0304", value);
			}
			
			//PCMP-2511
			if (attributeMap.get("A0172") != null) {
				String value = DatamigrationCommonUtil.getValuesFromLOV("A0172", attributeMap.get("A0172"), Boolean.TRUE);
				attributeMap.put("A0172", value);
			}

			setMinimumMaximumValue(attributeMap);

			removeLeadingTrailingSpace(attributeMap);

			converLowerCaseToUpperCase(attributeMap);

			dateFormatValidation(attributeMap);

			doubleToIntegerValidation(attributeMap);
		}

		return attributeMap;
	}

	private void setMinimumMaximumValue(Map<String, String> attributeMap) {

		// set the values to the 0.01 if the current value is < 0.01, and
		// 99999.99 if the current value is > 99999.99
		// A0307 < 0.01 , A0308,A0309, A0310

		// For PCMP-1723
		attributeMap.put("A0307", DatamigrationCommonUtil.setValueBasedOnMinMax(attributeMap.get("A0307"), "0.01", "99999.99", false));
		attributeMap.put("A0308", DatamigrationCommonUtil.setValueBasedOnMinMax(attributeMap.get("A0308"), "0.01", "99999.99", false));
		attributeMap.put("A0309", DatamigrationCommonUtil.setValueBasedOnMinMax(attributeMap.get("A0309"), "0.01", "99999.99", false));
		attributeMap.put("A0310", DatamigrationCommonUtil.setValueBasedOnMinMax(attributeMap.get("A0310"), "0.01", "99999.99", false));
		// For PCMP-1730
		attributeMap.put("A0078_RET",
				DatamigrationCommonUtil.setValueBasedOnMinMax(attributeMap.get("A0078_RET"), "0.1", "99999.99", false));
		attributeMap.put("A0078_NAD",
				DatamigrationCommonUtil.setValueBasedOnMinMax(attributeMap.get("A0078_NAD"), "0.1", "99999.99", false));

		// For PCMP-1840 also PCMP-1392
		attributeMap.put("A0240", DatamigrationCommonUtil.setValueBasedOnMinMax(attributeMap.get("A0240"), "0.01", "99999.99", false));
		attributeMap.put("A0239", DatamigrationCommonUtil.setValueBasedOnMinMax(attributeMap.get("A0239"), "0.01", "99999.99", false));
		attributeMap.put("A0238", DatamigrationCommonUtil.setValueBasedOnMinMax(attributeMap.get("A0238"), "0.01", "99999.99", false));
		attributeMap.put("A0237", DatamigrationCommonUtil.setValueBasedOnMinMax(attributeMap.get("A0237"), "0.01", "99999.99", false));
		attributeMap.put("A0077_RET",
				DatamigrationCommonUtil.setValueBasedOnMinMax(attributeMap.get("A0077_RET"), "0.01", "99999.99", false));
		attributeMap.put("A0077_NAD",
				DatamigrationCommonUtil.setValueBasedOnMinMax(attributeMap.get("A0077_NAD"), "0.01", "99999.99", false));

	}

	/**
	 * @param attributeMap
	 */
	private void converLowerCaseToUpperCase(Map<String, String> attributeMap) {

		// PCMP-1538 to upper case
		String fanIds[] = { "A0174", "A0173", "A0167", "A0164", "A0091", "A0148", "A0149", "A0160", "A0163", "A0157", "A0158", "A0159",
				"A0154", "A0156", "A0117", "A0116", "A0115", "A0113", "A0107", "A0108", "A0103", "A0102", "A0105", "A0141", "A0339",
				"A0134", "A0127", "A0318", "A0142", "A0404", "A0008", "A0140", "A0139", "A0138", "A0137", "A0320", "A0124", "A0135" };

		for (String value : fanIds) {
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(value))) {
				attributeMap.put(value, attributeMap.get(value).toUpperCase());
			}
		}

		// PCMP-1393 - Convert 'n/a' to 'N/A'
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0080_STR)) && "N/A".equalsIgnoreCase(attributeMap.get(A0080_STR))) {
			attributeMap.put(A0080_STR, attributeMap.get(A0083_STR).toUpperCase());
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0082_STR)) && "N/A".equalsIgnoreCase(attributeMap.get(A0082_STR))) {
			attributeMap.put(A0082_STR, attributeMap.get(A0082_STR).toUpperCase());
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0083_STR)) && "N/A".equalsIgnoreCase(attributeMap.get(A0083_STR))) {
			attributeMap.put(A0083_STR, attributeMap.get(A0083_STR).toUpperCase());
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0084_STR)) && "N/A".equalsIgnoreCase(attributeMap.get(A0084_STR))) {
			attributeMap.put(A0084_STR, attributeMap.get(A0084_STR).toUpperCase());
		}

	}

	/**
	 * @param attributeMap
	 */
	private void removeLeadingTrailingSpace(Map<String, String> attributeMap) {

		// PCMP-1748
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0013_RET_STR))) {
			attributeMap.put(A0013_RET_STR, attributeMap.get(A0013_RET_STR).trim());
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0013_NAD_STR))) {
			attributeMap.put(A0013_NAD_STR, attributeMap.get(A0013_NAD_STR).trim());
		}

		// For PCMP-1734
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0018_RET_STR))) {
			attributeMap.put(A0018_RET_STR, attributeMap.get(A0018_RET_STR).trim());
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0018_NAD_STR))) {
			attributeMap.put(A0018_NAD_STR, attributeMap.get(A0018_NAD_STR).trim());
		}
	}

	/**
	 * @param attributeMap
	 */
	public void setDefaultValues(Map<String, String> attributeMap) {

		if (attributeMap.containsKey("A0015") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0015"))) {
			attributeMap.put("A0015", Y_STR);
		}
		if (attributeMap.containsKey("A0022") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0022"))) {
			attributeMap.put("A0022", N_STR);
		}
		if (attributeMap.containsKey("A0036") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0036"))) {
			attributeMap.put("A0036", N_STR);
		}
		if (attributeMap.containsKey("A0037") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0037"))) {
			attributeMap.put("A0037", N_STR);
		}

		if (attributeMap.containsKey("A0043") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0043"))) {
			attributeMap.put("A0043", "EA");
		}

		if (attributeMap.containsKey("A0094") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0094"))) {
			attributeMap.put("A0094", "NA");
		}
		if (attributeMap.containsKey("A0102") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0102"))) {
			attributeMap.put("A0102", N_STR);
		}
		if (attributeMap.containsKey("A0103") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0103"))) {
			attributeMap.put("A0103", N_STR);
		}
		if (attributeMap.containsKey("A0104") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0104"))) {
			attributeMap.put("A0104", N_STR);
		}
		if (attributeMap.containsKey("A0107") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0107"))) {
			attributeMap.put("A0107", N_STR);
		}
		if (attributeMap.containsKey("A0111") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0111"))) {
			attributeMap.put("A0111", N_STR);
		}
		if (attributeMap.containsKey("A0112") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0112"))) {
			attributeMap.put("A0112", "NA");
		}
		if (attributeMap.containsKey("A0113") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0113"))) {
			attributeMap.put("A0113", N_STR);
		}

		if (attributeMap.containsKey("A0115") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0115"))) {
			attributeMap.put("A0115", N_STR);
		}
		if (attributeMap.containsKey("A0116") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0116"))) {
			attributeMap.put("A0116", N_STR);
		}
		if (attributeMap.containsKey("A0117") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0117"))) {
			attributeMap.put("A0117", N_STR);
		}
		if (attributeMap.containsKey("A0124") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0124"))) {
			attributeMap.put("A0124", N_STR);
		}
		if (attributeMap.containsKey("A0127") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0127"))) {
			attributeMap.put("A0127", N_STR);
		}
		if (attributeMap.containsKey(A0090_STR) && IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0090_STR))) {
			attributeMap.put(A0090_STR, N_STR);
		}
		// PCMP-1496 - If A0090-Hazardous Material Flag is 'Y', set
		// A0091-Material Safety Data Sheet (MSDS) Required to 'Y'.
		if (attributeMap.containsKey(A0090_STR) && !IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0090_STR))
				&& Y_STR.equalsIgnoreCase(attributeMap.get(A0090_STR))) {
			attributeMap.put("A0091", Y_STR);
		} else if (attributeMap.containsKey("A0091") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0091"))) {
			attributeMap.put("A0091", N_STR);
		}
		if (attributeMap.containsKey("A0148") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0148"))) {
			attributeMap.put("A0148", N_STR);
		}
		if (attributeMap.containsKey("A0149") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0149"))) {
			attributeMap.put("A0149", N_STR);
		}
		if (attributeMap.containsKey("A0153") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0153"))) {
			attributeMap.put("A0153", N_STR);
		}
		if (attributeMap.containsKey("A0156") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0156"))) {
			attributeMap.put("A0156", N_STR);
		}
		if (attributeMap.containsKey("A0157") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0157"))) {
			attributeMap.put("A0157", N_STR);
		}
		if (attributeMap.containsKey("A0157") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0157"))) {
			attributeMap.put("A0157", N_STR);
		}
		if (attributeMap.containsKey("A0158") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0158"))) {
			attributeMap.put("A0158", N_STR);
		}
		if (attributeMap.containsKey("A0159") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0159"))) {
			attributeMap.put("A0159", N_STR);
		}
		if (attributeMap.containsKey("A0160") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0160"))) {
			attributeMap.put("A0160", N_STR);
		}
		if (attributeMap.containsKey("A0163") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0163"))) {
			attributeMap.put("A0163", N_STR);
		}
		if (attributeMap.containsKey("A0164") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0164"))) {
			attributeMap.put("A0164", N_STR);
		}
		if (attributeMap.containsKey("A0167") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0167"))) {
			attributeMap.put("A0167", N_STR);
		}
		if (attributeMap.containsKey("A0173") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0173"))) {
			attributeMap.put("A0173", N_STR);
		}
		if (attributeMap.containsKey("A0174") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0174"))) {
			attributeMap.put("A0174", N_STR);
		}
		if (attributeMap.containsKey("A0175") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0175"))) {
			attributeMap.put("A0175", "0");
		}
		if (attributeMap.containsKey("A0179") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0179"))) {
			attributeMap.put("A0179", N_STR);
		}
		if (attributeMap.containsKey("A0183") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0183"))) {
			attributeMap.put("A0183", N_STR);
		}
		if (attributeMap.containsKey("A0339") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0339"))) {
			attributeMap.put("A0339", N_STR);
		}
		if (attributeMap.containsKey("A0404") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0404"))) {
			attributeMap.put("A0404", N_STR);
		}
		if (attributeMap.containsKey("A0189") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0189"))) {
			attributeMap.put("A0189", "S01");
		}
		if (attributeMap.containsKey("A0190")
				&& (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0190")) || (attributeMap.get("A0190").equalsIgnoreCase("W01")))) {
			attributeMap.put("A0190", "W02");
		}
		if (attributeMap.containsKey("A0191")
				&& (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0191")) || (attributeMap.get("A0191").equalsIgnoreCase("N01")))) {
			attributeMap.put("A0191", "N02");
		}
		if (attributeMap.containsKey("A0197") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0197"))) {
			attributeMap.put("A0197", "01");
		}
		// Added As per katie recommendation 07/24
		if (attributeMap.containsKey("A0020")
				&& Y_STR.equalsIgnoreCase(DatamigrationCommonUtil.converYESNOIntoChar(attributeMap.get("A0008")))) {
			attributeMap.put("A0020", "P");
		}
		// PCMP-1461 - Added As per katie recommendation 07/24
		// A0211 - Sell Unit of Measure Quantity
		// A0210 - Sell Unit of Measure
		String a0211Value = attributeMap.get("A0211");
		String a0210Value = attributeMap.get("A0210");
		// PCMP-1717 Added BD, RM and PD transformation added
		if (attributeMap.containsKey("A0211")
				&& ("BX".equalsIgnoreCase(a0210Value) || "PK".equalsIgnoreCase(a0210Value) || "RM".equalsIgnoreCase(a0210Value)
						|| "BD".equalsIgnoreCase(a0210Value) || "PD".equalsIgnoreCase(a0210Value) || "LT".equalsIgnoreCase(a0210Value))) {

			if (!(IntgSrvUtils.isNumeric(a0211Value) && IntgSrvUtils.toDouble(a0211Value) >= 2.0)) {
				attributeMap.put("A0211", "2");
			}
		} else if (attributeMap.containsKey("A0211") && (IntgSrvUtils.isNullOrEmpty(a0211Value) || "CT".equalsIgnoreCase(a0210Value))) {
			if (!(IntgSrvUtils.isNumeric(a0211Value) && IntgSrvUtils.toDouble(a0211Value) >= 1.0)) {
				attributeMap.put("A0211", "1");
			}

		} else if (attributeMap.containsKey("A0211") && (IntgSrvUtils.isNullOrEmpty(a0211Value) || "EA".equalsIgnoreCase(a0210Value))) {
			attributeMap.put("A0211", "1");
		}
		/*
		 * else if(attributeMap.containsKey("A0211") &&
		 * (IntgSrvUtils.isNullOrEmpty(a0211Value) ||
		 * "PK".equalsIgnoreCase(a0210Value))) {
		 * if(IntgSrvUtils.isNumeric(a0211Value) &&
		 * IntgSrvUtils.toDouble(a0211Value)<2.0){ attributeMap.put("A0211",
		 * "2"); }
		 * 
		 * }
		 */
		else if (attributeMap.containsKey("A0211") && (IntgSrvUtils.isNullOrEmpty(a0211Value) || "DZ".equalsIgnoreCase(a0210Value))) {
			// if(IntgSrvUtils.isNumeric(a0211Value) &&
			// IntgSrvUtils.toDouble(a0211Value)<12.0){
			attributeMap.put("A0211", "12");
			// }

		}
		/*if (attributeMap.containsKey("A0229") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0229"))) {
			attributeMap.put("A0229", "1");
		}
		*/if (attributeMap.containsKey("A0155") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0155"))) {
			attributeMap.put("A0155", N_STR);
		}
		if (attributeMap.containsKey("A0504") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0504"))) {
			attributeMap.put("A0504", N_STR);
		}
		if (attributeMap.containsKey("A0085") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0085"))
				&& IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0086"))) {
			attributeMap.put("A0085", "UN");
		}
		// PCMP-1410
		if (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0430"))) {
			if (attributeMap.get(A0080_STR).equalsIgnoreCase("N/A"))
				attributeMap.put("A0430", "");
			else
				attributeMap.put("A0430", Y_STR);
		}

		// For PCMP-1370
		if (attributeMap.containsKey("A0499") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0499"))) {
			attributeMap.put("A0499", N_STR);
		}

		// For A0150 and A0151, if 0 default to empty string
		if (attributeMap.containsKey("A0150") && DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0150"))) {
			attributeMap.put("A0150", EMPTY_STR);
		}

		// Commented for PCMP-1392
		/*
		 * if(attributeMap.containsKey("A0151") &&
		 * DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0151"))) {
		 * attributeMap.put("A0151", EMPTY_STR); }
		 */

		// PCMP-1389
		if (attributeMap.containsKey("A0178") && attributeMap.get("A0178") != null
				&& DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0178"))) {
			attributeMap.put("A0178", EMPTY_STR);
		}

		// If NA default to empty string
		if (attributeMap.containsKey("A0095") && isNAValue(attributeMap.get("A0095"))) {
			attributeMap.put("A0095", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0096") && isNAValue(attributeMap.get("A0096"))) {
			attributeMap.put("A0096", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0099") && isNAValue(attributeMap.get("A0099"))) {
			attributeMap.put("A0099", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0109") && isNAValue(attributeMap.get("A0109"))) {
			attributeMap.put("A0109", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0118") && isNAValue(attributeMap.get("A0118"))) {
			attributeMap.put("A0118", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0119") && isNAValue(attributeMap.get("A0119"))) {
			attributeMap.put("A0119", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0120") && isNAValue(attributeMap.get("A0120"))) {
			attributeMap.put("A0120", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0121") && isNAValue(attributeMap.get("A0121"))) {
			attributeMap.put("A0121", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0130") && isNAValue(attributeMap.get("A0130"))) {
			attributeMap.put("A0130", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0136") && isNAValue(attributeMap.get("A0136"))) {
			attributeMap.put("A0136", EMPTY_STR);
		}

		if (attributeMap.containsKey("A0097") && isNAValue(attributeMap.get("A0097"))) {
			attributeMap.put("A0097", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0098") && isNAValue(attributeMap.get("A0098"))) {
			attributeMap.put("A0098", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0100") && isNAValue(attributeMap.get("A0100"))) {
			attributeMap.put("A0100", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0110") && isNAValue(attributeMap.get("A0110"))) {
			attributeMap.put("A0110", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0134") && isNAValue(attributeMap.get("A0134"))) {
			attributeMap.put("A0134", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0135") && isNAValue(attributeMap.get("A0135"))) {
			attributeMap.put("A0135", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0144") && isNAValue(attributeMap.get("A0144"))) {
			attributeMap.put("A0144", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0145") && isNAValue(attributeMap.get("A0145"))) {
			attributeMap.put("A0145", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0146") && isNAValue(attributeMap.get("A0146"))) {
			attributeMap.put("A0146", EMPTY_STR);
		}
		if (attributeMap.containsKey("A0147") && isNAValue(attributeMap.get("A0147"))) {
			attributeMap.put("A0147", EMPTY_STR);
		}
		// For PCMP-1740
		if (attributeMap.containsKey("A0111") && isNAValue(attributeMap.get("A0111"))) {
			attributeMap.put("A0111", EMPTY_STR);
		}
		// For PCMP-1739
		if (attributeMap.containsKey("A0114") && isNAValue(attributeMap.get("A0114"))) {
			attributeMap.put("A0114", N_STR);
		}

		// precision
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0052"))) {
			attributeMap.put("A0052", setPrecision(attributeMap.get("A0052")));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0071"))) {
			attributeMap.put("A0071", setPrecision(attributeMap.get("A0071")));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0200"))) {
			attributeMap.put("A0200", setPrecision(attributeMap.get("A0200")));
		}

		// Set as 1.0 if 0 PCMP-1388
		if (attributeMap.containsKey("A0220")) {
			if (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0220")) || DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0220"))) {
				attributeMap.put("A0220", "1.0");
			}

		}
		if (attributeMap.containsKey("A0068")) {
			if (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0068")) || DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0068"))) {
				attributeMap.put("A0068", "1.0");
			}

		}
		if (attributeMap.containsKey("A0069")) {
			if (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0069")) || DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0069"))) {
				attributeMap.put("A0069", "1.0");
			}

		}
		if (attributeMap.containsKey("A0070")) {
			if (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0070")) || DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0070"))) {
				attributeMap.put("A0070", "1.0");
			}

		}

		if (attributeMap.containsKey(A0080_STR) && isUPCNeedToDefault(attributeMap.get(A0080_STR))) {
			attributeMap.put(A0080_STR, "N/A");
		} else if (!isNAValue(attributeMap.get(A0080_STR))
				&& !IntgSrvUtils.isNullOrEmpty(DatamigrationCommonUtil.splitValidUPC(attributeMap.get(A0080_STR)))) {
			String value = DatamigrationCommonUtil.splitValidUPC(attributeMap.get(A0080_STR));
			value = DatamigrationCommonUtil.addLeadingCharacter(value, 12, '0');
			attributeMap.put(A0080_STR, value);
		}

		if (attributeMap.containsKey(A0083_STR) && isUPCNeedToDefault(attributeMap.get(A0083_STR))) {
			attributeMap.put(A0083_STR, "N/A");
		} else if (!isNAValue(attributeMap.get(A0083_STR))
				&& !IntgSrvUtils.isNullOrEmpty(DatamigrationCommonUtil.splitValidUPC(attributeMap.get(A0083_STR)))) {
			String value = DatamigrationCommonUtil.splitValidUPC(attributeMap.get(A0083_STR));
			value = DatamigrationCommonUtil.addLeadingCharacter(value, 12, '0');
			attributeMap.put(A0083_STR, value);
		}
		if (attributeMap.containsKey(A0084_STR) && isUPCNeedToDefault(attributeMap.get(A0084_STR))) {
			attributeMap.put(A0084_STR, "N/A");
		} else if (!isNAValue(attributeMap.get(A0084_STR))
				&& !IntgSrvUtils.isNullOrEmpty(DatamigrationCommonUtil.splitValidUPC(attributeMap.get(A0084_STR)))) {
			String value = DatamigrationCommonUtil.splitValidUPC(attributeMap.get(A0084_STR));
			value = DatamigrationCommonUtil.addLeadingCharacter(value, 12, '0');
			attributeMap.put(A0084_STR, value);
		}
		if (attributeMap.containsKey(A0082_STR) && isUPCNeedToDefault(attributeMap.get(A0082_STR))) {
			attributeMap.put(A0082_STR, "N/A");
		} else if (!isNAValue(attributeMap.get(A0082_STR))
				&& !IntgSrvUtils.isNullOrEmpty(DatamigrationCommonUtil.splitValidUPC(attributeMap.get(A0082_STR)))) {
			String value = DatamigrationCommonUtil.splitValidUPC(attributeMap.get(A0082_STR));
			value = DatamigrationCommonUtil.addLeadingCharacter(value, 12, '0');
			attributeMap.put(A0082_STR, value);
		}

		if (attributeMap.containsKey("A0077_RET") && !ProductInboundValidation.isGreaterThanZero(attributeMap.get("A0077_RET"))) {
			attributeMap.put("A0077_RET", "0.01");
		}
		if (attributeMap.containsKey("A0077_NAD") && !ProductInboundValidation.isGreaterThanZero(attributeMap.get("A0077_NAD"))) {
			attributeMap.put("A0077_NAD", "0.01");
		}
		if (attributeMap.containsKey("A0078_RET") && !ProductInboundValidation.isGreaterThanZero(attributeMap.get("A0078_RET"))) {
			attributeMap.put("A0078_RET", "0.01");
		}
		if (attributeMap.containsKey("A0078_NAD") && !ProductInboundValidation.isGreaterThanZero(attributeMap.get("A0078_NAD"))) {
			attributeMap.put("A0078_NAD", "0.01");
		}
		if (attributeMap.containsKey("A0067_RET") && !ProductInboundValidation.isGreaterThanZero(attributeMap.get("A0067_RET"))) {
			attributeMap.put("A0067_RET", "1");
		}
		if (attributeMap.containsKey("A0067_NAD") && !ProductInboundValidation.isGreaterThanZero(attributeMap.get("A0067_NAD"))) {
			attributeMap.put("A0067_NAD", "1");
		}
		// If A0052 is null or empty or 0, set A0077_ret's value to it
		if (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0052")) || DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0052"))) {
			attributeMap.put("A0052", attributeMap.get("A0077_RET"));
		}
		// PCMP-1318 - This transformation may be removed after fixing this
		// issue in PIMCore
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0178"))) {
			String value = DatamigrationCommonUtil.replaceDotTrimExpon(attributeMap.get("A0178"));
			attributeMap.put("A0178", value);
		}

		// For PCMP-1329
		if (attributeMap.containsKey("A0077_RET") && attributeMap.containsKey("A0077_NAD")) {
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0077_RET")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0077_NAD"))) {
				attributeMap.put("A0077_NAD", attributeMap.get("A0077_RET"));
			}
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0077_NAD")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0077_RET"))) {
				attributeMap.put("A0077_RET", attributeMap.get("A0077_NAD"));
			}
			if (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0077_RET")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0077_NAD"))) {
				attributeMap.put("A0077_RET", "0.01");
				attributeMap.put("A0077_NAD", "0.01");
			}

		}
		// For PCMP-1327
		if (attributeMap.containsKey("A0078_RET") && attributeMap.containsKey("A0078_NAD")) {
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0078_RET")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0078_NAD"))) {
				attributeMap.put("A0078_NAD", attributeMap.get("A0078_RET"));
			}
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0078_NAD")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0078_RET"))) {
				attributeMap.put("A0078_RET", attributeMap.get("A0078_NAD"));
			}
			if (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0078_RET")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0078_NAD"))) {
				attributeMap.put("A0078_RET", "0.01");
				attributeMap.put("A0078_NAD", "0.01");
			}

		}

		// For PCMP-1328
		if (attributeMap.containsKey("A0067_RET") && attributeMap.containsKey("A0067_NAD")) {
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0067_RET")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0067_NAD"))) {
				attributeMap.put("A0067_NAD", attributeMap.get("A0067_RET"));
			}
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0067_NAD")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0067_RET"))) {
				attributeMap.put("A0067_RET", attributeMap.get("A0067_NAD"));
			}
			if (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0067_RET")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0067_NAD"))) {
				attributeMap.put("A0067_RET", "1");
				attributeMap.put("A0067_NAD", "1");
			}
		}
		// PCMP-1549
		if (attributeMap.containsKey("A0018_RET") && attributeMap.containsKey("A0018_NAD")) {
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0018_RET")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0018_NAD"))) {
				attributeMap.put("A0018_NAD", attributeMap.get("A0018_RET"));
			}
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0018_NAD")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0018_RET"))) {
				attributeMap.put("A0018_RET", attributeMap.get("A0018_NAD"));
			}
		}

		// PCMP-1550
		if (attributeMap.containsKey("A0075_RET") && attributeMap.containsKey("A0075_NAD")) {
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0075_RET")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0075_NAD"))) {
				attributeMap.put("A0075_NAD", attributeMap.get("A0075_RET"));
			}
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0075_NAD")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0075_RET"))) {
				attributeMap.put("A0075_RET", attributeMap.get("A0075_NAD"));
			}
		}

		// For PCMP-1325
		if (attributeMap.containsKey("A0243") && attributeMap.containsKey("A0244")) {
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0244")) && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0243"))) {
				attributeMap.put("A0244", "1");
			}
		}
		// For PCMP-1326
		if (attributeMap.containsKey("A0017") && attributeMap.get("A0017").equalsIgnoreCase(Y_STR)) {
			if (attributeMap.containsKey("A0051") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0051"))) {
				attributeMap.put("A0051", "0.01");
			}
		}
		// For PCMP-1348
		// if(attributeMap.containsKey(A0090_STR) &&
		// attributeMap.get(A0090_STR).equalsIgnoreCase(Y_STR))
		if (attributeMap.containsKey("A0092")
				&& (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0092")) || isNAValue(attributeMap.get("A0092").trim()))) {
			attributeMap.put("A0092", "http://sds.staples.com/msds/" + attributeMap.get("A0012") + ".pdf");
		}

		if (attributeMap.containsKey("A0093")
				&& (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0093")) || isNAValue(attributeMap.get("A0093").trim()))) {
			attributeMap.put("A0093", attributeMap.get("A0012") + ".pdf");
		}
		// For PCMP-1350
		// if(attributeMap.containsKey(A0090_STR) &&
		// attributeMap.get(A0090_STR).equalsIgnoreCase(Y_STR))
		if (attributeMap.containsKey("A0118") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0118"))) {
			attributeMap.put("A0118", attributeMap.get("A0012") + "SL.pdf");
		}

		// if(attributeMap.containsKey(A0090_STR) &&
		// attributeMap.get(A0090_STR).equalsIgnoreCase(Y_STR))
		if (attributeMap.containsKey("A0119") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0119"))) {
			attributeMap.put("A0119", "http://sds.staples.com/sl/" + attributeMap.get("A0012") + "SL.pdf");
		}

		// if(attributeMap.containsKey(A0090_STR) &&
		// attributeMap.get(A0090_STR).equalsIgnoreCase(Y_STR))
		if (attributeMap.containsKey("A0121") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0121"))) {
			attributeMap.put("A0121", "http://sds.staples.com/tds/" + attributeMap.get("A0012") + "TDS.pdf");
		}
		// For PCMP-1321
		if (attributeMap.containsKey("A0071")
				&& (DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0071")) || IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0071")))) {
			attributeMap.put("A0071", "0.01");
		}
		// PCMP-1389
		if (attributeMap.containsKey("A0155") && !IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0155"))) {
			attributeMap.put("A0155", DatamigrationCommonUtil.converYESNOIntoChar(attributeMap.get("A0155")));
		}

		// PCMP-1321
		if (attributeMap.containsKey("A0241")
				&& (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0241")) || DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0241")))) {
			if (attributeMap.containsKey("A0071") && !IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0071"))
					&& !DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0071"))) {
				attributeMap.put("A0241", attributeMap.get("A0071"));
			} else {
				attributeMap.put("A0241", "0.01");
			}
		}
		// PCMP-1370-validation removed , PCMP-1396 - transformation added
		/*
		 * if(attributeMap.containsKey("A0499") &&
		 * attributeMap.get("A0499").equalsIgnoreCase(Y_STR)) {
		 * if(!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0500"))) {
		 * System.out
		 * .println("A0500's value when A0499 is Y"+attributeMap.get("A0500"));
		 * } if(IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0500"))) {
		 * attributeMap.put("A0500",""); attributeMap.put("A0499",Y_STR); } }
		 */
		// For PCMP-1752
		if (attributeMap.containsKey("A0499") && !Y_STR.equalsIgnoreCase(attributeMap.get("A0499"))) {
			attributeMap.put("A0499", N_STR);
		}

		if (attributeMap.containsKey("A0499") && attributeMap.get("A0499").equalsIgnoreCase(N_STR)) {
			// if(DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0500")))
			// For PCMP-1272
			if (IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0500")) || DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0500"))) {
				attributeMap.put("A0500", "");
			}
		}
		// PCMP-1398
		if (attributeMap.containsKey(A0013_RET_STR) && attributeMap.containsKey(A0013_NAD_STR)) {
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0013_RET_STR)) && IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0013_NAD_STR))) {
				attributeMap.put(A0013_NAD_STR, attributeMap.get(A0013_RET_STR));
			}
			if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0013_NAD_STR)) && IntgSrvUtils.isNullOrEmpty(attributeMap.get(A0013_RET_STR))) {
				attributeMap.put(A0013_RET_STR, attributeMap.get(A0013_NAD_STR));
			}
		}

		// PCMP-1392
		if (attributeMap.containsKey("A0240") && DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0240"))) {
			attributeMap.put("A0240", "0.01");
		}
		if (attributeMap.containsKey("A0239") && DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0239"))) {
			attributeMap.put("A0239", "0.01");
		}
		if (attributeMap.containsKey("A0238") && DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0238"))) {
			attributeMap.put("A0238", "0.01");
		}
		if (attributeMap.containsKey("A0237") && DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0237"))) {
			attributeMap.put("A0237", "0.01");
		}
		// PCMP-1473 A0125 is defaulted as empty
		if (attributeMap.containsKey("A0124") && attributeMap.get("A0124").equalsIgnoreCase(N_STR)) {
			attributeMap.put("A0125", "");
		}
		// PCMP-1467 If A0231 Contract X-Item % = 0;, set A0431 X-Item Flag to
		// N. See Rule 2640.
		if (attributeMap.containsKey("A0231") && DatamigrationCommonUtil.isZeroValue(attributeMap.get("A0231"))) {
			attributeMap.put("A0431", "N");
		}
		// PCMP-1536 Apply rule 6527: For Item Create/Update (Screen and Mass),
		// if populated, the value can never be NA, N/A, N.A., N.A or N-A
		// regardless of case. Empty (length 0) is an acceptable value.
		if (attributeMap.containsKey("A0120") && isNAValue(attributeMap.get("A0120"))) {
			attributeMap.put("A0120", EMPTY_STR);
		}

		// PCMP-1553
		if (attributeMap.containsKey("A0506") && !IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0506"))
				&& attributeMap.get("A0506").equalsIgnoreCase(N_STR)) {
			attributeMap.put("A0507", "");
		}

		// For PCMP-1552
		if (attributeMap.containsKey("A0124") && attributeMap.get("A0124").equalsIgnoreCase("Y")) {
			if (attributeMap.containsKey("A0137") && IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0137"))) {
				attributeMap.put("A0137", N_STR);
			}
		}

		// For PCMP-1496
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0067_NAD")) && IntgSrvUtils.isDouble(attributeMap.get("A0067_NAD"))
				&& Double.parseDouble(attributeMap.get("A0067_NAD")) > 1.0) {
			attributeMap.put("A0067_NAD", "1");
		}
		// For PCMP-1756
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0067_RET")) && IntgSrvUtils.isDouble(attributeMap.get("A0067_RET"))
				&& Double.parseDouble(attributeMap.get("A0067_RET")) > 1.0) {
			attributeMap.put("A0067_RET", "1");
		}

		// Defaulted based on 07/16/15 call - Bill
		// attributeMap.put("A0028", "70");
		//PCMP-2047 Commented as A0028 and A0304 (IRebuyer) is changed to LOV in STEP
		// attributeMap.put("A0304", "70");

		// For PCMP-1736
		if (attributeMap.containsKey("A0166") && N_STR.equalsIgnoreCase(attributeMap.get("A0166"))) {
			attributeMap.put("A0166", "");
		}
		// For PCMP-1737
		if (attributeMap.containsKey("A0165") && N_STR.equalsIgnoreCase(attributeMap.get("A0165"))) {
			attributeMap.put("A0165", "");
		}

		// For PCMP-1751
		if (attributeMap.containsKey(A0090_STR) && Y_STR.equalsIgnoreCase(attributeMap.get(A0090_STR)) && attributeMap.containsKey("A0095")
				&& isNAValue(attributeMap.get("A0095"))) {
			attributeMap.put("A0095", EMPTY_STR);
		}

		// For PCMP-1720 & PCMP-1719

		if (IntgSrvUtils.toDouble(attributeMap.get("A0151")) > 0.0 || IntgSrvUtils.toDouble(attributeMap.get("A0150")) > 0.0
				|| "Y".equalsIgnoreCase(attributeMap.get("A0149"))) {
			attributeMap.put("A0148", Y_STR);
		} else {
			attributeMap.put("A0148", N_STR);
		}
		
		//Set Y or N for A0171 and A0172
		if (attributeMap.containsKey(A0171_STR)) {
			attributeMap.put(A0171_STR, DatamigrationCommonUtil.converYESNOIntoChar(attributeMap.get(A0171_STR)));
		}
		if (attributeMap.containsKey("A0172")) {
			attributeMap.put("A0172", DatamigrationCommonUtil.converYESNOIntoChar(attributeMap.get("A0172")));
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private boolean isUPCNeedToDefault(String string) {

		if (IntgSrvUtils.isNullOrEmpty(string) || string.equalsIgnoreCase("N") || DatamigrationCommonUtil.isZeroValue(string)) {
			return true;
		}
		return false;
	}

	/**
	 * @param attributeMap
	 * @throws ParseException
	 */
	public void dateFormatValidation(Map<String, String> attributeMap) throws ParseException {

		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0180")) && !invalidDateFormat.equalsIgnoreCase(attributeMap.get("A0180"))) {

			attributeMap.put("A0180",
					DatamigrationCommonUtil.changeISODateFormat(attributeMap.get("A0180"), ProductInboundScheduler.PUBLISH_ID));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0184")) && !invalidDateFormat.equalsIgnoreCase(attributeMap.get("A0184"))) {
			attributeMap.put("A0184",
					DatamigrationCommonUtil.changeISODateFormat(attributeMap.get("A0184"), ProductInboundScheduler.PUBLISH_ID));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0198")) && !invalidDateFormat.equalsIgnoreCase(attributeMap.get("A0198"))) {
			attributeMap.put("A0198",
					DatamigrationCommonUtil.changeISODateTimeFormat(attributeMap.get("A0198"), ProductInboundScheduler.PUBLISH_ID));

		}
		// For PCMP-1394
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0299")) && !invalidDateFormat.equalsIgnoreCase(attributeMap.get("A0299"))) {
			attributeMap.put("A0299",
					DatamigrationCommonUtil.changeDDMONYYYYDateFormat(attributeMap.get("A0299"), ProductInboundScheduler.PUBLISH_ID));

		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0500")) && !invalidDateFormat.equalsIgnoreCase(attributeMap.get("A0500"))) {
			attributeMap.put("A0500",
					DatamigrationCommonUtil.changeDDMONYYYYDateFormat(attributeMap.get("A0500"), ProductInboundScheduler.PUBLISH_ID));

		}

	}

	/**
	 * @param attributeMap
	 * @throws ParseException
	 */
	public void doubleToIntegerValidation(Map<String, String> attributeMap) throws ParseException {

		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0031"))) {
			attributeMap.put("A0031", changeDoubleToInteger(attributeMap.get("A0031")));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0045_RET"))) {
			attributeMap.put("A0045_RET", changeDoubleToInteger(attributeMap.get("A0045_RET")));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0067_NAD"))) {
			attributeMap.put("A0067_NAD", changeDoubleToInteger(attributeMap.get("A0067_NAD")));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0066"))) {
			attributeMap.put("A0066", changeDoubleToInteger(attributeMap.get("A0066")));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0046_NAD"))) {
			attributeMap.put("A0046_NAD", changeDoubleToInteger(attributeMap.get("A0046_NAD")));
		}
		if (attributeMap.containsKey("A0178") && !IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0178"))) {
			attributeMap.put("A0178", changeDoubleToInteger(attributeMap.get("A0178")));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0067_RET"))) {
			attributeMap.put("A0067_RET", changeDoubleToInteger(attributeMap.get("A0067_RET")));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0220"))) {
			attributeMap.put("A0220", changeDoubleToInteger(attributeMap.get("A0220")));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0045_NAD"))) {
			attributeMap.put("A0045_NAD", changeDoubleToInteger(attributeMap.get("A0045_NAD")));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0211"))) {
			attributeMap.put("A0211", changeDoubleToInteger(attributeMap.get("A0211")));
		}
		if (!IntgSrvUtils.isNullOrEmpty(attributeMap.get("A0046_RET"))) {
			attributeMap.put("A0046_RET", changeDoubleToInteger(attributeMap.get("A0046_RET")));
		}

	}

	/**
	 * @param value
	 * @return
	 */
	public static String setPrecision(String value) {

		if (!IntgSrvUtils.isNullOrEmpty(value)) {
			Double doubleValue = Double.parseDouble(value);
			DecimalFormat df = new DecimalFormat("#.00");
			String precisedValue = df.format(doubleValue);
			// DatamigrationCommonUtil.printConsole(precisedValue);
			return precisedValue;
		}
		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String changeDoubleToInteger(String value) {

		if (value != null) {
			Double doubleValue = Double.parseDouble(value);
			Integer intValue = doubleValue.intValue();
			return intValue.toString();
		}
		return null;
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
		}
		return false;
	}

	public static void main(String[] args) throws ParseException {

		try {
			/*
			 * System.out.println("Test : "+DatamigrationCommonUtil.
			 * changeDDMONYYYYDateFormat ("Wed Aug 06 00:00:00 EDT 2014"));
			 * System.out.println("Test : "+DatamigrationCommonUtil
			 * .changeISODateTimeFormat("Wed Aug 06 00:00:00 EDT 2014"));
			 * System.
			 * out.println("Test : "+DatamigrationCommonUtil.changeISODateFormat
			 * ("Wed Aug 06 00:00:00 EDT 2014"));
			 */
			/*
			 * System.out.println(IntgSrvUtils.isNumeric("1.0"));
			 * System.out.println(IntgSrvUtils.isNumeric("1"));
			 */
			/*
			 * System.out.println("empty:"+IntgSrvUtils.toDouble(""));
			 * System.out.println("1:"+(IntgSrvUtils.toDouble("1")>0));
			 * System.out.println("0:"+IntgSrvUtils.toDouble("0"));
			 * System.out.println("null:"+(IntgSrvUtils.toDouble(null)>0.0));
			 */

		} catch (Exception e) {
			System.out.println("testsetet");
			e.printStackTrace();
		}

	}

}
