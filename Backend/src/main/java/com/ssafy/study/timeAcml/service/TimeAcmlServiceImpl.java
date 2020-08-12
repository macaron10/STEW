package com.ssafy.study.timeAcml.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.study.timeAcml.model.dto.TimeAcmlDto;
import com.ssafy.study.timeAcml.model.dto.addTimeAcmlDto;
import com.ssafy.study.timeAcml.model.entity.TimeAcml;
import com.ssafy.study.timeAcml.repository.TimeAcmlRepository;

@Service
public class TimeAcmlServiceImpl implements TimeAcmlService {

	@Autowired
	private TimeAcmlRepository timeRepo;

	@Override
	public void saveTimer(long userId, addTimeAcmlDto addTime) {
		String date = LocalDateTime.now().toLocalDate().toString();
		TimeAcml acml = timeRepo.selectByUserIdAndGpNoAndTmAcmlDate(userId, addTime.getGpNo(), date);
		if (acml == null) {
			acml = new TimeAcml(userId, addTime.getGpNo());
		}

		acml.update(addTime);
		timeRepo.save(acml);
		return;
	}

	@Override
	public List<TimeAcmlDto> selectUserTimerTotalDate(long userId, int year, int month) {
		return timeRepo.selectUserTimerTotalDate(userId, year, month);
	}

	@Override
	public List<TimeAcmlDto> selectGroupTimerTotalDateUser(long gpNo, int year, int month) {
		return timeRepo.selectGroupTimerTotalDateUser(gpNo, year, month);
	}

	@Override
	public List<TimeAcmlDto> selectUserTimerTotalMonth(long userId, int year) {
		return timeRepo.selectUserTimerTotalMonth(userId, year);
	}

	@Override
	public List<TimeAcmlDto> selectGroupTimerTotalDate(long gpNo, int year, int month) {
		return timeRepo.selectGroupTimerTotalDate(gpNo, year, month);
	}

	@Override
	public List<TimeAcmlDto> selectGroupTimerTotalMonth(long gpNo, int year) {
		return timeRepo.selectGroupTimerTotalMonth(gpNo, year);
	}

	@Override
	public List<TimeAcmlDto> selectGroupRankTimerTotalMonth(int year, int month) {
		return timeRepo.selectGroupRankTimerTotalMonth(year, month);
	}

	@Override
	public TimeAcmlDto selectUserTodayTmAcml(long userId) {
		return timeRepo.selectUserToadyTmAcml(userId);
	}

}
