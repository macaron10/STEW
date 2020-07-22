package com.ssafy.study.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupDto;
import com.ssafy.study.group.model.GroupSearch;
import com.ssafy.study.group.service.GroupService;

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
	private GroupService gpService;

	@GetMapping("/all")
	@ApiOperation("전체 스터디 ")
	public Object allStudyList() {
		ResponseEntity response;

		BasicResponse result = new BasicResponse();
		result.object = gpService.findAllGroups();
		result.msg = "success";
		result.status = true;

		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@GetMapping("/my")
	@ApiOperation("회원의 스터디 목록 조회")
	public Object findMyStudyList() {
		ResponseEntity response;

		BasicResponse result = new BasicResponse();
		result.object = gpService.findMyGroups("userid");
		result.msg = "success";
		result.status = true;

		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@PostMapping("/")
	@ApiOperation("스터디 생성")
	public Object createStudy(@RequestBody @Valid GroupDto.RegistGroup group) {
		ResponseEntity response;

		gpService.createGroup(group.toEntity());
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
		result.object = gpService.selectGroup(no);
		result.msg = "success";
		result.status = true;

		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@PutMapping("/{no}")
	@ApiOperation("스터디 수정")
	public Object modifytudy(@RequestBody GroupDto.ModifyGroup group) {
		ResponseEntity response;

		gpService.modifyGroup(group.toEntity());
		BasicResponse result = new BasicResponse();
		result.msg = "success";
		result.status = true;

		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	@DeleteMapping("/{no}")
	@ApiOperation("스터디번호로 스터디 삭제")
	public Object deleteStudy(@PathVariable long no) {
		ResponseEntity response;

		gpService.deleteGroup(no);
		BasicResponse result = new BasicResponse();
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

	@ExceptionHandler(Exception.class)
	public @ResponseBody String groupExceptionHandler(Exception e) {
		return "group controller expcetion";
	}

}
