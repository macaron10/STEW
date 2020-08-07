package com.ssafy.study.calendar.model;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Valid
public class CreateCalEvt {
	@ApiModelProperty(example = "U||G")
	@NotNull
	private char cType;
	@ApiModelProperty(required = true, example = "userId || groupId")
	@NotNull
	@Setter
	private long cOwn;
	@ApiModelProperty(required = true, example = "2020-07-29T06:26")
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime cStTm;
	@ApiModelProperty(example = "2020-07-29T06:26")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime cEndTm;
	@ApiModelProperty(required = true)
	@NotNull
	private String cEvtNm;
	private String cEvtDsc;

	@ApiModelProperty(required = true)
	@NotNull
	private boolean useTime;

	public CalEvent toEntity() {
		return CalEvent.builder().cType(cType).cOwn(cOwn).cStTm(cStTm).cEndTm(cEndTm).cEvtNm(cEvtNm).cEvtDsc(cEvtDsc)
				.useTime(useTime).build();
	}
}