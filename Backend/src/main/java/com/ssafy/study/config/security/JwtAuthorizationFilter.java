package com.ssafy.study.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ssafy.study.user.model.UserPrincipal;
import com.ssafy.study.user.model.UserToken;
import com.ssafy.study.user.service.UserPrincipalDetailsService;
import com.ssafy.study.util.JwtProperties;
import com.ssafy.study.util.JwtUtil;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter{
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private UserPrincipalDetailsService userPrincipalDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = request.getHeader(JwtProperties.HEADER_STRING);
		
//		if(request.getCookies() != null) {
//			for(Cookie c : request.getCookies()) {
//				if(c.getName().equals("accessToken")) {
//					token = c.getValue();
//				}
//			}
//		}
		
//		토큰이 없는 경우
		if(token == null ||
				!token.startsWith(JwtProperties.TOKEN_PREFIX) ||
//				블랙리스트 
				((UserToken) redisTemplate.opsForValue().get(token.replace(JwtProperties.TOKEN_PREFIX, ""))) != null) {
			chain.doFilter(request, response);
			return;
		}else {
			try {
				JwtUtil.verify(token, request);
			}catch(TokenExpiredException e) {
				request.setAttribute("error", "token expired");
				chain.doFilter(request, response);
				return;
			}
		}
		
		token = token.replace(JwtProperties.TOKEN_PREFIX, "");
		
		Authentication authentication = getUsernamePasswordAuthentication(token);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(request, response);
	}
	
    @Bean
    public FilterRegistrationBean JwtRequestFilterRegistration (JwtAuthorizationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
	
	private Authentication getUsernamePasswordAuthentication(String token) {
		UserPrincipal userPrincipal = (UserPrincipal) userPrincipalDetailsService.loadUserByUserEmailAndType(
				JwtUtil.getUsernameFromToken(token),
				JwtUtil.getUserTypeFromToken(token)
				);
		
		return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
	}
	
	
}
