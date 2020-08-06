package com.ssafy.study.controller;

import java.io.IOException;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ssafy.study.common.exception.FileUploadException;
import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.common.util.FileUtils;
import com.ssafy.study.group.model.dto.GroupDto;
import com.ssafy.study.group.model.dto.GroupJoinDto;
import com.ssafy.study.group.model.dto.GroupReqDto;
import com.ssafy.study.group.model.dto.ModifyGroupDto;
import com.ssafy.study.group.model.dto.RegistGroupDto;
import com.ssafy.study.group.model.dto.RequestGroupJoinDto;
import com.ssafy.study.group.model.entity.Group;
import com.ssafy.study.group.model.exception.GroupFullException;
import com.ssafy.study.group.model.exception.GroupNotExistException;
import com.ssafy.study.group.model.exception.GroupNotJoinedExcpetion;
import com.ssafy.study.group.model.exception.GroupUnAuthException;
import com.ssafy.study.group.service.GroupService;
import com.ssafy.study.user.model.UserPrincipal;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/study/user")
public class GroupController {

	@Autowired
	private GroupService groupService;
	@Autowired
	private FileUtils fileUtil;

	private final String fileBaseUrl = "/home/ubuntu/app/img/group";

	@GetMapping("/my")
	@ApiOperation("로그인한 회원의 스터디 목록 조회")
	public ResponseEntity findMyStudyList(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		System.out.println(userId);
		BasicResponse result = new BasicResponse();
		result.object = groupService.findMyGroups(userId);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/")
	@ApiOperation(value = "스터디 생성", produces = "multipart/form-data")
	public ResponseEntity createStudy(RegistGroupDto group,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		Group saveGroup = group.toEntity();
		saveGroup.setGpMgrId(principal.getUserId());

		if (group.getGpImg() != null) {
			try {
				saveGroup.setGpImg(fileUtil.uploadFile(group.getGpImg(), fileBaseUrl));
			} catch (IOException e) {
				e.printStackTrace();
				throw new FileUploadException();
			}
		}

		GroupDto responseGroup = groupService.saveGroup(saveGroup);

		groupService.joinGroup(principal.getUserId(), responseGroup.getGpNo());

		BasicResponse result = new BasicResponse();
		result.object = responseGroup;
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/{no}")
	@ApiOperation("스터디 상세 조회")
	public ResponseEntity selectStudyNo(@PathVariable long no,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse result = new BasicResponse();
		if (!groupService.ckGroupExist(no))
			throw new GroupNotExistException();

		if (!groupService.ckGroupJoin(no, principal.getUserId()))
			throw new GroupNotJoinedExcpetion();

		GroupDto group = groupService.selectGroup(no);
		List<GroupJoinDto> joinList = groupService.selectGroupMemberList(no);
		JSONObject obj = new JSONObject();
		obj.append("group", new Gson().toJson(group));
		obj.append("joinList", joinList);

		result.object = obj.toString();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/mem/{no}")
	@ApiOperation("스터디 가입 멤버 조회")
	public ResponseEntity findStudyMemberList(@PathVariable long no,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse result = new BasicResponse();
		if (!groupService.ckGroupExist(no))
			throw new GroupNotExistException();

		if (!groupService.ckGroupJoin(no, principal.getUserId()))
			throw new GroupNotJoinedExcpetion();

		result.object = groupService.selectGroupMemberList(no);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping("/{no}")
	@ApiOperation(value = "스터디 수정", produces = "multipart/form-data")
	public ResponseEntity modifytudy(@PathVariable long no, ModifyGroupDto modifyGroup,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse result = new BasicResponse();

		long userId = principal.getUserId();
		ckGroupAuth(userId, no);

		modifyGroup.setGpNo(no);

		if (modifyGroup.isUpdateGpImg() && modifyGroup.getGpImg() != null) {
			try {
				modifyGroup.setGpImgPath(fileUtil.uploadFile(modifyGroup.getGpImg(), fileBaseUrl));
			} catch (IOException e) {
				e.printStackTrace();
				throw new FileUploadException();
			}
		}

		GroupDto group = groupService.updateGroup(modifyGroup);

		result.object = group;
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/mgr")
	@ApiOperation("그룹장 넘기기")
	public ResponseEntity passGroupMgr(@RequestParam("no") @ApiParam(value = "gpNo", required = true) long gpNo,
			@RequestParam("userId") @ApiParam(value = "userId", required = true) long uid,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse result = new BasicResponse();

		ckGroupAuth(userId, gpNo);
		if (groupService.ckGroupJoin(gpNo, uid)) {
			result.msg = "Not Joined Member";
			result.status = false;

			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}

		result.object = groupService.passGroupMgr(gpNo, uid);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity(result, HttpStatus.OK);
	}

	@PostMapping("/req")
	@ApiOperation("스터디 가입 요청 (공개는 자동 가입, 비공개는 가입 요청)")
	public ResponseEntity reqJoinGroup(RequestGroupJoinDto reqJoin,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse result = new BasicResponse();

		if (groupService.ckGroupJoin(reqJoin.getGpNo(), userId)) {
			result.msg = "duplicate";
			result.status = false;

			return new ResponseEntity<>(result, HttpStatus.CONFLICT);
		}

		if (groupService.isGroupFull(reqJoin.getGpNo()))
			throw new GroupFullException();

		GroupDto group = groupService.selectGroup(reqJoin.getGpNo());
		if (group.isGpPublic()) {
			groupService.joinGroup(userId, reqJoin.getGpNo());
		} else {
			groupService.requestJoinGroup(userId, reqJoin);
		}

		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/accept")
	@ApiOperation("스터디 가입 승인")
	public ResponseEntity acceptJoinGroup(@RequestParam("no") @ApiParam(value = "reqNo", required = true) long reqNo,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();

		GroupReqDto req = groupService.selectGroupReqByReqnNo(reqNo);
		ckGroupAuth(userId, req.getGp().getGpNo());

		BasicResponse result = new BasicResponse();
		if (groupService.isGroupFull(req.getGp().getGpNo()))
			throw new GroupFullException();

		groupService.acceptJoinGroup(reqNo);

		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/reject")
	@ApiOperation("스터디 가입 거절")
	public ResponseEntity rejectJoinGroup(@RequestParam("no") @ApiParam(value = "reqNo", required = true) long reqNo,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();

		GroupReqDto req = groupService.selectGroupReqByReqnNo(reqNo);
		ckGroupAuth(userId, req.getGp().getGpNo());
		groupService.rejectJoinGroup(reqNo);

		BasicResponse result = new BasicResponse();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/remove")
	@ApiOperation("스터디 퇴출")
	public ResponseEntity removeGroupMember(
			@RequestParam("no") @ApiParam(value = "joinNo", required = true) long joinNo,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();

		GroupJoinDto join = groupService.selectGroupJoinByJoinNo(joinNo);
		ckGroupAuth(userId, join.getGpGpNo());
		groupService.removeGroupMember(joinNo);

		BasicResponse result = new BasicResponse();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/exit")
	@ApiOperation("그룹 나가기")
	public ResponseEntity exitGroup(@RequestParam("no") @ApiParam(value = "gpNo", required = true) long gpNo,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse result = new BasicResponse();

		if (!groupService.ckGroupJoin(gpNo, userId))
			throw new GroupNotJoinedExcpetion();

		if (isGroupMgr(userId, gpNo)) {
			if (groupService.selectGroup(gpNo).getGpCurNum() <= 1) {
				groupService.deleteGroup(gpNo);
			} else {
				result.msg = "매니저 탈퇴 불가";
				result.status = false;

				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		}

		groupService.exitGroup(gpNo, userId);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/reqlist")
	@ApiOperation("회원이 그룹장인 그룹의 모든 가입 요청 보기")
	public ResponseEntity groupReqList(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse result = new BasicResponse();
		long userId = principal.getUserId();

		result.object = groupService.selectGroupReq(userId);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/reqlist/{gpNo}")
	@ApiOperation("해당 그룹의 가입 요청 보기")
	public ResponseEntity groupReqList(@PathVariable long gpNo,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse result = new BasicResponse();
		long userId = principal.getUserId();

		result.object = groupService.selectGroupReqByGpNo(gpNo);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// Group Function******************************************

	public void ckGroupAuth(long userId, long gpNo) {
		if (!isGroupMgr(userId, gpNo))
			throw new GroupUnAuthException();
	}

	public boolean isGroupMgr(long userId, long gpNo) {
		long mgrId = groupService.selectGroup(gpNo).getGpMgrId();

		return userId == mgrId ? true : false;
	}

}
