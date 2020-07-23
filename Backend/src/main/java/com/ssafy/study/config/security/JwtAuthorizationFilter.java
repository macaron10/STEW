package com.ssafy.study.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ssafy.study.user.model.User;
import com.ssafy.study.user.model.UserPrincipal;
import com.ssafy.study.user.repository.UserRepository;
import com.ssafy.study.util.JwtProperties;
import com.ssafy.study.util.JwtUtil;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

	private UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(JwtProperties.HEADER_STRING);
		
		if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		Authentication authentication = getUsernamePasswordAuthentication(request);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(request, response);
	}
	
	private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
		String token = request.getHeader(JwtProperties.HEADER_STRING);
		
		if(token == null) return null;
		
		try {
			JwtUtil.verify(token);
		} catch (TokenExpiredException expiredException) {
			System.out.println("에에에 만료됐대요");
			
			
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String userEmail = JwtUtil.getUsernameFromToken(token); 
		
		if(userEmail == null) return null;
		
		User user = this.userRepository.findByUserEmail(userEmail).get();
		
		UserPrincipal principal = new UserPrincipal(user);
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userEmail, null, principal.getAuthorities());
		
		return auth;
	}
}
