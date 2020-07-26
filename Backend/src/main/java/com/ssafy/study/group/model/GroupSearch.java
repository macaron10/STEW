package com.ssafy.study.group.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class GroupSearch {

	private String gpNm;
	private int gpStTm;
	private int gpEndTm;
	private String gpTag;
	private boolean gpPrivate;

	private String gpCatLg;
	private String gpCatMd;
	private String gpCatSm;

	public GroupSearch() {
		gpStTm = -1;
		gpEndTm = -1;
		gpPrivate = false;
	}

}
