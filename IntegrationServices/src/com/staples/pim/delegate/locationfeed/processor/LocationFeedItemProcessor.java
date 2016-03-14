package com.staples.pim.delegate.locationfeed.processor;

import org.springframework.batch.item.ItemProcessor;

import com.staples.pim.delegate.locationfeed.model.LocationFeedBean;

public class LocationFeedItemProcessor implements ItemProcessor<LocationFeedBean, LocationFeedBean> {

	@Override
	public LocationFeedBean process(LocationFeedBean locationFeedBean) throws Exception {

		return locationFeedBean;
	}
}