package com.ssafy.study.group.service;

import java.util.List;
import java.util.Optional;

import com.ssafy.study.group.model.Group;

public interface GroupService {

	Group createGroup(Group group);

	void deleteGroup(long gp_id);

	int modifyGroup(Group group);
	
	Optional<Group> selectGroup(long gpNo);

	List<Group> findMyGroups(String userId);

	List<Group> findAllGroups();
	
	List<Group> searchGroups();
}
