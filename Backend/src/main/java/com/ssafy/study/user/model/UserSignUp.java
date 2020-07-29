package com.ssafy.study.user.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@Valid
public class UserSignUp {
	@ApiModelProperty(required = true)
	@NotNull
	private String userNm;
	@ApiModelProperty(required = true)
	private String userEmail;
	@ApiModelProperty(required = true)
	private String userPw;
	@ApiModelProperty(required = true)
	private char userGender;
	@ApiModelProperty(required = true)
	private String roles;
	
	private String userPhone;
	private String userIntro;
	private String userImg;
	private int userGoalHr;
	
	public User toEntity() {
		return
				User.builder()
				.userEmail(this.userEmail)
				.userGender(this.userGender)
				.userNm(this.userNm)
				.userPw(this.userPw)
				.roles(this.roles)
				.userGoalHr(this.userGoalHr)
				.userImg(this.userImg)
				.userIntro(this.userIntro)
				.userPhone(this.userPhone)
				.build();
	}
}
