package com.ssafy.study.common.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Attachments {
	private List<Attachment> attachments;

	@Builder
	public Attachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
}