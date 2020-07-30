package com.ssafy.study.group.model.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.ssafy.study.group.model.entity.GroupTag;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Valid
@ToString
public class RegistGroupTagDto {

	private long gpTagNo;
	@NotBlank
	private String gpTagNm;

	public GroupTag toEntity() {
		return GroupTag.builder().gpTagNo(gpTagNo).gpTagNm(gpTagNm).build();
	}
}
