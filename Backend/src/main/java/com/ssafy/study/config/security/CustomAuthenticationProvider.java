package com.ssafy.study.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ssafy.study.user.model.UserPrincipal;
import com.ssafy.study.user.service.UserPrincipalDetailsService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider{

	@Autowired
	private UserPrincipalDetailsService userDetailsService;
	@NonNull
	private PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		
		String userEmail = token.getName();
		String userPw = (String) token.getCredentials();
		
		UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUserEmailAndType(userEmail, "stew");
		
		if (!passwordEncoder.matches(userPw, userPrincipal.getPassword())) {
		    throw new BadCredentialsException(userPrincipal.getUsername() + "Invalid password");
		}

		return new UsernamePasswordAuthenticationToken(userPrincipal, userPw, userPrincipal.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}