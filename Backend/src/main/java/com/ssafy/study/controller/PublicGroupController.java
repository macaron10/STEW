package com.ssafy.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.common.util.FileUtils;
import com.ssafy.study.group.model.dto.GroupSearchDto;
import com.ssafy.study.group.service.GroupService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
public class PublicGroupController {
	@Autowired
	private GroupService groupService;

	@Autowired
	private FileUtils fileUtil;

	private final String fileBaseUrl = "C:\\Users\\multicampus\\Desktop\\group_thumb";

	private final SimpMessagingTemplate template2;

	@GetMapping("/all")
	@ApiOperation("전체 스터디 ")
	public ResponseEntity allStudyList() {
		BasicResponse result = new BasicResponse();
		result.object = groupService.selectAllGroups();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/search")
	@ApiOperation("스터디 검색")
	public Object searchStudy(String keyword) {
		BasicResponse result = new BasicResponse();

		System.out.println(keyword);
		GroupSearchDto groupSearch = new GroupSearchDto(keyword.split(" "));
		result.object = groupService.searchGroups(groupSearch);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

//	@GetMapping("/cate/lg")
//	@ApiOperation("카테고리 대분류 출력")
//	public Object selectBoxLg() {
//		BasicResponse result = new BasicResponse();
//		result.object = groupService.selectBoxLgGroupCategory();
//		result.msg = "success";
//		result.status = true;
//
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
//
//	@GetMapping("/cate/md")
//	@ApiOperation("카테고리 중분류 출력")
//	public Object selectBoxMd(String lg) {
//		BasicResponse result = new BasicResponse();
//		result.object = groupService.selectBoxMdGroupCategory(lg);
//		result.msg = "success";
//		result.status = true;
//
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
//
//	@GetMapping("/cate/sm")
//	@ApiOperation("카테고리 소분류 출력")
//	public Object selectBoxSm(String lg, String md) {
//		BasicResponse result = new BasicResponse();
//		result.object = groupService.selectBoxSmGroupCategory(lg, md);
//		result.msg = "success";
//		result.status = true;
//
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}

	@GetMapping("/")
	@ApiOperation("스터디 목록 페이지네이션 출력")
	public ResponseEntity paginationStudyList(@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "12") int size) {
		BasicResponse result = new BasicResponse();

		Pageable pageable = PageRequest.of(page, size, Sort.by("regDate").descending());

		result.object = groupService.selectAllGroups(pageable);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/cate")
	@ApiOperation("전체 카테고리 출력")
	public ResponseEntity selectBoxGroupCategory() {
		BasicResponse result = new BasicResponse();
		result.object = groupService.selecBoxAllGroupCategory();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/rank")
	@ApiOperation("스터디 랭크 출력")
	public ResponseEntity rankStudyTime() {
		BasicResponse result = new BasicResponse();
		result.object = groupService.rankGroupStudyTime();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
