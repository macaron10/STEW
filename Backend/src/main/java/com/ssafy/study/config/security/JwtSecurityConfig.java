package com.ssafy.study.config.security;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ssafy.study.config.oauth2.CustomOAuth2Provider;
import com.ssafy.study.user.service.CustomOAuth2UserService;
import com.ssafy.study.user.service.UserPrincipalDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final UserPrincipalDetailsService userPrincipalDetailsService;
	
	@Autowired
	private JwtAuthorizationFilter jwtAuthorizationFilter;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.cors()
			.and()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			
			.formLogin()
				.disable()
				
			.logout()
				.logoutUrl("/user/logout")
				.addLogoutHandler(jwtLogoutHandler())
				.logoutSuccessHandler(jwtLogoutSuccessHandler())
			.and()
			
			.authorizeRequests()
				.antMatchers(permittedPaths()).permitAll()
				.antMatchers("/manager/**").hasRole("MANAGER")
				.antMatchers("/admin/**").hasAnyRole("ADMIN")
				.anyRequest().authenticated()
			.and()
			
//			그게그거임 이거 넣지말고 기본 필터로 ㄱㄱ?
			.addFilterBefore(jwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
			
			.and()
				.oauth2Login()
				.userInfoEndpoint().userService(new CustomOAuth2UserService()); // for naver user info 
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setPasswordEncoder(passwordEncoder());
		daoProvider.setUserDetailsService(this.userPrincipalDetailsService);
		
		return daoProvider;
	}
	
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
		return new JwtAuthenticationSuccessHandler();
	}
	@Bean
	JwtLogoutHandler jwtLogoutHandler() {
		JwtLogoutHandler jwtLogoutHandler = new JwtLogoutHandler();
		jwtLogoutHandler.setClearAuthentication(false);
		
		return jwtLogoutHandler;
	}
	
	@Bean
	JwtLogoutSuccessHandler jwtLogoutSuccessHandler() {
		return new JwtLogoutSuccessHandler();
	}
	
	private JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
		JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(authenticationManager);
		authenticationFilter.setPostOnly(true);
		authenticationFilter.setFilterProcessesUrl("/user/signin");
		authenticationFilter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler());
		
		return authenticationFilter;
	}

	
	@Bean
    public ClientRegistrationRepository clientRegistrationRepository(
            OAuth2ClientProperties oAuth2ClientProperties,
            @Value("${custom.oauth2.kakao.client-id}") String kakaoClientId
            ,@Value("${custom.oauth2.kakao.client-secret}") String kakaoClientSecret
//            ,@Value("${custom.oauth2.naver.client-id}") String naverClientId
//            ,@Value("${custom.oauth2.naver.client-secret}") String naverClientSecret
            ) {
		
        List<ClientRegistration> registrations = oAuth2ClientProperties
                .getRegistration().keySet().stream()
                .map(client -> getRegistration(oAuth2ClientProperties, client))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                    .clientId(kakaoClientId)
                    .clientSecret(kakaoClientSecret)
                    .jwkSetUri("temp")
                    .build());

//        registrations.add(CustomOAuth2Provider.NAVER.getBuilder("naver")
//                .clientId(naverClientId)
//                .clientSecret(naverClientSecret)
//                .jwkSetUri("temp")
//                .build());
        
        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties, String client) {
        if("google".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("google");
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .scope("email", "profile")
                    .build();
        }

        if("facebook".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("facebook");
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,link")
                    .scope("email")
                    .build();
        }

        return null;
    }
	
	
	
	@Override
    public void configure(WebSecurity web) throws Exception {
		
//		swagger ignore authenticating
        web.ignoring().antMatchers("/v2/api-docs",
                                   "/configuration/ui",
                                   "/swagger-resources/**",
                                   "/configuration/security",
                                   "/swagger-ui.html",
                                   "/webjars/**");
    }
	private String[] permittedPaths() {
		return new String[] {
				"/",
				"/study/*",
				"/study/thumb/**",
				"/study/cate/*",
				"/user/signin",
				"/user/signup",
				"/user/check",
//				리프레쉬토큰만 멀쩡하면 허용.. 하는게 맞나?
				"/user/refresh",
				"/login/oauth2/**"
		};
	}
	
}
