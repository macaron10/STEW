package com.ssafy.study.common.model;

import java.io.PrintWriter;
import java.io.StringWriter;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Props {
	private String card;
	
	public Props(Exception e, String uri, String params) {
		StringBuilder text = new StringBuilder();

		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		text.append("**Stack Trace**").append("\n").append('\n').append("```");
		text.append(sw.toString().substring(0, 6000) + "...").append('\n').append('\n');

		this.card = text.toString();
	}
}
