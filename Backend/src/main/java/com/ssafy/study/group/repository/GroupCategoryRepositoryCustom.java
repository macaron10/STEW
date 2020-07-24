package com.ssafy.study.group.repository;

import java.util.List;

import com.ssafy.study.group.model.GroupCategory;

public interface GroupCategoryRepositoryCustom {

	public List<GroupCategory> selectBoxLgGroupCategory();

	public List<GroupCategory> selectBoxMdGroupCategory(String lg);

	public List<GroupCategory> selectBoxSmGroupCategory(String lg, String md);

}
