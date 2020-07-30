package com.ssafy.study.user.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@Valid
public class UserSignIn {	
	@ApiModelProperty(required = true)
	@NotNull
	private String userEmail;
	@ApiModelProperty(required = true)
	@NotNull
	private String userPw;
}
