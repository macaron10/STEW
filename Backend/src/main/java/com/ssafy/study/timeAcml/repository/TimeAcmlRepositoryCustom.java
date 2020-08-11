package com.ssafy.study.timeAcml.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ssafy.study.timeAcml.model.dto.TimeAcmlDto;
import com.ssafy.study.timeAcml.model.entity.TimeAcml;

public interface TimeAcmlRepositoryCustom {

	TimeAcml selectByUserIdAndGpNoAndTmAcmlDate(long userId, long gpNo, String date);

	List<TimeAcmlDto> selectUserTimerTotalDate(long userId, int year, int month);

	List<TimeAcmlDto> selectUserTimerTotalMonth(long userId, int year);

	List<TimeAcmlDto> selectGroupTimerTotalDate(long gpNo, int year, int month);

	List<TimeAcmlDto> selectGroupTimerTotalMonth(long gpNo, int year);

	List<TimeAcmlDto> selectGroupTimerTotalDateUser(long gpNo, int year, int month);

	List<TimeAcmlDto> selectGroupRankTimerTotalMonth(int year, int month);

	TimeAcmlDto selectUserToadyTmAcml(long userId);

}
