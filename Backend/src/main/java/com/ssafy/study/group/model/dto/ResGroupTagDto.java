package com.ssafy.study.group.model.dto;

import com.ssafy.study.group.model.entity.GroupTag;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResGroupTagDto {
	private long gpTagNo;
	private String gpTagNm;

	public ResGroupTagDto(GroupTag tag) {
		this.gpTagNo = tag.getGpTagNo();
		this.gpTagNm = tag.getGpTagNm();
	}
}
