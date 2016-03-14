
package com.staples.pim.delegate.pyramid.itemonbrdtmupdate.mapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.staples.pim.delegate.pyramid.itemonbrdtmupdate.model.ItemOnbrdTmFeedBean;

public class ItemOnbrdTmFeedFieldSetMapper implements FieldSetMapper<ItemOnbrdTmFeedBean> {

	@Override
	public ItemOnbrdTmFeedBean mapFieldSet(FieldSet fieldSet) throws BindException {

		ItemOnbrdTmFeedBean bean = new ItemOnbrdTmFeedBean();
		bean.setSuperCategoryName((StringUtils.trimToEmpty(fieldSet.readString("superCategoryName"))));
		bean.setCategoryName((StringUtils.trimToEmpty(fieldSet.readString("categoryName"))));
		bean.setDepartmentName((StringUtils.trimToEmpty(fieldSet.readString("departmentName"))));
		bean.setClassName((StringUtils.trimToEmpty(fieldSet.readString("className"))));
		bean.setSuperCategoryId((StringUtils.trimToEmpty(fieldSet.readString("superCategoryId"))));
		bean.setCategoryId((StringUtils.trimToEmpty(fieldSet.readString("categoryId"))));
		bean.setDepartmentId((StringUtils.trimToEmpty(fieldSet.readString("departmentId"))));
		bean.setClassId((StringUtils.trimToEmpty(fieldSet.readString("classId"))));
		bean.setSkuSetName((StringUtils.trimToEmpty(fieldSet.readString("skuSetName"))));
		bean.setBusinessUnit((StringUtils.trimToEmpty(fieldSet.readString("businessUnit"))));
		return bean;

	}

}
