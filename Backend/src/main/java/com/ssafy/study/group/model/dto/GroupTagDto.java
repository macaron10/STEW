package com.ssafy.study.group.model.dto;

import com.ssafy.study.group.model.entity.GroupTag;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupTagDto {
	private long gpTagNo;
	private String gpTagNm;

	public GroupTagDto(GroupTag tag) {
		this.gpTagNo = tag.getGpTagNo();
		this.gpTagNm = tag.getGpTagNm();
	}
}
