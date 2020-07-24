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
		
		String accessToken = JwtUtil.generateAccessToken(userPrincipal);
		String refreshToken = JwtUtil.generateRefreshToken(userPrincipal);
		
		UserToken userToken = new UserToken(userPrincipal.getUsername(), refreshToken);
		
		redisTemplate.opsForValue().set(userToken.getUsername(), userToken);
		redisTemplate.expire(userToken.getUsername(), JwtProperties.EXPIRATION_TIME_REFRESH, TimeUnit.MILLISECONDS);
		
		SecurityContextHolder.getContext().setAuthentication(authResult);
		
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
	}
	
}
