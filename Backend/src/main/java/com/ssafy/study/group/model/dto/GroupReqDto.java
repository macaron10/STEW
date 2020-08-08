package com.ssafy.study.group.model.dto;

import java.time.LocalDateTime;

import com.ssafy.study.group.model.entity.Group;
import com.ssafy.study.group.model.entity.GroupReq;
import com.ssafy.study.user.model.User;
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

	private String gpReqMsg;

	private LocalDateTime regDate;

	public GroupReqDto(GroupReq req) {
		this.gpReqNo = req.getGpReqNo();
		this.gp = new GroupDto(req.getGp());
		this.user = new UserDto(req.getUser());

		this.gpReqMsg = req.getGpReqMeg();
		this.regDate = req.getRegDate();
	}

	public GroupReqDto(GroupReq req, Group group, User u) {
		this.gpReqNo = req.getGpReqNo();
		this.gp = new GroupDto(group);
		this.user = new UserDto(u);

		this.gpReqMsg = req.getGpReqMeg();
		this.regDate = req.getRegDate();
	}
}
