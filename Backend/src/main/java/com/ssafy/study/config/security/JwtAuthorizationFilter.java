package com.ssafy.study.config.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ssafy.study.user.model.UserPrincipal;
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
		
		String token = null;
		
		for(Cookie c : request.getCookies()) {
			if(c.getName().equals("accessToken")) {
				token = c.getValue();
			}
		}
		
		if(token == null ||
//				!token.startsWith(JwtProperties.TOKEN_PREFIX) || 
				redisTemplate.opsForValue().get(token.replace(JwtProperties.TOKEN_PREFIX, "")) != null) {
			chain.doFilter(request, response);
			return;
		}
		
		Authentication authentication = getUsernamePasswordAuthentication(request, response, token);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(request, response);
	}
	
    @Bean
    public FilterRegistrationBean JwtRequestFilterRegistration (JwtAuthorizationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
	
	private Authentication getUsernamePasswordAuthentication(HttpServletRequest request, HttpServletResponse response, String token) {
		String userEmail = JwtUtil.getUsernameFromToken(token);
		if(userEmail == null) return null;
		
		UserPrincipal userPrincipal = getUserPrincipalByUserEmail(userEmail);
		
		try {
			JwtUtil.verify(token);
		} catch (TokenExpiredException e) {
			try {
				response.sendError(HttpStatus.UNAUTHORIZED.value(), "Expired");
				logger.error("Token Expired");
			} catch (IOException e1) {
				
			}
		} catch(Exception e) {
			return null;
		}
		
		return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
	}
	
	private UserPrincipal getUserPrincipalByUserEmail(String userEmail) {
		return (UserPrincipal) this.userPrincipalDetailsService.loadUserByUsername(userEmail);
	}
}
