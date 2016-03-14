
package com.staples.pim.delegate.copyandprint.beans;

import java.util.List;

/**
 * @author 522462
 * 
 */
public class SheetListVO {

	private List<WebHierarchyVO>		webHierarchyList;
	private List<ConfigHierarchyVO>		configHierarchyList;
	private List<TemplatesVO>			templatesList;
	private List<ConfigurationsVO>		configList;
	private List<ImagesVO>				imageList;
	// Phase 2
	private List<ComponentPropertiesVO>	componentPropertiesList;
	private List<MessagingTypeVO>		messagingTypeList;
	private List<StyleReferencesVO>		styleReferenceList;
	private List<InvalidAssociationsVO>	invalidAssocationsList;
	private List<TemplateComponentsVO>	templateComponentsList;
	private List<KitContentsVO>			kitContentsList;
	private List<List<String>>			generatedKeys;

	/**
	 * @return the webHierarchyList
	 */
	public List<WebHierarchyVO> getWebHierarchyList() {

		return webHierarchyList;
	}

	/**
	 * @param webHierarchyList
	 *            the webHierarchyList to set
	 */
	public void setWebHierarchyList(List<WebHierarchyVO> webHierarchyList) {

		this.webHierarchyList = webHierarchyList;
	}

	/**
	 * @return the configHierarchyList
	 */
	public List<ConfigHierarchyVO> getConfigHierarchyList() {

		return configHierarchyList;
	}

	/**
	 * @param configHierarchyList
	 *            the configHierarchyList to set
	 */
	public void setConfigHierarchyList(List<ConfigHierarchyVO> configHierarchyList) {

		this.configHierarchyList = configHierarchyList;
	}

	/**
	 * @return the templatesList
	 */
	public List<TemplatesVO> getTemplatesList() {

		return templatesList;
	}

	/**
	 * @param templatesList
	 *            the templatesList to set
	 */
	public void setTemplatesList(List<TemplatesVO> templatesList) {

		this.templatesList = templatesList;
	}

	/**
	 * @param configList
	 *            the configList to set
	 */
	public void setConfigList(List<ConfigurationsVO> configList) {

		this.configList = configList;
	}

	/**
	 * @return the configList
	 */
	public List<ConfigurationsVO> getConfigList() {

		return configList;
	}

	/**
	 * @param imageList
	 *            the imageList to set
	 */
	public void setImageList(List<ImagesVO> imageList) {

		this.imageList = imageList;
	}

	/**
	 * @return the imageList
	 */
	public List<ImagesVO> getImageList() {

		return imageList;
	}

	// Phase 2
	/**
	 * @param componentPropertiesList
	 *            the componentPropertiesList to set
	 */
	public void setComponentPropertiesList(List<ComponentPropertiesVO> componentPropertiesList) {

		this.componentPropertiesList = componentPropertiesList;
	}

	/**
	 * @return the componentPropertiesList
	 */
	public List<ComponentPropertiesVO> getComponentPropertiesList() {

		return componentPropertiesList;
	}

	/**
	 * @param messagingTypeList
	 *            the messagingTypeList to set
	 */
	public void setMessagingTypeList(List<MessagingTypeVO> messagingTypeList) {

		this.messagingTypeList = messagingTypeList;
	}

	/**
	 * @return the componentPropertiesList
	 */
	public List<MessagingTypeVO> getMessagingTypeList() {

		return messagingTypeList;
	}

	/**
	 * @param styleReferenceList
	 *            the styleReferenceList to set
	 */
	public void setStyleReferenceVOList(List<StyleReferencesVO> styleReferenceVOList) {

		this.styleReferenceList = styleReferenceVOList;
	}

	/**
	 * @return the styleReferenceVOList
	 */
	public List<StyleReferencesVO> getStyleReferenceVOList() {

		return styleReferenceList;
	}

	/**
	 * @param invalidAssocationsList
	 *            the invalidAssocationsList to set
	 */
	public void setInvalidAssocationsList(List<InvalidAssociationsVO> invalidAssocationsList) {

		this.invalidAssocationsList = invalidAssocationsList;
	}

	/**
	 * @return the styleReferenceVOList
	 */
	public List<InvalidAssociationsVO> getInvalidAssocationsList() {

		return invalidAssocationsList;
	}

	/**
	 * @param templateComponentsList
	 *            the templateComponentsList to set
	 */
	public void setTemplateComponentsList(List<TemplateComponentsVO> templateComponentsList) {

		this.templateComponentsList = templateComponentsList;
	}

	/**
	 * @return the templateComponentsList
	 */
	public List<TemplateComponentsVO> getTemplateComponentsList() {

		return templateComponentsList;
	}

	/**
	 * @param kitContentsList
	 *            the kitContentsList to set
	 */
	public void setKitContentsList(List<KitContentsVO> kitContentsList) {

		this.kitContentsList = kitContentsList;
	}

	/**
	 * @return the kitContentsList
	 */
	public List<KitContentsVO> getKitContentsList() {

		return kitContentsList;
	}

	/**
	 * @param generatedKeys
	 *            the generatedKeys to set
	 */
	public void setgeneratedKeys(List<List<String>> generatedKeys) {

		this.generatedKeys = generatedKeys;
	}

	/**
	 * @return the generatedKeys
	 */
	public List<List<String>> getGeneratedKeys() {

		return generatedKeys;
	}
}
