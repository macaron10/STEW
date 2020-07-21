package com.ssafy.study.group.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.study.group.model.Group;
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
	public void deleteGroup(long gp_id) {
		gpRepo.deleteById(gp_id);
	}

	@Override
	public int modifyGroup(Group group) {
		return 0;
	}

	@Override
	public Optional<Group> selectGroup(long gpNo) {
		return gpRepo.findByGpNo(gpNo);
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
	public List<Group> searchGroups() {
		return null;
	}


}
