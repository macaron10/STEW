package com.ssafy.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jca.context.SpringContextResourceAdapter;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ssafy.study.group.service.GroupService;
import com.ssafy.study.user.model.UserPrincipal;
import com.ssafy.study.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class SocketController {

	private final SimpMessagingTemplate template;


//	@MessageMapping("/receive") // receive를 받을 endpoint, prefix적용시 /pub/recieve, 클라이언트가 서버에 메세지를 전송
//	@SendTo("/send") // 전달하려고하는 곳의 subscribe, 서버가 해당 주소로 클라이언트에게 메세지를 전송
//	public String sendGpReqNotifiction() {
//		UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//		long userId = principal.getUserId();
//
//		System.out.println("******************************************");
//		String str = userId + " : " + "HIHI";
//		System.out.println(str);
//		// System.out.println(test);
////		template.convertAndSend("/sub/send", str);
//
//		return str;
//	}

}
