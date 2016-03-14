
package com.staples.pim.delegate.assetImport.model;

public class Asset {

	private String	skuNo;
	private String	assetId;
	private String	assetSequence;
	private String	assetURL;

	public String getSkuNo() {

		return skuNo;
	}

	public void setSkuNo(String skuNo) {

		this.skuNo = skuNo;
	}

	public String getAssetId() {

		return assetId;
	}

	public void setAssetId(String assetId) {

		this.assetId = assetId;
	}

	public String getAssetSequence() {

		return assetSequence;
	}

	public void setAssetSequence(String assetSequence) {

		this.assetSequence = assetSequence;
	}

	public String getAssetURL() {

		return assetURL;
	}

	public void setAssetURL(String assetURL) {

		this.assetURL = assetURL;
	}

	public String toString() {

		return skuNo + ":assetId" + assetId + ":assetSequence" + assetSequence + ":assetURL" + assetURL;
	}

	public boolean isValidAsset() {

		return (isNonEmptyString(skuNo) && isNonEmptyString(assetId) && isNonEmptyString(assetSequence) && isNonEmptyString(assetURL));
	}

	private boolean isNonEmptyString(String str) {

		return (null != str && str.length() > 0);
	}
}
