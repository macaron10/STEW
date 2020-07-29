package com.ssafy.study.group.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupJoin;
import com.ssafy.study.group.model.GroupReq;
import com.ssafy.study.group.model.GroupSearch;
import com.ssafy.study.group.model.ResGroupCategoryDto;
import com.ssafy.study.group.repository.GroupCategoryRepository;
import com.ssafy.study.group.repository.GroupJoinRepository;
import com.ssafy.study.group.repository.GroupRepository;
import com.ssafy.study.group.repository.GroupReqRepository;
import com.ssafy.study.user.model.User;

@Service
public class GroupServiceImpl implements GroupService {
	@Autowired
	private GroupRepository gpRepo;
	@Autowired
	private GroupCategoryRepository cateRepo;
	@Autowired
	private GroupJoinRepository joinRepo;
	@Autowired
	private GroupReqRepository reqRepo;

	@Override
	public Group saveGroup(Group group) {
		group.setGpCat(cateRepo.findByGpCatNo(group.getGpCat().getGpCatNo()));
		return gpRepo.save(group);
	}

	@Override
	public void deleteGroup(long gpNo) {
		joinRepo.deleteByGpNo(gpNo);
		reqRepo.deleteByGpNo(gpNo);
		gpRepo.deleteByGpNo(gpNo);
	}

	@Override
	public Group selectGroup(long gpNo) {
		return gpRepo.findByGpNo(gpNo);
	}

	@Override
	public List<Group> findMyGroups(long userId) {
		return gpRepo.findMyJoinGroup(userId);
	}

	@Override
	public List<Group> findAllGroups() {
		return gpRepo.findAll();
	}

	@Override
	public List<Group> searchGroups(GroupSearch groupSearch) {
		return gpRepo.searchGroup(groupSearch);
	}

	@Override
	public List<ResGroupCategoryDto> selectBoxLgGroupCategory() {
		return cateRepo.selectBoxLgGroupCategory().stream().map(cat -> new ResGroupCategoryDto(cat))
				.collect(Collectors.toList());
	}

	@Override
	public List<ResGroupCategoryDto> selectBoxMdGroupCategory(String lg) {
		return cateRepo.selectBoxMdGroupCategory(lg).stream().map(cat -> new ResGroupCategoryDto(cat))
				.collect(Collectors.toList());
	}

	@Override
	public List<ResGroupCategoryDto> selectBoxSmGroupCategory(String lg, String md) {
		return cateRepo.selectBoxSmGroupCategory(lg, md).stream().map(cat -> new ResGroupCategoryDto(cat))
				.collect(Collectors.toList());
	}

	@Override
	public void requestJoinGroup(long userId, long gpNo) {
		GroupReq req = new GroupReq(userId, gpNo);

		reqRepo.save(req);
	}

	@Override
	public void acceptJoinGroup(long reqNo) {
		GroupReq req = reqRepo.findByGpReqNo(reqNo);
		reqRepo.deleteByGpReqNo(reqNo);

		joinGroup(req.getUser().getUserId(), req.getGp().getGpNo());
	}

	@Override
	public void joinGroup(long userId, long gpNo) {
		GroupJoin join = new GroupJoin(gpNo, userId);

		joinRepo.save(join);
		gpRepo.increaseMemberCnt(gpNo);
	}

	@Override
	public void rejectJoinGroup(long reqNo) {
		reqRepo.deleteByGpReqNo(reqNo);
	}

	@Override
	public void removeGroupMember(long joinNo) {
		long gpNo = joinRepo.findByGpJoinNo(joinNo).getGp().getGpNo();
		joinRepo.deleteByGpJoinNo(joinNo);

		gpRepo.decreaseMemberCnt(gpNo);
	}

	@Override
	public boolean ckGroupJoin(long gpNo, long userId) {
		return joinRepo.ckGroupJoin(gpNo, userId);
	}

	@Override
	public void exitGroup(long gpNo, long userId) {
		joinRepo.deleteByGpNoAndUserId(gpNo, userId);
	}

	@Override
	public boolean isGroupFull(long gpNo) {
		return gpRepo.isGroupFull(gpNo);
	}

}
