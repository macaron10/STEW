package com.ssafy.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.ssafy.study.chat.service.RedisSubscriber;
import com.ssafy.study.user.model.UserToken;

@Configuration
public class RedisConfig {

	@Bean(name = "chatChannelTopic")
	public ChannelTopic chatChannelTopic() {
		return new ChannelTopic("chatroom");
	}

	@Bean(name = "notiChannelTopic")
	public ChannelTopic notiChannelTopic() {
		return new ChannelTopic("notification");
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();

		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));

//		객체를 Json 형태로 깨지지 않고 받기 위한 직렬화
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(UserToken.class));

		return redisTemplate;

	}

	@Bean
	public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter msgAdapter, ChannelTopic chatChannelTopic, MessageListenerAdapter notiAdapter,
			ChannelTopic notiChannelTopic) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();

		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(msgAdapter, chatChannelTopic);
		container.addMessageListener(notiAdapter, notiChannelTopic);

		return container;
	}

	@Bean("msgAdapter")
	public MessageListenerAdapter msgAdapter(RedisSubscriber subscriber) {
		return new MessageListenerAdapter(subscriber, "sendMessage");
	}

	@Bean("notiAdapter")
	public MessageListenerAdapter notiAdapter(RedisSubscriber subscriber) {
		return new MessageListenerAdapter(subscriber, "sendNotification");
	}

}
