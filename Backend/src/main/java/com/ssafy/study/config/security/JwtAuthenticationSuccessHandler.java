package com.ssafy.study.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.ssafy.study.user.model.UserPrincipal;
import com.ssafy.study.user.model.UserToken;
import com.ssafy.study.util.JwtProperties;
import com.ssafy.study.util.JwtUtil;

public class JwtAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws ServletException, IOException {
		UserPrincipal userPrincipal = (UserPrincipal) authResult.getPrincipal();
		
		List<String> authorities = new ArrayList<>();
		
		for(GrantedAuthority p : userPrincipal.getAuthorities()) {
			authorities.add(p.getAuthority());
		}
		
		String tokenKey = userPrincipal.getUsername() + "#" + userPrincipal.getType();
		
		UserToken userToken = (UserToken) redisTemplate.opsForValue().get(tokenKey);
		
		if(userToken != null) {
//			원래 액세스 토큰 로그아웃
			redisTemplate.opsForValue().set(userToken.getAccessToken(), new UserToken());
			redisTemplate.expire(userToken.getAccessToken(), JwtUtil.getExpiringTime(userToken.getAccessToken()) - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
			
		}
		userToken = new UserToken(JwtUtil.generateAccessToken(userPrincipal), JwtUtil.generateRefreshToken());
		
		redisTemplate.opsForValue().set(tokenKey, userToken);
		redisTemplate.expire(tokenKey, JwtProperties.EXPIRATION_TIME_REFRESH, TimeUnit.MILLISECONDS);
		
		SecurityContextHolder.getContext().setAuthentication(authResult);
		
		response.addHeader("accessToken", JwtProperties.TOKEN_PREFIX + userToken.getAccessToken());
		response.addHeader("refreshToken", JwtProperties.TOKEN_PREFIX + userToken.getRefreshToken());
		
	}
	
}
