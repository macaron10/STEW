package com.ssafy.study.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ssafy.study.user.model.UserDetail;
import com.ssafy.study.user.model.UserToken;
import com.ssafy.study.util.JwtUtil;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		UsernamePasswordAuthenticationToken authRequest = 
				new UsernamePasswordAuthenticationToken(request.getParameter("userEmail"), request.getParameter("userPw"));
		setDetails(request, authRequest);
		
		return this.getAuthenticationManager().authenticate(authRequest);
		
	}

}
