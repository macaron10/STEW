package com.ssafy.study.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Attachments {
	private Props props;
	private List<Attachment> attachments;

	@Builder
	public Attachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Attachments(Exception e, String uri, String params) {
		attachments = new ArrayList();
		Attachment attach = new Attachment(e, uri, params);
		attachments.add(attach);

		props = new Props(e, uri, params);
	}
}