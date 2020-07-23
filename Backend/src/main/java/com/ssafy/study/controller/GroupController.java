package com.ssafy.study.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.group.exception.NoAuthException;
import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupDto;
import com.ssafy.study.group.model.GroupSearch;
import com.ssafy.study.group.service.GroupService;
import com.ssafy.study.util.JwtUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

//@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
@RequestMapping("/study")
public class GroupController {
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private GroupService groupService;

	@GetMapping("/all")
	@ApiOperation("전체 스터디 ")
	public Object allStudyList() {
		ResponseEntity response;

		BasicResponse result = new BasicResponse();
		result.object = groupService.findAllGroups();
		result.msg = "success";
		result.status = true;

		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@GetMapping("/my")
	@ApiOperation("로그인한 회원의 스터디 목록 조회")
	public Object findMyStudyList(@RequestHeader("Authorization") String jwt) {
		ResponseEntity response;

		String userEmail = jwtUtil.getUsernameFromToken(jwt);
		BasicResponse result = new BasicResponse();
		result.object = groupService.findMyGroups(userEmail);
		result.msg = "success";
		result.status = true;

		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@PostMapping("/")
	@ApiOperation("스터디 생성")
	public Object createStudy(@RequestBody @Valid GroupDto.RegistGroup group) {
		ResponseEntity response;

		System.out.println(group);
		System.out.println(group.toEntity());
		groupService.saveGroup(group.toEntity());
		BasicResponse result = new BasicResponse();
		result.msg = "success";
		result.status = true;

		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@GetMapping("/{no}")
	@ApiOperation("스터디 상세 조회")
	public Object selectStudyNo(@PathVariable long no) {
		ResponseEntity response;

		BasicResponse result = new BasicResponse();
		result.object = groupService.selectGroup(no);
		result.msg = "success";
		result.status = true;

		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@PutMapping("/{no}")
	@ApiOperation("스터디 수정")
	public Object modifytudy(@RequestBody GroupDto.ModifyGroup modifyGroup) {
		ResponseEntity response;
		BasicResponse result = new BasicResponse();

		String userid = "admin";
		ckAuth(userid, modifyGroup.getGpNo());
		Group group = groupService.selectGroup(modifyGroup.getGpNo()).get();
		group.update(modifyGroup);
		groupService.saveGroup(group);

		result.msg = "success";
		result.status = true;

		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@DeleteMapping("/{no}")
	@ApiOperation("스터디번호로 스터디 삭제")
	public Object deleteStudy(@PathVariable long no) {
		ResponseEntity response;
		BasicResponse result = new BasicResponse();

		ckAuth("admin", no);
		groupService.deleteGroup(no);
		result.msg = "success";
		result.status = true;

		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@GetMapping("/search")
	@ApiOperation("스터디 검색")
	public Object searchStudy(@RequestBody GroupSearch groupSearch) {

		return null;
	}

	@ExceptionHandler(NoAuthException.class)
	public @ResponseBody Object NoAuthExceptionHandler(Exception e) {
		ResponseEntity response;
		BasicResponse result = new BasicResponse();
		result.msg = "권한이 없습니다";
		result.status = false;

		response = new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		return response;
	}

	public void ckAuth(String userId, long gpNo) {
		String mgrId = groupService.selectGroup(gpNo).get().getGpMgrId();

		if (!userId.equals(mgrId))
			throw new NoAuthException();
	}

}
