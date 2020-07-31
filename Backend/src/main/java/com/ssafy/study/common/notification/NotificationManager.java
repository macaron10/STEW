package com.ssafy.study.common.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ssafy.study.common.model.Attachment;

@Component
public class NotificationManager {
	private Logger log = LoggerFactory.getLogger(NotificationManager.class);
	private final MatterMostSender mmSender;

	public NotificationManager(MatterMostSender mmSender) {
		this.mmSender = mmSender;
	}

	public void sendNotification(Exception e) {
		log.info("#### send Notification.");
		Attachment attach = new Attachment(e);
		mmSender.sendMessage(attach);
		System.out.println(attach);
	}

}
