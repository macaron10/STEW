package com.ssafy.study.timeAcml.model.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Valid
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class addTimeAcmlDto {
	@ApiModelProperty(required = true)
	@NotNull
	private long gpNo;
	@ApiModelProperty(required = true)
	@NotNull
	private long tmAcmlTime;

}
