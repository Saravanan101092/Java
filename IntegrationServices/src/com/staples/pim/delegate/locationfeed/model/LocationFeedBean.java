package com.staples.pim.delegate.locationfeed.model;

public class LocationFeedBean {

	private String locationcode;
	private String locationname;
	private String locationaddress1;
	private String locationaddress2;
	private String locationaddress3;
	private String locationaddress4;
	private String locationcity;
	private String locationstate;
	private String locationpostalcode;
	private String locationcontact;
	private String locationphone;
	private String locationopendate;
	private String bgid;
	private String locationfax;
	private String businessunit;
	private String locationtype;
	private String locationregion;
	private String locationregionname;
	private String locationdist;
	private String locationdistname;
	private String financiallocationcode;

	public String getLocationcode() {
		return getStringValue(locationcode);
	}

	public void setLocationcode(String locationcode) {
		this.locationcode = locationcode;
	}

	public String getLocationname() {
		return getStringValue(locationname);
	}

	public void setLocationname(String locationname) {
		this.locationname = locationname;
	}

	public String getLocationaddress1() {
		return getStringValue(locationaddress1);
	}

	public void setLocationaddress1(String locationaddress1) {
		this.locationaddress1 = locationaddress1;
	}

	public String getLocationaddress2() {
		return getStringValue(locationaddress2);
	}

	public void setLocationaddress2(String locationaddress2) {
		this.locationaddress2 = locationaddress2;
	}

	public String getLocationaddress3() {
		return getStringValue(locationaddress3);
	}

	public void setLocationaddress3(String locationaddress3) {
		this.locationaddress3 = locationaddress3;
	}

	public String getLocationaddress4() {
		return getStringValue(locationaddress4);
	}

	public void setLocationaddress4(String locationaddress4) {
		this.locationaddress4 = locationaddress4;
	}

	public String getLocationcity() {
		return getStringValue(locationcity);
	}

	public void setLocationcity(String locationcity) {
		this.locationcity = locationcity;
	}

	public String getLocationstate() {
		return getStringValue(locationstate);
	}

	public void setLocationstate(String locationstate) {
		this.locationstate = locationstate;
	}

	public String getLocationpostalcode() {
		return getStringValue(locationpostalcode);
	}

	public void setLocationpostalcode(String locationpostalcode) {
		this.locationpostalcode = locationpostalcode;
	}

	public String getLocationcontact() {
		return getStringValue(locationcontact);
	}

	public void setLocationcontact(String locationcontact) {
		this.locationcontact = locationcontact;
	}

	public String getLocationphone() {
		return getStringValue(locationphone);
	}

	public void setLocationphone(String locationphone) {
		this.locationphone = locationphone;
	}

	public String getLocationopendate() {
		return getStringValue(locationopendate);
	}

	public void setLocationopendate(String locationopendate) {
		this.locationopendate = locationopendate;
	}

	public String getBgid() {
		return getStringValue(bgid);
	}

	public void setBgid(String bgid) {
		this.bgid = bgid;
	}

	public String getLocationfax() {
		return getStringValue(locationfax);
	}

	public void setLocationfax(String locationfax) {
		this.locationfax = locationfax;
	}

	public String getBusinessunit() {
		return getStringValue(businessunit);
	}

	public void setBusinessunit(String businessunit) {
		this.businessunit = businessunit;
	}

	public String getLocationtype() {
		return getStringValue(locationtype);
	}

	public void setLocationtype(String locationtype) {
		this.locationtype = locationtype;
	}

	public String getLocationregion() {
		return getStringValue(locationregion);
	}

	public void setLocationregion(String locationregion) {
		this.locationregion = locationregion;
	}

	public String getLocationregionname() {
		return getStringValue(locationregionname);
	}

	public void setLocationregionname(String locationregionname) {
		this.locationregionname = locationregionname;
	}

	public String getLocationdist() {
		return getStringValue(locationdist);
	}

	public void setLocationdist(String locationdist) {
		this.locationdist = locationdist;
	}

	public String getLocationdistname() {
		return getStringValue(locationdistname);
	}

	public void setLocationdistname(String locationdistname) {
		this.locationdistname = locationdistname;
	}

	public String getFinanciallocationcode() {
		return getStringValue(financiallocationcode);
	}

	public void setFinanciallocationcode(String financiallocationcode) {
		this.financiallocationcode = financiallocationcode;
	}

	private String getStringValue(String str) {
		return (str == null ? "" : str);
	}
}