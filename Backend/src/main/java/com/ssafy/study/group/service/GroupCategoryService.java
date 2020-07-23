package com.ssafy.study.group.service;

import java.util.List;

import com.ssafy.study.group.model.GroupCategoryDto.ResGroupCategoryDto;

public interface GroupCategoryService {

	public ResGroupCategoryDto selectInfoGroupCategory(int catNo);

	public List<ResGroupCategoryDto> selectBoxGroupCategory(int idx, int catNo);

}
