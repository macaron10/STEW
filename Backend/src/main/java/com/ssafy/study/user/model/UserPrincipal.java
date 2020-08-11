package com.ssafy.study.user.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.annotations.ApiModelProperty;

public class UserPrincipal implements UserDetails{
	
	@ApiModelProperty(hidden = true)
	private User user;

	public UserPrincipal(User user) {
		this.user = user;
	}

	private List<String> arrToList(String input) {
		if (input == null || input.length() == 0)
			return new ArrayList<>();

		return Arrays.asList(input.split(","));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();

//		arrToList(this.user.getPermissions()).forEach(p -> {
//			GrantedAuthority authority = new SimpleGrantedAuthority(p);
//			authorities.add(authority);
//		});

		arrToList(this.user.getRoles()).forEach(p -> {
			GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + p);
			authorities.add(authority);
		});

		return authorities;
	}

	@Override
	public String getPassword() {
		return this.user.getUserPw();
	}

	@Override
	public String getUsername() {
		return this.user.getUserEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.user.isEnable();
	}

	public long getUserId() {
		return user.getUserId();
	}

	public String getUserNm() {
		return user.getUserNm();
	}

	public String getUserImg() {
		return user.getUserImg();
	}

	public String getType() {
		return user.getType();
	}
	
}
