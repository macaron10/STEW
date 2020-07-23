package com.ssafy.study.util;

public class JwtProperties {
	private static final int SECOND = 1000;
	public static final String SECRET = "ssafy-study";
	public static final int EXPIRATION_TIME_ACCESS = 60 * 1 * SECOND;
	public static final int EXPIRATION_TIME_REFRESH = 60 * 5 * SECOND; 
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
}
