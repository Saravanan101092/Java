package com.staples.pim.delegate.wercs.steptopip.processor;


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
import com.staples.pim.delegate.wercs.model.MasterTableVO;


public class StepToPIPWriter implements ResourceAwareItemWriterItemStream<MasterTableVO>, StepExecutionListener,
SkipListener<STEPProductInformation, Throwable>{

	

	@Override
	public void close() throws ItemStreamException {

		// System.out.println("Writer : close() method");
	}

	@Override
	public void open(ExecutionContext arg0) throws ItemStreamException {
	
		// System.out.println("Writer : open() method");
	}

	@Override
	public void update(ExecutionContext arg0) throws ItemStreamException {

		// System.out.println("Writer : update() method");
	}
	
	@Override
	public void write(List<? extends MasterTableVO> arg0) throws Exception {
		// System.out.println("Writer : write() method");
	}

	@Override
	public void onSkipInProcess(STEPProductInformation arg0, Throwable arg1) {

		// System.out.println("Writer : onskipinprocess() method arg0"+arg0.getProducts().getProduct().get(0).getID());
	}

	@Override
	public void onSkipInRead(Throwable arg0) {

		// System.out.println("Writer : onskipinprocess() method");
	}

	@Override
	public void onSkipInWrite(Throwable arg0, Throwable arg1) {

		// System.out.println("Writer : onskipinprocess() method");
	}

	@Override
	public ExitStatus afterStep(StepExecution arg0) {

		// System.out.println("Writer : afterstep() method");
		return null;
	}

	@Override
	public void beforeStep(StepExecution arg0) {

		// System.out.println("Writer : open() method");
	}

	@Override
	public void setResource(Resource arg0) {

		// System.out.println("Writer : setResource() method");
	}

	

}
