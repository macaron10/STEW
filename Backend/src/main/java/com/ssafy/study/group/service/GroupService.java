package com.ssafy.study.group.service;

import java.util.List;
import java.util.Optional;

import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupDto.ResGroup;
import com.ssafy.study.group.model.GroupSearch;

public interface GroupService {

	Group createGroup(Group group);

	void deleteGroup(long gpNo);

	void modifyGroup(Group group);

	ResGroup selectGroup(long gpNo);

	List<Group> findMyGroups(String userId);

	List<Group> findAllGroups();

	List<Group> searchGroups(GroupSearch groupSearch);
}
