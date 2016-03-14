package com.staples.pim.delegate.wercs.steptopip.processor;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;

import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.delegate.wercs.common.SBDatabaseHandlingService;
import com.staples.pim.delegate.wercs.model.MasterTableVO;



public class StepToPIPIntgProcessor implements ItemProcessor<STEPProductInformation, MasterTableVO>
{
	
public SBDatabaseHandlingService databaseAccessor;
	
//	public static final String[] mandatoryAttributes = {"A2028","WERCS_Out_Trigger","A0080","A0405","A0013_RET","A0282","SupplierHierID","A0009","A0621","STEP_ID"};

	@Override
	public MasterTableVO process(STEPProductInformation stepProductInformation) throws Exception {
		
		System.out.println("WERCS processor.");
		String idList = "A0484,A0012,A0258,A0405,A0015,A0030,A0406,A0026,A0027,A0031,A0144,A0146,A0145,A0147,A0271,A0130,A0134,A0135,A0136,A0138,A0137,A0140,A0139,A0142,A0143,A0374,A0214,A0248,A0254,A0322,A0398,A0399,A0384,A0017,A0051,A0038,A0033,A0041,A0019,A0021,A0020,A0260,A0259,A0257,A0080,A0083,A0082,A0084,A0081,A0127,A0124,A0125,A0141,A0194,A0195,A0016,A0122,A0123,A0126,A0178,A0237,A0238,A0240,A0239,A0308,A0310,A0309,A0307,A0220,A0068,A0069,A0071,A0070,A0313,A0311,A0312,A0314,A0315,A0420,A0190,A0191,A0189,A0085,A0086,A0087,A0088,A0089,A0323,A0324,A0090,A0091,A0092,A0093,A0094,A0095,A0120,A0097,A0098,A0099,A0100,A0101,A0104,A0103,A0102,A0105,A0107,A0108,A0113,A0115,A0116,A0117,A0111,A0114,A0106,A0112,A0109,A0110,A0118,A0119,A0096,A0121,A0128,A0148,A0149,A0151,A0150,A0152,A0154,A0157,A0162,A0163,A0156,A0155,A0164,A0161,A0158,A0160,A0159,A0166,A0165,A0167,A0168,A0169,A0339,A0174,A0173,A0153,A0171,A0172,A0175,A0342,A0177,A0409,A0400,A0402,A0401,A0203,A0410,A0202,A0320,A0318,A0008,A0404,A0241,A0181,A0182,A0185,A0186,A0183,A0179,A0214,A0007,A0499,A0500,A0504,A0506,A0507,A0251,A0252,A0253,A0431,A0231,A0234,A0229,A0011,A0213,A0255,A0256,A0213,A0180,A0018_RET,A0013_RET,A0022,A0036,A0037,A0419,A0042,A0301,A0052,A0043,A0075_RET,A0302_RET,A0078_RET,A0077_RET,A0028,A0303,A0277,A0029,A0249,A0250,A0067_RET,A0045_RET,A0046_RET,A0065,A0066,A0023,A0281,A0279,A0283,A0284,A0416,A0417,A0418,A0289,A0290,A0291,A0292,A0293,A0299,A0029,A0184,A0018_NAD,A0013_NAD,A0385,A0210,A0211,A0212,A0391,A0224,A0075_NAD,A0302_NAD,A0078_NAD,A0077_NAD,A0197,A0243,A0244,A0067_NAD,A0045_NAD,A0046_NAD,A0230,A0304,A0305,A0217,A0076,A0508,A0509,A0516";
		String attributeIdList[] = idList.split(",");
		Map<String,String> valuesInXml = getMasterTableVO(stepProductInformation);
	//	System.out.println("*********************");
		for(String value : attributeIdList)
		{
		//	System.out.println(value + "-" + valuesInXml.get(value));
			
		}
//		databaseAccessor.pipTableUpdate(valuesInXml);	
		
		return null;
		
	}
	
	public Map<String,String> getMasterTableVO(STEPProductInformation stepProductInformation){
		
		
		Map<String,String> valuesInXml = new HashMap<String,String>();

		ProductsType products = stepProductInformation.getProducts();
		List<ProductType> productList = products.getProduct();
		for(ProductType product:productList){
			valuesInXml.put("STEP_ID", product.getID());
			for(Object productObj : product.getProductOrSequenceProductOrSuppressedProductCrossReference()){
				if("ValuesType".equalsIgnoreCase(productObj.getClass().getSimpleName())){
					ValuesType values = (ValuesType) productObj;
					for(Object valueObj : values.getValueOrMultiValueOrValueGroup()){
						if("ValueType".equalsIgnoreCase(valueObj.getClass().getSimpleName())){
							ValueType value = (ValueType) valueObj;
							valuesInXml.put(value.getAttributeID(), value.getContent());
						}

					}
				}
			}
		}
		return valuesInXml;
	}
	

}
