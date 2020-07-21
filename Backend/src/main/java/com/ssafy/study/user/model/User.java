package com.ssafy.study.user.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;// auto increase 유저아이디

	private String userEmail;// 이메일
	private String userNm; // 이름
	private String userPw;// 비밀번호
	private String userPhone;// 연락처
	private char userGender;// 성별
	private String userIntro;// 자기소개
	private String userImg;// 프로필이미지 경로
	private int userGoalHr;// 하루목표시간
	private Date userRegDate;// 가입일

}
