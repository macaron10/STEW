package com.ssafy.study.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ssafy.study.user.model.UserPrincipal;
import com.ssafy.study.user.model.UserToken;
import com.ssafy.study.util.JwtProperties;
import com.ssafy.study.util.JwtUtil;

public class JwtLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws ServletException, IOException {
		
		UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
		
		List<String> authorities = new ArrayList<>();
		
		for(GrantedAuthority p : principal.getAuthorities()) {
			authorities.add(p.getAuthority());
		}
		
		String accessToken = JWT.create()
				.withArrayClaim("role", authorities.toArray(new String[authorities.size()]))
				.withSubject(principal.getUsername())
				.withIssuedAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_ACCESS))
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_ACCESS))
				.sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
		
		String refreshToken = JWT.create()
				.withSubject(principal.getUsername())
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_REFRESH))
				.sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
		
		
		UserToken userToken = new UserToken();
		userToken.setUsername(principal.getUsername());
		userToken.setRefreshToken(refreshToken);
		
		redisTemplate.opsForValue().set(userToken.getUsername(), userToken);
		
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
		
		System.out.println(JwtUtil.getAuthoritiesFromToken(accessToken));
	}
	
}
