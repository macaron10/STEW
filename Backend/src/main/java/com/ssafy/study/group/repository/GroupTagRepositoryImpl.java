package com.ssafy.study.group.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.ssafy.study.group.model.entity.GroupTag;

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

	@Override
	public List<GroupTag> selectGroupTagList(long gpNo) {
		String jpql = "select gt from GroupTag gt where gt.gpTagNo in (select gm.gpTag.gpTagNo from GroupTagMap gm where gm.gp.gpNo = :gpNo)";
		TypedQuery<GroupTag> query = em.createQuery(jpql, GroupTag.class);

		query.setParameter("gpNo", gpNo);

		return query.getResultList();
	}

}
