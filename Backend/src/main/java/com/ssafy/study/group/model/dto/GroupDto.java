package com.ssafy.study.group.model.dto;

import com.ssafy.study.group.model.entity.Group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
	private long gpNo; // 스터디 아이디

	private int gpCatNo;// 타입 아이디
	private String gpNm;// 스터디 이름
	private long gpMgrId;// 팀장아이디
	private String gpIntro;// 소개
	private String gpTag;// 태그
	private int gpStTm;// 선호 시작시간
	private int gpEndTm;// 선호 종료시간
	private boolean gpPublic;// 공개여부
	private String gpImg;
	private int gpMaxNum;// 최대인원
	private int gpCurNum;// 현재인원
	
	public GroupDto(Group group) {
		this.gpNo = group.getGpNo();
		this.gpCatNo = group.getGpCat().getGpCatNo();
		this.gpNm = group.getGpNm();
		this.gpMgrId = group.getGpMgrId();
		this.gpIntro = group.getGpIntro();
		this.gpStTm = group.getGpStTm();
		this.gpEndTm = group.getGpEndTm();
		this.gpPublic = group.isGpPublic();
		this.gpMaxNum = group.getGpMaxNum();
		this.gpCurNum = group.getGpCurNum();
		this.gpImg = group.getGpImg();
	}

	public GroupDto(Group group, String gpTag) {
		this.gpNo = group.getGpNo();
		this.gpCatNo = group.getGpCat().getGpCatNo();
		this.gpNm = group.getGpNm();
		this.gpMgrId = group.getGpMgrId();
		this.gpIntro = group.getGpIntro();
		this.gpStTm = group.getGpStTm();
		this.gpEndTm = group.getGpEndTm();
		this.gpPublic = group.isGpPublic();
		this.gpMaxNum = group.getGpMaxNum();
		this.gpCurNum = group.getGpCurNum();
		this.gpImg = group.getGpImg();

		this.gpTag = gpTag;
	}

}
