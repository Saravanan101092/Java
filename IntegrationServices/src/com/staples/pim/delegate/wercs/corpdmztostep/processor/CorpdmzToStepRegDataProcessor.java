
package com.staples.pim.delegate.wercs.corpdmztostep.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvUtilConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.common.SBDatabaseHandlingService;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;
import com.staples.pim.delegate.wercs.corpdmztostep.runner.RunSchedulerCorpdmzToStep;
import com.staples.pim.delegate.wercs.model.WercsCollectionBean;

public class CorpdmzToStepRegDataProcessor implements ItemProcessor<WercsCollectionBean, WercsCollectionBean> {

	public static IntgSrvLogger			logger		= IntgSrvLogger
															.getInstance(WercsAppConstants.FREEFORM_TRACE_LOGGER_WERCS_CORPDMZTOSTEP);
	public static String				A0080		= "A0080";
	public static String				STEPID		= null;
	public static String				SKU			= null;
	public static String				PIPID		= null;
	public static String				UPC			= null;
	public static String				mailList	= "Sankar.Suganya@Staples.com";
	protected String					clazzname	= this.getClass().getName();
	public SBDatabaseHandlingService	databaseAccessor;

	@Override
	public WercsCollectionBean process(WercsCollectionBean wercsregulatorydataFeedBean) throws Exception {

		logger.info("Processing ReguatoryData and Putting into MAP");
		WercsCollectionBean wercsCollectionBeanObj = new WercsCollectionBean();
		Map<String, Map<String, String>> mapObject = new HashMap<String, Map<String, String>>();
		String UPCNo = wercsregulatorydataFeedBean.getAttributeValueMap().get(A0080);
		DatamigrationCommonUtil.printConsole("UPCNo : " + UPCNo);
		SBDatabaseHandlingService dbAccess = new SBDatabaseHandlingService(RunSchedulerCorpdmzToStep.datasource);
		List<String> UPCValue = dbAccess.getWercsUPCforRegData(UPCNo, RunSchedulerCorpdmzToStep.logger);
		DatamigrationCommonUtil.printConsole("UPCValue : " + UPCValue);

		UPC = UPCValue.get(0);
		SKU = UPCValue.get(1);
		PIPID = UPCValue.get(2);
		STEPID = UPCValue.get(3);
		String KeyID = getKeyId(SKU, PIPID, STEPID);
		DatamigrationCommonUtil.printConsole("KeyID : " + KeyID);
		List<String> KeyList = new ArrayList<String>();
		KeyList.add(KeyID);
		for (String KeyValue : KeyList) {
			wercsCollectionBeanObj.setKEYID(KeyValue);
		}
		if (UPC != null && UPC.equals(UPCNo)) {
			try {
				mapObject.put(UPC, wercsregulatorydataFeedBean.getAttributeValueMap());
				DatamigrationCommonUtil.printConsole("Map Value : " + mapObject);

				wercsCollectionBeanObj.setAttributeValueMap(mapObject.get(UPC));
			} catch (Exception e) {
				logger.error("Caught an exception in CreationofMapforRegulatoryData :" + e.getMessage());
				IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), RunSchedulerCorpdmzToStep.PUBLISH_ID);
			}
		} else {
			SendMail();
		}

		return wercsCollectionBeanObj;
	}

	private void SendMail() {

		System.out.println("Sending Mail to LP Team");
	}

	public static String getKeyId(String SKU, String PIPID, String STEPID) {

		logger.info("Getting KeyID for STEPXML");
		String KeyId = null;
		if (SKU != null) {
			KeyId = SKU;
		} else if (PIPID != null) {
			KeyId = "Item-" + PIPID;
		} else if (STEPID != null) {
			KeyId = "Item-" + STEPID;
		}
		return KeyId;
	}

}
