package com.ssafy.study.user.model;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@RedisHash
@AllArgsConstructor
public class UserToken implements Serializable{
	
	private String username;
	private String refreshToken;
	
}