package com.ssafy.study.config.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.study.user.model.UserSignInRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException{
		if(!request.getMethod().equals("POST")) 
			throw new AuthenticationServiceException("Authentication method not supported " + request.getMethod());
		
		UserSignInRequest credentials = null;
		
		try {
			credentials = new ObjectMapper().readValue(request.getInputStream(), UserSignInRequest.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					credentials.getUserEmail(),
					credentials.getUserPw(),
					new ArrayList<>()
				);
		
		return authenticationManager.authenticate(authToken);
	}
	
//	@Override
//	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
//			Authentication authResult) throws IOException, ServletException {
//		
//		
//		
//	}
//	
//	@Bean
//	RedisTemplate<String, Object> redisTemplate() {
//		return new RedisTemplate<String, Object>();
//	}
}
