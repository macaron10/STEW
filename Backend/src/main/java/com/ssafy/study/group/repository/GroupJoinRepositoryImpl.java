package com.ssafy.study.group.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.ssafy.study.group.model.dto.GroupJoinDto;
import com.ssafy.study.group.model.entity.GroupJoin;

public class GroupJoinRepositoryImpl implements GroupJoinRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean ckGroupJoin(long gpNo, long userId) {
		String jpql = "select count(gj) from GroupJoin gj where gj.gp.gpNo = :gpNo and gj.user.userId = :userId";
		TypedQuery<Long> query = em.createQuery(jpql, Long.class);

		query.setParameter("gpNo", gpNo);
		query.setParameter("userId", userId);

		return query.getSingleResult() > 0 ? true : false;
	}

	@Override
	public List<GroupJoinDto> findGpJoinMemeber(long gpNo) {
		String jpql = "select new com.ssafy.study.group.model.dto.GroupJoinDto(gj, u) "
				+ "from GroupJoin gj, User u "
				+ "where gj.gp.gpNo = :gpNo and gj.user.userId = u.userId";
		TypedQuery<GroupJoinDto> query = em.createQuery(jpql, GroupJoinDto.class);

		query.setParameter("gpNo", gpNo);

		return query.getResultList();
	}

//	@Override
//	public void deleteByGpNoAndUserId(long gpNo, long userId) {
//		String jpql = "delete from GroupJoin gj where gj.gp.gpNo = :gpNo and gj.user.userId = :userId";
//		Query query = em.createQuery(jpql);
//
//		query.setParameter("gpNo", gpNo);
//		query.setParameter("userId", userId);
//
//		query.getSingleResult();
//	}

//	@Override
//	public void deleteByGpNo(long gpNo) {
//		String jpql = "delete from GroupJoin gj where gj.gp.gpNo = :gpNo";
//		Query query = em.createQuery(jpql);
//
//		query.setParameter("gpNo", gpNo);
//		query.getSingleResult();
//	}

}
