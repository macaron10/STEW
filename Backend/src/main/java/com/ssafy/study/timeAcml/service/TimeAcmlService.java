package com.ssafy.study.timeAcml.service;

import java.util.List;

import com.ssafy.study.timeAcml.model.dto.TimeAcmlDto;
import com.ssafy.study.timeAcml.model.dto.addTimeAcmlDto;

public interface TimeAcmlService {

	void saveTimer(long userId, addTimeAcmlDto addTime);

	List<TimeAcmlDto> selectUserTimerTotalDate(long userId, int year, int month);

	List<TimeAcmlDto> selectUserTimerTotalMonth(long userId, int year);

	List<TimeAcmlDto> selectGroupTimerTotalDate(long gpNo, int year, int month);
	
	List<TimeAcmlDto> selectGroupTimerTotalDateUser(long gpNo, int year, int month);

	List<TimeAcmlDto> selectGroupTimerTotalMonth(long gpNo, int year);

	List<TimeAcmlDto> selectGroupRankTimerTotalMonth(int year, int month);

	TimeAcmlDto selectUserTodayTmAcml(long userId);

}
