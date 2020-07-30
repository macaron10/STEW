package com.ssafy.study.user.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

@Getter
@Valid
@Data
public class UserModify {

	@ApiModelProperty(required = true)
	@NotNull
	private String userNm;
	@ApiModelProperty(required = true)
	private String userEmail;
	@ApiModelProperty(required = true)
	private String userPw;
	
	private String userNewPw;
	private String userIntro;
	private String userImg;
	private int userGoalHr;
	
	public User toEntity() {
		return
				User.builder()
				.userEmail(this.userEmail)
				.userNm(this.userNm)
				.userPw(this.userNewPw == null ? this.userNewPw : this.userPw)
				.userGoalHr(this.userGoalHr)
				.userImg(this.userImg)
				.userIntro(this.userIntro)
				.build();
	}
	
}
