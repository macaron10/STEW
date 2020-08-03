package com.ssafy.study.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ssafy.study.common.notification.NotificationManager;

@ControllerAdvice
public class ExceptionController {
	@Autowired
	private NotificationManager notificationManager;
	
	@ExceptionHandler(IOException.class)
	public void ioExceptionHandle(Exception e) {
		e.printStackTrace();
		
		
	}

	@ExceptionHandler(Exception.class)
	public void exceptionTest(Exception e, HttpServletRequest req) {
		e.printStackTrace();
		notificationManager.sendNotification(e, req.getRequestURI());
	}

}
