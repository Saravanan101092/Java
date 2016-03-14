
package com.staples.pim.delegate.datamigration.utils;

import javax.xml.bind.JAXBContext;

import com.staples.pcm.stepcontract.beans.STEPProductInformation;

public class STEPProductInformationJAXBContextSingleton {

	/**
	 * Private constructor. Prevents instantiation from other classes.
	 */
	private STEPProductInformationJAXBContextSingleton() {

	}

	/**
	 * Initializes singleton.
	 */
	private static class SingletonHolder {

		private static final JAXBContext	JAXBContextObj;
		static {
			try {
				JAXBContextObj = JAXBContext.newInstance(STEPProductInformation.class);
			} catch (Exception exception) {
				throw new RuntimeException("Singleton JAXBContext, an error occurred!", exception);
			}
		}
	}

	public static JAXBContext getInstance() {

		return SingletonHolder.JAXBContextObj;
	}
}
