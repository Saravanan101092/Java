
package com.staples.pim.delegate.pyramid.itemonbrdtmupdate.model;

import java.util.LinkedHashSet;
import java.util.Set;

public class ItemOnbrdTmTaxonomyLevelRelation {

	private Set<ItemOnbrdTmTaxonomyUnit>	webSuperCategory	= new LinkedHashSet<ItemOnbrdTmTaxonomyUnit>();
	private Set<ItemOnbrdTmTaxonomyUnit>	webCategory			= new LinkedHashSet<ItemOnbrdTmTaxonomyUnit>();
	private Set<ItemOnbrdTmTaxonomyUnit>	webDepartment		= new LinkedHashSet<ItemOnbrdTmTaxonomyUnit>();
	private Set<ItemOnbrdTmTaxonomyUnit>	webClass			= new LinkedHashSet<ItemOnbrdTmTaxonomyUnit>();

	public String toString() {

		StringBuffer buffer = new StringBuffer();

		for (ItemOnbrdTmTaxonomyUnit unit : webSuperCategory) {
			buffer.append(unit);
		}

		for (ItemOnbrdTmTaxonomyUnit unit : webCategory) {
			buffer.append(unit);
		}

		for (ItemOnbrdTmTaxonomyUnit unit : webDepartment) {
			buffer.append(unit);
		}

		for (ItemOnbrdTmTaxonomyUnit unit : webClass) {
			buffer.append(unit);
		}

		return buffer.toString();
	}

	public Set<ItemOnbrdTmTaxonomyUnit> getWebSuperCategory() {

		return webSuperCategory;
	}

	public void setWebSuperCategory(Set<ItemOnbrdTmTaxonomyUnit> webSuperCategory) {

		this.webSuperCategory = webSuperCategory;
	}

	public Set<ItemOnbrdTmTaxonomyUnit> getWebCategory() {

		return webCategory;
	}

	public void setWebCategory(Set<ItemOnbrdTmTaxonomyUnit> webCategory) {

		this.webCategory = webCategory;
	}

	public Set<ItemOnbrdTmTaxonomyUnit> getWebDepartment() {

		return webDepartment;
	}

	public void setWebDepartment(Set<ItemOnbrdTmTaxonomyUnit> webDepartment) {

		this.webDepartment = webDepartment;
	}

	public Set<ItemOnbrdTmTaxonomyUnit> getWebClass() {

		return webClass;
	}

	public void setWebClass(Set<ItemOnbrdTmTaxonomyUnit> webClass) {

		this.webClass = webClass;
	}

}
