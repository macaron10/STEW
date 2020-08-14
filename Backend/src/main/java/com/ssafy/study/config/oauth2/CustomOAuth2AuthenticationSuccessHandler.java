package com.ssafy.study.config.oauth2;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.ssafy.study.user.model.User;
import com.ssafy.study.user.model.UserPrincipal;
import com.ssafy.study.user.service.UserService;
import com.ssafy.study.util.BaseProperties;
import com.ssafy.study.util.JwtProperties;
import com.ssafy.study.util.JwtUtil;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	
	@NonNull
	private RedisTemplate<String, Object> redisTemplate;
	
	@NonNull
	private UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();	
		System.out.println("oauth2User");
		System.out.println(oauth2User);
		
		Map<String, Object> userAttributes = oauth2User.getAttributes();
		
		String provider = (String) userAttributes.get("provider");
		System.out.println("provider");
		System.out.println(provider);
		
		String userEmail = null;
		String userNm = null;
		
		if(provider.equals("kakao")) {
			Map<String, Object> kakaoAccount = (LinkedHashMap) userAttributes.get("kakao_account");
			userEmail = (String) kakaoAccount.get("email");
			userNm = (String) ((LinkedHashMap) kakaoAccount.get("profile")).get("nickname");
		}else if(provider.equals("google")) {
			userEmail = (String) userAttributes.get("email");
			userNm = (String) userAttributes.get("name");
		}else if(provider.equals("facebook")) {
			userEmail = (String) userAttributes.get("email");
			userNm = (String) userAttributes.get("name");
		}
		
		User user = userService.findByUserEmailAndType(userEmail, provider);
		
		if(user == null) {
			user = User.builder()
					.userEmail(userEmail)
					.userPw("")
					.userNm(userNm)
					.type(provider)
					.build();
			
			user.setUserImg(BaseProperties.DEFAULT_USER_PROFILE);
			userService.save(user);
		}
		
		UserPrincipal principal = new UserPrincipal(user);
		
		String accessToken = JwtUtil.generateAccessToken(principal);
		String refreshToken = JwtUtil.generateRefreshToken();
		String refreshKey = JwtUtil.getRefreshKey(accessToken);
		
		redisTemplate.opsForValue().set(refreshKey, refreshToken);
		redisTemplate.expire(refreshKey, JwtProperties.EXPIRATION_TIME_REFRESH, TimeUnit.MILLISECONDS);
		
		response.sendRedirect(BaseProperties.BASE_DEPLOY_URL + "/#/oauth2" + "?email=" + userEmail + "&accessToken=" + accessToken + "&refreshToken=" + refreshToken);
		
	}
	
}
