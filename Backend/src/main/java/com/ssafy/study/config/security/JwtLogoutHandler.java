package com.ssafy.study.config.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ssafy.study.exception.JwtNotFoundException;
import com.ssafy.study.exception.UserNotFoundException;
import com.ssafy.study.user.model.UserToken;
import com.ssafy.study.util.JwtUtil;

public class JwtLogoutHandler extends SecurityContextLogoutHandler{

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String accessToken = null;
		
		if(request.getCookies() != null) {
			for(Cookie c : request.getCookies()) {
				if(c.getName().equals("accessToken")) accessToken = c.getValue();
			}
		}
		
		if(accessToken == null 
//				|| !accessToken.startsWith(JwtProperties.TOKEN_PREFIX)
				) throw new JwtNotFoundException();
//		else accessToken = accessToken.replace(JwtProperties.TOKEN_PREFIX, "");
		
		JwtUtil.verify(accessToken, request);
		
		String userEmail = JwtUtil.getUsernameFromToken(accessToken);
		
		UserToken userToken = (UserToken) redisTemplate.opsForValue().get(userEmail);
		if(userToken == null) throw new UserNotFoundException(userEmail);
		
		super.logout(request, response, authentication);
	}
	
}
