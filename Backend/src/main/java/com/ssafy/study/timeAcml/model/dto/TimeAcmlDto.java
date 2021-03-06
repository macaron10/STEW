package com.ssafy.study.timeAcml.model.dto;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import com.ssafy.study.group.model.dto.GroupDto;
import com.ssafy.study.group.model.entity.Group;
import com.ssafy.study.timeAcml.model.entity.TimeAcml;
import com.ssafy.study.user.model.User;
import com.ssafy.study.user.model.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeAcmlDto {

	private long tmAcmlId;
	private UserDto user;
	private GroupDto gp;
	private String tmAcmlDate;
	private String tmAcmlTime;

	private long tmAcmlTimeLong;

	public TimeAcmlDto(TimeAcml time) {
		this.tmAcmlId = time.getTmAcmlId();
		this.user = new UserDto(time.getUser());
		this.gp = new GroupDto(time.getGp());
		this.tmAcmlDate = time.getTmAcmlDate().toLocalDate().toString();

		this.tmAcmlTime = secendToString(time.getTmAcmlTime());
	}

	public TimeAcmlDto(long time) {
		tmAcmlTime = secendToString(time);
		tmAcmlTimeLong = time;
	}

	public TimeAcmlDto(long time, String date) {
		tmAcmlDate = date;
		tmAcmlTime = secendToString(time);
		tmAcmlTimeLong = time;
	}

	public TimeAcmlDto(long time, LocalDateTime date) {
		tmAcmlDate = date.toLocalDate().toString();
		tmAcmlTime = secendToString(time);
		tmAcmlTimeLong = time;
	}

	public TimeAcmlDto(long time, String date, User user) {
		this(time, date);
		this.user = new UserDto(user);
	}

	public TimeAcmlDto(long time, String date, Group group) {
		this(time, date);
		this.gp = new GroupDto(group);
	}

	public TimeAcmlDto(long time, LocalDateTime date, User user) {
		this(time, date);
		this.user = new UserDto(user);
	}

	public TimeAcmlDto(long time, LocalDateTime date, Group group) {
		this(time, date);
		this.gp = new GroupDto(group);
	}

	public String secendToString(long second) {
		return String.format("%02d:%02d:%02d", TimeUnit.SECONDS.toHours(second),
				TimeUnit.SECONDS.toMinutes(second) % 60, TimeUnit.SECONDS.toSeconds(second) % 60);
	}
}
