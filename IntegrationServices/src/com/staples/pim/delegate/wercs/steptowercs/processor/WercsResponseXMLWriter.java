package com.staples.pim.delegate.wercs.steptowercs.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.core.io.Resource;

import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;
import com.staples.pim.delegate.wercs.common.WercsCommonUtil;


@SuppressWarnings("hiding")
public class WercsResponseXMLWriter<MasterTableVO> implements ResourceAwareItemWriterItemStream<MasterTableVO>, StepExecutionListener,
SkipListener<STEPProductInformation, Throwable> {
	
	private static final String					TRACELOGGER_WERCS_STEPTOWERCS			= "tracelogger.wercs.steptowercs";
	static IntgSrvLogger						logger									= IntgSrvLogger
			.getInstance(TRACELOGGER_WERCS_STEPTOWERCS);
	
	public static final String WERCS_OUTPUT_FOLDER="WERCS_STEP_OUTPUT_FOLDER";
	
	public WERCSRestAPIProcessor wercsRestAPIProcessor = new WERCSRestAPIProcessor();

	@Override
	public void close() throws ItemStreamException {

		DatamigrationCommonUtil.printConsole("Writer : close() method");
	}

	@Override
	public void open(ExecutionContext arg0) throws ItemStreamException {
	
		DatamigrationCommonUtil.printConsole("Writer : open() method");
	}

	@Override
	public void update(ExecutionContext arg0) throws ItemStreamException {

		DatamigrationCommonUtil.printConsole("Writer : update() method");
	}

	@Override
	public void write(List<? extends MasterTableVO> arg0) throws Exception {
		DatamigrationCommonUtil.printConsole("Writer : write() method");
		
		List<com.staples.pim.delegate.wercs.model.MasterTableVO> mastertableVOs = new ArrayList<com.staples.pim.delegate.wercs.model.MasterTableVO>();
		for(int i=0;i<arg0.size();i++){
			mastertableVOs.add((com.staples.pim.delegate.wercs.model.MasterTableVO) arg0.get(i));
		}
		if(mastertableVOs.size()>0){
		logger.info("No of Bean objects received="+mastertableVOs.size());
		com.staples.pcm.stepcontract.beans.STEPProductInformation stepProductInfo = wercsRestAPIProcessor.processSTEPWercsRequest(mastertableVOs);
	
		if(stepProductInfo!=null){
			
			File file = new File(IntgSrvUtils.reformatFilePath(IntgSrvPropertiesReader.getProperty(WERCS_OUTPUT_FOLDER))+new Date().getTime()+".xml");
			logger.info("Marshalling output STEP xml");
			File outputFile=DatamigrationCommonUtil.marshallObject(stepProductInfo, file, WERCS_OUTPUT_FOLDER, "WERCSToSTEP");
			logger.info("Sending output XML to STEP hotfolder.");
			WercsCommonUtil.sendWercsResponseFile(outputFile, WercsAppConstants.STEPTOWERCS_APPLICATIONID, "",logger);
			
		}else{
			logger.info("No step xml generated.");
			DatamigrationCommonUtil.printConsole("STEPProductInformation object is null. No valid UPC found in the input xmls.");
		}
		}else{
			logger.info("No valid beans in input");
		}
	}

	@Override
	public void onSkipInProcess(STEPProductInformation arg0, Throwable arg1) {

		DatamigrationCommonUtil.printConsole("Writer : onskipinprocess() method arg0"+arg0.getProducts().getProduct().get(0).getID());
	}

	@Override
	public void onSkipInRead(Throwable arg0) {

		DatamigrationCommonUtil.printConsole("Writer : onskipinprocess() method");
	}

	@Override
	public void onSkipInWrite(Throwable arg0, Throwable arg1) {

		DatamigrationCommonUtil.printConsole("Writer : onskipinprocess() method");
	}

	@Override
	public ExitStatus afterStep(StepExecution arg0) {

		DatamigrationCommonUtil.printConsole("Writer : afterstep() method");
		return null;
	}

	@Override
	public void beforeStep(StepExecution arg0) {

		DatamigrationCommonUtil.printConsole("Writer : open() method");
	}

	@Override
	public void setResource(Resource arg0) {

		DatamigrationCommonUtil.printConsole("Writer : setResource() method");
	}

	public WERCSRestAPIProcessor getWercsRestAPIProcessor() {
		
		return wercsRestAPIProcessor;
	}

	
	public void setWercsRestAPIProcessor(WERCSRestAPIProcessor wercsRestAPIProcessor) {
	
		this.wercsRestAPIProcessor = wercsRestAPIProcessor;
	}
}
