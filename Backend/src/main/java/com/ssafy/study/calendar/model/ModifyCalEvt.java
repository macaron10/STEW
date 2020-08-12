package com.ssafy.study.calendar.model;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Valid
public class ModifyCalEvt {
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
	
	@ApiModelProperty(required = true)
	@NotNull
	private String cColor;

	@ApiModelProperty(required = true)
	@NotNull
	private boolean useTime;

	public CalEvent toEntity() {
		return CalEvent.builder().cNo(cNo).cOwn(cOwn).cStTm(cStTm).cEndTm(cEndTm).cEvtNm(cEvtNm).cEvtDsc(cEvtDsc)
				.useTime(useTime).cColor(cColor).build();
	}
}
