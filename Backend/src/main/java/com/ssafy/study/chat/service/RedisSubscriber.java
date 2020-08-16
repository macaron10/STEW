package com.ssafy.study.chat.service;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.study.chat.model.ChatMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {
	private final ObjectMapper objMapper;
	private final SimpMessageSendingOperations messagingTemplate;

	public void sendMessage(String publishMessage) {
		try {
			ChatMessage chatMessage = objMapper.readValue(publishMessage, ChatMessage.class);

			messagingTemplate.convertAndSend("/sub/chat/" + chatMessage.getGpNo(), chatMessage);
		} catch (Exception e) {
			log.error("Exception {}", e);
		}

	}

}
