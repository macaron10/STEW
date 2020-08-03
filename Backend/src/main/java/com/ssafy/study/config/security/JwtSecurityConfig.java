package com.ssafy.study.config.security;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
				
			.oauth2Login()
			.and()
			
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
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
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
		JwtLogoutHandler jwtLogoutHandler = new JwtLogoutHandler();
		jwtLogoutHandler.setClearAuthentication(false);
//		jwtLogoutHandler.setInvalidateHttpSession(true);
		
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
				"/test",
				"/user/signin",
				"/user/signup",
				"/study/*",
				"/study/thumb/**",
				"/study/cate/*",
				"/user/check",
//				리프레쉬토큰만 멀쩡하면 허용.. 하는게 맞나?
				"/user/refresh"
		};
	}
	
}
