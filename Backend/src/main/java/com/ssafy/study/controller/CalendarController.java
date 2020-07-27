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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.calendar.model.CalDto;
import com.ssafy.study.calendar.model.CalEvent;
import com.ssafy.study.calendar.service.CalendarService;
import com.ssafy.study.common.exception.NoAuthException;
import com.ssafy.study.common.model.BasicResponse;
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
@RequestMapping("/cal")
public class CalendarController {
	@Autowired
	private CalendarService calService;
	@Autowired
	private GroupService gpService;

	@PostMapping("/")
	@ApiOperation("일정 생성")
	public Object createCalEvt(@Valid CalDto.createCal cal, @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse response = new BasicResponse();

		if (cal.getCType() != 'U') {
			long gpMgrId = gpService.selectGroup(cal.getCOwn()).getGpMgrId();
			if (gpMgrId != principal.getUserId())
				throw new NoAuthException();
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
	@ApiOperation("일정 수정")
	public Object modifyCalEvt(@Valid CalDto.modifyCal cal, @AuthenticationPrincipal UserPrincipal principal) {
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
	public Object deleteCalEvt(@PathVariable long no, @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse response = new BasicResponse();

		ckAuth(no, userId);
		calService.deleteCalEvt(no);
		response.msg = "success";
		response.status = true;

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/personal")
	@ApiOperation("개인 일정 조회")
	public Object personalCalList(@AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse response = new BasicResponse();

		response.object = calService.selectPersonalCalEvt(userId);
		response.msg = "success";
		response.status = true;

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/group")
	@ApiOperation("가입한 그룹들의 일정 조회")
	public Object groupCalList(@AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();
		BasicResponse response = new BasicResponse();

		response.object = calService.selectGroupCalEvt(userId);
		response.msg = "success";
		response.status = true;

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void ckAuth(long calNo, long userId) {
		CalEvent cal = calService.selectCalNo(calNo);
		if (cal.getCType() == 'U')
			return;

		long gpMgrId = gpService.selectGroup(cal.getCOwn()).getGpMgrId();
		if (gpMgrId != userId)
			throw new NoAuthException();
	}

	@ExceptionHandler(NoAuthException.class)
	public Object noAuthExceptionHandler() {
		BasicResponse res = new BasicResponse();

		res.msg = "no auth";
		res.status = false;

		return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
	}
}
