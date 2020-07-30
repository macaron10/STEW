package com.ssafy.study.group.repository;

import java.util.List;

import com.ssafy.study.group.model.dto.GroupSearchDto;
import com.ssafy.study.group.model.dto.ResGroupDto;
import com.ssafy.study.group.model.entity.Group;

public interface GroupRepositoryCustom {

	List<Group> findMyJoinGroup(long userId);

	List<Group> searchGroup(GroupSearchDto search);

	boolean isGroupFull(long gpNo);

	List<ResGroupDto> selectAllGroups();

}
