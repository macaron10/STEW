package com.ssafy.study.calendar.service;

import java.util.List;

import com.ssafy.study.calendar.model.CalDto.ResCal;
import com.ssafy.study.calendar.model.CalEvent;

public interface CalendarService {

	CalEvent saveCalendar(CalEvent cal);
	
	CalEvent selectCalNo(long calNo);

	void deleteCalEvt(long no);

	List<ResCal> selectPersonalCalEvt(long userId);

	List<ResCal> selectGroupCalEvt(long userId);

}
