package com.ssafy.study.config.oauth2;

import java.util.LinkedHashMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class KakaoOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService{

	@Override
	public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId,
			String principalName) {

		return null;
		
	}

	@Override
	public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
		
		String provider = authorizedClient.getClientRegistration().getRegistrationId();
		
		OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
		
		System.out.println("인증 액세스 토큰 : " + accessToken.getTokenValue());
		
		OAuth2User oauth2User = (OAuth2User) principal.getPrincipal();
		
		System.out.println("인증 정보");
		System.out.println(oauth2User.getAttributes());
		
		
		
		
	}

	@Override
	public void removeAuthorizedClient(String clientRegistrationId, String principalName) {

		return;
		
	}
	
	

}
