
package com.staples.pim.delegate.itemutility.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0077_NAD_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0077_RET_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0078_NAD_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0078_RET_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0051_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.A0052_ID;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_ITEMUTLITY;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.LIST_PRICE_UPDATE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.PO_COST_UPDATE;

import java.util.List;
import java.util.Map.Entry;

import com.staples.pim.base.common.bean.STEPProductInformation.Products.Product.Values.Value;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.itemutility.beans.CommonVO;

public class GalaxyOutboundValidator {

	static IntgSrvLogger logger = IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER_ITEMUTLITY);

	/**
	 * To validate the attributes are valid or not
	 * 
	 * @param stepProductInformation
	 * @return
	 * @throws Throwable
	 */
	public static boolean isValidGalaxyMessage(CommonVO commonVO) throws Throwable {

		boolean isValid = Boolean.TRUE;

		/** Model No. - mandatory,data type,attribute value(length) */
		/*
		 * if (!isModelNumberValidation(commonVO.getModelNo())) { throw new
		 * Exception("Invalid Model Number :"+commonVO.getModelNo()); }
		 *//** Vendor No. - mandatory,data type,attribute value(length) */
		/*
		 * if (!isVendorNumberValidation(commonVO.getVendorNo())) { throw new
		 * Exception("Invalid Vendor Number :"+commonVO.getVendorNo()); }
		 */

		/**
		 * Validate the attribute values which is going to update in STEP system
		 */
		for (Entry<String, String> attObjMap : commonVO.getAttributeMap().entrySet()) {
			if (A0077_RET_ID.equals(attObjMap.getKey()) || A0077_NAD_ID.equals(attObjMap.getKey())
					|| A0078_RET_ID.equals(attObjMap.getKey()) || A0078_NAD_ID.equals(attObjMap.getKey())) {
				/**
				 * validate attribute value based on transaction type
				 */
				if (A0077_RET_ID.equals(attObjMap.getKey())) {

					/**
					 * If transaction type is list price update, continue the
					 * validation else remove the value from list
					 */
					if (LIST_PRICE_UPDATE.equalsIgnoreCase(commonVO.getTransactionType())) {
						isValid = isListPricePOCostValid(attObjMap.getValue(), A0077_RET_ID);
					} else {
						logger.info("No validation required for " + attObjMap.getKey() + " Since the transaction type is : "
								+ commonVO.getTransactionType());
					}
				}
				if (A0077_NAD_ID.equals(attObjMap.getKey())) {

					/**
					 * If transaction type is list price update, continue the
					 * validation else remove the value from list
					 */
					if (LIST_PRICE_UPDATE.equalsIgnoreCase(commonVO.getTransactionType())) {
						isValid = isListPricePOCostValid(attObjMap.getValue(), A0077_NAD_ID);
					} else {
						logger.info("No validation required for " + attObjMap.getKey() + " Since the transaction type is : "
								+ commonVO.getTransactionType());
					}
				} else if (A0078_RET_ID.equalsIgnoreCase(attObjMap.getKey())) {
					/**
					 * PO Cost - If transaction type is Po Cost update, continue
					 * the validation else remove the value from list
					 */
					if (PO_COST_UPDATE.equalsIgnoreCase(commonVO.getTransactionType())) {
						isValid = isListPricePOCostValid(attObjMap.getValue(), A0078_RET_ID);
					} else {
						logger.info("No validation required for " + attObjMap.getKey() + " Since the transaction type is : "
								+ commonVO.getTransactionType());
					}
				} else if (A0078_NAD_ID.equalsIgnoreCase(attObjMap.getKey())) {
					/**
					 * PO Cost - If transaction type is Po Cost update, continue
					 * the validation else remove the value from list
					 */
					if (PO_COST_UPDATE.equalsIgnoreCase(commonVO.getTransactionType())) {
						isValid = isListPricePOCostValid(attObjMap.getValue(), A0078_NAD_ID);
					} else {
						logger.info("No validation required for " + attObjMap.getKey() + " Since the transaction type is : "
								+ commonVO.getTransactionType());
					}
				}
			} /*
				 * else if (A0012_ID.equals(attObjMap.getKey())) {
				 */
			/**
			 * SKU Number validation- mandatory,data type,attribute
			 * value(length)
			 */
			/*
			 * isValid = isValidSKUID(attObjMap.getValue()); }
			 */
			logger.info(
					"Attribute Id :" + attObjMap.getKey() + " Attribute Value :" + attObjMap.getValue() + " Validation status :" + isValid);
			if (!isValid) {
				throw new Exception("Error in attribute :" + attObjMap.getKey() + ": value is :" + attObjMap.getValue());
				// return isValid;
			}
		}
		return isValid;

	}

	/**
	 * To remove the product values from prdValues list.
	 * 
	 * @param prdValues
	 * @param removeIndex
	 */
	public static void removeAttributes(List<Value> prdValues, List<Integer> removeIndex) {

		for (int index : removeIndex) {
			prdValues.remove(index);
		}
	}

	/**
	 * Limit to the two decimal places for A0077, A0078
	 * 
	 * @param valueStr
	 *            PO Cost or List Price Cost
	 * @return formatted value
	 */
	public static double changeCostAndPriceValueFormat(String valueStr, String attributeName) {

		Double value = IntgSrvUtils.toDouble(valueStr);
		if (value != 0) {
			// Issue fix for PCM-687 - format changes based on attribute ie. If
			// A0078 convert into four decimal place value
			// else If A0077 then convert two decimal place value
			if (A0078_RET_ID.equalsIgnoreCase(attributeName) || A0078_NAD_ID.equalsIgnoreCase(attributeName))
				return value / 10000;
			else if (A0077_RET_ID.equalsIgnoreCase(attributeName) || A0077_NAD_ID.equalsIgnoreCase(attributeName)
					|| A0051_ID.equalsIgnoreCase(attributeName) || A0052_ID.equalsIgnoreCase(attributeName))
				return value / 100;
		}
		return value;
	}

	/**
	 * Validate length of the SKU number, should be 9 character
	 * 
	 * @param valueStr
	 *            the SKU number
	 * @return boolean
	 */
	private static boolean isValidSKUID(String valueStr) {

		/*
		 * if (!CommonUtils.isEmptyString(valueStr) && valueStr.length() == 9) {
		 * return Boolean.TRUE; }
		 */
		return Boolean.TRUE;
	}

	/**
	 * To validate the List Price/ PO Cost is valid or not
	 * 
	 * @param listPOPrice
	 * @return
	 */
	private static boolean isListPricePOCostValid(String listPOPrice, String attributeName) {

		/*
		 * if (!CommonUtils.isEmptyString(listPOPrice)) { double value =
		 * changeCostAndPriceValueFormat(listPOPrice,attributeName); if
		 * (CommonUtil.isMinMaxCheck(value, LIST_PO_MIN, LIST_PO_MAX)) { return
		 * Boolean.TRUE; } }
		 */
		return Boolean.TRUE;
	}
	/*
	*//**
		 * To validate the Channel is valid or not
		 * 
		 * @param channelStr
		 * @return
		 */
	/*
	 * private static boolean isChannelValid(String channelStr) {
	 * 
	 * if ("NAD".equalsIgnoreCase(channelStr) ||
	 * "COR".equalsIgnoreCase(channelStr) || "SCC".equalsIgnoreCase(channelStr)
	 * || "RET".equalsIgnoreCase(channelStr)) { return Boolean.TRUE;
	 * 
	 * } return Boolean.FALSE; }
	 */
	/**
	 * To validate the vendor number is valid or not
	 * 
	 * @param vendorNo
	 * @return
	 */
	/*
	 * private static boolean isVendorNumberValidation(String vendorNo) {
	 * 
	 * if (!CommonUtils.isEmptyString(vendorNo)) { if
	 * (CommonUtil.isIntMinMaxCheck(vendorNo, VENDOR_NO_MIN, VENDOR_NO_MAX)) {
	 * return Boolean.TRUE; } } return Boolean.TRUE; }
	 *//**
		 * To validate the model number is valid or not
		 * 
		 * @param modelNum
		 * @return
		 */
	/*
	 * private static boolean isModelNumberValidation(String modelNum) {
	 * 
	 * if (!CommonUtils.isEmptyString(modelNum)) { if
	 * (modelNum.matches(MODEL_NUMBER_VALIDATION) && modelNum.length() <=
	 * MODEL_NO_LENGTH) { return Boolean.TRUE; } } return Boolean.TRUE; } public
	 * static void main(String st[]){
	 * System.out.println(isModelNumberValidation(
	 * "PARI12l423423ASAFSAFWERSDFSDF23234234SDFSDF@#@FAS32")); }
	 */
}
