package com.ssafy.study.common.exception;

public class JwtNotFoundException extends RuntimeException{
	
	public JwtNotFoundException() {
		super("JWT not Found");
	}

}
