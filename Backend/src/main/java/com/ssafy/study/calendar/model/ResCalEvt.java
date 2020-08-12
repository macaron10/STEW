package com.ssafy.study.calendar.model;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResCalEvt {
	private long cNo;
	private char cType;
	private long cOwn;
	private LocalDateTime cStTm;
	private LocalDateTime cEndTm;
	private String cEvtNm;
	private String cEvtDsc;

	private String cColor;
	private boolean useTime;

	public ResCalEvt(CalEvent c) {
		this.cNo = c.getCNo();
		this.cType = c.getCType();
		this.cOwn = c.getCOwn();
		this.cStTm = c.getCStTm();
		this.cEndTm = c.getCEndTm();
		this.cEvtNm = c.getCEvtNm();
		this.cEvtDsc = c.getCEvtDsc();

		this.useTime = c.isUseTime();
		this.cColor = c.getCColor();
	}
}
