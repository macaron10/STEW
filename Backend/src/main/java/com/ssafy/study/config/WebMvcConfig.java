package com.ssafy.study.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.study.config.filter.HeaderFilter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Bean
	public FilterRegistrationBean getFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(new HeaderFilter());
		registrationBean.setOrder(Integer.MAX_VALUE);
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}
}
