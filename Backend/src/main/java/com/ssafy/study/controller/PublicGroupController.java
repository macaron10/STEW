package com.ssafy.study.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.common.util.FileUtils;
import com.ssafy.study.group.model.dto.GroupSearchDto;
import com.ssafy.study.group.service.GroupService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/study")
public class PublicGroupController {
	@Autowired
	private GroupService groupService;

	@Autowired
	private FileUtils fileUtil;

	private final String fileBaseUrl = "C:\\Users\\multicampus\\Desktop\\group_thumb";

	@GetMapping("/all")
	@ApiOperation("전체 스터디 ")
	public ResponseEntity allStudyList() {
		BasicResponse result = new BasicResponse();
		result.object = groupService.selectAllGroups();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/thumb/{year}/{month}/{date}/{file}", produces = MediaType.IMAGE_JPEG_VALUE)
	@ApiOperation("그룹의 썸네일 출력 <img src='http://localhost:8399/api/study/thumb/{gpImg}'>")
	public byte[] showThumbnail(@PathVariable String year, @PathVariable String month, @PathVariable String date,
			@PathVariable String file) {
		String path = File.separator + year + File.separator + month + File.separator + date + File.separator + file;
		byte[] img = {};
		try {
			img = fileUtil.downloadFile(fileBaseUrl, path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	@GetMapping(value = "/thumb/{path}", produces = MediaType.IMAGE_JPEG_VALUE)
	@ApiOperation("그룹의 썸네일 출력 <img src='http://localhost:8399/api/study/thumb/{gpImg}'>")
	public byte[] showThumbnailSinglePath(@PathVariable String path) {
		byte[] img = {};
		try {
			img = fileUtil.downloadFile(fileBaseUrl, File.separator + path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	@GetMapping("/search")
	@ApiOperation("스터디 검색")
	public Object searchStudy(GroupSearchDto groupSearch) {
		BasicResponse result = new BasicResponse();

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

	@GetMapping("/cate")
	@ApiOperation("전체 카테고리 출력")
	public ResponseEntity selectBoxGroupCategory() {
		BasicResponse result = new BasicResponse();
		result.object = groupService.selecBoxAllGroupCategory();
		result.msg = "success";
		result.status = true;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/test")
	public void test(int no) {
	}
}
