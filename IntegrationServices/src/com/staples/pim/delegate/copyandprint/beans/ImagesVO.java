
package com.staples.pim.delegate.copyandprint.beans;

/**
 * @author 522462
 * 
 */
public class ImagesVO {

	private String	entityId;
	private String	entityType;
	private String	imageId;
	private String	imageType;
	private String	defInd;

	public ImagesVO() {

	}

	public ImagesVO(String entityId, String entityType, String imageId, String imageType, String defInd) {

		this.entityId = entityId;
		this.entityType = entityType;
		this.imageId = imageId;
		this.imageType = imageType;
		this.defInd = defInd;
	}

	/**
	 * @return the entityId
	 */
	public String getEntityId() {

		return entityId;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(String entityId) {

		this.entityId = entityId;
	}

	/**
	 * @return the entityType
	 */
	public String getEntityType() {

		return entityType;
	}

	/**
	 * @param entityType
	 *            the entityType to set
	 */
	public void setEntityType(String entityType) {

		this.entityType = entityType;
	}

	/**
	 * @return the imageId
	 */
	public String getImageId() {

		return imageId;
	}

	/**
	 * @param imageId
	 *            the imageId to set
	 */
	public void setImageId(String imageId) {

		this.imageId = imageId;
	}

	/**
	 * @return the imageType
	 */
	public String getImageType() {

		return imageType;
	}

	/**
	 * @param imageType
	 *            the imageType to set
	 */
	public void setImageType(String imageType) {

		this.imageType = imageType;
	}

	/**
	 * @return the defInd
	 */
	public String getDefInd() {

		return defInd;
	}

	/**
	 * @param defInd
	 *            the defInd to set
	 */
	public void setDefInd(String defInd) {

		this.defInd = defInd;
	}

}
