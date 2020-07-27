package com.ssafy.study.exception;

public class JwtNotFoundException extends RuntimeException{
	
	public JwtNotFoundException() {
		super("JWT not Found");
	}

}
