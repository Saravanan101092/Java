/**
 * -----------------------------------------------------------------------
 * STAPLES, INC
 * -----------------------------------------------------------------------
 * (C) Copyright 2007 Staples, Inc.          All rights reserved.
 *
 * NOTICE:  All information contained herein or attendant hereto is,
 *          and remains, the property of Staples Inc.  Many of the
 *          intellectual and technical concepts contained herein are
 *          proprietary to Staples Inc. Any dissemination of this
 *          information or reproduction of this material is strictly
 *          forbidden unless prior written permission is obtained
 *          from Staples Inc.
 * -----------------------------------------------------------------------
 */
/*
 * File name     :   IntgSrvDAOFactory
 * Creation Date :   July 18, 2015
 * @author        :  Sima Zaslavsky 
 * @version 1.0
 */ 
package com.staples.pim.base.persistence.daofactory;

import com.staples.pim.base.common.exception.IntgSrvBatchException;
import com.staples.pim.base.util.IntgSrvUtilConstants;

 

public class IntgSrvDAOFactory {
	
	private static IntgSrvDAOFactory factoryImpl = null;
	/**
	 * Method to create an instance of DAO factory
	 * 
	 * @return returns an instance of DAOFactory
	 */
	private static synchronized IntgSrvDAOFactory getInstance()
	{
		if (factoryImpl == null)
		{
			factoryImpl = new IntgSrvDAOFactory();
		}
		return factoryImpl;
	}
	
	/**
	 * Method to create an instance of StplOrganizationDAOImpl
	 * 
	 * @return StplOrganizationDAOImpl
	 * @throws IntgSrvBatchException 
	 */
	public static ILocationLevelPushDownDAOLev getLocationLevelDAO() throws IntgSrvBatchException
	{
		Object dao = getInstance().createDaoInstance(IntgSrvUtilConstants.INTEGRATION_SERVICES_DAO_CLASS);
		return dao != null ? (ILocationLevelPushDownDAOLev) dao : null;
	}
	
	
	
	/**
	 * Method used to create instance of DAO object
	 * 
	 * @param daoClassName the name of the DAO class as a String
	 * @return returns an instance of Object
	 * @throws IntgSrvBatchException 
	 */
	private Object createDaoInstance(String daoClassName) throws IntgSrvBatchException
	{		
		
		Object daoInstance = null;
		LocationLevelPushDownDAOImpl daoInst = new LocationLevelPushDownDAOImpl();
		daoInstance = daoInst;
		
		/*  try
		{
			daoInstance =
				this
					.getClass()
					.getClassLoader()
					.loadClass(daoClassName)
					.newInstance();
		}
		catch (IllegalAccessException iae)
		{
			throw new IntgSrvBatchException("IllegalAccessException", iae);
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new IntgSrvBatchException("ClassNotFoundException", cnfe);
		}
		catch (InstantiationException ie)
		{
			throw new IntgSrvBatchException("InstantiationException", ie);
		}
		catch (Exception e)
		{
			throw new IntgSrvBatchException("Exception", e);
		}    */
		return daoInstance;
	}

}
