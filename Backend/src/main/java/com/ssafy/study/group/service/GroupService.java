package com.ssafy.study.group.service;

import java.util.List;

import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupSearch;
import com.ssafy.study.group.model.ResGroupCategoryDto;

public interface GroupService {

	Group saveGroup(Group group);

	void deleteGroup(long gpNo);

	Group selectGroup(long gpNo);

	List<Group> findMyGroups(long userId);

	List<Group> findAllGroups();

	List<Group> searchGroups(GroupSearch groupSearch);

	public List<ResGroupCategoryDto> selectBoxLgGroupCategory();

	public List<ResGroupCategoryDto> selectBoxMdGroupCategory(String lg);

	public List<ResGroupCategoryDto> selectBoxSmGroupCategory(String lg, String md);

	public void requestJoinGroup(long userId, long gpNo);

	public void acceptJoinGroup(long reqNo);

	public void rejectJoinGroup(long reqNo);

	public void removeGroupMember(long joinNo);

	public boolean ckGroupJoin(long gpNo, long userId);

}
