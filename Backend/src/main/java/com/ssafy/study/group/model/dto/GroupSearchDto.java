package com.ssafy.study.group.model.dto;

import java.util.ArrayList;
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

	private String gpNm;
	private int gpStTm;
	private int gpEndTm;
	@NotNull
	private boolean gpPrivate;

	private int gpCatNo;

	private String gpTag;

	private String[] gpTagArr;

	public GroupSearchDto() {
		gpCatNo = 0;
		gpStTm = -1;
		gpEndTm = -1;
		gpPrivate = false;

		gpTagArr = null;
	}

	public void setGpTag(String tag) {
		gpTagArr = gpTag.split(",");
	}

}
