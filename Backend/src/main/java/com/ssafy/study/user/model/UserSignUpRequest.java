package com.ssafy.study.user.model;

import lombok.Builder;
import lombok.Data;

@Data
public class UserSignUpRequest {
	private String userEmail;
	private String userPw;
	private char userGender;
	private String roles;
	private String userNm;

	public User toEntity() {
		return
				User.builder()
				.userEmail(this.userEmail)
				.userGender(this.userGender)
				.userNm(this.userNm)
				.userPw(this.userPw)
				.roles(this.roles)
				.build();
	}
}
