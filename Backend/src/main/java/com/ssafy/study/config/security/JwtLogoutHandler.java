package com.ssafy.study.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ssafy.study.common.exception.JwtNotFoundException;
import com.ssafy.study.user.model.UserToken;
import com.ssafy.study.util.JwtProperties;
import com.ssafy.study.util.JwtUtil;

public class JwtLogoutHandler extends SecurityContextLogoutHandler{

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String accessToken = request.getHeader(JwtProperties.HEADER_STRING);
		
		if(accessToken == null 
				|| !accessToken.startsWith(JwtProperties.TOKEN_PREFIX)
				) throw new JwtNotFoundException();
//		else accessToken = accessToken.replace(JwtProperties.TOKEN_PREFIX, "");
		
		if(!JwtUtil.verify(accessToken))
				throw new TokenExpiredException("Token Expired");
		
		UserToken userToken = (UserToken) redisTemplate.opsForValue().get(JwtUtil.getRefreshKey(accessToken));
		
		super.logout(request, response, authentication);
	}
	
}
