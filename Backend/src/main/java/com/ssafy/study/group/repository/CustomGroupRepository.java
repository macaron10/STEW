package com.ssafy.study.group.repository;

import java.util.List;

import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupSearch;

public interface CustomGroupRepository {

	List<Group> searchGroups(GroupSearch groupSearch);
}
