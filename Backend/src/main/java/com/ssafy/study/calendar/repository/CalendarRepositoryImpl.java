package com.ssafy.study.calendar.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ssafy.study.calendar.model.CalEvent;
import com.ssafy.study.calendar.model.ResCalEvt;

@Repository
public class CalendarRepositoryImpl implements CalendarRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<CalEvent> findPersonalCalEvt(long userId) {
		String jpql = "select cal from CalEvent cal where cal.cType = 'U' and cal.cOwn = :userId";

		TypedQuery<CalEvent> query = em.createQuery(jpql, CalEvent.class);
		query.setParameter("userId", userId);

		return query.getResultList();
	}

	@Override
	public List<CalEvent> findGroupCalEvt(long userId) {
		String jpql = "select cal from CalEvent cal where cal.cType = 'G' and cal.cOwn in (select gj.gp.gpNo from GroupJoin gj where gj.user.userId = :userId)";

		TypedQuery<CalEvent> query = em.createQuery(jpql, CalEvent.class);
		query.setParameter("userId", userId);
		return query.getResultList();
	}

	@Override
	public List<CalEvent> findPersonalCalEvt(long userId, int year, int month) {
		String dateForm = String.format("%d-%d-01", year, month);
		String jpql = "select cal from CalEvent cal where cal.cType = 'U' and cal.cOwn = :userId"
				+ " and (DATE_FORMAT(cal.cStTm,'%Y-%m') <= DATE_FORMAT(:date, '%Y-%m') and DATE_FORMAT(cal.cEndTm,'%Y-%m') >= DATE_FORMAT(:date, '%Y-%m'))";

		TypedQuery<CalEvent> query = em.createQuery(jpql, CalEvent.class);
		query.setParameter("userId", userId);
		query.setParameter("date", dateForm);

		return query.getResultList();
	}

	@Override
	public List<CalEvent> findGroupCalEvt(long userId, int year, int month) {
		String dateForm = String.format("%d-%d-01", year, month);
		String jpql = "select cal from CalEvent cal where cal.cType = 'G' and cal.cOwn in (select gj.gp.gpNo from GroupJoin gj where gj.user.userId = :userId)"
				+ " and (DATE_FORMAT(cal.cStTm,'%Y-%m') <= DATE_FORMAT(:date, '%Y-%m') and DATE_FORMAT(cal.cEndTm,'%Y-%m') >= DATE_FORMAT(:date, '%Y-%m'))";

		TypedQuery<CalEvent> query = em.createQuery(jpql, CalEvent.class);
		query.setParameter("userId", userId);
		query.setParameter("date", dateForm);

		return query.getResultList();
	}

	@Override
	public List<ResCalEvt> findGroupCalEvtByGpNo(long gpNo, int year, int month) {
		String dateForm = String.format("%d-%d-01", year, month);
		String jpql = "select new com.ssafy.study.calendar.model.ResCalEvt(cal) from CalEvent cal where cal.cType = 'G' and cal.cOwn = :gpNo"
				+ " and (DATE_FORMAT(cal.cStTm,'%Y-%m') <= DATE_FORMAT(:date, '%Y-%m') and DATE_FORMAT(cal.cEndTm,'%Y-%m') >= DATE_FORMAT(:date, '%Y-%m'))";

		TypedQuery<ResCalEvt> query = em.createQuery(jpql, ResCalEvt.class);
		query.setParameter("gpNo", gpNo);
		query.setParameter("date", dateForm);

		return query.getResultList();
	}

}
