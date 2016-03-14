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
 * File name     :   
 * Creation Date :   
 * @author  
 * @version 1.0
 */ 

package com.staples.pim.delegate.futuredatedpo.processor;

import java.util.HashMap;

import com.staples.pim.base.util.IntgSrvAppConstants;

public class TransformationRulesForFDPO {

	public static HashMap<String, String> overrideValuesForFutureDatedPO(HashMap<String, String> allValuesHashMap)
	{
		HashMap<String, String> allValues = new HashMap<String, String>(); 
		allValues.putAll(allValuesHashMap);  

		if (allValues.get(IntgSrvAppConstants.A0200)!= null  			&& 
				!allValues.get(IntgSrvAppConstants.A0200).equals("") 	&& 
				((String)allValues.get(IntgSrvAppConstants.A0009 )).equalsIgnoreCase(IntgSrvAppConstants.COST) 	&&
				(((String)allValues.get(IntgSrvAppConstants.A0410)).equalsIgnoreCase(IntgSrvAppConstants.CORPORATE)  ||
						((String)allValues.get(IntgSrvAppConstants.A0410)).equalsIgnoreCase(IntgSrvAppConstants.RETAIL)) )  	    
		{
			allValues.put(IntgSrvAppConstants.A0202,IntgSrvAppConstants.CO);   
			allValues.put(IntgSrvAppConstants.A0077,"0");
			//fix me 
			allValues.put(IntgSrvAppConstants.A0077_RET,"0");
			//				allValues.put(IntgSrvAppConstants.A0077_NAD,"0");

			allValues.put(IntgSrvAppConstants.A0078,
					(String)allValues.get(IntgSrvAppConstants.A0200)); 
			allValues.put(IntgSrvAppConstants.A0078_RET,
					(String)allValues.get(IntgSrvAppConstants.A0200)); 
			//				allValues.put(IntgSrvAppConstants.A0078_NAD,
			//						(String)allValues.get(IntgSrvAppConstants.A0200)); 
		} 
		else
			if (allValues.get(IntgSrvAppConstants.A0204)!= null  			&& 
			!allValues.get(IntgSrvAppConstants.A0204).equals("") 	&& 
			((String)allValues.get(IntgSrvAppConstants.A0009)).equalsIgnoreCase(IntgSrvAppConstants.LIST)) 				
			{ 




				//PCMP-3070 seperated CORPORATE and RETAIL LOGICS 

				if(((String)allValues.get(IntgSrvAppConstants.A0410)).equalsIgnoreCase(IntgSrvAppConstants.CORPORATE))
				{

					allValues.put(IntgSrvAppConstants.A0202,IntgSrvAppConstants.LI);
					allValues.put(IntgSrvAppConstants.A0077,
							(String)allValues.get(IntgSrvAppConstants.A0204));
					//fix me 
					allValues.put(IntgSrvAppConstants.A0077_RET,
							(String)allValues.get(IntgSrvAppConstants.A0204));
					allValues.put(IntgSrvAppConstants.A0077_NAD,
							(String)allValues.get(IntgSrvAppConstants.A0204));

					allValues.put(IntgSrvAppConstants.A0078,"0");
					allValues.put(IntgSrvAppConstants.A0078_RET,"0");
					//			allValues.put(IntgSrvAppConstants.A0078_NAD,"0");
					allValues.put(IntgSrvAppConstants.A0200,(String)allValues.get(IntgSrvAppConstants.A0204));
				}

				if(((String)allValues.get(IntgSrvAppConstants.A0410)).equalsIgnoreCase(IntgSrvAppConstants.RETAIL))
				{

					allValues.put(IntgSrvAppConstants.A0202,IntgSrvAppConstants.LI);
					allValues.put(IntgSrvAppConstants.A0077,
							(String)allValues.get(IntgSrvAppConstants.A0204));
					//fix me 
					allValues.put(IntgSrvAppConstants.A0077_RET,
							(String)allValues.get(IntgSrvAppConstants.A0204));


					allValues.put(IntgSrvAppConstants.A0078,"0");
					allValues.put(IntgSrvAppConstants.A0078_RET,"0");
					//			allValues.put(IntgSrvAppConstants.A0078_NAD,"0");
					allValues.put(IntgSrvAppConstants.A0200,(String)allValues.get(IntgSrvAppConstants.A0204));
				}




			}  


			else
				if (allValues.get(IntgSrvAppConstants.A0204)!= null  			&&  
				allValues.get(IntgSrvAppConstants.A0200)!= null  		&&
				!allValues.get(IntgSrvAppConstants.A0204).equals("") 	&& 
				!allValues.get(IntgSrvAppConstants.A0200).equals("") 	&& 
				allValues.get(IntgSrvAppConstants.A0009)!= null  		&& 
				!allValues.get(IntgSrvAppConstants.A0009).equals("")  	&& 
				((String)allValues.get(IntgSrvAppConstants.A0009)).equalsIgnoreCase(IntgSrvAppConstants.BOTH))   			
				{ 
					allValues.put(IntgSrvAppConstants.A0202,IntgSrvAppConstants.CO);  
					allValues.put(IntgSrvAppConstants.A0077,"0");
					//					fix me
					allValues.put(IntgSrvAppConstants.A0077_RET,"0");
					//					allValues.put(IntgSrvAppConstants.A0077_NAD,"0");
					allValues.put(IntgSrvAppConstants.A0078,
							(String)allValues.get(IntgSrvAppConstants.A0200));
					allValues.put(IntgSrvAppConstants.A0078_RET,
							(String)allValues.get(IntgSrvAppConstants.A0200)); 
					//					allValues.put(IntgSrvAppConstants.A0078_NAD,
					//							(String)allValues.get(IntgSrvAppConstants.A0200)); 
				}  

		if (((String)allValues.get(IntgSrvAppConstants.A0410)).equalsIgnoreCase(IntgSrvAppConstants.SCC)) 
		{
			if (allValues.get(IntgSrvAppConstants.A0200)!= null  && 
					!allValues.get(IntgSrvAppConstants.A0200).equals("")) 	     
			{  
				allValues.put(IntgSrvAppConstants.A0078,
						(String)allValues.get(IntgSrvAppConstants.A0200));
				allValues.put(IntgSrvAppConstants.A0078_RET,
						(String)allValues.get(IntgSrvAppConstants.A0200));
				//		 			allValues.put(IntgSrvAppConstants.A0078_NAD,
				//		 					(String)allValues.get(IntgSrvAppConstants.A0200));
				allValues.put(IntgSrvAppConstants.A0202,IntgSrvAppConstants.CO); 
			} 
			else   	    
			{  
				allValues.put(IntgSrvAppConstants.A0078,"0");
				allValues.put(IntgSrvAppConstants.A0078_RET,"0");
				//		 			allValues.put(IntgSrvAppConstants.A0078_NAD,"0");
			} 

			if (allValues.get(IntgSrvAppConstants.A0204)!= null  && 
					!allValues.get(IntgSrvAppConstants.A0204).equals(""))
			{
				allValues.put(IntgSrvAppConstants.A0077,
						(String)allValues.get(IntgSrvAppConstants.A0204));
				//PCMP-3070 if A0140 have value as SCC A0077_NAD will get A0204 value 
				//	allValues.put(IntgSrvAppConstants.A0077_RET,
				//		(String)allValues.get(IntgSrvAppConstants.A0204));
				allValues.put(IntgSrvAppConstants.A0077_NAD,
						(String)allValues.get(IntgSrvAppConstants.A0204));
				if (allValues.get(IntgSrvAppConstants.A0200)== null  ||  
						allValues.get(IntgSrvAppConstants.A0200).equals("")) 	
				{
					allValues.put(IntgSrvAppConstants.A0202,IntgSrvAppConstants.LI); 
				}
			}
			else   	    
			{  
				allValues.put(IntgSrvAppConstants.A0077,"0");
				//		 			fix me
				allValues.put(IntgSrvAppConstants.A0077_RET,"0");
				//		 			allValues.put(IntgSrvAppConstants.A0077_NAD,"0");
			} 
		}	


		if (allValues.get(IntgSrvAppConstants.ITMDECCNT) != null)
		{
			allValues.put(IntgSrvAppConstants.ITMDECCNT, "1");
		}
		if (allValues.get(IntgSrvAppConstants.ITMSKUCNT) != null)
		{
			allValues.put(IntgSrvAppConstants.ITMSKUCNT, "1");
		}
		if (allValues.get(IntgSrvAppConstants.ITMWSKUCNT) != null)
		{
			allValues.put(IntgSrvAppConstants.ITMWSKUCNT, "1");
		}
		if (allValues.get(IntgSrvAppConstants.ITMVNDCNT) != null)
		{
			allValues.put(IntgSrvAppConstants.ITMVNDCNT, "1");
		}
		if (allValues.get(IntgSrvAppConstants.ITMSUPCCNT) != null)
		{
			allValues.put(IntgSrvAppConstants.ITMSUPCCNT, "1");
		}
		if (allValues.get(IntgSrvAppConstants.ITMCUPCCNT) != null)
		{
			allValues.put(IntgSrvAppConstants.ITMCUPCCNT, "1");
		}
		if (allValues.get(IntgSrvAppConstants.ITMIUPCCNT) != null)
		{
			allValues.put(IntgSrvAppConstants.ITMIUPCCNT, "1");
		}
		if (allValues.get(IntgSrvAppConstants.ITMPUPCCNT) != null)
		{
			allValues.put(IntgSrvAppConstants.ITMPUPCCNT, "1");
		}
		if (allValues.get(IntgSrvAppConstants.ITMHAZCNT) != null)
		{
			allValues.put(IntgSrvAppConstants.ITMHAZCNT, "1");
		}
		if (allValues.get(IntgSrvAppConstants.ITMENVCNT) != null)
		{
			allValues.put(IntgSrvAppConstants.ITMENVCNT, "1");
		}
		if (allValues.get(IntgSrvAppConstants.ITMOTHCNT) != null)
		{
			allValues.put(IntgSrvAppConstants.ITMOTHCNT, "1");
		}
		if (allValues.get(IntgSrvAppConstants.ITMPVTCNT) != null)
		{
			allValues.put(IntgSrvAppConstants.ITMPVTCNT, "1"); 
		}

		if (allValues.get(IntgSrvAppConstants.A0205) != null)
		{
			if (allValues.get(IntgSrvAppConstants.A0205).equalsIgnoreCase(IntgSrvAppConstants.SKU_LEVEL))
			{ 
				allValues.put(IntgSrvAppConstants.A0205, IntgSrvAppConstants.YES); 
				allValues.put(IntgSrvAppConstants.A0206, IntgSrvAppConstants.NO); 
			}
		}

		if (allValues.get(IntgSrvAppConstants.A0205) != null && 
				allValues.get(IntgSrvAppConstants.A0205).equalsIgnoreCase(IntgSrvAppConstants.SPECIFIC_DC))
		{ 
			allValues.put(IntgSrvAppConstants.A0205, IntgSrvAppConstants.NO);
			allValues.put(IntgSrvAppConstants.A0206, IntgSrvAppConstants.YES); 
			allValues.put(IntgSrvAppConstants.A0203, allValues.get(IntgSrvAppConstants.A0203_DC)); 
		}
		else if (allValues.get(IntgSrvAppConstants.A0205) != null && 
				allValues.get(IntgSrvAppConstants.A0205).equalsIgnoreCase(IntgSrvAppConstants.SPECIFIC_FC))
		{  
			allValues.put(IntgSrvAppConstants.A0205, IntgSrvAppConstants.NO);
			allValues.put(IntgSrvAppConstants.A0206, IntgSrvAppConstants.YES); 
			allValues.put(IntgSrvAppConstants.A0203, allValues.get(IntgSrvAppConstants.A0203_FC)); 
		}
		else  if (allValues.get(IntgSrvAppConstants.A0205) != null && !allValues.get(IntgSrvAppConstants.A0205).equalsIgnoreCase(IntgSrvAppConstants.SPECIFIC_DC)  && 
				!allValues.get(IntgSrvAppConstants.A0205).equalsIgnoreCase(IntgSrvAppConstants.SPECIFIC_FC))
		{
			allValues.put(IntgSrvAppConstants.A0203, " ");  
			allValues.put(IntgSrvAppConstants.A0206, IntgSrvAppConstants.NO); 
		}

		return allValues;
	}

}
