package com.ssafy.study.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ssafy.study.common.exception.FileUploadException;
import com.ssafy.study.common.model.ErrorResponse;
import com.ssafy.study.common.notification.NotificationManager;
import com.ssafy.study.group.model.exception.GroupFullException;
import com.ssafy.study.group.model.exception.GroupNotExistException;
import com.ssafy.study.group.model.exception.GroupNotJoinedExcpetion;
import com.ssafy.study.group.model.exception.GroupUnAuthException;

@ControllerAdvice
public class ExceptionController {
	@Autowired
	private NotificationManager notificationManager;

	@ExceptionHandler(GroupUnAuthException.class)
	public ResponseEntity unAuthExceptionHandler(Exception e) {
		ErrorResponse result = new ErrorResponse();
		result.error = "Unauthorized";
		result.status = false;
		result.msg = "해당 그룹에 대한 권한이 없습니다";

		return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(GroupNotJoinedExcpetion.class)
	public ResponseEntity groupNotJoinHandler(Exception e) {
		ErrorResponse result = new ErrorResponse();
		result.error = "Not Joined";
		result.status = false;
		result.msg = "해당 그룹에 가입한 회원이 아닙니다";

		return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(GroupNotExistException.class)
	public ResponseEntity groupNotExistHandler(Exception e) {
		ErrorResponse result = new ErrorResponse();
		result.error = "Not Exist";
		result.status = false;
		result.msg = "존재하지 않는 그룹입니다";

		return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(GroupFullException.class)
	public ResponseEntity groupFullHandler(Exception e) {
		ErrorResponse result = new ErrorResponse();
		result.status = false;
		result.msg = "정원 초과입니다!";

		return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
	}

	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity groupFileUploadHandler(Exception e) {
		ErrorResponse result = new ErrorResponse();
		result.status = false;
		result.msg = "사진 첨부에 실패하셨습니다";

		return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
	}

	@ExceptionHandler(Exception.class)
	public void exceptionTest(Exception e, HttpServletRequest req) {
		e.printStackTrace();
		notificationManager.sendNotification(e, req.getRequestURI());
	}
}
