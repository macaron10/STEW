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
	private GroupRepository gpRepo;

	@Override
	public Group createGroup(Group group) {
		return gpRepo.save(group);
	}

	@Override
	public void modifyGroup(Group group) {
		gpRepo.save(group);
	}

	@Override
	public void deleteGroup(long gpNo) {
		gpRepo.deleteById(gpNo);
	}

	@Override
	public GroupDto.ResGroup selectGroup(long gpNo) {
		Optional<Group> g = gpRepo.findByGpNo(gpNo);
		if (g.isPresent())
			return new ResGroup(g.get());
		return null;
	}

	@Override
	public List<Group> findMyGroups(String userId) {
		return gpRepo.findByGpMgrId(userId);
	}

	@Override
	public List<Group> findAllGroups() {
		return gpRepo.findAll();
	}

	@Override
	public List<Group> searchGroups(GroupSearch groupSearch) {
		return gpRepo.searchGroups(groupSearch);
	}

}
