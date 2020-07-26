package com.ssafy.study.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ssafy.study.user.repository.UserRepository;
import com.ssafy.study.user.service.UserPrincipalDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final UserPrincipalDetailsService userPrincipalDetailsService;
	private final UserRepository userRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private JwtAuthorizationFilter jwtAuthorizationFilter;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.cors().and()
			.csrf().disable()
			.formLogin().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
//			.logout()
//			.logoutUrl("/user/logout")
//			.addLogoutHandler(jwtLogoutHandler())
			.authorizeRequests()
			.antMatchers("/api/manager/**").hasRole("MANAGER")
			.antMatchers("/api/admin/**").hasAnyRole("ADMIN")
			
//			.antMatchers("/v2/**").permitAll()
//			.antMatchers("/swagger-resources/**").permitAll()
//			.antMatchers("/webjars/**").permitAll()
//			.antMatchers("/swagger-ui.html/**").permitAll()
			
			.anyRequest().permitAll()
			.and()
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.and()	
			.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
			
//			그게그거임 이거 넣지말고 기본 필터로 
			.addFilterBefore(jwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
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
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
		return new JwtAuthenticationSuccessHandler();
	}
	@Bean
	JwtLogoutHandler jwtLogoutHandler() {
		return new JwtLogoutHandler();
	}
	
//	@Bean
//	CustomLogoutSuccessHandler customLogoutSuccessHandler() {
//		return new CustomLogoutSuccessHandler();
//	}
	
	private JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
		JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(authenticationManager);
		authenticationFilter.setPostOnly(true);
		authenticationFilter.setFilterProcessesUrl("/user/signin");
		authenticationFilter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler());
		
		return authenticationFilter;
	}
	
}
