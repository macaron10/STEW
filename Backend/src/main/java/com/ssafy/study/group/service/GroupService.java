package com.ssafy.study.group.service;

import java.util.List;

import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupSearch;
import com.ssafy.study.group.model.ResGroupCategoryDto;
import com.ssafy.study.user.model.User;

public interface GroupService {

	Group saveGroup(Group group);

	void deleteGroup(long gpNo);

	Group selectGroup(long gpNo);

	List<Group> findMyGroups(long userId);

	List<Group> findAllGroups();

	List<Group> searchGroups(GroupSearch groupSearch);

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

}
