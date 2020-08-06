package com.ssafy.study.group.model.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
	private String gpCatNm;

	private String gpNm;// 스터디 이름
	private long gpMgrId;// 팀장아이디
	private String gpIntro;// 소개
	private int gpStTm;// 선호 시작시간
	private int gpEndTm;// 선호 종료시간
	private boolean gpPublic;// 공개여부
	private String gpImg;
	private int gpMaxNum;// 최대인원
	private int gpCurNum;// 현재인원

	private List<String> gpTag;
	private LocalDateTime regDate;

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

		this.regDate = group.getRegDate();

		if (group.getGpTag() != null)
			this.gpTag = Arrays.asList(group.getGpTag().split(","));
	}

	public GroupDto(Group group, String gpCatNm) {
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
		
		this.regDate = group.getRegDate();

		if (group.getGpTag() != null)
			this.gpTag = Arrays.asList(group.getGpTag().split(","));

		this.gpCatNm = gpCatNm;
	}

}
