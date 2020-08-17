package com.ssafy.study.util;

public class JwtProperties {
	private static final int SECOND = 1000;
	private static final int MINUTE = 60 * SECOND;
	private static final int HOUR = 60 * MINUTE;
	public static final String SECRET = "ssafy-study";
	public static final int EXPIRATION_TIME_ACCESS = 5 * HOUR;
	public static final int EXPIRATION_TIME_REFRESH = 30 * HOUR; 
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
}
