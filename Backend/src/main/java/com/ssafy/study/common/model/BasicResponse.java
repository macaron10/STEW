package com.ssafy.study.common.model;

import io.swagger.annotations.ApiModelProperty;

public class BasicResponse {
	@ApiModelProperty(value = "status", position = 1)
	public boolean status;
	@ApiModelProperty(value = "msg", position = 2)
	public String msg;
	@ApiModelProperty(value = "object", position = 3)
	public Object object;
}
