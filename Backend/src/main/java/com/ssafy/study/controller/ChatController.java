package com.ssafy.study.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.chat.model.ChatMessage;
import com.ssafy.study.chat.model.ChatRoom;
import com.ssafy.study.chat.repository.ChatRoomRepository;
import com.ssafy.study.chat.service.ChatService;
import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.user.model.UserDto;
import com.ssafy.study.util.JwtProperties;
import com.ssafy.study.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessageSendingOperations template;
	private final RedisTemplate redisTemplate;
	private final ChannelTopic channelTopic;
	private final ChatRoomRepository chatRoomRepo;
	private final ChatService chatService;

	@MessageMapping("/chat")
	public void sendMessage(@Header("accessToken") String jwt, ChatMessage msg) {
		UserDto user = JwtUtil.getUserFromToken(jwt.replace(JwtProperties.TOKEN_PREFIX, ""));
		msg.setUserId(user.getUserId());
		msg.setUserNm(user.getUserNm());
		msg.setUserImg(user.getUserImg());

		chatService.sendChatMessage(msg);
	}

	@GetMapping("/chatroom-info/{gpNo}")
	public ResponseEntity getChatRoomInfo(@PathVariable String gpNo) {
		ChatRoom chatRoom = chatRoomRepo.findRoomById(gpNo);

		BasicResponse response = new BasicResponse();
		response.object = chatRoom;
		response.msg = "success";
		response.status = true;

		return new ResponseEntity(response, HttpStatus.OK);
	}

}
