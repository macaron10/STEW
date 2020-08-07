package com.ssafy.study.group.service;

import java.util.List;

import com.ssafy.study.group.model.dto.GroupCategoryDto;
import com.ssafy.study.group.model.dto.GroupDto;
import com.ssafy.study.group.model.dto.GroupJoinDto;
import com.ssafy.study.group.model.dto.GroupReqDto;
import com.ssafy.study.group.model.dto.GroupSearchDto;
import com.ssafy.study.group.model.dto.ModifyGroupDto;
import com.ssafy.study.group.model.dto.RequestGroupJoinDto;
import com.ssafy.study.group.model.entity.Group;

public interface GroupService {

	GroupDto saveGroup(Group group);

	void deleteGroup(long gpNo);

	GroupDto selectGroup(long gpNo);

	List<GroupDto> selectAllGroups();

	List<GroupDto> findMyGroups(long userId);

	GroupDto passGroupMgr(long gpNo, long uid);

	List<GroupDto> searchGroups(GroupSearchDto groupSearch);

//	List<GroupCategoryDto> selectBoxLgGroupCategory();
//
//	List<GroupCategoryDto> selectBoxMdGroupCategory(String lg);
//
//	List<GroupCategoryDto> selectBoxSmGroupCategory(String lg, String md);

	List<GroupCategoryDto> selecBoxAllGroupCategory();

	void requestJoinGroup(long userId, RequestGroupJoinDto reqJoin);

	void acceptJoinGroup(long reqNo);

	void joinGroup(long userId, long gpNo);

	void rejectJoinGroup(long reqNo);

	void removeGroupMember(long joinNo);

	boolean ckGroupJoin(long gpNo, long userId);

	void exitGroup(long gpNo, long userId);

	boolean isGroupFull(long gpNo);

	GroupDto updateGroup(ModifyGroupDto modifyGroup);

	boolean ckGroupExist(long no);

	GroupJoinDto selectGroupJoinByJoinNo(long gpJoinNo);

	GroupReqDto selectGroupReqByReqnNo(long gpReqNo);

	List<GroupReqDto> selectGroupReq(long userId);

	List<GroupReqDto> selectGroupReqByGpNo(long gpNo);

	List<GroupJoinDto> selectGroupMemberList(long gpNo);

}
