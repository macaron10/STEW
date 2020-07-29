package com.ssafy.study.group.service;

import java.util.List;

import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupDto.ModifyGroup;
import com.ssafy.study.group.model.GroupDto.ResGroup;
import com.ssafy.study.group.model.GroupSearch;
import com.ssafy.study.group.model.ResGroupCategoryDto;

public interface GroupService {

	ResGroup saveGroup(Group group);

	void deleteGroup(long gpNo);

	ResGroup selectGroup(long gpNo);

	List<ResGroup> findMyGroups(long userId);

	List<ResGroup> findAllGroups();

	List<ResGroup> searchGroups(GroupSearch groupSearch);

	List<ResGroupCategoryDto> selectBoxLgGroupCategory();

	List<ResGroupCategoryDto> selectBoxMdGroupCategory(String lg);

	List<ResGroupCategoryDto> selectBoxSmGroupCategory(String lg, String md);

	void requestJoinGroup(long userId, long gpNo);

	void acceptJoinGroup(long reqNo);

	void joinGroup(long userId, long gpNo);

	void rejectJoinGroup(long reqNo);

	void removeGroupMember(long joinNo);

	boolean ckGroupJoin(long gpNo, long userId);

	void exitGroup(long gpNo, long userId);

	boolean isGroupFull(long gpNo);

	ResGroup updateGroup(ModifyGroup modifyGroup);

}
