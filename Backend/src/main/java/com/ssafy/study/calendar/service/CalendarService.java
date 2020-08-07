package com.ssafy.study.calendar.service;

import java.util.List;

import com.ssafy.study.calendar.model.CalEvent;
import com.ssafy.study.calendar.model.ResCalEvt;

public interface CalendarService {

	CalEvent saveCalendar(CalEvent cal);

	CalEvent selectCalNo(long calNo);

	void deleteCalEvt(long no);

	List<ResCalEvt> selectPersonalCalEvt(long userId);

	List<ResCalEvt> selectGroupCalEvt(long userId);

	List<ResCalEvt> selectPersonalCalEvt(long userId, int year, int month);

	List<ResCalEvt> selectGroupCalEvt(long userId, int year, int month);

	List<ResCalEvt> selectGroupCalEvtByGpNo(long gpNo, int year, int month);

}
