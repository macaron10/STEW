package com.ssafy.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.timeAcml.model.dto.addTimeAcmlDto;
import com.ssafy.study.timeAcml.service.TimeAcmlService;
import com.ssafy.study.user.model.UserPrincipal;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/timer")
public class TimeAcmlController {
	@Autowired
	private TimeAcmlService timeService;

	@GetMapping("/my/{year}/{month}")
	public ResponseEntity getdayTotal(@PathVariable int year, @PathVariable int month,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse result = new BasicResponse();

		result.object = timeService.selectUserTimerTotalDate(userId, year, month);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity(result, HttpStatus.OK);
	}

	@GetMapping("/my/{year}")
	public ResponseEntity getMonthTotal(@PathVariable int year,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse result = new BasicResponse();

		result.object = timeService.selectUserTimerTotalMonth(userId, year);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity(result, HttpStatus.OK);
	}

	@GetMapping("/{gpNo}/{year}/{month}")
	public ResponseEntity getdayTotalGp(@PathVariable long gpNo, @PathVariable int year, @PathVariable int month,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse result = new BasicResponse();

		result.object = timeService.selectGroupTimerTotalDate(gpNo, year, month);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity(result, HttpStatus.OK);
	}

	@GetMapping("/{gpNo}/{year}")
	public ResponseEntity getMonthTotalGp(@PathVariable long gpNo, @PathVariable int year,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse result = new BasicResponse();

		result.object = timeService.selectGroupTimerTotalMonth(gpNo, year);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity(result, HttpStatus.OK);
	}

	@PostMapping("")
	@ApiOperation("그룹에 공부 시간 추가")
	public ResponseEntity addTime(addTimeAcmlDto time, @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse result = new BasicResponse();

		timeService.saveTimer(userId, time);
		result.msg = "success";
		result.status = true;

		return new ResponseEntity(result, HttpStatus.OK);
	}

}
