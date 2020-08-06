package com.ssafy.study.group.model.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.ssafy.study.group.model.entity.Group;
import com.ssafy.study.group.model.entity.GroupReq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Valid
public class RequestGroupJoinDto {
	@ApiModelProperty(required = true)
	@NotNull
	private long gpNo;
	private String reqMsg;

	public GroupReq toEntity() {
		return GroupReq.builder().gp(new Group(gpNo)).gpReqMeg(reqMsg).build();
	}
}
