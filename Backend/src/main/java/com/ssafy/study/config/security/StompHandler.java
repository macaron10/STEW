package com.ssafy.study.config.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.ssafy.study.chat.model.ChatMessage;
import com.ssafy.study.chat.repository.ChatRoomRepository;
import com.ssafy.study.chat.service.ChatService;
import com.ssafy.study.common.exception.StompJwtExcpetion;
import com.ssafy.study.user.model.UserDto;
import com.ssafy.study.util.JwtProperties;
import com.ssafy.study.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
	private final ChatRoomRepository chatRoomRepo;
	private final ChatService chatService;

	private Map<String, UserDto> sessionMap = new HashMap<>();

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		System.out.println(accessor.getCommand());
		System.out.println(message);

		if (StompCommand.CONNECT == accessor.getCommand()) {
			if (!JwtUtil.verify(accessor.getFirstNativeHeader("accessToken"))) {
				throw new StompJwtExcpetion();
			}
			UserDto user = JwtUtil.getUserFromToken(
					accessor.getFirstNativeHeader("accessToken").replace(JwtProperties.TOKEN_PREFIX, ""));
			String sessionId = (String) message.getHeaders().get("simpSessionId");
			sessionMap.put(sessionId, user);

			return message;
		}
		if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
			if (((String) message.getHeaders().get("simpDestination")).contains("chat")) {
				String gpNo = chatService.getGpNo(Optional
						.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
				String sessionId = (String) message.getHeaders().get("simpSessionId");

				chatRoomRepo.setUserEnterInfo(sessionId, gpNo);
				UserDto user = sessionMap.remove(sessionId);
				chatRoomRepo.addUser(gpNo, sessionId, user);

				chatService.sendChatMessage(new ChatMessage(user, gpNo, ChatMessage.MessageType.ENTER));

				log.info("SUBSCRIBED {}, {}", sessionId, gpNo);

				return message;
			}
		}
		if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
			String sessionId = (String) message.getHeaders().get("simpSessionId");
			if (sessionMap.containsKey(sessionId)) {
				sessionMap.remove(sessionId);
			} else {
				String roomId = chatRoomRepo.getUserEnterRoomId(sessionId);
				UserDto user = chatRoomRepo.exitUser(roomId, sessionId);
				chatRoomRepo.removeUserEnterInfo(sessionId);

				chatService.sendChatMessage(new ChatMessage(user, roomId, ChatMessage.MessageType.QUIT));
				log.info("DISCONNECTED {}, {}", sessionId, roomId);
			}
		}

		return message;
	}

}
