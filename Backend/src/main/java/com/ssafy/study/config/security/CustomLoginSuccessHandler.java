package com.ssafy.study.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.ssafy.study.user.model.UserDetail;
import com.ssafy.study.user.model.UserToken;
import com.ssafy.study.util.JwtUtil;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		logger.info("Login Succeed");
		
		String accessToken = jwtUtil.generateAccessToken((UserDetail)userDetailsService.loadUserByUsername(authentication.getName()));
		String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());
		
		logger.info("AccessToken : " + accessToken);
		logger.info("RefreshToken : " + refreshToken);
		
		UserToken uToken = new UserToken();
		
		uToken.setRefreshToken(refreshToken);
		uToken.setUsername(jwtUtil.getUsernameFromToken(refreshToken));
		
		redisTemplate.opsForValue().set(uToken.getUsername(), uToken);
		
		response.setHeader("Authorization", accessToken);
	}
	
}
