package com.staples.pim.base.common.bean;

import static com.staples.pim.delegate.productupdate.processor.ProductInboundProcessor.VENDORITEM_PRODUCT_ID_PREFIX_NON_STOCKED;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List; 
import java.util.Set;
 
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue; 

import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkHandler;
import com.staples.common.errorhandlingframework.ErrorHandlingFrameworkICD;
import com.staples.common.errorhandlingframework.exceptions.ErrorHandlingFrameworkException;
import com.staples.pim.base.common.errorhandling.IntgSrvErrorHandlingFrameworkConstants;
import com.staples.pim.base.common.logging.IntgSrvLogger;
import com.staples.pim.base.domain.ProductAttributesInProcess;
import com.staples.pim.base.loader.IntgSrvPropertiesReader;
import com.staples.pim.base.util.IntgSrvAppConstants;
import com.staples.pim.base.util.IntgSrvLocationLevelUtils;
import com.staples.pim.base.util.IntgSrvUtils;
import com.staples.pim.delegate.commonusecases.formatter.FormaterToFixLength;
import com.staples.pim.delegate.commonusecases.formatter.FormatterToXSVFile;
import com.staples.pim.delegate.commonusecases.writer.PublisherToMQ;
import com.staples.pim.delegate.createupdateitem.listenerrunner.RunSchedulerItemCreateUpdate;
import com.staples.pim.delegate.locationlevel.bean.StepLocationLevelAttrib;
import com.staples.pim.delegate.locationlevel.bean.LocationDataAudit;
/**
* <p>Java class for anonymous complex type.
*
* <p>The following schema fragment specifies the expected content contained within this class.
*
* <pre>
* &lt;complexType>
*   &lt;complexContent>
*     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
*       &lt;sequence>
*         &lt;element name="Products">
*           &lt;complexType>
*             &lt;complexContent>
*               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
*                 &lt;sequence>
*                   &lt;element name="Product" maxOccurs="unbounded" minOccurs="0">
*                     &lt;complexType>
*                       &lt;complexContent>
*                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
*                           &lt;sequence>
*                             &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                             &lt;element name="ClassificationReference" maxOccurs="unbounded" minOccurs="0">
*                               &lt;complexType>
*                                 &lt;simpleContent>
*                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
*                                     &lt;attribute name="ClassificationID" type="{http://www.w3.org/2001/XMLSchema}string" />
*                                     &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}string" />
*                                   &lt;/extension>
*                                 &lt;/simpleContent>
*                               &lt;/complexType>
*                             &lt;/element>
*                             &lt;element name="AssetCrossReference">
*                               &lt;complexType>
*                                 &lt;simpleContent>
*                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
*                                     &lt;attribute name="AssetID" type="{http://www.w3.org/2001/XMLSchema}string" />
*                                     &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}string" />
*                                   &lt;/extension>
*                                 &lt;/simpleContent>
*                               &lt;/complexType>
*                             &lt;/element>
*                             &lt;element name="Values">
*                               &lt;complexType>
*                                 &lt;complexContent>
*                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
*                                     &lt;sequence>
*                                       &lt;element name="Value" maxOccurs="unbounded" minOccurs="0">
*                                         &lt;complexType>
*                                           &lt;simpleContent>
*                                             &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
*                                               &lt;attribute name="AttributeID" type="{http://www.w3.org/2001/XMLSchema}string" />
*                                               &lt;attribute name="UnitID" type="{http://www.w3.org/2001/XMLSchema}string" />
*                                               &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
*                                               &lt;attribute name="Derived" type="{http://www.w3.org/2001/XMLSchema}string" />
*                                             &lt;/extension>
*                                           &lt;/simpleContent>
*                                         &lt;/complexType>
*                                       &lt;/element>
*                                     &lt;/sequence>
*                                   &lt;/restriction>
*                                 &lt;/complexContent>
*                               &lt;/complexType>
*                             &lt;/element>
*                           &lt;/sequence>
*                           &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
*                           &lt;attribute name="UserTypeID" type="{http://www.w3.org/2001/XMLSchema}string" />
*                           &lt;attribute name="ParentID" type="{http://www.w3.org/2001/XMLSchema}string" />
*                         &lt;/restriction>
*                       &lt;/complexContent>
*                     &lt;/complexType>
*                   &lt;/element>
*                 &lt;/sequence>
*               &lt;/restriction>
*             &lt;/complexContent>
*           &lt;/complexType>
*         &lt;/element>
*       &lt;/sequence>
*       &lt;attribute name="ExportTime" type="{http://www.w3.org/2001/XMLSchema}string" />
*       &lt;attribute name="ExportContext" type="{http://www.w3.org/2001/XMLSchema}string" />
*       &lt;attribute name="ContextID" type="{http://www.w3.org/2001/XMLSchema}string" />
*       &lt;attribute name="WorkspaceID" type="{http://www.w3.org/2001/XMLSchema}string" />
*       &lt;attribute name="UseContextLocale" type="{http://www.w3.org/2001/XMLSchema}string" />
*     &lt;/restriction>
*   &lt;/complexContent>
* &lt;/complexType>
* </pre>
*
*
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "STEP-ProductInformation")
public class STEPProductInformation {

 @XmlElement(name = "Products")
 public STEPProductInformation.Products products;
 @XmlAttribute(name = "ExportTime")
 public String exportTime;
 @XmlAttribute(name = "ExportContext")
 public String exportContext;
 @XmlAttribute(name = "ContextID")
 public String contextID;
 @XmlAttribute(name = "WorkspaceID")
 public String workspaceID;
 @XmlAttribute(name = "UseContextLocale")
 public String useContextLocale;
 
 
    private static IntgSrvLogger LOGGER = IntgSrvLogger.getInstance("ImportXMLSetMapper", STEPProductInformation.class.getName());
    private static IntgSrvLogger ehfLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER);
    private static IntgSrvLogger ehfItemLoggerXSV = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_XSV);
    private static IntgSrvLogger ehfItemLoggerFixLength = IntgSrvLogger.getInstance(IntgSrvAppConstants.EHF_LOGGER_PRODUCTITEMS_FIXLENGTH);
    private static IntgSrvLogger traceLogger = IntgSrvLogger.getInstance(IntgSrvAppConstants.FREEFORM_TRACE_LOGGER);
    
	private static String clazzroot = "STEPProductInformation";
	private String clazzname = this.getClass().getName();
	 
 /**
  * Gets the value of the products property.
  *
  * @return
  *     possible object is
  *     {@link STEPProductInformation.Products }
  *
  */
 public STEPProductInformation.Products getProducts() {
     traceLogger.info(clazzroot, "0Root.getProducts", "ENTER/EXIT");
     return products;
 }

 /**
  * Sets the value of the products property.
  *
  * @param value
  *     allowed object is
  *     {@link STEPProductInformation.Products }
  *
  */
 public void setProducts(STEPProductInformation.Products value) {
     traceLogger.info(clazzroot, "0Root.setProducts", "ENTER/EXIT");
     this.products = value;
 }

 /**
  * Gets the value of the exportTime property.
  *
  * @return
  *     possible object is
  *     {@link String }
  *
  */
 public String getExportTime() {
     traceLogger.info(clazzroot, "0Root.getExportTime", "ENTER/EXIT: exportTime = " + exportTime);
     return exportTime;
 }

 /**
  * Sets the value of the exportTime property.
  *
  * @param value
  *     allowed object is
  *     {@link String }
  *
  */
 public void setExportTime(String value) {
     traceLogger.info(clazzroot, "0Root.setExportTime", "ENTER/EXIT: value = " + value);
     this.exportTime = value;
 }

 /**
  * Gets the value of the exportContext property.
  *
  * @return
  *     possible object is
  *     {@link String }
  *
  */
 public String getExportContext() {
     traceLogger.info(clazzroot, "0Root.getExportContext", "ENTER/EXIT: exportContext = " + exportContext);
     return exportContext;
 }

 /**
  * Sets the value of the exportContext property.
  *
  * @param value
  *     allowed object is
  *     {@link String }
  *
  */
 public void setExportContext(String value) {
     traceLogger.info(clazzroot, "0Root.setExportContext", "ENTER/EXIT: value = " + value);
     this.exportContext = value;
 }

 /**
  * Gets the value of the contextID property.
  *
  * @return
  *     possible object is
  *     {@link String }
  *
  */
 public String getContextID() {
     traceLogger.info(clazzroot, "0Root.getContextID", "ENTER/EXIT: contextID = " + contextID);
     return contextID;
 }

 /**
  * Sets the value of the contextID property.
  *
  * @param value
  *     allowed object is
  *     {@link String }
  *
  */
 public void setContextID(String value) {
     traceLogger.info(clazzroot, "0Root.setContextID", "ENTER/EXIT: value = " + value);
     this.contextID = value;
 }

 /**
  * Gets the value of the workspaceID property.
  *
  * @return
  *     possible object is
  *     {@link String }
  *
  */
 public String getWorkspaceID() {
     traceLogger.info(clazzroot, "0Root.getWorkspaceID", "ENTER/EXIT: workspaceID = " + workspaceID);
     return workspaceID;
 }

 /**
  * Sets the value of the workspaceID property.
  *
  * @param value
  *     allowed object is
  *     {@link String }
  *
  */
 public void setWorkspaceID(String value) {
     traceLogger.info(clazzroot, "0Root.setWorkspaceID", "ENTER/EXIT: value = " + value);
     this.workspaceID = value;
 }

 /**
  * Gets the value of the useContextLocale property.
  *
  * @return
  *     possible object is
  *     {@link String }
  *
  */
 public String getUseContextLocale() {
     traceLogger.info(clazzroot, "0Root.getUseContextLocale", "ENTER/EXIT: useContextLocale = " + useContextLocale);
     return useContextLocale;
 }

 /**
  * Sets the value of the useContextLocale property.
  *
  * @param value
  *     allowed object is
  *     {@link String }
  *
  */
 public void setUseContextLocale(String value) {
     traceLogger.info(clazzroot, "0Root.setUseContextLocale", "ENTER/EXIT value = " + value);
     this.useContextLocale = value;
 }


 /**
  * <p>Java class for anonymous complex type.
  *
  * <p>The following schema fragment specifies the expected content contained within this class.
  *
  * <pre>
  * &lt;complexType>
  *   &lt;complexContent>
  *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
  *       &lt;sequence>
  *         &lt;element name="Product" maxOccurs="unbounded" minOccurs="0">
  *           &lt;complexType>
  *             &lt;complexContent>
  *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
  *                 &lt;sequence>
  *                   &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *                   &lt;element name="ClassificationReference" maxOccurs="unbounded" minOccurs="0">
  *                     &lt;complexType>
  *                       &lt;simpleContent>
  *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
  *                           &lt;attribute name="ClassificationID" type="{http://www.w3.org/2001/XMLSchema}string" />
  *                           &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}string" />
  *                         &lt;/extension>
  *                       &lt;/simpleContent>
  *                     &lt;/complexType>
  *                   &lt;/element>
  *                   &lt;element name="AssetCrossReference">
  *                     &lt;complexType>
  *                       &lt;simpleContent>
  *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
  *                           &lt;attribute name="AssetID" type="{http://www.w3.org/2001/XMLSchema}string" />
  *                           &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}string" />
  *                         &lt;/extension>
  *                       &lt;/simpleContent>
  *                     &lt;/complexType>
  *                   &lt;/element>
  *                   &lt;element name="Values">
  *                     &lt;complexType>
  *                       &lt;complexContent>
  *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
  *                           &lt;sequence>
  *                             &lt;element name="Value" maxOccurs="unbounded" minOccurs="0">
  *                               &lt;complexType>
  *                                 &lt;simpleContent>
  *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
  *                                     &lt;attribute name="AttributeID" type="{http://www.w3.org/2001/XMLSchema}string" />
  *                                     &lt;attribute name="UnitID" type="{http://www.w3.org/2001/XMLSchema}string" />
  *                                     &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
  *                                     &lt;attribute name="Derived" type="{http://www.w3.org/2001/XMLSchema}string" />
  *                                   &lt;/extension>
  *                                 &lt;/simpleContent>
  *                               &lt;/complexType>
  *                             &lt;/element>
  *                           &lt;/sequence>
  *                         &lt;/restriction>
  *                       &lt;/complexContent>
  *                     &lt;/complexType>
  *                   &lt;/element>
  *                 &lt;/sequence>
  *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
  *                 &lt;attribute name="UserTypeID" type="{http://www.w3.org/2001/XMLSchema}string" />
  *                 &lt;attribute name="ParentID" type="{http://www.w3.org/2001/XMLSchema}string" />
  *               &lt;/restriction>
  *             &lt;/complexContent>
  *           &lt;/complexType>
  *         &lt;/element>
  *       &lt;/sequence>
  *     &lt;/restriction>
  *   &lt;/complexContent>
  * &lt;/complexType>
  * </pre>
  *
  *
  */
 @XmlAccessorType(XmlAccessType.FIELD)
 @XmlType(name = "", propOrder = {})
 public static class Products {

     @XmlElement(name = "Product")
     public List<STEPProductInformation.Products.Product> product;
     //public static HashMap AssetValueHashMap = new HashMap();
     
     /**
      * Gets the value of the product property.
      *
      * <p>
      * This accessor method returns a reference to the live list,
      * not a snapshot. Therefore any modification you make to the
      * returned list will be present inside the JAXB object.
      * This is why there is not a <CODE>set</CODE> method for the product property.
      *
      * <p>
      * For example, to add a new item, do as follows:
      * <pre>
      *    getProduct().add(newItem);
      * </pre>
      *
      *
      * <p>
      * Objects of the following type(s) are allowed in the list
      * {@link STEPProductInformation.Products.Product }
      *
      *
      */
     public List<STEPProductInformation.Products.Product> getProduct() {
         traceLogger.info(clazzroot, "1Products.getProduct", "ENTER");
    	 
         if (product == null) {
             product = new ArrayList<STEPProductInformation.Products.Product>();
         }
         traceLogger.info(clazzroot, "1Products.getProduct", "EXIT");
         return this.product;
     }


     /**
      * <p>Java class for anonymous complex type.
      *
      * <p>The following schema fragment specifies the expected content contained within this class.
      *
      * <pre>
      * &lt;complexType>
      *   &lt;complexContent>
      *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
      *       &lt;sequence>
      *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
      *         &lt;element name="ClassificationReference" maxOccurs="unbounded" minOccurs="0">
      *           &lt;complexType>
      *             &lt;simpleContent>
      *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
      *                 &lt;attribute name="ClassificationID" type="{http://www.w3.org/2001/XMLSchema}string" />
      *                 &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}string" />
      *               &lt;/extension>
      *             &lt;/simpleContent>
      *           &lt;/complexType>
      *         &lt;/element>
      *         &lt;element name="AssetCrossReference">
      *           &lt;complexType>
      *             &lt;simpleContent>
      *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
      *                 &lt;attribute name="AssetID" type="{http://www.w3.org/2001/XMLSchema}string" />
      *                 &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}string" />
      *               &lt;/extension>
      *             &lt;/simpleContent>
      *           &lt;/complexType>
      *         &lt;/element>
      *         &lt;element name="Values">
      *           &lt;complexType>
      *             &lt;complexContent>
      *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
      *                 &lt;sequence>
      *                   &lt;element name="Value" maxOccurs="unbounded" minOccurs="0">
      *                     &lt;complexType>
      *                       &lt;simpleContent>
      *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
      *                           &lt;attribute name="AttributeID" type="{http://www.w3.org/2001/XMLSchema}string" />
      *                           &lt;attribute name="UnitID" type="{http://www.w3.org/2001/XMLSchema}string" />
      *                           &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
      *                           &lt;attribute name="Derived" type="{http://www.w3.org/2001/XMLSchema}string" />
      *                         &lt;/extension>
      *                       &lt;/simpleContent>
      *                     &lt;/complexType>
      *                   &lt;/element>
      *                 &lt;/sequence>
      *               &lt;/restriction>
      *             &lt;/complexContent>
      *           &lt;/complexType>
      *         &lt;/element>
      *       &lt;/sequence>
      *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
      *       &lt;attribute name="UserTypeID" type="{http://www.w3.org/2001/XMLSchema}string" />
      *       &lt;attribute name="ParentID" type="{http://www.w3.org/2001/XMLSchema}string" />
      *     &lt;/restriction>
      *   &lt;/complexContent>
      * &lt;/complexType>
      * </pre>
      *
      *
      */
     @XmlRootElement(name = "Product")
     @XmlAccessorType(XmlAccessType.FIELD)
     @XmlType(name = "", propOrder = {})
     public static class Product{
         @XmlElement(name = "Name", required = true)
         protected String name;
         @XmlElement(name = "ClassificationReference")
         protected List<STEPProductInformation.Products.Product.ClassificationReference> classificationReference;
         @XmlElement(name = "AssetCrossReference", required = true)
         protected List<STEPProductInformation.Products.Product.AssetCrossReference> assetCrossReference;
         @XmlElement(name = "ProductCrossReference", required = false)
         protected STEPProductInformation.Products.Product.ProductCrossReference productCrossReference;
         @XmlElement(name = "Values", required = true)
         public STEPProductInformation.Products.Product.Values values;
         @XmlAttribute(name = "ID")
         public String id;
         @XmlAttribute(name = "UserTypeID")
         protected String userTypeID;
         @XmlAttribute(name = "ParentID")
         protected String parentID;
         
         String delimiter = "~|~";
         String headerLine = "ID" + delimiter + "Name" + delimiter;
         
         private ErrorHandlingFrameworkICD ehfICD;
         private ErrorHandlingFrameworkHandler ehfHandler = new ErrorHandlingFrameworkHandler();
         private String traceId;
         
         private void init() throws ErrorHandlingFrameworkException {
        	 ehfICD = ErrorHandlingFrameworkHandler.getErrorHandlingFrameworkICDFactory(
        			 IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ECO_SYSTEM, 
        			 IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_COMPONENT_ID, 
        			 IntgSrvErrorHandlingFrameworkConstants.SPRINGBATCH_ICD_CLASSNAME);
         }
         
         
         /**
          * Gets the value of the name property.
          *
          * @return
          *     possible object is
          *     {@link String }
          *
          */
         public String getName() {
        	 traceLogger.info(clazzroot, "2Product.getName", "ENTER/EXIT (Mapped): Product name = " + name);
             return name;
         }

         /**
          * Sets the value of the name property.
          *
          * @param value
          *     allowed object is
          *     {@link String }
          *
          */
         public void setName(String value) {
        	 traceLogger.info(clazzroot, "2Product.setName", "ENTER/EXIT: value = " + value);
             this.name = value;
         }

         /**
          * Gets the value of the classificationReference property.
          *
          * <p>
          * This accessor method returns a reference to the live list,
          * not a snapshot. Therefore any modification you make to the
          * returned list will be present inside the JAXB object.
          * This is why there is not a <CODE>set</CODE> method for the classificationReference property.
          *
          * <p>
          * For example, to add a new item, do as follows:
          * <pre>
          *    getClassificationReference().add(newItem);
          * </pre>
          *
          *
          * <p>
          * Objects of the following type(s) are allowed in the list
          * {@link STEPProductInformation.Products.Product.ClassificationReference }
          *
          *
          */
         public List<STEPProductInformation.Products.Product.ClassificationReference> getClassificationReference() {
        	 traceLogger.info(clazzroot, "2Product.getClassificationReference", "ENTER");
             if (classificationReference == null) {
                 classificationReference = new ArrayList<STEPProductInformation.Products.Product.ClassificationReference>();
             }
             
        	 traceLogger.info(clazzroot, "2Product.getClassificationReference", "this.classificationReference = " + this.classificationReference.toString());
        	 traceLogger.info(clazzroot, "2Product.getClassificationReference", "EXIT");
             return this.classificationReference;
         }

         /**
          * Gets the value of the assetCrossReference property.
          *
          * @return
          *     possible object is
          *     {@link STEPProductInformation.Products.Product.AssetCrossReference }
          *
          */
         public List<STEPProductInformation.Products.Product.AssetCrossReference> getAssetCrossReference() {
        	 traceLogger.info(clazzroot, "2Product.getAssetCrossReference", "ENTER/EXIT");
             if (assetCrossReference == null) {
            	 assetCrossReference = new ArrayList<STEPProductInformation.Products.Product.AssetCrossReference>();
             }
             
        	 traceLogger.info(clazzroot, "2Product.getAssetCrossReference", "this.assetCrossReference = " + this.assetCrossReference.toString());
        	 traceLogger.info(clazzroot, "2Product.getAssetCrossReference", "EXIT");
             return assetCrossReference;
         }

         /**
          * Gets the value of the assetCrossReference property.
          *
          * @return
          *     possible object is
          *     {@link STEPProductInformation.Products.Product.AssetCrossReference }
          *
          */
         public STEPProductInformation.Products.Product.ProductCrossReference getProductCrossReference() {
        	 traceLogger.info(clazzroot, "2Product.getProductCrossReference", "ENTER/EXIT");
        	 //PCMP-1499 Boomerang report generate with VendorItem details
        	 if(productCrossReference!=null && productCrossReference.getProduct() !=null &&  "ItemToVendor".equalsIgnoreCase(productCrossReference.getProduct().getUserTypeID())){
        		 return null;
        	 }
             return productCrossReference;
         }

         /**
          * Sets the value of the assetCrossReference property.
          *
          * @param value
          *     allowed object is
          *     {@link STEPProductInformation.Products.Product.AssetCrossReference }
          *
          */
         public void setAssetCrossReference(List<STEPProductInformation.Products.Product.AssetCrossReference> value) {
        	 traceLogger.info(clazzroot, "2Product.setAssetCrossReference", "ENTER/EXIT");
             this.assetCrossReference = value;
         }

         /**
          * Gets the value of the values property.
          *
          * @return
          *     possible object is
          *     {@link STEPProductInformation.Products.Product.Values }
          *
          */
         public STEPProductInformation.Products.Product.Values getValues() {
        	 traceLogger.info(clazzroot, "2Product.getValues()", "ENTER/EXIT: to call 3Value.getValue()");
             return values;
         }

         /**
          * Sets the value of the values property.
          *
          * @param value
          *     allowed object is
          *     {@link STEPProductInformation.Products.Product.Values }
          *
          */
         public void setValues(STEPProductInformation.Products.Product.Values value) {
        	 traceLogger.info(clazzroot, "2Product.setValues", "ENTER/EXIT");
             this.values = value;
         }

         /**
          * Gets the value of the id property.
          *
          * @return
          *     possible object is
          *     {@link String }
          *
          */
         public String getID() {
        	 traceLogger.info(clazzroot, "2Product.getID", "ENTER/EXIT (Mapped): Product id = " + id);
             return id;
         }

         /**
          * Sets the value of the id property.
          *
          * @param value
          *     allowed object is
          *     {@link String }
          *
          */
         public void setID(String value) {
        	 traceLogger.info(clazzroot, "2Product.setID", "ENTER/EXIT: value = " + value);
             this.id = value;
         }

         /**
          * Gets the value of the userTypeID property.
          *
          * @return
          *     possible object is
          *     {@link String }
          *
          */
         public String getUserTypeID() {
        	 traceLogger.info(clazzroot, "2Product.getUserTypeID", "ENTER/EXIT: userTypeID = " + userTypeID);
             return userTypeID;
         }

         /**
          * Sets the value of the userTypeID property.
          *
          * @param value
          *     allowed object is
          *     {@link String }
          *
          */
         public void setUserTypeID(String value) {
        	 traceLogger.info(clazzroot, "2Product.setUserTypeID", "ENTER/EXIT: value = " + value);
             this.userTypeID = value;
         }

         /**
          * Gets the value of the parentID property.
          *
          * @return
          *     possible object is
          *     {@link String }
          *
          */
         public String getParentID() {
        	 traceLogger.info(clazzroot, "2Product.getParentID", "return getXSVofValues() - ENTER/EXIT");
             //return getXSVofValues();   
        	 return null;
         }

         /**
          * Sets the value of the parentID property.
          *
          * @param value
          *     allowed object is
          *     {@link String }
          *
          */
         public void setParentID(String value) {
        	 traceLogger.info(clazzroot, "2Product.setParentID", "ENTER/EXIT: value = " + value);
             this.parentID = value;
         }
     	public String getDelimiter() {
        	traceLogger.info(clazzroot, "2Product.getDelimiter", "ENTER/EXIT: delimiter = " + delimiter);
			return delimiter;
		}

		public void setDelimiter(String delimiter) {
        	traceLogger.info(clazzroot, "2Product.setDelimiter()", "ENTER/EXIT: delimiter = " + delimiter);
			this.delimiter = delimiter;
		}

		public String getHeaderLine() {
        	traceLogger.info(clazzroot, "2.getHeaderLine", "ENTER/EXIT: headerLine = " + headerLine);
			return headerLine;
		}

		public void setHeaderLine(String headerLine) {
        	traceLogger.info(clazzroot, "2Product.setHeaderLine", "ENTER/EXIT: headerLine = " + headerLine);
			this.headerLine = headerLine;
		}			 
          
		
		public String getXSVofValues() {
			   
			FormatterToXSVFile transformerToXSVFormat = new FormatterToXSVFile(); 
			String strDelimiter = getDelimiter();
			String formattedXSVString = null; 
			List<STEPProductInformation.Products.Product.AssetCrossReference> assetCrossReferenceList = null;
			STEPProductInformation.Products.Product.ProductCrossReference productCrossReference = null;
			List<STEPProductInformation.Products.Product.ClassificationReference> classificationReference = null;
			List<STEPProductInformation.Products.Product.Values.Value> valueListProductCrossRef = null;
				
			if (null != getProductCrossReference())
			{
				if (null != getProductCrossReference().getProduct().getAssetCrossReference()) 
				{
					productCrossReference  = getProductCrossReference();	 			
					assetCrossReferenceList =  getProductCrossReference().getProduct().getAssetCrossReference(); 	
				}
				valueListProductCrossRef = getProductCrossReference().getProduct().getValues().getValue();
			}
			if (null != getClassificationReference())
			{
				classificationReference = getClassificationReference();	 
			}				 
			 
			List<STEPProductInformation.Products.Product.Values.Value> valueList = getValues().getValue();
		
				 
			formattedXSVString =  transformerToXSVFormat.buildXSVString(assetCrossReferenceList,  
						  												classificationReference,
						  												productCrossReference, 
						  												valueListProductCrossRef,
						  												valueList,
						  												strDelimiter);						 
				 
		return formattedXSVString; 			   
	} 
		 
		  @SuppressWarnings({ "unchecked", "rawtypes" })
		public String getFixLengthofValues() {
			   
				String publishIds = IntgSrvAppConstants.BRACKET_LEFT + IntgSrvAppConstants.STEE103 + IntgSrvAppConstants.COMMA
						+ IntgSrvAppConstants.STEE109 + IntgSrvAppConstants.COMMA + IntgSrvAppConstants.STEE110 + IntgSrvAppConstants.BRACKET_WRITE;
				
			 String formattedFixLengthString = null;
			 STEPProductInformation.Products.Product.ProductCrossReference productCrossReference = null;
			 List<STEPProductInformation.Products.Product.Values.Value> valueListProductCrossRef = null;
			 List<STEPProductInformation.Products.Product.Values.MultiValue> multiValueProdCrossRefAttribList = null;
			 List<STEPProductInformation.Products.Product.Values.Value> valueList = null;
			 List<STEPProductInformation.Products.Product.Values.MultiValue> multiValueAttribList = null;
			 
			try {
			  StepTransmitterBean transformer = null;				
			 
			  if (null != getProductCrossReference())
			  {
				  productCrossReference  = getProductCrossReference();
				  valueListProductCrossRef = getProductCrossReference().getProduct().getValues().getValue();
				  multiValueProdCrossRefAttribList =  getProductCrossReference().getProduct().getValues().getMultiValue();
			  }
			   
			  if (null != getValues()) 
			  {  
				  if (null != getValues().getValue())
				  {
					  valueList = getValues().getValue();
				  }
				  if (null!= getValues().getMultiValue())
				  {
					  multiValueAttribList = getValues().getMultiValue();
				  }
			  }
			    
			    
			  String productID = this.getID();
				
			  FormaterToFixLength transformerToFixlength = new FormaterToFixLength(); 
			  transformer =  transformerToFixlength.buildFixLengthString(productCrossReference,
					  													valueListProductCrossRef,
					  													multiValueProdCrossRefAttribList,
					  													valueList,
					  													multiValueAttribList,
					  													productID);
			  transformer.setPublishId(publishIds);
				
			  PublisherToMQ publishTransaction = new PublisherToMQ();	
			  List<List> listOfMessagesInTheListToPublish  = ((List<List>)transformer.getItem(IntgSrvAppConstants.LIST_OF_MESSAGES_TO_HASHMAP));
			  
			  for (int q = 0;  q < listOfMessagesInTheListToPublish.size(); q++ )
			 {
					List<String>listOfMessagesToPublish = listOfMessagesInTheListToPublish.get(q); 
				
					String msgFixLength= listOfMessagesToPublish.get(0);
					formattedFixLengthString = listOfMessagesToPublish.get(1);  
			  
					transformer.setMessage(msgFixLength);
					String mqHostName = IntgSrvPropertiesReader.getProperty(
						IntgSrvAppConstants.MQ_HOSTNAME); 
					transformer.setMqHostName(mqHostName); 
				
					int mqPort = Integer.parseInt(IntgSrvPropertiesReader.getProperty(
						IntgSrvAppConstants.MQ_PORT)); 
					transformer.setMqPport(mqPort); 
				
					String mqQueueManager = IntgSrvPropertiesReader.getProperty(
						IntgSrvAppConstants.MQ_QUEUE_MANAGER); 
					transformer.setMqQueueManager(mqQueueManager); 
				
					String mqChannel = IntgSrvPropertiesReader.getProperty(
						IntgSrvAppConstants.MQ_CHANNEL); 
					transformer.setMqChannel(mqChannel); 
				
					String mqQueueName = IntgSrvPropertiesReader.getProperty(
						IntgSrvAppConstants.MQ_QUEUE_NAME); 
					transformer.setMqQueueName(mqQueueName);
				
					int mqTimeout =  Integer.parseInt(IntgSrvPropertiesReader.getProperty(
						IntgSrvAppConstants.MQ_TIMEOUT)); 
					transformer.setMqTimeout(mqTimeout); 
			  
					publishTransaction.publishToMq(transformer);	
			 }
			  
			} catch (Throwable e) {
				String usecase = Thread.currentThread().getStackTrace()[1].getMethodName();
				String codeModule = getClass().getName();
				String errorLogString = ehfHandler.getErrorLog(new Date(), traceId, 
						IntgSrvErrorHandlingFrameworkConstants.EHF_ERROR_PATH3, e, 
						IntgSrvErrorHandlingFrameworkConstants.EHF_ELEM_SPRINGBATCH_USER, 
						usecase, codeModule, ehfICD); 
    	 		ehfLogger.error(errorLogString);
    	 		ehfItemLoggerFixLength.error(errorLogString); 
    	 		traceLogger.error(clazzroot, "2Product.getFixLengthofValues", ehfICD.toStringEHFExceptionStackTrace(e));
        		e.printStackTrace();
        	}
			  
			  
			  return formattedFixLengthString;
		   }
		  
		  

			public void setLocationLevel(String sSKUID, String sSTEP_Item_ID,String channel) {
				IntgSrvUtils.printConsole("Enter setLocationLevel()");
				IntgSrvUtils.printConsole("channel="+channel);
				HashMap<String,String> hmFANID_NAME = new HashMap();
				hmFANID_NAME.put("L_A0007","Distribution Center aka List of Warehouses");
				hmFANID_NAME.put("L_A0013","Vendor Model Number");
				hmFANID_NAME.put("L_A0023","Direct Store Delivery (DSD Flag)");
				hmFANID_NAME.put("L_A0028","Retail IA (Rebuyer) Number");
				hmFANID_NAME.put("L_A0029","Retail Product Manager Number");
				hmFANID_NAME.put("L_A0031","Inventory Group");
				hmFANID_NAME.put("L_A0045","Pallet Ti");
				hmFANID_NAME.put("L_A0046","Pallet Hi");
				hmFANID_NAME.put("L_A0065","WHSP");
				hmFANID_NAME.put("L_A0066","Store Tmax (Min Pres)");
				hmFANID_NAME.put("L_A0067","Selling Units per Master Case Pack (SUCP)");
				hmFANID_NAME.put("L_A0068","Master Case Pack Length");
				hmFANID_NAME.put("L_A0069","Master Case Pack Width");
				hmFANID_NAME.put("L_A0070","Master Case Pack Height");
				hmFANID_NAME.put("L_A0071","Master Case Pack Weight");
				hmFANID_NAME.put("L_A0072","STA aka Merchandising Status @ Location Level");
				hmFANID_NAME.put("L_A0073","SRC");
				hmFANID_NAME.put("L_A0074","DC Purchase Flag");
				hmFANID_NAME.put("L_A0075","Vendor Number");
				hmFANID_NAME.put("L_A0077","List Price");
				hmFANID_NAME.put("L_A0237","Sell Unit Length");
				hmFANID_NAME.put("L_A0238","Sell Unit Width");
				hmFANID_NAME.put("L_A0239","Sell Unit Height");
				hmFANID_NAME.put("L_A0240","Sell Unit Weight");
				hmFANID_NAME.put("L_A0241","Purchase Case Weight");
				hmFANID_NAME.put("L_A0372","DC Hold Flag");
				hmFANID_NAME.put("L_A0373","DC Flow");
				hmFANID_NAME.put("L_A0374","Store - Source Warehouse (DC)");
				hmFANID_NAME.put("L_A0375","Store Locations Activate");
				hmFANID_NAME.put("L_A0376","Store Locations Inactivate");
				hmFANID_NAME.put("L_A0389","Add/Remove SWO SKU");
				hmFANID_NAME.put("L_A0390","Purchase Case Cube");
				hmFANID_NAME.put("L_A0391","Discontinued by Manufacturer (DBM) / Staples (DBS)");
				hmFANID_NAME.put("L_A0421","Specific Store");
				hmFANID_NAME.put("L_A0432","Drop Ship @ FC Level");
				hmFANID_NAME.put("L_A0433","FC Purchase Flag");
				hmFANID_NAME.put("L_A0526","Store Code (List)");
				hmFANID_NAME.put("L_A0527","Fulfillment  Center Code (List)");
				hmFANID_NAME.put("L_A0528","Distribution Center Code (List)");
				hmFANID_NAME.put("L_A0497","End Cost");
				hmFANID_NAME.put("L_A0498","Vendor Load");
				hmFANID_NAME.put("L_A0054","Add on allowed");
				hmFANID_NAME.put("L_A0055","Distribution Pack");
				hmFANID_NAME.put("L_A0056","Distribution Policy Code");
				hmFANID_NAME.put("L_A0044","Velocity Code");
				hmFANID_NAME.put("L_A0057","Seasonal Code");
				hmFANID_NAME.put("L_A0058","Target  days");
				hmFANID_NAME.put("L_A0059","DC Min balance");
				hmFANID_NAME.put("L_A0060","Order PO Code");
				hmFANID_NAME.put("L_A0061","Order Base Qty");
				hmFANID_NAME.put("L_A0062","Order Incr Qty");
				hmFANID_NAME.put("L_A0063","Cases/Pallet");
				hmFANID_NAME.put("L_A0078","PO Cost");
				hmFANID_NAME.put("L_A0047","Foreign Cost/Unit Freight");
				hmFANID_NAME.put("L_A0048","Duty %");
				hmFANID_NAME.put("L_A0064","Other Cost");
				hmFANID_NAME.put("L_A0049","Other %");
				hmFANID_NAME.put("L_A0066","Store Tmax");
				hmFANID_NAME.put("L_A0043","Sell UoM");
				//StepLocationLevelAttrib stepLocationLevelAttrib = new StepLocationLevelAttrib();

				// process ClassificationReference for Location Level data
	    		//if (getClassificationReference() != null){
	    			//List<STEPProductInformation.Products.Product.ClassificationReference> classificationList =  getClassificationReference();

				if(getProductCrossReference()!=null&&getProductCrossReference().getProduct()!=null&&getProductCrossReference().getProduct().getClassificationReference()!=null){
	    			List<STEPProductInformation.Products.Product.ClassificationReference> classificationList =  getProductCrossReference().getProduct().getClassificationReference();
	    			Iterator<STEPProductInformation.Products.Product.ClassificationReference> it = classificationList.iterator();
	    			HashMap<String,HashMap> hmLocationLevel = new HashMap();
	    			
	    			while(it.hasNext()){
	        			STEPProductInformation.Products.Product.ClassificationReference classificationCrossRef = (STEPProductInformation.Products.Product.ClassificationReference)it.next();
	        			String classificationID = classificationCrossRef.getClassificationID();
	        			String classificationType = classificationCrossRef.getType();
	        			IntgSrvUtils.printConsole("classificationID="+classificationID);
	        			IntgSrvUtils.printConsole("classificationType="+classificationType);

	        			if (classificationID !=null && !classificationID.equals("")
	        					&& (classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_DC_ITEM_LINK)
        							|| classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_FC_ITEM_LINK)
        							|| classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_STORE_ITEM_LINK)
        							|| classificationType.equalsIgnoreCase(IntgSrvAppConstants.LOCATION_LEVEL_ALL_LINK)	        							
	        					) ){
	        				
	        				try {
	        					
	        					//List<STEPProductInformation.Products.Product.Values.Value> listOfMetaDataValue = classificationCrossRef.getMetaData().getValue();
	        		    		List<STEPProductInformation.Products.Product.Values.Value> valueList = classificationCrossRef.getMetaData().getValue();
	        	    			
	        		    		Iterator<STEPProductInformation.Products.Product.Values.Value> itValue = valueList.iterator();
	        		    		
	        		    		int i = 0;
	    	        			HashMap valuesHashMap = new HashMap();

	        		    		while(itValue.hasNext())
	        		    		{
	        						STEPProductInformation.Products.Product.Values.Value valueAttr = (STEPProductInformation.Products.Product.Values.Value)itValue.next();
	        						 
	        						String iD = valueAttr.getAttributeID();
	        						
	        						//System.out.println("ClassificationReference.MetaData: iD="+iD);
	        						traceLogger.debug(clazzroot, "2Product.setLocationLevel", "ClassificationReference.MetaData: iD="+iD);
	        						
	        						if (valueAttr.getValue() ==null || valueAttr.getValue() == ""){
	        							//System.out.println("ClassificationReference.MetaData: value=[EMPTY]"); 
	        						}
	        						else {
	        							//System.out.println("ClassificationReference.MetaData: value="+valueAttr.getValue());
	        						}
	        						//store all value to HashMap
	        						valuesHashMap.put(iD, valueAttr.getValue());
	        		    		}
	        		    		Set<String> keySet = valuesHashMap.keySet();
	        		            Iterator itKey = keySet.iterator();  

	        		            while (itKey.hasNext()) {  
	        		            	String attrID = (String) itKey.next();
	        		            	String attrValue = (String) valuesHashMap.get(attrID);
	        		            	if (attrID.endsWith("_CI") && attrValue.equalsIgnoreCase("True")){
		        		            	System.out.println("Attribute ID: " + attrID);  
		        		            	System.out.println("Attribute value: " + attrValue);
		        		            	String locationAttrID = attrID.replace("_CI", "");
		        		            	IntgSrvUtils.printConsole("locationAttrID: " + locationAttrID);
		        		            	String locationAttrValue = (String) valuesHashMap.get(locationAttrID);
		        		            	IntgSrvUtils.printConsole("locationAttrValue: " + locationAttrValue);
		        		            	IntgSrvLocationLevelUtils.addLocationToHM(hmLocationLevel,locationAttrID,locationAttrValue,classificationType,classificationID);
		        		            	
		        		            	//for LocationDataAudit
		        		            	LocationDataAudit locationDataAudit = new LocationDataAudit();
		        		            	locationDataAudit.setSKU_ID(Integer.parseInt(sSKUID));
		        		            	locationDataAudit.setSTEP_Item_ID(sSTEP_Item_ID);
		        		            	locationDataAudit.setLocation_ID(classificationID);
		        		            	locationDataAudit.setLocation_Type(classificationType.replace("ItemLink", ""));
		        		            	//locationDataAudit.setLocation_Attribute_FAN(locationAttrID);
		        		            	locationDataAudit.setAttribute_Value(locationAttrValue);
										// set channel and FAN
		        		            	locationDataAudit.setChannel(channel);
		        		            	locationDataAudit.setLocation_Attribute_FAN(locationAttrID);
										if (locationAttrID.endsWith("_NAD")){
											//locationDataAudit.setChannel("NAD");
											//locationDataAudit.setLocation_Attribute_FAN(locationAttrID.replace("_NAD", ""));
											locationDataAudit.setAttribute_Name(hmFANID_NAME.get(locationAttrID.replace("_NAD", "")));
										}
										else if (locationAttrID.endsWith("_RET")){
											//locationDataAudit.setChannel("RET");
											//locationDataAudit.setLocation_Attribute_FAN(locationAttrID.replace("_RET", ""));
											locationDataAudit.setAttribute_Name(hmFANID_NAME.get(locationAttrID.replace("_RET", "")));
										}
										else if (locationAttrID.endsWith("_COR")){
											//locationDataAudit.setChannel("COR");
											//locationDataAudit.setLocation_Attribute_FAN(locationAttrID.replace("_COR", ""));
											locationDataAudit.setAttribute_Name(hmFANID_NAME.get(locationAttrID.replace("_COR", "")));
										}
										else {
											//locationDataAudit.setLocation_Attribute_FAN(locationAttrID);
											locationDataAudit.setAttribute_Name(hmFANID_NAME.get(locationAttrID));
										}
										RunSchedulerItemCreateUpdate.listOfLocationDataAudit.add(locationDataAudit);
										RunSchedulerItemCreateUpdate.SKUID_ItemIDHashMap.put(sSKUID, sSTEP_Item_ID);
	        		            	}
	        		            }  

	        		    		
	        				}
	                        catch (Exception ex) {  
	                        	System.out.println("Exception="+ex);
	                        }  
	        			}
	        		}
	    			HashMap<String,StepLocationLevelAttrib> hmVO = IntgSrvLocationLevelUtils.createStepLocationLevelAttrib(Integer.parseInt(sSKUID),channel,hmLocationLevel);
					Set<String> keySet1 = hmVO.keySet();
					Iterator itKey1 = keySet1.iterator();  
					
					while (itKey1.hasNext()) {  
						String key1 = (String) itKey1.next();
						IntgSrvUtils.printConsole("key1="+key1);
						RunSchedulerItemCreateUpdate.listOfStepLocationLevelAttrib.add(hmVO.get(key1));
					}
	    		}
	    		else {
	    			IntgSrvUtils.printConsole("getClassificationReference() == null");
	    		}
				
				
				IntgSrvUtils.printConsole("Exit setLocationLevel()");
			}
		
		
        @SuppressWarnings("rawtypes")
		public void setAllDBValues() {
        	 
        	if (!this.Is_DB_Values_Set){
        		        		
        	HashMap<String, String> allValuesHashMap = new DBAttributes().getAttrHashMap();
        	
        	// start merged with sprint_A3
    		// process ClassificationReference
    		if (getClassificationReference() != null){
    			List<STEPProductInformation.Products.Product.ClassificationReference> classificationList =  getClassificationReference();
    			Iterator<STEPProductInformation.Products.Product.ClassificationReference> it = classificationList.iterator();
        		while(it.hasNext()){
        			STEPProductInformation.Products.Product.ClassificationReference classificationCrossRef = (STEPProductInformation.Products.Product.ClassificationReference)it.next();
        			String classificationID = classificationCrossRef.getClassificationID();
        			IntgSrvUtils.printConsole("classificationID="+classificationID);

        			if (classificationID !=null && !classificationID.equals("") && classificationCrossRef.getType().equalsIgnoreCase("WebsiteLink")){
        				
        				try {
        					if (RunSchedulerItemCreateUpdate.classificationHashMap == null){
        						IntgSrvUtils.printConsole("RunScheduler.classificationHashMap is NULL");
        					}
        					if (RunSchedulerItemCreateUpdate.classificationHashMap != null && RunSchedulerItemCreateUpdate.classificationHashMap.get(classificationID) == null){
        						IntgSrvUtils.printConsole("RunScheduler.classificationHashMap="+RunSchedulerItemCreateUpdate.classificationHashMap);
        						IntgSrvUtils.printConsole("RunScheduler.classificationHashMap.get(classificationID) is NULL");
        					}
        					if (RunSchedulerItemCreateUpdate.classificationHashMap != null && RunSchedulerItemCreateUpdate.classificationHashMap.get(classificationID) != null){
        						allValuesHashMap.put(IntgSrvAppConstants.WEB_SUPER_CATEGORY, 
                						(String)((HashMap)RunSchedulerItemCreateUpdate.classificationHashMap.get(classificationID)).get(IntgSrvAppConstants.SUPER_CATEGORY));
                				allValuesHashMap.put(IntgSrvAppConstants.WEB_CATEGORY, 
                						(String)((HashMap)RunSchedulerItemCreateUpdate.classificationHashMap.get(classificationID)).get(IntgSrvAppConstants.CATEGORY));
                				allValuesHashMap.put(IntgSrvAppConstants.WEB_DEPARTMENT, 
                						(String)((HashMap)RunSchedulerItemCreateUpdate.classificationHashMap.get(classificationID)).get(IntgSrvAppConstants.DEPARTMENT));
                				allValuesHashMap.put(IntgSrvAppConstants.WEB_CLASS, 
                						(String)((HashMap)RunSchedulerItemCreateUpdate.classificationHashMap.get(classificationID)).get(IntgSrvAppConstants.CLASS));
                				
        					}
        					allValuesHashMap.put(IntgSrvAppConstants.WEB_CLASS_ID, 
            						classificationID.replace(IntgSrvAppConstants.STAPLES_DOT_COM_CLASS, ""));        					
        				}
                        catch (Exception ex) {  
                        	System.out.println("Exception="+ex);
                        }  
        			}
        		}
    		}
			// end merged with sprint_A3
        	
    		if (getProductCrossReference() != null){
    			IntgSrvUtils.printConsole("ProductCrossReference is not null");
    			
    			List<STEPProductInformation.Products.Product.Values.Value> valueList = getProductCrossReference().getProduct().getValues().getValue();
    			    			
    			Iterator<STEPProductInformation.Products.Product.Values.Value> it = valueList.iterator();
    			int j = 0;
    			while(it.hasNext())
    			{
    				STEPProductInformation.Products.Product.Values.Value valueAttr = (STEPProductInformation.Products.Product.Values.Value)it.next();
    				 
    				String iD = valueAttr.getAttributeID();  
    				//PCMP-2047 As IARebuyer is changed to LOV in STEP
    				if(iD.equalsIgnoreCase("A0028") || iD.equalsIgnoreCase("A0304")){
    					String value = valueAttr.getValue();
    					if(value.contains(":")) {
    						valueAttr.setValue(value.substring(0, value.indexOf(":")).trim());
    					}
    				}
    				traceLogger.debug(clazzroot, "2Product.setAllDBValues", "ProductCrossReference: iD="+iD);
    				
    				if (valueAttr.getValue() ==null || valueAttr.getValue() == ""){
    					System.out.println("ProductCrossReference: value=[EMPTY]"); 
    				}
    				else {
    					allValuesHashMap.put(iD, valueAttr.getValue());    					
    				}
    			}
    		}
    		
    		List<STEPProductInformation.Products.Product.Values.Value> valueList = getValues().getValue();
    			
    		Iterator<STEPProductInformation.Products.Product.Values.Value> it = valueList.iterator();
    		
    		int i = 0;
    		while(it.hasNext())
    		{
				STEPProductInformation.Products.Product.Values.Value valueAttr = (STEPProductInformation.Products.Product.Values.Value)it.next();
				 
				String iD = valueAttr.getAttributeID();
				//PCMP-2047 As IARebuyer is changed to LOV in STEP
				if(iD.equalsIgnoreCase("A0028") || iD.equalsIgnoreCase("A0304")){
					String value = valueAttr.getValue();
					if(value.contains(":")) {
						valueAttr.setValue(value.substring(0, value.indexOf(":")).trim());
					}
				}
				IntgSrvUtils.printConsole("Product: iD="+iD);
				traceLogger.debug(clazzroot, "2Product.setAllDBValues", "Product: iD="+iD);
				
				if (valueAttr.getValue() ==null || valueAttr.getValue() == ""){
					IntgSrvUtils.printConsole("Product: value=[EMPTY]"); 
				}
				else {
					allValuesHashMap.put(iD, valueAttr.getValue());
					IntgSrvUtils.printConsole("Product: iD="+iD+"; set value="+valueAttr.getValue());
					if (iD.equals(IntgSrvAppConstants.SKULIFECYCLE))
					{
						if (valueAttr.getValue().equalsIgnoreCase(IntgSrvAppConstants.NEW)
								|| valueAttr.getValue().equalsIgnoreCase(IntgSrvAppConstants.ACTIVATED))
						{
							allValuesHashMap.put(IntgSrvAppConstants.A0003, IntgSrvAppConstants.CREATE_NEW_ITEM); 
							allValuesHashMap.put(IntgSrvAppConstants.REQUEST_TYPE, IntgSrvAppConstants.CREATE_NEW_ITEM);						 
						}
						else if (valueAttr.getValue().equalsIgnoreCase(IntgSrvAppConstants.MAINTENANCE))
						{
							allValuesHashMap.put(IntgSrvAppConstants.A0003, IntgSrvAppConstants.UPDATE_ITEM);
							allValuesHashMap.put(IntgSrvAppConstants.REQUEST_TYPE, IntgSrvAppConstants.UPDATE_ITEM);						 
						}
						else if (valueAttr.getValue().equalsIgnoreCase(IntgSrvAppConstants.RECLASS))
						{
							allValuesHashMap.put(IntgSrvAppConstants.A0003, IntgSrvAppConstants.RECLASS);
							allValuesHashMap.put(IntgSrvAppConstants.REQUEST_TYPE, IntgSrvAppConstants.RECLASS);						 
						}
						else if (valueAttr.getValue().equalsIgnoreCase(IntgSrvAppConstants.FUTURE_DATED_PO))
						{
							allValuesHashMap.put(IntgSrvAppConstants.A0003, IntgSrvAppConstants.FUTURE_DATED_PO);
							allValuesHashMap.put(IntgSrvAppConstants.REQUEST_TYPE, IntgSrvAppConstants.FUTURE_DATED_PO);						 
						} 
					} 
				}
    		}
         
    		if ((String)allValuesHashMap.get(IntgSrvAppConstants.A0012)==""){
	    		if(getID().startsWith(VENDORITEM_PRODUCT_ID_PREFIX_NON_STOCKED)){
					String[] strArr=getID().split("-");
					allValuesHashMap.put(IntgSrvAppConstants.A0012, strArr[2]);
					IntgSrvUtils.printConsole("Product: iD=A0012; set value="+strArr[2]);
				}else if(getID().startsWith("Item-")){
					allValuesHashMap.put(IntgSrvAppConstants.A0012, getID().substring(5));
					IntgSrvUtils.printConsole("Product: iD=A0012; set value="+getID().substring(5));
				}else{
					allValuesHashMap.put(IntgSrvAppConstants.A0012,"0");
				} 
    		}
    		if ((String)allValuesHashMap.get("Product_Specialist")==""){
    			allValuesHashMap.put("Product_Specialist", allValuesHashMap.get("Product Specialist"));
    			IntgSrvUtils.printConsole("Product: iD=Product_Specialist; set value="+allValuesHashMap.get("Product Specialist"));
    		}
    		if ((String)allValuesHashMap.get("Boomerang_Trigger")==""){
    			allValuesHashMap.put("Boomerang_Trigger", allValuesHashMap.get("Boomerang Trigger"));
    			IntgSrvUtils.printConsole("Product: iD=Boomerang_Trigger; set value="+allValuesHashMap.get("Boomerang Trigger"));
    		}
    		setLocationLevel((String)allValuesHashMap.get(IntgSrvAppConstants.A0012), getProductCrossReference().getProduct().getID(),(String)allValuesHashMap.get(IntgSrvAppConstants.A0410));
          }
        	this.Is_DB_Values_Set = true;        	
        }
        
		private boolean Is_DB_Values_Set = false;
        
        public int getA0012() {
        	if (!Is_DB_Values_Set){
        		setAllDBValues();
        		IntgSrvUtils.printConsole("Boomerang_trigger in DB::"+DBAttributes.getAttrValue("Boomerang_Trigger"));
        	}
        	//return A0012;
        	return Integer.parseInt(DBAttributes.getAttrValue("A0012"));
		}
        
     // start merged with sprint_A3
		public String getWeb_Super_Category() {
			return DBAttributes.getAttrValue("Web_Super_Category");
		}
		public String getWeb_Category() {
			return DBAttributes.getAttrValue("Web_Category");
		}
		public String getWeb_Department() {
			return DBAttributes.getAttrValue("Web_Department");
		}
		public String getWeb_Class() {
			return DBAttributes.getAttrValue("Web_Class");
		}
		public String getWeb_Class_ID() {
			return DBAttributes.getAttrValue("Web_Class_ID");
		}
		public String getSTEP_Item_ID() {
			
			if (getProductCrossReference() != null){
				IntgSrvUtils.printConsole("STEP_Item_ID="+getProductCrossReference().productID);
				return getProductCrossReference().productID;
			}
			else 
			{
				IntgSrvUtils.printConsole("STEP_Item_ID="+getID());
				return getID();
				
			}
		}
		
		public String getProduct_Specialist() { 
			IntgSrvUtils.printConsole("Product_Specialist="+DBAttributes.getAttrValue("Product_Specialist"));
			return DBAttributes.getAttrValue("Product_Specialist");
		}
        
		// end merged with sprint_A3

		public String getA0003() {
			return DBAttributes.getAttrValue("A0003");
		}

		public String getRequest_Type() {
			return DBAttributes.getAttrValue("Request_Type");
		}

        
		
		public String getD0247() {
			return DBAttributes.getAttrValue("D0247");
		}


		public String getD0246() {
			return DBAttributes.getAttrValue("D0246");
		}


		public String getD0249() {
			return DBAttributes.getAttrValue("D0249");
		}


		public String getD0248() {
			return DBAttributes.getAttrValue("D0248");
		}


		public String getD0243() {
			return DBAttributes.getAttrValue("D0243");
		}


		public String getD0242() {
			return DBAttributes.getAttrValue("D0242");
		}


		public String getD0245() {
			return DBAttributes.getAttrValue("D0245");
		}


		public String getD0244() {
			return DBAttributes.getAttrValue("D0244");
		}


		public String getD0241() {
			return DBAttributes.getAttrValue("D0241");
		}


		public String getD0240() {
			return DBAttributes.getAttrValue("D0240");
		}


		public String getD0259() {
			return DBAttributes.getAttrValue("D0259");
		}


		public String getD0258() {
			return DBAttributes.getAttrValue("D0258");
		}


		public String getD0257() {
			return DBAttributes.getAttrValue("D0257");
		}


		public String getD0256() {
			return DBAttributes.getAttrValue("D0256");
		}


		public String getD0255() {
			return DBAttributes.getAttrValue("D0255");
		}


		public String getD0254() {
			return DBAttributes.getAttrValue("D0254");
		}


		public String getD0253() {
			return DBAttributes.getAttrValue("D0253");
		}


		public String getD0252() {
			return DBAttributes.getAttrValue("D0252");
		}


		public String getD0251() {
			return DBAttributes.getAttrValue("D0251");
		}


		public String getD0250() {
			return DBAttributes.getAttrValue("D0250");
		}


		public int getA0045() {
			String sValue = DBAttributes.getAttrValue("A0045");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}
		public int getA0045_RET() {
			String sValue = DBAttributes.getAttrValue("A0045_RET");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}
		public int getA0045_NAD() {
			String sValue = DBAttributes.getAttrValue("A0045_NAD");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}

		public int getA0046() {
			String sValue = DBAttributes.getAttrValue("A0046");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}

		public int getA0046_RET() {
			String sValue = DBAttributes.getAttrValue("A0046_RET");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}

		public int getA0046_NAD() {
			String sValue = DBAttributes.getAttrValue("A0046_NAD");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}


		public String getA0043() {
			return DBAttributes.getAttrValue("A0043");
		}


		public double getA0195() {
			String sValue = DBAttributes.getAttrValue("A0195");
			if (!sValue.equals("")){
				////System.out.println("double A0195 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getA0194() {
			return DBAttributes.getAttrValue("A0194");
		}


		public String getD0225() {
			return DBAttributes.getAttrValue("D0225");
		}


		public String getD0224() {
			return DBAttributes.getAttrValue("D0224");
		}


		public String getD0227() {
			return DBAttributes.getAttrValue("D0227");
		}


		public String getD0226() {
			return DBAttributes.getAttrValue("D0226");
		}


		public String getD0229() {
			return DBAttributes.getAttrValue("D0229");
		}


		public String getD0228() {
			return DBAttributes.getAttrValue("D0228");
		}


		public String getD0221() {
			return DBAttributes.getAttrValue("D0221");
		}


		public String getD0220() {
			return DBAttributes.getAttrValue("D0220");
		}


		public String getD0223() {
			return DBAttributes.getAttrValue("D0223");
		}


		public double getA0051() {
			String sValue = DBAttributes.getAttrValue("A0051");
			if (!sValue.equals("")){
				////System.out.println("double A0051 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0222() {
			return DBAttributes.getAttrValue("D0222");
		}


		public String getD0238() {
			return DBAttributes.getAttrValue("D0238");
		}


		public String getD0237() {
			return DBAttributes.getAttrValue("D0237");
		}


		public String getD0236() {
			return DBAttributes.getAttrValue("D0236");
		}


		public String getD0235() {
			return DBAttributes.getAttrValue("D0235");
		}


		public String getA0197() {
			return DBAttributes.getAttrValue("A0197");
		}


		public String getD0239() {
			return DBAttributes.getAttrValue("D0239");
		}


		public String getD0230() {
			return DBAttributes.getAttrValue("D0230");
		}


		public String getD0234() {
			return DBAttributes.getAttrValue("D0234");
		}


		public String getD0233() {
			return DBAttributes.getAttrValue("D0233");
		}


		public String getD0232() {
			return DBAttributes.getAttrValue("D0232");
		}


		public String getD0231() {
			return DBAttributes.getAttrValue("D0231");
		}


		public String getA0174() {
			return DBAttributes.getAttrValue("A0174");
		}


		public String getA0173() {
			return DBAttributes.getAttrValue("A0173");
		}


		public String getA0172() {
			return DBAttributes.getAttrValue("A0172");
		}


		public String getA0171() {
			return DBAttributes.getAttrValue("A0171");
		}


		public double getA0069() {
			String sValue = DBAttributes.getAttrValue("A0069");
			if (!sValue.equals("")){
				////System.out.println("double A0069 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public double getA0068() {
			String sValue = DBAttributes.getAttrValue("A0068");
			if (!sValue.equals("")){
				//System.out.println("double A0068 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public int getA0067() {
			String sValue = DBAttributes.getAttrValue("A0067");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}
		public int getA0067_RET() {
			String sValue = DBAttributes.getAttrValue("A0067_RET");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}
		public int getA0067_NAD() {
			String sValue = DBAttributes.getAttrValue("A0067_NAD");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}


		public String getD0194() {
			return DBAttributes.getAttrValue("D0194");
		}


		public String getD0195() {
			return DBAttributes.getAttrValue("D0195");
		}


		public int getA0075() {
			String sValue = DBAttributes.getAttrValue("A0075");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}

		public int getA0075_RET() {
			String sValue = DBAttributes.getAttrValue("A0075_RET");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}

		public int getA0075_NAD() {
			String sValue = DBAttributes.getAttrValue("A0075_NAD");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}

		public String getD0196() {
			return DBAttributes.getAttrValue("D0196");
		}


		public String getD0197() {
			return DBAttributes.getAttrValue("D0197");
		}


		public double getA0070() {
			String sValue = DBAttributes.getAttrValue("A0070");
			if (!sValue.equals("")){
				//System.out.println("double A0070 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0198() {
			return DBAttributes.getAttrValue("D0198");
		}


		public double getA0071() {
			String sValue = DBAttributes.getAttrValue("A0071");
			if (!sValue.equals("")){
				//System.out.println("double A0071 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0199() {
			return DBAttributes.getAttrValue("D0199");
		}


		public String getD0099() {
			return DBAttributes.getAttrValue("D0099");
		}


		public String getA0166() {
			return DBAttributes.getAttrValue("A0166");
		}


		public String getA0167() {
			return DBAttributes.getAttrValue("A0167");
		}


		public String getA0164() {
			return DBAttributes.getAttrValue("A0164");
		}


		public String getA0165() {
			return DBAttributes.getAttrValue("A0165");
		}


		public String getD0095() {
			return DBAttributes.getAttrValue("D0095");
		}


		public String getD0096() {
			return DBAttributes.getAttrValue("D0096");
		}


		public String getD0097() {
			return DBAttributes.getAttrValue("D0097");
		}


		public String getA0168() {
			return DBAttributes.getAttrValue("A0168");
		}


		public String getD0098() {
			return DBAttributes.getAttrValue("D0098");
		}


		public String getA0169() {
			return DBAttributes.getAttrValue("A0169");
		}


		public String getD0094() {
			return DBAttributes.getAttrValue("D0094");
		}


		public String getD0093() {
			return DBAttributes.getAttrValue("D0093");
		}


		public String getD0092() {
			return DBAttributes.getAttrValue("D0092");
		}


		public String getD0091() {
			return DBAttributes.getAttrValue("D0091");
		}


		public String getD0090() {
			return DBAttributes.getAttrValue("D0090");
		}


		public String getD0193() {
			return DBAttributes.getAttrValue("D0193");
		}


		public double getA0077() {
			String sValue = DBAttributes.getAttrValue("A0077");
			if (!sValue.equals("")){
				//System.out.println("double A0077 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}

		public double getA0077_RET() {
			String sValue = DBAttributes.getAttrValue("A0077_RET");
			if (!sValue.equals("")){
				//System.out.println("double A0077 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}

		public double getA0077_NAD() {
			String sValue = DBAttributes.getAttrValue("A0077_NAD");
			if (!sValue.equals("")){
				//System.out.println("double A0077 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getA0076() {
			return DBAttributes.getAttrValue("A0076");
		}


		public String getD0192() {
			return DBAttributes.getAttrValue("D0192");
		}


		public String getD0191() {
			return DBAttributes.getAttrValue("D0191");
		}


		public double getA0078() {
			String sValue = DBAttributes.getAttrValue("A0078");
			if (!sValue.equals("")){
				//System.out.println("double A0078 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}

		public double getA0078_RET() {
			String sValue = DBAttributes.getAttrValue("A0078_RET");
			if (!sValue.equals("")){
				//System.out.println("double A0078 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}

		public double getA0078_NAD() {
			String sValue = DBAttributes.getAttrValue("A0078_NAD");
			if (!sValue.equals("")){
				//System.out.println("double A0078 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0190() {
			return DBAttributes.getAttrValue("D0190");
		}


		public String getA0083() {
			return DBAttributes.getAttrValue("A0083");
		}


		public String getD0185() {
			return DBAttributes.getAttrValue("D0185");
		}


		public String getA0084() {
			return DBAttributes.getAttrValue("A0084");
		}


		public String getD0186() {
			return DBAttributes.getAttrValue("D0186");
		}


		public String getA0085() {
			return DBAttributes.getAttrValue("A0085");
		}


		public double getA0498() {
			String sValue = DBAttributes.getAttrValue("A0498");
			if (!sValue.equals("")){
				//System.out.println("double A0498 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0183() {
			return DBAttributes.getAttrValue("D0183");
		}


		public String getD0184() {
			return DBAttributes.getAttrValue("D0184");
		}


		public String getA0086() {
			return DBAttributes.getAttrValue("A0086");
		}


		public String getD0189() {
			return DBAttributes.getAttrValue("D0189");
		}


		public String getA0080() {
			return DBAttributes.getAttrValue("A0080");
		}


		public double getA0497() {
			String sValue = DBAttributes.getAttrValue("A0497");
			if (!sValue.equals("")){
				//System.out.println("double A0497 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}
		
		public double getA0497_RET() {
			String sValue = DBAttributes.getAttrValue("A0497_RET");
			if (!sValue.equals("")){
				//System.out.println("double A0497 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}
		
		public double getA0497_NAD() {
			String sValue = DBAttributes.getAttrValue("A0497_NAD");
			if (!sValue.equals("")){
				//System.out.println("double A0497 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0187() {
			return DBAttributes.getAttrValue("D0187");
		}


		public String getA0082() {
			return DBAttributes.getAttrValue("A0082");
		}


		public String getD0188() {
			return DBAttributes.getAttrValue("D0188");
		}


		public String getA0175() {
			return DBAttributes.getAttrValue("A0175");
		}


		public String getD0088() {
			return DBAttributes.getAttrValue("D0088");
		}


		public String getA0177() {
			return DBAttributes.getAttrValue("A0177");
		}


		public String getD0089() {
			return DBAttributes.getAttrValue("D0089");
		}


		public int getA0178() {
			String sValue = DBAttributes.getAttrValue("A0178");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}


		public String getD0087() {
			return DBAttributes.getAttrValue("D0087");
		}


		public String getA0152() {
			return DBAttributes.getAttrValue("A0152");
		}


		public String getA0151() {
			return DBAttributes.getAttrValue("A0151");
		}


		public String getA0150() {
			return DBAttributes.getAttrValue("A0150");
		}


		public String getD0270() {
			return DBAttributes.getAttrValue("D0270");
		}


		public String getA0092() {
			return DBAttributes.getAttrValue("A0092");
		}


		public String getD0260() {
			return DBAttributes.getAttrValue("D0260");
		}


		public String getD0261() {
			return DBAttributes.getAttrValue("D0261");
		}


		public String getA0093() {
			return DBAttributes.getAttrValue("A0093");
		}


		public String getD0262() {
			return DBAttributes.getAttrValue("D0262");
		}


		public String getA0090() {
			return DBAttributes.getAttrValue("A0090");
		}


		public String getD0263() {
			return DBAttributes.getAttrValue("D0263");
		}


		public String getA0091() {
			return DBAttributes.getAttrValue("A0091");
		}


		public String getD0264() {
			return DBAttributes.getAttrValue("D0264");
		}


		public String getA0096() {
			return DBAttributes.getAttrValue("A0096");
		}


		public String getD0265() {
			return DBAttributes.getAttrValue("D0265");
		}


		public String getA0097() {
			return DBAttributes.getAttrValue("A0097");
		}


		public String getD0266() {
			return DBAttributes.getAttrValue("D0266");
		}


		public String getA0094() {
			return DBAttributes.getAttrValue("A0094");
		}


		public String getD0267() {
			return DBAttributes.getAttrValue("D0267");
		}


		public String getA0095() {
			return DBAttributes.getAttrValue("A0095");
		}


		public String getD0268() {
			return DBAttributes.getAttrValue("D0268");
		}


		public String getA0148() {
			return DBAttributes.getAttrValue("A0148");
		}


		public String getD0269() {
			return DBAttributes.getAttrValue("D0269");
		}


		public String getA0149() {
			return DBAttributes.getAttrValue("A0149");
		}


		public String getA0146() {
			return DBAttributes.getAttrValue("A0146");
		}


		public String getA0147() {
			return DBAttributes.getAttrValue("A0147");
		}


		public String getA0144() {
			return DBAttributes.getAttrValue("A0144");
		}


		public String getA0145() {
			return DBAttributes.getAttrValue("A0145");
		}


		public String getA0142() {
			return DBAttributes.getAttrValue("A0142");
		}


		public String getA0143() {
			return DBAttributes.getAttrValue("A0143");
		}


		public String getA0161() {
			return DBAttributes.getAttrValue("A0161");
		}


		public String getA0160() {
			return DBAttributes.getAttrValue("A0160");
		}


		public String getA0163() {
			return DBAttributes.getAttrValue("A0163");
		}


		public String getA0162() {
			return DBAttributes.getAttrValue("A0162");
		}


		public String getA0099() {
			return DBAttributes.getAttrValue("A0099");
		}


		public String getA0098() {
			return DBAttributes.getAttrValue("A0098");
		}


		public String getD0273() {
			return DBAttributes.getAttrValue("D0273");
		}


		public String getD0274() {
			return DBAttributes.getAttrValue("D0274");
		}


		public String getD0271() {
			return DBAttributes.getAttrValue("D0271");
		}


		public String getD0272() {
			return DBAttributes.getAttrValue("D0272");
		}


		public String getD0275() {
			return DBAttributes.getAttrValue("D0275");
		}


		public String getD0276() {
			return DBAttributes.getAttrValue("D0276");
		}


		public String getA0157() {
			return DBAttributes.getAttrValue("A0157");
		}


		public String getA0158() {
			return DBAttributes.getAttrValue("A0158");
		}


		public String getA0159() {
			return DBAttributes.getAttrValue("A0159");
		}


		public String getA0153() {
			return DBAttributes.getAttrValue("A0153");
		}


		public String getA0154() {
			return DBAttributes.getAttrValue("A0154");
		}


		public String getA0155() {
			return DBAttributes.getAttrValue("A0155");
		}


		public String getA0156() {
			return DBAttributes.getAttrValue("A0156");
		}


		public String getD0159() {
			return DBAttributes.getAttrValue("D0159");
		}


		public String getD0158() {
			return DBAttributes.getAttrValue("D0158");
		}


		public String getD0153() {
			return DBAttributes.getAttrValue("D0153");
		}


		public String getD0152() {
			return DBAttributes.getAttrValue("D0152");
		}


		public String getD0151() {
			return DBAttributes.getAttrValue("D0151");
		}


		public String getD0150() {
			return DBAttributes.getAttrValue("D0150");
		}


		public String getD0157() {
			return DBAttributes.getAttrValue("D0157");
		}


		public String getD0156() {
			return DBAttributes.getAttrValue("D0156");
		}


		public String getD0155() {
			return DBAttributes.getAttrValue("D0155");
		}


		public String getD0154() {
			return DBAttributes.getAttrValue("D0154");
		}


		public String getD0160() {
			return DBAttributes.getAttrValue("D0160");
		}


		public String getD0045() {
			return DBAttributes.getAttrValue("D0045");
		}


		public String getD0148() {
			return DBAttributes.getAttrValue("D0148");
		}


		public String getD0044() {
			return DBAttributes.getAttrValue("D0044");
		}


		public String getD0147() {
			return DBAttributes.getAttrValue("D0147");
		}


		public String getD0047() {
			return DBAttributes.getAttrValue("D0047");
		}


		public String getD0046() {
			return DBAttributes.getAttrValue("D0046");
		}


		public String getD0149() {
			return DBAttributes.getAttrValue("D0149");
		}


		public String getD0041() {
			return DBAttributes.getAttrValue("D0041");
		}


		public String getD0040() {
			return DBAttributes.getAttrValue("D0040");
		}


		public String getA0244() {
			return DBAttributes.getAttrValue("A0244");
		}


		public String getD0043() {
			return DBAttributes.getAttrValue("D0043");
		}


		public String getA0243() {
			return DBAttributes.getAttrValue("A0243");
		}


		public String getD0042() {
			return DBAttributes.getAttrValue("D0042");
		}


		public String getD0140() {
			return DBAttributes.getAttrValue("D0140");
		}


		public String getD0142() {
			return DBAttributes.getAttrValue("D0142");
		}


		public String getA0342() {
			return DBAttributes.getAttrValue("A0342");
		}


		public String getD0141() {
			return DBAttributes.getAttrValue("D0141");
		}


		public String getD0049() {
			return DBAttributes.getAttrValue("D0049");
		}


		public String getD0144() {
			return DBAttributes.getAttrValue("D0144");
		}


		public String getD0048() {
			return DBAttributes.getAttrValue("D0048");
		}


		public String getD0143() {
			return DBAttributes.getAttrValue("D0143");
		}


		public String getD0146() {
			return DBAttributes.getAttrValue("D0146");
		}


		public String getD0145() {
			return DBAttributes.getAttrValue("D0145");
		}


		public String getD0032() {
			return DBAttributes.getAttrValue("D0032");
		}


		public String getD0031() {
			return DBAttributes.getAttrValue("D0031");
		}


		public String getD0030() {
			return DBAttributes.getAttrValue("D0030");
		}


		public double getA0237() {
			String sValue = DBAttributes.getAttrValue("A0237");
			if (!sValue.equals("")){
				//System.out.println("double A0237 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0036() {
			return DBAttributes.getAttrValue("D0036");
		}


		public String getD0035() {
			return DBAttributes.getAttrValue("D0035");
		}


		public String getD0034() {
			return DBAttributes.getAttrValue("D0034");
		}


		public String getD0033() {
			return DBAttributes.getAttrValue("D0033");
		}


		public String getD0179() {
			return DBAttributes.getAttrValue("D0179");
		}


		public String getD0178() {
			return DBAttributes.getAttrValue("D0178");
		}


		public String getD0039() {
			return DBAttributes.getAttrValue("D0039");
			/*	String sValue = DBAttributes.getAttrValue("D0039");
			if (!sValue.equals("")){
				//System.out.println("double D0039 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}*/
		}


		public double getA0239() {
			String sValue = DBAttributes.getAttrValue("A0239");
			if (!sValue.equals("")){
				//System.out.println("double A0239 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0177() {
			return DBAttributes.getAttrValue("D0177");
		}


		public String getD0038() {
			return DBAttributes.getAttrValue("D0038");
		}


		public String getD0176() {
			return DBAttributes.getAttrValue("D0176");
		}


		public double getA0238() {
			String sValue = DBAttributes.getAttrValue("A0238");
			if (!sValue.equals("")){
				//System.out.println("double A0238 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0037() {
			return DBAttributes.getAttrValue("D0037");
		}


		public String getD0181() {
			return DBAttributes.getAttrValue("D0181");
		}


		public String getD0182() {
			return DBAttributes.getAttrValue("D0182");
		}


		public String getD0180() {
			return DBAttributes.getAttrValue("D0180");
		}


		public double getA0240() {
			String sValue = DBAttributes.getAttrValue("A0240");
			if (!sValue.equals("")){
				//System.out.println("double A0240 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public int getA0220() {
			String sValue = DBAttributes.getAttrValue("A0220");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}


		public String getD0021() {
			return DBAttributes.getAttrValue("D0021");
		}


		public String getD0020() {
			return DBAttributes.getAttrValue("D0020");
		}


		public String getD0023() {
			return DBAttributes.getAttrValue("D0023");
		}


		public String getD0169() {
			return DBAttributes.getAttrValue("D0169");
		}


		public String getD0022() {
			return DBAttributes.getAttrValue("D0022");
		}


		public String getD0025() {
			return DBAttributes.getAttrValue("D0025");
		}


		public String getD0024() {
			return DBAttributes.getAttrValue("D0024");
		}


		public String getD0166() {
			return DBAttributes.getAttrValue("D0166");
		}


		public String getD0027() {
			return DBAttributes.getAttrValue("D0027");
		}


		public String getD0165() {
			return DBAttributes.getAttrValue("D0165");
		}


		public String getD0026() {
			return DBAttributes.getAttrValue("D0026");
		}


		public int getD0168() {
			String sValue = DBAttributes.getAttrValue("D0168");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}


		public String getD0029() {
			return DBAttributes.getAttrValue("D0029");
		}


		public String getD0167() {
			return DBAttributes.getAttrValue("D0167");
		}


		public String getD0028() {
			return DBAttributes.getAttrValue("D0028");
		}


		public String getD0162() {
			return DBAttributes.getAttrValue("D0162");
		}


		public String getD0161() {
			return DBAttributes.getAttrValue("D0161");
		}


		public String getD0164() {
			return DBAttributes.getAttrValue("D0164");
		}


		public String getD0163() {
			return DBAttributes.getAttrValue("D0163");
		}


		public String getD0170() {
			return DBAttributes.getAttrValue("D0170");
		}


		public double getD0171() {
			String sValue = DBAttributes.getAttrValue("D0171");
			if (!sValue.equals("")){
				//System.out.println("double D0171 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public double getA0310() {
			String sValue = DBAttributes.getAttrValue("A0310");
			if (!sValue.equals("")){
				//System.out.println("double A0310 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0019() {
			return DBAttributes.getAttrValue("D0019");
		}


		public String getA0313() {
			return DBAttributes.getAttrValue("A0313");
		}


		public String getD0017() {
			return DBAttributes.getAttrValue("D0017");
		}


		public String getD0112() {
			return DBAttributes.getAttrValue("D0112");
		}


		public String getA0314() {
			return DBAttributes.getAttrValue("A0314");
		}


		public String getD0018() {
			return DBAttributes.getAttrValue("D0018");
		}


		public int getD0113() {
			String sValue = DBAttributes.getAttrValue("D0113");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}


		public String getD0210() {
			return DBAttributes.getAttrValue("D0210");
		}


		public String getA0311() {
			return DBAttributes.getAttrValue("A0311");
		}


		public String getD0211() {
			return DBAttributes.getAttrValue("D0211");
		}


		public String getD0110() {
			return DBAttributes.getAttrValue("D0110");
		}


		public String getA0312() {
			return DBAttributes.getAttrValue("A0312");
		}


		public String getD0212() {
			return DBAttributes.getAttrValue("D0212");
		}


		public String getD0111() {
			return DBAttributes.getAttrValue("D0111");
		}


		public String getD0213() {
			return DBAttributes.getAttrValue("D0213");
		}


		public String getD0116() {
			return DBAttributes.getAttrValue("D0116");
		}


		public String getD0214() {
			return DBAttributes.getAttrValue("D0214");
		}


		public String getD0117() {
			return DBAttributes.getAttrValue("D0117");
		}


		public String getA0315() {
			return DBAttributes.getAttrValue("A0315");
		}


		public String getD0215() {
			return DBAttributes.getAttrValue("D0215");
		}


		public String getA0212() {
			return DBAttributes.getAttrValue("A0212");
		}


		public String getD0114() {
			return DBAttributes.getAttrValue("D0114");
		}


		public String getD0011() {
			return DBAttributes.getAttrValue("D0011");
		}


		public String getA0316() {
			return DBAttributes.getAttrValue("A0316");
		}


		public String getD0216() {
			return DBAttributes.getAttrValue("D0216");
		}


		public String getD0115() {
			return DBAttributes.getAttrValue("D0115");
		}


		public String getD0217() {
			return DBAttributes.getAttrValue("D0217");
		}


		public String getD0218() {
			return DBAttributes.getAttrValue("D0218");
		}


		public double getA0211() {
			String sValue = DBAttributes.getAttrValue("A0211");
			if (!sValue.equals("")){
				//System.out.println("double A0211 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0219() {
			return DBAttributes.getAttrValue("D0219");
		}


		public String getD0118() {
			return DBAttributes.getAttrValue("D0118");
		}


		public String getD0119() {
			return DBAttributes.getAttrValue("D0119");
		}


		public String getA0017() {
			return DBAttributes.getAttrValue("A0017");
		}


		public String getA0119() {
			return DBAttributes.getAttrValue("A0119");
		}


		public String getA0118() {
			return DBAttributes.getAttrValue("A0118");
		}


		public String getA0117() {
			return DBAttributes.getAttrValue("A0117");
		}


		public String getA0013() {
			return DBAttributes.getAttrValue("A0013");
		}

		public String getA0013_RET() {
			return DBAttributes.getAttrValue("A0013_RET");
		}
		
		public String getA0013_NAD() {
			return DBAttributes.getAttrValue("A0013_NAD");
		}
		
		public String getA0112() {
			return DBAttributes.getAttrValue("A0112");
		}


		public String getA0111() {
			return DBAttributes.getAttrValue("A0111");
		}


		public String getA0110() {
			return DBAttributes.getAttrValue("A0110");
		}


		public String getA0116() {
			return DBAttributes.getAttrValue("A0116");
		}


		public String getA0115() {
			return DBAttributes.getAttrValue("A0115");
		}


		public String getA0114() {
			return DBAttributes.getAttrValue("A0114");
		}


		public String getA0113() {
			return DBAttributes.getAttrValue("A0113");
		}


		public String getD0200() {
			return DBAttributes.getAttrValue("D0200");
		}


		public String getD0201() {
			return DBAttributes.getAttrValue("D0201");
		}


		public String getD0100() {
			return DBAttributes.getAttrValue("D0100");
		}


		public String getD0101() {
			return DBAttributes.getAttrValue("D0101");
		}


		public String getA0302() {
			return DBAttributes.getAttrValue("A0302");
		}


		public String getD0102() {
			return DBAttributes.getAttrValue("D0102");
		}


		public String getD0103() {
			return DBAttributes.getAttrValue("D0103");
		}


		public String getD0204() {
			return DBAttributes.getAttrValue("D0204");
		}


		public String getD0205() {
			return DBAttributes.getAttrValue("D0205");
		}


		public String getD0104() {
			return DBAttributes.getAttrValue("D0104");
		}


		public String getD0202() {
			return DBAttributes.getAttrValue("D0202");
		}


		public String getD0105() {
			return DBAttributes.getAttrValue("D0105");
		}


		public double getA0307() {
			String sValue = DBAttributes.getAttrValue("A0307");
			if (!sValue.equals("")){
				//System.out.println("double A0307 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0203() {
			return DBAttributes.getAttrValue("D0203");
		}


		public String getD0106() {
			return DBAttributes.getAttrValue("D0106");
		}


		public double getA0308() {
			String sValue = DBAttributes.getAttrValue("A0308");
			if (!sValue.equals("")){
				//System.out.println("double A0308 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0208() {
			return DBAttributes.getAttrValue("D0208");
		}


		public String getD0107() {
			return DBAttributes.getAttrValue("D0107");
		}


		public double getA0309() {
			String sValue = DBAttributes.getAttrValue("A0309");
			if (!sValue.equals("")){
				//System.out.println("double A0309 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0209() {
			return DBAttributes.getAttrValue("D0209");
		}


		public String getD0108() {
			return DBAttributes.getAttrValue("D0108");
		}


		public String getD0206() {
			return DBAttributes.getAttrValue("D0206");
		}


		public String getD0109() {
			return DBAttributes.getAttrValue("D0109");
		}


		public String getD0207() {
			return DBAttributes.getAttrValue("D0207");
		}


		public String getA0107() {
			return DBAttributes.getAttrValue("A0107");
		}


		public String getA0106() {
			return DBAttributes.getAttrValue("A0106");
		}


		public String getA0109() {
			return DBAttributes.getAttrValue("A0109");
		}


		public String getA0108() {
			return DBAttributes.getAttrValue("A0108");
		}


		public String getA0101() {
			return DBAttributes.getAttrValue("A0101");
		}


		public String getA0100() {
			return DBAttributes.getAttrValue("A0100");
		}


		public String getA0103() {
			return DBAttributes.getAttrValue("A0103");
		}


		public String getA0102() {
			return DBAttributes.getAttrValue("A0102");
		}


		public String getA0105() {
			return DBAttributes.getAttrValue("A0105");
		}


		public String getA0104() {
			return DBAttributes.getAttrValue("A0104");
		}


		public String getA0140() {
			return DBAttributes.getAttrValue("A0140");
		}


		public String getD0134() {
			return DBAttributes.getAttrValue("D0134");
		}


		public String getD0135() {
			return DBAttributes.getAttrValue("D0135");
		}


		public String getA0141() {
			return DBAttributes.getAttrValue("A0141");
		}


		public String getD0132() {
			return DBAttributes.getAttrValue("D0132");
		}


		public String getD0133() {
			return DBAttributes.getAttrValue("D0133");
		}


		public String getD0130() {
			return DBAttributes.getAttrValue("D0130");
		}


		public String getD0131() {
			return DBAttributes.getAttrValue("D0131");
		}


		public String getA0339() {
			return DBAttributes.getAttrValue("A0339");
		}


		public String getD0138() {
			return DBAttributes.getAttrValue("D0138");
		}


		public String getD0139() {
			return DBAttributes.getAttrValue("D0139");
		}


		public String getD0136() {
			return DBAttributes.getAttrValue("D0136");
		}


		public String getD0137() {
			return DBAttributes.getAttrValue("D0137");
		}


		public String getA0139() {
			return DBAttributes.getAttrValue("A0139");
		}


		public String getA0138() {
			return DBAttributes.getAttrValue("A0138");
		}


		public String getA0137() {
			return DBAttributes.getAttrValue("A0137");
		}


		public String getA0136() {
			return DBAttributes.getAttrValue("A0136");
		}


		public String getA0135() {
			return DBAttributes.getAttrValue("A0135");
		}


		public String getA0134() {
			return DBAttributes.getAttrValue("A0134");
		}


		public String getD0121() {
			return DBAttributes.getAttrValue("D0121");
		}


		public String getA0323() {
			return DBAttributes.getAttrValue("A0323");
		}


		public String getD0122() {
			return DBAttributes.getAttrValue("D0122");
		}


		public String getA0324() {
			return DBAttributes.getAttrValue("A0324");
		}


		public String getD0123() {
			return DBAttributes.getAttrValue("D0123");
		}


		public String getD0124() {
			return DBAttributes.getAttrValue("D0124");
		}


		public String getA0130() {
			return DBAttributes.getAttrValue("A0130");
		}


		public String getD0120() {
			return DBAttributes.getAttrValue("D0120");
		}


		public String getD0129() {
			return DBAttributes.getAttrValue("D0129");
		}


		public String getD0125() {
			return DBAttributes.getAttrValue("D0125");
		}


		public String getD0126() {
			return DBAttributes.getAttrValue("D0126");
		}


		public String getD0127() {
			return DBAttributes.getAttrValue("D0127");
		}


		public String getD0128() {
			return DBAttributes.getAttrValue("D0128");
		}


		public String getA0128() {
			return DBAttributes.getAttrValue("A0128");
		}


		public String getA0125() {
			return DBAttributes.getAttrValue("A0125");
		}


		public String getA0124() {
			return DBAttributes.getAttrValue("A0124");
		}


		public String getA0127() {
			return DBAttributes.getAttrValue("A0127");
		}


		public String getA0126() {
			return DBAttributes.getAttrValue("A0126");
		}


		public String getA0121() {
			return DBAttributes.getAttrValue("A0121");
		}


		public String getA0120() {
			return DBAttributes.getAttrValue("A0120");
		}


		public String getA0123() {
			return DBAttributes.getAttrValue("A0123");
		}


		public String getA0122() {
			return DBAttributes.getAttrValue("A0122");
		}


		public String getA0027() {
			return DBAttributes.getAttrValue("A0027");
		}


		public String getA0028() {
			return DBAttributes.getAttrValue("A0028");
		}


		public String getA0025() {
			return DBAttributes.getAttrValue("A0025");
		}


		public String getA0026() {
			return DBAttributes.getAttrValue("A0026");
		}


		public String getA0024() {
			return DBAttributes.getAttrValue("A0024");
		}


		public String getA0254() {
			return DBAttributes.getAttrValue("A0254");
		}


		public String getD0015() {
			return DBAttributes.getAttrValue("D0015");
		}


		public String getD0016() {
			return DBAttributes.getAttrValue("D0016");
		}


		public String getA0022() {
			return DBAttributes.getAttrValue("A0022");
		}


		public String getA0214() {
			return DBAttributes.getAttrValue("A0214");
		}


		public String getD0013() {
			return DBAttributes.getAttrValue("D0013");
		}


		public String getD0014() {
			return DBAttributes.getAttrValue("D0014");
		}


		public String getA0318() {
			return DBAttributes.getAttrValue("A0318");
		}


		public String getD0012() {
			return DBAttributes.getAttrValue("D0012");
		}


		public String getA0213() {
			return DBAttributes.getAttrValue("A0213");
		}


		public String getA0210() {
			return DBAttributes.getAttrValue("A0210");
		}


		public String getA0029() {
			return DBAttributes.getAttrValue("A0029");
		}


		public int getA0066() {
			String sValue = DBAttributes.getAttrValue("A0066");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}


		public String getA0404() {
			return DBAttributes.getAttrValue("A0404");
		}


		public int getA0065() {
			String sValue = DBAttributes.getAttrValue("A0065");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}


		public String getA0015() {
			return DBAttributes.getAttrValue("A0015");
		}


		public String getA0011() {
			return DBAttributes.getAttrValue("A0011");
		}


		public String getA0030() {
			return DBAttributes.getAttrValue("A0030");
		}


		public String getA0018() {
			return DBAttributes.getAttrValue("A0018");
		}

		public String getA0018_RET() {
			return DBAttributes.getAttrValue("A0018_RET");
		}

		public String getA0018_NAD() {
			return DBAttributes.getAttrValue("A0018_NAD");
		}


		public String getA0036() {
			return DBAttributes.getAttrValue("A0036");
		}


		public String getA0037() {
			return DBAttributes.getAttrValue("A0037");
		}


		public String getA0181() {
			return DBAttributes.getAttrValue("A0181");
		}


		public String getA0248() {
			return DBAttributes.getAttrValue("A0248");
		}


		public Date getA0180() {
			Date date = null;
			String sValue = DBAttributes.getAttrValue("A0180");
			try {
				if (!sValue.equals("")){
					date = (new SimpleDateFormat("yyyy-MM-dd")).parse(sValue);
				}
			 } catch (Throwable e) {
				 e.printStackTrace();
			 }
			return date;
		}


		public String getA0183() {
			return DBAttributes.getAttrValue("A0183");
		}


		public String getA0182() {
			return DBAttributes.getAttrValue("A0182");
		}


		public int getA0301() {
			String sValue = DBAttributes.getAttrValue("A0301");
			if (!sValue.equals("")){
				return Integer.parseInt(sValue);
			}
			else {
				return 0;
			}
		}


		public double getA0241() {
			String sValue = DBAttributes.getAttrValue("A0241");
			if (!sValue.equals("")){
				//System.out.println("double A0241 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getD0005() {
			return DBAttributes.getAttrValue("D0005");
		}


		public String getA0185() {
			return DBAttributes.getAttrValue("A0185");
		}


		public Date getA0184() {
			Date date = null;
			String sValue = DBAttributes.getAttrValue("A0184");
			try {
				if (!sValue.equals("")){
					date = (new SimpleDateFormat("yyyy-MM-dd")).parse(sValue);
				}
			 } catch (Throwable e) {
				 e.printStackTrace();
			 }
			return date;
		}


		public String getD0007() {
			return DBAttributes.getAttrValue("D0007");
		}


		public String getA0304() {
			return DBAttributes.getAttrValue("A0304");
		}


		public String getA0251() {
			return DBAttributes.getAttrValue("A0251");
		}


		public String getD0086() {
			return DBAttributes.getAttrValue("D0086");
		}


		public String getA0008() {
			return DBAttributes.getAttrValue("A0008");
		}


		public String getA0179() {
			return DBAttributes.getAttrValue("A0179");
		}


		public String getA0007() {
			return DBAttributes.getAttrValue("A0007");
		}


		public String getD0085() {
			return DBAttributes.getAttrValue("D0085");
		}


		public String getA0230() {
			return DBAttributes.getAttrValue("A0230");
		}


		public String getD0175() {
			return DBAttributes.getAttrValue("D0175");
		}


		public String getA0191() {
			return DBAttributes.getAttrValue("A0191");
		}


		public String getD0174() {
			return DBAttributes.getAttrValue("D0174");
		}


		public String getD0173() {
			return DBAttributes.getAttrValue("D0173");
		}


		public String getA0190() {
			return DBAttributes.getAttrValue("A0190");
		}


		public String getA0189() {
			return DBAttributes.getAttrValue("A0189");
		}


		public String getA0186() {
			return DBAttributes.getAttrValue("A0186");
		}


		public double getA0052() {
			String sValue = DBAttributes.getAttrValue("A0052");
			if (!sValue.equals("")){
				//System.out.println("double A0052 sValue="+sValue);
				return Double.parseDouble(sValue);
			}
			else {
				return 0.0;
			}
		}


		public String getA0320() {
			return DBAttributes.getAttrValue("A0320");
		}


		public String getA0410() {
			return DBAttributes.getAttrValue("A0410");
		}


		public String getA0016() {
			return DBAttributes.getAttrValue("A0016");
		}


		public String getA0038() {
			return DBAttributes.getAttrValue("A0038");
		}


		public String getA0231() {
			return DBAttributes.getAttrValue("A0231");
		}


		public String getA0224() {
			return DBAttributes.getAttrValue("A0224");
		}


		public String getA0033() {
			return DBAttributes.getAttrValue("A0033");
		}


		public String getA0081() {
			return DBAttributes.getAttrValue("A0081");
		}


		public String getA0234() {
			return DBAttributes.getAttrValue("A0234");
		}


		public String getA0217() {
			return DBAttributes.getAttrValue("A0217");
		}


		public String getA0431() {
			return DBAttributes.getAttrValue("A0431");
		}


		public String getA0031() {
			return DBAttributes.getAttrValue("A0031");
		}


		public String getA0020() {
			return DBAttributes.getAttrValue("A0020");
		}


		public String getA0042() {
			return DBAttributes.getAttrValue("A0042");
		}

		public String getA0229() {
			return DBAttributes.getAttrValue("A0229");
		}
		
		// start merged with sprint_A3
		public String getD0285() {
			return DBAttributes.getAttrValue("D0285");
		}
		public String getBoomerang_Trigger() {
			return DBAttributes.getAttrValue("Boomerang_Trigger");
		}

		public String getA0501() {
			return DBAttributes.getAttrValue("A0501");
		}
		// end merged with sprint_A3
		
	 	public String getA0506() {
			return DBAttributes.getAttrValue("A0506");
		}
		public String getA0507() {
			return DBAttributes.getAttrValue("A0507");
		}
		public String getA0508() {
			return DBAttributes.getAttrValue("A0508");
		}
		public String getA0509() {
			return DBAttributes.getAttrValue("A0509");
		}   
		
		public String getA0516() {
			//modified by saravanan
			return DBAttributes.getAttrValue("A0516");
		}
		// newly added attributes
		public String getA0499(){
			IntgSrvUtils.printConsole("A0049 "+DBAttributes.getAttrValue("A0499"));
			return DBAttributes.getAttrValue("A0499");
		}
		public String getA0504(){
			IntgSrvUtils.printConsole("A0504 "+DBAttributes.getAttrValue("A0504"));
			return DBAttributes.getAttrValue("A0504");
		}
		public Date getA0500(){
			Date date = null;
			String sValue = DBAttributes.getAttrValue("A0500");
			try {
				if (!sValue.equals("")){
					date = (new SimpleDateFormat("yyyy-MM-dd")).parse(sValue);
				}
			 } catch (Throwable e) {
				 e.printStackTrace();
			 }
			return date;
		}
		
		public String getA0002() {
			return DBAttributes.getAttrValue("A0002");
		}
		
		public String getA0537() {
			return DBAttributes.getAttrValue("A0537");
		}
		
		private String A0012 = "";
        // end of code block for writing to Database
        // ***********************************************************************************************

         /**
          * <p>Java class for anonymous complex type.
          *
          * <p>The following schema fragment specifies the expected content contained within this class.
          *
          * <pre>
          * &lt;complexType>
          *   &lt;simpleContent>
          *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
          *       &lt;attribute name="AssetID" type="{http://www.w3.org/2001/XMLSchema}string" />
          *       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}string" />
          *     &lt;/extension>
          *   &lt;/simpleContent>
          * &lt;/complexType>
          * </pre>
          *
          *
          */
         @XmlAccessorType(XmlAccessType.FIELD)
         @XmlType(name = "", propOrder = {})
         public static class AssetCrossReference {

             @XmlValue
             protected String value;
             @XmlAttribute(name = "AssetID")
             protected String assetID;
             @XmlAttribute(name = "Type")
             protected String type;
             
             /**
              * Gets the value of the value property.
              *
              * @return
              *     possible object is
              *     {@link String }
              *
              */
             public String getValue() {
                 traceLogger.info(clazzroot, "3AssestCrossReference.getValue", "ENTER/EXIT");
                 return value;
             }

             /**
              * Sets the value of the value property.
              *
              * @param value
              *     allowed object is
              *     {@link String }
              *
              */
             public void setValue(String value) {
                 traceLogger.info(clazzroot, "3AssestCrossReference.setValue", "ENTER/EXIT: value = " + value);
                 this.value = value;
             }

             /**
              * Gets the value of the assetID property.
              *
              * @return
              *     possible object is
              *     {@link String }
              *
              */
             public String getAssetID() {
                 traceLogger.info(clazzroot, "3AssestCrossReference.getAssetID", "ENTER/EXIT: assetID = " + assetID);
                 return assetID;
             }

             /**
              * Sets the value of the assetID property.
              *
              * @param value
              *     allowed object is
              *     {@link String }
              *
              */
             public void setAssetID(String value) {
                 traceLogger.info(clazzroot, "3AssestCrossReference.setAssetID", "ENTER/EXIT: value = " + value);
                 this.assetID = value;
             }

             /**
              * Gets the value of the type property.
              *
              * @return
              *     possible object is
              *     {@link String }
              *
              */
             public String getType() {
                 traceLogger.info(clazzroot, "3AssestCrossReference.getType", "ENTER/EXIT: type = " + type);
                 return type;
             }

             /**
              * Sets the value of the type property.
              *
              * @param value
              *     allowed object is
              *     {@link String }
              *
              */
             public void setType(String value) {
                 traceLogger.info(clazzroot, "3AssestCrossReference.setType", "ENTER/EXIT: value = " + value);
                 this.type = value;
             }

         }

         @XmlAccessorType(XmlAccessType.FIELD)
         @XmlType(name = "", propOrder = {})
         public static class ProductCrossReference {

             @XmlElement(name = "Product")
             public STEPProductInformation.Products.Product product;
             @XmlAttribute(name = "ProductID")
             protected String productID;
             @XmlAttribute(name = "Type")
             protected String type;
             
             /**
              * Gets the value of the value property.
              *
              * @return
              *     possible object is
              *     {@link String }
              *
              */
             public STEPProductInformation.Products.Product getProduct() {
                 traceLogger.info(clazzroot, "3ProductCrossReverence.getProdcut", "ENTER/EXIT");
                 if(ProductAttributesInProcess.responsibilityAttributeList!=null && !(ProductAttributesInProcess.responsibilityAttributeList.isEmpty())){
						product.values.value.addAll(ProductAttributesInProcess.responsibilityAttributeList);
						ProductAttributesInProcess.responsibilityAttributeList.clear();
					}
                 if(ProductAttributesInProcess.vendorData!=null && !(ProductAttributesInProcess.vendorData.isEmpty())){
						product.values.value.addAll(ProductAttributesInProcess.vendorData);
						ProductAttributesInProcess.vendorData.clear();
					}
                 
                 return product;
             }

             /**
              * Sets the value of the value property.
              *
              * @param value
              *     allowed object is
              *     {@link String }
              *
              */
             public void setValue(STEPProductInformation.Products.Product product) {
                 traceLogger.info(clazzroot, "3ProductCrossReverence.setProdcut", "ENTER/EXIT");
                 this.product = product;
             }

             /**
              * Gets the value of the assetID property.
              *
              * @return
              *     possible object is
              *     {@link String }
              *
              */
             public String getProductID() {
                 traceLogger.info(clazzroot, "3ProductCrossReverence.getProductID", "ENTER/EXIT: productID = " + productID);
                 return productID;
             }

             /**
              * Sets the value of the assetID property.
              *
              * @param value
              *     allowed object is
              *     {@link String }
              *
              */
             public void setProductID(String value) {
                 traceLogger.info(clazzroot, "3ProductCrossReverence.setProductID", "ENTER/EXIT: value = " + value);
                 this.productID = value;
             }

             /**
              * Gets the value of the type property.
              *
              * @return
              *     possible object is
              *     {@link String }
              *
              */
             public String getType() {
                 traceLogger.info(clazzroot, "3ProductCrossReverence.getType", "ENTER/EXIT: type = " + type);
                 return type;
             }

             /**
              * Sets the value of the type property.
              *
              * @param value
              *     allowed object is
              *     {@link String }
              *
              */
             public void setType(String value) {
                 traceLogger.info(clazzroot, "3ProductCrossReverence.setType", "ENTER/EXIT: value = " + value);
                 this.type = value;
             }

         }


         /**
          * <p>Java class for anonymous complex type.
          *
          * <p>The following schema fragment specifies the expected content contained within this class.
          *
          * <pre>
          * &lt;complexType>
          *   &lt;simpleContent>
          *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
          *       &lt;attribute name="ClassificationID" type="{http://www.w3.org/2001/XMLSchema}string" />
          *       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}string" />
          *     &lt;/extension>
          *   &lt;/simpleContent>
          * &lt;/complexType>
          * </pre>
          *
          *
          */
         @XmlAccessorType(XmlAccessType.FIELD)
         @XmlType(name = "", propOrder = {})
         public static class ClassificationReference {

        	 //@XmlValue
             //protected String value;//comment out, since the value change to MetaData
             @XmlAttribute(name = "ClassificationID")
             protected String classificationID;
             @XmlAttribute(name = "Type")
             protected String type;
             @XmlElement(name = "MetaData", required = true)
             public STEPProductInformation.Products.Product.ClassificationReference.MetaData metaData;
             /**
              * Gets the value of the value property.
              *
              * @return
              *     possible object is
              *     {@link String }
              *
              */
             //comment out, since the value change to MetaData
             /*
             public String getValue() {
                 traceLogger.info(clazzroot, "3ClassificationReference.getValue", "ENTER/EXIT: THIS.value = " + value);
                 return value;
             }
             */

             /**
              * Sets the value of the value property.
              *
              * @param value
              *     allowed object is
              *     {@link String }
              *
              */
             //comment out, since the value change to MetaData
             /*
             public void setValue(String value) {
                 traceLogger.info(clazzroot, "3ClassificationReference.setValue", "ENTER/EXIT: value = " + value);
                 this.value = value;
             }
             */

             /**
              * Gets the value of the classificationID property.
              *
              * @return
              *     possible object is
              *     {@link String }
              *
              */
             public String getClassificationID() {
                 traceLogger.info(clazzroot, "3ClassificationReference.getClassificationID", "ENTER/EXIT: classificationID = " + classificationID);
                 return classificationID;
             }

             /**
              * Sets the value of the classificationID property.
              *
              * @param value
              *     allowed object is
              *     {@link String }
              *
              */
             public void setClassificationID(String value) {
                 traceLogger.info(clazzroot, "3ClassificationReference.setClassificationID", "ENTER/EXIT: value = " + value);
                 this.classificationID = value;
             }

             /**
              * Gets the value of the type property.
              *
              * @return
              *     possible object is
              *     {@link String }
              *
              */
             public String getType() {
                 traceLogger.info(clazzroot, "3ClassificationReference.getType", "ENTER/EXIT: type = " + type);
                 return type;
             }

             /**
              * Sets the value of the type property.
              *
              * @param value
              *     allowed object is
              *     {@link String }
              *
              */
             public void setType(String value) {
                 traceLogger.info(clazzroot, "3ClassificationReference.setType", "ENTER/EXIT: value = " + value);
                 this.type = value;
             }
             public STEPProductInformation.Products.Product.ClassificationReference.MetaData getMetaData() {
            	 traceLogger.info(clazzroot, "ClassificationReference.getMetaData()", "ENTER/EXIT: to call ClassificationReference.getMetaData()");
                 return metaData;
             }

             /**
              * Sets the value of the values property.
              *
              * @param value
              *     allowed object is
              *     {@link STEPProductInformation.Products.Product.Values }
              *
              */
             public void setMetaData(STEPProductInformation.Products.Product.ClassificationReference.MetaData metaData) {
            	 traceLogger.info(clazzroot, "ClassificationReference.setMetaData()", "ENTER/EXIT");
                 this.metaData = metaData;
             }


             
             
             @XmlAccessorType(XmlAccessType.FIELD)
             @XmlType(name = "", propOrder = {})
             public static class MetaData {   

                 @XmlElement(name = "Value")
                 public List<STEPProductInformation.Products.Product.Values.Value> value;
                 
                 
                 public List<STEPProductInformation.Products.Product.Values.Value> getValue() {
                     traceLogger.info(clazzroot, "3Values.getValue", "ENTER/.../EXIT: to call 4Value.getValue()");
                     if (value == null) {
                         value = new ArrayList<STEPProductInformation.Products.Product.Values.Value>();
                         //LOGGER.debug("new ArrayList for values: ImportXMLValuesSetMapper");
                     }
                     //traceLogger.info(clazzroot, "3Values.getValue", "EXIT");
                     return this.value;
                 }

                 /**
                  * <p>Java class for anonymous complex type.
                  *
                  * <p>The following schema fragment specifies the expected content contained within this class.
                  *
                  * <pre>
                  * &lt;complexType>
                  *   &lt;simpleContent>
                  *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
                  *       &lt;attribute name="AttributeID" type="{http://www.w3.org/2001/XMLSchema}string" />
                  *       &lt;attribute name="UnitID" type="{http://www.w3.org/2001/XMLSchema}string" />
                  *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
                  *       &lt;attribute name="Derived" type="{http://www.w3.org/2001/XMLSchema}string" />
                  *     &lt;/extension>
                  *   &lt;/simpleContent>
                  * &lt;/complexType>
                  * </pre>
                  *
                  *
                  */
                 @XmlAccessorType(XmlAccessType.FIELD)
                 @XmlType(name = "", propOrder = {})
                 public static class Value {   

                     @XmlValue
                     public String value;
                     @XmlAttribute(name = "AttributeID")
                     public String attributeID;
                     @XmlAttribute(name = "UnitID")
                     public String unitID;
                     @XmlAttribute(name = "ID")
                     public String id;
                     @XmlAttribute(name = "Derived")
                     public String derived;
                     
                     /**
                      * Gets the value of the value property.
                      *
                      * @return
                      *     possible object is
                      *     {@link String }
                      *
                      */
                     public String getValue() {
                         //traceLogger.info(clazzroot, "4Value.getValue()", "ENTER/EXIT: this.value = " + value);
                    	 //LOGGER.debug("getValue()="+value+"; for value: ImportXMLValue");
                    	 return value;
                     }

                     /**
                      * Sets the value of the value property.
                      *
                      * @param value
                      *     allowed object is
                      *     {@link String }
                      *
                      */
                     public void setValue(String value) {
                         //traceLogger.info(clazzroot, "4Value.setValue", "ENTER/EXIT: param-value = " + value);
                         this.value = value;
                     }

                     /**
                      * Gets the value of the attributeID property.
                      *
                      * @return
                      *     possible object is
                      *     {@link String }
                      *
                      */
                     public String getAttributeID() {
                         //traceLogger.info(clazzroot, "4Value.getAttributeID", "ENTER/EXIT: attributeID = " + attributeID);
                    	 //LOGGER.debug("getAttributeID()="+attributeID+"; for value: ImportXMLValue");
                         return attributeID;
                     }

                     /**
                      * Sets the value of the attributeID property.
                      *
                      * @param value
                      *     allowed object is
                      *     {@link String }
                      *
                      */
                     public void setAttributeID(String value) {
                         //traceLogger.info(clazzroot, "4Value.setAttributeID", "ENTER/EXIT: value = " + value);
                         this.attributeID = value;
                     }

                     /**
                      * Gets the value of the unitID property.
                      *
                      * @return
                      *     possible object is
                      *     {@link String }
                      *
                      */
                     public String getUnitID() {
                         //traceLogger.info(clazzroot, "4Value.getUnitID", "ENTER/EXIT: unitID = " + unitID);
                    	 //LOGGER.debug("getUnitID()="+unitID+"; for value: ImportXMLValue");
                         return unitID;
                     }

                     /**
                      * Sets the value of the unitID property.
                      *
                      * @param value
                      *     allowed object is
                      *     {@link String }
                      *
                      */
                     public void setUnitID(String value) {
                         //traceLogger.info(clazzroot, "4Value.setUnitID", "ENTER/EXIT: value = " + value);
                         this.unitID = value;
                     }

                     /**
                      * Gets the value of the id property.
                      *
                      * @return
                      *     possible object is
                      *     {@link String }
                      *
                      */
                     public String getID() {
                         //traceLogger.info(clazzroot, "4Value.getID", "ENTER/EXIT: id = " + id);
                         return id;
                     }

                     /**
                      * Sets the value of the id property.
                      *
                      * @param value
                      *     allowed object is
                      *     {@link String }
                      *
                      */
                     public void setID(String value) {
                         //traceLogger.info(clazzroot, "4Value.setID()", "ENTER/EXIT: value = " + value);
                         this.id = value;
                     }

                     /**
                      * Gets the value of the derived property.
                      *
                      * @return
                      *     possible object is
                      *     {@link String }
                      *
                      */
                     public String getDerived() {
                         //traceLogger.info(clazzroot, "4Value.getDrived", "ENTER/EXIT: derived = " + derived);
                         return derived;
                     }

                     /**
                      * Sets the value of the derived property.
                      *
                      * @param value
                      *     allowed object is
                      *     {@link String }
                      *
                      */
                     public void setDerived(String value) {
                         //traceLogger.info(clazzroot, "4Value.setDrived", "ENTER/EXIT: value = " + value);
                         this.derived = value;
                     }
                     /* sima added  MultiValue attribute start changes*/          
                 }// end value 

             }             

         }


         /**
          * <p>Java class for anonymous complex type.
          *
          * <p>The following schema fragment specifies the expected content contained within this class.
          *
          * <pre>
          * &lt;complexType>
          *   &lt;complexContent>
          *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
          *       &lt;sequence>
          *         &lt;element name="Value" maxOccurs="unbounded" minOccurs="0">
          *           &lt;complexType>
          *             &lt;simpleContent>
          *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
          *                 &lt;attribute name="AttributeID" type="{http://www.w3.org/2001/XMLSchema}string" />
          *                 &lt;attribute name="UnitID" type="{http://www.w3.org/2001/XMLSchema}string" />
          *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
          *                 &lt;attribute name="Derived" type="{http://www.w3.org/2001/XMLSchema}string" />
          *               &lt;/extension>
          *             &lt;/simpleContent>
          *           &lt;/complexType>
          *         &lt;/element>
          *       &lt;/sequence>
          *     &lt;/restriction>
          *   &lt;/complexContent>
          * &lt;/complexType>
          * </pre>
          *
          *
          */
         @XmlAccessorType(XmlAccessType.FIELD)
         @XmlType(name = "", propOrder = {})
         public static class Values {   

             @XmlElement(name = "Value")
             public List<STEPProductInformation.Products.Product.Values.Value> value;
             
             /* sima added multiValue start  */
             @XmlElement(name = "MultiValue")
             public List<STEPProductInformation.Products.Product.Values.MultiValue> multiValue;
             /* sima added multiValue end  */
             
             /**
              * Gets the value of the value property.
              *
              * <p>
              * This accessor method returns a reference to the live list,
              * not a snapshot. Therefore any modification you make to the
              * returned list will be present inside the JAXB object.
              * This is why there is not a <CODE>set</CODE> method for the value property.
              *
              * <p>
              * For example, to add a new item, do as follows:
              * <pre>
              *    getValue().add(newItem);
              * </pre>
              *
              *
              * <p>
              * Objects of the following type(s) are allowed in the list
              * {@link STEPProductInformation.Products.Product.Values.Value }
              *
              *
              */
             public List<STEPProductInformation.Products.Product.Values.Value> getValue() {
                 traceLogger.info(clazzroot, "3Values.getValue", "ENTER/.../EXIT: to call 4Value.getValue()");
                 if (value == null) {
                     value = new ArrayList<STEPProductInformation.Products.Product.Values.Value>();
                     //LOGGER.debug("new ArrayList for values: ImportXMLValuesSetMapper");
                 }
                 //traceLogger.info(clazzroot, "3Values.getValue", "EXIT");
                 return this.value;
             }
             /* sima start changes  */
             
             /**
              * Gets the value of the value property.
              *
              * <p>
              * This accessor method returns a reference to the live list,
              * not a snapshot. Therefore any modification you make to the
              * returned list will be present inside the JAXB object.
              * This is why there is not a <CODE>set</CODE> method for the value property.
              *
              * <p>
              * For example, to add a new item, do as follows:
              * <pre>
              *    getValue().add(newItem);
              * </pre>
              *
              *
              * <p>
              * Objects of the following type(s) are allowed in the list
              * {@link STEPProductInformation.Products.Product.Values.Value.MultiValue }
              *
              *
              */
             public List<STEPProductInformation.Products.Product.Values.MultiValue> getMultiValue() {
                 traceLogger.info(clazzroot, "3Values.MultiValue.getValue", "ENTER/.../EXIT: to call 4Value.MultiValue.getMultiValues()");
                 if (multiValue == null) {
                	 multiValue = new ArrayList<STEPProductInformation.Products.Product.Values.MultiValue>(); 
                 }
                  
                 return this.multiValue;
             }

             
             
             /* sima end changes */

             /**
              * <p>Java class for anonymous complex type.
              *
              * <p>The following schema fragment specifies the expected content contained within this class.
              *
              * <pre>
              * &lt;complexType>
              *   &lt;simpleContent>
              *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
              *       &lt;attribute name="AttributeID" type="{http://www.w3.org/2001/XMLSchema}string" />
              *       &lt;attribute name="UnitID" type="{http://www.w3.org/2001/XMLSchema}string" />
              *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
              *       &lt;attribute name="Derived" type="{http://www.w3.org/2001/XMLSchema}string" />
              *     &lt;/extension>
              *   &lt;/simpleContent>
              * &lt;/complexType>
              * </pre>
              *
              *
              */
             @XmlAccessorType(XmlAccessType.FIELD)
             @XmlType(name = "", propOrder = {})
             public static class Value {   

                 @XmlValue
                 public String value;
                 @XmlAttribute(name = "AttributeID")
                 public String attributeID;
                 @XmlAttribute(name = "UnitID")
                 public String unitID;
                 @XmlAttribute(name = "ID")
                 public String id;
                 @XmlAttribute(name = "Derived")
                 public String derived;
                 
                 /**
                  * Gets the value of the value property.
                  *
                  * @return
                  *     possible object is
                  *     {@link String }
                  *
                  */
                 public String getValue() {
                     //traceLogger.info(clazzroot, "4Value.getValue()", "ENTER/EXIT: this.value = " + value);
                	 //LOGGER.debug("getValue()="+value+"; for value: ImportXMLValue");
                	 return value;
                 }

                 /**
                  * Sets the value of the value property.
                  *
                  * @param value
                  *     allowed object is
                  *     {@link String }
                  *
                  */
                 public void setValue(String value) {
                     //traceLogger.info(clazzroot, "4Value.setValue", "ENTER/EXIT: param-value = " + value);
                     this.value = value;
                 }

                 /**
                  * Gets the value of the attributeID property.
                  *
                  * @return
                  *     possible object is
                  *     {@link String }
                  *
                  */
                 public String getAttributeID() {
                     //traceLogger.info(clazzroot, "4Value.getAttributeID", "ENTER/EXIT: attributeID = " + attributeID);
                	 //LOGGER.debug("getAttributeID()="+attributeID+"; for value: ImportXMLValue");
                     return attributeID;
                 }

                 /**
                  * Sets the value of the attributeID property.
                  *
                  * @param value
                  *     allowed object is
                  *     {@link String }
                  *
                  */
                 public void setAttributeID(String value) {
                     //traceLogger.info(clazzroot, "4Value.setAttributeID", "ENTER/EXIT: value = " + value);
                     this.attributeID = value;
                 }

                 /**
                  * Gets the value of the unitID property.
                  *
                  * @return
                  *     possible object is
                  *     {@link String }
                  *
                  */
                 public String getUnitID() {
                     //traceLogger.info(clazzroot, "4Value.getUnitID", "ENTER/EXIT: unitID = " + unitID);
                	 //LOGGER.debug("getUnitID()="+unitID+"; for value: ImportXMLValue");
                     return unitID;
                 }

                 /**
                  * Sets the value of the unitID property.
                  *
                  * @param value
                  *     allowed object is
                  *     {@link String }
                  *
                  */
                 public void setUnitID(String value) {
                     //traceLogger.info(clazzroot, "4Value.setUnitID", "ENTER/EXIT: value = " + value);
                     this.unitID = value;
                 }

                 /**
                  * Gets the value of the id property.
                  *
                  * @return
                  *     possible object is
                  *     {@link String }
                  *
                  */
                 public String getID() {
                     //traceLogger.info(clazzroot, "4Value.getID", "ENTER/EXIT: id = " + id);
                     return id;
                 }

                 /**
                  * Sets the value of the id property.
                  *
                  * @param value
                  *     allowed object is
                  *     {@link String }
                  *
                  */
                 public void setID(String value) {
                     //traceLogger.info(clazzroot, "4Value.setID()", "ENTER/EXIT: value = " + value);
                     this.id = value;
                 }

                 /**
                  * Gets the value of the derived property.
                  *
                  * @return
                  *     possible object is
                  *     {@link String }
                  *
                  */
                 public String getDerived() {
                     //traceLogger.info(clazzroot, "4Value.getDrived", "ENTER/EXIT: derived = " + derived);
                     return derived;
                 }

                 /**
                  * Sets the value of the derived property.
                  *
                  * @param value
                  *     allowed object is
                  *     {@link String }
                  *
                  */
                 public void setDerived(String value) {
                     //traceLogger.info(clazzroot, "4Value.setDrived", "ENTER/EXIT: value = " + value);
                     this.derived = value;
                 }
                 /* sima added  MultiValue attribute start changes*/          
             }// end value 

     /* sima added  MultiValue attribute start changes*/           
                 /**
                  * <p>Java class for anonymous complex type.
                  *
                  * <p>The following schema fragment specifies the expected content contained within this class.
                  *
                  * <pre>
                  * &lt;complexType>
                  *   &lt;simpleContent>
                  *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
                  *       &lt;attribute name="AttributeID" type="{http://www.w3.org/2001/XMLSchema}string" />                  *     
                  *     &lt;/extension>
                  *   &lt;/simpleContent>
                  * &lt;/complexType>
                  * </pre>
                  *
                  *
                  */
                 @XmlAccessorType(XmlAccessType.FIELD)
                 @XmlType(name = "", propOrder = {})
                 public static class MultiValue {   
                   
                     @XmlAttribute(name = "AttributeID")
                     public String attributeID;  
                     
                     @XmlElement(name = "Value")
                     public List<STEPProductInformation.Products.Product.Values.MultiValue.Value> value;

                     /**
                      * Gets the value of the attributeID property.
                      *
                      * @return
                      *     possible object is
                      *     {@link String }
                      *
                      */
                     public String getAttributeID() { 
                         return attributeID;
                     }

                     /**
                      * Sets the value of the attributeID property.
                      *
                      * @param value
                      *     allowed object is
                      *     {@link String }
                      *
                      */
                     public void setAttributeID(String value) { 
                         this.attributeID = value;
                     } 
                     
                     
                     public List<STEPProductInformation.Products.Product.Values.MultiValue.Value> getValue() {
                         traceLogger.info(clazzroot, "3Values.MultiValue.getValues", "ENTER/.../EXIT: to call 4Value.MultiValue.getValue()");
                         if (value == null) {
                        	 value = new ArrayList<STEPProductInformation.Products.Product.Values.MultiValue.Value>();
                             //LOGGER.debug("new ArrayList for values: ImportXMLValuesSetMapper");
                         }
                         //traceLogger.info(clazzroot, "3Values.getValue", "EXIT");
                         return this.value;
                     }


					/**
                      * <p>Java class for anonymous complex type.
                      *
                      * <p>The following schema fragment specifies the expected content contained within this class.
                      *
                      * <pre>
                      * &lt;complexType>
                      *   &lt;simpleContent>
                      *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
                      *       &lt;attribute name="Value" type="{http://www.w3.org/2001/XMLSchema}string" />                  *     
                      *     &lt;/extension>
                      *   &lt;/simpleContent>
                      * &lt;/complexType>
                      * </pre>
                      *
                      *
                      */
                     @XmlAccessorType(XmlAccessType.FIELD)
                     @XmlType(name = "", propOrder = {})
                     public static class Value {   
                       
                    	 @XmlValue
                    	 public String value;

						public String getValue() {
							return value;
						}

						public void setValue(String value) {
							this.value = value;
						}
                     }	  
                     
                 } // end muliValue 
         } 
     } 
 }
}