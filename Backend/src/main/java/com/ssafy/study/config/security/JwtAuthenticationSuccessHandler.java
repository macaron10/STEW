package com.ssafy.study.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
		
		String refreshToken = JwtUtil.generateRefreshToken();
		String accessToken = JwtUtil.generateAccessToken(userPrincipal);
		
		redisTemplate.opsForValue().set(userPrincipal.getUsername(), refreshToken);
		redisTemplate.expire(userPrincipal.getUsername(), JwtProperties.EXPIRATION_TIME_REFRESH, TimeUnit.MILLISECONDS);
		
		SecurityContextHolder.getContext().setAuthentication(authResult);
		
		response.addHeader("accessToken", JwtProperties.TOKEN_PREFIX + accessToken);
		response.addHeader("refreshToken", JwtProperties.TOKEN_PREFIX + refreshToken);
	}
	
}
