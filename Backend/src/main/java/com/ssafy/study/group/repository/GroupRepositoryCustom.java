package com.ssafy.study.group.repository;

import java.util.List;

import com.ssafy.study.group.model.dto.GroupSearchDto;
import com.ssafy.study.group.model.dto.GroupDto;
import com.ssafy.study.group.model.entity.Group;

public interface GroupRepositoryCustom {
	
	GroupDto selectByGpNo(long gpNo);

	List<GroupDto> findMyJoinGroup(long userId);

	List<GroupDto> searchGroup(GroupSearchDto search);

	boolean isGroupFull(long gpNo);

	List<GroupDto> selectAllGroups();

}
