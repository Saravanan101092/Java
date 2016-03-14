
package com.staples.pim.delegate.pyramid.itemonbrdtmupdate.model;

import com.staples.pim.delegate.wayfair.taxonomyupdate.TaxonomyProcessor;

public class ItemOnbrdTmFeedBean {

	private String	superCategoryName;
	private String	categoryName;
	private String	departmentName;
	private String	className;
	private String	superCategoryId;
	private String	categoryId;
	private String	departmentId;
	private String	classId;
	private String	skuSetName;
	private String	businessUnit;

	public String getSuperCategoryName() {

		return superCategoryName;
	}

	public void setSuperCategoryName(String superCategoryName) {

		this.superCategoryName = superCategoryName;
	}

	public String getCategoryName() {

		return categoryName;
	}

	public void setCategoryName(String categoryName) {

		this.categoryName = categoryName;
	}

	public String getDepartmentName() {

		return departmentName;
	}

	public void setDepartmentName(String departmentName) {

		this.departmentName = departmentName;
	}

	public String getClassName() {

		return className;
	}

	public void setClassName(String className) {

		this.className = className;
	}

	public String getSuperCategoryId() {

		return superCategoryId;
	}

	public void setSuperCategoryId(String superCategoryId) {

		this.superCategoryId = superCategoryId;
	}

	public String getCategoryId() {

		return categoryId;
	}

	public void setCategoryId(String categoryId) {

		this.categoryId = categoryId;
	}

	public String getDepartmentId() {

		return departmentId;
	}

	public void setDepartmentId(String departmentId) {

		this.departmentId = departmentId;
	}

	public String getClassId() {

		return classId;
	}

	public void setClassId(String classId) {

		this.classId = classId;
	}

	public String getSkuSetName() {

		return skuSetName;
	}

	public void setSkuSetName(String skuSetName) {

		this.skuSetName = skuSetName;
	}

	public String getBusinessUnit() {

		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {

		this.businessUnit = businessUnit;
	}

	public String getGeneratedSuperCategoryId() {

		String prefix = businessUnit.equalsIgnoreCase("1") ? TaxonomyProcessor.STAPLES_SUPERCATEGORYID_PREFIX
				: TaxonomyProcessor.QUILL_SUPERCATEGORYID_PREFIX;
		return prefix + getSuperCategoryId();
	}

	public String getGeneratedCategoryId() {

		String prefix = businessUnit.equalsIgnoreCase("1") ? TaxonomyProcessor.STAPLES_CATEGORYID_PREFIX
				: TaxonomyProcessor.QUILL_CATEGORYID_PREFIX;
		return prefix + (getSuperCategoryId() + getCategoryId());
	}

	public String getGeneratedDepartmentId() {

		String prefix = businessUnit.equalsIgnoreCase("1") ? TaxonomyProcessor.STAPLES_DEPARTMENTID_PREFIX
				: TaxonomyProcessor.QUILL_DEPARTMENTID_PREFIX;

		return prefix + (getSuperCategoryId() + getCategoryId() + getDepartmentId());
	}

	public String getGeneratedClassId() {

		String prefix = businessUnit.equalsIgnoreCase("1") ? TaxonomyProcessor.STAPLES_CLASSID_PREFIX
				: TaxonomyProcessor.QUILL_CLASSID_PREFIX;
		return prefix + (getSuperCategoryId() + getCategoryId() + getDepartmentId() + getClassId());
	}

}
