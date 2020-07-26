package com.ssafy.study.common.exception;

public class NoResultException extends RuntimeException {
	String msg;

	public NoResultException(String msg) {
		this.msg = msg;
	}

}
