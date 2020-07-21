package com.ssafy.study.group.model;

import java.sql.Date;

import lombok.Getter;

@Getter
public class GroupResponseDto {

	private long gpNo; // 스터디 아이디
	private int gpCatNo;// 타입 아이디
	private String gpNm;// 스터디 이름
	private String gpMgrId;// 팀장아이디
	private String gpIntro;// 소개
	private String gpTag;// 태그
	private int gpStTm;// 선호 시작시간
	private int gpEndTm;// 선호 종료시간
	private boolean gpPublic;// 공개여부
	private String gpImg;// 썸네일
	private int gpMaxNum;// 최대인원
	private int gpCurNum;// 현재인원
	private Date gpRegDate;// 등록일
}
