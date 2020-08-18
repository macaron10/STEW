package com.ssafy.study.notification.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {

	private static final long serialVersionUID = 1000000L;

	public enum NotiType {
		INFO, ACCEPT, REJECT
	}

	private NotiType type;
	private long notiNo;
	private long userId;
	private String msg;
	private String url;

	private String regTime;

	public Notification(NotiType type, long userId, String msg, String url) {
		super();
		this.type = type;
		this.userId = userId;
		this.msg = msg;
		this.url = url;

		regTime = LocalDateTime.now().toString();
	}

	public Notification(NotiType type, long userId, String msg) {
		super();
		this.type = type;
		this.userId = userId;
		this.msg = msg;

		regTime = LocalDateTime.now().toString();
	}

	public Notification(long userId, String msg) {
		super();
		this.type = NotiType.INFO;
		this.userId = userId;
		this.msg = msg;

		regTime = LocalDateTime.now().toString();
	}

}
