package com.staples.pim.delegate.locationfeed.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.staples.pim.delegate.locationfeed.model.LocationFeedBean;

public class LocationFeedFieldSetMapper implements FieldSetMapper<LocationFeedBean> {

	@Override
	public LocationFeedBean mapFieldSet(FieldSet fieldSet) throws BindException {

		LocationFeedBean locBean = new LocationFeedBean();

		locBean.setLocationcode(fieldSet.readString("locationcode"));
		locBean.setLocationname(fieldSet.readString("locationname"));
		locBean.setLocationaddress1(fieldSet.readString("locationaddress1"));
		locBean.setLocationaddress2(fieldSet.readString("locationaddress2"));
		locBean.setLocationaddress3(fieldSet.readString("locationaddress3"));
		locBean.setLocationcity(fieldSet.readString("locationcity"));
		locBean.setLocationstate(fieldSet.readString("locationstate"));
		locBean.setLocationpostalcode(fieldSet.readString("locationpostalcode"));
		locBean.setLocationcontact(fieldSet.readString("locationcontact"));
		locBean.setLocationphone(fieldSet.readString("locationphone"));
		locBean.setLocationopendate(fieldSet.readString("locationopendate"));
		locBean.setBgid(fieldSet.readString("bgid"));
		locBean.setLocationfax(fieldSet.readString("locationfax"));
		locBean.setBusinessunit(fieldSet.readString("businessunit"));
		locBean.setLocationtype(fieldSet.readString("locationtype"));
		locBean.setLocationregion(fieldSet.readString("locationregion"));
		locBean.setLocationregionname(fieldSet.readString("locationregionname"));
		locBean.setLocationdist(fieldSet.readString("locationdist"));
		locBean.setLocationdistname(fieldSet.readString("locationdistname"));
		locBean.setFinanciallocationcode(fieldSet.readString("financiallocationcode"));

		return locBean;
	}
}