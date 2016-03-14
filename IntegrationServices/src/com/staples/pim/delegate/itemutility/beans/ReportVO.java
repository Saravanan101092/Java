
package com.staples.pim.delegate.itemutility.beans;

/**
 * Value object created for DB transaction
 * 
 * @author 522462
 * 
 */
public class ReportVO {

	public String	A0003;
	// PCMP-766 channel specific implementation
	public Double	A0077_RET;
	// PCMP-766 channel specific implementation
	public Double	A0077_NAD;
	// changed from A0078 to A0497
	// PCMP-766 channel specific implementation
	public Double	A0497_RET;
	// PCMP-766 channel specific implementation
	public Double	A0497_NAD;
	public String	A0410;
	public String	A0013;
	public Integer	A0075;
	// added to make SKU no as Key
	public Integer	A0012;

	public String	A0017;
	public Double	A0051;
	public String	A0020;
	public String	A0015;
	public String	A0030;
	public Double	A0052;
	public String	A0385;

	public String getA0017() {

		return A0017;
	}

	public void setA0017(String a0017) {

		A0017 = a0017;
	}

	public Double getA0051() {

		return A0051;
	}

	public void setA0051(Double a0051) {

		A0051 = a0051;
	}

	public String getA0020() {

		return A0020;
	}

	public void setA0020(String a0020) {

		A0020 = a0020;
	}

	public String getA0015() {

		return A0015;
	}

	public void setA0015(String a0015) {

		A0015 = a0015;
	}

	public String getA0030() {

		return A0030;
	}

	public void setA0030(String a0030) {

		A0030 = a0030;
	}

	public Double getA0052() {

		return A0052;
	}

	public void setA0052(Double a0052) {

		A0052 = a0052;
	}

	public String getA0385() {

		return A0385;
	}

	public void setA0385(String a0385) {

		A0385 = a0385;
	}

	/**
	 * @return the a0003
	 */
	public String getA0003() {

		return A0003;
	}

	/**
	 * @param a0003
	 *            the a0003 to set
	 */
	public void setA0003(String a0003) {

		A0003 = a0003;
	}

	// PCMP-766 channel specific implementation
	/**
	 * @return the a0077_RET
	 */
	public Double getA0077_RET() {

		return A0077_RET;
	}

	// PCMP-766 channel specific implementation
	/**
	 * @param a0077_RET
	 *            the a0077_RET to set
	 */
	public void setA0077_RET(Double a0077_RET) {

		A0077_RET = a0077_RET;
	}

	// PCMP-766 channel specific implementation
	/**
	 * @return the a0077_RET
	 */
	public Double getA0077_NAD() {

		return A0077_NAD;
	}

	// PCMP-766 channel specific implementation
	/**
	 * @param a0077_NAD
	 *            the a0077_NAD to set
	 */
	public void setA0077_NAD(Double a0077_NAD) {

		A0077_NAD = a0077_NAD;
	}

	// PCMP-766 channel specific implementation
	/**
	 * @return the a0497_RET
	 */
	public Double getA0497_RET() {

		return A0497_RET;
	}

	// PCMP-766 channel specific implementation
	/**
	 * @param a0497_RET
	 *            the a0497_RET to set
	 */
	public void setA0497_RET(Double a0497_RET) {

		A0497_RET = a0497_RET;
	}

	// PCMP-766 channel specific implementation
	/**
	 * @return the a0497_NAD
	 */
	public Double getA0497_NAD() {

		return A0497_NAD;
	}

	// PCMP-766 channel specific implementation
	/**
	 * @param a0497_RET
	 *            the a0497_RET to set
	 */
	public void setA0497_NAD(Double a0497_NAD) {

		A0497_NAD = a0497_NAD;
	}

	/**
	 * @return the a0410
	 */
	public String getA0410() {

		return A0410;
	}

	/**
	 * @param a0410
	 *            the a0410 to set
	 */
	public void setA0410(String a0410) {

		A0410 = a0410;
	}

	/**
	 * @return the a0013
	 */
	public String getA0013() {

		return A0013;
	}

	/**
	 * @param a0013
	 *            the a0013 to set
	 */
	public void setA0013(String a0013) {

		A0013 = a0013;
	}

	/**
	 * @return the a0075
	 */
	public Integer getA0075() {

		return A0075;
	}

	/**
	 * @param a0075
	 *            the a0075 to set
	 */
	public void setA0075(Integer a0075) {

		A0075 = a0075;
	}

	/**
	 * @return the a0012
	 */
	public Integer getA0012() {

		return A0012;
	}

	/**
	 * @param a0012
	 *            the a0012 to set
	 */
	public void setA0012(Integer a0012) {

		A0012 = a0012;
	}

	@Override
	public String toString() {

		return "A0003=" + A0003 + ",A0077_RET=" + A0077_RET + ",A0077_NAD=" + A0077_NAD + ",A0497_RET=" + A0497_RET + ",A0497_NAD="
				+ A0497_NAD + ",A0410=" + A0410 + ",A0013=" + A0013 + ",A0075=" + A0075;
	}
}
