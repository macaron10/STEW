package com.ssafy.study.notification.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.ssafy.study.chat.model.ChatMessage;
import com.ssafy.study.chat.repository.ChatRoomRepository;
import com.ssafy.study.notification.model.Notification;
import com.ssafy.study.notification.repository.NotiRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotiService {

	private final ChannelTopic notiChannelTopic;
	private final RedisTemplate redisTemplate;
	private final NotiRepository notiRepo;

	public void sendNotification(Notification noti) {
		noti = notiRepo.saveNotification(noti);
		redisTemplate.convertAndSend(notiChannelTopic.getTopic(), noti);
	}

	public void deleteNoti(long userId, long notiNo) {
		notiRepo.deleteNotification(userId, notiNo);
	}

	public List<Notification> getNotiList(long userId) {
		return notiRepo.getUserNotification(userId);
	}

}
