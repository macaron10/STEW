package com.ssafy.study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.ssafy.study.config.security.StompHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	private final StompHandler stompHandler;

	@Override
	// 클라이언트가 메시지를 구독할 endpoint 정의
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//메모리 기반 메세지 브로커가 해당 api를 구독하고 있는 클라이언트에게 메시지 전달
		registry.enableSimpleBroker("/sub");
		
		//서버에서 클라이언트로부터의 메세지를 받을 api의 prefix
		registry.setApplicationDestinationPrefixes("/pub");
	}

	@Override
	// connection시 CORS 허용
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/sock").setAllowedOrigins("*").withSockJS();
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompHandler);
	}

}
