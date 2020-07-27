package com.ssafy.study.calendar.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.study.calendar.model.CalDto.ResCal;
import com.ssafy.study.calendar.model.CalEvent;
import com.ssafy.study.calendar.repository.CalendarRepository;

@Service
public class CalendarServiceImpl implements CalendarService {
	@Autowired
	private CalendarRepository calRepo;

	@Override
	public CalEvent saveCalendar(CalEvent cal) {
		cal = calRepo.save(cal);
		return cal;
	}

	@Override
	public CalEvent selectCalNo(long calNo) {
		return calRepo.findBycNo(calNo);
	}

	@Override
	public void deleteCalEvt(long no) {
		calRepo.deleteById(no);
	}

	@Override
	public List<ResCal> selectPersonalCalEvt(long userId) {
		return calRepo.findPersonalCalEvt(userId).stream().map(c -> new ResCal(c)).collect(Collectors.toList());
	}

	@Override
	public List<ResCal> selectGroupCalEvt(long userId) {
		return calRepo.findGroupCalEvt(userId).stream().map(c -> new ResCal(c)).collect(Collectors.toList());
	}
}
