package com.ssafy.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ssafy.study.group.service.GroupService;
import com.ssafy.study.user.model.UserPrincipal;

import springfox.documentation.annotations.ApiIgnore;

@RestController
public class SocketController {

	private SimpMessagingTemplate template;
	@Autowired
	private GroupService groupService;

	@MessageMapping("/receive") // receive를 받을 endpoint, prefix적용시 /pub/recieve
	@SendTo("/send") // /send로 메시지 반환, 전달하려고하는 곳의 subscribe
	public String sendGpReqNotifiction(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
		long userId = principal.getUserId();

		return new Gson().toJson(groupService.selectGroupReq(userId));
	}

}
