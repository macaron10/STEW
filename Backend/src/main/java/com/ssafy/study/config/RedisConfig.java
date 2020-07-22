package com.ssafy.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.ssafy.study.user.model.UserToken;


@Configuration
public class RedisConfig {
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory();
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(){
		
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		
//		객체를 Json 형태로 깨지지 않고 받기 위한 직렬화
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(UserToken.class));
		
		return redisTemplate;
		
	}
}
