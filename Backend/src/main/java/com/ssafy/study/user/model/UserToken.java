package com.ssafy.study.user.model;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@RedisHash
@AllArgsConstructor
@NoArgsConstructor
public class UserToken implements Serializable{
	
	private String accessToken;
	private String refreshToken;
	
}