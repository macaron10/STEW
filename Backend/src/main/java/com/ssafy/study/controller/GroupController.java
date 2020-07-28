package com.ssafy.study.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.study.common.exception.GroupNoAuthException;
import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.common.service.FileService;
import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupDto;
import com.ssafy.study.group.model.GroupSearch;
import com.ssafy.study.group.service.GroupService;
import com.ssafy.study.user.model.User;
import com.ssafy.study.user.model.UserPrincipal;
import com.ssafy.study.user.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@RestController
@RequestMapping("/study")
public class GroupController {

	@Autowired
	private GroupService groupService;
	@Autowired
	private UserService userSerivce;
	@Autowired
	private FileService fileService;

	private final String fileBaseUrl = "C:\\Users\\multicampus\\Desktop\\group_thumb";

	@GetMapping("/all")
	@ApiOperation("전체 스터디 ")
	public Object allStudyList() {
		BasicResponse result = new BasicResponse();
		result.object = groupService.findAllGroups();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

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
	@ApiOperation("스터디 생성")
	public Object createStudy(@AuthenticationPrincipal UserPrincipal principal, GroupDto.RegistGroup group) {
		Group saveGroup = group.toEntity();
		saveGroup.setGpMgrId(principal.getUserId());

		if (group.getGpImg() != null) {
			saveGroup.setGpImg(fileService.uploadFile(group.getGpImg(), fileBaseUrl));
		}

		saveGroup = groupService.saveGroup(saveGroup);

		User user = userSerivce.loadUserByUserId(saveGroup.getGpMgrId());
		groupService.joinGroup(user, saveGroup);

		BasicResponse result = new BasicResponse();
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

		result.object = groupService.selectGroup(no);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping("/{no}")
	@ApiOperation("스터디 수정")
	public Object modifytudy(GroupDto.ModifyGroup modifyGroup, @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse result = new BasicResponse();

		long userId = principal.getUserId();
		ckAuth(userId, modifyGroup.getGpNo());
		
		System.out.println(modifyGroup.getGpImg());
		if (modifyGroup.getGpImg() != null) {
			modifyGroup.setGpImgName(fileService.uploadFile(modifyGroup.getGpImg(), fileBaseUrl));
		}

		Group group = groupService.selectGroup(modifyGroup.getGpNo());
		group.update(modifyGroup);
		groupService.saveGroup(group);

		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@DeleteMapping("/{no}")
	@ApiOperation("스터디번호로 스터디 삭제")
	public Object deleteStudy(@PathVariable long no, @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse result = new BasicResponse();

		long userId = principal.getUserId();
		ckAuth(userId, no);
		groupService.deleteGroup(no);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/search")
	@ApiOperation("스터디 검색")
	public Object searchStudy(GroupSearch groupSearch) {
		BasicResponse result = new BasicResponse();

		result.object = groupService.searchGroups(groupSearch);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/req")
	@ApiOperation("스터디에 가입요청")
	public Object reqJoinGroup(int gpNo, @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse result = new BasicResponse();

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

		ckAuth(userId, gpNo);
		groupService.acceptJoinGroup(reqNo);

		BasicResponse result = new BasicResponse();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/reject")
	@ApiOperation("스터디 가입 거절")
	public Object rejectJoinGroup(long reqNo, long gpNo, @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();

		ckAuth(userId, gpNo);
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

		ckAuth(userId, gpNo);
		groupService.removeGroupMember(joinNo);

		BasicResponse result = new BasicResponse();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/cate/lg")
	@ApiOperation("카테고리 대분류 출력")
	public Object selectBoxLg() {
		BasicResponse result = new BasicResponse();
		result.object = groupService.selectBoxLgGroupCategory();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/cate/md")
	@ApiOperation("카테고리 중분류 출력")
	public Object selectBoxMd(String lg) {
		BasicResponse result = new BasicResponse();
		result.object = groupService.selectBoxMdGroupCategory(lg);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/cate/sm")
	@ApiOperation("카테고리 소분류 출력")
	public Object selectBoxSm(String lg, String md) {
		BasicResponse result = new BasicResponse();
		result.object = groupService.selectBoxSmGroupCategory(lg, md);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@ExceptionHandler(GroupNoAuthException.class)
	public @ResponseBody Object NoAuthExceptionHandler(Exception e) {
		BasicResponse result = new BasicResponse();
		result.msg = "권한이 없습니다";
		result.status = false;

		return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
	}

	public void ckAuth(long userId, long gpNo) {
		long mgrId = groupService.selectGroup(gpNo).getGpMgrId();

		if (userId != mgrId)
			throw new GroupNoAuthException();
	}

	@PostMapping("/test")
	public void test(GroupDto.RegistGroup group) {
		System.out.println(group);
//		System.out.println(group.getGpImg());
	}

}
