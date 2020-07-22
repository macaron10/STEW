package com.ssafy.study.user.model;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@Data
@RedisHash
public class UserToken implements Serializable{
	
	private String username;
	private String refreshToken;
	
}