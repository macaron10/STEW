package com.ssafy.study.group.model.dto;

import java.time.LocalDateTime;

import com.ssafy.study.group.model.entity.GroupJoin;
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
public class GroupJoinDto {
	private long gpJoinNo;
	private long gpGpNo;

	private UserDto user;
	private long userId;

	private LocalDateTime regDate;

	public GroupJoinDto(GroupJoin join) {
		this.gpJoinNo = join.getGpJoinNo();
		this.gpGpNo = join.getGp().getGpNo();

		this.userId = join.getUser().getUserId();

		this.regDate = join.getRegDate();
	}

	public GroupJoinDto(GroupJoin join, User u) {
		this.gpJoinNo = join.getGpJoinNo();
		this.gpGpNo = join.getGp().getGpNo();

		this.user = new UserDto(u);
		this.userId = user.getUserId();
		
		this.regDate = join.getRegDate();
	}
}
