package com.ssafy.study.config.security;

import java.io.IOException;
import java.util.ArrayList;

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
		
		if(token == null || !token.startsWith(JwtProperties.TOKEN_PREFIX) || redisTemplate.opsForValue().get(token.replace(JwtProperties.TOKEN_PREFIX, "")) != null) {
			chain.doFilter(request, response);
			return;
		}
		
		Authentication authentication = getUsernamePasswordAuthentication(response, token);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(request, response);
	}
	
    @Bean
    public FilterRegistrationBean JwtRequestFilterRegistration (JwtAuthorizationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
	
	private Authentication getUsernamePasswordAuthentication(HttpServletResponse response, String token) {
		if(token == null || !token.startsWith(JwtProperties.TOKEN_PREFIX)) return null;
		
		String userEmail = JwtUtil.getUsernameFromToken(token.replace(JwtProperties.TOKEN_PREFIX, ""));
		if(userEmail == null) return null;
		
		UserPrincipal userPrincipal = getUserPrincipalByUserEmail(userEmail);
		
		try {
			JwtUtil.verify(token);
		} catch (TokenExpiredException accessExpiredException) {
			logger.error("AccessToken Expired");
			UserToken userToken = (UserToken) redisTemplate.opsForValue().get(userEmail);
			
			try {
				JwtUtil.verify(userToken.getRefreshToken());
				
				response.setHeader(
						JwtProperties.HEADER_STRING,
						JwtProperties.TOKEN_PREFIX + JwtUtil.generateAccessToken(userPrincipal));
				logger.info("AccessToken Regenerated");
				
			} catch (TokenExpiredException refreshExpiredException) {
				logger.error("RefreshToken Expired");
				return null;
			} catch (NullPointerException e) {
				logger.error("RefreshToken Expired");
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		logger.info("Authorities : " + userPrincipal.getAuthorities());
		return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
	}
	
	private UserPrincipal getUserPrincipalByUserEmail(String userEmail) {
		return (UserPrincipal) this.userPrincipalDetailsService.loadUserByUsername(userEmail);
	}
}
