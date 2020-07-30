package com.ssafy.study.group.service;

import java.util.List;

import com.ssafy.study.group.model.dto.GroupSearchDto;
import com.ssafy.study.group.model.dto.ModifyGroupDto;
import com.ssafy.study.group.model.dto.RegistGroupTagDto;
import com.ssafy.study.group.model.dto.ResGroupCategoryDto;
import com.ssafy.study.group.model.dto.ResGroupDto;
import com.ssafy.study.group.model.dto.ResGroupTagDto;
import com.ssafy.study.group.model.entity.Group;

public interface GroupService {

	ResGroupDto saveGroup(Group group);

	void deleteGroup(long gpNo);

	ResGroupDto selectGroup(long gpNo);

	List<ResGroupDto> selectAllGroups();

	List<ResGroupDto> findMyGroups(long userId);

	List<ResGroupDto> findAllGroups();

	List<ResGroupDto> searchGroups(GroupSearchDto groupSearch);

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

	ResGroupDto updateGroup(ModifyGroupDto modifyGroup);

	long addGroupTag(RegistGroupTagDto tag);

	boolean checkGroupTagExist(String tagNm);

	List<ResGroupTagDto> selectAllGroupTags();

	List<ResGroupTagDto> selectGroupTagList(long gpNo);

}
