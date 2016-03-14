
package com.staples.pim.delegate.copyandprint.inbound.domain;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author 522462
 * 
 */
public class PIMCoreContentHandler extends DefaultHandler {

	private String					xPath				= "";
	private XMLReader				xmlReader;
	private PIMCoreContentHandler	parent;
	private StringBuilder			characters			= new StringBuilder();
	public Map<String, Integer>		elementNameCount	= new HashMap<String, Integer>();
	// private static Map<String, String> xPathValueMap = new HashMap<String,
	// String>();
	private Map<String, String>		xPathValueMap		= null;

	public PIMCoreContentHandler(XMLReader xmlReader, Map<String, String> xPathValueMap) {

		this.xmlReader = xmlReader;
		this.xPathValueMap = xPathValueMap;
	}

	private PIMCoreContentHandler(String xPath, XMLReader xmlReader, PIMCoreContentHandler parent, Map<String, String> xPathValueMap) {

		this(xmlReader, xPathValueMap);
		this.xPath = xPath;
		this.parent = parent;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

		Integer count = elementNameCount.get(qName);
		if (null == count) {
			count = 1;
		} else {
			count++;
		}
		elementNameCount.put(qName, count);
		if (qName.contains(":")) {
			qName = qName.split(":")[1];
		}
		String childXPath = xPath + "/" + qName + "[" + count + "]";

		int attsLength = atts.getLength();
		for (int x = 0; x < attsLength; x++) {
			// CommonUtil.printConsole(childXPath + "/@" + atts.getQName(x) +
			// "=" + atts.getValue(x) );
			xPathValueMap.put(childXPath + "/@" + atts.getQName(x), atts.getValue(x).trim());
		}

		PIMCoreContentHandler child = new PIMCoreContentHandler(childXPath, xmlReader, this, xPathValueMap);
		xmlReader.setContentHandler(child);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		String value = characters.toString().trim();
		if (value.length() > 0) {
			// CommonUtil.printConsole(xPath + "='" + characters.toString() +
			// "'");
			xPathValueMap.put(xPath, characters.toString().trim());
		}
		xmlReader.setContentHandler(parent);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {

		characters.append(ch, start, length);
	}

	/**
	 * @param xPathValueMap
	 */
	public void setxPathValueMap(Map<String, String> xPathValueMap) {

		this.xPathValueMap = xPathValueMap;
	}

	/**
	 * @return
	 */
	public Map<String, String> getxPathValueMap() {

		return xPathValueMap;
	}

	public static void main(String[] args) throws Exception {
		File folder = new File("D:\\CnP_phase2\\Inbound_xmls2");
		File[] files=folder.listFiles();
		for(File file:files )
		{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();
		PIMCoreContentHandler pimConHna = new PIMCoreContentHandler(xr, new HashMap<String, String>());
		xr.setContentHandler(pimConHna);
		xr.parse(new InputSource(new FileInputStream(file)));
		Map<String, String> xpathValueMap = pimConHna.getxPathValueMap();
		if(xpathValueMap.containsKey("/PublishItem[1]/Item[1]/CodeList[1]/Code[1]/Hierarchy[1]/Class[1]/Code[1]")) {
		if(xpathValueMap.get("/PublishItem[1]/Item[1]/CodeList[1]/Code[1]/Hierarchy[1]/Class[1]/Code[1]").equalsIgnoreCase("471") || xpathValueMap.get("/PublishItem[1]/Item[1]/CodeList[1]/Code[1]/Hierarchy[1]/Class[1]/Code[1]").equalsIgnoreCase("476") || xpathValueMap.get("/PublishItem[1]/Item[1]/CodeList[1]/Code[1]/Hierarchy[1]/Class[1]/Code[1]").equalsIgnoreCase("479")) {
			System.out.println(file.getName());
		}
		}
		//System.out.println(xpathValueMap);
		}

	}
}