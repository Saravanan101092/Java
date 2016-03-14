package com.staples.pim.delegate.wercs.steptowercs.processor;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.CONTEXT_ID_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.EXPORT_CONTEXT_VALUE;
import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.WORKSPACE_ID_VALUE;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.staples.pcm.stepcontract.beans.MultiValueType;
import com.staples.pcm.stepcontract.beans.ObjectFactory;
import com.staples.pcm.stepcontract.beans.ProductType;
import com.staples.pcm.stepcontract.beans.ProductsType;
import com.staples.pcm.stepcontract.beans.STEPProductInformation;
import com.staples.pcm.stepcontract.beans.ValueType;
import com.staples.pcm.stepcontract.beans.ValuesType;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;
import com.staples.pim.delegate.wercs.common.PIPDatabaseHandlingService;
import com.staples.pim.delegate.wercs.common.SBDatabaseHandlingService;
import com.staples.pim.delegate.wercs.common.WercsAppConstants;
import com.staples.pim.delegate.wercs.model.MasterTableVO;
import com.staples.pim.delegate.wercs.steptowercs.bean.ResponseClass;
import com.staples.pim.delegate.wercs.steptowercs.bean.Upc;
import com.staples.pim.delegate.wercs.steptowercs.bean.Upclist;
import com.staples.pim.delegate.wercs.steptowercs.retry.runner.RunSchedulerWercsRetry;
import com.staples.pim.delegate.wercs.steptowercs.runner.RunSchedulerStepToWercs;

public class WERCSRestAPIProcessor {

	private static final String					TRACELOGGER_WERCS_STEPTOWERCS			= "tracelogger.wercs.steptowercs";
	static IntgSrvLogger						logger									= IntgSrvLogger
			.getInstance(TRACELOGGER_WERCS_STEPTOWERCS);
	
	
	public static final String USERNAME="UserName";
	public static final String PASSWORD="Password";
	public static final String CONTENTTYPE_STR="Content-Type";
	
	public static final String CONTENTTYPEJSON=IntgSrvPropertiesReader.getProperty("WERCSAPI_CONTENTTYPE_JSON");
	public static final String WERCS_USERNAME=IntgSrvPropertiesReader.getProperty("WERCSAPI_USERNAME");
	public static final String WERCS_PASSWORD=IntgSrvPropertiesReader.getProperty("WERCSAPI_PASSWORD");
	public static final String URL_AUTH=IntgSrvPropertiesReader.getProperty("WERCSAPI_URLAUTH");
	public static final String URL = IntgSrvPropertiesReader.getProperty("WERCSAPI_URL");
	
	public STEPProductInformation processSTEPWercsRequest(List<MasterTableVO> masterTableVOs){
		
		Map<String,MasterTableVO> masterTableVOMap = processWercsAPIRequests(masterTableVOs);
	
		if(masterTableVOMap!=null){
			logger.info("UPCs list not empty. No of UPCs="+masterTableVOs.size());
			
			return getSTEPProductInformationObjectForResponses(masterTableVOMap);
		}else{
			logger.info("UPCs list is empty");
			return null;
		}
	}

	public Map<String,MasterTableVO> processWercsAPIRequests(List<MasterTableVO> masterTableVOs){

		DatamigrationCommonUtil.printConsole("processWercsAPIRequests");

		Upclist upcList = new Upclist();
		List<Upc> responseList= new ArrayList<Upc>();
		Set<String> upcsUnique = new HashSet<String>();
		Map<String,MasterTableVO> masterTableVOMap = new HashMap<String,MasterTableVO>();
		
		for(MasterTableVO masterTableVO : masterTableVOs){

			Upc upc = new Upc();
			String upcNo = masterTableVO.getUPCNo();
			masterTableVOMap.put(upcNo, masterTableVO);
			if(upcNo != null){
				upcsUnique.add(upcNo);
				upc.setGtin(upcNo);
				upcList.getUpc().add(upc);
			}else{
				DatamigrationCommonUtil.printConsole("UPC number not found in the xml.");
			}

			if(upcList.getUpc().size()>999){
				
				logger.info("max no of UPCs per request reached. Getting RegistrationStatuses from WERCS API");
				DatamigrationCommonUtil.printConsole("unique upcs size:"+upcsUnique.size());
				List<Upc> responses = hitRestAPI(upcList);
				if(responses!=null){
					responseList.addAll(responses);
				}else{
					logger.error("Response list is null");
				}
				upcList.getUpc().clear();
			}
		}
		if(upcList.getUpc().size()>0){
			logger.info("Extracted UPC number from input files. Getting the RegistrationStatuses from WERCS");
			DatamigrationCommonUtil.printConsole("unique upcs size:"+upcsUnique.size());
			List<Upc> finalResponses = hitRestAPI(upcList);

			if(finalResponses!=null){
				responseList.addAll(finalResponses);
			}else{
				logger.error("response list is null");
			}
		}
		
		if(responseList.size()>0){
			masterTableVOMap=setRegistrationStatuses(responseList,masterTableVOMap);
		}else{
			DatamigrationCommonUtil.printConsole("ResponseList is empty!");
			logger.error("No response is received from WERCS");
			return null;
		}

		return masterTableVOMap;
	}

	public Map<String,MasterTableVO> setRegistrationStatuses(List<Upc> upcsResponseList,Map<String,MasterTableVO> masterTableMap){

		logger.info("Setting the Registration statuses in bean object");
		for(Upc upc :upcsResponseList){

			MasterTableVO masterTableVO = masterTableMap.get(upc.getGtin());
			
			//update response in db
			SBDatabaseHandlingService dbAccess;
			PIPDatabaseHandlingService pipDBAccess;
			if(RunSchedulerStepToWercs.context!=null){
				logger.info("Getting Step to WERCS context");
				dbAccess = new SBDatabaseHandlingService(RunSchedulerStepToWercs.datasource);
				pipDBAccess = new PIPDatabaseHandlingService(RunSchedulerStepToWercs.pipdatasource);
			}else{
				logger.info("Getting WERCSRetry context");
				dbAccess = new SBDatabaseHandlingService(RunSchedulerWercsRetry.datasource);
				pipDBAccess = new PIPDatabaseHandlingService(RunSchedulerWercsRetry.pipdatasource);
			}
			
			dbAccess.registrationStatusUpdate(masterTableVO.getUPCNo(),masterTableVO.getPipid(),upc.getStatus().intValue(),logger);
			pipDBAccess.pipRegistrationStatusUpdate(masterTableVO.getUPCNo(),masterTableVO.getPipid(),upc.getStatus().intValue(),logger);
			
			masterTableVO.setRegistrationStatus(upc.getStatus().intValue());
		}

		return masterTableMap;
	}
	public STEPProductInformation getSTEPProductInformationObjectForResponses(Map<String,MasterTableVO> masterTableVOMap){

		logger.info("Creating STEPProductInformation object for RegistrationStatus Responses");
		
		ObjectFactory objectFactory = new ObjectFactory();
		STEPProductInformation stepProductInformation = objectFactory.createSTEPProductInformation();
		ProductsType products = objectFactory.createProductsType();

		for(String upcNO : masterTableVOMap.keySet()){
			String upcNumber = upcNO;
			MasterTableVO masterTableVO = masterTableVOMap.get(upcNumber);
			String pipID = masterTableVO.getPipid();

			if(masterTableVO.getRegistrationStatus() != -1){
				String registrationStatus = Integer.toString(masterTableVO.getRegistrationStatus());
				if(!WercsAppConstants.REGSTATUS_1.equalsIgnoreCase(registrationStatus) && (WercsAppConstants.SUPPLIER_EXEMPT.equalsIgnoreCase(masterTableVO.getStepExemptFlag()))){

					logger.info("Supplier Exempt status received from STEP. UPC="+upcNO);
					
					SBDatabaseHandlingService dbAccess = new SBDatabaseHandlingService(RunSchedulerStepToWercs.datasource);
					dbAccess.auditTableInsert(masterTableVO.getUPCNo(),masterTableVO.getPipid(),506,logger);
					DatamigrationCommonUtil.printConsole("Send Exempt Mail:"+masterTableVO.getUPCNo()+" updated in DB Audit table");

					//send mail with XL sheet
					new SendExemptMailSender().createAndSendExemptMail(upcNumber, pipID, masterTableVO.getSupplierName(),masterTableVO.getToner_Wholesaler());
				}


				ProductType product = objectFactory.createProductType();
				product.setID(masterTableVO.getStepid());
				product.setUserTypeID(WercsAppConstants.ITEM);

				ValuesType values = objectFactory.createValuesType();

				MultiValueType multiValue = objectFactory.createMultiValueType();
				multiValue.setAttributeID(WercsAppConstants.A0080_STR);
				ValueType upcValue = objectFactory.createValueType();
				upcValue.setContent(upcNumber);
				multiValue.getValueOrValueGroup().add(upcValue);
				values.getValueOrMultiValueOrValueGroup().add(multiValue);

				ValueType statusValue = objectFactory.createValueType();
				statusValue.setAttributeID(WercsAppConstants.A2034_STR);
				statusValue.setContent(DatamigrationCommonUtil.getValuesFromLOV(WercsAppConstants.REGISTRATION_STATUSLOVKEY,registrationStatus , false));
				values.getValueOrMultiValueOrValueGroup().add(statusValue);

				product.getProductOrSequenceProductOrSuppressedProductCrossReference().add(values);
				products.getProduct().add(product);
			}else{
				logger.error("Registration status is -1. skipping upc="+upcNO);
			}
		}

		stepProductInformation.setExportTime(DatamigrationCommonUtil.getCurrentDateForSTEP()); // Current
		stepProductInformation.setContextID(CONTEXT_ID_VALUE); // Hard coded
		stepProductInformation.setExportContext(EXPORT_CONTEXT_VALUE);// Hard
		stepProductInformation.setWorkspaceID(WORKSPACE_ID_VALUE);
		stepProductInformation.setUseContextLocale(false);
		stepProductInformation.setProducts(products);
		return stepProductInformation;
	}

	public String getUpcNo(STEPProductInformation stepProductInfo){

		ProductsType products = stepProductInfo.getProducts();
		for(ProductType product : products.getProduct()){
			List<Object> valuesTypeObjects = product.getProductOrSequenceProductOrSuppressedProductCrossReference();

			for(Object valuesObject : valuesTypeObjects){
				if("ValuesType".equalsIgnoreCase(valuesObject.getClass().getSimpleName())){
					ValuesType values = (ValuesType)valuesObject;
					List<Object> valueObjects = values.getValueOrMultiValueOrValueGroup();
					for(Object valueObject : valueObjects){
						if("ValueType".equalsIgnoreCase(valueObject.getClass().getSimpleName())){
							ValueType value = (ValueType)valueObject;
							if("A0080".equalsIgnoreCase(value.getAttributeID())){
								return value.getContent();
							}
						}
					}
				}
			}
		}

		return null;
	}

	public List<Upc> hitRestAPI(Upclist upclist){

		DatamigrationCommonUtil.printConsole("Request UPC size:"+upclist.getUpc().size());
		Upclist responses=null;
		try{
			RestTemplate restTemplate = new RestTemplate();


			MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
			headers.add(USERNAME,WERCS_USERNAME);
			headers.add(PASSWORD,WERCS_PASSWORD);
			headers.add(CONTENTTYPE_STR, CONTENTTYPEJSON);

			logger.info("Getting Authentication token from WERCS");
			ResponseClass response= restTemplate.postForObject(URL_AUTH,headers, ResponseClass.class );

			DatamigrationCommonUtil.printConsole(response.getsErrorMessage());
			DatamigrationCommonUtil.printConsole("Token from server:"+response.getsToken());
			DatamigrationCommonUtil.printConsole("bError flag:"+response.getBError());

			if(!response.getBError()){
				logger.info("Authentication successfull. Token="+response.getsToken());
				HttpHeaders httpHeader = new HttpHeaders();
				httpHeader.setContentType(MediaType.TEXT_XML);

				JAXBContext jaxbContext = JAXBContext.newInstance(Upclist.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				StringWriter strWriter = new StringWriter();
				logger.info("Marshalling request xml object");
				jaxbMarshaller.marshal(upclist, strWriter);
				DatamigrationCommonUtil.printConsole(strWriter.toString());
				HttpEntity<String> entity = new HttpEntity<String>(strWriter.toString(), httpHeader);

				String urlForUPC=URL+response.getsToken();
				restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

				logger.info("Hitting API for Registration statuses");
				ResponseEntity<Upclist> resultString = restTemplate.exchange(urlForUPC, HttpMethod.POST, entity, Upclist.class);
				responses = resultString.getBody();
				return responses.getUpc();
			}else{
				logger.error("Authentication failed! berror=true");
				DatamigrationCommonUtil.printConsole("Authentication error!");
			}
		}catch(HttpClientErrorException httpclientException){
			DatamigrationCommonUtil.printConsole(new Date().toString());
			logger.error("HttpClientErrorException caught !! "+httpclientException.getMessage());
			IntgSrvUtils.alertByEmail(httpclientException, DatamigrationCommonUtil.getClassName(), "");
		}catch(Exception e){
			DatamigrationCommonUtil.printConsole(new Date().toString());
			DatamigrationCommonUtil.printConsole("Exception caught:"+e.getMessage());
			e.printStackTrace();
			logger.error("Exception caught while hitting REST API");
			IntgSrvUtils.alertByEmail(e, DatamigrationCommonUtil.getClassName(), "");
		}

		return null;
	}

	public static void main(String[] args){
		Upclist upclist = new Upclist();
		Upc upc1 = new Upc();
		upc1.setGtin("686875462398");
		upclist.getUpc().add(upc1);
		Upc upc2 = new Upc();
		upc2.setGtin("686875462399");
		upclist.getUpc().add(upc2);
		new WERCSRestAPIProcessor().hitRestAPI(upclist);
	}
}
