package com.staples.pim.delegate.wercs.corpdmztostep.processor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;

import com.staples.pim.delegate.wercs.model.WercsCollectionBean;

public class CopdmzToStepRegDataProcessor  implements ItemProcessor<WercsCollectionBean, WercsCollectionBean> {
	
	public static String A0080 = "A0080";
	public static String STEPID =null;
	public static String SKU =null;
	public static String PIPID =null;
	public static String UPC =null;
	public static int value =346789;
	public static String mailList ="Sankar.Suganya@Staples.com";
	protected String clazzname = this.getClass().getName();

	@Override
	public WercsCollectionBean process(WercsCollectionBean wercsregulatorydataFeedBean) throws Exception {
	
		
		String firstvalue=wercsregulatorydataFeedBean.getAttributeValueMap().get(A0080);
		System.out.println("firstvalue : "+firstvalue);
		List<String> UPCValue=getDataBaseMasterTableConnect(firstvalue);
		System.out.println("UPCValue : "+UPCValue);
		WercsCollectionBean wercsCollectionBeanObj1 = new WercsCollectionBean();
		Map<String, Map<String, String>> mapObject1 = new HashMap<String, Map<String, String>>();
		
		UPC=UPCValue.get(0);	
		PIPID=UPCValue.get(1); 
		SKU=UPCValue.get(2); 
		STEPID=UPCValue.get(3);  
		String GroupID=getGroupId(SKU,PIPID,STEPID);
		
		 List<String> wholeList = new ArrayList<String>();
		 wholeList.add(GroupID);
		 for(String string : wholeList)
		 {
		 //wercsCollectionBeanObj1.setPIPID(string);
		 }
		if(UPC!=null && UPC.equals(firstvalue))
		{
				mapObject1.put(UPC, wercsregulatorydataFeedBean.getAttributeValueMap());
				System.out.println("mapobject value : "+mapObject1);
				
				wercsCollectionBeanObj1.setAttributeValueMap(mapObject1.get(UPC));
				System.out.println("wercscollectionbean : "+wercsCollectionBeanObj1.getAttributeValueMap());
			}

		return wercsCollectionBeanObj1;
		
	}

	private String getGroupId(String SKU, String PIPID, String STEPID) {
		
		String GroupId=null;
		if(PIPID!=null)
		{
			GroupId=PIPID;
		}
		else if(SKU!=null)
		{
			GroupId=SKU;
		}
		else if(STEPID!=null)
		{
			GroupId=STEPID;
		}

		// TODO Auto-generated method stub
		return GroupId;
	}

	private List<String> getDataBaseCrossRefTableConnect(String firstvalue) {

		Connection con = null;
	     Statement stmt = null;
		System.out.println("firstvalue :::"+firstvalue);
		ResultSet rs=null;
		List<String> list = new ArrayList<String>();
	        try {
	        	
	        	Class.forName("oracle.jdbc.driver.OracleDriver");  
	        	  
	        	//step2 create  the connection object  
	        	con=DriverManager.getConnection(  
	        	"jdbc:oracle:thin:@lpcmdndbv05.staples.com:51521/PCMDEV5","SB_OWNER","pcm_sbowner_d5");  
	        	  
	        	//step3 create the statement object  
	        	stmt=con.createStatement();  
	        	  
	        	//step4 execute query  
	        	rs=stmt.executeQuery("SELECT UPC,PIP_ID,SKU,STEP_ID FROM WERCS_CROSS_REFERENCE where UPC='" + firstvalue + "'");
	        	if(rs.next()){
	        		System.out.println("UPC available cross reference table");
	        		   list.add(rs.getString(1));
	        		   list.add(rs.getString(2));
	        		   list.add(rs.getString(3));
	        		   list.add(rs.getString(4));
	        		   
	        		}
	        	else
	        	{
	        		System.out.println("sending mail to LP Team");
	        		SendMail();
	        	}
	        	
	        	con.close();  
	        	  
	        	}catch(Exception e)
	        	{
	        		System.out.println(e);
	        	}  
	        return list;
	        	  
	}  
	
	private void SendMail() {
		System.out.println("sending mail to LP Team");

		// TODO Auto-generated method stub
		
	}


	private List<String> getDataBaseMasterTableConnect(String firstvalue) {

		// Connection con = null;
	     //   Statement stmt = null;
		System.out.println("firstvalue : "+firstvalue);
		 List<String> list = new ArrayList<String>();
		 ResultSet rs=null;
	        try {
	        	
	        	Class.forName("oracle.jdbc.driver.OracleDriver");  
	        	  
	        	//step2 create  the connection object  
	        	Connection con=DriverManager.getConnection(  
	        	"jdbc:oracle:thin:@lpcmdndbv05.staples.com:51521/PCMDEV5","SB_OWNER","pcm_sbowner_d5");  
	        	  
	        	//step3 create the statement object  
	        	Statement stmt=con.createStatement();  
	        	  
	        	//step4 execute query  
	        	 rs=stmt.executeQuery("SELECT UPC,PIP_ID,SKU,STEP_ID FROM SB_OWNER.WERCS_STATUS_MASTER WHERE WERCS_OUT_TRIGGER IN ('GETSTATUS', 'SENDMAILEXCEMPT') AND REGISTRATION_STATUS=1 AND REGULATORY_DATA_STATUS='0' AND UPC='" + firstvalue + "'");
				if(rs.next())  
				{
					System.out.println("All the conditions are satisfied");
			   list.add(rs.getString(1));
     		   list.add(rs.getString(2));
     		   list.add(rs.getString(3));
     		   list.add(rs.getString(4));
	        	//step5 close the connection object  
				}
				else
				{
				rs=stmt.executeQuery("SELECT UPC,PIP_ID,SKU,STEP_ID FROM SB_OWNER.WERCS_STATUS_MASTER WHERE UPC='" + firstvalue + "'");
        		if(rs.next())
        		{
        			System.out.println("UPC Available");
    	        		   list.add(rs.getString(1));
    	        		   list.add(rs.getString(2));
    	        		   list.add(rs.getString(3));
    	        		   list.add(rs.getString(4));
				}
        		
        		else
        		{
        			list=getDataBaseCrossRefTableConnect(firstvalue);
        		}
        		
        	}
	        	con.close();  
	        	  
	        	}catch(Exception e)
	        	{
	        		System.out.println(e);
	        	}  
	        return list;
	        	  
	        	}  
	
	
}
	        
	
	


	