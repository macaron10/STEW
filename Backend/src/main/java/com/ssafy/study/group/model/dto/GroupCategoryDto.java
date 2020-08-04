package com.ssafy.study.group.model.dto;

import com.ssafy.study.group.model.entity.GroupCategory;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GroupCategoryDto {
	private int gpCatNo;// 타입 아이디

	private String gpCatNm;

	public GroupCategoryDto(GroupCategory category) {
		this.gpCatNo = category.getGpCatNo();
		this.gpCatNm = category.getGpCatNm();
	}
}
