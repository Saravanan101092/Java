
package com.staples.pim.delegate.itemutility.beans;

/**
 * Value object created for holding the attribute name and position of the
 * corresponding attribute
 * 
 * @author 522462
 * 
 */
public class PropertiesVO {

	String	propertyKey;

	String	attributeName;

	int		startPosition;

	int		endPosition;

	/**
	 * @return the propertyKey
	 */
	public String getPropertyKey() {

		return propertyKey;
	}

	/**
	 * @param propertyKey
	 *            the propertyKey to set
	 */
	public void setPropertyKey(String propertyKey) {

		this.propertyKey = propertyKey;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {

		return attributeName;
	}

	/**
	 * @param attributeName
	 *            the attributeName to set
	 */
	public void setAttributeName(String attributeName) {

		this.attributeName = attributeName;
	}

	/**
	 * @return the startPosition
	 */
	public int getStartPosition() {

		return startPosition;
	}

	/**
	 * @param startPosition
	 *            the startPosition to set
	 */
	public void setStartPosition(int startPosition) {

		this.startPosition = startPosition;
	}

	/**
	 * @return the endPosition
	 */
	public int getEndPosition() {

		return endPosition;
	}

	/**
	 * @param endPosition
	 *            the endPosition to set
	 */
	public void setEndPosition(int endPosition) {

		this.endPosition = endPosition;
	}

	public String toString() {

		return "propertyKey : " + propertyKey + " attributeName: " + attributeName + " startPosition : " + startPosition
				+ " endPosition : " + endPosition;
	}
}
