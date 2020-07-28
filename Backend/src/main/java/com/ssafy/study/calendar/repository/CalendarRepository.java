package com.ssafy.study.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.study.calendar.model.CalEvent;

public interface CalendarRepository extends JpaRepository<CalEvent, Long>, CalendarRepositoryCustom {
	
	CalEvent findBycNo(long cno);
}
