package com.ssafy.study.chat.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.ssafy.study.chat.model.ChatRoom;
import com.ssafy.study.user.model.UserDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
	private static final String CHAT_ROOMS = "CHAT_ROOM";
	private static final String USER_COUNT = "USER_COUNT";
	private static final String ENTER_INFO = "ENTER_INFO";

//	private final RedisMessageListenerContainer redisMessageListner;
//	private final RedisTemplate<String, Object> redisTemplate;

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, String> hashOpsEnterInfo;
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOps;

//	public List<ChatRoom> findAllChatRooms() {
//		return hashOpsChatRoom.values(CHAT_ROOMS);
//	}

	public ChatRoom findRoomById(String roomId) {
		if (!hashOpsChatRoom.hasKey(CHAT_ROOMS, roomId)) {
			ChatRoom chatRoom = new ChatRoom(roomId);
			hashOpsChatRoom.put(CHAT_ROOMS, roomId, chatRoom);
		}
		System.out.println(hashOpsChatRoom.get(CHAT_ROOMS, roomId));
		return hashOpsChatRoom.get(CHAT_ROOMS, roomId);
	}
//
//	public ChatRoom createChatRoom(String name) {
//		ChatRoom chatRoom = ChatRoom.create(name);
//		hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
//
//		return chatRoom;
//	}

	public void setUserEnterInfo(String sessionId, String roomId) {
		hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
	}

	public String getUserEnterRoomId(String sessionId) {
		return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
	}

	public void removeUserEnterInfo(String sessionId) {
		hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
	}

//	public long getUserCount(String roomId) {
//		return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
//	}
//
//	public long addUserCount(String roomId) {
//		return Long.valueOf(Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L));
//	}
//
//	public long minusUserCount(String roomId) {
//		return Long.valueOf(Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + roomId)).orElse(0L));
//	}

	public List<UserDto> addUser(String roomId, String sessionId, UserDto user) {
		if (!hashOpsChatRoom.hasKey(CHAT_ROOMS, roomId)) {
			ChatRoom chatRoom = new ChatRoom(roomId);
			hashOpsChatRoom.put(CHAT_ROOMS, roomId, chatRoom);
		}
		ChatRoom chatRoom = hashOpsChatRoom.get(CHAT_ROOMS, roomId);
		chatRoom.addCount();
		chatRoom.getUserList().put(sessionId, user);

		hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getGpNo(), chatRoom);

		return chatRoom.getUserList().keySet().stream().map(key -> {
			return chatRoom.getUserList().get(key);
		}).collect(Collectors.toList());
	}

	public UserDto exitUser(String roomId, String sessionId) {
		ChatRoom chatRoom = hashOpsChatRoom.get(CHAT_ROOMS, roomId);
		UserDto user = chatRoom.getUserList().remove(sessionId);
		chatRoom.minusCount();

		if (chatRoom.getUserCount() == 0)
			hashOpsChatRoom.delete(CHAT_ROOMS, chatRoom.getGpNo());
		else
			hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getGpNo(), chatRoom);

		return user;
	}

//	public void enterChatRoom(String roomId) {
//		ChannelTopic topic = topics.get(roomId);
//		if (topic == null) {
//			topic = new ChannelTopic(roomId);
//			redisMessageListner.addMessageListener(redisSubscriber, topic);
//			topics.put(roomId, topic);
//		}
//	}
//
//	public ChannelTopic getTopic(String roomId) {
//		return topics.get(roomId);
//	}
}
