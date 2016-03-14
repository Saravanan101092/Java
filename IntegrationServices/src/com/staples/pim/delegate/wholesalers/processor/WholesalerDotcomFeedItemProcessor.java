
package com.staples.pim.delegate.wholesalers.processor;

import org.springframework.batch.item.ItemProcessor;

import com.staples.pim.delegate.wholesalers.model.WholesalerDotcomFeedBean;

public class WholesalerDotcomFeedItemProcessor implements ItemProcessor<WholesalerDotcomFeedBean, WholesalerDotcomFeedBean> {

	@Override
	public WholesalerDotcomFeedBean process(WholesalerDotcomFeedBean wholesalersFeedBean) throws Exception {

		return wholesalersFeedBean;
	}
}