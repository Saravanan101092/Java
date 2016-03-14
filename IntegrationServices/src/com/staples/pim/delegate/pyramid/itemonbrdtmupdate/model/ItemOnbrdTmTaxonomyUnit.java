
package com.staples.pim.delegate.pyramid.itemonbrdtmupdate.model;

public class ItemOnbrdTmTaxonomyUnit {

	private String	parentName	= "";
	private String	name		= "";
	private String	id			= "";

	public ItemOnbrdTmTaxonomyUnit(String parentName, String name, String id) {

		this.parentName = parentName;
		this.name = name;
		this.id = id;
	}

	public String getParentName() {

		return parentName;
	}

	public void setParentName(String parentName) {

		this.parentName = parentName;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	@Override
	public boolean equals(Object unit1) {

		return (this.getParentName().equals(((ItemOnbrdTmTaxonomyUnit) unit1).getParentName())
				&& this.getName().equals(((ItemOnbrdTmTaxonomyUnit) unit1).getName()) && this.getId().equals(
				((ItemOnbrdTmTaxonomyUnit) unit1).getId()));
	}

	@Override
	public int hashCode() {

		return name.hashCode() ^ id.hashCode();
	}

	public String toString() {

		return parentName + "\t" + name + "\t" + id + "\n";
	}
}
