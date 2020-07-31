package com.ssafy.study.group.model.dto;

import com.ssafy.study.group.model.entity.GroupCategory;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GroupCategoryDto {
	private int gpCatNo;// 타입 아이디

	private String gpCatLg;// 대분류
	private String gpCatMd;// 중분류
	private String gpCatSm;// 소분류

	public GroupCategoryDto(GroupCategory category) {
		this.gpCatNo = category.getGpCatNo();
		this.gpCatLg = category.getGpCatLg();
		this.gpCatMd = category.getGpCatMd();
		this.gpCatSm = category.getGpCatSm();
	}
}
