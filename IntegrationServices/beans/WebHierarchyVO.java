package com.staples.pcm.springbatch.common.beans;

import com.staples.pcm.springbatch.common.utils.CommonUtil;

/**
 * @author 522462
 *
 */
public class WebHierarchyVO {

	private String detailId;
	private String detailName;
	private String detailDesc;
	private String detailDispInd;
	private Integer levelId;
	private String levelName;
	private String detailParentId;
//	private String activeInd;
//	private String deleteInd;
	//Phase 2 changes-extra column "Hierarchy ID" in Web Hierarchy sheet
	private String hierarchyId;
		
	 
	/**
	 * @param detailId
	 * @param detailName
	 * @param detailDesc
	 * @param detailDispInd
	 * @param levelId
	 * @param levelName
	 * @param detailParentId
	 * @param activeInd
	 * @param deleteInd
	 */
	public WebHierarchyVO(String detailId, String detailName,
			String detailDesc, String detailDispInd, Integer levelId,
			String levelName, String detailParentId,String hierarchyId) {
		super();
		this.detailId = detailId;
		this.detailName = detailName;
		this.detailDesc = detailDesc;
		this.detailDispInd = detailDispInd;
		this.levelId = levelId;
		this.levelName = levelName;
		this.detailParentId = detailParentId;
//		this.activeInd = activeInd;
//		this.deleteInd = deleteInd;
		//Phase 2 changes-extra column "Hierarchy ID" in Web Hierarchy sheet
		this.hierarchyId=hierarchyId;
	}
	
	//Phase 2
	public WebHierarchyVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the detailId
	 */
	public String getDetailId() {
		return detailId;
	}
	/**
	 * @param detailId the detailId to set
	 */
	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	/**
	 * @return the detailName
	 */
	public String getDetailName() {
		return detailName;
	}
	/**
	 * @param detailName the detailName to set
	 */
	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}
	/**
	 * @return the detailDesc
	 */
	public String getDetailDesc() {
		return detailDesc;
	}
	/**
	 * @param detailDesc the detailDesc to set
	 */
	public void setDetailDesc(String detailDesc) {
		this.detailDesc = detailDesc;
	}
	/**
	 * @return the detailDispInd
	 */
	public String getDetailDispInd() {
		return CommonUtil.converYESNOIntoChar(detailDispInd);
	}
	/**
	 * @param detailDispInd the detailDispInd to set
	 */
	public void setDetailDispInd(String detailDispInd) {
		this.detailDispInd = detailDispInd;
	}
	/**
	 * @return the levelId
	 */
	public Integer getLevelId() {
		return levelId;
	}
	/**
	 * @param levelId the levelId to set
	 */
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	/**
	 * @return the levelName
	 */
	public String getLevelName() {
		return levelName;
	}
	/**
	 * @param levelName the levelName to set
	 */
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	/**
	 * @return the detailParentId
	 */
	public String getDetailParentId() {
		return detailParentId;
	}
	/**
	 * @param detailParentId the detailParentId to set
	 */
	public void setDetailParentId(String detailParentId) {
		this.detailParentId = detailParentId;
	}
	/**
	 * @return the activeInd
	 */
	/*public String getActiveInd() {
		return activeInd;
	}
	*//**
	 * @param activeInd the activeInd to set
	 *//*
	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
	}
	*//**
	 * @return the deleteInd
	 *//*
	public String getDeleteInd() {
		return deleteInd;
	}
	*//**
	 * @param deleteInd the deleteInd to set
	 *//*
	public void setDeleteInd(String deleteInd) {
		this.deleteInd = deleteInd;
	}*/
	
	//Phase 2 changes-extra column "Hierarchy ID" in Web Hierarchy sheet
	/**
	 * @return hierarchyId
	 */
	public String getHierarchyId()
	{
		return hierarchyId;
	}
	/**
	 * @param hierarchyId
	 */
	public void setHierarchyId(String hierarchyId)
	{
		this.hierarchyId=hierarchyId;
	}
}
