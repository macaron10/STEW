package com.ssafy.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ssafy.study.common.notification.NotificationManager;

@ControllerAdvice
public class ExceptionController {
	@Autowired
	private NotificationManager notificationManager;

	@ExceptionHandler(Exception.class)
	public void exceptionTest(Exception e) {
		e.printStackTrace();
		notificationManager.sendNotification(e);
	}

}
