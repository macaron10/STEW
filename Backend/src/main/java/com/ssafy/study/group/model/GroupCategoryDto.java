package com.ssafy.study.group.model;

import lombok.Getter;
import lombok.ToString;

public class GroupCategoryDto {

	@Getter
	@ToString
	public static class ResGroupCategoryDto {
		private int gpCatNo;// 타입 아이디

		private String gpCatLg;// 대분류
		private String gpCatMd;// 중분류
		private String gpCatSm;// 소분류

		public ResGroupCategoryDto(GroupCategory category) {
			this.gpCatNo = category.getGpCatNo();
			this.gpCatLg = category.getGpCatLg();
			this.gpCatMd = category.getGpCatMd();
			this.gpCatLg = category.getGpCatSm();
		}

	}

}
