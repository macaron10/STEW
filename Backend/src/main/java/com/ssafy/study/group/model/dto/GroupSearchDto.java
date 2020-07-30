package com.ssafy.study.group.model.dto;

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

	private String gpCatLg;
	private String gpCatMd;
	private String gpCatSm;

	private List<String> gpTagList;

	public GroupSearchDto() {
		gpStTm = -1;
		gpEndTm = -1;
		gpPrivate = false;
	}

}
