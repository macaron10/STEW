//package com.ssafy.study.oauth2;
//
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//
//
//public enum CustomOAuthProvider {
//	KAKAO{
//		@Override
//		public ClientRegistration.Builder getBuilder(){
//			return getBuilder("kakao", ClientAuthenticationMethod.POST)
//					.scope("profile", "talk_message")
//					.authorizationUri("https://kauth.kakao.com/oauth/authorize")
//					.tokenUri("https://kauth.kakao.code/oauth/token")
//					.userInfoUri("https://kapi.kakao.com/v2/user/me")  // 유저 정보 조회 api
//					.clientId("d2a6c95bf8150a6bd64d03b08ae15d3f")
//					.clientSecret("796k9ibwcc8e0Pnhn8y5gjTBUoJb9mfh")
//					.userNameAttributeName("id") // userInfo API Response에서 얻어올 ID 프로퍼티
//                    .clientName("Kakao"); // spring 내에서 인식할 OAuth2 Provider Name
//		}
//	};
//	
//	private static final String BASE_URL = "http://localhost:8399/api";
//	private static final String REGISTRATION_ID = "d2a6c95bf8150a6bd64d03b08ae15d3f";
//	private static final String DEFAULT_LOGIN_REDIRECT_URL = "{baseUrl/login/oauth2/code/{registrationId}";
//	
//	protected final ClientRegistration.Builder getBuilder(String registrationId, ClientAuthenticationMethod method){
//		
//		ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
//		builder.clientAuthenticationMethod(method);
//		builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
//		builder.redirectUriTemplate(CustomOAuthProvider.DEFAULT_LOGIN_REDIRECT_URL);
//		
//		return builder;
//		
//	}
//	
//	public abstract ClientRegistration.Builder getBuilder();
//}
