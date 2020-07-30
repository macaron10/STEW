package com.ssafy.study.group.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class GroupTagDto {

	@Getter
	@NoArgsConstructor
	@Valid
	public static class RegistGroupTag {
		private long gpTagNo;
		@NotBlank
		private String gpTagNm;

		public GroupTag toEntity() {
			return new GroupTag().builder().gpTagNo(gpTagNo).gpTagNm(gpTagNm).build();
		}
	}

	@Getter
	@NoArgsConstructor
	public static class ResGroupTag {
		private long gpTagNo;
		private String gpTagNm;

		public ResGroupTag(GroupTag tag) {
			this.gpTagNo = tag.getGpTagNo();
			this.gpTagNm = tag.getGpTagNm();
		}

	}
}
