package com.ssafy.study.config.security;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import com.ssafy.study.util.JwtProperties;
import com.ssafy.study.util.JwtUtil;

public class JwtLogoutSuccessHandler extends HttpStatusReturningLogoutSuccessHandler{

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {
		
		String accessToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
		
		long remains = JwtUtil.getExpiringTime(accessToken) - System.currentTimeMillis();
		
//		BlackListing
		redisTemplate.opsForValue().set(accessToken, "logout");
		redisTemplate.expire(accessToken, remains, TimeUnit.MILLISECONDS);
		
//		Delete RefreshToken
		redisTemplate.delete(JwtUtil.getUsernameFromToken(accessToken));
	}
}
