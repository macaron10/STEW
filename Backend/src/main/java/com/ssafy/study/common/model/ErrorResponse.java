package com.ssafy.study.common.model;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;

public class ErrorResponse {
	@ApiModelProperty(value = "timestamp", position = 1)
	private LocalDateTime timestamp;
	@ApiModelProperty(value = "status", position = 2)
	public boolean status;
	@ApiModelProperty(value = "error", position = 3)
	public String error;
	@ApiModelProperty(value = "msg", position = 4)
	public String msg;

	public ErrorResponse() {
		timestamp = LocalDateTime.now();
	}
}
