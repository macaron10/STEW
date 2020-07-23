//package com.ssafy.study.user.service;
//
//import java.util.Collections;
//
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.ssafy.study.exception.UserNotFoundException;
//import com.ssafy.study.user.model.UserDetail;
//import com.ssafy.study.user.repository.UserRepository;
//
//import lombok.AllArgsConstructor;
//
//@AllArgsConstructor
//
//// Service("name") 반대편에서 Autowired 대신 Resource("name")으로 가져옴
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService{
//	
//	private UserRepository userRepository;
//
//	@Override
//	public UserDetail loadUserByUsername(String userEmail) throws UsernameNotFoundException {
//		return userRepository.findByUserEmail(userEmail)
//				.map(u -> new UserDetail(u, Collections.singleton(new SimpleGrantedAuthority(u.getRole().getValue()))))
//				.orElseThrow(() -> new UserNotFoundException(userEmail));
//	}
//
//}
