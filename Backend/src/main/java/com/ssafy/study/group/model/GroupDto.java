package com.ssafy.study.group.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GroupDto {

	@Getter
	@Valid
	@NoArgsConstructor
	public class RegistGroup {
		@ApiModelProperty(required = true)
		@NotNull
		private int gpCatNo;// 타입 아이디
		@ApiModelProperty(required = true)
		@NotNull
		private String gpNm;// 스터디 이름
		@ApiModelProperty(required = true)
		@NotNull
		private String gpMgrId;// 팀장아이디
		private String gpIntro;// 소개
		private String gpTag;// 태그
		private int gpStTm;// 선호 시작시간
		private int gpEndTm;// 선호 종료시간

		@ApiModelProperty(required = true)
		@NotNull
		private boolean gpPublic;// 공개여부
		private String gpImg;// 썸네일

		public Group toEntity() {
			return Group.builder().gpCat(new GroupCategory(gpCatNo)).gpNm(gpNm).gpIntro(gpIntro).gpTag(gpTag)
					.gpStTm(gpStTm).gpEndTm(gpEndTm).gpPublic(gpPublic).gpImg(gpImg).build();
		}
	}

	@Getter
	@Valid
	@NoArgsConstructor
	public class ModifyGroup {
		private long gpNo; // 스터디 아이디

		@ApiModelProperty(required = true)
		@NotNull
		private int gpCatNo;// 타입 아이디
		@ApiModelProperty(required = true)
		@NotNull
		private String gpNm;// 스터디 이름
		private String gpIntro;// 소개
		private String gpTag;// 태그
		private int gpStTm;// 선호 시작시간
		private int gpEndTm;// 선호 종료시간

		@ApiModelProperty(required = true)
		@NotNull
		private boolean gpPublic;// 공개여부
		private String gpImg;// 썸네일

		public Group toEntity() {
			return Group.builder().gpNo(gpNo).gpCat(new GroupCategory(gpCatNo)).gpNm(gpNm).gpIntro(gpIntro).gpTag(gpTag)
					.gpStTm(gpStTm).gpEndTm(gpEndTm).gpPublic(gpPublic).gpImg(gpImg).build();
		}
	}

	@Getter
	public static class ResGroup {
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

		public ResGroup(Group group) {
			this.gpNo = group.getGpNo();
			this.gpCatNo = group.getGpCat().getGpCatNo();
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

}