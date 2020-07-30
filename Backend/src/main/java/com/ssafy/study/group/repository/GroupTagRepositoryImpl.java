package com.ssafy.study.group.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class GroupTagRepositoryImpl implements GroupTagRepositoryCustom {
	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean checkGroupTagExist(String tagNm) {
		String jpql = "select count(gt) from GroupTag gt where gt.gpTagNm = :tagNm";
		TypedQuery<Long> query = em.createQuery(jpql, Long.class);

		query.setParameter("tagNm", tagNm);

		return query.getSingleResult() >= 1 ? true : false;
	}

}
