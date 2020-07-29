package com.ssafy.study.group.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;

import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupSearch;

public interface GroupRepositoryCustom {

	List<Group> findMyJoinGroup(long userId);

	List<Group> searchGroup(GroupSearch search);

	boolean isGroupFull(long gpNo);


}
