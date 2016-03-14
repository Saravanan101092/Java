
package com.staples.pim.delegate.copyandprint.inbound.domain;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.staples.pim.delegate.datamigration.utils.DatamigrationCommonUtil;

import static com.staples.pim.delegate.datamigration.utils.DatamigrationAppConstants.INBOUND_MAPPING_FILE;

public class MappingLoader {

	private static MappingLoader						mappingLoader	= null;

	public static List<Mapping.Attributes.Attribute>	mappingAttList	= null;

	private MappingLoader() {

	}

	public static MappingLoader getInstanceLoadContext() {

		if (mappingLoader == null) {
			mappingLoader = new MappingLoader();
		}
		return mappingLoader;
	}

	public List<Mapping.Attributes.Attribute> loadMappingXML(String argStr) {

		if (mappingAttList == null) {
			// String path = CommonUtil.getConfigDir(argStr);
			File fileIn = new File(argStr);
			Mapping mappingObj = null;
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Mapping.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				mappingObj = (Mapping) jaxbUnmarshaller.unmarshal(fileIn);
				mappingAttList = mappingObj.getAttributes().getAttribute();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return mappingAttList;
	}

	public static void main(String st[]) {

		MappingLoader.getInstanceLoadContext().loadMappingXML(INBOUND_MAPPING_FILE);

		for (Mapping.Attributes.Attribute attObj : MappingLoader.mappingAttList) {
			DatamigrationCommonUtil.printConsole(attObj.getPimCore().getXPath());
			// DatamigrationCommonUtil.printConsole(attObj.getStep().getAttributeID());
		}
	}
}
