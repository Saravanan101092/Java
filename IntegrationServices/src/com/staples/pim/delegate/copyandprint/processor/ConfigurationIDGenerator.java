
package com.staples.pim.delegate.copyandprint.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.FREEFORM_TRACE_LOGGER_CNP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.copyandprint.beans.ConfigurationsVO;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

public class ConfigurationIDGenerator {

	/**
	 * Logger initialization
	 */
	static IntgSrvLogger		logger		= IntgSrvLogger.getInstance(FREEFORM_TRACE_LOGGER_CNP);

	/**
	 * The function getCombinations() takes two lists of string as input and
	 * gets all possible combinations of those string from the two lists.
	 * 
	 * @param listWithPreviousCombinations
	 *            - contains the combinations generated during previous
	 *            iterations
	 * @param newList
	 *            contains the new list of Strings
	 */
	public List<List<String>> getCombinations(List<String> newList, List<List<String>> listWithPreviousCombinations) {

		List<List<String>> listWithNewCombinations = new ArrayList<List<String>>();
		List<String> keyList;
		if (listWithPreviousCombinations.isEmpty()) {
			for (String value : newList) {
				keyList = new ArrayList<String>();
				keyList.add(value);
				listWithNewCombinations.add(keyList);
			}
		} else {
			for (String newString : newList) {
				for (List<String> stringlist : listWithPreviousCombinations) {
					keyList = new ArrayList<String>();
					for (String value : stringlist) {
						keyList.add(value);
					}
					keyList.add(newString);
					listWithNewCombinations.add(keyList);
				}
			}
		}
		return listWithNewCombinations;
	}

	/**
	 * The function sort() sorts individual keys in ascending order
	 * 
	 * @param originalList
	 */
	public void sort(List<String> originalList) {

		List<Integer> tempList = new ArrayList<Integer>();
		List<String> prefixList = new ArrayList<String>();

		for (String string : originalList) {
			tempList.add(IntgSrvUtils.toInt(string.substring(1)));
			prefixList.add("" + string.charAt(0));
		}

		originalList.removeAll(originalList);
		Collections.sort(tempList);

		for (int i = 0; i < tempList.size(); i++) {
			originalList.add(prefixList.get(i) + tempList.get(i));
		}
	}

	/**
	 * The function generateKeys() takes the map object and the string array of
	 * keys as input and calls the getCombination() function n number of times
	 * based on the length of the key array.
	 * 
	 * @param map
	 * @return
	 */
	public List<List<String>> generateKeys(Map<String, List<String>> map) {

		logger.info(DatamigrationCommonUtil.getClassAndMethodName() + ": generating keys..");
		List<List<String>> listWithCombinations = new ArrayList<List<String>>();
		for (int i = map.keySet().toArray().length; i > 0; i--) {
			listWithCombinations = getCombinations(map.get(Integer.toString(i)), listWithCombinations);
		}
//		for (List<String> currentCombination : listWithCombinations) {
//			Collections.sort(currentCombination);
//		}
		 for (List<String> stringlist : listWithCombinations) {
		 sort(stringlist);
		 }
		logger.info(DatamigrationCommonUtil.getClassAndMethodName() + "Number of Unique key combinations generated"+listWithCombinations.size());
		return listWithCombinations;
	}

	/**
	 * The method converts the input list into a map with integer as a key and
	 * groups the attributeValues based on the attributeID.
	 * 
	 * @param configlist
	 * @return
	 */
	public Map<String, List<String>> convertListtoMap(List<ConfigurationsVO> configlist) {

		logger.info(DatamigrationCommonUtil.getClassAndMethodName() + ": Converting list to Map..");
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		Map<String, String> integerToAttributemap = new HashMap<String, String>();
		List<String> attributeValueList;
		int noOfGroups = 1;
		for (ConfigurationsVO configVO : configlist) {
			if (integerToAttributemap.containsKey(configVO.getConfigAttrId())) {
				map.get(integerToAttributemap.get(configVO.getConfigAttrId())).add(configVO.getConfigAttrValId());
			} else {
				integerToAttributemap.put(configVO.getConfigAttrId(), Integer.toString(noOfGroups));
				attributeValueList = new ArrayList<String>();
				attributeValueList.add(configVO.getConfigAttrValId());
				map.put(Integer.toString(noOfGroups), attributeValueList);
				noOfGroups++;
			}
		}
		return map;
	}

	/**
	 * This method is used to retrieve the attribute id from the input list of
	 * configurationVO based on the attributeValue and also sets the other
	 * values of the ConfigurationVO class.
	 * 
	 * @param attributevalue
	 * @param configList
	 * @return
	 */
	public ConfigurationsVO getAttributeidFromConfigVOObjects(String attributevalue, List<ConfigurationsVO> configList) {

		ConfigurationsVO tempConfigVO = null;
		for (ConfigurationsVO configvo : configList) {
			if (attributevalue.equals(configvo.getConfigAttrValId())) {
				tempConfigVO = new ConfigurationsVO();
				tempConfigVO.setConfigAttrId(configvo.getConfigAttrId());
				tempConfigVO.setConfigAttrValId(configvo.getConfigAttrValId());
				tempConfigVO.setActiveId(configvo.getActiveId());
				tempConfigVO.setDefAttrValInd(configvo.getDefAttrValInd());
				tempConfigVO.setTemplateId(configvo.getTemplateId());
				tempConfigVO.setProductTagId(configvo.getProductTagId());
				return tempConfigVO;

			}
		}
		return null;
	}

	//Revised version of this commented method is available below
	/**
	 * This method gets the keys generated as input, removes the invalid
	 * configurations from the list and finally creates a list of
	 * ConfigurationsVO
	 * 
	 * @param configVOList
	 * @param invalidConfigList
	 * @return
	 */
	// public List<ConfigurationsVO> getGeneratedKeys(List<ConfigurationsVO>
	// configVOList, List<List<String>> invalidConfigList,
	// String skuNumber) {
	//
	// List<ConfigurationsVO> configListForXLS = new
	// ArrayList<ConfigurationsVO>();
	// Map<String, List<ConfigurationsVO>> mapWithKeys = new HashMap<String,
	// List<ConfigurationsVO>>();
	//
	// List<List<String>> generatedKeys =
	// generateKeys(convertListtoMap(configVOList));
	// String key;
	// List<ConfigurationsVO> configvolist;
	// ConfigurationsVO configvo;
	//
	// for (List<String> stringlist : generatedKeys) {
	// key = "";
	// configvolist = new ArrayList<ConfigurationsVO>();
	// for (String string : stringlist) {
	// configvo = getAttributeidFromConfigVOObjects(string, configVOList);
	// configvolist.add(configvo);
	// key += string;
	//
	// }
	//
	// mapWithKeys.put(key, configvolist);
	// if (isValidConfiguration(mapWithKeys.get(key), invalidConfigList)) {
	// for (ConfigurationsVO con : mapWithKeys.get(key)) {
	// //
	// System.out.println("Attribute Id : "+con.getConfigAttrId()+"\t"+"Attribute VAlue :"+con.getConfigAttrValId());
	// con.setTemplateConfigId(skuNumber + key);
	// configListForXLS.add(con);
	// //logger.info(" Configuration VO Details: " +
	// DatamigrationCommonUtil.voToString(con));
	// //DatamigrationCommonUtil.printConsole(" Configuration VO Details: " +
	// DatamigrationCommonUtil.voToString(con));
	// }
	// }
	// }
	// return configListForXLS;
	//
	// }

	/**
	 * @param configVOList
	 * @param invalidConfigList
	 * @param skuNumber
	 * @return
	 */
	public List<List<String>> getGeneratedKeys(List<ConfigurationsVO> configVOList, List<List<String>> invalidConfigList, String skuNumber) {

		List<List<String>> generatedKeys = generateKeys(convertListtoMap(configVOList));
		List<List<String>> tempList = new ArrayList<List<String>>();
		if (!invalidConfigList.isEmpty()) {
		for (List<String> stringlist : generatedKeys) {
				if (!isValidConfiguration(stringlist, invalidConfigList)) {
					tempList.add(stringlist);
					logger.info("Invalid Combination removed "+stringlist);
				}
			}
		}
		generatedKeys.removeAll(tempList);
		return generatedKeys;
	}

	//Revised version of this commented method is available below
	/**
	 * This method checks for invalid configurations from the input list and
	 * returns false if it is invalid.
	 * 
	 * @param list
	 * @param invalidConfigList
	 * @return
	 */
	// private boolean isValidConfiguration(List<ConfigurationsVO> list,
	// List<List<String>> invalidConfigList) {
	//
	// for (List<String> invalidList : invalidConfigList) {
	//
	// int inValidITotalAttCount = invalidList.size();
	// int invalidCount = 0;
	// for (ConfigurationsVO configVO : list) {
	// if (invalidList.contains(configVO.getConfigAttrValId())) {
	// invalidCount++;
	// if (inValidITotalAttCount == invalidCount) {
	// DatamigrationCommonUtil.printConsole("Configuration ID generation is Invalid..");
	// logger.info("Configuration ID generation is Invalid..");
	// return false;
	// }
	// }
	// }
	// }
	// DatamigrationCommonUtil.printConsole("Configuration ID generation is Valid..");
	// logger.info("Configuration ID generation is Valid..");
	// return true;
	// }

	private boolean isValidConfiguration(List<String> list, List<List<String>> invalidConfigList) {

		for (List<String> invalidList : invalidConfigList) {

			int inValidITotalAttCount = invalidList.size();
			int invalidCount = 0;
			for (String id : list) {
				if (invalidList.contains(id)) {
					invalidCount++;
					if (inValidITotalAttCount == invalidCount) {
						DatamigrationCommonUtil.printConsole("Configuration ID generation is Invalid..");
						logger.info("Configuration ID generation is Invalid..");
						return false;
					}
				}
			}
		}
		logger.info("Configuration ID generation is Valid..");
		return true;
	}

}
