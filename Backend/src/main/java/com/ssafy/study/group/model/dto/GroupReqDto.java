package com.ssafy.study.group.model.dto;

import com.ssafy.study.group.model.entity.GroupReq;

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
	private long gpGpNo;
	private long userId;

	public GroupReqDto(GroupReq req) {
		this.gpReqNo = req.getGpReqNo();
		this.gpGpNo = req.getGp().getGpNo();
		this.userId = req.getUser().getUserId();
	}

}
