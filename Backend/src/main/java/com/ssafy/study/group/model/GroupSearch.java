package com.ssafy.study.group.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupSearch {

	private String gpNm;
	private int gpStTm;
	private int gpEndTm;
	private String gpTag;
	private boolean gpPublic;

	private String gpCatLg;
	private String gpCatMd;
	private String gpCatSm;

}
