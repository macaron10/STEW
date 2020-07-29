package com.ssafy.study.calendar.model;

import java.sql.Timestamp;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class CalEvtDto {

	@Getter
	@ToString
	@NoArgsConstructor
	@Valid
	public static class CreateCalEvt {
		@ApiModelProperty(required = true)
		@NotNull
		private char cType;
		@ApiModelProperty(required = true)
		@NotNull
		@Setter
		private long cOwn;
		@ApiModelProperty(required = true)
		@NotNull
		private Timestamp cStTm;
		private Timestamp cEndTm;
		@ApiModelProperty(required = true)
		@NotNull
		private String cEvtNm;
		private String cEvtDsc;

		public CalEvent toEntity() {
			return CalEvent.builder().cType(cType).cOwn(cOwn).cStTm(cStTm).cEndTm(cEndTm).cEvtNm(cEvtNm)
					.cEvtDsc(cEvtDsc).build();
		}
	}

	@Getter
	@ToString
	@NoArgsConstructor
	@Valid
	public static class ModifyCalEvt {
		@ApiModelProperty(required = true)
		@NotNull
		private long cNo;
		@ApiModelProperty(required = true)
		@NotNull
		private long cOwn;
		@ApiModelProperty(required = true)
		@NotNull
		private Timestamp cStTm;
		private Timestamp cEndTm;
		@ApiModelProperty(required = true)
		@NotNull
		private String cEvtNm;
		private String cEvtDsc;

		public CalEvent toEntity() {
			return CalEvent.builder().cNo(cNo).cOwn(cOwn).cStTm(cStTm).cEndTm(cEndTm).cEvtNm(cEvtNm).cEvtDsc(cEvtDsc)
					.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	public static class ResCalEvt {
		private long cNo;
		private char cType;
		private long cOwn;
		private Timestamp cStTm;
		private Timestamp cEndTm;
		private String cEvtNm;
		private String cEvtDsc;

		public ResCalEvt(CalEvent c) {
			this.cNo = c.getCNo();
			this.cType = c.getCType();
			this.cOwn = c.getCOwn();
			this.cStTm = c.getCStTm();
			this.cEndTm = c.getCEndTm();
			this.cEvtNm = c.getCEvtNm();
			this.cEvtDsc = c.getCEvtDsc();
		}
	}
}
