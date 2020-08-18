package com.ssafy.study.notification.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.util.MethodInvocationRecorder.Recorded.ToCollectionConverter;
import org.springframework.stereotype.Repository;

import com.ssafy.study.notification.model.Notification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class NotiRepository {
	private static final String NOTI_NO = "NOTI_NO";
	private static final String NOTIFICATION = "NOTI";

	@Resource(name = "redisTemplate")
	private HashOperations<String, Long, Notification> hashOpsNoti;
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOps;

	public Notification saveNotification(Notification notification) {
		notification.setNotiNo(Optional.ofNullable(valueOps.increment(NOTI_NO)).orElse(1L));

		hashOpsNoti.put(NOTIFICATION + "_" + notification.getUserId(), notification.getNotiNo(), notification);
		return notification;
	}

	public long deleteNotification(long userId, long notiNo) {
		long result = 0;
		if (hashOpsNoti.hasKey(NOTIFICATION + "_" + userId, notiNo))
			result = hashOpsNoti.delete(NOTIFICATION + "_" + userId, notiNo);
		return result;
	}

	public List<Notification> getUserNotification(long userId) {
		System.out.println(userId);
		System.out.println(hashOpsNoti.entries(NOTIFICATION + "_" + userId).values().stream().collect(Collectors.toList()));
		return hashOpsNoti.entries(NOTIFICATION + "_" + userId).values().stream().collect(Collectors.toList());
	}

}
