package com.ssafy.study.user.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.ssafy.study.enums.UserRole;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Entity
public class User implements Serializable{
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
	
	@Column(nullable = false, length = 50)
	private String userPhone;
	
	@Column(nullable = false)
	private char userGender;
	
	@Column(nullable = false, length = 15)
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	@Column(length = 200)
	private String userIntro;
	
	@Column(length = 100)
	private String userImg;
	
	private int userGoalHr;
	
	private Date userRegDate;
	
	
	@Builder
	public User(String userEmail, String userPw) {
		this.userEmail = userEmail;
		this.userPw = userPw;
	}
	
	@Setter
	@Transient
	private boolean isEnable = true;
	
}