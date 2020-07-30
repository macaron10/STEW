package com.ssafy.study.user.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ssafy.study.common.model.TimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class User extends TimeEntity{
	
	@Builder
	public User(String userNm, String userEmail, String userPw,
			String userIntro, String userImg, int userGoalHr) {
		this.userNm = userNm;
		this.userEmail = userEmail;
		this.setUserPw(userPw);
		this.userIntro = userIntro;
		this.userImg = userImg;
		this.userGoalHr = userGoalHr;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private long userId;

	@Column(nullable = false, length = 15)
	private String userNm;

	@Column(nullable = false, unique = true, length = 128)
	private String userEmail;

	@Column(nullable = false, length = 128)
	private String userPw;

	public void setUserPw(String userPw) {
		this.userPw = new BCryptPasswordEncoder().encode(userPw);
	}
	
	@Setter
	@Column(nullable = false)
	private String roles = "USER";

	@Setter
	@Column(length = 200)
	private String userIntro;

	@Setter
	@Column(length = 100)
	private String userImg;

	@Setter
	private int userGoalHr;

	@Setter
	@Transient
	private boolean isEnable = true;

}