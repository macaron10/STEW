package com.ssafy.study.config.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ssafy.study.common.exception.JwtNotFoundException;
import com.ssafy.study.common.exception.UserNotFoundException;
import com.ssafy.study.util.JwtProperties;
import com.ssafy.study.util.JwtUtil;

public class JwtLogoutHandler extends SecurityContextLogoutHandler{

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String accessToken = request.getHeader(JwtProperties.HEADER_STRING);
		
//		if(request.getCookies() != null) {
//			for(Cookie c : request.getCookies()) {
//				if(c.getName().equals("accessToken")) accessToken = c.getValue();
//			}
//		}
		
		if(accessToken == null 
				|| !accessToken.startsWith(JwtProperties.TOKEN_PREFIX)
				) throw new JwtNotFoundException();
//		else accessToken = accessToken.replace(JwtProperties.TOKEN_PREFIX, "");
		
		if(!JwtUtil.verify(accessToken))
//			try {
//				response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid Token");
				throw new TokenExpiredException("Token Expired");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		
		accessToken = accessToken.replace(JwtProperties.TOKEN_PREFIX, "");
		
		String userEmail = JwtUtil.getUsernameFromToken(accessToken);
		
		String userToken = (String) redisTemplate.opsForValue().get(userEmail);
		if(userToken == null) throw new UserNotFoundException(userEmail);
		
		super.logout(request, response, authentication);
	}
	
}
