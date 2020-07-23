package com.ssafy.study.group.repository;

import java.util.List;

import com.ssafy.study.group.model.GroupCategory;

public interface GroupCategoryRepositoryCustom {

	public List<GroupCategory> selectBoxGroupCategory(int idx, int catNo);
}
