
package com.staples.pim.delegate.pyramid.itemonbrdtmupdate.processor;

import org.springframework.batch.item.ItemProcessor;

import com.staples.pim.delegate.pyramid.itemonbrdtmupdate.model.ItemOnbrdTmFeedBean;

public class ItemOnbrdTmProcessor implements ItemProcessor<ItemOnbrdTmFeedBean, ItemOnbrdTmFeedBean> {

	public ItemOnbrdTmFeedBean process(ItemOnbrdTmFeedBean feedBean) throws Exception {

		return (validatePyramidIOBTemplateFeed(feedBean) ? feedBean : null);
	}

	public boolean validatePyramidIOBTemplateFeed(ItemOnbrdTmFeedBean feedBean) {

		return (feedBean.getSuperCategoryId() != null && feedBean.getCategoryId() != null && feedBean.getDepartmentId() != null && feedBean
				.getClassId() != null && "1".equalsIgnoreCase(feedBean.getBusinessUnit()));

	}

}
