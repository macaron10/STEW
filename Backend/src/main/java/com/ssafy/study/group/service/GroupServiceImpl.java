package com.ssafy.study.group.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupDto;
import com.ssafy.study.group.model.GroupDto.ResGroup;
import com.ssafy.study.group.model.GroupSearch;
import com.ssafy.study.group.repository.GroupRepository;

@Service
public class GroupServiceImpl implements GroupService {
	@Autowired
	private GroupRepository groupRepository;

	@Override
	public Group createGroup(Group group) {
		return groupRepository.save(group);
	}

	@Override
	public void modifyGroup(Group group) {
		groupRepository.save(group);
	}

	@Override
	public void deleteGroup(long gpNo) {
		groupRepository.deleteById(gpNo);
	}

	@Override
	public GroupDto.ResGroup selectGroup(long gpNo) {
		Optional<Group> g = groupRepository.findByGpNo(gpNo);
		if (g.isPresent())
			return new ResGroup(g.get());
		return null;
	}

	@Override
	public List<Group> findMyGroups(String userId) {
		return groupRepository.findByGpMgrId(userId);
	}

	@Override
	public List<Group> findAllGroups() {
		return groupRepository.findAll();
	}

	@Override
	public List<Group> searchGroups(GroupSearch groupSearch) {
		return groupRepository.searchGroups(groupSearch);
	}

}
