package com.ssafy.study.user.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.study.user.model.User;
import com.ssafy.study.user.model.UserPrincipal;
import com.ssafy.study.user.repository.UserRepository;
import com.ssafy.study.util.JwtUtil;

@Service
public class UserPrincipalDetailsService implements UserDetailsService{

	private UserRepository userRepository;
	
	public UserPrincipalDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		
		User user = this.userRepository.findByUserEmail(userEmail).get();
		
		UserPrincipal userPrincipal = new UserPrincipal(user);
		
		return userPrincipal;
	}
	
	public UserDetails loadUserByUserEmailAndType(String userEmail, String type) throws UsernameNotFoundException{
		
		User user = userRepository.findByUserEmailAndType(userEmail, type);
		
		if(user == null) throw new UsernameNotFoundException("Email or Type Not Found");
		
		return new UserPrincipal(user);
		
	}
	
}
