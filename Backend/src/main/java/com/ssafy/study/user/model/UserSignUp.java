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
	
	private String userIntro;
	private String userImg;
	private int userGoalHr;
	
	public User toEntity() {
		return
				User.builder()
				.userEmail(this.userEmail)
				.userNm(this.userNm)
				.userPw(this.userPw)
				.userGoalHr(this.userGoalHr)
				.userImg(this.userImg)
				.userIntro(this.userIntro)
				.build();
	}
}
