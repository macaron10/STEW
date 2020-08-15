package com.ssafy.study.chat.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.study.user.model.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class ChatMessage {
	public enum MessageType {
		ENTER, TALK, QUIT
	}

	@JsonIgnore
	private SimpleDateFormat sdf;

	private MessageType type;
	private String gpNo;
	private long userId;
	private String userNm;
	private String userImg;
	private String chatMsg;

	private String regTime;

	public ChatMessage() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		this.regTime = sdf.format(date);
	}

	public ChatMessage(UserDto user) {
		this();
		this.userId = user.getUserId();
		this.userImg = user.getUserImg();
		this.userNm = user.getUserNm();
	}

	public ChatMessage(UserDto user, String gpNo, MessageType type) {
		this(user);

		this.gpNo = gpNo;
		this.type = type;
	}
}
