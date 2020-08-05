package com.ssafy.study.group.model.dto;

import com.ssafy.study.group.model.entity.GroupJoin;

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
public class GroupJoinDto {
	private long gpJoinNo;
	private long gpGpNo;
	private long userId;

	public GroupJoinDto(GroupJoin join) {
		this.gpJoinNo = join.getGpJoinNo();
		this.gpGpNo = join.getGp().getGpNo();
		this.userId = join.getUser().getUserId();
	}
}
