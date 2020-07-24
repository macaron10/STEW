package com.ssafy.study.group.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ssafy.study.group.model.Group;

@Repository
public class GroupRepositoryImpl /* extends QuerydslRepositorySupport */ implements GroupRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Group> findMyJoinGroup(long userId) {
		String jpql = "select g from Group g where g.gpNo in (select gj.gpNo from GroupJoin gj where gj.userId = :userId)";
		TypedQuery<Group> query = em.createQuery(jpql, Group.class);
		query.setParameter("userId", userId);

		return query.getResultList();
	}

}
