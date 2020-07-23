package com.ssafy.study.user.model;

import lombok.Data;

@Data
public class UserSignInRequest {	
	private String userEmail;
	private String userPw;
}
