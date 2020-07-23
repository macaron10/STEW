//package com.ssafy.study.user.model;
//
//import java.util.Collection;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.experimental.Delegate;
//
//@AllArgsConstructor
//@Getter
//public class UserDetail implements UserDetails{
//	
//	@Delegate
//	private User userDto;
//	private Collection<? extends GrantedAuthority> authorities;
//	
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return authorities;
//	}
//
//	@Override
//	public String getPassword() {
//		return userDto.getUserPw();
//	}
//	
//	@Override
//	public String getUsername() {
//		return userDto.getUserEmail();
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		return userDto.isEnable();
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return userDto.isEnable();
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return userDto.isEnable();
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return userDto.isEnable();
//	}
//	
//}
