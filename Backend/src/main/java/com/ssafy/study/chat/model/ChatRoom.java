package com.ssafy.study.chat.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ssafy.study.user.model.UserDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatRoom implements Serializable {

	private static final long serialVersionUID = 10000L;

	private String gpNo;
	private long userCount;
	private Map<String, UserDto> userList;

	public ChatRoom(String gpNo) {
		this.gpNo = gpNo;
		this.userCount = 0;
		this.userList = new HashMap<String, UserDto>();
	}

	public void minusCount() {
		userCount--;
	}

	public void addCount() {
		userCount++;
	}

//	public static ChatRoom create() {
//		ChatRoom chatRoom = new ChatRoom();
//		chatRoom.roomId = UUID.randomUUID().toString();
//
//		return chatRoom;
//	}

}
