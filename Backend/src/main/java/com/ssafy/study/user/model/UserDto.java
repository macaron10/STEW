package com.ssafy.study.user.model;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	private long userId;
	private String userNm;
	private String userEmail;
	private String userIntro;
	private String userImg;
	private int userGoalHr;

	public UserDto(User user) {
		this.userId = user.getUserId();
		this.userNm = user.getUserNm();
		this.userEmail = user.getUserEmail();
		this.userIntro = user.getUserIntro();
		this.userImg = user.getUserImg();
		this.userGoalHr = user.getUserGoalHr();
	}
}
