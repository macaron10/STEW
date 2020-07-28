package com.ssafy.study.calendar.model;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class CalEvtDto {

	@Data
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
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
		private LocalDateTime cStTm;
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
		private LocalDateTime cEndTm;
		@ApiModelProperty(required = true)
		@NotNull
		private String cEvtNm;
		private String cEvtDsc;

		public CalEvent toEntity() {
			return CalEvent.builder().cType(cType).cOwn(cOwn).cStTm(cStTm).cEndTm(cEndTm).cEvtNm(cEvtNm)
					.cEvtDsc(cEvtDsc).build();
		}
	}

	@Data
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
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
		private LocalDateTime cStTm;
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
		private LocalDateTime cEndTm;
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
		private LocalDateTime cStTm;
		private LocalDateTime cEndTm;
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
