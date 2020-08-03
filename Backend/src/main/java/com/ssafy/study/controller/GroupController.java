package com.ssafy.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.common.model.ErrorResponse;
import com.ssafy.study.common.util.FileUtils;
import com.ssafy.study.group.model.dto.GroupDto;
import com.ssafy.study.group.model.dto.ModifyGroupDto;
import com.ssafy.study.group.model.dto.RegistGroupDto;
import com.ssafy.study.group.model.entity.Group;
import com.ssafy.study.group.model.exception.GroupUnAuthException;
import com.ssafy.study.group.service.GroupService;
import com.ssafy.study.user.model.UserPrincipal;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@RestController
@RequestMapping("/study/user")
public class GroupController {

	@Autowired
	private GroupService groupService;
	@Autowired
	private FileUtils fileUtil;

	private final String fileBaseUrl = "C:\\Users\\multicampus\\Desktop\\group_thumb";

	@GetMapping("/my")
	@ApiOperation("로그인한 회원의 스터디 목록 조회")
	public Object findMyStudyList(@AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse result = new BasicResponse();
		result.object = groupService.findMyGroups(userId);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/")
	@ApiOperation(value = "스터디 생성", produces = "multipart/form-data")
	public Object createStudy(RegistGroupDto group, @AuthenticationPrincipal UserPrincipal principal) {
		Group saveGroup = group.toEntity();
		saveGroup.setGpMgrId(principal.getUserId());

		if (group.getGpImg() != null) {
			saveGroup.setGpImg(fileUtil.uploadFile(group.getGpImg(), fileBaseUrl));
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
	public Object selectStudyNo(@PathVariable long no, @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse result = new BasicResponse();
		if (!groupService.ckGroupJoin(no, principal.getUserId())) {
			result.status = false;
			result.msg = "not join";

			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		}

		GroupDto group = groupService.selectGroup(no);

		result.object = group;
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping("/")
	@ApiOperation(value = "스터디 수정", produces = "multipart/form-data")
	public Object modifytudy(ModifyGroupDto modifyGroup, @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse result = new BasicResponse();

		long userId = principal.getUserId();
		ckGroupAuth(userId, modifyGroup.getGpNo());

		if (modifyGroup.isUpdateGpImg() && modifyGroup.getGpImg() != null) {
			modifyGroup.setGpImgPath(fileUtil.uploadFile(modifyGroup.getGpImg(), fileBaseUrl));
		}

		GroupDto group = groupService.updateGroup(modifyGroup);

		result.object = group;
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/req")
	@ApiOperation("스터디에 가입요청")
	public Object reqJoinGroup(int gpNo, @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse result = new BasicResponse();

		if (groupService.isGroupFull(gpNo)) {
			result.msg = "group is full";
			result.status = false;
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

		if (groupService.ckGroupJoin(gpNo, userId)) {
			result.msg = "duplicate";
			result.status = false;

			return new ResponseEntity<>(result, HttpStatus.CONFLICT);
		}
		groupService.requestJoinGroup(userId, gpNo);

		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/accept")
	@ApiOperation("스터디 가입 승인")
	public Object acceptJoinGroup(long reqNo, long gpNo, @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();

		ckGroupAuth(userId, gpNo);

		BasicResponse result = new BasicResponse();
		if (groupService.isGroupFull(gpNo)) {
			result.msg = "group is full";
			result.status = false;
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

		groupService.acceptJoinGroup(reqNo);

		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/reject")
	@ApiOperation("스터디 가입 거절")
	public Object rejectJoinGroup(long reqNo, long gpNo, @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();

		ckGroupAuth(userId, gpNo);
		groupService.rejectJoinGroup(reqNo);

		BasicResponse result = new BasicResponse();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/remove")
	@ApiOperation("스터디 퇴출")
	public Object removeGroupMember(long joinNo, long gpNo, @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();

		ckGroupAuth(userId, gpNo);
		groupService.removeGroupMember(joinNo);

		BasicResponse result = new BasicResponse();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/exit")
	@ApiOperation("그룹 나가기")
	public Object exitGroup(long gpNo, @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse result = new BasicResponse();

		if (!groupService.ckGroupJoin(gpNo, userId)) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}

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

	// GROUP TAG******************************************

	public void ckGroupAuth(long userId, long gpNo) {
		if (!isGroupMgr(userId, gpNo))
			throw new GroupUnAuthException();
	}

	public boolean isGroupMgr(long userId, long gpNo) {
		long mgrId = groupService.selectGroup(gpNo).getGpMgrId();

		return userId == mgrId ? true : false;
	}

	// EXCPETION **********************************
	@ExceptionHandler(GroupUnAuthException.class)
	public Object unAuthExceptionHandler(Exception e) {
		ErrorResponse result = new ErrorResponse();
		result.error = "Unauthorized about group";
		result.status = 403;
		result.msg = "해당 그룹에 대한 권한이 없습니다";

		return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
	}

}
