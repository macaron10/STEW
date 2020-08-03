package com.ssafy.study.group.service;

import java.util.List;

import com.ssafy.study.group.model.dto.GroupSearchDto;
import com.ssafy.study.group.model.dto.ModifyGroupDto;
import com.ssafy.study.group.model.dto.RegistGroupTagDto;
import com.ssafy.study.group.model.dto.GroupCategoryDto;
import com.ssafy.study.group.model.dto.GroupDto;
import com.ssafy.study.group.model.dto.GroupTagDto;
import com.ssafy.study.group.model.entity.Group;

public interface GroupService {

	GroupDto saveGroup(Group group);

	void deleteGroup(long gpNo);

	GroupDto selectGroup(long gpNo);

	List<GroupDto> selectAllGroups();

	List<GroupDto> findMyGroups(long userId);

	List<GroupDto> searchGroups(GroupSearchDto groupSearch);

	List<GroupCategoryDto> selectBoxLgGroupCategory();

	List<GroupCategoryDto> selectBoxMdGroupCategory(String lg);

	List<GroupCategoryDto> selectBoxSmGroupCategory(String lg, String md);

	void requestJoinGroup(long userId, long gpNo);

	void acceptJoinGroup(long reqNo);

	void joinGroup(long userId, long gpNo);

	void rejectJoinGroup(long reqNo);

	void removeGroupMember(long joinNo);

	boolean ckGroupJoin(long gpNo, long userId);

	void exitGroup(long gpNo, long userId);

	boolean isGroupFull(long gpNo);

	GroupDto updateGroup(ModifyGroupDto modifyGroup);

	long addGroupTag(RegistGroupTagDto tag);

	boolean checkGroupTagExist(String tagNm);

	List<GroupTagDto> selectAllGroupTags();

	List<GroupTagDto> selectGroupTagList(long gpNo);

	boolean ckGroupExist(long no);

}
