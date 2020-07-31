package com.ssafy.study.group.repository;

import java.util.List;

import com.ssafy.study.group.model.entity.GroupTag;

public interface GroupTagRepositoryCustom {
	boolean checkGroupTagExist(String tagNm);

	List<GroupTag> selectGroupTagList(long gpNo);
}
