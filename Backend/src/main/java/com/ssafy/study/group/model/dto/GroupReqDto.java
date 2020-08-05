package com.ssafy.study.group.model.dto;

import com.ssafy.study.group.model.entity.GroupReq;
import com.ssafy.study.user.model.UserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GroupReqDto {
	private long gpReqNo;
	private GroupDto gp;
	private UserDto user;

	public GroupReqDto(GroupReq req) {
		this.gpReqNo = req.getGpReqNo();
		this.gp = new GroupDto(req.getGp());
		this.user = new UserDto(req.getUser());
	}

}
