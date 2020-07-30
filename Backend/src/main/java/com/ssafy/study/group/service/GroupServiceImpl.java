package com.ssafy.study.group.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.study.group.model.dto.GroupSearchDto;
import com.ssafy.study.group.model.dto.ModifyGroupDto;
import com.ssafy.study.group.model.dto.RegistGroupTagDto;
import com.ssafy.study.group.model.dto.ResGroupCategoryDto;
import com.ssafy.study.group.model.dto.ResGroupDto;
import com.ssafy.study.group.model.dto.ResGroupTagDto;
import com.ssafy.study.group.model.entity.Group;
import com.ssafy.study.group.model.entity.GroupJoin;
import com.ssafy.study.group.model.entity.GroupReq;
import com.ssafy.study.group.repository.GroupCategoryRepository;
import com.ssafy.study.group.repository.GroupJoinRepository;
import com.ssafy.study.group.repository.GroupRepository;
import com.ssafy.study.group.repository.GroupReqRepository;
import com.ssafy.study.group.repository.GroupTagRepository;

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
	@Autowired
	private GroupTagRepository tagRepo;

	@Override
	public ResGroupDto saveGroup(Group group) {
		return new ResGroupDto(gpRepo.save(group));
	}

	@Override
	public void deleteGroup(long gpNo) {
		joinRepo.deleteByGpNo(gpNo);
		reqRepo.deleteByGpNo(gpNo);
		gpRepo.deleteByGpNo(gpNo);
	}

	@Override
	public List<ResGroupDto> selectAllGroups() {
		return gpRepo.selectAllGroups();
	}

	@Override
	public ResGroupDto selectGroup(long gpNo) {
		ResGroupDto group = new ResGroupDto(gpRepo.findByGpNo(gpNo));

		return group;
	}

	@Override
	public List<ResGroupDto> findMyGroups(long userId) {
		return gpRepo.findMyJoinGroup(userId).stream().map(g -> new ResGroupDto(g)).collect(Collectors.toList());
	}

	@Override
	public List<ResGroupDto> findAllGroups() {
		return gpRepo.findAll().stream().map(g -> new ResGroupDto(g)).collect(Collectors.toList());
	}

	@Override
	public List<ResGroupDto> searchGroups(GroupSearchDto groupSearch) {
		return gpRepo.searchGroup(groupSearch).stream().map(g -> new ResGroupDto(g)).collect(Collectors.toList());
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

	@Override
	public ResGroupDto updateGroup(ModifyGroupDto modifyGroup) {
		Group group = gpRepo.findByGpNo(modifyGroup.getGpNo());
		group.update(modifyGroup);

		return new ResGroupDto(gpRepo.save(group));
	}

	@Override
	public long addGroupTag(RegistGroupTagDto tag) {
		return tagRepo.save(tag.toEntity()).getGpTagNo();
	}

	@Override
	public boolean checkGroupTagExist(String tagNm) {
		return tagRepo.checkGroupTagExist(tagNm);
	}

	@Override
	public List<ResGroupTagDto> selectAllGroupTags() {
		return tagRepo.findAll().stream().map(t -> new ResGroupTagDto(t)).collect(Collectors.toList());
	}

	@Override
	public List<ResGroupTagDto> selectGroupTagList(long gpNo) {
		return tagRepo.selectGroupTagList(gpNo).stream().map(t -> new ResGroupTagDto(t)).collect(Collectors.toList());
	}

}
