package com.ssafy.study.timeAcml.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;

import com.ssafy.study.timeAcml.model.dto.TimeAcmlDto;
import com.ssafy.study.timeAcml.model.entity.TimeAcml;

@Service
public class TimeAcmlRepositoryImpl implements TimeAcmlRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public TimeAcml selectByUserIdAndGpNoAndTmAcmlDate(long userId, long gpNo, String date) {
		String jpql = "select t from TimeAcml t where t.user.userId = :userId and t.gp.gpNo = :gpNo and t.tmAcmlDate = '"
				+ date + "'";
		TypedQuery<TimeAcml> query = em.createQuery(jpql, TimeAcml.class);

		query.setParameter("userId", userId);
		query.setParameter("gpNo", gpNo);

		List<TimeAcml> list = query.getResultList();
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public List<TimeAcmlDto> selectUserTimerTotalDate(long userId, int year, int month) {
		String jpql = "select new com.ssafy.study.timeAcml.model.dto.TimeAcmlDto(sum(t.tmAcmlTime) as total, substring(t.tmAcmlDate, 1, 7)) "
				+ "from TimeAcml t " + "where t.user.userId = :userId and year(t.tmAcmlDate) = :year "
				+ "and month(t.tmAcmlDate) = :month group by 2";

		TypedQuery<TimeAcmlDto> query = em.createQuery(jpql, TimeAcmlDto.class);
		query.setParameter("userId", userId);
		query.setParameter("year", year);
		query.setParameter("month", month);

		return query.getResultList();
	}

	@Override
	public List<TimeAcmlDto> selectUserTimerTotalMonth(long userId, int year) {
		String jpql = "select new com.ssafy.study.timeAcml.model.dto.TimeAcmlDto(sum(t.tmAcmlTime) as total, substring(t.tmAcmlDate, 1, 4)) "
				+ "from TimeAcml t " + "where t.user.userId = :userId and year(t.tmAcmlDate) = :year group by 2";

		TypedQuery<TimeAcmlDto> query = em.createQuery(jpql, TimeAcmlDto.class);
		query.setParameter("userId", userId);
		query.setParameter("year", year);

		return query.getResultList();
	}

	@Override
	public List<TimeAcmlDto> selectGroupTimerTotalDate(long gpNo, int year, int month) {
		String jpql = "select new com.ssafy.study.timeAcml.model.dto.TimeAcmlDto(sum(t.tmAcmlTime) as total, substring(t.tmAcmlDate, 1, 7)) "
				+ "from TimeAcml t " + "where t.gp.gpNo = :gpNo and year(t.tmAcmlDate) = :year "
				+ "and month(t.tmAcmlDate) = :month group by 2";

		TypedQuery<TimeAcmlDto> query = em.createQuery(jpql, TimeAcmlDto.class);
		query.setParameter("gpNo", gpNo);
		query.setParameter("year", year);
		query.setParameter("month", month);

		return query.getResultList();
	}

	@Override
	public List<TimeAcmlDto> selectGroupTimerTotalDateUser(long gpNo, int year, int month) {
		String jpql = "select new com.ssafy.study.timeAcml.model.dto.TimeAcmlDto(sum(t.tmAcmlTime) as total, substring(t.tmAcmlDate, 1, 7), u) "
				+ "from TimeAcml t join User u on t.user.userId = u.userId "
				+ "where t.gp.gpNo = :gpNo and year(t.tmAcmlDate) = :year "
				+ "and month(t.tmAcmlDate) = :month group by 2, 3 order by 1 desc";

		TypedQuery<TimeAcmlDto> query = em.createQuery(jpql, TimeAcmlDto.class);
		query.setParameter("gpNo", gpNo);
		query.setParameter("year", year);
		query.setParameter("month", month);

		return query.getResultList();
	}

	@Override
	public List<TimeAcmlDto> selectGroupTimerTotalMonth(long gpNo, int year) {
		String jpql = "select new com.ssafy.study.timeAcml.model.dto.TimeAcmlDto(sum(t.tmAcmlTime) as total, substring(t.tmAcmlDate, 1, 4)) "
				+ "from TimeAcml t " + "where t.gp.gpNo = :gpNo and year(t.tmAcmlDate) = :year group by 2";

		TypedQuery<TimeAcmlDto> query = em.createQuery(jpql, TimeAcmlDto.class);
		query.setParameter("gpNo", gpNo);
		query.setParameter("year", year);

		return query.getResultList();
	}

	@Override
	public List<TimeAcmlDto> selectGroupRankTimerTotalMonth(int year, int month) {
		String jpql = "select new com.ssafy.study.timeAcml.model.dto.TimeAcmlDto(sum(t.tmAcmlTime) as total, substring(t.tmAcmlDate, 1, 7), gp) "
				+ "from TimeAcml t join Group gp on t.gp.gpNo = gp.gpNo "
				+ "where year(t.tmAcmlDate) = :year "
				+ "and month(t.tmAcmlDate) = :month group by 2, 3 order by 1 desc";

		TypedQuery<TimeAcmlDto> query = em.createQuery(jpql, TimeAcmlDto.class);
		query.setParameter("year", year);
		query.setParameter("month", month);

		return query.getResultList();
	}

}
