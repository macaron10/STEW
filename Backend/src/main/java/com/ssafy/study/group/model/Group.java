package com.ssafy.study.group.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import com.ssafy.study.group.model.GroupDto.ResGroup;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "gp_tb")
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long gpNo; // 스터디 아이디

	@ManyToOne
	@JoinColumn(name = "gp_cat_no")
	private GroupCategory gpCat;
//	private int gpCatNo;// 타입 아이디

	private String gpNm;// 스터디 이름
	private String gpMgrId;// 팀장아이디
	private String gpIntro;// 소개
	private String gpTag;// 태그
	private int gpStTm;// 선호 시작시간
	private int gpEndTm;// 선호 종료시간
	private boolean gpPublic;// 공개여부
	private String gpImg;// 썸네일

	@Column(columnDefinition = "integer default 5")
	private int gpMaxNum;// 최대인원
	private int gpCurNum;// 현재인원

	@CreatedDate
	private LocalDateTime gpRegDate;// 등록일

	public void update(ResGroup group) {
		this.gpNo = group.getGpNo();
		this.setGpCat(new GroupCategory(group.getGpCatNo()));
		this.gpNm = group.getGpNm();
		this.gpMgrId = group.getGpMgrId();
		this.gpIntro = group.getGpIntro();
		this.gpTag = group.getGpTag();
		this.gpStTm = group.getGpStTm();
		this.gpEndTm = group.getGpEndTm();
		this.gpPublic = group.isGpPublic();
		this.gpImg = group.getGpImg();
		this.gpMaxNum = group.getGpMaxNum();
		this.gpCurNum = group.getGpCurNum();
	}

}
