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
import com.ssafy.study.notification.service.NotiService;
import com.ssafy.study.user.model.UserPrincipal;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/noti")
public class NotificationController {

	@Autowired
	private NotiService notiService;

	@GetMapping("/list")
	public ResponseEntity getNotification(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse res = new BasicResponse();

		res.object = notiService.getNotiList(principal.getUserId());
		res.msg = "success";
		res.status = true;

		System.out.println(res.object);
		return new ResponseEntity(res, HttpStatus.OK);
	}

	@PostMapping("/{notiNo}")
	public ResponseEntity deleteNoti(@PathVariable long notiNo,
			@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		BasicResponse res = new BasicResponse();
		notiService.deleteNoti(principal.getUserId(), notiNo);

		res.msg = "success";
		res.status = true;

		return new ResponseEntity(res, HttpStatus.OK);
	}

}
