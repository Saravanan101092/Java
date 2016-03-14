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
 * File name     :   
 * Creation Date :   
 * @author  
 * @version 1.0
 */

package com.staples.pim.delegate.createupdateitem.writer;

import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;

import com.staples.pim.base.common.bean.STEPProductInformation.Products.Product;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.createupdateitem.listenerrunner.RunSchedulerItemCreateUpdate;

public class MultiOutputItemWriterStepXML implements ItemWriter<Product>, ItemStream {

	public MultiOutputItemWriterStepXML() {

		// TODO Auto-generated constructor stub
	}

	private String							clazzname	= this.getClass().getName();
	private IntgSrvLogger					traceLogger	= IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);

	private FlatFileItemWriter<Product>		delegateXSV;
	private JdbcBatchItemWriter<Product>	delegateDB;
	private FlatFileItemWriter<Product>		delegateFIXLength;
	private ProductExcelWriter<Product>		delegateExcel;

	public void write(List<? extends Product> items) throws Exception {

		traceLogger.info(clazzname, "write", "ENTER: ItemWriter<Product> & ItemStream: to delegate");
		traceLogger.info(clazzname, "write", "call delegateXSV.write(items)");
		delegateXSV.write(items);
		traceLogger.info(clazzname, "write", "call delegateDB.write(items)");
		// delegateDB.write(items);
		try {
			delegateDB.write(items);

		} catch (Throwable exception) {
			traceLogger.error(clazzname, "run", "Exception: " + exception.toString());
			System.out.println("exception==" + exception.toString());
			exception.printStackTrace();
			IntgSrvUtils.alertByEmail(exception, clazzname, RunSchedulerItemCreateUpdate.publishIds);
		}

		traceLogger.info(clazzname, "write", "call delegateFixLength.write(items)");

		// Below line is added for blocking the empty file(6KB) generation.
		delegateFIXLength.setShouldDeleteIfEmpty(true);
		// IF Condition is added to restrict the fix length file creation only
		// for SKUs.
		if (RunSchedulerItemCreateUpdate.bContainsSKU) {
			delegateFIXLength.write(items);
		}
		traceLogger.info(clazzname, "write", "call delegateExcel.write(items)");
		delegateExcel.write(items);
		traceLogger.info(clazzname, "write", "EXIT");
	}

	public void setDelegateExcel(ProductExcelWriter delegateExcel) {

		traceLogger.info(clazzname, "setDelegateExcel", "ENTER/EXIT: bean-property-context");
		this.delegateExcel = delegateExcel;
	}

	public void setDelegateDB(JdbcBatchItemWriter<Product> delegateDB) {

		traceLogger.info(clazzname, "setDelegateDB", "ENTER/EXIT: bean-property-context");
		this.delegateDB = delegateDB;
	}

	public void setDelegateXSV(FlatFileItemWriter<Product> delegateXSV) {

		traceLogger.info(clazzname, "setDelegateXSV", "ENTER/EXIT: bean-property-context");
		this.delegateXSV = delegateXSV;
	}

	public void setDelegateFIXLength(FlatFileItemWriter<Product> delegateFIXLength) {

		traceLogger.info(clazzname, "setDelegateFixLength", "ENTER/EXIT: bean-property-context");
		this.delegateFIXLength = delegateFIXLength;
	}

	public void close() throws ItemStreamException {

		traceLogger.info(clazzname, "close", "ENTER: ItemWriter<Product> & ItemStream: to delegate");
		traceLogger.info(clazzname, "close", "call delegateXSV.close()");
		this.delegateXSV.close();
		traceLogger.info(clazzname, "close", "call delegateFIXLength.close()");
		this.delegateFIXLength.close();
		traceLogger.info(clazzname, "close", "call delegateExcel.close()");
		this.delegateExcel.close();
		traceLogger.info(clazzname, "close", "EXIT");
	}

	public void open(ExecutionContext arg0) throws ItemStreamException {

		traceLogger.info(clazzname, "open", "ENTER: ItemWriter<Product> & ItemStream: to delegate");
		traceLogger.info(clazzname, "open", "call delegateXSV.open()");
		this.delegateXSV.open(arg0);
		traceLogger.info(clazzname, "open", "call delegateFIXLength.open()");
		this.delegateFIXLength.open(arg0);
		traceLogger.info(clazzname, "open", "call delegateExcel.open()");
		this.delegateExcel.open(arg0);
		traceLogger.info(clazzname, "open()", "EXIT");
	}

	public void update(ExecutionContext arg0) throws ItemStreamException {

		traceLogger.info(clazzname, "update", "ENTER: ItemWriter<Product> & ItemStream: to delegate");
		traceLogger.info(clazzname, "update", "call delegateXSV.update()");
		this.delegateXSV.update(arg0);
		traceLogger.info(clazzname, "update", "call delegateFIXLength.update()");
		this.delegateFIXLength.update(arg0);
		traceLogger.info(clazzname, "update", "call delegateExcel.update()");
		this.delegateExcel.update(arg0);
		traceLogger.info(clazzname, "update", "EXIT");
	}

}
