package com.ssafy.study.chat.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.ssafy.study.chat.model.ChatMessage;
import com.ssafy.study.chat.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {

	private final ChannelTopic channelTopic;
	private final RedisTemplate redisTemplate;
	private final ChatRoomRepository chatRoomRepository;

	public String getGpNo(String destination) {
		int lastIndex = destination.lastIndexOf('/');
		if (lastIndex != -1)
			return destination.substring(lastIndex + 1);
		else
			return "";
	}

	public void sendChatMessage(ChatMessage msg) {

		if (ChatMessage.MessageType.ENTER.equals(msg.getType()))
			msg.setChatMsg(msg.getUserNm() + "님이 입장하셨습니다.");
		else if (ChatMessage.MessageType.QUIT.equals(msg.getType()))
			msg.setChatMsg(msg.getUserNm() + "님이 퇴장하셨습니다.");

		System.out.println(msg);
		redisTemplate.convertAndSend(channelTopic.getTopic(), msg);
	}

}
