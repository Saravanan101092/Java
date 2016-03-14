
package com.staples.pim.delegate.wholesalers.processor;

import org.springframework.batch.item.ItemProcessor;

import com.staples.pim.delegate.wholesalers.model.WholesalerContractFeedBean;

public class WholesalerContractFeedItemProcessor implements ItemProcessor<WholesalerContractFeedBean, WholesalerContractFeedBean> {

	@Override
	public WholesalerContractFeedBean process(WholesalerContractFeedBean feedBean) throws Exception {

		return feedBean;
	}
}