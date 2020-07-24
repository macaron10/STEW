package com.ssafy.study.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
@RequiredArgsConstructor
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final UserPrincipalDetailsService userPrincipalDetailsService;
	private final UserRepository userRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.formLogin().disable()
//			.addFilter(jwtAuthenticationFilter(authenticationManager()))
//			.addFilter(new JwtAuthorizationFilter(authenticationManager(), this.userRepository, this.redisTemplate))
			.addFilter(jwtAuthenticationFilter(authenticationManager()))
			.logout()
			.logoutUrl("/user/logout")
			.addLogoutHandler(jwtLogoutHandler())
			.and()
			.authorizeRequests()
			.antMatchers("/swagger-ui.html").permitAll()
			.and()
			.authorizeRequests()
			.antMatchers("/api/manager/*").hasRole("MANAGER")
			.and()
			.authorizeRequests()
			.antMatchers("/api/admin/*").hasRole("ADMIN")
			.and()
			.authorizeRequests()
			.anyRequest().permitAll()
			.and()
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.and()
			.addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), this.userRepository, this.redisTemplate), UsernamePasswordAuthenticationFilter.class);
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
