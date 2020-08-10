package com.ssafy.study.group.model.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Valid
public class GroupSearchDto {

	private List<String> gpNm;
	private int gpCatNo;
	private List<String> gpTag;

	public GroupSearchDto() {
		gpNm = new ArrayList<String>();
		gpTag = new ArrayList<String>();
		gpCatNo = -1;
	}

	public GroupSearchDto(String[] keyword) {
		this();
		Arrays.stream(keyword).forEach(e -> {
			if (e.startsWith("#"))
				gpTag.add(e.replace("#", ""));
			else
				gpNm.add(e);
		});
	}

	public GroupSearchDto(String[] keyword, int gpCatNo) {
		this(keyword);
		this.gpCatNo = gpCatNo;

	}

}
