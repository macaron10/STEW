package com.ssafy.study.common.exception;

public class StompJwtExcpetion extends RuntimeException {
	public StompJwtExcpetion() {
		super("권한 없는 사용자의 소켓 요청");
	}
}
