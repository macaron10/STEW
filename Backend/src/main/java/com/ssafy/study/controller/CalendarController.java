package com.ssafy.study.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.calendar.model.CalEvent;
import com.ssafy.study.calendar.model.CreateCalEvt;
import com.ssafy.study.calendar.model.ModifyCalEvt;
import com.ssafy.study.calendar.model.exception.CalNoAuthException;
import com.ssafy.study.calendar.service.CalendarService;
import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.group.model.exception.GroupNotJoinedExcpetion;
import com.ssafy.study.group.model.exception.GroupUnAuthException;
import com.ssafy.study.group.service.GroupService;
import com.ssafy.study.user.model.UserPrincipal;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/cal")
public class CalendarController {
	@Autowired
	private CalendarService calService;
	@Autowired
	private GroupService gpService;

	@PostMapping("/")
	@ApiOperation(value = "일정 생성 yyyy-MM-dd'T'HH:mm ex)2020-12-12T12:12")
	public Object createCalEvt(@RequestBody CreateCalEvt cal,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse response = new BasicResponse();

		if (cal.getCType() != 'U') {
			long gpMgrId = gpService.selectGroup(cal.getCOwn()).getGpMgrId();
			if (gpMgrId != principal.getUserId())
				throw new GroupUnAuthException();
		} else {
			cal.setCOwn(principal.getUserId());
		}

		CalEvent calEvt = calService.saveCalendar(cal.toEntity());
		response.object = calEvt;
		response.msg = "success";
		response.status = true;

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/")
	@ApiOperation("일정 수정 yyyy-MM-dd'T'HH:mm")
	public Object modifyCalEvt(@RequestBody @Valid ModifyCalEvt cal,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse response = new BasicResponse();

		CalEvent calEvt = cal.toEntity();
		ckAuth(cal.getCNo(), principal.getUserId());

		calEvt = calService.saveCalendar(cal.toEntity());
		response.object = calEvt;
		response.msg = "success";
		response.status = true;

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{no}")
	@ApiOperation("일정 삭제")
	public Object deleteCalEvt(@PathVariable long no, @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse response = new BasicResponse();

		ckAuth(no, userId);
		calService.deleteCalEvt(no);
		response.msg = "success";
		response.status = true;

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/personal")
	@ApiOperation("개인의 모든 일정 조회")
	public Object personalCalList(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse response = new BasicResponse();

		response.object = calService.selectPersonalCalEvt(userId);
		response.msg = "success";
		response.status = true;

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/group")
	@ApiOperation("가입한 그룹들의 모든 일정 조회")
	public Object groupCalList(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse response = new BasicResponse();

		response.object = calService.selectGroupCalEvt(userId);
		response.msg = "success";
		response.status = true;

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/personal/{year}/{month}")
	@ApiOperation("year + month 개인 일정 조회")
	public Object personalMonthCalList(@PathVariable int year, @PathVariable int month,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse response = new BasicResponse();

		response.object = calService.selectPersonalCalEvt(userId, year, month);
		response.msg = "success";
		response.status = true;

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/group/{year}/{month}")
	@ApiOperation("year + month 사용자가 가입한 그룹들의 일정 조회")
	public Object groupMonthCalList(@PathVariable int year, @PathVariable int month,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse response = new BasicResponse();

		response.object = calService.selectGroupCalEvt(userId, year, month);
		response.msg = "success";
		response.status = true;

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/group/{gpNo}/{year}/{month}")
	@ApiOperation("year + month 특정 그룹의 일정 조회")
	public Object groupGpMonthCalList(@PathVariable long gpNo, @PathVariable int year, @PathVariable int month,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();

		if (gpService.ckGroupJoin(gpNo, userId))
			throw new GroupNotJoinedExcpetion();

		BasicResponse response = new BasicResponse();

		response.object = calService.selectGroupCalEvtByGpNo(gpNo, year, month);
		response.msg = "success";
		response.status = true;

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void ckAuth(long calNo, long userId) {
		CalEvent cal = calService.selectCalNo(calNo);
		if (cal.getCType() == 'U' && cal.getCOwn() == userId)
			return;

		long gpMgrId = gpService.selectGroup(cal.getCOwn()).getGpMgrId();
		if (gpMgrId == userId)
			return;

		throw new CalNoAuthException();
	}

	@ExceptionHandler({ CalNoAuthException.class, GroupUnAuthException.class })
	public Object noAuthExceptionHandler() {
		BasicResponse res = new BasicResponse();

		res.msg = "해당 일정에 대한 권한이 없습니다!";
		res.status = false;

		return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
	}

}
