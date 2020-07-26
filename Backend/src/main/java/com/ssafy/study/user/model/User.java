package com.ssafy.study.user.model;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;

import com.ssafy.study.common.model.TimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class User extends TimeEntity implements Serializable {

	@Builder
	public User(String userEmail, String userPw, char userGender, String roles, String userNm) {
		this.userEmail = userEmail;
		this.userPw = userPw;
		this.userGender = userGender;
		this.roles = roles;
		this.userNm = userNm;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private long userId;

	@Column(nullable = false, length = 15)
	private String userNm;

	@Column(nullable = false, unique = true, length = 128)
	private String userEmail;

	@Setter
	@Column(nullable = false, length = 128)
	private String userPw;

	@Setter
	@Column(length = 15)
	private String userPhone;

	@Column(nullable = false)
	private char userGender;

	@Setter
	@Column(nullable = false)
	private String roles;

	@Setter
	private String permissions;

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