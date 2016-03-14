
package com.staples.pim.delegate.wayfair.attributeupdate;

import java.util.ArrayList;
import java.util.List;

public class ClassVO {

	public ClassVO() {

		searchableAttributes = new ArrayList<String>();
		mandatoryAttributes = new ArrayList<String>();
		rangeAttributes = new ArrayList<String>();
		categorySpecAttribute = new ArrayList<String>();
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getAttributeGroup() {

		return attributeGroup;
	}

	public void setAttributeGroup(String attributeGroup) {

		this.attributeGroup = attributeGroup;
	}

	public String getCategoryID() {

		return categoryID;
	}

	public void setCategoryID(String categoryID) {

		this.categoryID = categoryID;
	}

	public List<String> getSearchableAttributes() {

		return searchableAttributes;
	}

	public void setSearchableAttributes(List<String> searchableAttributes) {

		this.searchableAttributes = searchableAttributes;
	}

	public List<String> getMandatoryAttributes() {

		return mandatoryAttributes;
	}

	public void setMandatoryAttributes(List<String> mandatoryAttributes) {

		this.mandatoryAttributes = mandatoryAttributes;
	}

	public List<String> getRangeAttributes() {

		return rangeAttributes;
	}

	public void setRangeAttributes(List<String> rangeAttributes) {

		this.rangeAttributes = rangeAttributes;
	}

	public List<String> getCategorySpecAttribute() {

		return categorySpecAttribute;
	}

	public void setCategorySpecAttribute(List<String> categorySpecAttribute) {

		this.categorySpecAttribute = categorySpecAttribute;
	}

	public String getAttributeGroupID() {

		return attributeGroupID;
	}

	public void setAttributeGroupID(String attributeGroupID) {

		this.attributeGroupID = attributeGroupID;
	}

	public String getID() {

		return ID;
	}

	public void setID(String iD) {

		ID = iD;
	}

	public String getHierarchyCode() {

		return hierarchyCode;
	}

	public void setHierarchyCode(String hierarchyCode) {

		this.hierarchyCode = hierarchyCode;
	}

	public String		categoryID;
	public List<String>	searchableAttributes;
	public List<String>	mandatoryAttributes;
	public List<String>	rangeAttributes;
	public List<String>	categorySpecAttribute;
	public String		hierarchyCode;

	public String		name;
	public String		attributeGroupID;
	public String		attributeGroup;
	public String		ID;
}
