package com.ssafy.study.oauth2;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoOauth2User implements OAuth2User {

	@JsonProperty("kakao_account")
    private Map<String, Object> kakaoAccount;
    private Map<String, Object> properties;
	
    private List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
    private String id;
	
	@Override
	public Map<String, Object> getAttributes() {
		Map<String, Object> attributes = new HashMap<>();
        attributes.putAll(kakaoAccount);
        attributes.putAll(properties);
        return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getName() {
		return this.id;
	}
	
	

}
