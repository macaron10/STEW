package com.ssafy.study.group.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

}
