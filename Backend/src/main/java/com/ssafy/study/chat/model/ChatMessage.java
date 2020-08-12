package com.ssafy.study.chat.model;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
	private SimpleDateFormat sdf;

	public enum MessageType {
		ENTER, TALK, QUIT
	}

	private MessageType type;
	private long gpNo;
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
}
