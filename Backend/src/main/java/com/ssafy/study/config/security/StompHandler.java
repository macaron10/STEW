package com.ssafy.study.config.security;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ssafy.study.common.exception.StompJwtExcpetion;
import com.ssafy.study.util.JwtUtil;

@Component
public class StompHandler implements ChannelInterceptor {

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		if (StompCommand.CONNECT == accessor.getCommand()) {
			if(!JwtUtil.verify(accessor.getFirstNativeHeader("accessToken"))) {
				throw new StompJwtExcpetion();
			}
		}
		
		return message;
	}

}
