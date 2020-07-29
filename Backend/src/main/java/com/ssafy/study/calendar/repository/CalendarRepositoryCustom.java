package com.ssafy.study.calendar.repository;

import java.util.List;

import com.ssafy.study.calendar.model.CalEvent;

public interface CalendarRepositoryCustom {

	List<CalEvent> findPersonalCalEvt(long userId);

	List<CalEvent> findGroupCalEvt(long userId);

	List<CalEvent> findPersonalCalEvt(long userId, int year, int month);
	
	List<CalEvent> findGroupCalEvt(long userId, int year, int month);

}
