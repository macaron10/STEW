package com.ssafy.study.chat.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

	public enum MessageType {
		ENTER, TALK, QUIT
	}

	private MessageType type;
	private long gpNo;
	private long userId;
	private String userNm;
	private String userImg;
	private String chatMsg;

	private LocalDateTime regTime;
}
