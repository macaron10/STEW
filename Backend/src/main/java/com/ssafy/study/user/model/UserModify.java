package com.ssafy.study.user.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

@Getter
@Valid
@Data
public class UserModify {

	@NotNull
	private String userNm;
	private String userPw;
	
	private String userNewPw;
	private String userIntro;
	private MultipartFile userImg;
	private boolean updateImg;
	private int userGoalHr;
	
}
