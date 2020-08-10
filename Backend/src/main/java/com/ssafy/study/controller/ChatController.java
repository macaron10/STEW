package com.ssafy.study.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.chat.model.ChatMessage;
import com.ssafy.study.user.model.UserDto;
import com.ssafy.study.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate template;

	@MessageMapping("/chat")
	public void sendMessage(@Header("accessToken") String jwt,@RequestBody ChatMessage msg) {
		System.out.println(msg);
		UserDto user = JwtUtil.getUserFromToken(jwt);
		msg.setUserId(user.getUserId());
		msg.setUserNm(user.getUserNm());
		msg.setUserImg(user.getUserImg());

		msg.setRegTime(LocalDateTime.now());

		if (ChatMessage.MessageType.ENTER.equals(msg.getType()))
			msg.setChatMsg(msg.getUserNm() + "님이 입장하셨습니다.");
		else if (ChatMessage.MessageType.ENTER.equals(msg.getType()))
			msg.setChatMsg(msg.getUserNm() + "님이 퇴장하셨습니다.");

		template.convertAndSend("/sub/chat/" + msg.getGpNo(), msg);
	}
}
